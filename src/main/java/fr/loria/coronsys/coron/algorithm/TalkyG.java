package fr.loria.coronsys.coron.algorithm;

import java.util.BitSet;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.Result;
import fr.loria.coronsys.coron.datastructure.Result2;
import fr.loria.coronsys.coron.datastructure.carpathiag.Table_CarpathiaG_F;
import fr.loria.coronsys.coron.datastructure.charm.CharmExtra;
import fr.loria.coronsys.coron.datastructure.charm.ITnode;
import fr.loria.coronsys.coron.datastructure.charm.ITtree;
import fr.loria.coronsys.coron.datastructure.charm.MyHash;
import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.Convert;
import fr.loria.coronsys.coron.helper.Database;
import fr.loria.coronsys.coron.helper.DemoCoron;
import fr.loria.coronsys.coron.helper.Global;
import fr.loria.coronsys.coron.helper.Memory;
import fr.loria.coronsys.coron.helper.Statistics;
import fr.loria.coronsys.coron.helper.Warning;

/** 
 * Talky-G
 * 
 * Like Eclat-G, but the traversal is changed to reverse pre-order.
 * 
 * Output: FGs (frequent generators). It uses a hash structure similar to
 * Charm's.
 *  
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class TalkyG 
implements Algorithm
{
   /**
    * Contains lines of the input file as bitsets.
    */
   private Vector<BitSet> database;
   
   /**
    * Min. support as an integer number.
    */
   private int min_supp;
      
   /**
    * We'll access the final result (list of FGs) through this object.
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
    * Number of FGs.
    */
   private int fgCnt;
   /**
    * Save the F tables in a database?
    */
   private boolean saveF;
   
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
    * Is there a full column in the input dataset? It'll be interesting
    * for the empty set. The empty set is by definition a minimal generator.
    * If there is a full column, its minimal generator will be the empty set.
    */
   private boolean fullColumn;
   
   /**
    * Number of objects in the input dataset.
    */
   private int databaseSize;
   
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
   
   /**
    * Hashtable to store FGs.
    */
   private MyHash myHash;
   
   /**
    * Delete database during the process? Default: yes.
    */
   private boolean deleteDatabase;
   
   /**
    * Should the result (the list of FGs) be sorted?
    * It means: sort FGs in ascending order by size.
    * Default value: false (don't sort).
    */
   private boolean sort;
   
   /**
    * At the end the hashtable (that stores FGs) is converted in
    * this format. 
    */
   private Table_CarpathiaG_F frequentGenerators;
   
   /**
    * Convert attribute numbers to names?
    */
   private boolean name;

   /**
    * Delete the hash structure? Default value: yes, destroy it.
    */
   private boolean destroyHash;

   /**
    * When FGs are found, they are in a hash. Should this hash be processed,
    * i.e. itemsets stored in the hash are read and stored in a table.
    * Default: NO, do not process the hash.
    */
   private boolean processHash;
   
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
   public TalkyG(Vector<BitSet> database, int min_supp)
   {
      Warning.warning("Talky-G v2 performs a bit better");
      //
      DemoCoron.checkAlg(C.ALG_TALKY_G);
      //
      this.database           = database;
      this.min_supp           = min_supp;
      this.result             = new Result();
      this.memory             = new Memory();
      this.dumpCnt	         = 0;
      this.fgCnt              = 0;
      this.saveF 	            = Global.getExtra().get(C.DB_SAVE_F);
      // Talky-G is a variant of Eclat-G, so the IT-tree
      // is constructed the same way
      this.itTree	            = new ITtree(C.ALG_TALKY_G);
      this.text		         = Global.getExtra().get(C.X_LETTERS);
      this.skipCnt	         = 0;
      this.rareCand           = 0;
      this.largestAttr        = Database.getTotalNumberOfAttr();
      this.databaseSize       = Database.getDatabase().size();
      this.fullColumn         = false;
      this.toNull             = Global.getExtra().get(C.X_CORON_NULL);
      this.myHash             = new MyHash();
      this.deleteDatabase     = true;
      this.sort               = false;
      this.frequentGenerators = null;
      this.name               = Global.getExtra().get(C.X_LETTERS);
      this.destroyHash        = true;
      this.processHash        = false;
   }

   /**
    * Controller function. 
    */
   public void start()
   {
      if (Global.memInfo()) System.err.println("> Memory usage (after reading the dataset horizontally, without gc()): "+Convert.byteToPrettyString(this.memory.testMemUsage()));
      CharmExtra charmExtra = new CharmExtra(this.database, this.min_supp);
      /*
       * if the usage of the 2D matrix is enabled
       */
      if (Global.getUseF2()) {
         this.f2Matrix = charmExtra.getF2Matrix();
      }
      //System.out.println(">talky-g: " + this.f2Matrix);
      
      this.database = charmExtra.convertDBHorizontal2Vertical();
      charmExtra = null;	// not needed anymore
      if (Global.memInfo()) System.err.println("> Memory usage (after finding F_2 and converting vertically, with gc()): "+Convert.byteToPrettyString(this.memory.getGCMemUsage()));
      //System.exit(0);
      //
      
      ITnode root = this.itTree.getRoot();
      ITnode child;
      Vector<ITnode> rootChildren;
      int size, pos;
      
      initItTree();
      if (this.fullColumn)  // then the empty set is a useful frequent generator
      {
         saveFg(this.itTree.getRoot());
      }
      //debug();
      if (Global.followFunctions()) System.err.println("> Searching for FGs...");
      long startTime = System.currentTimeMillis();
      
      rootChildren = root.getChildren();
      size = rootChildren.size();
      for (pos = size - 1; pos >= 0; --pos)          // traversal from right-to-left
      {
         child = (ITnode) rootChildren.get(pos);
         saveFg(child);
         extend(child, pos);
      }
      
      {
         // free the IT-tree
         root.deleteChildren();
         this.itTree = null;
      }
      
      if (Global.followFunctions()) { 
         System.err.print("> Searching for FGs done. ");
         System.err.println("Elapsed time: "+Statistics.getRuntime(System.currentTimeMillis() - startTime)+" sec.");
      }
      this.f2Matrix 	= null;	// not needed anymore
      //System.err.println(">>> tree's max. size: "+Convert.byteToPrettyString(treeMaxSize));
      
      /*
       * OK, print the result
       */
      // ON: debug, show the contents of the hash structure
      //this.myHash.printInfo();
      // OFF: debug
      
      /*if (this.processHash) {
         this.frequentGenerators = this.getFrequentGenerators();
      }*/
      
      /* if (this.toNull == false)
      {    
         if (this.name == false) this.frequentGenerators.toStringDump();
         else                    this.frequentGenerators.toStringDumpName();
      }*/
      
      if (Global.memInfo()) System.err.println("> Memory usage (after finding all FGs and deleting the IT-tree and the F_2 matrix, with gc()): "+Convert.byteToPrettyString(this.memory.getGCMemUsage()));      
      if (Global.memInfo()) System.err.println("> Memory usage (after deleting everything, with gc()): "+Convert.byteToPrettyString(this.memory.getGCMemUsage()));
      if (this.toNull==false) 
      {
         System.out.println();
         System.out.println("# FGs: "+Convert.byteToPrettyString(this.fgCnt));
         if (this.fullColumn) 
            System.out.println("# There IS a full column in the input dataset.");
         else
            System.out.println("# There is NO full column in the input dataset.");
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
    * @param currPos Index position of the current node. It is used
    * to access its brothers on its right side.
    */
   private void extend(ITnode curr, final int currPos)
   {
      //if (this.itTree.size() > this.treeMaxSize) this.treeMaxSize = this.itTree.size();
      Vector<ITnode> currVector;
      Vector<ITnode> childrenVector;
      int sizeCurrV;
      int sizeChildrenV;
      ITnode other, generator, child;
      
      /* 
       * At currPos there is the current element.
       * We want to make pairs with this and all its brothers that
       * are on its right side.
       */
      currVector = curr.getContainer();
      sizeCurrV = currVector.size();
      for (int posOther = currPos + 1; posOther < sizeCurrV; ++posOther)
      {                       
         other = (ITnode) currVector.get(posOther);
         
         generator = getNextGenerator(curr, other);
         if (generator != null) 
            curr.addChild(generator);
      }
      curr.sortChildren();
      
      //
      
      childrenVector = curr.getChildren();
      sizeChildrenV = childrenVector.size();
      for (int posChild = sizeChildrenV - 1; posChild >= 0; --posChild)
      {
         child = (ITnode) childrenVector.get(posChild);
         saveFg(child);
         extend(child, posChild);
      }    
      // free memory
      curr.deleteChildren();
   }

   /**
    * Creates a new frequent generator or returns null. 
    * 
    * @param curr Current node.
    * @param other The other node.
    * @return Null, if the candidate is not an FG. Otherwise it
    * returns a frequent generator produced from the two nodes.
    */
   private ITnode getNextGenerator(ITnode curr, ITnode other)
   {
      BitSet intent1 = curr.getIntent(),
             intent2 = other.getIntent();
      
      /*
       * This part here uses the 2D matrix to determine the support
       * of 2-itemsets.
       */
      if (Global.getUseF2())
      {
         /*
          * if the usage of the 2D matrix is enabled:
          */
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
   
      /*
       * Else, if 
       *    - we are below level 1, or
       *    - the usage of the 2D matrix is disabled  
       */
      int supp;
      BitSet cand_extent = (BitSet) curr.getExtent().clone();
         cand_extent.and(other.getExtent());
      if ((supp = cand_extent.cardinality()) < this.min_supp)
      {
         ++this.rareCand;
         return null;
      }
      
      // check if it's surely not generator
      if (cand_extent.equals(curr.getExtent()) || cand_extent.equals(other.getExtent())) 
      {
         // it's not a generator because it has  
         // a proper subset with the same support
         return null;
      }
      
      // else, if it's a potential generator 
      BitSet cand_intent = (BitSet) intent1.clone();
      	cand_intent.or(intent2);
      
      ITnode cand = new ITnode(itTree);   // "cand" like candidate
      cand.setIntent(cand_intent);
      cand.setExtent(cand_extent);
      cand.setSupp(supp);
      
      /* 
       * Check if it's really a generator, i.e.:
       * did we find before a proper subset of it with the same support?
       */
      if (this.myHash.containsSupersetOrSubsetOf(cand, MyHash.SUBSET)) {
         return null;
      }
      	
      // else, if it's really a generator
      return cand;
   }
   
   /**
    * Save the node before deleting from memory. The node represents an FG.
    * 
    * @param node The node (FG) that we want to save.
    */
   private void saveFg(ITnode node)
   {      
      this.myHash.add(node);
      ++this.fgCnt;
      
      // if printing the result is not suppressed
      if (this.toNull==false) 
      {
         if (this.text) System.out.println(node.toStringName());
         else           System.out.println(node);
      }
      
      //debug on
      //System.out.println(">> hash value: " + node.getHash()
      //      + "; tidset: " + node.getExtent());
      //debug off
   }

   /**
    * Initialization of the IT-tree.
    * Create root node + add frequent one-size attributes to level 1. 
    */
   private void initItTree()
   {
      BitSet set;
      ITnode root = this.itTree.getRoot();
      Vector<BitSet> db = Database.getDatabase(); 
      int size = db.size();
      int card;
      //boolean frequent, key;
      boolean first = true;
      
      for (int i = 0; i < size; ++i)
      {
         set = (BitSet) db.get(i);
         card = set.cardinality();
         // if it's frequent AND key (if card == this.databaseSize, then its key is the empty set)
         if ((card >= this.min_supp) && (card < this.databaseSize)) 
            root.addChild(new ITnode(this.itTree, i, set));
         
         if (card == this.databaseSize) 
         {
            this.fullColumn = true;
            /*
             * if there is a full column, then the empty set IS an important
             * generator. We need to count its hash value. The following code below
             * needs to be executed just once (the variable 'first' is for this purpose)
             */
            if (first)
            {
               root.setExtent(set);             // register full column as its extent
               root.getHash();                  // its hash will be calculated and stored
               root.setExtent(new BitSet());    // remove full column, not needed anymore (free some memory)
               first = false;
            }
         }
      }
      
      if (this.deleteDatabase) {
         Database.freeDatabase();      // delete the vertical representation, not needed anymore
      }
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
    * Show the contents of the hash structure.
    */
   /*private void showHash()
   {
      System.err.println(">>>");
      System.out.print(this.myHash);
      System.err.println("<<<");
   }*/

   /**
    * Get the number of FIs. Not known, we only find FGs.
    * 
    * @return Number of FIs. Not known, we only find FGs.
    */
   public long getFiCnt() {
      return (-1);
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
   
   
   /** (non-Javadoc)
    * @see fr.loria.coronsys.coron.algorithm.Algorithm#getResult2()
    */
   public Result2 getResult2()
   {
      return null;
   }
   
   /**
    * Eclat-G needs to store FGs in a hash table. It is used to
    * filter out duplicates. When calculation is finished this hash table will be converted
    * to a format which is edible.
    * 
    * @return Returns the FGs in a proper table object.
    */
   /*public Table_CarpathiaG_F getFrequentGenerators()
   {
      if (this.frequentGenerators == null)
         fillTableAndDestroyHash();
      
      return this.frequentGenerators;
   }*/

   /**
    * Reads FGs from the hash table and stores them in a table.
    */
   /*private void fillTableAndDestroyHash()
   {
      this.frequentGenerators = new Table_CarpathiaG_F(false);
      
      Vector<Vector<HashElem>> fciV = this.myHash.getHashtable();
      Vector<HashElem> list;
      Enumeration<HashElem> in;
      HashElem elem;
      Row_CarpathiaG_F row;
      
      for (Enumeration<Vector<HashElem>> e = fciV.elements(); e.hasMoreElements(); )
      {
         list = e.nextElement();
         if (list == null) continue;
         // else
         for (in = list.elements(); in.hasMoreElements(); )
         {
            elem = in.nextElement();
            row = new Row_CarpathiaG_F(elem.getIntent(), elem.getSupp());
            this.frequentGenerators.add(row);
         }
         
         if (destroyHash) {
            list.clear();      // delete elements in the hash
         }
      }
      
      if (this.destroyHash) {
         this.freeHash();   // the hash is not needed anymore
      }
   }*/

   /**
    * Free the memory space occupied by the hash. 
    */
   public void freeHash() {
      this.myHash = null;
   }
   
   /**
    * It is called from AssRuleX. It sets to suppress output to >/dev/null,
    * i.e. no output to stdout.
    */
   public void setToNull()
   {
      Global.getExtra().set(C.X_CORON_NULL);
      this.toNull = true;
   }

   /**
    * Should the database be deleted during the process?
    */
   public void dontDeleteDatabase() {
      this.deleteDatabase = false;
   }
   
   /**
    * Sort FGs in ascending order by size (length).
    */
   public void setSort() 
   {
      Global.getExtra().set(C.X_SORT);
      this.sort = true;
   }

   /**
    * @return Hash table of FGs. Used by the TUS algorithm.
    */
   public MyHash getFgHash() {
      return this.myHash;
   }

   /**
    * Don't delete the hash structure. Called from TUS.
    */
   public void dontDestroyHash() {
      this.destroyHash = false;
   }

   /**
    * Don't process the hash, i.e. don't read its elements
    * and don't store them in a table. Called from TUS.
    */
   public void dontProcessHash() {
      this.processHash = false;
   }
}
