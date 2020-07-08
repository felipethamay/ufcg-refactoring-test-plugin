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

public class TestASTComparator extends TestCase {

	private static final String OK = "OK";

	private static final String SUCCESSFUL_REFACTORING = "SUCCESSFUL_REFACTORING";

	private static final String WARNING_STATUS = "WARNING_STATUS";

	private static final String PRE_REFACTOR_NOT_COMPILE = "PRE_REFACTOR_NOT_COMPILE";

	private static final String ENGINE1 = "jrrt";
	private static final String ENGINE2 = "netbeans";

	private static final String REFACTORING = "renamemethod";

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
			String refactoringName = "move";
			String subDirectoryName = "MoveInnerToTopLevel";
			// BufferedWriter out = new BufferedWriter(new FileWriter(
			// getSystemTempDir() + File.separator + refactoringName + "_"
			// + subDirectoryName + "_Differences.txt"));
			String separator = System.getProperty("file.separator");

			BufferedWriter out = new BufferedWriter(new FileWriter(
					"/private/var/folders/bx/bxpvKGfwF-yg3RjPZ5LRJk+++TI/-Tmp-/pushdownmethod0/"
							+ ENGINE1 + "_" + ENGINE2 + "_Differences.txt"));

			List<String> noequivalents = new ArrayList<String>();
			// String directory = getSystemTempDir() + File.separator
			// + refactoringName + File.separator + subDirectoryName;
			String directory = "/private/var/folders/bx/bxpvKGfwF-yg3RjPZ5LRJk+++TI/-Tmp-/pushdownmethod0/";
			File last = new File(directory);

			if (!last.exists())
				throw new Exception("The directory " + last + " does not exist");
			File[] files = last.listFiles();
			int numTestsComparedEqual = 0;
			int numTestsComparedDifferent = 0;

			List<String> listOfTestDifferences = new ArrayList<String>();
			// int numTestsOnlyECNoCompile = 0, numTestsOnlyNBNoCompile = 0,
			// numTestsBothECNB_ECNoCompile = 0, numTestsBothECNB_NBNoCompile =
			// 0;
			int numBothEc_Jrrt = 0;

