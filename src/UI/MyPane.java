package UI;

import Shape.*;
import Shape.Shape;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;

public class MyPane extends FatherPane implements MouseMotionListener,
		MouseListener, KeyListener, ActionListener {
	int state = 0;
	Diagram dd;
	private boolean sub = false;
	Shape nowSelected = null;
	Shape from = null;
	boolean dragged = false;
	int nowx = 0;
	int nowy = 0;
	JPopupMenu set = new JPopupMenu();
	JMenuItem match = new JMenuItem("match");
	boolean matched = false;

	public MyPane(Diagram d) {
		this.state = 0;
		this.dd = d;
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(Color.white);
		addKeyListener(this);
		this.set.add(this.match);
		this.match.addActionListener(this);
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (this.dd != null)
			this.dd.draw(g);
	}

	public void setSub(boolean sub) {
		this.sub = sub;
	}

	public boolean getSub() {
		return this.sub;
	}

	public void actionPerformed(ActionEvent e) {
		int tmp_i = event_match();
		int k;
		if (tmp_i != 0) {
			String name = "";
			if (tmp_i == 1) {
				name = "Required Behavour Frame";
				this.matched = true;
			}
			if (tmp_i == 2) {
				name = "Commanded Behavour Frame";
				this.matched = true;
			}
			if (tmp_i == 3) {
				name = "Information Display Frame";
				this.matched = true;
			}
			if (tmp_i == 4) {
				name = "Simple workpieces Frame";
				this.matched = true;
			}
			if (tmp_i == 5) {
				name = "Transformation Frame";
				this.matched = true;
			}
			Object[] bs = { "OK" };

			k = JOptionPane.showOptionDialog(Main.win,
					"Succed to be fitted to " + name + "!", "Tip", 0, 1, null,
					bs, null);

			Main.win.myInfoPane.setDescription(this.dd
					.getInteractionDescription());
		} else {
			Object[] bs = { "OK" };

			k = JOptionPane.showOptionDialog(Main.win, "Fail to match!", "Tip",
					0, 0, null, bs, null);
		}
	}

	public void setState(int state) {
		if ((state >= 1) && (state <= 7)) {
			setCursor(Cursor.getPredefinedCursor(1));
		}
		if (state == 0) {
			setCursor(Cursor.getDefaultCursor());
		}
		this.state = state;
	}

	public static void main(String[] args) {
		JFrame a = new JFrame();
		a.setSize(400, 400);
		a.getContentPane().add(new MyPane(null));
		a.setVisible(true);
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if ((this.nowSelected != null) && (e.getKeyCode() == 127)) {
			this.dd.delete(this.nowSelected);
			this.nowSelected = null;
			repaint();
			FatherPane tmp_mp = (FatherPane) Main.win.myDisplayPane.desk
					.getSelectedComponent();

			if (tmp_mp == null) {
				return;
			}
			Main.win.myInfoPane.setDescription(((MyPane) tmp_mp).dd
					.getInteractionDescription());
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		if (!this.dragged) {
			return;
		}
		int x = e.getX();
		int y = e.getY();
		this.nowSelected.moveTo(x - this.nowx, y - this.nowy);
		this.dd.follow(this.nowSelected);
		this.nowx = x;
		this.nowy = y;
		Main.win.repaint();
	}

	public void mouseMoved(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
		if ((e.getClickCount() == 2) && (this.nowSelected != null)) {
			/***********************************************************************/
			if (this.nowSelected.shape == 0
					&& !dd.getTitle().contains("ClockDiagram")) {//��ʱ��ͼ���Ĺ�
				new RectEditor((Rect) this.nowSelected).show();
			}
			if (this.nowSelected.shape == 2
					&& !dd.getTitle().contains("ClockDiagram")) {//��ʱ��ͼ���Ĺ�
				new LineEditor((Line) this.nowSelected, this.dd.getMachine(),
						this.dd).show();
			}

			if (this.nowSelected.shape == 0 && dd.getTitle().contains("ClockDiagram")) {//��ʱ��ͼ���Ĺ�
				Rect temp=(Rect)this.nowSelected;
				Clock clock = null;
				if(temp.getState()!=2){
					new ClockDialog((Rect) this.nowSelected );
                    for(int i = 0;i < Main.win.cd.getClocks().size();i++){
                    	if(Main.win.cd.getClocks().get(i).getDomainText().equals(temp.getText())){
                    		clock = Main.win.cd.getClocks().get(i);
                    		break;
						}
					}
                    try{
                        new ClockSpecification((Rect) this.nowSelected,clock);
                    }
                    catch (Exception e1){
                        e1.printStackTrace();
                    }
                };
			}

			/***********************************************************************/
			if (this.nowSelected.shape == 1) {
				String s = JOptionPane.showInputDialog(this,
						"Please input the description of the requirement!",
						"Input", 2);

				if ((s == null) || (s.equals(""))) {
					return;
				}
				Oval tmp_o = (Oval) this.nowSelected;
				tmp_o.setText(s);
			}
			this.dd.follow(this.nowSelected);
			Main.win.repaint();
			Main.win.myInfoPane.setDescription(this.dd.getInteractionDescription());
		}
	}

	public void mousePressed(MouseEvent e) {
		requestFocus();
		if (this.state == 1) {
			Rect tmpr = new Rect(e.getX(), e.getY());
			tmpr.setState(0);
			tmpr.setText(Data.GD_TEXT);
			this.dd.add(tmpr);
			setState(0);
			Main.win.smooth();
			Main.win.repaint();
			return;
		}
		if (this.state == 2) {
			Rect tmpr = new Rect(e.getX(), e.getY());
			tmpr.setState(1);
			tmpr.setText(Data.DD_TEXT);
			this.dd.add(tmpr);
			setState(0);
			Main.win.smooth();
			Main.win.repaint();
			return;
		}
		if (this.state == 3) {
			Rect tmpr = new Rect(e.getX(), e.getY());
			tmpr.setState(2);
			tmpr.setText(Data.M_TEXT);
			this.dd.add(tmpr);
			setState(0);
			Main.win.smooth();
			Main.win.repaint();
			return;
		}

		if (this.state == 4) {
			Oval tmp_o = new Oval(e.getX(), e.getY());
			this.dd.add(tmp_o);
			setState(0);
			Main.win.smooth();
			Main.win.repaint();
			Main.win.myInfoPane.setDescription(this.dd
					.getInteractionDescription());
			if (this.sub) {
				Main.win.myInfoPane.setTreeNodeDescription(this.dd.getTitle(),
						tmp_o.getBiaohao());
			}
			return;
		}
		Point p = e.getPoint();
		Shape tmp = this.dd.whichSelected(p.x, p.y);

		if (tmp == null) {
			if (this.nowSelected != null) {
				this.nowSelected.selected = false;
				this.nowSelected = null;
			}
			this.from = null;
			setState(0);
			Main.win.smooth();
			Main.win.repaint();
		} else {
			if (this.nowSelected != null) {
				this.nowSelected.selected = false;
				this.nowSelected = null;
			}
			this.nowSelected = tmp;
			tmp.selected = true;
			if ((this.state >= 5) && (this.state <= 7)) {
				if (this.from == null) {
					this.from = tmp;
				} else {
					Line tline = null;
					tline = new Line(this.from, this.nowSelected,
							this.state - 5);
					this.dd.add(tline);
					this.dd.setLineName();
					setState(0);
					Main.win.repaint();
					this.from = null;
					Main.win.smooth();
				}
				Main.win.myInfoPane.setDescription(this.dd
						.getInteractionDescription());
				return;
			}
			this.dragged = true;
			this.nowx = e.getX();
			this.nowy = e.getY();
			Main.win.repaint();
		}
		if ((e.getButton() == 3) && (this.sub)) {
			this.set.show(this, e.getX(), e.getY());
			return;
		}
	}

	public void mouseReleased(MouseEvent e) {
		this.dragged = false;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public int event_match() {
		if ((this.dd.match1()) || (this.dd.match2()) || (this.dd.match3(true))
				|| (this.dd.match3(false)) || (this.dd.match4())
				|| (this.dd.match5(true)) || (this.dd.match5(false))) {
			Main.win.repaint();
			if (this.dd.match1()) {
				return 1;
			}
			if (this.dd.match2()) {
				return 2;
			}
			if ((this.dd.match3(true)) || (this.dd.match3(false))) {
				return 3;
			}
			if (this.dd.match4()) {
				return 4;
			}
			if ((this.dd.match5(true)) || (this.dd.match5(false))) {
				return 5;
			}
		}
		return 0;
	}

	public void newMypane() {
		if (!this.sub)
			return;
		Oval req = null;
		for (int i = 0; i <= this.dd.components.size() - 1; i++) {
			Shape tmp_s = (Shape) this.dd.components.get(i);
			if (tmp_s.shape == 1) {
				req = (Oval) tmp_s;
			}
		}
		if (req == null)
			return;
		req.setBiaohao(Data.firstq);
		Data.firstq += 1;
		Main.win.repaint();
		Main.win.myInfoPane.setTreeNodeDescription(this.dd.getTitle(),
				req.getBiaohao());
	}
}
