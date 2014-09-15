package fr.loria.coronsys.coron.datastructure.carpathia;

import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_predSupp;
import fr.loria.coronsys.coron.datastructure.pascal.Table_Pascal_F;

/** 
 * Table Pascal F.
 * 
 * - for Frequent
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_Carpathia_F extends Table_Pascal_F
{  
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
      Row_Carpathia_F toAdd = new Row_Carpathia_F(row);
      rows.add(toAdd);
      this.trie.add(toAdd.getItemset(), toAdd);
   }
   
   //ajout d'Edouard
   public static Vector clonerRows(Vector v) {
	   Vector res = new Vector();
	   int taille = v.size();
	   for (int k = 0;k<taille;k++) {
		   Row_Carpathia_F r = (Row_Carpathia_F)v.get(k);
		   res.add(r.clonerC());
	   }
	   return res;
   } 
   
   public Table_Carpathia_F cloner() { 
	   Table_Carpathia_F res = new Table_Carpathia_F();
	   res.trie =  trie;
	   res.useTrie = useTrie;
	   res.rows =  clonerRows(rows);
	   return res;
   }

   /**
    * This constructor is used by Carpathia-G-Rare.
    * 
    * @param row This row represents a rare generator.
    */
   @SuppressWarnings("unchecked")
   public void add_row(Row row)
   {
      Row_Carpathia_F toAdd = new Row_Carpathia_F(row);
      toAdd.setKey(true);
      rows.add(toAdd);
      this.trie.add(toAdd.getItemset(), toAdd);
   }
}
