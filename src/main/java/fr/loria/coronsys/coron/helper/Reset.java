package fr.loria.coronsys.coron.helper;

/**
 * Goal: re-initialise the state of the system. If you launch Coron's
 * algorithms in a loop, it must be called.
 * 
 * Reason: in Coron we use some static classes that hold the current
 * state of the system and everybody can access it. During the execution,
 * the state changes, but since they are static, they must be reset if
 * we want to perform a new execution!
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Reset
{
   /**
    * Empty private constructor. The class cannot be instantiated.
    */
   private Reset() { }
   
   /**
    * Let's call all necessary reset functions.
    */
   public static void reset()
   {
      Database.reset();
      Global.reset();
   }
}
