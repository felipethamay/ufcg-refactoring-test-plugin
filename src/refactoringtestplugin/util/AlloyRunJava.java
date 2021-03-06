package refactoringtestplugin.util;

import io.OutputManager;
import io.OutputManagerASCII;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4viz.VizGUI;
import edu.mit.csail.sdg.alloy4whole.ExampleUsingTheCompiler;

public final class AlloyRunJava {

	public static void main(String[] args) throws Err {

		long start = System.currentTimeMillis();
		List<String> programs = getPrograms();
		int ce = 0;
		int i = 0;
		
		for (String string : programs) {
			i++;
			System.out.println("=================== Programa: " + i);
			System.out.println(string);
			

//			System.out.println(string);
//			OutputManager out3 = new OutputManagerASCII(
//					"programs3/Node_0.java");
//			try {
//				out3.createFile();
//				out3.writeLine(string);
//				out3.closeFile();
//				File jdollyProgram = new File("programs3/Node_0.java");
//				String compile2 = refactoringtestplugin.util.Compile
//						.compile("/Users/gustavo/workspaces/jdolly/RefactoringTestPlugin/programs3/Node_0.java");
//				
//				if (compile2.contains("error")) {
//					ce++;
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
		}
		long stop = System.currentTimeMillis();
		long total = (stop - start);
//		System.out.println("Erros de compila��o: " + ce);
		System.out.println("Tempo total: " + total);
		System.out.println("Programas gerados: " + i);
	}

	public static List<String> getPrograms() throws Err {
		List<String> result = new ArrayList<String>()				;
		VizGUI viz = null;

		A4Reporter rep = new A4Reporter() {
			@Override
			public void warning(ErrorWarning msg) {
				System.out.print("Relevance Warning:\n"
						+ (msg.toString().trim()) + "\n\n");
				System.out.flush();
			}
		};

		String filename = "C:/Users/Felipe/Documents/Projects/Java/experiments/alloy/program_counter/alloyTheory/teste.als";

		System.out.println("=========== Parsing+Typechecking " + filename
				+ " =============");
		Module world = CompUtil.parseEverything_fromFile(rep, null, filename);

		// Choose some default options for how you want to execute the commands
		A4Options options = new A4Options();
		options.solver = A4Options.SatSolver.SAT4J;

		for (Command command : world.getAllCommands()) {
			// Execute the command
			System.out.println("============ Command " + command
					+ ": ============");
			A4Solution ans = TranslateAlloyToKodkod.execute_command(rep, world
					.getAllReachableSigs(), command, options);
			// Print the outcome
			// System.out.println(ans);
			int i = 0;
//			 System.out.println("Geracao: " + i);
//			 System.out.println("==============================");
			ExtractJavaCodeToyExample ex = new ExtractJavaCodeToyExample(ans);
			result.add(ex.getJavaCode());
			
			int cont = 1;
			long tempoAntes = System.currentTimeMillis();
			
			
			while (ans.next() != null) {
//				System.out.println("=============================="); 
//				System.out.println("Geracao: " + i);
//				 System.out.println("==============================");
				ans = ans.next();
				ex = new ExtractJavaCodeToyExample(ans);
				result.add(ex.getJavaCode());
				
				
				if (ans.next().satisfiable())
					cont++;

				else
					break;
				i++;
			}
			long tempoDepois = System.currentTimeMillis();
			System.out.println("O n�mero de solu��es �: " + cont + " em "
					+ (tempoDepois - tempoAntes) / 1000 + "s");
			// If satisfiable...
			// if (ans.satisfiable()) {
			// // You can query "ans" to find out the values of each set or
			// type.
			// // This can be useful for debugging.
			// //
			// // You can also write the outcome to an XML file
			// ans.writeXML("alloy_example_output.xml");
			// //
			// // You can then visualize the XML file by calling this:
			// if (viz==null) {
			// viz = new VizGUI(false, "alloy_example_output.xml", null);
			// } else {
			// viz.loadXML("alloy_example_output.xml", true);
			// }
			// }
			// }
			
		}
		return result;
	}
}
