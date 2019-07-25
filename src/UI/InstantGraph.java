package UI;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

import Shape.*;
import Shape.Colour;
import foundation.Constraint;
import org.eclipse.swt.internal.C;

public class InstantGraph implements Serializable {

	private static final long serialVersionUID = -1776292504828741371L;

	public static int TYPE_NORMAL = 0;
	public static int TYPE_CONSTRUCTION = 1;

	private int type = 0;

	private String name = "";

	private LinkedList<Jiaohu> nowJiaohu = new LinkedList<>();
	private LinkedList<Changjing> nowChangjing = new LinkedList<>();
	private LinkedList<Changjing> changjing = new LinkedList<>();
	private IntDiagram intDiagram = null;
	private LinkedList<Phenomenon> phenomenons = new LinkedList<Phenomenon>();// �������������йص�����
	private LinkedList<String> constraint = new LinkedList<String>();// ��Ŵ�ʱ��ͼ��Լ�����
	private LinkedList<Jiaohu> jiaohu = new LinkedList<Jiaohu>(); // ���潻��
	private Hashtable<Integer, String> weight = new Hashtable<Integer, String>();//���潻����˳��
	static LinkedList<Colour> colors = new LinkedList<>();
	static Map<String, Colour> domainColors = new HashMap<>();
	private Rect domain;// Ҫ����ʱ��ͼ��������
	private Clock clock;
	private Diagram problemDiagram;// ����ͼ

	int originX;// ������ԭ��X����
	int originY;// ������ԭ��Y����

	int length;// �����᳤��

	static {
		colors.add(new Colour(255,228,196));
		colors.add(new Colour(255,0,0));
		colors.add(new Colour(165,42,42));
		colors.add(new Colour(255,165,0));
		colors.add(new Colour(255,255,0));
		colors.add(new Colour(0,128,0));
		colors.add(new Colour(0,255,255));
		colors.add(new Colour(205,133,63));
		colors.add(new Colour(153,50,204));
		colors.add(new Colour(255,0,255));
		colors.add(new Colour(255,182,193));
		colors.add(new Colour(211,211,211));
		colors.add(new Colour(255,127,80));
		colors.add(new Colour(255,215,0));
	}

	public LinkedList<Phenomenon> getPhenomenons() {
		return phenomenons;
	}

	public InstantGraph(Rect domain, Clock clock) {

		originX = 20;
		originY = 60;
		length = 800;
		this.clock = clock;
		this.domain = domain;
		this.problemDiagram = Main.win.myProblemDiagram;
		if (problemDiagram == null)
			return;

		LinkedList temp = problemDiagram.getPhenomenon();
		int state;

		this.setName(domain.getShortName());

		for (int i = 0; i <= temp.size() - 1; i++) {
			state = 0;
			Phenomenon temp_p = (Phenomenon) temp.get(i);
			if (temp_p.getFrom().getShortName().equals(domain.getShortName())
					|| temp_p.getTo().getShortName()
							.equals(domain.getShortName())) {
				 phenomenons.add(temp_p);
			}
		}
		//setOrder();
		try {
			//fixInteractions(this.phenomenons);
		} catch (Exception e){
			e.printStackTrace();
		}

		int interactions_size = phenomenons.size();

		int interval = length / (interactions_size + 1);

		for (int m = 0; m < interactions_size; m++) {
			state = 0;
			Phenomenon temp_p2 = phenomenons.get(m);

			if (temp_p2.getRequirement() != null)
				state = 1;

			Jiaohu tempJh = new TriangleJiaohu(originX + (jiaohu.size() + 1)
					* interval, originY, temp_p2.getBiaohao(), state);

			jiaohu.add(tempJh);
		}
	}

