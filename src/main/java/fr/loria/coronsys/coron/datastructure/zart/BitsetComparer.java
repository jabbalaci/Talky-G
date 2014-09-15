package fr.loria.coronsys.coron.datastructure.zart;

import java.util.BitSet;
import java.util.Comparator;

/**
 * Compare two BitSets lexicographically.
 *
 * @author Laszlo Szathmary
 */
public class BitsetComparer 
implements Comparator<Object> 
{
   public int compare(Object o1, Object o2) {
      return (((BitSet) o1).toString().compareTo(((BitSet) o2).toString()));
   }
}
