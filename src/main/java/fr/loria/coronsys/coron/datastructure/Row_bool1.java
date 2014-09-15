package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;

/**
 * Class for rows that need a boolean value.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_bool1 extends Row
//implements Serializable
{
   /**
    * Eclipse wanted to add it...
    */
   //private static final long serialVersionUID = 1L;
   
   /**
    * Constructor.
    * 
    * @param row A Row object.
    */
   public Row_bool1(BitSet row) {
      super(row);
   }
   
   /**
    * @param row
    */
   public Row_bool1(Row row) {
      super(row);
   }

   /**
    * Constructor. 
    */
   public Row_bool1()
   {
   }

   /**
    * A boolean value.
    */
   protected boolean flag;
}
