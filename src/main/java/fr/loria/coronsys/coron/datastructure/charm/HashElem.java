package fr.loria.coronsys.coron.datastructure.charm;

import java.util.BitSet;

import fr.loria.coronsys.coron.helper.Database;

/** 
 * A class that represents an element in the hash table.
 *  
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class HashElem
implements Comparable<Object>
{
   /**
    * Intent of a concept (an FCI).
    */
   protected BitSet intent;
   
   /**
    * Support of the FCI.
    */
   protected int supp;
   
   /**
    * Number of items in the itemset.
    */
   private int cardinality;

   /**
    * Constructor.
    * 
    * @param intent Intent of a concept (an FCI).
    * @param supp Support of a concept (support of the corresponding FCI).
    */
   public HashElem(BitSet intent, int supp) 
   {
      this.intent = intent;
      this.supp = supp;
      
      this.cardinality = this.intent.cardinality();
   }
   
   /**
    * @return Returns the intent.
    */
   public BitSet getIntent() {
      return this.intent;
   }
   
   /**
    * @return Returns the supp.
    */
   public int getSupp() {
      return this.supp;
   }
   
   public int getCardinality() {
	   return cardinality;
   }
   
   /**
    * String representation of an element.
    * 
    * @return String representation of an element.
    */
   public String toString() {
      return this.intent+" ("+this.supp+") +";
   }
   
   /**
    * This must always be the same as the function toString(). The only
    * difference is that numbers are replaced here by their names.
    * 
    * @return String representation of the row (closure, support, isClosed),
    * where numbers are replaced by their names.
    */
   public String toStringName()
   {
      StringBuilder sb = new StringBuilder();
      
      sb.append(Database.toNamesAttr(this.intent))
        //.append(" x ")
        //.append(Database.toNamesObj(this.getExtent()))
        .append(" (").append(this.supp).append(") +");
      
      return sb.toString();
   }

   /** (non-Javadoc)
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
//   public int compareTo(Object o)
//   {
//      if (o==null || !(o instanceof HashElem)) {
//         throw new ClassCastException();
//      }
//      return (this.intent.toString().compareTo(((HashElem) o).getIntent().toString()));
//   }
   
   public int compareTo(Object o)
   {
      if (o==null || !(o instanceof HashElem)) {
         throw new ClassCastException();
      }
      return (this.cardinality - ((HashElem) o).cardinality);
   }
}
