package fr.loria.coronsys.coron.datastructure.charm;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

import fr.loria.coronsys.coron.datastructure.Row;
import fr.loria.coronsys.coron.datastructure.Row_predSupp;
import fr.loria.coronsys.coron.datastructure.Table;
import fr.loria.coronsys.coron.datastructure.close.Table_Close_FCC;
import fr.loria.coronsys.coron.datastructure.pascal.Row_Pascal_F;
import fr.loria.coronsys.coron.datastructure.pascal.Table_Pascal_F;
import fr.loria.coronsys.coron.datastructure.pascal.Table_Pascal_FC;
import fr.loria.coronsys.coron.datastructure.trie.Trie;
import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.Database;
import fr.loria.coronsys.coron.helper.Global;
import fr.loria.coronsys.coron.helper.Statistics;

/**
 * Class for extra things for Charm.
 * Like: determining frequent 2-patterns in horizontal database (using a modified Pascal).
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
*/
public class CharmExtra
{
   /**
    * The dataset in horizontal format.
    */
   private Vector<BitSet> database;
   
   /**
    * Minimum support.
    */
   private int min_supp;
   
   /**
    * It'll contain all the attributes.
    */
   private BitSet attributes;
   
   /**
    * Constructor.
    * 
    * @param database The dataset in horizontal format.
    * @param min_supp Minimum support.
    */
   public CharmExtra(Vector<BitSet> database, int min_supp) 
   {
      this.database     = database;
      this.attributes   = new BitSet();
      init_attributes(database);
      this.min_supp     = min_supp;
   }

   /**
    * Finds frequent 2-itemsets in the database, and
    * returns them in a trie.
    * 
    * @return A trie with frequent 2-long itemsets.
    */
   public Trie getF2Trie()
   {
      if (Global.followFunctions()) System.err.println("Calculating frequent 2-itemesets...");
      long startTime = System.currentTimeMillis();
      Trie trie = new Trie();
      Table_Pascal_FC fc_1;
      Table_Pascal_F  f_1;
      Table_Pascal_FC fc_2;
      
      init_FC_1(fc_1 = new Table_Pascal_FC());
      SupportCount(fc_1);
      f_1 = new Table_Pascal_F();
      prune_FC_i(1, fc_1, f_1);
      fc_1.clear();
      fc_2 = fc_1;
      pascal_gen_mod(1, f_1, fc_2);     // filling up the generator field of FC_2 using F_1
      f_1 = null;
      if (fc_2.isEmpty()) return trie;
      if (fc_2.hasKeyItemset()) { 
         SupportCount(fc_2);
      }
      register_frequent(fc_2, trie);
      
      if (Global.followFunctions()) {
         System.err.print("Calculating frequent 2-itemesets: done. ");
         System.err.println("Elapsed time: "+Statistics.getRuntime(System.currentTimeMillis() - startTime)+" sec.");
      }
      return trie;
   }
   
   /**
    * Calculates frequent 2-patterns and stores them in a triangular matrix.
    * 
    * @return Frequent 2-patterns stored in a triangular matrix.
    */
   public int[][] getF2Matrix()
   {
      if (Global.followFunctions()) System.err.println("> Calculating frequent 2-itemesets...");
      long startTime = System.currentTimeMillis();
      int max = Database.getTotalNumberOfAttr();
      int[][] matrix = new int[max][];
      
      for (int i = 0; i < matrix.length; ++i)
         matrix[i] = new int[max-i];
      
      BitSet set;
      int first, second;
      for (Enumeration<BitSet> e = this.database.elements(); e.hasMoreElements(); )
      {
         set = e.nextElement();
         if (set.cardinality() < 2) continue;
         
         for (first=set.nextSetBit(0); first>=0; first=set.nextSetBit(first+1)) 
            for (second = set.nextSetBit(first+1); second>=0; second=set.nextSetBit(second+1))
               ++matrix[first][max-second];
      }
      
      //printMatrix(matrix);
      //System.err.println("DB: "+this.database);
      //System.err.println("Largest attribute: "+Database.getLargestAttr());
      if (Global.followFunctions()) {
         System.err.print("> Calculating frequent 2-itemesets: done. ");
         System.err.println("Elapsed time: "+Statistics.getRuntime(System.currentTimeMillis() - startTime)+" sec.");
      }
      return matrix;
   }
   
   /**
    * Collect all the attributes in lexicographic order and return the maximal attribute.
    * 
    * @param v Vector, containing the input file's lines as bitsets.
    * @return Maximal attribute.
    */
   private int init_attributes(Vector<BitSet> v)    
   {
      for (Enumeration<BitSet> e = v.elements(); e.hasMoreElements(); )
         this.attributes.or(e.nextElement());
      return (attributes.length());
   }
   
   /**
    * Initializes the FC_1 table.
    * 
    * @param fc The FC_1 table.
    */
   private void init_FC_1(Table_Pascal_FC fc)
   {
      BitSet set;
      Trie fc_trie = fc.getTrie();
      Row_predSupp row;
      
      for (int i = attributes.nextSetBit(0); i>=0; i = attributes.nextSetBit(i+1)) 
      {
         set = new BitSet();
         set.set(i);
         row = fc.add_row(set);
         fc_trie.add(row.getItemset(), row);
      }
   }
   
