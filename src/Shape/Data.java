 package Shape;
 
 public class Data
 {
   public static String GD_TEXT = "?ProblemDomain";
   public static String M_TEXT = "?Machine";
   public static String I_TEXT = "?Interface";
   public static String R_TEXT = "?Requirement";
   public static String DD_TEXT = "?GivenDomain";
   public static String RR_TEXT = "?ReqRef";
   public static String RC_TEXT = "?ReqCon";
   public static float[] LENGTHOFDASH = { 5.0F };
 
   public static int WIDE = 10;
   public static int first = 1;
   public static int firstq = 1;
 
   public static boolean same(Shape a, Shape b)
   {
     if (a.shape != b.shape) {
       return false;
     }
     if (a.shape == 0) {
       Rect tmpa = (Rect)a;
       Rect tmpb = (Rect)b;
       String s1 = tmpa.getText();
       String s2 = tmpb.getText();
       if ((tmpa.state != tmpb.state) || (!tmpa.getText().equals(tmpb.getText()))) {
         return false;
       }
     }
     if (a.shape == 1) {
       Oval tmpa = (Oval)a;
       Oval tmpb = (Oval)b;
       if (!tmpa.getText().equals(tmpb.getText())) {
         return false;
       }
     }
     return true;
   }
   public static boolean same(Phenomenon a, Phenomenon b) {
     if (a.getBiaohao() != b.getBiaohao()) {
       return false;
     }
     if (!a.getName().equals(b.getName())) {
       return false;
     }
 
     return a.getState().equals(b.getState());
   }
 }