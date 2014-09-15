package fr.loria.coronsys.coron.datastructure.universal;

import java.util.ArrayList;
import java.util.List;

import fr.loria.coronsys.coron.datastructure.Row_str;

/**
 * A universal row data structure for rows of F tables.
 * These F tables contain FIs, but some of them can be marked to be FCI.  
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_Universal_F_str extends Row_str
{
   /**
    * Is the itemset closed? Is it an FCI?
    */
   protected boolean closed;
   
   /**
    * Is the itemset a key (minimal) generator?
    */
   protected boolean key;
 
   /**
    * List of minimal generators.
    */
   protected List minGen;   
      
   /**
    * Default constructor.
    */
   public Row_Universal_F_str() {
      super();
   }
     
   /**
    * @param row
    */
   public Row_Universal_F_str(Row_Universal_F row) {
      super(row);
      this.closed = row.closed;
      this.key = row.key;
   }
   
   /**
    * Register that the itemset is closed (it's an FCI).
    */
   public void setClosed() {
      this.closed = true;
   }
   
   /**
    * Set the itemset closed or not.
    * 
    * @param closed Is the itemset closed?
    */
   public void setClosed(boolean closed) {
      this.closed = closed;
   }
   
   /**
    * Is the itemset closed? Is it an FCI?
    * 
    * @return True, if the itemset is closed. False, otherwise.
    */
   public boolean isClosed() {
      return this.closed;
   }
   
   /**
    * Register that the itemset is a key (minimal) generator.
    */
   public void setKey() {
      this.key = true;
   }
   
   /**
    * Sets if the itemset is key.
    * 
    * @param key Is the itemset key?
    */
   public void setKey(boolean key) {
      this.key = key;
   }
   
   /**
    * Is the itemset a key (minimal) generator?
    * 
    * @return True, if the itemset is a key (minimal) generator. False, otherwise.
    */
   public boolean isKey() {
      return this.key;
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
    * @return String representation of the row 
    * (itemset, support, closed, key, minimal generator(s)).
    */
   /*public String toString() {
		return new ToStringBuilder(this)
			.append("itemset", this.itemset)
			.append("support", this.supp)
			.append("closed", this.closed)
			.append("key", this.key)
			.append("min_gen", this.minGen)
			.toString();
	}*/
}
