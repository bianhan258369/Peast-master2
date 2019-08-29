package Shape;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class Diagram implements Serializable {
	public LinkedList components;
	String title;
	private String interactionDescription = "";

	public static boolean hasSub(String path) throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(path);
		Element rootElement = document.getRootElement();
		Iterator it = rootElement.elementIterator("fileList");
		Element root = (Element) it.next();
		return root.elementIterator("SubProblemDiagramList").next().elementIterator("SubProblemDiagram").hasNext();
	}

	public String getTitle() {
		return this.title;
	}

	public Diagram(String title) {
		this.title = title;
		this.components = new LinkedList();
	}

	public Diagram(String title, File file){
		this.title = title;
		this.components = new LinkedList();
		try {

				SAXReader saxReader = new SAXReader();
				String path = getFilePath(file.getPath());
				Document project = saxReader.read(file);
				Element rootElement = project.getRootElement();
				Element filelist = rootElement.elementIterator("fileList").next();
				String cdPath = path + filelist.elementIterator("ContextDiagram").next().getText() + ".xml";
				String pdPath = path + filelist.elementIterator("ProblemDiagram").next().getText() + ".xml";

				Iterator it;

				Document contextDiagram = saxReader.read(cdPath);
				Document problemDiagram = saxReader.read(pdPath);

				rootElement = contextDiagram.getRootElement();
				//it = rootElement.elementIterator("data");
				Element cdRoot = rootElement;

				rootElement = problemDiagram.getRootElement();
				//it = rootElement.elementIterator("data");
				Element pdRoot = rootElement;

				Element temp;
				for(Iterator i = cdRoot.elementIterator("Machine");i.hasNext();){
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
					this.components.add(rect);
				}

				Element requirement = (Element) pdRoot.elementIterator("Requirement").next();
				for(Iterator i = requirement.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String str = temp.attributeValue("requirement_locality");
					String[] locality = str.split(",");
					int x1 = Integer.parseInt(locality[0]);
					int y1 = Integer.parseInt(locality[1]);
					int x2 = Integer.parseInt(locality[2]);
					int y2 = Integer.parseInt(locality[3]);
					Oval oval = new Oval(x1 + x2 / 2, y1 + y2 / 2);
					oval.setText(temp.attributeValue("requirement_context"));
					oval.des = 1;
					oval.setBiaohao(Integer.parseInt(temp.attributeValue("requirement_no")));
					this.components.add(oval);
				}

				Element problemDomain = (Element) cdRoot.elementIterator("ProblemDomain").next();
				Element givenDomain = problemDomain.elementIterator("GivenDomain").next();
				for(Iterator i = givenDomain.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String str = temp.attributeValue("problemdomain_locality");
					String[] locality = str.split(",");
					int x1 = Integer.parseInt(locality[0]);
					int y1 = Integer.parseInt(locality[1]);
					int x2 = Integer.parseInt(locality[2]);
					int y2 = Integer.parseInt(locality[3]);
					Rect rect = new Rect(x1 + x2 / 2, y1 + y2 / 2);
					rect.setText(temp.attributeValue("problemdomain_name"));
					//System.out.println(rect.getText());
					rect.setShortName(temp.attributeValue("problemdomain_shortname"));
					rect.setState(1);
					rect.setCxb(temp.attributeValue("problemdomain_type").charAt(0));
					this.components.add(rect);
				}

				Element designDomain = problemDomain.elementIterator("DesignDomain").next();
				for(Iterator i = designDomain.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String str = temp.attributeValue("problemdomain_locality");
					String[] locality = str.split(",");
					int x1 = Integer.parseInt(locality[0]);
					int y1 = Integer.parseInt(locality[1]);
					int x2 = Integer.parseInt(locality[2]);
					int y2 = Integer.parseInt(locality[3]);
					Rect rect = new Rect(x1 + x2 / 2, y1 + y2 / 2);
					rect.setText(temp.attributeValue("problemdomain_name"));
					//System.out.println(rect.getText());
					rect.setShortName(temp.attributeValue("problemdomain_shortname"));
					rect.setState(1);
					rect.setCxb(temp.attributeValue("problemdomain_type").charAt(0));
					this.components.add(rect);
				}

				Element Interface = (Element) cdRoot.elementIterator("Interface").next();
				for(Iterator i = Interface.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String name = temp.attributeValue("interface_no");
					String to = temp.attributeValue("interface_to");
					String from = temp.attributeValue("interface_from");
					Shape toShape = null;
					Shape fromShape = null;
					for(int j = 0;j < this.components.size();j++){
						Shape tempShape = (Shape)this.components.get(j);
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
						String phenomenonName = tempPhenomenon.attributeValue("phenomenon_name");
						String phenomenonState = tempPhenomenon.attributeValue("phenomenon_type");
						String phenomenonFrom = tempPhenomenon.attributeValue("phenomenon_from");
						String phenomenonTo = tempPhenomenon.attributeValue("phenomenon_to");
						Rect phenomenonFromRect = null;
						Rect phenomenonToRect = null;
						for(int k = 0;k < this.components.size();k++){
							Shape tempShape = (Shape)this.components.get(k);
							if(tempShape instanceof Rect){
								Rect tempRect = (Rect)tempShape;
								if(tempRect.getShortName().equals(phenomenonFrom)) phenomenonFromRect = tempRect;
								if(tempRect.getShortName().equals(phenomenonTo)) phenomenonToRect = tempRect;
							}
						}
						int phenomenonBiaohao = Integer.parseInt(tempPhenomenon.attributeValue("phenomenon_no"));
						Phenomenon phenomenon = new Phenomenon(phenomenonName, phenomenonState, phenomenonFromRect, phenomenonToRect);
						phenomenon.setConstraining(false);
						phenomenon.setBiaohao(phenomenonBiaohao);
						line.phenomenons.add(phenomenon);
					}
					this.components.add(line);
				}

				Element reference = pdRoot.elementIterator("Reference").next();
				for(Iterator i = reference.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String name = temp.attributeValue("reference_name");
					String to = temp.attributeValue("reference_to");
					String from = temp.attributeValue("reference_from");
					Shape toShape = null;
					Shape fromShape = null;
					for(int j = 0;j < this.components.size();j++){
						Shape tempShape = (Shape)this.components.get(j);
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
						String phenomenonName = tempPhenomenon.attributeValue("phenomenon_name");
						String phenomenonState = tempPhenomenon.attributeValue("phenomentn_type");
						String phenomenonFrom = tempPhenomenon.attributeValue("phenomenon_from");
						String phenomenonTo = tempPhenomenon.attributeValue("phenomenon_to");
						int pehnomenonRequirementBiaohao = Integer.parseInt(tempPhenomenon.attributeValue("phenomenon_requirement"));
						boolean phenomenonConstraining = (tempPhenomenon.attributeValue("phenomenon_constraint")).equals("true") ? true : false;
						int phenomenonBiaohao = Integer.parseInt(tempPhenomenon.attributeValue("phenomenon_no"));
						Rect phenomenonFromRect = null;
						Rect phenomenonToRect = null;
						for(int k = 0;k < this.components.size();k++){
							Shape tempShape = (Shape)this.components.get(k);
							if(tempShape instanceof Rect){
								Rect tempRect = (Rect)tempShape;
								if(tempRect.getShortName().equals(phenomenonFrom)) phenomenonFromRect = tempRect;
								if(tempRect.getShortName().equals(phenomenonTo)) phenomenonToRect = tempRect;
							}
						}
						Oval oval = this.getRequirement(pehnomenonRequirementBiaohao);
						Phenomenon phenomenon = new Phenomenon(phenomenonName, phenomenonState, phenomenonFromRect, phenomenonToRect);
						phenomenon.setConstraining(phenomenonConstraining);
						phenomenon.setBiaohao(phenomenonBiaohao);
						phenomenon.setRequirement(oval);
						line.phenomenons.add(phenomenon);
					}
					this.components.add(line);
				}

				Element constraint = (Element) pdRoot.elementIterator("Constraint").next();
				for(Iterator i = constraint.elementIterator("Element");i.hasNext();){
					temp = (Element)i.next();
					String name = temp.attributeValue("constraint_name");
					String to = temp.attributeValue("constraint_to");
					String from = temp.attributeValue("constraint_from");
					Shape toShape = null;
					Shape fromShape = null;
					for(int j = 0;j < this.components.size();j++){
						Shape tempShape = (Shape)this.components.get(j);
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
						String phenomenonName = tempPhenomenon.attributeValue("phenomenon_name");
						String phenomenonState = tempPhenomenon.attributeValue("phenomentn_type");
						String phenomenonFrom = tempPhenomenon.attributeValue("phenomenon_from");
						String phenomenonTo = tempPhenomenon.attributeValue("phenomenon_to");
						int pehnomenonRequirementBiaohao = Integer.parseInt(tempPhenomenon.attributeValue("phenomenon_requirement"));
						boolean phenomenonConstraining = (tempPhenomenon.attributeValue("phenomenon_constraint")).equals("true") ? true : false;
						int phenomenonBiaohao = Integer.parseInt(tempPhenomenon.attributeValue("phenomenon_no"));
						Rect phenomenonFromRect = null;
						Rect phenomenonToRect = null;
						for(int k = 0;k < this.components.size();k++){
							Shape tempShape = (Shape)this.components.get(k);
							if(tempShape instanceof Rect){
								Rect tempRect = (Rect)tempShape;
								if(tempRect.getShortName().equals(phenomenonFrom)) phenomenonFromRect = tempRect;
								if(tempRect.getShortName().equals(phenomenonTo)) phenomenonToRect = tempRect;
							}
						}
						Oval oval = this.getRequirement(pehnomenonRequirementBiaohao);
						Phenomenon phenomenon = new Phenomenon(phenomenonName, phenomenonState, phenomenonFromRect, phenomenonToRect);
						phenomenon.setConstraining(phenomenonConstraining);
						phenomenon.setBiaohao(phenomenonBiaohao);
						phenomenon.setRequirement(oval);
						line.phenomenons.add(phenomenon);
					}
					this.components.add(line);
				}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void add(Shape component) {
		for (int i = 0; i <= this.components.size() - 1; i++) {
			Shape tmp_s = (Shape) this.components.get(i);
			if (tmp_s.equals(component)) {
				return;
			}
		}
		this.components.add(component);
	}

	public void deletePhenomenon(String name, String state) {
		for (int i = 0; i <= this.components.size() - 1; i++) {
			Shape tmp_s = (Shape) this.components.get(i);
			if (tmp_s.shape == 2) {
				Line tmp_l = (Line) tmp_s;
				for (int j = 0; j <= tmp_l.phenomenons.size() - 1; j++) {
					Phenomenon tmp_p = (Phenomenon) tmp_l.phenomenons.get(j);
					if ((tmp_p.getName().equals(name))
							&& (tmp_p.getState().equals(state))) {
						tmp_l.phenomenons.remove(j);
						j--;
					}
				}
			}
		}
	}

	public LinkedList getPhenomenon() {
		LinkedList ll = new LinkedList();
		for (int i = 0; i <= this.components.size() - 1; i++) {
			Shape tmp_s = (Shape) this.components.get(i);
			if (tmp_s.shape == 2) {
				Line tmp_l = (Line) tmp_s;
				for (int j = 0; j <= tmp_l.phenomenons.size() - 1; j++) {
					Phenomenon tmp_p = (Phenomenon) tmp_l.phenomenons.get(j);
					boolean jia = true;
					for (int k = 0; k <= ll.size() - 1; k++) {
						Phenomenon tmp1 = (Phenomenon) ll.get(k);
						if ((!tmp1.getName().equals(tmp_p.getName()))
								|| (!tmp1.getState().equals(tmp_p.getState())))
							continue;
						jia = false;
					}

					if (jia) {
						ll.add(tmp_p);
					}
				}
			}
		}
		return ll;
	}

	public LinkedList getReference() {
		LinkedList ll = new LinkedList();
		for (int i = 0; i <= this.components.size() - 1; i++) {
			Shape tmp_s = (Shape) this.components.get(i);
			if (tmp_s.shape == 2) {
				Line tmp_l = (Line) tmp_s;
				if (tmp_l.getState() == 0) {
					continue;
				}
				for (int j = 0; j <= tmp_l.phenomenons.size() - 1; j++) {
					ll.add((Phenomenon) tmp_l.phenomenons.get(j));
				}
			}
		}

		return ll;
	}

	public void addInterface(int i) {
		Line line = new Line((Shape) this.components.get(0),
				(Shape) this.components.get(1), i);

		setLineName();
		this.components.addFirst(line);
	}

	public Rect getMachine() {
		for (int i = 0; i < this.components.size(); i++) {
			if (((Shape) this.components.get(i)).shape == 0) {
				Rect rr = (Rect) this.components.get(i);
				if (rr.state == 2) {
					return rr;
				}
			}
		}
		return null;
	}


	public int hasMachine() {
		int count = 0;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp_s = (Shape) this.components.get(i);
			if (tmp_s.shape == 0) {
				Rect tmp_r = (Rect) tmp_s;
				if (tmp_r.state == 2) {
					count++;
				}
			}
		}
		return count;
	}

	public int hasInterface() {
		int count = 0;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp_s = (Shape) this.components.get(i);
			if (tmp_s.shape == 2) {
				Line tmp_r = (Line) tmp_s;
				if (tmp_r.getState() == 0) {
					count++;
				}
			}
		}
		return count;
	}

	public void setLineName() {
		int j = 0;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 2) {
				String s = "" + (char) (97 + j);
				j++;
				((Line) tmp).name = s;
			}
		}
	}

	public LinkedList getRequirements() {
		LinkedList ll = new LinkedList();
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 1) {
				ll.add(tmp);
			}
		}
		return ll;
	}

	public LinkedList getProblemDomains() {
		LinkedList ll = new LinkedList();
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 0) {
				if(((Rect)tmp).getState() != 2) ll.add(tmp);
			}
		}
		return ll;
	}

	public LinkedList getLines(){
		LinkedList ll = new LinkedList();
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 2 ) {
				ll.add(tmp);
			}
		}
		return ll;
	}

	public Oval getRequirement(int num) {
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 1) {
				Oval tmp_o = (Oval) tmp;
				if (tmp_o.getBiaohao() == num) {
					return tmp_o;
				}
			}
		}
		return null;
	}

	public int hasProDom() {
		int count = 0;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp_s = (Shape) this.components.get(i);
			if (tmp_s.shape == 0) {
				Rect tmp_r = (Rect) tmp_s;
				if ((tmp_r.state == 1) || (tmp_r.state == 0)) {
					count++;
				}
			}
		}
		return count;
	}

	public int hasReqCon() {
		int count = 0;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp_s = (Shape) this.components.get(i);
			if (tmp_s.shape == 2) {
				Line tmp_r = (Line) tmp_s;
				if (tmp_r.getState() == 2) {
					count++;
				}
			}
		}
		return count;
	}

	public int hasReqRef() {
		int count = 0;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp_s = (Shape) this.components.get(i);
			if (tmp_s.shape == 2) {
				Line tmp_r = (Line) tmp_s;
				if (tmp_r.getState() == 1) {
					count++;
				}
			}
		}
		return count;
	}

	public int hasReq() {
		int count = 0;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp_s = (Shape) this.components.get(i);
			if (tmp_s.shape == 1) {
				count++;
			}
		}
		return count;
	}

	public void follow(Shape a) {
		if (a.shape == 2) {
			return;
		}
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);

			if ((tmp.shape == 1) || (tmp.shape == 0)) {
				continue;
			}
			Line tmp1 = (Line) tmp;
			if ((tmp1.from.equals(a)) || (tmp1.to.equals(a)))
				tmp1.refresh();
		}
	}

	public void draw(Graphics g) {
		int n = 0;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);

			if (tmp.shape == 2) {
				Line tmpLine = (Line) tmp;

				if (tmpLine.selected) {
					g.setColor(Color.red);
				}

				String biao = tmpLine.name;
				g.drawString(biao, (tmpLine.x1 + tmpLine.x2) / 2,
						(tmpLine.y1 + tmpLine.y2) / 2);

				g.setColor(Color.black);
			}
			tmp.draw(g);
		}
	}

	public String getInteractionDescription() {
		this.interactionDescription = "";
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 2) {
				Line tmp_l = (Line) tmp;
				this.interactionDescription = (this.interactionDescription
						+ tmp_l.getDescription() + "\n");
			}
		}

		this.interactionDescription += "\n";
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 1) {
				Oval tmp_l = (Oval) tmp;
				this.interactionDescription = (this.interactionDescription
						+ "req" + tmp_l.getBiaohao() + ":" + tmp_l.getText() + "\n");
			}

		}
		return this.interactionDescription;
	}

	public boolean find(Shape a, Shape b) {
		for (int i = 0; i < this.components.size(); i++) {
			if (((Shape) this.components.get(i)).shape == 2) {
				Line line = (Line) this.components.get(i);
				if ((line.from.equals(a)) && (line.to.equals(b))) {
					return true;
				}
				if ((line.from.equals(b)) && (line.to.equals(a))) {
					return true;
				}
			}
		}
		return false;
	}

	public void rule() {
		Rect machine = getMachine();
		if (machine == null) {
			return;
		}
		for (int i = 0; i < this.components.size(); i++)
			if (((Shape) this.components.get(i)).shape == 0) {
				Rect rr = (Rect) this.components.get(i);
				if ((rr.state == 2) || (find(machine, rr)))
					continue;
				add(new Line(machine, rr, 0));
				setLineName();
			}
	}

	public Shape whichSelected(int x, int y) {
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.isIn(x, y)) {
				return tmp;
			}
		}
		return null;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Diagram convertToProblemDiagram(String name) {
		int maxx = 0;
		int miny = 0;
		int maxy = 0;
		Diagram tmp_d = new Diagram("");
		Persist.save(new File("tmp"), this);
		tmp_d = Persist.load(new File("tmp"));
		tmp_d.setTitle(name);

		for (int i = 0; i < tmp_d.components.size(); i++) {
			Shape tmp = (Shape) tmp_d.components.get(i);
			if (tmp.shape == 2) {
				Line tmpLine = (Line) tmp;
				tmpLine.des = 1;
			} else {
				Rect tmp1 = (Rect) tmp;
				if (tmp1.x1 + tmp1.x2 > maxx) {
					maxx = tmp1.x1 + tmp1.x2;
				}
				if (tmp1.y1 < miny) {
					miny = tmp1.y1;
				}
				if (tmp1.y1 + tmp1.y2 > maxy)
					maxy = tmp1.y1 + tmp1.y2;
			}
		}
		Oval tmp_oval = new Oval(maxx + 100, (maxy + miny) / 2);

		for (int i = 0; i < tmp_d.components.size(); i++) {
			Shape tmp = (Shape) tmp_d.components.get(i);
			if (tmp.shape == 2) {
				continue;
			}
			Rect tmp1 = (Rect) tmp;
			if (tmp1.state != 2) {
				tmp_d.add(new Line(tmp_oval, tmp1, 1));
				setLineName();
			}
		}

		tmp_d.add(tmp_oval);
		return tmp_d;
	}

	public void delete(Shape shape) {
		if (shape.shape == 2) {
			for (int i = 0; i < this.components.size(); i++) {
				Shape tmp = (Shape) this.components.get(i);
				if (shape.equals(tmp))
					this.components.remove(i);
			}
		} else {
			boolean k = false;
			for (int i = 0; i < this.components.size(); i++) {
				Shape tmp = (Shape) this.components.get(i);
				if (tmp.shape == 2) {
					Line tmpLine = (Line) tmp;
					if ((tmpLine.from.equals(shape))
							|| (tmpLine.to.equals(shape))) {
						this.components.remove(i);
						i--;
						if (i == -1) {
							k = true;
							i++;
						}
					}
				}
				if (tmp.equals(shape)) {
					this.components.remove(i);
					i--;
					if (i == -1) {
						k = true;
						i++;
					}
				}
			}
			if (k) {
				if (this.components.size() == 0) {
					return;
				}
				Shape tmp = (Shape) this.components.get(0);
				if (tmp.shape == 2) {
					Line tmpLine = (Line) tmp;
					if ((tmpLine.from.equals(shape))
							|| (tmpLine.to.equals(shape))) {
						this.components.remove(0);
					}
				}
				if (tmp.equals(shape))
					this.components.remove(0);
			}
		}
	}

	public static void main(String[] args) {
		Diagram dd = new Diagram("ee");
		Rect machine = new Rect(0, 0);
		machine.setState(2);
		machine.setText("MM");
		Rect[] r = new Rect[4];
		r[0] = new Rect(0, 0);
		r[0].setState(0);
		r[0].setText("0");

		r[1] = new Rect(0, 0);
		r[1].setState(0);
		r[1].setText("1");

		r[2] = new Rect(0, 0);
		r[2].setState(0);
		r[2].setText("2");

		r[3] = new Rect(0, 0);
		r[3].setState(0);

		Oval re = new Oval(0, 0);
		dd.add(machine);
		dd.add(re);
		for (int i = 0; i <= 3; i++) {
			dd.add(r[i]);
			dd.add(new Line(machine, r[i], 0));
			dd.add(new Line(re, r[i], 1));
		}
	}

	public boolean match1() {
		if (this.components.size() != 5) {
			return false;
		}
		Rect machine = null;
		Rect domain = null;
		Oval requirement = null;
		Line line1 = null;
		Line line2 = null;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 1) {
				requirement = (Oval) tmp;
			}
			if (tmp.shape == 0) {
				Rect tmp_rect = (Rect) tmp;
				if (tmp_rect.state == 2) {
					machine = tmp_rect;
				}
				if (((tmp_rect.state != 1) && (tmp_rect.state != 0))
						|| (tmp_rect.cxb != 'C'))
					continue;
				domain = tmp_rect;
			}

		}

		if ((machine == null) || (domain == null) || (requirement == null)) {
			return false;
		}
		line1 = findALine(machine, domain);
		if (line1 == null) {
			line1 = findALine(domain, machine);
		}
		if (line1 == null) {
			return false;
		}
		if (line1.getState() != 0) {
			return false;
		}
		line2 = findALine(requirement, domain);
		if (line2 == null) {
			return false;
		}
		if (line2.getState() != 2) {
			return false;
		}
		line1.setCore("C1");
		line1.setCore1("C2");
		line2.setCore1("C3");
		return true;
	}

	public boolean match2() {
		if (this.components.size() != 8) {
			return false;
		}
		Rect machine = null;
		Rect domain_c = null;
		Rect domain_b = null;
		Oval re = null;
		Line line1 = null;
		Line line2 = null;
		Line line3 = null;
		Line line4 = null;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 0) {
				Rect tmp_r = (Rect) tmp;
				if (tmp_r.state == 2) {
					machine = tmp_r;
				} else {
					if (tmp_r.cxb == 'C') {
						domain_c = tmp_r;
					}
					if (tmp_r.cxb == 'B') {
						domain_b = tmp_r;
					}
				}
			}
			if (tmp.shape == 1) {
				re = (Oval) tmp;
			}
		}
		if ((machine == null) || (domain_c == null) || (domain_b == null)
				|| (re == null)) {
			return false;
		}
		line1 = findALine(machine, domain_c);
		if (line1 == null) {
			line1 = findALine(domain_c, machine);
		}
		if (line1 == null) {
			return false;
		}
		if (line1.getState() != 0) {
			return false;
		}
		line2 = findALine(machine, domain_b);
		if (line2 == null) {
			line2 = findALine(domain_b, machine);
		}
		if (line2 == null) {
			return false;
		}
		if (line2.getState() != 0) {
			return false;
		}
		line3 = findALine(re, domain_c);
		if (line3 == null) {
			return false;
		}
		if (line3.getState() != 2) {
			return false;
		}
		line4 = findALine(re, domain_b);
		if (line4 == null) {
			return false;
		}
		if (line4.getState() != 1) {
			return false;
		}

		line1.core = "C1";
		line1.core1 = "C2";
		line2.core1 = "E4";
		line3.core1 = "C3";
		line4.core1 = "E4";
		return true;
	}

	public boolean match3(boolean pat) {
		if (this.components.size() != 8) {
			return false;
		}
		Rect machine = null;
		Rect domain1 = null;
		Rect domain2 = null;
		Oval re = null;
		Line line1 = null;
		Line line2 = null;
		Line line3 = null;
		Line line4 = null;
		boolean k = true;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 1) {
				re = (Oval) tmp;
			}
			if (tmp.shape == 0) {
				Rect tmp_r = (Rect) tmp;
				if (tmp_r.state == 2) {
					machine = tmp_r;
				} else if (tmp_r.cxb == 'C') {
					if (k) {
						domain1 = tmp_r;
						k = false;
					} else {
						domain2 = tmp_r;
					}
				}
			}
		}

		if ((machine == null) || (domain1 == null) || (domain2 == null)
				|| (re == null)) {
			return false;
		}
		if (pat) {
			line1 = findALine(machine, domain1);
			if (line1 == null) {
				return false;
			}
			if (line1.getState() != 0) {
				return false;
			}

			line2 = findALine(machine, domain2);
			if (line2 == null) {
				return false;
			}
			if (line2.getState() != 0) {
				return false;
			}

			line3 = findALine(re, domain1);
			if (line3 == null) {
				return false;
			}
			if (line3.getState() != 1) {
				return false;
			}

			line4 = findALine(re, domain2);
			if (line4 == null) {
				return false;
			}
			if (line4.getState() != 2) {
				return false;
			}

			line1.core1 = "C1";
			line2.core = "E2";
			line3.core1 = "C3";
			line4.core1 = "Y4";
			return true;
		}

		line1 = findALine(machine, domain2);
		if (line1 == null) {
			line1 = findALine(domain2, machine);
		}
		if (line1 == null) {
			return false;
		}
		if (line1.getState() != 0) {
			return false;
		}

		line2 = findALine(machine, domain1);
		if (line2 == null) {
			line1 = findALine(domain1, machine);
		}
		if (line2 == null) {
			return false;
		}
		if (line2.getState() != 0) {
			return false;
		}

		line3 = findALine(re, domain2);
		if (line3 == null) {
			return false;
		}
		if (line3.getState() != 1) {
			return false;
		}

		line4 = findALine(re, domain1);
		if (line4 == null) {
			return false;
		}
		if (line4.getState() != 2) {
			return false;
		}

		line1.core1 = "C1";
		line2.core = "E2";
		line3.core1 = "C3";
		line4.core1 = "Y4";
		return true;
	}

	public boolean match4() {
		if (this.components.size() != 8) {
			return false;
		}
		Rect machine = null;
		Rect domain_x = null;
		Rect domain_b = null;
		Oval re = null;
		Line line1 = null;
		Line line2 = null;
		Line line3 = null;
		Line line4 = null;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 0) {
				Rect tmp_r = (Rect) tmp;
				if (tmp_r.state == 2) {
					machine = tmp_r;
				} else {
					if (tmp_r.cxb == 'X') {
						domain_x = tmp_r;
					}
					if (tmp_r.cxb == 'B') {
						domain_b = tmp_r;
					}
				}
			}
			if (tmp.shape == 1) {
				re = (Oval) tmp;
			}
		}
		if ((machine == null) || (domain_x == null) || (domain_b == null)
				|| (re == null)) {
			return false;
		}
		line1 = findALine(machine, domain_x);
		if (line1 == null) {
			line1 = findALine(domain_x, machine);
		}
		if (line1 == null) {
			return false;
		}
		if (line1.getState() != 0) {
			return false;
		}
		line2 = findALine(machine, domain_b);
		if (line2 == null) {
			line2 = findALine(domain_b, machine);
		}
		if (line2 == null) {
			return false;
		}
		if (line2.getState() != 0) {
			return false;
		}
		line3 = findALine(re, domain_x);
		if (line3 == null) {
			return false;
		}
		if (line3.getState() != 2) {
			return false;
		}
		line4 = findALine(re, domain_b);
		if (line4 == null) {
			return false;
		}
		if (line4.getState() != 1) {
			return false;
		}
		line1.core = "E1";
		line1.core1 = "Y2";
		line2.core1 = "E3";
		line3.core1 = "Y4";
		line4.core1 = "E3";
		return true;
	}

	public boolean match5(boolean pat) {
		if (this.components.size() != 8) {
			return false;
		}
		Rect machine = null;
		Rect domain1 = null;
		Rect domain2 = null;
		Oval re = null;
		Line line1 = null;
		Line line2 = null;
		Line line3 = null;
		Line line4 = null;
		boolean k = true;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.shape == 1) {
				re = (Oval) tmp;
			}
			if (tmp.shape == 0) {
				Rect tmp_r = (Rect) tmp;
				if (tmp_r.state == 2) {
					machine = tmp_r;
				} else if (tmp_r.cxb == 'X') {
					if (k) {
						domain1 = tmp_r;
						k = false;
					} else {
						domain2 = tmp_r;
					}
				}
			}
		}

		if ((machine == null) || (domain1 == null) || (domain2 == null)
				|| (re == null)) {
			return false;
		}
		if (pat) {
			line1 = findALine(machine, domain1);
			if (line1 == null) {
				return false;
			}
			if (line1.getState() != 0) {
				return false;
			}

			line2 = findALine(machine, domain2);
			if (line2 == null) {
				return false;
			}
			if (line2.getState() != 0) {
				return false;
			}

			line3 = findALine(re, domain1);
			if (line3 == null) {
				return false;
			}
			if (line3.getState() != 1) {
				return false;
			}
			line4 = findALine(re, domain2);
			if (line4 == null) {
				return false;
			}
			if (line4.getState() != 2) {
				return false;
			}
			line1.core = "Y2";
			line2.core1 = "Y1";
			line3.core1 = "Y4";
			line4.core1 = "Y3";
			return true;
		}

		line1 = findALine(machine, domain2);
		if (line1 == null) {
			return false;
		}
		if (line1.getState() != 0) {
			return false;
		}

		line2 = findALine(machine, domain1);
		if (line2 == null) {
			return false;
		}
		if (line2.getState() != 0) {
			return false;
		}

		line3 = findALine(re, domain2);
		if (line3 == null) {
			return false;
		}
		if (line3.getState() != 1) {
			return false;
		}

		line4 = findALine(re, domain1);
		if (line4 == null) {
			return false;
		}
		if (line4.getState() != 2) {
			return false;
		}
		line1.core = "Y2";
		line2.core1 = "Y1";
		line3.core1 = "Y4";
		line4.core1 = "Y3";

		return true;
	}

	public Diagram refreshContextDiagram() {
		Diagram tmp_d = new Diagram("");
		Persist.save(new File("tmp"), this);
		tmp_d = Persist.load(new File("tmp"));
		tmp_d.setTitle("ContextDiagram");
		while (true) {
			boolean zhaodao = false;
			for (int i = 0; i <= tmp_d.components.size() - 1; i++) {
				Shape tmp_s = (Shape) tmp_d.components.get(i);
				if (tmp_s.shape == 1) {
					Oval tmp_o = (Oval) tmp_s;
					tmp_d.delete(tmp_o);
					zhaodao = true;
					break;
				}
			}
			if (!zhaodao)
				return tmp_d;
		}
	}

	public Line findALine(Shape r1, Shape r2) {
		Line re = null;
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp1 = (Shape) this.components.get(i);
			if (tmp1.shape == 2) {
				Line tmp_line = (Line) tmp1;
				if ((Data.same(tmp_line.from, r1))
						&& (Data.same(tmp_line.to, r2))) {
					re = tmp_line;
					break;
				}
			}
		}
		return re;
	}


	public Document createXML(){
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Diagram");
		root.addElement("Title").addText("ProblemDiagram");
		Rect rect = this.getMachine();
		Element machine = root.addElement("Machine").addAttribute("machine_name",rect.getText())
				.addAttribute("machine_shortname",rect.getShortName())
				.addAttribute("machine_state",Integer.toString(rect.getState()))
				.addAttribute("machine_locality",Integer.toString(rect.x1) + "," + Integer.toString(rect.y1) +
						"," + Integer.toString(rect.x2) + "," + Integer.toString(rect.y2));
		LinkedList<Oval> requirements = this.getRequirements();
		for(int i = 0;i < requirements.size();i++){
			Oval oval = requirements.get(i);
			Element requirement = root.addElement("Requirement").addAttribute("requirement_NO",Integer.toString(i))
					.addAttribute("requirement_text",oval.getText())
					.addAttribute("requirement_biaohao",Integer.toString(oval.getBiaohao()))
					.addAttribute("requirement_des",Integer.toString(oval.des))
					.addAttribute("requirement_locality",Integer.toString(oval.x1) + "," + Integer.toString(oval.y1) +
					"," + Integer.toString(oval.x2) + "," + Integer.toString(oval.y2));
		}
		LinkedList<Rect> domains = this.getProblemDomains();
		for(int i = 0;i < domains.size();i++){
			rect = domains.get(i);
			Element domain = root.addElement("ProblemDomain").addAttribute("problemdomain_NO", Integer.toString(i))
					.addAttribute("problemdomain_name",rect.getText())
					.addAttribute("problemdomain_shortname",rect.getShortName())
					.addAttribute("problemdomain_cxb",Character.toString(rect.getCxb()))
					.addAttribute("problemdomain_state",Integer.toString(rect.getState()))
					.addAttribute("problemdomain_locality",Integer.toString(rect.x1) + "," + Integer.toString(rect.y1) +
							"," + Integer.toString(rect.x2) + "," + Integer.toString(rect.y2));
		}
		LinkedList<Line> lines = this.getLines();
		for(int i = 0;i < lines.size();i++){
			Line line = lines.get(i);
			if(line.getState() == 0){
				Element Interface = root.addElement("Interface").addAttribute("line1_description",line.getDescription())
						.addAttribute("line1_name",line.name)
						.addAttribute("line1_locality",Integer.toString(line.x1)+ "," + Integer.toString(line.y1) +
								"," + Integer.toString(line.x2) + "," + Integer.toString(line.y2))
						.addAttribute("line1_tofrom",((Rect)line.to).getShortName() + "," + ((Rect)line.from).getShortName());
				for(int j = 0;j < line.phenomenons.size();j++){
					Phenomenon phenomenon = (Phenomenon) line.phenomenons.get(j);
					Element phenomenonElement = Interface.addElement("Phenomenon").addAttribute("name",phenomenon.getName())
							.addAttribute("state",phenomenon.getState())
							.addAttribute("from",phenomenon.getFrom().getText())
							.addAttribute("to",phenomenon.getTo().getText())
							.addAttribute("constraining",phenomenon.getConstraining() ? "true" : "false")
							.addAttribute("name",phenomenon.getName())
							.addAttribute("biaohao",((Integer)phenomenon.getBiaohao()).toString());
				}
			}
			else if(line.getState() == 2){
				Element constraint = root.addElement("Constraint").addAttribute("line2_description",line.getDescription())
						.addAttribute("line2_name",line.name)
						.addAttribute("line2_locality",Integer.toString(line.x1)+ "," + Integer.toString(line.y1) +
								"," + Integer.toString(line.x2) + "," + Integer.toString(line.y2))
						.addAttribute("line2_tofrom",((Rect)line.to).getShortName() + "," + ((Oval)line.from).getText());
				for(int j = 0;j < line.phenomenons.size();j++){
					Phenomenon phenomenon = (Phenomenon) line.phenomenons.get(j);
					Element phenomenonElement = constraint.addElement("Phenomenon").addAttribute("name",phenomenon.getName())
							.addAttribute("state",phenomenon.getState())
							.addAttribute("from",phenomenon.getFrom().getText())
							.addAttribute("to",phenomenon.getTo().getText())
							.addAttribute("constraining",phenomenon.getConstraining() ? "true" : "false")
							.addAttribute("name",phenomenon.getName())
							.addAttribute("biaohao",((Integer)phenomenon.getBiaohao()).toString())
							.addAttribute("requirement",((Integer)phenomenon.getRequirement().getBiaohao()).toString());
				}
			}
			else{
				Element reference = root.addElement("Reference").addAttribute("line2_description",line.getDescription())
						.addAttribute("line2_name",line.name)
						.addAttribute("line2_locality",Integer.toString(line.x1)+ "," + Integer.toString(line.y1) +
								"," + Integer.toString(line.x2) + "," + Integer.toString(line.y2))
						.addAttribute("line2_tofrom",((Rect)line.to).getShortName() + "," + ((Oval)line.from).getText());
				for(int j = 0;j < line.phenomenons.size();j++){
					Phenomenon phenomenon = (Phenomenon) line.phenomenons.get(j);
					Element phenomenonElement = reference.addElement("Phenomenon").addAttribute("name",phenomenon.getName())
							.addAttribute("state",phenomenon.getState())
							.addAttribute("from",phenomenon.getFrom().getText())
							.addAttribute("to",phenomenon.getTo().getText())
							.addAttribute("constraining",phenomenon.getConstraining() ? "true" : "false")
							.addAttribute("name",phenomenon.getName())
							.addAttribute("biaohao",((Integer)phenomenon.getBiaohao()).toString())
							.addAttribute("requirement",((Integer)phenomenon.getRequirement().getBiaohao()).toString());
				}
			}
		}
		return document;
	}

	public static String getFilePath(String path){
		int index = path.lastIndexOf('\\');
		String result = path.substring(0,index+1);
		return result;
	}

}