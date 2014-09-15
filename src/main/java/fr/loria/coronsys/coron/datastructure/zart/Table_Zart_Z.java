package fr.loria.coronsys.coron.datastructure.zart;

import fr.loria.coronsys.coron.datastructure.pascalplus.Table_PascalPlus_Z;

/** 
 * Zart Z table:
 * 
 * - for FCIs
 * - no trie
 * - store minimal generators of the FCI too
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_Zart_Z extends Table_PascalPlus_Z
{
   /**
    * @param useTrie Should a trie be built?
    */
   public Table_Zart_Z(boolean useTrie) {
      super(useTrie);
   }

   /**
    * Constructor. Use trie.
    */
   public Table_Zart_Z() {
      this(true);
   }

   /**
    * @param row
    */
   public void add_row(Row_Zart_F row)
   {
      Row_Zart_Z toAdd = new Row_Zart_Z(row);
      rows.add(toAdd);
      
      if (useTrie) this.trie.add(toAdd.getItemset(), toAdd);
   }

   /**
    * Used by BtB.
    * 
    * @param row A Row_Zart_Z object.
    */
   public void add_row(Row_Zart_Z row)
   {
      rows.add(row);
      if (useTrie) this.trie.add(row.getItemset(), row);
   }

   /**
    * @param eq_class A row of the table that represents an equivalence class.
    * @return True, if the eq. class is simple. False, otherwise.
    */
   public static boolean isSimple(Row_Zart_Z eq_class) {
      return (eq_class.isSimple());
   }
}
