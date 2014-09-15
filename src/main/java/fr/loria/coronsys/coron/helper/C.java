package fr.loria.coronsys.coron.helper;

/**
 * Some constants.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */

/*
 * Categories:                                           Values:
 * ===========                                           ==========
 * anything	(mix)                                        any
 * memory usage                                          101 - 150
 * error messages                                        151 - 200
 * verbosity messages                                    201 - 250
 * extra settings                                        251 - 300
 * GraphViz .dot errors                                  301 - 350
 * different file types and their error codes            351 - 400
 * error codes for .rcf files                            401 - 450
 * algorithms #1                                         451 - 500
 * database/memory related (Hibernate, HSQLDB, etc.)     501 - 550
 * rule settings (for AssRuleX)                          551 - 600
 * algorithms #2                                         601 - 700
 */
public class C
{  
   // ******************************************************
   // constants that do not fall in any category
   // Allowed values: any
   // ******************************************************

   /**
    * Soemthing is not specified.
    */
   public static final int UNDEFINED = 0;
   
   /**
    * Version information.
    * Important! It must contain 4 digits! 
    */
	public static final String VERSION = "0.8.0.0";
	
	/**
	 * Put a hasmark after having found X links between concepts in the Galois lattice.
	 */
	public static final int HASH_STEP = 100; 
	
	/**
	 * The dataset is stored horizontally.
	 */
	public static final int DBR_HORIZONTAL = 1;
	
	/**
	 * The dataset is stored vertically.
	 */
	public static final int DBR_VERTICAL = 2;
   
   /**
    * Size of the hash table (for Charm and its variations).
    */
   public static final int HASH_SIZE = 100000;
   //public static final int HASH_SIZE = 4;   // just for tests
   
   /**
    * Represent itemsets as BitSets.
    */
   public static final int REPR_BITSET = 1;
   
   /**
    * Represent itemsets as TreeSets.
    */
   public static final int REPR_TREESET = 2;
   
   /**
    * Represent itemsets as Vectors.
    */
   public static final int REPR_VECTOR = 3;
   
   /**
    * Shows that the version string belongs to
    * the new version on the server.
    */
   public static final int VERSION_NEW = 1;
   
   /**
    * Shows that the version string belongs to
    * the current version, which is hard-coded in the application.
    */
   public static final int VERSION_CURR = 2;
   

/////////////////////////////////////////////////////////////////////////////
   
   // ******************************************************
   // memory usage
   // Allowed values: 101 - 150
   // ******************************************************
   
   /**
    * Memory info at the beginning of the loop.
    */
   public static final int MEM_START = 101;

   /**
    * Memory info at the end of the loop.
    */
   public static final int MEM_END = 102;

   /**
    * Maximal memory usage.
    */
   public static final int MEM_MAX = 103;
   
/////////////////////////////////////////////////////////////////////////////
	
   // ******************************************************
   // error messages
   // Allowed values: 151 - 200
   // ******************************************************
   
   /**
    * The program should exit without any explication.
    */
   public static final int ERR_JUST_EXIT = 151;

   /**
    * Problem with the 2nd argument (minsupport), which should have the
    * form like: 2, or 2.54%, that is: integer or double. If double, followed
    * by a '%' sign.
    */
   public static final int ERR_SECOND_ARG = 152;

   /**
    * The input file is not found.
    */
   public static final int ERR_FILE_NOT_FOUND = 153;

   /**
    * I/O error while reading the input file.
    */
   public static final int ERR_IO = 154;

   /**
    * Wrong number format in the input file.
    */
   public static final int ERR_WRONG_NUMBER_FORMAT_IN_INPUT_FILE = 155;

   /**
    * Wrong switch format (its correct form is like -v=f for instance).
    */
   public static final int ERR_BAD_SWITCH = 156;

   /**
    * Too many arguments at the end.
    */
   public static final int ERR_MANY_ARGS_AT_END = 157;

   /**
    * Not enough arguments at the end.
    */
   public static final int ERR_LESS_ARGS_AT_END = 158;
   
   /**
    * Not rcf file format
    */
   public static final int ERR_NOT_RCF = 159;
   
   /**
    * The user specified a min_supp value that exceeds the 
    * total number of objects in the database, i.e.
    * the specified min_supp is larger than 100%.
    */
   public static final int ERR_MIN_SUPP_TOO_LARGE = 160;
   
