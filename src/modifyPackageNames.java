import java.io.File;
import java.io.IOException;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class modifyPackageNames {

	public static void modifyPackage(String file) throws IOException {
		InputManager im = new InputManagerASCII(file);
		String contents = "";
		String line;
		im.openFile();
		while (!im.isEndOfFile()) {
			line = im.readLine() + "\n";
			if (line.contains("package")) {
				line = line.replace("_out", "_in");
			}
			contents += line;
		}
		im.closeFile();

		OutputManager om = new OutputManagerASCII(file);
		om.createFile();
		om.writeLine(contents);
		om.closeFile();
	}

	public static void main(String[] args) throws IOException {

		// String dir =
		// "/Users/melmongiovi/Documents/doutorado/experiments/OWC/eclipse/extractmethod2/";
		String dir = "C:/Users/Felipe/Documents/doutorado/experiments/MT/eclipse/extractmethod/";
		dir = "C:/Users/Felipe/Documents/doutorado/experiments/MT/eclipse/encapsulate/";
		File f = new File(dir);
		String[] list = f.list();
		for (String file : list) {
			if (!file.startsWith("."))
				modifyPackage(dir + file + "/out/" + file + ".java");
		}
	}
}
