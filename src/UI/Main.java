package UI;

import Shape.*;
import Shape.Shape;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
import jdk.internal.util.xml.XMLStreamException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import sun.awt.image.ImageWatched;
import util.OWLFileFilter;
import util.XMLFileFilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;

public class Main extends JFrame implements ActionListener {
	public static String errmes = "";
	public static int errstate = 0;// 0û���� 1�ߴ��� 2���󽻻�����
	private int buttonState;
	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu help = new JMenu("Help");
	JMenu ontology = new JMenu("Ontology");
	JMenu constraint = new JMenu("Constraint");
	JMenuItem save = new JMenuItem("Save");
	JMenuItem open = new JMenuItem("Open");
	JMenuItem news = new JMenuItem("New");
	JMenuItem exit = new JMenuItem("Exit");
	JMenuItem load = new JMenuItem("Load");
	JMenuItem check = new JMenuItem("Check");
	JMenuItem show = new JMenuItem("Show");
	JMenuItem about = new JMenuItem("About");
	JMenuItem add = new JMenuItem("Add Clock Constraint");
	//JMenuItem combine = new JMenuItem("Combine Clock");
	JMenuItem createTxt = new JMenuItem("Export Relations");
	DrawPane myDrawPane = new DrawPane();
	InfoPane myInfoPane = new InfoPane();
	public IntPane nowIntPane;
	public ClockDiagram myClockDiagram;          /////////////
	public LinkedList intPane_l = new LinkedList();
	public LinkedList subpd_l = new LinkedList();
	/*************************************************************************/
	ClockDiagram cd;
	ClockDiagram[] clockDiagrams;
	public LinkedList<IntDiagram> myIntDiagram = new LinkedList<IntDiagram>();
	public InstantPane instantPane = null;
	public LinkedList<InstantPane> instantPanes = new LinkedList<>();
	/*************************************************************************/
	DisplayPane myDisplayPane = new DisplayPane();
	JSplitPane all = new JSplitPane();
	JSplitPane rightp = new JSplitPane();
	static Main win;
	JFileChooser chooser;
	String nameOfProject;
	static String pathOfProject;
	public Diagram myContextDiagram;
	public Diagram myProblemDiagram;
	public Diagram[] subProblemDiagrams;

	public static void main(String[] args) {
		try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {
			// TODO exception
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException ex) {
		} catch (IllegalAccessException ex) {
		} catch (InstantiationException ex) {
		} catch (ClassNotFoundException ex) {
		}
		win = new Main();
		win.setVisible(true);
		win.setVisible(false);
		new WelcomeWindow().open();
		win.setVisible(true);
		win.setExtendedState(6);
		win.addWindowListener(new MyAdapter(win));
		win.constraint.setEnabled(false);
	}

	private void clear() {
		this.intPane_l = new LinkedList();
		this.subpd_l = new LinkedList();
		errmes = "";
		errstate = 0;
		this.myContextDiagram = null;
		this.myProblemDiagram = null;
		this.nowIntPane = null;
		this.myDisplayPane.desk.removeAll();
		this.myInfoPane.desk.removeAll();
	}


