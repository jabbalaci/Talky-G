package fr.loria.coronsys.coron.datastructure.carpathiag;

import fr.loria.coronsys.coron.datastructure.Row_predSupp;
import fr.loria.coronsys.coron.datastructure.pascal.Table_Pascal_F;

/** 
 * Table Pascal F.
 * 
 * - for Frequent
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_CarpathiaG_F extends Table_Pascal_F
{  
   /**
    * Constructor.
    * 
    * @param useTrie Should we use a trie?
    */
   public Table_CarpathiaG_F(boolean useTrie)
   {
      super(useTrie);
   }

   /**
    * Constructor.
    * 
    * Use trie.
    */
   public Table_CarpathiaG_F()
   {
      super(true);   // use trie
   }

   /**
    * Adds a row to the table and registers its itemset field in the trie.
    * It makes a new Row_Pascal_F object, and copies values to it. The
    * itemset is a reference, but other values are copied. The field 
    * pred_supp is not kept in table F. It may be slower, and because of
    * the copying, during the copy process we have two tables in the memory.
    * 
    * @param row The row we want to add to the table.
    */
   public void add_row(Row_predSupp row)
   {
      Row_CarpathiaG_F toAdd = new Row_CarpathiaG_F(row);
      rows.add(toAdd);
      if (useTrie) this.trie.add(toAdd.getItemset(), toAdd);
   }

   /**
    * Constructor. Used in Eclat-G.
    * 
    * @param row Row to add.
    */
   public void add(Row_CarpathiaG_F row)
   {
      rows.add(row);
      if (useTrie) this.trie.add(row.getItemset(), row);
   }
}
