package fr.loria.coronsys.coron.helper;

import java.util.Properties;

/**
 * Checks the Java version. The software requires Java 1.5.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class CheckJavaVersion
{
   /**
    * For running the software without any problems we need this version at least.
    */
   private final String required = "1.5";
   
   /**
    * Current Java version available. 
    */
   private String current;
   
   /**
    * For accessing environment properties. 
    */
   private Properties prop;
   
   /**
    * JAVA_HOME of the available Java version. 
    */
   private String java_home;
   
   /**
    * Constructor.
    */
   public CheckJavaVersion()
   {
      this.prop = System.getProperties();
      this.current = prop.getProperty("java.specification.version");
      this.java_home = prop.getProperty("java.home");
   }
   
   /**
    * Checks if the available Java version is OK or not.
    * If not, it displays some warning.
    */
   public void check()
   {
      // if we have an older version of Java 
      if (this.current.compareTo(this.required) < 0)
      {
         System.err.println("Warning: this software requires Java "+this.required);
         System.err.println("         your version (" + this.current + ") is older");
         System.err.println("         your Java home: "+this.java_home);
      }
   }

   /**
    * This method can be called statically.
    */
   public static void checkJavaVersion()
   {
      CheckJavaVersion cjv = new CheckJavaVersion();
      cjv.check();
   }
}