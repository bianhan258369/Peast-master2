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

public class IntDiagram implements Serializable,Cloneable {
	
	/****************************************************/
	private static final long serialVersionUID = -6036556398408475676L;
	/****************************************************/
	private LinkedList changjing = new LinkedList();
	private LinkedList jiaohu = new LinkedList();
	private String title;
	private int biaohao;

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
		try{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(file);
			Element root = document.getRootElement().elementIterator("ScenarioGraph").next();
			Element temp;

			Element intNode = root.elementIterator("IntNode").next();
			Element controlNode = root.elementIterator("ControlNode").next();
			Element lineNode = root.elementIterator("LineList").next();

			Element actIntNode = intNode.elementIterator("ActIntNode").next();
			for(Iterator i = actIntNode.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[] = temp.attributeValue("node_locality").split(",");
				int x = Integer.parseInt(str[0]);
				int y = Integer.parseInt(str[1]);
				int number = Integer.parseInt(temp.attributeValue("node_no"));
				int state = 0;
				String name = "int";
				Jiaohu tempJiaohu = new Jiaohu(x,y,number,state);
				tempJiaohu.setName(name);
				jiaohu.add(tempJiaohu);
			}

			Element expectIntNode = intNode.elementIterator("ExpectIntNode").next();
			for(Iterator i = expectIntNode.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[] = temp.attributeValue("node_locality").split(",");
				int x = Integer.parseInt(str[0]);
				int y = Integer.parseInt(str[1]);
				int number = Integer.parseInt(temp.attributeValue("node_no"));
				int state = 1;
				String name = "int";
				Jiaohu tempJiaohu = new Jiaohu(x,y,number,state);
				tempJiaohu.setName(name);
				jiaohu.add(tempJiaohu);
			}

			Element actCause = lineNode.elementIterator("BehEnable").next();
			for(Iterator i = actCause.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("from_locality").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("from_locality").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("from_no"));
				int fState = -1;
				if(temp.attributeValue("from_type").equals("Start")) fState = 2;
				else if(temp.attributeValue("from_type").equals("Start")) fState = 0;
				else if(temp.attributeValue("from_type").equals("ExpInt")) fState = 1;
				else if(temp.attributeValue("from_type").equals("End")) fState = 3;
				else if(temp.attributeValue("from_type").equals("Decision")) fState = 4;
				else if(temp.attributeValue("from_type").equals("Merge")) fState = 5;
				else if(temp.attributeValue("from_type").equals("Branch")) fState = 6;
				String fName = "int";
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("to_locality").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("to_locality").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("to_no"));
				int tState = -1;
				if(temp.attributeValue("to_type").equals("Start")) tState = 2;
				else if(temp.attributeValue("to_type").equals("Start")) tState = 0;
				else if(temp.attributeValue("to_type").equals("ExpInt")) tState = 1;
				else if(temp.attributeValue("to_type").equals("End")) tState = 3;
				else if(temp.attributeValue("to_type").equals("Decision")) tState = 4;
				else if(temp.attributeValue("to_type").equals("Merge")) tState = 5;
				else if(temp.attributeValue("to_type").equals("Branch")) tState = 6;
				String tName = "int";
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				int state = 0;
				Changjing tempChangjing = new Changjing(list, from, to,state);
				changjing.add(tempChangjing);
			}

			Element actOrder = lineNode.elementIterator("BehOrder").next();
			for(Iterator i = actOrder.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("from_locality").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("from_locality").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("from_no"));
				int fState = -1;
				if(temp.attributeValue("from_type").equals("Start")) fState = 2;
				else if(temp.attributeValue("from_type").equals("Start")) fState = 0;
				else if(temp.attributeValue("from_type").equals("ExpInt")) fState = 1;
				else if(temp.attributeValue("from_type").equals("End")) fState = 3;
				else if(temp.attributeValue("from_type").equals("Decision")) fState = 4;
				else if(temp.attributeValue("from_type").equals("Merge")) fState = 5;
				else if(temp.attributeValue("from_type").equals("Branch")) fState = 6;
				String fName = "int";
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("to_locality").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("to_locality").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("to_no"));
				int tState = -1;
				if(temp.attributeValue("to_type").equals("Start")) tState = 2;
				else if(temp.attributeValue("to_type").equals("Start")) tState = 0;
				else if(temp.attributeValue("to_type").equals("ExpInt")) tState = 1;
				else if(temp.attributeValue("to_type").equals("End")) tState = 3;
				else if(temp.attributeValue("to_type").equals("Decision")) tState = 4;
				else if(temp.attributeValue("to_type").equals("Merge")) tState = 5;
				else if(temp.attributeValue("to_type").equals("Branch")) tState = 6;
				String tName = "int";
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				int state = 1;
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
				int fx = Integer.parseInt(temp.attributeValue("from_locality").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("from_locality").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("from_no"));
				int fState = -1;
				if(temp.attributeValue("from_type").equals("Start")) fState = 2;
				else if(temp.attributeValue("from_type").equals("Start")) fState = 0;
				else if(temp.attributeValue("from_type").equals("ExpInt")) fState = 1;
				else if(temp.attributeValue("from_type").equals("End")) fState = 3;
				else if(temp.attributeValue("from_type").equals("Decision")) fState = 4;
				else if(temp.attributeValue("from_type").equals("Merge")) fState = 5;
				else if(temp.attributeValue("from_type").equals("Branch")) fState = 6;
				String fName = "int";
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("to_locality").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("to_locality").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("to_no"));
				int tState = -1;
				if(temp.attributeValue("to_type").equals("Start")) tState = 2;
				else if(temp.attributeValue("to_type").equals("Start")) tState = 0;
				else if(temp.attributeValue("to_type").equals("ExpInt")) tState = 1;
				else if(temp.attributeValue("to_type").equals("End")) tState = 3;
				else if(temp.attributeValue("to_type").equals("Decision")) tState = 4;
				else if(temp.attributeValue("to_type").equals("Merge")) tState = 5;
				else if(temp.attributeValue("to_type").equals("Branch")) tState = 6;
				String tName = "int";
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				int state = 2;
				Changjing tempChangjing = new Changjing(list, from, to,state);
				changjing.add(tempChangjing);
			}

			Element expectOrder = lineNode.elementIterator("ExpOrder").next();
			for(Iterator i = expectOrder.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("from_locality").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("from_locality").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("from_no"));
				int fState = -1;
				if(temp.attributeValue("from_type").equals("Start")) fState = 2;
				else if(temp.attributeValue("from_type").equals("Start")) fState = 0;
				else if(temp.attributeValue("from_type").equals("ExpInt")) fState = 1;
				else if(temp.attributeValue("from_type").equals("End")) fState = 3;
				else if(temp.attributeValue("from_type").equals("Decision")) fState = 4;
				else if(temp.attributeValue("from_type").equals("Merge")) fState = 5;
				else if(temp.attributeValue("from_type").equals("Branch")) fState = 6;
				String fName = "int";
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("to_locality").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("to_locality").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("to_no"));
				int tState = -1;
				if(temp.attributeValue("to_type").equals("Start")) tState = 2;
				else if(temp.attributeValue("to_type").equals("Start")) tState = 0;
				else if(temp.attributeValue("to_type").equals("ExpInt")) tState = 1;
				else if(temp.attributeValue("to_type").equals("End")) tState = 3;
				else if(temp.attributeValue("to_type").equals("Decision")) tState = 4;
				else if(temp.attributeValue("to_type").equals("Merge")) tState = 5;
				else if(temp.attributeValue("to_type").equals("Branch")) tState = 6;
				String tName = "int";
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				int state = 3;
				Changjing tempChangjing = new Changjing(list, from, to,state);
				changjing.add(tempChangjing);
			}

			Element expectCause = lineNode.elementIterator("ExpEnable").next();
			for(Iterator i = expectCause.elementIterator("Element");i.hasNext();){
				temp = (Element)i.next();
				String str[];
				if(temp.attributeValue("turnings").contains(",")) str = temp.attributeValue("turnings").split(",");
				else str = new String[0];
				LinkedList list = new LinkedList();
				for(int j = 0;j < str.length;j++) list.add(str[j]);
				int fx = Integer.parseInt(temp.attributeValue("from_locality").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("from_locality").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("from_no"));
				int fState = -1;
				if(temp.attributeValue("from_type").equals("Start")) fState = 2;
				else if(temp.attributeValue("from_type").equals("Start")) fState = 0;
				else if(temp.attributeValue("from_type").equals("ExpInt")) fState = 1;
				else if(temp.attributeValue("from_type").equals("End")) fState = 3;
				else if(temp.attributeValue("from_type").equals("Decision")) fState = 4;
				else if(temp.attributeValue("from_type").equals("Merge")) fState = 5;
				else if(temp.attributeValue("from_type").equals("Branch")) fState = 6;
				String fName = "int";
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("to_locality").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("to_locality").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("to_no"));
				int tState = -1;
				if(temp.attributeValue("to_type").equals("Start")) tState = 2;
				else if(temp.attributeValue("to_type").equals("Start")) tState = 0;
				else if(temp.attributeValue("to_type").equals("ExpInt")) tState = 1;
				else if(temp.attributeValue("to_type").equals("End")) tState = 3;
				else if(temp.attributeValue("to_type").equals("Decision")) tState = 4;
				else if(temp.attributeValue("to_type").equals("Merge")) tState = 5;
				else if(temp.attributeValue("to_type").equals("Branch")) tState = 6;
				String tName = "int";
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				int state = 4;
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
				int fx = Integer.parseInt(temp.attributeValue("from_locality").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("from_locality").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("from_no"));
				int fState = -1;
				if(temp.attributeValue("from_type").equals("Start")) fState = 2;
				else if(temp.attributeValue("from_type").equals("Start")) fState = 0;
				else if(temp.attributeValue("from_type").equals("ExpInt")) fState = 1;
				else if(temp.attributeValue("from_type").equals("End")) fState = 3;
				else if(temp.attributeValue("from_type").equals("Decision")) fState = 4;
				else if(temp.attributeValue("from_type").equals("Merge")) fState = 5;
				else if(temp.attributeValue("from_type").equals("Branch")) fState = 6;
				String fName = "int";
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("to_locality").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("to_locality").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("to_no"));
				int tState = -1;
				if(temp.attributeValue("to_type").equals("Start")) tState = 2;
				else if(temp.attributeValue("to_type").equals("Start")) tState = 0;
				else if(temp.attributeValue("to_type").equals("ExpInt")) tState = 1;
				else if(temp.attributeValue("to_type").equals("End")) tState = 3;
				else if(temp.attributeValue("to_type").equals("Decision")) tState = 4;
				else if(temp.attributeValue("to_type").equals("Merge")) tState = 5;
				else if(temp.attributeValue("to_type").equals("Branch")) tState = 6;
				String tName = "int";
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				if(tState == 4){
					Element decisionNode = controlNode.elementIterator("DecisionNode").next();
					for(Iterator j = decisionNode.elementIterator("Element");j.hasNext();){
						Element tempDecisionNode = (Element)j.next();
						if(Integer.parseInt(tempDecisionNode.attributeValue("node_no"))==tNumber){
							for(Iterator k = tempDecisionNode.elementIterator("to");k.hasNext();){
								Element tempFrom = (Element)k.next();
								int dx = Integer.parseInt(tempFrom.attributeValue("node_locality").split(",")[0]);
								int dy = Integer.parseInt(tempFrom.attributeValue("node_locality").split(",")[1]);
								int dNumber = Integer.parseInt(tempFrom.attributeValue("node_no"));
								int dState = -1;
								if(tempFrom.attributeValue("node_type").equals("Start")) dState = 2;
								else if(tempFrom.attributeValue("node_type").equals("Start")) dState = 0;
								else if(tempFrom.attributeValue("node_type").equals("ExpInt")) dState = 1;
								else if(tempFrom.attributeValue("node_type").equals("End")) dState = 3;
								else if(tempFrom.attributeValue("node_type").equals("Decision")) dState = 4;
								else if(tempFrom.attributeValue("node_type").equals("Merge")) dState = 5;
								else if(tempFrom.attributeValue("node_type").equals("Branch")) dState = 6;
								Jiaohu dJiaohu = new Jiaohu(dx, dy, dNumber, dState);
								changjing.add(new Changjing(new LinkedList(), from, dJiaohu, 1));
							}
						}
					}
				}
				if(tState == 5){
					Element mergeNode = controlNode.elementIterator("MergeNode").next();
					for(Iterator j = mergeNode.elementIterator("Element");j.hasNext();){
						Element tempMergeNode = (Element)j.next();
						Element tempTo = (Element)tempMergeNode.elementIterator("to").next();
						int mx = Integer.parseInt(tempTo.attributeValue("node_locality").split(",")[0]);
						int my = Integer.parseInt(tempTo.attributeValue("node_locality").split(",")[1]);
						int mNumber = Integer.parseInt(tempTo.attributeValue("node_no"));
						int mState = -1;
						if(tempTo.attributeValue("node_type").equals("Start")) mState = 2;
						else if(tempTo.attributeValue("node_type").equals("Start")) mState = 0;
						else if(tempTo.attributeValue("node_type").equals("ExpInt")) mState = 1;
						else if(tempTo.attributeValue("node_type").equals("End")) mState = 3;
						else if(tempTo.attributeValue("node_type").equals("Decision")) mState = 4;
						else if(tempTo.attributeValue("node_type").equals("Merge")) mState = 5;
						else if(tempTo.attributeValue("node_type").equals("Branch")) mState = 6;
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
				int fx = Integer.parseInt(temp.attributeValue("from_locality").split(",")[0]);
				int fy = Integer.parseInt(temp.attributeValue("from_locality").split(",")[1]);
				int fNumber = Integer.parseInt(temp.attributeValue("from_no"));
				int fState = -1;
				if(temp.attributeValue("from_type").equals("Start")) fState = 2;
				else if(temp.attributeValue("from_type").equals("Start")) fState = 0;
				else if(temp.attributeValue("from_type").equals("ExpInt")) fState = 1;
				else if(temp.attributeValue("from_type").equals("End")) fState = 3;
				else if(temp.attributeValue("from_type").equals("Decision")) fState = 4;
				else if(temp.attributeValue("from_type").equals("Merge")) fState = 5;
				else if(temp.attributeValue("from_type").equals("Branch")) fState = 6;
				String fName = "int";
				Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
				from.setName(fName);
				int tx = Integer.parseInt(temp.attributeValue("to_locality").split(",")[0]);
				int ty = Integer.parseInt(temp.attributeValue("to_locality").split(",")[1]);
				int tNumber = Integer.parseInt(temp.attributeValue("to_no"));
				int tState = -1;
				if(temp.attributeValue("to_type").equals("Start")) tState = 2;
				else if(temp.attributeValue("to_type").equals("Start")) tState = 0;
				else if(temp.attributeValue("to_type").equals("ExpInt")) tState = 1;
				else if(temp.attributeValue("to_type").equals("End")) tState = 3;
				else if(temp.attributeValue("to_type").equals("Decision")) tState = 4;
				else if(temp.attributeValue("to_type").equals("Merge")) tState = 5;
				else if(temp.attributeValue("to_type").equals("Branch")) tState = 6;
				String tName = "int";
				Jiaohu to = new Jiaohu(tx, ty, tNumber, tState);
				to.setName(tName);
				if(tState == 4){
					Element decisionNode = controlNode.elementIterator("DecisionNode").next();
					for(Iterator j = decisionNode.elementIterator("Element");j.hasNext();){
						Element tempDecisionNode = (Element)j.next();
						if(Integer.parseInt(tempDecisionNode.attributeValue("node_no"))==tNumber){
							for(Iterator k = tempDecisionNode.elementIterator("to");k.hasNext();){
								Element tempFrom = (Element)k.next();
								int dx = Integer.parseInt(tempFrom.attributeValue("node_locality").split(",")[0]);
								int dy = Integer.parseInt(tempFrom.attributeValue("node_locality").split(",")[1]);
								int dNumber = Integer.parseInt(tempFrom.attributeValue("node_no"));
								int dState = -1;
								if(tempFrom.attributeValue("node_type").equals("Start")) dState = 2;
								else if(tempFrom.attributeValue("node_type").equals("Start")) dState = 0;
								else if(tempFrom.attributeValue("node_type").equals("ExpInt")) dState = 1;
								else if(tempFrom.attributeValue("node_type").equals("End")) dState = 3;
								else if(tempFrom.attributeValue("node_type").equals("Decision")) dState = 4;
								else if(tempFrom.attributeValue("node_type").equals("Merge")) dState = 5;
								else if(tempFrom.attributeValue("node_type").equals("Branch")) dState = 6;
								Jiaohu dJiaohu = new Jiaohu(dx, dy, dNumber, dState);
								changjing.add(new Changjing(new LinkedList(), from, dJiaohu, 3));
							}
						}
					}
				}
				if(tState == 5){
					Element mergeNode = controlNode.elementIterator("MergeNode").next();
					for(Iterator j = mergeNode.elementIterator("Element");j.hasNext();){
						Element tempMergeNode = (Element)j.next();
						Element tempTo = (Element)tempMergeNode.elementIterator("to").next();
						int mx = Integer.parseInt(tempTo.attributeValue("node_locality").split(",")[0]);
						int my = Integer.parseInt(tempTo.attributeValue("node_locality").split(",")[1]);
						int mNumber = Integer.parseInt(tempTo.attributeValue("node_no"));
						int mState = -1;
						if(tempTo.attributeValue("node_type").equals("Start")) mState = 2;
						else if(tempTo.attributeValue("node_type").equals("Start")) mState = 0;
						else if(tempTo.attributeValue("node_type").equals("ExpInt")) mState = 1;
						else if(tempTo.attributeValue("node_type").equals("End")) mState = 3;
						else if(tempTo.attributeValue("node_type").equals("Decision")) mState = 4;
						else if(tempTo.attributeValue("node_type").equals("Merge")) mState = 5;
						else if(tempTo.attributeValue("node_type").equals("Branch")) mState = 6;
						Jiaohu merge = new Jiaohu(mx, my, mNumber, mState);
						changjing.add(new Changjing(new LinkedList(), from, merge, 3));
					}
				}
			}

			Element branchNode = controlNode.elementIterator("BranchNode").next();
			for(Iterator i = branchNode.elementIterator("Element");i.hasNext();){
				Element tempBranchNode = (Element)i.next();
				for(Iterator it = tempBranchNode.elementIterator("from");it.hasNext();){
					Element tempFrom = (Element)it.next();
					int fx = Integer.parseInt(tempFrom.attributeValue("node_locality").split(",")[0]);
					int fy = Integer.parseInt(tempFrom.attributeValue("node_locality").split(",")[1]);
					int fNumber = Integer.parseInt(tempFrom.attributeValue("node_no"));
					int fState = -1;
					if(tempFrom.attributeValue("node_type").equals("Start")) fState = 2;
					else if(tempFrom.attributeValue("node_type").equals("Start")) fState = 0;
					else if(tempFrom.attributeValue("node_type").equals("ExpInt")) fState = 1;
					else if(tempFrom.attributeValue("node_type").equals("End")) fState = 3;
					else if(tempFrom.attributeValue("node_type").equals("Decision")) fState = 4;
					else if(tempFrom.attributeValue("node_type").equals("Merge")) fState = 5;
					else if(tempFrom.attributeValue("node_type").equals("Branch")) fState = 6;
					if(fState != 0 && fState != 1) continue;
					String fName = "int";
					Jiaohu from = new Jiaohu(fx, fy, fNumber, fState);
					from.setName(fName);
					for(Iterator j = tempBranchNode.elementIterator("to");j.hasNext();){
						Element temoTo = (Element)j.next();
						int tx = Integer.parseInt(temoTo.attributeValue("node_locality").split(",")[0]);
						int ty = Integer.parseInt(temoTo.attributeValue("node_locality").split(",")[1]);
						int tNumber = Integer.parseInt(temoTo.attributeValue("node_no"));
						int tState = -1;
						if(temoTo.attributeValue("node_type").equals("Start")) tState = 2;
						else if(temoTo.attributeValue("node_type").equals("Start")) tState = 0;
						else if(temoTo.attributeValue("node_type").equals("ExpInt")) tState = 1;
						else if(temoTo.attributeValue("node_type").equals("End")) tState = 3;
						else if(temoTo.attributeValue("node_type").equals("Decision")) tState = 4;
						else if(temoTo.attributeValue("node_type").equals("Merge")) tState = 5;
						else if(temoTo.attributeValue("node_type").equals("Branch")) tState = 6;
						if(tState != 0 && tState != 1) continue;
						String tName = "int";
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
				LinkedList dians = replace.getDian();
				LinkedList newDians = new LinkedList();
				newDians.add(dians.get(2));
				newDians.add(dians.get(3));
				newDians.add(dians.get(0));
				newDians.add(dians.get(1));
				changjing.add(new Changjing(newDians, replace.getTo(), replace.getFrom(), replace.getState()));
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		IntDiagram intDiagram = (IntDiagram) super.clone();
		LinkedList<Jiaohu> tempJiaohu = new LinkedList<>();
		LinkedList<Changjing> tempChangjing = new LinkedList<>();
		for(int i = 0;i < this.jiaohu.size();i++){
			tempJiaohu.add((Jiaohu) ((Jiaohu)this.jiaohu.get(i)).clone());
		}
		for(int i = 0;i < this.changjing.size();i++){
			tempChangjing.add((Changjing) ((Changjing)this.changjing.get(i)).clone());
		}
		intDiagram.changjing = tempChangjing;
		intDiagram.jiaohu = tempJiaohu;
		return intDiagram;
	}

}