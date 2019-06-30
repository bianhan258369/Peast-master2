 package Shape;
 
 import java.awt.BasicStroke;
 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.RenderingHints;
	import java.awt.geom.Line2D;
 import java.awt.geom.Line2D.Double;
 import java.io.PrintStream;
	import java.util.LinkedList;
 
 public class Line extends Shape
 {
   private int state;
   protected int x1;
   protected int y1;
   protected int x2;
   protected int y2;
   public Shape from;
   public Shape to;
   public String name;
   public LinkedList phenomenons = new LinkedList();
   public String core = "";
   public String core1 = "";
 
   public Line(Shape from, Shape to, int state) { 
	   this.shape = 2;
     setState(state);
 
     this.name = "";
     if (from.shape == 0) {
       Rect tmp_r = (Rect)from;
       if (tmp_r.state == 2) {
         this.from = from;
         this.to = to;
       }
       else {
         this.to = from;
         this.from = to;
       }
     }
     else {
       this.from = from;
       this.to = to;
     }
     refresh(); }
 
   protected void setCore(String core) {
     this.core = core;
   }
 
   protected void setCore1(String core) {
     this.core1 = core;
   }
 
   public String getDescription() {
     String s = "";
     s = s + this.name + ":";
     String s1 = ""; String s2 = "";
     Rect m = null;
     Rect d = null;
     if (this.phenomenons.size() == 0) {
       if (this.state == 0) {
         return s + Data.I_TEXT;
       }
       if (this.state == 1) {
         return s + Data.RR_TEXT;
       }
       if (this.state == 2) {
         return s + Data.RC_TEXT;
       }
     }
 
     for (int i = 0; i <= this.phenomenons.size() - 1; i++) {
       Phenomenon tmp_p = (Phenomenon)this.phenomenons.get(i);
       Rect f = tmp_p.getFrom();
       Rect t = tmp_p.getTo();
       if (f.state == 2) {
         m = f;
         d = t;
         s1 = s1 + tmp_p.getName() + ",";
       }
       else {
         d = f;
         m = t;
         s2 = s2 + tmp_p.getName() + ",";
       }
     }
 
     if (!s1.equals("")) {
       s1 = s1.substring(0, s1.length() - 1);
       s1 = "{" + s1 + "}";
       if (m.getShortName() != null) {
         s1 = m.getShortName() + "!" + s1;
       }
     }
     if (!s2.equals("")) {
       s2 = s2.substring(0, s2.length() - 1);
       s2 = "{" + s2 + "}";
       if (d.getShortName() != null) {
         s2 = d.getShortName() + "!" + s2;
       }
     }
     if ((!s1.equals("")) && (!this.core.equals(""))) {
       s1 = s1 + "[" + this.core + "]";
     }
     if ((!s2.equals("")) && (!this.core1.equals(""))) {
       s2 = s2 + "[" + this.core1 + "]";
     }
     if ((!s1.equals("")) && (!s2.equals("")))
     {
       s = s + s1 + "," + s2;
     }
     else if (!s1.equals("")) {
       s = s + s1;
     }
     else if (!s2.equals("")) {
       s = s + s2;
     }
     return s;
   }
 
   public void refresh()
   {
     if ((this.from.shape == 0) && (this.to.shape == 0)) {
       Rect tmp2 = (Rect)this.to;
       Rect tmp1 = (Rect)this.from;
       if ((tmp2.x1 < tmp1.x1 + tmp1.x2) && (tmp2.x1 > tmp1.x1 - tmp2.x2)) {
         if (tmp1.y1 < tmp2.y1) {
           this.x1 = (tmp1.x1 + tmp1.x2 / 2);
           this.y1 = (tmp1.y1 + tmp1.y2);
           this.x2 = (tmp2.x1 + tmp2.x2 / 2);
           this.y2 = tmp2.y1;
         }
         else {
           this.x1 = (tmp1.x1 + tmp1.x2 / 2);
           this.y1 = tmp1.y1;
           this.x2 = (tmp2.x1 + tmp2.x2 / 2);
           this.y2 = (tmp2.y1 + tmp2.y2);
         }
 
         return;
       }
       if (tmp1.x1 <= tmp2.x1) {
         this.x1 = (tmp1.x1 + tmp1.x2);
         this.y1 = (tmp1.y1 + tmp1.y2 / 2);
         this.x2 = tmp2.x1;
         if (tmp2.state == 1) {
           this.x2 -= 10;
         }
         if (tmp2.state == 2) {
           this.x2 -= 20;
         }
         this.y2 = (tmp2.y1 + tmp2.y2 / 2);
       }
       else {
         this.x1 = tmp1.x1;
         if (tmp1.state == 1) {
           this.x1 -= 10;
         }
         if (tmp1.state == 2) {
           this.x1 -= 20;
         }
         this.y1 = (tmp1.y1 + tmp1.y2 / 2);
         this.x2 = (tmp2.x1 + tmp2.x2);
         this.y2 = (tmp2.y1 + tmp2.y2 / 2);
       }
     }
     if ((this.from.shape == 1) && (this.to.shape == 0)) {
       Rect tmp2 = (Rect)this.to;
       Oval tmp1 = (Oval)this.from;
       if ((tmp2.x1 < tmp1.x1 + tmp1.x2) && (tmp2.x1 > tmp1.x1 - tmp2.x2)) {
         if (tmp1.y1 < tmp2.y1) {
           this.x1 = (tmp1.x1 + tmp1.x2 / 2);
           this.y1 = (tmp1.y1 + tmp1.y2);
           this.x2 = (tmp2.x1 + tmp2.x2 / 2);
           this.y2 = tmp2.y1;
         }
         else {
           this.x1 = (tmp1.x1 + tmp1.x2 / 2);
           this.y1 = tmp1.y1;
           this.x2 = (tmp2.x1 + tmp2.x2 / 2);
           this.y2 = (tmp2.y1 + tmp2.y2);
         }
 
         return;
       }
 
       if (tmp1.x1 <= tmp2.x1) {
         this.x1 = (tmp1.x1 + tmp1.x2);
         this.y1 = (tmp1.y1 + tmp1.y2 / 2);
         this.x2 = tmp2.x1;
         if (tmp2.state == 1) {
           this.x2 -= 10;
         }
         if (tmp2.state == 2) {
           this.x2 -= 20;
         }
         this.y2 = (tmp2.y1 + tmp2.y2 / 2);
       }
       else {
         this.x1 = tmp1.x1;
         this.y1 = (tmp1.y1 + tmp1.y2 / 2);
         this.x2 = (tmp2.x1 + tmp2.x2);
         this.y2 = (tmp2.y1 + tmp2.y2 / 2);
       }
     }
     if ((this.from.shape == 0) && (this.to.shape == 1)) {
       Oval tmp2 = (Oval)this.to;
       Rect tmp1 = (Rect)this.from;
       if ((tmp2.x1 < tmp1.x1 + tmp1.x2) && (tmp2.x1 > tmp1.x1 - tmp2.x2)) {
         if (tmp1.y1 < tmp2.y1) {
           this.x1 = (tmp1.x1 + tmp1.x2 / 2);
           this.y1 = (tmp1.y1 + tmp1.y2);
           this.x2 = (tmp2.x1 + tmp2.x2 / 2);
           this.y2 = tmp2.y1;
         }
         else {
           this.x1 = (tmp1.x1 + tmp1.x2 / 2);
           this.y1 = tmp1.y1;
           this.x2 = (tmp2.x1 + tmp2.x2 / 2);
           this.y2 = (tmp2.y1 + tmp2.y2);
         }
 
         return;
       }
 
       if (tmp1.x1 <= tmp2.x1) {
         this.x1 = (tmp1.x1 + tmp1.x2);
         this.y1 = (tmp1.y1 + tmp1.y2 / 2);
         this.x2 = tmp2.x1;
         this.y2 = (tmp2.y1 + tmp2.y2 / 2);
       }
       else {
         this.x1 = tmp1.x1;
         if (tmp1.state == 1) {
           this.x1 -= 10;
         }
         if (tmp1.state == 2) {
           this.x1 -= 20;
         }
 
         this.y1 = (tmp1.y1 + tmp1.y2 / 2);
         this.x2 = (tmp2.x1 + tmp2.x2);
         this.y2 = (tmp2.y1 + tmp2.y2 / 2);
       }
     }
     if ((this.from.shape == 1) && (this.to.shape == 1)) {
       Oval tmp2 = (Oval)this.to;
       Oval tmp1 = (Oval)this.from;
       if ((tmp2.x1 < tmp1.x1 + tmp1.x2) && (tmp2.x1 > tmp1.x1 - tmp2.x2)) {
         if (tmp1.y1 < tmp2.y1) {
           this.x1 = (tmp1.x1 + tmp1.x2 / 2);
           this.y1 = (tmp1.y1 + tmp1.y2);
           this.x2 = (tmp2.x1 + tmp2.x2 / 2);
           this.y2 = tmp2.y1;
         }
         else {
           this.x1 = (tmp1.x1 + tmp1.x2 / 2);
           this.y1 = tmp1.y1;
           this.x2 = (tmp2.x1 + tmp2.x2 / 2);
           this.y2 = (tmp2.y1 + tmp2.y2);
         }
 
         return;
       }
 
       if (tmp1.x1 <= tmp2.x1) {
         this.x1 = (tmp1.x1 + tmp1.x2);
         this.y1 = (tmp1.y1 + tmp1.y2 / 2);
         this.x2 = tmp2.x1;
         this.y2 = (tmp2.y1 + tmp2.y2 / 2);
       }
       else {
         this.x1 = tmp1.x1;
         this.y1 = (tmp1.y1 + tmp1.y2 / 2);
         this.x2 = (tmp2.x1 + tmp2.x2);
         this.y2 = (tmp2.y1 + tmp2.y2 / 2);
       }
     }
   }
 
   public void draw(Graphics g)
   {
     Graphics2D g2d = (Graphics2D)g;
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
     if (this.selected) {
       g.setColor(Color.red);
     }
     if (this.state == 0) {
       g.drawLine(this.x1, this.y1, this.x2, this.y2);
     }
     if (this.state == 1) {
       Graphics2D g2 = (Graphics2D)g;
       BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F, Data.LENGTHOFDASH, 0.0F);
 
       g2.setStroke(dashed);
       g2.draw(new Line2D.Double(this.x1, this.y1, this.x2, this.y2));
       dashed = new BasicStroke();
       g2.setStroke(dashed);
     }
 
     if (this.state == 2) {
       Graphics2D g2 = (Graphics2D)g;
       BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F, Data.LENGTHOFDASH, 0.0F);
 
       g2.setStroke(dashed);
       g2.draw(new Line2D.Double(this.x1, this.y1, this.x2, this.y2));
       dashed = new BasicStroke();
       g2.setStroke(dashed);
       drawArrow(g, this.x1, this.y1, this.x2, this.y2);
     }
 
     if (this.selected)
       g.setColor(Color.black);
   }
 
   private void drawArrow(Graphics g, int x1, int y1, int x2, int y2)
   {
     int a1 = 0; int b1 = 0; int a2 = 0; int b2 = 0;
     if (x1 == x2) {
       a1 = x1 - (int)Math.sqrt(2.0D) * 5;
       a2 = x1 + (int)Math.sqrt(2.0D) * 5;
       if (y2 > y1) {
         b1 = y2 - (int)Math.sqrt(2.0D) * 5;
         b2 = b1;
       }
       if (y2 <= y1) {
         b1 = y2 + (int)Math.sqrt(2.0D) * 5;
         b2 = b1;
       }
     }
     else
     {
       double s1 = Math.tan(0.7853981633974483D + Math.atan((y2 - y1) / (x2 - x1)));
 
       double s2 = Math.tan(-0.7853981633974483D + Math.atan((y2 - y1) / (x2 - x1)));
 
       double k = (y2 - y1) / (x2 - x1);
       if ((x2 > x1) && (y2 >= y1)) {
         if (k > 1.0D) {
           a1 = x2 + (int)Math.sqrt(100.0D / (1.0D + Math.pow(s1, 2.0D)));
           b1 = y2 - (int)Math.sqrt(100.0D * Math.pow(s1, 2.0D) / (1.0D + Math.pow(s1, 2.0D)));
 
           a2 = x2 - (int)Math.sqrt(100.0D / (1.0D + Math.pow(s2, 2.0D)));
           b2 = y2 - (int)Math.sqrt(100.0D * Math.pow(s2, 2.0D) / (1.0D + Math.pow(s2, 2.0D)));
         }
 
         if (k <= 1.0D) {
           a1 = x2 - (int)Math.sqrt(100.0D / (1.0D + Math.pow(s1, 2.0D)));
           b1 = y2 - (int)Math.sqrt(100.0D * Math.pow(s1, 2.0D) / (1.0D + Math.pow(s1, 2.0D)));
 
           a2 = x2 - (int)Math.sqrt(100.0D / (1.0D + Math.pow(s2, 2.0D)));
           b2 = y2 + (int)Math.sqrt(100.0D * Math.pow(s2, 2.0D) / (1.0D + Math.pow(s2, 2.0D)));
         }
 
       }
 
       if ((x2 < x1) && (y2 >= y1)) {
         if (k <= -1.0D) {
           a1 = x2 + (int)Math.sqrt(100.0D / (1.0D + Math.pow(s1, 2.0D)));
           b1 = y2 - (int)Math.sqrt(100.0D * Math.pow(s1, 2.0D) / (1.0D + Math.pow(s1, 2.0D)));
 
           a2 = x2 - (int)Math.sqrt(100.0D / (1.0D + Math.pow(s2, 2.0D)));
           b2 = y2 - (int)Math.sqrt(100.0D * Math.pow(s2, 2.0D) / (1.0D + Math.pow(s2, 2.0D)));
         }
 
         if (k > -1.0D) {
           a1 = x2 + (int)Math.sqrt(100.0D / (1.0D + Math.pow(s1, 2.0D)));
           b1 = y2 + (int)Math.sqrt(100.0D * Math.pow(s1, 2.0D) / (1.0D + Math.pow(s1, 2.0D)));
 
           a2 = x2 + (int)Math.sqrt(100.0D / (1.0D + Math.pow(s2, 2.0D)));
           b2 = y2 - (int)Math.sqrt(100.0D * Math.pow(s2, 2.0D) / (1.0D + Math.pow(s2, 2.0D)));
         }
 
       }
 
       if ((x2 < x1) && (y2 <= y1)) {
         if (k <= 1.0D) {
           a1 = x2 + (int)Math.sqrt(100.0D / (1.0D + Math.pow(s1, 2.0D)));
           b1 = y2 + (int)Math.sqrt(100.0D * Math.pow(s1, 2.0D) / (1.0D + Math.pow(s1, 2.0D)));
 
           a2 = x2 + (int)Math.sqrt(100.0D / (1.0D + Math.pow(s2, 2.0D)));
           b2 = y2 - (int)Math.sqrt(100.0D * Math.pow(s2, 2.0D) / (1.0D + Math.pow(s2, 2.0D)));
         }
 
         if (k > 1.0D) {
           a1 = x2 - (int)Math.sqrt(100.0D / (1.0D + Math.pow(s1, 2.0D)));
           b1 = y2 + (int)Math.sqrt(100.0D * Math.pow(s1, 2.0D) / (1.0D + Math.pow(s1, 2.0D)));
 
           a2 = x2 + (int)Math.sqrt(100.0D / (1.0D + Math.pow(s2, 2.0D)));
           b2 = y2 + (int)Math.sqrt(100.0D * Math.pow(s2, 2.0D) / (1.0D + Math.pow(s2, 2.0D)));
         }
 
       }
 
       if ((x2 > x1) && (y2 <= y1)) {
         if (k <= -1.0D) {
           a1 = x2 - (int)Math.sqrt(100.0D / (1.0D + Math.pow(s1, 2.0D)));
           b1 = y2 + (int)Math.sqrt(100.0D * Math.pow(s1, 2.0D) / (1.0D + Math.pow(s1, 2.0D)));
 
           a2 = x2 + (int)Math.sqrt(100.0D / (1.0D + Math.pow(s2, 2.0D)));
           b2 = y2 + (int)Math.sqrt(100.0D * Math.pow(s2, 2.0D) / (1.0D + Math.pow(s2, 2.0D)));
         }
 
         if (k > -1.0D) {
           a1 = x2 - (int)Math.sqrt(100.0D / (1.0D + Math.pow(s1, 2.0D)));
           b1 = y2 - (int)Math.sqrt(100.0D * Math.pow(s1, 2.0D) / (1.0D + Math.pow(s1, 2.0D)));
 
           a2 = x2 - (int)Math.sqrt(100.0D / (1.0D + Math.pow(s2, 2.0D)));
           b2 = y2 + (int)Math.sqrt(100.0D * Math.pow(s2, 2.0D) / (1.0D + Math.pow(s2, 2.0D)));
         }
       }
 
     }
 
     g.drawLine(a1, b1, x2, y2);
     g.drawLine(a2, b2, x2, y2);
   }
 
   public boolean isIn(int x, int y) {
     if (this.x2 == this.x1)
     {
       return (x < this.x1 + 10) && (x > this.x1 - 10) && 
         (y < Math.max(this.y1, this.y2)) && (y > Math.min(this.y1, this.y2));
     }
 
     if ((y < (x - this.x1) * (this.y2 - this.y1) / (this.x2 - this.x1) + this.y1 + 10) && (y > (x - this.x1) * (this.y2 - this.y1) / (this.x2 - this.x1) + this.y1 - 10))
     {
       if ((x <= Math.max(this.x1, this.x2)) && (x >= Math.min(this.x1, this.x2)) && (y >= Math.min(this.y1, this.y2)) && (y >= Math.min(this.y1, this.y2)))
       {
         return true;
       }
     }
     return false;
   }
 
   public void setState(int state) {
     this.state = state;
   }
 
   public int getState()
   {
     return this.state;
   }
 
   public void moveTo(int x, int y)
   {
   }
 }