	public InstantGraph(Rect domain, Clock clock, int index, boolean inClockSpecification) throws CloneNotSupportedException {
		if (inClockSpecification) {
			{
				originX = 20;
				originY = 60;
				length = 800;
				this.domain = domain;
				this.clock = clock;
				this.problemDiagram = Main.win.subProblemDiagrams[index];
				this.intDiagram = (IntDiagram) Main.win.myIntDiagram.get(index).clone();
				if (problemDiagram == null) return;

				if (!domainColors.containsKey(domain.getShortName())) {
					//Random random = new Random();
					//int colorIndex = random.nextInt(colors.size());
					int colorIndex = 0;
					clock.setColor(colors.get(colorIndex));
					domainColors.put(domain.getShortName(), colors.get(colorIndex));
					//System.out.println(domain.getShortName() + " : " + colors.get(colorIndex).toString());
					colors.remove(colorIndex);
				} else clock.setColor(domainColors.get(domain.getShortName()));

				LinkedList tempJiaohu = intDiagram.getJiaohu();
				LinkedList tempPhenomenon = problemDiagram.getPhenomenon();
				this.setName(domain.getShortName());

				for (int i = 0; i < tempJiaohu.size(); i++) {
					Jiaohu temp_j = (Jiaohu) tempJiaohu.get(i);
					int number = temp_j.getNumber();
					for (int j = 0; j < tempPhenomenon.size(); j++) {
						Phenomenon phenomenon = (Phenomenon) tempPhenomenon.get(j);
						if (phenomenon.getBiaohao() == number) {
							if (phenomenon.getFrom().getShortName().equals(domain.getShortName()) || phenomenon.getTo().getShortName().equals(domain.getShortName())) {
								if (!phenomenons.contains(phenomenon)) phenomenons.add(phenomenon);
								jiaohu.add(temp_j);
								break;
							}
						}
					}
				}


				LinkedList<Jiaohu> absentBehaviourJiaohu = new LinkedList<>();
				LinkedList<Jiaohu> absentExpectJiaohu = new LinkedList<>();
				LinkedList<Jiaohu> allJiaohu = intDiagram.getJiaohu();
				LinkedList<Changjing> allChangjing = intDiagram.getChangjing();
				Set<Integer> tempBehaviourSet = new HashSet<>();
				Set<Integer> tempExpectSet = new HashSet<>();

				//分别得到当前ig所有的行为交互与期望交互的编号
				for (int i = 0; i < jiaohu.size(); i++) {
					if (jiaohu.get(i).getState() == 0) tempBehaviourSet.add(jiaohu.get(i).getNumber());
					if (jiaohu.get(i).getState() == 1) tempExpectSet.add(jiaohu.get(i).getNumber());
				}


				//分别得到当前ig所没有的行为交互与期望交互的编号
				for (int i = 0; i < allJiaohu.size(); i++) {
					int number = allJiaohu.get(i).getNumber();
					if ((tempBehaviourSet.contains(number) || tempExpectSet.contains(number)) && !nowJiaohu.contains(allJiaohu.get(i)))
						nowJiaohu.add(allJiaohu.get(i));
					if (allJiaohu.get(i).getState() == 0 && !tempBehaviourSet.contains(number))
						absentBehaviourJiaohu.add(allJiaohu.get(i));
					if (allJiaohu.get(i).getState() == 1 && !tempExpectSet.contains(number))
						absentExpectJiaohu.add(allJiaohu.get(i));
				}

				//得到与缺少的交互无关的场景
				for (int i = 0; i < intDiagram.getChangjing().size(); i++) {
					Changjing changjing = (Changjing) intDiagram.getChangjing().get(i);
					Jiaohu from = changjing.getFrom();
					Jiaohu to = changjing.getTo();
					if (nowJiaohu.contains(from) && nowJiaohu.contains(to)
							&& (changjing.getState() == 1 || changjing.getState() == 3)) nowChangjing.add(changjing);
				}


				//添加与缺少的行为交互有关的场景
				LinkedList<Changjing> tempBehaviourChangjing = new LinkedList<>();
				for (int i = 0; i < intDiagram.getChangjing().size(); i++) {
					Changjing changjing = (Changjing) intDiagram.getChangjing().get(i);
					if (changjing.getState() == 1) tempBehaviourChangjing.add(changjing);
				}

				for (int i = 0; i < absentBehaviourJiaohu.size(); i++) {
					Jiaohu absent = absentBehaviourJiaohu.get(i);
					LinkedList<Jiaohu> toAbsent = new LinkedList<>();//指向absent的交互
					LinkedList<Jiaohu> fromAbsent = new LinkedList<>();//来自absent的交互
					Iterator it = tempBehaviourChangjing.iterator();
					while (it.hasNext()) {
						Changjing changjing = (Changjing) it.next();
						Jiaohu from = changjing.getFrom();
						Jiaohu to = changjing.getTo();
						if (from.equals(absent) && !fromAbsent.contains(to)) {
							fromAbsent.add(to);
							it.remove();
						}
						if (to.equals(absent) && !toAbsent.contains(from)) {
							toAbsent.add(from);
							it.remove();
						}
					}
					for (int j = 0; j < fromAbsent.size(); j++) {
						Jiaohu tempFrom = fromAbsent.get(j);
						for (int k = 0; k < toAbsent.size(); k++) {
							Jiaohu tempTo = toAbsent.get(k);
							Changjing add = new Changjing(new LinkedList(), tempTo, tempFrom, 1);
							nowChangjing.add(add);
							tempBehaviourChangjing.add(add);
						}
					}
				}

				//添加与缺少的期望交互有关的场景
				LinkedList<Changjing> tempExpectedChangjing = new LinkedList<>();
				for (int i = 0; i < intDiagram.getChangjing().size(); i++) {
					Changjing changjing = (Changjing) intDiagram.getChangjing().get(i);
					if (changjing.getState() == 3) tempExpectedChangjing.add(changjing);
				}
				for (int i = 0; i < absentExpectJiaohu.size(); i++) {
					Jiaohu absent = absentExpectJiaohu.get(i);
					LinkedList<Jiaohu> toAbsent = new LinkedList<>();//指向absent的交互
					LinkedList<Jiaohu> fromAbsent = new LinkedList<>();//来自absent的交互
					Iterator it = tempExpectedChangjing.iterator();
					while (it.hasNext()) {
						Changjing changjing = (Changjing) it.next();
						Jiaohu from = changjing.getFrom();
						Jiaohu to = changjing.getTo();
						if (from.equals(absent) && !fromAbsent.contains(to)) {
							fromAbsent.add(to);
							it.remove();
						}
						if (to.equals(absent) && !toAbsent.contains(from)) {
							toAbsent.add(from);
							it.remove();
						}
					}
					for (int j = 0; j < fromAbsent.size(); j++) {
						Jiaohu tempFrom = fromAbsent.get(j);
						for (int k = 0; k < toAbsent.size(); k++) {
							Jiaohu tempTo = toAbsent.get(k);
							Changjing add = new Changjing(new LinkedList(), tempTo, tempFrom, 3);
							nowChangjing.add(add);
							tempExpectedChangjing.add(add);
						}
					}
				}

				Iterator it = nowChangjing.iterator();
				while (it.hasNext()) {
					Changjing changjing = (Changjing) it.next();
					Jiaohu from = changjing.getFrom();
					Jiaohu to = changjing.getTo();
					if (!nowJiaohu.contains(from) || !nowJiaohu.contains(to)) it.remove();
				}

				for (int i = 0; i < allChangjing.size(); i++) {
					Changjing changjing = allChangjing.get(i);
					Jiaohu from = changjing.getFrom();
					Jiaohu to = changjing.getTo();
					if ((changjing.getState() == 0 || changjing.getState() == 4) && nowJiaohu.contains(from) && nowJiaohu.contains(to)) {
						nowChangjing.add(changjing);
					}
				}
				this.changjing = allChangjing;
			}
		}
	}

