package fr.loria.coronsys.coron.datastructure.carpathiag;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.Row_predSupp;
import fr.loria.coronsys.coron.datastructure.pascal.Row_Pascal_F;
import fr.loria.coronsys.coron.helper.Database;

/**
 * Row for Pascal F table.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_CarpathiaG_F extends Row_Pascal_F
{
   /**
    * Closure of the minimal generator.
    */
   public BitSet closure;
   
   /**
    * @param row
    */
   public Row_CarpathiaG_F(Row_predSupp row)
   {
      super(row);
      this.flag = row.getFlag();
      this.closure = null;
   }
   
   /**
    * Constructor. Used in Eclat-G.
    * 
    * @param itemset Itemset.
    * @param supp Support.
    */
   public Row_CarpathiaG_F(BitSet itemset, int supp)
   {
      super(itemset);
      this.supp = supp;
   }

   /** 
    * Get the closure of the row.
    * 
    * @return Closure (as a bitset) of the row.
    */
   public BitSet getClosure() { 
        return this.closure; 
   }

   /** 
    * Sets the closure of a row.
    * 
    * @param set The new closure of the row.
    */
   public void setClosure(BitSet set) { 
      if (this.closure == null) this.closure = new BitSet();
      this.closure.clear();
      this.closure.or(set);
   }
   
   /**
    * @return String representation of the row (itemset, pred_supp, key, support).
    */
   public String toString() 
   {
      return ""+this.itemset+" ("+this.supp+") [key: +]";
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
        .append(" (").append(this.supp).append(") [key: +]");
      
      return sb.toString();
   }
}
