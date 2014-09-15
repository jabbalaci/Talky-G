package fr.loria.coronsys.coron.datastructure.pascalplus;

import fr.loria.coronsys.coron.datastructure.aprioriclose.Table_AprioriClose_Z;

/** 
 * Apriori-Close Z table:
 * 
 * - for FCIs
 * - no trie
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_PascalPlus_Z extends Table_AprioriClose_Z
{
   /**
    * @param useTrie Should a trie be built?
    */
   public Table_PascalPlus_Z(boolean useTrie) {
      super(useTrie);
   }

   /**
    * Constructor. Use trie.
    */
   public Table_PascalPlus_Z() {
      this(true);
   }

   /**
    * @param row
    */
   @SuppressWarnings("unchecked")
   public void add_row(Row_PascalPlus_F row)
   {
      Row_PascalPlus_Z toAdd = new Row_PascalPlus_Z(row);
      
      rows.add(toAdd);
      if (useTrie) this.trie.add(toAdd.getItemset(), toAdd);
   }
}
