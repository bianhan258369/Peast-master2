package UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Shape.Phenomenon;

public class ClockConsDialog extends JDialog implements ActionListener {

	String[] constraints = { "subClock", "strictPre", "nstrictPre",
			"alternate", "boundedDiff" };
	JComboBox constraint = new JComboBox(constraints);

	JComboBox ClockFrom = new JComboBox();
	DefaultComboBoxModel modelFrom = new DefaultComboBoxModel();
	JComboBox ClockTo = new JComboBox();
	DefaultComboBoxModel modelTo = new DefaultComboBoxModel();
	LinkedList<InstantGraph> graphs;

	JTextField number = new JTextField();

	JButton confirm = new JButton("OK");

	JLabel background = null;

	public ClockConsDialog(LinkedList<InstantGraph> graphs) {
		super(Main.win, true);
		this.graphs = graphs;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() {
		// TODO Auto-generated method stub
		setTitle("ClockConstraintEditor");
		setSize(new Dimension(400, 150));
		this.setResizable(false);
		ImageIcon img = new ImageIcon(Main.class.getResource("/images/ee.jpg"));
		background = new JLabel(img);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		setLocation((int) width / 2 - 200, (int) height / 2 - 150);
		final JPanel contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setLayout(null);

		constraint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int selectedIndex = constraint.getSelectedIndex();
				if (selectedIndex == 3) {
					contentPane.add(number);
					repaint();
				} else if (number != null) {
					contentPane.remove(number);
					repaint();
				}
			}
		});

		ClockFrom.setModel(this.modelFrom);
		if (this.graphs != null) {
			for (int i = 0; i < this.graphs.size(); i++) {
				if (this.graphs.get(i).getType() == InstantGraph.TYPE_CONSTRUCTION)
					continue;
				this.modelFrom
						.addElement("CLK:" + this.graphs.get(i).getName());
			}
		}

		ClockTo.setModel(this.modelTo);
		if (graphs != null) {
			for (int i = 0; i < this.graphs.size(); i++) {
				if (this.graphs.get(i).getType() == InstantGraph.TYPE_CONSTRUCTION)
					continue;
				this.modelTo.addElement("CLK:" + this.graphs.get(i).getName());
			}
		}

		ClockFrom.setBounds(20, 20, 120, 20);
		constraint.setBounds(150, 20, 110, 20);
		ClockTo.setBounds(270, 20, 120, 20);
		// number.setBounds(220,20,60,20);

		confirm.setBounds(150, 80, 80, 25);
		confirm.addActionListener(this);

		contentPane.add(ClockFrom);
		contentPane.add(constraint);
		contentPane.add(ClockTo);
		contentPane.add(confirm);

		this.setContentPane(contentPane);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("OK")) {
			dispose();
			String from = ClockFrom.getSelectedItem().toString();
			String to = ClockTo.getSelectedItem().toString();
			String cons = constraint.getSelectedItem().toString();
			String num = "";
			if (constraint.getSelectedIndex() == 3) {
				num = "=" + number.getText();
			}
			Main.win.instantPane.south.addConstraint(from, cons, to,num);
		}
	}

	public static void main(String[] arg0) {
		new ClockConsDialog(null);
	}

}
