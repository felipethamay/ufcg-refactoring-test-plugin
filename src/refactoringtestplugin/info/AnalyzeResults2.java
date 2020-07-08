package refactoringtestplugin.info;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManagerASCII;


public class AnalyzeResults2 {

	
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
	int AAiguaisBP_ =0;
	
	int AP_ = 0;
	
	List<Integer> behavioralChanges = new ArrayList<Integer>();
	List<Integer> compilationErrors = new ArrayList<Integer>();
	List<Integer> AAdiferentes = new ArrayList<Integer>();
	List<Integer> AN = new ArrayList<Integer>();
	List<Integer> NA = new ArrayList<Integer>();
	List<Integer> NN = new ArrayList<Integer>();
	List<Integer> AAiguais = new ArrayList<Integer>();
	List<Integer> AP = new ArrayList<Integer>();
	List<Integer> AiguaisBP = new ArrayList<Integer>();
	List<Integer> APigual = new ArrayList<Integer>();
	
	
	//caminho feliz, as duas aplicaram e preservou o comporamento mas sao diferentes
	List<Integer> AAdiferentesBP = new ArrayList<Integer>();
	
	public void analyze(String path) throws IOException {
		
		int sum = 0;
		InputManager im = new InputManagerASCII(path);
		im.openFile();
		int count = 0;
		while (!im.isEndOfFile()) {
			String line = im.readLine();
			if (line.equals("compilation_errors")) {
				line = im.readLine();
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
				
			} else if(line.equals("input_compilation_errors")) {
				line = im.readLine();
				if (line.contains("[]")) {
					input_compilation_errors = 0;
				} else {
					String[] split = line.split(",");
					input_compilation_errors = split.length;
					System.out.println("input_compilation_error: "+input_compilation_errors);
					sum += input_compilation_errors;
				}
			}else if(line.equals("engine_crash")) {
				line = im.readLine();
				if (line.contains("[]")) {
					engine_crash = 0;
				} else {
					String[] split = line.split(",");
					engine_crash = split.length;
					System.out.println("engine_crash "+engine_crash);
				}
				
			}else if(line.equals("warning_status")) {
				line = im.readLine();
				if (line.contains("[]")) {
					warning_status = 0;
				} else {
					String[] split = line.split(",");
					warning_status = split.length;
					System.out.println("warning_status "+warning_status);
					sum += warning_status;
				}
				
			}else if(line.equals("pos_condition_failure")) {
				line = im.readLine();
				if (line.contains("[]")) {
					pos_condition_failure = 0;
				} else {
					String[] split = line.split(",");
					pos_condition_failure = split.length;
					System.out.println("pos_condition_failure "+pos_condition_failure);
				}
				
			}else if(line.equals("behavioral_changes")) {
				line = im.readLine();
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
				
			}else if(line.equals("null_pointer")) {
				line = im.readLine();
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
					System.out.println("Eclipse aplicou e JRRT nao "+NA_);
					sum+= NA_;
				}
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
					System.out.println("JRRT aplicou e Eclipse nao "+AN_);
					sum+=AN_;
				}
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
		
		System.out.println("Eclipse aplicou: "+(AAiguais_+ AAdiferentes_+NA_));
		System.out.println("JRRT aplicou: "+(AAiguais_+ AAdiferentes_+AN_));
		
		if (path.contains("eclipse"))
		System.out.println("Eclipse aplicou e preservou comportamento: "+(AAiguais_+ AAdiferentes_+NA_ - behavioral_changes - compilation_errors));
		if (path.contains("jrrt"))
		System.out.println("JRRT aplicou e preservou comportamento: "+(AAiguais_+ AAdiferentes_+AN_- behavioral_changes - compilation_errors));
		
		
		if (AP.isEmpty()) {
			for (Integer d : AAdiferentes) {
				if (!behavioralChanges.contains(d) && !compilationErrors.contains(d)) {
	//				AP_++;
					AP.add(d);
				}
			}
		} else {
			for (Integer d : AAdiferentes) {
				if (!behavioralChanges.contains(d) && !compilationErrors.contains(d) && AP.contains(d)) {
	//				AP_++;
					AAdiferentesBP.add(d);
				}
			}
		}
		
		if (APigual.isEmpty()) {
			for (Integer d : AAiguais) {
				if (!behavioralChanges.contains(d) && !compilationErrors.contains(d)) {
	//				AP_++;
					APigual.add(d);
				}
			}
		} else {
			for (Integer d : AAdiferentes) {
				if (!behavioralChanges.contains(d) && !compilationErrors.contains(d) && APigual.contains(d)) {
	//				AP_++;
					AiguaisBP.add(d);
				}
			}
		}
		
System.out.println(sum);
System.out.println("AP " +
		""+AP);
System.out.println("BP diferentes: "+AAdiferentesBP.size());
System.out.println(AAdiferentesBP.toString());

System.out.println("BP iguais: "+AiguaisBP.size());
System.out.println(AiguaisBP.toString());

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
	
	public void printDiff(String path, String refactoring, int skip) throws IOException {
		OutputManagerASCII out = new OutputManagerASCII(path+"transfResults/diff"+refactoring+skip+"Teste2.txt");
		out.createFile();
		List<String> diffs = new ArrayList<String>();
		
		for (Integer d : AAdiferentesBP) {
//			for (Integer d : AAdiferentes) {
//				if (d == 692) {
			String path1 = path +"eclipse/"+refactoring+"/skip"+skip+"/test"+d+"/out/eclipse/";
			String path2 = path +"jrrt/"+refactoring+"/skip"+skip+"/test"+d+"/out/jrrt/";

//			System.out.println("==========================Diff "+d+" ===========================");
//			System.out.println(new ComparePrograms().getDiff(path1, path2));
//			System.out.println();
			out.writeLine("==========================Diff "+d+" ===========================\n");
			String diff = new ComparePrograms().getDiff(path1, path2);
			
			String[] split = diff.split("\n");
			for (String string : split) {
				while (string.startsWith(" ") || string.startsWith(",")) {
					string = string.substring(1);
				}
				
				while (string.endsWith(" ") || string.endsWith(",")) {
					string = string.substring(0,string.length()-1);
				}
				if (!diffs.contains(string)) {
					diffs.add(string);
				}
			}
				
			
			out.writeLine(diff+"\n");
//				}
		}
		System.out.println(diffs.toString());
		out.writeLine("Different diffs:\n");
		out.writeLine(diffs.toString());
		out.closeFile();
	}
	
	public static void main(String[] args) throws IOException {
		
		AnalyzeResults2 a = new AnalyzeResults2();
		int skip = 1;
		String tool = "eclipse";
		String refactoring = "pushdownmethod";
		for (int i = 1; i <= 1; i++) {
			if (i == 2) {
				skip = 10;
			}
			if (i == 3) {
				skip = 25;
			}
//			a.analyze("/home/spg-experiment-3/Documents/overly_strong_conditions_"+refactoring+skip+".txt");
//			a.analyze("C:/Users/Felipe/Downloads/transfResults/attachments/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
//			a.analyze("C:/Users/Felipe/Downloads/transfResults/transfResults/"+refactoring+skip+".txt");
			a.analyze("/home/spg-experiment-3/Documents/transfResults/"+refactoring+" "+skip+".txt");
			a.analyze("/home/spg-experiment-3/Documents/"+tool+"/"+refactoring+"/skip"+skip+"/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
			
			
			
			a.reset();
			tool = "jrrt";
			a.analyze("/home/spg-experiment-3/Documents/transfResults/"+refactoring+" "+skip+".txt");
			a.analyze("/home/spg-experiment-3/Documents/"+tool+"/"+refactoring+"/skip"+skip+"/"+tool+"_sri_message_"+refactoring+"_"+skip+".txt");
			
//			System.out.println();
			a.printDiff("/home/spg-experiment-3/Documents/", refactoring, skip);
			
			
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
