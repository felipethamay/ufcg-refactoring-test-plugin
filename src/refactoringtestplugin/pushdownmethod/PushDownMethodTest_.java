package refactoringtestplugin.pushdownmethod;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.structure.PushDownRefactoringProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import refactoringtestplugin.RefactoringTest;

public abstract class PushDownMethodTest_ extends RefactoringTest {

	// protected static final String METHOD_TO_REFACTOR = "m";
	// protected static final String CLASS_NAME = "A";
	// protected static final String CLASS_NAME2 = "A";
	public static String[] METHOD_TO_REFACTOR = { "m" };
	public static String CLASS_NAME = "B";
	public static boolean deleteAllInSourceType;
	public static boolean deleteAllMatchingMethods;

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
		// for (ICompilationUnit icu : icus) {
		// for (IType type : icu.getAllTypes()) {
		// if (type.getElementName().equals(CLASS_NAME)) {
		// for (IMethod method : type.getMethods()) {
		// if (method.getElementName().equals(METHOD_TO_REFACTOR)) {
		// return new IMember[] {method};
		// }
		// }
		// }
		// }
		// }
		//
		// for (ICompilationUnit icu : icus) {
		// for (IType type : icu.getTypes()) {
		// if (type.getElementName().equals(CLASS_NAME2)) {
		// for (IMethod method : type.getMethods()) {
		// if (method.getElementName().equals(METHOD_TO_REFACTOR)) {
		// return new IMember[] {method};
		// }
		// }
		// }
		// }
		// }
		List<IMember> members = new ArrayList<IMember>();
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getAllTypes()) {
				if (type.getElementName().equals(CLASS_NAME)) {
					for (IMethod method : type.getMethods()) {
						for (String iMember : METHOD_TO_REFACTOR) {
							if (method.getElementName().equals(iMember)) {
								members.add((IMember) method);
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

		throw new RuntimeException("Method " + METHOD_TO_REFACTOR + " was not found"); // TODO:
																						// better
																						// exception
	}

}
