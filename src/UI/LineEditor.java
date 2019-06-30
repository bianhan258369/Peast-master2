 package UI;
 
 import Shape.Diagram;
 import Shape.Line;
 import Shape.Oval;
 import Shape.Persist;
 import Shape.Phenomenon;
 import Shape.Rect;
 import Shape.Shape;
 import java.awt.Container;
 import java.awt.Dimension;
 import java.awt.Rectangle;
 import java.awt.Toolkit;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.io.File;
 import java.io.PrintStream;
 import java.util.LinkedList;
 import javax.swing.BorderFactory;
 import javax.swing.DefaultListModel;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JComboBox;
 import javax.swing.JDialog;
 import javax.swing.JLabel;
 import javax.swing.JList;
 import javax.swing.JOptionPane;
 import javax.swing.JPanel;
 import javax.swing.JTextField;
 
 public class LineEditor extends JDialog
   implements ActionListener
 {
   Line editor;
   Diagram dd;
   DefaultListModel listModel1 = new DefaultListModel();
   DefaultListModel listModel2 = new DefaultListModel();
   JList jList1 = new JList(this.listModel1);
   JList jList2 = new JList(this.listModel2);
   JLabel jLabel1 = new JLabel();
   JLabel jLabel2 = new JLabel();
   JTextField jTextField1 = new JTextField();
   JLabel jLabel3 = new JLabel();
   JLabel jLabel4 = new JLabel();
   JTextField jTextField2 = new JTextField();
   JLabel jLabel5 = new JLabel();
   JLabel jLabel6 = new JLabel();
   String[] neirong = { "event", "state", "value" };
 
   JComboBox jComboBox1 = new JComboBox(this.neirong);
   JComboBox jComboBox2 = new JComboBox(this.neirong);
   JButton jButton1 = new JButton();
   JButton jButton2 = new JButton();
   JButton jButton3 = new JButton();
   JButton jButton4 = new JButton();
   JButton jButton5 = new JButton();
   Rect machine;
   JCheckBox jCheckBox1 = new JCheckBox();
   JCheckBox jCheckBox2 = new JCheckBox();
   JLabel jLabel7 = new JLabel();
   JLabel jLabel8 = new JLabel();
   JComboBox jComboBox3 = new JComboBox();
   JComboBox jComboBox4 = new JComboBox();
   LinkedList l1 = new LinkedList();
   LinkedList l2 = new LinkedList();
   JPanel jPanel1 = new JPanel();
   JPanel jPanel2 = new JPanel();
 
   public static void main(String[] args) {
     Diagram dd = new Diagram("test");
     Rect machine = new Rect(0, 0);
     Rect domain = new Rect(0, 0);
     Line line = new Line(machine, domain, 0);
 
     dd.add(line);
     dd.add(machine);
     dd.add(domain);
 
     Diagram ddd = Persist.load(new File("D:/wq"));
  }

   public void actionPerformed(ActionEvent e)
   {
     if (e.getActionCommand().equals("combo1")) {
       int i = this.jComboBox3.getSelectedIndex();
       if (i == 0) {
         this.jTextField1.setText("");
         this.jComboBox1.setSelectedIndex(0);
         this.jTextField1.setEnabled(true);
         this.jComboBox1.setEnabled(true);
       }
       else {
         Phenomenon phe = (Phenomenon)this.l1.get(i - 1);
         this.jTextField1.setText(phe.getName());
         if (phe.getState().equals("event")) {
           this.jComboBox1.setSelectedIndex(0);
         }
         if (phe.getState().equals("state")) {
           this.jComboBox1.setSelectedIndex(1);
         }
         if (phe.getState().equals("value")) {
           this.jComboBox1.setSelectedIndex(2);
         }
         this.jTextField1.setEnabled(false);
         this.jComboBox1.setEnabled(false);
       }
     }
     if (e.getActionCommand().equals("combo2")) {
       int i = this.jComboBox4.getSelectedIndex();
       if (i == 0) {
         this.jTextField2.setText("");
         this.jComboBox2.setSelectedIndex(0);
         this.jTextField2.setEnabled(true);
         this.jComboBox2.setEnabled(true);
       }
       else {
         Phenomenon phe = (Phenomenon)this.l2.get(i - 1);
         this.jTextField2.setText(phe.getName());
         if (phe.getState().equals("event")) {
           this.jComboBox2.setSelectedIndex(0);
         }
         if (phe.getState().equals("state")) {
           this.jComboBox2.setSelectedIndex(1);
         }
         if (phe.getState().equals("value")) {
           this.jComboBox2.setSelectedIndex(2);
         }
         this.jTextField2.setEnabled(false);
         this.jComboBox2.setEnabled(false);
       }
 
     }
 
     if (e.getActionCommand().equals("delete")) {
       this.dd.delete(this.editor);
       dispose();
     }
     if (e.getActionCommand().equals("Close")) {
       dispose();
     }
     if (e.getActionCommand().equals("add1")) {
       if (this.jTextField1.getText().equals("")) {
         Object[] bs = { "OK" };
 
         JOptionPane.showOptionDialog(this, "You have not inputed name of the phenomenon!", "Tip", 0, 1, null, bs, null);
 
         return;
       }
 
       Phenomenon tmp_pm = new Phenomenon(this.jTextField1.getText(), this.jComboBox1.getSelectedItem().toString(), this.machine, (Rect)this.editor.to);
 
       if (this.editor.getState() != 0) {
         if (this.jCheckBox1.isSelected()) {
           tmp_pm.setConstraining(true);
           this.jCheckBox1.setSelected(false);
           this.editor.setState(2);
         }
         tmp_pm.setRequirement((Oval)this.editor.from);
       }
       this.editor.phenomenons.add(tmp_pm);
 
       this.listModel1.addElement(this.jTextField1.getText() + " " + this.jComboBox1.getSelectedItem().toString());
     }
 
     if (e.getActionCommand().equals("add2")) {
       if (this.jTextField2.getText().equals("")) {
         Object[] bs = { "OK" };
 
         JOptionPane.showOptionDialog(this, "You have not inputed name of the phenomenon!", "Tip", 0, 1, null, bs, null);
 
         return;
       }
 
       Phenomenon tmp_pm = new Phenomenon(this.jTextField2.getText(), this.jComboBox2.getSelectedItem().toString(), (Rect)this.editor.to, this.machine);
 
       if (this.editor.getState() != 0) {
         if (this.jCheckBox2.isSelected()) {
           tmp_pm.setConstraining(true);
           this.jCheckBox2.setSelected(false);
           this.editor.setState(2);
         }
         tmp_pm.setRequirement((Oval)this.editor.from);
       }
       this.editor.phenomenons.add(tmp_pm);
       this.listModel2.addElement(this.jTextField2.getText() + " " + this.jComboBox2.getSelectedItem().toString());
     }
 
     if (e.getActionCommand().equals("delete1")) {
       int i = this.jList1.getSelectedIndex();
       String s = this.listModel1.getElementAt(i).toString();
       s = s.substring(0, s.indexOf(" "));
       for (int j = 0; j <= this.editor.phenomenons.size() - 1; j++) {
         Phenomenon tmp_p = (Phenomenon)this.editor.phenomenons.get(j);
         if (tmp_p.getName().equals(s)) {
           this.editor.phenomenons.remove(j);
           break;
         }
       }
       this.listModel1.remove(i);
     }
     if (e.getActionCommand().equals("delete2")) {
       int i = this.jList2.getSelectedIndex();
       String s = this.listModel2.getElementAt(i).toString();
       s = s.substring(0, s.indexOf(" "));
       for (int j = 0; j <= this.editor.phenomenons.size() - 1; j++) {
         Phenomenon tmp_p = (Phenomenon)this.editor.phenomenons.get(j);
         if (tmp_p.getName().equals(s)) {
           this.editor.phenomenons.remove(j);
           break;
         }
       }
       this.listModel2.remove(i);
     }
   }
 
   public LinkedList xuqiuyinyong1(Line l)
   {
     LinkedList re = new LinkedList();
     for (int i = 0; i <= this.dd.components.size() - 1; i++) {
       Shape tmp_s = (Shape)this.dd.components.get(i);
       if (tmp_s.shape == 2) {
         Line tmp_l = (Line)tmp_s;
         if (tmp_l.getState() != 0) {
           continue;
         }
         if (tmp_l.to.equals(l.to)) {
           LinkedList ll = tmp_l.phenomenons;
           for (int j = 0; j <= ll.size() - 1; j++) {
             Phenomenon tmp_p = (Phenomenon)ll.get(j);
             if (tmp_p.getTo().equals(l.to)) {
               re.add(tmp_p);
             }
           }
         }
       }
     }
     return re;
   }
 
   public LinkedList xuqiuyinyong2(Line l) {
     LinkedList re = new LinkedList();
     for (int i = 0; i <= this.dd.components.size() - 1; i++) {
       Shape tmp_s = (Shape)this.dd.components.get(i);
       if (tmp_s.shape == 2) {
         Line tmp_l = (Line)tmp_s;
         if (tmp_l.to.equals(l.to)) {
           LinkedList ll = tmp_l.phenomenons;
           for (int j = 0; j <= ll.size() - 1; j++) {
             Phenomenon tmp_p = (Phenomenon)ll.get(j);
             if (tmp_p.getFrom().equals(l.to)) {
               re.add(tmp_p);
             }
           }
         }
       }
     }
     return re;
   }
 
   public void lineInit() {
     setTitle(this.editor.name);
     if (this.editor.from.shape == 0) {
       Rect machine = (Rect)this.editor.from;
       Rect domain = (Rect)this.editor.to;
       this.jLabel1.setText(machine.getText() + ":");
       this.jLabel2.setText(domain.getText() + ":");
       for (int i = 0; i <= this.editor.phenomenons.size() - 1; i++) {
         Phenomenon phe = (Phenomenon)this.editor.phenomenons.get(i);
         if (phe.getFrom().getState() == 2) {
           this.listModel1.addElement(phe.getName() + "  " + phe.getState());
         }
         else
           this.listModel2.addElement(phe.getName() + "  " + phe.getState());
       }
     }
     else
     {
       Rect domain = (Rect)this.editor.to;
       this.jLabel1.setText(this.machine.getText() + ":");
       this.jLabel2.setText(domain.getText() + ":");
       this.jCheckBox1.setEnabled(true);
       this.jCheckBox2.setEnabled(true);
       for (int i = 0; i <= this.editor.phenomenons.size() - 1; i++) {
         Phenomenon phe = (Phenomenon)this.editor.phenomenons.get(i);
         if (phe.getFrom().getState() == 2) {
           this.listModel1.addElement(phe.getName() + "  " + phe.getState());
         }
         else {
           this.listModel2.addElement(phe.getName() + "  " + phe.getState());
         }
       }
     }
 
     if (this.editor.getState() == 0) {
       this.jComboBox3.setEnabled(false);
       this.jComboBox4.setEnabled(false);
     }
     else {
       this.l1 = xuqiuyinyong1(this.editor);
       this.l2 = xuqiuyinyong2(this.editor);
       String[] s1 = new String[this.l1.size() + 1];
       String[] s2 = new String[this.l2.size() + 1];
       s1[0] = "";
       s2[0] = "";
       for (int i = 0; i <= s1.length - 2; i++) {
         s1[(i + 1)] = ((Phenomenon)this.l1.get(i)).getName();
       }
       for (int i = 0; i <= s2.length - 2; i++) {
         s2[(i + 1)] = ((Phenomenon)this.l2.get(i)).getName();
       }
       this.jComboBox3 = new JComboBox(s1);
       this.jComboBox4 = new JComboBox(s2);
       this.jComboBox3.setActionCommand("combo1");
       this.jComboBox3.setBounds(new Rectangle(83, 142, 86, 22));
       this.jComboBox4.setActionCommand("combo2");
       this.jComboBox4.setBounds(new Rectangle(296, 142, 92, 22));
     }
 
     getContentPane().add(this.jComboBox3, null);
     getContentPane().add(this.jComboBox4, null);
     getContentPane().add(this.jPanel1, null);
     getContentPane().add(this.jPanel2, null);
     this.jComboBox3.addActionListener(this);
     this.jComboBox4.addActionListener(this);
   }
 
   public LineEditor(Line editor, Rect machine, Diagram dd) {
     super(Main.win, true);
     this.editor = editor;
     this.dd = dd;
     this.machine = machine;
     try {
       jbInit();
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }
 
   private void jbInit() throws Exception {
     setSize(new Dimension(400, 457));
     Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
     double width = d.getWidth();
     double height = d.getHeight();
     setLocation((int)width / 2 - 200, (int)height / 2 - 200);
     getContentPane().setLayout(null);
     this.jList1.setBounds(new Rectangle(14, 243, 153, 115));
     this.jList2.setBounds(new Rectangle(231, 244, 153, 115));
     this.jLabel1.setText("jLabel1");
     this.jLabel1.setBounds(new Rectangle(17, 30, 149, 16));
     this.jLabel2.setText("jLabel2");
     this.jLabel2.setBounds(new Rectangle(234, 34, 152, 16));
     this.jTextField1.setText("");
     this.jTextField1.setBounds(new Rectangle(81, 61, 86, 22));
     this.jLabel3.setText("name");
     this.jLabel3.setBounds(new Rectangle(17, 66, 34, 16));
     this.jLabel4.setText("name");
     this.jLabel4.setBounds(new Rectangle(234, 65, 34, 16));
     this.jTextField2.setText("");
     this.jTextField2.setBounds(new Rectangle(294, 62, 93, 22));
     this.jLabel5.setText("kind");
     this.jLabel5.setBounds(new Rectangle(18, 102, 34, 16));
     this.jLabel6.setText("kind");
     this.jLabel6.setBounds(new Rectangle(235, 101, 34, 16));
     this.jComboBox1.setBounds(new Rectangle(82, 97, 87, 22));
     this.jComboBox2.setBounds(new Rectangle(294, 101, 94, 22));
     this.jButton1.setBounds(new Rectangle(12, 206, 73, 25));
     this.jButton1.setActionCommand("add1");
     this.jButton1.setText("add");
     this.jButton2.setBounds(new Rectangle(93, 205, 73, 25));
     this.jButton2.setActionCommand("delete1");
     this.jButton2.setText("delete");
     this.jButton3.setBounds(new Rectangle(230, 206, 73, 25));
     this.jButton3.setActionCommand("add2");
     this.jButton3.setText("add");
     this.jButton4.setBounds(new Rectangle(311, 206, 73, 25));
     this.jButton4.setActionCommand("delete2");
     this.jButton4.setText("delete");
     this.jButton5.setBounds(new Rectangle(302, 380, 73, 25));
     this.jButton5.setText("Close");
     this.jCheckBox1.setEnabled(false);
     this.jCheckBox1.setText("constraining");
     this.jCheckBox1.setBounds(new Rectangle(12, 175, 154, 25));
     this.jCheckBox2.setEnabled(false);
     this.jCheckBox2.setText("constraining");
     this.jCheckBox2.setBounds(new Rectangle(230, 172, 154, 25));
     this.jLabel7.setText("select");
     this.jLabel7.setBounds(new Rectangle(15, 145, 164, 16));
     this.jLabel8.setText("select");
     this.jLabel8.setBounds(new Rectangle(235, 143, 147, 16));
     this.jPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
     this.jPanel1.setBounds(new Rectangle(9, 52, 178, 79));
     this.jPanel2.setBounds(new Rectangle(217, 54, 178, 79));
     this.jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
     getContentPane().add(this.jTextField1, null);
     getContentPane().add(this.jComboBox1, null);
     getContentPane().add(this.jLabel6, null);
     getContentPane().add(this.jTextField2, null);
     getContentPane().add(this.jLabel5, null);
     getContentPane().add(this.jLabel3, null);
     getContentPane().add(this.jLabel1, null);
     getContentPane().add(this.jLabel2, null);
     getContentPane().add(this.jLabel4, null);
     getContentPane().add(this.jComboBox2, null);
     getContentPane().add(this.jList1, null);
     getContentPane().add(this.jList2, null);
     getContentPane().add(this.jButton1, null);
     getContentPane().add(this.jButton2, null);
     getContentPane().add(this.jButton3, null);
     getContentPane().add(this.jButton4, null);
     getContentPane().add(this.jButton5, null);
     getContentPane().add(this.jLabel7, null);
     getContentPane().add(this.jLabel8, null);
     getContentPane().add(this.jCheckBox1, null);
     getContentPane().add(this.jCheckBox2, null);
     this.jButton1.addActionListener(this);
     this.jButton2.addActionListener(this);
     this.jButton3.addActionListener(this);
     this.jButton4.addActionListener(this);
     this.jButton5.addActionListener(this);
     lineInit();
   }
 }

