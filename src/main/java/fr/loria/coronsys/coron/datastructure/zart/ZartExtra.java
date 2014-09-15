package fr.loria.coronsys.coron.datastructure.zart;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

import fr.loria.coronsys.coron.helper.Database;

/**
 * Class for extra things for Zart.
 * Like: determining the support of 1-patterns.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
*/
public class ZartExtra
{
   /**
    * The dataset in horizontal format.
    */
   private Vector database;
   
   /**
    * Minimum support.
    */
   private int min_supp;
      
   /**
    * Constructor.
    * 
    * @param database The dataset in horizontal format.
    * @param min_supp Minimum support.
    */
   public ZartExtra(Vector database, int min_supp) 
   {
      this.database     = database;
      this.min_supp     = min_supp;
   }
   
   /**
    * Calculates the support of 1-patterns and stores them in a 1-dimensional array.
    * 
    * @return Frequent 1-patterns stored in a 1-dimensional array.
    */
   public int[] getF1Matrix()
   {
      int max = Database.getTotalNumberOfAttr();
      int[] matrix = new int[max+1];
      
      BitSet set;
      int attr;
      for (Enumeration e = this.database.elements(); e.hasMoreElements(); )
      {
         set = (BitSet) e.nextElement();
         for (attr=set.nextSetBit(0); attr>=0; attr=set.nextSetBit(attr+1)) 
            ++matrix[attr];
      }
      
      //printF1Matrix(matrix);

      return matrix;
   }
   
   /**
    * Prints the values of the F_1 matrix. For debug purposes.
    * 
    * @param matrix The 1-dimensional matrix that we want to display.
    */
   private void printF1Matrix(int[] matrix)
   {  
      for (int i=0; i<matrix.length; ++i)
      {
         System.err.print(matrix[i]+", ");
      }
      System.err.println();
   }
   
   /**
    * Calculates the support of 2-patterns and stores them in an upper-triangular matrix.
    * 
    * @return Support of 2-patterns stored in a triangular matrix.
    */
   public int[][] getF2Matrix()
   {
      int max = Database.getTotalNumberOfAttr();
      int[][] matrix = new int[max][];
      
      for (int i = 0; i < matrix.length; ++i)
         matrix[i] = new int[max-i];
      
      BitSet set;
      int first, second;
      for (Enumeration e = this.database.elements(); e.hasMoreElements(); )
      {
         set = (BitSet) e.nextElement();
         if (set.cardinality() < 2) continue;
         for (first=set.nextSetBit(0); first>=0; first=set.nextSetBit(first+1)) 
         { 
            for (second = set.nextSetBit(first+1); second>=0; second=set.nextSetBit(second+1))
               ++matrix[first][max-second];
         }
      }
      
      return matrix;
   }
   
   /**
    * Prints the values of the F_2 matrix. For debug purposes.
    * 
    * @param matrix The triangular matrix we want to display.
    */
   private void printF2Matrix(int[][] matrix)
   {
      int i, j;
      
      for (i=0; i<matrix.length; ++i)
      {
         for (j=0; j<matrix[i].length; ++j)
         {
            System.err.print(matrix[i][j]+", ");
         }
         System.err.println();
      }
      System.err.println();
   }
}
