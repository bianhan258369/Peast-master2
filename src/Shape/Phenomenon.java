 package Shape;
 
 import java.io.Serializable;
 
 public class Phenomenon
   implements Serializable
 {
   private String name;
   private String state;
   private Rect from;
   private Rect to;
   private boolean constraining = false;
   private Oval requirement;
   private int biaohao;
 
   public void setName(String name)
   {
     this.name = name;
   }
   public String getName() {
     return this.name;
   }
   public int getBiaohao() {
     return this.biaohao;
   }
   public void setBiaohao() {
     this.biaohao = this.biaohao;
   }
   public void setBiaohao(int biaohao) {
     this.biaohao = biaohao;
   }
   public void setState(String state) {
     this.state = state;
   }
   public String getState() {
     return this.state;
   }
   public Phenomenon(String name, String state, Rect from, Rect to) {
     this.name = name;
     this.state = state;
     this.from = from;
     this.to = to;
     this.biaohao = Data.first;
     Data.first += 1;
   }
   public Rect getFrom() {
     return this.from;
   }
   public Rect getTo() {
     return this.to;
   }
   public void setConstraining(boolean t) {
     this.constraining = t;
   }
   public boolean getConstraining() {
     return this.constraining;
   }
   public void setRequirement(Oval o) {
     this.requirement = o;
   }
   public Oval getRequirement() {
     return this.requirement;
   }
 }