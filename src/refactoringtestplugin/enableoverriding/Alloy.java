package refactoringtestplugin.enableoverriding;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;

import br.edu.dsc.ufcg.jdolly.JDollyNoField;








public class Alloy extends EnableOverridingTest {



	@Override
	protected String getLogDirPath() {
		return super.getLogDirPath() + "/alloyASTGenerator";
//		return "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/addparameter/last/";
	}

	@Override
	public String getDescription() {
		return "Two classes A and B. A declares a method and B references it in some expression. B extends A. Refactor to push m down to B.";
	}

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		String pluginFolder = refactoringtestplugin.Activator.getDefault().getPluginFolder();
		return new JDollyNoField(pluginFolder + 
				"/../program_counter/alloyTheory3/enableOverriding.als", 1, 2,
				3);
	}

	
}