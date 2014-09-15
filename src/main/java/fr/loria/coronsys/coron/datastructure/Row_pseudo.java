package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;

import fr.loria.coronsys.coron.datastructure.pascalplus.Row_PascalPlus_F;
import fr.loria.coronsys.coron.helper.Database;

/**
 * Class for rows that contain:
 * - itemset
 * - closure of the itemset
 * - support of the itemset
 * - is the itemset pseudo-closed?
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_pseudo extends Row_closure
//implements Serializable
{
   /**
    * Eclipse wanted to add it...
    */
   //private static final long serialVersionUID = 1L;
   
   /**
    * Is the itemset pseudo-closed?
    */
   private boolean pseudo;

   /**
    * Constructor. Calls the constructor of the superclass +
    * initialises the pseudo field. 
    * @param row 
    */
   public Row_pseudo(Row_PascalPlus_F row) 
   {
      super(row);
      this.setClosure(null);
      this.pseudo = true;
   }

   /**
    * Constructeur pour le cas de l'ensemble vide.
    */
   public Row_pseudo() 
   {
      super();
	  this.setClosure(null);
	  this.pseudo = true;
   }

   /** 
    * Is it a pseudo-closed itemset?
    * 
    * @return The pseudo field of the row.
    */
   public boolean isPseudo() { 
   	  return this.pseudo; 
   }
   
   /**
    * Set the state of the itemset: is it pseudo or not.
    * 
    * @param isPseudo State of the itemset.
    */
   public void setPseudo(boolean isPseudo) {
       this.pseudo = isPseudo;
   }
   
   /**
    * A detailed String representation of the row, including the pseudo too.
    * 
    * @return String representation of the row (generator, closure, support).
    */
   public String toString() {
      return ""+itemset+"; ("+supp+") [pseudo-closed: "+(pseudo?"+":"-")+"; closure: "+itemset2+"]";
   }
   
   /**
    * Replace attribute numbers with their names.
    * 
    * @return String representtion of the row.
    */
   public String toStringName()
   {
      StringBuilder sb = new StringBuilder();
      
      sb.append(Database.toNamesAttr(this.itemset))
        .append(" (").append(this.supp).append(") ")
        .append("[pseudo-closed: "+(pseudo?"+":"-")+"; ")
        .append("closure: "+Database.toNamesAttr(this.itemset2)).append("]");
      
      return sb.toString();
   }

   /**
    * @return Closure of the itemset.
    */
   public BitSet getClosure() {
      return this.itemset2;
   }

   /**
    * Set the closure of the itemset.
    * 
    * @param closure Closure of the itemset.
    */
   public void setClosure(BitSet closure) {
      this.itemset2 = closure;
   }
}
