package fr.loria.coronsys.coron.datastructure.trie;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_answer;
import fr.loria.coronsys.coron.datastructure.lut.LUT;
import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.Database;
import fr.loria.coronsys.coron.helper.Error;
import fr.loria.coronsys.coron.helper.FileHandler;


/**
 * Class for representing the FC.generators in a prefix tree.
 * It allows a very fast lookup + it is possible to find all the subsets of a given set.
 * Main advantage: _very_ fast!
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
*/
public class Trie
{
   /**
    * Root element of the prefix tree (trie).
    */
   private Node root;
   
   /**
    * Number of nodes in the trie.
    */
   private int nodes;
   
   /**
    * Number of words in the trie.
    * Ex.: add {1,2,3} to an empty trie. Then nodes==3, words==1.
    */
   private int words;        
   
   /**
    * A special pointer to the empty set. If it is null, then the trie
    * does not have the empty set. If it points on an empty Row object,
    * then the trie contains the empty set.
    * 
    * This is a very special element of the trie. It is needed for the 
    * algorithms Pascal, Pascal+, Zart, and Carpathia. Actually even if it's
    * in the trie, it's not present in the tree, so when visualizing the content
    * of the trie, it will stay invisible. It has to be handled with attention.
    */
   private Node emptySet;
   
   /**
    * Counting the number of nodes in the trie. It is used to assign a unique
    * identifier for nodes.
    */
   protected int idCounter;

   /** 
    * Constructor, creates a new trie.
    */
   public Trie() {
      this.idCounter = 0;  // !!! it must be initialised before creating the first (root) node
      root = new Node(this);
      nodes = words = 0;
      this.emptySet = null;
   }
   
   /**
    * Re-initializes the trie. Clears all of its elements.
    */
   public void clear() {
      this.idCounter = 0;  // !!! it must be initialised before creating the first (root) node
      root = new Node(this);
      nodes = words = 0;
   }
   
   /** 
    * Determines if the trie is empty.
    * 
    * @return Boolean value indicating if the trie is empty.
    */
   public boolean isEmpty() {
      return (root.getChildren().isEmpty());
   }

   /** 
    * Returns the number of nodes in the trie.
    * 
    * @return Number of nodes in the trie. Not equals with the number of words!
    */
   public int getSizeNodes() { return nodes; }

   /**
    * Increments the number of nodes by one. 
    */
   void incNodeCounter() { ++nodes; }
   
   /**
    * Decrements the number of nodes by one. 
    */
   void decNodeCounter() { --nodes; }

   /**
    * Returns the number of words in the trie.
    * 
    * @return Number of words in the trie.
    */
   public int getSizeWords() { return words; }

   /**
    * Increments the number of words by one. 
    */
   void incWordCounter() { ++words; }
   
   /**
    * Decrements the number of words by one. 
    */
   void decWordCounter() { --words; }

   /** 
    * Adds a word to the trie.
    * 
    * @param word A word, a bitset of numbers.
    * @param row A reference to the row to which the word belongs.
    */
   public void add(BitSet word, Row row)
   {
      Node curr = root;
      for(int i=word.nextSetBit(0); i>=0; i=word.nextSetBit(i+1)) 
         curr = insert(i, curr);

      curr.setTerminal(this);
      curr.addRow(row);
   }
   
   /**
    * Add the empty set to the trie.
    */
   public void addEmptySet() {
      Node emptyNode = new Node();
      emptyNode.addRow(new Row());
      this.emptySet = emptyNode;
   }
   
   /**
    * Is the empty set in the trie?
    * 
    * @return True, if the empty set is in the trie. False, otherwise.
    */
   public boolean hasEmptySet() {
      return (this.emptySet != null);
   }
   
   /**
    * Deletes the empty set from the trie.
    */
   public void deleteEmptySet() {
      this.emptySet = null;
   }

   /** 
    * Inserts a node in the trie.
    * 
    * @param value Value of the node.
    * @param parent A node _under which_ we want to insert a new node.
    * @return The newly inserted child node.
    */
   private Node insert(int value, Node parent) {
      return parent.add(LUT.getInteger(value), this);
   }

   /** 
    * Get the root element of the trie.
    * 
    * @return The root element of the trie.
    */
   public Node getRoot() {
      return root;
   }

   /** 
    * Determines if a word is in the trie or not.
    * 
    * @param word A word, a bitset of numbers.
    * @return Is the word in the trie?
    */
   public boolean contains(BitSet word)
   {
      Node curr = root;
      for(int i=word.nextSetBit(0); (i>=0) && (curr!=null); i=word.nextSetBit(i+1)) 
         curr = (Node)(curr.getChildren().get(LUT.getInteger(i)));

      if ((curr!=null) && (curr.isTerminal())) return true;
      // else
      return false;
   }

   /** 
    * Traverses the trie by enumerating the nodes.
    * 
    * @param node A node from which we want to traverse recursively.
    */
   public void traverseNodes(Node node)
   {
      Iterator<Node> i;
      Node curr;
      if (node != null)
      {
         for (i = node.getChildren().values().iterator(); i.hasNext(); )
         {
            curr = i.next();
            System.out.println(curr);
            traverseNodes(curr);
         }
      }
   }
   
