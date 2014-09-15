package fr.loria.coronsys.coron.helper.extra;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

import fr.loria.coronsys.coron.helper.Database;

/**
 * Converts the context in .ppm format. It's a very simple graphical format.
 * Used for the visualization of the database. You can open it with Gimp
 * and you can even convert it with "convert" (see the ImageMagick package).
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class PpmWriter
{
   /**
    * RGB colors of white.
    */
   private final String WHITE = "255\n255\n255\n";
   
   /**
    * RGB colors of black.
    */
   private final String BLACK = "0\n0\n0\n";
   
   /**
    * The dataset that we want to convert.
    */
   private Vector<BitSet> database;

   /**
    * Constructor.
    * 
    * @param database The database that is to be converted.
    */
   public PpmWriter(Vector<BitSet> database) {
      this.database  = database;
   }
   
   /**
    * Let's do the work.
    */
   public void start()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("P3").append("\n");    // magic number
      sb.append("# Original dataset: " + Database.getDatabaseAbsoluteFilePath()).append("\n");  // remark
      sb.append(Database.getTotalNbOfNonEmptyAttr()).append(" ")           // width
        .append(Database.getNumberOfObjects()).append("\n");      // height
      sb.append("255").append("\n");                              // largest color value
      
      System.out.print(sb);

      /*
       * OK, let's not keep everything in memory. Process one line, and
       * write it.
       */
      int i;
      BitSet set;
      boolean oneLine[] = new boolean[Database.getTotalNumberOfAttr()+1];
      
      for (Enumeration<BitSet> e = this.database.elements(); e.hasMoreElements(); )
      {  
         sb = new StringBuilder();
         for (i=0; i<oneLine.length; ++i) oneLine[i]=false;     // re-init
         
         set = e.nextElement();
         for(i=set.nextSetBit(0); i>=0; i=set.nextSetBit(i+1)) {
            oneLine[i] = true;
         }
         
         for (i=1; i<oneLine.length; ++i)           // position 0 is not used 
         {
            if (oneLine[i]) sb.append(BLACK);     // '1' in the binary matrix 
            else            sb.append(WHITE);     // '0' in the binary matrix
         }
         System.out.print(sb);
      }
   }
}
