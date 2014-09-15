package fr.loria.coronsys.coron.helper;

import java.io.File;
import java.io.IOException;

/**
 * Class to handle errors.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Error
{
   /**
    * Empty private constructor. The class cannot be instantiated.
    */
   private Error() { }
   
   /** 
    * Drops an error message and exits.
    * 
    * @param what An integer (constant from the C class) indicating what kind of error occured.
    */
   public static void die(int what)
   {
      switch (what)
      {
//         case C.ARGS:
//            System.err.println("Error: at least one agument is required!");
//            System.err.println("Usage: java Main <basenum_file> [<minsupport>]");
//            break;
         case C.ERR_SECOND_ARG:
            System.err.println("Error: the second argument (min_supp) should be an integer");
            System.err.println("       or a double value (between 0 and 100) followed by '%'!");
            System.err.println("       example: 2 or 20% or 1.75%");
            break;
         case C.ERR_FILE_NOT_FOUND:
            System.err.println("Error: the input file was not found!");
            break;
         case C.ERR_IO:
            System.err.println("Error: I/O problem while reading the input file!");
            break;
         case C.ERR_WRONG_NUMBER_FORMAT_IN_INPUT_FILE:
            System.err.println("Error: wrong number format in the input file! Use just integers!");
            break;
         case C.ERR_BAD_SWITCH:
            System.err.println("Error: wrong switch format (its correct form is like -v=f for instance)!");
            break;
         case C.ERR_MANY_ARGS_AT_END:
            System.err.println("Error: too many arguments at the end (allowed: <database> [<minsupport>])!");
            break;
         case C.ERR_LESS_ARGS_AT_END:
            System.err.println("Error: not enough arguments at the end (allowed: <database> [<minsupport>])!");
            break;
         case C.ERR_MIN_SUPP_TOO_LARGE:
            System.err.println("Error: the specified min_supp value exceeds the total number of objects in the database!");
            System.err.println("Tip:   choose a value less than or equal to " + Convert.byteToPrettyString(Database.getNumberOfObjects()) + ".");
            break;
         case C.ERR_REDIRECTED:
            System.err.println("Error: Too much output files. Use -of|out=<output> option only once.");
            break;
         case C.ERR_NOT_RCF:
            System.err.println("Error: the input file is not in RCF format!");
            break;
         case C.ERR_JUST_EXIT:
            break;
         default:
            // it shouldn't arrive here
            System.err.println(">>> Error: the value ("+what+") is not treated in coron/helper/Error.java! <<<");
            break;
      }
      
      try {
         CheckOfOptionAndDestroy();
      }
      catch (IOException e) {
         System.out.println("Error: "+e);
      }
      
      System.exit(-1);  // exit in all cases
   }
   
   /**
    * Error message about the wrong number format in a .basenum file, 
    * indicating the exact line number of the error.
    * 
    * @param line Which line in the input file contains errors. 
    * @param error The string which causes the error.
    */
   public static void dieBasenumNfe(final int line, String error)
   {
      System.err.println("Error: wrong number format in the input file in line "+line+" with the string \""+error+"\"! Use just integers!");
      
      try {
         CheckOfOptionAndDestroy();
      }
      catch (IOException e) {
         System.out.println("Error: "+e);
      }
      
      System.exit(-1);  // exit
   }
   
   /**
    * In a boolean table only zeros and ones are permitted. If an illegal character
    * was used, then an error message is dropped indicating the line number
    * and specifying the wrong character.
    * 
    * @param line The number of the line where the error is.
    * @param wrong_char The wrong character.
    */
   public static void dieBoolNfe(final int line, char wrong_char)
   {
      System.err.println("Error: illegal character ('"+wrong_char+"') in the input file in line "+line+"! Use just zeros and ones!");

      try {
         CheckOfOptionAndDestroy();
      }
      catch (IOException e) {
         System.out.println("Error: "+e);
      }
      
      System.exit(-1);  // exit
   }
   
   /**
    * In a boolean table each line must have the same quantity of boolean values.
    * Otherwise we drop an error message.
    * 
    * @param line The wrong line whose size differs from the previous ones.
    * @param wrong
    * @param good
    */
   public static void dieBoolUnequalLineLength(int line, int good, int wrong)
   {
      System.err.println("Error: line "+line+" has a different length ("+wrong+") than the previous lines ("+good+")!");
      System.err.println("       Each line must have the same quantity of boolean values!");
      System.err.println("       If it's the last line, then make sure that the line");
      System.err.println("       '[END Relational Context]' is correctly written!");

      try {
         CheckOfOptionAndDestroy();
      }
      catch (IOException e) {
         System.out.println("Error: "+e);
      }
      
      System.exit(-1);  // exit
   }
   
   /**
    * In the header part of an .rcf file different errors can occur. We treat them here.
    * 
    * @param error_type Type of the error.
    * @param wrong_line Which line has the error.
    */
   public static void dieRcfHeaderFooter(int error_type, int wrong_line)
   {
      switch (error_type)
      {
         case C.RCF_COMMENT:
            System.err.println("Error: in an .rcf file's header part only comments (lines starting with #),");
         	System.err.println("       and blank lines are allowed. Problematic line: "+wrong_line+".");
         	System.err.println();
         	printRcfTemplate();
            break;
         case C.RCF_NO_REL_CONTEXT:
            System.err.println("Error: the compulsory starting line '[Relational Context]' is missing.");
         	System.err.println();
         	printRcfTemplate();
            break;
         case C.RCF_NO_BIN_RELATION:
            System.err.println("Error: the compulsory line '[Binary Relation]' is missing.");
         	System.err.println();
         	printRcfTemplate();
            break;
         case C.RCF_NO_END_REL_CONTEXT:
            System.err.println("Error: the compulsory end line '[END Relational Context]' is missing.");
         	System.err.println();
         	printRcfTemplate();
            break;
         default:
            break;
      }
      
      try {
         CheckOfOptionAndDestroy();
      }
      catch (IOException e) {
         System.out.println("Error: "+e);
      }
      
      System.exit(-1);
   }
   
   /**
    * The size of the boolean table is not the same as it should be 
    * by the list of object names and attribute names.
    * 
    * @param error_type Type of the error (object number or attribute number is different).
    * @param file Name of the input file.
    * @param found Found number of objects/attributes.
    * @param should_be This is the number of objects/attributes that is in the name list. The
    * size of the boolean table should exactly be this.
    */
   public static void dieRcfTable(int error_type, String file, int found, int should_be)
   {
      switch (error_type)
      {
         case C.RCF_OBJ_DIFF:
            System.err.println("Error: in the file "+file+" the number of objects in the boolean table ("+found+")");
         	System.err.println("       is not the same as the number of object names ("+should_be+").");
            break;
         case C.RCF_ATTR_DIFF:
            System.err.println("Error: in the file "+file+" the number of attributes in the boolean table ("+found+")");
         	System.err.println("       is not the same as the number of attribute names ("+should_be+").");
            break;
         default:
            break;
      }
      
      try {
         CheckOfOptionAndDestroy();
      }
      catch (IOException e) {
         System.out.println("Error: "+e);
      }
      
      System.exit(-1);
   }
   
   /**
    * It prints a template .rcf file to the STDOUT, thus it can easily be
    * redirected to a file, and so one can modify it to his/her needs.
    */
   private static void printRcfTemplate()
   {
      StringBuilder sb = new StringBuilder();
      
      System.err.println("Here is a template for an .rcf file. Save it and modify it to your needs:");
      System.err.println();
      
      sb.append("[Relational Context]\n");
      sb.append("Default Name\n");
      sb.append("[Binary Relation]\n");
      sb.append("Name_of_dataset\n");
      sb.append("1 | 2 | 3 | 4 | 5 | \n");
      sb.append("a | b | c | d | e | \n");
      sb.append("1 0 1 1 0 \n");
      sb.append("0 1 1 0 1 \n");
      sb.append("1 1 1 0 1 \n");
      sb.append("0 1 0 0 1 \n");
      sb.append("1 1 1 0 1 \n");
      sb.append("[END Relational Context]\n");
      
      System.out.println(sb.toString());
   }
   
   /** 
    * Drops a warning message. Does not quit!
    * 
    * @param what An integer (constant from the C class) indicating what kind of warning occured.
    * @param dir The directory in which we want to create the file.
    * @param file The file we want to create.
    */
   public static void warning(int what, File dir, File file)
   {
      switch (what)
      {
         case C.F_FILE_ALREADY_EXISTS:
            System.err.println("Warning: cannot write trie's .dot output to directory "+dir.getName()+",\n" +
                               "because a file with this name already exists!");
            break;
         case C.F_CANNOT_CREATE_DIR:
            System.err.println("Warning: cannot create directory "+dir.getName()+" for the trie's .dot output!");
            break;
         case C.F_CANNOT_WRITE_DIR:
            System.err.println("Warning: the output directory "+dir.getName()+" is not writeable!");
            break;
         case C.F_CANNOT_DELETE_FILE:
            System.err.println("Warning: The output file "+dir.getName()+"/"+file.getName()+" already exists, and cannot be deleted!");
            break;
         case C.F_CANNOT_CREATE_FILE:
            System.err.println("Warning: The output file "+dir.getName()+"/"+file.getName()+" cannot be created for write operation!");
            break;
         case C.F_IO:
            System.err.println("Warning: I/O error while writing to "+dir.getName()+"/"+file.getName()+"!");
            break;
         default:
            break;
      }
      System.err.println();
   }
   

   /**
    * Display a given error message and leave the program. (-1 status)
    * @param message
    */
   public static void die (String message)
   {
	   System.err.println("Coron: "+message);
      
      try {
         CheckOfOptionAndDestroy();
      }
      catch (IOException e) {
         System.out.println("Error: "+e);
      }
      
	   System.exit(-1);
   }
   
   /**
    * This method is designed to prevent from creating an empty file, in writing a wrong
    * command line, containing "-of:outputFileName"
    * 
    * @throws IOException
    */
   private static void CheckOfOptionAndDestroy() throws IOException
   {
      // Is -of set?
      if (Global.isRedirected)
      {
         // If "yes" -> is there any file called "Global.outputName"?
         File output = new File(Global.getOutputFileName());
         if (output.exists())
         {
            // is this file empty? if "yes" -> just remove it
            if (output.length() == 0d){
               output.delete();
               }
            // If "no" (can it be? i don't think so) -> just throw message
            else System.err.println("Warning: " + output.getCanonicalPath() + " is not an empty file!");
         }
      }
   }
}