   /**
    * In converter, for instance, the user can choose an
    * output file name. An extension can be added, starting
    * with a dot. If the chosen name is incorrect, the resulting
    * file will be hidden.
    */
   public static final int ERR_HIDDEN_OUTPUT_FILE = 161;
   
   /**
    * Already redirected.
    */
   public static final int ERR_REDIRECTED = 162;
   
/////////////////////////////////////////////////////////////////////////////
   
   // ******************************************************
   // verbosity messages
   // Allowed values: 201 - 250
   // ******************************************************
   
   /**
    * Print memory usage.
    */
   public static final int V_MEMORY = 201;

   /**
    * Print information about function calls.
    */
   public static final int V_FUNCTION = 202;

   /**
    * Measure and print information about running time of the program.
    */
   public static final int V_RUNTIME = 203;

   /**
    * Print contents of FCC tables.
    */
   public static final int V_FCC = 204;

   /**
    * Print contents of FC tables.
    */
   public static final int V_FC = 205;
   
   /**
    * One hashmark for finding a connection in the Galois lattice.
    */
   public static final int V_HASH = 206;

/////////////////////////////////////////////////////////////////////////////
   
   // ******************************************************
   // extra settings
   // Allowed values: 251 - 300
   // ******************************************************
   
   /**
    * Order FCIs by size in ascending order.
 	*/
   public static final int X_ASC = 251;
   
   /**
    * Order FCIs by size in descending order.
 	*/
   public static final int X_DESC = 252;
   
   /**
    * Find all frequent itemsets, like Apriori.
    */
   public static final int X_FI = 253;
   
   /**
    * Find extension for FCIs (FCIs are the intension parts of Galois/iceberg lattices).
    */
   public static final int X_EXT = 254;
   
   /**
    * Print statistics, then stop (exit program).
    */
   public static final int X_STOP_STAT = 255;
   
   /**
    * Find order between concepts => construct Galois lattice.
    */
   public static final int X_LATTICE = 256;
   
   /**
    * No output of result to stdout (like >/dev/null).
    */
   public static final int X_CORON_NULL = 257;
   
   /**
    * Create a GraphViz .dor representation of the trie, which is built up from the found
    * FCIs. By default this output is written to the directory ./trie.tmp
    */
   public static final int X_SHOW_TRIE = 258;
   
   /**
    * Dump partial results in FC tables immediately, don't collect the union of FC tables in memory.
    * It could help to avoid "out of memory" errors.
    */
   public static final int X_DUMP = 259;
   
   /**
    * Indicates that the numbering of attributes in the dataset start from 0.
    * The default is 1.
    * 
    * This was real mess. We finally decided to annihilate this option.
    */
   //public static final int X_POS0 = 260;
   
   /**
    * Do the number -> letter/text conversion in itemsets.
    */
   public static final int X_LETTERS = 261;
   
   /**
    * Sort result. It'll be used in Charm.
    * Result: FCIs will be ordered by ascending order by their length.
    */
   public static final int X_SORT = 262;
   
   /**
    * Show only MFIs.
    * It is used when extracting MFIs with Charm. Normally the output
    * shows all FCIs where MFIs are marked. This option is used to
    * indicate that we only want to see the MFIs.
    */
   public static final int X_SHOW_ONLY_MFI = 263;
    
   //************************************************************************
   /**
    * For algorithms that extract MRIs. Which MRIs do we want?
    * 1) all of them (the ones with support = 0 are included), or
    */
   public static final int X_RARE_ALL = 264;
   
   /**
    * 2) non-zero MRIs, i.e. MRIs whose support > 0
    */
   public static final int X_RARE_NONZERO = 265;
   
   //************************************************************************
   
   /**
    * Uses by BtB. Delete those equivalence classes that are simple, i.e.
    * delete those whose generator and closure are the same itemset.
    */
   public static final int X_DEL_SIMPLE_CLASSES = 266;
   
   /**
    * Print more statistics, then stop (exit program).
    * Similar to -stat, but it gives more info about the dataset.
    */
   public static final int X_STOP_ANALYZE = 267;
   
   /**
    * When -null option is used, unnecessary output to
    * stdout is disabled.
    * However, some algorithms continue printing some info to the stderr.
    * With the option -shutup these outputs can be disabled.
    */
   public static final int X_SHUT_UP = 268;
   
   /**
    * Don't read the database. Used for LCM v2 / GrGrowth.
    */
   public static final int X_DONT_READ_DATABASE = 269;

/////////////////////////////////////////////////////////////////////////////
   
