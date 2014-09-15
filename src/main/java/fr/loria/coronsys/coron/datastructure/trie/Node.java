package fr.loria.coronsys.coron.datastructure.trie;

import java.util.BitSet;
import java.util.HashMap;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.lut.LUT;

/**
 * Nodes of the prefix tree (trie).
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Node
{
   /**
    * The trie to which this node belongs.
    */
   private Trie trie;
   
   /**
    * ID of the node. This is a primary key, i.e. unique value for a node.
    * Used for the GraphViz visualisation when nodes need to be linked together.
    */
   private int id;
   
   /**
    * Value of the node (an integer).
    */
   private int value;
   
   /**
    * Children of the node (stored in a hash).
    */
   private HashMap<Integer, Node> children;
   
   /**
    * A reference back to the node's oarent.
    */
   private Node parent;
   
   /**
    * Is this node a terminal node?
    */
   private boolean terminal;
   
   /**
    * Used in terminal nodes. A reference to the Row object whose intent is stored in the trie.
    * Ex.: we store {1,4,5}, then at the terminal node 5 there is a reference to its row.
    */
   private Row row;
   
   /**
    * We'll use this for the optimised version of "extent finder". The
    * current number of children is always the size of the HashMap "children"!
    * This counter registers how many children are processed of the current node.
    * This is needed to find extents of closed frequent itemsets.
    */
   private int childrenCnt;

   /**
    * Constructor. Children of a node are stored in a hashmap. 
    * @param trie The trie data structure to which this node belongs to.
    */
   Node(Trie trie) {
      this.trie         = trie;
      this.id           = this.trie.idCounter++;
      this.children     = new HashMap<Integer, Node>();
      this.terminal     = false;
      this.parent       = null;
      this.childrenCnt  = 0;
   }
   
   /**
    * Basic constructor. Used for the empty set.
    */
   Node() {
      this.terminal     = false;
      this.parent       = null;
      this.childrenCnt  = 0;
   }
   
   /**
    * Deletes a word from the trie. This node is the last letter
    * of the word. We start the deletion of the word from here.
    */
   public void deleteWord()
   {
      if (this.isLeaf())
      {
         this.parent.deleteChild(this);
         this.trie.decNodeCounter();
         this.trie.decWordCounter();
         this.parent.delete();
      }
      else // if the current node is not leaf
      {
         this.terminal = false;
         this.trie.decWordCounter();
         this.row = null;
         // and stop!
      }
   }

   /**
    * Delete an inner node (a letter which is not the last element of a word).
    */
   private void delete()
   {
      if (this == trie.getRoot()) return;
      //
      if (this.isTerminal() == false)
      {
         if (this.isLeaf())
         {
            this.parent.deleteChild(this);
            this.trie.decNodeCounter();
            this.parent.delete();
         }
         // else, if not leaf: stop!
      }
      // else, if terminal: stop!
   }

   /**
    * Deletes the specified child.
    * 
    * @param node The child that we want to delete.
    */
   private void deleteChild(Node node) {
      this.children.remove(LUT.getInteger(node.getValue()));
      --this.childrenCnt;
   }

   /**
    * Returns the ID of the node.
    * 
    * @return ID of the node (a unique integer).
    */
   public int getId() {
      return this.id;
   }

   /**
    * Determines if the node is the first in the "up" list. That is: from the
    * current node we are going up to the direction of the root node and we collect
    * nodes in a list. The list contains elements in a reversed order, so if we put
    * in {4,2,1}, we'll read {1,2,4}. This function tests if the node is the first
    * element in this list or not. The first element already has an extent set, so
    * this is what we check. And if it has an extent, it must have a row too.
    * Thus if we find a reference (pointer) to a Row object, then it's the first 
    * element in the list.
    * 
    * @return Is it the first element in the list?
    */
   public boolean isFirstNode() {
      return (this.getRow() != null);
   }

   /**
    * Decreases the value of childrenCnt by 1.
    * 
    */
   public void decChildrenCnt() {
      --this.childrenCnt;
   }
   
   /**
    * Gets the value of childrenCnt.
    * 
    * @return Returns the value of childrenCnt, i.e.: how many children were 
    * NOT YET processed.
    */
   public int getChildrenCnt() {
      return this.childrenCnt;
   }

   /**
    * Sets the row part of a node.
    * 
    * @param row The new row part of a node.
    */
   public void setRow(Row row) {
      this.row = row;
   }

   /** 
    * Constructor. Create a node and set its value.
    * 
    * @param trie The trie data structure to which this node belongs to.
    * @param value Value of the newly created node.
    */
   Node(Trie trie, int value) {
      this(trie);
      this.value = value;
   }

   /**
    * @return String representation of the node (value, terminal?).
    */
   public String toString() {
      return ""+(terminal?"(":"")+"("+value+")"+(terminal?")":"");
   }

   /** 
    * Adds a node to the trie.
    * 
    * @param value Value of the new node.
    * @param trie The trie itself.
    * @return The new node that we just inserted, or the node if it already existed.
    */
   @SuppressWarnings("unchecked")
   public Node add(Integer value, Trie trie) 
   {
      Node child = (Node)children.get(value);
      if (child == null)                  // if it doesn't exist yet
      {
         child = new Node(this.trie, value.intValue());
         child.setParent(this);
         children.put(value, child);
         trie.incNodeCounter();
         ++this.childrenCnt;
      }
      // else, if it already exists => just send it back
      return child; 
   }

   /** 
    * Sets the parent of a node.
    * 
    * @param parent Parent of the node.
    */
   public void setParent(Node parent) {
      this.parent = parent;
   }

   /** 
    * Get all the children of a node.
    *  
    * @return Children of a node in a hashmap.
    */
   public HashMap<Integer, Node> getChildren() {
      return children;
   }
   
   /**
    * Gets the given child of a node.
    * 
    * @param key Key of the child (by which we can locate it in a map).
    * @return Returns the given child of the node.
    */
   public Node getChild(Integer key) {
      return (Node) this.children.get(key);
   }

   /** 
    * Makes a node terminal (that is: it's the end of a word).
    * 
    * @param trie The trie itself (bacause we need to increment the wordcount).
    */
   public void setTerminal(Trie trie) 
   {
      if (terminal == false) {
         terminal = true;
         trie.incWordCounter();
      }
   }

   /** 
    * Determines if a node is terminal or not.
    * 
    * @return Is this node terminal?
    */
   public boolean isTerminal() {
      return terminal;
   }

   /** 
    * Get the value of a node.
    * 
    * @return The value of a node.
    */
   public int getValue() {
      return value;
   }

   /** 
    * Get the parent of a node.
    * 
    * @return The parent of the node.
    */
   public Node getParent() {
      return parent;
   }

   /** 
    * Is this node a leaf node?
    * 
    * @return Is it a leaf node?
    */
   public boolean isLeaf() {
      return (children.isEmpty());
   }

   /** 
    * Get the word belonging to this node. That is: get the numbers from this
    * node back to the root (then reverse the order, but as we work with bitsets,
    * this reverse step is not necessary).
    * 
    * @return The word belonging to this node.
    */
   public BitSet getWord()
   {
      Node curr = this;
      BitSet word = new BitSet();

      while (curr.getParent() != null)
      {
         word.set(curr.getValue());
         curr = curr.getParent();
      }
      return word;
   }

   /** 
    * Set the row reference to a terminal node.
    * 
    * @param row Reference to the row to which the word belongs. Thus only terminal
    * nodes have references to rows.
    */
   void addRow(Row row) {
      this.row = row;
   }

   /** 
    * Get the reference to the row assigned to this node.
    * 
    * @return Reference to the row to which this word belongs to.
    */
   public Row getRow() {
      return this.row;
   }

   /**
    * Is it the root node? The root is the only node which has no parent.
    * 
    * @return Is it the root node?
    */
   public boolean isRoot() {
      return (this.parent == null);
   }
}
