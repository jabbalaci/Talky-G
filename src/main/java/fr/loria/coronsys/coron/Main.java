package fr.loria.coronsys.coron;

import fr.loria.coronsys.coron.algorithm.Algorithm;
import fr.loria.coronsys.coron.algorithm.DEclat;
import fr.loria.coronsys.coron.algorithm.DTalkyG;
import fr.loria.coronsys.coron.algorithm.Eclat1;
import fr.loria.coronsys.coron.algorithm.Talky;
import fr.loria.coronsys.coron.algorithm.TalkyG;
import fr.loria.coronsys.coron.algorithm.TalkyG2;
import fr.loria.coronsys.coron.helper.Arguments;
import fr.loria.coronsys.coron.helper.C;
import fr.loria.coronsys.coron.helper.CheckJavaVersion;
import fr.loria.coronsys.coron.helper.Convert;
import fr.loria.coronsys.coron.helper.Database;
import fr.loria.coronsys.coron.helper.DemoCoron;
import fr.loria.coronsys.coron.helper.Error;
import fr.loria.coronsys.coron.helper.FileFormatHandler;
import fr.loria.coronsys.coron.helper.Global;
import fr.loria.coronsys.coron.helper.Memory;
import fr.loria.coronsys.coron.helper.Statistics;

/* Abbreviations used:   FCI  -- frequent closed itemset
 *                       FI   -- frequent itemset
 */

/** 
 * Entry point. 
 * Handles arguments, reads input file, min. support,
 * and calls Close to do the real work.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>) 
*/
public class Main
{
   /**
    * For measuring runtime -- when calculation started.
 	*/
   private long startTime;
   
   /**
    * For measuring runtime -- when calculation ended.
 	*/
   private long endTime;
   
   /**
    * Which algorithm is used for the mining process.
    */
   private Algorithm algorithm;
   
   /**
    * main() function, entry point.
    * 
    * @param args Runtime arguments.
    */
   public static void main(String[] args)
   {
      CheckJavaVersion.checkJavaVersion();
      DemoCoron.checkDateLimit();
      //
      Main main = new Main();
      main.start(args);
   }

   /**
    * Running of the program starts here. It controls the whole run of the program.
    * 
    * @param args Runtime arguments.
    */
   private void start(String[] args)
   {
	   // process arguments (parse, drop error message if malformed)
	   (new Arguments()).processArguments(args);
	   
	   if (Global.getExtra().get(C.X_DONT_READ_DATABASE) == false)
	   {
		   //Result result;
		   // let's see the initial memory usage
		   if (Global.memInfo()) System.err.println("> Memory usage (at the beginning, without gc()): "+Convert.byteToPrettyString((new Memory()).testMemUsage()));
		   // read input file and get the lines as a vector of bitsets
		   FileFormatHandler.readFile();
		   // do a post-check on the min_supp
		   verifyMinSupp();
		   // do a post-check on the position of the first value in basenum files
		   Database.postCheckPosBasenum();

		   if (Database.getConvertFileType() != C.FT_NOTHING)
		   {
			   FileFormatHandler.writeFile();
			   System.exit(0);
		   }
		   // print a little statistics about the input file and minsupport
		   // just to better imagine with what we are working with
		   Statistics.printDatabaseStatistics();

		   startTime = System.currentTimeMillis();	// we'll measure time from here
		   // start: main part
		   callMiner();
		   // end:   main part
		   endTime = System.currentTimeMillis();		// we'll measure time until here

		   // print runtime if needed
		   if (Global.getVerbosity().get(C.V_RUNTIME)) Statistics.printRuntime(endTime - startTime);
	   }
	   else		// if C.X_DONT_READ_DATABASE is on
	   {
		   callMiner();
	   }
   }

