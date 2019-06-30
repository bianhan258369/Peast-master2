package foundation;

import java.io.IOException;
import java.util.LinkedList;

import Shape.InstantRelation;
import Shape.Jiaohu;
import UI.InstantGraph;

public class Checker {

	private String fileName = "mySMV.smv";
	private LinkedList<InstantGraph> igs;
	LinkedList<InstantRelation> relations;
	LinkedList<InstantRelation> cRelations;

	public Checker(String fileName) {
		this.fileName = fileName;
	}

	public Checker(LinkedList<InstantGraph> igs,
			LinkedList<InstantRelation> relations,
			LinkedList<InstantRelation> cRelations) {
		this.igs = igs;
		this.relations = relations;
		this.cRelations = cRelations;
	}

	public void check() {

		LinkedList<String> clocks = new LinkedList<String>();
		LinkedList<Jiaohu> temp;
		String clock = "";
		for (int i = 0; i < igs.size(); i++) {
			clocks.add(igs.get(i).getName());
			temp = igs.get(i).getJiaohu();
			for (int j = 0; j < temp.size(); j++) {
				clock = temp.get(j).getName() + temp.get(j).getNumber();
				clocks.add(clock);
			}
		}

		LinkedList<Constraint> constraints = new LinkedList<Constraint>();
		Constraint con_union = null;
		Constraint con_alter = null;
		for (int i = 0; i < igs.size(); i++) {
			temp = igs.get(i).getJiaohu();
			for (int j = 0; j < temp.size() - 1; j++) {
				Jiaohu tempJH = temp.get(j);
				Jiaohu nextJH = temp.get(j + 1);

				con_alter = new Constraint(Constraint.ALTER, tempJH.getName()
						+ tempJH.getNumber(), nextJH.getName()
						+ nextJH.getNumber(), null);
				constraints.add(con_alter);

				if (j == 0) {
					con_union = new Constraint(Constraint.UNION,
							tempJH.getName() + tempJH.getNumber(),
							nextJH.getName() + nextJH.getNumber(), "uisg" + i
									+ j);// uisg--union in
											// single graph
					clocks.add("uisg" + i + j);
					constraints.add(con_union);
					continue;
				}

				if (j == temp.size() - 2) {
					con_union = new Constraint(Constraint.UNION, "uisg" + i
							+ (j - 1), nextJH.getName() + nextJH.getNumber(),
							igs.get(i).getName());
					constraints.add(con_union);
					break;
				}

				con_union = new Constraint(Constraint.UNION, "uisg" + i
						+ (j - 1), nextJH.getName() + nextJH.getNumber(),
						"uisg" + i + j);
				clocks.add("uisg" + i + j);
				constraints.add(con_union);
			}
		}

		ModelCreater creater = ModelCreater.getCreater();
		creater.setFileName(this.fileName);
		creater.setClocks(clocks);
		System.out.println("The length of constraints as input is "
				+ constraints.size());
		creater.addConstraints(constraints);
		creater.creat();

		RunNuSMV run = new RunNuSMV();
		try {
			run.exec(new String[] { "NuSMV", creater.getFileName() });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
	}

}

class RunNuSMV extends ExecCommand {

	@Override
	protected void lineHandler(String lineStr) {
		if (lineStr.equals("")) {
			return;
		}
		System.out.println(lineStr);
	}

}