			System.out.println("files.length: " + files.length);
			boolean naoChegou = true;
			// iteração em cada teste
			for (int i = 0; i < files.length; i++) {

				directory = files[i].toString();
				if (naoChegou) {
					if (!directory
							.equals("/private/var/folders/bx/bxpvKGfwF-yg3RjPZ5LRJk+++TI/-Tmp-/pushdownmethod0/test1126"))
						continue;
					else
						naoChegou = false;
				}
				File inputDirectory = new File(directory + File.separator
						+ "in");
				File outDir = new File(directory + File.separator + "out");
				File engine1Dir = new File(outDir, ENGINE1);
				// File engine2Dir = new File(outDir,ENGINE2);

				File notCompileEcl = new File(engine1Dir,
						PRE_REFACTOR_NOT_COMPILE);

				// TODO: consertar isso, para o netbeans
				if (inputDirectory.exists() && !notCompileEcl.exists()) {

					// pega os arquivos retornados de cada engine
					File[] outJavaFiles = inputDirectory.listFiles(fileFilter);

					// File[] outNBJavaFiles = engine2Dir.listFiles(fileFilter);

					// File f1 = new File(engine1Dir, SUCCESSFUL_REFACTORING);
					// File f2 = new File(engine2Dir, SUCCESSFUL_REFACTORING);

					// if (f1.exists() && f2.exists()) {

					boolean status = true;
					String message = "";

					int j = 0;
					// System.out.println("outJavaFiles.length: "+
					// outJavaFiles.length);

					while (j < outJavaFiles.length) {

						// interar sobre cada pacote
						// if (outJavaFiles[j].isDirectory()) {

						// File packageEcl = outJavaFiles[j];
						// File packageJRRT = new File(engine2Dir,
						// packageEcl.getName());

						// File[] sourcesEcl = packageEcl
						// .listFiles(fileFilter);
						// File[] sourcesJRRT = packageJRRT
						// .listFiles(fileFilter);
						// compara cada arquivo
						// for (File fileA : sourcesEcl) {
						File fileA = outJavaFiles[j];
						System.out.println("Comparando classe:" + fileA);
						boolean hasEquivalent = false;
						// compara arquivo com cada arquivo do UDITA
						int error = 0;
						InputManager im = new InputManagerASCII(
								"/Users/gustavo/workspaces/jdolly/generator_util/programs.xml");
						im.openFile();
						boolean isCode = true;
						int counter = 0;

						ASTComparator comparator = new ASTComparator();
						comparator.setUpProject();

						while (!im.isEndOfFile() && hasEquivalent == false) {
							String line = im.readLine();
							if (line.contains("input") && isCode) {
								StringBuffer sb = new StringBuffer();
								while (!im.isEndOfFile()) {
									line = im.readLine();
									if (line.contains("input")) {
										break;
									} else {
										sb.append(line + "\n");
									}
								}
								OutputManager out2 = new OutputManagerASCII(
										"programs/ClassId_1.java");
								out2.createFile();
								out2.writeLine(sb.toString());
								out2.closeFile();
								File uditaProgram = new File(
										"programs/ClassId_1.java");
								System.out.println("Comparando..." + counter);
								counter++;

								String compile = Compile.compile("programas/");
								if (!compile.contains("error")) {

									try {
										if (counter % 1000 == 0) {
											comparator.tearDownProject();
											comparator.setUpProject();
										}
										comparator.setInputs(fileA,
												uditaProgram);
									} catch (Exception e) {
										System.out
												.println("files could not be read, # "
														+ i);
										continue;
									}
									if (comparator.compareIsomorphicWithSimetry()) {
										hasEquivalent = true;
										System.out.println("Equivalente");
									}

								}

								// String compile = Compile.compile("test");
								// if (compile.contains("error"))
								// error++;
								// String uditaProgram = sb.toString();
								// System.out.println(uditaProgram);
								// i++;
								// System.out.println("------------" + i);
								isCode = false;
							} else {
								// if (line.contains("input") && !isCode) {
								// while (!im.isEndOfFile()) {
								// line = im.readLine();
								// if (line.contains("input")) {
								isCode = true;
								// break;
								// }
								// }
								// }
							}

						}
						if (hasEquivalent == false) {
							out.write("Test#" + i + " " + files[i] + " "
									+ fileA.getName()
									+ " has no equivalent program \n");
							// out.write(comparator
							// .getCompareMessage() + "\n\n");
							String noEquivalent = files[i].toString();
							noequivalents.add(noEquivalent);

							System.out.println("Test#" + i + " " + files[i]
									+ " " + fileA.getName()
									+ " has no equivalent program");
							break;
						}

						// System.out.println(fileA);
						// File fileA = outJavaFiles[j];
						// File fileB = null;
						// for (File file : sourcesJRRT) {
						// if (file.getName().equals(fileA.getName())) {
						// fileB = file;
						// break;
						// }
						// }
						// if (fileB == null) { // fileA was not
						// // found
						// // in outNB
						// status = false;
						// message = "Class "
						// + fileA.getName()
						// + " was not found in directory outNB";
						// out.write("Test#" + i + " " + files[i]
						// + "\n" + message + "\n");
						// } else {
						// try {
						// comparator.setInputs(fileA, fileB);
						// } catch (Exception e) {
						// System.out
						// .println("files could not be read, # "
						// + i);
						// continue;
						// }
						// if (!comparator.compareIsomorphic()) {
						// status = false;
						// out.write("Test#" + i + " " + files[i]
						// + " " + fileA.getName()
						// + " is different \n");
						// out.write(comparator
						// .getCompareMessage() + "\n\n");
						// System.out.println("Test#" + i + " "
						// + files[i] + " "
						// + fileA.getName()
						// + " is different");
						// }
						// }

						// }

						// }

						j++;
					}

					// at the end affecting the totals
					if (status) {
						numTestsComparedEqual++;
						System.out.println("Test#: " + files[i] + " pass");
					} else {
						listOfTestDifferences.add(directory);
						numTestsComparedDifferent++;
						// numDifferencesEclipse_Netbeans++;
					}
					// }

				}
			}

			System.out.println("Summary of tests that were refactored in"
					+ ENGINE1 + "and " + ENGINE2);
			out.write("Summary of tests that were refactored in" + ENGINE1
					+ "and " + ENGINE2 + "\n");

			System.out.println("Total # for Ec & NB: " + numBothEc_Jrrt);
			out.write("Total # for Ec & NB: " + numBothEc_Jrrt + "\n");

			if (listOfTestDifferences.size() > 0) {
				System.out
						.println("There are "
								+ listOfTestDifferences.size()
								+ " tests that gave different results in Eclipse & Netbeans: ");
				for (int i = 0; i < listOfTestDifferences.size(); i++) {
					System.out.println(listOfTestDifferences.get(i));
				}
			}
			System.out.println("# Tests that have equivalent outputs in "
					+ ENGINE1 + "and " + ENGINE2 + " are: "
					+ numTestsComparedEqual);
			out.write("# Tests that have equivalent outputs in " + ENGINE1
					+ "and " + ENGINE2 + " are: " + numTestsComparedEqual
					+ "\n");
			System.out.println("# Tests that have different outputs in "
					+ ENGINE1 + "and " + ENGINE2 + " are: "
					+ numTestsComparedDifferent);
			out.write("# Tests that have different outputs in " + ENGINE1
					+ "and " + ENGINE2 + " are: " + numTestsComparedDifferent
					+ "\n");
			out.close();
			for (String string : noequivalents) {
				System.out.println("Programas que não tiveram equivalents");
				System.out.println(string);
			}
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

	}

}
