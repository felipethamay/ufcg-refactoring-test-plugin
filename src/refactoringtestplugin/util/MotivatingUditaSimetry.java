package refactoringtestplugin.util;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jdolly.util.Compile;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;

import sun.reflect.generics.factory.CoreReflectionFactory;

import junit.framework.TestCase;

public class MotivatingUditaSimetry extends TestCase {

	FileFilter fileFilter = new FileFilter() {
		public boolean accept(File file) {
			return (file.getName().indexOf(".java") == (file.getName().length() - 5));
		}
	};

	/**
	 * Utility function that returns the system's temporary directory.
	 */
	public static String getSystemTempDir() {
		String tempdir = System.getProperty("java.io.tmpdir");
		if (tempdir == null) {
			throw new IllegalArgumentException("Temp dir is not specified");
		}
		String separator = System.getProperty("file.separator");
		if (!tempdir.endsWith(separator)) {
			return tempdir + separator;
		}
		return tempdir;
	}

	public void testCompareFromFiles_ForGeneratedInputFiles() throws Exception {

		try {

			List<String> noequivalents = new ArrayList<String>();

			// pega programas do udita
			InputManager im = new InputManagerASCII(
					"/Users/gustavo/workspaces/jdolly/RefactoringTestPlugin/ecopo3.rtf");
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

			int i = 0;
			int ce = 0;
			ASTComparator comparator = new ASTComparator();
			comparator.setUpProject();
			List<List<Integer>> todosSimetricos = new ArrayList<List<Integer>>();
			for (String program : uditaPrograms) {
				boolean para = false;

				System.out.println("Comparando programa: " + i);

				OutputManager out3 = new OutputManagerASCII(
						"programs2/Node_0.java");
				out3.createFile();
				out3.writeLine(program);
				out3.closeFile();
				File jdollyProgram = new File("programs2/Node_0.java");
				String compile2 = refactoringtestplugin.util.Compile
						.compile("/Users/gustavo/workspaces/jdolly/RefactoringTestPlugin/programs2/Node_0.java");
				if (!compile2.contains("error")) {
					System.out.println(program);
					// iteração em cada teste
					boolean hasEquivalent = false;

					int counter = 1;
					List<Integer> simetricos = new ArrayList<Integer>();
					for (String sb : uditaPrograms) {

						OutputManager out2 = new OutputManagerASCII(
								"programs/Node_0.java");
						out2.createFile();
						out2.writeLine(sb);
						out2.closeFile();
						File uditaProgram = new File("programs/Node_0.java");

						String compile = refactoringtestplugin.util.Compile
								.compile("/Users/gustavo/workspaces/jdolly/RefactoringTestPlugin/programs/Node_0.java");

						if (!compile.contains("error")) {

							System.out.println("Comparando..." + counter);
							// System.out.println(program);

							try {
								if (counter % 10 == 0) {
									comparator.tearDownProject();
									comparator.setUpProject();
								}
								comparator.setInputs(jdollyProgram,
										uditaProgram);
							} catch (Exception e) {
								System.out
										.println("files could not be read, # "
												+ i);
								continue;
							}
							if (comparator.compareIsomorphicWithSimetry()) {
								if (hasEquivalent == true) {
									System.out.println("Programa simetrico: "
											+ i);
									para = true;

									// break;
								}

								hasEquivalent = true;
								simetricos.add(new Integer(counter));
								System.out.println("Equivalente: " + i);
								// break;
							}

						} else {
							System.out.println("Programa do Udita não compila");
						}
						counter++;
					}

					if (hasEquivalent == false) {
						// out.write(comparator
						// .getCompareMessage() + "\n\n");
						String noEquivalent = program;
						noequivalents.add(noEquivalent);

						System.out.println("Test#" + i
								+ " has no equivalent program");
						System.out.println(noEquivalent);

					}

					if (para == true) {
						todosSimetricos.add(simetricos);
					}

				} else {
					ce++;
					System.out.println("compilation error");
				}

				i++;
			}
			System.out.println("Total de erros de compilação: " + ce);
			System.out.println("Total de programas não equivalentes: "
					+ noequivalents.size());

			System.out.println("Casos Simetricos: " + todosSimetricos.size());
			for (List<Integer> list : todosSimetricos) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
				for (Integer integer : list) {
					System.out.println("Programa: " + integer);
				}
			}
			for (String program : noequivalents) {
				System.out.println("=========================");
				System.out.println(program);
			}

		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

	}

}
