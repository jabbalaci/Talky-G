package fr.loria.coronsys.coron.helper;

import java.text.NumberFormat;
import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.Result;

/**
 * Gets different kind of information about the database, support, runtime, etc.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Statistics
{
   /**
    * Empty private constructor. The class cannot be instantiated.
    */
   private Statistics() {
   }
   
   /**
    * Counts only the time of real calculation and prints the elapsed time broken down to hour, minute, sec, millisec.
    * 
    * @param time The elapsed time of calculation in millisec.
    */
   public static void printRuntime(long time) 
   {
      long hour, min, sec, msec = time%1000;
      time /= 1000;     // chop msec, becomes sec
      sec = time%60;
      time /= 60;       // chop sec, becomes minute
      min = time%60;
      hour = time/60;   // chop minute, becomes hour
      System.err.println("> Elapsed time of (just) the calculation: "+hour+":"+min+":"+sec+"."+msec);
   }
   
   /**
    * Converts a time period to sec and msec.
    * Ex.: input: 2025 => "2.025", where 2 is sec, .025 is msec.
    * 
    * @param time Time is milliseconds.
    * @return Time formatted as sec and msec.
    */
   public static String getRuntime(long time) {
      return (String.format("%d.%03d", time / 1000, time % 1000));
   }

   /**
    * Prints a little statistics about the database, number of attributes (all, in average per line), minsupport (as integer and in percent), etc.
    * It allows us to imagine more easily with what kind of data we are working with.
    */
   public static void printDatabaseStatistics()
   {
      Double min_conf 	= Database.getMinConf();
      boolean stop		= Global.getExtra().get(C.X_STOP_STAT);
      //
      int min_supp = Database.getMinSupp();
      int lines    = Database.getNumberOfObjects();
      double min_supp_percent = (double)min_supp / (double)lines;
      Database.setMinSuppPercent(new Double(min_supp_percent));
      
      NumberFormat fmt_average 	= NumberFormat.getInstance();
         fmt_average.setMinimumFractionDigits(2);
         fmt_average.setMaximumFractionDigits(2);
      NumberFormat fmt_percent 	= NumberFormat.getPercentInstance();
         fmt_percent.setMinimumFractionDigits(2);
         fmt_percent.setMaximumFractionDigits(2);
      NumberFormat conf_percent 	= NumberFormat.getPercentInstance();
         conf_percent.setMinimumFractionDigits(3);
         conf_percent.setMaximumFractionDigits(3);
      StringBuilder info = new StringBuilder();
      
      // density
//      int contextSize      = lines * Database.getTotalNumberOfAttr();
//      double occupiedSlots = lines * Database.getNumberOfAttrInAvg();
//      double density = occupiedSlots / contextSize;
      double density = Database.getNumberOfAttrInAvg() / (double) Database.getTotalNumberOfAttr();

      info.append("# Database file name:              " + Database.getDatabaseAbsoluteFilePath()).append("\n");
      //System.out.println("# Database file name:              " + Database.getDatabaseFilePath());
      info.append(("# Database file size:              " + Convert.byteToPrettyString(Database.getDatabaseFileSize())+" bytes")).append("\n");
      //System.out.println("# Database file size:              " + Convert.byteToPrettyString(Database.getDatabaseFileSize())+" bytes");
      info.append("# Number of lines:                 " + Convert.byteToPrettyString(lines)).append("\n");
      //System.out.println("# Number of lines:                 " + Convert.byteToPrettyString(lines));
      info.append("# Total number of attributes:      " + Convert.byteToPrettyString(Database.getTotalNumberOfAttr())).append("\n");
      //System.out.println("# Largest attribute:               " + Convert.byteToPrettyString(Database.getLargestAttr()));
      info.append("# Number of non-empty attributes:  " + Convert.byteToPrettyString(Database.getTotalNbOfNonEmptyAttr())).append("\n");
      //System.out.println("# Number of attributes:            " + Convert.byteToPrettyString(Database.getNumberOfAttr()));
      info.append("# Number of attributes in average: " + (fmt_average.format(Database.getNumberOfAttrInAvg()))).append("\n");
      //System.out.println("# Number of attributes in average: " + (fmt_average.format(Database.getNumberOfAttrInAvg())));
      info.append("# Density:                         " + (fmt_percent.format(density))).append("\n");
      info.append("# min_supp:                        " + min_supp + ", i.e. " + (fmt_percent.format(min_supp_percent))).append("\n");
      //System.out.println("# min_supp:                        " + min_supp + ", i.e. " + (fmt_percent.format(min_supp_percent)));
      if (min_conf != null) { // for AssRuleX
         info.append("# min_conf:                        " + conf_percent.format(min_conf)).append("\n");
         //System.out.println("# min_conf:                        " + conf_percent.format(min_conf));
      }
      info.append("# Chosen algorithm:                "+Global.getAlgorithmStr()).append("\n");
      if (Global.getRule() != C.UNDEFINED) { // for AssRuleX
         info.append("# Rules to extract:                "+Global.getRuleStr()).append("\n");
      }
      
      System.out.println(info);
      //System.err.println(info.toString());
      //
      if (Global.getExtra().get(C.X_STOP_STAT)) {
         AnalyzeDB analyzeDB = new AnalyzeDB();
         analyzeDB.start();
      }
      if (stop) System.exit(0);      
   }

   /**
    * At the end of the program it prints how many FCIs were found.
    * 
    * @param result The result object that contains the final result (list of FCIs).
    * @param extra Global extra options (were given as command line options).
    */
   public static void printResultStatistics(Result result, BitSet extra) 
   {
   	  String text = "# Number of found frequent" +
	                ((extra.get(C.X_FI)==false)?" closed":"") +
					" itemsets: ";
   	  System.out.println(text + result.getRowNumber());
   	  System.out.println("# Number of attributes in the largest itemset: " + result.getMaxClosureSize());
   }
}
