package fr.loria.coronsys.coron.datastructure.pascal;

import fr.loria.coronsys.coron.datastructure.Row_predSupp;
import fr.loria.coronsys.coron.datastructure.Table;

/** 
 * Table Pascal F.
 * 
 * - for Frequent
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_Pascal_F extends Table
{
   /**
    * Adds a row to the table and registers its itemset field in the trie.
    * This constructor may be faster as it makes a reference from
    * table F to the rows of table FC. But as a consequence,
    * we'll also have the field pred_supp in table F, which is not
    * necessary. The other constructor solves this "problem".
    */
   /*public void add_row(Row_Pascal_F row)
   {
      rows.add(row);
      this.trie.add(row.itemset, row);
   }*/
   
   /**
    * Constructor.
    * 
    * @param useTrie Should we use a trie? 
    */
   public Table_Pascal_F(boolean useTrie)
   {
      super(useTrie);
   }

   /**
    * Constructor. Use trie.
    */
   public Table_Pascal_F()
   {
      this(true);
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
   @SuppressWarnings("unchecked")
   public void add_row(Row_predSupp row)
   {
      Row_Pascal_F toAdd = new Row_Pascal_F(row);
      rows.add(toAdd);
      if (useTrie) this.trie.add(toAdd.getItemset(), toAdd);
   }
}
