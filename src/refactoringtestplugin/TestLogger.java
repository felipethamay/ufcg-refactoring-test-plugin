package refactoringtestplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;







import AST.Program;
import AST.TypeDecl;
//import AST.Program;
//import AST.TypeDecl;
import br.edu.ufcg.saferefactor.refactoring.Constants;
import refactoringtestplugin.classes.CopyOfRefactoringTest;
import refactoringtestplugin.classes.DTTechniqueExhastive;
import refactoringtestplugin.classes.DTTechniqueOneToolTTFF;
import refactoringtestplugin.classes.DTTechniqueOnlyOneToolExhastive;
import refactoringtestplugin.classes.MTTechniqueExhastive;
import refactoringtestplugin.classes.MTTechniqueTTFF;
import refactoringtestplugin.classes.RefactoringTest;
import refactoringtestplugin.classes.RefactoringTestDTtestandoEclipseComJRRT;
import refactoringtestplugin.classes.RefactoringTestOriginal;
import refactoringtestplugin.classes.RefactoringTestRealSystems;
import refactoringtestplugin.classes.RefactoringTestWithoutJDolly;
import refactoringtestplugin.classes.RefactoringTest_;
import refactoringtestplugin.classes.RefactoringTest_testsEclipse;



/**
 * Logs refactoring test results to the console and a directory structure.
 * 
 * The directory structure has the following format:
 * 
 * <pre>
 * {logDir}N/{SUMMARY_FILE_NAME}
 *           {TEST_NAME_BASE}M/
 *                            in/
 *                              {compilationUnit}.java
 *                            out/
 *                               {compilationUnit}.java
 *                               {SUCCESSFUL_REFACTORING_LOG_FILE}
 *                               {PRE_REFACTOR_NOT_COMPILE_LOG_FILE}
 *                               {REFACTORING_INAPPLICABLE_LOG_FILE}
 *                               {REFACTORING_EXECUTION_ERROR_LOoutputToLogFileG_FILE}
 *                               {POST_REFACTOR_NOT_COMPILE_LOG_FILE}
 *                               {POSTCONDITION_FAILURE_LOG_FILE}
 * </pre>
 * 
 * Where {logDir} is the directory passed into the constructor (usually from
 * {@link RefactoringTest_#getLogDirPath()}), N and M are unique numbers to
 * ensure that multiple runs do not overwrite each other, and other curly-braced
 * values are those defined in the class.
 */
public class TestLogger {
	private static final String TEST_NAME_BASE = "test";
	private static final String PRE_REFACTOR_NOT_COMPILE_LOG_FILE = "PRE_REFACTOR_NOT_COMPILE";
	private static final String REFACTORING_INAPPLICABLE_LOG_FILE = "REFACTORING_INAPPLICABLE";
	private static final String REFACTORING_EXECUTION_ERROR_LOG_FILE = "REFACTORING_EXECUTION_ERROR";
	private static final String POST_REFACTOR_NOT_COMPILE_LOG_FILE = "POST_REFACTOR_NOT_COMPILE";
	private static final String POSTCONDITION_FAILURE_LOG_FILE = "POSTCONDITION_FAILURE";
	// gustavo
	private static final String BEHAVIORCHANGE_LOG_FILE = "BEHAVIORAL_CHANGE";
	private static final String BEHAVIORCHANGE_LOG_FILE_SAFIRA = "BEHAVIORCHANGE_FAILURE_SAFIRA";
	private static final String BEHAVIORCHANGE_LOG_FILE_SR2 = "BEHAVIORCHANGE_FAILURE_SR2";
	private static final String BEHAVIORCHANGE_LOG_FILE2 = "BEHAVIORCHANGE_FAILURE2";
	private static final String SUCCESSFUL_REFACTORING_LOG_FILE = "SUCCESSFUL_REFACTORING";
	private static final String SUMMARY_FILE_NAME = "SUMMARY";
	private List<String> programsCE = new ArrayList<String>();;
	private List<String> programsBC = new ArrayList<String>();;
	private int generatedCount = 0;
	private int preRefactorNotCompileCount = 0;
	private int refactoringInapplicableCount = 0;
	private int refactoringExecutionErrorCount = 0;
	private int postRefactoringNotCompileCount = 0;
	private int postconditionFailureCount = 0;
	private int successfulRefactorings = 0;
	// gustavo
	private int behaviorChangeCount = 0;
	private int behaviorChangeCount2 = 0;
	private long start;
	private long stop;
	Map<String, Integer> problems = new HashMap<String, Integer>();
	private File logDir;
	private String refactoringName;
	private String generatorName;
	private double menorCobertura = 0; 
	private double maiorCobertura = 0; 
	private long refactoringTime = 0;
	private List<Double> coberturas = new ArrayList<Double>();
	public String currentSource;
	public String currentTarget;
	private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private double currentCobertura;
	public int dir = 0;
	public static String packageName;

