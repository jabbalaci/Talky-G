package fr.loria.coronsys.coron.datastructure.aprioriclose;

import fr.loria.coronsys.coron.datastructure.Table;

/** 
 * Apriori-Close Z table:
 * 
 * - for FCIs
 * - no trie
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_AprioriClose_Z extends Table
{
   /**
    * @param useTrie Should a trie be built?
    */
   public Table_AprioriClose_Z(boolean useTrie) {
      super(useTrie);
   }

   /**
    * Constructor. Use trie.
    */
   public Table_AprioriClose_Z() {
      this(true);
   }

   /**
    * Adds a row to the table.
    * 
    * @param row The row we want to add to the table.
    */
   //public void add_row(Row row) {
   //   rows.add(row);
   //}
   
   @SuppressWarnings("unchecked")
   public void add_row(Row_AprioriClose_F row)
   {
      Row_AprioriClose_Z toAdd = new Row_AprioriClose_Z(row);
      
      rows.add(toAdd);
      if (useTrie) this.trie.add(toAdd.getItemset(), toAdd);
   }
}
