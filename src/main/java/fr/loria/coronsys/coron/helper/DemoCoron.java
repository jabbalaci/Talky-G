package fr.loria.coronsys.coron.helper;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * Check different things if the software runs in demo mode.
 * There are two constraints possible:
 * 
 * 1) demo mode
 *      in this mode you must specify which algorithms are enabled
 *      
 * 2) date limit
 *      if it's turned on, then you can use the software until a specified date
 * 
 * Call this checking from every algorithm's constructor!
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class DemoCoron
{  
   /**
    * Is it a demo version or not?
    */
   //protected static final boolean isDemo = true;
   protected static final boolean isDemo = false;
   
   /**
    * Is the software date limited?
    */
   //public static final boolean isDateLimited = true;
   protected static final boolean isDateLimited = false;
   
   //////////////////////////////////////////////////////////////////////////
   
   /**
    * Time limit of the software.
    * If you want to use it, switch on the "isDateLimited" variable!
    */
   protected static final Calendar dateLimit = new GregorianCalendar(2005, Calendar.OCTOBER, 10);
   //public static final Calendar dateLimit = new GregorianCalendar(2005, Calendar.AUGUST, 31);
   
   /**
    * Which algorithms are allowed to use?
    */
   protected static Vector<Integer> allowedAlgs;
   
   /**
    * "Constructor"
    * 
    * 2007.10.08. -- preparing the first release
    * ==========================================
    * The following algorithms are allowed:
    * 
    * (01) Apriori
    * (02) AprioriClose
    * (03) AprioriInverse
    * (04) AprioriRare
    * (05) Arima
    * (06) CarpathiaG
    * (07) CarpathiaGRare
    * (08) Charm4
    * (09) Charm4b
    * (10) Close
    * (11) Eclat1
    * (12) EclatZ
    * (13) Pascal
    * (14) PascalPlus
    * (15) PseudoClosed
    * (16) Zart2
    */
   static 
   {
      DemoCoron.allowedAlgs = new Vector<Integer>();
      /*
       * 2007.10.08.
       */
      /* 01 */ DemoCoron.allowedAlgs.add(C.ALG_APRIORI);
      /* 02 */ DemoCoron.allowedAlgs.add(C.ALG_APRIORI_CLOSE);
      /* 03 */ DemoCoron.allowedAlgs.add(C.ALG_APRIORI_INVERSE);
      /* 04 */ DemoCoron.allowedAlgs.add(C.ALG_APRIORI_RARE);
      /* 05 */ DemoCoron.allowedAlgs.add(C.ALG_ARIMA);
      /* 06 */ DemoCoron.allowedAlgs.add(C.ALG_CARPATHIA_G);
      /* 07 */ DemoCoron.allowedAlgs.add(C.ALG_CARPATHIA_G_RARE);
      /* 08 */ DemoCoron.allowedAlgs.add(C.ALG_CHARM_4);
      /* 09 */ DemoCoron.allowedAlgs.add(C.ALG_CHARM_4B);
      /* 10 */ DemoCoron.allowedAlgs.add(C.ALG_CLOSE);
      /* 11 */ DemoCoron.allowedAlgs.add(C.ALG_ECLAT_1);
      /* 12 */ DemoCoron.allowedAlgs.add(C.ALG_ECLAT_Z);
      /* 13 */ DemoCoron.allowedAlgs.add(C.ALG_PASCAL);
      /* 14 */ DemoCoron.allowedAlgs.add(C.ALG_PASCAL_PLUS);
      /* 15 */ DemoCoron.allowedAlgs.add(C.ALG_PSEUDO_CLOSED);
      /* 16 */ DemoCoron.allowedAlgs.add(C.ALG_ZART_2);
      
      /*
       * algorithms added in a next release come here... 
       */
   }
   
   /**
    * @param alg Algorithm identifier.
    * @return True, if the algorithm is allowed to use. False, otherwise.
    */
   public static boolean isAlgAllowed(final int alg) 
   {
      // if it's not in demo mode, then everything is allowed
      if (DemoCoron.isDemo == false) return true;
      
      // else, if it's in demo mode
      return DemoCoron.allowedAlgs.contains(alg);
   }
   
   /**
    * Checks the date limit.
    */
   public final static void checkDateLimit()
   {
      // if it's not date-limited, do nothing, continue execution
      if (DemoCoron.isDateLimited == false) return;
      
      // else, if date limit is switched on, check if it can be executed or not
      if (DemoCoron.isExpired()) 
      {
         System.err.println("Coron: the evaluation period of the software has expired.");
         System.err.println("       If you want to continue using the program,\n"
                           +"       contact its authors ({szathmar, napoli}@loria.fr).");
         Error.die(C.ERR_JUST_EXIT);
      }      
   }
   
   /**
    * @return True, if the date limit of the program is expired. False, otherwise.
    */
   private final static boolean isExpired()
   {
      Calendar limit = DemoCoron.dateLimit;
      Calendar now = Calendar.getInstance();
      
      return (now.after(limit)); 
   }
   
   /**
    * @return True, if the software is in demo mode. False, otherwise.
    */
   public final static boolean isDemo() {
      return DemoCoron.isDemo;
   }
   
   /**
    * Prints some debug info about the limitations of the program.
    */
   @SuppressWarnings("static-access")
   public final static void getDemoCoronDescription()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("Demo mode:  ").append(DemoCoron.isDemo?"yes":"no").append("\n");
      sb.append("Date limit: ");
      if (DemoCoron.isDateLimited)
      {
         Calendar limit = DemoCoron.dateLimit;
         sb.append("yes (")
           .append(limit.get(limit.YEAR)).append(".")
           .append(limit.get(limit.MONTH)+1).append(".")
           .append(limit.get(limit.DAY_OF_MONTH))
           .append(") [yyyy.mm.dd]");
         if (DemoCoron.isExpired()) sb.append(", expired");
      }
      else sb.append("no");
      sb.append("\n");
      if (DemoCoron.isDemo)
      {
         sb.append("\n");
         sb.append("Allowed algorithms:").append("\n");
         int cnt = 0;
         int alg;
         for (Enumeration<Integer> e = DemoCoron.allowedAlgs.elements(); e.hasMoreElements(); )
         {
            ++cnt;
            alg = e.nextElement();
            sb.append(String.format("(%02d)  ", cnt))
              .append(Global.getAlgorithmStr(alg)).append("\n");
         }
      }
      
      System.out.println(sb);
   }
   
   /**
    * Checks if the software is in demo mode. If yes, then check
    * if the algorithm is allowed.
    * 
    * @param alg The chosen algorithm.
    */
   public static void checkIfAlgAllowed(final int alg)
   {
      if (DemoCoron.isDemo() == false) return;
      
      // else, if we are in demo mode
      if (DemoCoron.isAlgAllowed(alg) == false)
      {
         System.err.println("Coron: the chosen algorithm is not enabled\n"
                           +"       in this demo version. If you want to use this algorithm,\n"
                           +"       contact the authors ({szathmar, napoli}@loria.fr).");
         Error.die(C.ERR_JUST_EXIT);
      }
   }

   /**
    * Checks if the alg. is available AND the date limit is not over.
    * @param alg The algorithm to check.
    */
   public static void checkAlg(final int alg)
   {
      /*
       * (1) Check if the alg. is allowed. 
       *     // if not allowed, terminate the program
       */
      DemoCoron.checkIfAlgAllowed(alg);
      
      /*
       * (2) Check if we still have time to use the software.
       *     // if the date limit is over, terminate the program
       */
      DemoCoron.checkDateLimit();
   }
}
