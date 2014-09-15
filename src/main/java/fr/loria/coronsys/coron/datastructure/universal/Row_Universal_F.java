package fr.loria.coronsys.coron.datastructure.universal;

import java.util.BitSet;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_closure;
import fr.loria.coronsys.coron.datastructure.aprioriclose.Row_AprioriClose_F;
import fr.loria.coronsys.coron.datastructure.carpathia.Row_Carpathia_F;
import fr.loria.coronsys.coron.datastructure.pascal.Row_Pascal_F;
import fr.loria.coronsys.coron.datastructure.pascalplus.Row_PascalPlus_F;
import fr.loria.coronsys.coron.datastructure.zart.Row_Zart_F;


/**
 * A universal row data structure for rows of F tables.
 * These F tables contain FIs, but some of them can be marked to be FCI.  
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_Universal_F extends Row
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
    * Vector of minimal generators.
    */
   protected Vector min_gen;
   
   /**
    * Adds a minimal generator.
    * 
    * @param min_gen A minimal generator that we want to add.
    */
   @SuppressWarnings("unchecked")
   public void addMinGen(BitSet min_gen) {
      if (this.min_gen == null) this.min_gen = new Vector();
      this.min_gen.add(min_gen);
   }
   
   /**
    * Returns the minimal generator(s).
    * 
    * @return Returns the minimal generator(s).
    */
   public Vector getMinGen() {
      return this.min_gen;
   }
   
   /**
    * Creates a universal row from a Row object.
    * Algorithms: Apriori.
    * 
    * @param row A Row object.
    */
   public Row_Universal_F(Row row)
   {
      this.itemset = row.getItemset();
      this.supp  	 = row.getSupp();
   }
   
   /**
    * Creates a universal row from a Row_closure object.
    * Algorithms: Close.
    * 
    * @param row A Row_closure object.
    */
   public Row_Universal_F(Row_closure row) {
      this((Row)row);
      this.closed = true;
   }

   /**
    * Creates a universal row from a Row_AprioriClose_F object.
    * Algorithms: Apriori-Close.
    * 
    * @param row A Row_AprioriClose_F object.
    */
   public Row_Universal_F(Row_AprioriClose_F row) {
      this((Row)row);
      this.closed = row.isClosed();
   }
   
   /**
    * Creates a universal row from a Row_PascalPlus_F object.
    * Algorithms: Pascal+.
    * 
    * @param row A Row_PascalPlus_F object.
    */
   public Row_Universal_F(Row_PascalPlus_F row)
   {
      this((Row)row);
      this.closed = row.isClosed();
      this.key = row.isKey();
   }
   
   /**
    * Creates a universal row from a Row_Zart_F object.
    * Algorithms: Zart.
    * 
    * @param row A Row_Zart_F object.
    */
   public Row_Universal_F(Row_Zart_F row)
   {
      this((Row)row);
      this.closed = row.isClosed();
      this.key = row.isKey();
   }
   
   /**
    * Creates a universal row from a Row_Pascal_F object.
    * Algorithms: Zart.
    * 
    * @param row A Row_Zart_F object.
    */
   public Row_Universal_F(Row_Pascal_F row) {
      this((Row)row);
      this.key = row.isKey();
   }
   
   /**
    * Creates a universal row from a Row_Carpathia_F object.
    * Algorithms: Carpathia.
    * 
    * @param row A Row_Carpathia_F object.
    */
   public Row_Universal_F(Row_Carpathia_F row)
   {
      this.itemset = row.closure;
      this.supp = row.getSupp();
      this.addMinGen(row.getItemset());
   }
      
   /**
    * Register that the itemset is closed (it's an FCI).
    */
   public void setClosed() {
      this.closed = true;
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
    * Is the itemset a key (minimal) generator?
    * 
    * @return True, if the itemset is a key (minimal) generator. False, otherwise.
    */
   public boolean isKey() {
      return this.key;
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
			.append("min_gen", this.min_gen)
			.toString();
	}*/
}
