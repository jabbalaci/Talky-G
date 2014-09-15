package fr.loria.coronsys.coron.datastructure.carpathia;

import fr.loria.coronsys.coron.datastructure.pascal.Table_Pascal_FC;

/**
 * Table Pascal FC
 * 
 * - for Frequent
 * - don't add rows to trie immediately! Pascal-Gen() will do that.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_Carpathia_FC extends Table_Pascal_FC
{
   /**
    * Constructor.
    * 
    * @param useTrie Use trie or not?
    */
   public Table_Carpathia_FC(boolean useTrie) {
      super(useTrie);
   }

   /**
    * Constructor.
    */
   public Table_Carpathia_FC() {
      super();
   }
}
