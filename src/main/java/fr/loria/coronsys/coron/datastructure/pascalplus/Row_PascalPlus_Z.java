package fr.loria.coronsys.coron.datastructure.pascalplus;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.aprioriclose.Row_AprioriClose_Z;

/**
 * Class for the common properties of different rows.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_PascalPlus_Z extends Row_AprioriClose_Z
//implements Serializable
{
   /**
    * Eclipse wanted to add it...
    */
   //private static final long serialVersionUID = 1L;
   
   /**
    * @param row
    */
   public Row_PascalPlus_Z(Row_PascalPlus_F row) {
      super(row);
   }

   /**
    * Empty constructor. Used by BtB.
    * @param supp Support.
    * @param closure Closure.
    */
   public Row_PascalPlus_Z(BitSet closure, int supp)
   {
      super(closure, supp);
   }
}
