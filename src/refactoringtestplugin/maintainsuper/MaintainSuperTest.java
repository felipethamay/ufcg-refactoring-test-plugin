package refactoringtestplugin.maintainsuper;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.structure.PushDownRefactoringProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import refactoringtestplugin.classes.RefactoringTest_;

public abstract class MaintainSuperTest extends RefactoringTest_ {

	protected static final String METHOD_TO_REFACTOR = "m_0";
	protected static final String CLASS_NAME = "ClassId_1";
	
	@Override
	protected String getLogDirPath() {
		return super.getSystemTempDir() + "/pushdownmethod";
	}

	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus) throws JavaModelException {
		PushDownRefactoringProcessor processor = new PushDownRefactoringProcessor(findMembers(icus));
		Refactoring refactoring = new ProcessorBasedRefactoring(processor);
		return refactoring;
	}
	@Override
	protected String getRefactoredClassName() {		
		return CLASS_NAME;
	}
	private IMember[] findMembers(List<ICompilationUnit> icus) throws JavaModelException {
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
//				if (type.getElementName().equals(CLASS_NAME)) {
					for (IMethod method : type.getMethods()) {
						if (method.getElementName().equals(METHOD_TO_REFACTOR)) {
							return new IMember[] {method};
						}
					}
//				}			
			}
		}
		
		throw new RuntimeException("Method " + METHOD_TO_REFACTOR + " was not found"); // TODO: better exception
	}

}

