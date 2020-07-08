package refactoringtestplugin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.Main;

public class ComparePreAndPostRefactoredFiles {
	static String refactoringName;

	static String testClassName;

	static String className = "A";

	static String className2;

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

	public static List runFiles(String directoryInput, String directoryOutput)
			throws Exception {
		List results = new ArrayList();
		Process pro = Runtime.getRuntime().exec(
				"java -cp " + directoryInput + " A");
		// "java -cp " + directory + "in" + File.separator + " A");

		/* handling the streams so that dead lock situation never occurs. */
		ProcessHandler inputStream = new ProcessHandler(pro.getInputStream(),
				"INPUT", 0);
		ProcessHandler errorStream = new ProcessHandler(pro.getErrorStream(),
				"ERROR", 0);

		/* start the stream threads */
		inputStream.start();
		errorStream.start();

		while (inputStream.isAlive() && errorStream.isAlive())
			pro.waitFor();

		Process pro2 = Runtime.getRuntime().exec(
		// "java -cp " + directory + "out" + File.separator + " A");
				"java -cp " + directoryOutput + " A");
		ProcessHandler inputStream2 = new ProcessHandler(pro2.getInputStream(),
				"INPUT", 1);
		ProcessHandler errorStream2 = new ProcessHandler(pro2.getErrorStream(),
				"ERROR", 1);

		/* start the stream threads */
		inputStream2.start();
		errorStream2.start();

		while (inputStream2.isAlive() && errorStream2.isAlive())
			pro2.waitFor();

		// results.add(errorStream2.error);
		// results.add(inputStream2.output);
		String[] listError = new String[2];
		listError[0] = errorStream.error;
		listError[1] = errorStream2.error;
		results.add(listError);
		String[] listOutput = new String[2];
		listOutput[0] = inputStream.output;
		listOutput[1] = inputStream2.output;
		results.add(listOutput);
		return results;
	}

