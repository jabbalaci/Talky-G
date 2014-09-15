package fr.loria.coronsys.coron.helper.extra;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

import fr.loria.coronsys.coron.helper.Database;

/**
 * Converts the context in .pbm format. It's a very simple graphical format.
 * Used for the visualization of the database. You can open it with Gimp.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class PbmWriter
{
   /**
    * The dataset that we want to convert.
    */
   private Vector<BitSet> database;

   /**
    * Constructor.
    * 
    * @param database The database that is to be converted.
    */
   public PbmWriter(Vector<BitSet> database) {
      this.database  = database;
   }
   
   /**
    * Let's do the work.
    */
   public void start()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("P1").append("\n");    // magic number
      sb.append(Database.getTotalNbOfNonEmptyAttr()).append(" ")        // width
        .append(Database.getNumberOfObjects()).append("\n");   // height
      
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
            if (oneLine[i]) sb.append("1 ");
            else            sb.append("0 ");
         }
         System.out.println(sb);
      }
   }
}
