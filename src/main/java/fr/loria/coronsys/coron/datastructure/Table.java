package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.trie.Trie;
import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.Global;

/**
 * Abstract class for the common properties of FCC, FC and Answer 
 * tables.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public abstract class Table
{
   /**
    * It'll contain Row objects.
    */
   protected Vector<Row> rows;
   
   /**
    * A prefix tree (trie) representation of the generators of rows
    * present in the table. So this trie is built on the GENERATORS.
    */
   protected Trie trie;
   
   /**
    * Change attribute numbers to attribute names?
    */
   private boolean name;
   
   /**
    * Should a trie be used? It means: when we add a row to the
    * table, should it be added to the table's trie?
    */
   protected boolean useTrie;

   /**
    * Constructor. We initialise the vector that will contain rows of the table,
    * plus we create a trie.
    * By default trie is built/used.
    */
   public Table() {
      rows = new Vector<Row>();
      this.useTrie = true;
      this.trie    = new Trie();
      this.name    = Global.getExtra().get(C.X_LETTERS);
   }

   /**
    * Constructor.
    * 
    * @param useTrie Use trie or not?
    */
   public Table(boolean useTrie) {
      this();
      this.useTrie = useTrie;
   }

   /**
    * Reinitialises the table. Delete the rows, plus
    * clears the trie. Enables the trie too.
    */
   public void clear() {
      rows.clear();
      this.useTrie = true;
      this.trie    = new Trie();
      //this.trie.clear();		// What's wrong with this? Doesn't want to work :(
   }

   /**
    * Determines the subsets of a set using the trie datastructure.
    * 
    * @param set A set whose subsets we want to get.
    * @return Subsets of the set 'set' present in the trie.
    */
   public Vector<Row> getSubsetsOf(BitSet set) {
      return this.trie.getSubsetsOf(set);
   }

   /**
    * Returns the trie of the table.
    * 
    * @return Trie object of the table.
    */
   public Trie getTrie() {
      return this.trie;
   }

   /**
    * Deletes the trie of the table. When we know we won't need it any more,
    * we can delete it to free up some memory.
    * Disables the trie too, so if we want to add some rows, it won't drop
    * a null pointer exception as it won't trie to add rows to the trie too. 
    */
   public void freeTrie() {
      this.trie = null;
      useTrie(false);
   }

   /**
    * Get the rows in the table.
    * 
    * @return Rows in the table.
    */
   public Vector<Row> getRows() { 
      return this.rows; 
   }
   
   /**
    * Converts the generators of the FC table to a 
    * 2-dimensional Integer array. Example: let {1,4,5}, {1,5,7} be two
    * generators (bitsets) of rows in FC. The function returns the following array:
    * {{1,4,5}, {1,5,7}}. 
    * 
    * @return A 2-dimensional Integer array representation of the generator bitsets in
    * the FC table.
    */
   public int[][] getGenerators()
   {
      int i, k, m;
      Row row;
      BitSet bs;
      int size       = this.getRows().size();
      int[][] result = new int[size][];

      for (k = 0; k<size; ++k)
      {
         row = (Row)this.getRows().get(k);
         bs = row.getItemset();
         result[k] = new int[bs.cardinality()];
         m = 0;
         for (i = bs.nextSetBit(0); i>=0; i = bs.nextSetBit(i+1))
            result[k][m++] = i;
      }

      return result;
   }
   
   /**
    * Creates a candidate generator by concatenation. It receives an int array
    * plus an int number, and the function puts them together and returns the
    * result in a bitset.
    * 
    * @param a An int array.
    * @param b An int number.
    * @return Concatenates the int array and the int number, and returns the result in a bitset.
    */
   public static BitSet createCandidate(int[] a, int b)
   {
      BitSet set = new BitSet();
      
      for (int i = 0; i < a.length; ++i)
         set.set(a[i]);
      set.set(b);
      
      return set;
   }
   
   /**
    * Is the table empty?
    * 
    * @return True, if the table is empty.
    */
   public boolean isEmpty() {
      return this.rows.isEmpty();
   }
   
   /**
    * Get the size of the table (how many rows it has).
    * 
    * @return Number of rows in the table.
    */
   public int size() {
      return this.rows.size();
   }
   
   /**
    * Returns the cardinality of its itemsets. For ex. if we store
    * 3-itemsets (like {ABC}, {ABE}, etc.), then it returns 3.
    * If its empty, it returns 0.
    *  
    * @return Cardinality of its itemsets.
    */
   public int getCardinality()
   {
      if (this.isEmpty()) return 0;
      // else
      Row firstRow = this.getFirstRow();
      return firstRow.getItemset().cardinality();
   }

   /**
    * @return First row of the table. If it's empty, return null.
    */
   public Row getFirstRow()
   {
      if (this.isEmpty()) return null;
      // else
      return ((Row) this.rows.get(0));
   }
   
   /**
    * Print rows of the table. First it collects the table in a 
    * StringBuilder, then it prints the whole table in one piece.
    * 
    * @return String representation of the table (prints its rows).
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      for (Enumeration<Row> e = rows.elements(); e.hasMoreElements(); )
      {
         sb.append(e.nextElement());
         sb.append("\n");
      }
      return sb.toString();
   }
   
   /**
    * Print rows of the table. First it collects the table in a 
    * StringBuilder, then it prints the whole table in one piece.
    * 
    * @return String representation of the table (prints its rows).
    */
   public String toStringName()
   {
      StringBuilder sb = new StringBuilder();
      Row row;
      for (Enumeration<Row> e = rows.elements(); e.hasMoreElements(); )
      {
         row = e.nextElement();
         sb.append(row.toStringName());
         sb.append("\n");
      }
      return sb.toString();
   }
   
   /**
    * Print rows of the table by lines. It prints a line immediately.
    */
   public void toStringDump()
   {
      for (Enumeration<Row> e = rows.elements(); e.hasMoreElements(); )
         System.out.println(e.nextElement());
   }
   
   /**
    * Print rows of the table by lines. It prints a line immediately.
    * Replaces attribute numbers by their names. 
    */
   public void toStringDumpName()
   {
      Row row;
      for (Enumeration<Row> e = rows.elements(); e.hasMoreElements(); )
      {
         row = e.nextElement();
         System.out.println(row.toStringName());
      }
   }
   
   /**
    * Sets whether to use the trie when we add a row or not.
    * 
    * @param useTrie Boolean value indicating whether to use trie or not.
    */
   public void useTrie(boolean useTrie) {
      this.useTrie = useTrie;
   }
}