   // start: debug
   /**
    * A function which was used to display debug information during
    * the development of adding extents to concepts.
    * 
    * @param node A node whose extent and intent we want to see. As the function
    * is recursive, we start calling it by passing the root node to it.
    */
   public void traverseNodesDebug(Node node)
   {
      Iterator<Node> i;
      Node curr;
      Row_answer row;
      String ext;
      
      if (node != null)
      {
         for (i = node.getChildren().values().iterator(); i.hasNext(); )
         {
            curr = i.next();
            //
            row = (Row_answer) curr.getRow();
//            if (row!=null) ext = "; ext: "+row.getExtent();
//            else           ext = "";
//            System.out.println("Node: "+curr.getWord());
            if (curr.isTerminal()) 
            {
               System.out.println("ext: "+row.getExtent()+"; int: "+row.getIntent());
               //System.out.println("int: "+row.getIntent());
               //System.out.println("ext: "+row.getExtent());
               //System.out.println(row);
            }
            //
            traverseNodesDebug(curr);
         }
      }
   }
   // end:   debug

   /** 
    * Traverses the trie by enumerating its words.
    * 
    * @param node A node from which we want to traverse recursively.
    */
   public void traverseWords(Node node)
   {
      Iterator<Node> i;
      Node curr;
      if (node != null)
      {
         for (i = node.getChildren().values().iterator(); i.hasNext(); )
         {
            curr = i.next();
            if (curr.isTerminal()) System.out.println(curr.getWord());
            traverseWords(curr);
         }
      }
   }
   
   /**
    * String representation of nodes in the trie.
    * 
    * @return String representation of the nodes in the trie.
    */
   public String toStringNodes() {
      StringBuilder sb = new StringBuilder();
      this.toStringNodes2(this.root, sb);
      return sb.toString();
   }
   
   /**
    * Traverses recursively the nodes of the trie.
    * 
    * @param node Current node.
    * @param sb StringBuilder, in which we collect the words.
    */
   private void toStringNodes2(Node node, StringBuilder sb)
   {
      Iterator<Node> i;
      Node curr;
      if (node != null)
      {
         sb.append(node.toString()).append('\n');
         for (i = node.getChildren().values().iterator(); i.hasNext(); )
         {
            curr = i.next();
            //sb.append(curr.toString()).append('\n');
            toStringNodes2(curr, sb);
         }
      }
   }   
   
   /**
    * String representation of words in the trie.
    * 
    * @return String representation of the words in the trie.
    */
   public String toStringWords() {
      StringBuilder sb = new StringBuilder();
      this.toStringWords2(this.root, sb);
      return sb.toString();
   }
   
   /**
    * Traverses recursively the words of the trie.
    * 
    * @param node Current node.
    * @param sb StringBuilder, in which we collect the words.
    */
   private void toStringWords2(Node node, StringBuilder sb)
   {
      Iterator<Node> i;
      Node curr;
      if (node != null)
      {
         for (i = node.getChildren().values().iterator(); i.hasNext(); )
         {
            curr = i.next();
            if (curr.isTerminal()) sb.append(curr.getWord()).append('\n');
            toStringWords2(curr, sb);
         }
      }
   }

   /** 
    * Get the subsets of a set.
    * (NOT proper subsets).
    * 
    * @param set A set whose subsets we want to find.
    * @return Subsets of the set which are present in the trie.
    */
   public Vector<Row> getSubsetsOf(BitSet set)
   {
      Vector<Row> result = new Vector<Row>();

      findSubsetsOf(set, this.root, result);
      return result;
   }
   
   /** 
    * Find subsets of a set.
    * 
    * @param set A set whose subsets we are looking for.
    * @param curr Current node.
    * @param v When we find a subset, we add it to this vector. We collect the results here.
    */
   @SuppressWarnings("unchecked")
   private void findSubsetsOf(BitSet set, Node curr, Vector v)
   {
      if (curr == null) return;
      // else
      BitSet copy;
      Node child;
      if (curr.isTerminal()) v.add(curr.getRow());
      for (int i=set.nextSetBit(0); i>=0; i=set.nextSetBit(i+1)) 
      {
         child = (Node)(curr.getChildren().get(LUT.getInteger(i)));
         if (child != null)      // if the child at position i exists
         {
            copy = (BitSet)set.clone();
            copy.clear(i);
            findSubsetsOf(copy, child, v);
         }
      }
   }
   
   /** 
    * Has the given set a subset in the trie?
    * 
    * @param set The set that we are interested if it has a subset in the trie.
    * @return True, if the trie contains a subset of the given set. False, otherwise.
    */
   public boolean hasSubset(BitSet set)
   {
      Vector<Row> result = new Vector<Row>();

      hasSubset2(set, this.root, result);
      // if the "result" vector is empty, then the set has no subsets
      return (result.isEmpty() ? false : true);
   }
   
   /** 
    * This is a helper function for hasSubset(). If it finds a subset
    * of the given set, then it stops searching and it returns.
    * 
    * @param set The set that we are interested if it has a subset in the trie.
    * @param curr Current node.
    * @param v When we find a subset, we add it to this vector. We collect the results here.
    * This vector can have maximum 1 element, because if we add something here, we stop
    * further search.
    */
   @SuppressWarnings("unchecked")
   private void hasSubset2(BitSet set, Node curr, Vector<Row> v)
   {
      if (v.isEmpty() == false) return;
      //
      if (curr == null) return;
      // else
      BitSet copy;
      Node child;
      if (curr.isTerminal()) {
         v.add(curr.getRow());
         return;
      }
      for (int i=set.nextSetBit(0); i>=0; i=set.nextSetBit(i+1)) 
      {
         child = (Node)(curr.getChildren().get(LUT.getInteger(i)));
         if (child != null)      // if the child at position i exists
         {
            copy = (BitSet)set.clone();
            copy.clear(i);
            findSubsetsOf(copy, child, v);
         }
      }
   }
   
