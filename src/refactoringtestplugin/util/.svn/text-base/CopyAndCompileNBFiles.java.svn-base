package refactoringtestplugin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.Main;

public class CopyAndCompileNBFiles {

	/**
	 * Utility function that returns the system's temporary directory.
	 */
	static String refactoringName;
	static String testClassName;
	static String className;
	static String className2;
	
	static FileFilter fileFilter = new FileFilter() {
	        public boolean accept(File file) {
	        	return (file.getName().indexOf(".java") == (file.getName().length() - 5));
	        }
	};		

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

	static void copyEncapsulateFieldWithArrayAccess() throws Exception {
		File rootDir = new File(getSystemTempDir());

		File parentDir = new File(rootDir.getAbsolutePath() + File.separator + refactoringName + File.separator + testClassName);
		if (! parentDir.exists()) {
			System.out.println("This directory does not exist: "+ parentDir.getAbsolutePath());
			System.exit(1);
		} 
		//
		try {
		BufferedWriter out = new BufferedWriter(new FileWriter(
				getSystemTempDir() + File.separator + refactoringName + "_" + testClassName + "_ListNotCompiles.txt"));
		
		className2 = "B";
		if (refactoringName.equals("renametype"))
			className = "NewA";
		else 
			className = "A";
		//deleteNonOutNBAndCopyJavaFilesInParent(parentDir);

		compileJavaClasses(parentDir);

		File[] files = parentDir.listFiles();
		String [] listClasses;
		int counterCompiles = 0;
		int counterDoesNotCompile = 0;
		
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				File outputDirectory = new File(file.getAbsolutePath() + File.separator + "outNB");
				File performRefact = new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + "REFACTORING_WAS_PERFORMED");
				if (performRefact.exists()){
					boolean resultDoesNotCompile = false;
						File postRefactorCompile = new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + "POST_REFACTOR_NOT_COMPILE");
						//System.out.println(postRefactorCompile.getName() + " length: "+ postRefactorCompile.length() + " "+ postRefactorCompile.canWrite());
						if (postRefactorCompile.length() == 0){							
							System.gc();
							if (!postRefactorCompile.delete()) System.out.println("there was a problem deleting the post_refactor file");
						}	
						else {
							out.write("Test: " + file.getAbsolutePath() + " does not compile\n");							
							resultDoesNotCompile = true;
						}					
					if (resultDoesNotCompile) counterDoesNotCompile++;
					else counterCompiles++;
						
				}
				
				
				//File javaClassFile = new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + className + ".java");
//				if (javaClassFile.exists()) {
//					File postRefactorCompile = new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + "POST_REFACTOR_NOT_COMPILE");
//					if (refactoringName.equals("innertoouter")){						
//						if (postRefactorCompile.length() == 0){
//							counterCompiles++;
//							new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + "POST_REFACTOR_NOT_COMPILE").delete();
//						}	
//						else {
//							out.write("Class: " + javaClassFile.getAbsolutePath() + " does not compile\n");
//							counterDoesNotCompile++;
//						}									
//					} else {						
//						if (postRefactorCompile.length() == 0){
//							counterCompiles++;
//							new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + "POST_REFACTOR_NOT_COMPILE").delete();
//						}	
//						else {
//							out.write("Class: " + javaClassFile.getAbsolutePath() + " does not compile\n");							
//							counterDoesNotCompile++;
//						}					
//					}
//				}
			}
		}
		out.write("\nTotal # tests that don't compile: " + counterDoesNotCompile + "\n");
		out.write("Total # tests that do compile: " + counterCompiles + "\n");
		out.close();
		
		System.out.println(parentDir.getAbsolutePath() + " compiles="
						+ counterCompiles + ", doesNotCompile="
						+ counterDoesNotCompile);
		} catch (Exception e){
			System.out.println("Error: " + e);
		}

	}
	
	private static void compileJavaClasses(File parentDir) {
		File[] files = parentDir.listFiles();
		PrintWriter printWriter;		
		try {
		
		for (int i = 0; i < files.length; i++) {
			File file = files[i];			
			File performRefact = new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + "REFACTORING_WAS_PERFORMED");
			if (file.isDirectory()) {
				File outNB = new File(file.getAbsolutePath() + File.separator + "outNB");	
				if (outNB.exists() && performRefact.exists()) {
					File[] listJavaFiles = outNB.listFiles(fileFilter);
					String[] listJavaFilesPaths = new String[listJavaFiles.length];
					for (int j = 0; j < listJavaFiles.length; j++) {
						listJavaFilesPaths[j]=listJavaFiles[j].getAbsolutePath();
					}
					printWriter = new PrintWriter(file.getAbsolutePath() + File.separator + "outNB" + File.separator + "POST_REFACTOR_NOT_COMPILE");
					int resultCompilation2 = Main.compile(listJavaFilesPaths, printWriter); //
				}				
				System.out.println("compiled # " + i);
			}
		}
		} catch (IOException e){
			System.out.println(e);
		}
	}
	
//	private static void compileJavaClasses(File parentDir) {
//		File[] files = parentDir.listFiles();
//		PrintWriter printWriter;		
//		try {
//		for (int i = 0; i < files.length; i++) {
//			File file = files[i];
//			File sourceFile = new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + className + ".java");
//			if (file.isDirectory()) {
//				File outNB = new File(file.getAbsolutePath() + File.separator + "outNB");
//				if (outNB.exists() && sourceFile.exists()) {
//					printWriter = new PrintWriter(file.getAbsolutePath() + File.separator + "outNB" + File.separator + "POST_REFACTOR_NOT_COMPILE");
//					if (!refactoringName.equals("innertoouter")) {												
//						int resultCompilation = Main.compile(new String[] { sourceFile.getAbsolutePath() }, printWriter);						
//						if (resultCompilation == 1) {					
//							new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + className + ".class").delete();
//						}
//					} else {						
//						File sourceFileB = new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + className2 + ".java");						
//						int resultCompilation2 = Main.compile(new String[] { sourceFileB.getAbsolutePath(), sourceFile.getAbsolutePath() }, printWriter); //
//						if (resultCompilation2 == 1) {
//							new File(file.getAbsolutePath() + File.separator + "outNB" + File.separator + className2 + ".class").delete();
//						}	
//					}
//				}				
//				System.out.println("compiled # " + i);
//			}
//		}
//		} catch (IOException e){
//			System.out.println(e);
//		}
//	}
		

	static public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
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
			copyEncapsulateFieldWithArrayAccess();
		} catch (Exception e) {
			System.exit(1);
		}

		
	}

}
