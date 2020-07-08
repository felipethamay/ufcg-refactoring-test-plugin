package refactoringtestplugin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class List_Different_CompilationError_After_Refactor {

	static String ideName; // Eclipse or Netbeans

	static String refactoringName;

	static String testClassName;

	static List listCompilationErrors;
	
	static String typeOfFile;

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
	
	public static String getErrorMessage(File f){
		String message = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(f.getAbsolutePath()));
			String str;
			while ((str = in.readLine()) != null)
				message += str + "\n";
			
		} catch (Exception e){
			System.out.println("Error is: "+ e);
		}
		return message;
	}

	public static void findCompilationErrors() {
		String outputDirectory = null;
		File rootDir = new File(getSystemTempDir());
		listCompilationErrors = new ArrayList<String>();
		File parentDir = new File(rootDir.getAbsolutePath() + File.separator
				+ refactoringName + File.separator + testClassName);
		if (!parentDir.exists()) {
			System.out.println("This directory does not exist: "
					+ parentDir.getAbsolutePath());
			System.exit(1);
		}
		// setting the output direct. 
		if (ideName.equals("Eclipse")) outputDirectory = "out";
		else if (ideName.equals("Netbeans")) outputDirectory = "outNB";
		
		try {
			String FileName = "";
			String FileApplicable = "";
			if (typeOfFile.equals("Error")) FileName = "CompilationErrors";			
			else FileName = "NotApplicableMessages";
			
			if (ideName.equals("Eclipse")) FileApplicable = "REFACTORING_INAPPLICABLE";
			else FileApplicable = "REFACTORING_NOT_PERFORMED";
				
			BufferedWriter out = new BufferedWriter(new FileWriter(
					getSystemTempDir() + File.separator + refactoringName + "_"
							+ testClassName + "_List_" + FileName + "_" + ideName + ".txt"));

			File[] files = parentDir.listFiles();
			int counterCompiles = 0;
			int counterDoesNotCompile = 0;
			String errorMessage;
			int counter = 0;
			int counterDifferentMessages = 0;
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				errorMessage = "";
				if (file.isDirectory()) {
					if (typeOfFile.equals("Error")){
						File classRefact = new File(file.getAbsolutePath() + File.separator + outputDirectory + File.separator + "REFACTORING_NOT_PERFORMED");
						File classFile = new File(file.getAbsolutePath()
								+ File.separator + outputDirectory + File.separator + "POST_REFACTOR_NOT_COMPILE");
						if (!classRefact.exists() && classFile.exists()) {					   	
						   errorMessage = getErrorMessage(classFile);	
						   counter++;
						   System.out.println("errorMessage: "+ errorMessage);
						   if (!listCompilationErrors.contains(errorMessage)) {
							   listCompilationErrors.add(errorMessage);						   
							   out.write(errorMessage + "\n");
							   counterDifferentMessages++;
						   }   
						}
					} else {
						File applicableFile = new File(file.getAbsolutePath() + File.separator + outputDirectory + File.separator + FileApplicable);
						if (applicableFile.exists()){
							   errorMessage = getErrorMessage(applicableFile);
							   System.out.println(file.getName()+ " " + errorMessage); 
							   counter++;
							   System.out.println("notApplicableMessage: "+ errorMessage);
							   if (!listCompilationErrors.contains(errorMessage)) {
								   listCompilationErrors.add(errorMessage);						   
								   out.write(errorMessage + "\n");
								   counterDifferentMessages++;
							   }							
						}
					}
				} 
			}
			out.write("\n\n" + "Total # of errorMessages: "+ counter);
			out.write("\n\n" + "Total # of different errorMessages: "+ counterDifferentMessages);
			out.close();
			System.out.println("Total # of errorMessages: "+ counter);
			System.out.println("Total # of different errorMessages: "+ counterDifferentMessages);
		} catch (Exception e) {
			System.out.println("Error is: " + e);
		}
	}

	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-refactoring"))
				refactoringName = args[++i];
			else if (args[i].equals("-testclass"))
				testClassName = args[++i];
			else if (args[i].equals("-ide"))
				ideName = args[++i];
			else if (args[i].equals("-typeoffile"))
				typeOfFile = args[++i];
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
			if (!ideName.equals("Eclipse") && !ideName.equals("Netbeans")) {
				System.out.println("ideName must be Eclipse or Netbeans");
				System.exit(0);
			}
			if (!typeOfFile.equals("Error") && !typeOfFile.equals("Applicable")) {
				System.out.println("typeOfFile must be Error or Applicable");
				System.exit(0);			
			}
			findCompilationErrors();
		} catch (Exception e) {
			System.exit(1);
		}
	}
}
