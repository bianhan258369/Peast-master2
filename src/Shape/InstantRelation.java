package Shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.io.Serializable;

public class InstantRelation implements Serializable {

	private int state;// 0-coincidence;1-precedence;2-strict precedence
	private Jiaohu from;
	private Jiaohu to;
	public int x1;
	public int x2;// 不出意外是起始点坐标
	public int y1;
	public int y2;// 不出意外是终止点坐标
	public boolean selected = false;

	public InstantRelation(Jiaohu from, Jiaohu to, int state) {
		// TODO Auto-generated constructor stub
		this.state = state;
		this.from = from;
		this.to = to;
		refresh();
	}

	public Jiaohu getFrom() {
		return this.from;
	}

	public int getState() {
		return this.state;
	}

	public Jiaohu getTo() {
		return this.to;
	}

	public void refresh() {
		// TODO Auto-generated method stub
		/*
		 * if ((this.to.x1 < this.from.x1 + this.from.x2) && (this.to.x1 >
		 * this.from.x1 - this.to.x2)) { if (this.from.y1 < this.to.y1) {
		 * this.x1 = (this.from.x1 + this.from.x2 / 2); this.y1 = (this.from.y1
		 * + this.from.y2); this.x2 = (this.to.x1 + this.to.x2 / 2); this.y2 =
		 * this.to.y1; } else { this.x1 = (this.from.x1 + this.from.x2 / 2);
		 * this.y1 = this.from.y1; this.x2 = (this.to.x1 + this.to.x2 / 2);
		 * this.y2 = (this.to.y1 + this.to.y2); }
		 * 
		 * return; } if (this.from.x1 <= this.to.x1) { this.x1 = (this.from.x1 +
		 * this.from.x2); this.y1 = (this.from.y1 + this.from.y2 / 2); this.x2 =
		 * this.to.x1;
		 * 
		 * this.y2 = (this.to.y1 + this.to.y2 / 2); } else { this.x1 =
		 * this.from.x1; this.y1 = (this.from.y1 + this.from.y2 / 2); this.x2 =
		 * (this.to.x1 + this.to.x2); this.y2 = (this.to.y1 + this.to.y2 / 2); }
		 */
		Point pFrom = ((TriangleJiaohu) this.from).getCentre();
		Point pTo = ((TriangleJiaohu) this.to).getCentre();
		this.x1 = pFrom.x;
		this.y1 = pFrom.y;
		this.x2 = pTo.x;
		this.y2 = pTo.y;
		/*
		 * this.x1 = this.from.x1+this.from.x2/2; this.y1 = this.from.y1+15;
		 * this.x2 = this.to.x1+this.to.x2/2; this.y2 = this.to.y1+15;
		 */
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (this.state == 0) {
			g.drawLine(this.x1, this.y1, this.x2, this.y2);
		} else {
			Graphics2D g2 = (Graphics2D) g;
			BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F,
					Data.LENGTHOFDASH, 0.0F);

			g2.setStroke(dashed);
			g2.draw(new Line2D.Double(this.x1, this.y1, this.x2, this.y2));
			dashed = new BasicStroke();
			g2.setStroke(dashed);
		}

