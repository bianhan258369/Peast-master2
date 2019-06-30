 package UI;
 
 import Shape.Data;
 import Shape.Diagram;
 import Shape.Oval;
 import Shape.Phenomenon;
 import Shape.Rect;
 import java.awt.BorderLayout;
 import java.awt.Dimension;
 import java.awt.Toolkit;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.util.LinkedList;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.JSplitPane;
 import javax.swing.JTabbedPane;
 import javax.swing.JTextArea;
 import javax.swing.JTree;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import javax.swing.tree.DefaultMutableTreeNode;
 import javax.swing.tree.TreePath;
 
 public class InfoPane extends JPanel
   implements MouseListener, ChangeListener
 {
   JSplitPane js = new JSplitPane();
   BorderLayout bl = new BorderLayout();
   JScrollPane jt_qian = new JScrollPane();
   JTextArea jt = new JTextArea();
   JTabbedPane desk = new JTabbedPane();
   JScrollPane treePane;
   JTree tree;
   DefaultMutableTreeNode root;
   LinkedList sen = new LinkedList();
   LinkedList sub = new LinkedList();
   ThreePane phe;
   ThreePane inter;
   ThreePane ref;
   DefaultMutableTreeNode intPane;
   DefaultMutableTreeNode problem;
   String[] column_P = { "Name", "Description", "PheType" };
 
   String[] column_I = { "Name", "Initiator", "Receiver", "Content" };
 
   String[] column_R = { "Name", "Initiator", "Receiver", "Content", "RefType" };
 
   public static void main(String[] args)
   {
   }
 
   public InfoPane() {
     try {
       jbInit();
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }
 
   public void setTreeNodeDescription(String s, int num)
   {
     int n = new Integer(s.substring(17)).intValue();
     for (int i = 0; i <= this.sub.size() - 1; i++) {
       DefaultMutableTreeNode tmp = (DefaultMutableTreeNode)this.sub.get(i);
       if (tmp.toString().startsWith(s)) {
         DefaultMutableTreeNode tmp_d = (DefaultMutableTreeNode)tmp.getParent();
         tmp_d.remove(tmp);
         this.sub.remove(i);
         tmp = new DefaultMutableTreeNode(s + "(req" + num + ")");
         tmp_d.add(tmp);
         this.sub.add(tmp);
         this.tree.updateUI();
       }
     }
     for (int i = 0; i <= this.sen.size() - 1; i++) {
       DefaultMutableTreeNode tmp = (DefaultMutableTreeNode)this.sen.get(i);
       if (tmp.toString().equals("ScenarioGraph" + n)) {
         DefaultMutableTreeNode tmp_d = (DefaultMutableTreeNode)tmp.getParent();
         tmp_d.remove(tmp);
         this.sen.remove(i);
         tmp = new DefaultMutableTreeNode("ScenarioGraph" + new Integer(s.substring(17)) + "(req" + num + ")");
         tmp_d.add(tmp);
         this.sen.add(tmp);
         this.tree.updateUI();
       }
     }
   }
 
   public LinkedList add(LinkedList l1, LinkedList l2)
   {
     LinkedList re = new LinkedList();
     for (int i = 0; i <= l1.size() - 1; i++) {
       re.add(l1.get(i));
     }
     for (int i = 0; i <= l2.size() - 1; i++) {
       Phenomenon tmp_p = (Phenomenon)l2.get(i);
       boolean zhaodao = false;
       for (int j = 0; j <= l1.size() - 1; j++) {
         if (Data.same(tmp_p, (Phenomenon)l1.get(j))) {
           zhaodao = true;
           break;
         }
       }
       if (!zhaodao) {
         re.add(tmp_p);
       }
     }
     return re;
   }
 
   public void stateChanged(ChangeEvent e) {
     JTabbedPane tabbedPane = (JTabbedPane)e.getSource();
     int selectedIndex = tabbedPane.getSelectedIndex();
     Object[][] data = null;
     if (selectedIndex == 0) {
       return;
     }
     if (Main.win.myProblemDiagram == null) {
       return;
     }
     LinkedList ll = Main.win.myProblemDiagram.getPhenomenon();
     if (Main.win.myDrawPane.i >= 11) {
       ll = new LinkedList();
       for (int i = 0; i <= Main.win.subpd_l.size() - 1; i++) {
         Diagram dd = ((MyPane)Main.win.subpd_l.get(i)).dd;
         ll = add(ll, dd.getPhenomenon());
       }
     }
     if (selectedIndex == 1) {
       data = new Object[ll.size()][3];
       for (int i = 0; i <= ll.size() - 1; i++) {
         Phenomenon tmp_p = (Phenomenon)ll.get(i);
         data[i][0] = ("phe" + tmp_p.getBiaohao());
         data[i][1] = tmp_p.getName();
         data[i][2] = tmp_p.getState();
       }
       this.phe.setData(data);
     }
 
     if (selectedIndex == 2) {
       data = new Object[ll.size()][4];
       for (int i = 0; i <= ll.size() - 1; i++) {
         Phenomenon tmp_p = (Phenomenon)ll.get(i);
         data[i][0] = ("int" + tmp_p.getBiaohao());
         data[i][3] = tmp_p.getName();
         data[i][1] = tmp_p.getFrom().getShortName();
         data[i][2] = tmp_p.getTo().getShortName();
       }
       this.inter.setData(data);
     }
     if (selectedIndex == 3) {
       ll = Main.win.myProblemDiagram.getReference();
       if (Main.win.myDrawPane.i >= 11) {
         ll = new LinkedList();
         for (int i = 0; i <= Main.win.subpd_l.size() - 1; i++) {
           Diagram dd = ((MyPane)Main.win.subpd_l.get(i)).dd;
           ll = add(ll, dd.getReference());
         }
       }
 
       data = new Object[ll.size()][5];
       for (int i = 0; i <= ll.size() - 1; i++) {
         Phenomenon tmp_p = (Phenomenon)ll.get(i);
         data[i][0] = ("req" + tmp_p.getRequirement().getBiaohao());
         data[i][3] = tmp_p.getName();
         data[i][1] = tmp_p.getFrom().getShortName();
         data[i][2] = tmp_p.getTo().getShortName();
         if (tmp_p.getConstraining()) {
           data[i][4] = "constraining";
         }
         else {
           data[i][4] = "normal";
         }
       }
       this.ref.setData(data);
     }
   }
 
   public void setDescription(String s)
   {
     this.jt.setText(s);
   }
 
   public void treeInit() {
     this.root = new DefaultMutableTreeNode(Main.win.nameOfProject);
     this.tree = new JTree(this.root);
     this.tree.addMouseListener(this);
     this.treePane = new JScrollPane(this.tree);
     this.desk.setTabPlacement(3);
     this.desk.add(this.treePane, "Diagram");
     this.phe = new ThreePane("Phenomenon", this.column_P, new Object[0][]);
     this.inter = new ThreePane("Interaction", this.column_I, new Object[0][]);
     this.ref = new ThreePane("Reference", this.column_R, new Object[0][]);
     this.desk.add(this.phe, this.phe.name);
     this.desk.add(this.inter, this.inter.name);
     this.desk.add(this.ref, this.ref.name);
   }
 
   public void treeAdd(int i, int j, String s)
   {
     if (i == 0) {
       DefaultMutableTreeNode tmp = new DefaultMutableTreeNode("ContextDiagram");
       this.root.add(tmp);
       this.tree.updateUI();
     }
     if (i == 1) {
       this.problem = new DefaultMutableTreeNode("ProblemDiagram");
       this.root.add(this.problem);
       this.tree.updateUI();
     }
     if ((i == 2) && (j == 1)) {
       this.intPane = new DefaultMutableTreeNode("ScenarioGraph");
       this.root.add(this.intPane);
       DefaultMutableTreeNode tmp = new DefaultMutableTreeNode("ScenarioGraph" + j + "(" + s + ")");
 
       this.intPane.add(tmp);
       this.sen.add(tmp);
       this.tree.updateUI();
     }
     if ((i == 2) && (j != 1)) {
       DefaultMutableTreeNode tmp = new DefaultMutableTreeNode("ScenarioGraph" + j + "(" + s + ")");
 
       this.intPane.add(tmp);
       this.sen.add(tmp);
       this.tree.updateUI();
     }
     if (i == 3) {
       DefaultMutableTreeNode tmp = new DefaultMutableTreeNode("SubProblemDiagram" + j + "(" + s + ")");
 
       this.problem.add(tmp);
       this.sub.add(tmp);
       this.tree.updateUI();
     }
   }
 
   public void treeAdd1(int i, int j, String s) {
     DefaultMutableTreeNode tmp_d = null;
     if (i == 0) {
       for (int k = 0; k <= this.sen.size() - 1; k++) {
         DefaultMutableTreeNode tmp = (DefaultMutableTreeNode)this.sen.get(k);
         if (tmp.toString().indexOf("(") == -1) {
           if (tmp.toString().equals(s)) {
             tmp_d = new DefaultMutableTreeNode(tmp.toString() + "" + j);
 
             tmp.add(tmp_d);
           }
         } else {
           if (!tmp.toString().substring(0, tmp.toString().indexOf("(")).equals(s))
             continue;
           tmp_d = new DefaultMutableTreeNode(tmp.toString().substring(0, tmp.toString().indexOf("(")) + j);
 
           tmp.add(tmp_d);
         }
       }
     }
     if (i == 1) {
       for (int k = 0; k <= this.sub.size() - 1; k++) {
         DefaultMutableTreeNode tmp = (DefaultMutableTreeNode)this.sub.get(k);
         if (tmp.toString().indexOf("(") == -1) {
           if (tmp.toString().equals(s)) {
             tmp_d = new DefaultMutableTreeNode(tmp.toString());
 
             tmp.add(tmp_d);
           }
         } else {
           if (!tmp.toString().substring(0, tmp.toString().indexOf("(")).equals(s))
             continue;
           tmp_d = new DefaultMutableTreeNode(tmp.toString().substring(0, tmp.toString().indexOf("(")) + j);
 
           tmp.add(tmp_d);
         }
       }
     }
     if (i == 0) {
       this.sen.add(tmp_d);
     }
     if (i == 1) {
       this.sub.add(tmp_d);
     }
     this.tree.updateUI();
   }
 
   private void jbInit() throws Exception {
     setLayout(this.bl);
     this.js.setOrientation(0);
     Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
     double width = d.getWidth();
     double height = d.getHeight();
     this.js.setDividerLocation((int)(2.0D * height / 5.0D));
     this.jt_qian = new JScrollPane(this.jt);
     this.jt_qian.add(this.jt);
     this.jt_qian.setViewportView(this.jt);
     this.js.setBottomComponent(this.jt_qian);
     this.js.setTopComponent(this.desk);
     this.jt.setEditable(false);
     add(this.js, "Center");
     this.desk.addChangeListener(this);
   }
   public void mouseClicked(MouseEvent e) {
   }
   public void mousePressed(MouseEvent e) {
   }
 
   public void mouseReleased(MouseEvent e) {
     TreePath path = ((JTree)e.getComponent()).getPathForLocation(e.getX(), e.getY());
 
     DefaultMutableTreeNode node = null;
     if (path != null) {
       node = (DefaultMutableTreeNode)path.getLastPathComponent();
       if (node.isRoot()) {
         return;
       }
       String s = node.toString();
       int i = s.indexOf("(");
       if (i != -1) {
         s = s.substring(0, i);
       }
       FatherPane kk = Main.win.myDisplayPane.getMyPane(s);
       if (kk != null)
         Main.win.myDisplayPane.setSelected(kk);
     }
   }
 
   public void mouseEntered(MouseEvent e)
   {
   }
 
   public void mouseExited(MouseEvent e)
   {
   }
 }