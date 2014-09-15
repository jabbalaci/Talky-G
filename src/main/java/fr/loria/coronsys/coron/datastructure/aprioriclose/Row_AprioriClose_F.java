package fr.loria.coronsys.coron.datastructure.aprioriclose;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_bool1;
import fr.loria.coronsys.coron.helper.Database;

/**
 * Class for rows that need a boolean value.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_AprioriClose_F extends Row_bool1
//implements Serializable
{
   /**
    * Eclipse wanted to add it...
    * @param row
    */
   //private static final long serialVersionUID = 1L;
   
   Row_AprioriClose_F(BitSet row) {
      super(row);
      this.flag = true;
   }
   
   Row_AprioriClose_F(Row row) {
      super(row);
      this.flag = true;
   }
   
   /**
    * Is this itemset closed?
    * 
    * @return True, if the itemset is closed.
    */
   public boolean isClosed() {
      return this.flag;
   }
   
   /**
    * Change the closed state of the itemset.
    * 
    * @param value Set the closed state of the itemset to this.
    */
   public void setClosed(boolean value) {
      this.flag = value;
   }
   
   /**
    * @return String representation of the row (itemset, support, isClosed).
    */
   public String toString() {
      return ""+this.itemset+" ("+this.supp+")"+(this.flag?" +":"");
   }
   
   /**
    * This must always be the same as the function toString(). The only
    * difference is that numbers are replaced here by their names.
    * 
    * @return String representation of the row (itemset, support, isClosed),
    * where numbers are replaced by their names.
    */
   public String toStringName()
   {
      StringBuilder sb = new StringBuilder();
      
      sb.append(Database.toNamesAttr(this.itemset))
        .append(" (").append(this.supp).append(")")
        .append(this.flag?" +":"");
      
      return sb.toString();
   }
}
