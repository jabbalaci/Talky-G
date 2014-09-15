package fr.loria.coronsys.coron.datastructure;

import java.util.BitSet;

/**
 * Stores rows of the final answer (closure + support),
 * that is it contains the closure and support of closed frequent itemsets.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Answer extends Table
{
   /**
    * Conctructor. We call the constructor of the super class + we initialise
    * the map structure. 
    */
   Answer() {
      super();
   }

   /**
    * Adds a row to the final list. We only need to store the closure and its support. 
    * 
    * @param closure Closure of the row.
    * @param support Support of the closure in the row.
    */
   @SuppressWarnings("unchecked")
   public void add_row(BitSet closure, int support) {
      this.rows.add(new Row_answer(closure, support, true));
   }

   /**
    * It will add a row to the final list IF its closure is not there yet.
    * This is the function that checks for dupliates.
    * 
    * @param row The row to be added to the final list.
 	*/
   @SuppressWarnings("unchecked")
   public void add_row(Row row) {
      this.rows.add(row);
   }

   /**
    * Determines the number of rows in the final result, that is it determines
    * how many FCIs were found.
    * 
    * @return Number of rows in the final result, that is: number of FCIs found.
    */
   public int getRowNumber() {
      return this.rows.size();
   }
}
