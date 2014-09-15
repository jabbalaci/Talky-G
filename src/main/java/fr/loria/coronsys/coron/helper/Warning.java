package fr.loria.coronsys.coron.helper;

/**
 * Class to handle warnings.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Warning
{
   /**
    * Empty private constructor. The class cannot be instantiated.
    */
   private Warning() { }
   
   /** 
    * Print a warning message to stderr.
    * 
    * @param msg Warning message.
    */
   public static void warning(String msg)
   {
      int length = msg.length();
      StringBuilder sb = new StringBuilder();
      
      for (int i = 0; i < length + (2 * 4); ++i) {
         sb.append('#');
      }
      
      System.err.println(sb);
      System.err.println("##  " + msg + "  ##");
      System.err.println(sb);
   }
   
}
