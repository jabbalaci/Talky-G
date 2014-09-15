package fr.loria.coronsys.coron.datastructure.aprioriclose;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.pascalplus.Row_PascalPlus_F;

/**
 * Class for the common properties of different rows.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_AprioriClose_Z extends Row 
//implements Serializable
{
   /**
    * Eclipse wanted to add it...
    */
   //private static final long serialVersionUID = 1L;

   /**
    * @param row
    */
   public Row_AprioriClose_Z(Row_AprioriClose_F row) {
      super(row);
   }

   /**
    * @param row
    */
   public Row_AprioriClose_Z(Row_PascalPlus_F row) {
      super(row);
   }

   /**
    * Empty constructor. Used by BtB.
    * @param supp Support.
    * @param closure Closure.
    */
   public Row_AprioriClose_Z(BitSet closure, int supp)
   {
      super(closure, supp);
   }

   /**
    * @return String representation of the row (closure, support).
    */
   public String toString() {
      return ""+this.itemset+" ("+this.supp+") +";
   }
}