   /** 
    * Get the subsets of a set.
    * 
    * @param set A set whose subsets we want to find.
    * @param asSet If true, the result will be a set of Nodes. 
    * If false, the result is a set of bitsets. 
    * 
    * @return Subsets of the set which are present in the trie.
    */
   @SuppressWarnings("unchecked")
   public Vector getSubsetsOf(BitSet set, boolean asSet)
   {
      if (asSet == false) return getSubsetsOf(set);
      // else, if we want a list of Nodes
      Vector result = new Vector();

      findSubsetsOfAsNodes(set, this.root, result);
      // if the empty set is present, then it is (by definition) a subset of everything
      if (this.emptySet != null) {
         result.add(this.emptySet);
      }
      return result;
   }

   /** 
    * Find subsets of a set as a set of Nodes.
    * 
    * @param set A set whose subsets we are looking for.
    * @param curr Current node.
    * @param v When we find a subset, we add it to this vector. We collect the results here.
    * The result will be a set of Nodes.
    */
   @SuppressWarnings("unchecked")
   private void findSubsetsOfAsNodes(BitSet set, Node curr, Vector v)
   {
      if (curr == null) return;
      // else
      BitSet copy;
      Node child;
      if (curr.isTerminal()) v.add(curr);
      for (int i=set.nextSetBit(0); i>=0; i=set.nextSetBit(i+1)) 
      {
         child = (Node)(curr.getChildren().get(LUT.getInteger(i)));
         if (child != null)      // if the child at position i exists
         {
            copy = (BitSet)set.clone();
            copy.clear(i);
            findSubsetsOfAsNodes(copy, child, v);
         }
      }
   }
   
// old version, delete if the new works well (should :))
//   /**
//    * Breadth-first traversal of the trie to find supersets of a given set.
//    * 
//    * @param bf BreadthFirst object, controlling the traversal in the breadth-first manner.
//    * @param result A vector which is modified by the function. It'll contain the supersets of the given set.
//    * @param found
//    */
//   private void findSupersetsOf(BreadthFirst bf, Vector result, boolean found)
//   {
//      Node curr = bf.getFirstOpenNode();
//      // on
//      if (found) System.out.println("first open node: "+curr);
//      // off
//      bf.close(curr);						// set it closed, children will be open
//      BitSet set = bf.getSetOf(curr);
//      // debug ON
//      if (found) {
//         System.out.println("      List: "+bf);
//         System.out.println("   curr: "+curr.getValue()+"; "+set);
//         
//      }
//      // debug OFF
//
//      if (set.isEmpty() && curr.isTerminal()) {
//         result.add(curr.getWord());
//         // debug ON
//         if (found) System.out.println("Set added to result: "+curr.getWord());
//         // debug OFF
//         return;
//      }
//
//      // else, if set is not empty
//      int first_letter;
//      Iterator it;
//      Node child;
//      
//      if (curr == this.root)
//      {
//         for (it = curr.getChildren().values().iterator(); it.hasNext(); )
//         {
//            child = (Node)it.next();
//            bf.add(child, (BitSet)set.clone());
//            // on
//            if (found) System.out.println("   added: "+child);
//            // off
//         }
//      }
//      else  // if the current node is not the root
//      {
//	      // else, if curr is not the root node
//	      first_letter = set.nextSetBit(0);
//	      if (curr.getValue() > first_letter) return;
//	      else
//	      {
//	         if (curr.getValue() == first_letter)
//	         {
//	            set.clear(first_letter);
//	            if (set.isEmpty() && curr.isTerminal()) { 
//	               result.add(curr.getWord());
//	               // on
//	               if (found) System.out.println("Set added to result: "+curr.getWord());
//	               // off
//	               return;
//	            }
//	         }
//	
//	         for (it = curr.getChildren().values().iterator(); it.hasNext(); )
//	         {
//	            child = (Node)it.next();
//	            bf.add(child, (BitSet)set.clone());
//	            // on
//	            if (found) System.out.println("   added: "+child);
//	            // off
//
//	         }
//	      }
//      }
//   }
   
   /**
    * Gets the smallest (less elements) superset of a set in the trie.
    * 
    * @param set The set whose smallest superset we're looking for.
    * @return Smallest superset of the given set.
    */
   public BitSet getSmallestSupersetOf(BitSet set)
   {
      Vector result = new Vector();
      BreadthFirst bf = new BreadthFirst(this.getRoot(), set);
    
      while (bf.hasOpenNodes()) 
      {
         findSupersetsOf(set, bf, result, true, true);
         //if (result.contains(set)) result.remove(set);	// we are looking for the real supersets of "set". We don't want to get back "set".
         if (result.isEmpty() == false) return ((BitSet)(result.get(0)));
      }
    
      return null;		// it should never arrive here actually! 
   }
   
