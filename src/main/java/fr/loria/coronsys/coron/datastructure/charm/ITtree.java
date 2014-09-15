package fr.loria.coronsys.coron.datastructure.charm;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.Database;

/**
 * Class for the IT-tree in the CHARM algorithm.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
*/
public class ITtree
{
   /**
    * Root pointer.
    */
   private ITnode root;
   
   /**
    * Counts the number of nodes in the search tree.
    */
   private int nodeCnt;
   
   /**
    * What kind of nodes are stored in the tree? FCIs or FIs?
    * The default is: FCIs, as this class was first made for Charm.
    * For Eclat this variable has to be false.
    */
   protected boolean closed;
   
   /**
    * Are the elements minimal generators? For Charm and Eclat
    * it has to be false by default. For Eclat-S and Sabre it has to be
    * true, and they will modify it as needed. 
    */
   protected boolean key;
   
   /**
    * Which algorithm uses this IT-tree?
    */
   protected int algo;
   
   /**
    * Constructor.
    * 
    * @param algo Which algorithm calls this constructor?
    */
   public ITtree(final int algo)
   {  
      this.root = new ITnode(this);
      this.root.setIntent(new BitSet());
      // its extent contains all objects by definition, but we don't store it to save memory
      this.root.setExtent(new BitSet());
      this.root.setSupp(Database.getNumberOfObjects());
      this.root.setLevel(0);
      
      this.nodeCnt   = 1;				   // there is 1 node, the root
      this.algo      = algo;
      
      switch (algo)
      {
         case C.ALG_CHARM:
            this.closed = true;           // the nodes are closed
            this.key    = false;          // we don't know this in the case of Charm
            break;
         case C.ALG_ECLAT:
            this.closed = false;          // not closed. We don't know which are closed.
            this.key    = false;          // no information about it
            break;
         case C.ALG_ECLAT_Z:
            this.closed = false;
            this.key    = true;
            break;
         case C.ALG_SABRE:
            this.closed = true;
            this.key    = true;
            break;
         case C.ALG_ECLAT_G:
            this.closed = false;          // no information about it
            this.key    = true;           // the empty set is always a key
            break;
      }
   }
   
   /**
    * Get the root node.
    * 
    * @return The root node of the tree.
    */
   public ITnode getRoot() {
      return this.root;
   }

   /**
    * Traverses the IT-tree in depth first order.
    * For debug purposes.
    * 
    * @param node Current node.
    */
   public void depthFirstTraversal(ITnode node)
   {
      if (node!=null)
      {
         System.err.println(node.toStringName());
         
         for (Enumeration<ITnode> e = node.getChildren().elements(); e.hasMoreElements(); )
            this.depthFirstTraversal((ITnode) e.nextElement());
      }
   }
   
   /**
    * Traverses the IT-tree in breadth first order.
    * For debug purposes.
    * 
    * @param node Current node.
    */
   @SuppressWarnings("unchecked")
   public void breadthFirstTraversal(ITnode node)
   {
      Vector nodes = new Vector();
      ITnode first;
      nodes.add(node);
      
      while (nodes.size() > 0)
      {
         first = (ITnode) nodes.get(0);
         System.err.println(first.toStringName());
         nodes.remove(0);
         nodes.addAll(first.getChildren());
      }
   }

   /**
    * Get the number of nodes in the tree.
    * 
    * @return Number of nodes in the tree.
    */
   public int size() {
      return this.nodeCnt;
   }

   /**
    * Decrease the number of nodes with the number of deleted nodes.
    * 
    * @param deleted Number of deleted nodes.
    */
   public void decNodes(int deleted) {
      this.nodeCnt -= deleted;
   }

   /**
    * @return Returns the nodeCnt.
    */
   public int getNodeCnt() {
      return nodeCnt;
   }
   
   /**
    * @param nodeCnt The nodeCnt to set.
    */
   public void setNodeCnt(int nodeCnt) {
      this.nodeCnt = nodeCnt;
   }
   
   /**
    * Increase the value of nodeCnt by 1.
    */
   public void incNodeCnt() {
      ++this.nodeCnt;
   }
   
   /**
    * Decrease the value of nodeCnt by 1.
    */
   public void decNodeCnt() {
      --this.nodeCnt;
   }
   
   /**
    * Decrease the value of nodeCnt by a given value.
    * 
    * @param value Number of nodes that were removed. 
    */
   public void decNodeCnt(int value) {
      this.nodeCnt -= value;
   }

   /**
    * @return Returns the closed.
    */
   public boolean isClosed() {
      return this.closed;
   }
}
