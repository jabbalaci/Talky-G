package fr.loria.coronsys.coron.datastructure.zart;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_predSupp;
import fr.loria.coronsys.coron.datastructure.pascalplus.Table_PascalPlus_F;

/** 
 * Table Zart F.
 * 
 * - for Frequent
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_Zart_F extends Table_PascalPlus_F
{
   /**
    * Adds a row to the table and registers its itemset field in the trie.
    * It makes a new Row_Pascal_F object, and copies values to it. The
    * itemset is a reference, but other values are copied. The field 
    * pred_supp is not kept in table F. It may be slower, and because of
    * the copying, during the copy process we have two tables in the memory.
    * 
    * @param row The row we want to add to the table.
    */
   @SuppressWarnings("unchecked")
   public void add_row(Row_predSupp row)
   {
      Row_Zart_F toAdd = new Row_Zart_F(row);
      
      rows.add(toAdd);
      this.trie.add(toAdd.getItemset(), toAdd);
   }

   /**
    * This constructor is used by Eclat-Z.
    * 
    * @param row A frequent itemset.
    * @param key Is it a minimal generator?
    */
   @SuppressWarnings("unchecked")
   public void add_row(Row row, boolean key)
   {
      Row_Zart_F toAdd = new Row_Zart_F(row, key);
      
      rows.add(toAdd);
      this.trie.add(toAdd.getItemset(), toAdd);
   }
}