   /**
    * Gets all the supersets of a given set present in the trie. It may also 
    * return the given set if it's in the trie! If you don't want this, that is
    * you only need real supersets, then use the function getRealSupersetsOf().
    * 
    * @param set The set whose supersets we want to get.
    * @param asSet If true, result is returned as a list of sets. If false, result 
    * will be sent back as a list of Row objects.
    * @return All supersets (present in the trie) of the given set.  
    */
   public Vector getSupersetsOf(BitSet set, boolean asSet) 
   {
      Vector result = new Vector();
      BreadthFirst bf = new BreadthFirst(this.root, set); 
      
      while (bf.hasOpenNodes())
         findSupersetsOf(set, bf, result, asSet, false);
      
      return result;
   }
   
   /**
    * Find all the supersets of a given set.
    * 
    * @param set The set whose supersets we are looking for.
    * @param asSet If true, result is returned as a list of sets. If false, result 
    * will be sent back as a list of Row objects.
    * @param findProperSupersets If true, real supersets are found (original set is NOT
    * included). If false, then if the original set is in the trie, it'll be included in 
    * the result.
    * @return Returns all supersets of a given set. The resulting list (sets or Row objects)
    * can be modified by a parameter. Finding all or just real (original set excluded) can
    * also be set by a parameter.
    */
   public Vector getSupersetsOf(BitSet set, boolean asSet, boolean findProperSupersets) 
   {
      Vector result = new Vector();
      BreadthFirst bf = new BreadthFirst(this.root, set); 
      
      while (bf.hasOpenNodes())
         findSupersetsOf(set, bf, result, asSet, findProperSupersets);
      
      return result;
   }
   
   /**
    * Gets all real supersets of a given set present in the trie. It is guaranteed
    * that it won't return the given set, even if it's in the trie.
    * 
    * @param set The set whose real supersets we want to get.
    * @param asSet What result do we want? If asSet is true, the result will be sets. If asSet
    * is false, the result wil be Row objects, whose intents are the supersets.
    * @return All real supersets (present in the trie) of the given set.  
    */
//   public Vector getRealSupersetsOf(BitSet set, boolean asSet) 
//   {
//      Vector result = this.getSupersetsOf(set, asSet, true);
//      if (asSet) result.remove(set);
//      else
//      {
//         Row_answer row;
//         for (Enumeration e = result.elements(); e.hasMoreElements(); )
//         {
//            row = (Row_answer) e.nextElement();
//            if (set.equals(row.getIntent())) {
//               result.remove(row);
//               break;
//            }
//         }
//      }
//      
//      return result;
//   }  
   
   /**
    * Breadth-first traversal of the trie to find supersets of a given set. Its behaviour
    * can be parameterised.
    * 
    * @param originalSet The original set whose supersets we are looking for.
    * @param bf BreadthFirst object, controlling the traversal in the breadth-first manner.
    * @param result A vector which is modified by the function. It'll contain the supersets of the given set.
    * @param asSet What result do we want? If asSet is true, the result will be sets. If asSet
    * is false, the result wil be Row objects, whose intents are the supersets.
    * @param findRealSupersets If true, real supersets are found (original set is NOT
    * included). If false, then if the original set is in the trie, it'll be included in 
    * the result.
    */
   @SuppressWarnings("unchecked")
   private void findSupersetsOf(BitSet originalSet, BreadthFirst bf, Vector result, boolean asSet, boolean findRealSupersets)
   {
      Node curr = bf.getFirstOpenNode();
      // on
      //if (found) System.out.println("first open node: "+curr);
      // off
      //bf.close(curr);						// set it closed, children will be open
      BitSet set = bf.getSetOf(curr);
      Node child;
      boolean canAdd = true;
      
      if (curr != this.root)
      {
	      // else, if curr is not the root node
         if (set.isEmpty() == false)
         {
            int first_letter = set.nextSetBit(0);
            if (curr.getValue() == first_letter)
	            set.clear(first_letter);
            else if (curr.getValue() > first_letter) {
               bf.close(curr);	// set it closed first
               return;				// then return. This subtree doesn't have to be processed, we'll find nothing there.
            }
         }
	      
         // if the search set is empty and the current node is terminal node => found a superset
         // The result can be registered in two different format depending on who asks it.
         // AssRuleX want the superset as a BitSet, but OrderFinder wants rather the row whose
         //    intent is the superset.
         // We combined the two options, and it can be set via the "asSet" variable.
         if (set.isEmpty() && curr.isTerminal())
         {
            if (findRealSupersets && curr.getWord().equals(originalSet)) canAdd=false;
            if (canAdd) {
               if (asSet) result.add(curr.getWord());
               else       result.add(curr.getRow());
            }
         }
      }
    
      // give search set to the children and make them opened
      for (Iterator it = curr.getChildren().values().iterator(); it.hasNext(); )
      {
         child = (Node)it.next();
         // if the search set is empty, then we are just traversing the subtree and collecting all
         // terminal nodes. In this case, the search set is always empty, and thus it is not necessary
         // to always pass a new object. We can pass a reference to the same empty object, nobody
         // will modify it. In other cases we have to pass a copy, because in the subtree they may delete
         // from the set, but at an upper position coming back in recursion we will need the original set
         // (without deletion).
         bf.add(child, set.isEmpty()?set:(BitSet)set.clone());
         // on
         //if (found) System.out.println("   added: "+child);
         // off

      }
      // close the current node. We moved on from the node, so it must be set closed.
      bf.close(curr);
   }

