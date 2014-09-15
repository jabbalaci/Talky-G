package fr.loria.coronsys.coron.datastructure;

//import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class for the common properties of different rows.
 * BitSets are changed to String.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_str 
{  
   /**
    * For saving in a database.
    */
   protected Integer	id;
   
   /**
    * Itemset.
    */
   protected String itemset;
   
   /**
    * Support of the itemset.
    */
   protected int supp;
      
   /**
    * Default constructor.
    */
   public Row_str() {
   }
   
   /**
    * Constructor.
    * 
    * @param row A Row object.
    */
   public Row_str(Row row) {
      this.itemset = row.itemset.toString();
      this.supp    = row.supp;
   }
   
   /**
    * Gets the ID. Needed for Hibernate.
    * 
    * @return Returns the ID of the row.
    */
   public Integer getId() {
		return id;
	}

	/**
	 * Sets the ID. Needed for Hibernate.
	 * 
	 * @param id New ID of the row.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
   
   /** 
    * Get the itemset.
    * 
    * @return Itemset.
    */
   public String getItemset() { 
   	  return this.itemset; 
   }

   /** 
    * Sets the itemset.
    * 
    * @param set The new itemset.
    */
   public void setItemset(String set) { 
      this.itemset = set;
   }

   /**
    *  Get the support.
    * 
    * @return Support.
    */
   public int getSupp() { 
   	  return this.supp; 
   }
   
   /**
    * Sets the support of the itemset.
    * 
    * @param supp New support of the itemset.
 	 */
   public void setSupp(int supp) {
   	  this.supp = supp;
   }
   
   /**
    * String representation of the row.
    * 
    * @return String representation of the row.
    */
   /*public String toString() {
		return new ToStringBuilder(this)
			.append("id", this.id)
			.append("itemset", this.itemset)
			.append("support", this.supp)
			.toString();
	}*/
}
