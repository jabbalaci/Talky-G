package fr.loria.coronsys.coron.helper;

/**
 * Checks if the software has a new version available.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class CheckUpdate
{
   /**
    * Where is the information about the most current version stored? 
    */
   protected String versionUrl;
   
   /**
    * Name of the software that uses this class. 
    */
   protected String self;
   
   
   /**
    * Current version of the software. 
    */
   protected String currVer;
   
   /**
    * Constructor.
    */
   public CheckUpdate() 
   {
      this.versionUrl   = "http://coron.loria.fr/version/coron.txt";
      this.self         = "Coron";
      this.currVer      = C.VERSION;
   }

   /**
    * Start checking if there is update available.
    */
   public void start()
   {
      MyGetURL getURL  = new MyGetURL();
      
      System.out.println(this.self+": connecting to server...");
      String newVer    = getURL.getURL(this.versionUrl);
      String newVerBak = this.createBackup(newVer);
      newVer           = this.clean(newVer);
      check(newVer, C.VERSION_NEW);
      
      String currVerBak = this.createBackup(this.currVer);
      this.currVer      = this.clean(this.currVer);
      check(this.currVer, C.VERSION_CURR);
      
      int state = this.currVer.compareTo(newVer); 
      if (state < 0)
      {
         // current is old, there is a newer version
         System.out.println(this.self+": there is a new version available ("+newVerBak+")");
         System.out.println(this.space(this.self, 2)+"your current version: "+currVerBak);
      }
      else if (state == 0)
      {
         System.out.println(this.self+": your current version ("+currVerBak+") is up-to-date");
      }
      else // if (state > 0)
      {
         System.out.println(this.self+": your version ("+currVerBak+") is newer than the one on the server ("+newVerBak+")");
         System.out.println(this.space(this.self, 2)+"this is quite strange, the author must have forgotten to update the server :(");
      }
   }

   /**
    * First line:  "Coron: ...."
    * Second line: "       continue here"
    * 
    * That is: in the first line we print the name of the current software, which can
    * be Coron, AssRuleX, etc., and one line below we continue, BUT the 2nd line
    * has to be well-aligned. We compute here how many spaces to add to the 2nd line.
    * 
    * @param str Name of the software.
    * @param extraSpace Number of extrasapces.
    * @return A string containing the necessary extra spaces.
    */
   private String space(String str, int extraSpace)
   {
      StringBuilder sb = new StringBuilder();
      int cnt = str.length() + extraSpace;
      
      for (int i = 0; i < cnt; ++i)
         sb.append(' ');
      
      return sb.toString();
   }

   /**
    * Creates a backup copy of the version string.
    * 
    * @param version Version as string.
    * @return Backup copy of the version string.
    */
   private String createBackup(String version)
   {
      String str = new String(version);
      return str.replaceAll("\n", "");
   }

   /**
    * Checks if the fetched version info is good or not.
    * 
    * @param version Version info.
    * @param whichVersion Shows that the string belongs to which version (new/current).
    */
   private void check(String version, final int whichVersion)
   {
      boolean ok = true;
      
      if (version.length() != 4) ok = false;
      if (ok)
      {
         for (int i = 0; i < 4; ++i)
         {
            if (Character.isDigit(version.charAt(i)) == false)
            {
               ok = false;
               break;
            } 
         }
      }
      
      if (ok == false)
      {
         String errorMsg = "";
         switch (whichVersion)
         {
            case C.VERSION_NEW:
               errorMsg = "the fetched version information is not correct!";
               break;
            case C.VERSION_CURR:
               errorMsg = "the internal current version information is not correct!";
               break;
         }
         System.err.println("Coron: "+errorMsg);
         Error.die(C.ERR_JUST_EXIT);
      }
   }

   /**
    * Cleans the version string.
    * 
    * @param versionStr String holding version information.
    * @return Cleaned version info.
    */
   private String clean(String versionStr) {
      return (versionStr.replaceAll("\\.", "").replaceAll("\n",""));
   }
}
