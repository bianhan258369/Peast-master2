 package UI;
 
 import java.awt.event.WindowAdapter;
 import java.awt.event.WindowEvent;
 
 class MyAdapter extends WindowAdapter
 {
   Main father;
 
   public MyAdapter(Main father)
   {
     this.father = father;
   }
 
   public void windowClosing(WindowEvent e)
   {
     System.exit(0);
   }
 }

