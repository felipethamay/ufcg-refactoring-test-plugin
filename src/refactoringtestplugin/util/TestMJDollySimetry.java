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

public class TestMJDollySimetry extends TestCase {

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
			StringBuffer logger = new StringBuffer();
			

			

			List<String> programs = AlloyRunJava.getPrograms();
			System.out.println("Programas gerados pelo JDolly: "
					+ programs.size());
			

			OutputManager out3 = new OutputManagerASCII(
					"programs2/Node_0.java");
			OutputManager out2 = new OutputManagerASCII(
					"programs/Node_0.java");
			
			int i = 0;
			int ce = 0;
			ASTComparator comparator = new ASTComparator();
			comparator.setUpProject();
			List<List<Integer>> todosSimetricos = new ArrayList<List<Integer>>();
			List<List<String>> todosProgramasSimetricos = new ArrayList<List<String>>();
			for (String program : programs) {
				boolean hasSimetric = false; 
				i++;
				System.out.print("========================Programa ");
				System.out.print(i);
				System.out.println("===============================");
				boolean alreadyEvaluated = false;
				for (List<Integer> list : todosSimetricos) {
					if (list.contains(new Integer(i)))
						alreadyEvaluated = true;
				}
				if (alreadyEvaluated)
					continue;
				
				out3.createFile();
				out3.writeLine(program);
				out3.closeFile();
				File jdollyProgram = new File("programs2/Node_0.java");
				String compile2 = refactoringtestplugin.util.Compile
						.compile("programs2/Node_0.java");
				if (!compile2.contains("error")) {
					System.out.println(program);
					// itera��o em cada teste
					boolean hasEquivalent = false;
					
					int counter = 1;
					List<Integer> simetricos = new ArrayList<Integer>();
					List<String> programasSimetricos = new ArrayList<String>();
					for (String sb : programs) {						
						out2.createFile();
						out2.writeLine(sb);
						out2.closeFile();
						File uditaProgram = new File("programs/Node_0.java");
						
						String compile = refactoringtestplugin.util.Compile
								.compile("programs/Node_0.java");

						if (!compile.contains("error")) {								
							System.out.print("Comparando...");
							System.out.println(counter);

							try {
								if (counter % 100 == 0) {
									comparator.tearDownProject();
									comparator.setUpProject();
								}
								//depurar
//								if (counter == 80)
//									System.out.println("depurar");
								comparator.setInputs(jdollyProgram,
										uditaProgram);
							} catch (Exception e) {
								System.out
										.println("files could not be read, # ");
								continue;
							}
							if (comparator.compareIsomorphicWithSimetry()) {
								if (hasEquivalent == true) {
									System.out.println("Programa simetrico");
									hasSimetric = true;
								}  									
								hasEquivalent = true;
								simetricos.add(new Integer(counter));
								programasSimetricos.add(sb);
								System.out.println("Equivalente");
//								break;
							}							

						} else {
							System.out
									.println("Programa do Udita n�o compila");
						}
						counter++;
					}
					
					if (hasEquivalent == false) {
						// out.write(comparator
						// .getCompareMessage() + "\n\n");
						String noEquivalent = program;
						

						System.out.println("Problema: Program has no equivalent program");						
						System.out.println(noEquivalent);
						break;
						
					}

					if (hasSimetric == true) {
					 todosSimetricos.add(simetricos);	
					 todosProgramasSimetricos.add(programasSimetricos);
					}
					
				} else {
					ce++;
					System.out.println("compilation error");
				}

//				i++;
//				break;
			}
			
			int totalSimetricos = 0;
			for (List<String> list : todosProgramasSimetricos) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
				logger.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>\n");
				int j = 0;
				for (String string : list) {
					j++;
					if (j > 1)
						totalSimetricos++;
					System.out.println(string);
					logger.append("\n" + string);
				}
			}
			System.out.println("Casos Simetricos: " + totalSimetricos);
			logger.append("\nCasos Simetricos: " + totalSimetricos);
			OutputManager out4 = new OutputManagerASCII("logTestMUditaSimetry");
			out4.createFile();
			out4.writeLine(logger.toString());
			out4.closeFile();

		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

	}

}
