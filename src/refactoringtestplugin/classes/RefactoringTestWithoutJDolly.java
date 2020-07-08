
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
import br.edu.ufcg.saferefactor.core.Saferefactor;
import br.edu.ufcg.saferefactor.refactoring.Constants;
import io.OutputManager;
import io.OutputManagerASCII;
import jdolly.JDollyImp;
import refactoringtestplugin.Activator;
import refactoringtestplugin.ProblemReport;
import refactoringtestplugin.ReadProject;
import refactoringtestplugin.SafeRefactorLogger;
import refactoringtestplugin.TestLogger;
import refactoringtestplugin.util.JavaProjectHelper;
import saferefactor.core.SRImpact;


//import br.edu.ufcg.dsc.safeRefactor.core.analyser.Analyser;
//import br.edu.ufcg.dsc.safeRefactor.core.testGenerator.TestGenerator;
//import br.edu.ufcg.dsc.safeRefactor.core.testRunner.TestRunner;

public abstract class RefactoringTestWithoutJDolly {
	
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

	public void runTests(int skip) throws Exception {

		
		SRImpact srii;
		for (int i = 1; i <=30 ; i++) {
			
//			if (i ==3 || i == 4 || i == 7 || i == 10 || i == 25 || i == 29) {
				if (i ==24) {
		
		String source = "C:/Users/Felipe/Documents/doutorado/experiments/MT/jrrt/PullUpMethod/test"+i+"/";
		String target = "C:/Users/Felipe/Documents/doutorado/experiments/MT/jrrt/PullUpMethod/test"+i+"/out/jrrt/";
		srii = new SRImpact("", source, target, "","1", "", true);
		System.out.println("================is refactoring "+srii.isRefactoring());
		if (srii.isRefactoring()) {
		OutputManager out = new OutputManagerASCII("C:/Users/Felipe/Documents/doutorado/experiments/MT/jrrt/PullUpMethod/test"+i+"/out/jrrt/BUG_OSC.txt");
		out.createFile();
		out.closeFile();
		}
			}
		}
		if (true) {
			return;
		}
		
//		String source = "/var/folders/w8/q3gmb1w51y3dst2j7lxvr3qw0000gn/T/pullupmethod/alloyASTGenerator0/test47/in";
//		String target = "/var/folders/w8/q3gmb1w51y3dst2j7lxvr3qw0000gn/T/pullupmethod/alloyASTGenerator0/test47/out/eclipse";
//					SRImpact sr;
//					sr = new SRImpact("",source , target, "","1", "", true);
//					System.out.println(sr.isRefactoring());
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
 
		
		ProblemReport problemReport = null;
		File file = new File(logger.getTestDirectory().getAbsolutePath());
//		System.out.println("--------------------------"+logger.getTestDirectory());
		
		StringBuffer safiraResults = new StringBuffer();
//		StringBuffer srResults = new StringBuffer();
//		StringBuffer srResults2 = new StringBuffer();
		
		logger.start(this);

		for (int i = 0; i<=2 ;i++) {
		int counter = 0;
		long experimentTime = System.currentTimeMillis();
		long usedMemory = 0;
		long freeMemory = 0;
		long freeMemory_perc = 0;
		int mb = 1024 * 1024;
		StringBuffer sb = new StringBuffer();
		long timee = System.currentTimeMillis();
		String path = "C:/Users/Felipe/Documents/memory_encapsulate.txt";
//		for (List<CompilationUnit> cus : getCUGen()) {
			
		ReadProject rp = new ReadProject();
//		List<CompilationUnit> cus = rp.getCus_("C:/Users/Felipe/Documents/workspace/source/");
//		List<CompilationUnit> cus = rp.getCus_("C:/Users/Felipe/Downloads/org.eclipse.jdt.ui/src/org.eclipse.jdt.ui.tests.refactoring/resources/MoveInstanceMethod/cannotMove/test"+i+"/in/");
//		List<CompilationUnit> cus = rp.getCus_("C:/Users/Felipe/git/eclipse.jdt.ui/org.eclipse.jdt.ui.tests.refactoring/resources/PushDown/testFail"+i+"/in/");
		List<CompilationUnit> cus = rp.getCus_("C:/Users/Felipe/Documents/doutorado/experiments/MT/eclipse/PullUpInputs/testGenericsFail"+i+"/in/");
		
//		if (i > 0) {
//		ConditionsPullUpMethod c = new ConditionsPullUpMethod();
//		c.cond1.disable();
//		}
		
//			System.gc();
			counter++;
			
			
//			if (counter < 135 ) continue;
//			if (counter > 135) break;

//			if (true)
//			continue; 
			
			
			
//			if (generationIsComplete(logger.getGeneratedCount()))
//				break;
			
//			if (((counter % 2) != 0) && ((counter % 3) != 0)) {
//			if ((counter % skip) != 0) {
			
				createProject();
	
				if (ENABLE_COMPILE_CHECK || ENABLE_REFACTORING_EXECUTION) {
	
					IPackageFragmentRoot packageRoot = createPackageRoot(project);
	
					List<ICompilationUnit> icus = new ArrayList<ICompilationUnit>();
					for (CompilationUnit cu : cus) {
						String cuName = getPrimaryTypeName(cu);
						ICompilationUnit icu = createICompilationUnit(packageRoot,
								cu, cuName);
						icus.add(icu);
					}
					logger.logGenerated(cus);
//					if (true)
//						continue; 
	
				
//					
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
//						
						if (hasProblem) {
//							Iterable<String> problems = problemReport.getProblems();
//							boolean continue_ = false;
//							for (String string : problems) {
//								if (string.contains("is undefined for the type") || string.contains("is inconsistent") ||
//										string.contains("must implement the inherited abstract method") || string.contains("cannot be a superinterface of")) {
//									continue_ = true;
//								}
//							}
//							if (continue_) continue;
//							break;
//							continue;
						}
//							continue;
//							break;
//						else
//							continue;
	
					}
					
					String sourcePath = logger.getTestDirectory().getAbsolutePath()+"/in/";
					String targetPath = logger.getTestDirectory().getAbsolutePath()+"/out/eclipse/";
	//				String path = "pattttSOurce: ------------------"+logger.getTestDirectory().getAbsolutePath();
	//				
	//				System.out.println(path);
	//				System.out.println(path);
					
					if (ENABLE_REFACTORING_EXECUTION) {
						Refactoring refactoring = getRefactoring(icus);
						try{
						problemReport = checkRefactoringApplies(refactoring);
						if (problemReport.hasProblems()) {
							logger.logRefactoringInapplicable(problemReport, 0); // TODO - FUncao espera inteiro. Adicionei 0
							safiraResults.append("WARNING: "+counter+"\n");
							tearDownProject();
//							continue;
						}
						}catch(java.lang.NullPointerException e) {
//							logger.logNullPointer(); TODO Funcao "logNullPointer" nao existe
						}
						try {
						// faz a refatoração e verifica se teve problema
						long tmpStart = System.currentTimeMillis();
						problemReport = performRefactoring(refactoring);
						long tmpStop = System.currentTimeMillis();
						long tmpTotal = tmpStop - tmpStart;
						logger.setRefactoringTime(logger.getRefactoringTime() + tmpTotal);
						if (problemReport.hasProblems()) {
							logger.logRefactoringExecutionErrors(problemReport);
							tearDownProject();
//							continue;
						}
						} catch (Exception e) {
							continue;
						}
						// loga a refatoração no tmp/out
						logger.logPostRefactoring(packageRoot, 0); // TODO - FUncao espera inteiro. Adicionei 0
//						SRImpact c = null;
//						// checa se a refatoração compilou
						if (ENABLE_COMPILE_CHECK) {
							problemReport = checkCompilationProblems(packageRoot);
							if (problemReport.hasProblems()) {
								logger.logPostRefactorNotCompile(problemReport);
								safiraResults.append("CE: "+counter+"\n");
								tearDownProject();
								System.out.println("COMPILATION ERROR");
//								break;
//								continue;
							} else {
//								
//								
								checkBehaviorConditions3(logger.getTestDirectory());
								SRImpact sri = new SRImpact("",sourcePath , targetPath, "","0.5", "", true);
								System.out.println("is refactoring "+sri.isRefactoring());
								if (!sri.isRefactoring()) {
									logger.logBehavioralChange();
									System.out.println("BEHAVIORAL CHANGE");
									safiraResults.append("BC: "+counter+"\n");
									logger.logBC();
//									break;
//									continue;
								} else {
									logger.logBUG();
									
								}
								}
						} 
						//bug de transformacao 2
						
						
//						AnalyzeTransformation a = new AnalyzeTransformation();
//						if (a.isTransformationBug(sourcePath, targetPath, "fieldid_0", "ClassId_1", "ClassId_0")) {
//							logger.logTransformationBug(a.getProblem());
//						}
//						System.out.println(a.getProblem());
						
						
						//bug de transformacao ====================
//						 String programpath = "C:/Users/Felipe/Documents/workspace6/program_counter/alloyTheory/pull_up_method_instance.als";
//						 String specPath = targetPath;
//						 PullUpMethod p = new PullUpMethod();
//						 
//						 try {
//							BuildSpecification be = new BuildSpecification(specPath,programpath, 
//									 "javametamodel_nofield", p,
//										2, 3, 4, 0, 3, 3,4);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						 Analyzer a = new Analyzer(programpath);
//						 System.out.println(a.alloyAnalyzer());
//						 if (a.alloyAnalyzer()) {
//							 System.out.println("bug");
//							 break;
//						 }
						 //=====================================
					}
					//ate aqui
				}
				tearDownProject();
				
			
			//createProject();
//		}
		}
		logger.stop(srLogger);
//		experimentTime = System.currentTimeMillis() - experimentTime;
////		safiraResults.append(experimentTime);
////		srResults.append(experimentTime);
//		StringBuffer time = new StringBuffer();
//		time.append("time: "+experimentTime);
//		logResults(file, time, safiraResults.toString());
//		System.out.println("total time ----------------------");
//		System.out.println("programas gerados"+ counter);
//		System.out.println(safiraResults.toString());
//		System.out.println(experimentTime);
//		logResults(file, srResults, "sr_results");
//		logResults(file, srResults2, "sr_results2");
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
	
