package foundation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class ModelCreater {

	private FileWriter fw = null;
	private String fileName = "mySMV.smv";
	private LinkedList<String> clocks = new LinkedList<String>();
	private LinkedList<Constraint> constraints = new LinkedList<Constraint>();
	private int[] types = new int[Constraint.totalTypes];

	private ModelCreater() {
		initTypes();
	}

	public void setClocks(LinkedList<String> clocks) {
		this.clocks = clocks;
	}

	private void initTypes() {
		for (int i = 0; i < Constraint.totalTypes; i++) {
			types[i] = 0;
		}
	}

	public void addConstraint(Constraint con) {
		this.constraints.add(con);
	}

	public void addConstraints(LinkedList<Constraint> cons) {
		int length = cons.size();
		for (int i = 0; i < length; i++) {
			this.constraints.add(cons.get(i));
		}
	}

	public void beginCreate() {
		try {
			fw = new FileWriter(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeToModel(String line) {
		try {
			fw.write(line);
			// fw.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void endCreate() {
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void creat() {
		this.beginCreate();
		this.writeToModel("MODULE main VAR\r\n");
		int clk_length = this.clocks.size();
		for (int i = 0; i < clk_length; i++) {
			this.writeToModel(this.clocks.get(i) + ": boolean;\r\n");
		}

		int ctr_length = this.constraints.size();
		for (int j = 0; j < ctr_length; j++) {
			System.out.println("The length of constraints is " + ctr_length);
			Constraint temp = this.constraints.get(j);
			if (this.types[temp.getType()] == 0) {
				this.types[temp.getType()] = 1;
			}
			this.writeToModel("ctr" + j + ": "
					+ this.constraints.get(j).toString() + ";\r\n");
		}

		this.writeToModel("ASSIGN\r\n");
		for (int i = 0; i < clk_length; i++) {
			this.writeToModel("init(" + this.clocks.get(i) + "):=FALSE;\r\n");
		}

		this.writeToModel("\r\n");

		for (int c = 0; c < Constraint.totalTypes; c++) {
			if (this.types[c] == 1) {
				String mName = Constraint.Type[c].toLowerCase();
				File file = new File("modules\\" + mName + ".smv");
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(file));
					String tempString = null;
					while ((tempString = reader.readLine()) != null) {
						this.writeToModel(tempString);
						this.writeToModel("\r\n");
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e1) {
						}
					}
				}
			}
		}
		this.endCreate();
	}

	public void appendToModel(String line) {
		BufferedWriter append = null;
		try {
			append = new BufferedWriter(new FileWriter(fileName, true));
			append.write(line);
		} catch (IOException e) {
			// error processing code
		} finally {
			if (append != null) {
				try {
					append.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void setFileName(String fName) {
		this.fileName = fName;
	}

	public String getFileName() {
		return this.fileName;
	}

	private static final ModelCreater modelCreater = new ModelCreater();

	public static ModelCreater getCreater() {
		return modelCreater;
	}

}
