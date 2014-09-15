package fr.loria.coronsys.coron.datastructure.pascal;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_bool1;
import fr.loria.coronsys.coron.datastructure.Row_predSupp;
import fr.loria.coronsys.coron.helper.Database;


/**
 * Row for Pascal F table.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_Pascal_F extends Row_bool1
//implements Serializable
{
   /**
    * Eclipse wanted to add it...
    */
   //private static final long serialVersionUID = 1L;

   /**
    * Constructor.
    * 
    * @param itemset Itemset of the row.
    */
   public Row_Pascal_F(BitSet itemset) {
      super(itemset);
   }

   /**
    * Constructor #2.
    */
   public  Row_Pascal_F() {
	   super();
   }
   
   public Row_Pascal_F(Row_predSupp row)
   {
      super(row);
      this.flag = row.flag;
   }

   /**
    * This constructor is used by Eclat-Z.
    * 
    * @param row A frequent itemset.
    * @param key Is it a minimal generator?
    */
   public Row_Pascal_F(Row row, boolean key)
   {
      super(row);
      this.flag = key;    
   }

   /**
    * This constructor is used by Carpathia-G-Rare.
    * 
    * @param row A row representing a rare generator.
    */
   public Row_Pascal_F(Row row)
   {
      super(row);
   }

   /**
    * Is this itemset a key pattern?
    * 
    * @return True, if the itemset is a key pattern.
    */
   public boolean isKey() {
      return this.flag;
   }
   
   /**
    * Change the key state of the itemset.
    * 
    * @param value Set the key state of the itemset to this.
    */
   public void setKey(boolean value) {
      this.flag = value;
   }
   
   /**
    * @return String representation of the row (itemset, pred_supp, key, support).
    */
   public String toString() {
      return ""+this.itemset+" ("+this.supp+") [key: "+(this.flag?"+":"-")+"]";
   }
   
   /**
    * This must always be the same as the function toString(). The only
    * difference is that numbers are replaced here by their names.
    * 
    * @return String representation of the row (itemset, pred_supp, key, support),
    * where numbers are replaced by their names.
    */
   public String toStringName()
   {
      StringBuilder sb = new StringBuilder();
      
      sb.append(Database.toNamesAttr(this.itemset))
        .append(" (").append(this.supp).append(") [key: ")
        .append(this.flag?"+":"-")
        .append("]");
      
      return sb.toString();
   }
}
