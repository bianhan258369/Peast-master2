 package UI;
 
 import Ontology.Ontology;
import Ontology.Ontology_c;

 import java.awt.BorderLayout;
 import java.awt.Container;
 import java.awt.Dimension;
 import java.awt.Toolkit;
 import java.util.LinkedList;
 import javax.swing.JDialog;
 import javax.swing.JLabel;
 import javax.swing.JScrollPane;
 import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
 
 public class OntologyShow extends JDialog
 {
   Ontology ont;
   BorderLayout borderLayout1 = new BorderLayout();
   JLabel jLabel1 = new JLabel();
   JScrollPane js;
   JTree tree;
   DefaultMutableTreeNode root;
 
   public static void main(String[] args)
   {
   }
 
   public OntologyShow()
   {
     this.ont = new Ontology();
     try {
       jbInit();
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }
 
   private void deel(DefaultMutableTreeNode dn, String name) {
     LinkedList ll = this.ont.getSubClass(name);
     for (int i = 0; i <= ll.size() - 1; i++) {
       String n = ll.get(i).toString();
       n = n.substring(n.indexOf("#") + 1);
       DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(n);
       dn.add(tmp);
       deel(tmp, ll.get(i).toString());
     }
   }
 
   private void jbInit() throws Exception {
     Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
     double width = d.getWidth();
     double height = d.getHeight();
     setLocation((int)width / 2 - 200, (int)height / 2 - 150);
     setSize(400, 300);
     setTitle("Ontology");
     this.jLabel1.setText("Show the ontology.");
     getContentPane().setLayout(this.borderLayout1);
     getContentPane().add(this.jLabel1, "North");
 
     this.root = new DefaultMutableTreeNode("Thing");
     this.tree = new JTree(this.root);
     this.js = new JScrollPane(this.tree);
     getContentPane().add(this.js, "Center");
     deel(this.root, "Thing");
     this.tree.updateUI();
   }
 }