   // **************************************************************************
   // errors that can happen while writing the GraphViz .dot file from the trie
   // Allowed values: 301 - 350
   // **************************************************************************
   
   /**
    * A file with the same name already exists.
    */
   public static final int F_FILE_ALREADY_EXISTS = 301;
   
   /**
    * The directory is write-protected.
    */
   public static final int F_CANNOT_WRITE_DIR = 302;
   
   /**
    * Cannot create directory.
    */
   public static final int F_CANNOT_CREATE_DIR = 303;
   
   /**
    * Cannot delete the file.
    */
   public static final int F_CANNOT_DELETE_FILE = 304;
   
   /**
    * Cannot create the file.
    */
   public static final int F_CANNOT_CREATE_FILE = 305;
   
   /**
    * I/O error while writing to the output file.
    */
   public static final int F_IO = 306;
   
/////////////////////////////////////////////////////////////////////////////
   
   // ******************************************************
   // different file types and their error codes
   // Allowed values: 351 - 400
   // ******************************************************
   
   /**
    * Unknown file format.
    */
   public final static int FT_UNKNOWN = 351;
   
   /**
    * Basenum format.
    */
   public static final int FT_BASENUM = 352;
   
   /**
    * Boolean table.
    */
   public static final int FT_BOOL = 353;
   
   /**
    * Galicia's Rich Context Format.
    */
   public static final int FT_RCF = 354;
   
   /**
    * Used for conversion. It means that no conversion was set.
    */
   public static final int FT_NOTHING = 355;
   
   /**
    * .pbm file format (for visualization)
    * http://netpbm.sourceforge.net/doc/pbm.html
    * 
    * +: very simple, Gimp reads it
    * -: not widely supported (convert doesn't know it)
    * 
    * Use .ppm rather (see below).
    */
   public static final int FT_PBM = 356;
   
   /**
    * It's an .rcf file that indicates similarities
    * and dissimilarities. It makes couples.
    * 
    * Original .rcf : m x n (m lines, n attributes)
    * Square :        (m * (m-1)) x (3 * n)
    * 
    * For difference between "square" and "pairs", 
    * see the explication at FT_PAIRS below.
    * 
    * Used in Cabamaka.
    */
   public static final int FT_SQUARE = 357;
   
   /**
    * .ppm file format (for visualization)
    * http://netpbm.sourceforge.net/doc/ppm.html
    * 
    * +: simple, widely supported (convert too)
    */
   public static final int FT_PPM = 358;

   /**
    * It's an .rcf file that indicates similarities
    * and dissimilarities. It makes pairs.
    * 
    * Original .rcf : m x n (m lines, n attributes)
    * Pairs:          (m * (m-1) / 2) x (3 * n)
    * 
    * Example:
    * ========
    * Suppose we have 3 objects.
    * 
    * Square will produce the following couples:
    * 1-2, 1-3
    * 2-1, 2-3
    * 3-1, 3-2
    * 
    * Pair will produce the following pairs:
    * 1-2, 1-3
    * 2-3
    * 
    * "Pair" will thus produce just the half of "square".
    */
   public static final int FT_PAIRS = 359;
   
/////////////////////////////////////////////////////////////////////////////
   
   // ******************************************************
   // error codes for .rcf files
   // Allowed values: 401 - 450
   // ******************************************************
   
   /**
    * Comment error in the header part of .rcf files.
    */
   public static final int RCF_COMMENT = 401;
   
   /**
    * The necessary first line ("[Relational Context]") is missing.
    */
   public static final int RCF_NO_REL_CONTEXT = 402;
   
   /**
    * The necessary line ("[Binary Relation]") is missing.
    */
   public static final int RCF_NO_BIN_RELATION = 403;
   
   /**
    * In the .rcf file the number of objects in the boolean table
    * is not the same as the number of object names.
    */
   public static final int RCF_OBJ_DIFF = 404;
   
   /**
    * In the .rcf file the number of attributes in the boolean table
    * is not the same as the number of attribute names.
    */
   public static final int RCF_ATTR_DIFF = 405;
   
   /**
    * The necessary last line ("[END Relational Context]") is missing.
    */
   public static final int RCF_NO_END_REL_CONTEXT = 406;
   
/////////////////////////////////////////////////////////////////////////////
   
   // ******************************************************
   // algorithms #1
   // Allowed values: 451 - 500
   // ******************************************************
   
