package fr.loria.coronsys.coron.helper;

import javax.management.*;
import java.lang.management.*;
import java.util.*;

/**
 * This memory warning system will call the listener when we exceed the
 * percentage of available memory specified. There should only be one instance
 * of this object created, since the usage threshold can only be set to one
 * number.
 * 
 * From: 2004-07-20 The Java Specialists' Newsletter [Issue 092] -
 * OutOfMemoryError Warning System Author: Dr. Heinz M. Kabutz
 */
public class MemoryWarningSystem
{
   private final Collection<Listener> listeners = new ArrayList<Listener>();

   /**
    * @author szathmar
    *
    */
   public interface Listener
   {
      /**
       * @param usedMemory
       * @param maxMemory
       */
      public void memoryUsageLow(long usedMemory, long maxMemory);
   }

   /**
    * 
    */
   public MemoryWarningSystem()
   {
      MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
      NotificationEmitter emitter = (NotificationEmitter) mbean;
      emitter.addNotificationListener(new NotificationListener() {
         public void handleNotification(Notification n, Object hb)
         {
            if (n.getType().equals(
                  MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED))
            {
               long maxMemory = tenuredGenPool.getUsage().getMax();
               long usedMemory = tenuredGenPool.getUsage().getUsed();
               for (Iterator<Listener> listener = listeners.iterator(); listener
                     .hasNext();)
               {
                  listener.next().memoryUsageLow(usedMemory,
                        maxMemory);
               }
            }
         }
      }, null, null);
   }

   /**
    * @param listener
    * @return True, if the collection changed as a result of the call.
    */
   @SuppressWarnings("unchecked")
   public boolean addListener(Listener listener)
   {
      return listeners.add(listener);
   }

   /**
    * @param listener
    * @return True, if the collection changed as a result of the call.
    */
   public boolean removeListener(Listener listener)
   {
      return listeners.remove(listener);
   }

   private static final MemoryPoolMXBean tenuredGenPool = findTenuredGenPool();

   /**
    * @param percentage
    */
   public static void setPercentageUsageThreshold(double percentage)
   {
      if (percentage <= 0.0 || percentage > 1.0) { throw new IllegalArgumentException(
            "Percentage not in range"); }
      long maxMemory = tenuredGenPool.getUsage().getMax();
      long warningThreshold = (long) (maxMemory * percentage);
      tenuredGenPool.setUsageThreshold(warningThreshold);
   }

   /**
    * Tenured Space Pool can be determined by it being of type HEAP and by it
    * being possible to set the usage threshold.
    * 
    * @return pool 
    */
   private static MemoryPoolMXBean findTenuredGenPool()
   {
      for (Iterator<MemoryPoolMXBean> it = ManagementFactory.getMemoryPoolMXBeans().iterator(); it.hasNext();)
      {
         MemoryPoolMXBean pool = it.next();
         if (pool.getType() == MemoryType.HEAP
               && pool.isUsageThresholdSupported()) { return pool; }
      }
      throw new AssertionError("Could not find tenured space");
   }
}
