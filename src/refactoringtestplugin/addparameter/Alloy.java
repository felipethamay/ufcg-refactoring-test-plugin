package refactoringtestplugin.addparameter;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;

import jdolly.JDollyImp;

public class Alloy extends AddParameterTest {

	// @Override
	// protected String getLogDirPath() {
	// return super.getLogDirPath() + "/alloyASTGenerator";
	//// return
	// "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/addparameter/last/";
	// }

	protected String getLogDirPath() {
		// return super.getLogDirPath() + "/alloyASTGenerator";
		return "C:/Users/Felipe/Documents/doutorado/experiments/alloy/eclipse/addparameter/";
		// return
		// "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupmethod/last/";
	}

	@Override
	public String getDescription() {
		return "Two classes A and B. A declares a method and B references it in some expression. B extends A. Refactor to push m down to B.";
	}

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		String pluginFolder = refactoringtestplugin.Activator.getDefault().getPluginFolder();
		return new JDollyImp(
				"C:/Users/Felipe/Documents/workspace3/program_counter/alloyTheory/addparameter_final.als", 2, 3, 3);
		// return new JDollyNoField(pluginFolder +
		// "/../program_counter/alloyTheory/addparameter_final.als", 2, 3, 3);
	}

}
