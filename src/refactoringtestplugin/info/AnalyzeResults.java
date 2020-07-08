package refactoringtestplugin.info;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManagerASCII;


public class AnalyzeResults {

	
int compilation_errors = 0;
	
	int input_compilation_errors = 0;
	
	int engine_crash = 0;
	
	int warning_status = 0;
	
	int pos_condition_failure = 0;
	
	int behavioral_changes = 0;
	
	int null_pointer = 0;
	
	int AAdiferentes_ = 0;
	int AN_ =0;
	int NA_ = 0;
	int NN_ =0;
	int AAiguais_ =0;

	
	List<Integer> behavioralChanges = new ArrayList<Integer>();
	List<Integer> warnings = new ArrayList<Integer>();
	List<Integer> compilationErrors = new ArrayList<Integer>();
	List<Integer> AAdiferentes = new ArrayList<Integer>();
	List<Integer> AN = new ArrayList<Integer>();
	List<Integer> NA = new ArrayList<Integer>();
	List<Integer> NN = new ArrayList<Integer>();
	List<Integer> AAiguais = new ArrayList<Integer>();

	
	

	
	public void analyze( AnalyzeResults ecl, AnalyzeResults jrrt, String path, String refactoring, int skip) {
		
		//caminho feliz, as duas aplicaram e preservou o comporamento mas sao iguais
		List<Integer> AAiguaisBP = new ArrayList<Integer>();
		//caminho feliz, as duas aplicaram e preservou o comporamento mas sao diferentes
		List<Integer> AAdiferentesBP = new ArrayList<Integer>();
		
		System.out.println("Eclipse aplicou: "+(ecl.AAiguais_+ ecl.AAdiferentes_+ecl.NA_));
		System.out.println("JRRT aplicou: "+(jrrt.AAiguais_+ jrrt.AAdiferentes_+jrrt.AN_));
		
		
		System.out.println("Eclipse aplicou e preservou comportamento: "+(ecl.AAiguais_+ ecl.AAdiferentes_+ecl.NA_ - ecl.behavioral_changes - ecl.compilation_errors));
		System.out.println("JRRT aplicou e preservou comportamento: "+(jrrt.AAiguais_+ jrrt.AAdiferentes_+jrrt.AN_- jrrt.behavioral_changes - jrrt.compilation_errors));
		
		for (Integer dif : ecl.AAdiferentes) {
			if (!ecl.behavioralChanges.contains(dif) && !ecl.compilationErrors.contains(dif) && 
					!jrrt.behavioralChanges.contains(dif) && !jrrt.compilationErrors.contains(dif)) {
				if (dif == 3400) {
					System.out.println();
				}
				AAdiferentesBP.add(dif);
			}
		}
		System.out.println("As duas aplicaram, as duas preservaram o comportamento e foram diferentes: "+AAdiferentesBP.size());
		System.out.println(AAdiferentesBP.toString());
		
		for (Integer dif : ecl.AAiguais) {
			if (!ecl.behavioralChanges.contains(dif) && !ecl.compilationErrors.contains(dif) && 
					!jrrt.behavioralChanges.contains(dif) && !jrrt.compilationErrors.contains(dif)) {
				if (dif == 3400) {
					System.out.println();
				}
				AAiguaisBP.add(dif);
			} else {
				System.out.println("================"+dif);
				System.out.println(ecl.behavioralChanges.contains(dif));
				System.out.println(ecl.compilationErrors.contains(dif));
				System.out.println(jrrt.behavioralChanges.contains(dif));
				System.out.println(jrrt.compilationErrors.contains(dif));
			}
		}
		
		System.out.println("As duas aplicaram, as duas preservaram o comportamento e foram iguais: "+AAiguaisBP.size());
		System.out.println(AAiguaisBP.toString());
			
		for (Integer integer : jrrt.compilationErrors) {
			if (AAiguaisBP.contains(integer)) {
				System.out.println("CONTEMM "+integer);
			}
		}
		
		try {
			ecl.printDiff(path, refactoring, skip, AAdiferentesBP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readFile(String path) throws IOException {
		
		int sum = 0;
		InputManager im = new InputManagerASCII(path);
		im.openFile();
		int count = 0;
		while (!im.isEndOfFile()) {
			String line = im.readLine();
			if ((line.contains("compilation_errors") && !line.contains("input_compilation_errors")) || line.contains("Compilation error") ) {
				line = im.readLine();
				while (line.equals("")) line = im.readLine();
				if (line.contains("[]")) {
					compilation_errors = 0;
				} else {
					String[] split = line.split(",");
					compilation_errors = split.length;
					for (String string : split) {
						string = string.replace(",", "");
						string = string.replace("[", "");
						string = string.replace("]", "");
						string = string.replace(" ", "");
						compilationErrors.add(Integer.parseInt(string));
					}
					System.out.println("compilation_errors: "+ compilation_errors);
					sum += compilation_errors;
				}
				
			} else if(line.contains("input_compilation_errors")|| line.contains("nput compilation error")) {
				line = im.readLine();
				while (line.equals("")) line = im.readLine();
				if (line.contains("[]")) {
					input_compilation_errors = 0;
				} else {
					String[] split = line.split(",");
					input_compilation_errors = split.length;
					System.out.println("input_compilation_error: "+input_compilation_errors);
					sum += input_compilation_errors;
				}
			}else if(line.contains("engine_crash") || line.contains("Engine Crash")) {
				line = im.readLine();
				while (line.equals("")) line = im.readLine();
				if (line.contains("[]")) {
					engine_crash = 0;
				} else {
					String[] split = line.split(",");
					engine_crash = split.length;
					System.out.println("engine_crash "+engine_crash);
				}
				
			}else if(line.contains("warning_status") || line.contains("Warning")) {
				line = im.readLine();
				while (line.equals("")) line = im.readLine();
				if (line.contains("[]")) {
					warning_status = 0;
				} else {
					String[] split = line.split(",");
					warning_status = split.length;
					System.out.println("warning_status "+warning_status);
					for (String string : split) {
						string = string.replace(",", "");
						string = string.replace("[", "");
						string = string.replace("]", "");
						string = string.replace(" ", "");
						warnings.add(Integer.parseInt(string));
					}
					sum += warning_status;
				}
				
			}else if(line.contains("pos_condition_failure") || line.contains("Pos condition faiulure")) {
				line = im.readLine();
				while (line.equals("")) line = im.readLine();
				if (line.contains("[]")) {
					pos_condition_failure = 0;
				} else {
					String[] split = line.split(",");
					pos_condition_failure = split.length;
					System.out.println("pos_condition_failure "+pos_condition_failure);
				}
				
			}else if(line.contains("behavioral_changes") || line.contains("Behavioral Change") ) {
				line = im.readLine();
				while (line.equals("")) line = im.readLine();
				if (line.contains("[]")) {
					behavioral_changes = 0;
				} else {
					String[] split = line.split(",");
					behavioral_changes = split.length;
					for (String string : split) {
						string = string.replace(",", "");
						string = string.replace("[", "");
						string = string.replace("]", "");
						string = string.replace(" ", "");
						behavioralChanges.add(Integer.parseInt(string));
					}
					System.out.println("behavioral_changes "+behavioral_changes);
				}
				
			}else if(line.contains("null_pointer") || line.contains("Null Pointer")) {
				line = im.readLine();
				while (line.equals("")) line = im.readLine();
				if (line.contains("[]")) {
					null_pointer = 0;
				} else {
					String[] split = line.split(",");
					null_pointer = split.length;
					System.out.println("null_pointer "+null_pointer);
					sum += null_pointer;
				}
				
			}else if (line.equals("overly_strong_conditions_eclipse")) {
				line = im.readLine();
				if (!line.contains("[]")) {
					String[] split = line.split(",");
					System.out.println("os eclipse "+split.length);
				}
			}else if (line.equals("overly_strong_conditions_jrrt")) {
				line = im.readLine();
				if (!line.contains("[]")) {
					String[] split = line.split(",");
					System.out.println("os jrrt "+split.length);
				}
			} else if(line.equals("Aplicaram <igual>")) {
				line = im.readLine();
				if (line.contains("[]")) {
					AAiguais_ = 0;
				} else {
					String[] split = line.split(",");
					AAiguais_ = split.length;
					for (String string : split) {
						string = string.replace(",", "");
						string = string.replace("[", "");
						string = string.replace("]", "");
						string = string.replace(" ", "");
						AAiguais.add(Integer.parseInt(string));
					}
					System.out.println("Aplicaram <igual> "+AAiguais_);
					sum+=AAiguais_;
				}
			} else if(line.equals("Aplicaram <diferente>")) {
				line = im.readLine();
				if (line.contains("[]")) {
					AAdiferentes_ = 0;
				} else {
					String[] split = line.split(",");
					AAdiferentes_ = split.length;
					for (String string : split) {
						string = string.replace(",", "");
						string = string.replace("[", "");
						string = string.replace("]", "");
						string = string.replace(" ", "");
						AAdiferentes.add(Integer.parseInt(string));
					}
					System.out.println("Aplicaram <diferente> "+AAdiferentes_);
					sum+=AAdiferentes_;
				}
			} else if(line.equals("Eclipse aplicou e JRRT nao")) {
				line = im.readLine();
				if (line.contains("[]")) {
					NA_ = 0;
				} else {
					String[] split = line.split(",");
					NA_ = split.length;
					for (String string : split) {
						string = string.replace(",", "");
						string = string.replace("[", "");
						string = string.replace("]", "");
						string = string.replace(" ", "");
						NA.add(Integer.parseInt(string));
					}
				}
				System.out.println("Eclipse aplicou e JRRT nao "+NA_);
				sum+= NA_;
			}else if(line.equals("JRRT aplicou e Eclipse nao")) {
				line = im.readLine();
				if (line.contains("[]")) {
					AN_ = 0;
				} else {
					String[] split = line.split(",");
					AN_ = split.length;
					for (String string : split) {
						string = string.replace(",", "");
						string = string.replace("[", "");
						string = string.replace("]", "");
						string = string.replace(" ", "");
						AN.add(Integer.parseInt(string));
					}
					
				}
				System.out.println("JRRT aplicou e Eclipse nao "+AN_);
				sum+=AN_;
			}else if(line.equals("Nenhuma aplicou")) {
				line = im.readLine();
				if (line.contains("[]")) {
					NN_ = 0;
				} else {
					String[] split = line.split(",");
					NN_ = split.length;
					for (String string : split) {
						string = string.replace(",", "");
						string = string.replace("[", "");
						string = string.replace("]", "");
						string = string.replace(" ", "");
						NN.add(Integer.parseInt(string));
					}
					System.out.println("Nenhuma aplicou "+NN_);
					sum+= NN_;
				}
			}
		}
		
	
		

		im.closeFile();
		
	}
	
	
	
	public void reset() {
		behavioralChanges = new ArrayList<Integer>();
		compilationErrors = new ArrayList<Integer>();
		AAdiferentes = new ArrayList<Integer>();
		AN = new ArrayList<Integer>();
		NA = new ArrayList<Integer>();
		NN = new ArrayList<Integer>();
		AAiguais = new ArrayList<Integer>();
//		AP = new ArrayList<Integer>();
//		AAdiferentesBP = new ArrayList<Integer>();
	}
	
	public void printDiff(String path, String refactoring, int skip, List<Integer> diffList) throws IOException {
		OutputManagerASCII out = new OutputManagerASCII(path+"transfResults/diff"+refactoring+skip+"completo.txt");
		out.createFile();
		List<String> diffs = new ArrayList<String>();
		Map<String, Integer> diffs_map = new HashMap<String, Integer>();
		
		for (Integer d : diffList) {
//			for (Integer d : AAdiferentes) {
//				if (d == 692) {
//			String path1 = path +"eclipse/"+refactoring+"/skip"+skip+"/test"+d+"/out/eclipse/";
			String path1 = path +"eclipse/"+refactoring+"/skip"+skip+"/test"+d+"/out/eclipse/";
			String path2 = path +"jrrt/"+refactoring+"/skip"+skip+"/test"+d+"/out/jrrt/";

//			System.out.println("==========================Diff "+d+" ===========================");
//			System.out.println(new ComparePrograms().getDiff(path1, path2));
//			System.out.println();
			out.writeLine("==========================Diff "+d+" ===========================\n");
			String diff = new ComparePrograms().getDiff(path1, path2);
//			System.out.println(diff);
			String[] split = diff.split("\n");
			for (String string : split) {
				while (string.startsWith(" ") || string.startsWith(",")) {
					string = string.substring(1);
				}
				
				while (string.endsWith(" ") || string.endsWith(",")) {
					string = string.substring(0,string.length()-1);
				}
				//if (!diffs.contains(string)) {
				if (!diffs_map.containsKey(string)) {
//					diffs.add(string);
					diffs_map.put(string, 1);
				} else {
					Integer count = diffs_map.get(string)+1;
					diffs_map.put(string, count);
				}
			}
//			System.out.println(diffs_map.toString());
			//usado para comparar source e target
//			while (diff.startsWith(" ") || diff.startsWith(",")) {
//				diff = diff.substring(1);
//			}
//			
//			while (diff.endsWith(" ") || diff.endsWith(",")) {
//				diff = diff.substring(0,diff.length()-1);
//			}
//			if (!diffs.contains(diff)) {
//				diffs.add(diff);
//			}
				
			
			out.writeLine(diff+"\n");
//				}
		}
		System.out.println(diffs_map.toString());
		out.writeLine("Different diffs:\n");
		out.writeLine(diffs_map.toString());
		out.closeFile();
	}
	
	public static void main(String[] args) throws IOException {
		
		AnalyzeResults a = new AnalyzeResults();
		int skip = 1;
		String tool = "eclipse";
		String refactoring = "pullupfield";
		for (int i = 1; i <= 1; i++) {
			if (i == 2) {
				skip = 10;
			}
			if (i == 3) {
				skip = 25;
			}
			
			AnalyzeResults aEcl = new AnalyzeResults();
//			a.analyze("/home/spg-experiment-3/Documents/overly_strong_conditions_"+refactoring+skip+".txt");
//			a.analyze("C:/Users/Felipe/Downloads/transfResults/attachments/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
//			a.analyze("C:/Users/Felipe/Downloads/transfResults/transfResults/"+refactoring+skip+".txt");
			aEcl.readFile("/home/spg-experiment-3/Documents/bug_transf/transfResults/"+refactoring+" "+skip+"AI.txt");
//			aEcl.readFile("/home/spg-experiment-3/Documents/"+tool+"/"+refactoring+"/skip"+skip+"/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
			
			aEcl.readFile("/home/spg-experiment-3/Documents/bug_transf/"+tool+"/"+refactoring+"/skip"+skip+"/msgs.txt");
			
			
			AnalyzeResults aJrrt = new AnalyzeResults();
			tool = "jrrt";
			aJrrt.readFile("/home/spg-experiment-3/Documents/bug_transf/transfResults/"+refactoring+" "+skip+"AI.txt");
//			aJrrt.readFile("/home/spg-experiment-3/Documents/"+tool+"/"+refactoring+"/skip"+skip+"/msgs.txt");
//			aJrrt.readFile("/home/spg-experiment-3/Documents/"+tool+"/"+refactoring+"/skip"+skip+"/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
			aJrrt.readFile("/home/spg-experiment-3/Documents/bug_transf/"+tool+"/"+refactoring+"/skip"+skip+"/msgs.txt");
			a.analyze(aEcl, aJrrt, "/home/spg-experiment-3/Documents/bug_transf/", refactoring, skip);
//			System.out.println();
			
			
		}
//		a = new AnalyzeResults();
//		refactoring = "pushdownmethod";
//		for (int i = 1; i <= 1; i++) {
//			if (i == 2) {
//				skip = 10;
//			}
//			if (i == 3) {
//				skip = 25;
//			}
////			a.analyze("/home/spg-experiment-3/Documents/overly_strong_conditions_"+refactoring+skip+".txt");
////			a.analyze("C:/Users/Felipe/Downloads/transfResults/attachments/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
////			a.analyze("C:/Users/Felipe/Downloads/transfResults/transfResults/"+refactoring+skip+".txt");
//			a.analyze("/home/spg-experiment-3/Documents/transfResults/"+refactoring+" "+skip+".txt");
////			a.analyze("/home/spg-experiment-3/Documents/"+tool+"/"+refactoring+"/skip"+skip+"/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
//			System.out.println();
//			a.printDiff("/home/spg-experiment-3/Documents/", refactoring, skip);
//			
//			
//		}
		
		
		
	}
}
