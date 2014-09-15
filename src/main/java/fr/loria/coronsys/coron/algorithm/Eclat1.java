package fr.loria.coronsys.coron.algorithm;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

//import coron.helper.DatabaseHandler;
import fr.loria.coronsys.coron.datastructure.Result;
import fr.loria.coronsys.coron.datastructure.Result2;
import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.charm.CharmExtra;
import fr.loria.coronsys.coron.datastructure.charm.ITnode;
import fr.loria.coronsys.coron.datastructure.charm.ITtree;
import fr.loria.coronsys.coron.datastructure.universal.Row_Universal_F;
import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.Convert;
import fr.loria.coronsys.coron.helper.Database;
import fr.loria.coronsys.coron.helper.DemoCoron;
import fr.loria.coronsys.coron.helper.Global;
import fr.loria.coronsys.coron.helper.Memory;
import fr.loria.coronsys.coron.helper.Statistics;

/** 
 * Controller class.
 * Eclat1 - modified version of Charm4 (some lines are deleted :))
 *  
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Eclat1 
implements Algorithm
{
   /**
    * Contains lines of the input file as bitsets.
    */
   private Vector database;
   
   /**
    * Min. support as an integer number.
    */
   private int min_supp;
      
   /**
    * We'll access the final result (list of FCIs) through this object.
    */
   private Result result;
   
   private Result2 result2;
   
   /**
    * Monitoring memory usage. 
    */
   private Memory memory;
   
   /**
    * Counter for dumped output (when FC_i tables are flushed immediately, and
    * thus duplicates are posible).
    */
   private long dumpCnt;
   
   /**
    * Number of found FCIs. Duplicates spossible in -dump mode!
    */
   private int fiCnt;
   
   /**
    * Save the F tables in a database?
    */
   private boolean saveF;
   
   /**
    * Save F tables in memory.
    */
   private boolean saveMemF;
   
   /**
    * This is responsible for saving F/Z tables in the SQL database.
    */
   //private DatabaseHandler db_handler;
   
   /**
    * The IT-tree that we use for finding concepts.
    */
   private ITtree itTree;
   
   /**
    * Frequent 2-long itemsets are stored in this triangular matrix.
    */
   private int[][] f2Matrix;
   
   /**
    * Do you want to get the result in text?
    * Default: attribute numbers (not attribute names).
    */
   private boolean text;
   
   /**
    * Count the number of skipped infrequent 2-itemsets. For test purposes,
    * to see the usefulness of the trie containing all frequent 2-patterns.
    */
   private long skipCnt;
   
   /**
    * Number of rare candidates.
    * Eclat takes the extent intersection of two concepts. If it's rare, then this
    * candidate is pruned. We want to test the number of such candidates. We want to 
    * see how the number of the candidates can be minimized. One idea: sort the
    * children of a concept by ascending order of their support. 
    */
   private long rareCand;
   
   /**
    * The largest attribute in the database. It is needed to check if a 2-pattern 
    * is frequent or not. We need this value to get the support of a 2-pattern from
    * the triangular matrix. With this value we make a position transformation in the matrix.
    * Anyway, it's better to store this value here than always ask it from the Database class
    * through a function call.
    */
   private int largestAttr;
   
   /**
    * Is the suppression of stdout on? I.e.: is the -null option set?
    */
   private boolean toNull;
   
   //private long treeMaxSize;
   
   /**
    * Constructor. What is needed? Context (the database) + minsupport.
    * Attention! Charm2 gets the database in horizontal format first!
    * The F_2 has to be determined, then the database is converted to 
    * vertical format, and evything goes on as in Charm1.
    * 
    * @param database Vector, containing the input file's lines as bitsets.
    * @param min_supp Min. support as integer.
    */
   public Eclat1(Vector database, int min_supp)
   {
      DemoCoron.checkAlg(C.ALG_ECLAT_1);
      //
      this.database    = database;
      this.min_supp    = min_supp;
      this.result      = new Result();
      this.result2     = new Result2();
      this.memory      = new Memory();
      this.dumpCnt	  = 0;
      this.fiCnt       = 0;
      this.saveF 	     = Global.getExtra().get(C.DB_SAVE_F);
      this.itTree	     = new ITtree(C.ALG_ECLAT);
      this.text		  = Global.getExtra().get(C.X_LETTERS);
      this.skipCnt	  = 0;
      this.rareCand    = 0;
      this.largestAttr = Database.getTotalNumberOfAttr();
      this.toNull      = Global.getExtra().get(C.X_CORON_NULL);
   }

   /**
    * Controller function. 
    */
   public void start()
   {
      if (Global.memInfo()) System.err.println("> Memory usage (after reading the dataset horizontally, without gc()): "+Convert.byteToPrettyString(this.memory.testMemUsage()));
      CharmExtra charmExtra = new CharmExtra(this.database, this.min_supp);
      this.f2Matrix = charmExtra.getF2Matrix();
      this.database = charmExtra.convertDBHorizontal2Vertical();
      charmExtra = null;	// not needed anymore
      if (Global.memInfo()) System.err.println("> Memory usage (after finding F_2 and converting vertically, with gc()): "+Convert.byteToPrettyString(this.memory.getGCMemUsage()));
      //System.exit(0);
      //
      
      ITnode root = this.itTree.getRoot();
      ITnode child;
      Vector rootChildren;
      
      initItTree();
      //debug();
      if (Global.followFunctions()) System.err.println("> Searching for frequent itemesets...");
      long startTime = System.currentTimeMillis();
      
      rootChildren = root.getChildren();
      while (rootChildren.size() > 0)
      {
         child = (ITnode) rootChildren.get(0);
         extend(child);
         save(child);
         child.delete();
      }
      if (Global.followFunctions()) { 
         System.err.print("> Searching for frequent itemesets done. ");
         System.err.println("> Elapsed time: "+Statistics.getRuntime(System.currentTimeMillis() - startTime)+" sec.");
      }
      this.f2Matrix 	= null;	// not needed anymore
      //System.err.println(">>> tree's max. size: "+Convert.byteToPrettyString(treeMaxSize));
      
      if (Global.memInfo()) System.err.println("> Memory usage (after finding all FCIs and deleting the IT-tree and the F_2 matrix, with gc()): "+Convert.byteToPrettyString(this.memory.getGCMemUsage()));      
      if (Global.memInfo()) System.err.println("> Memory usage (after deleting everything, with gc()): "+Convert.byteToPrettyString(this.memory.getGCMemUsage()));
      if (this.toNull==false) 
      {
         System.out.println();
         System.out.println("# FIs: "+Convert.byteToPrettyString(this.fiCnt));
      }
      if (Global.followFunctions()) {
         System.err.println("> Skipped infrequent 2-itemsets: "+Convert.byteToPrettyString(this.skipCnt));
         System.err.println("> Deleted infrequent candidates: "+Convert.byteToPrettyString(this.rareCand));
      }
   }

   /**
    * Extends a node in the IT-tree.
    * 
    * @param curr The node that we want to extend.
    */
   private void extend(ITnode curr)
   {
      //if (this.itTree.size() > this.treeMaxSize) this.treeMaxSize = this.itTree.size();
      Vector currVector = curr.getContainer();
      Vector childrenVector;
      int size = currVector.size();
      ITnode other, candidate, child;
      
      /* 
       * at the 0th position there is the current element
       * we want to make pairs with this and all the others (they start from index 1)
       */
      for (int i = 1; i<size; ++i)		
      {   							
         other = (ITnode) currVector.get(i);
         
         candidate = getCandidate(curr, other);
         if (candidate != null) 
            curr.addChild(candidate);
      }
      curr.sortChildren();
      
      childrenVector = curr.getChildren();
      while (childrenVector.size() > 0)
      {
         child = (ITnode) childrenVector.get(0);
         this.extend(child);
         save(child);
         child.delete();
      }      
   }

   /**
    * Creates a frequent concept. 
    * If the support of a candidate is not enough, it returns null.
    * 
    * @param curr Current node.
    * @param other The other node.
    * @return Null, if the candidate has not enough support. Otherwise it
    * returns a frequent concept produced from the two nodes.
    */
   private ITnode getCandidate(ITnode curr, ITnode other)
   {
      BitSet intent1 = curr.getIntent(),
             intent2 = other.getIntent();
      
      if (Global.getUseF2())
      {
         if ((intent1.cardinality()==1) && (intent2.cardinality()==1)) 		// if we are at level 1
         {
            int a = intent1.nextSetBit(0),
                b = intent2.nextSetBit(0);
            //System.err.println(">>> "+min+", "+max);
            if (this.f2Matrix[Math.min(a,b)][this.largestAttr - Math.max(a,b)] < this.min_supp)
            {
               ++this.skipCnt;
               return null;
            }
         }
      }
      
      // else, if we are below level 1
      int supp;
      BitSet cand_extent = (BitSet) curr.getExtent().clone();
         cand_extent.and(other.getExtent());
      if ((supp = cand_extent.cardinality()) < this.min_supp)
      {
         ++this.rareCand;
         return null;
      }
      // else, if it's surely frequent 
      BitSet cand_intent = (BitSet) intent1.clone();
      	cand_intent.or(intent2);
      
      ITnode candidate = new ITnode(itTree);
      candidate.setIntent(cand_intent);
      candidate.setExtent(cand_extent);
      candidate.setSupp(supp);
      	
      return candidate;
   }
   
   /**
    * Saves the node before deleting from memory.
    * 
    * @param node The node that we want to save.
    */
   private void save(ITnode node)
   {
      /*
       * Do we want to save FIs in the memory? AssRuleX may need it for instance.
       */
      if (this.saveMemF)
      {
         Row_Universal_F row = new Row_Universal_F(new Row(node.getIntent(), node.getSupp()));
         this.result2.add(row);
      }
      
      // if printing the result is not suppressed
      if (this.toNull==false) 
      {
         if (this.text) System.out.println(node.toStringName());
         else           System.out.println(node);
      }
      
      ++this.fiCnt;        // we found one more FI
   }
   
   /**
    * Initialization of the IT-tree.
    * Create root node + add frequent one-size attributes to level 1. 
    */
   private void initItTree()
   {
      BitSet set;
      ITnode root = this.itTree.getRoot();
      Vector db = Database.getDatabase(); 
      int size = db.size();
      
      for (int i = 0; i < size; ++i)
      {
         set = (BitSet) db.get(i);
         if (set.cardinality() >= this.min_supp) root.addChild(new ITnode(this.itTree, i, set));
      }
      Database.freeDatabase();		// delete the vertical representation, not needed anymore
      root.sortChildren();
   }

   /** (non-Javadoc)
    * @see fr.loria.coronsys.coron.algorithm.Algorithm#getResult()
    */
   public Result getResult() {
      return this.result;
   }
   
   /**
    * Some debug information.
    */
   private void debug()
   {
      //this.itTree.depthFirstTraversal(this.itTree.getRoot());
      //this.itTree.breadthFirstTraversal(this.itTree.getRoot());
      //System.err.println("Number of nodes in the IT-tree: "+this.itTree.size());
      //System.err.println("===================================================");
      //System.exit(0);
      // off debug
      // on debug
      //this.itTree.deepFirstTraversal(root);
      //System.exit(0);
      ITnode node;
      for (Enumeration e = this.itTree.getRoot().getChildren().elements(); e.hasMoreElements(); )
      {
         node = (ITnode) e.nextElement();
         System.err.println(node);
      }
      System.exit(0);
   }

   /**
    * Get the number of FIs.
    * 
    * @return Number of FIs.
    */
   public long getFiCnt() {
      return this.fiCnt;
   }

   /**
    * Get the number of FCIs.
    * Since Eclat finds FIs, this function has no sense.
    * 
    * @return Number of FCIs.
    */
   public long getFciCnt() {
      return 0;
   }

   /**
    * Get the Result2 object. Needed for AssRuleX.
    * 
    * @return Returns the Result2 object.
    */
   public Result2 getResult2() {
      return this.result2;
   }

   /**
    * It is called from AssRuleX. It sets to suppress output to >/dev/null,
    * i.e. no output to stdout.
    */
   public void setToNull() {
      Global.getExtra().set(C.X_CORON_NULL);
      this.toNull = true;
   }
   
   /**
    * It is called from AssRuleX. It sets to save F tables in memory.
    * AssRuleX will take that over.
    */
   public void setSaveMemF() {
      Global.getExtra().set(C.MEM_SAVE_F);
      this.saveMemF = true;
   }
}
