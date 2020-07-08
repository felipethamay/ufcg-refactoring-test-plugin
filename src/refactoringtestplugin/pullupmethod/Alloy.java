package refactoringtestplugin.pullupmethod;

import jdolly.JDolly;
import jdolly.JDollyImp;

public class Alloy extends PullUpMethodTest {

	@Override
	protected String getLogDirPath() {
		// return super.getLogDirPath() + "/alloyASTGenerator";
		return "C:/Users/Felipe/Documents/Projects/Java/experiments/alloy/eclipse/pullupMethod/";
		// return
		// "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupmethod/last/";
	}

	@Override
	public String getDescription() {
		return "Two classes A and B. A declares a method and B references it in some expression. B extends A. Refactor to push m down to B.";
	}

	@Override
	protected JDolly getCUGen() {
		String pluginFolder = refactoringtestplugin.Activator.getDefault().getPluginFolder();
		System.out.println(pluginFolder);
		return new JDollyImp(
				"C:/Users/Felipe/Documents/Projects/Java/experiments/alloy/program_counter/alloyTheory/pullupmethod_final.als", 2, 3, 4,
				0);

	}

}
