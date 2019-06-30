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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import Shape.Rect;

public class ClockDialog extends JDialog implements ActionListener {

	Rect editor;

	JTextField unitText = new JTextField();
	JButton confirm = new JButton("ok");
	JButton cancel = new JButton("cancel");
	JLabel background = null;


	Clock myClock;

	public void actionPerformed(ActionEvent e) {
		LinkedList<Rect> tempRects = new LinkedList<Rect>();
		for(int i = 0;i < Main.win.myProblemDiagram.components.size();i++){
			if(Main.win.myProblemDiagram.components.get(i) instanceof Rect && Main.win.myProblemDiagram.getMachine() != Main.win.myProblemDiagram.components.get(i)){
				tempRects.add((Rect)Main.win.myProblemDiagram.components.get(i));
			}
		}
		LinkedList<Clock> tempClocks = new LinkedList<Clock>();
		for(int i = 0;i < Main.win.cd.getClocks().size();i++){
			tempClocks.add(Main.win.cd.getClocks().get(i));
		}
		if (e.getActionCommand().equals("ok")) {
			dispose();
			myClock.updateUnit(unitText.getText());
			for(int i = 0;i < Main.win.myIntDiagram.size();i++){
				InstantPane instantPane = Main.win.instantPanes[i];
				for(int j = 0;j < instantPane.igs.size();j++){
					InstantGraph ig = instantPane.igs.get(j);
					if(ig.getClock().getName().equals(myClock.getName()) && ig.getClock().getDomainText().equals(myClock.getDomainText()) && !ig.getClock().getUnit().equals(myClock.getUnit())){
						ig.setClock(myClock);
					}
				}
				instantPane.repaint();
			}
		} else {
			dispose();
		}
	}

	public ClockDialog(Rect editor) {
		super(Main.win, true);
		this.editor = editor;
		for(int i = 0;i < Main.win.cd.getClocks().size();i++){
			if(editor.getText().equals(Main.win.cd.getClocks().get(i).getDomainText())){
				myClock = Main.win.cd.getClocks().get(i);
				break;
			}
		}
		if (myClock == null) {
			myClock = new Clock(editor.getText());
		}
		try {
			Init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void Init() throws Exception {
		setTitle("ClockEditor");
		setSize(new Dimension(360, 160));
		this.setResizable(false);
		ImageIcon img = new ImageIcon(Main.class.getResource("/images/ee.jpg"));
		background = new JLabel(img);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		setLocation((int) width / 2 - 200, (int) height / 2 - 70);
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setLayout(null);

		JLabel unitLabel = new JLabel("Unit :");

		unitText.setEnabled(true);
		unitText.setText(myClock.getUnit());


		unitLabel.setBounds(60, 45, 100, 20);
		unitText.setBounds(180, 45, 100, 20);

		confirm.setBounds(100, 90, 80, 25);
		confirm.addActionListener(this);
		cancel.setBounds(200, 90, 80, 25);
		cancel.addActionListener(this);


		contentPane.add(unitLabel);
		contentPane.add(unitText);
		contentPane.add(confirm);
		contentPane.add(cancel);

		this.setContentPane(contentPane);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		this.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		// ClockDialog dd = new ClockDialog();
	}

}
