package refactoringtestplugin.pushdownfield;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.corext.refactoring.structure.PushDownRefactoringProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import org.testorrery.IGenerator;
import refactoringtestplugin.RefactoringTest;

public abstract class PushDownFieldTest extends RefactoringTest {

	protected static final String FIELD_TO_REFACTOR = "fieldid_0";
	protected static final String CLASS_NAME = "A_0";
	
//	@Override
//	protected String getLogDirPath() {
//		return super.getSystemTempDir() + "/pushdownfield";
//	}

	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus) throws JavaModelException {
		PushDownRefactoringProcessor processor = new PushDownRefactoringProcessor(findMembers(icus));
		Refactoring refactoring = new ProcessorBasedRefactoring(processor);
		return refactoring;
	}

	@Override
	protected String getRefactoredClassName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private IMember[] findMembers(List<ICompilationUnit> icus) throws JavaModelException {
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
				if (type.getElementName().equals(CLASS_NAME))
				for (IField field : type.getFields()) {
					if (field.getElementName().equals(FIELD_TO_REFACTOR)) {
						return new IMember[] {field};
					}
				}
			}
		}
		
		throw new RuntimeException("Field " + FIELD_TO_REFACTOR + " was not found"); // TODO: better exception
	}

}