	public static List<Object> checkBehaviorConditionsSafira(
			SRImpact c, StringBuffer b, int counter) {

		ProblemReport pr = new ProblemReport();

		boolean isRefactoring = c.isRefactoring();

//		b.append(c.getFileIntersection().size() + " " + c.getGeneratedTests() + " "+
//				c.getAnalysisTime()+" "+ c.getTotalTime()+" "+isRefactoring + "\n");
		
		b.append(counter+" "+c.getTests() + " "+ c.getTotalTime()+" "+isRefactoring );

		if (!isRefactoring) {
			pr.addProblem("has behavior change");
		}
		
		Saferefactor s = new Saferefactor(logger
				.getTestDirectory() + "/in/",  logger.getTestDirectory() + "/out/eclipse", "", "", "");
		
//		b = decomposeMethod(s, c, b);
		List<Object> result = new ArrayList<Object>();
		result.add(pr);
		result.add(b);
		return result;
	}
	
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
	
	public static ProblemReport checkBehaviorConditions3(File testDirectory) {

		ProblemReport pr = new ProblemReport();

		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			AntRunner runner = new AntRunner();

			//propriedades
			Map<String, String> properties = new HashMap<String, String>();						
			properties.put("source", testDirectory + "/in/");
			properties.put("target", testDirectory + "/out/eclipse/");	
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
	public static void main(String[] args) {
		
		JDollyImp jDollyImp = new JDollyImp("C:/Users/Felipe/Documents/Projects/Java/experiments/alloy/program_counter/alloyTheoryNew/pullupmethod_final.als", 2, 3,
				4, 0); 
		jDollyImp.hasNext();
		List<CompilationUnit> current = jDollyImp.next();
		Runtime rt = Runtime.getRuntime(); 
		long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long freeMemory  = Runtime.getRuntime().maxMemory() - usedMemory;
		int mb = 1024 * 1024;
		System.out.println(freeMemory/mb);
		while (jDollyImp.hasNext()) {
			//entao a memoria livre total corresponde a memoria livre (free memory) + (max - total)
			current = jDollyImp.next();
			usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			 freeMemory  = Runtime.getRuntime().maxMemory() - usedMemory;
			 
			 long freeMemory_perc = 100* freeMemory/Runtime.getRuntime().maxMemory();
			System.out.println(freeMemory/mb);
			System.out.println((Runtime.getRuntime().freeMemory()+ Runtime.getRuntime().maxMemory()- Runtime.getRuntime().totalMemory())/mb);
			System.out.println(Runtime.getRuntime().maxMemory()/mb);
			System.out.println(freeMemory_perc+"%");
		}
	}
}
