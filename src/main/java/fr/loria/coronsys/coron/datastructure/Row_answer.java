package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;
import java.util.TreeSet;

import fr.loria.coronsys.coron.helper.Database;

/**
 * Class for the rows of the Answer table.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_answer extends Row
{
   /**
    * Shows whether the itemset is closed or not.
    */
   protected boolean closed;
    
    /**
    * Extension part of closed frequent itemsets.
    */
   protected TreeSet<Integer> extent;
    
   /**
    * Constructor to create an empty row. It'll be used when we want to calculate
    * the extents of concepts. In the trie datastructure, when we initialize nodes
    * directly under the root node (these are the itemsets of size 1, that is
    * the attributes) non-terminal nodes will also point on a row, on an empty
    * row first (that's why this constructor). Then we set the extent of this row 
    * int the trie. This is the way we assign the extent to these nodes.
    */
   public Row_answer() {
      super();
   }
   
   /**
    *  Constructor for the rows in the Answer table. Generator field is not needed here.
    * 
    * @param closure Closure of the row.
    * @param support Support of the row.
    */
   public Row_answer(BitSet closure, int support) {
      this.itemset = closure;
      this.supp = support;
      this.closed = true;
   }
   
   /**
    * An alternative constructor that can also set if the itemset
    * is closed or not.
    * 
    * @param itemset An itemset.
    * @param support Support of the itemset.
    * @param closed Is the itemset closed?
    */
   public Row_answer(BitSet itemset, int support, boolean closed) {
      this(itemset, support);
      this.closed = closed;
   }
   
   /**
    * Creates a new Row_answer object (a row in the Answer class) from
    * a Row_fcc_fc object (a row used in the FC tables).
    * 
    * @param row
    */
   public Row_answer(Row_closure row)
   {
      this.itemset = row.getItemset();
      this.supp = row.getSupp();
      this.closed  = true;
   }

   /**
    * Determines the number of attributes in the closure (size of the itemset).
    * 
    * @return Size (number of attributes) of the itemset.
 	*/
   public int getClosureSize() {
      return this.itemset.cardinality();
   }
   
   /**
    * @return Is this itemset closed?
    */
   public boolean isClosed() {
      return this.closed;
   }   
   
   /**
    * Returns the extension part of a concept.
    * 
    * @return Extension part of a concept.
    */
   public TreeSet<Integer> getExtent() {
      return this.extent;
   }
   
   /**
    * Sets the extent part of the row (extent part of a concept).
    * 
    * @param extent The extent we want to set in the row.
    */
   public void setExtent(TreeSet<Integer> extent) {
      this.extent = extent;
   }
   
   /**
    * Sets the value of intent.
    * 
    * @param intent The new intent.
    */
   public void setIntent(BitSet intent) {
      this.itemset = intent;
   }
   
   /**
    * Initializes extent.
    */
   public void initExtent() {
      this.extent = new TreeSet<Integer>();
   }
   
   /**
    * Returns the intension part of a concept.
    * 
    * @return Intension part of a concept (the closed frequent itemset).
    */
   public BitSet getIntent() {
      return this.itemset;
   }
   
   
   /**
    * String representation of the row (closure, support, isClosed).
    * 
    * @return String representation of the row (closure, support, isClosed).
    */
   public String toString() 
   {
      return (this.extent == null)
      		 ? ""+itemset+" ("+supp+")"+(this.closed?" +":"")
      		 : ""+this.getExtent()+" x "+this.getIntent()+";  ("+supp+")"+(this.closed?" +":"");
//      return "ext: "+this.getExtent()+"; int: "+this.getIntent();		 
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
      //
      if (this.extent == null)
         sb.append(Database.toNamesAttr(itemset)).append(" ("+supp+")"+(this.closed?" +":""));
      else
         sb.append(Database.toNamesObj(this.getExtent())).append(" x ")
           .append(Database.toNamesAttr(this.getIntent())).append(";  ("+supp+")"+(this.closed?" +":""));
      //
      return sb.toString();
   }
}
