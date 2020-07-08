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

public class TestMJDollyContainsUdita extends TestCase {

	public void testCompareFromFiles_ForGeneratedInputFiles() throws Exception {

		try {
			StringBuffer logger = new StringBuffer();

			

			List<String> noequivalents = new ArrayList<String>();

			List<String> jdollyPrograms = AlloyRunJava.getPrograms();
			List<String> uditaPrograms = ComparatorUtil.getUitaPrograms(3);

			OutputManager out2 = new OutputManagerASCII("programs/Node_0.java");
			// System.out.println(program);
			OutputManager out3 = new OutputManagerASCII("programs2/Node_0.java");
			int counter = 0;
			int ce = 0;
			ASTComparator comparator = new ASTComparator();
			comparator.setUpProject();
			
			for (String sb : uditaPrograms) {
				counter++;
				System.out.print("========================Programa ");
				System.out.print(counter);
				System.out.println("===============================");
				out2.createFile();
				out2.writeLine(sb.toString());
				out2.closeFile();
				File uditaProgram = new File("programs/Node_0.java");
				// System.out.println(sb.toString());

				String compile = refactoringtestplugin.util.Compile
						.compile("programs/Node_0.java");
				
				if (!compile.contains("error")) {

					// itera��o em cada teste
					boolean hasEquivalent = false;
					

					int i = 0;
					for (String program : jdollyPrograms) {
						System.out.print("Comparando...");
						System.out.println(i);

						out3.createFile();
						out3.writeLine(program);
						out3.closeFile();
						File jdollyProgram = new File("programs2/Node_0.java");
						String compile2 = refactoringtestplugin.util.Compile
								.compile("programs2/Node_0.java");
						if (!compile2.contains("error")) {

							try {
								if (i % 100 == 0) {
									comparator.tearDownProject();
									comparator.setUpProject();
								}
								comparator.setInputs(jdollyProgram,
										uditaProgram);
							} catch (Exception e) {
								System.out
										.println("files could not be read, # ");
								continue;
							}
							if (comparator.compareIsomorphicWithSimetry()) {
								hasEquivalent = true;
								System.out.println("Equivalente");
								break;
							}
						} else {
							System.out
									.println("Programa do JDolly n�o compila");
						}
						i++;
					}
					//
					if (hasEquivalent == false) {
						String noEquivalent = sb;
						noequivalents.add(noEquivalent);

						System.out.println("Program has no equivalent program");
						System.out.println(noEquivalent);
						
						
						
					}

				} else {
					// ce++;
					System.out.println("compilation error");
				}

			}
			
			System.out.println("Total de programas nao contidos: "
					+ noequivalents.size());
			logger.append("Total de programas nao contidos: "
					+ noequivalents.size());

			List<String> toRemove = new ArrayList<String>();
			
			logger.append("\nProgramas nao contidos:");
			for (String program : noequivalents) {
				if (toRemove.contains(program))
					continue;
				
				logger.append("\n====================================\n");
				logger.append(program);
//				System.out.println("==================Comparando:");
//				System.out.println(program);
//				System.out.println("==================Com:");
				boolean hasSimetry = false;
				for (String program2 : noequivalents) {
//					System.out.println(program2);
//					System.out.println("=================");
					boolean isEquivalent = ComparatorUtil.isEquivalent(program, program2,
							comparator);
					if (hasSimetry && isEquivalent) {
						toRemove.add(program2);
						break;
					}
					if (isEquivalent)
						hasSimetry = true;

				}
			}
			for (String program : toRemove) {
				noequivalents.remove(program);
			}
			System.out.println("Total de programas unicos nao contidos: "
					+ noequivalents.size());
			logger.append("Total de programas unicos nao contidos: "
					+ noequivalents.size());
			logger.append("\nProgramas nao contidos:");
			for (String string : noequivalents) {
				System.out.println("=====================");
				System.out.println(string);
				logger.append("\n====================================\n");
				logger.append(string);
			}
			OutputManager out4 = new OutputManagerASCII("logTestJDollyContainsUdita");
			out4.createFile();
			out4.writeLine(logger.toString());
			out4.closeFile();

		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

	}

}
