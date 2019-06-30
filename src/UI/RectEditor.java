package UI;

import Shape.Rect;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class RectEditor extends JDialog implements ActionListener {
	Rect editor;
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JButton jButton1 = new JButton();
	JButton jButton2 = new JButton();
	JTextField jTextField1 = new JTextField();
	JTextField jTextField2 = new JTextField();
	JLabel jLabel3 = new JLabel();
	String[] neirong = { "", "Causal", "Biddable", "Lexical" };
	String[] neirong1 = { "", "Machine", "GivenDomain", "DesignedDomain" };
	JComboBox jComboBox1 = new JComboBox(this.neirong);
	JLabel jLabel4 = new JLabel();
	JComboBox jComboBox2 = new JComboBox(this.neirong1);

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Set")) {
			if ((this.jTextField1.getText().trim().equals(""))
					|| (this.jTextField2.getText().trim().equals(""))) {
				return;
			}

			this.editor.setText(this.jTextField1.getText());
			this.editor.setShortName(this.jTextField2.getText());
			if (this.editor.getState() != 2) {
				if (this.jComboBox1.getSelectedItem().toString()
						.equals("Causal"))
					this.editor.setCxb('C');
				if (this.jComboBox1.getSelectedItem().toString()
						.equals("Biddable"))
					this.editor.setCxb('B');
				if (this.jComboBox1.getSelectedItem().toString()
						.equals("Lexical"))
					this.editor.setCxb('X');
			}
			if (this.jComboBox2.getSelectedItem().toString().equals("Machine")) {
				this.editor.setState(2);
			}
			if (this.jComboBox2.getSelectedItem().toString()
					.equals("GivenDomain")) {
				this.editor.setState(0);
			}
			if (this.jComboBox2.getSelectedItem().toString()
					.equals("DesignedDomain")) {
				this.editor.setState(1);
			}

			dispose();
		}
		if (e.getActionCommand().equals("Cancel"))
			dispose();
	}

	public RectEditor(Rect editor) {
		super(Main.win, true);
		this.editor = editor;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setTitle("Editor");
		setSize(new Dimension(400, 326));
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		setLocation((int) width / 2 - 200, (int) height / 2 - 150);
		if (this.editor.getText().startsWith("?")) {
			this.jTextField1.setText("");
		} else {
			this.jTextField1.setText(this.editor.getText());
		}
		this.jTextField2.setText(this.editor.getShortName());
		if (this.editor.getState() == 2) {
			this.jLabel3.setEnabled(false);
			this.jComboBox1.setEnabled(false);
		}
		this.jLabel1.setText("Description");
		this.jLabel1.setBounds(new Rectangle(41, 52, 115, 25));
		getContentPane().setLayout(null);
		this.jLabel2.setText("ShortName");
		this.jLabel2.setBounds(new Rectangle(42, 97, 117, 28));
		this.jButton1.setBounds(new Rectangle(94, 239, 73, 25));
		this.jButton1.setText("Set");
		this.jButton2.setBounds(new Rectangle(216, 238, 73, 25));
		this.jButton2.setText("Cancel");
		this.jTextField1.setBounds(new Rectangle(202, 54, 100, 22));
		this.jTextField2.setBounds(new Rectangle(202, 98, 101, 22));
		this.jLabel3.setText("DomType");
		this.jLabel3.setBounds(new Rectangle(43, 147, 102, 16));
		this.jComboBox1.setBounds(new Rectangle(202, 145, 101, 22));
		this.jLabel4.setText("Physical Property");
		this.jLabel4.setBounds(new Rectangle(44, 187, 109, 16));
		this.jComboBox2.setBounds(new Rectangle(202, 184, 101, 22));
		getContentPane().add(this.jLabel1, null);
		getContentPane().add(this.jLabel2, null);
		getContentPane().add(this.jTextField1, null);
		getContentPane().add(this.jLabel3, null);
		getContentPane().add(this.jComboBox1, null);
		getContentPane().add(this.jTextField2, null);
		getContentPane().add(this.jButton1, null);
		getContentPane().add(this.jButton2, null);
		getContentPane().add(this.jLabel4, null);
		getContentPane().add(this.jComboBox2, null);
		this.jButton1.addActionListener(this);
		this.jButton2.addActionListener(this);
	}
}
