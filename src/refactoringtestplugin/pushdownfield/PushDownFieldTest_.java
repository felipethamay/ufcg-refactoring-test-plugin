package refactoringtestplugin.pushdownfield;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.structure.PushDownRefactoringProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import refactoringtestplugin.RefactoringTest;

public abstract class PushDownFieldTest_ extends RefactoringTest {

	// protected static final String FIELD_TO_REFACTOR = "i";
	// protected static final String CLASS_NAME = "A";
	// \ protected static final String FIELD_TO_REFACTOR = "fieldid_0";
	// protected static final String CLASS_NAME = "A_0";

	public static String[] fields;
	public static boolean deleteAllInSourceType;
	public static boolean deleteAllMatchingMethods;
	public static String CLASS_NAME = "A";

	// @Override
	protected String getLogDirPath() {
		return super.getSystemTempDir() + "/pushdownfield";
	}

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
		// for (ICompilationUnit icu : icus) {
		// for (IType type : icu.getTypes()) {
		// if (type.getElementName().equals(CLASS_NAME))
		// for (IField field : type.getFields()) {
		// if (field.getElementName().equals(FIELD_TO_REFACTOR)) {
		// return new IMember[] {field};
		// }
		// }
		// for (IField field : type.getFields()) {
		// if (field.getElementName().equals("bar")) {
		// return new IMember[] {field};
		// }
		// }
		// for (IField field : type.getFields()) {
		// if (field.getElementName().equals("f")) {
		// return new IMember[] {field};
		// }
		// }
		// }
		// }

		List<IMember> members = new ArrayList<IMember>();
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getAllTypes()) {
				if (type.getElementName().equals(CLASS_NAME)) {
					for (IField field : type.getFields()) {
						for (String iMember : fields) {
							if (field.getElementName().equals(iMember)) {
								members.add((IMember) field);
							}
						}
						// DESCOMENTAR AQUI
						// if
						// (method.getElementName().equals(METHOD_TO_REFACTOR))
						// {
						// //DESCOMENTAR ISSO AQUI DEPOIS
						// //String[] parameterTypes =
						// method.getParameterTypes();
						// //if (parameterTypes.length > 0 &&
						// parameterTypes[0].equals("I"))
						// return new IMember[] { method };
						// }
					}
				}
			}

		}
		if (members.size() > 0) {
			IMember[] mm = new IMember[members.size()];
			int i = 0;
			for (IMember iMember : members) {
				mm[i] = iMember;
				i++;
			}
			return mm;
		}

		throw new RuntimeException("Field " + fields.toString() + " was not found"); // TODO:
																						// better
																						// exception
	}

}
