package fr.loria.coronsys.coron.datastructure;

import java.util.Vector;

/**
 * Class for rows that need to store things in a Vector
 * (ex.: collecting minimal generators).
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_mingen extends Row
//implements Serializable
{
   /**
    * Eclipse wanted to add it...
    */
   //private static final long serialVersionUID = 1L;
   
   /**
    * A boolean value.
    */
   private Vector min_gen;
   
   /**
    * Constructor.
    */
   public Row_mingen() {
      super();
      this.min_gen = new Vector();
   }
   
   /**
    * Gets the list of minimal generators.
    * 
    * @return Returns the min_gen.
    */
   public Vector getMinGen() {
      return min_gen;
   }
   
   /**
    * Sets the list of minimal generators.
    * 
    * @param min_gen The min_gen to set.
    */
   public void setMinGen(Vector min_gen) {
      this.min_gen = min_gen;
   }
}
