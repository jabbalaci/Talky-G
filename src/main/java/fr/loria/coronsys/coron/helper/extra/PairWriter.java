package fr.loria.coronsys.coron.helper.extra;

import java.util.BitSet;
import java.util.Vector;

import fr.loria.coronsys.coron.helper.Database;

/**
 * This class is responsible for converting the dataset in pair format.
 * Pair format: this is an RCF file that contains information about
 * similarities and dissimilarities between objects. In the case of 5
 * objects, it creates the following pairs: 
 * 1-2, 1-3, 1-4, 1-5,
 * 2-3, 2-4, 2-5
 * 3-4, 3-5
 * 4-5 
 * 
 * Input:  (m) x (n)
 * Output: (m * (m-1) / 2) x (3 * n)
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class PairWriter
{
   /**
    * The dataset that we want to convert.
    */
   private Vector<BitSet> database;
   
   /**
    * Object names in the result.
    */
   private Vector<String> objNames;
   
   /**
    * Attribute names in the result.
    */
   private Vector<String> attrNames;
   
   /**
    * Constructor.
    * 
    * @param database The database that is to be converted.
    */
   public PairWriter(Vector<BitSet> database)
   {
      this.database  = database;
         addZeroElementNull();
      this.objNames  = new Vector<String>();
         this.objNames.add(null);               // position 0 is not used
      this.attrNames = new Vector<String>();
         this.attrNames.add(null);              // position 0 is not used
   }

   /**
    * We add a null at position 0. Reason:  we want to get
    * objects using "normal" indexes, i.e. database[1] is the 
    * first object, etc.
    */
   private void addZeroElementNull()
   {
      if (Database.isZeroElementNull() == false) 
         Database.addZeroElementNull();
   }

   /**
    * Controller function.
    */
   public void start()
   {
      printExtraInfo();
      printRcfHeader();
      printObjNames();
      printAttrNames();
      printDiffTable();
      printRcfTail();
   }
   
   /**
    * Print some info about the size of the input and output datasets.
    */
   private void printExtraInfo()
   {
      StringBuilder sb = new StringBuilder();
      int m = Database.getObjNumber(),        // the size of the input dataset: m x n
          n = Database.getAttrNumber();       // that is: m lines, n columns
      
      sb.append("#").append("\n");
      sb.append("# Input DB size:  ").append(m).append(" x ").append(n).append("\n");
      sb.append("# Output DB size: ")
        .append("(").append(m).append(" * ").append(m-1).append(" / 2").append(")")
        .append(" x ")
        .append("(").append(n).append(" * ").append("3").append(")")
        .append(" = ").append(m * (m-1) / 2).append(" * ").append(n * 3)
        .append("\n");
      
      System.out.println(sb);
   }

   /**
    * Print the header part of the RCF file.
    */
   private void printRcfHeader()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("[Relational Context]\n")
        .append("Default Name\n")
        .append("[Binary Relation]\n")
        .append("Name_of_dataset\n");
      
      System.out.print(sb);
   }
   
   /**
    * Print the tail part of the RCF file.
    */
   private void printRcfTail() {
      System.out.println("[END Relational Context]");
   }

   /**
    * Print the object names.
    */
   private void printObjNames()
   {
      int i, j;
      boolean first = true;
      int size = Database.getNumberOfObjectsInDatabase();
      StringBuilder sb = new StringBuilder();
      
      for (i = 1; i < size; ++i)
      {
         for (j = i+1; j <= size; ++j)
         {
            if (first == false) sb.append(" | ");     // avoiding '|' after the last element
            
            sb.append("d(").append(Database.getObj(i)).append(",")
              .append(Database.getObj(j)).append(")");
            
            if (first) first = false;
         }
      }
      
      System.out.println(sb);
   }

   /**
    * Print the attribute names.
    */
   private void printAttrNames()
   {
      int from = 1;      
      boolean first = true;
      String attr;
      StringBuilder sb = new StringBuilder();
      
      Vector<String> attributes = Database.getAttributeNames();
      /*
       * it is 100% sure here that the input file is in .rcf format
       * (otherwise the program terminates before)
       */
      if (attributes.get(0) != null) from = 0;
      //      
      int size = attributes.size();
      for (; from < size; ++from)
      {
         if (first == false) sb.append(" | ");     // avoiding '|' after the last element
         
         attr = (String)attributes.get(from);
         sb.append(attr+"__-").append(" | ");
         sb.append(attr+"__=").append(" | ");
         sb.append(attr+"__+");
         
         if (first) first = false;
      }
      
      System.out.println(sb);
   }
   
   /**
    * Find similarities and dissimilarities betwwen objects.
    */
   private void printDiffTable()
   {      
      int i, j, k;
      int size = Database.getNumberOfObjectsInDatabase();
      StringBuilder sb = new StringBuilder();
      BitSet set1, set2;
      int nbAttr = Database.getAttrNumber();
      
      /*
       * These two loops make all pairs between objects. For instance, we have 3 lines.
       * Then we'll have the following pairs: 1-2, 1-3, 2-3.
       */
      for (i = 1; i < size; ++i)           
      {                                   
         for (j = i+1; j <= size; ++j)
         {
            set1 = this.database.get(i);
            set2 = this.database.get(j);
            
            //System.out.println(set1+", "+set2);
            
            for (k=1; k <= nbAttr; ++k)
            {
               if ((set1.get(k) == true) && (set2.get(k) == false))        // present in 1, not present in 2
                  sb.append("100");
               else if ((set1.get(k) == false) && (set2.get(k) == true))   // present in 2, not present in 1
                  sb.append("001");
               else if ((set1.get(k) == true) && (set2.get(k) == true))    // present in both 1 and 2
                  sb.append("010");
               else sb.append("000");                                      // not present neither in 1, nor in 2
            }
            sb.append("\n");
         }
      }
      
      System.out.print(sb.toString().replaceAll("0", "0 ").replaceAll("1", "1 "));
   }
}