	private static void runJavaClass(String refactoringName,
			String testClassName) throws IOException {
		// get the list of sub-directories
		File rootDir = new File(getSystemTempDir());

		File parentDir = new File(rootDir.getAbsolutePath() + File.separator
				+ refactoringName + File.separator + testClassName);
		if (!parentDir.exists()) {
			System.out.println("This directory does not exist: "
					+ parentDir.getAbsolutePath());
			System.exit(1);
		}

		File[] files = parentDir.listFiles();
		List resultsEclipse, resultsNetbeans;
		String directoryInput, directoryOutput;
		String[] errorEclipse, errorNetbeans;
		String[] outputEclipse, outputNetbeans;
		int counterOutputDifferences = 0;
		int counterOutputEquivalences = 0;

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					getSystemTempDir() + File.separator + refactoringName + "_"
							+ testClassName + "_ListChangeOfBehavior.txt"));

			int counterDifBehEclipse, counterDifBehNetbeans, counterEquBehEclipse, counterEquBehNetbeans;
			counterDifBehEclipse = counterDifBehNetbeans = counterEquBehEclipse = counterEquBehNetbeans = 0;

			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					File outEC = new File(file.getAbsolutePath()
							+ File.separator + "out" + File.separator
							+ className + ".java");
					File input = new File(file.getAbsolutePath()
							+ File.separator + "in" + File.separator
							+ className + ".java");
					if (outEC.exists() && input.exists())
						Main.compile(new String[] { input.getAbsolutePath() });

				}
			}

			int numInputWithRuntimeErrorEclipse = 0;
			int numInputWithRuntimeErrorNetbeans = 0;
			out.write("List of Changes of Behavior  >>>>\n");
			int resultCompilationEC, resultCompilationNB;

			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					// File outEC_Comp = new File(file.getAbsolutePath() +
					// File.separator + "out" + File.separator + className +
					// ".class");
					File outEC = new File(file.getAbsolutePath()
							+ File.separator + "out" + File.separator
							+ className + ".java");
					directoryInput = "java -cp " + file.getAbsolutePath()
							+ File.separator + "in" + File.separator;
					resultCompilationEC = resultCompilationNB = -1;
					errorNetbeans = errorEclipse = null;// new String[2];
					outputEclipse = outputNetbeans = null;// new String[2];
					if (outEC.exists()) {
						resultCompilationEC = Main.compile(new String[] { outEC
								.getAbsolutePath() });
						if (resultCompilationEC == 0) {
							directoryOutput = "java -cp "
									+ file.getAbsolutePath() + File.separator
									+ "out" + File.separator;
							resultsEclipse = runFiles(directoryInput,
									directoryOutput);
							errorEclipse = (String[]) resultsEclipse.get(0);
							outputEclipse = (String[]) resultsEclipse.get(1);
							if (errorEclipse[0] == "_") {
								if (errorEclipse[1] != "_") {
									out.write("Change of behavior, Runtime Error in Eclipse, test: " + file.getAbsolutePath() + "\n");
									out.write("\nEclipse error: " + errorEclipse[1] + "\n");
									counterDifBehEclipse++;
								}
								if (errorEclipse[1] == "_") {
									if (!outputEclipse[0].equals(outputEclipse[1])) {
										//out.write("\nTest: "	+ file.getAbsolutePath());
										out.write("Change of behavior in Eclipse, test: " + file.getAbsolutePath() + "\n");
										out.write("\nEclipse outputBefore: " + outputEclipse[0] + " outputAfter: " + outputEclipse[1] + "\n");										
										counterDifBehEclipse++;
									} else {
										counterEquBehEclipse++;
									}
								}
							} else {
								numInputWithRuntimeErrorEclipse++;
							}
						} else {
							System.out.println("Compilation error in Eclipse, test: " + file.getAbsolutePath());
						}
					}

					File outNB = new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + className + ".java");
					directoryInput = "java -cp " + file.getAbsolutePath() + File.separator + "in" + File.separator;
					if (outNB.exists()) {
						removeFirstLine(outNB);
						resultCompilationNB = Main.compile(new String[] { outNB.getAbsolutePath() });
						if (resultCompilationNB == 0) {
							directoryOutput = "java -cp " + file.getAbsolutePath() + File.separator + "outNB" + File.separator;
							resultsNetbeans = runFiles(directoryInput, directoryOutput);
							errorNetbeans = (String[]) resultsNetbeans.get(0);
							outputNetbeans = (String[]) resultsNetbeans.get(1);
							if (errorNetbeans[0] == "_") {
								if (errorNetbeans[1] != "_") {
									out.write("Change of behavior, Runtime Error in Netbeans, test: " + file.getAbsolutePath() + "\n");
									out.write("\nNetbeans error " + errorNetbeans[1] + "\n");
									counterDifBehNetbeans++;
								}
								if (errorNetbeans[1] == "_") {
									if (!outputNetbeans[0].equals(outputNetbeans[1])) {
										//out.write("\nTest: " + file.getAbsolutePath());																				
										out.write("Change of behavior in Netbeans, test: " + file.getAbsolutePath() + "\n");
										out.write("\nNetbeans outputBefore: " + outputNetbeans[0] + " outputAfter: " + outputNetbeans[1] + "\n");
										counterDifBehNetbeans++;
									} else {
										counterEquBehNetbeans++;
									}
								}
							} else {
								numInputWithRuntimeErrorNetbeans++;
							}

						} else {
							System.out.println("Compilation error in Netbeans, test: " + file.getAbsolutePath());
						}
					}

					if (outEC.exists() && outNB.exists()) {
						if (resultCompilationEC == 0 && resultCompilationNB == 0) {
							if (errorNetbeans[0] == "_" && errorNetbeans[1] == "_" 	&& errorEclipse[1] == "_") {
								out.write("outputNetbeans[1] " + outputNetbeans[1] + " outputEclipse[1] " 	+ outputEclipse[1]);
								if (!outputNetbeans[1].equals(outputEclipse[1])) {
									out.write("The output at runtime for test " + file.getAbsolutePath() + " is not the same in Ec & Nb\n");
									counterOutputDifferences++;
								} else {
									out.write("The output at runtime for test " + file.getAbsolutePath() + " is the same for Ec & Nb\n");
									counterOutputEquivalences++;
								}
							}
						}
					}

				}
			}

			out.write("\nTotal # tests that have different behavior in Eclipse: " 	+ counterDifBehEclipse + "\n");
			out.write("Total # tests that have equivalent behavior in Eclipse: " 	+ counterEquBehEclipse + "\n\n");
			out.write("\nTotal # tests that have different behavior in Netbeans: " 	+ counterDifBehNetbeans + "\n");
			out.write("Total # tests that have equivalent behavior in Netbeans: "   + counterEquBehNetbeans + "\n\n");
			out.write("Total # differences between outputs from Eclipse & Netbeans: " + counterOutputDifferences + "\n");
			out.write("Total # equivalences between outputs from Eclipse & Netbeans: " + counterOutputEquivalences + "\n");
			out.write("Total # input that failed at run time in Eclipse: " + numInputWithRuntimeErrorEclipse);
			out.write("Total # input that failed at run time in Netbeans: " + numInputWithRuntimeErrorNetbeans);
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) throws IOException {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-refactoring"))
				refactoringName = args[++i];
			else if (args[i].equals("-testclass"))
				testClassName = args[++i];
		}
		try {
			if (refactoringName == null) {
				System.out.println("Refactoring was not specified");
				throw new Exception("Refactoring was not specified");
			}
			if (refactoringName.equals("")) {
				System.out.println("Refactoring can not be an empty string");
				throw new Exception("Refactoring can not be an empty string");
			}
			if (testClassName == null) {
				System.out.println("TestClass was not specified");
				throw new Exception("TestClass was not specified");
			}
			if (testClassName.equals("")) {
				System.out.println("TestClass can not be an empty string");
				throw new Exception("TestClass can not be an empty string");
			}
			ComparePreAndPostRefactoredFiles.runJavaClass(refactoringName,
					testClassName);
		} catch (Exception e) {
			System.exit(1);
		}
	}

	public static void removeFirstLine(File argsFile) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(argsFile));
		String line;
		boolean firstLine = true;
		String codeForClass = "";
		while ((line = in.readLine()) != null) {
			if (firstLine) {
				firstLine = false;
				if (line.indexOf("package") < 0)
					codeForClass += line;
			} else {
				codeForClass += line;
			}
		}
		// System.out.println("codeForClass: "+ codeForClass);
		in.close();
		BufferedWriter out = new BufferedWriter(new FileWriter(argsFile));
		out.write(codeForClass);
		out.close();

	}

}
