package fr.loria.coronsys.coron.helper;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import java.io.File;

import fr.loria.coronsys.coron.helper.C;

/**
 * Stores every important information related to the input dataset.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class Database
{
	/**
	 * Empty private constructor. The class cannot be instantiated.
	 */
	private Database() { }

	/**
	 * Name of the database file as a string.
	 */
	private static String database_file_str;

	/**
	 * The database file as a File object.
	 */
	private static File database_file;

	/**
	 * Type of the input database (.basenum, .bool, .rcf, etc.).
	 */
	private static int database_file_type;

	/**
	 * Convert the input file into this format.
	 */
	private static int convert_file_type;

	/**
	 * The input database. An element of the vector contains one line of the database.
	 */
	private static Vector<BitSet> database;

	/**
	 * The minimum support.
	 */
	private static int min_supp;

	/**
	 * Minimum confidence. Used in AssRuleX.
	 */
	private static Double min_conf;

	/**
	 * Minimum support as a double value (percentage).
	 */
	private static Double min_supp_percent;

	/**
	 * Total Number Of Attributes of the database, empty or not. (previously "Largest attribute").
	 */
	private static int totalNumberOfAttr;

	/**
	 * Total number of NON EMPTY attributes. (previously "totalNbOfNonEmptyAttr").
	 */
	private static int totalNbOfNonEmptyAttr;

	/**
	 * Number of attributes per object in average.
	 */
	private static double numberOfAttrInAvg;

	/**
	 * An array of object names. Thus we can find out the name of an object 
	 * by its number.
	 */
	private static Vector<String> objectNames = new Vector<String>();

	/**
	 * An array of attribute names. Thus we can find out the name of an attribute 
	 * by its number.
	 */   
	private static Vector<String> attributeNames;

	/**
	 * In an .rcf file we know the number of attributes before reading
	 * the boolean table. We register this value here, and while reading
	 * the database we check if each object has the correct number of attributes.
	 */
	private static int attr_number;

	/**
	 * In an .rcf file we know the number of objects before reading
	 * the boolean table. We register this value here, and while reading
	 * the database we check if the number of objects is correct.
	 */
	private static int obj_number;

	/**
	 * It is needed when we use vertical representation of the dataset to register
	 * the number of objects. In horizontal case it is easy to find it out, but in
	 * vertical case it is better to store it in a variable.
	 */
	private static int numberOfObjects;

	/**
	 * Is the database representation horizontal (default) or vertical?
	 */
	private static int db_representation;

	/**
	 * Is there a full column in the input dataset?
	 */
	private static boolean fullColumn;

	/**
	 * Length of the longest possible itemset. It is determined while reading
	 * the dataset. 
	 */
	private static int longestItemset;

	/**
	 * It'll contain all the attributes.
	 */
	private static BitSet attributes;

	/**
	 * Is the database inverted?
	 * Normal database: a binary context as it was read.
	 * Inverted database: 0s and 1s are inverted.
	 * Normally, a DB is not inverted.
	 */
	private static boolean inverted;

	// ***********************************************************************

	/*
	 * "constructor"
	 * 
	 *  reset() must do the same thing as this one
	 */ 
	static {
		Database.reset();
	}

	/**
	 * Re-initialise the state.
	 * 
	 * If you want to run Coron's algorithms in a loop, this function must
	 * be called. This function will re-initialise the necessary things.
	 */
	public static void reset()
	{
		Database.convert_file_type = C.FT_NOTHING;
		// Horizontal repr. is the default. Exception for instance: Charm
		//Database.db_representation = C.DBR_HORIZONTAL;
		Database.inverted = false;
	}

	// ***********************************************************************

	/**
	 * Gets the vector of bitsets, representing the input database.
	 * 
	 * @return Returns the database as a vector of bitsets.
	 */
	public static Vector<BitSet> getDatabase() {
		return database;
	}

	/**
	 * @return True, if the DB representation is horizontal. False, otherwise.
	 */
	public static boolean isDBRepresentationHorizontal() {
		return (Database.getDBRepresentation() == C.DBR_HORIZONTAL);
	}

	/**
	 * @return True, if the DB representation is vertical. False, otherwise.
	 */
	public static boolean isDBRepresentationVertical() {
		return (Database.getDBRepresentation() == C.DBR_VERTICAL);
	}

	/**
	 * Gets the File object representing the input database.
	 * 
	 * @return Database file as a File object.
	 */
	public static File getDatabaseFile() {
		return Database.database_file;
	}

	/**
	 * Sets the database. After this we know the size of the dataset, i.e. we know
	 * how many objects it contains. If the min_supp was set as a double value (percentage),
	 * then we need to calculate how much it really means as an integer value.
	 * 
	 * Calculates the minsupport as integer by using the minsupport as a double (percent).
	 * If minsupport would be a dummy value (less than 1, which has no sense), the
	 * function corrects it automagically.
	 * That is: conversion from percentage to integer.
	 * 
	 * @param database The database to set.
	 * @param db_representation 
	 */
	public static void setDatabase(Vector<BitSet> database, final int db_representation) 
	{
		Database.database = database;
		//System.err.println("!!! DB size: "+Database.getNumberOfObjects());

		modifyMinSupp();
		setAttrInfos();

		Database.setDbRepresentation(db_representation);
	}

	/**
	 * Used after "horizontal to vertical" and "vertical to horizontal"
	 * transformations. Register the new database AND register the representation
	 * too!
	 * 
	 * setDatabase() does some post-modifications too, while this function not
	 * 
	 * @param database The database to register.
	 * @param db_representation Database representation (horizontal or vertical?)
	 */
	public static void registerDatabase(Vector<BitSet> database, final int db_representation) 
	{
		Database.database = database;
		Database.setDbRepresentation(db_representation);
	}

	/**
	 * Used for the CHARM algorithm. When level 1 is initialized in the IT-tree and 
	 * thus frequent attrbutes are copied, then the vertical representation of the
	 * dataset is not needed anymore in the memory, it can be freed.
	 */
	public static void freeDatabase() {
		Database.database = null;
	}

	/**
	 * If the minimum support was given in percentage, then we need to post-modify
	 * the integer value of minimum support too. Why? Imagine that we have 100 objects,
	 * and min. supp. is set to 2,6%. Thus the min_supp should be 2.6 , but it is
	 * impossible since the min_supp must be an integer. So we round it, it will be 3,
	 * which is 3%. So we modify 2,6% up to 3%, and min_supp will be 3.
	 */
	private static void modifyMinSupp()
	{
		if (Database.min_supp_percent != null)		// then modification of Database.min_supp (the int) is needed
		{
			//int lines = Database.database.size();
			int lines = Database.getNumberOfObjects();
			Database.min_supp = (int)Math.round((double)lines * (min_supp_percent.doubleValue() / 100));
		}
		// a minsupport value < 1 makes no sense, so if some dummy value was set
		// like -1 or 0, correct it back to 1. This is an automatic error correction.
		if (Database.min_supp < 1) Database.min_supp = 1;
	}

	/**
	 * Finds and sets different information about the attributes.
	 * Namely: largest attr., total number of attributes, and 
	 * number of attributes per object in average.
	 */
	@SuppressWarnings("unchecked")
	private static void setAttrInfos()
	{
		long all_attr = 0;

		if (Global.getItemsetRepresentation() == C.REPR_BITSET)
		{
			BitSet bs = new BitSet(), curr;

			for (Enumeration e = Database.database.elements(); e.hasMoreElements(); )
			{
				curr = (BitSet)e.nextElement();
				bs.or(curr);
				all_attr += curr.cardinality();
			}

			Database.totalNumberOfAttr			= bs.length()-1;
			Database.totalNbOfNonEmptyAttr 		= bs.cardinality();
			Database.numberOfAttrInAvg = (double)all_attr / (double)Database.database.size();
		}
		else if (Global.getItemsetRepresentation() == C.REPR_TREESET)
		{
			TreeSet ts = new TreeSet(), curr;

			for (Enumeration e = Database.database.elements(); e.hasMoreElements(); )
			{
				curr = (TreeSet) e.nextElement();
				ts.addAll(curr);
				all_attr += curr.size();
			}

			Database.totalNumberOfAttr       = ((Integer)ts.last()).intValue();
			Database.totalNbOfNonEmptyAttr      = ts.size();
			Database.numberOfAttrInAvg = (double)all_attr / (double)Database.database.size();
		}
		else // if representation is Vector
		{
			TreeSet ts = new TreeSet();
			Vector curr;

			for (Enumeration e = Database.database.elements(); e.hasMoreElements(); )
			{
				curr = (Vector) e.nextElement();
				ts.addAll(curr);
				all_attr += curr.size();
			}

			Database.totalNumberOfAttr       = ((Integer)ts.last()).intValue();
			Database.totalNbOfNonEmptyAttr      = ts.size();
			Database.numberOfAttrInAvg = (double)all_attr / (double)Database.database.size();
		}
	}

	/**
	 * Gets the minimum support (int).
	 * 
	 * @return Returns the minimum support.
	 */
	public static int getMinSupp() {
		return min_supp;
	}

	/**
	 * Sets the minimum support (int).
	 * 
	 * @param min_supp The minimum support to set.
	 */
	public static void setMinSupp(int min_supp) {
		Database.min_supp = min_supp;
	}

	/**
	 * Gets the filename of the input file.
	 * 
	 * @return Returns the filename of the input file.
	 */
	public static String getDatabaseFileStr() {
		return database_file_str;
	}

	/**
	 * Sets the filename of the database + creates a File
	 * object too for the input database too.
	 * 
	 * @param database_file The name of the database file.
	 */
	public static void setDatabaseFileStr(String database_file) {
		Database.database_file_str = database_file;
		Database.database_file		= new File(database_file);
	}

	/**
	 * Gets the minimum support in percentage (Double). 
	 * 
	 * @return Returns the minimum support in percentage (Double).
	 */
	public static Double getMinSuppPercent() {
		return min_supp_percent;
	}

	/**
	 * Sets the minimum support in percentage (Double).
	 * 
	 * @param min_supp_percent The min_supp_percent to set.
	 */
	public static void setMinSuppPercent(Double min_supp_percent) {
		Database.min_supp_percent = min_supp_percent;
	}

	/**
	 * Returns the _relative_ path of the input file (in Unix style).
	 * 
	 * @return Path of the input file.
	 */
	public static String getDatabaseFilePath() {
		return Database.database_file.getPath().replaceAll("\\\\", "/");
	}

	/**
	 * Returns the _absolute_ path of the input file (in Unix style).
	 * 
	 * @return Path of the input file.
	 */
	public static String getDatabaseAbsoluteFilePath() {
		return Database.database_file.getAbsolutePath().replaceAll("\\\\", "/");
	}

	/**
	 * Returns the size of the input file in bytes.
	 * 
	 * @return Size of the input file in bytes.
	 */
	public static long getDatabaseFileSize() {
		return Database.database_file.length();
	}
	/**
	 * Returns the total number of attributes (previously the largest attribute).
	 * 
	 * @return Returns the largest attribute.
	 */
	public static int getTotalNumberOfAttr() {
		return totalNumberOfAttr;
	}
	/**
	 * Total number of non empty attributes. 
	 * 
	 * @return Returns the total number of all attributes.
	 */
	public static int getTotalNbOfNonEmptyAttr() {
		return totalNbOfNonEmptyAttr;
	}
	/**
	 * Returns how many attributes an object has in average.
	 * 
	 * @return Returns how many attributes an object has in average.
	 */
	public static double getNumberOfAttrInAvg() {
		return numberOfAttrInAvg;
	}

	/**
	 * Returns the object names.
	 * 
	 * @return Returns the objectNames.
	 */
	public static Vector<String> getObjectNames() {
		return objectNames;
	}

	/**
	 * The object names are stored in a string array. There is
	 * one important thing! In the database the indexation of 
	 * objects always start by one (the first line shows 
	 * what attributes the first object has, etc.), but here in the
	 * array the indexation starts with 0. So to get the name of
	 * object i, we will have to read the element (i-1) of the array.
	 * 
	 * @param objectsV String array of object names.
	 */
	public static void setObjectNames(Vector<String> objectsV) {
		Database.objectNames = objectsV;
		Database.obj_number  = objectsV.size()-1;
	}

	/**
	 * Get the name of an object. There is one important thing here!
	 * In the database the indexation of 
	 * objects always start by one (the first line shows 
	 * what attributes the first object has, etc.), but here in the
	 * array the indexation starts with 0. So to get the name of
	 * object i, we will have to read the element (i-1) of the array.
	 * 
	 * @param i The number of the object (indexation starts with 1 in the dataset).
	 * @return Name of the object.
	 */
	public static String getObj(int i) 
	{
		if (Database.database_file_type == C.FT_RCF) 
			return (String) Database.objectNames.get(i);
		else
			return "o_"+i;
	}

	/**
	 * Gets the array of attribute names.
	 * 
	 * @return Returns the attributeNames.
	 */
	public static Vector<String> getAttributeNames() {
		return attributeNames;
	}

	/**
	 * @return Number of attribute names.
	 */
	public static int getAttributeNamesSize() {
		return attributeNames.size();
	}
	
	/**
	 * @return Given an attribute name, return the number of the attribute.
	 * For ex., in laszlo.rcf, given "c" it returns 3.
	 */
	public static int getAttributeNumberByName(String attrName) {
	   return attributeNames.indexOf(attrName);
	}

	/**
	 * Stores the attribute names in a string array.
	 * Indexation (0 or 1?) is already treated, we don't have to worry
	 * about it here.
	 * 
	 * @param attributes String array with attribute names.
	 */
	public static void setAttributeNames(Vector<String> attributes) {
		Database.attributeNames = attributes;
		Database.attr_number = attributes.size() -1;
	}

	/**
	 * Add a prefix to attribute names.
	 * 
	 * @param prefix A prefix to be added to attribute names.
	 */
	public static void addPrefixToAttributeNames(char prefix)
	{
		String oldName, newName;
		int size = Database.attributeNames.size();
		for (int i = 1; i < size; ++i)               // index 0 is not used
		{
			oldName = Database.attributeNames.get(i);
			newName = prefix + oldName; 
			Database.attributeNames.set(i, newName);
		}
	}

	/**
	 * If we invert an inverted dataset, then we get attribute
	 * names like "~~A". This is equivalent to "A" actually.
	 *  
	 * @param prefix The prefix that was used for indicating invertion.
	 */
	public static void removeDoubleNegationsFromAttributeNames(char prefix)
	{
		String oldName, newName;
		int size = Database.attributeNames.size();
		for (int i = 1; i < size; ++i)               // index 0 is not used
		{
			oldName = Database.attributeNames.get(i);
			newName = oldName.replaceFirst("^"+prefix+prefix, ""); 
			Database.attributeNames.set(i, newName);
		}  
	}

	/**
	 * Get the attribute's name at column number i.
	 * We don't have to worry about where the indexation begins (0 or 1),
	 * it is treated in an intelligent way. 
	 * 
	 * @param i Number of the attribute.
	 * @return Name of the attribute.
	 */
	public static String getAttr(int i) 
	{
		if (Database.database_file_type == C.FT_RCF) 
			return (String) Database.attributeNames.get(i);
		else
			return "a_"+i;
	}

	/**
	 * Used with .rcf files. Here we register the number of attribute names.
	 * Each object must have exactly this many attributes. If not => error.
	 * 
	 * @return Returns the number of attributes that each object must have
	 * according to the attribute name list in the .rcf file.
	 */
	public static int getAttrNumber() {
		return attr_number;
	}

	/**
	 * Used with .rcf files. Here we register the number of attribute names.
	 * Each object must have exactly this many attributes. If not => error.
	 *  
	 * @param attr_number The attribute number that each object must have.
	 */
	public static void setAttrNumber(int attr_number) {
		Database.attr_number = attr_number;
	}

	/**
	 * @return Returns the number of objects that must be in the database
	 * according to the object name list in the .rcf file.
	 */
	public static int getObjNumber() {
		return obj_number;
	}

	/**
	 * Gets the minimum confidence. Used in AssRuleX.
	 * 
	 * @return Returns the minimum confidence.
	 */
	public static Double getMinConf() {
		return min_conf;
	}

	/**
	 * Sets the minimum confidence. Used in AssRuleX.
	 * 
	 * @param min_conf The minimum confidence to set.
	 */
	public static void setMinConf(Double min_conf) {
		Database.min_conf = min_conf;
	}

	/**
	 * Gets the type of the database file (basenum?, bool?, rcf?, etc.).
	 * 
	 * @return Returns the database_file_type.
	 */
	public static int getDatabaseFileType() {
		return Database.database_file_type;
	}

	/**
	 * Sets the type of the database file (basenum?, bool?, rcf?, etc.).
	 * 
	 * @param database_file_type The database_file_type to set.
	 */
	public static void setDatabaseFileType(int database_file_type) {
		Database.database_file_type = database_file_type;
	}

	/**
	 * Gets the type of the format to which we want to convert.
	 * 
	 * @return Returns the convert_file_type.
	 */
	public static int getConvertFileType() {
		return convert_file_type;
	}

	/**
	 * Sets the type of the format to which we want to convert.
	 * 
	 * @param convert_file_type The convert_file_type to set.
	 */
	public static void setConvertFileType(int convert_file_type) {
		Database.convert_file_type = convert_file_type;
	}

	/**
	 * Deletes the given objects from the database. It means two things:
	 * 1) delete lines
	 * 2) delete object names too
	 * 
	 * @param to_delete List of numbers which objects to delete.
	 */
	@SuppressWarnings("unchecked")
	public static void deleteObjects(Vector to_delete)
	{
		int index;
		Vector toDeleteLines = new Vector(),
		toDeleteObjNames = new Vector();
		Vector database = Database.getDatabase();
		Vector objNames = Database.getObjectNames();
		boolean objectnamed = objNames.isEmpty();

		// on
		//System.err.println("db: "+database);
		//System.err.println("objNames: "+objNames);
		// off
		for (Enumeration e = to_delete.elements(); e.hasMoreElements(); )
		{
			index = ((Integer) e.nextElement()).intValue();
			toDeleteLines.add(database.get(index-1));
			if (!objectnamed) toDeleteObjNames.add(objNames.get(index));
		}

		database.removeAll(toDeleteLines);
		objNames.removeAll(toDeleteObjNames);

		// on
		//System.err.println("db: "+database);
		//System.err.println("objNames: "+objNames);
		// off
	}

	/**
	 * Used when the database is stored in vertical format.
	 * 
	 * @return Returns the numberOfObjects.
	 */
	public static int getNumberOfObjects() {
		return numberOfObjects;
	}
	
	/**
	 * Create a tidset that contains all the TIDs.
	 * This is the tidset of the empty set, which
	 * is present in every transaction by definition.
	 * 
	 * @return Tidset of an itemset with 100% support.
	 */
	public static BitSet getLargestTidset()
	{
		BitSet bs = new BitSet();
		for (int i=0; i<numberOfObjects; ++i) {
			bs.set(i+1);
		}
		return bs;
	}

	/**
	 * Used when the database is stored in vertical format.
	 * 
	 * @param numberOfObjects The numberOfObjects to set.
	 */
	public static void setNumberOfObjects(int numberOfObjects) {
		Database.numberOfObjects = numberOfObjects;
	}

	/**
	 * Gets a bitset of attribute numbers, and replaces numbers with their names.
	 * 
	 * @param bs A bitset in which we want to replace numbers with their names.
	 * @return Returns a string in which the attribute numbers are replaced by attribute names.
	 */
	public static String toNamesAttr(BitSet bs)
	{
		StringBuilder sb = new StringBuilder();
		int cnt = 0;

		sb.append("{");
		for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) 
		{ 
			if (++cnt > 1) sb.append(", ");
			sb.append(getAttr(i));
		}
		sb.append("}");

		return sb.toString();
	}

	/**
	 * It calls toNamesAttr with a BitSet. It transforms a TreeSet to BitSet, that's all
	 * it does.
	 * 
	 * @param intent A treeset in which we want to replace numbers with their names.
	 * @return Returns a string in which the attribute numbers are replaced by attribute names.
	 */
	public static String toNamesAttr(TreeSet<Integer> intent)
	{
		BitSet set = new BitSet();
		for (Iterator<Integer> it = intent.iterator(); it.hasNext(); )
		{
			set.set((it.next()).intValue());
		}
		return toNamesAttr(set);
	}

	/**
	 * Gets a vector of bitsets, and replaces numbers of attributes in them
	 * with their names.
	 * 
	 * @param sets Vector of bitsets.
	 * @return returns a string in which attribute numbers are replaced by their names.
	 */
	public static String toNamesAttr(Vector<BitSet> sets)
	{
		StringBuilder sb = new StringBuilder();
		int size = sets.size();

		sb.append("[");
		for (int i = 0; i < size; ++i) 
		{ 
			if (i > 0) sb.append(", ");
			sb.append(Database.toNamesAttr((BitSet) sets.get(i)));
		}
		sb.append("]");

		return sb.toString();
	}

	/**
	 * Gets a treeset of object numbers, and replaces numbers with their names.
	 * 
	 * @param set A treeset in which we want to replace numbers with their names.
	 * 
	 * @return Returns a string in which the object numbers are replaced by object names.
	 */
	public static String toNamesObj(TreeSet<Integer> set)
	{
		//System.err.println("!!!!!!!!");
		StringBuilder sb = new StringBuilder();
		Integer elem;
		int cnt = 0;

		sb.append("[");
		for (Iterator<Integer> it = set.iterator(); it.hasNext(); ) 
		{
			elem = it.next();
			if (++cnt > 1) sb.append(", ");
			sb.append(getObj(elem.intValue()));
		}
		sb.append("]");

		return sb.toString();
	}

	/**
	 * Gets a bitset of object numbers, and replaces numbers with their names.
	 * 
	 * @param extent A bitset of object numbers.
	 * @return Returns a string in which the object numbers are replaced by object names.
	 */
	public static String toNamesObj(BitSet extent)
	{
		StringBuilder sb = new StringBuilder();
		int cnt = 0;

		sb.append("{");
		for (int i = extent.nextSetBit(0); i >= 0; i = extent.nextSetBit(i+1)) 
		{ 
			if (++cnt > 1) sb.append(", ");
			sb.append(getObj(i));
		}
		sb.append("}");

		return sb.toString();
	}

	/**
	 * Prints information about itself. For debug purposes.
	 */
	public static void printInfo()
	{
		System.err.println("==============================================================================");
		System.err.println("DB file: "+Database.database_file_str);
		System.err.println("min_supp (int): "+Database.min_supp);
		System.err.println("min_supp (double): "+Database.min_supp_percent);
		System.err.println("# objects: "+Database.numberOfObjects);
		System.err.println("Attributes: "+Database.getAttributeNames());
		System.err.println("Objects: "+Database.getObjectNames());
		System.err.println("==============================================================================");
	}

	/**
	 * @return Returns the db_representation.
	 */
	public static int getDBRepresentation() {
		return db_representation;
	}

	/**
	 * @param db_representation The db_representation to set.
	 */

	public static void setDbRepresentation(final int db_representation) {
		Database.db_representation = db_representation;
	}

	/**
	 * @return Returns true if there was a full column in the input database.
	 */
	public static boolean hasFullColumn() {
		return Database.fullColumn;
	}

	/**
	 * @param fullColumn Does the database have a full column?
	 */
	public static void setFullColumn(boolean fullColumn) {
		Database.fullColumn = fullColumn;
	}

	/**
	 * @return Returns the longestItemset.
	 */
	public static int getLongestItemset() {
		return longestItemset;
	}

	/**
	 * @param longestItemset The longestItemset to set.
	 */
	public static void setLongestItemset(int longestItemset) {
		Database.longestItemset = longestItemset;
	}

	/**
	 * @param attributes All the attributes collected in a bitset.
	 */
	public static void registerAttributes(BitSet attributes) {
		Database.attributes = attributes;
	}

	/**
	 * @return All the attributes collected in a bitset.
	 */
	public static BitSet getAttributes() {
		return Database.attributes;
	}

	/**
	 * Delete attribute list to save some memory.
	 */
	public static void freeAttributes() {
		Database.attributes = null;
	}

	/**
	 * Putting a null at position 0 can be useful if we want to get
	 * objects using "normal" indexes, i.e. database[1] is the 
	 * first object, etc.
	 * 
	 * @return True, if the element at position 0 is null. False, otherwise.
	 */
	public static boolean isZeroElementNull() {
		return (Database.database.get(0) == null);
	}

	/**
	 * It puts a null at position 0. It can be useful if we want to get
	 * objects using "normal" indexes, i.e. database[1] is the 
	 * first object, etc. 
	 */
	public static void addZeroElementNull() 
	{
		if (Database.isZeroElementNull() == false) 
		{     
			// let's check it here too, just to be sure
			Database.database.insertElementAt(null, 0);
		}
	}

	/**
	 * If there is a null at position 0, then remove it from the dataset.
	 */
	public static void removeZeroElementNull()
	{
		if (Database.isZeroElementNull()) {
			Database.database.remove(0);
		}
	}

	/**
	 * Determines the number of "real" objects in the database.
	 * It takes into account that the element at position 0 can be null.
	 * In this case, null is not counted.
	 * 
	 * @return Number of "real" objects (without null) in the database.
	 */
	public static int getNumberOfObjectsInDatabase()
	{
		int size = Database.database.size();
		if (Database.isZeroElementNull()) size -= 1;    // the null element is not included

		return size;
	}

	/**
	 * @return True, if the DB is inverted; false, otherwise.
	 */
	public static boolean isDbInverted() {
		return Database.inverted;
	}

	/**
	 * If the DB is inverted, it must be registered with
	 * this function.
	 */
	public static void setDbInverted() {
		Database.inverted = (Database.inverted ? false : true);
	}

	/**
	 * A class to check if a basenum file starts with "0" or "1"
	 * In the first case, coron, assrulex and leco has to root the user to shiftContext. 
	 */
	public static void postCheckPosBasenum() {
		if (Database.getDatabaseFileType() == C.FT_BASENUM) {
			BitSet set;

			for (Enumeration<BitSet> e = Database.getDatabase().elements(); e.hasMoreElements();) {
				set = e.nextElement();

				if (set.nextSetBit(0) == 0) {
					System.err.println("WARNING: your database doesn't fit with the system!");
					System.err.println("         Its first position should be 1, not 0.");
					System.err.println("TIP: To make it start at 1, use ../tools/tool06_shiftContext.pl");

					Error.die(C.ERR_JUST_EXIT);
				}
			}
		}
	}


	/**
	 * Collect all the attributes in lexicographic order and return the maximal attribute.
	 * It is not to use for any algorithm, however, but it's needed by LeCo
	 * to find the set of attributes that are shared by at least one object.
	 * 
	 * @param v Vector, containing the input file's lines as bitsets.
	 * @return Maximal attribute.
	 */
	public static int init_attributes()    
	{
		attributes = new BitSet();
		for (Enumeration<BitSet> e = database.elements(); e.hasMoreElements(); )
			attributes .or(e.nextElement());

		Database.registerAttributes(attributes);     // needed for LeCo

		return (attributes.length());
	}
}
