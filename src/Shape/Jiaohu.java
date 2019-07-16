package Shape;

import UI.Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Jiaohu implements Serializable, Comparable<Jiaohu>,Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7665441956069222621L;
	private int number;
	protected int x1;
	protected int y1;
	protected int x2;
	protected int y2;
	private int length;
	public boolean selected;
	private int state;

	private String name = "int";

	public Jiaohu(int middlex, int middley, int number, int state) {
		this.number = number;
		setSize(middlex, middley);
		this.state = state;
	}

	public void setName(String m_name) {
		this.name = m_name;
	}
	
	public String getName(){
		return this.name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public int getNumber() {
		return this.number;
	}

	public void draw(Graphics g) {
		if (this.selected && this.state != 2)
			g.setColor(Color.red);
		if (this.state == 0 || this.state ==2)
			g.drawOval(this.x1, this.y1, this.x2, this.y2);
		if (this.state == 1) {
			Graphics2D g2 = (Graphics2D) g;
			BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F,
					Data.LENGTHOFDASH, 0.0F);

			g2.setStroke(dashed);
			g2.drawOval(this.x1, this.y1, this.x2, this.y2);
			dashed = new BasicStroke();
			g2.setStroke(dashed);
		}
		g.setColor(Color.black);
		if (this.state == 0) {
			g.setColor(new Color(196, 235, 236));
			Graphics2D g2 = (Graphics2D) g;
			g2.fillOval(this.x1 + 1, this.y1 + 1, this.x2 - 1, this.y2 - 1);
		}
		if (this.state == 1) {
			g.setColor(new Color(245, 214, 171));
			Graphics2D g2 = (Graphics2D) g;
			g2.fillOval(this.x1 + 1, this.y1 + 1, this.x2 - 1, this.y2 - 1);
		}
		g.setColor(Color.black);
		drawXiaBiao(g, this.x1 + 11, this.y1 + this.y2 / 2 + 8, Math.abs(this.number));
	}

	public void draw(Graphics g, Color color) {


		g.drawOval(this.x1, this.y1, this.x2, this.y2);
		g.setColor(Color.black);
		g.setColor(color);
		Graphics2D g2 = (Graphics2D) g;
		g2.fillOval(this.x1 + 1, this.y1 + 1, this.x2 - 1, this.y2 - 1);
		g.setColor(Color.black);
		drawXiaBiao(g, this.x1 + 11, this.y1 + this.y2 / 2 + 8, Math.abs(this.number));
	}

	public void drawXiaBiao(Graphics g, int x, int y, int num) {
		g.drawString(this.getName(), x, y);
		Font font1 = new Font("SansSerif", 0, 10);
		Font tmp = g.getFont();
		g.setFont(font1);
		g.drawString("" + num, x + 23, y + 1);
		g.setFont(tmp);
	}

	public int getMiddleX() {
		return this.x1 + this.x2 / 2;
	}

	public int getMiddleY() {
		return this.y1 + this.y2 / 2;
	}

	public boolean isIn(int x, int y) {
		int tmp1 = (int) Math.pow(x - (this.x1 + this.x2 / 2), 2.0D)
				/ (int) Math.pow(this.x2 / 2.0D, 2.0D);

		int tmp2 = (int) Math.pow(y - (this.y1 + this.y2 / 2), 2.0D)
				/ (int) Math.pow(this.y2 / 2.0D, 2.0D);

		return tmp1 + tmp2 <= 1;
	}

	public void setSize(int middlex, int middley) {
		this.length = (("int" + this.number).length() * 7 + 25);
		this.x2 = 60;
		this.y2 = 30;
		this.x1 = (middlex - this.x2 / 2);
		this.y1 = (middley - this.y2 / 2);
	}

	public void moveTo(int x, int y) {
		this.x1 += x;
		this.y1 += y;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Jiaohu){
			Jiaohu temp = (Jiaohu)obj;
			return (this.state == temp.state && this.number == temp.number);
			//return this.number == temp.number;
		}
		return false;
	}

	@Override
	public int compareTo(Jiaohu o) {
		// TODO Auto-generated method stub
		return this.x1 - o.x1;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return (Jiaohu)super.clone();
	}

}