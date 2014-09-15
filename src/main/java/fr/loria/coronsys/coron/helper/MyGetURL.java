package fr.loria.coronsys.coron.helper;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Get a file from the Internet.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class MyGetURL
{
   /**
    * The URL of the file we want to fetch.
    */
   private URL url;

   /**
    * Get the content of the given URL as a string.
    * 
    * @param url The URL we want to get.
    * @return The content of the URL as a string.
    */
   public String getURL(String url)
   {
      this.url = this.parseURLString(url);
      URLConnection urlc = this.getURLConnection(this.url);
      return getContent(urlc);
   }

   /**
    * Gets the content of the given URL as an array of strings.
    * 
    * @param address The URL of the file we want to read.
    * @return An array of strings containing the lines of the given file (URL).
    */
   public String[] getURLasLines(String address)
   {
      String all = this.getURL(address);
      all = all.replaceAll("\r","\n");
      return all.split("\n");
   }
   
   /**
    * It can check if a given URL is alive or not.
    * 
    * @param address URL address that we want to check if it's available or not.
    * @return True, if it's available. False, otherwise.
    */
   public boolean checkHttpUrl(String address) 
   {
      try {
         URL url = parseURLString(address);
         HttpURLConnection huc = (HttpURLConnection)url.openConnection();
         int response = huc.getResponseCode();
         if (response == 200)  return true;
         else                  return false;
      } catch (java.io.IOException ioe) {}
      return false;
   }

   /*
    * =======================================================================
    *                and here come the private functions
    * =======================================================================
    */
   
   /**
    * Get the content of a URL as a string.
    * 
    * @param urlc URLConnection object the URL we want to read.
    * @return String content of the given URL. 
    */
   private String getContent(URLConnection urlc) 
   {
      try 
      {
         InputStream is = urlc.getInputStream();
         DataInputStream ds = new DataInputStream(is);
         int length;
         length = urlc.getContentLength();
         if (length != -1) 
         {
            byte b[] = new byte[length];
            ds.readFully(b);
            String s = new String(b);
            return s;
         } 
         else 
         {
            StringBuilder s = new StringBuilder();
            int i = is.read();
            while (i != -1) 
            {
               s.append( (char)i );
               i = is.read();
            }
            return new String(s);
         }
      }
      catch (Exception e) 
      {
         System.err.println("Coron: I/O error while trying to read the update information!");
         Error.die(C.ERR_JUST_EXIT);
      }
      return null;          // it cannot arrive here
   }


   /**
    * Checks if the URL is well-formed.
    * 
    * @param address The URL we want to check.
    * @return The URL itself if it is correct. If it's not correct, the program terminates.
    */
   private URL parseURLString(String address) 
   {
      URL url = null;

      try {
         url = new URL(address);
      } 
      catch (MalformedURLException e) 
      {
         System.err.println("Coron: the URL \""+address+"\" is malformed!");
         Error.die(C.ERR_JUST_EXIT);
      }
      return url;
   }
      
   /**
    * Gets a URLConnection object towards the given URL.
    * 
    * @param url URL object representation of the file.
    * @return A URLConnection object towards the given URL.
    */
   private URLConnection getURLConnection(URL url)
   {
      URLConnection myUC = null;

      try {
         myUC = url.openConnection();
         myUC.connect();
      } 
      catch (Exception e) 
      {
         System.err.println("Coron: an error occured while trying to check for newer version!");
         Error.die(C.ERR_JUST_EXIT);
      }
      return myUC;
   }
}
