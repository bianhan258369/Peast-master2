package UI;

import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;

import com.hp.hpl.jena.util.tuple.TupleSet;
import foundation.Checker;

import Shape.*;
import javafx.util.Pair;
import util.TxtFileFilter;

public class InstantPane extends FatherPane implements MouseMotionListener,
		MouseListener, ActionListener {

	LinkedList<InstantGraph> igs = new LinkedList<InstantGraph>();
	Map<String, Integer> constraintRelations = new HashMap<String, Integer>();
	// LinkedList<InstantGraph> comIgs=new LinkedList<InstantGraph>();
	ConstraintDialog constraintDialog;
	static Diagram myProblemDiagram;
	boolean dragged = false;// ��ק�ر�����ʶ��ק״̬��
	Jiaohu nowSelected = null;// ��ǰѡ��Ľ���
	String title = "TD";
	Jiaohu from = null;
	Jiaohu to = null;
	int relation = 0;
	LinkedList<String> constructedClocks = new LinkedList<>();
	LinkedList<InstantRelation> relations = new LinkedList<InstantRelation>();
	LinkedList<InstantRelation> cRelations = new LinkedList<InstantRelation>();
	LinkedList<Pair<String, Integer>> ClockRelations = new LinkedList<>();
	LinkedList<Pair<String, List<Integer>>> params = new LinkedList<>();
	LinkedList<Jiaohu> froms = new LinkedList<>();
	LinkedList<Jiaohu> tos = new LinkedList<>();
    LinkedList<Colour> colors = new LinkedList<>();
    // ��ǰ����λ��
	int nowx = 0;
	int nowy = 0;
	// ��קǰλ��
	int previousX = 0;
	int previousY = 0;

	private int count = 1;// ͼ����Ŀ

	JPopupMenu popupMenu = new JPopupMenu();

	Font font = new Font("Arial", Font.PLAIN, 15);
	//JPanel buttonPanel = new JPanel();
	JPanel buttonAndConstrainPanel = new JPanel();
	JButton addBut = new JButton("Add Clock Constraint");
    JButton combineBut = new JButton("Clock Construction");
    JButton createTxtBut = new JButton("Export Relations");


	static ConstraintPane south = new ConstraintPane();

	private boolean isDraw = false;

	public InstantPane(InstantGraph ig) {
		constraintRelations.put("SubClock",0);
		constraintRelations.put("Alternate",0);
		constraintRelations.put("StrictPre",0);
		constraintRelations.put("nStrictPre",0);
		constraintRelations.put("BoundedDiff",2);
		constraintRelations.put("union",0);
		constraintRelations.put("sup",0);
		constraintRelations.put("inf",0);
		this.type = 1;
		this.setBackground(Color.white);
		igs.add(ig);
		this.setLayout(new BorderLayout(5,5));
		//this.add(south, BorderLayout.SOUTH);
		buttonAndConstrainPanel.setLayout(new BorderLayout());
		//buttonPanel.setLayout(new GridLayout(3,1));
		//buttonPanel.setPreferredSize(new Dimension(200,30));
		//addBut.setFont(font);
		//combineBut.setFont(font);
		//createTxtBut.setFont(font);
		//buttonPanel.add(addBut);
		//buttonPanel.add(combineBut);
		//buttonPanel.add(createTxtBut);
		//buttonAndConstrainPanel.add("Center",south);
		//buttonAndConstrainPanel.add("West", buttonPanel);
		this.add("South", buttonAndConstrainPanel);
		//addBut.addActionListener(this);
		//combineBut.addActionListener(this);
		//createTxtBut.addActionListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		//addBut.setEnabled(false);
		//combineBut.setEnabled(false);
		//createTxtBut.setEnabled(false);
	}

	/*
	public InstantPane(Rect domain, Clock clock) {

		constraintRelations.put("SubClock",0);
		constraintRelations.put("Alternate",0);
		constraintRelations.put("StrictPre",0);
		constraintRelations.put("nStrictPre",0);
		constraintRelations.put("BoundedDiff",2);
		this.type = 1;
		this.setBackground(Color.white);
		InstantGraph ig = new InstantGraph(domain, clock);
		igs.add(ig);
		this.setLayout(new BorderLayout());
		this.add(south, BorderLayout.SOUTH);
		buttonPanel.setLayout(new GridLayout(7, 1));
		buttonPanel.setPreferredSize(new Dimension(200,30));
		addBut.setFont(font);
		combineBut.setFont(font);
		createTxtBut.setFont(font);
		buttonPanel.add(addBut);
		buttonPanel.add(combineBut);
		buttonPanel.add(createTxtBut);
		this.add("East", buttonPanel);
		addBut.addActionListener(this);
		combineBut.addActionListener(this);
		createTxtBut.addActionListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	*/

	public void addGraph(InstantGraph newIg) {

		for (int m = 0; m < igs.size(); m++) {

			if (null == igs.get(m).getDomain()) {
				continue;
			}

			if (igs.get(m).getDomain().equals(newIg.getDomain())) {
				igs.remove(m);
				// count--;
				igs.add(m, newIg);
				return;
			}
		}
		/*
		 * int index = count; for (int i = 0; i < igs.size() && i <= index; i++)
		 * { if (igs.get(i).getClock().getName()
		 * .equals(newIg.getClock().getName())) index = i + 1; }
		 */
		newIg.setPosition(count * 200 + 20, 60);
		igs.add(newIg);
		count++;

		this.repaint();
	}


	/*
	public void addGraph(Rect domain, Clock clock) {

		for (int m = 0; m < igs.size(); m++) {

			if (null == igs.get(m).getDomain()) {
				continue;
			}

			if (igs.get(m).getDomain().equals(domain)) {
				igs.get(m).setClock(clock);
				return;
			}
		}
		InstantGraph ig = new InstantGraph(domain, clock);
		igs.add(ig);

		ig.setPosition(20, count * 60 + 60);
		// igs.add(index, newIg);
		count++;

		this.repaint();
	}
	*/

	public void addConstructionGraph(int index, LinkedList<String> domains,
			String name, Clock clock) {
		int count = getCount(index, domains);
		InstantGraph ig = new InstantGraph(count, name, clock);
		ig.setType(InstantGraph.TYPE_CONSTRUCTION);
		this.addGraph(ig);
		// Add relations
		if (index == 0) {
			String domain = domains.get(0);
			InstantGraph graph0 = getIG(domain);
			int _index = 0;
			char[] filter = domains.get(1).toCharArray();
			for (int i = 0; i < graph0.getJiaohu().size(); i++) {
				if (filter[i % filter.length] == '1') {
					InstantRelation new_ir = new InstantRelation(graph0
							.getJiaohu().get(i), ig.getJiaohu().get(_index++),
							0);
					this.cRelations.add(new_ir);
				}
			}
		} else if (index == 1) {
			String domain0 = domains.get(0);
			InstantGraph graph0 = getIG(domain0);
			LinkedList<Jiaohu> jiaohus = graph0.getJiaohu();
			for (int i = 1; i < domains.size(); i++) {
				InstantGraph temp = this.getIG(domains.get(i));
				jiaohus.addAll(temp.getJiaohu());
			}
			Collections.sort(jiaohus);
			for (int j = 0; j < count; j++) {
				InstantRelation new_ir = new InstantRelation(jiaohus.get(j), ig
						.getJiaohu().get(j), 0);
				this.cRelations.add(new_ir);
			}
		} else {
			for (int i = 0; i < domains.size(); i++) {
				for (int j = 0; j < count; j++) {
					InstantGraph temp = this.getIG(domains.get(i));
					if (index == 2) {
						InstantRelation new_ir = new InstantRelation(temp
								.getJiaohu().get(j), ig.getJiaohu().get(j), 1);
						this.cRelations.add(new_ir);
					} else {
						InstantRelation new_ir = new InstantRelation(ig
								.getJiaohu().get(j), temp.getJiaohu().get(j), 1);
						this.cRelations.add(new_ir);
					}
				}
			}
		}
	}

	private int getCount(int index, LinkedList<String> domains) {
		int count = 0;
		if (index == 0) {
			String domain = domains.get(0);
			char[] filter = domains.get(1).toCharArray();
			InstantGraph graph0 = getIG(domain);
			if (graph0 != null) {
				for (int i = 0; i < graph0.getJiaohu().size(); i++) {
					if (filter[i % filter.length] == '1')
						count++;
				}
			}
		} else if (index == 1) {
			for (int j = 0; j < domains.size(); j++) {
				InstantGraph graph1 = getIG(domains.get(j));
				count += graph1.getJiaohu().size();
			}
		} else {
			String example = domains.get(0);
			InstantGraph graph2 = getIG(example);
			count = graph2.getJiaohu().size();
		}
		return count;
	}

	private InstantGraph getIG(String shortName) {
		InstantGraph result = null;
		for (int i = 0; i < igs.size(); i++) {
			if (null == igs.get(i).getDomain())
				continue;
			if (igs.get(i).getDomain().getShortName().equals(shortName)) {
				result = igs.get(i);
				break;
			}
		}
		return result;
	}

	public LinkedList<InstantGraph> getGraphs() {
		return this.igs;
	}

	private void draw(Graphics g){

	}

	public void paint(Graphics g) {
		super.paint(g);
		Arrow arrow = new Arrow();
		Font font1 = new Font("SansSerif", 0, 12);
		if (this.igs != null) {
			for (int i = 0; i < igs.size(); i++) {
				InstantGraph ig = igs.get(i);
				Colour myColor = ig.getClock().getColor();
				Color color = new Color(myColor.getR(), myColor.getG(), myColor.getB());
				for(int j = 0;j < ig.getJiaohu().size();j++){
				    Jiaohu jiaohu = ig.getJiaohu().get(j);
				    jiaohu.draw(g,color);
				    int number = jiaohu.getNumber();
				    String text = null;
				    for(int k = 0;k < ig.getPhenomenons().size();k++){
				    	Phenomenon phenomenon = ig.getPhenomenons().get(k);
				    	if(phenomenon.getBiaohao() == number){
				    		text = phenomenon.getName();
				    		break;
						}
					}
					g.setFont(font1);
					g.drawString(text, jiaohu.getMiddleX() + 20,jiaohu.getMiddleY() - 15);
                }
                g.setColor(Color.black);
                Font font2 = new Font("SansSerif", 0, 12);
				g.setFont(font2);
				g.drawString(ig.getDomain().getShortName() + ":",30,30+20 * i);
				g.setColor(color);
				g.fillRect(70,18 + 20 * i,30,15);
			}
			for(int i = 0;i < igs.get(0).getIntDiagram().getChangjing().size();i++){
			    Changjing changjing = (Changjing) igs.get(0).getIntDiagram().getChangjing().get(i);
                if(changjing.getState() != 4){
                    if(changjing.getState() != 2){
                        changjing.setState(5);
                        changjing.draw(g);
                    }
                    if(changjing.getState() == 2){
                    	changjing.draw(g, true);
					}
                }
                else {
                    Changjing reverse = new Changjing(changjing.getDian(), changjing.getTo(), changjing.getFrom(), 5);
                    reverse.draw(g);
                }
            }
		}
		for(int i = 0;i < ClockRelations.size();i++){
			String str = new String("");
			if(ClockRelations.get(i).getKey().length() < 3) str = ClockRelations.get(i).getKey();
			else str = "(" + ClockRelations.get(i).getKey().substring(0, 3);
			for(int j = 0;j < ClockRelations.get(i).getValue();j++){
				str = str + " " + params.get(i).getValue().get(j).toString();
			}
			str = str + ")";
			if(froms.get(i).getMiddleX() <= tos.get(i).getMiddleX()){
				arrow.paintComponent(froms.get(i).getMiddleX() - 5, froms.get(i).getMiddleY() - 8, tos.get(i).getMiddleX() - 35, tos.get(i).getMiddleY() - 8,str, g);
			}
			else{
				arrow.paintComponent(froms.get(i).getMiddleX() - 35, froms.get(i).getMiddleY() - 8, tos.get(i).getMiddleX() - 5, tos.get(i).getMiddleY() - 8,str, g);
			}
		}

		if (this.relations != null) {
			for (int i = 0; i < relations.size(); i++) {
				relations.get(i).draw(g);
			}
		}

		if (this.cRelations != null) {
			for (int i = 0; i < cRelations.size(); i++) {
				cRelations.get(i).draw(g);
			}
		}

		/*
		// order from different InstantGraphs
		for(int i = 0 ;i < this.igs.size();i++){
			for(int j = i + 1;j < this.igs.size();j++){
				InstantGraph tempIg1 = igs.get(i);
				InstantGraph tempIg2 = igs.get(j);
				for(int m = 0;m < tempIg1.getJiaohu().size();m++){
					for(int n = 0;n < tempIg2.getJiaohu().size();n++){
						Jiaohu tempJh1 = tempIg1.getJiaohu().get(m);
						Jiaohu tempJh2 = tempIg2.getJiaohu().get(n);
						for(int index = 0;index < Main.win.myIntDiagram.size(); index++){
							IntDiagram tempId = Main.win.myIntDiagram.get(index);
							for(int x = 0;x < tempId.getChangjing().size();x++){
								Changjing changjing = (Changjing) tempId.getChangjing().get(x);
								if(changjing.getState() != 2){
									Jiaohu from = changjing.getFrom();
									Jiaohu to = changjing.getTo();
									if(from.getNumber()==tempJh1.getNumber() && to.getNumber() == tempJh2.getNumber()){
                                        if(tempJh1.getMiddleX() <= tempJh2.getMiddleX()){
                                            arrow.paintComponent(tempJh1.getMiddleX() - 5,tempJh1.getMiddleY()-8,tempJh2.getMiddleX() - 35,tempJh2.getMiddleY()-8,g);
                                        }
										else{
                                            arrow.paintComponent(tempJh1.getMiddleX() - 35,tempJh1.getMiddleY()-8,tempJh2.getMiddleX() - 5,tempJh2.getMiddleY()-8,g);
                                        }
									}
									else if (from.getNumber() == tempJh2.getNumber() && to.getNumber() == tempJh1.getNumber()){
										if(tempJh1.getMiddleX() <= tempJh2.getMiddleX()){
                                            arrow.paintComponent(tempJh2.getMiddleX() - 35,tempJh2.getMiddleY()-8,tempJh1.getMiddleX() - 5,tempJh1.getMiddleY()-8,g);
                                        }
                                        else{
                                            arrow.paintComponent(tempJh2.getMiddleX() - 5,tempJh2.getMiddleY()-8,tempJh1.getMiddleX() - 35,tempJh1.getMiddleY()-8,g);
                                        }
									}
								}
							}
						}
					}
				}
			}
		}
		*/

	}


	public void setDraw(boolean draw) {
		this.isDraw = draw;
		if(isDraw && (this.relation == 0 || this.relation == 1 || this.relation == 2))
			setCursor(Cursor.getPredefinedCursor(1));
		else setCursor(Cursor.getDefaultCursor());
	}

	public static void main(String[] args) {
	}

	public void addClockConstraint(){
        constraintDialog = new ConstraintDialog(this.igs);
    }

    public void ClockConstruction(){
        LinkedList<String> clocks = new LinkedList<String>();
        for (int j = 0; j < igs.size(); j++) {
            Rect domain = igs.get(j).getDomain();
            if (null != domain)
                clocks.add(domain.getShortName());
        }
        new CConstructionDialog(clocks);
    }

    public void createRelations(){
        try {
            createTxtFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("Add Clock Constraint")) {
            addClockConstraint();
		} else if (e.getActionCommand().equals("Coincidence")) {
			System.out.println("Coincidence");
			this.setDraw(true);
			relation = 0;
		} else if (e.getActionCommand().equals("Precedence")) {
			System.out.println("Precedence");
			this.setDraw(true);
			relation = 1;
		} else if (e.getActionCommand().equals("Strict Precedence")) {
			System.out.println("Strict Precedence");
			this.setDraw(true);
			relation = 2;
		} else if (e.getActionCommand().equals("Add Clock Constraint")) {
            ClockConstruction();
		} else if (e.getActionCommand().equals("Clock Construction")) {
			LinkedList<String> clocks = new LinkedList<String>();
			for (int j = 0; j < igs.size(); j++) {
				Rect domain = igs.get(j).getDomain();
				if (null != domain)
					clocks.add(domain.getShortName());
			}
			new CConstructionDialog(clocks);
		} else if (e.getActionCommand().equals("Check with NuSMV")) {
			Checker checker = new Checker(igs, relations, cRelations);
			checker.check();
		}
		else if(e.getActionCommand().equals("Export Relations")){
            createRelations();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		requestFocus();
		Point p = e.getPoint();

		if (this.nowSelected != null) {
			this.nowSelected.selected = false;
			repaint();
		}
		this.nowSelected = null;
		if (e.getButton() == 3) {
			this.popupMenu.show(this, e.getX(), e.getY());
			return;
		}

		if (this.isDraw) {
			if (this.relation < 0 || this.relation > 2) {
				return;
			}
			if (this.from == null) {
				Jiaohu tmpFrom = null;
				for (int i = 0; i < igs.size(); i++) {
					tmpFrom = igs.get(i).whichSelected(p.x, p.y);
					if (tmpFrom != null)
						break;
				}
				if (tmpFrom == null) {
					this.setDraw(false);
					return;
				}

				this.from = tmpFrom;
				return;
			}

			Jiaohu tmpTo = null;
			for (int i = 0; i < igs.size(); i++) {
				tmpTo = igs.get(i).whichSelected(p.x, p.y);
				if (tmpTo != null)
					break;
			}
			if (tmpTo != null) {

				String con = "<";

				if (this.relation == 0)
					con = "=";
				else if (this.relation == 1)
					con = "��";

				/*
				 * if(this.from || tmpTo belongs to construction Graph){
				 * InstantRelation new_cj = new InstantRelation(this.from,
				 * tmpTo, this.relation); this.cRelations.add(new_cj); }
				 */

				if (this.whichGraph(this.from).getType() == InstantGraph.TYPE_CONSTRUCTION
						|| this.whichGraph(tmpTo).getType() == InstantGraph.TYPE_CONSTRUCTION) {
					InstantRelation new_cj = new InstantRelation(this.from,
							tmpTo, this.relation);
					this.cRelations.add(new_cj);
				} else if (this.checkConstraint(
						this.from.getName() + this.from.getNumber(), con,
						tmpTo.getName() + tmpTo.getNumber())) {

					InstantRelation new_cj = new InstantRelation(this.from,
							tmpTo, this.relation);

					this.relations.add(new_cj);
				} else
					JOptionPane.showMessageDialog(this, Main.errmes);

				this.setDraw(false);
				this.from = null;
				repaint();

				return;
			}
			repaint();
			return;
		} else {
			Jiaohu tmp = null;
			for (int i = 0; i < igs.size(); i++) {
				tmp = igs.get(i).whichSelected(p.x, p.y);
				if (tmp != null)
					break;
			}
			if (tmp != null)
				System.out.println(tmp.toString());
			if (tmp == null) {
				if (this.nowSelected != null) {
					this.nowSelected.selected = false;
					this.nowSelected = null;
				}
				repaint();
			} else {
				if (this.nowSelected != null) {
					this.nowSelected.selected = false;
					this.nowSelected = null;
				}
				this.nowSelected = tmp;
				this.previousX = e.getX();
				this.previousY = e.getY();
				tmp.selected = true;
				this.dragged = true;
				this.nowx = e.getX();
				this.nowy = e.getY();
				repaint();
				return;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		int x = e.getX();
		if (this.dragged && !check()) {
			this.nowSelected.moveTo(this.previousX - x, 0);

			for (int i = 0; i <= this.relations.size() - 1; i++) {

				InstantRelation tmp_cj = this.relations.get(i);

				if ((this.nowSelected.equals(tmp_cj.getFrom()))
						|| (this.nowSelected.equals(tmp_cj.getTo())))

					tmp_cj.refresh();
			}
			for (int i = 0; i <= this.cRelations.size() - 1; i++) {

				InstantRelation tmp_cj = this.cRelations.get(i);

				if ((this.nowSelected.equals(tmp_cj.getFrom()))
						|| (this.nowSelected.equals(tmp_cj.getTo())))

					tmp_cj.refresh();
			}
			repaint();
			JOptionPane.showMessageDialog(this, Main.errmes);
		}
		for (int j = 0; j < this.igs.size(); j++) {
			igs.get(j).refresh();
		}
		this.dragged = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (!this.dragged) {
			return;
		}
		int x = e.getX();
		int y = e.getY();
		this.nowSelected.moveTo(x - this.nowx, 0);// �ڶ�����������Ϊ0��֤��ֻ�����������ƶ�
		this.nowx = x;
		this.nowy = y;

		for (int i = 0; i <= this.relations.size() - 1; i++) {
			InstantRelation tmp_cj = this.relations.get(i);
			if ((this.nowSelected.equals(tmp_cj.getFrom()))
					|| (this.nowSelected.equals(tmp_cj.getTo())))
				tmp_cj.refresh();
		}
		for (int i = 0; i <= this.cRelations.size() - 1; i++) {
			InstantRelation tmp_cj = this.cRelations.get(i);
			if ((this.nowSelected.equals(tmp_cj.getFrom()))
					|| (this.nowSelected.equals(tmp_cj.getTo())))
				tmp_cj.refresh();
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	private boolean check() {
		// TODO Auto-generated method stub
		int igs_size = igs.size();
		InstantGraph tempIG = null;

		for (int i = 0; i < igs_size; i++) {
			if (igs.get(i).contains(nowSelected)) {
				if (null == igs.get(i).getDomain()) {
					System.out.println("Combine");
					return true;
				}
				tempIG = igs.get(i);
				break;
			}
		}

		LinkedList<Jiaohu> tempIG_jiaohu = tempIG.getJiaohu();
		int tempIG_jiaohu_size = tempIG_jiaohu.size();

		if (tempIG != null) {
			Hashtable<Integer, String> weight = tempIG.getOrder();

			for (int m = 0; m < tempIG_jiaohu_size; m++) {

				double value1 = this.getValue(weight, tempIG_jiaohu.get(m)
						.getNumber());
				double value2 = this.getValue(weight, nowSelected.getNumber());

				if (tempIG_jiaohu.get(m).getMiddleX() < this.nowSelected
						.getMiddleX() && value1 > value2) {

					UI.Main.errmes = nowSelected.getName()
							+ nowSelected.getNumber() + " must before "
							+ tempIG_jiaohu.get(m).getName()
							+ tempIG_jiaohu.get(m).getNumber() + " !";

					UI.Main.errstate = 1;

					return false;
				}

				if (tempIG_jiaohu.get(m).getMiddleX() > this.nowSelected
						.getMiddleX() && value1 < value2) {

					UI.Main.errmes = nowSelected.getName()
							+ nowSelected.getNumber() + " must after "
							+ tempIG_jiaohu.get(m).getName()
							+ tempIG_jiaohu.get(m).getNumber() + " !";

					UI.Main.errstate = 1;

					return false;
				}

				if (tempIG_jiaohu.get(m).getMiddleX() == this.nowSelected
						.getMiddleX()
						&& tempIG_jiaohu.get(m).getNumber() != this.nowSelected
								.getNumber()) {

					UI.Main.errmes = nowSelected.getName()
							+ nowSelected.getNumber()
							+ " can't happen at the same time of "
							+ tempIG_jiaohu.get(m).getName()
							+ tempIG_jiaohu.get(m).getNumber() + " !";

					UI.Main.errstate = 1;

					return false;
				}

			}
		}
		return true;
	}

	private double getValue(Hashtable<Integer, String> p_weight, int key) {

		String weight_str = p_weight.get(key);

		double m_weight = Double.valueOf(weight_str.substring(weight_str
				.indexOf(',') + 1));

		return m_weight;
	}


	public void addConstraint(String from, String cons, String to, String num) {
		//if (checkConstraint(from, cons, to))
			this.south.addConstraint(from, cons, to ,num);
		//else
		//	JOptionPane.showMessageDialog(this, Main.errmes);
	}


	public boolean checkConstraint(String fName, String con, String tName) {

		int from = Integer.valueOf(fName.substring(3));
		int to = Integer.valueOf(tName.substring(3));

		int igs_size = this.igs.size();
		int state = 0;

		if (con.equals("��")) {
			state = 1;
		} else if (con.equals("<") || con.equals("=")) {
			state = 2;
		}

		for (int i = 0; i < igs_size; i++) {

			Hashtable<Integer, String> weight = igs.get(i).getOrder();

			if (weight.get(from) != null && weight.get(to) != null) {

				String group_str = weight.get(from);
				int from_group = Integer.valueOf(group_str.substring(0, 1));

				String group_str2 = weight.get(from);
				int to_group = Integer.valueOf(group_str2.substring(0, 1));

				if (from_group != to_group) {

					return true;

				} else {

					double from_value = this.getValue(weight, from);
					double to_value = this.getValue(weight, to);

					if (from_value > to_value) {

						UI.Main.errmes = tName + " must before " + fName + " !";

						UI.Main.errstate = 1;

						return false;
					} else if (from_value < to_value && state != 0)
						return true;
					else if ((from_value - to_value) < 0.1 && state != 0) {

						UI.Main.errmes = fName
								+ " can't happen at the same time of " + tName
								+ " !";

						UI.Main.errstate = 1;

						return false;
					}
				}
			}
		}
		return true;
	}

	public InstantGraph whichGraph(Jiaohu o) {
		InstantGraph result = null;
		for (int i = 0; i < this.igs.size(); i++) {
			if (this.igs.get(i).getJiaohu().contains(o)) {
				result = this.igs.get(i);
			}
		}
		return result;
	}

	public void createTxtFile() throws IOException {
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setDialogTitle("Export Relations into Txt File");
		jFileChooser.addChoosableFileFilter(new TxtFileFilter());
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jFileChooser.showDialog(null,null);
		File file = jFileChooser.getSelectedFile();
		if(!file.exists()) file.createNewFile();
		else {
			int i = JOptionPane.showConfirmDialog(jFileChooser, file + " has already existed,du you want to override it?");
			if(i == JOptionPane.YES_OPTION);
			else return;
		}
		String path = file.getPath();
		boolean flag = false;
		//String filein = "\r\n";
		String temp = "";

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			// ���ļ�����������
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();
			Set<Integer> set = new HashSet<>();
			int max = 0;
			for(int i = 0;i < Main.win.myIntDiagram.size();i++){
				IntDiagram tempIntDiagram = Main.win.myIntDiagram.get(i);
				for(int j = 0;j < tempIntDiagram.getJiaohu().size();j++){
					Jiaohu tempJiaoHu = (Jiaohu) tempIntDiagram.getJiaohu().get(j);
					System.out.println(tempJiaoHu.getName());
					max = Math.max(max, tempJiaoHu.getNumber());
				}
			}

			for(int i = 1;i <= max ;i++){
				buf = buf.append("int" + i);
				if(i == max) buf = buf.append(";");
				buf = buf.append(System.getProperty("line.separator"));
			}

			buf = buf.append(System.getProperty("line.separator"));

			for(int i = 0;i < south.relationString.size();i++){
				buf = buf.append(south.relationString.get(i) + ";");
				buf = buf.append(System.getProperty("line.separator"));
			}
			for(int i = 0;i < Main.win.myIntDiagram.size();i++){
				IntDiagram tempIntDiagram = Main.win.myIntDiagram.get(i);
				for(int j = 0;j < tempIntDiagram.getChangjing().size();j++){
					Changjing changjing = (Changjing) tempIntDiagram.getChangjing().get(j);
					Jiaohu tempFrom = changjing.getFrom();
					Jiaohu tempTo = changjing.getTo();
					if(changjing.getState() == 1){
						buf = buf.append(tempFrom.getName()+tempFrom.getNumber() + " StrictPre " + tempTo.getName() + tempTo.getNumber() + ";");
						buf = buf.append(System.getProperty("line.separator"));
					}
					else if(changjing.getState() == 2){
						buf = buf.append(tempFrom.getName()+tempFrom.getNumber() + " Coincidence " + tempTo.getName() + tempTo.getNumber() + ";");
						buf = buf.append(System.getProperty("line.separator"));
					}
					else if(changjing.getState() == 3){
						buf = buf.append(tempFrom.getName()+tempFrom.getNumber() + " StrictPre " + tempTo.getName() + tempTo.getNumber() + ";");
						buf = buf.append(System.getProperty("line.separator"));
					}
					else if(changjing.getState() == 4){
						buf = buf.append(tempTo.getName()+tempTo.getNumber() + " StrictPre " + tempFrom.getName() + tempFrom.getNumber() + ";");
						buf = buf.append(System.getProperty("line.separator"));
					}
					else{
						buf = buf.append(tempFrom.getName()+tempFrom.getNumber() + " StrictPre " + tempTo.getName() + tempTo.getNumber() + ";");
						buf = buf.append(System.getProperty("line.separator"));
					}
				}
			}

			for(int i = 0;i < this.constructedClocks.size();i++){
				buf = buf .append(constructedClocks.get(i)+";");
				buf = buf.append(System.getProperty("line.separator"));
			}
			//buf.append(filein);

			/*
			// ������ļ�ԭ�е�����
			for (int j = 1; (temp = br.readLine()) != null; j++) {
				buf = buf.append(temp);
				// System.getProperty("line.separator")
				// ������֮��ķָ��� �൱�ڡ�\n��
				buf = buf.append(System.getProperty("line.separator"));
			}
			buf.append(filein);
			*/

			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
		} catch (IOException e1) {
			// TODO �Զ����� catch ��
			e1.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
			JOptionPane.showMessageDialog(null,"success!","success",JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
