package fr.loria.coronsys.coron.helper;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.BitSet;

/**
 * Class to give direct access to some stuff, like command line options.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Global
{  
   /**
    * Empty private constructor. The class cannot be instantiated.
    */
   private Global() { }
   
   /**
    * Verbosity levels.
    */
   private static BitSet verbosity;
   
   /**
    * Extra options (like ordering, Apriori mode).
    */
   private static BitSet extra;
   
   /**
    * Options concerning the rule generation.
    * It is here because of AssRuleX.
    */
   private static BitSet rules;
   
   /**
    * Which algorithm was chosen for the mining process.
    */
   private static int algorithm;
   
   /**
    * What kind of rules are to be extracted.
    * It's here because of AssRuleX.
    */
   private static int rule;
   
   /**
    * If it's true, then the user wants extra information about the execution
    * of the program: which function is called, what's happening.
    */
   private static boolean followFunctions; 
   
   /**
    * Similar to "followunctions". Monitoring memory usage.
    */
   private static boolean memInfo;
   
   /**
    * Are itemsets represented as BitSets (default) or TreeSets?
    */
   private static int itemset_representation;
   
   /**
    * Don't calculate frequent 2-itemsets.
    * If the dataset is too big, with this we can save some memory.
    * However, if we cancel F_2 computation, the search process will be 
    * (much) slower.
    */
   //private static boolean noF2;
   
   /**
    * Default behaviour changed. By default, the F2 matrix is DISABLED.
    * If you want to use, enable it explicitly with the switch -usef2.
    */
   private static boolean useF2 = false;
   
   /**
    * If the user choose to redirect the result or not.
    */
   public static boolean isRedirected = false;
   
   /**
    * the output file name, chosen by the user
    */
   public static String outputFileName;
   
   /**
    * Usage:
    * 
    * BufferedWriter out = Global.bufferedOutWriter;
    * out.write("abcdefghijk ");
    * out.write('\n');
    * ...
    * out.flush();   // DON'T FORGET IT
    */
   public final static BufferedWriter bufferedOutWriter = 
      new BufferedWriter(new OutputStreamWriter(System.out), 4096);
   
   /*
    * "constructor"
    * 
    *  reset() must do the same thing as this one
    */ 
   static {
      reset();
   }
   
   /**
    * Re-initialise the state of this class.
    */
   public static void reset()
   {
      Global.verbosity              = new BitSet();
      Global.extra                  = new BitSet();
      Global.rules                  = new BitSet();
      Global.followFunctions        = false;
      Global.rule                   = C.UNDEFINED;
      Global.itemset_representation = C.REPR_BITSET;
   }
   
   // ***********************************************************************
   // ***********************************************************************
   
   /**
    * Gets the verbosity options.
    * 
    * @return Returns the verbosity.
    */
   public static BitSet getVerbosity() {
      return verbosity;
   }
   
   /**
    * Sets the verbosity options.
    * 
    * @param verbosity The verbosity to set.
    */
   public static void setVerbosity(BitSet verbosity) {
      Global.verbosity = verbosity;
   }
   
   /**
    * Gets the extra options.
    * 
    * @return Returns the extra.
    */
   public static BitSet getExtra() {
      return extra;
   }
   
   /**
    * Sets the extra options.
    * 
    * @param extra The extra to set.
    */
   public static void setExtra(BitSet extra) {
      Global.extra = extra;
   }
   
   /**
    * Gets the rules option. Used in AssRuleX.
    * 
    * @return Returns the rules.
    */
   public static BitSet getRules() {
      return rules;
   }
   
   /**
    * Sets the rules option. Used in AssRuleX.
    * 
    * @param rules The rules to set.
    */
   public static void setMethods(BitSet rules) {
      Global.rules = rules;
   }

   /**
    * Sets a given bit in extra.
    * 
    * @param bit A bit which is to set '1' in extra.
    */
   public static void setExtraValue(int bit) {
      Global.extra.set(bit);
   }
   
   /**
    * @return Returns the algorithm.
    */
   public static int getAlgorithm() {
      return algorithm;
   }
   
   /**
    * @return Returns the algorithm as a string.
    */
   public static String getAlgorithmStr() 
   {
      switch (Global.algorithm)
      {
         case C.ALG_APRIORI:           return "Apriori";
         case C.ALG_APRIORI_CLOSE:     return "Apriori-Close";
         case C.ALG_CLOSE:             return "Close";
         case C.ALG_A_CLOSE:           return "A-Close";
         case C.ALG_PASCAL:            return "Pascal";
         case C.ALG_PASCAL_PLUS:       return "Pascal+";
         case C.ALG_TITANIC:           return "Titanic";
         case C.ALG_ZART_1:            return "Zart (v1) [original]";
         case C.ALG_ZART_2:            return "Zart (v2) [triangular matrix]";
         case C.ALG_CARPATHIA:         return "RMS Carpathia";
         case C.ALG_CARPATHIA_G:       return "Carpathia-G [FGs]";
         case C.ALG_CARPATHIA_G_RARE:  return "Carpathia-G-Rare [mRGs (=mRIs)]";
         case C.ALG_CHARM_1:           return "Charm (v1) [simple]";
         case C.ALG_CHARM_2:           return "Charm (v2) [2-itemsets and FCIs in trie]";
         case C.ALG_CHARM_3:           return "Charm (v3) [triangular matrix; FCIs in trie]";
         case C.ALG_CHARM_4:           return "Charm (v4) [triangular matrix; hash for FCIs]";
         case C.ALG_CHARM_4B:          return "Charm (v4b) [triangular matrix; hash for FCIs]";
         case C.ALG_ECLAT_1:           return "Eclat (v1) [triangular matrix]";
         case C.ALG_ECLAT_2:           return "Eclat (v2) [breadth-first]";
         case C.ALG_ECLAT_3:           return "Eclat (v3) [like Eclat (v1), but delete nodes after extension]";
         case C.ALG_ECLAT_Z:           return "Eclat-Z [save to file; process file; output: like Zart]";
         //case C.ALG_ECLATZLM:          return "Eclat-ZLM [like Eclat-Z; different temp file process; low memory usage]";
         case C.ALG_ECLAT_Z_2:         return "Eclat-Z (v2) [collect mem.; save file; proc. file; out like Zart]";
         case C.ALG_ECLAT_Z_3:         return "Eclat-Z (v3) [like Eclat-Z (v1), but delete nodes after extension]";
         //case C.ALG_ECLATZT:           return "Eclat-ZT [Eclat-Z with TreeSet]";
         //case C.ALG_ECLATZV:           return "Eclat-ZV [like Eclat-ZT, but with Vector]";
         //case C.ALG_ECLATZVB:          return "Eclat-ZVB [like Eclat-ZV; on-the-fly Vect. to BitSet conv.]";
         //case C.ALG_ECLATZM:           return "Eclat-ZM [like Eclat-ZVB; index in temp file]";
         //case C.ALG_ECLATZH:           return "Eclat-ZH [like Eclat-Z; BitSet-Vector hybrid]";
         case C.ALG_SABRE_1:           return "Sabre (v1) [DON'T USE IT!!! Wrong!]";
         case C.ALG_APRIORI_RARE:      return "Apriori-Rare";
         case C.ALG_APRIORI_2:         return "Apriori2 [DON'T USE IT!!! Not efficient! pruning based on minimal rare itemsets]";
         case C.ALG_STONE:             return "Stone";
         case C.ALG_ARA:               return "Ara (Apriori-Rare + Arima) [RIs, FIs (using mZGs)]";
         case C.ALG_ARIMA:             return "Arima [RIs (using mRIs and mZGs)]";
         //case C.ALG_CHARM_MFI:         return "Charm-MFI [find FCIs first; filter MFIs]";
         case C.ALG_MFI_SIMPLE:        return "MFI-Simple [find FCIs first; filter MFIs]";
         case C.ALG_BTB:               return "BtB [rare eq. classes]";
         case C.ALG_ECLAT_G:           return "Eclat-G [FGs]";
         case C.ALG_GEN_CLOSURE:       return "Gen-Closure [FGs + their closures]";
         case C.ALG_TOUCH_0:           return "Touch (v0) [Charm + Eclat-G + association]";
         case C.ALG_APRIORI_INVERSE:   return "Apriori-Inverse [perfectly RIs]";
         case C.ALG_PSEUDO_CLOSED:     return "Pseudo-Closed [FPCIs + their closures]";
         case C.ALG_DECLAT:            return "dEclat [like Eclat (v1), with diffsets]";
         case C.ALG_DCHARM:            return "dCharm [like Charm (v4), with diffsets]";
         case C.ALG_DAA:               return "Dualize and Advance [MFIs and mRIs]";
         case C.ALG_MFI2MRI:           return "MFI2mRI [MFIs and mRIs]";
         case C.ALG_TALKY:             return "Talky [Eclat with reverse pre-order]";
         case C.ALG_TALKY_G:           return "Talky-G [Eclat-G with reverse pre-order]";
         case C.ALG_TOUCH_1:           return "Touch (v1) [Charm + Talky-G + association]";
         case C.ALG_DTALKY_G:          return "dTalky-G [like Talky-G, with diffsets]";
         case C.ALG_DTOUCH_1:          return "dTouch [dCharm + dTalky-G + association]";
         case C.ALG_CG_MATCH:          return "CG-Match [FCIs (dCharm) + FGs (dTalky-G) + association (general)]";
         case C.ALG_NORRIS:			   return "Norris algorithm (FCA community)";
         case C.ALG_CLOSE_BY_ONE:      return "CbO algorithm (FCA community)";
         case C.ALG_NEXT_CLOSURE:      return "Next Closure algorithm (FCA community)";
         case C.ALG_FLAKE:             return "Flake [Talky-G + FCIs in one step]";  // newInCoron1
         case C.ALG_FLAKE_EXP1:        return "Flake Exp. 1 [Flake, experimental version 1]";  // newInCoron1
         case C.ALG_FLAKE_EXP2:        return "Flake Exp. 2 [Flake, exp. v2, Petko]";  // newInCoron1
         case C.ALG_FLAKE_EXP3:        return "Flake Exp. 3 [Flake, exp. v3, like v2 with combined hashes]";  // newInCoron1
         case C.ALG_RALKY:             return "Ralky [based on Talky, filter mRIs]";  // newInCoron1
         case C.ALG_WALKY_G:           return "Walky-G [based on Talky-G, filter mRGs]";  // newInCoron1
         case C.ALG_WALKY_G2:          return "Walky-G v2 [filter mRGs; like Walky-G, one hash only]";  // newInCoron1
         case C.ALG_TALKY_G2:          return "Talky-G v2 [FGs; like Talky-G, but with HashMap]";  // newInCoron1
         case C.ALG_FLAKE_EXP4:        return "Flake Exp. 4 [Flake, exp. v4, FGs in a list]";  // newInCoron1
         case C.ALG_LCM_GRGROWTH:      return "LCM v2 / GrGrowth [FCIs + associated FGs]";  // newInCoron1
         case C.ALG_CGT: 			   return "CGT [Talky + FGs + FCIs]";  // newInCoron1
         default:                      return "? (edit Global.java)";      // it should never arrive here normally!
      }
   }
   
   /**
    * Returns the name of an algorithm.
    * 
    * @param alg The number of an algorithm.
    * @return Returns the name of the algorithm.
    */
   public static String getAlgorithmStr(int alg)
   {
      int bak = Global.getAlgorithm();
      
      Global.setAlgorithm(alg);
      String description = Global.getAlgorithmStr();
      Global.setAlgorithm(bak);
      
      return description;
   }
   
   /**
    * @return Returns the chosen rule extraction type as a string.
    */
   public static String getRuleStr()
   {
      switch (Global.getRule())
      {
         case C.RULE_ALL:              return "all association rules";
         case C.RULE_CLOSED:           return "closed association rules";
         case C.RULE_ALL_INF:          return "all informative association rules";
         case C.RULE_RED_INF:          return "reduced informative association rules";
         case C.RULE_GB:               return "Generic Basis (GB)";
         case C.RULE_ALL_IB:           return "(all) Informative Basis (IB)";
         case C.RULE_RED_IB:           return "reduced Informative Basis (IB)";
         case C.RULE_RARE:             return "rare association rules";
         case C.RULE_DG:               return "Duquennes-Guigues Basis (DG)";
         default:                      return "?";     // normally it should never arrive here
      }  
   }
   
   /**
    * @return Returns the algorithm as it must be given in the command line. 
    * 
    * @param alg a mining algorithm
    */
   public static String getAlgorithmParam(int alg)          // by Thomas 
   {
      switch (alg)
      {
         case C.ALG_APRIORI:           return "apriori";
         case C.ALG_APRIORI_CLOSE:     return "apriori-close";
         case C.ALG_CLOSE:             return "close";
         case C.ALG_A_CLOSE:           return "a-close";
         case C.ALG_PASCAL:            return "pascal";
         case C.ALG_PASCAL_PLUS:       return "pascal+";
         case C.ALG_TITANIC:           return "titanic";
         case C.ALG_ZART_1:            return "zart1";
         case C.ALG_ZART_2:            return "zart";
         case C.ALG_CARPATHIA:         return "carpathia";
         case C.ALG_CHARM_1:           return "charm1";
         case C.ALG_CHARM_2:           return "charm2";
         case C.ALG_CHARM_3:           return "charm3";
         case C.ALG_CHARM_4:           return "charm";
         case C.ALG_ECLAT_1:           return "eclat";
         case C.ALG_ECLAT_2:           return "eclat2";
         case C.ALG_ECLAT_3:           return "eclat3";
         case C.ALG_ECLAT_Z:           return "eclatz";
         case C.ALG_ECLAT_Z_2:         return "eclatz2";
         case C.ALG_ECLAT_Z_3:         return "eclatz3";
         //case C.ALG_ECLATZT:           return "eclatzt";
         //case C.ALG_ECLATZV:           return "eclatzv";
         //case C.ALG_ECLATZVB:          return "eclatzvb";
         //case C.ALG_ECLATZM:           return "eclatzm";
         //case C.ALG_ECLATZH:           return "eclatzh";
         //case C.ALG_SABRE1:            return "sabre";
         case C.ALG_APRIORI_RARE:      return "apriorirare";
         case C.ALG_BTB:               return "btb";
         case C.ALG_GEN_CLOSURE:       return "genclosure";
         case C.ALG_CGT:		       return "cgt";
         default:                      return "?";      // it should never arrive here normally!
      }
   }

   /**
    * Returns the name of a rule.
    * 
    * @param rule The number of a rule.
    * @return Returns the name of the rule.
    */
   public static String getRuleStr(int rule)        // by Thomas
   {
      int bak = Global.getRule();
      
      Global.setRule(rule);
      String description = Global.getRuleStr();
      Global.setRule(bak);
      
      return description;
   }
   
   /**
    * @return Returns the rule extraction type as it must be given in the command line.
    * 
    * @param rule a kind of rules
    */
   public static String getRuleParam(int rule)      // by Thomas
   {
      switch (rule)
      {
         case C.RULE_ALL:              return "all";
         case C.RULE_CLOSED:           return "closed";
         case C.RULE_ALL_INF:          return "all_inf";
         case C.RULE_RED_INF:          return "inf";
         case C.RULE_GB:               return "GB";
         case C.RULE_ALL_IB:           return "all_IB";
         case C.RULE_RED_IB:           return "IB";
         default:                      return "?";     // normally it should never arrive here
      }  
   }
   
   /**
    * @param algorithm The algorithm to set.
    */
   public static void setAlgorithm(int algorithm) {
      Global.algorithm = algorithm;
   }

   /**
    * This function is called _after_ the analysis of runtime
    * arguments. If the switch "-v=f" is on, then we set a boolean value on.
    * Later it'll be easier to access this boolean value directly, rather than
    * always ask it from a bitset.
    */
   public static void postModify()
   {
      if (Global.getVerbosity().get(C.V_FUNCTION))
         Global.followFunctions = true;
      //
      if (Global.getVerbosity().get(C.V_MEMORY))
         Global.memInfo = true;
   }
   
   /**
    * Is the "followFunctions" variable on?
    * 
    * @return Return true, if "followFunctions" is on.
    */
   public static boolean followFunctions() {
      return Global.followFunctions;
   }
   
   /**
    * Is the "memInfo" variable on?
    * 
    * @return Return true, if "memInfo" is on.
    */
   public static boolean memInfo() {
      return Global.memInfo;
   }
   
   /**
    * @return Returns the rule.
    */
   public static int getRule() {
      return Global.rule;
   }
   

   /**
    * @param rule The rule to set.
    */
   public static void setRule(int rule) {
      Global.rule = rule;
   }

   /**
    * @return Returns the itemset_representation.
    */
   public static int getItemsetRepresentation() {
      return Global.itemset_representation;
   }
   

   /**
    * @param itemset_representation The itemset_representation to set.
    */
   public static void setItemsetRepresentation(int itemset_representation) {
      Global.itemset_representation = itemset_representation;
   }

   /**
    * @return Returns the noF2.
    */
   //public static boolean getNoF2() {
   //   return Global.noF2;
   //}

   /**
    * Set noF2 to false.
    * Meaning: don't use an upper-triangular matrix for calculating the support
    *          of 2-itemsets.
    */
   //public static void setNoF2() {
   //   Global.noF2 = true;
   //}
   
   public static boolean getUseF2() {
      return Global.useF2;
   }
   
   public static void setUseF2() {
      Global.useF2 = true;
   }
   
   /**
    * set the output file name with the user's choice.
    * @param path
    */
   public static void setOutputFileName(String path){
      Global.outputFileName = path;
   }
   
   /**
    * Get the output file name.
    * @return
    */
   public static String getOutputFileName(){
      return Global.outputFileName;
   }
}