	public InstantGraph(Rect domain, Clock clock, int index) throws CloneNotSupportedException {

		originX = 20;
		originY = 60;
		length = 800;
		this.domain = domain;
		this.clock = clock;
		this.problemDiagram = Main.win.subProblemDiagrams[index];
		this.intDiagram = (IntDiagram) Main.win.myIntDiagram.get(index).clone();
		if (problemDiagram == null) return;

		if(!domainColors.containsKey(domain.getShortName())){
			//Random random = new Random();
			//int colorIndex = random.nextInt(colors.size());
			int colorIndex = 0;
			clock.setColor(colors.get(colorIndex));
			domainColors.put(domain.getShortName(), colors.get(colorIndex));
			System.out.println(domain.getShortName() + " : " + colors.get(colorIndex).toString());
			colors.remove(colorIndex);
		}
		else clock.setColor(domainColors.get(domain.getShortName()));

		LinkedList tempJiaohu = intDiagram.getJiaohu();
		LinkedList tempPhenomenon = problemDiagram.getPhenomenon();
		this.setName(domain.getShortName());

		for(int i = 0;i < tempJiaohu.size();i++){
			Jiaohu temp_j = (Jiaohu) tempJiaohu.get(i);
			int number = temp_j.getNumber();
			for(int j = 0; j <tempPhenomenon.size();j++){
				Phenomenon phenomenon = (Phenomenon) tempPhenomenon.get(j);
				if(phenomenon.getBiaohao() == number){
					if(phenomenon.getFrom().getShortName().equals(domain.getShortName()) || phenomenon.getTo().getShortName().equals(domain.getShortName())){
						if(!phenomenons.contains(phenomenon)) phenomenons.add(phenomenon);
						jiaohu.add(temp_j);
						break;
					}
				}
			}
		}

		/*
		LinkedList tempPhenomenon = problemDiagram.getPhenomenon();
		LinkedList tempJiaohu = intDiagram.getJiaohu();

		this.setName(domain.getShortName());

		for(int i = 0;i < tempJiaohu.size();i++){
			Jiaohu temp_j = (Jiaohu) tempJiaohu.get(i);
			int number = temp_j.getNumber();
			for(int j = 0; j <tempPhenomenon.size();j++){
				Phenomenon phenomenon = (Phenomenon) tempPhenomenon.get(j);
				if(phenomenon.getBiaohao() == number){
					if(phenomenon.getFrom().getShortName().equals(domain.getShortName()) || phenomenon.getTo().getShortName().equals(domain.getShortName())){
						if(!phenomenons.contains(phenomenon)) phenomenons.add(phenomenon);
						jiaohu.add(temp_j);
						break;
					}
				}
			}
		}

		LinkedList<Jiaohu> absentBehaviourJiaohu = new LinkedList<>();
		LinkedList<Jiaohu> absentExpectJiaohu = new LinkedList<>();
		LinkedList<Jiaohu> allJiaohu = intDiagram.getJiaohu();
		LinkedList<Changjing> allChangjing = intDiagram.getChangjing();
		Set<Integer> tempBehaviourSet = new HashSet<>();
		Set<Integer> tempExpectSet = new HashSet<>();

		//分别得到当前ig所有的行为交互与期望交互的编号
		for(int i = 0;i < jiaohu.size();i++){
			if(jiaohu.get(i).getState() == 0) tempBehaviourSet.add(jiaohu.get(i).getNumber());
			if(jiaohu.get(i).getState() == 1) tempExpectSet.add(jiaohu.get(i).getNumber());
		}


		//分别得到当前ig所没有的行为交互与期望交互的编号
		for(int i = 0;i < allJiaohu.size();i++){
			int number = allJiaohu.get(i).getNumber();
			if((tempBehaviourSet.contains(number) || tempExpectSet.contains(number)) && !nowJiaohu.contains(allJiaohu.get(i))) nowJiaohu.add(allJiaohu.get(i));
			if(allJiaohu.get(i).getState() == 0 && !tempBehaviourSet.contains(number)) absentBehaviourJiaohu.add(allJiaohu.get(i));
			if(allJiaohu.get(i).getState() == 1 && !tempExpectSet.contains(number)) absentExpectJiaohu.add(allJiaohu.get(i));
		}

		//得到与缺少的交互无关的场景
		for(int i = 0;i < intDiagram.getChangjing().size();i++){
			Changjing changjing = (Changjing) intDiagram.getChangjing().get(i);
			Jiaohu from = changjing.getFrom();
			Jiaohu to = changjing.getTo();
			if(nowJiaohu.contains(from) && nowJiaohu.contains(to)
					&& (changjing.getState() == 1 || changjing.getState() == 3)) nowChangjing.add(changjing);
		}


		//添加与缺少的行为交互有关的场景
		LinkedList<Changjing> tempBehaviourChangjing = new LinkedList<>();
		for(int i = 0;i < intDiagram.getChangjing().size();i++){
			Changjing changjing = (Changjing) intDiagram.getChangjing().get(i);
			if(changjing.getState() == 1) tempBehaviourChangjing.add(changjing);
		}

		for(int i = 0;i < absentBehaviourJiaohu.size();i++){
			Jiaohu absent = absentBehaviourJiaohu.get(i);
			LinkedList<Jiaohu> toAbsent = new LinkedList<>();//指向absent的交互
			LinkedList<Jiaohu> fromAbsent = new LinkedList<>();//来自absent的交互
			Iterator it = tempBehaviourChangjing.iterator();
			while(it.hasNext()){
				Changjing changjing = (Changjing) it.next();
				Jiaohu from = changjing.getFrom();
				Jiaohu to = changjing.getTo();
				if(from.equals(absent) && !fromAbsent.contains(to)){
					fromAbsent.add(to);
					it.remove();
				}
				if(to.equals(absent) && !toAbsent.contains(from)){
					toAbsent.add(from);
					it.remove();
				}
			}
			for(int j = 0;j < fromAbsent.size();j++){
				Jiaohu tempFrom = fromAbsent.get(j);
				for(int k = 0;k < toAbsent.size();k++){
					Jiaohu tempTo = toAbsent.get(k);
					Changjing add= new Changjing(new LinkedList(),tempTo,tempFrom,1);
					nowChangjing.add(add);
					tempBehaviourChangjing.add(add);
				}
			}
		}

		//添加与缺少的期望交互有关的场景
		LinkedList<Changjing> tempExpectedChangjing = new LinkedList<>();
		for(int i = 0;i < intDiagram.getChangjing().size();i++){
			Changjing changjing = (Changjing) intDiagram.getChangjing().get(i);
			if(changjing.getState() == 3) tempExpectedChangjing.add(changjing);
		}
		for(int i = 0;i < absentExpectJiaohu.size();i++){
			Jiaohu absent = absentExpectJiaohu.get(i);
			LinkedList<Jiaohu> toAbsent = new LinkedList<>();//指向absent的交互
			LinkedList<Jiaohu> fromAbsent = new LinkedList<>();//来自absent的交互
			Iterator it = tempExpectedChangjing.iterator();
			while(it.hasNext()){
				Changjing changjing = (Changjing) it.next();
				Jiaohu from = changjing.getFrom();
				Jiaohu to = changjing.getTo();
				if(from.equals(absent) && !fromAbsent.contains(to)){
					fromAbsent.add(to);
					it.remove();
				}
				if(to.equals(absent) && !toAbsent.contains(from)){
					toAbsent.add(from);
					it.remove();
				}
			}
			for(int j = 0;j < fromAbsent.size();j++){
				Jiaohu tempFrom = fromAbsent.get(j);
				for(int k = 0;k < toAbsent.size();k++){
					Jiaohu tempTo = toAbsent.get(k);
					Changjing add= new Changjing(new LinkedList(),tempTo,tempFrom,3);
					nowChangjing.add(add);
					tempExpectedChangjing.add(add);
				}
			}
		}

		Iterator it = nowChangjing.iterator();
		while(it.hasNext()){
			Changjing changjing = (Changjing) it.next();
			Jiaohu from = changjing.getFrom();
			Jiaohu to = changjing.getTo();
			if(!nowJiaohu.contains(from) || !nowJiaohu.contains(to)) it.remove();
		}

		for(int i = 0;i < allChangjing.size();i++){
			Changjing changjing = allChangjing.get(i);
			Jiaohu from = changjing.getFrom();
			Jiaohu to = changjing.getTo();
			if((changjing.getState() == 0 || changjing.getState() == 4) && nowJiaohu.contains(from) && nowJiaohu.contains(to)){
				nowChangjing.add(changjing);
			}
		}

		this.changjing = allChangjing;


		/*
		// ɸѡ�����������������ص�����
		for (int i = 0; i <= temp.size() - 1; i++) {
			state = 0;
			Phenomenon temp_p = (Phenomenon) temp.get(i);
			if (temp_p.getFrom().getShortName().equals(domain.getShortName())
					|| temp_p.getTo().getShortName()
					.equals(domain.getShortName())) {
				int biaohao = temp_p.getBiaohao();
				for(int j = 0;j < intDiagram.getJiaohu().size();j++){
					if(((Jiaohu)intDiagram.getJiaohu().get(j)).getNumber() == biaohao){
						phenomenons.add(temp_p);
						break;
					}
				}
			}
		}

		setOrder();

		try {
			fixInteractions(this.phenomenons);
		} catch (Exception e){
			e.printStackTrace();
		}

		int interactions_size = phenomenons.size();


		int interval = length / (interactions_size + 1);


		for (int m = 0; m < interactions_size; m++) {
			state = 0;
			Phenomenon temp_p2 = phenomenons.get(m);

			if (temp_p2.getRequirement() != null)
				state = 1;

			Jiaohu tempJh = new TriangleJiaohu(originX + (jiaohu.size() + 1)
					* interval, originY, temp_p2.getBiaohao(), state);

			jiaohu.add(tempJh);
		}
		*/


	}

