package fr.loria.coronsys.coron.datastructure.close;

import java.util.BitSet;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.Row_closure;

/**
 * Represents FCC tables (candidates).
 * Not all of the rows will be part of the final result, 
 * they have to be tested, pruned.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
*/
public class Table_Close_FCC extends Table_Close_FCC_FC
{
   /**
    * Constructor. Calls the constructor of the superclass.
    * Maybe it's redundant and can be deleted.
    */
   public Table_Close_FCC() {
      super();
   }

   /**
    * Determines if the generator field of the table is empty. 
    * In other words: is the table empty or not? It is empty if it has no rows.
    * 
    * @return True if the generator field of the table is empty (that is the table has no rows).
    */
   public boolean isGeneratorEmpty() {
      return getRows().isEmpty();
   }

   /**
    * Adds a generator to the table. That is: it adds a new row whose generator
    * field is set, but closure and support is unknown yet.
    * 
    * @param set The generator field that we want to add to table in a row.
    */
   @SuppressWarnings("unchecked")
   public void add_generator(BitSet set) {
      this.rows.add(new Row_closure(set));
   }

   /**
    * We have a generator, and we want to find all its
    * subsets that are one size smaller.
    * Ex.: {1,4,6}. The function will return a vector of
    * the following bitsets: {4,6}, {1,6}, {1,4}.
    * 
    * @param generator The generator whose one-size smaller subsets (all of them) we want to get.
    * @return All of the one-size smaller subsets of the 'generator'.
    */
   @SuppressWarnings("unchecked")
   public static Vector<BitSet> get_i_subsets(BitSet generator)  
   {                                                     
      Vector v = new Vector();

      for(int i=generator.nextSetBit(0); i>=0; i=generator.nextSetBit(i+1)) {
         generator.clear(i);
         v.add(generator.clone());
         generator.set(i);
      }
      return v;
   }

   /**
    * Creates a candidate generator by concatenation. It receives an int array
    * plus an int number, and the function puts them together and returns the
    * result in a bitset.
    * 
    * @param a An int array.
    * @param b An int number.
    * @return Concatenates the int array and the int number, and returns the result in a bitset.
    */
/*   public BitSet createCandidate(int[] a, int b)
   {
      BitSet set = new BitSet();
      
      for (int i = 0; i < a.length; ++i)
         set.set(a[i]);
      set.set(b);
      
      return set;
   }
   */
}