   /**
    * Counts the support of itemsets in the table.
    * 
    * @param fc An FC table in which we want to count the support of the itemsets.
    */
   private void SupportCount(Table_Pascal_FC fc)
   {
      Vector<Row> g_0;
      BitSet f_o, generator;
      Enumeration<BitSet> e;
      Enumeration<Row> ge;
      Row row;

      for (e = this.database.elements(); e.hasMoreElements(); )
      {
         f_o = e.nextElement();
         g_0 = fc.getSubsetsOf(f_o);     // trickiest part in the whole stuff :))
                                          // speed mainly depends on this!!!
         for (ge = g_0.elements(); ge.hasMoreElements(); )
         {
            row = (Row)ge.nextElement();
            row.incSupp();
         }
      }
      
      fc.freeTrie();    // we need it no more
   }
   
   /**
    * Pruning the FC table and copying rows (whose support is >= minsupport) to the F table.
    * 
    * @param i Which iteration we are at.
    * @param from The FCC table that we want to prune (clean from itemsets whose support is less than the minsupport).
    * @param to We copy itemsets (whose support >= minsupport) from the FCC table here, in this FC table. 
    */
   private void prune_FC_i(final int i, Table_Pascal_FC from, Table_Pascal_F to)
   {
      Row_predSupp row;
      int objSize = this.database.size();

      for (Enumeration<Row> e = from.getRows().elements(); e.hasMoreElements(); )
      {
         row = (Row_predSupp)e.nextElement();
         if ( row.getSupp() >= this.min_supp )        // support is enough 
         {  
            if (row.getSupp() == objSize) {
               row.setKey(false);
            }
            to.add_row(row);                          // insert in F_i
         }
      }      
   }
   
   /**
    * Calculates good candidates for the new FCC table.
    *  
    * @param i At which iteration we are at.
    * @param f We use this FC table to generate candidates for the next FCC table.
    * @param fc This FCC table's generator field will be filled.
    */
   private void pascal_gen_mod(final int i, Table_Pascal_F f, Table_Pascal_FC fc)
   {
      int[][] generators = f.getGenerators();      // get the generators of FC_i in a 2D-array
      boolean ok = true, test;      // set 'ok' for the compiler
      int l, k, c;
      BitSet candidate;    // candidate generator
      boolean prune_1 = false;

      for (l = 0; l < generators.length-1; ++l)
      {
         middle:
         for (k = l+1; k < generators.length; ++k)
         {
            for (c = 0; c < i-1; ++c)
               if ( generators[l][c] != generators[k][c] ) break middle;  // if they are not equal
                                                                          // continue in the 1st column      
            if (generators[l][i-1] < generators[k][i-1])       // OK, if the 1st number < the 2nd
            {  // optimization comes here: test the candidate here! If tests passed => add to FCC_i+1
               candidate = Table.createCandidate(generators[l], generators[k][i-1]);
               if ( (prune_1 = gen_generator_prune_1_test(candidate, f))) 
                  fc.add_row(candidate); 
            }
         }
      }
      
      // OK, FC_i+1 contains generators that are potentially frequent
      // Now comes phase 2:
      Row_predSupp row;
      Vector<BitSet> i_subsets;
      Enumeration<Row> e;
      Enumeration<BitSet> in;
      BitSet sub;
      Row_Pascal_F subItemset;
      Trie f_trie  = f.getTrie(),
           fc_trie = fc.getTrie();
      
      for (e = fc.getRows().elements(); e.hasMoreElements(); )
      {
         row = (Row_predSupp) e.nextElement();
         i_subsets = Table_Close_FCC.get_i_subsets(row.getItemset());
         for (in = i_subsets.elements(); in.hasMoreElements(); )
         {
            sub = in.nextElement();
            subItemset = (Row_Pascal_F)f_trie.getRowOf(sub);
            row.setPredSupp(Math.min(row.getPredSupp(), subItemset.getSupp()));
            if (subItemset.isKey() == false) row.setKey(false);
         }
         if (row.isKey() == false) row.setSupp(row.getPredSupp());
         else fc_trie.add(row.getItemset(), row); 
      }
   }
   
   /**
    * Prune test #1 for the candidate generators.
    * 
    * @param candidate A potential candidate for the new FCC table that needs to be tested.
    * @param fc The FC table which was used to generate the candidate.
    * @return A boolean value showing if the candidate passed this first test.
    */
   private boolean gen_generator_prune_1_test(BitSet candidate, Table fc)
   {
      BitSet s;
      Trie trie = fc.getTrie();
      
      Vector<BitSet> i_subsets = Table_Close_FCC.get_i_subsets(candidate);
      for (Enumeration<BitSet> e = i_subsets.elements(); e.hasMoreElements(); )
      {
         s = e.nextElement();
         if (trie.contains(s) == false) return false;
      }
      return true;
   }
   
