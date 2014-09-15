package fr.loria.coronsys.coron.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for using keyboard input.
 * 
 * Example:
 * ========
 * 
 *  Console con = new Console();
 *  //saisie :
 *  con.print("Nom : ?");
 *  String name = con.readLine();
 *  con.println("Hello " + name);
 *   
 */

public class Console 
{
   /**
    * For reading the input.
    */
   private BufferedReader buff;

   /**
    * Is it OK that we read? Can it be parsed?
    */
   private boolean ok;

   /**
    * Constructor. 
    */
   public Console() {
      this.buff = new BufferedReader(new InputStreamReader(System.in));
   }

   /**
    * @return Une ligne de texte saisie au clavier.
    */
   public String readLine()
   {
      String saisie = null;
      try {
          saisie = buff.readLine();
      }
      catch(IOException e){
         System.out.println(e.getMessage());
         System.exit(0);
      }
      return saisie;
   }

   /* ******************************************************************** */
   
   /**
    * @return Retourne un entier int saisi au clavier.
    */
   public int readInt()
   {
      int res = 0 ;
      
      do {
         try {
            res = Integer.parseInt(this.readLine()); 
            ok = true;
         }
         catch(NumberFormatException e) {
            ok = false;
         }
      } while(!ok);
      
      return res;
  }

   /**
    * @return Retourne un entier short saisi au clavier.
    */
   public short readShort()
   {
      short result = 0;
      
      do{
         try {
            result = Short.parseShort(this.readLine());
            ok = true;
         }
         catch(NumberFormatException e) { 
            ok = false;
         }
      } while(!ok);
      
      return result;
   }

   /**
    * @return Retourne un entier byte saisi au clavier.
    */
   public byte readByte()
   {
      byte result = 0 ;
      
      do{
         try {
            result = Byte.parseByte(this.readLine());
            ok = true;
         }
         catch(NumberFormatException e) {
            ok = false;
         }
      } while(!ok);
      
      return result;
   }

   /**
    * @return Retourne un entier long saisi au clavier.
    */
   public long readLong()
   {
      long result = 0;
      
      do {
         try {
            result = Long.parseLong(this.readLine());
            ok = true;
         }
         catch(NumberFormatException e) { 
            ok = false;
         }
      } while(!ok);
      
      return result;
   }

   /**
    * @return Retourne un reel double saisi au clavier.
    */
   public double readDouble()
   {
      double result = 0 ;
      
      do {
         try {
            result = Double.parseDouble(this.readLine());
            ok = true;
         }
         catch(NumberFormatException e) {
            ok = false;
         }
      } while(!ok);
      
      return result;
   }

   /**
    * @return Retourne un reel float saisi au clavier.
    */
   public float readFloat()
   {
      float result = 0 ;
      
      do {
         try {
            result = Float.parseFloat(this.readLine());
            ok = true;
         }
         catch(NumberFormatException e) {
            ok = false;
         }
      } while(!ok);
      
      return result;
   }

   /* ******************************************************************** */
   
   /**
    * Print a new line character.
    */
   public void println() {
      System.out.println();
   }
   
   /**
    * Ecriture de textes avec passage a la ligne.
    * 
    * @param s String to print.
    */
   public void println(String s){
      System.out.println(s);
   }

   /**
    * Ecriture de textes sans passage a la ligne.
    * 
    * @param s String to print.
    */
   public void print(String s){
      System.out.print(s);
   }

   /**
    * Ecriture de la traduction d'un objet
    * sous forme de texte avec passage a la ligne.
    * 
    * @param o Object to print.
    */
   public void println(Object o){
      System.out.println(o);
   }

   /**
    * Ecriture de la traduction d'un objet
    * sous forme de texte sans passage a la ligne.
    * 
    * @param o Object to print.
    */
   public void print(Object o){
      System.out.print(o.toString());
   }

   /**
    * Affiche un entier int avec passage a la ligne.
    * 
    * @param i int to print.
    */
   public void println(int i){
      System.out.println(String.valueOf(i));
   }

   /**
    * Affiche un entier int sans passage a la ligne.
    * 
    * @param i int to print.
    */
   public void print(int i){
      System.out.print(String.valueOf(i));
   }

   /**
    * Affiche un entier long avec passage a la ligne.
    * 
    * @param l long to print.
    */
   public void println(long l){
      System.out.println(String.valueOf(l));
   }

   /**
    * Affiche un entier long sans passage a la ligne.
    * 
    * @param l long to print.
    */
   public void print(long l){
      System.out.print(String.valueOf(l));
   }

   /**
    * Affiche un entier short avec passage a la ligne.
    * 
    * @param s short to print.
    */
   public void println(short s){
      System.out.println(String.valueOf(s));
   }

   /**
    * Affiche un entier short sans passage a la ligne.
    * 
    * @param s short to print.
    */
   public void print(short s){
      System.out.print(String.valueOf(s));
   }

   /**
    * Affiche un entier byte avec passage a la ligne.
    * 
    * @param b byte to print.
    */
   public void println(byte b){
      System.out.println(String.valueOf(b));
   }

   /**
    * Affiche un entier byte sans passage a la ligne.
    * 
    * @param b byte to print.
    */
   public void print(byte b){
      System.out.print(String.valueOf(b));
   }

   /**
    * Affiche un reel double avec passage a la ligne.
    * 
    * @param d double to print.
    */
   public void println(double d){
      System.out.println(String.valueOf(d));
   }

   /**
    * Affiche un reel double sans passage a la ligne.
    * 
    * @param d double to print.
    */
   public void print(double d){
      System.out.print(String.valueOf(d));
   }

   /**
    * Affiche un reel float avec passage a la ligne.
    * 
    * @param f float to print.
    */
   public void println(float f){
      System.out.println(String.valueOf(f));
   }

   /**
    * Affiche un reel float sans passage a la ligne.
    * 
    * @param f float to print.
    */
   public void print(float f){
      System.out.print(String.valueOf(f));
   }

   /**
    * Affiche un boolean avec passage a la ligne.
    * 
    * @param b boolean to print.
    */
   public void println(boolean b){
      System.out.println(String.valueOf(b));
   }

   /**
    * Affiche un boolean sans passage a la ligne.
    * 
    * @param b boolean to print.
    */
   public void print(boolean b){
      System.out.print(String.valueOf(b));
   }
   
   /**
    * Print a colored text to the terminal. Works with Linux only.
    * I use this colored text for debugging only.
    * 
    * @param text Custom text.
    * @return Text colored in green (blue?).
    */
   public static String green(String text) {
      return "\033[1;36m" + text + "\033[00m";
   }
}