package refactoringtestplugin.pushdownmethod_filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.testorrery.IGenerator;

import br.edu.dsc.ufcg.jdolly.JDolly;
import br.edu.dsc.ufcg.jdolly.JDollyNoField;
import br.edu.dsc.ufcg.jdolly.JDollyWithField;

import refactoringtestplugin.ModifyAlloyFile;
import refactoringtestplugin.ProblemReport;
import refactoringtestplugin.TestLogger;

public class Alloy extends PushDownMethodFiltersTest {
	
	List<String> filters = new ArrayList<String>();
	
	public Alloy() {
		filters.add("overriding[]");
//		filters.add("overloading[1,1]");
		filters.add("containSuper[M]");
//		filters.add("implicitCast[M]");
//		filters.add("containPrivateMethod[M]");
//		filters.add("shadowDeclaration[M]");
	}
	
	
	@Override
	protected String getLogDirPath() {
		return super.getLogDirPath() + "/alloyASTGenerator";
		// return
		// "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pushdownmethod/last/";
	}

	@Override
	public String getDescription() {
		return "Two classes A and B. A declares a method and B references it in some expression. B extends A. Refactor to push m down to B.";
	}

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		String pluginFolder = refactoringtestplugin.Activator.getDefault().getPluginFolder();
		System.out.println(pluginFolder);
		String path = pluginFolder + "/../program_counter/alloyTheory3/pushdownmethod_teste.als";
		try {
			ModifyAlloyFile maf = new ModifyAlloyFile(path);
			
			maf.addFilters(filters);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JDolly jdolly = new JDollyNoField(path,
				2, 3, 4);
		return jdolly;
	}

}
