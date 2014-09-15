package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.close.Table_Close_FCC;
import fr.loria.coronsys.coron.datastructure.universal.Row_Universal_F;
import fr.loria.coronsys.coron.datastructure.universal.Row_Universal_Z;
import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.Global;

/**
 * Wraps the Answer table. Facilitates getting the final result (list of FCIs).
 * DOES NOT CHECK DUPLICATES! For Close it can be a problem!
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Result2
{
   /**
    * Universal F rows of F tables are stored here.
    */
   private Vector<Row> rowsF;
   
   /**
    * Universal Z rows of Z tables are stored here.
    */
   private Vector<Row> rowsZ;
   
   /**
    * Maximal itemset size (number of attributes in the biggest itemset).
 	*/
   private int maxClosureSize;
      
   /**
    * Vector of maps, where maps contain itemsets. In this vector itemsets are
    * ordered by their size (asc or desc).
    */
   private Vector<Row> itemsetV;
   
   /**
    * Contains the max. index of the itemsetV.
 	*/
   private int maxIndex;

   /**
    * Constructor. Initialises the 'answer' table. 
    */
   @SuppressWarnings("unchecked")
   public Result2() {
      this.maxClosureSize = 0;
      this.itemsetV = new Vector();
      itemsetV.add(null);				// we won't use index 0
      this.maxIndex = 0;
      this.rowsF = new Vector();
      this.rowsZ = new Vector();
   }
   
   /**
    * @return Number of F rows.
    */
   public int getFSize() {
      return this.rowsF.size();
   }
   
   /**
    * @return Number of Z rows.
    */
   public int getZSize() {
      return this.rowsZ.size();
   }  
   
   /**
    * Returns the size of the maximal itemset, that is: how many attributes
    * are in the longest itemset?
    * 
    * @return Size (number of attributes) of the longest itemset.
 	*/
   public int getMaxClosureSize() {
      return this.maxClosureSize;
   }
   
   /**
    * Get the F rows.
    * 
    * @return Returns the F rows.
    */
   public Vector<Row> getFRows() {
      return this.rowsF;
   }
   
   /**
    * Get the Z rows.
    * 
    * @return Returns the Z rows.
    */
   public Vector<Row> getZRows() {
      return this.rowsZ;
   }   

   /**
 	* Generates all frequent itemsets from closed frequent itemsets.
 	* FCI => FI.
 	*/
   public void generateFIs() 
   {
      Map curr, prev;
      Iterator<Row> it;
      Row row;
      boolean verbose = Global.getVerbosity().get(C.V_FUNCTION);
   	
	   for (int i = this.maxIndex; i > 1; --i)
	   {
	  	   if (verbose) System.err.println("Start: generating FIs from FCIs of size "+i);
	  	   //
	      curr = (Map) this.itemsetV.get(i);
	      prev = (Map) this.itemsetV.get(i-1);
	     
	      for (it = curr.values().iterator(); it.hasNext(); )
	      {
	     	   row = it.next();
	         populateFIs(row.getItemset(), row.getSupp(), prev);
	      }
	      //
	      //if (verbose) System.err.println("End:   generating FIs from FCIs of size "+i);
	   }
   }   

   /**
    * Takes an FCI and finds all the one-sized smaller FIs that can be generated from it. 
    * 
    * @param fci The FCI whose one-size smaller FIs we are looking for.
    * @param support Support of the FCI.
    * @param prev A Map with itemsets of ose-size smaller than the FCI.
    */
   @SuppressWarnings("unchecked")
   private void populateFIs(BitSet fci, final int support, Map prev) 
   {
      BitSet subset;
      Row found;
   	
      for (Enumeration e = Table_Close_FCC.get_i_subsets(fci).elements(); e.hasMoreElements(); )
      {
      	 subset = (BitSet) e.nextElement();
      	 if ((found = (Row) prev.get(subset)) == null)
      	 {
      	 	prev.put(subset, new Row_answer(subset, support, false));
      	 }
      	 else
      	 {
      	    if (found.getSupp() < support) found.setSupp(support);
      	 }
      }
   }

   /**
    * Registers a universal F row.
    * 
    * @param row A Universal F Row.
    */
   @SuppressWarnings("unchecked")
   public void add(Row_Universal_F row) {
      this.rowsF.add(row);
   }

   /**
    * Registers a universal Z row.
    * 
    * @param row A Universal Z Row.
    */
   @SuppressWarnings("unchecked")
   public void add(Row_Universal_Z row) {
      this.rowsZ.add(row);
   }
   
   /**
    * Prints the F rows.
    */
   public void printF()
   {
      Row_Universal_F row;
      
      for (Enumeration<Row> e = this.rowsF.elements(); e.hasMoreElements(); )
      {
         row = (Row_Universal_F) e.nextElement();
         System.err.println(row);
      }
   }
   
   /**
    * Prints the Z rows.
    */
   public void printZ()
   {
      Row_Universal_Z row;
      
      for (Enumeration<Row> e = this.rowsZ.elements(); e.hasMoreElements(); )
      {
         row = (Row_Universal_Z) e.nextElement();
         System.err.println(row);
      }
   }

   /**
    * Copy rows (no duplicates) from Result to here.
    * 
    * @param res Rows in Result without any duplicates.
    */
   public void copyFrom(Result res)
   {
      Row row;
      
      for (Enumeration<Row> e = res.getAnswer().rows.elements(); e.hasMoreElements(); )
      {
         row = e.nextElement();
         this.add(new Row_Universal_F(row));
      }
   }

   /**
    * Sort Z rows.
    * It is here because of Charm. FCIs are stored in Z rows, and they 
    * might be needed to be ordered in ascending order by the length of itemsets.
    */
   @SuppressWarnings("unchecked")
   public void sortZ() {
      Collections.sort(this.rowsZ);
   }
}
