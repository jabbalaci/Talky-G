package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.pascal.Row_Pascal_F;
import fr.loria.coronsys.coron.helper.Database;

/**
 * Row for table Pascal FC.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_predSupp extends Row_Pascal_F
//implements Serializable
{
   /**
    * Eclipse wanted to add it...
    */
   //private static final long serialVersionUID = 1L;
   
   /**
    * pred_supp value, i.e. minimum support of the subsets of the current itemset
    */
   private int pred_supp;
   
   /**
    * Constructor.
    * @param itemset
    */
   public Row_predSupp(BitSet itemset) {
      super(itemset);
      // it should be positive infinity, but it's enough to assign a value
      // that is definitively larger than any possible support values
      this.flag = true;
      this.pred_supp = Database.getDatabase().size() + 1;
   }
   
   /**
    * Gets the value of pred_supp.
    * 
    * @return Returns the pred_supp.
    */
   
   public int getPredSupp() {
      return pred_supp;
   }
   
   /**
    * Sets the value of pred_supp.
    * 
    * @param pred_supp The pred_supp to set.
    */
   public void setPredSupp(int pred_supp) {
      this.pred_supp = pred_supp;
   }
   
   /**
    * @return Returns the flag.
    */
   public boolean getFlag() {
      return this.flag;
   }
   
   /**
    * @return String representation of the row (itemset, pred_supp, key, support).
    */
   public String toString() 
   {
      return ""+this.itemset+" ("+this.supp+") [pred_supp: "+this.pred_supp+", key: "
               +(this.flag?"+":"-")+"]";
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
        .append(" (").append(this.supp).append(") [pred_supp: ").append(this.pred_supp)
        .append(", key: ").append(this.flag?"+":"-")
        .append("]");
      
      return sb.toString();
   }
}
