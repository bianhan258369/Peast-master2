package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClockRelationDialog extends JDialog implements ActionListener {
    JTextField relationName = new JTextField();
    JTextField relationParaNum = new JTextField();
    JLabel name = new JLabel();
    JLabel paraNum = new JLabel();
    JButton confirm = new JButton("Add");
    JPanel contentPane = new JPanel();
    JLabel background = null;

    public ClockRelationDialog(){
        super(Main.win, true);
        setTitle("ClockConstraintRelationEditor");
        setSize(new Dimension(500, 150));
        this.setResizable(false);
        ImageIcon img = new ImageIcon(Main.class.getResource("/images/ee.jpg"));
        background = new JLabel(img);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        double width = d.getWidth();
        double height = d.getHeight();
        setLocation((int) width / 2 - 200, (int) height / 2 - 150);

        contentPane.setOpaque(false);
        contentPane.setLayout(null);

        name.setText("Input Relation Name : ");
        name.setBounds(20, 25,300, 20);
        paraNum.setText("Input Parameters Number : ");
        paraNum.setBounds(20, 50, 300,20);
        relationName.setBounds(340, 25, 50, 20);
        relationParaNum.setBounds(340, 50, 50, 20);
        confirm.setBounds(220, 80, 50, 20);
        confirm.addActionListener(this);

        contentPane.add(relationName);
        contentPane.add(relationParaNum);
        contentPane.add(name);
        contentPane.add(paraNum);
        contentPane.add(confirm);

        this.setContentPane(contentPane);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Add")){
            String name = relationName.getText();
            String params = relationParaNum.getText();
            boolean flag = true;
            for(int i = 0;i < params.length();i++){
                if(!(params.charAt(i) >= '0' && params.charAt(i) <= '9')) flag = false;
            }

            if(name.trim().equals("") || name == null){
                JOptionPane.showMessageDialog(null, "Please input RelationName", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if(! flag){
                JOptionPane.showMessageDialog(null, "Please input Parameters Number as an Integer", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int paramsNum = Integer.parseInt(params);
            Main.win.instantPane.constraintRelations.put(name, paramsNum);
            dispose();
        }
    }
}
