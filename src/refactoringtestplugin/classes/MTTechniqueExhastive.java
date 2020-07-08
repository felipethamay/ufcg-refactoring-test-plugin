package refactoringtestplugin.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.CreateChangeOperation;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.RefactoringStatusEntry;
import org.testorrery.IGenerator;

import br.edu.ufcg.saferefactor.core.Report;
import br.edu.ufcg.saferefactor.core.ResultComparator;
import br.edu.ufcg.saferefactor.refactoring.Constants;
//import executor.SRImpact;
import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;
import refactoringtestplugin.Activator;
import refactoringtestplugin.ProblemReport;
import refactoringtestplugin.SafeRefactorLogger;
import refactoringtestplugin.TestLogger;
import refactoringtestplugin.util.JavaProjectHelper;
import saferefactor.core.SRImpact;
import tests.jdolly.Conditions;

//import br.edu.ufcg.dsc.safeRefactor.core.analyser.Analyser;
//import br.edu.ufcg.dsc.safeRefactor.core.testGenerator.TestGenerator;
//import br.edu.ufcg.dsc.safeRefactor.core.testRunner.TestRunner;

public abstract class MTTechniqueExhastive {
	
	private Change undoChange;
	/**
	 * Flag indicating that the runner should check generated compilation units
	 * for compilation before running refactorings or outputting files.
	 */
	private static final boolean ENABLE_COMPILE_CHECK = true;
	/**
	 * Flag indicating that the runner should run the refactoring specified by
	 * the concrete class.
	 */
	private static final boolean ENABLE_REFACTORING_EXECUTION = true;
	/**
	 * Flag indicating that the runner should output files to the directory
	 * specified by {@link #getLogDirPath()}
	 */
	private static final boolean ENABLE_FILE_OUTPUT = true;
	/**
	 * The maximum number of compilation units to generate or null for unbounded
	 */
	private static final Integer MAX_GENERATIONS = null;

	
	private SafeRefactorLogger srLogger = new SafeRefactorLogger();
	
	/**
	 * The root directory in which generated compilation units and the
	 * refactoring results will be saved.
	 * 
	 * The returned value is defined by the derived class. In general it has the
	 * following form: TEMP_DIR/{refactoring}/{generator}/ where {refactoring}
	 * is the package name for a particular refacotring and {generator} is the
	 * specific generator being used to create input data. This value will be
	 * passed into the {@link TestLogger} that creates the test-by-test
	 * directory structure.
	 */
	protected abstract String getLogDirPath();

	/**
	 * The two-to-three sentence description of the type of refactoring being
	 * performed and the input used.
	 */
	public abstract String getDescription();

	/**
	 * The generator that provides the inputs for the derived class' test
	 */
	// protected abstract IGenerator<CompilationUnit> getCUGen();
	protected abstract IGenerator<List<CompilationUnit>> getCUGen();

	/**
	 * @return the {@link Refactoring} to perform
	 */
	protected abstract Refactoring getRefactoring(List<ICompilationUnit> icus)
			throws JavaModelException;

	protected abstract String getRefactoredClassName();
	
	static TestLogger logger;
	
