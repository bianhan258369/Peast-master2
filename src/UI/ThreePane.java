 package UI;
 
 import javax.swing.JScrollPane;
 import javax.swing.JTable;
 import javax.swing.JViewport;
 
 class ThreePane extends JScrollPane
 {
   String name;
   String[] column;
   Object[][] data;
   JTable table;
 
   public ThreePane(String name, String[] column, Object[][] data)
   {
     this.name = name;
     this.column = column;
     this.data = data;
     this.table = new JTable(data, column);
     add(this.table);
   }
 
   public void setData(Object[][] data) {
     this.data = data;
     this.table = new JTable(data, this.column);
     getViewport().add(this.table);
   }
}

