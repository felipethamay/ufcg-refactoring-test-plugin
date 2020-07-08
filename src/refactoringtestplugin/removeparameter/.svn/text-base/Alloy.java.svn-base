package refactoringtestplugin.removeparameter;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;

import br.edu.dsc.ufcg.jdolly.JDolly;
import br.edu.dsc.ufcg.jdolly.JDollyNoField;
import br.edu.dsc.ufcg.jdolly.JDollyWithField;



public class Alloy extends RemoveParameterTest {



	@Override
	protected String getLogDirPath() {
		return super.getLogDirPath() + "/alloyASTGenerator";
	}

	@Override
	public String getDescription() {
		return "Two classes A and B. A declares a method and B references it in some expression. B extends A. Refactor to push m down to B.";
	}

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {

	String pluginFolder = refactoringtestplugin.Activator.getDefault().getPluginFolder();
		
		
		return new JDollyNoField(pluginFolder + 
				"/../program_counter/alloyTheory/removeparameter_idioma_novo.als", 2,
				3, 3);
		}


	
}