   /**
    * Initializes the trie for finding extents of concepts. It means the following: it sets
    * the extent part of the direct children of the root node. In the trie only the terminal
    * nodes point on Row objects. Directly under the root we can have both terminal and
    * non-terminal nodes. In the case of terminal nodes: set the extent part of the row.
    * In the case of non-terminal nodes: create a new Row_answer object, which is empty,
    * we only set its extent part, and then the non-terminal node will point on this row. 
    * 
    * @param map A map containing the objects belonging to an attribute.
    */
   public void initTrieForFindingExtents(HashMap<Integer, TreeSet<Integer>> map)
   {
      Node child;
      Integer value;
      Row_answer row;
      
      for (Iterator<Node> it = this.root.getChildren().values().iterator(); it.hasNext(); )
      {
         child = it.next();
         value = LUT.getInteger(child.getValue());
         if (child.isTerminal())
         {
            row = (Row_answer) child.getRow();
            // the row must be a copy! cloning is necessary!
            row.setExtent((TreeSet<Integer>) (map.get(value)).clone());
         }
         else // if child is not terminal
         {
            row = new Row_answer();
            // the row must be a copy! cloning is necessary!            
            row.setExtent((TreeSet<Integer>) (map.get(value)).clone());
            child.setRow(row);
         }
      }
   }

   /**
    * Fills the extent part of the given closed frequent itemset. The procedure gets
    * an intension and finds its extension part.
    * 
    * @param word The intension part of a concept.
    * @param map A map which contains the objects that belong to an attribute.
    */
   @SuppressWarnings("unchecked")
   public void fillExtent(BitSet word, HashMap map)
   {
      Node curr = this.findLastLetter(word);    // locate the last letter of the word
      LinkedList up = new LinkedList();
      Node node;
      TreeSet extent = null, objects;
      ListIterator it;
      // on
      //boolean debug = false;
      //BitSet find = new BitSet();
      //find.set(2); find.set(3); find.set(8); find.set(11);
      //find.set(2); find.set(3); find.set(4); find.set(8); find.set(11); find.set(12); find.set(14);
      // off
      
      if (curr != null)       // it cannot be null, but sure is sure :)
      {
         // on: debug
         //if (curr.getWord().equals(find)) debug=true;
         //if (debug) System.out.println("curr: "+curr.getWord());
         // off: debug
         node = curr.getParent();
         
         while (node.getRow() == null)
         {
            up.addFirst(node);  
            node = node.getParent();
         }
         up.addFirst(node);     // add the node whose extension is already found too
         
         // on: debug
         //if (debug) System.out.println("   up: "+up);
         // off: debug
         
         // objects belonging to the attribute which is in the current node (current: last letter of the word) 
         //!!!// extent = (TreeSet) ((TreeSet) map.get(this.lut.getInteger(curr.getValue()))).clone(); 
         // on: debug
         //System.out.println("   extent: "+extent);
         // off: debug
         // enumerate the nodes and take intersections with the current node's objects
         for (it = up.listIterator(); it.hasNext(); )
         {
            node = (Node) it.next();
            // on
            //if (debug) System.out.println("   node in up: "+node);
            // off
            node.decChildrenCnt();
            // on
            //System.out.println("   node: "+node+"; children after dec.: "+node.getChildrenCnt()+"; first: "+node.isFirstNode());
            // off
            
            if (node.isFirstNode())
            {
               // on
               //if (debug) System.out.println("      first node");
               // off
               if (node.getChildrenCnt() == 0) 
               {
                  if (node.isTerminal()) extent = (TreeSet) ((Row_answer) node.getRow()).getExtent().clone();
                  else
                  {
                     extent = ((Row_answer) node.getRow()).getExtent();
                     node.setRow(null);
                  }
               }               
               else extent = (TreeSet) ((Row_answer) node.getRow()).getExtent().clone();
               // on
               //if (debug) {
               //   System.out.println("      extent of upper node: "+extent);
               //   System.out.println("      partial result of intersection:: "+extent);
               //}
               // off
            }
            else  // if node is not the first element in the list
            {
               // on
               //if (debug) System.out.println("      inner (NOT first) node");
               // off
               objects = (TreeSet) map.get(LUT.getInteger(node.getValue()));
               // on
               //if (debug) System.out.println("      objects of node "+node+": "+objects);
               // off
               // on: debug
               //if (debug) System.out.print("      extent: "+extent+" intersect objects: "+objects+" = ");
               // off: debug
               extent.retainAll(objects);
               // on: debug
               //if (debug) System.out.println(extent);
               // off: debug
               if (node.getChildrenCnt() > 0)
               {
                  node.setRow(new Row_answer());
                  ((Row_answer) node.getRow()).setExtent((TreeSet) extent.clone());
                  // on
                  //if (debug) System.out.println("   in node"+node+" extent was set to "+extent);
                  // off
               }
            }
         }
         objects = (TreeSet) map.get(LUT.getInteger(curr.getValue()));
         // on: debug
         //System.out.print("   extent: "+extent+" intersect objects: "+objects+" = ");
         // off: debug
         extent.retainAll(objects);
         // on: debug
         //if (debug) System.out.println(">>> extent, result: "+extent);
         // off: debug
         //((Row_answer) node.getRow()).setExtent(extent);
         // on: debug
         //System.out.println(extent);
         // off: debug
         // on: debug
         //System.out.println("extent: "+extent);
         // off: debug
         
         ((Row_answer) curr.getRow()).setExtent(extent);       // set the found extension for the current node
      }
   }

