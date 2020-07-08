package refactoringtestplugin.analyzeResultsTB;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import io.InputManager;
import io.InputManagerASCII;
import refactoringtestplugin.encapsulatefield.AnalyzeTransformation;


public class RunJRRTInfo {
	
	static final String WARNING_MESSAGE = "REFACTORING_INAPPLICABLE";
	static final String PRE_REFACTOR_NOT_COMPILE = "PRE_REFACTOR_NOT_COMPILE"; 
	static final String REFACTOR_EXECUTION_ERROR = "REFACTORING_EXECUTION_ERROR";
	static final String COMPILATION_ERROR = "POST_REFACTOR_NOT_COMPILE";
	static final String BEHAVIORAL_CHANGE = "BEHAVIORAL CHANGE";
	static final String NO_ERROR = "NO_ERROR";
	static final String TRANSFORMATION_BUG = "TRANSFORMATION_BUG";
	
public static String checkMessage(String source,String path, int i ) throws IOException {
		boolean ok = true;
		File f = new File(path);
		if (f.isDirectory()) {
			String[] list = f.list();
			for (String string : list) {
				if (string.contains(WARNING_MESSAGE) || string.contains(PRE_REFACTOR_NOT_COMPILE) ||
						string.contains(REFACTOR_EXECUTION_ERROR) || string.contains(COMPILATION_ERROR)) {
					ok = false;
					break;
				} else if(string.contains(TRANSFORMATION_BUG)) {
					File file = new File(path+"/"+string);
					file.delete();
				}
			}
//				if (string.contains("ENGINE_CRASH")) {
//					InputManager in = new InputManagerASCII(path + "/"+string);
//					in.openFile();
//					String line = in.readLine();
//					if (line.contains("Could not initialize class org.eclipse.jdt.internal.core.JavaModelManager")) {
						if (ok) {
							AnalyzeTransformation a = new AnalyzeTransformation();
							if (a.isTransformationBug(source, path, "fieldid_0", "ClassId_1", "ClassId_0")) {
								logTransformationBug(path,a.getProblem());
							}
						}
//					}
//				} 
//			}
		}
		return path;
		
		
	}

public static void logTransformationBug(String path, String problem) {

	outputToLogFileS(path, "TRANSFORMATION_BUG",
			problem);
//	logProgramCE(problemReport.getProblems());
	
}

protected static void outputToLogFileS(String path, String logFileName, String messages) {

	File logFile = new File(path, logFileName);
	try {
//		if (!logFile.createNewFile()) {
//			throw new RuntimeException(String.format(
//					"Log file %s could not be created",
//					logFile.getAbsolutePath()));
//		}
		FileWriter fw = new FileWriter(logFile);
		if (messages != null) {
				fw.write(messages);
				fw.write('\n');
		}
		fw.flush();
		fw.close();

	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}
public static void main(String[] args) throws IOException {
	String path = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/EncapsulateField_skip25/Alloy/";
	String source = "";
	File f = new File(path);
	String[] list = f.list();
	
	String out = "";
	for (int i = 1; i < list.length; i++) {
//		System.out.println(i);
		source = path +"test"+i+"/in/";
		File file = new File(path);
		if (file.exists()) {
		out = path + "test"+i+"/out/eclipse/";
		checkMessage(source, out, i);
		}
	}
}
	
}
