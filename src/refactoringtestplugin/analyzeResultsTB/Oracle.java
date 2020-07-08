package refactoringtestplugin.analyzeResultsTB;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Oracle {

	public static void behaviorchanges(String resultFolderPath) {

		File resultFolder = new File(resultFolderPath);
		File[] listFiles = resultFolder.listFiles();
		for (File test : listFiles) {
			if (test.isDirectory()) {
				File[] testFolders = test.listFiles();
				for (File testFolder : testFolders) {
					if (testFolder.getName().equals("out")) {
						File[] resultsFiles = testFolder.listFiles();
						for (File file : resultsFiles) {
							if (file.getName().equals("BEHAVIORCHANGE_FAILURE")) {
								try {
									String pathname = resultFolderPath
											+ "/behaviorchange/"
											+ test.getName();
									copyDirectory(test, new File(pathname));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}

				}
			}
		}

	}

	public static void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				boolean mkdir = targetLocation.mkdir();
				if (mkdir)
					System.out.println("copiado com sucesso");
				else
					System.out.println("problema ao copiar");

			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} else {
			// targetLocation.createNewFile();

			InputStream in = new FileInputStream(sourceLocation);

			OutputStream out = new FileOutputStream(targetLocation);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	public static void categorize(String engine, String resultFolderPath,
			String problemFileName) {

		Map<String, Integer> problems = new HashMap<String, Integer>();

		File resultFolder = new File(resultFolderPath);
		File[] listFiles = resultFolder.listFiles();
		for (File test : listFiles) {
			if (test.isDirectory()) {
				File out = new File(test, "out");
				File outEngine = new File(out, engine);

				File target = new File(outEngine, problemFileName);

				if (target.exists()) {
					if (engine.equals("eclipse"))
						eclipseOracle(problems, test, target);
					else if (engine.equals("jrrt")) {						
						jrrtOracle(problems, test, target);						
					} else {
						netbeansOracle(problems,test,target);
					}
						
				}

			}
		}

		for (Map.Entry<String, Integer> problem : problems.entrySet()) {
			System.out.println(problem.getKey() + ": " + problem.getValue());
		}
		System.out.println("Total de bugs: " + problems.size());

	}
	
	public static void categorizeAntigo(String resultFolderPath,
			String problemFileName) {

		Map<String, Integer> problems = new HashMap<String, Integer>();

		File resultFolder = new File(resultFolderPath);
		File[] listFiles = resultFolder.listFiles();
		for (File test : listFiles) {
			if (test.isDirectory()) {
				File out = new File(test, "out");
				File target = new File(out, problemFileName);

				if (target.exists()) {
					eclipseOracle(problems, test, target);					
				}

			}
		}

		for (Map.Entry<String, Integer> problem : problems.entrySet()) {
			System.out.println(problem.getKey() + ": " + problem.getValue());
		}
		System.out.println("Total de bugs: " + problems.size());

	}
	
	private static void netbeansOracle(Map<String, Integer> problems, File test,
			File target) {
		FileReader in;
		try {
			in = new FileReader(target);
			BufferedReader br = new BufferedReader(in);
			String s;
			String x = "";
			

			while ((s = br.readLine()) != null) {
				if (s.contains("^"))
					continue;
				if (!s.contains("javac"))
					continue;
				if (s.contains("warning"))
					continue;
				if (s.contains("Compiling"))
					continue;
				s = s.replaceAll("[.]*:[1-9]+:", "");
				s = s.replaceAll("[.]*.java", "");
				s = s.replaceAll("P[1-2]_0/Class[1-3]_0", "");
				s = s.replaceAll("P[1-2]_0.Class[1-3]_0", "");
				s = s.replaceAll("[a-z]_0\\([a-z]*\\)", "");
				
				x = x + s + "\n";
				break;
			}
			if (problems.containsKey(x)) {
				Integer integer = problems.get(x);
				integer = integer + 1;
				problems.put(x, integer);
			} else {
				problems.put(x, 1);
				System.out.println(test);
			}

			in.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	private static void jrrtOracle(Map<String, Integer> problems, File test,
			File target) {
		try {
			FileReader in = new FileReader(target);
			BufferedReader br = new BufferedReader(in);
			String s;
			String x = "";
			boolean inclueAproxima = true;
			boolean achouErro = false;

			while ((s = br.readLine()) != null) {
				if (s.contains("^"))
					continue;
				// so conta o 1o. erro
				if (s.contains("----------") && achouErro)
					break;
				if (s.contains("ERROR")) {
					inclueAproxima = true;
					achouErro = true;
				}

				if (inclueAproxima) {
					if (s.contains("WARNING"))
						inclueAproxima = false;
					else {
						if (!s.contains("ERROR")
								&& !s.contains("problem")) {
							//para remover nome de classes e metodos
//							s = s
//									.replaceAll(
//											"[(]?[(]?[a-zA-Z0-9]+_[0-9][(]?[(]?[\\w]*[)]?[\\w]*[)]?",
//											" ");
							//para remover o codigo que ocorre o problmea, deixar so a mensagem
							if (s.contains(";"))
								s = "";
							else
								s = s
								.replaceAll(
										"[(]?[(]?[a-zA-Z0-9]+_[0-9][(]?[(]?[\\w]*[)]?[\\w]*[)]?",
										" ");
							String[] split = s.split(" ");
							s = "";
							for (String string : split) {
								if (!(string.startsWith("'") && string.endsWith("'"))) {
									s+= string +" ";
								}
							}
								
						} else
							s = "ERROR";
						x = x + "\n" + s;

					}

				}
			}
			if (problems.containsKey(x)) {
				Integer integer = problems.get(x);
				integer = integer + 1;
				problems.put(x, integer);
//				if (x.contains("o metodo foi removido")) {
//					System.out.println(test);
//				}
			} else {
				problems.put(x, 1);
				System.out.println(test);
			}

			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void eclipseOracle(Map<String, Integer> problems, File test,
			File target) {
		try {
			FileReader in = new FileReader(target);
			BufferedReader br = new BufferedReader(in);
			String s;
			
			
			//pega s� o 1o. erro
//			s = br.readLine();
//			s = s.replaceAll(
//					" [a-zA-Z0-9]+_[0-9][(]?[\\w]*[)]?", " ");
//			System.out.println("----------------------------------------------");
//			System.out.println(test);
//			System.out.println("----------------------------------------------");
			//pega todos os errors
			String x = "";
			while ((s = br.readLine()) != null) {			
				s = s.replaceAll(
						" [a-zA-Z0-9]+_[0-9][(]?[\\w]*[)]?", " ");
				
				String[] split = s.split(" ");
				s = "";
				for (String string : split) {
					if (!(string.startsWith("'") && string.endsWith("'"))) {
						s+= string +" ";
					}
				}
				if (!x.contains(s))
					x = x + "\n" + s;
			}
			//System.out.println(x);
			if (problems.containsKey(x)) {
				Integer integer = problems.get(x);
				integer = integer + 1;
				problems.put(x, integer);
			} else {
				System.out.println(test);
				
				problems.put(x, 1);
			}

			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void categorizeOege(String resultFolderPath) {

		Map<String, Integer> problems = new HashMap<String, Integer>();

		File resultFolder = new File(resultFolderPath);
		File[] listFiles = resultFolder.listFiles();
		for (File test : listFiles) {
			if (test.isDirectory()) {
				File[] testFolders = test.listFiles();
				for (File testFolder : testFolders) {
					if (testFolder.getName().equals("COMPILATION_ERROR")) {
						jrrtOracle(problems, testFolder, testFolder);
					}

				}

			}
		}
		for (Map.Entry<String, Integer> problem : problems.entrySet()) {
			System.out.println("===========================================");
			System.out.println(problem.getKey() + ": " + problem.getValue());
		}

	}

	public static void main(String[] args) {
//		String path = "C:/Users/Felipe/Documents/doutorado/experimentos/eclipse/pushdownmethod/skip20/";
		String path = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/EncapsulateField_skip25/Alloy/";
//		 Oracle.categorizeOege("/Users/gustavo/Doutorado/experiments/refactoring-constraints/renamefield/jrrt/");
		// Oracle.behaviorchanges("/Users/gustavo/Mestrado/genExperimentos/genExperimento/pullupmethod/package/");
//		Oracle.categorize("eclipse",path,
//						"POST_REFACTOR_NOT_COMPILE");
		
		Oracle.categorize("eclipse",path,
		"TRANSFORMATION_BUG");
		
//		Oracle
//		.categorizeAntigo(				
//				"/Users/gustavo/Doutorado/experiments/refactoring-constraints/addparameter/eclipse/",
//				"POST_REFACTOR_NOT_COMPILE");
	}

}
