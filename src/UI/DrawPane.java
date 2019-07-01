package UI;

import Ontology.Ontology_c;
import Shape.Data;
import Shape.Diagram;
import Shape.IntDiagram;
import Shape.Line;
import Shape.Oval;
import Shape.Persist;
import Shape.Phenomenon;
import Shape.Rect;
import Shape.Shape;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DrawPane extends JPanel implements ActionListener {
	JButton jb1 = new JButton();
	public int i = 0;
	Ontology_c ont;
	JLabel[] jLabel = new JLabel[4];

	public static void main(String[] args) {
	}

	public void event_load() {
		this.ont = new Ontology_c();
		Object[] bs = { "OK" };
		// if(this.ont.checkConsistence())
		/*JOptionPane.showOptionDialog(Main.win,
				"Succeed to load the file of the ontology!", "Tip", 0, 1, null,
				bs, null);*/

		Main.win.check.setEnabled(true);
	}

	public void event_check() {
		if (this.ont.checkConsistence()) {
			Object[] bs = { "OK" };

			/*JOptionPane.showOptionDialog(Main.win,
					"The ontology is consistent!", "Tip", 0, 1, null, bs, null);*/
		} else {
			Object[] bs = { "OK" };

			/*JOptionPane.showOptionDialog(this,
					"The ontology is not consistent!", "Tip", 0, 0, null, bs,
					null);*/
		}

		Main.win.setButtonState(-1);
	}

	public DrawPane() {
		setLayout(null);

		int x = 10;
		int y = 10;
		int chang = 250;
		int kuan = 20;
		for (int i = 0; i < this.jLabel.length; i++) {
			this.jLabel[i] = new JLabel();
			add(this.jLabel[i]);
			this.jLabel[i].setEnabled(false);
			this.jLabel[i]
					.setBounds(new Rectangle(x, y + i * kuan, chang, kuan));
			add(this.jLabel[i]);
		}
		this.jb1.setBounds(new Rectangle(20, y + this.jLabel.length * kuan, 81,
				25));
		this.jb1.setText("next");
		add(this.jb1);
		this.jb1.setEnabled(false);
		this.jb1.addActionListener(this);
		this.jLabel[0].setText("1.read project file");
		this.jLabel[1].setText("2.read ontology file");
		this.jLabel[2].setText("3.manually add constrains");
		this.jLabel[3].setText("Finish");
	}

	public int getI(){
		return i;
	}

	private void setallnon() {
		for (int i = 0; i <= this.jLabel.length - 1; i++)
			this.jLabel[i].setEnabled(false);
	}

	public void setState(int i) {
		this.i = i;
		Main.win.setButtonState(i);
	}

	private void setLabel() {
		if (this.i == 0) {
			setallnon();
			this.jLabel[0].setEnabled(true);
			this.jLabel[1].setEnabled(true);
		}
		if (this.i == 1) {
			setallnon();
			this.jLabel[0].setEnabled(true);
			this.jLabel[2].setEnabled(true);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (this.i == 0) {
			Object[] bs = { "Yes", "No" };

			int k = JOptionPane.showOptionDialog(Main.win,
					"Have you finished loading project xml file?", "Tip", 2, 3, null,
					bs, null);

			if (k == 0) {
				if(Main.win.myProblemDiagram != null && Main.win.instantPanes.size() != 0){
					setState(1);
					return;
				}
				else {
					JOptionPane.showMessageDialog(Main.win, "load error!","ERROR",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}

		if (this.i == 1) {
			Object[] bs = { "Yes", "No" };

			int k = JOptionPane.showOptionDialog(Main.win,
					"Have you finished loading ontologty file?", "Tip", 2, 3,
					null, bs, null);

			if (k == 0) {
				setState(2);
			}
			return;
		}

		if (this.i == 2){
			Object[] bs = { "Yes", "No" };

			int k = JOptionPane.showOptionDialog(Main.win,
					"Have you finished adding constrains?", "Tip", 2, 3,
					null, bs, null);

			if (k == 0) {
				setState(3);
				JOptionPane.showMessageDialog(Main.win, "Finished!");
			}
			return;
		}

		if (this.i == 3) {
			this.jLabel[0].setEnabled(false);
			this.jLabel[1].setEnabled(false);
			this.jLabel[2].setEnabled(false);
			this.jLabel[3].setEnabled(false);
			this.jb1.setEnabled(false);
			return;
		}
		/*
		if (this.i == 3) {
			//if (checkContextDiagram()) {  ////////////////////////////
			if (checkContextDiagram()) {
			Object[] bs = { "OK" };

				int k = JOptionPane.showOptionDialog(Main.win,
						"The diagram is correct!", "Tip", 0, 1, null, bs, null);

				setState(4);
				Diagram kk = Main.win.myContextDiagram
						.convertToProblemDiagram("ProblemDiagram");

				kk.setLineName();
				Main.win.myProblemDiagram = kk;
				MyPane xin = new MyPane(kk);
				Main.win.myDisplayPane.addPane(xin, "ProblemDiagram");
				Main.win.myInfoPane.treeAdd(1, 0, "");
			} else {
				Object[] bs = { "OK" };

				int k = JOptionPane.showOptionDialog(Main.win,
						"The diagram is wrong!", "Tip", 0, 0, null, bs, null);

				setState(0);
			}
			return;
		}
		if (this.i == 4) {
			Object[] bs = { "Yes", "No" };

			int k = JOptionPane.showOptionDialog(Main.win,
					"Have you finished identifying requirements?", "Tip", 2, 3,
					null, bs, null);

			if (k == 0) {
				setState(5);
			}
			return;
		}
		if (this.i == 5) {
			Object[] bs = { "Yes", "No" };

			int k = JOptionPane.showOptionDialog(Main.win,
					"Have you finished indentifying references?", "Tip", 2, 3,
					null, bs, null);

			if (k == 0) {
				setState(6);
			}
			return;
		}
		if (this.i == 6) {
			//if (checkProblemDiagram()) {                 //////////////////////////////
			if (checkProblemDiagram()) {////////////////////////////////////
				Object[] bs = { "OK" };

				int k = JOptionPane.showOptionDialog(Main.win,
						"The diagram is correct!", "Tip", 0, 1, null, bs, null);

				setState(7);
				LinkedList ll = Main.win.myProblemDiagram.getRequirements();
				for (int j = 0; j <= ll.size() - 1; j++) {
					Oval tmp_o = (Oval) ll.get(j);
					IntDiagram dd = new IntDiagram("ScenarioGraph" + (j + 1),
							tmp_o.getBiaohao());

					IntPane intp = new IntPane(dd, 0, Main.win.myProblemDiagram);
					Main.win.intPane_l.add(intp);
					Main.win.myDisplayPane.addPane(intp, intp.dd.getTitle());
					Main.win.myInfoPane.treeAdd(2, j + 1,
							"req" + dd.getBiaohao());
					setState(7);
				}
			} else {
				Object[] bs = { "OK" };

				int k = JOptionPane.showOptionDialog(Main.win,
						"The diagram is wrong!", "Tip", 0, 0, null, bs, null);

				setState(4);
			}
			return;
		}
		if (this.i == 9) {
			stragety1();
			return;
		}
		if (this.i == 7) {
			Object[] bs = { "Yes", "No" };

			int k = JOptionPane.showOptionDialog(Main.win,
					"Have you finished drawing scenario graphs?", "Tip", 2, 3,
					null, bs, null);

			if (k == 0) {
				setState(8);
				JOptionPane.showMessageDialog(Main.win,
						"You should check each graph by right click!");
			}

			return;
		}
		if (this.i == 8) {
			boolean hao = true;
			for (int i = 0; i <= Main.win.intPane_l.size() - 1; i++) {
				IntPane ip = (IntPane) Main.win.intPane_l.get(i);
				hao = (hao) && (ip.zhengque);
			}
			if (hao) {
				setState(9);
				JOptionPane.showMessageDialog(Main.win,
						"You have checked all the scenario graphs!");

				return;
			}

			JOptionPane.showMessageDialog(Main.win,
					"You should check all the scenariographs!");

			setState(7);
			return;
		}
		*/
	}

	private void stragety2() {
		Integer s = stragety2OR();
		if (s != null) {
			String message = "Subproblem"
					+ s.intValue()
					+ " has dynamic and static domain at same time!\nSo introduce model domain to seperate two concerns.";
			JOptionPane.showMessageDialog(Main.win, message);

			int i = s.intValue();
			MyPane ip = (MyPane) Main.win.subpd_l.get(i);
			Diagram dd = ip.dd;
			Diagram tmp_d = new Diagram("");
			Persist.save(new File("tmp"), dd);
			tmp_d = Persist.load(new File("tmp"));
			tmp_d.setTitle(dd.getTitle() + "1");
			MyPane xin = new MyPane(tmp_d);
			xin.setSub(true);
			Main.win.subpd_l.add(xin);
			Main.win.myDisplayPane.addPane(xin, tmp_d.getTitle());
			Main.win.myInfoPane.treeAdd1(1, 1, dd.getTitle());

			IntPane mp = (IntPane) Main.win.intPane_l.get(i);
			IntDiagram tt = mp.dd;
			Persist.save1(new File("tmp"), tt);
			IntDiagram dd_i = Persist.load1(new File("tmp"));
			dd_i.setTitle("ScenarioGraph"
					+ new Integer(dd.getTitle().substring(17)).intValue() + "1");

			IntPane intp = new IntPane(dd_i, 1, tmp_d);
			Main.win.intPane_l.add(intp);
			Main.win.myDisplayPane.addPane(intp, intp.dd.getTitle());

			Main.win.myInfoPane.treeAdd1(0, 1, "ScenarioGraph"
					+ new Integer(dd.getTitle().substring(17)).intValue());

			tmp_d = new Diagram(dd.getTitle() + "2");
			MyPane xin1 = new MyPane(tmp_d);
			xin1.setSub(true);
			Main.win.subpd_l.add(xin1);
			Main.win.myDisplayPane.addPane(xin1, tmp_d.getTitle());
			Main.win.myInfoPane.treeAdd1(1, 2, dd.getTitle());

			dd_i = new IntDiagram(
					"ScenarioGraph"
							+ new Integer(dd.getTitle().substring(17)).intValue()
							+ "2", 0);

			intp = new IntPane(dd_i, 1, tmp_d);
			Main.win.intPane_l.add(intp);
			Main.win.myDisplayPane.addPane(intp, intp.dd.getTitle());
			Main.win.myInfoPane.treeAdd1(0, 2, "ScenarioGraph"
					+ new Integer(dd.getTitle().substring(17)).intValue());

			xin.newMypane();
		}
	}

	private boolean testDiagram2(Diagram dd) {
		for (int i = 0; i <= dd.components.size() - 1; i++) {
			Shape tmp_s = (Shape) dd.components.get(i);
			if (tmp_s.shape == 2) {
				Line tmp_l = (Line) tmp_s;
				if (tmp_l.getState() != 0) {
					boolean yin = false;
					boolean yue = false;
					for (int j = 0; j <= tmp_l.phenomenons.size() - 1; j++) {
						Phenomenon tmp_p = (Phenomenon) tmp_l.phenomenons
								.get(j);
						if (tmp_p.getConstraining()) {
							yue = true;
						} else {
							yin = true;
						}
					}
					if ((yin) && (yue)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private Integer stragety2OR() {
		for (int i = 0; i <= Main.win.subpd_l.size() - 1; i++) {
			MyPane ip = (MyPane) Main.win.subpd_l.get(i);
			Diagram dd = ip.dd;
			if (testDiagram2(dd)) {
				return new Integer(i);
			}
		}
		return null;
	}

	private void stragety1() {
		String s = stragety1OR(Main.win.myProblemDiagram);
		if (s == null) {
			JOptionPane.showMessageDialog(Main.win,
					"All of the designed domain are created!");

			setState(10);
			return;
		}

		JOptionPane
				.showMessageDialog(
						Main.win,
						s
								+ " is no created!\nPlease identify another domain to create it!");

		FatherPane kk = Main.win.myDisplayPane.getMyPane("ProblemDiagram");
		if (kk != null) {
			Oval tmp_o = new Oval(300, 300);
			Rect tmp_r = new Rect(150, 300);
			Main.win.myProblemDiagram.add(tmp_o);
			Main.win.myProblemDiagram.add(tmp_r);
			tmp_r.setState(0);
			tmp_r.setText(Data.GD_TEXT);
			int j = Main.win.intPane_l.size();
			IntDiagram dd = new IntDiagram("ScenarioGraph" + (j + 1),
					tmp_o.getBiaohao());

			IntPane intp = new IntPane(dd, 0, Main.win.myProblemDiagram);
			Main.win.intPane_l.add(intp);
			Main.win.myDisplayPane.addPane(intp, intp.dd.getTitle());
			Main.win.myInfoPane.treeAdd(2, j + 1, "req" + dd.getBiaohao());
			Main.win.myDisplayPane.setSelected(kk);
		}
	}

	private String stragety1OR(Diagram dd) {
		for (int i = 0; i <= dd.components.size() - 1; i++) {
			Shape tmp_s = (Shape) dd.components.get(i);
			if (tmp_s.shape == 0) {
				Rect tmp_r = (Rect) tmp_s;
				if (tmp_r.getState() == 1) {
					boolean zhaodao = false;
					for (int j = 0; j <= dd.components.size() - 1; j++) {
						Shape tmps = (Shape) dd.components.get(j);
						if (tmps.shape == 2) {
							Line tmpl = (Line) tmps;
							if ((tmpl.getState() != 2)
									|| (!tmpl.to.equals(tmp_r)))
								continue;
							zhaodao = true;
						}

					}

					if (!zhaodao) {
						return tmp_r.getText();
					}
				}
			}
		}
		return null;
	}

	boolean checkContextDiagram() {
		return new Ontology_c().check(Main.win.myContextDiagram,
				"ContextDiagram");
	}

	boolean checkProblemDiagram() {
		return new Ontology_c().check(Main.win.myProblemDiagram,
				"ProblemDiagram");
	}
}