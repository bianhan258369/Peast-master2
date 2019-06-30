 package Shape;
 
 import java.awt.Graphics;
 import java.io.Serializable;
 
 public abstract class Shape
   implements Serializable
 {
   public boolean selected = false;
   public int shape;
   public int des = 0;
 
   public abstract void draw(Graphics paramGraphics);
 
   public abstract boolean isIn(int paramInt1, int paramInt2);
 
   public abstract void moveTo(int paramInt1, int paramInt2);
 }