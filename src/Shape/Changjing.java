 package Shape;
 
 import java.awt.BasicStroke;
 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.Polygon;
 import java.awt.RenderingHints;
	import java.awt.geom.Line2D;
 import java.awt.geom.Line2D.Double;
 import java.io.PrintStream;
 import java.io.Serializable;
	import java.util.LinkedList;
 
 public class Changjing
   implements Serializable, Cloneable
 {
   private int state;
   private LinkedList dian = new LinkedList<>();
   private Jiaohu from;
   private Jiaohu to;
   public int x1;
   public int x2;
   public int y1;
   public int y2;
   public boolean selected = false;
 
   public Changjing(LinkedList dian, int x1, int y1, int x2, int y2) { this.x1 = x1;
     this.x2 = x2;
     this.y1 = y1;
     this.y2 = y2;
     this.dian = dian; }
 
   public Changjing(LinkedList dian, Jiaohu from, Jiaohu to, int state) {
     if (dian.size() % 2 != 0) {
       System.out.println("dian.size() % 2 != 0 dian.size()="+dian.size());
     }
     this.dian = dian;
     this.state = state;
     this.from = from;
     this.to = to;
     refresh();
   }
 
   public Jiaohu getFrom() {
     return this.from;
   }
 
   public int getState() {
     return this.state;
   }
 
   public Jiaohu getTo() {
     return this.to;
   }
 
   public void moveTo(int num, int x, int y) {
     this.dian.remove(num);
     this.dian.remove(num);
     this.dian.add(num, "" + y);
     this.dian.add(num, "" + x);
     refresh();
   }
 
   public LinkedList getDian() {
     return this.dian;
   }
 
   public void refresh() {
     if (this.dian.size() == 0) {
       if ((this.to.x1 < this.from.x1 + this.from.x2) && (this.to.x1 > this.from.x1 - this.to.x2)) {
         if (this.from.y1 < this.to.y1) {
           this.x1 = (this.from.x1 + this.from.x2 / 2);
           this.y1 = (this.from.y1 + this.from.y2);
           this.x2 = (this.to.x1 + this.to.x2 / 2);
           this.y2 = this.to.y1;
         }
         else {
           this.x1 = (this.from.x1 + this.from.x2 / 2);
           this.y1 = this.from.y1;
           this.x2 = (this.to.x1 + this.to.x2 / 2);
           this.y2 = (this.to.y1 + this.to.y2);
         }
 
         return;
       }
       if (this.from.x1 <= this.to.x1) {
         this.x1 = (this.from.x1 + this.from.x2);
         this.y1 = (this.from.y1 + this.from.y2 / 2);
         this.x2 = this.to.x1;
 
         this.y2 = (this.to.y1 + this.to.y2 / 2);
       }
       else {
         this.x1 = this.from.x1;
         this.y1 = (this.from.y1 + this.from.y2 / 2);
         this.x2 = (this.to.x1 + this.to.x2);
         this.y2 = (this.to.y1 + this.to.y2 / 2);
       }
     }
     else {
       int a = new Integer(this.dian.get(0).toString()).intValue();
       int b = new Integer(this.dian.get(1).toString()).intValue();
       int c = new Integer(this.dian.get(this.dian.size() - 2).toString()).intValue();
       int d = new Integer(this.dian.get(this.dian.size() - 1).toString()).intValue();
       if (a < this.from.x1) {
         this.x1 = this.from.x1;
         this.y1 = (this.from.y1 + this.from.y2 / 2);
       }
       else if (a > this.from.x1 + this.from.x2) {
         this.x1 = (this.from.x1 + this.from.x2);
         this.y1 = (this.from.y1 + this.from.y2 / 2);
       }
       else if (b < this.from.y1) {
         this.x1 = (this.from.x1 + this.from.x2 / 2);
         this.y1 = this.from.y1;
       }
       else if (b > this.from.y1 + this.from.y2) {
         this.x1 = (this.from.x1 + this.from.x2 / 2);
         this.y1 = (this.from.y1 + this.from.y2);
       }
 
       if (c < this.to.x1) {
         this.x2 = this.to.x1;
         this.y2 = (this.to.y1 + this.to.y2 / 2);
       }
       else if (c > this.to.x1 + this.to.x2) {
         this.x2 = (this.to.x1 + this.to.x2);
         this.y2 = (this.to.y1 + this.to.y2 / 2);
       }
       else if (d < this.to.y1) {
         this.x2 = (this.to.x1 + this.to.x2 / 2);
         this.y2 = this.to.y1;
       }
       else if (d > this.to.y1 + this.to.y2) {
         this.x2 = (this.to.x1 + this.to.x2 / 2);
         this.y2 = (this.to.y1 + this.to.y2);
       }
     }
   }
 
   public boolean isIn(int x, int y, int a, int b, int c, int d) {
     if (c == a)
     {
       return (x < a + 10) && (x > a - 10) && 
         (y < Math.max(b, d)) && (y > Math.min(b, d));
     }
 
     if ((y < (x - a) * (d - b) / (c - a) + b + 10) && (y > (x - a) * (d - b) / (c - a) + b - 10))
     {
       if ((x <= Math.max(a, c)) && (x >= Math.min(a, c)) && (y >= Math.min(b, d)) && (y >= Math.min(b, d)))
       {
         return true;
       }
     }
     return false;
   }
 
   public boolean in(int x, int y) {
     int w = this.x1; int q = this.y1;
     for (int i = 0; i <= this.dian.size() - 2; i++) {
       int tmp_i1 = new Integer((String)this.dian.get(i)).intValue();
       i++;
       int tmp_i2 = new Integer((String)this.dian.get(i)).intValue();
       if (isIn(x, y, w, q, tmp_i1, tmp_i2)) return true;
       w = tmp_i1;
       q = tmp_i2;
     }
     return isIn(x, y, w, q, this.x2, this.y2);
   }
 
   public int getSelected(int x, int y) {
     for (int i = 0; i <= this.dian.size() - 1; i++) {
       int a = new Integer(this.dian.get(i).toString()).intValue();
       int b = new Integer(this.dian.get(i + 1).toString()).intValue();
       if (Math.pow(x - a, 2.0D) + Math.pow(y - b, 2.0D) < 8.0D)
       {
         return i;
       }
       i++;
     }
     return this.dian.size();
   }

   public void setState(int state){
     this.state = state;
   }
 
   public void draw(Graphics g)
   {
     Graphics2D g2d = (Graphics2D)g;
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
     if (this.state == 0) {
       g.setColor(Color.red);
     }
     if (this.state == 1) {
       g.setColor(Color.BLUE);
     }
     if (this.state == 2) {
       g.setColor(Color.green);
     }
     if (this.state == 3) {
       g.setColor(new Color(190, 126, 20));
     }
     if (this.state == 4) {
       g.setColor(new Color(182, 88, 157));
     }
     if(this.state == 5) {
       g.setColor(Color.black);
     }
     if (this.dian.size() == 0) {
       if (this.state != 3) {
         g.drawLine(this.x1, this.y1, this.x2, this.y2);
       }
       else {
         Graphics2D g2 = (Graphics2D)g;
         BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F, Data.LENGTHOFDASH, 0.0F);
 
         g2.setStroke(dashed);
         g2.draw(new Line2D.Double(this.x1, this.y1, this.x2, this.y2));
         dashed = new BasicStroke();
         g2.setStroke(dashed);
       }
 
       if (this.selected) {
         int r = 4;
 
         Color c = g.getColor();
         g.setColor(Color.white);
         g.fillOval(this.x1 - r, this.y1 - r, 2 * r, 2 * r);
         g.fillOval(this.x2 - r, this.y2 - r, 2 * r, 2 * r);
         g.setColor(c);
       }
 
       if (this.state != 2)
         drawArrow(g, this.x1, this.y1, this.x2, this.y2);
     }
     else
     {
       int a = this.x1;
       int b = this.y1;
       for (int i = 0; i <= this.dian.size() - 1; i++) {
         int c = new Integer(this.dian.get(i).toString()).intValue();
         int d = new Integer(this.dian.get(i + 1).toString()).intValue();
         if (this.state != 3) {
           g.drawLine(a, b, c, d);
         }
         else {
           Graphics2D g2 = (Graphics2D)g;
           BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F, Data.LENGTHOFDASH, 0.0F);
 
           g2.setStroke(dashed);
           g2.draw(new Line2D.Double(a, b, c, d));
           dashed = new BasicStroke();
           g2.setStroke(dashed);
         }
         i++;
         a = c;
         b = d;
       }
       if (this.state != 3) {
         g.drawLine(a, b, this.x2, this.y2);
       }
       else {
         Graphics2D g2 = (Graphics2D)g;
         BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F, Data.LENGTHOFDASH, 0.0F);
 
         g2.setStroke(dashed);
         g2.draw(new Line2D.Double(a, b, this.x2, this.y2));
         dashed = new BasicStroke();
         g2.setStroke(dashed);
       }
       if (this.selected) {
         int r = 4;
 
         Color c = g.getColor();
         g.setColor(Color.white);
         g.fillOval(this.x1 - r, this.y1 - r, 2 * r, 2 * r);
         g.fillOval(this.x2 - r, this.y2 - r, 2 * r, 2 * r);
         g.setColor(c);
       }
 
       if (this.state != 2) {
         drawArrow(g, a, b, this.x2, this.y2);
       }
     }
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
 
     Polygon filledPolygon = new Polygon();
     filledPolygon.addPoint(a1, b1);
     filledPolygon.addPoint(x2, y2);
     filledPolygon.addPoint(a2, b2);
     g.fillPolygon(filledPolygon);
   }
   public static void main(String[] args) {
     LinkedList dian = new LinkedList();
 
     Changjing cj = new Changjing(dian, 248, 335, 204, 202);
     if (cj.in(308, 202)) System.out.println(23);
   }

   public void print(){
     System.out.println("from : " + this.getFrom().getName() + this.getFrom().getNumber() + " to : " + this.getTo().getName() + this.getTo().getNumber());
   }

   @Override
   public boolean equals(Object obj) {
     Changjing changjing = (Changjing)obj;
     return (this.getFrom().equals(changjing.getFrom()) && this.getTo().equals(changjing.getTo()) && this.getState() == changjing.getState());
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
     return (Changjing)super.clone();
   }
 }