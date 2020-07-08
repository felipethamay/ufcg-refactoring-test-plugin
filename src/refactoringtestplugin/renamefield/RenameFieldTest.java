package refactoringtestplugin.renamefield;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameFieldProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import refactoringtestplugin.ProblemReport;
import refactoringtestplugin.RefactoringTest;

public abstract class RenameFieldTest extends RefactoringTest {
	protected static final String FIELD_TO_REFACTOR = "fieldid_0";
	protected static final String NEW_FIELD_NAME = "fieldid_1";
	
	/**
	 * Pre-refactoring compilation unit used to check reverse refactoring
	 */
	private String originalCUSource;
	
	@Override
	protected String getLogDirPath() {
		return getSystemTempDir() + "/renamefield";
	}
	
	@Override
	protected String getRefactoredClassName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus) throws JavaModelException {
		
		IField field = findIField(icus, FIELD_TO_REFACTOR);		
		RenameFieldProcessor processor = new RenameFieldProcessor(field);
		RenameRefactoring refactoring = new RenameRefactoring(processor);
		processor.setNewElementName(NEW_FIELD_NAME);
		return refactoring;
	}
	
	private IField findIField(List<ICompilationUnit> icus, String fieldName) throws JavaModelException{
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
				for (IField field : type.getFields()) {
					if (field.getElementName().equals(fieldName)) {
						return field;
					}
				}
			}
		}		
		throw new RuntimeException("Field " + fieldName + " was not found"); // TODO: better exception
	}
	
	@Override
	protected ICompilationUnit createICompilationUnit(IPackageFragmentRoot packFragRoot, CompilationUnit cu, String cuName) throws JavaModelException {
		ICompilationUnit originalCU = super.createICompilationUnit(packFragRoot, cu, cuName);
		originalCUSource = originalCU.getSource();
		return originalCU;
	}
	
//	@Override
//	protected ProblemReport checkCustomPostconditions(ICompilationUnit icu) throws Exception {
//		performReverseRefactoring(icu);
//		if (!icu.getSource().equals(originalCUSource)) {
//			return new ProblemReport("Reverse rename refactoring does not yield original compilation unit");
//		}
//		return new ProblemReport();
//	}
//
//	private void performReverseRefactoring(ICompilationUnit icu) throws Exception {
//		IField field = findIField(icu, NEW_FIELD_NAME);		
//		RenameFieldProcessor processor = new RenameFieldProcessor(field);
//		RenameRefactoring refactoring = new RenameRefactoring(processor);
//		processor.setNewElementName(FIELD_TO_REFACTOR);
//		performRefactoring(refactoring);			
//	}
}