		if (this.selected) {
			int r = 4;

			Color c = g.getColor();
			g.setColor(Color.white);
			g.fillOval(this.x1 - r, this.y1 - r, 2 * r, 2 * r);
			g.fillOval(this.x2 - r, this.y2 - r, 2 * r, 2 * r);
			g.setColor(c);
		}
		if (this.state != 0)
			drawArrow(g, this.x1, this.y1, this.x2, this.y2);
	}

	private void drawArrow(Graphics g, int x12, int y12, int x22, int y22) {
		// TODO Auto-generated method stub

		int a1 = 0;
		int b1 = 0;
		int a2 = 0;
		int b2 = 0;
		if (x1 == x2) {
			a1 = x1 - (int) Math.sqrt(2.0D) * 5;
			a2 = x1 + (int) Math.sqrt(2.0D) * 5;
			if (y2 > y1) {
				b1 = y2 - (int) Math.sqrt(2.0D) * 5;
				b2 = b1;
			}
			if (y2 <= y1) {
				b1 = y2 + (int) Math.sqrt(2.0D) * 5;
				b2 = b1;
			}
		} else {
			double s1 = Math.tan(0.7853981633974483D + Math.atan((y2 - y1)
					/ (x2 - x1)));

			double s2 = Math.tan(-0.7853981633974483D
					+ Math.atan((y2 - y1) / (x2 - x1)));

			double k = (y2 - y1) / (x2 - x1);
			if ((x2 > x1) && (y2 >= y1)) {
				if (k > 1.0D) {
					a1 = x2
							+ (int) Math.sqrt(100.0D / (1.0D + Math.pow(s1,
									2.0D)));
					b1 = y2
							- (int) Math.sqrt(100.0D * Math.pow(s1, 2.0D)
									/ (1.0D + Math.pow(s1, 2.0D)));

					a2 = x2
							- (int) Math.sqrt(100.0D / (1.0D + Math.pow(s2,
									2.0D)));
					b2 = y2
							- (int) Math.sqrt(100.0D * Math.pow(s2, 2.0D)
									/ (1.0D + Math.pow(s2, 2.0D)));
				}

				if (k <= 1.0D) {
					a1 = x2
							- (int) Math.sqrt(100.0D / (1.0D + Math.pow(s1,
									2.0D)));
					b1 = y2
							- (int) Math.sqrt(100.0D * Math.pow(s1, 2.0D)
									/ (1.0D + Math.pow(s1, 2.0D)));

					a2 = x2
							- (int) Math.sqrt(100.0D / (1.0D + Math.pow(s2,
									2.0D)));
					b2 = y2
							+ (int) Math.sqrt(100.0D * Math.pow(s2, 2.0D)
									/ (1.0D + Math.pow(s2, 2.0D)));
				}

			}

			if ((x2 < x1) && (y2 >= y1)) {
				if (k <= -1.0D) {
					a1 = x2
							+ (int) Math.sqrt(100.0D / (1.0D + Math.pow(s1,
									2.0D)));
					b1 = y2
							- (int) Math.sqrt(100.0D * Math.pow(s1, 2.0D)
									/ (1.0D + Math.pow(s1, 2.0D)));

					a2 = x2
							- (int) Math.sqrt(100.0D / (1.0D + Math.pow(s2,
									2.0D)));
					b2 = y2
							- (int) Math.sqrt(100.0D * Math.pow(s2, 2.0D)
									/ (1.0D + Math.pow(s2, 2.0D)));
				}

				if (k > -1.0D) {
					a1 = x2
							+ (int) Math.sqrt(100.0D / (1.0D + Math.pow(s1,
									2.0D)));
					b1 = y2
							+ (int) Math.sqrt(100.0D * Math.pow(s1, 2.0D)
									/ (1.0D + Math.pow(s1, 2.0D)));

					a2 = x2
							+ (int) Math.sqrt(100.0D / (1.0D + Math.pow(s2,
									2.0D)));
					b2 = y2
							- (int) Math.sqrt(100.0D * Math.pow(s2, 2.0D)
									/ (1.0D + Math.pow(s2, 2.0D)));
				}

			}

			if ((x2 < x1) && (y2 <= y1)) {
				if (k <= 1.0D) {
					a1 = x2
							+ (int) Math.sqrt(100.0D / (1.0D + Math.pow(s1,
									2.0D)));
					b1 = y2
							+ (int) Math.sqrt(100.0D * Math.pow(s1, 2.0D)
									/ (1.0D + Math.pow(s1, 2.0D)));

					a2 = x2
							+ (int) Math.sqrt(100.0D / (1.0D + Math.pow(s2,
									2.0D)));
					b2 = y2
							- (int) Math.sqrt(100.0D * Math.pow(s2, 2.0D)
									/ (1.0D + Math.pow(s2, 2.0D)));
				}

				if (k > 1.0D) {
					a1 = x2
							- (int) Math.sqrt(100.0D / (1.0D + Math.pow(s1,
									2.0D)));
					b1 = y2
							+ (int) Math.sqrt(100.0D * Math.pow(s1, 2.0D)
									/ (1.0D + Math.pow(s1, 2.0D)));

					a2 = x2
							+ (int) Math.sqrt(100.0D / (1.0D + Math.pow(s2,
									2.0D)));
					b2 = y2
							+ (int) Math.sqrt(100.0D * Math.pow(s2, 2.0D)
									/ (1.0D + Math.pow(s2, 2.0D)));
				}

			}

			if ((x2 > x1) && (y2 <= y1)) {
				if (k <= -1.0D) {
					a1 = x2
							- (int) Math.sqrt(100.0D / (1.0D + Math.pow(s1,
									2.0D)));
					b1 = y2
							+ (int) Math.sqrt(100.0D * Math.pow(s1, 2.0D)
									/ (1.0D + Math.pow(s1, 2.0D)));

					a2 = x2
							+ (int) Math.sqrt(100.0D / (1.0D + Math.pow(s2,
									2.0D)));
					b2 = y2
							+ (int) Math.sqrt(100.0D * Math.pow(s2, 2.0D)
									/ (1.0D + Math.pow(s2, 2.0D)));
				}

				if (k > -1.0D) {
					a1 = x2
							- (int) Math.sqrt(100.0D / (1.0D + Math.pow(s1,
									2.0D)));
					b1 = y2
							- (int) Math.sqrt(100.0D * Math.pow(s1, 2.0D)
									/ (1.0D + Math.pow(s1, 2.0D)));

					a2 = x2
							- (int) Math.sqrt(100.0D / (1.0D + Math.pow(s2,
									2.0D)));
					b2 = y2
							+ (int) Math.sqrt(100.0D * Math.pow(s2, 2.0D)
									/ (1.0D + Math.pow(s2, 2.0D)));
				}
			}

		}

		Polygon filledPolygon = new Polygon();
		filledPolygon.addPoint(a1, b1);
		filledPolygon.addPoint(x2, y2);
		filledPolygon.addPoint(a2, b2);

		if (this.state == 1)
			g.drawPolygon(filledPolygon);
		else
			g.fillPolygon(filledPolygon);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
