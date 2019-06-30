package UI;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class CConstructionDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -959826007760179555L;

	JLabel jLabelType = new JLabel("Type");

	ButtonGroup bg = new ButtonGroup();
	JRadioButton filter = new JRadioButton("filteredby");
	JRadioButton union = new JRadioButton("union");
	JRadioButton sup = new JRadioButton("sup");
	JRadioButton inf = new JRadioButton("inf");

	JTextField editFilter = new JTextField();

	JLabel jLabelName = new JLabel();
	JLabel jLabelAdd = new JLabel();
	JButton jBtnCreate = new JButton();
	JButton jBtnCancel = new JButton();
	JButton jBtnAdd = new JButton();
	JTextField jEditName = new JTextField();
	String[] clocks;
	JComboBox jComboBox1;
	DefaultListModel listModel1 = new DefaultListModel();
	JList combineList = new JList(this.listModel1);
	List<String> tempList = new LinkedList();

	int selectedIndex = -1;

	public CConstructionDialog(LinkedList<String> clocks) {
		super(Main.win, true);

		this.clocks = (String[]) clocks.toArray(new String[0]);
		jComboBox1 = new JComboBox(this.clocks);

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() {
		setTitle("Clock Construction");
		setSize(new Dimension(400, 420));
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		setLocation((int) width / 2 - 200, (int) height / 2 - 150);

		getContentPane().setLayout(null);

		this.jLabelName.setText("Name");
		this.jLabelName.setBounds(new Rectangle(42, 52, 115, 25));

		this.jLabelType.setBounds(new Rectangle(42, 90, 117, 28));

		bg.add(this.filter);
		bg.add(this.union);
		bg.add(this.sup);
		bg.add(this.inf);

		this.filter.setBounds(new Rectangle(100, 95, 100, 20));
		this.union.setBounds(new Rectangle(100, 115, 100, 20));
		this.sup.setBounds(new Rectangle(100, 135, 100, 20));
		this.inf.setBounds(new Rectangle(100, 155, 100, 20));

		this.editFilter.setBounds(new Rectangle(200, 95, 100, 20));
		this.editFilter.setEditable(false);

		this.jLabelAdd.setText("Add");
		this.jLabelAdd.setBounds(new Rectangle(42, 175, 117, 28));

		this.jBtnAdd.setText("Add");
		this.jBtnAdd.setBounds(new Rectangle(250, 175, 70, 28));
		this.jBtnAdd.setEnabled(false);

		this.jBtnCreate.setBounds(new Rectangle(94, 330, 73, 25));
		this.jBtnCreate.setText("Create");

		this.jBtnCancel.setBounds(new Rectangle(216, 330, 73, 25));
		this.jBtnCancel.setText("Cancel");

		this.jEditName.setBounds(new Rectangle(100, 54, 100, 22));
		this.jComboBox1.setBounds(new Rectangle(100, 180, 101, 22));
		this.combineList.setBounds(new Rectangle(42, 210, 300, 100));

		getContentPane().add(this.jLabelName, null);
		getContentPane().add(this.jEditName, null);
		getContentPane().add(this.jLabelType, null);
		getContentPane().add(this.filter, null);
		getContentPane().add(this.union, null);
		getContentPane().add(this.sup, null);
		getContentPane().add(this.inf, null);
		getContentPane().add(this.editFilter, null);
		getContentPane().add(this.jLabelAdd, null);
		getContentPane().add(this.jComboBox1, null);
		getContentPane().add(this.jBtnAdd, null);
		getContentPane().add(this.combineList, null);
		getContentPane().add(this.jBtnCreate, null);
		getContentPane().add(this.jBtnCancel, null);
		this.jBtnAdd.addActionListener(this);
		this.jBtnCreate.addActionListener(this);
		this.jBtnCancel.addActionListener(this);
		this.filter.addActionListener(this);
		this.union.addActionListener(this);
		this.sup.addActionListener(this);
		this.inf.addActionListener(this);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == filter) {
			this.editFilter.setEditable(true);
			this.jBtnAdd.setEnabled(false);
			selectedIndex = 0;

		} else if (e.getSource() == union) {
			this.editFilter.setEditable(false);
			this.jBtnAdd.setEnabled(true);
			selectedIndex = 1;

		} else if (e.getSource() == sup) {
			this.editFilter.setEditable(false);
			this.jBtnAdd.setEnabled(true);
			selectedIndex = 2;

		} else if (e.getSource() == inf) {
			this.editFilter.setEditable(false);
			this.jBtnAdd.setEnabled(true);
			selectedIndex = 3;

		} else if (e.getSource() == jBtnAdd) {
			this.listModel1.addElement(this.jComboBox1.getSelectedItem()
					.toString());

		} else if (e.getSource() == jBtnCreate) {
			LinkedList<String> all = new LinkedList<String>();
			switch (selectedIndex) {
			case -1:
				dispose();
				return;
			case 0:
				all.add(this.jComboBox1.getSelectedItem().toString());
				all.add(this.editFilter.getText());
				Main.win.instantPane.constructedClocks.add(this.listModel1.getElementAt(0).toString() + " " + "filteredBy" + " " + this.editFilter.getText() + "(" + this.jEditName.getText() + ")");
				break;
			case 1:
				for (int i = 0; i < this.listModel1.size(); i++) {
					all.add(listModel1.getElementAt(i).toString());
				}
				Main.win.instantPane.constructedClocks.add(this.listModel1.getElementAt(0) + " " + "Union" + " " + this.listModel1.getElementAt(1) + "(" + this.jEditName.getText() + ")");
				break;
			case 2:
				for (int i = 0; i < this.listModel1.size(); i++) {
					all.add(listModel1.getElementAt(i).toString());
					Main.win.instantPane.constructedClocks.add(this.listModel1.getElementAt(0) + " " + "Sup" + " " + this.listModel1.getElementAt(1) + "(" + this.jEditName.getText() + ")");
				}
				break;
			case 3:
				for (int i = 0; i < this.listModel1.size(); i++) {
					all.add(listModel1.getElementAt(i).toString());
					Main.win.instantPane.constructedClocks.add(this.listModel1.getElementAt(0) + " " + "Inf" + " " + this.listModel1.getElementAt(1) + "(" + this.jEditName.getText() + ")");
				}
				break;
			}
			/*
			 * InstantGraph tempIg = new InstantGraph(selectedIndex, all, new
			 * Clock( jEditName.getText(), 0, "s"));
			 * Main.win.instantPane.addGraph(tempIg);
			 */
			Main.win.instantPane
					.addConstructionGraph(selectedIndex, all, jEditName
							.getText(), new Clock(jEditName.getText(), 0, "s",null));
			dispose();

		} else if (e.getSource() == jBtnCancel) {
			dispose();

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedList test = new LinkedList();
		test.add("a");
		test.add("b");
		test.add("c");
		new CConstructionDialog(test);
	}

}