    private void loadProjectXML() throws DocumentException {
		if(myDrawPane.getI() == 0) clear();
		//this.myDrawPane.setState(0);
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Load Project from XML File");
        jFileChooser.addChoosableFileFilter(new XMLFileFilter());
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jFileChooser.showDialog(null,null);
        File file = jFileChooser.getSelectedFile();
        if(!file.exists()) JOptionPane.showMessageDialog(null,"this file does not exist!","Error",JOptionPane.ERROR_MESSAGE);
		if(!Diagram.hasSub(file.getPath())){
			this.myProblemDiagram = new Diagram("ProblemDiagram",file);
			cd = new ClockDiagram(this.myProblemDiagram);
			//this.myDisplayPane.addPane(new MyPane(this.myProblemDiagram),this.myProblemDiagram.getTitle());
			this.myDisplayPane.addPane(new MyPane(cd),cd.getTitle());
			int count = 1;
			SAXReader saxReader = new SAXReader();
			String path = Diagram.getFilePath(file.getPath());
			Document project = saxReader.read(file);
			Element rootElement = project.getRootElement();
			Element filelist = rootElement.elementIterator("filelist").next();
			Element senList = filelist.elementIterator("SenarioFilelist").next();
			for(Iterator it = senList.elementIterator("SenarioDiagram");it.hasNext();){
				Element sd = (Element) it.next();
				String sdPath = Diagram.getFilePath(file.getPath()) + sd.getText()+".xml";
				File sdFile = new File(sdPath);
				IntDiagram intDiagram = new IntDiagram("SenarioDiagram" + count,count,sdFile);
				this.myIntDiagram.add(intDiagram);
				//IntPane tempIntPane = new IntPane(intDiagram, 1,myProblemDiagram);
				//this.myDisplayPane.addPane(tempIntPane,intDiagram.getTitle());
				count++;
			}
			this.myInfoPane.treeInit();
			for(int i = 0;i < cd.getClocks().size();i++){
				try{
					InstantGraph instantGraph = new InstantGraph((Rect) this.myProblemDiagram.getProblemDomains().get(i),cd.getClocks().get(i));
					if(instantPane == null) instantPane = new InstantPane(instantGraph);
					else instantPane.addGraph(instantGraph);
				}
				catch (Exception e){
					e.printStackTrace();
				}
			}
			this.myDisplayPane.addPane(instantPane,"InstantGraph");
		}
		else{
			int count = 1;
			SAXReader saxReader = new SAXReader();
			String path = Diagram.getFilePath(file.getPath());
			Document project = saxReader.read(file);
			Element rootElement = project.getRootElement();
			Element filelist = rootElement.elementIterator("filelist").next();
			Element subList = filelist.elementIterator("SubProblemDiagramList").next();
			for(Iterator it = subList.elementIterator("SubProblemDiagram");it.hasNext();){
				Diagram diagram = new Diagram("SubProblemDiagram" + count);
				diagram.components = new LinkedList();
				Element spd = (Element) it.next();
				String spdPath = Diagram.getFilePath(file.getPath()) + spd.getText()+".xml";
				Document subProDiagram = saxReader.read(spdPath);
				Element root = subProDiagram.getRootElement();
				Element spdRoot = (Element) root.elementIterator("data").next();
				Element temp;
				for(Iterator i = spdRoot.elementIterator("Machine");i.hasNext();){
					temp = (Element)i.next();
					String str = temp.attributeValue("machine_locality");
					String[] locality = str.split(",");
					int x1 = Integer.parseInt(locality[0]);
					int y1 = Integer.parseInt(locality[1]);
					int x2 = Integer.parseInt(locality[2]);
					int y2 = Integer.parseInt(locality[3]);
					Rect rect = new Rect(x1 + x2 / 2, y1 + y2 / 2);
					rect.setText(temp.attributeValue("machine_name"));
					rect.setShortName(temp.attributeValue("machine_shortname"));
					rect.setState(Integer.parseInt(temp.attributeValue("machine_state")));
					diagram.components.add(rect);
				}

				Element requirement = (Element) spdRoot.elementIterator("Requirement").next();
				for(Iterator i = requirement.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String str = temp.attributeValue("requirement_locality");
					String[] locality = str.split(",");
					int x1 = Integer.parseInt(locality[0]);
					int y1 = Integer.parseInt(locality[1]);
					int x2 = Integer.parseInt(locality[2]);
					int y2 = Integer.parseInt(locality[3]);
					Oval oval = new Oval(x1 + x2 / 2, y1 + y2 / 2);
					oval.setText(temp.attributeValue("requirement_text"));
					oval.des = Integer.parseInt(temp.attributeValue("requirement_des"));
					oval.setBiaohao(Integer.parseInt(temp.attributeValue("requirement_biaohao")));
					diagram.components.add(oval);
				}

				Element problemDomain = (Element) spdRoot.elementIterator("Problemdomain").next();
				for(Iterator i = problemDomain.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String str = temp.attributeValue("problemdomain_locality");
					String[] locality = str.split(",");
					int x1 = Integer.parseInt(locality[0]);
					int y1 = Integer.parseInt(locality[1]);
					int x2 = Integer.parseInt(locality[2]);
					int y2 = Integer.parseInt(locality[3]);
					Rect rect = new Rect(x1 + x2 / 2, y1 + y2 / 2);
					rect.setText(temp.attributeValue("problemdomain_name"));
					rect.setShortName(temp.attributeValue("problemdomain_shortname"));
					rect.setState(Integer.parseInt(temp.attributeValue("problemdomain_state")));
					rect.setCxb(temp.attributeValue("problemdomain_cxb").charAt(0));
					diagram.components.add(rect);
				}

				Element Interface = (Element) spdRoot.elementIterator("Interface").next();
				for(Iterator i = Interface.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String name = temp.attributeValue("line1_name");
					String str = temp.attributeValue("line1_tofrom");
					String[] locality = str.split(",");
					String to = locality[0];
					String from = locality[1];
					Shape toShape = null;
					Shape fromShape = null;
					for(int j = 0;j < diagram.components.size();j++){
						Shape tempShape = (Shape)diagram.components.get(j);
						if(tempShape instanceof Rect){
							Rect tempRect = (Rect)tempShape;
							if(tempRect.getState() != 2 && tempRect.getShortName().equals(to)){
								toShape = tempRect;
							}
							if(tempRect.getState() == 2 && tempRect.getShortName().equals(from)){
								fromShape = tempRect;
							}
						}
					}
					Line line = new Line(fromShape, toShape, 0);
					line.name = name;
					Element tempPhenomenon;
					for(Iterator j = temp.elementIterator("Phenomenon");j.hasNext();){
						tempPhenomenon = (Element)j.next();
						String phenomenonName = tempPhenomenon.attributeValue("name");
						String phenomenonState = tempPhenomenon.attributeValue("state");
						String phenomenonFrom = tempPhenomenon.attributeValue("from");
						String phenomenonTo = tempPhenomenon.attributeValue("to");
						Rect phenomenonFromRect = null;
						Rect phenomenonToRect = null;
						for(int k = 0;k < diagram.components.size();k++){
							Shape tempShape = (Shape)diagram.components.get(k);
							if(tempShape instanceof Rect){
								Rect tempRect = (Rect)tempShape;
								if(tempRect.getText().equals(phenomenonFrom)) phenomenonFromRect = tempRect;
								if(tempRect.getText().equals(phenomenonTo)) phenomenonToRect = tempRect;
							}
						}
						boolean phenomenonConstraining = (tempPhenomenon.attributeValue("constraining")).equals("True") ? true : false;
						int phenomenonBiaohao = Integer.parseInt(tempPhenomenon.attributeValue("biaohao"));
						Phenomenon phenomenon = new Phenomenon(phenomenonName, phenomenonState, phenomenonFromRect, phenomenonToRect);
						phenomenon.setConstraining(phenomenonConstraining);
						phenomenon.setBiaohao(phenomenonBiaohao);
						line.phenomenons.add(phenomenon);
					}
					diagram.components.add(line);
				}

				Element reference = spdRoot.elementIterator("Reference").next();
				for(Iterator i = reference.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String name = temp.attributeValue("line2_name");
					String str = temp.attributeValue("line2_tofrom");
					String[] locality = str.split(",");
					String to = locality[0];
					String from = locality[1];
					Shape toShape = null;
					Shape fromShape = null;
					for(int j = 0;j < diagram.components.size();j++){
						Shape tempShape = (Shape)diagram.components.get(j);
						if(tempShape instanceof Rect){
							Rect tempRect = (Rect)tempShape;
							if(tempRect.getState() != 2 && tempRect.getShortName().equals(to)){
								toShape = tempRect;
							}
						}
						if(tempShape instanceof Oval){
							Oval tempOval = (Oval)tempShape;
							if(tempOval.getText().equals(from)){
								fromShape = tempOval;
							}
						}
					}
					Line line = new Line(fromShape, toShape, 1);
					line.name = name;
					Element tempPhenomenon;
					for(Iterator j = temp.elementIterator("Phenomenon");j.hasNext();){
						tempPhenomenon = (Element)j.next();
						String phenomenonName = tempPhenomenon.attributeValue("name");
						String phenomenonState = tempPhenomenon.attributeValue("state");
						String phenomenonFrom = tempPhenomenon.attributeValue("from");
						String phenomenonTo = tempPhenomenon.attributeValue("to");
						int pehnomenonRequirementBiaohao = Integer.parseInt(tempPhenomenon.attributeValue("requirement"));
						boolean phenomenonConstraining = (tempPhenomenon.attributeValue("constraining")).equals("True") ? true : false;
						int phenomenonBiaohao = Integer.parseInt(tempPhenomenon.attributeValue("biaohao"));
						Rect phenomenonFromRect = null;
						Rect phenomenonToRect = null;
						for(int k = 0;k < diagram.components.size();k++){
							Shape tempShape = (Shape)diagram.components.get(k);
							if(tempShape instanceof Rect){
								Rect tempRect = (Rect)tempShape;
								if(tempRect.getText().equals(phenomenonFrom)) phenomenonFromRect = tempRect;
								if(tempRect.getText().equals(phenomenonTo)) phenomenonToRect = tempRect;
							}
						}
						Oval oval = diagram.getRequirement(pehnomenonRequirementBiaohao);
						Phenomenon phenomenon = new Phenomenon(phenomenonName, phenomenonState, phenomenonFromRect, phenomenonToRect);
						phenomenon.setConstraining(phenomenonConstraining);
						phenomenon.setBiaohao(phenomenonBiaohao);
						phenomenon.setRequirement(oval);
						line.phenomenons.add(phenomenon);
					}
					diagram.components.add(line);
				}

				Element constraint = (Element) spdRoot.elementIterator("Constraint").next();
				for(Iterator i = constraint.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String name = temp.attributeValue("line2_name");
					String str = temp.attributeValue("line2_tofrom");
					String[] locality = str.split(",");
					String to = locality[0];
					String from = locality[1];
					Shape toShape = null;
					Shape fromShape = null;
					for(int j = 0;j < diagram.components.size();j++){
						Shape tempShape = (Shape)diagram.components.get(j);
						if(tempShape instanceof Rect){
							Rect tempRect = (Rect)tempShape;
							if(tempRect.getState() != 2 && tempRect.getShortName().equals(to)){
								toShape = tempRect;
							}
						}
						if(tempShape instanceof Oval){
							Oval tempOval = (Oval)tempShape;
							if(tempOval.getText().equals(from)){
								fromShape = tempOval;
							}
						}
					}
					Line line = new Line(fromShape, toShape, 2);
					line.name = name;
					Element tempPhenomenon;
					for(Iterator j = temp.elementIterator("Phenomenon");j.hasNext();){
						tempPhenomenon = (Element)j.next();
						String phenomenonName = tempPhenomenon.attributeValue("name");
						String phenomenonState = tempPhenomenon.attributeValue("state");
						String phenomenonFrom = tempPhenomenon.attributeValue("from");
						String phenomenonTo = tempPhenomenon.attributeValue("to");
						int pehnomenonRequirementBiaohao = Integer.parseInt(tempPhenomenon.attributeValue("requirement"));
						boolean phenomenonConstraining = (tempPhenomenon.attributeValue("constraining")).equals("True") ? true : false;
						int phenomenonBiaohao = Integer.parseInt(tempPhenomenon.attributeValue("biaohao"));
						Rect phenomenonFromRect = null;
						Rect phenomenonToRect = null;
						for(int k = 0;k < diagram.components.size();k++){
							Shape tempShape = (Shape)diagram.components.get(k);
							if(tempShape instanceof Rect){
								Rect tempRect = (Rect)tempShape;
								if(tempRect.getText().equals(phenomenonFrom)) phenomenonFromRect = tempRect;
								if(tempRect.getText().equals(phenomenonTo)) phenomenonToRect = tempRect;
							}
						}
						Oval oval = diagram.getRequirement(pehnomenonRequirementBiaohao);
						Phenomenon phenomenon = new Phenomenon(phenomenonName, phenomenonState, phenomenonFromRect, phenomenonToRect);
						phenomenon.setConstraining(phenomenonConstraining);
						phenomenon.setBiaohao(phenomenonBiaohao);
						phenomenon.setRequirement(oval);
						line.phenomenons.add(phenomenon);
					}
					diagram.components.add(line);
				}
				subProblemDiagrams[count - 1] = diagram;
				clockDiagrams[count - 1] = new ClockDiagram(diagram);
				clockDiagrams[count - 1].setTitle("ClockDiagram" + count);
				//clockDiagrams[count - 1].setTitle("SCD" + count + ":" + ((Oval)clockDiagrams[count - 1].getRequirements().get(0)).getText());
				this.myDisplayPane.addPane(new MyPane(clockDiagrams[count - 1]), "SCD" + count + ":" + ((Oval)clockDiagrams[count - 1].getRequirements().get(0)).getText());
				count++;
			}
			this.myProblemDiagram = new Diagram("ProblemDiagram",file);
			int senCount = 1;
			Element senList = filelist.elementIterator("SenarioFilelist").next();
			for(Iterator it = senList.elementIterator("SenarioDiagram");it.hasNext();){
				Element sd = (Element) it.next();
				String sdPath = Diagram.getFilePath(file.getPath()) + sd.getText()+".xml";
				File sdFile = new File(sdPath);
				IntDiagram intDiagram = new IntDiagram("SenarioDiagram" + senCount,senCount,sdFile);
				this.myIntDiagram.add(intDiagram);
				IntPane tempIntPane = new IntPane(intDiagram, 1,myProblemDiagram);
				this.myDisplayPane.addPane(tempIntPane,intDiagram.getTitle());
				senCount++;
			}
			this.myInfoPane.treeInit();
			for(int i = 0;i < count - 1;i++){
				for(int j = 0;j < subProblemDiagrams[i].getProblemDomains().size();j++){
					try{
						Rect problemDomain = (Rect) subProblemDiagrams[i].getProblemDomains().get(j);
						InstantGraph instantGraph = new InstantGraph(problemDomain, clockDiagrams[i].getClocks().get(j),i);
						if(instantPanes.size() < i + 1) instantPanes.add(new InstantPane(instantGraph));
						else instantPanes.get(i).addGraph(instantGraph);
					}
					catch (Exception e){
						e.printStackTrace();
					}
				}
				instantPanes.get(i).setTitle("TD" + (i + 1) + ":" + ((Oval)subProblemDiagrams[i].getRequirements().get(0)).getText());
				this.myDisplayPane.addPane(instantPanes.get(i),"TD" + (i + 1) + ":" + ((Oval)subProblemDiagrams[i].getRequirements().get(0)).getText());
			}
		}
    }

