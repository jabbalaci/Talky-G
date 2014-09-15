package fr.loria.coronsys.coron.helper;

import java.util.BitSet;
import java.util.Vector;

/**
 * Class for commonly used context operations.
 *                         ==================
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 * @author Mehdi
 */
public class ContextOp
{
   /**
    * Empty private constructor. The class cannot be instantiated.
    */
   private ContextOp() { }

   /**
    * Increments the attribute name by one. Ex.: a, b, c, ..., z, aa, ab, ..., az, ba, bb, ...
    * 
    * "public static", because Coron calls this function too when converting
    * .basenum or .bool files to .rcf 
    * 
    * @param attr Attribute name.
    * @return Next attribute name.
    */
   public static String getNextName(String attr)
   {
      int i, chInt;
      boolean inc = true;

      StringBuilder sb = new StringBuilder(attr);
      sb.reverse();

      for (i=0; i<sb.length(); ++i)
      {
         if (inc == false) break;

         if (sb.charAt(i) < 'z')
         {
            chInt = sb.charAt(i);
            sb.setCharAt(i, (char) ++chInt);
            inc = false;
         }
         else
         {
            sb.setCharAt(i, 'a');
            inc = true;
         }
      }
      if (inc) sb.append('a');

      return sb.reverse().toString();
   }

   /**
    * Get the image of the given itemset (by Mehdi).
    * @param itemset The given itemset
    * @return the bitset with true at the i th position if the itemset is possessed by the i th object of the database
    */
   public static BitSet getImage (BitSet itemset) {
      return ContextOp.getImage(Database.getDatabase(), itemset);
   }
   
   /**
    * @param itemset An itemset.
    * @return Support of the itemset (by Laszlo).
    */
   public static int getSupportOf(BitSet itemset) {
      return ContextOp.getImage(itemset).cardinality();
   }

   /**
    * Get the image of a given itemset.
    * Same as getImage (BitSet itemset) but the search is done on the given
    * database and not on Database.getDatabase(). Useful when dealing
    * with several instances of databases.
    * 
    * @param database The database where all the objects are stored.
    * @param itemset The given itemset.
    * @return return The bitset with true at the i_th position if the itemset is 
    * possessed by the i_th object of the database.
    */
   public static BitSet getImage (Vector<BitSet> database, BitSet itemset)
   {
      BitSet image = new BitSet();

      BitSet currCopy;
      for (int i = 0; i < database.size(); ++i)
      {
         currCopy = (BitSet) (database.get(i).clone());
         currCopy.and(itemset);
         if (currCopy.equals(itemset)) {
            image.set(i+1);
         }
      }
      
      return image;
   }   
   
   /**
    * Get the closure of a given itemset.
    * 
    * @param itemset The itemset whose closure wa want.
    * @return Closure of the itemset.
    */
   public static BitSet getClosure(BitSet itemset) {
      return ContextOp.getClosure(Database.getDatabase(), itemset);
   }

   /**
    * Get the closure of an itemset.
    * 
    * @param itemset The itemset whose closure we want to calculate.
    * @return The closure of the given itemset.
    */
   public static BitSet getClosure(Vector<BitSet> database, BitSet itemset)
   {
      BitSet closure = null;
      BitSet curr;
      
      for (int i = 0; i < database.size(); ++i)
      {
         curr = (BitSet) database.get(i);
         if (SetOp.partOf(itemset, curr))
         {
            if (closure == null) closure = (BitSet) curr.clone();
            else                 closure.and(curr);
         }
      }
      
      return closure;      
   }
}
