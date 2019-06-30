 package Shape;
 
 import java.io.Serializable;
 
 public class Prj
   implements Serializable
 {
   String title = null;
   int first = 1;
   int firstq = 1;
 
   public Prj(String title, int first, int firstq) { this.title = title;
     this.first = first;
     this.firstq = firstq;
   }
 }