    public void loadOWLFile() throws FileNotFoundException {
		Map<String, Rect> domains = new HashMap<>();
		Map<String, Phenomenon> phenomena = new HashMap<>();
		LinkedList<StateMachine> stateMachines = new LinkedList<>();
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setDialogTitle("Load Ontology File");
		jFileChooser.addChoosableFileFilter(new OWLFileFilter());
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jFileChooser.showDialog(null,null);
		File file = jFileChooser.getSelectedFile();
		for(int i = 0;i < myIntDiagram.size();i++){
			Diagram diagram = subProblemDiagrams[i];
			IntDiagram intDiagram = myIntDiagram.get(i);
			for(int j = 0;j < diagram.getProblemDomains().size();j++){
				domains.put(((Rect) diagram.getProblemDomains().get(j)).getText(),(Rect) diagram.getProblemDomains().get(j));
			}
			for(int j = 0;j < diagram.getPhenomenon().size();j++){
				phenomena.put(((Phenomenon) diagram.getPhenomenon().get(j)).getName(),(Phenomenon) diagram.getPhenomenon().get(j));
			}
		}
		if(!file.exists()) JOptionPane.showMessageDialog(null,"this file does not exist!","Error",JOptionPane.ERROR_MESSAGE);
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		ontModel.read(new FileInputStream(file.getPath()), "");
		for(Iterator i = ontModel.listClasses();i.hasNext();){
			OntClass c = (OntClass) i.next();
			if (!c.isAnon()) {
				boolean isTrans = false;
				for (Iterator it = c.listSuperClasses(); it.hasNext(); ){
					OntClass sup = (OntClass) it.next();
					if(sup.getLocalName()!=null && sup.getLocalName().equals("Transition")){
						isTrans = true;
						break;
					}
				}
				if (isTrans) {
					StateMachine stateMachine = new StateMachine();
					for (Iterator it = c.listSuperClasses(); it.hasNext(); ) {
						OntClass sup = (OntClass) it.next();
						Iterator ipp = sup.listDeclaredProperties();
						String from = null;
						String to = null;
						if (sup.isRestriction()) {
							Restriction r = sup.asRestriction();
							OntProperty p = r.getOnProperty();
							if (r.isAllValuesFromRestriction()) {
								AllValuesFromRestriction avf = r.asAllValuesFromRestriction();
								if(p.getLocalName().equals("trigger")){
									stateMachine.setTrans(avf.getAllValuesFrom().getLocalName());
								}
								else if(p.getLocalName().equals("sink_to")){
									stateMachine.setTo(avf.getAllValuesFrom().getLocalName());
								}
								else if(p.getLocalName().equals("source_from")){
									stateMachine.setFrom(avf.getAllValuesFrom().getLocalName());
								}
							}
						}
					}
					stateMachines.add(stateMachine);
				}
			}
		}
		//Set set = phenomena.keySet();
		//for(Object s : set) System.out.println(s.toString());
		//for(int i = 0;i < stateMachines.size();i++) System.out.println(stateMachines.get(i).toString());
		for(int i = 0;i < stateMachines.size() - 1;i++){
			for(int j = i + 1;j < stateMachines.size();j++){
				StateMachine temp1 = stateMachines.get(i);
				StateMachine temp2 = stateMachines.get(j);
				if(temp1.isAlternate(temp2)){
					if(phenomena.containsKey(temp1.getFrom()) && phenomena.containsKey(temp1.getTo())){
						int from = phenomena.get(temp1.getFrom()).getBiaohao();
						int to = phenomena.get(temp1.getTo()).getBiaohao();
						for(int k = 0;k < Main.win.myIntDiagram.size();k++){
							if(Main.win.subProblemDiagrams[k].getPhenomenon().contains(phenomena.get(temp1.getFrom())) && Main.win.subProblemDiagrams[k].getPhenomenon().contains(phenomena.get(temp1.getTo()))){
								Main.win.instantPanes.get(k).addConstraint("int" + from + ".s", "Alternate","int" + from + ".f",null);
								Main.win.instantPanes.get(k).addConstraint("int" + to + ".s", "Alternate","int" + to + ".f",null);
								Main.win.instantPanes.get(k).addConstraint("int" + from + ".f", "Alternate","int" + to + ".s",null);
								Main.win.instantPanes.get(k).addConstraint("int" + to + ".f", "Alternate","int" + from + ".s",null);
							}
						}
					}

					if(phenomena.containsKey(temp1.getTrans()) && phenomena.containsKey(temp2.getTrans())){
						int from = phenomena.get(temp1.getTrans()).getBiaohao();
						int to = phenomena.get(temp2.getTrans()).getBiaohao();
						for(int k = 0;k < Main.win.myIntDiagram.size();k++){
							if(Main.win.subProblemDiagrams[k].getPhenomenon().contains(phenomena.get(temp1.getTrans())) && Main.win.subProblemDiagrams[k].getPhenomenon().contains(phenomena.get(temp2.getTrans()))){
								Main.win.instantPanes.get(k).addConstraint("int" + from,  "Alternate","int" + to,null);
							}
						}
					}
				}
			}
		}
	}


	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Open")){
			try {
				if(buttonState == 0)loadProjectXML();
				if(buttonState == 1)loadOWLFile();
			} catch (DocumentException e1) {
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getActionCommand().equals("Load")) {
			this.myDrawPane.event_load();
			this.show.setEnabled(true);
		}
		if (e.getActionCommand().equals("Check")) {
			this.myDrawPane.event_check();
		}
		if (e.getActionCommand().equals("Show")) {
			new OntologyShow().show();
		}
		if (e.getActionCommand().equals("About")) {
			String s = "tool to show clock specification";
			JOptionPane.showMessageDialog(this, s, "About PFTool", 1);
		}
		if (e.getActionCommand().equals("Add Clock Constraint")){
        	instantPane.addClockConstraint();
		}
		if (e.getActionCommand().equals("Export Relations")){
			instantPane.createRelations();
		}
	}



	public void setButtonState(int i) {
		buttonState = i;
		if (i == 0) {
			constraint.setEnabled(false);
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jLabel[0].setEnabled(true);
			this.myDrawPane.jLabel[1].setEnabled(false);
			this.myDrawPane.jLabel[2].setEnabled(false);
			this.myDrawPane.jLabel[3].setEnabled(false);
			this.myDrawPane.jb1.setEnabled(true);
		}
		if (i == 1) {
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jLabel[0].setEnabled(false);
			this.myDrawPane.jLabel[1].setEnabled(true);
			this.myDrawPane.jLabel[2].setEnabled(false);
			this.myDrawPane.jLabel[3].setEnabled(false);
			this.myDrawPane.jb1.setEnabled(true);
			instantPane.addBut.setEnabled(false);
			instantPane.combineBut.setEnabled(false);
			instantPane.createTxtBut.setEnabled(false);
			for(int j = 0;j < instantPanes.size();j++){
				instantPanes.get(i).addBut.setEnabled(false);
				instantPanes.get(i).combineBut.setEnabled(false);
				instantPanes.get(i).createTxtBut.setEnabled(false);
			}
			constraint.setEnabled(false);
		}
		if (i == 2) {
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jLabel[0].setEnabled(false);
			this.myDrawPane.jLabel[1].setEnabled(false);
			this.myDrawPane.jLabel[2].setEnabled(true);
			this.myDrawPane.jLabel[3].setEnabled(false);
			instantPane.addBut.setEnabled(true);
			instantPane.combineBut.setEnabled(true);
			instantPane.createTxtBut.setEnabled(true);
			for(int j = 0;j < instantPanes.size();j++){
				instantPanes.get(i).addBut.setEnabled(true);
				instantPanes.get(i).combineBut.setEnabled(true);
				instantPanes.get(i).createTxtBut.setEnabled(true);
			}
			constraint.setEnabled(true);
		}
		if (i == 3) {
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jLabel[0].setEnabled(false);
			this.myDrawPane.jLabel[1].setEnabled(false);
			this.myDrawPane.jLabel[2].setEnabled(false);
			this.myDrawPane.jb1.setEnabled(false);
			instantPane.addBut.setEnabled(false);
			instantPane.combineBut.setEnabled(false);
			instantPane.createTxtBut.setEnabled(false);
			for(int j = 0;j < instantPanes.size();j++){
				instantPanes.get(i).addBut.setEnabled(false);
				instantPanes.get(i).combineBut.setEnabled(false);
				instantPanes.get(i).createTxtBut.setEnabled(false);
			}
			constraint.setEnabled(false);
		}
		if ((i == 4) || (i == 5)) {
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);
		}
		if (i == 5) {
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);
		}
		if (i == 6) {
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);
		}
		if (i == 7) {
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);

		}
		if (i == 8) {
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);

		}
	}

	public Main() {
		super("A Tool For Generating Clock Specification And Timing Diagram");
		this.chooser = new JFileChooser();
		setJMenuBar(this.menuBar);
		this.menuBar.add(this.file);
		this.menuBar.add(this.ontology);
		this.menuBar.add(this.constraint);
		this.menuBar.add(this.help);
		this.constraint.add(this.add);
		//this.constraint.add(this.combine);
		this.constraint.add(this.createTxt);
		this.ontology.add(this.load);
		this.ontology.add(this.show);
		this.ontology.add(this.check);
		this.file.add(this.news);
		this.file.add(this.open);
		this.file.add(this.save);
		this.file.add(this.exit);
		this.help.add(this.about);
		this.news.addActionListener(this);
		this.open.addActionListener(this);
		this.save.addActionListener(this);
		this.load.addActionListener(this);
		this.check.addActionListener(this);
		this.exit.addActionListener(this);
		this.about.addActionListener(this);
		this.show.addActionListener(this);
		this.add.addActionListener(this);
		//this.combine.addActionListener(this);
		this.createTxt.addActionListener(this);
		this.clockDiagrams = new ClockDiagram[100];
		this.subProblemDiagrams = new Diagram[100];
		this.show.setEnabled(false);
		this.news.setEnabled(false);
		this.open.setEnabled(false);
		this.save.setEnabled(false);
		this.load.setEnabled(false);
		this.check.setEnabled(false);
		this.exit.setEnabled(true);
		this.all.setRightComponent(this.rightp);
		/********************************************************/
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		/********************************************************/
		this.all.setLeftComponent(this.myDrawPane);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();

		this.all.setDividerLocation((int) (width / 8.0D));
		this.rightp.setDividerLocation((int) (4.5D * width / 7.0D));
		this.rightp.setLeftComponent(this.myDisplayPane);
		this.rightp.setRightComponent(this.myInfoPane);
		getContentPane().add(this.all);
		setButtonState(0);
	}
}