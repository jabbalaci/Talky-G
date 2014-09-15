package fr.loria.coronsys.coron.datastructure.apriori;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Table;

/** 
 * Apriori F table.
 * 
 * - for Frequent
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_Apriori_F extends Table
{   
   /**
    * Constructor.
    */
   public Table_Apriori_F() {
      super();
   }
   
   /**
    * Do we want to use the table's trie?
    * 
    * @param useTrie Boolean value whether to use trie or not.
    */
   public Table_Apriori_F(boolean useTrie) {
      super();
      this.useTrie = useTrie;
   }

   /**
    * Adds a row to the table.
    * 
    * @param set A bitset that we want to add to the row.
    */
   public void add_row(BitSet set) {
      add_row(new Row(set));
   }

   /**
    * Adds a row to the table and registers its generator field in the trie.
    * 
    * @param row The row we want to add to the table.
    */
   @SuppressWarnings("unchecked")
   public void add_row(Row row)
   {
      rows.add(row);
      if (this.useTrie) this.trie.add(row.getItemset(), row);
   }
}
