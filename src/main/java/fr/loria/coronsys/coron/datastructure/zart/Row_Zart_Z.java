package fr.loria.coronsys.coron.datastructure.zart;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.charm.HashElem;
import fr.loria.coronsys.coron.datastructure.pascalplus.Row_PascalPlus_Z;
import fr.loria.coronsys.coron.helper.Database;

/**
 * Class for the common properties of different rows.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Row_Zart_Z extends Row_PascalPlus_Z
//implements Serializable
{
   /**
    * Minimal generator(s) of the FCI.
    */
   private Vector<BitSet> min_gens;
   
   /**
    * Empty constructor. Used by the BtB algorithm.
    * @param supp Support.
    * @param closure Closure.
    */
   public Row_Zart_Z(BitSet closure, int supp) {
      super(closure, supp);
      this.min_gens = new Vector<BitSet>();
   }
   
   /**
    * @param row
    */
   public Row_Zart_Z(Row_Zart_F row) {
      super(row);
      this.min_gens = new Vector<BitSet>();
   }
   
   /**
    * @return Returns the min_gens.
    */
   @SuppressWarnings("unchecked")
   public Vector<BitSet> getMinGens() 
   {
      if (this.min_gens.isEmpty()) {
         Vector v = new Vector();
         v.add(this.itemset);
         return v;
      }
      // else
      return min_gens;
   }
   
   /**
    * @param min_gens The min_gens to set.
    */
   public void setMinGens(Vector<BitSet> min_gens) {
      this.min_gens = min_gens;
   }
   
   /**
    * Adds a minimal generator to the list. 
    * We will make a reference only to the row's itemset,
    * which is the minimal generator, not to the whole row.
    * 
    * @param min_gen A minimal generator of the current FCI.
    */
   @SuppressWarnings("unchecked")
   public void addMinGen(BitSet min_gen) {
      this.min_gens.add(min_gen);
   }
   
   /**
    * @return String representation of the row (closure, support, minimal generator(s)).
    */
   public String toString() 
   {
      //return ""+this.itemset+" ("+this.supp+") + [min_gens: "+this.getMinGens()+"]";
      return ""+this.itemset+" ("+this.supp+") +; "+this.getMinGens();
      //return ""+this.itemset+" ("+this.supp+") + [min_gens: "+this.min_gens+"]";
   }
   
   /**
    * This must always be the same as the function toString(). The only
    * difference is that numbers are replaced here by their names.
    * 
    * @return String representation of the row (closure, support, min_gens),
    * where numbers are replaced by their names.
    */
   public String toStringName()
   {
      StringBuilder sb = new StringBuilder();
      
      sb.append(Database.toNamesAttr(this.itemset))
        .append(" (").append(this.supp).append(") +; ")
        .append(Database.toNamesAttr(this.getMinGens()));
      
      return sb.toString();
   }

   /**
    * @return True, if the eq. class (that is represented by this row) is simple. False,
    * otherwise.
    */
   public boolean isSimple()
   {  
      /*
       * A class is simple if it only has one generator AND
       * the closure of the class is identical with this generator.
       */
      return ( (this.min_gens.size() == 1) && this.itemset.equals((BitSet)this.min_gens.get(0)) );
   }

   /**
    * Fill the generator list. Used by TUS.
    * 
    * @param genVector A Vector of HashElems.
    */
   public void addMinGens(Vector<HashElem> genVector)
   {
      for (Enumeration<HashElem> e = genVector.elements(); e.hasMoreElements(); ) {
         this.addMinGen( (e.nextElement()).getIntent() );
      }
   }
}