	public TestLogger(String refactoringName, String generatorName) {
		this.refactoringName = refactoringName;
		this.generatorName = generatorName;
	}

	public TestLogger(String refactoringName, String generatorName,
			String logDirPath) {
		this(refactoringName, generatorName);
		this.logDir = createUniqueLogDir(logDirPath, 0);
	}

	private File createUniqueLogDir(String logDirPathBase, int suffix) {
		String logDirPath = logDirPathBase + suffix;
		File uniqueLogDir = new File(logDirPath);
		if (uniqueLogDir.exists()) {
			return createUniqueLogDir(logDirPathBase, suffix + 1);
		}
		info("Creating log directory " + uniqueLogDir.getAbsolutePath());
		if (!uniqueLogDir.mkdirs()) {
			throw new RuntimeException(String.format(
					"Log directory %s could not be created",
					uniqueLogDir.getAbsolutePath()));
		}
		return uniqueLogDir;
	}

	public boolean fileOutputIsEnabled() {
		return logDir != null;
	}

	public int getGeneratedCount() {
		return generatedCount;
	}

	public void start(DTTechniqueExhastive dtTechniqueExhastive) {
		info("Starting " + dtTechniqueExhastive.getClass().getName().toString());
		info("Description: " + dtTechniqueExhastive.getDescription());
		start = System.currentTimeMillis();
	}

	
	public void logGenerated(CompilationUnit cu, String cuName) {
		generatedCount++;
		info("==================================");
		String cuSource = cu.toString();
		info(String.format("Input %d before refactoring:\n%s", generatedCount,
				cuSource));
		outputCompilationUnitFile("in", cuSource, cuName);
	}

	public void logGeneratedIn(List<ICompilationUnit> icus) throws JavaModelException {
		
		info("==================================");
		String cuSources = "";
		for (ICompilationUnit cu : icus) {
			String cuName = cu.getElementName(); 
			String cuSource = cu.getSource();
			try {
				cuSource = cu.getSource();
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String packageName = "";
			if (cu.getPackageDeclarations() != null) {
				if (cu.getPackageDeclarations().length > 0) {
					packageName = cu.getPackageDeclarations()[0].getElementName();
				}
				
			}
			
			
			outputCompilationUnitFile("in/" + packageName, cuSource, cuName);
			cuSources = cuSources + cuSource;
		}
		info(String.format("Input %d before refactoring:\n%s", generatedCount,
				cuSources));
		currentSource = cuSources;

	}
	
	public void logGeneratedOut(List<ICompilationUnit> icus) throws JavaModelException {
		
		info("==================================");
		String cuSources = "";
		for (ICompilationUnit cu : icus) {
			String cuName = cu.getElementName(); 
			String cuSource = cu.getSource();
			try {
				cuSource = cu.getSource();
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String packageName = "";
			if (cu.getPackageDeclarations() != null) {
				if (cu.getPackageDeclarations().length > 0) {
					packageName = cu.getPackageDeclarations()[0].getElementName();
				}
				
			}
			
			
			outputCompilationUnitFile("out/eclipse/" + packageName, cuSource, cuName);
			cuSources = cuSources + cuSource;
		}
		info(String.format("Input %d before refactoring:\n%s", generatedCount,
				cuSources));
		currentSource = cuSources;

	}
	
public void logGenerated(List<CompilationUnit> icus, String source) {
		
		info("==================================");
		String cuSources = "";
		for (CompilationUnit cu : icus) {
			String cuName = getPrimaryTypeName(cu);
			String cuSource = source;
			packageName = "";
			if (cu.getPackage() != null) {
				packageName = cu.getPackage().getName()
						.getFullyQualifiedName();
			}
			
			
			outputCompilationUnitFile("in/" + packageName, cuSource, cuName);
			cuSources = cuSources + cuSource;
		}
		info(String.format("Input %d before refactoring:\n%s", generatedCount,
				cuSources));
		currentSource = cuSources;

	}

	public void logGenerated(List<CompilationUnit> icus) {
		
		info("==================================");
		String cuSources = "";
		for (CompilationUnit cu : icus) {
			String cuName = getPrimaryTypeName(cu);
			String cuSource = cu.toString();
			 packageName = "";
			if (cu.getPackage() != null) {
				packageName = cu.getPackage().getName()
						.getFullyQualifiedName();
			}
			
			
			outputCompilationUnitFile("in/" + packageName, cuSource, cuName);
			cuSources = cuSources + cuSource;
		}
		info(String.format("Input %d before refactoring:\n%s", generatedCount,
				cuSources));
		currentSource = cuSources;

	}
	
	public void logGeneratedWithTests(IPackageFragmentRoot packageRoot) 
		throws JavaModelException {
			info(String.format("Input %d with tests before refactoring", generatedCount));
			IPackageFragment packFrag;
			ICompilationUnit icu;
			String cuSources = "";
			for (IJavaElement element : packageRoot.getChildren()) {
				if (element instanceof IPackageFragment) {
					packFrag = (IPackageFragment) element;
					String packFragName = packFrag.getElementName();
					for (IJavaElement element2 : packFrag.getChildren()) {
						if (element2 instanceof ICompilationUnit) {
							icu = (ICompilationUnit) element2;
							String cuSource = icu.getSource();
							String cuName = icu.getElementName();

							info(cuName + "\n" + cuSource);
							cuSources = cuSources + "\n" + cuSource;
							outputCompilationUnitFile(
									"in/completo/" + packFragName, cuSource, cuName);
						}
					}
				}

			}
			currentTarget = cuSources;

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
			}else if (type instanceof EnumDeclaration) {
            	
           	 String typeName = ((EnumDeclaration) type).getName().toString();
           	 if (first == null) {
                    first = typeName;
                }
                for (Object modifier : ((EnumDeclaration) type).modifiers()) {
                    if (((Modifier) modifier).getKeyword() == Modifier.ModifierKeyword.PUBLIC_KEYWORD) {
                        return typeName;
                    }
                }
           }
		}
		return first;
	}

