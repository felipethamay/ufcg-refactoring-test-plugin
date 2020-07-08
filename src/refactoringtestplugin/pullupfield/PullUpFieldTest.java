package refactoringtestplugin.pullupfield;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.structure.PullUpRefactoringProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import refactoringtestplugin.RefactoringTest;

public abstract class PullUpFieldTest extends RefactoringTest {

	protected static final String FIELD_TO_REFACTOR = "fieldid_0";
	protected static final String CLASS_NAME = "ID1_0";
	
//	@Override
//	protected String getLogDirPath() {
//		return super.getSystemTempDir() + "/pullupfield";
////		return "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupfield/last/";
//	}
	@Override
	protected String getRefactoredClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus) throws JavaModelException {
		// Under Development 4/16/07
		//throw new NotImplementedException();
		final IMember[] members = findMembers(icus);
		PullUpRefactoringProcessor processor = 
			new PullUpRefactoringProcessor(
					members, JavaPreferencesSettings.getCodeGenerationSettings(members[0].getJavaProject()));
				//	JavaPreferencesSettings.getCodeGenerationSettings(getProject()));
		Refactoring refactoring = new ProcessorBasedRefactoring(processor);
        setSuperclassAsTargetClass(processor);
		return refactoring;
	}

	/* Copied from org.eclipse.jdt.ui.tests.refactoring.PullUpTests. */
	private IType[] getPossibleTargetClasses(PullUpRefactoringProcessor ref) throws JavaModelException{
		return ref.getCandidateTypes(new RefactoringStatus(), new NullProgressMonitor());
	}
	
	private void setSuperclassAsTargetClass(PullUpRefactoringProcessor ref) throws JavaModelException {
		IType[] possibleClasses= getPossibleTargetClasses(ref);
		ref.setDestinationType(possibleClasses[possibleClasses.length - 1]);
	}
    /* End copy. */

	private IMember[] findMembers(List<ICompilationUnit> icus) throws JavaModelException {
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
//				if (!type.getSuperclassName().equals("null")) {
				if (type.getElementName().endsWith(CLASS_NAME)) {
					for (IField field : type.getFields()) {
						if (field.getElementName().equals(FIELD_TO_REFACTOR)) {
							return new IMember[] {field};
						}
					}	
				}
//				}
				
			}
	
		}
		throw new RuntimeException("Field " + FIELD_TO_REFACTOR + " was not found"); // TODO: better exception
	}

}
