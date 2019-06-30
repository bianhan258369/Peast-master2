 package Ontology;
 
 import Shape.Diagram;
 import com.hp.hpl.jena.ontology.OntClass;
 import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
 import com.hp.hpl.jena.ontology.OntProperty;
 import com.hp.hpl.jena.ontology.Restriction;
 import com.hp.hpl.jena.rdf.model.InfModel;
 import com.hp.hpl.jena.rdf.model.ModelFactory;
 import com.hp.hpl.jena.rdf.model.RDFNode;
 import com.hp.hpl.jena.rdf.model.StmtIterator;
 import com.hp.hpl.jena.reasoner.Reasoner;
 import com.hp.hpl.jena.reasoner.ReasonerRegistry;
 import com.hp.hpl.jena.reasoner.ValidityReport;
 import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.PrintStream;
import java.util.LinkedList;
 
 public class Ontology
 {
   String uri = "http://www.owl-ontologies.com/Ontology1206426899.owl#";
  
   private String fileName = "a.owl"; 
 
  
   
   OntModel schema = null;
   InfModel model = null;
 
   public Ontology() {
     Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
     this.schema = ModelFactory.createOntologyModel();  
     //File myfile = new File("C:/Users/lyt-/Desktop/animal/animal.owl");///////////
     //File myfile =new File(fileName);///////////////////////////////
     try
     {
       
    	 this.schema.read(new FileInputStream(this.fileName), ""); 
    	 //this.schema.read(new FileInputStream(myfile), "");
    	 
    	 
     }
     catch (FileNotFoundException ex) {
       System.out.println("cuowu");
     }
     this.model = ModelFactory.createInfModel(reasoner, this.schema);
   }
   private LinkedList getThingSub(StmtIterator si) {
     LinkedList ll = new LinkedList();
     LinkedList show = new LinkedList();
     while (si.hasNext()) {
       String s = si.next().toString();
       String name = s.substring(s.indexOf("[") + 1, s.indexOf(","));
       boolean zhaodao = false;
       for (int j = 0; j <= show.size() - 1; j++) {
         ShowLei sl = (ShowLei)show.get(j);
         if (name.equals(sl.name)) {
           sl.num += 1;
           zhaodao = true;
         }
       }
       if (!zhaodao) {
         ShowLei sll = new ShowLei();
         sll.name = s.substring(s.indexOf("[") + 1, s.indexOf(","));
         sll.num = 1;
         show.add(sll);
       }
     }
     for (int i = 0; i <= show.size() - 1; i++) {
       ShowLei sl = (ShowLei)show.get(i);
       if ((sl.num != 1) || 
         (sl.name.indexOf(this.uri) == -1)) continue;
       ll.add(sl.name);
     }
 
     return ll;
   }
   public LinkedList getSubClass(String name) {
     LinkedList ll = new LinkedList();
 
     if (name.equals("Thing")) {
       OntProperty op = this.schema.getOntProperty("http://www.w3.org/2000/01/rdf-schema#subClassOf");
       StmtIterator si = this.schema.listStatements(null, op, (RDFNode)null);
       return getThingSub(si);
     }
     OntClass oc = this.schema.getOntClass(name);
     ExtendedIterator ei = oc.listSubClasses();
     while (ei.hasNext()) {
       String s = ei.next().toString();
       ll.add(s);
     }
 
     return ll;
   }
   public boolean checkConsistence() {
     if (this.model == null) return false;
     ValidityReport validity = this.model.validate();
     return validity.isClean();
   }
 
   public boolean check_min(Restriction res, Diagram dd)
   {
     OntProperty op = res.getOnProperty();
     if ((op.getLocalName().equals("hasMachine")) && 
       (dd.hasMachine() >= 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasInterface")) && 
       (dd.hasInterface() >= 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasProDom")) && 
       (dd.hasProDom() >= 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasReqCon")) && 
       (dd.hasReqCon() >= 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasReqRef")) && 
       (dd.hasReqRef() >= 1)) {
       return true;
     }
 
     return (op.getLocalName().equals("hasReq")) && 
       (dd.hasReq() >= 1);
   }
 
   public boolean check_max(Restriction res, Diagram dd)
   {
     OntProperty op = res.getOnProperty();
     if ((op.getLocalName().equals("hasMachine")) && 
       (dd.hasMachine() <= 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasInterface")) && 
       (dd.hasInterface() <= 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasProDom")) && 
       (dd.hasProDom() <= 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasReqCon")) && 
       (dd.hasReqCon() <= 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasReqRef")) && 
       (dd.hasReqRef() <= 1)) {
       return true;
     }
 
     return (op.getLocalName().equals("hasReq")) && 
       (dd.hasReq() <= 1);
   }
 
   public boolean check_card(Restriction res, Diagram dd)
   {
     OntProperty op = res.getOnProperty();
     if ((op.getLocalName().equals("hasMachine")) && 
       (dd.hasMachine() == 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasInterface")) && 
       (dd.hasInterface() == 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasProDom")) && 
       (dd.hasProDom() == 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasReqCon")) && 
       (dd.hasReqCon() == 1)) {
       return true;
     }
 
     if ((op.getLocalName().equals("hasReqRef")) && 
       (dd.hasReqRef() == 1)) {
       return true;
     }
 
     return (op.getLocalName().equals("hasReq")) && 
       (dd.hasReq() == 1);
   }
 
   class ShowLei
   {
     String name;
     int num;
 
     ShowLei()
     {
     }
   }
 }