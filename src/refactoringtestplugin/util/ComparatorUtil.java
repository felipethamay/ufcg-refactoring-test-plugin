package refactoringtestplugin.util;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.JavaModelException;

public class ComparatorUtil {

	public static List<String> getUitaPrograms(int scope) throws IOException {

		// pega programas do udita
		InputManager im = new InputManagerASCII("escopo" + scope + ".rtf");
		im.openFile();
		boolean isCode = true;

		List<String> uditaPrograms = new ArrayList<String>();
		while (!im.isEndOfFile()) {
			String line = im.readLine();
			if (line.contains("==============================================")
					&& isCode) {
				StringBuffer sb = new StringBuffer();
				while (!im.isEndOfFile()) {
					line = im.readLine();
					if (line.contains("Class interface hierarchy"))
						continue;
					if (line.contains("==============================================")) {
						break;
					} else {
						sb.append(line + " {}" + "\n");
					}
				}
				// System.out.println(sb.toString());
				uditaPrograms.add(sb.toString());

				isCode = false;
			} else {

				isCode = true;

			}

		}
		return uditaPrograms;
	}

	public static List<String> getUitaPrograms() throws IOException {

		// pega programas do udita
		InputManager im = new InputManagerASCII("programs.xml");
		im.openFile();
		boolean isCode = true;

		List<String> uditaPrograms = new ArrayList<String>();
		while (!im.isEndOfFile()) {
			String line = im.readLine();
			if (line.contains("input")
					&& isCode) {
				StringBuffer sb = new StringBuffer();
				while (!im.isEndOfFile()) {
					line = im.readLine();					
					if (line.contains("input")) {
						break;
					} else {
						sb.append(line + "\n");
					}
				}
				// System.out.println(sb.toString());
				uditaPrograms.add(sb.toString());

				isCode = false;
			} else {

				isCode = true;

			}

		}
		return uditaPrograms;
	}

	public static boolean isEquivalent(String program, String program2,
			ASTComparator comparator) throws IOException, JavaModelException {
		OutputManager out3 = new OutputManagerASCII("programs2/Node_0.java");
		out3.createFile();
		out3.writeLine(program);
		out3.closeFile();
		File jdollyProgram = new File("programs2/Node_0.java");

		OutputManager out2 = new OutputManagerASCII("programs/Node_0.java");
		out2.createFile();
		out2.writeLine(program2);
		out2.closeFile();
		File uditaProgram = new File("programs/Node_0.java");

		try {
			comparator.setInputs(jdollyProgram, uditaProgram);
		} catch (Exception e) {
			System.out.println("files could not be read, # ");
		}
		if (comparator.compareIsomorphicWithSimetry())
			return true;

		return false;
	}

}