   /**
    * Finds the last element (letter) of a sequence. Example: it gets {1,5,8},
    * and it returns the address of the node 8 (the path to 8 goes through
    * 1 and 5 of course). 
    * Warning! The input sequence can be anything! Maybe it's not a real word, 
    * i.e. maybe its last letter is not terminal!
    * If you want to find whole words, use the findWord() function instead. 
    * 
    * @param sequence The word whose last letter in the trie we want to locate.
    * @return The node in the trie which represents the last letter of the word.
    */
   private Node findLastLetter(BitSet sequence)
   {
      Node curr = root;
      for(int i = sequence.nextSetBit(0); (i>=0) && (curr!=null); i = sequence.nextSetBit(i+1)) 
         curr = (Node)(curr.getChildren().get(LUT.getInteger(i)));

      if (curr!=null) return curr;
      // else
      return null;
   }
   
   /**
    * In the trie each terminal node points on a Row object. Knowing an itemset (word),
    * we want to get its Row object.
    * 
    * @param word The word (itemset in our case) whose corresponding Row object we want to get.
    * @return The Row object which corresponds to the word (itemset).
    */
   public Row getRowOf(BitSet word)
   {
      Node node = this.findLastLetter(word);
      if (node == null) return null;
      // else
      return node.getRow();
   }
   
   /**
    * Finds the last letter of a word. It also tests if this word is
    * really a word in the trie, i.e. its last letter is terminal or not.
    * 
    * @param word The word which we are looking for.
    * @return The node in the trie which represents the last letter of the word.
    * null, if the word is not in the trie.
    */
   private Node findWord(BitSet word) 
   {
      Node last = findLastLetter(word);
      
      if ((last == null) || (last.isTerminal() == false)) return null;
      return last;
   }


   /**
    * Returns the GraphViz .dot representation of the trie.
    * 
    * @return The GraphViz .dot representation of the trie as a string.
    */
   public String toStringDot()
   {
      GraphVizDot dot = new GraphVizDot(this);
      dot.start();
      return dot.toString();
   }
   
   /**
    * Returns the GraphViz .dot representation of the trie.
    * Attributes are visualized by their names.
    * 
    * @return The GraphViz .dot representation of the trie as a string.
    */
   public String toStringNameDot()
   {
      boolean name = true;
      
      GraphVizDot dot = new GraphVizDot(this, name);
      dot.start();
      return dot.toString();
   }

   /**
    * Returns the terminal nodes in the trie.
    * 
    * @return Terminal nodes in the trie.
    */
   public Vector<Node> getTerminalNodes()
   {
      Vector<Node> terminals = new Vector<Node>();
      getTerminalNodes2(this.root, terminals);
      return terminals;
   }
   
   /**
    * This is a helper function for getTerminalNodes(). It'll actually traverse the trie
    * and collect terminal nodes in a vector.
    * 
    * @param node Current node.
    * @param terminals Vector of terminal nodes found so far.
    */
   @SuppressWarnings("unchecked")
   private void getTerminalNodes2(Node node, Vector terminals)
   {
      Iterator i;
      Node curr;
      if (node != null)
      {
         for (i = node.getChildren().values().iterator(); i.hasNext(); )
         {
            curr = (Node)i.next();
            if (curr.isTerminal()) terminals.add(curr);
            getTerminalNodes2(curr, terminals);
         }
      }
   }
   
   /**
    * Returns all nodes in the trie.
    * 
    * @return All nodes in the trie.
    */
   public Vector<Node> getAllNodes()
   {
      Vector<Node> allNodes = new Vector<Node>();
      getAllNodes2(this.root, allNodes);
      return allNodes;
   }
   
   /**
    * This is a helper function for getAllNodes(). It'll do the recursive traversal
    * and collect each node in a vector.
    * 
    * @param node Current node.
    * @param allNodes Vector of all nodes.
    */
   @SuppressWarnings("unchecked")
   private void getAllNodes2(Node node, Vector allNodes)
   {
      Iterator i;
      Node curr;
      if (node != null)
      {
         allNodes.add(node);
         for (i = node.getChildren().values().iterator(); i.hasNext(); )
         {
            curr = (Node)i.next();
            getAllNodes2(curr, allNodes);
         }
      }
   }
   
   /**
    * Returns all words in the trie.
    * 
    * @return All words in the trie.
    */
   public Vector<Node> getAllWords()
   {
      Vector<Node> allWords = new Vector<Node>();
      getAllWords2(this.root, allWords);
      return allWords;
   }
   
   /**
    * This is a helper function for getAllWords(). It'll do the recursive traversal
    * and collect each terminal node in a vector.
    * 
    * @param node Current node.
    * @param allWords Vector of all terminal nodes.
    */
   @SuppressWarnings("unchecked")
   private void getAllWords2(Node node, Vector<Node> allWords)
   {
      Iterator<Node> i;
      Node curr;
      if (node != null)
      {
         if (node.isTerminal()) allWords.add(node);
         for (i = node.getChildren().values().iterator(); i.hasNext(); )
         {
            curr = i.next();
            getAllWords2(curr, allWords);
         }
      }
   }

