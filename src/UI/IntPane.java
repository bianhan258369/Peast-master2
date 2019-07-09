 package UI;
 
 import Shape.Changjing;
 import Shape.Diagram;
 import Shape.IntDiagram;
 import Shape.Jiaohu;
 import Shape.Oval;
 import org.dom4j.Document;
 import org.dom4j.io.OutputFormat;
 import util.XMLFileFilter;

 import java.awt.BasicStroke;
 import java.awt.Color;
 import java.awt.Cursor;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.KeyEvent;
 import java.awt.event.KeyListener;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;

 import java.awt.geom.*;
 import java.io.File;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.io.Writer;
 import java.util.LinkedList;
 import javax.swing.*;

 public class IntPane extends FatherPane
   implements MouseMotionListener, MouseListener, ActionListener, KeyListener
 {
   public IntDiagram dd;
   private boolean drag_c = false;
   private Changjing drag_cj;
   private int int_cj;
   private boolean drag_j = false;
   private Jiaohu select_jh;
   private Changjing select_cj;
   private int yuan_x;
   private int yuan_y;
   private int now_x;
   private int now_y;
   private int hua;
   private LinkedList dian = new LinkedList();
   private Jiaohu from;
   private Jiaohu to;
   JButton jButton1 = new JButton();
   JButton jButton2 = new JButton();
   private Diagram dd_pd;
   JPopupMenu set = new JPopupMenu();
   JMenuItem check = new JMenuItem("check");
   JMenuItem save = new JMenuItem("save");
   public boolean zhengque = false;
   public int leixing;
 
   public IntPane(IntDiagram dd, int state, Diagram dd_pd)
   {
     this.state = 1;
     this.dd = dd;
     this.hua = 0;
     this.leixing = -1;
     this.dd_pd = dd_pd;
     addMouseListener(this);
     addMouseMotionListener(this);
     setBackground(Color.white);
     this.jButton1.setText("jButton1");
     this.jButton2.setText("jButton2");
 
     this.jButton1.addActionListener(this);
     this.jButton2.addActionListener(this);
     this.set.add(this.check);
     this.set.add(this.save);
     this.check.addActionListener(this);
     this.save.addActionListener(this);
     addKeyListener(this);
     Oval req = null;
     if (this.dd.getBiaohao() != 0) {
       req = this.dd_pd.getRequirement(this.dd.getBiaohao());
     }
     LinkedList fe = new IntEditor(null, req, dd_pd).getfe();
     int x = 50; int y = 50;
     int a = 0; int b = 0;
     if (state == 0) {
       for (int i = 0; i <= fe.size() - 1; i++) {
         String s = (String)fe.get(i);
         if (s.endsWith("(r)")) {
           s = s.substring(0, s.indexOf("("));
           Jiaohu jh1 = new Jiaohu(50 + b * 60, y + 100, 3, 0);
           jh1.setNumber(new Integer(s.substring(3)).intValue());
           jh1.setState(1);
           dd.addJiaohu(jh1);
           b++;
         }
         else {
           Jiaohu jh1 = new Jiaohu(50 + a * 60, y, 3, 0);
           jh1.setNumber(new Integer(s.substring(3)).intValue());
           jh1.setState(0);
           dd.addJiaohu(jh1);
           a++;
         }
       }
       repaint();
     }
   }
 
   public void keyTyped(KeyEvent e) {
   }
 
   public void keyPressed(KeyEvent e) {
     if ((this.select_cj != null) && 
       (e.getKeyCode() == 127)) {
       this.dd.deletecj(this.select_cj);
       this.select_cj = null;
       repaint();
     }
 
     if ((this.select_jh != null) && 
       (e.getKeyCode() == 127)) {
       this.dd.deletejh(this.select_jh);
       this.select_jh = null;
       repaint();
     }
   }
 
   public void keyReleased(KeyEvent e)
   {
   }
 
   public void paint(Graphics g)
   {
     super.paint(g);
     this.dd.draw(g);
     if ((this.leixing < 0) && (this.dian.size() != 0)) {
       return;
     }
 
     if (this.leixing == 0) {
       g.setColor(Color.red);
     }
     if (this.leixing == 1) {
       g.setColor(Color.BLUE);
     }
     if (this.leixing == 2) {
       g.setColor(Color.green);
     }
     if (this.leixing == 3) {
       g.setColor(Color.orange);
     }
     if (this.leixing == 4) {
       g.setColor(new Color(182, 88, 157));
     }
     for (int i = 0; i <= this.dian.size() - 1; i++) {
       int a = new Integer(this.dian.get(i).toString()).intValue();
       int b = new Integer(this.dian.get(i + 1).toString()).intValue();
       g.fillOval(a - 2, b - 2, 4, 4);
       i++;
     }
 
     g.setColor(Color.black);
     if (this.drag_c) {
       drawXu(g, this.now_x, this.now_y);
     }
     if (this.drag_j)
       drawXu(g, this.now_x, this.now_y);
   }
 
   public void setLeixing(int i)
   {
     if ((i >= 0) && (i <= 4)) {
       setHua(2);
     }
     this.leixing = i;
   }
 
   public static void main(String[] args)
   {
   }
 
   public void setHua(int hua) {
     this.hua = hua;
     if (hua == 0) {
       setCursor(Cursor.getDefaultCursor());
     }
     else
       setCursor(Cursor.getPredefinedCursor(1));
   }
 
   public void actionPerformed(ActionEvent e)
   {
     if (e.getActionCommand().equals("jButton1")) {
       setHua(1);
     }
     if (e.getActionCommand().equals("jButton2")) {
       setHua(2);
       setLeixing(1);
     }
     if (e.getActionCommand().equals("check"))
       if ((this.dd.check1()) && (this.dd.check2())) {
         JOptionPane.showMessageDialog(this, "OK");
         this.zhengque = true;
       }
       else {
         JOptionPane.showMessageDialog(this, Main.errmes);
         if (Main.errstate == 2) {
           FatherPane kk = Main.win.myDisplayPane.getMyPane("ProblemDiagram");
           if (kk != null) {
             Main.win.myDisplayPane.setSelected(kk);
           }
         }
       }
   }
 
   public void mousePressed(MouseEvent e)
   {
     requestFocus();
     if (this.select_jh != null) {
       this.select_jh.selected = false;
       repaint();
     }
     if (this.select_cj != null) {
       this.select_cj.selected = false;
       repaint();
     }
     this.select_cj = null;
     this.select_jh = null;
     if (e.getButton() == 3) {
       this.set.show(this, e.getX(), e.getY());
       return;
     }
     if (this.hua == 0) {
       Changjing tmp_cj = this.dd.getSelected(e.getX(), e.getY());
       if (tmp_cj != null) {
         this.drag_c = true;
         this.drag_cj = tmp_cj;
         this.int_cj = tmp_cj.getSelected(e.getX(), e.getY());
         this.now_x = e.getX();
         this.now_y = e.getY();
         repaint();
         return;
       }
       Changjing tmp_c = this.dd.which(e.getX(), e.getY());
       if (tmp_c != null) {
         this.select_cj = tmp_c;
         this.select_cj.selected = true;
         repaint();
         return;
       }
       Jiaohu tmp_j = this.dd.getSelecte(e.getX(), e.getY());
       if (tmp_j != null) {
         this.select_jh = tmp_j;
         tmp_j.selected = true;
         this.drag_j = true;
         this.yuan_x = e.getX();
         this.yuan_y = e.getY();
         this.now_x = this.select_jh.getMiddleX();
         this.now_y = this.select_jh.getMiddleY();
         repaint();
         return;
       }
     }
     if (this.hua == 1) {
       Jiaohu jh1 = new Jiaohu(e.getX(), e.getY(), 3, 0);
       Oval req = null;
       if (this.dd.getBiaohao() != 0) {
         req = this.dd_pd.getRequirement(this.dd.getBiaohao());
       }
       new IntEditor(jh1, req, this.dd_pd).show();
       this.dd.addJiaohu(jh1);
       setHua(0);
       repaint();
       return;
     }
 
     if (this.hua == 2) {
       if (this.leixing < 0) {
         return;
       }
       if (this.from == null) {
         Jiaohu tmp_jh = this.dd.getSelecte(e.getX(), e.getY());
         if (tmp_jh == null) {
           setHua(0);
           return;
         }
 
         this.from = tmp_jh;
         return;
       }
 
       Jiaohu tmp_jh = this.dd.getSelecte(e.getX(), e.getY());
       if (tmp_jh != null) {
         Changjing new_cj = new Changjing(this.dian, this.from, tmp_jh, this.leixing);
         this.dd.addChangjing(new_cj);
         repaint();
         setHua(0);
         this.from = null;
         this.dian = new LinkedList();
         setLeixing(-1);
         return;
       }
 
       this.dian.add("" + e.getX());
       this.dian.add("" + e.getY());
       repaint();
       return;
     }
   }
 
   public void mouseClicked(MouseEvent e)
   {
   }
 
   public void mouseReleased(MouseEvent e)
   {
     this.drag_c = false;
     this.drag_cj = null;
     this.drag_j = false;
     repaint();
   }
 
   public void mouseDragged(MouseEvent e) {
     if (this.drag_c) {
       this.drag_cj.moveTo(this.int_cj, e.getX(), e.getY());
       this.now_x = e.getX();
       this.now_y = e.getY();
       repaint();
       return;
     }
     if (this.drag_j) {
       this.select_jh.moveTo(e.getX() - this.yuan_x, e.getY() - this.yuan_y);
       this.yuan_x = e.getX();
       this.yuan_y = e.getY();
       this.now_x = this.select_jh.getMiddleX();
       this.now_y = this.select_jh.getMiddleY();
       this.dd.follow(this.select_jh);
       repaint();
     }
   }
   public void mouseEntered(MouseEvent e) {
   }
   public void mouseExited(MouseEvent e) {
   }
 
   public void mouseMoved(MouseEvent e) {
   }
 
   public void drawXu(Graphics g, int x, int y) {
     Graphics2D g2 = (Graphics2D)g;
     float[] k = { 5.0F };
 
     BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F, k, 0.0F);
 
     g2.setStroke(dashed);
     g2.draw(new Line2D.Double(x, 0.0D, x, 1000.0D));
     g2.draw(new Line2D.Double(0.0D, y, 1000.0D, y));
     dashed = new BasicStroke();
     g2.setStroke(dashed);
   }
 }