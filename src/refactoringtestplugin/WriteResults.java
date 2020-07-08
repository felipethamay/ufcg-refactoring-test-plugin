package refactoringtestplugin;


import io.OutputManager;
import io.OutputManagerASCII;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteResults {
	static final String WARNING_MESSAGE = "REFACTORING_INAPPLICABLE";
	static final String PRE_REFACTOR_NOT_COMPILE = "PRE_REFACTOR_NOT_COMPILE"; 
	static final String REFACTOR_EXECUTION_ERROR = "REFACTORING_EXECUTION_ERROR";
	static final String COMPILATION_ERROR = "POST_REFACTOR_NOT_COMPILE";
	static final String BEHAVIORAL_CHANGE = "BEHAVIORAL CHANGE";
	static final String NO_ERROR = "NO_ERROR";
	static final String TRANSFORMATION_BUG = "TRANSFORMATION_BUG";
	
	static List<Integer>  WARNING_MESSAGE_ = new ArrayList<Integer>();
	static List<Integer> PRE_REFACTOR_NOT_COMPILE_ = new ArrayList<Integer>(); 
	static List<Integer> REFACTOR_EXECUTION_ERROR_ = new ArrayList<Integer>();
	static List<Integer> COMPILATION_ERROR_ = new ArrayList<Integer>();
	static List<Integer> BEHAVIORAL_CHANGE_ = new ArrayList<Integer>();
	static List<Integer> NO_ERROR_ = new ArrayList<Integer>();
	static List<Integer> TRANSFORMATION_BUG_ = new ArrayList<Integer>();
	
	static String checkMessage(String path, int i ) {
		
		File f = new File(path);
		if (f.isDirectory()) {
			String[] list = f.list();
			for (String string : list) {
				if (string.contains(WARNING_MESSAGE)) {
					WARNING_MESSAGE_.add(i);
					return WARNING_MESSAGE;
				} else if (string.contains(PRE_REFACTOR_NOT_COMPILE)) {
					PRE_REFACTOR_NOT_COMPILE_.add(i);
					return PRE_REFACTOR_NOT_COMPILE;
				} else if (string.contains(REFACTOR_EXECUTION_ERROR)) {
					REFACTOR_EXECUTION_ERROR_.add(i);
					return REFACTOR_EXECUTION_ERROR;
				} else if (string.contains(COMPILATION_ERROR)) {
					COMPILATION_ERROR_.add(i);
					return COMPILATION_ERROR;
				}  else if (string.contains(BEHAVIORAL_CHANGE)) {
					BEHAVIORAL_CHANGE_.add(i);
					return BEHAVIORAL_CHANGE;
				} 
				 else if (string.contains(TRANSFORMATION_BUG)) {
					 TRANSFORMATION_BUG_.add(i);
						return TRANSFORMATION_BUG;
					} 
			}
		}
		NO_ERROR_.add(i);
		return NO_ERROR;
		
	}
	
	public static void writeResults(String path, String path2, int numberOfPrograms, String tool) {
		
		String out = "";
		for (int i = 1; i < numberOfPrograms; i++) {
			out = path + "test"+i+"/out/"+tool+"/";
			checkMessage(out, i);
		}
		OutputManager outfile = new OutputManagerASCII(path2);
		try {
			outfile.createFile();
			outfile.writeLine("REFACTORING_INAPPLICABLE\n");
			outfile.writeLine(WARNING_MESSAGE_.toString()+"\n");
			outfile.writeLine("PRE_REFACTOR_NOT_COMPILE\n");
			outfile.writeLine(PRE_REFACTOR_NOT_COMPILE_.toString()+"\n");
			outfile.writeLine("REFACTORING_EXECUTION_ERROR\n");
			outfile.writeLine(REFACTOR_EXECUTION_ERROR_.toString()+"\n");
			outfile.writeLine("POST_REFACTOR_NOT_COMPILE\n");
			outfile.writeLine(COMPILATION_ERROR_.toString()+"\n");
			outfile.writeLine("BEHAVIORAL CHANGE\n");
			outfile.writeLine(BEHAVIORAL_CHANGE_.toString()+"\n");
			outfile.writeLine("TRANSFORMATION_BUG\n");
			outfile.writeLine(TRANSFORMATION_BUG_.toString()+"\n");
			outfile.writeLine("NO_ERROR\n");
			outfile.writeLine(NO_ERROR_.toString()+"\n");
			outfile.closeFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
		String tool = "eclipse";
		
		String p = "C:/Users/Felipe/Documents/Projects/Java/experiments/bug_transf/results_exp/EncapsulateField_skip25/Alloy/";
//		p = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/jrrt/pullupmethod/skip1/";
		String p2 = "C:/Users/Felipe/Documents/Projects/Java/experiments/bug_transf/results_exp/EncapsulateField_skip25/results.txt";
//		p2 = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/jrrt/pullupmethod/results.txt";
		WriteResults.writeResults(p,p2,22022, tool);
	}
}
