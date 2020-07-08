package refactoringtestplugin.movemethod;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;

//import br.edu.dsc.ufcg.jdolly.JDolly;
//import br.edu.dsc.ufcg.jdolly.JDollyNoField;
//import br.edu.dsc.ufcg.jdolly.JDollyWithField;
import jdolly.JDollyImp;

public class Alloy extends MoveMethodTest {

	// @Override
	// protected String getLogDirPath() {
	//// return super.getLogDirPath() + "/alloyASTGenerator";
	// return
	// "C:/Users/Felipe/Documents/doutorado/experiments/MT/jrrt/movemethodInputs/";
	//// return
	// "C:/Users/Felipe/Documents/doutorado/experiments/MT/eclipse/MoveMethod/";
	// }

	protected String getLogDirPath() {
		// return super.getLogDirPath() + "/alloyASTGenerator";
//		return "C:/Users/Felipe/Documents/doutorado/experiments/alloy/eclipse/movemethod/";
		return "C:/Users/Felipe/Documents/Projects/Java/experiments/alloy/eclipse/moveMethod/";
		// return
		// "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupmethod/last/";
	}

	@Override
	public String getDescription() {
		return "Two classes A and B. A declares a method and B references it in some expression. B extends A. Refactor to push m down to B.";
	}

	// @Override
	// protected IGenerator<List<CompilationUnit>> getCUGen() {
	//
	// String pluginFolder =
	// refactoringtestplugin.Activator.getDefault().getPluginFolder();
	//
	//
	// return new JDollyWithField(pluginFolder +
	// "/../program_counter/alloyTheory/movemethod_final.als", 2,
	// 3, 3,1);
	// }

	protected IGenerator<List<CompilationUnit>> getCUGen() {

		String pluginFolder = refactoringtestplugin.Activator.getDefault().getPluginFolder();

		return new JDollyImp("C:/Users/Felipe/Documents/Projects/Java/experiments/alloy/program_counter/alloyTheory/movemethod_final.als",
				2, 2, 4, 1);
	}

	@Override
	protected String getRefactoredClassName() {
		// TODO Auto-generated method stub
		return null;
	}

}
