package fr.loria.coronsys.coron.helper;

import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class for commonly used set operations.
 *                         ==============
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class SetOp
{
   /**
    * Empty private constructor. The class cannot be instantiated.
    */
   private SetOp() { }

   /**
    * Is a bitset part of or equal to another bitset?
    *  
    * @param a A bitset.
    * @param b Another bitset.
    * @return Returns true if bitset 'a' is part of or equal to bitset 'b'.
    */
   public static boolean partOf(BitSet a, BitSet b) {
      BitSet a_copy = (BitSet)a.clone();
      a_copy.and(b);
      return a_copy.equals(a);
   }

   /**
    * Is a bitset part of or equal to another bitset in a vector of bitsets?
    * 
    * @param s A bitset.
    * @param v A vector of bitsets.
    * @return True if bitset 's' is part of or equal to a bitset in vector 'v'.
    */
   public static boolean partOf(BitSet s, Vector<BitSet> v)
   {
      for (Enumeration<BitSet> e = v.elements(); e.hasMoreElements(); )
         if (partOf(s, e.nextElement())) return true;
      // else
      return false;
   }

   /**
    * Difference operation. Example:
    * A = {1, 2, 3, 4, 5, 6}
    * B = {1, 3, 5, 6}
    * A \ B = {2, 4}
    * 
    * @param setA Set A.
    * @param setB Set B. 
    * @return Difference set operation on sets A and B, i.e. A \ B.
    */
   public static BitSet minus(BitSet setA, BitSet setB)
   {
      BitSet cloneA = (BitSet) setA.clone();
      cloneA.andNot(setB);
      return cloneA;
   }

   /**
    * Is a set a proper superset of another one?
    * 
    * @param other Current set.
    * @param curr Other set.
    * 
    * @return True, if curr is proper superset of other. False, otherwise.
    */
   public static boolean isProperSuperset(BitSet curr, BitSet other) 
   {
      if (curr.cardinality() > other.cardinality())
      {
         BitSet copy = (BitSet) other.clone();
         copy.and(curr);
         return copy.equals(other); 
      }
      // else
      return false;
   }  
   
   /**
    * Is a set a proper subset of another one?
    * 
    * @param curr Current set.
    * @param other Other set.
    * 
    * @return True, if curr is proper subset of other. False, otherwise.
    */
   public static boolean isProperSubset(BitSet curr, BitSet other)
   {
      if (curr.cardinality() < other.cardinality())
      {
         BitSet copy = (BitSet) curr.clone();
         copy.and(other);
         return copy.equals(curr); 
      }
      // else
      return false;
   }
   
   /**
    * Is a set a subset of another one?
    * 
    * In this version we don't check the cardinality.
    * 
    * @param curr Current set.
    * @param other Other set.
    * 
    * @return True, if curr is a subset of other. False, otherwise.
    */
   public static boolean isSubset(BitSet curr, BitSet other)
   {
      BitSet copy = (BitSet) curr.clone();
      copy.and(other);
      
      return copy.equals(curr);
   }

   /**
    * Given an input set, we want to keep the maximal elements
    * only (w.r.t. set inclusion). 
    * 
    * @param set An arbitrary set.
    * @return Maximal elements of the set (minimal elements as well as
    * duplicates are removed).
    */
   public static Vector<BitSet> getMaxima(Vector<BitSet> set)
   {
      Collections.sort(set, new BitsetComparatorBySize());
      
      final int size = set.size();
      int i, j;
      BitSet a, b;
      
      outer_loop:
      for (i = 0; i < size - 1; ++i)
      {
         if ( (a = set.get(i)) != null )
         {
            for (j = i + 1; j < size; ++j)
            {
               if ( (b = set.get(j)) != null )
               {
                  if (partOf(a, b)) {
                     set.set(i, null);
                     continue outer_loop;
                  }
               }
            }
         }
      }
      
      
      BitSet bs;
      Vector<BitSet> result = new Vector<BitSet>();
      
      for (i = 0; i < size; ++i) 
      {
         if ( (bs = set.get(i)) != null ) {
            result.add(bs);
         }
      }
      
      return result;
   }
   
   
   /** 
    * Creates a BitSet from a String such as "11001100"
    * @param str The String to convert
    * @return BitSet representation
    */
   public static BitSet StringToBitSet(String str) {
		BitSet res = new BitSet();
		for (int i = 0; i < str.length(); i++)
			if(str.charAt(i) == '1')
				res.set(i);
		return res;
	}
   

   /**
    * Same as StringToBitSet(String). However, first index in BitSet is 
    * ignored (in compliance with Coron data structure).
    * 
    * @param str The String to convert
    * @return BitSet representation (first possible true position is one)
    */
   public static BitSet StringToBitSetFromOne(String str) {
		BitSet res = new BitSet();
		for (int i = 0; i < str.length(); i++)
			if(str.charAt(i) == '1')
				res.set(i+1);
		return res;
	}
   
   /**
    * @param itemset An arbitrary itemset.
    * @return List of its one-size smaller subsets.
    */
   public static Vector<BitSet> getOneSizeSmallerSubsetsOf(BitSet itemset)
   {
      Vector<BitSet> v = new Vector<BitSet>();

      for (int i = itemset.nextSetBit(0); i >= 0; i = itemset.nextSetBit(i+1)) 
      {
         itemset.clear(i);
         v.add((BitSet) itemset.clone());
         itemset.set(i);
      }
      return v;
   }
   
}

/**
 * Compare two bitsets by size (cardinality).
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
class BitsetComparatorBySize 
implements Comparator<BitSet>
{
   // compare two Bitsets (callback for sort)
   // Effectively returns a-b; e.g. 
   // +1 (or any positive number) if a > b
   // 0 if a == b
   // -1 (or any negative number) if a < b
   public final int compare(BitSet a, BitSet b) {
      return ( ((BitSet) a).cardinality() - ((BitSet) b).cardinality() );
   } // end compare
}