   /**
    * Deletes a word from the trie.
    * 
    * @param del The word that we want to delete.
    * @return True, if the word was deleted. False, if the word was not in the trie.
    */
   public boolean deleteWord(BitSet del)
   {
      if (del.isEmpty()) {						// if we want to delete the empty set
         if (this.hasEmptySet() == false) return false;	// the empty set is not in the trie
         // else
         this.deleteEmptySet();									
         return true;												// the empty set was deleted successfully
      }
      Node last = findWord(del);
      if (last == null) return false;     // the word is not in the trie
      // else, if we found the word
      last.deleteWord();
      return true;
   }
   

   /**
    * Deletes a word from the trie. We know the address of its last
    * letter.
    * 
    * @param last Last letter of the word we want to delete.
    * @return True, if the word exists and was deleted successfully. False,
    * if the node is not the terminal node of a word.
    */
   public boolean deleteWord(Node last)
   {
      if (last == null) return false;
      if (last == this.emptySet) {
         this.deleteEmptySet();
         return true;
      }
      last.deleteWord();
      return true;
   }

   /**
    * Merges this trie with the other trie in the parameter. That is:
    * it adds all the elements of the other to this trie.
    * Warning! By the end of the operation, the other trie will
    * be deleted. After the merge we cannot use the other trie. 
    * 
    * @param t2 The other trie whose elements we want to add.
    */
   public void merge(Trie t2)
   {
      Vector<Node> allWords = t2.getAllWords();
      Node node;
      
      for (Enumeration<Node> e = allWords.elements(); e.hasMoreElements(); )
      {
         node = e.nextElement();
         // System.err.println(">> add: "+node);
         this.add(node.getWord(), node.getRow());
      }
   }

   /**
    * Write the dot representation of the trie to the specified
    * file under the specified directory.  
    * 
    * @param dName Directory name.
    * @param fName File name.
    * @return True, if the result was written successfully; false, otherwise.
    */
   public boolean writeDot(String dName, String fName)
   {
      // prepare file for writing
      File dirName     = new File(dName);
      File resultName  = new File(dName + "/" + fName);
      BufferedWriter file;
      
      if ((file = FileHandler.prepareDirFile(dirName, resultName)) == null)
         return false;
      
      // else, if everything is fine with the output file      
      try {
         file.write(this.toStringNameDot());
         file.flush();
         file.close();
      }
      catch (IOException ioe) {
         Error.warning(C.F_IO, dirName, resultName);
         return false;
      }
      
      return true;
   }
   
// TODO: add itarators
//   
//*   public Iterator getNodeIterator() {
//*      return new MyNodeIterator();
//*   }
//*
//*   class MyNodeIterator implements java.util.Iterator
//*   {
//*      private Node curr;
//*
//*      MyNodeIterator() {
//*         curr = root;
//*      }
//*
//*      public boolean hasNext() {
//*         return (curr == null);
//*      }
//*
//*      public Object next() {
//*         throw new UnsupportedOperationException();
//*      }
//*
//*      public void remove() {
//*         throw new UnsupportedOperationException();
//*      }
//*   }
}

/**
 * A class to help traversing the trie in the breadth-first manner. Why breadth-first, and not deep-first?
 * It's an optimization. We'll only need the SMALLEST superset. If we do breadth-first search, we'll find
 * the smallest superset for the first time! We can check the result, and if we found something, we can 
 * return it immediately, we don't need to traverse the whole trie!
 * 
 * @author Jabba Laci
 */
class BreadthFirst
{
	/**
	 * A linked list to store nodes. It'll function as a FIFO list. It stores the opened nodes
	 * which need to be extended. We extend the 1st node at the beginning of the list, the new
	 * nodes come to the end, and we make the extended node closed (delete it simply from the opened nodes).
	 */
	private LinkedList<Node> open = new LinkedList<Node>();
	
	/**
	 * Each node in the list has a set part. We use this map to assign nodes (key) and these sets (value). 
	 */
	private Map<Node, BitSet> map = new HashMap<Node, BitSet>();
	
	/**
	 * Constructor.
	 * 
	 * @param root Root node.
	 * @param set The set whose supersets we're looking for.
	 */
	@SuppressWarnings("unchecked") 
    BreadthFirst(Node root, BitSet set) {
		this.add(root, set);
		this.map.put(root, set);
	}
	
	/**
	 * Returns the set part of a node.
	 * 
	 * @param node The node whose set part we want to get.
	 * @return The set part of the node.
	 */
	public BitSet getSetOf(Node node) {
		return (BitSet) this.map.get(node);
	}
	
	/**
	 * Returns the first opened node (which is to be extended).
	 * 
	 * @return First opened node of the trie.
	 */
	public Node getFirstOpenNode() {
		return (Node) open.getFirst();
	}

	/**
	 * Adds a node to the list and also assigns its set to it.
	 * 
	 * @param node A node that we make opened.
	 * @param set The set part of the node.
	 */
	@SuppressWarnings("unchecked")
   public void add(Node node, BitSet set) {
		this.open.add(node);
		this.map.put(node, set);
		// debug ON
		//System.out.println("Put in: "+node.getValue()+"; "+set);
		// debug OFF
	}
	
	/**
	 * Asks if there are still open nodes. The breadth-first search stops when all nodes are closed.
	 * 
	 * @return True, if there are still opened nodes; false, if all nodes are closed.
	 */
	public boolean hasOpenNodes() {
		return this.open.isEmpty() == false;
	}

	/**
	 * Makes a node closed. 
	 * 
	 * @param node The node which we want to make closed.
	 */
	public void close(Node node) {
		open.remove(node);
	}
	
