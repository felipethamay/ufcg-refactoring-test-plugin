package refactoringtestplugin.disableoverloading_move;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;

import br.edu.dsc.ufcg.jdolly.JDolly;
import br.edu.dsc.ufcg.jdolly.JDollyNoField;
import br.edu.dsc.ufcg.jdolly.JDollyWithField;



public class Alloy extends DisableOverloadingMoveTest {



	@Override
	protected String getLogDirPath() {
		return super.getLogDirPath() + "/alloyASTGenerator";
//		return "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/movemethod/last/";	
	}

	@Override
	public String getDescription() {
		return "Two classes A and B. A declares a method and B references it in some expression. B extends A. Refactor to push m down to B.";
	}

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		
		String pluginFolder = refactoringtestplugin.Activator.getDefault().getPluginFolder();
		
		
		return new JDollyNoField(pluginFolder + 
				"/../program_counter/alloyTheory3/disableoverloading_move.als", 1,
				3, 3);
	}


}
