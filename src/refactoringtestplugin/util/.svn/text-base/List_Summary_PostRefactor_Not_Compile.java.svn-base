package refactoringtestplugin.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class List_Summary_PostRefactor_Not_Compile {
	static String refactoringName;
	static String testClassName;
	static String className;
	
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
	
	public static void getSummary_PostRefactor_Not_Compile(){
		List listOnlyNotCompileNB, listBothNotCompile, listOnlyNotCompileEC;
		File rootDir = new File(getSystemTempDir());
		className = "A";

		File parentDir = new File(rootDir.getAbsolutePath() + File.separator + refactoringName + File.separator + testClassName);
		if (! parentDir.exists()) {
			System.out.println("This directory does not exist: "+ parentDir.getAbsolutePath());
			System.exit(1);
		} 
		//
		try {
		listOnlyNotCompileEC = new ArrayList<String>();
		listOnlyNotCompileNB = new ArrayList<String>();
		listBothNotCompile = new ArrayList<String>();
		BufferedWriter out = new BufferedWriter(new FileWriter(
				getSystemTempDir() + File.separator + refactoringName + "_" + testClassName + "_Summary_PostRefactor_NotCompile.txt"));

		File[] files = parentDir.listFiles();
		int counterCompiles = 0;
		int counterDoesNotCompile = 0;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				File classRefactEC = new File(file.getAbsolutePath() + File.separator + "out" + File.separator + className + ".java");
				File classRefactNB = new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + className + ".java");
				
				File classFileEC = new File(file.getAbsolutePath() + File.separator + "out" + File.separator + "POST_REFACTOR_NOT_COMPILE");				
				File classFileNB = new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + "POST_REFACTOR_NOT_COMPILE");
				if (classRefactEC.exists() && classRefactNB.exists() && classFileEC.exists() && classFileNB.exists())
					listBothNotCompile.add(file.getAbsolutePath());
				else if (classRefactEC.exists() && classFileEC.exists())
					listOnlyNotCompileEC.add(file.getAbsolutePath());
				else if (classRefactNB.exists() && classFileNB.exists()){
					System.out.println(">>>");
					listOnlyNotCompileNB.add(file.getAbsolutePath());
				}	
			}	
		}
		
		out.write("Tests that dont compile after being refactored in Eclipse and Netbeans: " + listBothNotCompile.size() + "\n\n");
		for (int i=0; i < listBothNotCompile.size(); i++)
			out.write(listBothNotCompile.get(i) + "\n");
		out.write("\n\nTests that only dont compile in Eclipse after being refactored: " + listOnlyNotCompileEC.size() + "\n\n");
		for (int i=0; i < listOnlyNotCompileEC.size(); i++)
			out.write(listOnlyNotCompileEC.get(i) + "\n");
		out.write("\n\nTests that only dont compile in Netbeans after being refactored: " + listOnlyNotCompileNB.size() + "\n\n");
				
		for (int i=0; i < listOnlyNotCompileNB.size(); i++)
			out.write(listOnlyNotCompileNB.get(i) + "\n");
		out.close();
		
		System.out.println("#tests for both: "+ listBothNotCompile.size());
		System.out.println("#tests only for EC: "+ listOnlyNotCompileEC.size());
		System.out.println("#tests only for NB: "+ listOnlyNotCompileNB.size());
		} catch (Exception e){
			System.out.println("Error: " + e);
		}

		
	}	
	
	public static void main(String[] args) {
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-refactoring"))
				refactoringName = args[++i];
			else if (args[i].equals("-testclass"))
				testClassName = args[++i];
		}		
		try {
			if (refactoringName == null) {
				System.out.println("Refactoring was not specified");
				throw new Exception("Refactoring was not specified");
			}			
			if (refactoringName.equals("")) {
				System.out.println("Refactoring can not be an empty string");
				throw new Exception("Refactoring can not be an empty string");
			}
			if (testClassName == null) {
				System.out.println("TestClass was not specified");
				throw new Exception("TestClass was not specified");
			}			
			if (testClassName.equals("")) {
				System.out.println("TestClass can not be an empty string");
				throw new Exception("TestClass can not be an empty string");
			}
			getSummary_PostRefactor_Not_Compile();
		} catch (Exception e) {
			System.exit(1);
		}

		
	}

}
