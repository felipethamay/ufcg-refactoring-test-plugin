package refactoringtestplugin.encapsulatefield;

import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.sef.SelfEncapsulateFieldRefactoring;
import org.eclipse.ltk.core.refactoring.Refactoring;

import refactoringtestplugin.ProblemReport;
import refactoringtestplugin.RefactoringTest;

public abstract class EncapsulateFieldTest extends RefactoringTest {
	
	protected static final String FIELD_TO_REFACTOR = "fieldid_0";
	protected static final String CLASS_NAME = "ClassId_1";
	
	protected static final String CLASS_NAME0 = "ClassId_0";
	protected static final String CLASS_NAME2 = "ClassId_2";

//	protected static final String FIELD_TO_REFACTOR = "field";
//	protected static final String CLASS_NAME = "TestPostfixExpression";
	
	// @Override
	// protected String getLogDirPath() {
	// return getSystemTempDir() + "/encapsulatefield";
	// }

	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus) throws JavaModelException {
		IField field = findIField(icus, FIELD_TO_REFACTOR);
		SelfEncapsulateFieldRefactoring refactoring = new SelfEncapsulateFieldRefactoring(field);
		String name = field.getElementName();
		 name = Character.toUpperCase(name.charAt(0)) + name.substring(1);

		refactoring.setGetterName("get" + name);
		refactoring.setSetterName("set" + name);
		refactoring.setVisibility(Flags.AccPublic);

		return refactoring;
	}

	@Override
	protected String getRefactoredClassName() {
		// TODO Auto-generated method stub
		return CLASS_NAME;
	}

	private IField findIField(List<ICompilationUnit> icus, String fieldName) throws JavaModelException {

		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
				if (type.getFullyQualifiedName().contains(CLASS_NAME))
					for (IField field : type.getFields()) {
						if (field.getElementName().equals(fieldName)) {
							return field;
						}
					}

			}
		}

		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
				if (type.getFullyQualifiedName().contains(CLASS_NAME2))
					for (IField field : type.getFields()) {
						if (field.getElementName().equals(fieldName)) {
							return field;
						}
					}

			}
		}
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
				if (type.getFullyQualifiedName().contains(CLASS_NAME0))
					for (IField field : type.getFields()) {
						if (field.getElementName().equals(fieldName)) {
							return field;
						}
					}

			}
		}

		throw new RuntimeException("Field " + fieldName + " was not found"); // TODO:
																				// better
																				// exception
	}

	@Override
	protected ProblemReport checkCustomPostconditions(ICompilationUnit icu) throws Exception {
		String source = icu.getSource();
		int count = 0;
		int position = 0;
		while (true) {
			position = source.indexOf(FIELD_TO_REFACTOR, position);
			if (position >= 0) {
				count++;
				position++;
			} else {
				break;
			}
		}
		if (count > 5) {
			return new ProblemReport("A field was not encapsulated");
		}
		return super.checkCustomPostconditions(icu);
	}
}
