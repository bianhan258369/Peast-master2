package Shape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.ImageObserver;
import java.util.Comparator;

import javax.swing.ImageIcon;

import UI.Main;

public class TriangleJiaohu extends Jiaohu{
	ImageObserver observer = null;

	public TriangleJiaohu(int middlex, int middley, int number, int state) {
		super(middlex, middley, number, state);
	}

	public void setObserver(ImageObserver observer) {
		this.observer = observer;
	}

	public Point getCentre() {
		return new Point(this.x1 + 10, this.y1 + 5);
	}

	public int getRadius() {
		return 15;
	}

	public void draw(Graphics g) {
		// Image img = new
		// ImageIcon(Main.class.getResource("/images/sanjiao.png"))
		// .getImage();
		// g.setColor(Color.red);
		// System.out.println("x1:"+this.x1+";x2:"+this.x2+";y1:"+this.y1+";y2:"+this.y2);
		// g.drawOval(this.x1, this.y1, this.x2, this.y2);
		// g.drawImage(img, 0, 0, observer);
		// g.drawImage(img, this.x1+2, this.y1 - 4, this.x1 + 40, this.y1 + 26,
		// 0,
		// 10, 40, 40, observer);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(Color.WHITE);
		g2d.fillOval(this.x1 - 5, this.y1 - 10, 40, 30);
		g2d.setColor(Color.BLACK);
		g2d.drawOval(this.x1 - 5, this.y1 - 10, 40, 30);
		/*
		 * drawXiaBiao(g, this.x1 + 5, this.y1 + this.y2 / 2 -
		 * img.getHeight(observer) + 12, this.getNumber());
		 */

		drawXiaBiao(g, this.x1, this.y1 + this.y2 / 4, this.getNumber());
	}

	public boolean isIn(int x, int y) {
		Point centre = this.getCentre();
		int radius = this.getRadius();
		double _x = Math.abs(x - centre.x);
		double _y = Math.abs(y - centre.y);
		double distance = Math.sqrt(_x * _x + _y * _y);
		return distance <= radius;
	}
}
