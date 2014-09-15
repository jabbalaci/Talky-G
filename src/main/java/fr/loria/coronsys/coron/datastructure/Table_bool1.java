package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;

/** 
 * Class for the following tables:
 * 
 * - Apriori FC (frequent candidate)
 * - Apriori F  (frequent)
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_bool1 extends Table
{   
   /**
    * Adds a row to the table.
    * 
    * @param set A bitset that we want to add to the row.
    */
   public void add_row(BitSet set) {
      add_row(new Row_bool1(set));
   }

   /**
    * Adds a row to the table and registers its generator field in the trie.
    * 
    * @param row The row we want to add to the table.
    */
   @SuppressWarnings("unchecked")
   public void add_row(Row_bool1 row)
   {
      rows.add(row);
      this.trie.add(row.itemset, row);
   }
}