   /**
    * Apriori
    */
   public static final int ALG_APRIORI = 451;
   
   /**
    * Apriori-Close
    */
   public static final int ALG_APRIORI_CLOSE = 452;
   
   /**
    * Close (J-CLOSE)
    */
   public static final int ALG_CLOSE = 453;
   
   /**
    * A-Close
    */
   public static final int ALG_A_CLOSE = 454;
   
   /**
    * Pascal
    */
   public static final int ALG_PASCAL = 455;
   
   /**
    * Pascal+ (own idea, extension)
    */
   public static final int ALG_PASCAL_PLUS = 456;
   
   /**
    * ZART (the ultimate itemset miner :))
    */
   public static final int ALG_ZART_1 = 457;
   
   /**
    * Unknown algorithm.
    */
   public static final int ALG_UNKNOWN = 458;

   /**
    * RMS Carpathia
    */
   public static final int ALG_CARPATHIA = 459;
   
   /**
    * Titanic
    */
   public static final int ALG_TITANIC = 460;
   
   /**
    * Charm, first version, no optimisation
    */
   public static final int ALG_CHARM_1 = 461;
   
   /**
    * Charm, 2nd version, F_2 is determined using horizontal representation.
    * Then the database is converted to vertical representation on-the-fly.
    */
   public static final int ALG_CHARM_2 = 462;
   
   /**
    * Charm, 3rd version
    * Similar to Charm2. The difference is that F_2 is stored now in a 
    * triangular matrix.
    */
   public static final int ALG_CHARM_3 = 463;
   
   /**
    * Charm4, 4th version
    * replace the trie (that stores FCIs) with a hash 
    */
   public static final int ALG_CHARM_4 = 464;
   
   /**
    * Charm4b
    * same as  Charm4, but it doesn't print the list of FCIs to stdout
    */
   public static final int ALG_CHARM_4B = 465;
   
   /**
    * Zaki's Eclat algorithm, 1st version
    */
   public static final int ALG_ECLAT_1 = 466;
   
   /**
    * A slightly optimized version of Zart1. The difference is that
    * the support of 2-itemsets is calculated in an upper triangular matrix.
    * This idea is borrowod from Charm. 
    */
   public static final int ALG_ZART_2 = 467;
   
   /**
    * Like Eclat1, but instead of depth-first, we use breadth-first
    * search strategy in the IT-tree.
    */
   public static final int ALG_ECLAT_2 = 468;
   
   /**
    * Extension of Eclat1. Eclat2 was a catastrophe, breadth-first made
    * it terribly slow. 
    */
   public static final int ALG_SABRE_1 = 469;
   
   /**
    * Eclat-Z: using equivalence classes, minimal generators
    */
   public static final int ALG_ECLAT_Z = 470;
   
   /**
    * A common constant for ALL Charm versions.
    */
   public static final int ALG_CHARM = 471;

   /**
    * A common constant for ALL Eclat versions.
    */
   public static final int ALG_ECLAT = 472;
   
   /**
    * A common constant for ALL Sabre versions.
    */
   public static final int ALG_SABRE = 473;
   
   /**
    * Like Eclat-Z, but partial results are collected in the memory,
    * and when it's getting to be full, we dump everything
    * to a binary temp file.
    */
   public static final int ALG_ECLAT_Z_2 = 474;
   
   /**
    * Eclat-ZT
    * Deprecated. Don't re-assign this value to something else.
    */
   //public static final int ALG_ECLATZT = 475;
   
   /**
    * Eclat-ZV
    * Deprecated. Don't re-assign this value to something else.
    */
   //public static final int ALG_ECLATZV = 476;
   
   /**
    * Eclat-ZH
    * Deprecated. Don't re-assign this value to something else.
    */
   //public static final int ALG_ECLATZH = 477;
   
   /**
    * Eclat-ZVB
    * Deprecated. Don't re-assign this value to something else.
    */
   //public static final int ALG_ECLATZVB = 478;
   
   /**
    * Eclat-ZM
    * Deprecated. Don't re-assign this value to something else.
    */
   //public static final int ALG_ECLATZM = 479;
   
   /**
    * Find rare itemsets with Apriori.
    */
   public static final int ALG_APRIORI_RARE = 480;
   
   /**
    * Like Eclat1, but the IT-tree is processed in a different way,
    * thus this version occupies less memory.
    */
   public static final int ALG_ECLAT_3 = 481;
   
