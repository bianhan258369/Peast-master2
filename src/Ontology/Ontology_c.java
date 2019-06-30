 package Ontology;
 
 import Shape.Data;
 import Shape.Diagram;
 import Shape.Line;
 import Shape.Rect;
 import Shape.Shape;
 import com.hp.hpl.jena.ontology.Individual;
 import com.hp.hpl.jena.ontology.OntClass;
 import com.hp.hpl.jena.ontology.OntModel;
 import com.hp.hpl.jena.ontology.OntProperty;
 import com.hp.hpl.jena.ontology.Restriction;
 import com.hp.hpl.jena.ontology.UnionClass;
 import com.hp.hpl.jena.rdf.model.InfModel;
 import com.hp.hpl.jena.rdf.model.Property;
 import java.io.PrintStream;
 import java.util.Iterator;
 import java.util.LinkedList;
 
 public class Ontology_c extends Ontology
 {
   LinkedList not;
   LinkedList yes;
 
   public Ontology_c()
   {
     this.not = new LinkedList();
     this.yes = new LinkedList();
   }
 
   private void smooth() {
     this.not = new LinkedList();
     this.yes = new LinkedList();
   }
   public boolean canAdd(String name) {
     for (int i = 0; i < this.not.size(); i++) {
       String s = (String)this.not.get(i);
       if (s.equals(name)) {
         return false;
       }
     }
     return true;
   }
 
   public void deel(String name) {
     OntClass tmp = this.schema.getOntClass(this.uri + name);
     for (Iterator i = tmp.listSuperClasses(); i.hasNext(); ) {
       OntClass tmp_c = (OntClass)i.next();
 
       if (tmp_c.isRestriction()) {
         Restriction tmp_r = tmp_c.asRestriction();
         OntProperty tmp_p = tmp_r.getOnProperty();
         boolean tiao = false;
         if ((tmp_r.isCardinalityRestriction()) && (name.equals("ContextDiagram"))) {
           for (Iterator j = tmp_p.listRange(); j.hasNext(); ) {
             OntClass dom = (OntClass)j.next();
             for (int ii = 0; ii < this.yes.size(); ii++) {
               if (dom.getLocalName().equals((String)this.yes.get(ii))) {
                 tiao = true;
                 break;
               }
             }
           }
           if (tiao) {
             continue;
           }
         }
         for (Iterator j = tmp_p.listRange(); j.hasNext(); ) {
           OntClass dom = (OntClass)j.next();
 
           if (dom.isUnionClass()) {
             for (Iterator ii = dom.asUnionClass().listOperands(); ii.hasNext(); ) {
               OntClass tmp_oc = (OntClass)ii.next();
               if (canAdd(tmp_oc.getLocalName())) {
                 deel(tmp_oc.getLocalName());
                 break;
               }
             }
 
           }
           else if (canAdd(dom.getLocalName())) {
             deel(dom.getLocalName());
             if ((!tmp_r.isCardinalityRestriction()) || (!name.equals("ContextDiagram")))
               break;
             this.not.add(dom.getLocalName()); break;
           }
 
         }
 
       }
 
     }
 
     this.yes.add(name);
   }
 
   private Diagram distill() {
     Diagram dd = new Diagram("");
     int num = 0;
     for (int i = 0; i < this.yes.size(); i++) {
       if (((String)this.yes.get(i)).equals("Machine")) {
         Rect tmp_rect = new Rect(100, 155);
         tmp_rect.setState(2);
         tmp_rect.setText(Data.M_TEXT);
         dd.add(tmp_rect);
       }
 
       if ((((String)this.yes.get(i)).equals("GivenDomain")) || (((String)this.yes.get(i)).equals("DesignedDomain")))
       {
         Rect tmp_rect = new Rect(300 + num, 20 + num * 150);
         num++;
         tmp_rect.setState(0);
         tmp_rect.setText(Data.DD_TEXT);
         dd.add(tmp_rect);
       }
       if (((String)this.yes.get(i)).equals("Interface")) {
         dd.addInterface(0);
       }
     }
     dd.rule();
     return dd;
   }
   public Diagram getTemplate() {
     deel("ContextDiagram");
     Diagram dd = distill();
     smooth();
     return dd;
   }
   public void display() {
     for (int i = 0; i < this.yes.size(); i++)
       System.out.println((String)this.yes.get(i));
   }
 
   public boolean check(Diagram dd, String diagram)
   {
	  
     OntClass contextDiagram = this.schema.getOntClass(this.uri + diagram);
     for (Iterator i = contextDiagram.listSuperClasses(); i.hasNext(); ) {  //listSuperClasses
       OntClass tmp_class = (OntClass)i.next();
       if (tmp_class.isRestriction()) {
         Restriction tmp_res = tmp_class.asRestriction();
         if ((tmp_res.isSomeValuesFromRestriction()) && 
           (!check_min(tmp_res, dd))) return false;
 
         if ((tmp_res.isMinCardinalityRestriction()) && 
           (!check_min(tmp_res, dd))) return false;
 
         if ((tmp_res.isMaxCardinalityRestriction()) && 
           (!check_max(tmp_res, dd))) return false;
 
         if ((tmp_res.isCardinalityRestriction()) && 
           (!check_card(tmp_res, dd))) return false;
       }
 
       if (tmp_class.isUnionClass()) {
         UnionClass uc = tmp_class.asUnionClass();
         boolean shi = false;
         for (Iterator j = uc.listOperands(); j.hasNext(); ) {
           OntClass oc = (OntClass)j.next();
           if (oc.isRestriction()) {
             Restriction tmp_res = oc.asRestriction();
             if (tmp_res.isSomeValuesFromRestriction()) {
               shi |= check_min(tmp_res, dd);
             }
             if (tmp_res.isMinCardinalityRestriction()) {
               shi = (shi) || (check_min(tmp_res, dd));
             }
             if (tmp_res.isMaxCardinalityRestriction()) {
               shi = (shi) || (check_max(tmp_res, dd));
             }
             if (tmp_res.isCardinalityRestriction()) {
               shi = (shi) || (check_card(tmp_res, dd));
             }
           }
         }
         if (!shi) return false;
       }
     }
     Individual tmp_i = null;
     for (Iterator it = this.schema.listIndividuals(); it.hasNext(); ) {
       tmp_i = (Individual)it.next();
       String name = tmp_i.getLocalName();
       if (name.startsWith("r")) break;
     }
     if (tmp_i != null) {
       Iterator tm = this.model.listStatements(tmp_i, (Property)null, (Property)null);
       if (tm.hasNext()) {
         for (int i = 0; i < dd.components.size(); i++) {
           Shape tmp_s = (Shape)dd.components.get(i);
           if (tmp_s.shape == 2) {
             Line tmp_l = (Line)tmp_s;
             if (tmp_l.from.equals(tmp_l.to)) {
               return false;
             }
           }
         }
       }
     }
 
     return true;
   }
 
   public void test()
   {
   }
 
   public static void main(String[] args) {
     Ontology_c ont = new Ontology_c();
     ont.test();
   }
 }