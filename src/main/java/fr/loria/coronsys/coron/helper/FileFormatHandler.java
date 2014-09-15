package fr.loria.coronsys.coron.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.lut.LUT;
import fr.loria.coronsys.coron.helper.extra.PairWriter;
import fr.loria.coronsys.coron.helper.extra.PbmWriter;
import fr.loria.coronsys.coron.helper.extra.PpmWriter;
import fr.loria.coronsys.coron.helper.extra.SquareWriter;

/**
 * Handles (read/write/convert) different file formats.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class FileFormatHandler
{
   /**
    * Used to check if all lines have the same length in a .bool file. When the
    * line-parser is called for the 1st time, the length of this line is registered.
    * If another line differs in length, we drop an error message.
    */
   private static boolean first_call;
   
   /**
    * Used to check if all lines have the same length 
    * (same number of attributes) in a .bool file.
    */
   private static int attr_number;
   
   /**
    * Used to check how many objects are read in a .bool file.
    */
   private static int obj_number;
   
   /**
    * The length of the longest itemset.
    */
   private static int longestItemset;
   
   /**
    * Controller function for database reading. Investigates the input file's format,
    * and reads it in accordingly.
    */
   public static void readFile() 
   {
      if (Global.followFunctions()) System.err.println("> Reading input dataset...");
      long startTime = System.currentTimeMillis();
      String file 	= Database.getDatabaseFileStr();
      BitSet extra 	= Global.getExtra();
      //
      File input_file 	= Database.getDatabaseFile();
      int file_type		= checkFileType(input_file);
      Database.setDatabaseFileType(file_type);        // register it
      FileFormatHandler.longestItemset = 0;
      
      switch (file_type)
      {
         case C.FT_BASENUM:
            readBasenumFile(file);
            break;
         case C.FT_BOOL:
            readBoolFile(file, extra);
            break;
         case C.FT_RCF:
            readRcfFile(file, extra);
            break;
         default:
            System.err.println(">>> unknown file format");
         	Error.die(C.ERR_JUST_EXIT);
            break;
      }
      
      // during file processing we determined the longest possible itemset 
      Database.setLongestItemset(FileFormatHandler.longestItemset);
      
      if (Global.followFunctions()) {
         System.err.print("> Reading input dataset: done. ");
         System.err.println("Elapsed time: "+Statistics.getRuntime(System.currentTimeMillis() - startTime)+" sec.");
      }
   }

   /**
    * Controller function for database conversion. Investigates the output format,
    * and writes it out accordingly. It prints to the stdout.
    */
   public static void writeFile() 
   {   
      switch (Database.getConvertFileType())
      {
         case C.FT_BASENUM:
            System.out.println(getHeaderComments(C.FT_BASENUM));
            writeBasenumFile();
            break;
         case C.FT_BOOL:
            System.out.println(getHeaderComments(C.FT_BOOL));
            writeBoolFile();
            break;
         case C.FT_RCF:
            System.out.println(getHeaderComments(C.FT_RCF));
            writeRcfFile();
            break;
         case C.FT_SQUARE:
            /*
             * Only .rcf files can be converted to square format.
             * Otherwise, the program exits with an error message.
             */
            checkIfConvertable();
            System.out.println(getHeaderComments(C.FT_SQUARE));
            writeSquareFile();
            break;
         case C.FT_PAIRS:
            /*
             * Only .rcf files can be converted to pair format.
             * Otherwise, the program exits with an error message.
             */
            checkIfConvertable();
            System.out.println(getHeaderComments(C.FT_PAIRS));
            writePairFile();
            break;
         case C.FT_PBM:
            System.err.println(getHeaderComments(C.FT_PBM));
            writePbmFile();
            break;
         case C.FT_PPM:
            System.err.println(getHeaderComments(C.FT_PPM));
            writePpmFile();
            break;
         default:
            // it should never arrive here
            break;
      }
      
      //System.exit(0);
   }

   /**
    * If the input dataset is not in .rcf format, then it drops an error message,
    * and the program terminates.
    */
   private static void checkIfConvertable()
   {
      if (Database.getDatabaseFileType() != C.FT_RCF) 
      {
         System.err.println("Error: only .rcf files can be converted to this format.");
         System.err.println("Tip: try to do it in 2 steps.");
         System.err.println("     (1) convert your file to .rcf, then (2) convert the resulting .rcf to this format");
         Error.die(C.ERR_JUST_EXIT);
      }
   }

   /**
    * When converting a file in another format, it prints out
    * some info in the header part.
    * 
    * @param formatTo Into which format we want to convert the dataset. 
    * @return Header comment.
    */
   private static String getHeaderComments(int formatTo)
   {
      StringBuilder info = new StringBuilder();
      
      info.append("# Input dataset: " + Database.getDatabaseAbsoluteFilePath()).append("\n");
      info.append("# Output format: ");
      switch (formatTo)
      {
         case C.FT_BASENUM:
            info.append(".basenum");
            info.append("\n");
            break;
         case C.FT_BOOL:
            info.append(".bool");
            info.append("\n");
            break;
         case C.FT_RCF:
            if (Database.isDbInverted()) {
               info.append("inverted .rcf");
            } else {
               info.append(".rcf");
            }
            info.append("\n");
            break;
         case C.FT_SQUARE:
            info.append(".rcf (square [similarities, dissimilarities between objects])");
            break;
         case C.FT_PAIRS:
            info.append(".rcf (pairs [similarities, dissimilarities between objects])");
            break;
         case C.FT_PBM:
            info.append(".pbm (a graphical format, open with Gimp for instance)");
            info.append("\n");
            break;
         case C.FT_PPM:
            info.append(".ppm (a graphical format, open with Gimp for instance)");
            info.append("\n");
            break;
         default:
            break;
      }
      //info.append("\n");
      
      return (info.toString());
   }

   /**
    * Added by Edouard.
    */
   public static void writeFileFilter() 
   {   
      switch (Database.getConvertFileType())
      {
         case C.FT_BASENUM:
            //System.err.println("> The input file will be converted to .basenum format.");
            System.err.println();
            writeBasenumFile();
            break;
         case C.FT_BOOL:
            //System.err.println("> The input file will be converted to .bool format.");
            System.err.println();
            writeBoolFile();
            break;
         case C.FT_RCF:
            //System.err.println("> The input file will be converted to .rcf format.");
            System.err.println();
            writeRcfFile();
            break;
         default:
            // it should never arrive here
            break;
      }
      
      //System.exit(0);
   }
   
   /* ***********************************************************************
    * BASENUM filetype
    * ***********************************************************************/

   /** 
    * Reads in a file in basenum format and sets it in the static Database class.
    * Each line of the dataset will be stored in a bitset, and bitsets are stored
    * in a vector.
    * 
    * @param file Name of the input file.
    */
   @SuppressWarnings("unchecked")
   public static void readBasenumFile(String file)
   {
      String line;
      int currentLine = 0;					// which line we have read from the file
      int objCnt = 0;						// number of objects in the database (note: currentline != objCnt)
      Vector database = new Vector();
      
      try {
         BufferedReader in = new BufferedReader(new FileReader(file));
         while ( (line = in.readLine()) != null )
         {
            ++currentLine;
            // on
            //if (currentLine % 1000 == 0)
            //      System.err.println(currentLine); 
            // off
            line = line.trim();					// leading white-space characters can cause problems otherwise
            if (line.startsWith("#") || (line.length()==0)) continue;	// skip empty lines and remarks
            ++objCnt;	// remarks and empty lines are NOT objects, so they are not counted
            database.add(parseBasenumLine(line, currentLine));
         }
         database.trimToSize();
         in.close();
      }
      catch (FileNotFoundException fnfe) { Error.die(C.ERR_FILE_NOT_FOUND); }
      catch (java.io.IOException ioe)    { Error.die(C.ERR_IO); }

      Database.setNumberOfObjects(objCnt);
      Database.setDatabase(database, C.DBR_HORIZONTAL);
   }   
   
   /**
    * Adds a line of the dataset to the vertical representation.
    * 
    * @param database The vertical representation of the dataset.
    * @param objCnt The current line.
    * @param set Itemsets in the current line.
    */
   /*private static void addVertical(Vector<BitSet> database, int objCnt, BitSet set)
   {
      int size;
      int j;
      
      for (int i=set.nextSetBit(0); i>=0; i=set.nextSetBit(i+1)) 
      {
         // dynamic expansion of the vector
         if ((size = database.size()) < (i+1)) {
            for (j=0; j<(i+1-size); ++j) database.add(new BitSet());
         }
         ((BitSet) database.get(i)).set(objCnt);
      }
   }*/

   /**
    * Writes out the database in .basenum format.
    */
   private static void writeBasenumFile()
   {
      int i;
      BitSet set;
      
      for (Enumeration<BitSet> e = Database.getDatabase().elements(); e.hasMoreElements(); )
      {
         set = e.nextElement();
         for(i = set.nextSetBit(0); i >= 0; i = set.nextSetBit(i+1)) 
            System.out.print(i+" ");
         System.out.println();
      }
   }

   /** 
    * Parses a line of the input file and returns the numbers present in the line in a BitSet.
    * 
    * @param line A line of the input file as a String.
    * @param currentLine
    * @return A BitSet or a TreeSet containing the numbers present in the line. 
    * @throws NumberFormatException In case of the appearance of an illegal character.
    */
   @SuppressWarnings("unchecked")
   public static Object parseBasenumLine(String line, int currentLine)
   {                                                     
      BitSet  bitset  = new BitSet();
      TreeSet treeset = new TreeSet();
      Vector  vector  = new Vector();
      
      String items[] = line.split("\\s+");
      int i = 0;

      try 
      {
         if (Global.getItemsetRepresentation() == C.REPR_BITSET)
         {
            for (i=0; i<items.length; ++i)
               bitset.set(Integer.valueOf(items[i]).intValue());
         }
         else if (Global.getItemsetRepresentation() == C.REPR_TREESET)
         {
            for (i=0; i<items.length; ++i)
               treeset.add(LUT.getInteger(Integer.valueOf(items[i])));
               //treeset.add(Integer.valueOf(items[i]));
         }
         else // if representation is Vector
         {
            for (i=0; i<items.length; ++i)
               vector.add(LUT.getInteger(Integer.valueOf(items[i])));
         }
      } catch (NumberFormatException nfe) 
      {
         Error.dieBasenumNfe(currentLine, items[i]);
      }
      // the cardinality of the bitset is equal to items.length
      if (items.length > FileFormatHandler.longestItemset)
         FileFormatHandler.longestItemset = items.length;

      if      (Global.getItemsetRepresentation() == C.REPR_BITSET)  return bitset;
      else if (Global.getItemsetRepresentation() == C.REPR_TREESET) return treeset;
      else                                                          return vector;
   }
   
   /* ***********************************************************************
    * BOOL filetype
    * ***********************************************************************/
   
   /**
    * Reads in a file in bool format (boolean table) and sets it in the static Database class.
    * Each line of the dataset will be stored in a bitset, and bitsets are stored
    * in a vector.
    * 
    * @param file Name of the input file.
    * @param extra Extra options set at the command line. For us the interesting thing
    * is if the user set the indexing of attributes to 0. The default is 1 as starting index.
    */
   @SuppressWarnings("unchecked")
   public static void readBoolFile(String file, BitSet extra)
   {
      FileFormatHandler.first_call 	= true;
      FileFormatHandler.attr_number	= 0;
      FileFormatHandler.obj_number	= 0;
      //
      String line;
      int currentLine 	= 0;					// which line we have read from the file
      Vector database 	= new Vector();
      // boolean pos0		= extra.get(C.X_POS0); // Not used anymore
      int objCnt 			= 0;

      try {
         BufferedReader in = new BufferedReader(new FileReader(file));
         while ( (line = in.readLine()) != null )
         {
            ++currentLine;
            line = line.trim();					// leading white-space characters can cause problems otherwise
            if (line.startsWith("#") || (line.length()==0)) continue;	// skip empty lines and remarks
            ++objCnt;
            database.add(parseBoolLine(line, currentLine));
            //database.add(parseBoolLine(line, currentLine, pos0));
         }
         database.trimToSize();
         in.close();
      }
      catch (FileNotFoundException fnfe) { Error.die(C.ERR_FILE_NOT_FOUND); }
      catch (java.io.IOException ioe)    { Error.die(C.ERR_IO); }

      Database.setNumberOfObjects(objCnt);
      Database.setDatabase(database, C.DBR_HORIZONTAL);
   }

   /** 
    * Prints the input file in .bool format (zeros and ones).
    */
   public static void writeBoolFile() {
      System.out.print(FileFormatHandler.getBoolFile());
   }
   
   /** 
    * Prints the input file in .pbm format.
    * http://netpbm.sourceforge.net/doc/pbm.html
    */
   public static void writePbmFile() 
   {
      PbmWriter pw = new PbmWriter(Database.getDatabase());
      pw.start();
   }
   
   /** 
    * Prints the input file in .ppm format.
    * http://netpbm.sourceforge.net/doc/ppm.html
    */
   public static void writePpmFile() 
   {
      PpmWriter pw = new PpmWriter(Database.getDatabase());
      pw.start();
   }
   
   /** 
    * Converts the input file to .bool format (zeros and ones).
    * @return The database in .bool format.
    */
   @SuppressWarnings("unchecked")
   public static String getBoolFile()
   {
      Set set;
      int i, k, max = -1;
      StringBuilder line, pattern = new StringBuilder();
      Iterator it;
      BitSet bs;
      Enumeration e;
      Vector w = new Vector();

      for (e = Database.getDatabase().elements(); e.hasMoreElements(); )
      {
         set = new TreeSet();
         
         bs = (BitSet)e.nextElement();
         for(i=bs.nextSetBit(0); i>=0; i=bs.nextSetBit(i+1))
         {
            set.add(LUT.getInteger(i));
            if (i>max) max = i;
         }
         w.add(set);
      }
      
      /* 
       * Start: post modification
       * 
       * This part is for the filter "column keep", which means: we only keep
       * some specified columns. For ex.: the input .rcf file had 5 columns, but
       * we only want to keep the first three. In this case at the place of the missing two 
       * there should be zeros in the boolean table. That's why we set "max" back.
       */
      if (Database.getDatabaseFileType() == C.FT_RCF)
         max = Database.getAttrNumber();
      // End:   post modification

      for (e = w.elements(); e.hasMoreElements(); )
      {
         set = (SortedSet)e.nextElement();
         line = new StringBuilder(max);

         /*if (Global.getExtra().get(C.X_POS0))
         {
            for (k=0; k<=max; ++k) line.append("0");
            
            for (it = set.iterator(); it.hasNext(); )
               line.setCharAt(((Integer)it.next()).intValue(), '1');            
         }
         else
         {
            for (k=0; k<max; ++k) line.append("0");
            
            for (it = set.iterator(); it.hasNext(); )
               line.setCharAt(((Integer)it.next()).intValue()-1, '1');            
         }*/
         
         for (k=0; k<max; ++k) line.append("0");
         
         for (it = set.iterator(); it.hasNext(); )
            line.setCharAt(((Integer)it.next()).intValue()-1, '1');
         
         pattern.append(line);
         pattern.append("\n");
      }

      return pattern.toString().replaceAll("0", "0 ").replaceAll("1", "1 ");
   }
   
   /**
    * Parses a line of the input file and returns the numbers present in the line in a BitSet.
    * NOT USED ANYMORE: REPLACED BY parseBoolLine(String line, int currentLine)
    * 
    * @param line The current line of the input file.
    * @param currentLine The number of the current line.
    * @param pos0 Is the indexing of attributes start with 0?
    * 
    * @return A BitSet or a TreeSet containing the numbers present in the line.
    */
   @SuppressWarnings("unchecked")
   public static Object parseBoolLine(String line, int currentLine, boolean pos0)
   {                                                     
      BitSet  bitset  = new BitSet();
      TreeSet treeset = new TreeSet();
      Vector  vector  = new Vector();
      
      line = line.replaceAll("\\s+", "");
      int length = line.length();
      char c;
      
      // start: length check
      if (FileFormatHandler.first_call) {
         FileFormatHandler.attr_number = length;
         FileFormatHandler.first_call 	= false;
      }
      else /* if not the first call (not the first line */ 
         if (length != FileFormatHandler.attr_number) 
            Error.dieBoolUnequalLineLength(currentLine, FileFormatHandler.attr_number, length);
      // end: length check

      if (Global.getItemsetRepresentation() == C.REPR_BITSET)
      {
         for (int i = 0; i < length; ++i)
         {
            c = line.charAt(i);
            //if (c == '1') bitset.set(pos0?i:i+1);
            if (c == '1') bitset.set(i+1);
            else if (c != '0') Error.dieBoolNfe(currentLine, c);
         }
      }
      else if (Global.getItemsetRepresentation() == C.REPR_TREESET)
      {
         for (int i = 0; i < length; ++i)
         {
            c = line.charAt(i);
            //if (c == '1') treeset.add(LUT.getInteger(pos0?i:i+1));
            if (c == '1') treeset.add(LUT.getInteger(i+1));
            else if (c != '0') Error.dieBoolNfe(currentLine, c);
         }
      }
      else // if representation is Vector
      {
         for (int i = 0; i < length; ++i)
         {
            c = line.charAt(i);
            //if (c == '1') vector.add(LUT.getInteger(pos0?i:i+1));
            if (c == '1') vector.add(LUT.getInteger(i+1));
            else if (c != '0') Error.dieBoolNfe(currentLine, c);
         }
      }
            
      // the cardinality of the bitset is equal to "length"
      if (length > FileFormatHandler.longestItemset)
         FileFormatHandler.longestItemset = length;

      if      (Global.getItemsetRepresentation() == C.REPR_BITSET)  return bitset;
      else if (Global.getItemsetRepresentation() == C.REPR_TREESET) return treeset;
      else                                                          return vector;
   }
   
   /**
    * Parses a line of the input file and returns the numbers present in the line in a BitSet.
    * 
    * @param line The current line of the input file.
    * @param currentLine The number of the current line.
    * 
    * @return A BitSet or a TreeSet containing the numbers present in the line.
    */
   @SuppressWarnings("unchecked")
   public static Object parseBoolLine(String line, int currentLine)
   {                                                     
      BitSet  bitset  = new BitSet();
      TreeSet treeset = new TreeSet();
      Vector  vector  = new Vector();
      
      line = line.replaceAll("\\s+", "");
      int length = line.length();
      char c;
      
      // start: length check
      if (FileFormatHandler.first_call) {
         FileFormatHandler.attr_number = length;
         FileFormatHandler.first_call  = false;
      }
      else /* if not the first call (not the first line */ 
         if (length != FileFormatHandler.attr_number) 
            Error.dieBoolUnequalLineLength(currentLine, FileFormatHandler.attr_number, length);
      // end: length check

      if (Global.getItemsetRepresentation() == C.REPR_BITSET)
      {
         for (int i = 0; i < length; ++i)
         {
            c = line.charAt(i);
            if (c == '1') bitset.set(i+1);
            else if (c != '0') Error.dieBoolNfe(currentLine, c);
         }
      }
      else if (Global.getItemsetRepresentation() == C.REPR_TREESET)
      {
         for (int i = 0; i < length; ++i)
         {
            c = line.charAt(i);
            if (c == '1') treeset.add(LUT.getInteger(i+1));
            else if (c != '0') Error.dieBoolNfe(currentLine, c);
         }
      }
      else // if representation is Vector
      {
         for (int i = 0; i < length; ++i)
         {
            c = line.charAt(i);
            if (c == '1') vector.add(LUT.getInteger(i+1));
            else if (c != '0') Error.dieBoolNfe(currentLine, c);
         }
      }
            
      // the cardinality of the bitset is equal to "length"
      if (length > FileFormatHandler.longestItemset)
         FileFormatHandler.longestItemset = length;

      if      (Global.getItemsetRepresentation() == C.REPR_BITSET)  return bitset;
      else if (Global.getItemsetRepresentation() == C.REPR_TREESET) return treeset;
      else                                                          return vector;
   }
   
   /* ***********************************************************************
    * RCF filetype
    * ***********************************************************************/
   
   /**
    * Reads the .rcf file format. It is also used in the Galicia project.
    * Its advantage is that we can assign names to objects and attributes,
    * so at last the output will be human readable :)
    * 
    * @param file Name of the input file.
    * @param extra Extra options, given at the command line.
    */
   @SuppressWarnings("unchecked")
   public static void readRcfFile(String file, BitSet extra)
   {
      FileFormatHandler.first_call 	= true;
      FileFormatHandler.attr_number	= 0;
      FileFormatHandler.obj_number	= 0;
      //
      String line;
      int currentLine 	= 0;					// which line we have read from the file
      int objCnt 			= 0;
      Vector database 	= new Vector();
      // boolean pos0		= extra.get(C.X_POS0); // Not used anymore

      try {
         BufferedReader in = new BufferedReader(new FileReader(file));
         currentLine += skipHeader(in);
         currentLine = readObjectList(in, currentLine);
         currentLine = readAttributeList(in, extra, currentLine);
         while ( (line = in.readLine()) != null )
         {
            ++currentLine;
            line = line.trim();
            if (line.equals("[END Relational Context]")) break;
            if (line.startsWith("#") || (line.length()==0)) continue;	// skip empty lines and remarks
            //
            ++FileFormatHandler.obj_number;
            ++objCnt;
            database.add(parseBoolLine(line, currentLine));
            //database.add(parseBoolLine(line, currentLine, pos0));
            /*if (db_representation == C.DBR_HORIZONTAL) database.add(parseBoolLine(line, currentLine, pos0));
            else if (db_representation == C.DBR_VERTICAL) addVertical(database, objCnt, parseBoolLine(line, currentLine, pos0));*/
         }
         if (line==null)	Error.dieRcfHeaderFooter(C.RCF_NO_END_REL_CONTEXT, currentLine);
         
         database.trimToSize();
         in.close();
      }
      catch (FileNotFoundException fnfe) { Error.die(C.ERR_FILE_NOT_FOUND); }
      catch (java.io.IOException ioe)    { Error.die(C.ERR_IO); }
      
      /*
       * OK. We have read the boolean table of the database. If we got so far, then
       * it's sure that the boolean table was a rectangle, i.e. each object had the same
       * length of attribute list. Now let's check if it corresponds to the 
       * object names list and attribute list names (which are just before the
       * boolean table in the .rcf file).
       */
      int found_obj 			= FileFormatHandler.obj_number,
      	 should_be_obj		= Database.getObjNumber(),
      	 found_attr			= FileFormatHandler.attr_number,
      	 should_be_attr	= Database.getAttrNumber();
      
      if (found_obj != should_be_obj) 		Error.dieRcfTable(C.RCF_OBJ_DIFF, file, found_obj, should_be_obj);
      // else
      if (found_attr != should_be_attr)	Error.dieRcfTable(C.RCF_ATTR_DIFF, file, found_attr, should_be_attr);
      // else
      
      Database.setNumberOfObjects(objCnt);
      Database.setDatabase(database, C.DBR_HORIZONTAL);
      //System.err.println(">>> db size: "+database.size());   // debug
   }
   
   /**
    * Converts the input file to .rcf format.
    */
   private static void writeRcfFile()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("[Relational Context]\n")
        .append("Default Name\n")
        .append("[Binary Relation]\n")
        .append("Name_of_dataset\n");
      sb.append(getObjectList());
      sb.append(getAttrList());
      sb.append(getBoolFile());
      sb.append("[END Relational Context]\n");
      
      System.out.print(sb);
   }
   
   /**
    * Converts the input file to square format.
    */
   private static void writeSquareFile()
   {     
      SquareWriter sw = new SquareWriter(Database.getDatabase());
      sw.start();
   }
   
   /**
    * Converts the input file to pair format.
    */
   private static void writePairFile()
   {     
      PairWriter pw = new PairWriter(Database.getDatabase());
      pw.start();
   }
   
   /**
    * Returns the object list for .rcf file format.
    * 
    * @return Object list for .rcf file format.
    */
   private static Object getObjectList()
   {
      StringBuilder sb = new StringBuilder();
      Vector<String> objects = Database.getObjectNames();
      int size = Database.getDatabase().size();
      int i;
      
      if (objects == null)    // in the case of .basenum and .bool input
      {
         for (i = 0; i < size; ++i) {
            if (i>0) sb.append(" | ");    // to avoid '|' after the last object name
            sb.append("o_"+(i+1));
         }
         sb.append("\n");
      }
      else                       // in the case of .rcf input
      {
         for (i = 1; i <= size; ++i) {
            if (i>1) sb.append(" | ");    // to avoid '|' after the last object name
            sb.append(Database.getObj(i));
         }
         sb.append("\n");
      }
      //
      return sb.toString();
   }
   
   /**
    * Returns the attribute list for .rcf file format.
    * 
    * @return Attribute list for .rcf file format. 
    */
   private static String getAttrList()
   {
      StringBuilder sb = new StringBuilder();
      Vector<String> attributes = Database.getAttributeNames();
      int from = 1;
      boolean first = true;
      String attrName = "a";
      
      if (attributes == null)    // in the case of .basenum and .bool input
      {
         int largest = Database.getTotalNumberOfAttr();
         /*if (Global.getExtra().get(C.X_POS0)) {
            from = 0;
         }*/

         for (; from <= largest; ++from) 
         {
            if (first == false) sb.append(" | ");     // avoiding '|' after the last element 
            sb.append(attrName + "_(" + from + ")");
            attrName = ContextOp.getNextName(attrName);    // increments attr. name by one
            if (first) {                                    // a,b,...,z,aa,ab,...,az,ba,bb,...
               first = false;
            }
         }
      }
      else                       // in the case of .rcf input
      {
         if (attributes.get(0) != null) from = 0;
         //
         int size = attributes.size();
         for (; from < size; ++from)
         {
            if (first == false) sb.append(" | ");     // avoiding '|' after the last element
            sb.append((String)attributes.get(from));
            if (first) {
               first = false;
            }
         }
      }
      sb.append("\n");
      //
      return sb.toString();
   }

   /**
    * Reads the object list, parses it and registers the
    * object names in the static Database class.
    * 
    * @param in A BufferedReader object on the input line. We read just one line.
    * @param currentLine The number of the current line. We read one line, so we increment it by one.
    * @return Returns the value of the new current line (we incremented it by 1).
    * 
    * @throws IOException An I/O error can occur while reading from the file.
    */
   @SuppressWarnings("unchecked")
   private static int readObjectList(BufferedReader in, int currentLine) throws IOException
   {
      String line = in.readLine().trim();
      ++currentLine;
      // remove '|' character at the end
      if (line.charAt(line.length()-1) == '|') line = line.substring(0, line.length()-1).trim();
      
      String objects[] = line.split("\\|");
      Vector objectsV = new Vector(objects.length+1);
      objectsV.add(null);
      // trim object names
      for (int i=0; i<objects.length; ++i) {
         //objects[i] = objects[i].trim();
         objectsV.add(objects[i].trim());
      }
      Database.setObjectNames(objectsV);
      
      return currentLine;
   }
   
   /**
    * Reads the attribute list, parses it and registers the
    * attribute names in the static Database class.
    * 
    * @param in A BufferedReader object on the input line. We read just one line.
    * @param extra Extra options given at the command line.
    * @param currentLine The number of the current line. We read one line, so we increment it by one.
    * @return Returns the value of the new current line (we incremented it by 1).
    * 
    * @throws IOException An I/O error can occur while reading from the file.
    */
   @SuppressWarnings("unchecked")
   private static int readAttributeList(BufferedReader in, BitSet extra, int currentLine) throws IOException
   {
      String line = in.readLine().trim();
      ++currentLine;
      if (line.charAt(line.length()-1) == '|') line = line.substring(0, line.length()-1).trim();
      
      String attributes[]	= line.split("\\|");
      Vector attributesV = new Vector(attributes.length + 1);
      Database.setAttrNumber(attributes.length);
      /*
       * If the user chooses 0 as the start number of indexation for attributes, then the
       * split above is just good.
       * But, if the indexation start from 1 (it is the default), then we need to move the
       * elements of the array one position right. How to do it? Allocate a one-sized
       * bigger array, leave the place at 0 in peace, and copy the whole array from position 1.
       * Don't forget to clean names from trailing white spaces either.
       * See the block below.
       */
      attributesV.add(null); // as we are in pos1!!

      // trim attribute names
      for (int i=0; i<attributes.length; ++i)
         attributesV.add(attributes[i].trim());
      
      Database.setAttributeNames(attributesV);
      
      return currentLine;
   }
   
   /**
    * Skips the header part of the file till the object list. Various errors can occur,
    * they are also treated.
    * 
    * @param in A BufferedReader object on the input line. We read just one line.
    * @return How many lines were skipped.
    * 
    * @throws IOException An I/O error can occur while reading from the file.
    */
   private static int skipHeader(BufferedReader in) throws IOException
   {
      String line;
      int skippedLines = 0;
      
      // skip comments and empty lines at the beginning. In .rcf format we allow these kinds
      // of extra information just at the beginning of the file
      while ( (line = in.readLine()).trim().equals("[Relational Context]") == false )
      {
         ++skippedLines;
         if (line.startsWith("#") || (line.length()==0)) continue;	// skip empty lines and remarks
         else Error.dieRcfHeaderFooter(C.RCF_COMMENT, skippedLines);
      }
      // OK, comments and blank lines are skipped
      if (line.equals("[Relational Context]") == false)
         Error.dieRcfHeaderFooter(C.RCF_NO_REL_CONTEXT, skippedLines);
      ++skippedLines;
      in.readLine();		// "Default Name"
      ++skippedLines;
      if ( (line = in.readLine()).trim().equals("[Binary Relation]") == false )
         Error.dieRcfHeaderFooter(C.RCF_NO_BIN_RELATION, skippedLines);
      ++skippedLines;
      in.readLine();		// "Name_of_dataset"
      ++skippedLines;
      
      return skippedLines;
   }
   
   /* ***********************************************************************
    * other helper functions
    * ***********************************************************************/

   /**
    * Finds out the type of the input file by investigating its extension.
    * 
    * @param input_file The input database.
    * @return Type of the databse according to its extension.
    */
   public static int checkFileType(File input_file)
   {
      int type 	= C.FT_UNKNOWN;
      String ext 	= getExtension(input_file.getName());
      
      if (ext.equals("basenum")) 	type = C.FT_BASENUM;
      else if (ext.equals("bool"))	type = C.FT_BOOL;
      else if (ext.equals("rcf"))	type = C.FT_RCF;
         
      return type;
   }

   /**
    * Returns the extension of a filename, i.e. everything after the last dot.
    * 
    * @param file_name File's name.
    * @return Extension of the filename.
    */
   private static String getExtension(String file_name) {
      return file_name.substring(file_name.lastIndexOf(".")+1, file_name.length());
   }
}