   /**
    * Like Eclat-Z v1, but processes the IT-tree in a different
    * way to use less memory. 
    */
   public static final int ALG_ECLAT_Z_3 = 482;
   
   /**
    * An own idea to check if a candidate is a proper superset
    * of a rare itemset or not.
    */
   public static final int ALG_APRIORI_2 = 483;
   
   /**
    * Stone algorithm for finding rare itemsets.
    */
   public static final int ALG_STONE = 484;
   
   /**
    * ARA algorithm (Apriori-Rare + Arima)
    */
   public static final int ALG_ARA = 485;
   
   /**
    * Arima (A Rare Itemset Miner Algorithm)
    * reconstructs all rare (non-zero) itemsets from MRIs
    */
   public static final int ALG_ARIMA = 486;
   
   /**
    * Eclat-Z LM (low memory).
    * Deprecated. Don't re-assign this value to something else.
    */
   //public static final int ALG_ECLATZLM = 487;
   
   /**
    * Find MFIs (use Charm to find FCIs, then filtering).
    * Deprecated. Do not reuse this value.
    */
   //public static final int ALG_CHARM_MFI = 488;
   
   /**
    * Carpathia-G. Find frequent generators only.
    */
   public static final int ALG_CARPATHIA_G = 489;
   
   /**
    * Carpathia-G-Rare. Find MRGs (=MRIs) only.
    */
   public static final int ALG_CARPATHIA_G_RARE = 490;
   
   /**
    * BtB (Breaking the Barrier).
    */
   public static final int ALG_BTB = 491;
   
   /**
    * Eclat-G. Find frequent generators only.
    */
   public static final int ALG_ECLAT_G = 492;
   
   /**
    * GenClosure. Get FGs from Eclat-G and calculate their closures.
    */
   public static final int ALG_GEN_CLOSURE = 493;
   
   /**
    * Touch (v0): Charm + Eclat-G + association
    */
   public static final int ALG_TOUCH_0 = 494;
   
   /**
    * Apriori-Inverse
    */
   public static final int ALG_APRIORI_INVERSE = 495;
   
   /**
    * Pseudo-Closed
    */
   public static final int ALG_PSEUDO_CLOSED = 496;
   
   /*
    * Add new algorithms (that are written after Dec. 2006)
    * to the end, i.e. in the block 601 -- 700.
    */
   
/////////////////////////////////////////////////////////////////////////////
   
   // ******************************************************
   // database/memory related (Hibernate, HSQLDB, etc.)
   // Allowed values: 501 - 550
   // ******************************************************
   
   /**
    * Save the F tables in a database.
    */
   public static final int DB_SAVE_F = 501;
   
   /**
    * Save the Z tables in a database.
    */
   public static final int DB_SAVE_Z = 502;
   
   /**
    * Save the F tables in memory.
    */
   public static final int MEM_SAVE_F = 503;
   
   /**
    * Save the Z tables in memory.
    */
   public static final int MEM_SAVE_Z = 504;
   
   /**
    * Save the R (rare) tables in memory.
    */
   public static final int MEM_SAVE_R = 505;
   
/////////////////////////////////////////////////////////////////////////////   
   
   // ******************************************************
   // rule settings
   // Allowed values: 551-600
   // ******************************************************
   
   //////////////////////////////////////////
   
   /**
    * All association rules from FIs.
    */
   public static final int RULE_ALL = 551;
   
   /**
    * Closed association rules (my stuff).
    */
   public static final int RULE_CLOSED = 552;
   
   /**
    * All informative association rules.
    */
   public static final int RULE_ALL_INF = 553;
   
   /**
    * Reduced informative association rules.
    */
   public static final int RULE_RED_INF = 554;
   
   /**
    * Exact informative association rules, aka. Generic Basis.
    */
   public static final int RULE_GB = 555;
   
   /**
    * More than one thing was specified.
    */
   public static final int RULE_TOO_MANY = 556;
   
   /**
    * All informative association rules whose confidance
    * is less than 100% (Informative Basis).
    */
   public static final int RULE_ALL_IB = 557;

   /**
    * Reduced informative association rules whose confidance
    * is less than 100% (Informative Basis).
    */
   public static final int RULE_RED_IB = 558;
   
   /**
    * Rare association rules.
    */
   public static final int RULE_RARE = 559;
   
