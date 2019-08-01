package UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import Shape.Changjing;
import Shape.Jiaohu;
import Shape.Phenomenon;
import Shape.Rect;
import javafx.util.Pair;

public class ConstraintDialog extends JDialog implements ActionListener {
	JComboBox constraint;
	JComboBox JiaohuFrom=new JComboBox();
	DefaultComboBoxModel modelFrom = new DefaultComboBoxModel();
	JComboBox JiaohuTo=new JComboBox();
	DefaultComboBoxModel modelTo = new DefaultComboBoxModel();
	LinkedList<InstantGraph> igs;
	List relationList = new LinkedList();
	private boolean[] visited = new boolean[100];

	List<JTextField> numbers = new LinkedList<>();
	//JTextField number = new JTextField();
	//JTextField number2 = new JTextField();
	
	JButton confirm = new JButton("OK");
	JButton editRelations = new JButton("Edit");
	JButton refresh = new JButton("Refresh");
	
	JLabel background = null;

	public ConstraintDialog(LinkedList<InstantGraph> igs) {
		super(Main.win, true);
		this.igs=igs;
		try {
			jbInit();
			}
		    catch (Exception e) {
		    	e.printStackTrace();
		    	}
	}
	
	private void jbInit() {
		// TODO Auto-generated method stub

		for(Map.Entry<String, Integer> entry : Main.win.instantPane.constraintRelations.entrySet()){
            if(!relationList.contains(entry.getKey())) relationList.add(entry.getKey());
		}
        constraint = new JComboBox(relationList.toArray());
		setTitle("ConstraintEditor");
		setSize(new Dimension(500, 240));
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

		constraint.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int selectedIndex=constraint.getSelectedIndex();
				String temp = (String)constraint.getItemAt(selectedIndex);
				if(Main.win.instantPane.constraintRelations.get(temp) != 0){
                    for(int i = 0;i < numbers.size();i++){
                        contentPane.remove(numbers.get(i));
                    }
                    int size = numbers.size();
                    while(numbers.size() != 0) numbers.remove(0);
				    for(int i = 0;i < Main.win.instantPane.constraintRelations.get(temp);i++){
				        JTextField number = new JTextField();
                        numbers.add(number);
                    }
                    for(int i = 0;i < numbers.size();i++){
                        numbers.get(i).setBounds(160 + 35 * i, 100, 30, 20);
                    }
                    for(int i = 0;i < numbers.size();i++){
				        contentPane.add(numbers.get(i));
                    }
                    repaint();
                }
                else{
				    for(int i = 0;i < numbers.size();i++){
				        contentPane.remove(numbers.get(i));
                    }
                    int size = numbers.size();
                    while(numbers.size() != 0) numbers.remove(0);
                    repaint();
                }
			}
		});
		
		JiaohuFrom.setModel(this.modelFrom);
		if(igs!=null){
			for (int i = 0; i < this.igs.size(); i++) {
				LinkedList temp=igs.get(i).getInts();
				int intsSize=temp.size();
				for(int m=0;m<intsSize;m++){
					Phenomenon temp_p = (Phenomenon) temp.get(m);
					this.modelFrom.addElement("int"+temp_p.getBiaohao());
				}
			}
		}
		
		JiaohuTo.setModel(this.modelTo);
		if(igs!=null){
			for (int i = 0; i < this.igs.size(); i++) {
				LinkedList temp=igs.get(i).getInts();
				int intsSize=temp.size();
				for(int m=0;m<intsSize;m++){
					Phenomenon temp_p = (Phenomenon) temp.get(m);
					this.modelTo.addElement("int"+temp_p.getBiaohao());
				}
			}
		}

		JiaohuFrom.setBounds(50, 60, 100, 20);
		constraint.setBounds(160, 60, 200, 20);
		JiaohuTo.setBounds(370, 60, 100, 20);
		if(Main.win.instantPane.constraintRelations.get((String)constraint.getItemAt(0)) != 0){
		    for(int i = 0;i < Main.win.instantPane.constraintRelations.get((String)constraint.getItemAt(0));i++){
                numbers.add(new JTextField());
            }
        }
		int size = numbers.size();
		for(int i = 0;i < size;i++){
		    numbers.get(i).setBounds(160 + 35 * i, 100, 30, 20);
        }

		confirm.setBounds(160, 140, 80, 25);
		confirm.addActionListener(this);
		editRelations.setBounds(250, 140,80,25);
		editRelations.addActionListener(this);
        refresh.setBounds(340, 140, 80, 25);
        refresh.addActionListener(this);

		contentPane.add(JiaohuFrom);
		contentPane.add(constraint);
		contentPane.add(JiaohuTo);
		contentPane.add(confirm);
		contentPane.add(editRelations);
		contentPane.add(refresh);
		for(int i = 0;i < numbers.size();i++) contentPane.add(numbers.get(i));

		this.setContentPane(contentPane);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	//get the postfix number of an "int" string
	public static int getStringNum(String str){
		StringBuilder stringBuilder = new StringBuilder("");
		for(int i = 0;i < str.length();i++){
			if(str.charAt(i) <= '9' && str.charAt(i) >= '0') stringBuilder.append(str.substring(i, i+1));
		}
		return Integer.parseInt(stringBuilder.toString());
	}

	private boolean dfsCheckCircuit(boolean[][] graph, Jiaohu jiaohu, LinkedList<Jiaohu> jiaohus) {
		if (visited[jiaohu.getNumber()]) {
			return true;
		}
		visited[jiaohu.getNumber()] = true;
		for(int i = 0;i < jiaohus.size();i++){
			if(graph[jiaohu.getNumber()][jiaohus.get(i).getNumber()]){
				if(dfsCheckCircuit(graph, jiaohus.get(i),jiaohus)){
					return true;
				}
			}
		}
		visited[jiaohu.getNumber()] = false;
		return false;
	}
		@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("OK")) {
			String from=JiaohuFrom.getSelectedItem().toString();
			String to=JiaohuTo.getSelectedItem().toString();
			String cons=constraint.getSelectedItem().toString();
			String num="";
			if(Main.win.instantPane.constraintRelations.get(cons) == 0){
				if(cons.equals("StrictPre") || cons.equals("nStrictPre")){
					visited = new boolean[100];
					InstantPane instantPane = Main.win.instantPane;
					boolean[][] graph = instantPane.getGraph();
					for(int i = 0;i < 100;i++) visited[i] = false;
					graph[Integer.parseInt(from.substring(3))][Integer.parseInt(to.substring(3))] = true;
					//System.out.println(instantPane.getChangjings().size());
					for(int i = 0;i < instantPane.getChangjings().size();i++){
						Changjing changjing = instantPane.getChangjings().get(i);
						int fromNum = changjing.getFrom().getNumber();
						int toNum = changjing.getTo().getNumber();
						System.out.println(fromNum + "," + toNum);
						if(changjing.getState() != 2) graph[fromNum][toNum] = true;
						/*
						else{
							graph[fromNum][toNum] = true;
							graph[toNum][fromNum] = true;
						}
						*/
					}
					for(int i = 0;i < 100;i++){
						for(int j = 0;j < 100;j++){
							if(graph[i][j]) System.out.println("graph[" + i +"][" + j + "]=" + graph[i][j]);
						}
					}
					LinkedList<Jiaohu> jiaohus = instantPane.getJiaohus();
					for(int i = 0;i < jiaohus.size();i++){
						if(dfsCheckCircuit(graph, jiaohus.get(i),jiaohus)){
							JOptionPane.showMessageDialog(null,"Circuit Exists!","Error",JOptionPane.ERROR_MESSAGE);
							graph[Integer.parseInt(from.substring(3))][Integer.parseInt(to.substring(3))] = false;
							return;
						}
					}
				}
                Main.win.instantPane.ClockRelations.add(new Pair(cons, 0));
                Main.win.instantPane.params.add(new Pair(cons, new LinkedList<>()));
                for(int i = 0;i < igs.size();i++){
                    for(int j = 0;j < igs.get(i).getJiaohu().size();j++){
                        if(igs.get(i).getJiaohu().get(j).getNumber() == getStringNum(from)){
                            Main.win.instantPane.froms.add(igs.get(i).getJiaohu().get(j));
                        }
                    }
                }
                for(int i = 0;i < igs.size();i++){
                    for(int j = 0;j < igs.get(i).getJiaohu().size();j++){
                        if(igs.get(i).getJiaohu().get(j).getNumber() == getStringNum(to)){
                            Main.win.instantPane.tos.add(igs.get(i).getJiaohu().get(j));
                        }
                    }
                }
                Main.win.instantPane.repaint();
            }

			else{
				if(cons.equals("BoundedDiff")){
					if(Integer.parseInt(numbers.get(0).getText()) > 0){
						InstantPane instantPane = Main.win.instantPane;
						visited = new boolean[100];
						for(int i = 0;i < 100;i++) visited[i] = false;
						boolean[][] graph = instantPane.getGraph();
						graph[Integer.parseInt(from.substring(3))][Integer.parseInt(to.substring(3))] = true;
						//System.out.println(instantPane.getChangjings().size());
						for(int i = 0;i < instantPane.getChangjings().size();i++){
							Changjing changjing = instantPane.getChangjings().get(i);
							int fromNum = changjing.getFrom().getNumber();
							int toNum = changjing.getTo().getNumber();
							System.out.println(fromNum + "," + toNum);
							if(changjing.getState() != 2) graph[fromNum][toNum] = true;
						/*
						else{
							graph[fromNum][toNum] = true;
							graph[toNum][fromNum] = true;
						}
						*/
						}
						for(int i = 0;i < 100;i++){
							for(int j = 0;j < 100;j++){
								if(graph[i][j]) System.out.println("graph[" + i +"][" + j + "]=" + graph[i][j]);
							}
						}
						LinkedList<Jiaohu> jiaohus = instantPane.getJiaohus();
						for(int i = 0;i < jiaohus.size();i++){
							if(dfsCheckCircuit(graph, jiaohus.get(i),jiaohus)){
								JOptionPane.showMessageDialog(null,"Circuit Exists!","Error",JOptionPane.ERROR_MESSAGE);
								graph[Integer.parseInt(from.substring(3))][Integer.parseInt(to.substring(3))] = false;
								return;
							}
						}
					}
					else if(Integer.parseInt(numbers.get(1).getText()) < 0){
						InstantPane instantPane = Main.win.instantPane;
						visited = new boolean[100];
						for(int i = 0;i < 100;i++) visited[i] = false;
						boolean[][] graph = instantPane.getGraph();
						graph[Integer.parseInt(to.substring(3))][Integer.parseInt(from.substring(3))] = true;
						//System.out.println(instantPane.getChangjings().size());
						for(int i = 0;i < instantPane.getChangjings().size();i++){
							Changjing changjing = instantPane.getChangjings().get(i);
							int fromNum = changjing.getFrom().getNumber();
							int toNum = changjing.getTo().getNumber();
							System.out.println(fromNum + "," + toNum);
							if(changjing.getState() != 2) graph[fromNum][toNum] = true;
						/*
						else{
							graph[fromNum][toNum] = true;
							graph[toNum][fromNum] = true;
						}
						*/
						}
						for(int i = 0;i < 100;i++){
							for(int j = 0;j < 100;j++){
								if(graph[i][j]) System.out.println("graph[" + i +"][" + j + "]=" + graph[i][j]);
							}
						}
						LinkedList<Jiaohu> jiaohus = instantPane.getJiaohus();
						for(int i = 0;i < jiaohus.size();i++){
							if(dfsCheckCircuit(graph, jiaohus.get(i),jiaohus)){
								JOptionPane.showMessageDialog(null,"Circuit Exists!","Error",JOptionPane.ERROR_MESSAGE);
								graph[Integer.parseInt(to.substring(3))][Integer.parseInt(from.substring(3))] = false;
								return;
							}
						}
					}
				}
			    for(int i = 0;i < numbers.size();i++){
			        if(numbers.get(i).getText().trim().equals("")){
                        JOptionPane.showMessageDialog(null, "Please input parameters", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                num = "";
                List<Integer> list = new LinkedList<>();
                for(int i = 0;i < numbers.size() - 1;i++){
                    num  = num + numbers.get(i).getText() + " ";
                    list.add(Integer.parseInt(numbers.get(i).getText()));
                }
                num = num + numbers.get(numbers.size() - 1).getText();
                list.add((Integer.parseInt(numbers.get(numbers.size() - 1).getText())));
                Main.win.instantPane.ClockRelations.add(new Pair(cons, numbers.size()));
                Main.win.instantPane.params.add(new Pair(cons, list));
				for(int i = 0;i < igs.size();i++){
					for(int j = 0;j < igs.get(i).getJiaohu().size();j++){
						if(igs.get(i).getJiaohu().get(j).getNumber() == getStringNum(from)){
							Main.win.instantPane.froms.add(igs.get(i).getJiaohu().get(j));
						}
					}
				}
				for(int i = 0;i < igs.size();i++){
					for(int j = 0;j < igs.get(i).getJiaohu().size();j++){
						if(igs.get(i).getJiaohu().get(j).getNumber() == getStringNum(to)){
							Main.win.instantPane.tos.add(igs.get(i).getJiaohu().get(j));
						}
					}
				}
				Main.win.instantPane.repaint();
			}
			Main.win.instantPane.addConstraint(from,cons,to,num);
			dispose();
		}
		else if(e.getActionCommand().equals("Edit")){
            new ClockRelationDialog();
        }
        else{
            for(Map.Entry<String, Integer> entry : Main.win.instantPane.constraintRelations.entrySet()){
                if(!relationList.contains(entry.getKey())){
                    constraint.addItem(entry.getKey());
                    relationList.add(entry.getKey());
                }
            }
        }
	}
	
	public static void main(String[]arg0){
		new ConstraintDialog(null);
	}

}