	private boolean rejectedTransf = false;
	private String currentMsg = "";
	Map<String, Integer> warnings = new HashMap<String, Integer>();

	
	public void log() {
		String path = "/home/spg-experiment-3/Documents/OS/log.txt"; 
		OutputManager out = new OutputManagerASCII(path);
		try {
			out.createFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> keySet = warnings.keySet();
		for (String string : keySet) {
			try {
				if (!string.equals("")) {
					out.writeLine(string+"\n");
					out.writeLine(warnings.get(string)+"\n");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			out.closeFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void runTests(int skip) throws Exception {

//		try {
		// Bit of a hack to get the names.
		// Assumes this class is the generator and superclass is the
		// refactoring.
		String refactoringName = this.getClass().getSuperclass()
				.getSimpleName().replace("Test", "");
		String generatorName = this.getClass().getSimpleName();

	
		if (ENABLE_FILE_OUTPUT) {
			logger = new TestLogger(refactoringName, generatorName,
					getLogDirPath());

		} else {
			logger = new TestLogger(refactoringName, generatorName);
		}
 
		
		ProblemReport problemReport;
		File file = new File(logger.getTestDirectory().getAbsolutePath());
//		System.out.println("--------------------------"+logger.getTestDirectory());
		
		StringBuffer safiraResults = new StringBuffer();
//		StringBuffer srResults = new StringBuffer();
//		StringBuffer srResults2 = new StringBuffer();
		
		logger.start(this);

		int counter = 0;
		long experimentTime = System.currentTimeMillis();
		int generatedPrograms = 0;
		boolean bc = false;
		
		//apenas para segunda execucao
		String path = "/home/spg-experiment-3/Documents/OS/log.txt"; 
		InputManager in = new InputManagerASCII(path);
		in.openFile();
		while (!in.isEndOfFile()) {
			String line = in.readLine();
			if (!line.equals("")) {
				String msg = line;
				line = in.readLine();
				while (line.equals("")) {
					line = in.readLine();
				}
				try {
					int num = Integer.parseInt(line);
					warnings.put(msg, num);
					}catch(Exception e) {
						continue;
					}
			}
		}
		
		
		Conditions conds = Conditions.getConditionClass(refactoringName);
//		System.out.println(conds.numCond); TODO - Tentando acessar uma variavel nao publica
		
		conds.reset();
		StringBuffer sb = new StringBuffer();
		
//		List<List<CompilationUnit>> c = new ArrayList<List<CompilationUnit>>();
		
		
	
		
		
		for (List<CompilationUnit> cus : getCUGen()) {
//			if (firstFailure) break;
			try{
			counter++;
//			if (!list.contains(counter)) {
//				continue;
//			}
//			if (counter == 675 || counter == 675|| counter == 700) {
//				continue;
//			}
			
//			if (counter != 3750 && counter != 7400) continue;
			
			//=========================================
			if (counter < 12001) continue;
//			if (counter > 21000) {
//				this.log();
//				break;
//			}
			//=============================================
			
//			if (counter <= 20000 || counter > 25000) continue;
//			if (counter < 6337) continue;
//			if (counter > 6337) break;

//			if (true)
//			continue; 
			
			
		
//			
//			String source = "/home/spg-experiment-3/Documents/workspace/asource/bin/";
//			String target = "/home/spg-experiment-3/Documents/workspace/atarget/bin/";
//			String source = "/home/spg-experiment-3/Documents/OS/eclipse/movemethod/skip25/test15/in/";
//			String target = "/home/spg-experiment-3/Documents/OS/eclipse/movemethod/skip25/test15/out/eclipse1/";
//			SRImpact srii = new SRImpact("", source, target, "", "3","", true);
//			System.out.println("result ==== "+srii.isRefactoring());
//			
//			
//			if (true) break;
			if (generationIsComplete(logger.getGeneratedCount()))
				break;
			
//			if (((counter % 2) != 0) && ((counter % 3) != 0)) {
//			if ((counter % skip) != 0) {
			if ((counter % skip) == 0) {
				generatedPrograms++;
				int dir = 0;
				logger.dir = 0;
				conds.reset();
				boolean rejectedTransf = true;
				logger.increaseCount();
				String testDirectory = logger.getTestDirectory().getAbsolutePath();
			
				//descomentar aqui
				long timeSub = System.currentTimeMillis();
	
				
				
//				while (conds.nextChangeMin()) {
				
//				System.out.println(nextChangeMin);
				createProject();
	
				if (ENABLE_COMPILE_CHECK || ENABLE_REFACTORING_EXECUTION) {
	
					IPackageFragmentRoot packageRoot = createPackageRoot(project);
//					if (dir == 0) {
					List<ICompilationUnit> icus = new ArrayList<ICompilationUnit>();
					for (CompilationUnit cu : cus) {
						String cuName = getPrimaryTypeName(cu);
						ICompilationUnit icu = createICompilationUnit(packageRoot,
								cu, cuName);
						icus.add(icu);
					}
					if (dir == 0)
					logger.logGenerated(cus);
	
					if (ENABLE_COMPILE_CHECK) {
//						 IPackageFragment[] packageFragments =
//						 packageRoot.getJavaProject().getPackageFragments();
//						 for (IPackageFragment iPackageFragment :
//						 packageFragments) {
//						 ICompilationUnit[] compilationUnits =
//						 iPackageFragment.getCompilationUnits();
						boolean hasProblem = false;
						for (ICompilationUnit icu : icus) {
							problemReport = checkCompilationProblems(icu);
							if (problemReport.hasProblems()) {
								logger.logPreRefactorNotCompile(icu, problemReport);
								tearDownProject();
								hasProblem = true;
								break;
							}
						}
						if (hasProblem)
//							break;
							continue;
	
					}
//					}
					
				while (conds.nextChangeMin()) {
//					if (firstFailure) break;
					if (!rejectedTransf) break;
					if (dir == 0){
						rejectedTransf = false;
					}
				System.out.println(conds.getlog()+" "+dir);
					
						String targetPath;
						if (dir == 0) {
							conds.reset();
							System.out.println(conds.getlog());
							targetPath = testDirectory+ "/out/eclipse";
						} else {
							
//							tearDownProject();
//							createProject();
							targetPath =  testDirectory+  "/out/eclipse"+dir;
						}
						String sourcePath = testDirectory+"/in/";
	
//					String targetPath = logger.getTestDirectory().getAbsolutePath()+"/out/eclipse/";
	//				String path = "pattttSOurce: ------------------"+logger.getTestDirectory().getAbsolutePath();
	//				
	//				System.out.println(path);
	//				System.out.println(path);
					
					if (ENABLE_REFACTORING_EXECUTION) {
						Refactoring refactoring = getRefactoring(icus);
						problemReport = checkRefactoringApplies(refactoring);
						if (problemReport.hasProblems()) {
							String message = logger.logRefactoringInapplicable(problemReport, dir);
//							tearDownProject();
							//se ele rejeitou a transformacao com todas as condicoes (dir == 0), entao seta a variavel para true
							//para analisar as combinacoes das condicoes
							if (dir == 0 ) {
								//MELINA comentar isso depois
								//currentMsg = e.getMessage();
								//se a msg for nova
								//MELINA descomentar isso depois
								//if (e.getMessage().contains("cannot adjust") ||e.getMessage().contains("is used")) {
								if (oracleAM(message)) {
								//sb.append(e.getMessage()+" "+i+" "+generatedPrograms+"\n");
								rejectedTransf = true;
								}
							}
							dir++;
							logger.dir++;
//							rejectedTransf = true;
							continue;
						}
						//descomentar aqui
						if (!rejectedTransf) {
							break;
						}
						
						
						
						// faz a refatoração e verifica se teve problema
						long tmpStart = System.currentTimeMillis();
						problemReport = performRefactoring(refactoring);
						long tmpStop = System.currentTimeMillis();
						long tmpTotal = tmpStop - tmpStart;
						logger.setRefactoringTime(logger.getRefactoringTime() + tmpTotal);
						if (problemReport.hasProblems()) {
							logger.logRefactoringExecutionErrors(problemReport);
//							tearDownProject();
							undo();
							dir++;
							continue;
						}
						// loga a refatoração no tmp/out
						logger.logPostRefactoring(packageRoot, dir);
//						SRImpact c = null;
						// checa se a refatoração compilou
						if (ENABLE_COMPILE_CHECK) {
							problemReport = checkCompilationProblems(packageRoot);
							if (problemReport.hasProblems()) {
								logger.logPostRefactorNotCompile(problemReport);
//								tearDownProject();
								undo();
								System.out.println("COMPILATION ERROR");
								dir++;
//								break;
								continue;
							} 
						}
						
					
						checkBehaviorConditions3(logger.getTestDirectory(), dir);
					
						SRImpact sri = new SRImpact("",sourcePath , targetPath, "","1", "", true);
						System.out.println("================is refactoring "+sri.isRefactoring());
						if (sri.isRefactoring()) {
							if (dir != 0) {
								System.out.println("OverlyStrongConditions");
								sb.append(conds.getlog()+" subjetc OS"+generatedPrograms+" skip"+(counter/skip)+" dir"+dir+" "+currentMsg+"\n");
								System.out.println(conds.getlog()+" subjetc OS"+generatedPrograms+" skip"+(counter/skip)+" dir"+dir+" "+currentMsg);
								this.warnings.put(currentMsg, counter);
								}
								////MELINA descomentar isso depois
								rejectedTransf = false;
							break;
						}
						undo();
						dir++;
						logger.dir++;
						//descomentar aqui
					}		
//						COMENTEI AQUI ------------------

//						List<Object> checkBehaviorConditions = checkBehaviorConditions(logger
//								.getTestDirectory(), srResults, "1", counter);
//						problemReport = (ProblemReport)checkBehaviorConditions.get(0);
//						 srResults= (StringBuffer) checkBehaviorConditions.get(1);
//						String targetPath = logger.getTestDirectory().getAbsolutePath()+"/out/eclipse/";
//						if (problemReport.hasProblems()) {
//							logger.logBehaviorConditionFailure(problemReport);						
//							tearDownProject();
//	//						continue;
//						}
//						
//					
//						c = new SRImpact("",sourcePath,targetPath, "", "0.2");
//						List<Object> checkBehaviorConditionsSafira = checkBehaviorConditionsSafira(c, safiraResults, counter);
//						problemReport = (ProblemReport)checkBehaviorConditionsSafira.get(0);
//						safiraResults = (StringBuffer) checkBehaviorConditionsSafira.get(1);
//						
//	//					safiraResults = calculateChangeCoverage(c, safiraResults);
//	//					
//	//					srResults = calculateChangeCoverageSR(c, Constants.TEST, srResults);
//						
//						if (problemReport.hasProblems()) {
//							logger.logBehaviorConditionFailureSafira(problemReport);						
//							tearDownProject();
//	//						continue;
//						}
//						
//						long timeex = System.currentTimeMillis();
//						checkBehaviorConditions = checkBehaviorConditions(logger
//								.getTestDirectory(), srResults2, "0.2", counter);
//						problemReport = (ProblemReport)checkBehaviorConditions.get(0);
//						srResults2= (StringBuffer) checkBehaviorConditions.get(1);
//	//					srResults2 = calculateChangeCoverageSR(c, Constants.TEST, srResults2);
//						
//						SrexcTime += System.currentTimeMillis()-timeex;
//						
//						logger.logCoverage();
//						if (problemReport.hasProblems()) {
//							logger.logBehaviorConditionFailureSR2(problemReport);						
//							tearDownProject();
//							continue;
//						}
//						logger.logSuccessfulRefactoring();
						
						//			COMENTEI AQUI ------------------
					}
//				timeSub = System.currentTimeMillis() - timeSub;
//				if (dir != 0)
//				System.out.println("timeeeeee======================================= "+timeSub/1000);
					
//				tearDownProject();
				}
				
				tearDownProject();
				
			}
			}catch(java.lang.RuntimeException e) {
				this.log();	
			}
			//createProject();
		}
//		}catch(Exception e) {
//			
//		}
		logger.stop(srLogger);
		experimentTime = System.currentTimeMillis() - experimentTime;
//		safiraResults.append(experimentTime);
//		srResults.append(experimentTime);
		StringBuffer time = new StringBuffer();
		time.append("time: "+experimentTime);
		logResults(file, time, "safira_results");
		System.out.println("total time ----------------------");
		System.out.println("programas gerados"+ counter);
		System.out.println(experimentTime);
		String fileLog = "/home/spg-experiment-3/Documents/OS/eclipse/"+refactoringName+skip+".txt";
		
		conds.reset();
		System.out.println(sb.toString());
		
		sb.append( " total time: "+(experimentTime/1000) + "s programs: "+generatedPrograms+" "+safiraResults.toString());
//		System.out.println(text);
		refactoringtestplugin.util.FileUtil.gravaArquivo(fileLog, sb.toString());
//		refactoringtestplugin.util.FileUtil.gravaArquivo(fileLog, text);
//		logResults(file, srResults, "sr_results");
//		logResults(file, srResults2, "sr_results2");
	}
	
	
	private void undo() {
		PerformChangeOperation perform = new PerformChangeOperation(undoChange);
		//
		undoChange.initializeValidationData(new NullProgressMonitor());
		//
		try {
			ResourcesPlugin.getWorkspace().run(perform, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
								
	}
	private  boolean oracleAM( String msg) {
		
//		FileReader in = new FileReader(target);
//		BufferedReader br = new BufferedReader(in);
		String s = msg; 
		String x = "";
		boolean inclueAproxima = true;
		boolean achouErro = false;
//			if (s.contains("^"))
//				continue;
//			// so conta o 1o. erro
//			if (s.contains("----------") && achouErro)
//				break;
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
//						s = s
//								.replaceAll(
//										"[(]?[(]?[a-zA-Z0-9]+_[0-9][(]?[(]?[\\w]*[)]?[\\w]*[)]?",
//										" ");
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
							if (!(string.startsWith("'") && (string.endsWith("'") || string.endsWith("',")))) {
								s+= string +" ";
							}
						}
							
					} else
						s = "ERROR";
					x = x + "" + s;

				}

			}
		
		if (warnings.containsKey(x)) {
//			Integer integer = warnings.get(x);
//			integer = integer + 1;
//			warnings.put(x, integer);
			return false;
		} else {
//			warnings.put(x, 1);
			currentMsg = x;
			return true;
//			System.out.println(target.getAbsolutePath() + " "+x);
		}

//		in.close();
	
}
	
//	public StringBuffer calculateChangeCoverage(ClassManipulator c, StringBuffer safiraResults) {
//		
//		try {
//			Coverage coverage = new Coverage(c);
//			String safiraCoverage = coverage.calculateCoverage();
//			safiraResults.append(safiraCoverage+ "\n");
//			return safiraResults;
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	public StringBuffer calculateChangeCoverageSR(ClassManipulator c, String testPath, StringBuffer srResults) {
//		
//		try {
//			Coverage coverage = new Coverage(c);
//			String srCoverage = coverage.calculateCoverage(testPath);
//			srResults.append(srCoverage+ "\n");
//			return srResults;
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	
	public void logResults(File file, StringBuffer safiraResults,
			String filename) throws IOException {
		OutputManager opm = new OutputManagerASCII(file.getAbsolutePath() + "/"
				+ filename);
		opm.createFile();
		opm.writeLine(safiraResults.toString());
		opm.closeFile();
		System.out.println(safiraResults.toString());
	}
	
	private FileFilter directory() {
		FileFilter ff1 = new FileFilter() {
			public boolean accept(File b) {
				return b.isDirectory();
			}
		};
		return ff1;
	}

	private FileFilter javaFile() {
		FileFilter ff1 = new FileFilter() {
			public boolean accept(File b) {
				return b.getName().endsWith(".java");
			}
		};
		return ff1;
	}
	


	private List<ICompilationUnit> createICompilationUnits(File dir,
			IPackageFragmentRoot packageRoot) throws JavaModelException {

		List<ICompilationUnit> result = new ArrayList<ICompilationUnit>();

		File[] packages = dir.listFiles(directory());

		if (packages.length > 0) {

			for (File packageName : packages) {
				File[] javaFiles = packageName.listFiles(javaFile());

				for (File javaFile : javaFiles) {

					String cuName = javaFile.getName().replace(".java", "");
					String cuContent = getCuContent(javaFile);
					ICompilationUnit icu = createICompilationUnit(packageRoot,
							cuContent, packageName.getName(), cuName);
					result.add(icu);
				}
			}
		} 
			File[] javaFiles = dir.listFiles(javaFile());
 for (File javaFile : javaFiles) {
	 //File javaFile = javaFiles[0];

		String cuName = javaFile.getName().replace(".java", "");
		String cuContent = getCuContent(javaFile);
		ICompilationUnit icu = createICompilationUnit(packageRoot,
				cuContent, "", cuName);
		result.add(icu);
}
			

		
		return result;
	}

	private String getCuContent(File javaFile) {
		BufferedReader in;
		String str;
		String javaContent = "";
		try {
			in = new BufferedReader(new FileReader(javaFile));
			while ((str = in.readLine()) != null) {
				javaContent += "\n" + str;
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return javaContent;
	}
	
//	public static List<Object> checkBehaviorConditionsSafira(
//			SRImpact c, StringBuffer b, int counter) {
//
//		ProblemReport pr = new ProblemReport();
//
//		boolean isRefactoring = c.isRefactoring();
//
////		b.append(c.getFileIntersection().size() + " " + c.getGeneratedTests() + " "+
////				c.getAnalysisTime()+" "+ c.getTotalTime()+" "+isRefactoring + "\n");
//		
//		b.append(counter+" "+c.getTests() + " "+ c.getTotalTime()+" "+isRefactoring );
//
//		if (!isRefactoring) {
//			pr.addProblem("has behavior change");
//		}
//		
//		Saferefactor s = new Saferefactor(logger
//				.getTestDirectory() + "/in/",  logger.getTestDirectory() + "/out/eclipse", "", "", "");
//		
////		b = decomposeMethod(s, c, b);
//		List<Object> result = new ArrayList<Object>();
//		result.add(pr);
//		result.add(b);
//		return result;
//	}
	
//	public static StringBuffer decomposeMethod(Saferefactor s, SRImpact c, StringBuffer b) {
//		
//		int safiraMethods = 0;
//		int srMethods = 0;
//		List<String> fileIntersection = c.getIa().getFileIntersection();
////		getFileIntersection();
//
//		s.getAnalyzer().generateMethodListFile();
//		
//		List<SMethod> commonMethods = s.getAnalyzer().getCommonMethods();
//		
//		for (SMethod sMethod : commonMethods) {
//			srMethods += sMethod.getAllowedClasses().size();
//			if (sMethod.getAllowedClasses().size() == 0) {
//				srMethods++;
//			}
//		}
//		
//		List<SConstructor> commonConstructors = s.getAnalyzer().getCommonConstructors();
//		srMethods+= commonConstructors.size();
//		
//		List<String> srNotContains = new ArrayList<String>();
//		for (String string : fileIntersection) {
//			boolean contains = false;
//			
//			String[] method_class = string.split(":");
//
//			String methodFullName = method_class[1];
//			if (method_class[0].contains("cons")) {
//				safiraMethods++;
//				for (SConstructor sConstructor : commonConstructors) {
//					String consName = sConstructor.getName()+".<init>(";
//					boolean first = true;
//					for (String parameter : sConstructor.getParameters()) {
//						if (!first) {
//							consName+=","+parameter;
//						} else {
//							first = false;
//							consName+=parameter;
//						}
//					}
//					consName+=")";
//					if (consName.equals(methodFullName.trim())) {
//						contains = true;
//					} 
//				}
//			} else {
//				//nao � construtor
//				for (SMethod sMethod : commonMethods) {
//					String metName = sMethod.getDeclaringClass()+"."+ sMethod.getSimpleName()+"(";
//					boolean first = true;
//					for (String parameter : sMethod.getParameterList()) {
//						if (!first) {
//							metName+=","+parameter;
//						} else {
//							first = false;
//							metName+=parameter;
//						}
//					}
//					metName+=")";
//					if (methodFullName.trim().equals(metName.trim())) {
//						contains = true;
//						if (method_class.length ==3) {
//							String[] classes = method_class[2].split(";");
//							for (String ac : classes) {
//								safiraMethods++;
//								boolean containsAc = false;
//								Set<String> allowedClasses = sMethod.getAllowedClasses();
//								for (String acSR : allowedClasses) {
//									if (ac.equals(acSR)) {
//										containsAc = true;
//									}
//								}
//								if (!contains) {
//									srNotContains.add(methodFullName+":"+ac);
//								}
//	//							System.out.println("Safira: declaredClasses "+string2);
//							}
//						} else {
//							safiraMethods++;
//						}
//					} 
////					System.out.println("class "+sMethod.getDeclaringClass()+" method "+sMethod.getSimpleName()+ " declaringC "+sMethod.getDeclaringClass()
////							+" parameters "+sMethod.getParameterList());
//				}
//				
//			}
//			if (!contains) {
//				srNotContains.add(methodFullName);
//			}
//		}
//		b.append(" "+ safiraMethods+" "+srMethods+ "\n");
//		for (String string : srNotContains) {
//			b.append(string+"\n");
//		}
//		return b;
//	}
	
//	public String contains(String methodFullName, String[] classes, List<SMethod> methods) {
//		String result = "";
//		for (SMethod sMethod : methods) {
//			if ((sMethod.getDeclaringClass() + "."+sMethod.getSimpleName()))
//		}
//		
//	}
	
	
	public List<Object> checkBehaviorConditions(File testDirectory, StringBuffer b, String timeLimit, int counter) {
		
		long time = System.currentTimeMillis();
		
		ProblemReport pr = new ProblemReport();
		
		br.edu.ufcg.saferefactor.core.util.FileUtil.createFolders();
		File buildFile = new File(Activator.getDefault().getPluginFolder() + Constants.FILE_SEPARATOR + "ant/build.xml");
		Project p = new Project();

		// propriedades
		p.setProperty("source", testDirectory + "/in/");
		p.setProperty("target", testDirectory + "/out/eclipse");
		// p.setProperty("methodlist", methodList.toString());
		p.setProperty("timeout", timeLimit);
		p.setProperty("bin", "");
		p.setProperty("lib", "");
		p.setProperty("src", "");
		p.setProperty("tests.folder", Constants.TEST);

		// log do ant no console
		// TODO logar tamb�m em arquivo
		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
		p.addBuildListener(consoleLogger);
		
		p.addBuildListener(srLogger);
		
		p.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		p.addReference("ant.projectHelper", helper);
		helper.parse(p, buildFile);
		p.executeTarget(p.getDefaultTarget());


		// Saferefactor sr = new Saferefactor(in, out, "", "", "");
		// return !sr.isRefactoring("1",true);
		
		
		///
		

		
		ResultComparator rc = new ResultComparator(Constants.TESTSRC,
				Constants.TESTTGT);
		
		Report report = rc.generateReport();
		
		time = System.currentTimeMillis() - time;
		
		b.append(counter+" "+report.getGenratedTests() + " "+ time+" "+report.isSameBehavior() + "\n");
		
		if (!report.isSameBehavior())
			pr.addProblem("has behavior change");

		List<Object> result = new ArrayList<Object>();
		result.add(pr);
		result.add(b);
		return result;
	}
	
	public static ProblemReport checkBehaviorConditions3(File testDirectory, int dir) {

		ProblemReport pr = new ProblemReport();

		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			AntRunner runner = new AntRunner();

			//propriedades
			Map<String, String> properties = new HashMap<String, String>();						
			properties.put("source", testDirectory + "/in/");
			if (dir == 0) {
				properties.put("target", testDirectory + "/out/eclipse/");	
			} else {
				properties.put("target", testDirectory + "/out/eclipse"+dir+"/");	
			}
			properties.put("bin", "");
			properties.put("lib", "");
			properties.put("src", "");
			properties.put("tests.folder", Constants.TEST);

			runner.setBuildFileLocation(Activator.getDefault().getPluginFolder() + Constants.FILE_SEPARATOR + "ant/build_compile.xml");
			runner.setArguments("-Dmessage=Building -verbose");
			runner.addUserProperties(properties);
			
			runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");
			
			String tempdir = System.getProperty("java.io.tmpdir");
			runner.setArguments("-logfile "	+ tempdir + "/logAnt_analyzer3.txt");
			runner.run(monitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		ResultComparator rc = new ResultComparator(Constants.TESTSRC,
//				Constants.TESTTGT);
//		Report report = rc.generateReport();
//		if (!report.isSameBehavior())
//			pr.addProblem("has behavior change");

		return pr;
	}

	protected boolean generationIsComplete(int generatedCount) {
		return MAX_GENERATIONS != null && generatedCount >= MAX_GENERATIONS;
	}

	protected ProblemReport checkRefactoringApplies(Refactoring refactoring)
			throws CoreException {
		ProblemReport pr = new ProblemReport();
		RefactoringStatus status = refactoring
				.checkAllConditions(new NullProgressMonitor());
		if (status.getSeverity() != RefactoringStatus.OK) {
			for (RefactoringStatusEntry entry : status.getEntries()) {
				pr.addProblem(entry.getMessage());
			}
		}
		return pr;
	}

	protected IJavaProject project;

	public void createProject() throws Exception {
		project = JavaProjectHelper.createJavaProject(
				"TestProject" + System.currentTimeMillis(), "bin");
		JavaProjectHelper.addRTJar(project);
	}

	public IJavaProject getProject() {
		return project;
	}

	public void tearDownProject() throws CoreException {
		if (project.exists())
			JavaProjectHelper.delete(project);
		
	}

	protected IPackageFragmentRoot createPackageRoot(IJavaProject project)
			throws CoreException {
		String packageRootName = "src";
		return JavaProjectHelper.addSourceContainer(project, packageRootName);
	}

	protected ICompilationUnit createICompilationUnit(
			IPackageFragmentRoot packFragRoot, CompilationUnit cu, String cuName)
			throws JavaModelException {
		String packageName = getPackageNameFrom(cu);
		IPackageFragment packFrag = packFragRoot.createPackageFragment(
				packageName, true, null);

		ICompilationUnit icu = packFrag.createCompilationUnit(cuName + ".java", // CU
				// name
				cu.toString(), // CU source
				true, // overwrite if CU exists
				new NullProgressMonitor()); // ignore progress
		return icu;
	}

	protected ICompilationUnit createICompilationUnit(
			IPackageFragmentRoot packFragRoot, String cuContent,
			String packageName, String cuName) throws JavaModelException {
		IPackageFragment packFrag = packFragRoot.createPackageFragment(
				packageName, true, null);

		ICompilationUnit icu = packFrag.createCompilationUnit(cuName + ".java", // CU
				// name
				cuContent, // CU source
				true, // overwrite if CU exists
				new NullProgressMonitor()); // ignore progress
		return icu;
	}

	protected String getPackageNameFrom(CompilationUnit cu) {
		PackageDeclaration packDecl = cu.getPackage();
		if (packDecl == null) {
			return "";
		}
		return packDecl.getName().toString();
	}

	protected ProblemReport checkCompilationProblems(
			IPackageFragmentRoot packageRoot) throws JavaModelException {
		ProblemReport problemReport = new ProblemReport();
		IPackageFragment packFrag;
		for (IJavaElement element : packageRoot.getChildren()) {
			if (element instanceof IPackageFragment) {
				packFrag = (IPackageFragment) element;
				for (IJavaElement element2 : packFrag.getChildren()) {
					if (element2 instanceof ICompilationUnit) {
						checkCompilationProblems(problemReport,
								(ICompilationUnit) element2);
					}
				}
			}
		}
		return problemReport;
	}

	protected ProblemReport checkCompilationProblems(ICompilationUnit icu)
			throws JavaModelException {
		ProblemReport problemReport = new ProblemReport();
		checkCompilationProblems(problemReport, icu);
		return problemReport;
	}

	protected void checkCompilationProblems(final ProblemReport problemReport,
			ICompilationUnit icu) throws JavaModelException {
		class CompileStatus implements IProblemRequestor {
			public void acceptProblem(IProblem problem) {
				if (problem.isError()) {
					problemReport.addProblem(problem.getMessage());
				}
			}

			public void beginReporting() {
			}

			public void endReporting() {
			}

			public boolean isActive() {
				return true;
			}
		}
		CompileStatus status = new CompileStatus();
		icu.becomeWorkingCopy(status, null);
		icu.discardWorkingCopy();
	}

	protected String getPrimaryTypeName(CompilationUnit cu) {
		String first = null;
		for (Object type : cu.types()) {
			if (type instanceof TypeDeclaration) {
				String typeName = ((TypeDeclaration) type).getName().toString();
				if (first == null) {
					first = typeName;
				}
				for (Object modifier : ((TypeDeclaration) type).modifiers()) {
					if (((Modifier) modifier).getKeyword() == Modifier.ModifierKeyword.PUBLIC_KEYWORD) {
						return typeName;
					}
				}
			}
		}
		return first;
	}

	// simplified from
	// org.eclipse.jdt.ui.tests.refactoring.RefactoringTest.performRefactoring(Refactoring
	// ref, boolean providesUndo)
	protected ProblemReport performRefactoring(Refactoring refactoring)
			throws CoreException {
		final CreateChangeOperation create = new CreateChangeOperation(
				new CheckConditionsOperation(refactoring,
						CheckConditionsOperation.ALL_CONDITIONS),
				RefactoringStatus.FATAL);
		PerformChangeOperation perform = new PerformChangeOperation(create);
	   // perform.getChange().initializeValidationData(new NullProgressMonitor());
		

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.run(perform, new NullProgressMonitor());
		undoChange = perform.getUndoChange();
		RefactoringStatus conditionCheckingStatus = create
				.getConditionCheckingStatus();
		
		ProblemReport problemReport = new ProblemReport();
		if (conditionCheckingStatus.getSeverity() != RefactoringStatus.OK) {
			problemReport.addProblems(conditionCheckingStatus.getEntries());
		}
		return problemReport;
	}

	/**
	 * Should be overridden by refactorings that change the target
	 * {@link ICompilationUnit}
	 * 
	 * @return a new {@link ICompilationUnit} if the old one was changed by a
	 *         refactoring
	 */
	protected ICompilationUnit getPostRefactoringICompilationUnit() {
		throw new RuntimeException("ICompilationUnit should not have changed");
	}

	/**
	 * Checks any additional constraints on the refactored compilation unit.
	 * Should be overridden by derived classes that need to specify custom
	 * checks.
	 * 
	 * @throws JavaModelException
	 * @throws Exception
	 */
	protected ProblemReport checkCustomPostconditions(ICompilationUnit icu)
			throws Exception {
		return new ProblemReport();
	}

	/**
	 * Utility function that returns the system's temporary directory.
	 */
	public static String getSystemTempDir() {
//		String tempdir = System.getProperty("java.io.tmpdir");
//		if (tempdir == null) {
//			throw new IllegalArgumentException("Temp dir is not specified");
//		}
//		String separator = System.getProperty("file.separator");
//		if (!tempdir.endsWith(separator)) {
//			return tempdir + separator;
//		}
//		return tempdir;
		return "/home/spg-experiment-3/Documents/OS/eclipse/";
	}
	
//	public static void main(String[] args) throws IOException {
//		
//		StringBuffer s = new StringBuffer();
//		RefactoringTest r = new RefactoringTest() {
//			
//			@Override
//			protected Refactoring getRefactoring(List<ICompilationUnit> icus)
//					throws JavaModelException {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			protected String getRefactoredClassName() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			protected String getLogDirPath() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public String getDescription() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			protected IGenerator<List<CompilationUnit>> getCUGen() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};
//		for (int i = 1; i < 100; i++) {
//			String path = "C:/Users/Felipe/Documents/mestrado/dissertacao/experimento/eclipse/encapsulateField/test"+i+"/";
//			String pathVerify = path + "out/eclipse/";
//			File fileVerify = new File(pathVerify);
//			String[] list = fileVerify.list();
//			boolean ok = true;
//			for (String string : list) {
//				if (string.contains("REFACTORING_INAPPLICABLE") || string.contains("PRE_REFACTOR_NOT_COMPILE")) {
//					ok = false;
//					break;
//				}
//			}
//			if (ok) {
//				File f = new File(path);
//				List<Object> checkBehaviorConditions = r.checkBehaviorConditions(f, s, "0.2", i);
//				s = (StringBuffer) checkBehaviorConditions.get(1);
//			}
//		}
//		r.logResults(new File("C:/Users/Felipe/Documents/mestrado/dissertacao/experimento/eclipse/encapsulateField/sr_results2.txt"), s, "sr_results2");
//		
//		
//	}
}