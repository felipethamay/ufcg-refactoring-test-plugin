package refactoringtestplugin;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.ltk.core.refactoring.RefactoringStatusEntry;

/**
 * Represents the status returned from a refactoring check.
 */
public class ProblemReport {

	private Collection<String> problems = new LinkedList<String>();
	
	public ProblemReport() {}
	
	public ProblemReport(String... problems) {
		for (String problem : problems) {
			addProblem(problem);
		}
	}

	public boolean hasProblems() {
		return problems.size() > 0;
	}

	public Iterable<String> getProblems() {
		return problems;
	}
	
	public void addProblem(String problem) {
		problems.add(problem);
	}

	public void addProblems(RefactoringStatusEntry[] entries) {
		for (RefactoringStatusEntry entry : entries) {
			addProblem(entry.getMessage());
		}
	}

	
}
