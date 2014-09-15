package fr.loria.coronsys.coron.datastructure.close;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_answer;
import fr.loria.coronsys.coron.datastructure.Row_closure;

/**
 * Represents FC tables (closed frequent itemsets).
 * Rows present in these objects will already be part of the final result.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Table_Close_FC extends Table_Close_FCC_FC
{
   /**
    * A map object which is used to avoid duplicates in the table. We don't want to
    * store rows with the same closure twice or more, just once.
    */
   private Map map;
   
   /**
    * Size of the maximal (number of attributes) closure.
 	 */
   private int maxClosureSize;

   /**
    * Constructor. We call the constructor of the super class + 
    * we initialise the map object.
    */
   public Table_Close_FC() {
      super();
      map  = new HashMap();
      this.maxClosureSize = 0;
   }

   /**
    * Reinitialises the object. This way if we need another object of this
    * kind, we don't need to recreate a new object but to reinitialise an already
    * existing one.
    */
   public void clear() {
      super.clear();
      map.clear();
      this.maxClosureSize = 0;
   }

   /**
    * Adds a row to the table and guarantees that there will be
    * no duplicates of closures. Additionally it sets the itemsetSize
    * field, which is an Integer and shows the size of the closure. Storing 
    * this value in an Integer is faster than always calling the appropriate
    * method of the object representing the closure!
    * 
    * @param row The row to be added.
    */
   @SuppressWarnings("unchecked")
   public void add_row(Row row) 
   {         
      BitSet closure = row.getItemset();
      
      if (this.map.containsKey(closure) == false)
      {
         super.add_row(row); 
         this.map.put(closure, row);
         
      	 int closureSize = closure.cardinality();
         if (this.maxClosureSize < closureSize) this.maxClosureSize = closureSize;
      }
   }

   /**
    * It returns the final result formatted as the original C++
    * version. It exists mainly for test purposes to compare the output
    * of the two programs. 
    * 
    * @return List of FCIs formatted as in the original C++ version. 
 	*/
   public String toStringAsClose() 
   {
      StringBuilder sb = new StringBuilder();
      Row row;
      for (Enumeration e = rows.elements(); e.hasMoreElements(); )
      {
         row = (Row)e.nextElement();
         sb.append(row.getItemset()+" ("+row.getSupp()+")");
         sb.append("\n");
      }
      return sb.toString();
   }

   /**
    * Returns the map built on the closures of rows.
    * 
    * @return The map built on the closures of rows.
    */
   public Map getMap() {
      return this.map;
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
      Row_closure row;
      BitSet bs;
      int size       = this.getRows().size();
      int[][] result = new int[size][];

      for (k = 0; k<size; ++k)
      {
         row = (Row_closure)this.getRows().get(k);
         bs = row.getGenerator();
         result[k] = new int[bs.cardinality()];
         m = 0;
         for (i = bs.nextSetBit(0); i>=0; i = bs.nextSetBit(i+1))
            result[k][m++] = i;
      }

      return result;
   }
   
   /**
    * Get the size of the maximal (number of attributes) closure.
    * 
    * @return Size of the maximal (number of attributes) closure.
    */
   public int getMaxClosureSize() {
   	  return this.maxClosureSize;
   }
   
   /**
    * Prints FCi immediately to the STDOUT.
    * This way we don't store partial results (FC_i tables) in the memory.
    * It's a possible solution to avoid "out of memory" errors.
    * @param to_dev_null
    * 
    * @return Returns the size of the FC_i table.
    */
   public int dump(boolean to_dev_null)
   {
      if (to_dev_null == false)		// if the output is not redirected to /dev/null (-null option)
      {
	      Row_closure row;
	      for (Enumeration e = this.rows.elements(); e.hasMoreElements(); )
	      {
	         row = (Row_closure) e.nextElement();
	         System.out.println(new Row_answer(row));
	      }
	      System.out.println();
      }
      
      return this.rows.size();
   }
   
   /**
    * Print rows of the table by lines. It prints a line immediately.
    */
   public void toStringDump()
   {
      for (Enumeration e = rows.elements(); e.hasMoreElements(); )
         //System.out.println(e.nextElement());
         System.out.println(new Row_answer((Row_closure) e.nextElement()));
   }
}
