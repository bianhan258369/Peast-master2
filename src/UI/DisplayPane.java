 package UI;
 
 import Shape.Diagram;
 import Shape.IntDiagram;
 import java.awt.BorderLayout;
 import java.awt.Component;
 import javax.swing.JPanel;
 import javax.swing.JTabbedPane;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 
 public class DisplayPane extends JPanel
   implements ChangeListener
 {
   public JTabbedPane desk = new JTabbedPane();
   BorderLayout borderLayout1 = new BorderLayout();
 
   public DisplayPane() { add(this.desk);
     try {
       jbInit();
     }
     catch (Exception e) {
       e.printStackTrace();
     } }
 
   public FatherPane getMyPane(String s)
   {
     Component[] k = this.desk.getComponents();
     for (int i = 0; i <= k.length - 1; i++) {
       FatherPane kk = (FatherPane)k[i];
       /********************************************************************/
       if(kk.type==1&&s.contains("TD:")){
          return kk;
       }
       /********************************************************************/
       if ((kk.state == 0) && 
         (((MyPane)kk).dd.getTitle().equals(s))) {
         return kk;
       }
 
       if ((kk.state == 1) && 
         (((IntPane)kk).dd.getTitle().equals(s))) {
         return kk;
       }
     }
 
     return null;
   }
 
   public void setSelected(FatherPane pp) {
     this.desk.setSelectedComponent(pp);
   }
 
   public Diagram getNowDiagram() {
     MyPane aa = (MyPane)this.desk.getSelectedComponent();
     return aa.dd;
   }
 
   public void stateChanged(ChangeEvent e)
   {
     FatherPane tmp_mp = (FatherPane)this.desk.getSelectedComponent();
     if(tmp_mp instanceof InstantPane){
       Main.win.instantPane = (InstantPane) tmp_mp;
     }
     if (tmp_mp == null) {
       return;
     }
     
     if (tmp_mp.state == 0) {
       if (tmp_mp == null) {
         return;
       }
       if (Main.win == null) {
         return;
       }
       
       /********************************************************************/
       if(tmp_mp.type!=1){
    	   Main.win.myInfoPane.setDescription(((MyPane)tmp_mp).dd.getInteractionDescription());
       }
       if(tmp_mp instanceof InstantPane){
           String s = ((InstantPane)tmp_mp).getTitle();
           int index = Integer.parseInt(s.substring(2, s.indexOf(":")));
           Main.win.myInfoPane.setDescription(Main.win.subProblemDiagrams[index - 1].getInteractionDescription());
       }
       /********************************************************************/
 
       Main.win.nowIntPane = null;
       MyPane mp=null;//= (MyPane)tmp_mp
       /********************************************************************/
       if(tmp_mp.type!=1){
         mp = (MyPane)tmp_mp;
         if(Main.win.clockDiagrams[0] != null){
           int cdIndex = 1;
           if(!mp.dd.getTitle().equals("ClockDiagram"))
             cdIndex = Integer.parseInt(mp.dd.getTitle().substring(12));//The length of String "ClockDiagram" is 11 so the index starts from the 12th char
           //Main.win.myProblemDiagram = Main.win.subProblemDiagrams[cdIndex - 1];
           Main.win.cd = Main.win.clockDiagrams[cdIndex - 1];
         }
       }

       /********************************************************************/
       
       if (mp!=null&&(mp.dd.getTitle().equals("ContextDiagram")) && //mp!=null�Ǻ����ӵ�
         (Main.win.myProblemDiagram != null)) {
         Diagram tmp_d = Main.win.myProblemDiagram.refreshContextDiagram();
         mp.dd = tmp_d;
         Main.win.myContextDiagram = tmp_d;
         Main.win.repaint();
         Main.win.myInfoPane.setDescription(Main.win.myContextDiagram.getInteractionDescription());
         Main.win.buttonClear();
       }
     }
 
     if (tmp_mp.state == 1)
       Main.win.nowIntPane = ((IntPane)tmp_mp);
   }
 
   public void setState(int state)
   {
     MyPane tmp = (MyPane)this.desk.getSelectedComponent();
     tmp.setState(state);
   }
 
   public void addPane(JPanel jp, String title) {
     this.desk.add(title, jp);
     this.desk.setSelectedComponent(jp);
   }
 
   public static void main(String[] args) {
   }
 
   private void jbInit() throws Exception {
     setLayout(this.borderLayout1);
     add(this.desk, "Center");
     this.desk.addChangeListener(this);
   }
 }