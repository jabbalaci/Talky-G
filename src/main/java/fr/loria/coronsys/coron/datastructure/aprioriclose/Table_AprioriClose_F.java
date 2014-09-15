package fr.loria.coronsys.coron.datastructure.aprioriclose;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Table_bool1;

/** 
 * Class for the following tables:
 * 
 * - Apriori FC (frequent candidate)
 * - Apriori F  (frequent)
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_AprioriClose_F extends Table_bool1
{  
   /**
    * Adds a row to the table and registers its generator field in the trie.
    * 
    * @param row The row we want to add to the table.
    */
   public void add_row(Row row) {
      this.add_row(new Row_AprioriClose_F(row));
   }
   
   /**
    * @param row
    */
   @SuppressWarnings("unchecked")
   public void add_row(Row_AprioriClose_F row)
   {
      rows.add(row);
      this.trie.add(row.getItemset(), row);
   }
}