   /**
    * Duquenne-Guigues basis
    */
   public static final int RULE_DG = 560;
   
/////////////////////////////////////////////////////////////////////////////
   
   // ******************************************************
   // algorithms #2
   // Allowed values: 601 - 700
   // ******************************************************
   
   /**
    * dEclat algorithm, i.e. Eclat with diffsets.
    */
   public static final int ALG_DECLAT = 601;
   
   /**
    * dCharm algorithm, i.e. Charm with diffsets.
    */
   public static final int ALG_DCHARM = 602;
   
   /**
    * Default algorithm for Coron-base.
    */
   public static final int ALG_CHARM_DEFAULT = C.ALG_CHARM_4;
   
   /**
    * A simple method to find MFIs.
    */
   public static final int ALG_MFI_SIMPLE = 603;
   
   /**
    * Dualize and Advance algorithm.
    */
   public static final int ALG_DAA = 604;
   
   /**
    * Passing from MFIs to mRIs.
    */
   public static final int ALG_MFI2MRI = 605;
   
   /**
    * Talky.
    * Eclat with reverse pre-order traversal.
    */
   public static final int ALG_TALKY = 606;
   
   /**
    * Talky-G.
    * Eclat-G with reverse pre-order traversal.
    */
   public static final int ALG_TALKY_G = 607;
   
   /**
    * Touch (v0): Charm + Eclat-G + association
    * Touch (v1): Charm + Talky-G + association
    * 
    * Both give the same result.
    */
   public static final int ALG_TOUCH_1 = 608;
   
   /**
    * Default version of Touch.
    */
   public static final int ALG_TOUCH_DEFAULT = C.ALG_TOUCH_1;
   
   /**
    * dTalky-G, i.e. Talky-G with diffsets
    */
   public static final int ALG_DTALKY_G = 609;
   
   /**
    * Like Touch (v1), but with diffsets.
    */
   public static final int ALG_DTOUCH_1 = 610;
   
   /**
    * Generalization of Touch.
    */
   public static final int ALG_CG_MATCH = 611;
   
   /**
    * Norris algorithm. See Norris.
    */
   public static final int ALG_NORRIS = 612;
   
   /**
    * Close By One algorithm. See Kuznetsov.
    */
   public static final int ALG_NEXT_CLOSURE = 613;
   
   /**
    * NextClosure algorithm. See Ganter.
    */
   public static final int ALG_CLOSE_BY_ONE = 614;
   
   /**
    * Talky-G + Charm together.
    * newInCoron1
    */
   public static final int ALG_FLAKE = 615;
   
   /**
    * A modified version of Flake.
    * Experimental version, we are not sure that the idea is correct.
    * newInCoron1
    */
   public static final int ALG_FLAKE_EXP1 = 616;
   
   /**
    * A modified version of Flake Exp. v1.
    * We are trying to correct the previous alg.
    * newInCoron1
    */
   public static final int ALG_FLAKE_EXP2 = 617;
   
   /**
    * Similar to Flake Exp. v2. The end result is the same.
    * newInCoron1
    */
   public static final int ALG_FLAKE_EXP3 = 618;
 
   /**
    * Ralky. Based on Talky; the goal is to filter 
    * minimal rare itemsets. It works like Apriori-Rare
    * but in a depth-first fashion. It filters mRIs.
    */
   public static final int ALG_RALKY = 619;
   
   /**
    * Walky-G. Based on Talky-G; the goal is to filter
    * minimal rare generators. It works like Talky-G
    * but it also filters mRGs.
    * 
    * It was renamed to Walky-G.
    */
   public static final int ALG_WALKY_G = 620;
   
   /**
    * A variation of Walky-G. The idea is to eliminate
    * one of the hashes (the one from Charm) and work
    * with a normal hash only.
    */
   public static final int ALG_WALKY_G2 = 621;
   
   /**
    * Like Talky-G but:
    * (1) eliminate the hash adopted from Charm
    * (2) and use a normal hash (HashMap) instead
    */
   public static final int ALG_TALKY_G2 = 622;
   
   /**
    * Version 4 of Flake.
    * It's totally different from v3. 
    */
   public static final int ALG_FLAKE_EXP4 = 623;
   
   /**
    * Combine the output of LCM and GrGrowth to 
    * have a result like Zart's.
    */
   public static final int ALG_LCM_GRGROWTH = 624;
   
   /**
    * CGT (Talky + FGs + FCIs).
    */
   public static final int ALG_CGT = 625;
}
