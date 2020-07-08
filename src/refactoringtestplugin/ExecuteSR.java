package refactoringtestplugin;


import io.OutputManager;
import io.OutputManagerASCII;
import saferefactor.core.SRImpact;

import java.io.File;
import java.io.IOException;


public class ExecuteSR {

	static final String WARNING_MESSAGE = "REFACTORING_INAPPLICABLE";
	static final String PRE_REFACTOR_NOT_COMPILE = "PRE_REFACTOR_NOT_COMPILE"; 
	static final String REFACTOR_EXECUTION_ERROR = "REFACTORING_EXECUTION_ERROR";
	static final String COMPILATION_ERROR = "POST_REFACTOR_NOT_COMPILE";
	static final String BEHAVIORAL_CHANGE = "BEHAVIORAL CHANGE";
	
	static boolean checkMessage(String path) {
		
		File f = new File(path);
		if (f.isDirectory()) {
			String[] list = f.list();
			for (String string : list) {
				if (string.contains(WARNING_MESSAGE)) {
					return false;
				} else if (string.contains(PRE_REFACTOR_NOT_COMPILE)) {
					return false;
				} else if (string.contains(REFACTOR_EXECUTION_ERROR)) {
					return false;
				} else if (string.contains(COMPILATION_ERROR)) {
					return false;
				} 
			}
		}
		return true;
		
	}
	
	public static void logBehavioralChange(String path) {
		
		
		File f = new File(path);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void executeSR(String path, String path2) {
		
		String in = "";
		String out = "";
		long usedMemory = 0;
		long freeMemory = 0;
		long freeMemory_perc = 0;
		int mb = 1024 * 1024;
		StringBuffer sb = new StringBuffer();
		long timee = System.currentTimeMillis();
		for (int i = 1; i < 14298; i++) {
			if (i % 2000 == 0) {
				usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				 freeMemory  = Runtime.getRuntime().maxMemory() - usedMemory;
				 freeMemory_perc = 100* freeMemory/Runtime.getRuntime().maxMemory();
				 System.out.println("free memory (mb): "+freeMemory/mb);
				 System.out.println("free memory (%): "+freeMemory_perc+"%");
				 System.out.println((timee - System.currentTimeMillis())/1000);
				 sb.append("free memory (mb): "+freeMemory/mb+"\n");
				 sb.append("free memory (%): "+freeMemory_perc+"%"+"\n");
				 sb.append("tempo de execu��o"+(timee - System.currentTimeMillis())/1000+"\n");
			}
			
			System.out.println(i+"=====");
			out = path + "test"+i+"/out/eclipse/";
			if (checkMessage(out)) {
				in = path+ "test"+i+"/in/";
				SRImpact sri = new SRImpact("",in , out, "","0.5", "", true);
//				System.out.println("is refactoring "+sri.isRefactoring());
				if (!sri.isRefactoring()) {
					logBehavioralChange(out+BEHAVIORAL_CHANGE);
					System.out.println("BEHAVIORAL CHANGE");
					usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					 freeMemory  = Runtime.getRuntime().maxMemory() - usedMemory;
					 freeMemory_perc = 100* freeMemory/Runtime.getRuntime().maxMemory();
					 System.out.println("free memory (mb): "+freeMemory/mb);
					 System.out.println("free memory (%): "+freeMemory_perc+"%");
					 System.out.println((timee - System.currentTimeMillis())/1000);
					 sb.append("BC free memory (mb): "+freeMemory/mb+"\n");
					 sb.append("free memory (%): "+freeMemory_perc+"%"+"\n");
					 sb.append("tempo de execu��o"+(timee - System.currentTimeMillis())/1000+"\n");
				}
			}
		}
		OutputManager outfile = new OutputManagerASCII(path2);
		try {
			outfile.createFile();
			outfile.writeLine(sb.toString());
			outfile.closeFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		String p = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/PullUpMethod4/Alloy/";
		String p2 = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/PullUpMethod4/resu.txt";
		ExecuteSR.executeSR(p,p2);

	}
}
