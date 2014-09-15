package fr.loria.coronsys.coron.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;

/** 
 * Processes command-line arguments.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Arguments
{
	/**
	 * Name of the input file.
	 */
	private String database_file;

	/**
	 * Minimum support as an integer.
	 */
	private int min_supp;

	/**
	 * Verbosity options.
	 */
	private BitSet verbosity;

	/**
	 * Extra options.
	 */
	private BitSet extra;

	/**
	 * What algorithm is chosen.
	 */
	private BitSet algorithms;

	/**
	 * Minimum support in percentage.
	 */
	private Double min_supp_percent;

	/**
	 * Used when the stdout is redirected to a file. If the specified
	 * file exists, the system drops an error message and stops. If forced
	 * deletion is switched on, it'll try to delete it first.
	 */
	private boolean forceDel;

	/**
	 * Constructor.
	 */
	public Arguments()
	{
		this.database_file     = new String("");
		this.min_supp          = 1;      				// default
		this.verbosity         = new BitSet();
		this.extra			     = new BitSet();
		this.min_supp_percent  = null;
		this.algorithms        = new BitSet();
		this.forceDel          = false;
	}

	/** 
	 * Processes command line arguments.
	 * 
	 * @param s A String array of command line arguments. 
	 */
	@SuppressWarnings("unchecked")
	public void processArguments(String[] s)
	{
		Vector args = getArgs(s);		// convert String array to Vector

		if ((args.size() == 0) || (args.contains("--help")))
		{
			print_help();
			System.exit(0);
		}
		// else
		if (args.contains("--version") || args.contains("-V"))
		{
			print_version();
			System.exit(0);
		}
		// else
		if (args.contains("--demo"))
		{
			DemoCoron.getDemoCoronDescription();
			System.exit(0);
		}
		// else
		if (args.contains("--update"))
		{
			CheckUpdate update = new CheckUpdate();
			update.start();
			System.exit(0);
		}

		args = processPriorityOptions(args);

		Vector to_delete = new Vector();
		String str;
		for (Enumeration e=args.elements(); e.hasMoreElements(); )
		{
			str = (String)e.nextElement();
			if (str.startsWith("-")) { 			// an argument starting with '-' is a potential option
				process_option(str.substring(1));
				to_delete.add(str);					// after processing delete it from the list of arguments
			}
		}
		args.removeAll(to_delete);				// real deletion
		// by now we can only have 1 (database) or 2 arguments (database + minsupport)
		if (args.size() < 1) Error.die(C.ERR_LESS_ARGS_AT_END);
		if (args.size() > 2) Error.die(C.ERR_MANY_ARGS_AT_END);
		// OK, we have 1 or 2 arguments here
		if (args.size()==1) this.database_file = (String)args.get(0);
		else // if (args.size()==2)
		{
			this.database_file   = (String)args.get(0);
			try {
				setMinSupport((String)args.get(1));
			} catch (NumberFormatException nfe) { Error.die(C.ERR_SECOND_ARG); }
		}

		// OK, fill up the result vector and send it back
		Database.setDatabaseFileStr(database_file);
		Database.setMinSupp(min_supp);
		Database.setMinSuppPercent(min_supp_percent);

		Global.setVerbosity(verbosity);
		fr.loria.coronsys.coron.helper.Global.setVerbosity(verbosity);
		Global.setExtra(extra);

		// OK, let's do some post-checks and post-modifications
		postCheckExtra();
		postCheckAlg();
		postCheckFcaAlgorithms();
		Global.postModify();
	}

	/**
	 * FCA algorithms work with min_supp = 1.
	 * If the user sets a different support value,
	 * we will notify the user and exit.
	 */
	private void postCheckFcaAlgorithms() 
	{
	   int algo = Global.getAlgorithm();
		if ( (algo == C.ALG_CLOSE_BY_ONE)
		   || (algo == C.ALG_NEXT_CLOSURE)
	      || (algo == C.ALG_NORRIS) )
	   {
	      if (Database.getMinSupp() != 1)
	      {
	         System.err.println("Error: the chosen FCA algorithm requires mininimum support to be 1.\n" +
            "Tip: set minimum support to 1."); 
	         System.exit(-1);
	      }
	   }
	}

	/**
	 * There are some extra options that modify other, normal options.
	 * For example -del modifies -out=file.
	 * So before processing -out=file, we need to know if there is
	 * a special switch option.
	 * 
	 * @param args Command-line arguments in a vector.
	 * @return Command-line arguments from which the processed extra
	 * options are removed.
	 */
	private Vector<String> processPriorityOptions(Vector<String> args)
	{
		int index;

		if ((index = args.indexOf("-del")) != -1)    // if this option is on
		{
			this.forceDel = true;
			args.remove(index);
		}

		return args;
	}

	/**
	 * Prints current version of the program.
	 */
	private void print_version() {
		System.out.println("Coron " + C.VERSION);
	}

	/** 
	 * Processes a string containing the minsupport and sets minsupport (integer) and minsupportPercent (Double) too.
	 * 
	 * @param arg A String containing the minsupport.
	 * @throws NumberFormatException
	 */
	private void setMinSupport(String arg) throws NumberFormatException
	{
		if (arg.endsWith("%"))     // if it's a double value
		{
			arg = arg.substring(0, arg.length()-1);   // chop the last character ('%') off
			// an automatic error correction. If a comma was used in the number (like in Hungarian)
			// instead of a dot, it'll be replaced by a dot (English notation).
			arg = arg.replace(',', '.');
			double d = Double.valueOf(arg).doubleValue();
			// we only accept values from range 0..100
			if ((d < 0) || (d > 100))  throw new NumberFormatException();
			else                       this.min_supp_percent = Double.valueOf(arg);
		}
		else                       // if it's an integer
		{
			this.min_supp = Integer.valueOf(arg).intValue();
			// a negative value or 0 makes no sense, so correct it back to 1 (default)
			if (this.min_supp < 1) this.min_supp = 1;
		}
	}

	/**
	 * Processes options (that start with '-'), parses them and sets the necessary verbosity bits.
	 * 
	 * @param option A String that contains an option. Parses it and sets the verbosity BitSet.
	 */
	private void process_option(String option)
	{
		boolean modifiable = true;        // can options be simplified? E.g.: remove '_' and '-', etc.
		if (option.startsWith("of:")  || 
				option.startsWith("of=")  || 
				option.startsWith("out:") ||
				option.startsWith("out=")) modifiable = false;    // the output filename cannot be changed!
		/*
		 * anything can be modified, except the output filename
		 */

		// lowercase, change '-' to '_', then remove all '_'
		if (modifiable) option = option.toLowerCase().replaceAll("-", "_").replaceAll("_", "");

		/* I started to use the equal sign ('=') as a separator, like '-alg=zart',
		 * but the Windows shell cmd treats it as two parameters ('-alg' and 'zart').
		 * So under Windows you can use the ':' as the separator sign ('-alg:zart').
		 * 
		 * Here we'll convert it back to '=' and thus both are treated the same way.
		 */
		option = option.replaceFirst(":", "=");

		if (option.indexOf("=") == -1) processExtraOption(option);
		else
		{
			String items[] = option.split("=", 2);
			if (!((items.length==2) && (items[0].length()>0) && (items[1].length()>0))) Error.die(C.ERR_BAD_SWITCH);

			String o = items[1];
			if (items[0].equals("v"))     // verbosity
			{
				if (o.equals("m") || o.equals("mem") || o.equals("memory")) this.verbosity.set(C.V_MEMORY);
				else if (o.equals("f") || o.equals("fun") || o.equals("func") || o.equals("function") || o.equals("functions")) this.verbosity.set(C.V_FUNCTION);
				else if (o.equals("t") || o.equals("time") || o.equals("runtime")) this.verbosity.set(C.V_RUNTIME);
				else if (o.equals("fcc")) this.verbosity.set(C.V_FCC);
				else if (o.equals("fc")) this.verbosity.set(C.V_FC);
				//else if (o.equals("hash")) this.verbosity.set(C.V_HASH);
				else { System.err.println("Error: unknown option in -v: "+o); Error.die(C.ERR_JUST_EXIT); }
			}
			else if (items[0].equals("vc"))     // verbosity combo
			{
				char c;
				int length = o.length();
				for (int i=0; i<length; ++i)
				{
					c = o.charAt(i);
					if (c == 'm') this.verbosity.set(C.V_MEMORY);
					else if (c == 'f') this.verbosity.set(C.V_FUNCTION);
					else if ((c == 't') || (c == 'r')) this.verbosity.set(C.V_RUNTIME);
					//else if (c == 'h') this.verbosity.set(C.V_HASH);
					else { System.err.println("Error: unknown option in -vc: "+c); Error.die(C.ERR_JUST_EXIT); }
				}
			}
			else if (items[0].equals("convert") || items[0].equals("convertto") || items[0].equals("to") || items[0].equals("convert2"))
			{
				/*if (o.equals("basenum"))       Database.setConvertFileType(C.FT_BASENUM);
              else if (o.equals("bool"))     Database.setConvertFileType(C.FT_BOOL);
              else if (o.equals("rcf"))      Database.setConvertFileType(C.FT_RCF);
              else if (o.equals("pair") || 
                       o.equals("pairs"))    Database.setConvertFileType(C.FT_PAIRS);
              else if (o.equals("square"))   Database.setConvertFileType(C.FT_SQUARE);
              else if (o.equals("pbm"))      Database.setConvertFileType(C.FT_PBM);
              else if (o.equals("ppm"))      Database.setConvertFileType(C.FT_PPM);
              else {
                 System.err.println("Error: you want to convert to an unknown file format.");
                 System.exit(-1);
              }*/

				System.err.println("Error: unkwown option -"+items[0]);
				System.err.println("Tip: if you need to convert your file in a different format, please use");
				System.err.println("     ./pre02_converter.sh, in the preproc/ module.\n");
				Error.die(C.ERR_JUST_EXIT);
			}
			else if (items[0].equals("out") || items[0].equals("of"))
			{
				redirectOutput(o);
				Global.isRedirected = true;
			}
			else if (items[0].equals("alg"))
			{
				if      (o.equals("apriori"))                             this.algorithms.set(C.ALG_APRIORI);
				else if (o.equals("ac") 
						|| o.equals("aprioriclose"))                        this.algorithms.set(C.ALG_APRIORI_CLOSE);
				else if (o.equals("ai") 
						|| o.equals("aprioriinverse"))                      this.algorithms.set(C.ALG_APRIORI_INVERSE);
				else if (o.equals("close"))                               this.algorithms.set(C.ALG_CLOSE);
				else if (o.equals("aclose"))                              this.algorithms.set(C.ALG_A_CLOSE);
				else if (o.equals("pascal"))                              this.algorithms.set(C.ALG_PASCAL);
				else if (o.equals("pascal+") 
						|| o.equals("pascalplus"))                          this.algorithms.set(C.ALG_PASCAL_PLUS);
				else if (o.equals("zart1"))                               this.algorithms.set(C.ALG_ZART_1);
				else if (o.equals("zart") 
						|| o.equals("zart2"))                               this.algorithms.set(C.ALG_ZART_2);
				else if (o.equals("rms") 
						|| o.equals("carpathia"))                           this.algorithms.set(C.ALG_CARPATHIA);
				else if (o.equals("carpathiag"))                          this.algorithms.set(C.ALG_CARPATHIA_G);
				else if (o.equals("carpathiagrare") 
				      || o.equals("mrgexp"))                              this.algorithms.set(C.ALG_CARPATHIA_G_RARE);
				else if (o.equals("titanic"))                             this.algorithms.set(C.ALG_TITANIC);
				else if (o.equals("charm1"))                              this.algorithms.set(C.ALG_CHARM_1);
				else if (o.equals("charm2"))                              this.algorithms.set(C.ALG_CHARM_2);
				else if (o.equals("charm3"))                              this.algorithms.set(C.ALG_CHARM_3);
				else if (o.equals("charm4"))                              this.algorithms.set(C.ALG_CHARM_4);
				else if (o.equals("charm4b"))                             this.algorithms.set(C.ALG_CHARM_4B);
				else if (o.equals("charm"))                               this.algorithms.set(C.ALG_CHARM_DEFAULT);
				else if (o.equals("eclat") 
						|| o.equals("eclat1"))                              this.algorithms.set(C.ALG_ECLAT_1);
				else if (o.equals("eclat2"))                              this.algorithms.set(C.ALG_ECLAT_2);
				else if (o.equals("eclat3"))                              this.algorithms.set(C.ALG_ECLAT_3);
				//else if (o.equals("sabre") || o.equals("sabre1"))         this.algorithms.set(C.ALG_SABRE1);
				else if (o.equals("eclatz")
						|| o.equals("eclatz1"))                             this.algorithms.set(C.ALG_ECLAT_Z);
				else if (o.equals("eclatz2"))                             this.algorithms.set(C.ALG_ECLAT_Z_2);
				else if (o.equals("eclatz3"))                             this.algorithms.set(C.ALG_ECLAT_Z_3);
				else if (o.equals("apriorirare"))                         this.algorithms.set(C.ALG_APRIORI_RARE);
				else if (o.equals("apriori2"))                            this.algorithms.set(C.ALG_APRIORI_2);
				else if (o.equals("stone"))                               this.algorithms.set(C.ALG_STONE);
				else if (o.equals("ara"))                                 this.algorithms.set(C.ALG_ARA);
				else if (o.equals("arima"))                               this.algorithms.set(C.ALG_ARIMA);
				else if (o.equals("btb"))                                 this.algorithms.set(C.ALG_BTB);
				else if (o.equals("eclatg"))                              this.algorithms.set(C.ALG_ECLAT_G);
				else if (o.equals("genclosure"))                          this.algorithms.set(C.ALG_GEN_CLOSURE);
				else if (o.equals("touch0"))                              this.algorithms.set(C.ALG_TOUCH_0);
				else if (o.equals("touch1"))                              this.algorithms.set(C.ALG_TOUCH_1);
				else if (o.equals("touch"))                               this.algorithms.set(C.ALG_TOUCH_DEFAULT);
				else if (o.equals("pseudoclosed") || o.equals("pc"))      this.algorithms.set(C.ALG_PSEUDO_CLOSED);
				else if (o.equals("declat"))                              this.algorithms.set(C.ALG_DECLAT);
				else if (o.equals("dcharm"))                              this.algorithms.set(C.ALG_DCHARM);
				else if (o.equals("mfi"))                                 this.algorithms.set(C.ALG_MFI_SIMPLE);
				else if (o.equals("daa"))                                 this.algorithms.set(C.ALG_DAA);
				else if (o.equals("mfi2mri"))                             this.algorithms.set(C.ALG_MFI2MRI);
				else if (o.equals("talky"))                               this.algorithms.set(C.ALG_TALKY);
				else if (o.equals("talkyg"))                              this.algorithms.set(C.ALG_TALKY_G);
				else if (o.equals("dtalkyg"))                             this.algorithms.set(C.ALG_DTALKY_G);
				else if (o.equals("dtouch"))                              this.algorithms.set(C.ALG_DTOUCH_1);
				else if (o.equals("cgmatch"))                             this.algorithms.set(C.ALG_CG_MATCH);
				else if (o.equals("norris"))                              this.algorithms.set(C.ALG_NORRIS);
				else if (o.equals("cbo") || o.equals("closebyone"))       this.algorithms.set(C.ALG_CLOSE_BY_ONE);
				else if (o.equals("nc")  || o.equals("nextclosure"))      this.algorithms.set(C.ALG_NEXT_CLOSURE);
				else if (o.equals("flake"))                               this.algorithms.set(C.ALG_FLAKE);  // newInCoron1
				else if (o.equals("flakeexp1"))                           this.algorithms.set(C.ALG_FLAKE_EXP1);  // newInCoron1
				else if (o.equals("flakeexp2"))                           this.algorithms.set(C.ALG_FLAKE_EXP2);  // newInCoron1
				else if (o.equals("flakeexp3"))                           this.algorithms.set(C.ALG_FLAKE_EXP3);  // newInCoron1
				else if (o.equals("ralky"))                               this.algorithms.set(C.ALG_RALKY);  // newInCoron1
				else if (o.equals("walkyg"))                              this.algorithms.set(C.ALG_WALKY_G);  // newInCoron1
				else if (o.equals("walkyg2"))                             this.algorithms.set(C.ALG_WALKY_G2);  // newInCoron1
				else if (o.equals("talkyg2"))                             this.algorithms.set(C.ALG_TALKY_G2);  // newInCoron1
				else if (o.equals("flakeexp4"))                           this.algorithms.set(C.ALG_FLAKE_EXP4);  // newInCoron1
				else if (o.equals("lcmgr"))                           	  this.algorithms.set(C.ALG_LCM_GRGROWTH);  // newInCoron1
				else if (o.equals("cgt"))                           	  this.algorithms.set(C.ALG_CGT);  // newInCoron1
				/*else if (o.equals("charmmfi") || o.equals("mfi")) {
                 this.algorithms.set(C.ALG_CHARM_MFI);
                 if (o.equals("mfi")) this.extra.set(C.X_SHOW_ONLY_MFI);
                 // Charm-MFI is deprecated.
              }*/
				else {
					System.err.println("Error: you want to use an unknown algorithm.");
					Error.die(C.ERR_JUST_EXIT);
				}
			}
			else
			{
				System.err.println("Error: unknown option: -"+items[0]);
				Error.die(C.ERR_JUST_EXIT);
			}
		}
	}

	/**
	 * In the whole project we use System.out.println() everywhere.
	 * But, if we want to write the result directly to a file without
	 * redirection (" > file.txt"), what could we do? Change System.out
	 * everywhere? Thank God it is not necessary. Java makes it possible
	 * to redirect stdout to a file. Thus we can continue using
	 * System.out.println(), but these will go to a file.
	 * 
	 * Command line options: -out=file, or
	 *                       -of=file (where 'of' stands for 'output file')
	 * 
	 * @param outputFile Name of the output file.
	 */
	private void redirectOutput(final String outputFile)
	{
		if (Global.isRedirected)
			Error.die(C.ERR_REDIRECTED);
		else
			Global.isRedirected = true;

		File file = new File(outputFile);
		// Store the name chosen by the user then transformed:
		Global.setOutputFileName(file.getAbsolutePath());

		PrintStream printStream = null;

		if (file.isDirectory()) 
		{
			System.err.println("Coron: error: the specified output file '"+outputFile+"' cannot be created!");
			System.err.println("Reason: there is already a directory with this name.");
			System.err.println("Tip: choose a different filename.");
			Error.die(C.ERR_JUST_EXIT);
		}

		/*if (file.isFile()) 
      {
         // if forced deletion is off, and the file exists: error
         if (this.forceDel == false)
         {
            System.err.println("Coron: error: the specified output file '"+outputFile+"' already exists!");
            System.err.println("Tips: 1) delete this file if you don't need it (use the -del option), or");
            System.err.println("      2) rename/move the file somewhere else");
            System.exit(-1);
         }
         else  // if forced deletion is on: try to delete the file
         {
            if (file.delete() == false)
            {
               System.err.println("Coron: error: the specified output file '"+outputFile+"' exists and cannot be deleted!");
               System.err.println("Reason: the file may be write protected.");
               System.exit(-1);
            }
         }
      }*/

		try
		{
			printStream = new PrintStream(file);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Coron: error: the stdout redirection was not successful!");
			e.printStackTrace();
			Error.die(C.ERR_JUST_EXIT);
		}
		System.setOut( printStream ); 
	}

	/**
	 * Checks the algorithms, what was chosen. 
	 */
	private void postCheckAlg()
	{      
		if (this.algorithms.cardinality() == 0) {
			//System.err.println("> Remark: as no algorithm was chosen (-alg=<alg>), Charm will be used as default.");
			this.algorithms.set(C.ALG_CHARM_DEFAULT);    // Charm is used by default
		}

		// else
		if (this.algorithms.cardinality() > 1) {
			System.err.println("Error: choose just ONE algorithm please.");
			Error.die(C.ERR_JUST_EXIT);
		}

		// else, if everything is OK, i.e. only 1 algorithm was chosen
		int algo = this.algorithms.nextSetBit(0);
		Global.setAlgorithm(algo);
		
		if (algo == C.ALG_LCM_GRGROWTH)
		{
			if (this.min_supp_percent != null) {
				System.err.println("Error: you must use absolute support with this algorithm.");
				System.exit(1);
			}
		}

		DemoCoron.checkAlg(algo);
	}

	/**
	 * Processes extra options. These options look like: -asc , -desc , -all , etc.
	 * 
	 * @param option An extra option.
	 */
	private void processExtraOption(String option)
	{
		// lowercase, change '-' to '_', then remove all '_'
		option = option.toLowerCase().replaceAll("-", "_").replaceAll("_", "");
		//System.err.println("option: '"+option+"'");

		if (option.equals("asc"))                         this.extra.set(C.X_ASC);
		else if (option.equals("desc"))                   this.extra.set(C.X_DESC);
		else if (option.equals("fi"))                     this.extra.set(C.X_FI);
		//else if (option.equals("ext") || option.equals("extent") || option.equals("extension") || option.equals("concept")) this.extra.set(C.X_EXT);
		//else if (option.equals("galois") || option.equals("lattice")) this.extra.set(C.X_LATTICE);
		else if (option.equals("stat"))                   this.extra.set(C.X_STOP_STAT);
		else if (option.equals("analyze") || option.equals("analyzedb")) 
		{
			this.extra.set(C.X_STOP_STAT);         // print stat info
			this.extra.set(C.X_STOP_ANALYZE);      // and print then some more info
		}
		else if (option.equals("null"))                   this.extra.set(C.X_CORON_NULL);
		else if (option.equals("shutup"))                 this.extra.set(C.X_SHUT_UP);
		else if (option.equals("trie") || 
				option.equals("showtrie") || 
				option.equals("viewtrie"))               this.extra.set(C.X_SHOW_TRIE);
		else if (option.equals("dump"))                   this.extra.set(C.X_DUMP);
		/*else if (option.equals("pos0") || 
               option.equals("index0"))                 this.extra.set(C.X_POS0);*/
		else if (option.equals("letter") || 
				option.equals("letters") || 
				option.equals("text") || 
				option.equals("name") || 
				option.equals("names"))                  this.extra.set(C.X_LETTERS);
		else if (option.equals("saveF".toLowerCase()))    this.extra.set(C.DB_SAVE_F);
		else if (option.equals("saveZ".toLowerCase()))    this.extra.set(C.DB_SAVE_Z);
		else if (option.equals("saveMemF".toLowerCase())) this.extra.set(C.MEM_SAVE_F);
		else if (option.equals("saveMemZ".toLowerCase())) this.extra.set(C.MEM_SAVE_Z);
		//else if (option.equals("nof2".toLowerCase()))     Global.setNoF2();
		else if (option.equals("usef2".toLowerCase()))     Global.setUseF2();
		else if (option.equals("all".toLowerCase()))      this.extra.set(C.X_RARE_ALL);
		else if (option.equals("nonzero".toLowerCase()))  this.extra.set(C.X_RARE_NONZERO);
		// let's deactivate the treeset option. It'll rather be treated at the algorithm section.  
		//else if (option.equals("treeset".toLowerCase())) Global.setItemsetRepresentation(C.REPR_TREESET);
		else
		{
			System.err.println("Error: unknown option: -"+option);
			Error.die(C.ERR_JUST_EXIT);
		}
	}

	/**
	 * It performs some post checkings on the options. Ex.: sets the default
	 * ordering if it was not chosen by the user.
	 */
	private void postCheckExtra()
	{
		BitSet extra = this.extra;

		// conflict check
		if (extra.get(C.X_ASC) && extra.get(C.X_DESC))
		{
			System.err.println("Error: -asc and -desc options conflict with each other. Choose just one of them.");
			Error.die(C.ERR_JUST_EXIT);
		}
		// post check
		// if we want order between concepts, then first we need to find the extensions
		if (extra.get(C.X_LATTICE)) extra.set(C.X_EXT); 

		// if we want extensions => ascending order is required
		if (extra.get(C.X_EXT))
		{
			if (extra.get(C.X_DESC))
				System.err.println("Coron: you want to calculate the extensions too, but you "+
						"specified descending order.\n"+
						"         For the extensions ascending "+
				"order is needed, so your settings are overriden.");
			extra.set(C.X_ASC);
			System.err.println("Coron: ascending order is set.");
		}

		// if no order is specified
		if ((extra.get(C.X_ASC) == false) && (extra.get(C.X_DESC) == false))
		{
			extra.set(C.X_DESC);		// default ordering: descending
			// ascending/descending is only interesting for Close
			if (this.algorithms.get(C.ALG_CLOSE))
				System.err.println("> Coron: as no ordering was specified, the default will be descending.");
		}

		if (this.algorithms.get(C.ALG_APRIORI_RARE) || 
		    this.algorithms.get(C.ALG_CARPATHIA_G_RARE) ||
		    this.algorithms.get(C.ALG_WALKY_G) ||
		    this.algorithms.get(C.ALG_WALKY_G2))
		{
			checkRareAlgorithms();
		}
		
		if (this.algorithms.get(C.ALG_LCM_GRGROWTH))
		{
			extra.set(C.X_DONT_READ_DATABASE);
		}
	}

	/**
	 * Check if it's precisely set what to extract: all rare itemsets (zero itemesets too, 
	 * where support = 0), or just non-zero itemsets (support > 0).
	 */
	public static void checkRareAlgorithms()
	{
		boolean all     = Global.getExtra().get(C.X_RARE_ALL);
		boolean nonzero = Global.getExtra().get(C.X_RARE_NONZERO);

		/*
		 * problem: none of them is set OR both of them are set
		 * ONLY one of them can be set
		 */
		if ( (all == false && nonzero == false) || (all && nonzero) )
		{
			System.err.println("You want to extract rare itemsets. You must choose:");
			System.err.println("   -all     : extract zero itemsets too (where support = 0), or");
			System.err.println("   -nonzero : extract non-zero itemsets only (where support > 0)");
			System.err.println("You must specify just one option.");

			Error.die(C.ERR_JUST_EXIT);
		}
		// at the end, here, it is guaranteed that "all" xor "non-zero" is set
	}

	/**
	 * Converts a String array to a vector of Strings.
	 * 
	 * @param args Command line arguments in a String array.
	 * @return A vector that contains the command line arguments. 
	 */
	@SuppressWarnings("unchecked")
	private Vector getArgs(String[] args)
	{
		Vector v = new Vector();
		for (int i = 0; i<args.length; ++i)
			v.add(args[i]);
		return v;
	}

	/**
	 * Prints some help about the usage of the program.
	 */
	private void print_help()
	{
		boolean demo = DemoCoron.isDemo(); 
		StringBuilder sb = new StringBuilder();
		sb.append("Coron "+C.VERSION+", (c) copyright Laszlo Szathmary, 2004--2010 (Szathmary.L@gmail.com)\n");
		sb.append("\n");
		sb.append("Usage: ./core01_coron.sh  [switches]  <database>  <min_supp>  [-alg:<alg>]\n");
		sb.append("Switches:\n");
		sb.append("   --help                          help information (you can see this now)\n");
		sb.append("                                   can be used _alone_\n");
		sb.append("   --version, -V                   version information\n");
		sb.append("                                   can be used _alone_\n");
		sb.append("   --update                        check for new version\n");
		sb.append("                                   can be used _alone_\n");
		sb.append("\n");
		if (demo == false)
		{
			sb.append("   -v:<option>                     verbosity\n");
			sb.append("   verbosity options:\n");
			sb.append("      m, mem, memory               memory usage\n");
			sb.append("      f, fun, func,\n");
			sb.append("      function, functions          follow functions\n");
			sb.append("      t, time                      runtime\n");
			sb.append("      fcc                          FCC tables (only available with Close algorithm)\n");
			sb.append("      fc                           FC tables (only available with Close algorithm)\n");
			//sb.append("      hash                         one hashmark for "+C.HASH_STEP+" found links\n");
			sb.append("\n");
			sb.append("Note: these options cannot be combined in one step.\n");
			sb.append("Example: to get runtime and memory usage: -v:time -v:mem\n");
			sb.append("\n");
			sb.append("To combine options, use another switch:\n");
			sb.append("   -vc:<options>                   verbosity combo\n");
			sb.append("                                   an option can only be 1 character here\n");
			sb.append("   options:\n");
			sb.append("      m                            memory usage\n");
			sb.append("      f                            follow functions\n");
			sb.append("      t, r                         runtime\n");
			sb.append("      h                            one hashmark for "+C.HASH_STEP+" found links\n");
			//sb.append("Example: to get runtime and memory usage: -vc:tm\n");
			sb.append("\n");
		}
		sb.append("Extra options:\n");
		if (demo == false)
		{
			sb.append("   -asc                            order itemsets in ascending order\n");
			sb.append("   -desc                           order itemsets in descending order\n");
			// Past version:
			// sb.append("   -fi, -a, -all                   find all frequent itemsets\n");
			// New version suggested. Finally hidden for uselessness
			//   sb.append("   -fi                             find all frequent itemsets from closed frequent itemsets (with Close algorithm)\n");
			//
			sb.append("   -stat                           stop after printing statistics at the beginning\n");
			sb.append("   -null                           no output of result to stdout\n");
			sb.append("   -shutup                         (with -null) no output of result to stdout, for any algorithm used\n");
			sb.append("   -trie, -showtrie, -show-trie\n");
			sb.append("   -show_trie                      show GraphViz .dot representation of FCIs\n");
			sb.append("   -dump                           dump partial results of FC tables immediately\n");
			sb.append("                                   Warning! Duplicates are possible, some post-processing is required!\n");
		}
		//sb.append("   -pos0, -index0                  indexing of attributes start with 0 (default: 1)\n");
		sb.append("                                   can be used in the case of .bool and .rcf files\n");
		sb.append("   -letter, -letters\n");
		sb.append("   -text, -name, -names            numbers in itemsets are replaced by their names.\n");
		sb.append("                                   Only relevant with .rcf input files!\n");
		sb.append("   -usef2                          use an upper-triangular matrix for calculating the support of 2-itemsets\n");
		sb.append("   -of:<output_file>               redirect the output to the given file\n");
		sb.append("\n");
		/*sb.append("Conversions:\n");
      sb.append("   -to:<dest_format>, where <dest_format> can be:\n");
      sb.append("\n");
      sb.append("   basenum                         basenum format\n");
      sb.append("   bool                            binary matrix\n");
      sb.append("   rcf                             Rich Context Family, with binary relation\n");
      sb.append("   square                          like RCF, with similarities and dissimilarities between objects\n");
      sb.append("   pbm                             Portable Bit Map, a monochrome graphic format (open with Gimp)\n");
      sb.append("   ppm                             similar to PBM, but more widely supported (open with Gimp)\n");
      sb.append("\n");*/
		sb.append("Different algorithms:\n");
		sb.append("   Usage: -alg:<alg>, where <alg> can be:\n");
		sb.append("\n");
		if (DemoCoron.isAlgAllowed(C.ALG_APRIORI))
			sb.append("   apriori                         Apriori [FIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_APRIORI_CLOSE))
			sb.append("   apriori-close, ac               Apriori-Close [FIs, mark FCIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_APRIORI_INVERSE))
			sb.append("   apriori-inverse, ai             Apriori-Inverse [PRIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_CLOSE))
			sb.append("   close                           Close [FCIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_A_CLOSE))
			sb.append("   a-close                         A-Close [FGs, FCIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_PASCAL))
			sb.append("   pascal                          Pascal [FIs, FGs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_PASCAL_PLUS))
			sb.append("   pascal-plus, pascal+            Pascal+ [FIs, FGs, mark FCIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_TITANIC))
			sb.append("   titanic                         Titanic\n");
		if (DemoCoron.isAlgAllowed(C.ALG_ZART_2))
			sb.append("   zart                            ZART [FIs, FGs, mark FCIs,\n"+
			"                                         associate FGs to their closures]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_CARPATHIA))
			sb.append("   rms, carpathia                  RMS Carpathia [FGs and their closures]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_CARPATHIA_G))
			sb.append("   carpathia-g                     Carpathia-G [FGs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_CARPATHIA_G_RARE))
			sb.append("   carpathia-g-rare                Carpathia-G-Rare [MRGs (= MRIs)]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_CHARM_DEFAULT))
			sb.append("   charm                           Charm (default) [FCIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_DCHARM))
			sb.append("   dcharm                          dCharm [FCIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_ECLAT_1))
			sb.append("   eclat                           Eclat [FIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_DECLAT))
			sb.append("   declat                          dEclat [FIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_ECLAT_Z))
			sb.append("   eclat-z                         Eclat-Z [FIs, mark FCIs, mark FGs,\n"+
			"                                            associate FGs to their closures]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_ECLAT_G))
			sb.append("   eclat-g                         Eclat-G [FGs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_GEN_CLOSURE))
			sb.append("   gen-closure                     Gen-Closure [FGs + their closures]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_TOUCH_1))
			sb.append("   touch                           Touch [FCIs + their FGs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_APRIORI_RARE))
			sb.append("   apriori-rare                    Apriori-Rare [MRIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_STONE))
			sb.append("   stone                           Stone [RIs, MFIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_ARA))
			sb.append("   ara                             Ara [RIs, FIs (using MZGs)]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_ARIMA))
			sb.append("   arima                           Arima [RIs (using MRIs and MZGs)]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_BTB))
			sb.append("   btb                             BtB [rare equivalence classes]\n");
		/*if (DemoCoron.isAlgAllowed(C.ALG_CHARM_MFI)) {
      sb.append("   charm-mfi                       Charm-MFI [FCIs, MFIs are marked]\n");
      sb.append("   mfi                             Charm-MFI [MFIs]\n");
      // Charm-MFI is deprecated
      }*/
		if (DemoCoron.isAlgAllowed(C.ALG_MFI_SIMPLE))
			sb.append("   mfi                             MFI-Simple [MFIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_PSEUDO_CLOSED))
			sb.append("   pseudo-closed, pc               Pseudo-Closed [FPCIs + their closures]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_DAA))
			sb.append("   daa                             Dualize and Advance [MFIs and mRIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_MFI2MRI))
			sb.append("   mfi2mri                         MFI2mRI [MFIs and mRIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_TALKY))
			sb.append("   talky                           Talky [FIs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_TALKY_G))
			sb.append("   talky-g                         Talky-G [FGs]\n");      
		if (DemoCoron.isAlgAllowed(C.ALG_DTALKY_G))
			sb.append("   dtalky-g                        dTalky-G [FGs]\n");
		if (DemoCoron.isAlgAllowed(C.ALG_DTOUCH_1))
			sb.append("   dtouch                          dTouch [FCIs + their FGs]\n");
		sb.append("   cbo                             CloseByOne [CIs with min_supp > 0]\n");
		sb.append("   norris                          Norris [CIs with min_supp > 0]\n");
		sb.append("   nc                              NextClosure [CIs with min_supp > 0]\n");
		sb.append("\n");
		sb.append("   database                        database file\n");
		sb.append("   min_supp                        minimum support, integer or double followed by '%'\n");
		sb.append("                                   example: 2 or 20% or 1.75%\n");
		System.out.println(sb);
	}

	/**
	 * Prints a warning to let the user know that the output may contain duplicates.
	 */
	public void dump_warning()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Warning! This output was generated using the -dump option, which means that the\n");
		sb.append("partial results (FC_i tables) are not stored in the memory but dumped to stdout\n");
		sb.append("immediately. It can result duplicates, so some kind of postprocessing is required!\n");
		sb.append("Tip: redirect output to a file, then: 'sort file | uniq'\n");

		System.err.println(sb.toString());
	}

	/**
	 * @return Returns the algorithms.
	 */
	public BitSet getAlgorithms() {
		return this.algorithms;
	}

	/**
	 * @param algorithms The algorithms to set.
	 */
	public void setAlgorithms(BitSet algorithms) {
		this.algorithms = algorithms;
	}
}
