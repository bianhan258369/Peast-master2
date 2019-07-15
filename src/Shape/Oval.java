 package Shape;
 
 import java.awt.BasicStroke;
 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 
 public class Oval extends Shape
 {
   private String text;
   protected int x1;
   protected int y1;
   protected int x2;
   protected int y2;
   private int length;
   public int des;
   private int biaohao;
 
   public int getBiaohao()
   {
     return this.biaohao;
   }
   public void setBiaohao(int biaohao) {
     this.biaohao = biaohao;
   }
   public Oval(int middlex, int middley) {
     this.shape = 1;
     this.text = Data.R_TEXT;
     setSize(middlex, middley);
     this.des = 2;
     this.biaohao = Data.firstq;
     Data.firstq += 1;
   }
 
   public String getText() {
     return this.text;
   }
 
   public void draw(Graphics g)
   {
     if (this.selected) {
       g.setColor(Color.red);
     }
 
     Graphics2D g2 = (Graphics2D)g;
     BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F, Data.LENGTHOFDASH, 0.0F);
 
     g2.setStroke(dashed);
     g2.drawOval(this.x1, this.y1, this.x2, this.y2);
     dashed = new BasicStroke();
     g2.setStroke(dashed);
 
     if (this.des == 0) {
       g.drawString(Data.R_TEXT, this.x1 + 5, this.y1 + this.y2 / 2 + 5);
     }
     else {
       g.drawString("req" + this.biaohao, this.x1 + 5, this.y1 + this.y2 / 2 + 5);
     }
     if (this.selected)
       g.setColor(Color.black);
   }
 
   public boolean isIn(int x, int y)
   {
     int tmp1 = (int)Math.pow(x - (this.x1 + this.x2 / 2), 2.0D) / (int)Math.pow(this.x2 / 2.0D, 2.0D);
 
     int tmp2 = (int)Math.pow(y - (this.y1 + this.y2 / 2), 2.0D) / (int)Math.pow(this.y2 / 2.0D, 2.0D);
 
     return tmp1 + tmp2 <= 1;
   }
 
   private void setSize(int middlex, int middley)
   {
     this.length = (this.text.length() * 7 + 25);
     this.length = 40;
     this.x2 = this.length;
     this.y2 = 50;
     this.x1 = (middlex - this.x2 / 2);
     this.y1 = (middley - this.y2 / 2);
   }
 
   public void moveTo(int x, int y) {
     this.x1 += x;
     this.y1 += y;
   }
 
   public void setText(String text) {
     this.text = text;
     setSize(this.x1 + this.x2 / 2, this.y1 + this.y2 / 2);
     this.des = 1;
   }

   @Override
   public boolean equals(Object obj) {
     if(obj instanceof Oval){
       Oval temp = (Oval)obj;
       return temp.getText().equals(this.getText());
     }
     return false;
   }

   @Override
   public int hashCode() {
     return getText().hashCode() * 31;
   }
 }