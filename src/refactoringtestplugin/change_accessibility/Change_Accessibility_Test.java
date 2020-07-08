package refactoringtestplugin.change_accessibility;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.structure.PullUpRefactoringProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import refactoringtestplugin.classes.RefactoringTest_;

public abstract class Change_Accessibility_Test extends RefactoringTest_ {

	protected static final String METHOD_TO_REFACTOR = "m_0";
	protected static final String CLASS_NAME = "ClassId_0";

	@Override
	protected String getLogDirPath() {
		return super.getSystemTempDir() + "/pullupmethod";
	}

	@Override
	protected String getRefactoredClassName() {
		return CLASS_NAME;
	}

	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus)
			throws JavaModelException {
		final IMember[] members = findMembers(icus);
		PullUpRefactoringProcessor processor = new PullUpRefactoringProcessor(
				members, JavaPreferencesSettings
						.getCodeGenerationSettings(members[0].getJavaProject()));
		// JavaPreferencesSettings.getCodeGenerationSettings(getProject()));
//		IType type = getType(icus, CLASS_NAME);
//		IMethod[] methods = getMethods(type,
//				new String[] { METHOD_TO_REFACTOR },
//				new String[][] { new String[0] });
		IMethod[] methods = new IMethod[] { (IMethod) members[0] };
		processor.setDeletedMethods(methods);
		Refactoring refactoring = new ProcessorBasedRefactoring(processor);
		setSuperclassAsTargetClass(processor);
		return refactoring;
	}

	/* Copied from org.eclipse.jdt.ui.tests.refactoring.PullUpTests. */
	private IType[] getPossibleTargetClasses(PullUpRefactoringProcessor ref)
			throws JavaModelException {
		return ref.getCandidateTypes(new RefactoringStatus(),
				new NullProgressMonitor());
	}

	private void setSuperclassAsTargetClass(PullUpRefactoringProcessor ref)
			throws JavaModelException {
		IType[] possibleClasses = getPossibleTargetClasses(ref);
		ref.setDestinationType(possibleClasses[possibleClasses.length - 1]);
	}

	/* End copy. */

	private IMember[] findMembers(List<ICompilationUnit> icus)
			throws JavaModelException {
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
				for (IMethod method : type.getMethods()) {
					if (method.getElementName().equals(METHOD_TO_REFACTOR)) {
						//String[] parameterTypes = method.getParameterTypes();
						//if (parameterTypes[0].equals("I"))
							return new IMember[] { method };
					}
				}
			}
			// Kely add this part
//			for (IType type : icu.getTypes()) {
//				IType[] listOfTypes = type.getTypes();
//				for (IType subType : listOfTypes) {
//					for (IMethod method : subType.getMethods()) {
//						if (method.getElementName().equals(METHOD_TO_REFACTOR)) {
//							String[] parameterTypes = method.getParameterTypes();
//							if (parameterTypes[0].equals("I"))
//								return new IMember[] { method };
//						}
//					}
//				}
//			}

//			for (IType type : icu.getTypes()) {
//				IMethod[] listOfMethods = type.getMethods();
//				for (IMethod method : listOfMethods) {
//					IType localType = method.getType(CLASS_NAME, 1);
//					for (IMethod subMethod : localType.getMethods()) {
//						if (subMethod.getElementName().equals(
//								METHOD_TO_REFACTOR)) {
//							String[] parameterTypes = method.getParameterTypes();
//							if (parameterTypes[0].equals("I"))
//								return new IMember[] { method };
//						}
//					}
//				}
//			}
			
		}
		throw new RuntimeException("Method " + METHOD_TO_REFACTOR
				+ " was not found"); // TODO: better exception
	

	}

	public static IMethod[] getMethods(IType type, String[] names,
			String[][] signatures) throws JavaModelException {
		if (names == null || signatures == null)
			return new IMethod[0];
		List methods = new ArrayList(names.length);
		for (int i = 0; i < names.length; i++) {
			IMethod method = type.getMethod(names[i], signatures[i]);
			// assertTrue("method " + method.getElementName() +
			// " does not exist", method.exists());
			if (!methods.contains(method))
				methods.add(method);
		}
		return (IMethod[]) methods.toArray(new IMethod[methods.size()]);
	}

	protected IType getType(List<ICompilationUnit> icus, String name)
			throws JavaModelException {
		name = "P2_0.Class2_0";
		for (ICompilationUnit cu : icus) {
			IType[] types = cu.getAllTypes();
			for (int i = 0; i < types.length; i++) {
				String fullyQualifiedName = types[i].getFullyQualifiedName();
				if (fullyQualifiedName.equals(name)
						|| types[i].getElementName().equals(name))
					return types[i];
			}

		
		}
		

		return null;
	}

}
