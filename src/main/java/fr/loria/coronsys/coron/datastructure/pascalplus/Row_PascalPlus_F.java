package fr.loria.coronsys.coron.datastructure.pascalplus;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_predSupp;
import fr.loria.coronsys.coron.datastructure.pascal.Row_Pascal_F;
import fr.loria.coronsys.coron.helper.Database;


/**
 * Row for Pascal F table.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_PascalPlus_F extends Row_Pascal_F
//implements Serializable
{
   /**
    * Eclipse wanted to add it...
    */
   //private static final long serialVersionUID = 1L;

   /**
    * Is this itemset closed?
    */
   private boolean flag2;
   
   /**
    * Constructor.
    * 
    * @param itemset Itemset of the row.
    */
   public Row_PascalPlus_F(BitSet itemset) {
      super(itemset);
      this.flag2 = true;
   }

   /**
    * @param row
    */
   public Row_PascalPlus_F(Row_predSupp row) {
      super(row);
      this.flag2 = true;
   }
   
   /**
    * This constructor is used by Eclat-Z.
    * 
    * @param row A frequent itemset.
    * @param key Is it a minimal generator?
    */
   public Row_PascalPlus_F(Row row, boolean key)
   {
      super(row, key);
      this.flag2 = true;   // by default it is closed. It'll be modified later.
   }

   /**
    * Is this itemset closed?
    * 
    * @return True, if the itemset is closed. False, otherwise.
    */
   public boolean isClosed() {
      return this.flag2;
   }
   
   /**
    * Change the closed state of the itemset.
    * 
    * @param value Set the closed state of the itemset to this.
    */
   public void setClosed(boolean value) {
      this.flag2 = value;
   }

   /**
    * @return String representation of the row (itemset, pred_supp, key, support).
    */
   public String toString() 
   {
      return ""+this.itemset+" ("+this.supp+") [key: "+(this.flag?"+":"-")+"]"
               +(this.flag2?" +":"");
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
        .append("]")
        .append(this.flag2?" +":"");
      
      return sb.toString();
   }
}
