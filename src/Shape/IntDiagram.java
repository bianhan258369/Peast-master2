package Shape;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.awt.Graphics;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class IntDiagram implements Serializable {
	
	/****************************************************/
	private static final long serialVersionUID = -6036556398408475676L;
	/****************************************************/
	private LinkedList changjing = new LinkedList();
	private LinkedList jiaohu = new LinkedList();
	private String title;
	private int biaohao;
	HashMap<Integer, Integer> replaceChangjing = new HashMap<>();//前一个是场景的index，后一个是交互的编号

	public int getBiaohao() {
		return this.biaohao;
	}

	public IntDiagram(String title, int biaohao) {
		this.title = title;
		this.biaohao = biaohao;
	}

	public IntDiagram(String title, int biaohao, File file){
		this.title = title;
		this.biaohao = biaohao;
		System.out.println(title);
		try{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(file);
			Element root = document.getRootElement().elementIterator("data").next();
			Element temp;

			Element intNode = root.elementIterator("IntNode").next();
			Element lineNode = root.elementIterator("LineNode").next();

			Element actIntNode = intNode.elementIterator("ActIntNode").next();
			for(Iterator i = actIntNode.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[] = temp.attributeValue("middleXY").split(",");
				int x = Integer.parseInt(str[0]);
				int y = Integer.parseInt(str[1]);
				int number = Integer.parseInt(temp.attributeValue("number"));
				int state = Integer.parseInt(temp.attributeValue("state"));
				String name = temp.attributeValue("name");
				Jiaohu tempJiaohu = new Jiaohu(x,y,number,state);
				tempJiaohu.setName(name);
				jiaohu.add(tempJiaohu);
			}

			Element expectIntNode = intNode.elementIterator("ExpectIntNode").next();
			for(Iterator i = expectIntNode.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[] = temp.attributeValue("middleXY").split(",");
				int x = Integer.parseInt(str[0]);
				int y = Integer.parseInt(str[1]);
				int number = Integer.parseInt(temp.attributeValue("number"));
				int state = Integer.parseInt(temp.attributeValue("state"));
				String name = temp.attributeValue("name");
				Jiaohu tempJiaohu = new Jiaohu(x,y,number,state);
				tempJiaohu.setName(name);
				jiaohu.add(tempJiaohu);
			}

			Element actCause = lineNode.elementIterator("ActCause").next();
			for(Iterator i = actCause.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("fromXY").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("fromXY").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("fromNum"));
				int fState = Integer.parseInt(temp.attributeValue("fromState"));
				String fName = temp.attributeValue("fromName");
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("toXY").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("toXY").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("toNum"));
				int tState = Integer.parseInt(temp.attributeValue("toState"));
				String tName = temp.attributeValue("toName");
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				int state = Integer.parseInt(temp.attributeValue("state"));
				Changjing tempChangjing = new Changjing(list, from, to,state);
				changjing.add(tempChangjing);
			}

			Element actOrder = lineNode.elementIterator("ActOrder").next();
			for(Iterator i = actOrder.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("fromXY").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("fromXY").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("fromNum"));
				int fState = Integer.parseInt(temp.attributeValue("fromState"));
				String fName = temp.attributeValue("fromName");
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("toXY").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("toXY").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("toNum"));
				int tState = Integer.parseInt(temp.attributeValue("toState"));
				String tName = temp.attributeValue("toName");
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				if((fState != 0 && fState != 1) || (tState != 0 && tState != 1)) continue;
				to.setName(tName);
				int state = Integer.parseInt(temp.attributeValue("state"));
				Changjing tempChangjing = new Changjing(list, from, to,state);
				changjing.add(tempChangjing);
			}

			Element synchrony = lineNode.elementIterator("Synchrony").next();
			for(Iterator i = synchrony.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("fromXY").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("fromXY").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("fromNum"));
				int fState = Integer.parseInt(temp.attributeValue("fromState"));
				String fName = temp.attributeValue("fromName");
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("toXY").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("toXY").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("toNum"));
				int tState = Integer.parseInt(temp.attributeValue("toState"));
				String tName = temp.attributeValue("toName");
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				int state = Integer.parseInt(temp.attributeValue("state"));
				Changjing tempChangjing = new Changjing(list, from, to,state);
				changjing.add(tempChangjing);
			}

			Element expectOrder = lineNode.elementIterator("ExpectOrder").next();
			for(Iterator i = expectOrder.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("fromXY").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("fromXY").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("fromNum"));
				int fState = Integer.parseInt(temp.attributeValue("fromState"));
				String fName = temp.attributeValue("fromName");
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("toXY").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("toXY").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("toNum"));
				int tState = Integer.parseInt(temp.attributeValue("toState"));
				String tName = temp.attributeValue("toName");
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				if((fState != 0 && fState != 1) || (tState != 0 && tState != 1)) continue;
				int state = Integer.parseInt(temp.attributeValue("state"));
				Changjing tempChangjing = new Changjing(list, from, to,state);
				changjing.add(tempChangjing);
			}

			Element expectCause = lineNode.elementIterator("ExpectCause").next();
			for(Iterator i = expectCause.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("fromXY").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("fromXY").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("fromNum"));
				int fState = Integer.parseInt(temp.attributeValue("fromState"));
				String fName = temp.attributeValue("fromName");
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("toXY").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("toXY").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("toNum"));
				int tState = Integer.parseInt(temp.attributeValue("toState"));
				String tName = temp.attributeValue("toName");
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				int state = Integer.parseInt(temp.attributeValue("state"));
				Changjing tempChangjing = new Changjing(list, from, to,state);
				changjing.add(tempChangjing);
			}

			for(Iterator i = actOrder.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("fromXY").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("fromXY").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("fromNum"));
				int fState = Integer.parseInt(temp.attributeValue("fromState"));
				String fName = temp.attributeValue("fromName");
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("toXY").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("toXY").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("toNum"));
				int tState = Integer.parseInt(temp.attributeValue("toState"));
				String tName = temp.attributeValue("toName");
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				if(tState == 4){
					Element decisionNode = intNode.elementIterator("DecisionNode").next();
					for(Iterator j = decisionNode.elementIterator("Element");j.hasNext();){
						Element tempDecisionNode = (Element)j.next();
						if(Integer.parseInt(tempDecisionNode.attributeValue("number"))==tNumber){
							int dx1 = Integer.parseInt(tempDecisionNode.attributeValue("to1XY").split(",")[0]);
							int dy1 = Integer.parseInt(tempDecisionNode.attributeValue("to1XY").split(",")[1]);
							int dNumber1 = Integer.parseInt(tempDecisionNode.attributeValue("to1Num"));
							int dState1 = Integer.parseInt(tempDecisionNode.attributeValue("to1State"));
							int dx2 = Integer.parseInt(tempDecisionNode.attributeValue("to2XY").split(",")[0]);
							int dy2 = Integer.parseInt(tempDecisionNode.attributeValue("to2XY").split(",")[1]);
							int dNumber2 = Integer.parseInt(tempDecisionNode.attributeValue("to2Num"));
							int dState2 = Integer.parseInt(tempDecisionNode.attributeValue("to2State"));
							Jiaohu left = new Jiaohu(dx1, dy1, dNumber1, dState1);
							Jiaohu right = new Jiaohu(dx2, dy2, dNumber2, dState2);
							changjing.add(new Changjing(new LinkedList(), from, left, 1));
							changjing.add(new Changjing(new LinkedList(), from, right, 1));
						}
					}
				}
				if(tState == 5){
					Element mergeNode = intNode.elementIterator("MergeNode").next();
					for(Iterator j = mergeNode.elementIterator("Element");j.hasNext();){
						Element tempMergeNode = (Element)j.next();
						int mx = Integer.parseInt(tempMergeNode.attributeValue("toXY").split(",")[0]);
						int my = Integer.parseInt(tempMergeNode.attributeValue("toXY").split(",")[1]);
						int mNumber = Integer.parseInt(tempMergeNode.attributeValue("toNum"));
						int mState = Integer.parseInt(tempMergeNode.attributeValue("toState"));
						Jiaohu merge = new Jiaohu(mx, my, mNumber, mState);
						changjing.add(new Changjing(new LinkedList(), from, merge, 1));
					}
				}
			}

			for(Iterator i = expectOrder.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("fromXY").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("fromXY").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("fromNum"));
				int fState = Integer.parseInt(temp.attributeValue("fromState"));
				String fName = temp.attributeValue("fromName");
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("toXY").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("toXY").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("toNum"));
				int tState = Integer.parseInt(temp.attributeValue("toState"));
				String tName = temp.attributeValue("toName");
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				if(tState == 4){
					Element decisionNode = intNode.elementIterator("DecisionNode").next();
					for(Iterator j = decisionNode.elementIterator("Element");j.hasNext();){
						Element tempDecisionNode = (Element)j.next();
						if(Integer.parseInt(tempDecisionNode.attributeValue("number"))==tNumber){
							int dx1 = Integer.parseInt(tempDecisionNode.attributeValue("to1XY").split(",")[0]);
							int dy1 = Integer.parseInt(tempDecisionNode.attributeValue("to1XY").split(",")[1]);
							int dNumber1 = Integer.parseInt(tempDecisionNode.attributeValue("to1Num"));
							int dState1 = Integer.parseInt(tempDecisionNode.attributeValue("to1State"));
							int dx2 = Integer.parseInt(tempDecisionNode.attributeValue("to2XY").split(",")[0]);
							int dy2 = Integer.parseInt(tempDecisionNode.attributeValue("to2XY").split(",")[1]);
							int dNumber2 = Integer.parseInt(tempDecisionNode.attributeValue("to2Num"));
							int dState2 = Integer.parseInt(tempDecisionNode.attributeValue("to2State"));
							Jiaohu left = new Jiaohu(dx1, dy1, dNumber1, dState1);
							Jiaohu right = new Jiaohu(dx2, dy2, dNumber2, dState2);
							changjing.add(new Changjing(new LinkedList(), from, left, 3));
							changjing.add(new Changjing(new LinkedList(), from, right, 3));
						}
					}
				}
				if(tState == 5){
					Element mergeNode = intNode.elementIterator("MergeNode").next();
					for(Iterator j = mergeNode.elementIterator("Element");j.hasNext();){
						Element tempMergeNode = (Element)j.next();
						int mx = Integer.parseInt(tempMergeNode.attributeValue("toXY").split(",")[0]);
						int my = Integer.parseInt(tempMergeNode.attributeValue("toXY").split(",")[1]);
						int mNumber = Integer.parseInt(tempMergeNode.attributeValue("toNum"));
						int mState = Integer.parseInt(tempMergeNode.attributeValue("toState"));
						Jiaohu merge = new Jiaohu(mx, my, mNumber, mState);
						changjing.add(new Changjing(new LinkedList(), from, merge, 3));
					}
				}
			}

			Element branchNode = intNode.elementIterator("BranchNode").next();
			for(Iterator i = branchNode.elementIterator("Element");i.hasNext();){
				Element tempBranchNode = (Element)i.next();
				for(Iterator it = tempBranchNode.elementIterator("from");it.hasNext();){
					Element tempFrom = (Element)it.next();
					int fx = Integer.parseInt(tempFrom.attributeValue("middleXY").split(",")[0]);
					int fy = Integer.parseInt(tempFrom.attributeValue("middleXY").split(",")[1]);
					int fNumber = Integer.parseInt(tempFrom.attributeValue("number"));
					int fState = Integer.parseInt(tempFrom.attributeValue("state"));
					if(fState != 0 && fState != 1) continue;
					String fName = tempFrom.attributeValue("name");
					Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
					from.setName(fName);
					for(Iterator j = tempBranchNode.elementIterator("to");j.hasNext();){
						Element temoTo = (Element)j.next();
						int tx = Integer.parseInt(temoTo.attributeValue("middleXY").split(",")[0]);
						int ty = Integer.parseInt(temoTo.attributeValue("middleXY").split(",")[1]);
						int tNumber = Integer.parseInt(temoTo.attributeValue("number"));
						int tState = Integer.parseInt(temoTo.attributeValue("state"));
						if(tState != 0 && tState != 1) continue;
						String tName = temoTo.attributeValue("name");
						Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
						to.setName(tName);
						if (fState == 0) changjing.add(new Changjing(new LinkedList(), from, to, 1));
						if (fState == 1) changjing.add(new Changjing(new LinkedList(), from, to, 3));
					}
				}
			}

			LinkedList<Integer> replaces = new LinkedList<>();
			for(int i = 0;i < changjing.size();i++){
				Changjing tempChangjing = (Changjing) changjing.get(i);
				if(tempChangjing.getState() == 0){
					int index = 0;
					Jiaohu tempFrom = tempChangjing.getFrom();
					Jiaohu tempTo = tempChangjing.getTo();
					LinkedList<Jiaohu> precedent = new LinkedList<>();
					LinkedList<Jiaohu> successor = new LinkedList<>();
					getBehaviourPrecedent(tempFrom, precedent);
					getExpectedSuccessor(tempTo, successor);
					if(precedent.size() > 0) {
						while (index < precedent.size()) {
							getBehaviourPrecedent(precedent.get(index), precedent);
							index++;
						}
					}
					if(successor.size() > 0){
						index = 0;
						while(index < successor.size()){
							getExpectedSuccessor(successor.get(index), successor);
							index++;
						}
					}
					for(int j = 0;j < precedent.size();j++){
						Jiaohu tempPrecedent = precedent.get(j);
						for(int k = 0;k < successor.size();k++){
							Jiaohu tempSuccessor = successor.get(k);
							if(tempPrecedent.getNumber() == tempSuccessor.getNumber()){
								if(!replaces.contains(i)) replaces.add(i);
							}
						}
					}
				}
			}

			for(int i = 0;i < replaces.size();i++){
				Changjing replace = (Changjing) changjing.get(replaces.get(i));
				changjing.remove(replace);
				changjing.add(new Changjing(replace.getDian(), replace.getTo(), replace.getFrom(), replace.getState()));
			}
		}catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public LinkedList getJiaohu() {
		return this.jiaohu;
	}

	public LinkedList getChangjing() {
		return this.changjing;
	}

	public boolean check1() {
		for (int i = 0; i <= this.changjing.size() - 1; i++) {
			Changjing tmp_c = (Changjing) this.changjing.get(i);
			Jiaohu tmp_1 = tmp_c.getFrom();
			Jiaohu tmp_2 = tmp_c.getTo();
			if (tmp_c.getState() == 3) {
				if ((tmp_1.getState() == 1) && (tmp_2.getState() == 1)) {
					continue;
				}
				UI.Main.errmes = "Interaction" + tmp_1.getNumber()
						+ " and Interaction" + tmp_2.getNumber()
						+ " have a wrong relationship!";

				UI.Main.errstate = 1;
				return false;
			}

			if (tmp_c.getState() == 1) {
				if ((tmp_1.getState() == 0) && (tmp_2.getState() == 0)) {
					continue;
				}
				UI.Main.errmes = "Interaction" + tmp_1.getNumber()
						+ " and Interaction" + tmp_2.getNumber()
						+ " have a wrong relationship!";

				UI.Main.errstate = 1;
				return false;
			}

			if (tmp_c.getState() == 0) {
				if ((tmp_1.getState() == 0) && (tmp_2.getState() == 1)) {
					continue;
				}
				UI.Main.errmes = "Interaction" + tmp_1.getNumber()
						+ " and Interaction" + tmp_2.getNumber()
						+ " have a wrong relationship!";

				UI.Main.errstate = 1;
				return false;
			}

			if (tmp_c.getState() == 4) {
				if ((tmp_1.getState() == 1) && (tmp_2.getState() == 0)) {
					continue;
				}
				UI.Main.errmes = "Interaction" + tmp_1.getNumber()
						+ " and Interaction" + tmp_2.getNumber()
						+ " have a wrong relationship!";

				UI.Main.errstate = 1;
				return false;
			}

			if (tmp_c.getState() == 2) {
				if (((tmp_1.getState() == 1) && (tmp_2.getState() == 0))
						|| ((tmp_1.getState() == 0) && (tmp_2.getState() == 1))) {
					if (tmp_1.getNumber() == tmp_2.getNumber()) {
						continue;
					}
					UI.Main.errmes = "Interaction" + tmp_1.getNumber()
							+ " and Interaction" + tmp_2.getNumber()
							+ " have a wrong relationship!";

					UI.Main.errstate = 1;
					return false;
				}

				UI.Main.errmes = "Interaction" + tmp_1.getNumber()
						+ " and Interaction" + tmp_2.getNumber()
						+ " have a wrong relationship!";

				UI.Main.errstate = 1;
				return false;
			}
		}

		return true;
	}

	public boolean check2() {
		boolean re = true;
		for (int i = 0; i <= this.jiaohu.size() - 1; i++) {
			Jiaohu tmp_j = (Jiaohu) this.jiaohu.get(i);

			if (tmp_j.getState() == 1) {
				boolean zhaodao = false;
				for (int j = 0; j <= this.changjing.size() - 1; j++) {
					Changjing tmp_c = (Changjing) this.changjing.get(j);
					if ((tmp_c.getTo().equals(tmp_j))
							&& (tmp_c.getState() == 0)) {
						zhaodao = true;
						break;
					}
					if ((tmp_c.getFrom().equals(tmp_j))
							&& (tmp_c.getState() == 4)) {
						zhaodao = true;
						break;
					}
					if ((tmp_c.getFrom().equals(tmp_j))
							&& (tmp_c.getState() == 2)) {
						zhaodao = true;
						break;
					}
					if ((tmp_c.getTo().equals(tmp_j))
							&& (tmp_c.getState() == 2)) {
						zhaodao = true;
						break;
					}
				}
				if (!zhaodao) {
					UI.Main.errmes = "Interaction" + tmp_j.getNumber()
							+ "(r) does not confirm!";

					UI.Main.errstate = 2;
					return false;
				}
			}
		}
		return true;
	}

	public void addChangjing(Changjing cj) {
		this.changjing.add(cj);
	}

	public void addJiaohu(Jiaohu jh) {
		this.jiaohu.add(jh);
	}

	public void setTitle(String s) {
		this.title = s;
	}

	public String getTitle() {
		return this.title;
	}

	public void draw(Graphics g) {
		for (int i = 0; i <= this.jiaohu.size() - 1; i++) {
			Jiaohu jh = (Jiaohu) this.jiaohu.get(i);
			jh.draw(g);
		}
		for (int i = 0; i <= this.changjing.size() - 1; i++) {
			Changjing cj = (Changjing) this.changjing.get(i);
			cj.draw(g);
		}
	}

	public Changjing which(int x, int y) {
		for (int i = 0; i <= this.changjing.size() - 1; i++) {
			Changjing tmp_cj = (Changjing) this.changjing.get(i);
			if (tmp_cj.in(x, y)) {
				return tmp_cj;
			}
		}
		return null;
	}

	public Changjing getSelected(int x, int y) {
		for (int i = 0; i <= this.changjing.size() - 1; i++) {
			Changjing tmp_cj = (Changjing) this.changjing.get(i);
			int s = tmp_cj.getSelected(x, y);
			if (s < tmp_cj.getDian().size()) {
				return tmp_cj;
			}
		}
		return null;
	}

	public Jiaohu getSelecte(int x, int y) {
		for (int i = 0; i <= this.jiaohu.size() - 1; i++) {
			Jiaohu tmp_jh = (Jiaohu) this.jiaohu.get(i);
			if (tmp_jh.isIn(x, y)) {
				return tmp_jh;
			}
		}
		return null;
	}

	public void deletecj(Changjing cj) {
		for (int i = 0; i <= this.changjing.size() - 1; i++)
			if (cj.equals((Changjing) this.changjing.get(i))) {
				this.changjing.remove(i);
				break;
			}
	}

	public void deletejh(Jiaohu jh) {
		for (int i = 0; i <= this.jiaohu.size() - 1; i++) {
			Jiaohu jht = (Jiaohu) this.jiaohu.get(i);
			if (jh.equals(jht)) {
				this.jiaohu.remove(i);
				for (int j = 0; j <= this.changjing.size() - 1; j++) {
					Changjing cj = (Changjing) this.changjing.get(j);
					if ((cj.getFrom().equals(jht)) || (cj.getTo().equals(jht))) {
						this.changjing.remove(j);
						j--;
					}
				}
				break;
			}
		}
	}

	public void follow(Jiaohu jh) {
		for (int i = 0; i <= this.changjing.size() - 1; i++) {
			Changjing tmp_cj = (Changjing) this.changjing.get(i);
			if ((jh.equals(tmp_cj.getFrom())) || (jh.equals(tmp_cj.getTo())))
				tmp_cj.refresh();
		}
	}

	//找到一个行为交互的所有前驱
	private void getBehaviourPrecedent(Jiaohu jiaohu, LinkedList<Jiaohu> Precedent){
		for(int i = 0;i < changjing.size();i++){
			Changjing tempChangjing = (Changjing) changjing.get(i);
			if(tempChangjing.getState() == 1 && tempChangjing.getTo().getNumber() == jiaohu.getNumber()){
				Precedent.add(tempChangjing.getFrom());
			}
		}
	}

	//找到一个期望交互的所有后继
	private void getExpectedSuccessor(Jiaohu jiaohu, LinkedList<Jiaohu> successor){
		for(int i = 0;i < changjing.size();i++){
			Changjing tempChangjing = (Changjing) changjing.get(i);
			if(tempChangjing.getState() == 3 && tempChangjing.getFrom().getNumber() == jiaohu.getNumber()){
				successor.add(tempChangjing.getTo());
			}
		}
	}
}