	/**
	 * String representation of the opened nodes.
	 * 
	 * @return String representation of the opened nodes.
	 */
	public String toString() {
		return this.open.toString();
	}
}

/**
 * Class responsible to create a GraphViz .dot representation of the trie.
 * Its output can be compiled by the program "dot", which produces an image
 * of the trie.
 * For linking nodes, we needed to assign an ID to each node to be able to
 * distinguisg them. For this we added an "id" attribute to nodes. This value
 * is unique for each node. The root has id 0, and whenever a new node is
 * added, its id value is incremented by one.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
class GraphVizDot
{
   /**
    * A reference to the trie with which we want to work.
    */
   private Trie trie;
   
   /**
    * We collect the .dot output in a StringBuilder.
    */
   private StringBuilder sb;
   
   /**
    * Should attributes be visualized by their names?
    */
   private boolean name;
   
   /**
    * Constructor. It gets the reference of the trie with which we will work.
    * 
    * @param trie The trie whose .dot description we want to make.
    */
   GraphVizDot(Trie trie) {
      this.trie = trie;
      this.sb = new StringBuilder();
      this.name = false;
   }

   /**
    * Constructor. It gets the reference of the trie with which we will work +
    * should attributes be visualized by their names?
    * 
    * @param trie The trie whose .dot description we want to make.
    * @param name Should attributes be visualized by their names?
    */
   public GraphVizDot(Trie trie, boolean name) {
      this(trie);
      this.name = true;
   }

   /**
    * Controller function.
    */
   public void start() 
   {
      //print_debug_info(trie.getRoot());
      print_header();
      print_terminal_nodes();
      print_nonterminal_nodes();
      print_all_nodes();
      print_links();
      print_footer();
   }
   
//   /**
//    * It was used for debug purposes.
//    * 
//    * @param parent The current node.
//    */
//   private void print_debug_info(Node parent)
//   {
//      Iterator i;
//      Node child;
//      if (parent != null)
//      {
//         sb.append("// ").
//            append("id: "+parent.getId()+"; ").
//            append(parent.toString()).
//            append("\n");
//         for (i = parent.getChildren().values().iterator(); i.hasNext(); )
//         {
//            child = (Node)i.next();  
//            print_debug_info(child);
//         }
//      }
//   }

   /**
    * Prints the header of the .dot file.
    */
   private void print_header()
   {
      sb.append("digraph trie\n")
        .append("{\n")
        .append("   // top to bottom; edge without arrow\n")
        .append("   rankdir=TB;\n")
        .append("   concentrate=true;\n")
        .append("   edge [dir=none, color=black];\n")
        .append('\n');
   }
   
   /**
    * Prints the footer (end) of the .dot file.
    */
   private void print_footer() {
      sb.append("}\n");
   }
   
   /**
    * Prints terminal nodes. They are denoted by double circles.
    */
   private void print_terminal_nodes()
   {
      sb.append("   // terminal nodes\n")
        .append("   node [shape=circle, peripheries=2, style=filled ];\n")
        .append("   ");
      
      Node node;
      Vector<Node> terminals = trie.getTerminalNodes();
      for (Enumeration<Node> e = terminals.elements(); e.hasMoreElements(); )
      {
         node = (Node) e.nextElement();
         sb.append(node.getId()).append(' ');
      }
      sb.append('\n');
      sb.append('\n');
   }
   
   /**
    * Prints non-terminal nodes. They are denoted by single circles.
    * The root node is special, it is denoted by an ellipse.
    */
   private void print_nonterminal_nodes()
   {
      sb.append("   // non-terminal nodes\n")
        .append("   node [shape = circle, peripheries=1, color=black, style=solid];\n")
        .append('\n');
   }
   
   private void print_all_nodes()
   {
      sb.append("   // root and all the other nodes\n");
      
      int attrNumber;
      Node node;
      Vector<Node> allNodes = trie.getAllNodes();
      for (Enumeration<Node> e = allNodes.elements(); e.hasMoreElements(); )
      {
         node = e.nextElement();
         if (node.isRoot()) {
            sb.append("   ").append(node.getId()).append(" [label=root, shape=ellipse];\n");
         }
         else
         {
            attrNumber = node.getValue();
            
            sb.append("   ").append(node.getId());
            sb.append(" [label=");
            if (this.name) sb.append(Database.getAttr(attrNumber));
            else           sb.append(attrNumber);
            sb.append("];\n");
         }
      }
      sb.append('\n');
   }
   
   /**
    * Prints links between nodes.
    */
   private void print_links() {
      sb.append("   // links between the nodes\n");
      print_links2(trie.getRoot());
   }
   
   /**
    * It is a helper function for print_links(). It'll do the recursive traversal,
    * and it will link a parent node with all its children.
    * 
    * @param parent
    */
   private void print_links2(Node parent)
   {
      Iterator<Node> i;
      Node child;
      if (parent != null)
      {
         for (i = parent.getChildren().values().iterator(); i.hasNext(); )
         {
            child = i.next();
            sb.append("   ").
               append(parent.getId()).
               append(" -> ").
               append(child.getId()).
               append(";\n");
            print_links2(child);
         }
      }
   }
   
   /**
    * Returns the .dot file as a string.
    * 
    * @return The .dot file as a string.
    */
   public String toString() {
      return sb.toString();
   }
}