	public void logPreRefactorNotCompile(ICompilationUnit icu,
			ProblemReport problemReport) {
		preRefactorNotCompileCount++;
		warn("Generated input does not compile");

		for (String problem : problemReport.getProblems()) {
			warn(problem);
		}
		outputToLogFile(PRE_REFACTOR_NOT_COMPILE_LOG_FILE,
				problemReport.getProblems());
	}

	public String logRefactoringInapplicable(ProblemReport problemReport, int dir) {
		refactoringInapplicableCount++;
		warn("Refactoring does not apply");
		String completeMessage = "";
		for (String problem : problemReport.getProblems()) {
			warn(problem);
			completeMessage += problem+" ";
		}
		outputToLogFile(REFACTORING_INAPPLICABLE_LOG_FILE,
				problemReport.getProblems(), dir);
		return completeMessage;
	}
	
	public void log(String problemReport, String name) {
		outputToLogFileJRRT(problemReport,
				logDir+"/"+ TEST_NAME_BASE + generatedCount+"/out/jrrt/"+name+".txt", dir);
	}

	public void logRefactoringExecutionErrors(ProblemReport problemReport) {
		refactoringExecutionErrorCount++;
		fail("Error(s) encountered during refactoring execution");

		for (String problem : problemReport.getProblems()) {
			fail(problem);
		}
		outputToLogFile(REFACTORING_EXECUTION_ERROR_LOG_FILE,
				problemReport.getProblems());
	}

	public void logPostRefactoring(IPackageFragmentRoot packageRoot, int dir)
			throws JavaModelException {
		info(String.format("Input %d after refactoring", generatedCount));
		IPackageFragment packFrag;
		ICompilationUnit icu;
		String cuSources = "";
		for (IJavaElement element : packageRoot.getChildren()) {
			if (element instanceof IPackageFragment) {
				packFrag = (IPackageFragment) element;
				String packFragName = packFrag.getElementName();
				for (IJavaElement element2 : packFrag.getChildren()) {
					if (element2 instanceof ICompilationUnit) {
						icu = (ICompilationUnit) element2;
						String cuSource = icu.getSource();
						String cuName = icu.getElementName();

						info(cuName + "\n" + cuSource);
						cuSources = cuSources + "\n" + cuSource;
						if (dir == 0) {
							outputCompilationUnitFile(
									"out/eclipse/" + packFragName, cuSource, cuName);
						} else 
						outputCompilationUnitFile(
								"out/eclipse"+dir+"/" + packFragName, cuSource, cuName);
					}
				}
			}

		}
		currentTarget = cuSources;
	}
	
