package fr.loria.coronsys.coron.datastructure.charm;

import java.text.NumberFormat;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.Convert;
import fr.loria.coronsys.coron.helper.SetOp;

/** 
 * A class that represents a hash table to store FCIs.
 * New: 
 *    20050324 - hash value of a node is stored inside the node
 *               this value is calculated at construction
 *               Consequence: as it's calculated only once => faster! 
 *  
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class MyHash
{
   /**
    * Constant for 'find superset'.
    */
   public final static int SUPERSET = 1;
   
   /**
    * Constant for 'find subset'.
    */
   public final static int SUBSET = 2;
   
   /**
    * FCIs are stored in this hash table. An element of the hash table
    * is a list (vector), so if two objects have the same hash value, they are 
    * stored in the same list.
    */
   private Vector<Vector<HashElem>> hashTable;
   
   /**
    * Number of slots in the hash table.
    * Use "-v=f" option to see info about the hash table. If the number of
    * elements in an occupied slot (list) is too much, then increase the
    * value of slots. 
    * 
    * TODO: Maybe it should be changeable through a command-line
    * option too. 
    */
   private int slots;
   
   /**
    * Constructor.
    */
   public MyHash() {
      this.hashTable = new Vector<Vector<HashElem>>();
      this.slots = C.HASH_SIZE;
   }
   
   /**
    * Gets the hash table.
    * 
    * @return Returns the hash table.
    */
   public Vector<Vector<HashElem>> getHashtable() {
      return this.hashTable;
   }

   /**
    * Prints some additional information about the hash table.
    */
   public void printInfo()
   {
      int occupied = 0, all = 0;
      Vector<HashElem> v;
      
      for (Enumeration<Vector<HashElem>> e = this.hashTable.elements(); e.hasMoreElements(); )
      {
         v = e.nextElement();
         if (v != null) {
            ++occupied;
            all += v.size();
         }
      }
      NumberFormat avg = NumberFormat.getInstance();
      avg.setMinimumFractionDigits(2); avg.setMaximumFractionDigits(2);
      System.err.println("  Hashtable: size = "+Convert.byteToPrettyString(this.size()));
      System.err.println("  Hashtable: occupied slots = "+Convert.byteToPrettyString(occupied));
      System.err.println("  Hashtable: avg. number of elems in occupied slots = "+avg.format((double)all/(double)occupied));
   }

   /**
    * Gets the size of the hash table.
    * 
    * @return The size of the hash table.
    */
   public long size() {
      return this.hashTable.size();
   }

   /**
    * Registers a node in the hash table.
    * 
    * @param node The node (an FCI) that we want to register.
    */
   public void add(ITnode node)
   {
      int sizeV, j;
      int index = node.getHash();
   
      // increase the size of the hash table dynamically 
      if ((sizeV = this.hashTable.size()) < (index+1)) {
         for (j=0; j<(index+1-sizeV); ++j) this.hashTable.add(null);
      }
      
      Vector<HashElem> pos = this.hashTable.get(index);
         if (pos==null) { pos = new Vector<HashElem>(); }
      pos.add(new HashElem(node.getIntent(), node.getSupp()));
      
      this.hashTable.set(index, pos);
   }

   /**
    * Tells if the hash table contains the superset of a node.
    * 
    * Example: we stored CWAT x 135, but later we find CTW x 135. They
    * have the same support, but since CTW already has a proper superset registered here,
    * we don't have to register CTW too. That is: we check if an itemset has
    * a registered superset here with the same support.
    * 
    * @param node The node whose superset we are looking for in the hash table.
    * @return True, if the hash table already contains a superset of the node. False, otherwise.
    */
   public boolean containsSupersetOf(ITnode node)
   {
      final int index = node.getHash();
      int sizeV, j;
      
      // increase the size of the hash table dynamically
      if ((sizeV = this.hashTable.size()) < (index+1)) 
      {
         for (j=0; j<(index+1-sizeV); ++j) {
            this.hashTable.add(null);
         }
         return false;
      }
      
      Vector<HashElem> pos = this.hashTable.get(index);
      if (pos == null) {
         return false;
      }
      // else
      HashElem elem;
      int supp = node.getSupp();
      BitSet intent = node.getIntent();
      
      for (Enumeration<HashElem> e = pos.elements(); e.hasMoreElements(); )
      {
         elem = e.nextElement();
         if (elem.supp == supp)
         {
            if (SetOp.isProperSubset(intent, elem.intent)) {
               return true;
            }
         }
      }
      return false;
   }
   
   /**
    * Tells if the hash table contains the superset (or a subset) of a node.
    * This is the same as the function 'containsSupersetOf' just right above,
    * but now we have the option to choose between superset or subset lookup.
    * 
    * @param node
    * @param supersetOrSubset
    * @return
    */
   public boolean containsSupersetOrSubsetOf(ITnode node, final int supersetOrSubset)
   {
      final int index = node.getHash();
      int sizeV, j;
      
      // increase the size of the hash table dynamically
      if ((sizeV = this.hashTable.size()) < (index+1)) 
      {
         for (j=0; j<(index+1-sizeV); ++j) {
            this.hashTable.add(null);
         }
         return false;
      }
      
      Vector<HashElem> pos = this.hashTable.get(index);
      if (pos == null) {
         return false;
      }
      // else
      HashElem elem;
      int supp = node.getSupp();
      BitSet intent = node.getIntent();
      
      for (Enumeration<HashElem> e = pos.elements(); e.hasMoreElements(); )
      {
         elem = e.nextElement();
         
         if (elem.supp == supp)
         {
            // looking for a proper superset in the list
            if (supersetOrSubset == MyHash.SUPERSET) {
               if (SetOp.isProperSubset(intent, elem.intent)) {
                  return true;
               }
            }
            else // if (supersetOrSubset == MyHash.SUBSET), i.e. looking for a proper subset in the list
            {
               if (SetOp.isProperSuperset(intent, elem.intent)) {
                  return true;
               }
            }
         }
      }
      
      return false;
   }
   
   /**
    * We have a node, for instance DJ x 1. We are interested if it has
    * a superset registered with the same support (like CDJ x 1). If yes, 
    * then return the vector that contains its superset, otherwise return null.
    * We need the vector because the node may have more supersets with the same
    * support! 
    * 
    * @param node The node whose superset we are looking for in the hash table.
    * @return Container of the superset of the node. If the node has no superset
    * registered, return null.
    */
   public Vector<HashElem> getSupersetContainerOf(ITnode node)
   {
      final int index = node.getHash();
      int sizeV, j;
      
      // increase the size of the hash table dynamically
      if ((sizeV = this.hashTable.size()) < (index+1)) 
      {
         for (j=0; j<(index+1-sizeV); ++j) {
            this.hashTable.add(null);
         }
         return null;
      }
      
      Vector<HashElem> container = this.hashTable.get(index);
      if (container == null) {
         return null;
      }
      
      // else
      HashElem elem;
      int supp = node.getSupp();
      BitSet intent = node.getIntent();
      
      for (Enumeration<HashElem> e = container.elements(); e.hasMoreElements(); )
      {
         elem = (HashElem) e.nextElement();
         if (elem.supp == supp)
         {
            if (SetOp.isProperSubset(intent, elem.intent)) {
               return container;
            }
         }
      }
      return null;
   }
   
   /**
    * We have a registered elem in the hash. We thought that it
    * was a frequent generator but we eventually found a subset of it
    * with the same support. !!! More subsets may be registered !!!
    * Thus we delete ALL these supersets and we register the subset.
    * 
    * @param elemContainer The vector that contains the supersets.
    * @param node The subset that we want to store instead.
    * @return How the number of elements changed (it can be 0 or negative).
    */
   public int replace(Vector<HashElem> elemContainer, ITnode node) 
   {
      Vector<HashElem> container = this.hashTable.get(node.getHash());
      HashElem elem;
      int supp = node.getSupp();
      BitSet intent = node.getIntent();
      Vector<HashElem> toDelete = new Vector<HashElem>();
      
      for (Enumeration<HashElem> e = container.elements(); e.hasMoreElements(); )
      {
         elem = e.nextElement();
         if (elem.supp == supp)
         {
            if (SetOp.isProperSubset(intent, elem.intent)) {
               toDelete.add(elem);
            }
         }
      }

      container.removeAll(toDelete);
      this.add(node);
      
      return (0 - toDelete.size() + 1);
   }
   
   /**
    * @return String representation of the hash.
    */
   public String toString()
   {
      Vector<HashElem> v;
      StringBuilder sb = new StringBuilder();
      final int size = this.hashTable.size();
      
      for (int i = 0; i < size; ++i)
      {
         v = this.hashTable.get(i);
         if ((v != null) && (v.isEmpty() == false)) 
         {
            sb.append(i).append(") ").append(v).append("\n");
         }
      }
      
      return sb.toString();
   }
}
