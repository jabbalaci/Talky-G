package fr.loria.coronsys.coron.helper;

/*
 * We won't use this feature any more because DB connection
 * is too slow.
 */


/*import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;
import net.sf.hibernate.tool.hbm2ddl.SchemaUpdate;
import coron.datastructure.Row;
import coron.datastructure.Row_closure;
import coron.datastructure.apriori.Table_Apriori_F;
import coron.datastructure.aprioriclose.Row_AprioriClose_F;
import coron.datastructure.aprioriclose.Row_AprioriClose_Z;
import coron.datastructure.aprioriclose.Table_AprioriClose_F;
import coron.datastructure.aprioriclose.Table_AprioriClose_Z;
import coron.datastructure.carpathia.Row_Carpathia_F;
import coron.datastructure.carpathia.Table_Carpathia_F;
import coron.datastructure.close.Table_Close_FC;
import coron.datastructure.pascal.Row_Pascal_F;
import coron.datastructure.pascal.Table_Pascal_F;
import coron.datastructure.pascalplus.Row_PascalPlus_F;
import coron.datastructure.pascalplus.Row_PascalPlus_Z;
import coron.datastructure.pascalplus.Table_PascalPlus_F;
import coron.datastructure.pascalplus.Table_PascalPlus_Z;
import coron.datastructure.universal.Row_Universal_F;
import coron.datastructure.universal.Row_Universal_F_str;
import coron.datastructure.universal.Row_Universal_Z;
import coron.datastructure.universal.Row_Universal_Z_str;
import coron.datastructure.zart.Row_Zart_F;
import coron.datastructure.zart.Row_Zart_Z;
import coron.datastructure.zart.Table_Zart_F;
import coron.datastructure.zart.Table_Zart_Z;

*//**
 * Writes F and/or Z tables to an SQL database (with Hibernate, HSQLDB, etc.).
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 *//*
public class DatabaseHandler
{
   Session	session = null;
   SessionFactory sf = null;
   Transaction	tx = null;

   *//**
    * Constructor. 
    *//*
   public DatabaseHandler()
   {
      try 
      {
         Configuration	cfg = new Configuration()
         	.addClass(Row_Universal_F_str.class)
         	.addClass(Row_Universal_Z_str.class);
   		new SchemaExport(cfg).create(true,true);
   		//new SchemaUpdate(cfg).execute(true, true);
   		sf = cfg.buildSessionFactory();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }
   
   *//**
    * Writes Apriori's F table to the database.
    * 
    * @param table An Apriori F table.
    *//*
   public void write(Table_Apriori_F table)
   {
      Row rowF;
      Row_Universal_F rowU;
      Row_Universal_F_str rowUstr;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	      for (Enumeration e = table.getRows().elements(); e.hasMoreElements(); )
	      {
	         rowF = (Row) e.nextElement();
	         rowU = new Row_Universal_F(rowF);
	         rowUstr = new Row_Universal_F_str(rowU);
	         session.save(rowUstr);
	         //System.out.println(rowUstr);
	      }
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }
   
   *//**
    * Writes Apriori's F table to the database.
    * 
    * @param table A Pascal F table.
    *//*
   public void write(Table_Pascal_F table)
   {
      Row_Pascal_F rowF;
      Row_Universal_F rowU;
      Row_Universal_F_str rowUstr;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	      for (Enumeration e = table.getRows().elements(); e.hasMoreElements(); )
	      {
	         rowF = (Row_Pascal_F) e.nextElement();
	         rowU = new Row_Universal_F(rowF);
	         rowUstr = new Row_Universal_F_str(rowU);
	         session.save(rowUstr);
	         //System.out.println(rowUstr);
	      }
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }

   *//**
    * Writes Apriori-Close's F table to the database.
    * 
    * @param table An Apriori-Close F table.
    *//*
   public void write(Table_AprioriClose_F table)
   {
      Row_AprioriClose_F rowF;
      Row_Universal_F rowU;
      Row_Universal_F_str rowUstr;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	      for (Enumeration e = table.getRows().elements(); e.hasMoreElements(); )
	      {
	         rowF = (Row_AprioriClose_F) e.nextElement();
	         rowU = new Row_Universal_F(rowF);
	         rowUstr = new Row_Universal_F_str(rowU);
	         session.save(rowUstr);
	         //System.out.println(rowUstr);
	      }
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }

   *//**
    * Writes Apriori-Close's Z table to the database.
    * 
    * @param table An Apriori-Close Z table.
    *//*
   public void write(Table_AprioriClose_Z table)
   {
      Row_AprioriClose_Z rowZ;
      Row_Universal_Z rowU;
      Row_Universal_Z_str rowUstr;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	      for (Enumeration e = table.getRows().elements(); e.hasMoreElements(); )
	      {
	         rowZ = (Row_AprioriClose_Z) e.nextElement();
	         rowU = new Row_Universal_Z(rowZ);
	         rowUstr = new Row_Universal_Z_str(rowU);
	         session.save(rowUstr);
	         //System.out.println(rowUstr);
	      }
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }

   *//**
    * Writes Carpathia's F table to the database.
    * 
    * @param table A Carpathia F table.
    *//*
   public void write(Table_Carpathia_F table)
   {
      Row_Carpathia_F rowF;
      Row_Universal_F rowU;
      Row_Universal_F_str rowUstr;
      List found;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	      for (Enumeration e = table.getRows().elements(); e.hasMoreElements(); )
	      {
	         rowF = (Row_Carpathia_F) e.nextElement();

				found = session.find("from coron.datastructure.Row_Universal_F_str as f where f.itemset = '" + rowF.getClosure().toString()+"'");
				
				if (found.isEmpty())
				{
				   rowU = new Row_Universal_F(rowF);
		         rowUstr = new Row_Universal_F_str(rowU);
		         //System.out.println(rowUstr);
		         session.save(rowUstr);
				}
				else // if there is already such an FCI is the DB
				{
				   rowUstr = (Row_Universal_F_str) found.get(0);
				   rowUstr.addMinGen(rowF.getItemset().toString());
				   //System.out.println("duplicate: "+rowUstr);
				}
	      }
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}      
   }

   *//**
    * Prints the F table of the database.
    *//*
   public void printF()
   {
      Row rowF;
      Row_Universal_F rowU;
      Row_Universal_F_str rowUstr;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	         Iterator	i = session.iterate("from coron.datastructure.Row_Universal_F_str f");
				while (i.hasNext()) {
					rowUstr = (Row_Universal_F_str) i.next();
					System.out.println(rowUstr);
				}   
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }
   
   *//**
    * Prints the Z table of the database.
    *//*
   public void printZ()
   {
      Row rowZ;
      Row_Universal_Z rowU;
      Row_Universal_Z_str rowUstr;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	         Iterator	i = session.iterate("from coron.datastructure.Row_Universal_Z_str z");
         	while (i.hasNext()) {
					rowUstr = (Row_Universal_Z_str) i.next();
					System.out.println(">>> "+rowUstr);
				}   
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }

   *//**
    * Writes Zart's F table to the database.
    * 
    * @param table A Zart F table.
    *//*
   public void write(Table_Zart_F table)
   {
      Row_Zart_F rowF;
      Row_Universal_F rowU;
      Row_Universal_F_str rowUstr;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	      for (Enumeration e = table.getRows().elements(); e.hasMoreElements(); )
	      {
	         rowF = (Row_Zart_F) e.nextElement();
	         rowU = new Row_Universal_F(rowF);
	         rowUstr = new Row_Universal_F_str(rowU);
	         session.save(rowUstr);
	         //System.out.println(rowUstr);
	      }
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }
   
   *//**
    * Writes PascalPlus's F table to the database.
    * 
    * @param table A PascalPlus F table.
    *//*
   public void write(Table_PascalPlus_F table)
   {
      Row_PascalPlus_F rowF;
      Row_Universal_F rowU;
      Row_Universal_F_str rowUstr;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	      for (Enumeration e = table.getRows().elements(); e.hasMoreElements(); )
	      {
	         rowF = (Row_PascalPlus_F) e.nextElement();
	         rowU = new Row_Universal_F(rowF);
	         rowUstr = new Row_Universal_F_str(rowU);
	         session.save(rowUstr);
	         //System.out.println(rowUstr);
	      }
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }
   
   *//**
    * Writes Zart's Z table to the database.
    * 
    * @param table A Zart Z table.
    *//*
   public void write(Table_Zart_Z table)
   {
      Row_Zart_Z rowZ;
      Row_Universal_Z rowU;
      Row_Universal_Z_str rowUstr;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	      for (Enumeration e = table.getRows().elements(); e.hasMoreElements(); )
	      {
	         rowZ = (Row_Zart_Z) e.nextElement();
	         rowU = new Row_Universal_Z(rowZ);
	         rowUstr = new Row_Universal_Z_str(rowU);
	         session.save(rowUstr);
	         //System.out.println(rowUstr);
	      }
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }
   
   *//**
    * Writes PascalPlus's Z table to the database.
    * 
    * @param table A PascalPlus Z table.
    *//*
   public void write(Table_PascalPlus_Z table)
   {
      Row_PascalPlus_Z rowZ;
      Row_Universal_Z rowU;
      Row_Universal_Z_str rowUstr;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	      for (Enumeration e = table.getRows().elements(); e.hasMoreElements(); )
	      {
	         rowZ = (Row_PascalPlus_Z) e.nextElement();
	         rowU = new Row_Universal_Z(rowZ);
	         rowUstr = new Row_Universal_Z_str(rowU);
	         session.save(rowUstr);
	         //System.out.println(rowUstr);
	      }
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}
   }

   *//**
    * Writes Close's FC table to the database.
    * 
    * @param table A Close FC table.
    *//*
   public void write(Table_Close_FC table)
   {
      Row_closure rowF;
      Row_Universal_F rowU;
      Row_Universal_F_str rowUstr;
      List found;
      
      try 
      {
         session = sf.openSession();
         tx = session.beginTransaction();
         
	      for (Enumeration e = table.getRows().elements(); e.hasMoreElements(); )
	      {
	         rowF = (Row_closure) e.nextElement();

				found = session.find("from coron.datastructure.Row_Universal_F_str as f where f.itemset = '" + rowF.getItemset().toString()+"'");
				
				if (found.isEmpty())		// if this closure is not yet in the database
				{
				   rowU = new Row_Universal_F(rowF);
		         rowUstr = new Row_Universal_F_str(rowU);
		         //System.out.println("!! >> "+rowUstr);
		         session.save(rowUstr);
				}
	      }
	      
	      tx.commit();
      } catch(HibernateException e) {
			System.err.println(e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch(HibernateException f) {}
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch(HibernateException e) {}
			}
		}      
   }
}
*/
