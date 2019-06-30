package Shape;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintStream;

public class Rect extends Shape {
	private String text;
	protected int state;//2-machine
	protected char cxb = '\000';
	protected int x1;
	protected int y1;
	protected int x2;
	protected int y2;
	private int length;
	private String shortName;

	public Rect(int middlex, int middley) {
		this.shape = 0;
		this.state = 0;
		this.text = "";
		setSize(middlex, middley);
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getText() {
		return this.text;
	}

	private void setSize(int middlex, int middley) {
		this.length = (this.text.length() * 7 + 25);
		this.y2 = 40;
		this.x2 = this.length;

		this.x1 = (middlex - this.x2 / 2);
		this.y1 = (middley - this.y2 / 2);
	}

	public void setCxb(char cxb) {
		this.cxb = cxb;
	}

	public char getCxb() {
		return this.cxb;
	}

	public void setText(String text) {
		this.text = text;
		setSize(this.x1 + this.x2 / 2, this.y1 + this.y2 / 2);
	}

	public void setState(int state) {
		this.state = state;
		if ((state != 0) && (state != 1) && (state != 2)) {
			System.out.println("state!=0,1,2");
		}
		setSize(this.x1 + this.x2 / 2, this.y1 + this.y2 / 2);
	}

	public int getState() {
		return this.state;
	}

	public void draw(Graphics g) {
		if (this.selected) {
			g.setColor(Color.red);
		}
		g.drawRect(this.x1, this.y1, this.x2, this.y2);
		g.drawString(this.text, this.x1 + 5, this.y1 + this.y2 / 2 + 5);
		if ((this.state == 1) || (this.state == 2)) {
			g.drawRect(this.x1 - Data.WIDE, this.y1, Data.WIDE, this.y2);
		}
		if (this.state == 2) {
			g.drawRect(this.x1 - Data.WIDE * 2, this.y1, Data.WIDE, this.y2);
		}
		if (this.cxb != 0) {
			g.drawRect(this.x2 + this.x1 - 12, this.y2 + this.y1 - 12, 12, 12);
			g.drawString("" + this.cxb, this.x2 + this.x1 - 9, this.y2
					+ this.y1 - 1);
		}
		if (this.selected)
			g.setColor(Color.black);
	}

	public boolean isIn(int x, int y) {
		return (x >= this.x1) && (x <= this.x1 + this.x2) && (y >= this.y1)
				&& (y <= this.y1 + this.y2);
	}

	public void moveTo(int x, int y) {
		this.x1 += x;
		this.y1 += y;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Rect){
			Rect temp = (Rect)obj;
			return (this.getText() == temp.getText() && this.getState() == temp.getState());
		}
		return false;
	}
}