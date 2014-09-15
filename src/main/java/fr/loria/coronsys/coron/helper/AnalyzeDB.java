package fr.loria.coronsys.coron.helper;

import java.text.NumberFormat;
import java.util.BitSet;
import java.util.Enumeration;

/** 
 * Analyze the input dataset in a more thorough way.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class AnalyzeDB
{
   final private int   dbLines;
   final private int   dbColumns;
   private boolean[][] matrix;
   private int allAttr;
   
   /**
    * Constructor.
    */
   public AnalyzeDB()
   {
      this.dbLines   = Database.getNumberOfObjects()+1;
      this.dbColumns = Database.getTotalNumberOfAttr()+1;
      this.matrix    = new boolean[this.dbLines][this.dbColumns];
      
      int i;
      BitSet set;
      int line = 1;
      
      for (Enumeration<BitSet> e = Database.getDatabase().elements(); e.hasMoreElements(); )
      {  
         set = e.nextElement();
         for(i=set.nextSetBit(0); i>=0; i=set.nextSetBit(i+1))
         {
            matrix[line][i] = true;
            ++this.allAttr;
         }
         ++line;
      }
      
      Database.freeDatabase();      // not needed anymore
   }
   
   /**
    * Start working.
    */
   public void start()
   {
      NumberFormat fmt_average  = NumberFormat.getInstance();
      fmt_average.setMaximumFractionDigits(2);
      //printMatrix();
      int emptyColumns = getNumberOfEmptyColumns();
      double numberOfObjectsInAvg = (double)this.allAttr / (this.dbColumns - 1); 
      
      
      System.out.println("# Number of empty columns: " + emptyColumns);
      System.out.println("# Number of objects in average: " + (fmt_average.format(numberOfObjectsInAvg)));
   }

   /**
    * @return Number of empty columns.
    */
   private int getNumberOfEmptyColumns()
   {
      int i,j;
      int result = 0;
      int objectsInColumn;
      
      for (i=1; i<dbColumns; ++i)
      {
         objectsInColumn = 0;
         for (j=1; j<dbLines; ++j)
         {
            if (this.matrix[j][i])
               ++objectsInColumn;
         }
         if (objectsInColumn == 0) ++result;
      }
      
      return result;
   }

   /**
    * Show the boolean matrix.
    */
   private void printMatrix()
   {
      int i,j;
      
      for (i=1; i<this.dbLines; ++i)
      {
         for (j=1; j<dbColumns; ++j)
         {
            System.out.print(matrix[i][j]?"1":"0");
            System.out.print(" ");
         }
         System.out.println();
      }
      System.out.println("Number of attributes: "+this.allAttr);
      System.out.println("Number of columns: "+this.dbColumns);
   }
}
