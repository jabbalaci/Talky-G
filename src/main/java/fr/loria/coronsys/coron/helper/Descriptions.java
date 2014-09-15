package fr.loria.coronsys.coron.helper;

/**
 * Contains the descriptions of all algorithms.
 * 
 * @author Thomas Bouton (<a
 *         href="thomas.bouton@gmail.com">thomas.bouton@gmail.com</a>)
 * 
 */
public class Descriptions
{
   /**
    * Empty private constructor. The class cannot be instantiated.
    */
   private Descriptions() { }
   
   /**
    * Returns the description of the given algorithm
    * 
    * @param alg
    *           an algorithm constant
    * @return the algorithm's description
    */
   public static String getDescription(int alg)
   {
      switch (alg)
      {
         case C.ALG_APRIORI:
            return "Extracts FIs";
         case C.ALG_APRIORI_CLOSE:
            return "Extracts FIs, indicates which are FCIs";
         case C.ALG_APRIORI_RARE:
            return "Extracts minimal rare itemsets (MRIs)";
         case C.ALG_CARPATHIA:
            return "Extracts minimal generators and their closure (FCIs)";
         case C.ALG_CHARM:
         case C.ALG_CHARM_4:
            return "Extracts FCIs";
         case C.ALG_CLOSE:
            return "Extracts FCIs";
         case C.ALG_ECLAT:
         case C.ALG_ECLAT_1:
            return "Extracts FIs";
         case C.ALG_ECLAT_Z:
            return "Extracts FCIs, minimal generators, and associates minimal generators with their closure";
         case C.ALG_PASCAL:
            return "Extracts FIs, indicates which are minimal generators";
         case C.ALG_PASCAL_PLUS:
            return "Extracts FIs, indicates which are minimal generators and marks itemsets that are FCIs";
         case C.ALG_ZART_2:
            return "Extracts FCIs, minimal generators, and associates minimal generators with their closure";
         case C.ALG_UNKNOWN:
            return "Unknown algorithm";
         default:
            return "No description available";
      }
   }
}