	public void logPostRefactoringWithTests(IPackageFragmentRoot packageRoot)
			throws JavaModelException {
		info(String.format("Input %d with tests after refactoring", generatedCount));
		IPackageFragment packFrag;
		ICompilationUnit icu;
		String cuSources = "";
		for (IJavaElement element : packageRoot.getChildren()) {
			if (element instanceof IPackageFragment) {
				packFrag = (IPackageFragment) element;
				String packFragName = packFrag.getElementName();
				for (IJavaElement element2 : packFrag.getChildren()) {
					if (element2 instanceof ICompilationUnit) {
						icu = (ICompilationUnit) element2;
						String cuSource = icu.getSource();
						String cuName = icu.getElementName();

						info(cuName + "\n" + cuSource);
						cuSources = cuSources + "\n" + cuSource;
						outputCompilationUnitFile(
								"out/eclipse/completo/" + packFragName, cuSource, cuName);
					}
				}
			}

		}
		currentTarget = cuSources;
	}

	public void logPostRefactorNotCompile(ProblemReport problemReport) {
		postRefactoringNotCompileCount++;
		fail("Unit does not compile after refactoring");

		String ce = "";
		for (String problem : problemReport.getProblems()) {
			fail(problem);
			String template = problem.replaceAll(
					" [a-zA-Z0-9]+_[0-9][(]?[\\w]*[)]?", " ");
		}
		// System.out.println(x);
		if (problems.containsKey(ce)) {
			Integer integer = problems.get(ce);
			integer = integer + 1;
			problems.put(ce, integer);
		} else {
			// System.out.println(test);
			problems.put(ce, 1);
		}

		outputToLogFile(POST_REFACTOR_NOT_COMPILE_LOG_FILE,
				problemReport.getProblems());
		logProgramCE(problemReport.getProblems());
		
	}
	
	

