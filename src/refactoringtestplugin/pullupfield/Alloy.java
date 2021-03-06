package refactoringtestplugin.pullupfield;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;

import jdolly.JDollyImp;

public class Alloy extends PullUpFieldTest {

	// @Override
	// protected String getLogDirPath() {
	// return super.getLogDirPath() + "/alloyASTGenerator";
	//// return
	// "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupfield/last/";
	// }

	protected String getLogDirPath() {
		// return super.getLogDirPath() + "/alloyASTGenerator";
		return "C:/Users/Felipe/Documents/Projects/Java/experiments/alloy/eclipseTest/pullupfield/";
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
		System.out.println(pluginFolder);
		return new JDollyImp(
				"C:/Users/Felipe/Documents/Projects/Java/experiments/alloy/program_counter/alloyTheory/pullupfield_final.als", 2, 3, 2,
				2);
		// JDolly jdolly = new JDollyWithField(pluginFolder +
		// "/../program_counter/alloyTheory/pullupfield_final.als", 2,
		// 3, 2, 2);
		// return jdolly;

	}

}