   /**
    * This function will call the chosen miner algorithm.
    */
   private void callMiner()
   {
      int value = Global.getAlgorithm();
      
      switch(value)
      {
//         case C.ALG_A_CLOSE:
//            checkIfClassAvailable("AClose");
//            //
//            AClose aclose = new AClose(Database.getDatabase(), Database.getMinSupp());
//            aclose.start();
//            this.algorithm = aclose;
//            break;
//         case C.ALG_APRIORI:
//            checkIfClassAvailable("Apriori");
//            //
//            Apriori apriori = new Apriori(Database.getDatabase(), Database.getMinSupp());
//      		apriori.start();
//      		this.algorithm = apriori;
//            break;
//         case C.ALG_APRIORI_CLOSE:
//            checkIfClassAvailable("AprioriClose");
//            //
//            AprioriClose apriori_close = new AprioriClose(Database.getDatabase(), Database.getMinSupp());
//   			apriori_close.start();
//   			this.algorithm = apriori_close;
//            break;
//         case C.ALG_APRIORI_INVERSE:
//            checkIfClassAvailable("AprioriInverse");
//            //
//            AprioriInverse apriori_inverse = new AprioriInverse(Database.getDatabase(), Database.getMinSupp());
//            apriori_inverse.start();
//            this.algorithm = apriori_inverse;
//            break;
//         case C.ALG_APRIORI_RARE:
//            checkIfClassAvailable("AprioriRare");
//            //
//            AprioriRare aprioriRare = new AprioriRare(Database.getDatabase(), Database.getMinSupp());
//            aprioriRare.start();
//            this.algorithm = aprioriRare;
//            break;
//         case C.ALG_ARA:
//            checkIfClassAvailable("Ara");
//            //
//            Ara ara = new Ara(Database.getDatabase(), Database.getMinSupp());
//            ara.start();
//            this.algorithm = ara;
//            break;
//         case C.ALG_ARIMA:
//            checkIfClassAvailable("Arima");
//            //
//            Arima arima = new Arima(Database.getDatabase(), Database.getMinSupp());
//            arima.start();
//            this.algorithm = arima;
//            break;
//         case C.ALG_BTB:
//            checkIfClassAvailable("BtB");
//            //
//            BtB btb = new BtB(Database.getDatabase(), Database.getMinSupp());
//            btb.start();
//            this.algorithm = btb;
//            break;
//         case C.ALG_CARPATHIA:
//            checkIfClassAvailable("Carpathia");
//            //
//            Carpathia carpathia = new Carpathia(Database.getDatabase(), Database.getMinSupp());
//            carpathia.start();
//            this.algorithm = carpathia;
//            break;
//         case C.ALG_CARPATHIA_G:
//            checkIfClassAvailable("CarpathiaG");
//            //
//            CarpathiaG carpathiaG = new CarpathiaG(Database.getDatabase(), Database.getMinSupp());
//            carpathiaG.start();
//            this.algorithm = carpathiaG;
//            break;
//         case C.ALG_CARPATHIA_G_RARE:
//            checkIfClassAvailable("CarpathiaGRare");
//            //
//            CarpathiaGRare carpathiaGRare = new CarpathiaGRare(Database.getDatabase(), Database.getMinSupp());
//            carpathiaGRare.start();
//            this.algorithm = carpathiaGRare;
//            break;
//         case C.ALG_CG_MATCH:
//            checkIfClassAvailable("CGMatch");
//            //
//            CGMatch cgMatch = new CGMatch(Database.getDatabase(), Database.getMinSupp());
//            cgMatch.start();
//            this.algorithm = cgMatch;
//            break;
//         case C.ALG_CHARM_4:
//            checkIfClassAvailable("Charm4");
//            //
//            Charm4 charm4 = new Charm4(Database.getDatabase(), Database.getMinSupp());
//            charm4.start();
//            this.algorithm = charm4;
//            break;
//         case C.ALG_CHARM_4B:
//            checkIfClassAvailable("Charm4b");
//            //
//            Charm4b charm4b = new Charm4b(Database.getDatabase(), Database.getMinSupp());
//            charm4b.start();
//            this.algorithm = charm4b;
//            break;
//         case C.ALG_CLOSE:
//            checkIfClassAvailable("Close");
//            //
//            Close close = new Close(Database.getDatabase(), Database.getMinSupp());
//            close.start();
//            this.algorithm = close;
//            break;   
//         case C.ALG_DAA:
//            checkIfClassAvailable("DAA");
//            //
//            DAA daa = new DAA(Database.getDatabase(), Database.getMinSupp());
//            daa.start();
//            this.algorithm = daa;
//            break;   
//         case C.ALG_DCHARM:
//            checkIfClassAvailable("DCharm");
//            //
//            DCharm dcharm = new DCharm(Database.getDatabase(), Database.getMinSupp());
//            dcharm.start();
//            this.algorithm = dcharm;
//            break;   
         case C.ALG_DECLAT:
            checkIfClassAvailable("DEclat");
            //
            DEclat declat = new DEclat(Database.getDatabase(), Database.getMinSupp());
            declat.start();
            this.algorithm = declat;
            break;
         case C.ALG_DTALKY_G:
            checkIfClassAvailable("DTalkyG");
            //
            DTalkyG dtalkyg = new DTalkyG(Database.getDatabase(), Database.getMinSupp());
            dtalkyg.start();
            this.algorithm = dtalkyg;
            break;   
//         case C.ALG_DTOUCH_1:
//            checkIfClassAvailable("DTouch1");
//            //
//            DTouch1 dtouch1 = new DTouch1(Database.getDatabase(), Database.getMinSupp());
//            dtouch1.start();
//            this.algorithm = dtouch1;
//            break;   
         case C.ALG_ECLAT_1:
            checkIfClassAvailable("Eclat1");
            //
            Eclat1 eclat1 = new Eclat1(Database.getDatabase(), Database.getMinSupp());
            eclat1.start();
            this.algorithm = eclat1;
            break;   
//         case C.ALG_ECLAT_G:
//            checkIfClassAvailable("EclatG");
//            //
//            EclatG eclatG = new EclatG(Database.getDatabase(), Database.getMinSupp());
//            eclatG.start();
//            this.algorithm = eclatG;
//            break;   
//         case C.ALG_ECLAT_Z:
//            checkIfClassAvailable("EclatZ");
//            //
//            EclatZ eclatz = new EclatZ(Database.getDatabase(), Database.getMinSupp());
//            eclatz.start();
//            this.algorithm = eclatz;
//            break;
//         case C.ALG_FLAKE:   // newInCoron1
//            checkIfClassAvailable("flake.Flake");
//            //
//            Flake flake = new Flake(Database.getDatabase(), Database.getMinSupp());
//            flake.start();
//            this.algorithm = flake;
//            break;
//         case C.ALG_FLAKE_EXP1:   // newInCoron1
//            checkIfClassAvailable("flake.FlakeExp1");
//            //
//            FlakeExp1 flakeExp1 = new FlakeExp1(Database.getDatabase(), Database.getMinSupp());
//            flakeExp1.start();
//            this.algorithm = flakeExp1;
//            break;
//         case C.ALG_FLAKE_EXP2:   // newInCoron1
//            checkIfClassAvailable("flake.FlakeExp2");
//            //
//            FlakeExp2 flakeExp2 = new FlakeExp2(Database.getDatabase(), Database.getMinSupp());
//            flakeExp2.start();
//            this.algorithm = flakeExp2;
//            break;
//         case C.ALG_FLAKE_EXP3:   // newInCoron1
//            checkIfClassAvailable("flake.FlakeExp3");
//            //
//            FlakeExp3 flakeExp3 = new FlakeExp3(Database.getDatabase(), Database.getMinSupp());
//            flakeExp3.start();
//            this.algorithm = flakeExp3;
//            break;
//         case C.ALG_FLAKE_EXP4:   // newInCoron1
//            checkIfClassAvailable("flake.FlakeExp4");
//            //
//            FlakeExp4 flakeExp4 = new FlakeExp4(Database.getDatabase(), Database.getMinSupp());
//            flakeExp4.start();
//            this.algorithm = flakeExp4;
//            break;
//         case C.ALG_CGT:   // newInCoron1
//             checkIfClassAvailable("CGT");
//             //
//             CGT cgt = new CGT(Database.getDatabase(), Database.getMinSupp());
//             cgt.start();
//             this.algorithm = cgt;
//             break;
//         case C.ALG_GEN_CLOSURE:
//            checkIfClassAvailable("GenClosure");
//            //
//            GenClosure genClosure = new GenClosure(Database.getDatabase(), Database.getMinSupp());
//            genClosure.start();
//            this.algorithm = genClosure;
//            break;   
//         case C.ALG_MFI2MRI:
//            checkIfClassAvailable("Mfi2mri");
//            //
//            Mfi2mri mfi2mri = new Mfi2mri(Database.getDatabase(), Database.getMinSupp());
//            mfi2mri.start();
//            this.algorithm = mfi2mri;
//            break;
//         case C.ALG_MFI_SIMPLE:
//            checkIfClassAvailable("MfiSimple");
//            //
//            MfiSimple mfiSimple = new MfiSimple(Database.getDatabase(), Database.getMinSupp());
//            mfiSimple.start();
//            this.algorithm = mfiSimple;
//            break;
//         case C.ALG_PASCAL:
//            checkIfClassAvailable("Pascal");
//            //
//            Pascal pascal = new Pascal(Database.getDatabase(), Database.getMinSupp());
//            pascal.start();
//            this.algorithm = pascal;
//            break;
//         case C.ALG_PASCAL_PLUS:
//            checkIfClassAvailable("PascalPlus");
//            //
//            PascalPlus pascal_plus = new PascalPlus(Database.getDatabase(), Database.getMinSupp());
//            pascal_plus.start();
//            this.algorithm = pascal_plus;
//            break;   
//         case C.ALG_PSEUDO_CLOSED:
//            checkIfClassAvailable("PseudoClosed");
//            //
//            PseudoClosed pc = new PseudoClosed(Database.getDatabase(), Database.getMinSupp());
//            pc.start();
//            this.algorithm = pc;
//            break;
//         case C.ALG_STONE:
//            checkIfClassAvailable("Stone");
//            //
//            Stone stone = new Stone(Database.getDatabase(), Database.getMinSupp());
//            stone.start();
//            this.algorithm = stone;
//            break;   
         case C.ALG_TALKY:
            checkIfClassAvailable("Talky");
            //
            Talky talky = new Talky(Database.getDatabase(), Database.getMinSupp());
            talky.start();
            this.algorithm = talky;
            break;
//         case C.ALG_RALKY:
//            checkIfClassAvailable("Ralky");
//            //
//            Ralky ralky = new Ralky(Database.getDatabase(), Database.getMinSupp());
//            ralky.start();
//            this.algorithm = ralky;
//            break;
         case C.ALG_TALKY_G:
            checkIfClassAvailable("TalkyG");
            //
            TalkyG talkyg = new TalkyG(Database.getDatabase(), Database.getMinSupp());
            talkyg.start();
            this.algorithm = talkyg;
            break;
         case C.ALG_TALKY_G2:
            checkIfClassAvailable("TalkyG2");
            //
            TalkyG2 talkyg2 = new TalkyG2(Database.getDatabase(), Database.getMinSupp());
            talkyg2.start();
            this.algorithm = talkyg2;
            break;
//         case C.ALG_WALKY_G:
//            checkIfClassAvailable("WalkyG");
//            //
//            WalkyG walkyg = new WalkyG(Database.getDatabase(), Database.getMinSupp());
//            walkyg.start();
//            this.algorithm = walkyg;
//            break;
//         case C.ALG_WALKY_G2:
//            checkIfClassAvailable("WalkyG2");
//            //
//            WalkyG2 walkyg2 = new WalkyG2(Database.getDatabase(), Database.getMinSupp());
//            walkyg2.start();
//            this.algorithm = walkyg2;
//            break;
//         case C.ALG_TITANIC:
//            checkIfClassAvailable("Titanic");
//            //
//            Titanic titanic = new Titanic(Database.getDatabase(), Database.getMinSupp());
//            titanic.start();
//            this.algorithm = titanic;
//            break;
//         case C.ALG_TOUCH_0:
//            checkIfClassAvailable("Touch0");
//            //
//            Touch0 touch0 = new Touch0(Database.getDatabase(), Database.getMinSupp());
//            touch0.start();
//            this.algorithm = touch0;
//            break;       
//         case C.ALG_TOUCH_1:
//            checkIfClassAvailable("Touch1");
//            //
//            Touch1 touch1 = new Touch1(Database.getDatabase(), Database.getMinSupp());
//            touch1.start();
//            this.algorithm = touch1;
//            break;
//         case C.ALG_ZART_2:
//            checkIfClassAvailable("Zart2");
//            //
//            Zart2 zart2 = new Zart2(Database.getDatabase(), Database.getMinSupp());
//            zart2.start();
//            this.algorithm = zart2;
//            break;
//         case C.ALG_LCM_GRGROWTH:
//            checkIfClassAvailable("LcmGrgrowth");
//            //
//            LcmGrgrowth alg = new LcmGrgrowth(Database.getDatabaseAbsoluteFilePath(), Database.getMinSupp());
//            alg.start();
//            break;
//            
//         case C.ALG_NORRIS:
//            Norris norris = new Norris();
//            norris.start();
//         	break;
//         	
//         case C.ALG_CLOSE_BY_ONE:
//         	CloseByOne cbo = new CloseByOne();
//         	cbo.start();
//          	break;
//          	
//         case C.ALG_NEXT_CLOSURE:
//         	NextClosure nc = new NextClosure();
//         	nc.start();
//          	break;
//         // #################################################################
//         /*
//          * BEGIN: OLD ALGORITHMS
//          */
//         case C.ALG_APRIORI_2:
//            checkIfClassAvailable("Apriori2");
//            //
//            Apriori2 apriori2 = new Apriori2(Database.getDatabase(), Database.getMinSupp());
//            apriori2.start();
//            this.algorithm = apriori2;
//            break;   
//         case C.ALG_CHARM_1:
//            checkIfClassAvailable("Charm1");
//            //
//            Charm1 charm1 = new Charm1(Database.getDatabase(), Database.getMinSupp());
//            charm1.start();
//            this.algorithm = charm1;
//            break;
//         case C.ALG_CHARM_2:
//            checkIfClassAvailable("Charm2");
//            //
//            Charm2 charm2 = new Charm2(Database.getDatabase(), Database.getMinSupp());
//            charm2.start();
//            this.algorithm = charm2;
//            break;
//         case C.ALG_CHARM_3:
//            checkIfClassAvailable("Charm3");
//            //
//            Charm3 charm3 = new Charm3(Database.getDatabase(), Database.getMinSupp());
//            charm3.start();
//            this.algorithm = charm3;
//            break;
//         case C.ALG_ECLAT_2:
//            checkIfClassAvailable("Eclat2");
//            //
//            Eclat2 eclat2 = new Eclat2(Database.getDatabase(), Database.getMinSupp());
//            eclat2.start();
//            this.algorithm = eclat2;
//            break;
//         case C.ALG_ECLAT_3:
//            checkIfClassAvailable("Eclat3");
//            //
//            Eclat3 eclat3 = new Eclat3(Database.getDatabase(), Database.getMinSupp());
//            eclat3.start();
//            this.algorithm = eclat3;
//            break;
//         case C.ALG_ECLAT_Z_2:
//            checkIfClassAvailable("EclatZ2");
//            //
//            EclatZ2 eclatz2 = new EclatZ2(Database.getDatabase(), Database.getMinSupp());
//            eclatz2.start();
//            this.algorithm = eclatz2;
//            break;
//         case C.ALG_ECLAT_Z_3:
//            checkIfClassAvailable("EclatZ3");
//            //
//            EclatZ3 eclatz3 = new EclatZ3(Database.getDatabase(), Database.getMinSupp());
//            eclatz3.start();
//            this.algorithm = eclatz3;
//            break;
//         case C.ALG_ZART_1:
//            checkIfClassAvailable("Zart1");
//            //
//            Zart1 zart1 = new Zart1(Database.getDatabase(), Database.getMinSupp());
//            zart1.start();
//            this.algorithm = zart1;
//            break;
         /*
          * END: OLD ALGORITHMS
          */
         // #################################################################            
           
         /*case C.ALG_ECLATZLM:
            checkIfClassAvailable("EclatZLM");
            //
            EclatZLM eclatzlm = new EclatZLM(Database.getDatabase(), Database.getMinSupp());
            eclatzlm.start();
            this.algorithm = eclatzlm;
            break;*/
         /*case C.ALG_CHARM_MFI:
            checkIfClassAvailable("CharmMfi");
            //
            CharmMfi charmMfi = new CharmMfi(Database.getDatabase(), Database.getMinSupp());
            charmMfi.start();
            this.algorithm = charmMfi;
            break;*/
            
         case C.ALG_UNKNOWN:
            System.err.println("Error: you have chosen an unknown algorithm.");
         	Error.die(C.ERR_JUST_EXIT);
            break;
         default:
            // normally we should never get here!
            System.err.println("Error: Ooops! The value ("+value+") is not treated in Main.java!");
         	System.err.println("       exiting...");
         	Error.die(C.ERR_JUST_EXIT);
            break;
      }
   }

