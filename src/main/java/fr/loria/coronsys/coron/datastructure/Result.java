package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.close.Table_Close_FC;
import fr.loria.coronsys.coron.datastructure.close.Table_Close_FCC;
import fr.loria.coronsys.coron.datastructure.trie.Trie;
import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.Global;

/**
 * Wraps the Answer table. Facilitates getting the final result (list of FCIs).
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Result
{
   /**
    * An answer table that will store the final result (list of FCIs).
    */
   private Answer answer;
   
   /**
    * Maximal itemset size (number of attributes in the biggest itemset).
 	*/
   private int maxClosureSize;
      
   /**
    * Vector of maps, where maps contain itemsets. In this vector itemsets are
    * ordered by their size (asc or desc).
    */
   private Vector<Map> itemsetV;
   
   /**
    * Contains the max. index of the itemsetV.
 	*/
   private int maxIndex;

   /**
    * Constructor. Initialises the 'answer' table. 
    */
   @SuppressWarnings("unchecked")
   public Result() {
      answer = new Answer();
      this.maxClosureSize = 0;
      this.itemsetV = new Vector<Map>();
      itemsetV.add(null);				// we won't use index 0
      this.maxIndex = 0;
   }
   
   /**
    * @return Number of rows, that is number of found FCIs.
    */
   public int getRowNumber() {
      return this.answer.getRowNumber();
   }

   /** 
    * Copies results from an FC table. The answer table will drop duplicates.
    * 
    * @param fc An FC table containing FCIs.
    */
   @SuppressWarnings("unchecked")
   public void add_fc_closures(Table_Close_FC fc)
   {
   	  if (this.maxIndex < fc.getMaxClosureSize()) expandItemsetV(fc.getMaxClosureSize());
   	  
      Row_closure row;
      BitSet closure;
      Map<BitSet, Row_answer> map;
      
      // we enumerate the rows of the FC table and add each row to the answer table.
      for (Enumeration e = fc.getRows().elements(); e.hasMoreElements(); )
      {
         row = (Row_closure)e.nextElement();
         closure = row.getItemset();
         map = itemsetV.get(closure.cardinality());
         
         if (map.containsKey(closure) == false)
         	map.put(closure, new Row_answer(row));
      }
      if (maxClosureSize < fc.getMaxClosureSize()) maxClosureSize = fc.getMaxClosureSize();
   }

   /**
    * Expands dynamically the itemsetV vector.
    * 
    * @param end The index until (inclusive) we need to expand the itemsetV.
    */
   @SuppressWarnings("unchecked")
   private void expandItemsetV(int end)
   {
      for (int i = this.maxIndex+1; i <= end; ++i)
   	  	this.itemsetV.add(new HashMap());
   	  
   	this.maxIndex = end;
   }
      
   /**
    * Print itemsets. Get the final answer, we were working for this. 
    */
   public void printAnswer() 
   {  
      if ( Global.getExtra().get(C.X_LETTERS) == false )
      {
	      for (Enumeration<Row> e = this.answer.getRows().elements(); e.hasMoreElements(); )
	         System.out.println(e.nextElement());
      }
      else // if we need to replace numbers by their names
      {
         Row_answer row;
         for (Enumeration<Row> e = this.answer.getRows().elements(); e.hasMoreElements(); ) 
         {
            row = (Row_answer) e.nextElement();
	         System.out.println(row.toStringName());
         }
      }
   }
   
   /**
    * Returns the size of the maximal itemset, that is: how many attributes
    * are in the biggest itemset?
    * 
    * @return Size (number of attributes) of the biggest itemset.
 	*/
   public int getMaxClosureSize() {
      return this.maxClosureSize;
   }
   
   /**
    * Returns the Answer object (which contains the FCIs).
    * 
    * @return Answer object containing all the FCIs with their support.
 	*/
   public Answer getAnswer() {
      return this.answer;
   }

   /**
    * Get the rows in the table.
    * 
    * @return Returns the rows in the table (which are actually in its "answer" object).
    */
   public Vector<Row> getRows() {
      return this.answer.getRows();
   }
   
   /**
    * Organizes rows in ascending or descending order.
    * 
    * @param extra A BitSet containing the extra options.
 	*/
   @SuppressWarnings("unchecked")
   public void organizeRows(BitSet extra) 
   {
	  int i;
	  Map map;
	  Iterator it;
	  Vector<Map> traverse = new Vector<Map>();
	  
	  if (extra.get(C.X_ASC))
	  {
	     for (i = 1; i<= this.maxIndex; ++i)
	     	traverse.add(this.itemsetV.get(i));
	  }
	  else // if (extra.get(C.X_DESC))
	  {
	     for (i = this.maxIndex; i >= 1; --i)
	        traverse.add(this.itemsetV.get(i));
	  }
   	
	  for (Enumeration<Map> e = traverse.elements(); e.hasMoreElements(); )
	  {
	     map = e.nextElement();
	     for (it = map.values().iterator(); it.hasNext(); )
            this.answer.add_row((Row) it.next());
	     map.clear();			// delete, we don't need it any more
	  }
	  
	  this.itemsetV = null;		// free up memory, we don't need it any more
   }

   /**
 	* Generates all frequent itemsets from closed frequent itemsets.
 	* FCI => FI.
 	*/
   public void generateFIs() 
   {
      Map<BitSet, Row> curr, prev;
      Iterator<Row> it;
      Row row;
      boolean verbose = Global.getVerbosity().get(C.V_FUNCTION);
   	
	   for (int i = this.maxIndex; i > 1; --i)
	   {
	  	   if (verbose) System.err.println("Start: generating FIs from FCIs of size "+i);
	  	   //
	      curr = this.itemsetV.get(i);
	      prev = this.itemsetV.get(i-1);
	     
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
   private void populateFIs(BitSet fci, final int support, Map<BitSet, Row> prev) 
   {
      BitSet subset;
      Row found;
   	
      for (Enumeration e = Table_Close_FCC.get_i_subsets(fci).elements(); e.hasMoreElements(); )
      {
      	 subset = (BitSet) e.nextElement();
      	 if ((found = prev.get(subset)) == null)
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
    * Prints the GraphViz .dot representation of the trie of FCIs to
    * a file.
    * 
    * @return True, if the output was written successfully; else, false.
    * 
    */
   public boolean printTrieOfIntents()
   {
      Trie trie = new Trie();
      Row_answer row;
      // filling the trie
      for (Enumeration<Row> e = this.getRows().elements(); e.hasMoreElements(); )
      {
         row = (Row_answer) e.nextElement();
         trie.add(row.getIntent(), row);
      }
      
      return trie.writeDot("tmp", "trie.dot");
   }
}
