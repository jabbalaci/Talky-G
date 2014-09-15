package fr.loria.coronsys.coron.algorithm;

import fr.loria.coronsys.coron.datastructure.Result;
import fr.loria.coronsys.coron.datastructure.Result2;

/** 
 * All algorithms need to implement this interface to ensure
 * that we can ask the result from it.
 *  
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public interface Algorithm
{
   /**
    * Gets a Result object through which we can ask the result.
    * 
    * @return A Result object that holds the final result.
    */
   Result getResult();
   
   /**
    * Gets a Result2 object (needed for AssRuleX).
    * 
    * @return A Result2 object with the final result.
    */
   Result2 getResult2();
   
   /**
    * Suppress stdout.
    */
   void setToNull();

   /**
    * How many FIs were found?
    * 
    * @return Number of found FIs.
    */
   long getFiCnt();
   
   /**
    * How many FCIs were found?
    * 
    * @return Number of found FCIs.
    */
   long getFciCnt();
}
