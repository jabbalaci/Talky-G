package fr.loria.coronsys.coron.datastructure.close;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_closure;
import fr.loria.coronsys.coron.datastructure.Table;

/** 
 * Abstract class for the common properties of FCC and FC tables.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
abstract class Table_Close_FCC_FC extends Table
{
   /**
    * Adds a row to the table.
    * 
    * @param set A bitset that we want to add to the row.
    */
   public void add_row(BitSet set) {
      Row row = new Row_closure(set);
      add_row(row);
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
      this.trie.add(((Row_closure)row).getGenerator(), row);
   }
}