	private static void eclipseOracle(Map<String, Integer> problems, File test,
			File target) {
		try {
			FileReader in = new FileReader(target);
			BufferedReader br = new BufferedReader(in);
			String s;

			// pega s� o 1o. erro
			// s = br.readLine();
			// s = s.replaceAll(
			// " [a-zA-Z0-9]+_[0-9][(]?[\\w]*[)]?", " ");
			// System.out.println("----------------------------------------------");
			// System.out.println(test);
			// System.out.println("----------------------------------------------");
			// pega todos os errors
			String x = "";
			while ((s = br.readLine()) != null) {
				s = s.replaceAll(" [a-zA-Z0-9]+_[0-9][(]?[\\w]*[)]?", " ");
				x = x + "\n" + s;
			}
			// System.out.println(x);
			if (problems.containsKey(x)) {
				Integer integer = problems.get(x);
				integer = integer + 1;
				problems.put(x, integer);
			} else {
				// System.out.println(test);

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

	public void logPostconditionFailure(ProblemReport problemReport) {
		postconditionFailureCount++;
		fail("Custom post-condition failure(s) found");

		for (String problem : problemReport.getProblems()) {
			fail(problem);
		}
		outputToLogFile(POSTCONDITION_FAILURE_LOG_FILE,
				problemReport.getProblems());
	}

	public void logSuccessfulRefactoring() {
		outputToLogFile(SUCCESSFUL_REFACTORING_LOG_FILE, null);
		successfulRefactorings++;
	}
	
	public void logSomeThing(String thing) {
		outputToLogFile(thing, null);
	}

	public void stop(SafeRefactorLogger srLogger) {
		stop = System.currentTimeMillis();
		outputSummary(srLogger);
		logProgramsBC();
		logProgramsCE();
		
	}
	
	public void logProgramsCE() {
		StringBuffer sb = new StringBuffer();

		for (String transformation : programsCE) {
			sb.append("<transformation>");
			sb.append(transformation);
			sb.append("<transformation>");
		}
		
		File summaryFile = new File(logDir, "programsCE");
		try {
			if (!summaryFile.createNewFile()) {
				throw new RuntimeException("Unable to create summary file");
			}
			FileWriter fw = new FileWriter(summaryFile, false);
			fw.write(sb.toString());			
			fw.flush();
			fw.close();
		} catch (IOException e) {
			throw new RuntimeException(e); // HACK
		}

	}

	public void logProgramsBC() {
		StringBuffer sb = new StringBuffer();

		for (String transformation : programsBC) {
			sb.append("<transformation>");
			sb.append(transformation);
			sb.append("<transformation>");
		}
		File summaryFile = new File(logDir, "programsBC");
		try {
			if (!summaryFile.createNewFile()) {
				throw new RuntimeException("Unable to create summary file");
			}
			FileWriter fw = new FileWriter(summaryFile, false);
			fw.write(sb.toString());			
			fw.flush();
			fw.close();
		} catch (IOException e) {
			throw new RuntimeException(e); // HACK
		}

	}
	

	public void logProgramCE(Iterable<String> iterable) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n<compilation error>\n");
		for (String string : iterable) {
			sb.append(iterable);
		}		
		sb.append("\n<compilation error>\n");
		sb.append("\n<source>\n");
		sb.append(currentSource);
		sb.append("\n<source>\n");
		sb.append("\n<target>\n");
		sb.append(currentTarget);
		sb.append("\n<target>\n");
		programsCE.add(sb.toString());
		
	}

	private static class Result {
		public String name;
		public String value;

		public Result(String name, Object value) {
			this.name = name;
			this.value = value.toString();
		}
	}

	private void outputSummary(SafeRefactorLogger srLogger) {
		LinkedList<Result> results = new LinkedList<Result>();
		results.add(new Result("Refactoring", refactoringName));
		results.add(new Result("Primary Generator", generatorName));		
		results.add(new Result("Total Generated", generatedCount));
		results.add(new Result("Time for applying the refactoring", this.refactoringTime /1000));
		float minutes = (stop - start) / 1000F;
		results.add(new Result("Time", minutes));
		double hours = (double) minutes / 60 / 60;
		results.add(new Result("Time in hour", hours));
		
		results.add(new Result("Compilable Inputs", generatedCount
				- preRefactorNotCompileCount));
		// results.add(new Result("Pre-Refactoring Compilation Failures",
		// preRefactorNotCompileCount));
		results.add(new Result("WarningStatus (Ecl)",
				refactoringInapplicableCount));
		
		// results.add(new Result("Successful Refactorings",
		// successfulRefactorings));
		// results.add(new Result("Refactoring Execution Errors",
		// refactoringExecutionErrorCount));
		results.add(new Result("DoesNotCompile (Failures)",
				postRefactoringNotCompileCount));
		results.add(new Result("DoesNotCompile (Bugs)",
				problems.size()));
		
		
		
		results.add(new Result("BehavioralChange (Ecl)", behaviorChangeCount));
		
		results.add(new Result("Compilation error bugs", problems.toString()));
		results.add(new Result("Safe Refactor results", srLogger.getResults()));
		
		
 
		double somaCoberturas = 0;
		for (Double cobertura : coberturas) {
			somaCoberturas = somaCoberturas + cobertura;
		}
		double porcentagemCobertura = somaCoberturas / coberturas.size();		
		results.add(new Result("Menor Cobertura", menorCobertura));
		results.add(new Result("Maior Cobertura", maiorCobertura));
		results.add(new Result("Test coverage", porcentagemCobertura));
		outputConsoleSummary(results);
		outputSummaryLogFile(results);
	}

	private void outputConsoleSummary(LinkedList<Result> results) {
		for (Result result : results) {
			System.out.print(result.name);
			System.out.print(": ");
			System.out.println(result.value);
		}
	}

	public void info(String info) {
		System.out.println("INFO: " + info);
	}

	public void warn(String warning) {
		System.out.println("WARNING: " + warning);
	}

	public void fail(String failure) {
		System.out.println("FAILURE: " + failure);
	}

	
	
	protected void outputToLogFile(String logFileName, Iterable<String> messages) {
		if (!fileOutputIsEnabled()) {
			return;
		}
		File logFile;
		if (dir == 0) {
			logFile = new File(getInOutDir("out/eclipse"), logFileName);
		} else {
			logFile = new File(getInOutDir("out/eclipse"+dir), logFileName);
		}
		try {
			if (!logFile.createNewFile()) {
				throw new RuntimeException(String.format(
						"Log file %s could not be created",
						logFile.getAbsolutePath()));
			}
			FileWriter fw = new FileWriter(logFile);
			if (messages != null) {
				for (String message : messages) {
					fw.write(message);
					fw.write('\n');
				}
			}
			fw.flush();
			fw.close();

		} catch (Exception e) {
			//throw new RuntimeException(e);
		}
	}
	
	protected void outputToLogFile(String logFileName, Iterable<String> messages, int dir) {
		if (!fileOutputIsEnabled()) {
			return;
		}
		File logFile;
		if (dir != 0)
			logFile = new File(getInOutDir("out/eclipse"+dir), logFileName);
		else 
			logFile = new File(getInOutDir("out/eclipse"), logFileName);
		try {
			if (!logFile.createNewFile()) {
				throw new RuntimeException(String.format(
						"Log file %s could not be created",
						logFile.getAbsolutePath()));
			}
			FileWriter fw = new FileWriter(logFile);
			if (messages != null) {
				for (String message : messages) {
					fw.write(message);
					fw.write('\n');
				}
			}
			fw.flush();
			fw.close();

		} catch (Exception e) {
		//	throw new RuntimeException(e);
		}
	}

	protected void outputToLogFileJRRT(String messages, String path, int dir) {
		if (!fileOutputIsEnabled()) {
			return;
		}
		File logFile;
		logFile = new File(path);
		try {
			if (!logFile.createNewFile()) {
				throw new RuntimeException(String.format(
						"Log file %s could not be created",
						logFile.getAbsolutePath()));
			}
			FileWriter fw = new FileWriter(logFile);
			if (messages != null) {
					fw.write(messages);
			}
			fw.flush();
			fw.close();

		} catch (Exception e) {
		//	throw new RuntimeException(e);
		}
	}
	
	protected void outputCompilationUnitFile(String inOutDirName,
			String source, String cuName, int skip) {
		if (!fileOutputIsEnabled()) {
			return;
		}

		File inOutDir = getInOutDir(inOutDirName, skip);
		if (!cuName.endsWith(".java")) {
			cuName = cuName + ".java";
		}
		File cuFile = new File(inOutDir, cuName); // ../<compilation_unit_name>.java
		try {
			if (!cuFile.createNewFile()) {
				throw new RuntimeException(String.format(
						"Compilation unit file %s could not be created",
						cuFile.getAbsolutePath()));
			}
			FileWriter fw = new FileWriter(cuFile);
			fw.write(source);
			fw.flush();
			fw.close();
		} catch (Exception e) {
		//	throw new RuntimeException(e); // HACK
		}
	}

	protected File getInOutDir(String inOutDirName, int skip) {
		if (!fileOutputIsEnabled()) {
			return null;
		}
		File inOutDir = new File(getTestDirectoryjrrt(skip), inOutDirName); // <temp_dir>/<refactoring>/<test_dir>/(in|out)
		if (!inOutDir.exists()) {
			if (!inOutDir.mkdirs()) {
				throw new RuntimeException(String.format(
						"In/Out directory %s could not be created",
						inOutDir.getAbsolutePath()));
			}
		}
		return inOutDir;
	}

	public void increaseCount() {
		generatedCount++;
	}
	
	public File getTestDirectory() {
		
		if (!fileOutputIsEnabled()) {
			return null;
		}
		File curTestDir = new File(logDir, TEST_NAME_BASE + generatedCount);
		if (!curTestDir.exists()) {
			if (!curTestDir.mkdirs()) {
				throw new RuntimeException(String.format(
						"Test directory %s could not be created",
						curTestDir.getAbsolutePath()));
			}
			info("Test directory created at " + curTestDir.getAbsolutePath());
		}
		return curTestDir;
	}

	private void outputSummaryLogFile(Iterable<Result> results) {
		if (!fileOutputIsEnabled()) {
			return;
		}
		File summaryFile = new File(logDir, SUMMARY_FILE_NAME);
		try {
			if (!summaryFile.createNewFile()) {
				throw new RuntimeException("Unable to create summary file");
			}
			FileWriter fw = new FileWriter(summaryFile);
			writeHeaders(results, fw);
			writeValues(results, fw);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			throw new RuntimeException(e); // HACK
		}
	}

	private void writeHeaders(Iterable<Result> results, FileWriter fw)
			throws IOException {
		fw.write("%%");
		boolean first = true;
		for (Result result : results) {
			if (!first) {
				fw.write(" & ");
			}
			fw.write("\\RHD{"); // the rotated header command in the TeX file
			fw.write(result.name);
			fw.write("}");
			first = false;
		}
		fw.write('\n');
	}

	private void writeValues(Iterable<Result> results, FileWriter fw)
			throws IOException {
		Boolean first2 = true;
		for (Result result : results) {
			if (!first2) {
				fw.write(" & ");
			}
			fw.write(result.value);
			first2 = false;
		}
		fw.write("\\\\\n");
	}

	public void logBehaviorConditionFailure(ProblemReport problemReport) {
		
		behaviorChangeCount++;
		fail("Behavior Change found");

		for (String problem : problemReport.getProblems()) {
			fail(problem);
		}
		outputToLogFile(BEHAVIORCHANGE_LOG_FILE, problemReport.getProblems());
		logProgramBC();
		

	}
	
	public void logBehaviorConditionFailureSafira(ProblemReport problemReport) {
		
		behaviorChangeCount++;
		fail("Behavior Change found");

		for (String problem : problemReport.getProblems()) {
			fail(problem);
		}
		outputToLogFile(BEHAVIORCHANGE_LOG_FILE_SAFIRA, problemReport.getProblems());
		logProgramBC();
		

	}
	
	public void logBehaviorConditionFailureSR2(ProblemReport problemReport) {
		
		behaviorChangeCount++;
		fail("Behavior Change found");

		for (String problem : problemReport.getProblems()) {
			fail(problem);
		}
		outputToLogFile(BEHAVIORCHANGE_LOG_FILE_SR2, problemReport.getProblems());
		logProgramBC();
		

	}
	
public void logBehaviorConditionFailure2(ProblemReport problemReport) {
		
		behaviorChangeCount2++;
		fail("Behavior Change found");

		for (String problem : problemReport.getProblems()) {
			fail(problem);
		}
		outputToLogFile(BEHAVIORCHANGE_LOG_FILE2, problemReport.getProblems());
		//logProgramBC();
		

	}
	
	public void logProgramBC() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n<source>\n");
		sb.append(currentSource);
		sb.append("\n<source>\n");
		sb.append("\n<target>\n");
		sb.append(currentTarget);
		sb.append("\n<target>\n");
		programsBC.add(sb.toString());
	}

	public void logCoverage() {
		

//		dbf.setNamespaceAware(false);
//
//		DocumentBuilder docBuilder;
//		try {
//			docBuilder = dbf.newDocumentBuilder();
//			Document docSource = docBuilder.parse(new File(Constants.TEST + "/coverage/coverage.xml"));
//
//		
//			double cl = Integer.parseInt(docSource.getDocumentElement().getAttribute("lines-covered"));
////			System.out.println(coveredLines);
//			double vl = Integer.parseInt(docSource.getDocumentElement().getAttribute("lines-valid"));
//
//			double pc = cl / vl;
//			currentCobertura = pc;
//			if (menorCobertura == 0)
//				menorCobertura = pc;
//			if (menorCobertura > pc)
//				menorCobertura = pc;
//			if (maiorCobertura == 0)
//				maiorCobertura = pc;
//			if (maiorCobertura < pc)
//				maiorCobertura = pc;
//			coberturas.add(pc);
//			//			System.out.println(validLines);
//			
//			
//
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		
	}

	public long getRefactoringTime() {
		return refactoringTime;
	}

	public void setRefactoringTime(long refactoringTime) {
		this.refactoringTime = refactoringTime;
	}
//
//	public void start(RefactoringTestGen test) {
//		info("Starting " + test.getClass().getName().toString());
//		info("Description: " + test.getDescription());
//		start = System.currentTimeMillis();
//		
//	}

//	public void logPostRefactoring(Program target, int dir, int skip) {
//		info(String.format("Input %d after refactoring", generatedCount));
//		AST.List<AST.CompilationUnit> compilationUnits = target
//				.getCompilationUnits();
//		String cuSources = "";
//		for (AST.CompilationUnit compilationUnit : compilationUnits) {
//			if (compilationUnit.fromSource()) {
//				String packageDecl = compilationUnit.getPackageDecl();
//
//				// File pacote = new File(path + packageDecl);
//				// pacote.mkdir();
//
//				TypeDecl fileName = compilationUnit.getTypeDecl(0);
//				String javaContent = compilationUnit.toString();
//				cuSources = cuSources + "\n" + javaContent;
//
//				
//				if (dir == 0) {
//					outputCompilationUnitFile("out/jrrt/" + packageDecl,
//							javaContent, fileName.getID());
//				} else {
//					outputCompilationUnitFile("out/jrrt"+dir+"/" + packageDecl,
//							javaContent, fileName.getID());
//				}
//			}
//		}
//		currentTarget = cuSources;
//	}



	public File getTestDirectoryjrrt(int skip) {
		if (!fileOutputIsEnabled()) {
			return null;
		}
		File curTestDir = new File(logDir, TEST_NAME_BASE + generatedCount+"/out/jrrt/");
		if (!curTestDir.exists()) {
			if (!curTestDir.mkdirs()) {
				throw new RuntimeException(String.format(
						"Test directory %s could not be created",
						curTestDir.getAbsolutePath()));
			}
			info("Test directory created at " + curTestDir.getAbsolutePath());
		}
		return curTestDir;
	}
	public void logPostRefactoring(Program target, int dir) {
		info(String.format("Input %d after refactoring", generatedCount));
		AST.List<AST.CompilationUnit> compilationUnits = target
				.getCompilationUnits();
		String cuSources = "";
		for (AST.CompilationUnit compilationUnit : compilationUnits) {
			if (compilationUnit.fromSource()) {
				String packageDecl = compilationUnit.getPackageDecl();

				// File pacote = new File(path + packageDecl);
				// pacote.mkdir();

				TypeDecl fileName = compilationUnit.getTypeDecl(0);
				String javaContent = compilationUnit.toString();
				cuSources = cuSources + "\n" + javaContent;

				
				if (dir == 0) {
					outputCompilationUnitFile("out/jrrt/" + packageDecl,
							javaContent, fileName.getID());
				} else {
					outputCompilationUnitFile("out/jrrt"+dir+"/" + packageDecl,
							javaContent, fileName.getID());
				}
			}
		}
		currentTarget = cuSources;
	}

	protected File getInOutDir(String inOutDirName) {

		File inOutDir = new File(getTestDirectory(), inOutDirName); // <temp_dir>/<refactoring>/<test_dir>/(in|out)
		if (!inOutDir.exists()) {
			if (!inOutDir.mkdirs()) {
				throw new RuntimeException(String.format(
						"In/Out directory %s could not be created",
						inOutDir.getAbsolutePath()));
			}
		}
		return inOutDir;
	}


	protected String outputCompilationUnitFile(String inOutDirName,
			String source, String cuName) {

		File inOutDir = getInOutDir(inOutDirName);
		if (!cuName.endsWith(".java")) {
			cuName = cuName + ".java";
		}
		File cuFile = new File(inOutDir, cuName); // ../<compilation_unit_name>.java
		if (cuFile.exists())
			cuFile.delete();
		try {
			if (!cuFile.createNewFile()) {
				throw new RuntimeException(String.format(
						"Compilation unit file %s could not be created",
						cuFile.getAbsolutePath()));
			}
			FileWriter fw = new FileWriter(cuFile);
			fw.write(source);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // HACK
		}
		return inOutDir.toString();
	}

	public void start(DTTechniqueOneToolTTFF dtTechniqueOneToolTTFF) {
		// TODO Auto-generated method stub
		
	}

	public void start(DTTechniqueOnlyOneToolExhastive dtTechniqueOnlyOneToolExhastive) {
		// TODO Auto-generated method stub
		
	}

	public void start(MTTechniqueExhastive mtTechniqueExhastive) {
		// TODO Auto-generated method stub
		
	}

	public void start(MTTechniqueTTFF mtTechniqueTTFF) {
		// TODO Auto-generated method stub
		
	}

	public void start(RefactoringTest_ refactoringTest_) {
		// TODO Auto-generated method stub
		
	}

	public void start(RefactoringTest_testsEclipse refactoringTest_testsEclipse) {
		// TODO Auto-generated method stub
		
	}

	public void logBehavioralChange() {
		// TODO Auto-generated method stub
		
	}

	public void start(RefactoringTest refactoringTest) {
		// TODO Auto-generated method stub
		
	}

	public void start(RefactoringTestDTtestandoEclipseComJRRT refactoringTestDTtestandoEclipseComJRRT) {
		// TODO Auto-generated method stub
		
	}

	public void start(RefactoringTestOriginal refactoringTestOriginal) {
		// TODO Auto-generated method stub
		
	}

	public void start(RefactoringTestRealSystems refactoringTestRealSystems) {
		// TODO Auto-generated method stub
		
	}

	public void start(RefactoringTestWithoutJDolly refactoringTestWithoutJDolly) {
		// TODO Auto-generated method stub
		
	}

	public void logBC() {
		// TODO Auto-generated method stub
		
	}

	public void logBUG() {
		// TODO Auto-generated method stub
		
	}

	public void start(refactoringtestplugin.RefactoringTest refactoringTest) {
		// TODO Auto-generated method stub
		
	}



}