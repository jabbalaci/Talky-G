package fr.loria.coronsys.coron.datastructure.charm;

import java.util.BitSet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.Database;

/**
 * Class for the IT-nodes in the IT-tree.
 * Used by: Charm, Eclat, etc.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
*/
public class ITnode
implements Comparable<Object>
{
   /**
    * The IT-tree to which this node belongs to.
    */
   private ITtree itTree;
   
   /**
    * Intent (itemset) of a concept. Ex.: in CWAx1345 "CWA" is the intent.
    */
   private BitSet intent;
   
   /**
    * Extent (tidset) of a concept. Ex.: in CWAx1345 "1345" is the extent.
    */
   private BitSet extent;
   
   /**
    * Support of the itemset (support of the intent). 
    * It is equal to the size of the extent. 
    * Ex.: the support of the concept CWAx1345 is 4, since size(1345)=4.  
    */
   private int supp;
   
   /**
    * Pointer to the vector that contains the children of the current node.
    */
   private Vector<ITnode> children;
   
   /**
    * Children of a node are stored in a container.
    * This is a reference to that container. This way if we only have a reference
    * to a node whose parent has already been deleted (Eclat breadth-first), then
    * we can still enumerate the neighbor nodes in the same container.
    */
   private Vector<ITnode> container;
   
   /**
    * Hash value of the node.
    */
   private int hash;
   
   /**
    * Is the hash value of the node stored? I.e. is the "hash" variable set?
    */
   private boolean isHashSet;
   
   /**
    * Is it a minimal generator?
    */
   private boolean key;
   
   /**
    * Is this node closed?
    */
   private boolean closed;
   
   /**
    * Distance of the node from the root. Root's level is O.
    * Direct children of root are at level 1, etc.
    */
   private int level;
   
   /**
    * Closure of the itemset. Used by Flake.
    * newInCoron1
    */
   private BitSet closure;
   
   /**
    * Pointer to parent1. Used by Flake.
    * newInCoron1
    */
   private ITnode parent1;
   
   /**
    * Pointer to parent2. Used by Flake.
    * newInCoron1
    */
   private ITnode parent2;
   
   /**
    * Is the current node an FG-candidate?
    * Required for Flake v4. 
    */
   private boolean isCandidate = false;
   
   /************************************************************************/
   
   /**
    * Constructor. 
    * 
    * @param itTree The IT-tree to which this node belongs to.
    */
   public ITnode(ITtree itTree)
   {
      this.itTree    = itTree;
      this.children  = new Vector<ITnode>();
      this.closed    = itTree.closed;
      this.key       = itTree.key;
   }
   
   /************************************************************************/

   /**
    * Constructor. Used to create the DIRECT children of root.
    * These nodes are all at level 1.
    * 
    * @param itTree The IT-tree to which this node belongs to.
    * @param attr A 1-long attribute.
    * @param extent The tidset (column) that belongs to the attribute.
    */
   public ITnode(ITtree itTree, int attr, BitSet extent)
   {
      this(itTree);
      this.setIntent(attr);
      this.setExtent(extent);
      this.setSupp(extent.cardinality());
      //this.setLevel(1);
   }
      
   /**
    * This constructor is just for test purposes.
    * It should NOT be used in a real project.
    * 
    * @param set
    * @param supp
    */
   public ITnode(BitSet set, int supp)
   {
      this.intent = set;
      this.supp = supp;
   }

   /**
    * Adds a child to the node.
    * 
    * @param child The child that we want to add.
    */
   public void addChild(ITnode child)
   {
      // on
      //System.err.println(">>> added: "+child);
      // off
      this.children.add(child);
      this.itTree.incNodeCnt();
      child.container = this.children;
      child.setLevel(this.level + 1);
   }

   /**
    * Get the vector of children.
    * 
    * @return Pointer to the vector of children.
    */
   public Vector<ITnode> getChildren() {
      return children;
   }
   
   /**
    * Sets the vector of children.
    * 
    * @param children The new vector that contains children nodes of the current node.
    */
   public void setChildren(Vector<ITnode> children) {
      this.children = children;
   }
   
   /**
    * @return Returns the extent.
    */
   public BitSet getExtent() {
      return extent;
   }
   
   /**
    * @param extent The extent to set.
    */
   public void setExtent(BitSet extent) {
      this.extent = extent;
   }
   
   /**
    * Deletes the extent part. Used by Eclat-S and Sabre. 
    */
   public void freeExtent() {
      this.extent = null;
   }

   /**
    * Calculates a hash value for an IT-node.
    * 
    * Very important! It must return the same value for itemsets
    * describing the same objects!
    * 
    * @param extent The extent of the IT-node.
    * @return Hash value of the IT-nodes.
    */
   public static int calculateHash(BitSet extent)
   {
      int sum = 0;
      
      for (int attr = extent.nextSetBit(0); attr >= 0; attr = extent.nextSetBit(attr+1))
      {
         //sum += (attr * 5381 + 13 * sum);
         sum += attr;   // original one
      }
        
      return (Math.abs(sum) % C.HASH_SIZE);
   }
   
   /**
    * @return Returns the intent.
    */
   public BitSet getIntent() {
      return intent;
   }
   
   /**
    * @param intent The intent to set.
    */
   public void setIntent(BitSet intent) {
      this.intent = intent;
   }
      
   /**
    * @return Returns the supp.
    */
   public int getSupp() {
      return supp;
   }
   
   /**
    * @param supp The supp to set.
    */
   public void setSupp(int supp) {
      this.supp = supp;
   }
   
   /**
    * Gets the number of an attribute, and sets it as the intent.
    * 
    * @param i The number of an attribute that is to be added to the intent.
    */
   private void setIntent(int i)
   {
      this.intent = new BitSet();
      this.intent.set(i);
   }

   /**
    * Get the IT-tree to which this node belongs to.
    * 
    * @return Returns the itTree.
    */
   public ITtree getItTree() {
      return this.itTree;
   }

   /**
    * Adds a set to the intent part recursively (modifies the whole subtree of the node).
    * 
    * @param otherIntent The intent part of the other node.
    */
   public void replaceAll(BitSet otherIntent)
   {
      BitSet toAdd = (BitSet) this.intent.clone();		// add := this.intent 
      toAdd.or(otherIntent);									// add := this.intent union other.intent
      toAdd.xor(this.intent);									// add -= this.intent
      this.addRecursively(toAdd);
   }

   /**
    * Adds a set to the intent part recursively (modifies the whole subtree of the node).
    * 
    * @param toAdd The set that we want to add to the nodes of the subtree.
    */
   private void addRecursively(BitSet toAdd)
   {
      this.intent.or(toAdd);
      for (Enumeration<ITnode> e = this.children.elements(); e.hasMoreElements(); )
         (e.nextElement()).addRecursively(toAdd);
   }
   
   /**
    * Sorts the children vector of a node in increasing order of their support.
    */
   public void sortChildren() {
      Collections.sort(children);
   }

   /**
    * Deletes the current node.
    */
   public void delete() {
      this.container.remove(this);
      this.itTree.decNodeCnt();
   }
   
   /**
    * Remove all the direct children of the node.
    */
   public void deleteChildren()
   {
      /*for (Enumeration<ITnode> e = this.children.elements(); e.hasMoreElements(); ) {
         e.nextElement().delete();
      }*/
      this.itTree.decNodeCnt(this.children.size());
      this.children.clear();
      this.children = null;
   }

   /** (non-Javadoc)
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo(Object o)
   {
      if (o==null || !(o instanceof ITnode))
         throw new ClassCastException();
      return this.supp - ((ITnode)o).supp;
   }
   
   /**
    * In which container is this element stored?
    * 
    * @return Returns the container.
    */
   public Vector<ITnode> getContainer() {
      return container;
   }

   /**
    * @return Returns the hash.
    */
   public int getHash() 
   {
      if (this.isHashSet == false) 
      {
         this.hash   = ITnode.calculateHash(this.extent);
         this.isHashSet = true;
      }
      return this.hash;
   }
   
   /**
    * Calculates the hash value of a node which is below level 1, i.e.
    * the level value of the node > 1. It means that this node stores 
    * diffsets (and not tidsets).
    * In this case, the hash of the current node is calculated as follows:
    * hash value of the parent minus the sum of the attributes in
    * the current node's diffset.
    * Example: parent: A x 2345 with hash = 4. Current node AD x 2.
    * Sum of the attributes in the diffset of AD is 2, thus hash of
    * AD is 4 - 2 = 2.
    * 
    * Attention! It's not that simple as it seems, since the substraction
    * may result in a negative number, and in this case the hash must be
    * cyclic! For instance, let the size of the hash be (n = 5).
    * 1 - 0 = 1
    * 1 - 1 = 0
    * 1 - 2 = 4
    * 1 - 3 = 3
    * 1 - 4 = 2
    * =========
    * 1 - 5 = 1
    * 1 - 6 = 0
    * 1 - 7 = 4  (!!! not -1, but 4 again)
    * 1 - 8 = 3
    * ...
    * 
    * Let x be the first number and let y be the second number.
    * (1) First y <- y % n. That is, by doing a modulo n
    * on the second number, we fall back to a simpler case
    * where y <= n.
    * (2) Then we compute (x - y). Since it can be a negative
    * number, we add n to it. If it wasn't negative, then it became > n.
    * So let's modulo it by n, and thus we get a hash value in [0; n[.
    * 
    * @param x Hash value of the parent.
    */
   public void calculateHashByDiffsets(final int x) 
   {
      int y = (ITnode.calculateHash(this.extent) % C.HASH_SIZE);
      this.hash = ( ((x - y) + C.HASH_SIZE) % C.HASH_SIZE);
      
      this.isHashSet = true;
   }

   /**
    * @return Returns the key.
    */
   public boolean isKey() {
      return this.key;
   }
   
   /**
    * @param key The key to set.
    */
   public void setKey(boolean key) {
      this.key = key;
   }

   /**
    * @return Returns the closed.
    */
   public boolean isClosed() {
      return closed;
   }

   /**
    * @param closed The closed to set.
    */
   public void setClosed(boolean closed) {
      this.closed = closed;
   }
   
   /**
    * String representation of the node.
    * 
    * @return String representation of the node.
    */
   public String toString() 
   {
      switch (this.itTree.algo)
      {
         case C.ALG_CHARM:
            return this.intent+" ("+this.supp+") +";
            //break;
         case C.ALG_ECLAT:
            return this.intent+" ("+this.supp+")"+(this.closed?" +":"");
            //break;
         case C.ALG_ECLAT_Z:
            return ""+this.intent+" ("+this.supp+") [key: "+(this.key?"+":"-")+"]";
            //break;
         case C.ALG_ECLAT_G:
            return this.intent+" ("+this.supp+")"+" [key: +]";
         case C.ALG_WALKY_G:
         case C.ALG_TALKY_G:
            return this.intent+" ("+this.supp+")";
         default:
            break;
      }
      // it should never arrive here normally!
      System.err.println("Warning! The value "+this.itTree.algo+" is not treated in coron.datastructure.charm.ITnode.toString()");
      return null;      
   }
   
   /**
    * This must always be the same as the function toString(). The only
    * difference is that numbers are replaced here by their names.
    * 
    * @return String representation of the row (closure, support, isClosed),
    * where numbers are replaced by their names.
    */
   public String toStringName()
   {
      StringBuilder sb = new StringBuilder();
      
      sb.append(Database.toNamesAttr(this.getIntent()))
        .append(" (").append(this.supp).append(")");
      
      switch (this.itTree.algo)
      {
         case C.ALG_ECLAT_G:
            sb.append(" [key: +]");
            break;
         case C.ALG_WALKY_G:
            break;
         default:
            break;
      }
      // on: debug
      //sb.append(" level:"+this.level+" ");
//      sb.append(" x "+this.extent+" ");
      //sb.append(" hash: "+this.hash+" ");
      // off: debug
      
        //.append(" x ")
        //.append(Database.toNamesObj(this.getExtent()))
      sb.append(this.closed ? " +" : "");
      
      return sb.toString();
   }

   /**
    * @param level Level of the node (its distance from the root).
    */
   public void setLevel(int level) {
      this.level = level;
   }
   
   /**
    * @return Level of the node (its distance from the root).
    */
   public int getLevel() {
      return this.level;
   }
   
   /**
    * @return Closure of the itemset. Used by Flake.
    * newInCoron1
    */
   public BitSet getClosure() {
      return closure;
   }

   /**
    * Sets the closure of the itemset. Used by Flake.
    * 
    * @param closure Closure of the itemset.
    * newInCoron1
    */
   public void setClosure(BitSet closure) {
      this.closure = closure;
   }
   
   /**
    * @return First parent of the node.
    * newInCoron1
    */
   public ITnode getParent1() {
      return parent1;
   }

   /**
    * @param parent1 First parent of the node.
    * newInCoron1
    */
   public void setParent1(ITnode parent1) {
      this.parent1 = parent1;
   }

   /**
    * @return Second parent of the node.
    * newInCoron1
    */
   public ITnode getParent2() {
      return parent2;
   }

   /**
    * @param parent2 Second parent of the node.
    * newInCoron1
    */
   public void setParent2(ITnode parent2) {
      this.parent2 = parent2;
   }
   
   public void setCandidateStatus(boolean isCandidate) {
	   this.isCandidate = isCandidate;
   }
   
   public boolean getCandidateStatus() {
	   return this.isCandidate;
   }
   
}
