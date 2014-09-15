/*
 * Created on Nov 18, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fr.loria.coronsys.coron.helper;

/**
 * @author szathmar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Memory
{
   /**
    * To access memory usage.
    */
   private Runtime rt;
   
   /**
    * Maximal memory usage during the runtime of the program.
    */
   private long maxMemUsage;
   
   /**
    * Constructor.
    */
   public Memory() {
      this.rt = Runtime.getRuntime(); 
      this.maxMemUsage = -1;
   }
   
   /**
    * Gets the maximal memory usage until now. 
    * 
    * @return Returns the maximal memory usage until now in bytes.
    */
   public long getMaxMemUsage() {
      return this.maxMemUsage;
   }
   
   /**
    * Prints memory usage information.
    * 
    * @param pos Where we call it: at the beginning or at the end of a function.
    */
   public void print_mem_info(int pos)
   {
      System.err.println("-----------------------------------------------");
      switch (pos)
      {
         case C.MEM_START:
            System.err.println("Memory info at the beginning of the loop:");
            break;
         case C.MEM_END:
            System.err.println("Memory info at the end of the loop:");
            break;
         default:
            break;
      }
//      System.err.println("totalMemory in bytes: " + this.rt.totalMemory());
//      System.err.println("freeMemory in bytes: "  + this.rt.freeMemory());

      this.testMemUsage();
      
      if (pos == C.MEM_MAX)
      {
         System.err.println("Maximal memory usage: " + Convert.byteToPrettyString(this.maxMemUsage) + " bytes");
         System.err.println("-----------------------------------------------");
      }
      else
      {
         System.err.println("Used memory at the moment: " + 
            Convert.byteToPrettyString(this.rt.totalMemory() - this.rt.freeMemory()) + " bytes");
         System.err.println("Maximal memory usage until now: " + Convert.byteToPrettyString(this.maxMemUsage) + " bytes");
         System.err.println("-----------------------------------------------");
         if (pos == C.MEM_END) System.err.println(">>");
      }
   }

   /**
    * Determines memory usage of the program at the given moment. 
    * 
    * @return Returns the current memory usage.
    */
   public long testMemUsage() {
      long memUsage = this.rt.totalMemory() - this.rt.freeMemory();
      if (memUsage > this.maxMemUsage) this.maxMemUsage = memUsage;
      
      return memUsage;
   }
   
   /**
    * Determines a garbage collected memory usage.
    * 
    * @return Collects garbage, then determines the memory usage.
    */
   public long getGCMemUsage() {
      System.gc();
      return this.testMemUsage();
   }
   
   /**
    * This function returns the absolute available memory. If the JVM was started
    * with -Xmx1024m , then it takes it into account too.
    * 
    * @return Returns the available memory.
    */
   public long getFreeMemory() 
   {
      long max = this.rt.maxMemory();
      long used = this.rt.totalMemory() - this.rt.freeMemory();
      return max-used;
   }
}
