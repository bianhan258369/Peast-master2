package Shape;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

public class Persist {
	public static Diagram load(File file) {
		ObjectInputStream input = null;
		Diagram a = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			input = new ObjectInputStream(fis);
			a = (Diagram) input.readObject();
			input.close();
		} catch (EOFException endofFileException) {
			System.out.println(endofFileException.getMessage());
		} catch (ClassNotFoundException classNotFoundException) {
			System.out.println(classNotFoundException.getMessage());
		} catch (IOException ioException) {
			System.out.println(ioException.getMessage());
		}
		return a;
	}

	public static IntDiagram load1(File file) {
		ObjectInputStream input = null;
		IntDiagram a = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			input = new ObjectInputStream(fis);
			a = (IntDiagram) input.readObject();
			input.close();
		} catch (EOFException endofFileException) {
			System.out.println(endofFileException.getMessage());
		} catch (ClassNotFoundException classNotFoundException) {
			System.out.println(classNotFoundException.getMessage());
		} catch (IOException ioException) {
			System.out.println(ioException.getMessage());
		}
		return a;
	}

	public static boolean save(File file, Diagram dd) {
		if (file == null) {
			return false;
		}
		file.canWrite();
		file.delete();
		FileOutputStream fos = null;
		ObjectOutputStream output = null;
		try {
			fos = new FileOutputStream(file);
			output = new ObjectOutputStream(fos);
			output.writeObject(dd);
			output.flush();
			output.close();
			fos.close();
			return true;
		} catch (IOException e) {
		}
		return false;
	}

	public static boolean save1(File file, IntDiagram dd) {
		if (file == null) {
			return false;
		}
		file.canWrite();
		file.delete();
		FileOutputStream fos = null;
		ObjectOutputStream output = null;
		try {
			fos = new FileOutputStream(file);
			output = new ObjectOutputStream(fos);
			output.writeObject(dd);
			output.flush();
			output.close();
			fos.close();
			return true;
		} catch (IOException e) {
		}
		return false;
	}

	public static boolean save(String title, String path) {
		File file = new File(path);
		FileOutputStream fos = null;
		ObjectOutputStream output = null;
		try {
			fos = new FileOutputStream(file);
			output = new ObjectOutputStream(fos);
			Prj prj = new Prj(title, Data.first, Data.firstq);
			output.writeObject(prj);
			output.flush();
			output.close();
			fos.close();
			return true;
		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
		return false;
	}

	public static String load(String path) {
		ObjectInputStream input = null;
		Prj a = null;
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			input = new ObjectInputStream(fis);
			a = (Prj) input.readObject();
			input.close();
		} catch (EOFException endofFileException) {
			System.out.println(endofFileException.getMessage());
		} catch (ClassNotFoundException classNotFoundException) {
			System.out.println(classNotFoundException.getMessage());
		} catch (IOException ioException) {
			System.out.println(ioException.getMessage());
		} catch (Exception e) {
		}
		Data.first = a.first;
		Data.firstq = a.firstq;
		return a.title;
	}

	public static void main(String[] args) {
		Persist pe = new Persist();
		Diagram dd = load(new File("D:/tmp"));
		System.out.println(dd.getTitle());
	}
}