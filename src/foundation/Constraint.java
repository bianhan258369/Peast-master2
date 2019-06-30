package foundation;

public class Constraint {

	public static final int ALTER = 0;
	public static final int DELAY = 1;
	public static final int FILTER_BY = 2;
	public static final int INF = 3;
	public static final int STRICT_PRE = 4;
	public static final int SUB_CLOCK = 5;
	public static final int SUP = 6;
	public static final int UNION = 7;

	public static final int totalTypes = 8;

	public static final String[] Type = { "Alter", "Delay", "FilterBy", "Inf",
			"StrictPre", "Subclock", "Sup", "Union" };

	private int cType = -1;
	private String left = "";
	private String right = "";
	private String newClk = "";

	public Constraint() {

	}

	public Constraint(int type, String left, String right, String newClk) {
		this.cType = type;
		this.left = left;
		this.right = right;
		this.newClk = newClk;
	}

	public void setType(int type) {
		this.cType = type;
	}

	public int getType() {
		return this.cType;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getLeft() {
		return this.left;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public String getRight() {
		return this.right;
	}

	public void setNewClk(String newClk) {
		this.newClk = newClk;
	}

	public String getNewClk() {
		return this.newClk;
	}

	public String toString() {
		int _t = this.getType();
		String result = Constraint.Type[_t] + "(";

		switch (_t) {
		case Constraint.ALTER:
		case Constraint.SUB_CLOCK:
			result = result + this.getLeft() + "," + this.getRight();
			break;
		case Constraint.DELAY:
			break;
		case Constraint.FILTER_BY:
			break;
		case Constraint.INF:
			result = result + this.getLeft() + "," + this.getRight() + ","
					+ this.getNewClk() + "," + 1000;
			break;
		case Constraint.SUP:
			result = result + this.getLeft() + "," + this.getRight() + ","
					+ this.getNewClk() + "," + 1000;
			break;
		case Constraint.STRICT_PRE:
			result = result + this.getLeft() + "," + this.getRight() + ","
					+ 1000;
			break;
		case Constraint.UNION:
			result = result + this.getLeft() + "," + this.getRight() + ","
					+ this.getNewClk();
			break;
		}
		return result + ")";
	}

	public static void main(String[] args) {
		Constraint con = new Constraint();
		con.setLeft("Left");
		con.setRight("Right");
		con.setNewClk("New");
		for (int i = 0; i < Constraint.totalTypes; i++) {
			con.setType(i);
			System.out.println(con.toString());
		}
	}
}
