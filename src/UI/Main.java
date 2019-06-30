package UI;

import Shape.*;
import Shape.Shape;
import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
import jdk.internal.util.xml.XMLStreamException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import util.XMLFileFilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Main extends JFrame implements ActionListener {
	public static String errmes = "";
	public static int errstate = 0;// 0û���� 1�ߴ��� 2���󽻻�����
	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu help = new JMenu("Help");
	JMenu ontology = new JMenu("Ontology");
	JMenuItem save = new JMenuItem("Save");
	JMenuItem open = new JMenuItem("Open");
	JMenuItem news = new JMenuItem("New");
	JMenuItem exit = new JMenuItem("Exit");
	JMenuItem load = new JMenuItem("Load");
	JMenuItem check = new JMenuItem("Check");
	JMenuItem show = new JMenuItem("Show");
	JMenuItem about = new JMenuItem("About");
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
	public InstantPane[] instantPanes = new InstantPane[100];
	/*************************************************************************/
	DisplayPane myDisplayPane = new DisplayPane();
	JSplitPane all = new JSplitPane();
	JSplitPane rightp = new JSplitPane();
	JToolBar toolbar = new JToolBar();
	JToggleButton b_machine;
	JToggleButton b_givendomain;
	JToggleButton b_designeddomain;
	JToggleButton b_requirement;
	JToggleButton b_interface;
	JToggleButton b_requirementconstraint;
	JToggleButton b_requirementreference;
	JToggleButton b_hong;
	JToggleButton b_lan;
	JToggleButton b_lv;
	JToggleButton b_cheng;
	JToggleButton b_zi;
	JToggleButton b_cheng_y;
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
	}

	private void clear() {
		smooth();
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
		clear();
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
				this.myDisplayPane.addPane(new MyPane(clockDiagrams[count - 1]), "Sub" + clockDiagrams[count - 1].getTitle());
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
				//IntPane tempIntPane = new IntPane(intDiagram, 1,myProblemDiagram);
				//this.myDisplayPane.addPane(tempIntPane,intDiagram.getTitle());
				senCount++;
			}
			this.myInfoPane.treeInit();
			for(int i = 0;i < count - 1;i++){
				for(int j = 0;j < subProblemDiagrams[i].getProblemDomains().size();j++){
					try{
						Rect problemDomain = (Rect) subProblemDiagrams[i].getProblemDomains().get(j);
						InstantGraph instantGraph = new InstantGraph(problemDomain, clockDiagrams[i].getClocks().get(j),i);
						if(instantPanes[i] == null) instantPanes[i] = new InstantPane(instantGraph);
						else instantPanes[i].addGraph(instantGraph);
					}
					catch (Exception e){
						e.printStackTrace();
					}
				}
				this.myDisplayPane.addPane(instantPanes[i],"InstantGraph" + (i + 1));
			}
		}
    }



	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Open")){
			try {
				loadProjectXML();
			} catch (DocumentException e1) {
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
			String s = "DPTool Version1.0\n1.It is a graphical supporting tool for describing and analyzing software problem and performing the problem projection.\n2.It provides guidance of problem description and projection.\n3.It brings scenario into PF for conducting a scenario based problem projection and implements scenario-extended problem description and automated problem projection.\n4.Some checking techniques are included for ensuring the quality of the requirements document.\n";
			s = s
					+ "\nCopyright 2010 Academy of Mathematics and Systems Science, Chinese Academy of Sciences All Rights Reserved";
			s = s + "\nAuthor:Zhi Jin,Bin Yin,Xiaohong Chen";
			JOptionPane.showMessageDialog(this, s, "About PFTool", 1);
		}
	}

	public void setButtonState(int i) {
		if (i == -2) {
			buttonClear();
			this.load.setEnabled(true);
		}
		if (i == -1) {
			buttonClear();
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.load.setEnabled(true);
		}
		if (i == 0) {
			buttonClear();
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);
			this.b_machine.setEnabled(true);
		}
		if (i == 1) {
			buttonClear();
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);
			this.b_givendomain.setEnabled(true);
		}
		if (i == 2) {
			buttonClear();
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);
			this.b_interface.setEnabled(true);
		}
		if (i == 3) {
			buttonClear();
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);
		}
		if ((i == 4) || (i == 5)) {
			buttonClear();
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);
			this.b_requirement.setEnabled(true);
		}
		if (i == 5) {
			buttonClear();
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);
			this.b_requirementconstraint.setEnabled(true);
			this.b_requirementreference.setEnabled(true);
		}
		if (i == 6) {
			buttonClear();
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);
		}
		if (i == 7) {
			buttonClear();
			this.b_cheng.setEnabled(true);
			this.b_cheng_y.setEnabled(true);
			this.b_hong.setEnabled(true);
			this.b_lan.setEnabled(true);
			this.b_lv.setEnabled(true);
			this.b_zi.setEnabled(true);
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);

		}
		if (i == 8) {
			buttonClear();
			this.load.setEnabled(true);
			this.news.setEnabled(true);
			this.open.setEnabled(true);
			this.save.setEnabled(true);
			this.check.setEnabled(true);
			this.myDrawPane.jb1.setEnabled(true);

		}
	}

	public void buttonClear() {
		this.b_cheng.setEnabled(false);
		this.b_cheng_y.setEnabled(false);
		this.b_designeddomain.setEnabled(false);
		this.b_givendomain.setEnabled(false);
		this.b_hong.setEnabled(false);
		this.b_interface.setEnabled(false);
		this.b_lan.setEnabled(false);
		this.b_lv.setEnabled(false);
		this.b_machine.setEnabled(false);
		this.b_requirement.setEnabled(false);
		this.b_requirementconstraint.setEnabled(false);
		this.b_requirementreference.setEnabled(false);
		this.b_zi.setEnabled(false);
	}

	public void smooth() {
		this.b_machine.setSelected(false);
		this.b_givendomain.setSelected(false);
		this.b_designeddomain.setSelected(false);
		this.b_requirement.setSelected(false);
		this.b_interface.setSelected(false);
		this.b_requirementconstraint.setSelected(false);
		this.b_requirementreference.setSelected(false);
		this.b_lv.setSelected(false);
		this.b_hong.setSelected(false);
		this.b_cheng.setSelected(false);
		this.b_zi.setSelected(false);
		this.b_lan.setSelected(false);
		this.b_cheng_y.setSelected(false);
	}

	private void toolBarInit() {
		this.b_givendomain = new JToggleButton(new ImageIcon("./src/icons/rect.jpg"));//src/icons/rect.jpg
		this.b_givendomain.addActionListener(this);
		this.b_givendomain.setToolTipText("GivenDomain");
		this.b_givendomain.setActionCommand("Draw_GivenDomain");

		this.b_designeddomain = new JToggleButton(new ImageIcon(
				"./src/icons/drect.jpg"));
		this.b_designeddomain.addActionListener(this);
		this.b_designeddomain.setToolTipText("DesignedDomain");
		this.b_designeddomain.setActionCommand("Draw_DesignedDomain");

		this.b_machine = new JToggleButton(new ImageIcon("./src/icons/machine.jpg"));
		this.b_machine.addActionListener(this);
		this.b_machine.setToolTipText("Machine");
		this.b_machine.setActionCommand("Draw_Machine");

		this.b_requirement = new JToggleButton(new ImageIcon("./src/icons/r.jpg"));
		this.b_requirement.addActionListener(this);
		this.b_requirement.setToolTipText("Requirement");
		this.b_requirement.setActionCommand("Draw_Requirement");

		this.b_interface = new JToggleButton(new ImageIcon("./src/icons/i.jpg"));
		this.b_interface.addActionListener(this);
		this.b_interface.setToolTipText("Interface");
		this.b_interface.setActionCommand("Draw_Interface");

		this.b_requirementconstraint = new JToggleButton(new ImageIcon(
				"./src/icons/rc.jpg"));
		this.b_requirementconstraint.addActionListener(this);
		this.b_requirementconstraint.setToolTipText("RequirementConstraint");
		this.b_requirementconstraint
				.setActionCommand("Draw_RequirementConstraint");

		this.b_requirementreference = new JToggleButton(new ImageIcon(
				"./src/icons/rr.jpg"));
		this.b_requirementreference.addActionListener(this);
		this.b_requirementreference.setToolTipText("RequirementReference");
		this.b_requirementreference
				.setActionCommand("Draw_RequirementReference");

		this.b_hong = new JToggleButton(new ImageIcon("./src/icons/hong.jpg"));
		this.b_hong.addActionListener(this);
		this.b_hong.setActionCommand("Hong");
		this.b_hong.setToolTipText("behEna");

		this.b_lan = new JToggleButton(new ImageIcon("./src/icons/lan.jpg"));
		this.b_lan.addActionListener(this);
		this.b_lan.setActionCommand("Lan");
		this.b_lan.setToolTipText("behOrd");

		this.b_lv = new JToggleButton(new ImageIcon("./src/icons/lv.jpg"));
		this.b_lv.addActionListener(this);
		this.b_lv.setActionCommand("Lv");
		this.b_lv.setToolTipText("synchrocity");

		this.b_cheng = new JToggleButton(new ImageIcon("./src/icons/cheng.jpg"));
		this.b_cheng.addActionListener(this);
		this.b_cheng.setActionCommand("Cheng");
		this.b_cheng.setToolTipText("expOrd");

		this.b_zi = new JToggleButton(new ImageIcon("./src/icons/zi.jpg"));
		this.b_zi.addActionListener(this);
		this.b_zi.setActionCommand("Zi");
		this.b_zi.setToolTipText("reqEna");

		this.b_cheng_y = new JToggleButton(new ImageIcon("./src/icons/y_cheng.jpg"));
		this.b_cheng_y.addActionListener(this);
		this.b_cheng_y.setActionCommand("Cheng_y");
		this.b_cheng_y.setToolTipText("Int");

		this.toolbar.add(this.b_givendomain);
		this.toolbar.add(this.b_designeddomain);
		this.toolbar.add(this.b_machine);
		this.toolbar.add(this.b_requirement);
		this.toolbar.add(this.b_interface);
		this.toolbar.add(this.b_requirementreference);
		this.toolbar.add(this.b_requirementconstraint);
		this.toolbar.addSeparator();
		this.toolbar.add(this.b_hong);
		this.toolbar.add(this.b_lan);
		this.toolbar.add(this.b_lv);
		this.toolbar.add(this.b_cheng);
		this.toolbar.add(this.b_zi);
		this.toolbar.add(this.b_cheng_y);
		this.toolbar.setFloatable(false);
		this.toolbar.setOrientation(0);
		getContentPane().add(this.toolbar, "North");
	}

	public Main() {
		super(
				"DPTool: A Tool for supporting the Problem Description and Projection");

		this.chooser = new JFileChooser();
		setJMenuBar(this.menuBar);
		this.menuBar.add(this.file);
		this.menuBar.add(this.ontology);
		this.menuBar.add(this.help);
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

		this.all.setDividerLocation((int) (0));
		this.rightp.setDividerLocation((int) (5.0D * width / 7.0D));
		this.rightp.setLeftComponent(this.myDisplayPane);
		this.rightp.setRightComponent(this.myInfoPane);
		getContentPane().add(this.all);
		toolBarInit();
		setButtonState(-2);
	}
}