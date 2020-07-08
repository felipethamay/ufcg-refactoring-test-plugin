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

public class TestJDollyContainsUdita extends TestCase {

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

			String directory = "/tmp/pushdownmethod3/";
			File last = new File(directory);

			if (!last.exists())
				throw new Exception("The directory " + last + " does not exist");
			File[] files = last.listFiles();

			List<String> listOfTestDifferences = new ArrayList<String>();
			// int numTestsOnlyECNoCompile = 0, numTestsOnlyNBNoCompile = 0,
			// numTestsBothECNB_ECNoCompile = 0, numTestsBothECNB_NBNoCompile =
			// 0;
			int numBothEc_Jrrt = 0;

			System.out.println("files.length: " + files.length);
			boolean naoChegou = true;


			int counter = 0;

			List<String> uditaPrograms = ComparatorUtil.getUitaPrograms();

			OutputManager out2 = new OutputManagerASCII("programs/Node_0.java");
			File uditaProgram = new File("programs/Node_0.java");
			// System.out.println(program);
			int ce = 0;
			ASTComparator comparator = new ASTComparator();
			comparator.setUpProject();

			for (String program : uditaPrograms) {
				counter++;
				if (counter < 446) continue;
				System.out.print("========================Programa ");
				System.out.print(counter);
				System.out.println("===============================");

				out2.createFile();
				out2.writeLine(program);
				out2.closeFile();
				String compile2 = refactoringtestplugin.util.Compile
						.compile("programs/Node_0.java");
				
				if (!compile2.contains("error")) {
					System.out.println(program);
					// itera��o em cada teste
					boolean hasEquivalent = false;

					for (int i = 0; i < files.length; i++) {
						if (hasEquivalent)
							break;
						directory = files[i].toString();
						File inputDirectory = new File(directory
								+ File.separator + "in");
						File outDir = new File(directory + File.separator
								+ "out");

						// pega os arquivos retornados de cada engine
						File[] outJavaFiles = inputDirectory.listFiles(fileFilter);

						int j = 0;
						while (j < outJavaFiles.length) {

							File fileA = outJavaFiles[j];

							// compara arquivo com cada arquivo do UDITA
							int error = 0;
							System.out.println("Comparando..." + i);
							try {
								if (i % 200 == 0) {
									comparator.tearDownProject();
									comparator.setUpProject();
								}
								comparator.setInputs(fileA, uditaProgram);
							} catch (Exception e) {
								System.out
										.println("files could not be read, # "
												+ i);
								continue;
							}
							if (comparator.compareIsomorphic()) {
								hasEquivalent = true;
								i = files.length;
								System.out.println("Equivalente");
							} 

							j++;
						}

					}
					
					//se nao tiver equivalente para
					if (!hasEquivalent) {
						System.out.println("Programa n�o tem equivalente");
						System.out.println(program);
						break;
					}

				} else {
					System.out.println("compilation error");
					ce++;
				}
			}

			
			
			
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

	}

}
