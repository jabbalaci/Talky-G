package fr.loria.coronsys.coron.datastructure.universal;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.List;

import fr.loria.coronsys.coron.datastructure.Row_str;

/**
 * A universal row data structure for rows of Z tables (that contain FCIs).
 * BitSets are changed to String.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_Universal_Z_str extends Row_str
{
   /**
    * List of minimal generators.
    */
   protected List minGen;
   
   /**
    * Default constructor.
    */
   public Row_Universal_Z_str() {
      super();
   }
      
   /**
    * Constructor. Gets a Row_Universal_Z ibject, and
    * replaces BitSets to Strings. 
    * 
    * @param row A Row_Universal_Z row.
    */
   @SuppressWarnings("unchecked")
   public Row_Universal_Z_str(Row_Universal_Z row)
   {
      super(row);
      if (row.getMinGen() == null) return;
      // else
      if (this.minGen == null) this.minGen = new ArrayList();
      BitSet key;
      for (Enumeration e = row.getMinGen().elements(); e.hasMoreElements(); )
      {
         key = (BitSet) e.nextElement();
         this.minGen.add(key.toString());
      }      
   }
      
   /**
    * Returns the minimal generator(s).
    * 
    * @return Returns the minimal generator(s).
    */
   public List getMinGen() {
      return this.minGen;
   }
   
   /**
    * Sets the minimal generator.
    * 
    * @param list List of minimal generators.
    */
   public void setMinGen(List list) {
      this.minGen = list;
   }
   
   /**
    * Adds a minimal generator.
    * 
    * @param min_gen A minimal generator that we want to add.
    */
   @SuppressWarnings("unchecked")
   public void addMinGen(String min_gen) {
      if (this.minGen == null) this.minGen = new ArrayList();
      this.minGen.add(min_gen);
   }
   
   /**
    * @return String representation of the row (closure, support, minimal generator(s)).
    */
   /*public String toString() {
		return new ToStringBuilder(this)
			.append("itemset", this.itemset)
			.append("support", this.supp)
			.append("min_gen", this.minGen)
			.toString();
	}*/
}
