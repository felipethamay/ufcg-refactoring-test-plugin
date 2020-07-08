package refactoringtestplugin.pushdownmethod;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;
import jdolly.JDollyImp;

public class Alloy extends PushDownMethodTest {

	protected String getLogDirPath() {
		// return super.getLogDirPath() + "/alloyASTGenerator";
		return "C:/Users/Felipe/Documents/Projects/Java/experiments/alloy/eclipse/pushdownmethod/";
	}

	@Override
	public String getDescription() {
		return "Two classes A and B. A declares a method and B references it in some expression. B extends A. Refactor to push m down to B.";
	}

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		String pluginFolder = refactoringtestplugin.Activator.getDefault().getPluginFolder();
		// System.out.println(pluginFolder);
		// JDolly jdolly = new JDollyNoField(pluginFolder +
		// "/../program_counter/alloyTheory/pushdownmethod_final.als",
		// 2, 3, 4);
		return new JDollyImp(
				"C:/Users/Felipe/Documents/Projects/Java/experiments/alloy/program_counter/alloyTheory/pushdownmethod_final.als", 2, 4, 5,
				0);
	}

}
