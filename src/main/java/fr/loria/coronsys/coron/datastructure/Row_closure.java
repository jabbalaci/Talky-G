package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.pascalplus.Row_PascalPlus_F;

/**
 * Class for the common properties of FCC and FC rows.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_closure extends Row
//implements Serializable
{
   /**
    * Eclipse wanted to add it...
    */
   //private static final long serialVersionUID = 1L;
   
   /**
    * Generator field of the row.
    */
   protected BitSet itemset2;

   /**
    * Constructor. Calls the constructor of the superclass +
    * initialises the generator field. 
    */
   Row_closure() {
      super();
      itemset2 = new BitSet();
   }

   /** 
    * Constructor. Calls the constructor of the superclass + sets the generator field.
    * 
    * @param set Generator of the row.
    */
   public Row_closure(BitSet set) {
      super();
      itemset2 = set;
   }

   /**
    * Another constructor. Used by Pseudo-Closed
    * 
    * @param row Row to add.
    */
   public Row_closure(Row_PascalPlus_F row)
   {
      super(row);
      itemset2 = new BitSet();
   }

   /** 
    * Get the generator of a row.
    * 
    * @return The generator of a row.
    */
   public BitSet getGenerator() { 
   	  return this.itemset2; 
   }
   
   /**
    * A detailed String representation of the row, including the generator too.
    * 
    * @return String representation of the row (generator, closure, support).
    */
   public String toString() {
      return ""+itemset2+"; "+itemset+"; ("+supp+")";
   }
}
