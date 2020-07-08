package refactoringtestplugin.change_super_to_this;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;

import br.edu.dsc.ufcg.jdolly.JDolly;
import br.edu.dsc.ufcg.jdolly.JDollyNoField;



public class Alloy extends Change_Super_To_This_Test {



	@Override
	protected String getLogDirPath() {
		return super.getLogDirPath() + "/alloyASTGenerator";
//		return "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupmethod/last/";
	}

	@Override
	public String getDescription() {
		return "Two classes A and B. A declares a method and B references it in some expression. B extends A. Refactor to push m down to B.";
	}
	
	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		String pluginFolder = refactoringtestplugin.Activator.getDefault().getPluginFolder();
		System.out.println(pluginFolder);
		JDolly jdolly = new JDollyNoField(pluginFolder + 
				"/../program_counter/alloyTheory3/change_super_to_this.als",
				1, 2, 4);
		return jdolly;
	}
}
