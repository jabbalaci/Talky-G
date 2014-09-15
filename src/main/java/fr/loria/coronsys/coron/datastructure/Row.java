package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;

import fr.loria.coronsys.coron.helper.Database;

/**
 * Class for the common properties of different rows.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row 
implements Comparable<Object>
{  
   /**
    * Closure field of the row (contains the closure of the generator).
    */
   protected BitSet itemset;
   
   /**
    * Support field of the row (contains the support of the closure).
    */
   protected int supp;
   
   /** 
    * Constructor. Initialisation: sets closure to empty, sets support to 0.
    */
   public Row() {
      this.itemset = new BitSet();
      this.supp    = 0;
   }
   
   /**
    * Alternative constructor.
    * 
    * @param set An itemset.
    */
   public Row(BitSet set) {
      this.itemset = set;
      this.supp    = 0;
   }
   
   /**
    * Alternative constructor.
    * 
    * @param set An itemset.
    * @param supp Support of the itemset.
    */
   public Row(BitSet set, int supp)
   {
      this.itemset = set;
      this.supp    = supp;
   }
   
   /**
    * Alternative constructor.
    * 
    * @param row A Row object.
    */
   public Row(Row row) 
   {
      this.itemset = row.getItemset();
      this.supp    = row.getSupp();
   }

   /** 
    * Get the closure of the row.
    * 
    * @return Closure (as a bitset) of the row.
    */
   public BitSet getItemset() { 
      return this.itemset; 
   }

   /** 
    * Sets the closure of a row.
    * 
    * @param set The new closure of the row.
    */
   public void setItemset(BitSet set) 
   { 
      this.itemset.clear();
      this.itemset.or(set);
   }

   /**
    *  Get the support of an itemset in a row.
    * 
    * @return Support of an itemset in a row.
    */
   public int getSupp() { 
      return this.supp; 
   }
   
   /**
    * Set the support of an itemset in a row.
    * 
    * @param supp New support of the itemset.
 	 */
   public void setSupp(int supp) {
      this.supp = supp;
   }

   /**
    * Increment the support of an itemset by one. 
    */
   public void incSupp() { ++this.supp; }
   
   /**
    * Gets the size of the itemset (closure), how many attributes it contains.
    * 
    * @return Size of the itemset closure, how many attributes it has.
 	 */
   public int getSize() {
      return this.itemset.cardinality();
   }
   
   /**
    * Sort the concepts in ascending order by the length of the intents.
    * 
    * @param o The other concept of the lattice.
    * @return Value to be able to compare the objects.
    */
   public int compareTo(Object o)
   {
      if (o==null || !(o instanceof Row))
         throw new ClassCastException();
      return this.itemset.cardinality() - ((Row)o).itemset.cardinality();
   }
   
   /**
    * @return String representation of the row (closure, support).
    */
   public String toString() {
      return ""+this.itemset+" ("+this.supp+")";
   }

   /**
    * Replace attribute numbers with their names.
    * 
    * @return String representtion of the row.
    */
   public String toStringName()
   {
      StringBuilder sb = new StringBuilder();
      
      sb.append(Database.toNamesAttr(this.itemset))
        .append(" (").append(this.supp).append(")");
      
      return sb.toString();
   }
}