   /**
    * Registers frequent 2-itemsets in the trie.
    * 
    * @param fc FC table with 2-itemsets.
    * @param trie The trie in which we want to register frequent 2-itemsets.
    */
   private void register_frequent(Table_Pascal_FC fc, Trie trie)
   {
      Row_predSupp row;

      for (Enumeration<Row> e = fc.getRows().elements(); e.hasMoreElements(); )
      {
         row = (Row_predSupp)e.nextElement();
         if ( row.getSupp() >= this.min_supp )        // support is enough 
            trie.add(row.getItemset(), null);
      }      
   }

   /**
    * Converts the database from horizontal to vertical representation.
    * Registers it too.
    * 
    * @return Returns the vertical database.
    */
   @SuppressWarnings("unchecked")
   public Vector<BitSet> convertDBHorizontal2Vertical()
   {
      // if the DB is already in vertical format, then return it
      if (Database.isDBRepresentationVertical()) return Database.getDatabase();
      // else
      
      if (Global.followFunctions()) System.err.println("> Converting DB from horizontal to vertical...");
      int sizeH = this.database.size();      // size of horizontal DB
      int sizeV;                             // size of vertical DB
      Vector vertV = new Vector();           // vertical vector, to be built
      BitSet set;                            // a line in the horizontal DB
      int attr, j, row;
      
      long startTime = System.currentTimeMillis();
      for (row=0; row < sizeH; ++row)        // loop over the rows of the horizontal DB
      {
         set = (BitSet) this.database.get(row);
         // loop over the attributes of a line
         for (attr = set.nextSetBit(0); attr >= 0; attr = set.nextSetBit(attr+1))
         {
            // dynamic expansion of the vector
            if ((sizeV = vertV.size()) < (attr+1)) {
               for (j=0; j<(attr+1-sizeV); ++j) vertV.add(new BitSet());
            }
            ((BitSet) vertV.get(attr)).set(row+1);
         }
         // when a line was processed in the horizontal DB, delete it
         // we don't need it anymore, so destroy it parallely to save memory
         this.database.set(row, null);
      }
      if (Global.followFunctions()) {
         System.err.print("> Converting DB from horizontal to vertical: done. ");
         System.err.println("Elapsed time: "+Statistics.getRuntime(System.currentTimeMillis() - startTime)+" sec.");
      }
      
      // register the new vertical database
      Database.registerDatabase(vertV, C.DBR_VERTICAL);
      //Database.setDBRepresentation(C.DBR_VERTICAL);
      return Database.getDatabase();
   }

   /**
    * Converts the database from vertical to horizontal representation.
    * Registers it too.
    * 
    * @return Returns the horizontal database.
    */
   @SuppressWarnings("unchecked")
   public Vector convertDBVertical2Horizontal()
   {
      // if the DB is already in horizontal format, then return it
      if (Database.isDBRepresentationHorizontal()) return Database.getDatabase();
      // else
      
      if (Global.followFunctions()) System.err.println("> Converting DB from vertical to horizontal...");
      int sizeH = this.database.size();      // size of horizontal DB
      int sizeV;                             // size of vertical DB
      Vector vertV = new Vector();           // vertical vector, to be built
      BitSet set;                            // a line in the horizontal DB
      int attr, j, row, k;
      
      long startTime = System.currentTimeMillis();
      for (row=0; row < sizeH; ++row)        // loop over the rows of the horizontal DB
      {
         set = (BitSet) this.database.get(row);
         // loop over the attributes of a line
         for (attr = set.nextSetBit(0); attr >= 0; attr = set.nextSetBit(attr+1))
         {
            // dynamic expansion of the vector
            if ((sizeV = vertV.size()) < (attr+1)) {
               for (j=0; j<(attr+1-sizeV); ++j) vertV.add(new BitSet());
            }
            ((BitSet) vertV.get(attr)).set(row);
         }
         // when a line was processed in the horizontal DB, delete it
         // we don't need it anymore, so destroy it parallely to save memory
         this.database.set(row, null);
      }
      if (((BitSet)vertV.get(0)).cardinality() == 0)
         vertV.remove(0);
      
      if (Global.followFunctions()) {
         System.err.print("> Converting DB from vertical to horizontal: done. ");
         System.err.println("Elapsed time: "+Statistics.getRuntime(System.currentTimeMillis() - startTime)+" sec.");
      }
      
      // register the new horizontal database
      Database.registerDatabase(vertV, C.DBR_HORIZONTAL);
      //Database.setDbRepresentation(C.DBR_HORIZONTAL);
      return Database.getDatabase();
   }
   
   /**
    * Prints the values of the matrix. For debug purposes.
    * 
    * @param matrix The triangular matrix we want to display.
    */
   public void printMatrix(int[][] matrix)
   {
      int i, j;
      
      for (i=0; i<matrix.length; ++i)
      {
         for (j=0; j<matrix[i].length; ++j)
         {
            System.err.print(matrix[i][j]+", ");
         }
         System.err.println();
      }
      System.err.println();
   }
}
