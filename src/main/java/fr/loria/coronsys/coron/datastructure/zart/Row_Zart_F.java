package fr.loria.coronsys.coron.datastructure.zart;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_predSupp;
import fr.loria.coronsys.coron.datastructure.pascalplus.Row_PascalPlus_F;


/**
 * Row for Pascal F table.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_Zart_F extends Row_PascalPlus_F
//implements Serializable
{
   /**
    * @param row
    */
   public Row_Zart_F(Row_predSupp row) {
      super(row);
   }

   /**
    * This constructor is used by Eclat-Z.
    * 
    * @param row A frequent itemset.
    * @param key Is it a minimal generator?
    */
   public Row_Zart_F(Row row, boolean key) {
      super(row, key);
   }
}
