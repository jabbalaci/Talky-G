package fr.loria.coronsys.coron.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Class to handle file operations.
 * 
 * @author Laszlo Szathmary (<a href="Szathmary.L@gmail.com">Szathmary.L@gmail.com</a>)
 */
public class FileHandler
{
   /**
    * Creates a directory and a file in this. Handles all (hopefully :)) possible
    * errors.
    * 
    * @param dir The directory in which we want to create the file.
    * @param file The file which we want to create.
    * @return Returns the created file as a File object, or null if it was not successful.
    */
   public static BufferedWriter prepareDirFile(File dir, File file)
   {
      BufferedWriter result = null;
      
      if (dir.isFile()) {
         Error.warning(C.F_FILE_ALREADY_EXISTS, dir, file);
         return null;
      }
      // else, if such a file not yet exists
      if (dir.exists())
      {
         if (dir.canWrite() == false) {
            Error.warning(C.F_CANNOT_WRITE_DIR, dir, file);
            return null;
         }
      }
      else // if the dir. not yet exists
      {
         if (dir.mkdir() == false) {
            Error.warning(C.F_CANNOT_CREATE_DIR, dir, file);
            return null;
         }
      }
      // OK, the output directory is created and writeable
      
      if (file.exists())
      {
         if (file.delete() == false) {
            Error.warning(C.F_CANNOT_DELETE_FILE, dir, file);
            return null;
         }
      }
      try
      {
         result = new BufferedWriter(new PrintWriter(file));
      }
      catch (FileNotFoundException e) { Error.warning(C.F_CANNOT_CREATE_FILE, dir, file); }
      
      // OK, we can write to the output file from now on
      return result;
   }
}
