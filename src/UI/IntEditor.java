 package UI;
 
 import Shape.Diagram;
 import Shape.Jiaohu;
 import Shape.Line;
 import Shape.Oval;
 import Shape.Phenomenon;
 import Shape.Rect;
 import Shape.Shape;
 import java.awt.Container;
 import java.awt.Dimension;
 import java.awt.Rectangle;
 import java.awt.Toolkit;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.util.LinkedList;
 import javax.swing.JButton;
 import javax.swing.JComboBox;
 import javax.swing.JDialog;
 import javax.swing.JLabel;
 
 public class IntEditor extends JDialog
   implements ActionListener
 {
   JComboBox jComboBox1;
   Jiaohu editor;
   JButton jButton1 = new JButton();
   JLabel jLabel1 = new JLabel();
   JLabel jLabel2 = new JLabel();
   Oval req;
   Diagram dd;
   LinkedList fe = new LinkedList();
 
   public static void main(String[] args) {
   }
 
   public LinkedList getfe() {
     setXuan(this.dd);
     return this.fe;
   }
 
   public void actionPerformed(ActionEvent e) {
     if (e.getActionCommand().equals("set")) {
       String s = this.jComboBox1.getSelectedItem().toString();
       if (s.endsWith("(r)")) {
         s = s.substring(0, s.indexOf("("));
         this.editor.setNumber(new Integer(s.substring(3)).intValue());
         this.editor.setState(1);
       }
       else {
         this.editor.setNumber(new Integer(s.substring(3)).intValue());
         this.editor.setState(0);
       }
       Main.win.myDisplayPane.repaint();
       dispose();
     }
   }
 
   public IntEditor(Jiaohu editor, Oval req, Diagram dd) {
     super(Main.win, true);
     this.editor = editor;
     this.req = req;
     this.dd = dd;
     try {
       jbInit();
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }
 
   public void setXuan(Diagram dd) {
     LinkedList shuju = new LinkedList();
     if (this.req == null) {
       LinkedList ll = dd.getPhenomenon();
       LinkedList lll = dd.getReference();
       for (int i = 0; i <= ll.size() - 1; i++) {
         Phenomenon tmp_p = (Phenomenon)ll.get(i);
         shuju.add("int" + tmp_p.getBiaohao());
       }
       for (int i = 0; i <= lll.size() - 1; i++) {
         Phenomenon tmp_p = (Phenomenon)lll.get(i);
         shuju.add("int" + tmp_p.getBiaohao() + "(r)");
       }
     }
     else {
       LinkedList ll = dd.getPhenomenon();
       LinkedList dom = new LinkedList();
       LinkedList lll = dd.getReference();
 
       for (int i = 0; i <= dd.components.size() - 1; i++) {
         Shape tmp_s = (Shape)dd.components.get(i);
         if (tmp_s.shape == 2) {
           Line tmp_l = (Line)tmp_s;
           if ((tmp_l.getState() == 0) || 
             (!tmp_l.from.equals(this.req))) continue;
           dom.add(tmp_l.to);
         }
 
       }
 
       for (int i = 0; i <= ll.size() - 1; i++) {
         Phenomenon tmp_p = (Phenomenon)ll.get(i);
         boolean shan = true;
         for (int j = 0; j <= dom.size() - 1; j++) {
           Rect tmp_r = (Rect)dom.get(j);
           if ((tmp_p.getFrom().equals(tmp_r)) || (tmp_p.getTo().equals(tmp_r))) {
             shan = false;
             break;
           }
         }
         if (!shan) {
           if (tmp_p.getRequirement() != null) {
             shuju.add("int" + tmp_p.getBiaohao() + "(r)");
           }
           else {
             shuju.add("int" + tmp_p.getBiaohao());
           }
         }
       }
 
       for (int i = 0; i <= lll.size() - 1; i++) {
         Phenomenon tmp_p = (Phenomenon)lll.get(i);
         if (tmp_p.getRequirement().equals(this.req)) {
           for (int j = 0; j <= ll.size() - 1; j++) {
             Phenomenon tmp1 = (Phenomenon)ll.get(j);
             if ((!tmp1.getName().equals(tmp_p.getName())) || (!tmp1.getState().equals(tmp_p.getState())) || (tmp_p.equals(tmp1)))
               continue;
             shuju.add("int" + tmp1.getBiaohao() + "(r)");
           }
         }
       }
     }
 
     String[] data = new String[shuju.size()];
     this.fe = shuju;
     for (int i = 0; i <= data.length - 1; i++) {
       data[i] = ((String)shuju.get(i));
     }
     this.jComboBox1 = new JComboBox(data);
   }
 
   private void jbInit() throws Exception {
     setSize(new Dimension(265, 147));
     setTitle("Select");
     Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
     double width = d.getWidth();
     double height = d.getHeight();
     setLocation((int)width / 2 - 200, (int)height / 2 - 200);
 
     getContentPane().setLayout(null);
     setXuan(this.dd);
 
     this.jComboBox1.setBounds(new Rectangle(103, 46, 98, 22));
     this.jButton1.setBounds(new Rectangle(127, 81, 73, 25));
     this.jButton1.setText("set");
     this.jLabel1.setText("Please select an interaction!");
     this.jLabel1.setBounds(new Rectangle(28, 17, 155, 18));
     this.jLabel2.setText("interaction");
     this.jLabel2.setBounds(new Rectangle(29, 50, 59, 16));
     getContentPane().add(this.jLabel1, null);
     getContentPane().add(this.jLabel2, null);
     getContentPane().add(this.jComboBox1, null);
     getContentPane().add(this.jButton1, null);
     this.jButton1.addActionListener(this);
   }
 }