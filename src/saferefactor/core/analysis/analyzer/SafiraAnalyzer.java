package saferefactor.core.analysis.analyzer;

import java.util.List;

import saferefactor.core.analysis.Report;
import saferefactor.core.analysis.naive.NaiveReport;
import saferefactor.core.analysis.safira.analyzer.ImpactAnalysis;
import saferefactor.core.util.Project;
import saferefactor.core.util.ast.Clazz;
import saferefactor.core.util.ast.Method;

public class SafiraAnalyzer implements TransformationAnalyzer {

	private List<Clazz> sourceClasses;
	private List<Clazz> targetClasses;
	private final Project source;
	private final Project target;
	private final String tmpDir;
	private String bin = "";
	private ImpactAnalysis ia;

	public SafiraAnalyzer(Project source, Project target, String tmpDir) {
		this.source = source;
		this.target = target;
		this.tmpDir = tmpDir;
		this.bin = source.getBin();

	}

	public Report analyze() throws Exception {
		
		Report result = new NaiveReport();

		ia = new ImpactAnalysis(source.getProjectFolder().getAbsolutePath(), 
				target.getProjectFolder().getAbsolutePath(), bin);

		List<Method> methods_to_test = ia.getMethods_to_test();
		result.setMethodsToTest(methods_to_test);

		return result;
		
	}

	public ImpactAnalysis getIa() {
		return ia;
	}

	public void setIa(ImpactAnalysis ia) {
		this.ia = ia;
	}

	

	
}
