package fr.loria.coronsys.coron.helper;

/** 
 * Class to measure execution times.
 * Similar to ExecutionTime, there is only one difference:
 * 
 * (1) ExecutionTime is static
 * 
 * (2) ExecutionTime2 (this class) must be instanciated
 * 
 * Usage:
 * ======
 * ExecutionTime2 exTime = new ExecutionTime2();
 * exTime.start();
 * ...
 * exTime.stop();
 * System.err.println("Elapsed time: " + exTime.getTime() + " sec.");
 *  
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class ExecutionTime2
{
   /**
    * The moment when we start measuring execution time.
    */
   private long startTime;
   
   /**
    * The moment when we stop measuring execution time. 
    */
   private long endTime;
   
   /**
    * Start measuring execution time.
    * @return start time
    */
   public long start() {
      return (this.startTime = System.currentTimeMillis());
   }
   
   /**
    * @param msg Like start() but it prints some message too.
    */
   public void start(String msg) {
	   System.err.println(msg);
	   this.start();
   }
   
   /**
    * Stop measuring execution time.
    * @return stop time
    */
   public long stop() {
      return (this.endTime = System.currentTimeMillis());
   }
   
   /**
    * Converts the elapsed time to sec and msec.
    * Ex.: 2025 => "2.025", where 2 is sec, .025 is msec.
    * 
    * @return Elapsed time. 
    */
   public String getTime() {
      return (Statistics.getRuntime(this.endTime - this.startTime));
   }
   
   /**
    * @param msec Elapsed time in millisec.
    * @return Converts msec to readable format (sec.msec).
    */
   public String getTime(long msec) {
      return (Statistics.getRuntime(msec));
   }
   
   /**
    * @return Elapsed time in msec.
    */
   public long getTimeMillis() {
      return (this.endTime - this.startTime);
   }

}
