package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class ConstraintPane extends JPanel {
	/*
	 * 示例，将所在的Pane设置为BorderLayout this.setLayout(new BorderLayout());
	 * ConstraintPane south=new ConstraintPane(); 然后将这个Pane加到它的下方
	 * this.add(south,BorderLayout.SOUTH); 添加调用函数
	 * south.addConstraint("x1>x2");参数类型String
	 */
	public ArrayList<String> relationString = new ArrayList<>();
	private ArrayList<JLabel> labellist = new ArrayList<JLabel>();
	private ArrayList<JButton> buttonlist = new ArrayList<JButton>();
	private final int PANEWIDTH = 210;
	private final int PANEHEIGHT = 140;
	JPanel relation = null;

	ConstraintPane() {
		this.setLayout(new BorderLayout());
		relation = new JPanel();
		relation.setPreferredSize(new Dimension(PANEWIDTH, PANEHEIGHT));
		relation.setBackground(Color.white);
		relation.revalidate();

		relation.setLayout(null);
		JScrollPane constraint = new JScrollPane(relation);
		constraint.setPreferredSize(new Dimension(500, 200));
		constraint.setViewportView(relation);
		constraint.setBorder(new TitledBorder("Other Constrains :"));

		this.setLayout(new BorderLayout());

		this.add(constraint, BorderLayout.EAST);
	}

	public void addConstraint(String from, String cons, String to, String num) {
		/*
		 * 增加触发的函数，要加代码在这里加
		 */
		String constraint;
        if(Main.win.instantPane.constraintRelations.get(cons) == 0){
            if(num.trim() != "" && num != null) constraint = from + " " + cons + " " + num + " " +  to ;
            else constraint = from + " " + cons + " " + to;
            relationString.add(constraint);
        }
        else{
            String[] temps = num.split(" ");
            StringBuilder s = new StringBuilder(from + " " + cons + " " + "[");
            for(int i = 0;i < temps.length;i++){
                if(i != temps.length - 1) s.append(temps[i] + ",");
                else s.append(temps[i]);
            }
            s.append("]" + " " + to);
            constraint = s.toString();
            relationString.add(constraint);
        }

		final JLabel label = new JLabel(constraint);
		int size = labellist.size();
		label.setBounds(25, size * 25 + 10, 300, 20);
		final JButton button = new JButton("DEL");
		// ImageIcon icon = new
		// ImageIcon(Main.class.getResource("/image/delete.png"));
		// button.setIcon(icon);
		button.setBounds(340, size * 25 + 10, 50, 21);
		button.setContentAreaFilled(false); // 设置JButton透明
        String finalConstraint = constraint;
        button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				relationString.remove(finalConstraint);
				String relationStr = cons;
				labellist.remove(label);
				buttonlist.remove(button);
				int res = 0;
				for(int i = 0;i < Main.win.instantPane.froms.size();i++){
					if( Main.win.instantPane.froms.get(i).getNumber() == ConstraintDialog.getStringNum(from)
							&& Main.win.instantPane.tos.get(i).getNumber() == ConstraintDialog.getStringNum(to)
							&& Main.win.instantPane.ClockRelations.get(i).getKey() .equals(relationStr)){
						res = i;
						break;
					}
				}
				Main.win.instantPane.ClockRelations.remove(res);
				Main.win.instantPane.params.remove(res);
				Main.win.instantPane.froms.remove(res);
				Main.win.instantPane.tos.remove(res);
				int num = labellist.size();
				if (num > 4) {
					relation.setPreferredSize(new Dimension(PANEWIDTH,
							PANEHEIGHT + (num - 4) * 25));
					relation.revalidate();
				}
				conrepaint();
				Main.win.instantPane.repaint();
			}

		});
		if (size > 4) {
			relation.setPreferredSize(new Dimension(PANEWIDTH, PANEHEIGHT
					+ (size - 4) * 25));
			relation.revalidate();
		}
		labellist.add(label);
		buttonlist.add(button);
		relation.add(button);
		relation.add(label);
		this.repaint();
	}

	public void conrepaint() {
		/*
		 * 删除触发的函数，要加代码在这里加
		 */

		relation.removeAll();
		for (int i = 0; i < labellist.size(); i++) {
			labellist.get(i).setBounds(25, i * 25 + 10, 150, 20);
			buttonlist.get(i).setBounds(190, i * 25 + 10, 50, 21);
			relation.add(labellist.get(i));
			relation.add(buttonlist.get(i));
		}
		repaint();
	}

}
