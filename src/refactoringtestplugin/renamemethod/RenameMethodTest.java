package refactoringtestplugin.renamemethod;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameMethodProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameNonVirtualMethodProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import org.testorrery.IGenerator;
import org.testorrery.Literal;
import refactoringtestplugin.ProblemReport;
import refactoringtestplugin.RefactoringTest;

public abstract class RenameMethodTest extends RefactoringTest {

	protected static final String METHOD_TO_RENAME = "n_0";
	protected static final String NEW_METHOD_NAME = "k_0";
	
//	@Override
//	protected String getLogDirPath() {
//		return super.getSystemTempDir() + "/renamemethod";
//	}

	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus)
	throws JavaModelException {
		
		RenameNonVirtualMethodProcessor processor = new RenameNonVirtualMethodProcessor(findMethod(icus));
		processor.setNewElementName(NEW_METHOD_NAME);
		
		
		processor.setUpdateReferences(true);
		processor.checkNewElementName(NEW_METHOD_NAME);
//		processor.
		
		RenameRefactoring refactoring = new RenameRefactoring(processor);
//		processor.checkFinalConditions(new NullProgressMonitor(), context)
		return refactoring;
	}
	@Override
	protected String getRefactoredClassName() {
		// TODO Auto-generated method stub
		return null;
	}
	private IMethod findMethod(List<ICompilationUnit> icus) {
		try {
			for (ICompilationUnit icu : icus) {
				for (IType type : icu.getTypes()) {
					for (IMethod method : type.getMethods()) {
						if (method.getElementName().equals(METHOD_TO_RENAME)
						 	/* && method.getNumberOfParameters() == 0 */) {
							return method;
						}
					}
				}	
			}
			
		} 
		catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
		throw new RuntimeException("Method " + METHOD_TO_RENAME + " was not found"); 

	}
	
	@Override
	protected ProblemReport checkCustomPostconditions(ICompilationUnit icu) throws Exception {
		// TODO: Check that all method names have been replaced (and not the referencing method)
		return new ProblemReport();
	}

}
