package fr.loria.coronsys.coron.datastructure.universal;

import java.util.BitSet;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.aprioriclose.Row_AprioriClose_Z;
import fr.loria.coronsys.coron.datastructure.pascalplus.Row_PascalPlus_Z;
import fr.loria.coronsys.coron.datastructure.zart.Row_Zart_Z;

/**
 * A universal row data structure for rows of Z tables (that contain FCIs).  
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_Universal_Z extends Row
implements Comparable<Object>
{
   /**
    * Vector of minimal generators.
    */
   protected Vector<BitSet> min_gen;
   
   /**
    * Default constructor.
    */
   public Row_Universal_Z() {
      super();
   }
   
   /**
    * Creates a universal row from a Row_AprioriClose_Z object.
    * Algorithms: Apriori-Close.
    * 
    * @param row A Row_AprioriClose_Z object.
    */
   public Row_Universal_Z(Row_AprioriClose_Z row) {
      this.itemset = row.getItemset();
      this.supp = row.getSupp();
   }
   
   /**
    * Creates a universal row from an intent and a support.
    * Algorithms: Charm.
    * 
    * @param intent The FCI.
    * @param supp Support of the FCI.
    */
   public Row_Universal_Z(BitSet intent, int supp)
   {
      this.itemset = intent;
      this.supp = supp;
   }
   
   /**
    * @param closure Closure.
    * @param supp    Support.
    * @param gens    List of generators.
    */
   public Row_Universal_Z(BitSet closure, int supp, Vector<BitSet> gens)
   {
      this(closure, supp);
      this.min_gen = gens;
   }
   
   /**
    * Creates a universal row from a Row_Zart_Z object.
    * Algorithms: Zart.
    * 
    * @param row A Row_AprioriClose_Z object.
    */
   public Row_Universal_Z(Row_Zart_Z row) {
      this((Row_AprioriClose_Z)row);
      this.min_gen = row.getMinGens();
   }
   
   /**
    * Creates a universal row from a Row_Zart_Z object.
    * Algorithms: Zart.
    * 
    * @param row A Row_AprioriClose_Z object.
    */
   public Row_Universal_Z(Row_PascalPlus_Z row) {
      this((Row_AprioriClose_Z)row);
   }
   
   /**
    * Adds a minimal generator.
    * 
    * @param min_gen A minimal generator that we want to add.
    */
   @SuppressWarnings("unchecked")
   public void addMinGen(BitSet min_gen) {
      if (this.min_gen == null) this.min_gen = new Vector();
      this.min_gen.add(min_gen);
   }
   
   /**
    * Returns the minimal generator(s).
    * 
    * @return Returns the minimal generator(s).
    */
   public Vector<BitSet> getMinGen() {
      return this.min_gen;
   }

   /**
    * Sort the concepts in ascending order by the length of the intents.
    * 
    * @param o The other concept of the lattice.
    * @return Value to be able to compare the objects.
    */
   /*public int compareTo(Object o)
   {
      if (o==null || !(o instanceof Row_Universal_Z))
         throw new ClassCastException();
      return this.itemset.cardinality() - ((Row_Universal_Z)o).itemset.cardinality();
   }*/

   /**
    * @return True, if the eq. class (that is represented by this row) is simple. False,
    * otherwise.
    */
   public boolean isSimple()
   {  
      /*
       * A class is simple if it only has one generator AND
       * the closure of the class is identical with this generator.
       */
      return ( (this.min_gen.size() == 1) && this.itemset.equals((BitSet)this.min_gen.get(0)) );
   }
   
   /**
    * @return String representation of the row (closure, support, minimal generator(s)).
    */
   /*public String toString() {
		return new ToStringBuilder(this)
			.append("itemset", this.itemset)
			.append("support", this.supp)
			.append("min_gen", this.min_gen)
			.toString();
	}*/
}
