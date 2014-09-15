package fr.loria.coronsys.coron.helper;

/** 
 * Class to measure execution times.
 * 
 * Usage:
 * ======
 * 
 * ExecutionTime.start();
 * ...
 * ExecutionTime.stop();
 * System.err.println("Elapsed time: " + ExecutionTime.getTime() + " sec.");
 *  
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class ExecutionTime
{
   /**
    * The moment when we start measuring execution time.
    */
   private static long startTime;
   
   /**
    * The moment when we stop measuring execution time. 
    */
   private static long endTime;
   
   /**
    * Start measuring execution time.
    * @return start time
    */
   public static long start() {
      return (ExecutionTime.startTime = System.currentTimeMillis());
   }
   
   /**
    * Stop measuring execution time.
    * @return stop time
    */
   public static long stop() {
      return (ExecutionTime.endTime = System.currentTimeMillis());
   }
   
   /**
    * Converts the elapsed time to sec and msec.
    * Ex.: 2025 => "2.025", where 2 is sec, .025 is msec.
    * 
    * @return Elapsed time. 
    */
   public static String getTime() {
      return (Statistics.getRuntime(ExecutionTime.endTime - ExecutionTime.startTime));
   }
   
   /**
    * @param msec Elapsed time in millisec.
    * @return Converts msec to readable format (sec.msec).
    */
   public static String getTime(long msec) {
      return (Statistics.getRuntime(msec));
   }
   
   /**
    * @return Elapsed time in msec.
    */
   public static long getTimeMillis() {
      return (ExecutionTime.endTime - ExecutionTime.startTime);
   }
}
