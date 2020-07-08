package refactoringtestplugin.info;



import io.OutputManagerASCII;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ComparePrograms {
	
	
	List<Integer> AAdiferentes = new ArrayList<Integer>();
	List<Integer> AN = new ArrayList<Integer>();
	List<Integer> NA = new ArrayList<Integer>();
	List<Integer> NN = new ArrayList<Integer>();
	List<Integer> AAiguais = new ArrayList<Integer>();
	
	
	public void reset() {
		AAdiferentes = new ArrayList<Integer>();
		AN = new ArrayList<Integer>();
		NA = new ArrayList<Integer>();
		NN = new ArrayList<Integer>();
		AAiguais = new ArrayList<Integer>();
	}
	
	
public void compareProjects(String path1, String path2) {
		
		boolean aplicou1 = !(new File(path1+"/REFACTORING_INAPPLICABLE").exists()) && !(new File(path1+"/NULL_POINTER").exists());
		boolean aplicou2 = !(new File(path2+"/REFACTORING_INAPPLICABLE").exists())&& !(new File(path2+"/NULL_POINTER").exists());
		
		if (aplicou1 && aplicou2) {
			List<String> classes = getAllClasses(path1, "", new ArrayList<String>());
			for (String c : classes) {
				Clas c1 = new ProgramInfo().getClass_(path1+c);
				if (c1.getModifiers().contains("abstract")) {
					System.out.println("================okkk==================");
				}
			}
		}
		
}
	public void compareProjects(String path1, String path2, int i) {
		
		boolean aplicou1 = !(new File(path1+"/REFACTORING_INAPPLICABLE").exists()) && !(new File(path1+"/NULL_POINTER").exists());
		boolean aplicou2 = !(new File(path2+"/REFACTORING_INAPPLICABLE").exists())&& !(new File(path2+"/NULL_POINTER").exists());
		
		if (aplicou1 && !aplicou2) {
			AN.add(i);
		} else if (!aplicou1 && aplicou2) {
			NA.add(i);
		} else if (!aplicou1 && !aplicou2) {
			NN.add(i);
		} else {
			List<String> classes = getAllClasses(path1, "", new ArrayList<String>());
			List<String> classes2 = getAllClasses(path2, "", new ArrayList<String>());
			
			if (classes.isEmpty()) {
				if (classes2.isEmpty()) {
					NN.add(i);
					return;
				} else {
					NA.add(i);
					return;
				}
			}
			if (classes2.isEmpty()) {
				AN.add(i);
				return;
			}
			
			if (classes.size() != classes2.size()){
				AAdiferentes.add(i);
				return;
			}			
//			if (compareProjects(path1, path2)) AAiguais.add(i);
//			else AAdiferentes.add(i);
			for (String c : classes) {
				if (new File(path2+c).exists()) {
					
					Clas c1 = new ProgramInfo().getClass_(path1+c);
					Clas c2 = new ProgramInfo().getClass_(path2+c);
					
					boolean isEqual = c1.equals(c2);
					if (!isEqual) {
						AAdiferentes.add(i);
						return;
					}
				} else {
					AAdiferentes.add(i);
					return;
				}
			}
			AAiguais.add(i);
		}
		
		
		
	}
	
	public String getDiff(String path1, String path2) {
		List<String> classes = getAllClasses(path1, "", new ArrayList<String>());
		List<String> classes2 = getAllClasses(path2, "", new ArrayList<String>());
		
		String diff = "";
		String diff2 = "";
	
		int size = classes.size();
		int size2 = classes2.size();
		if (size > size2) {
			for (String string : classes) {
				if (!classes2.contains(string)) {
					diff2+=" 1 contem e 2 nao "+string;
					diff+= "l1 has a class that l2 does not have\n";
				}
			}
		}
		if (size < size2) {
			for (String string : classes2) {
				if (!classes.contains(string)) {
					diff2+=" 2 contem e 1 nao "+string;
					diff+= "l2 has a class that l1 does not have\n";
				}
			}
		}
		for (String c : classes) {
			if (new File(path2+c).exists()) {
				
				Clas c1 = new ProgramInfo().getClass_(path1+c);
				Clas c2 = new ProgramInfo().getClass_(path2+c);
				
				diff+= " "+c2.getDiff(c1);
			} 
		}
//		System.out.println("================================"+path1);
//		diff+="=============";
//		System.out.println(diff);
		return diff;
	}
	
//	public boolean compareProjects(String path1, String path2) {
//		
//		
//	
//		
////		List<String> classes = getAllClasses(path1, "", new ArrayList<String>());
////		for (String c : classes) {
////			if (new File(path2+c).exists()) {
////				boolean isEqual = compare(path1+c, path2+c);
////				if (!isEqual) {
////					return false;
////				}
////			} else {
////				return false;
////			}
////		}
////		return true;
//		
//	}
//	
	public static List<String> getAllClasses(String path, String className, List<String> classes) {
		File project1 = new File(path);
		String[] list = project1.list();
		for (String string : list) {
			if (string.startsWith(".")) continue;
			if (string.endsWith(".java")) {
				classes.add(className+"/"+string);
			} else {
				if (new File(path+"/"+string).isDirectory()) {
					getAllClasses(path+"/"+string, className+ "/"+string, classes);
				}
			}
		}
		return classes;
	}
	
//	public boolean compare(String path1, String path2)  {
//		
//		StringBuffer b1 = null;
//		StringBuffer b2 = null;
//		
//		try {
//			b1 = read(path1);
//			b2 = read(path2);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return b1.toString().equals(b2.toString());
//	}
//	
//	public StringBuffer read(String path) throws IOException {
//		InputManager file = new InputManagerASCII(path);
//		StringBuffer b = new StringBuffer();
//		file.openFile();
//		while (!file.isEndOfFile()) {
//			String line = file.readLine();
//			line = trim(line);
//			b.append(line);
//		}
//		file.closeFile();
//		return b;
//	}
//	
//	public String trim(String string) {
//		String result = ""; 
//		char[] charArray = string.toCharArray();
//		for (char c : charArray) {
//			if (c != ' ' && c != '\t') {
//				result += c;
//			}
//		}
//		return result;
//		
//	}
	
	public static void main(String[] args) throws IOException {
		
		
		ComparePrograms cp = new ComparePrograms();
		
		List<String> refactorings = new ArrayList<String>();
//		refactorings.add("renamemethod");
//		refactorings.add("movemethod");
//		refactorings.add("pushdownmethod");
//		refactorings.add("pushdownfield");
		refactorings.add("pullupmethod");
//		refactorings.add("pullupfield");
//		refactorings.add("addparameter");
		
//		refactorings.add("encapsulatefield");
		
		
//		refactorings.add("renamemethod");
//		refactorings.add("renamefield");
//		refactorings.add("renametype");
		
		for (String refactoring : refactorings) {
			int skip = 1;
			cp.reset();
			
			for (int i = 1; i<= 40000; i++) {
//				System.out.println("================================== "+i);
//				if (i == 879) {
//				String path1 = "/home/spg-experiment-3/Documents/bug_transf/jrrt/"+refactoring+"/skip"+skip+"/test"+i+"/out/jrrt/";
				String path2;
				//if (refactoring.equals("addparameter")) {
				//	path2 = "/home/spg-experiment-3/Documents/eclipse/"+refactoring+"/skip"+skip+"_undo/test"+i+"/out/eclipse/";
				//} else
//				path2 = "/home/spg-experiment-3/Documents/bug_transf/eclipse/"+refactoring+"/skip"+skip+"/test"+i+"/out/eclipse/";
				
//				String path1 = "C:/Users/Felipe/Downloads/movemethodJRRT/skip25/test"+i+"/out/jrrt/";
//				String path2 = "C:/Users/Felipe/Downloads/movemethodEclipse/skip25/test"+i+"/out/eclipse/";
				
				String path1 = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/jrrt/"+refactoring+"/skip1/test"+i+"/out/jrrt/";
				path2 = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/eclipse/"+refactoring+"/skip1/test"+i+"/out/eclipse/";
				if (!new File(path1).exists()) break;
				
				if (!new File(path2).exists()) continue;
				
				cp.compareProjects(path1, path2, i);
//				cp.compareProjects(path1, path2);
//				}

			}
			OutputManagerASCII out = new OutputManagerASCII("C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/"+refactoring+"/AI.txt");
			out.createFile();
			out.writeLine("Aplicaram <diferente>");
			out.writeLine(cp.AAdiferentes.toString());
			out.writeLine("Aplicaram <igual>");
			out.writeLine(cp.AAiguais.toString());
			out.writeLine("Eclipse aplicou e JRRT nao");
			out.writeLine(cp.NA.toString());
			out.writeLine("JRRT aplicou e Eclipse nao");
			out.writeLine(cp.AN.toString());
			out.writeLine("Nenhuma aplicou");
			out.writeLine(cp.NN.toString());
			out.closeFile();
			System.out.println(cp.AAdiferentes.toString());
			System.out.println(cp.AAiguais.toString());
			System.out.println(cp.NA.toString());
			System.out.println(cp.AN.toString());
			System.out.println(cp.NN.toString());
			
			
			AnalyzeResults a = new AnalyzeResults();
//			int skip = 1;
			String tool = "eclipse";
//			String refactoring = "pushdownmethod";
	
				
				AnalyzeResults aEcl = new AnalyzeResults();
//				a.analyze("/home/spg-experiment-3/Documents/overly_strong_conditions_"+refactoring+skip+".txt");
//				a.analyze("C:/Users/Felipe/Downloads/transfResults/attachments/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
//				a.analyze("C:/Users/Felipe/Downloads/transfResults/transfResults/"+refactoring+skip+".txt");
				aEcl.readFile("C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/"+refactoring+"/AI.txt");
//				aEcl.readFile("/home/spg-experiment-3/Documents/"+tool+"/"+refactoring+"/skip"+skip+"/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
				
				aEcl.readFile("C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/"+tool+"/"+refactoring+"/results.txt");
				
				
				AnalyzeResults aJrrt = new AnalyzeResults();
				tool = "jrrt";
				aJrrt.readFile("C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/"+refactoring+"/AI.txt");
//				aJrrt.readFile("/home/spg-experiment-3/Documents/"+tool+"/"+refactoring+"/skip"+skip+"/msgs.txt");
//				aJrrt.readFile("/home/spg-experiment-3/Documents/"+tool+"/"+refactoring+"/skip"+skip+"/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
				aJrrt.readFile("C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/"+tool+"/"+refactoring+"/results.txt");
				a.analyze(aEcl, aJrrt, "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/", refactoring, skip);

				


		}

	}
}
