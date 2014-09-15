package fr.loria.coronsys.coron.datastructure.pascal;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.Row_predSupp;
import fr.loria.coronsys.coron.datastructure.Table;

/**
 * Table Pascal FC
 * 
 * - for Frequent
 * - don't add rows to trie immediately! Pascal-Gen() will do that.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_Pascal_FC extends Table
{
   /**
    * Constructor.
    * 
    * @param useTrie Use trie or not?
    */
   public Table_Pascal_FC(boolean useTrie) {
      super(useTrie);
   }

   /**
    * Constructor.
    */
   public Table_Pascal_FC() {
      super();
   }

   /**
    * Adds a row to the table.
    * 
    * @param set A bitset that we want to add to the row.
    * @return Returns the reference of the row added.
    */
   public Row_predSupp add_row(BitSet set) 
   {
      Row_predSupp row = new Row_predSupp(set); 
      add_row(row);
      return row;
   }

   /**
    * Adds a row to the table and registers its generator field in the trie.
    * 
    * @param row The row we want to add to the table.
    */
   @SuppressWarnings("unchecked")
   public void add_row(Row_predSupp row) {
      rows.add(row);
      // not inserted in the trie!
   }
   
   /**
    * Are there key itemsets in this table?
    * 
    * @return True, if there are key itemsets in the table. False, if all itemsets are not keys.
    */
   public boolean hasKeyItemset() {
      return (this.getTrie().isEmpty() == false);
   }
}