   /**
    * This is used in demo versions, where some "secret" algorithms
    * are deleted. In that case, instead of an Exception we can
    * show a nice message.
    * 
    * @param algName
    */
   public static void checkIfClassAvailable(String algName)
   {
      // dynamic class loading

      // variable fully qualified class name, as a String!!
      String className = "fr.loria.coronsys.coron.algorithm."+algName;

      // Create a class of the specified algorithm
      try
      {
         @SuppressWarnings("unused")
         Object alg = Class.forName( className ).newInstance();
      }
      catch (InstantiationException e)
      {
         /*
          * this exception occurs because we call a constructor
          * without any parameters, but it doesn't exist. No problem,
          * we only want to know if the class exists or not.
          */
      }
      catch (IllegalAccessException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (ClassNotFoundException e)
      {
         System.err.println("Coron: the specified algorithm is not available.");
         System.err.println("Tip: for a complete list of available algorithms,");
         System.err.println("     please refer to the documentation site.");
         Error.die(C.ERR_JUST_EXIT);
      }
   }
   
   /**
    * It may be possible that the user specified a min_supp value
    * that is larger than the total number of lines. However, we will
    * find it out only when we read the database. So this problem 
    * cannot be cought before when processing the command-line arguments.
    */
   private static void verifyMinSupp()
   { 
      if (Database.getMinSupp() > Database.getNumberOfObjects()) 
      {
         Error.die(C.ERR_MIN_SUPP_TOO_LARGE);
      }
   }
}