	public InstantGraph(int intCount, String m_name, Clock clock) {

		originX = 20;
		originY = 60;
		length = 800;

		this.clock = clock;
		this.problemDiagram = Main.win.myProblemDiagram;
		this.name = m_name.trim();

		if (problemDiagram == null)
			return;

		int interval = length / (intCount + 1);

		for (int i = 0; i < intCount; i++) {
			Jiaohu tempJh = new TriangleJiaohu(originX + (jiaohu.size() + 1)
					* interval, originY, i, 0);
			jiaohu.add(tempJh);
		}

	}

	public void setType(int m_type) {
		this.type = m_type;
		for (int i = 0; i < this.jiaohu.size(); i++) {
			String jName = this.getName().length() < 3 ? this.getName() : this
					.getName().substring(0, 3);
			this.jiaohu.get(i).setName(jName);
		}
	}

	public int getType() {
		return this.type;
	}

	public void setName(String m_name) {
		this.name = m_name.trim();
	}

	public String getName() {
		return this.name;
	}

	//Insert Sort
	private void fixInteractions(LinkedList<Phenomenon> interactions2) {
		try {
			Collections.sort(interactions2, new Comparator<Phenomenon>() {
				@Override
				public int compare(Phenomenon o1, Phenomenon o2) {
					double w1 = getValue(o1.getBiaohao());
					double w2 = getValue(o2.getBiaohao());
					if(w1 - w2 > 0) return 1;
					else return -1;
				}
			});
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private double getValue(int key) {
		try{
			String weight_str = this.weight.get(key);
			double m_weight = Double.valueOf(weight_str.substring(weight_str
					.indexOf(',') + 1));
			return m_weight;
		}
		catch (Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	public void setPosition(int x, int y) {
		this.originX = x;
		this.originY = y;
		for (int i = 0; i < nowJiaohu.size(); i++) {
			Jiaohu temp = nowJiaohu.get(i);
			nowJiaohu.get(i).moveTo(originX + temp.getMiddleX(), 0);
		}
		LinkedList<Changjing> temp = new LinkedList<>();
		for(int i = 0;i < nowChangjing.size();i++){
			Changjing changjing = nowChangjing.get(i);
			int fromNumber = changjing.getFrom().getNumber();
			int toNumber = changjing.getTo().getNumber();
			int fromState = changjing.getFrom().getState();
			int toState = changjing.getTo().getState();
			Jiaohu from = null;
			Jiaohu to = null;
			for(int j = 0;j < nowJiaohu.size();j++){
				Jiaohu now = nowJiaohu.get(j);
				if(now.getNumber() == fromNumber && now.getState() == fromState) from = now;
				if(now.getNumber() == toNumber && now.getState() == toState) to = now;
			}
			temp.add(new Changjing(new LinkedList(), from, to, changjing.getState()));
		}
		nowChangjing = temp;
	}

	public Point getOrigin() {
		return new Point(originX, originY);
	}

	public LinkedList<Phenomenon> getInts() {
		return phenomenons;
	}

	public LinkedList<Jiaohu> getJiaohu() {
		return this.jiaohu;
	}

	public void addConstraint(String con) {
		constraint.add(con);
	}

	public Rect getDomain() {
		return this.domain;
	}

	public Clock getClock() {
		return this.clock;
	}

	public void setClock(Clock mClock) {
		this.clock = mClock;
	}

	public Hashtable<Integer, String> getOrder() {
		return this.weight;
	}

	public boolean contains(Jiaohu p_jiaohu) {

		boolean isHave = false;
		int jiaohu_size = this.jiaohu.size();

		for (int i = 0; i < jiaohu_size; i++) {

			// if (p_jiaohu.getNumber() == jiaohu.get(i).getNumber()) {
			if (p_jiaohu.equals(jiaohu.get(i))) {

				isHave = true;

				break;
			}
		}

		return isHave;
	}

	public void refresh() {
		Collections.sort(this.getJiaohu());
	}

	public void draw(Graphics g) {
		// ��ͼ�ı�ʶ

		/*
		int minX = Integer.MAX_VALUE;
		int maxX = 0;
		int minY = Integer.MAX_VALUE;
		int maxY = 0;

		for(int i = 0;i < nowJiaohu.size();i++){
			Jiaohu jiaohu = nowJiaohu.get(i);
			jiaohu.setState(2);
			jiaohu.draw(g);
		}
		for(int i = 0;i < nowChangjing.size();i++){
			Changjing changjing = nowChangjing.get(i);
			if(changjing.getState() != 4){
				changjing.setState(5);
				changjing.draw(g);
			}
			else {
				Changjing reverse = new Changjing(changjing.getDian(), changjing.getTo(), changjing.getFrom(), 5);
				reverse.draw(g);
			}
		}

		if(nowJiaohu.size() > 0){
			for(int i = 0;i < nowJiaohu.size();i++){
				Jiaohu jiaohu = nowJiaohu.get(i);
				if(jiaohu.getMiddleX() > maxX) maxX = jiaohu.getMiddleX();
				if(jiaohu.getMiddleX() < minX) minX = jiaohu.getMiddleX();
				if(jiaohu.getMiddleY() > maxY) maxY = jiaohu.getMiddleY();
				if(jiaohu.getMiddleY() < minY) minY = jiaohu.getMiddleY();
			}

			for(int i = 0;i < nowChangjing.size();i++){
				LinkedList dian = nowChangjing.get(i).getDian();
				if(dian.size() == 4){
					int x1 = (Integer.parseInt((String) dian.get(0)));
					int x2 = (Integer.parseInt((String) dian.get(2)));
					int y1 = (Integer.parseInt((String) dian.get(1)));
					int y2 = (Integer.parseInt((String) dian.get(3)));
					if(x1 > maxX) maxX = x1;
					if(x2 > maxX) maxX = x2;
					if(y1 > maxY) maxY = y1;
					if(y2 > maxY) maxY = y2;
					if(x1 < minX) minX = x1;
					if(x2 < minX) minX = x2;
					if(y1 < minY) minY = y1;
					if(y2 < minY) minY = y2;
				}
			}

			Graphics2D g2 = (Graphics2D) g;
			BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F,
					Data.LENGTHOFDASH, 0.0F);

			g2.setStroke(dashed);
			g2.drawLine(minX - 35, minY - 35, maxX + 35, minY - 35);
			g2.drawLine(minX - 35,minY - 35,minX - 35,maxY + 35);
			g2.drawLine(maxX + 35,maxY + 35,maxX + 35,minY - 35);
			g2.drawLine(maxX + 35,maxY + 35,minX - 35,maxY + 35);

			g.drawString("C", minX-25, minY-40);
			Font font1 = new Font("SansSerif", 0, 9);
			Font tmp = g.getFont();
			g.setFont(font1);
			g.drawString(this.getName(), minX-17, minY-38);
			g.setFont(tmp);
		}
		*/
	}

	public void draw(Graphics g, boolean inClockSpecification) {
		// ��ͼ�ı�ʶ
		if(inClockSpecification){
			// ��ͼ�ı�ʶ

			int minX = Integer.MAX_VALUE;
			int maxX = 0;
			int minY = Integer.MAX_VALUE;
			int maxY = 0;
			Colour colour = clock.getColor();
			Color color = new Color(colour.getR(),colour.getG(),colour.getB());
			for(int i = 0;i < nowJiaohu.size();i++){
				Jiaohu jiaohu = nowJiaohu.get(i);
				jiaohu.setState(2);
				jiaohu.draw(g,color);
			}
			for(int i = 0;i < nowChangjing.size();i++){
				Changjing changjing = nowChangjing.get(i);
				if(changjing.getState() != 4){
					changjing.setState(5);
					changjing.draw(g);
				}
				else {
					Changjing reverse = new Changjing(changjing.getDian(), changjing.getTo(), changjing.getFrom(), 5);
					reverse.draw(g);
				}
			}

			if(nowJiaohu.size() > 0){
				for(int i = 0;i < nowJiaohu.size();i++){
					Jiaohu jiaohu = nowJiaohu.get(i);
					if(jiaohu.getMiddleX() > maxX) maxX = jiaohu.getMiddleX();
					if(jiaohu.getMiddleX() < minX) minX = jiaohu.getMiddleX();
					if(jiaohu.getMiddleY() > maxY) maxY = jiaohu.getMiddleY();
					if(jiaohu.getMiddleY() < minY) minY = jiaohu.getMiddleY();
				}

				for(int i = 0;i < nowChangjing.size();i++){
					LinkedList dian = nowChangjing.get(i).getDian();
					if(dian.size() == 4){
						int x1 = (Integer.parseInt((String) dian.get(0)));
						int x2 = (Integer.parseInt((String) dian.get(2)));
						int y1 = (Integer.parseInt((String) dian.get(1)));
						int y2 = (Integer.parseInt((String) dian.get(3)));
						if(x1 > maxX) maxX = x1;
						if(x2 > maxX) maxX = x2;
						if(y1 > maxY) maxY = y1;
						if(y2 > maxY) maxY = y2;
						if(x1 < minX) minX = x1;
						if(x2 < minX) minX = x2;
						if(y1 < minY) minY = y1;
						if(y2 < minY) minY = y2;
					}
				}

				Graphics2D g2 = (Graphics2D) g;
				BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F,
						Data.LENGTHOFDASH, 0.0F);

				g2.setStroke(dashed);
				g2.drawLine(minX - 35, minY - 35, maxX + 35, minY - 35);
				g2.drawLine(minX - 35,minY - 35,minX - 35,maxY + 35);
				g2.drawLine(maxX + 35,maxY + 35,maxX + 35,minY - 35);
				g2.drawLine(maxX + 35,maxY + 35,minX - 35,maxY + 35);

				g.drawString("C", minX-25, minY-40);
				Font font1 = new Font("SansSerif", 0, 9);
				Font tmp = g.getFont();
				g.setFont(font1);
				g.drawString(this.getName(), minX-17, minY-38);
				g.setFont(tmp);
			}
		}
	}

	public Jiaohu whichSelected(int x, int y) {
		for (int i = 0; i <= this.jiaohu.size() - 1; i++) {
			Jiaohu tmp_jh = (Jiaohu) this.jiaohu.get(i);
			if (tmp_jh.isIn(x, y)) {
				return tmp_jh;
			}
		}
		return null;
	}

	public static void main(String[] args){
		LinkedList<Jiaohu> froms = new LinkedList<>();
		LinkedList<Jiaohu> tos = new LinkedList<>();
		Jiaohu from = new Jiaohu(12,34,1,0);
		Jiaohu to = new Jiaohu(55,56,2,0);
		froms.add(from);
		tos.add(to);
		Changjing changjing = new Changjing(new LinkedList(), froms.get(0), tos.get(0), 1);
		froms.get(0).moveTo(50, 50);
		tos.get(0).moveTo(50, 50);
		System.out.println(changjing.getFrom().getMiddleX());
		System.out.println(changjing.getFrom().getMiddleY());
	}

	/**
	 * �õ��ý���ͼ������stateΪ0����1�ĳ��� ����improve��������Щ������������
	 * */
	private void setOrder() {
		IntDiagram myIntDiagram = whichDiagram();

		if (myIntDiagram == null)
			return;
		LinkedList m_changjing = myIntDiagram.getChangjing();
		int m_changjing_size = m_changjing.size();

		LinkedList<Changjing> new_changjing = new LinkedList<Changjing>();
		for (int i = 0; i < m_changjing_size; i++) {

			Changjing temp_changjing = (Changjing) m_changjing.get(i);

			if (temp_changjing.getState() == 3
					|| temp_changjing.getState() == 1) {

				new_changjing.add(temp_changjing);
			}
		}
		improve(new_changjing);
		/*
		for(int i = 0;i < myIntDiagram.getJiaohu().size();i++){
			Jiaohu temp = (Jiaohu) myIntDiagram.getJiaohu().get(i);
			System.out.println("int" + temp.getNumber() + " : " + weight.get(temp.getNumber()) + ",state : " + temp.getState());
		}
		*/
	}

	/*
	the parameter weight is a HashMap,the key is the number of a Jiaohu,and the value is in the format of "int,double",
	the same former parameter represents the  Jiaohus are in a line and the latter is the order of it(the larger means the later)
	*/
	private void improve(LinkedList<Changjing> newChangjing) {
		//System.out.println("IntDiagram :" + whichDiagram().getBiaohao());

		LinkedList<Changjing> changjing = newChangjing;

		int count = getCount(newChangjing);
		//System.out.println("count : " + count);

		int N = 0;

		while (this.weight.size() != count) {
			//System.out.println("improve round : " + (N + 1));
			LinkedList<Changjing> hasWeight = new LinkedList<Changjing>();
			int changjing_size = changjing.size();
			for (int i = 0; i < changjing_size; i++) {
				Changjing current = changjing.get(i);

				Jiaohu from = current.getFrom();
				Jiaohu to = current.getTo();

				String value = String.valueOf(N) + ",";
				if (i == 0) {
					weight.put(from.getNumber(), value + String.valueOf(0.0));

					if (current.getState() == 1) {
						weight.put(to.getNumber(), value + String.valueOf(1.0));
					} else if (current.getState() == 3) {
						weight.put(to.getNumber(), value + String.valueOf(0.5));
					}

					hasWeight.add(current);

					continue;
				}

				if ((weight.get(from.getNumber()) == null && weight.get(to.getNumber()) == null)
						|| (weight.get(from.getNumber()) != null && weight.get(to.getNumber()) != null)) {
					continue;

				}
				else if (weight.get(from.getNumber()) != null && weight.get(to.getNumber()) == null) {
					double w = this.getValue(from.getNumber());
					if (current.getState() == 1) {
						weight.put(to.getNumber(),
								value + String.valueOf(w + 1));
					} else if (current.getState() == 3) {
						weight.put(to.getNumber(),
								value + String.valueOf(w + 0.5));
					}

					hasWeight.add(current);

				}
				else if (weight.get(from.getNumber()) == null && weight.get(to.getNumber()) != null) {
					double w = Double.valueOf(to.getNumber());
					if (current.getState() == 1) {
						weight.put(from.getNumber(),
								value + String.valueOf(w - 1));
					} else if (current.getState() == 3) {
						weight.put(from.getNumber(),
								value + String.valueOf(w - 0.5));
					}

					hasWeight.add(current);
				}
			}

			int hasWeight_size = hasWeight.size();
			for(int i = 0;i < hasWeight_size;i++){
				//System.out.println("from : int" + hasWeight.get(i).getFrom().getNumber() + "," + "to : int" + hasWeight.get(i).getTo().getNumber());
			}
			for (int k = 0; k < hasWeight_size; k++) {
				changjing.remove(hasWeight.get(k));
			}

			N++;
		}
	}

	/**
	 * �������������� ����ֵ���������Ĳ��ظ��Ľ���
	 * */
	private int getCount(LinkedList<Changjing> newChangjing) {

		LinkedList<Jiaohu> jiaohu = new LinkedList<Jiaohu>();

		/*
		 * ����������Ը�Ϊ���� Set<Jiaohu> jiaohus=new HashSet();
		 */

		int newChangjing_size = newChangjing.size();

		for (int i = 0; i < newChangjing_size; i++) {

			Jiaohu from = newChangjing.get(i).getFrom();
			Jiaohu to = newChangjing.get(i).getTo();

			if (!jiaohu.contains(from)) {
				jiaohu.add(from);
			}
			if (!jiaohu.contains(to)) {
				jiaohu.add(to);
			}
		}
		return jiaohu.size();
	}

	/**
	 * �õ������������ص��龳ͼ
	 * */
	private IntDiagram whichDiagram() {

		LinkedList<IntDiagram> temp = Main.win.myIntDiagram;
		int temp_size = temp.size();

		for (int i = 0; i < temp_size; i++) {

			@SuppressWarnings("unchecked")
			LinkedList<Jiaohu> jiaohu = temp.get(i).getJiaohu();
			int jiaohu_size = jiaohu.size();

			for (int m = 0; m < jiaohu_size; m++) {

				Jiaohu temp_jiaohu = jiaohu.get(m);

				if (temp_jiaohu.getNumber() == this.phenomenons.get(0)
						.getBiaohao()) {
					return temp.get(i);
				}
			}
		}
		return null;
	}

	public IntDiagram getIntDiagram() {
		return intDiagram;
	}

}
