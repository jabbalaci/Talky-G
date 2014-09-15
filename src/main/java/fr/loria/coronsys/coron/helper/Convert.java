package fr.loria.coronsys.coron.helper;

/**
 * Class to do different conversations.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Convert
{
   /**
    * Empty private constructor. The class cannot be instantiated.
    */
   private Convert() { }
   
   /** 
    * Converts a long number to a prettily formatted string (ex.: 1234567 => "1,234,567").
    * 
    * @param number A number as a long (integer), ex.: 1234567
    * @return The number as a prettily formatted string, ex.: "1,234,567"
    */
   public static String byteToPrettyString(long number)
   {
      StringBuilder sb = (new StringBuilder(""+number)).reverse();
      StringBuilder copy = new StringBuilder();

      int length = sb.length();
      for (int i=0; i<length; ++i)
      {
         if ((i%3==0) && (i!=0)) copy.append(',');
         copy.append(sb.charAt(i));
      }

      return copy.reverse().toString();
   }
}
