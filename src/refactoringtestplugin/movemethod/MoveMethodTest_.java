package refactoringtestplugin.movemethod;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;

import refactoringtestplugin.RefactoringTest;

public abstract class MoveMethodTest_ extends RefactoringTest {

	// private static final String TARGET_CLASS_NAME = "ClassId_0";
	// private static final String TARGET_METHOD_NAME = "m_0";
	// private static final String CLASS_NAME = "A_0";

	// para onde o metodo vai
	// private static String TARGET_CLASS_NAME = "C";
	public static String TARGET_METHOD_NAME = "m";
	// onde está o metodo
	public static String CLASS_NAME = "X";

	protected String getLogDirPath() {
		return getSystemTempDir() + "/movemethod";
	}

	// @Override
	// protected String getRefactoredClassName() {
	// // TODO Auto-generated method stub
	// return TARGET_CLASS_NAME;
	// }

	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus) throws JavaModelException {
		// Under Development 4/16/07

		IMember[] findMember = findMember(icus);
		IMethod method = (IMethod) findMember[0];

		MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(method,
				JavaPreferencesSettings.getCodeGenerationSettings(icus.get(0).getJavaProject()));
		Refactoring ref = new MoveRefactoring(processor);
		try {
			ref.checkInitialConditions(new NullProgressMonitor());
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IVariableBinding target = null;
		IVariableBinding[] targets = processor.getPossibleTargets();
		for (int i = 0; i < targets.length; i++) {
			IVariableBinding candidate = targets[i];
			// if (candidate.getName().equals(newTargetName) &&
			// typeMatches("C2_0", candidate)) {
			target = candidate;
			break;
		}
		if (target != null)
			processor.setTarget(target);
		// processor.setTargetName("f_0");
		// processor.setMethodName("k_0");
		processor.setInlineDelegator(true);
		processor.setRemoveDelegator(true);
		processor.setDeprecateDelegates(false);
		//

		return ref;
		// MoveStaticMembersProcessor processor =
		// new MoveStaticMembersProcessor(
		// findMember(icu),
		// JavaPreferencesSettings.getCodeGenerationSettings(getProject()));
		// processor.setDestinationTypeFullyQualifiedName("p.A");
		// MoveRefactoring refactoring = new MoveRefactoring(processor);
		//
		// return refactoring;
	}

	private IMember[] findMember(List<ICompilationUnit> icus) throws JavaModelException {
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getAllTypes()) {
				if (type.getElementName().equals(CLASS_NAME)) {
					for (IMethod method : type.getMethods()) {
						if (method.getElementName().equals(TARGET_METHOD_NAME)) {
							// DESCOMENTAR ISSO AQUI DEPOIS
							// String[] parameterTypes =
							// method.getParameterTypes();
							// if (parameterTypes.length > 0 &&
							// parameterTypes[0].equals("I"))
							return new IMember[] { method };
						}
					}
				}
			}

		}

		// for (ICompilationUnit icu : icus) {
		// for (IType type : icu.getAllTypes()) {
		// for (IMethod method : type.getMethods()) {
		// if (method.getSource() != null) {
		//
		// int count = 1;
		// while (true) {
		// IType type2 = method.getType("0", 1);
		//
		// if (type2 == null) {
		// break;
		// } else {
		// count ++;
		// }
		// if (type2.getElementName().equals(CLASS_NAME)) {
		// for (IMethod method2 : type2.getMethods()) {
		// if (method2.getElementName().equals(TARGET_METHOD_NAME)) {
		// //DESCOMENTAR ISSO AQUI DEPOIS
		// //String[] parameterTypes = method.getParameterTypes();
		// //if (parameterTypes.length > 0 && parameterTypes[0].equals("I"))
		// return new IMember[] { method2 };
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }

		throw new RuntimeException("Method " + TARGET_METHOD_NAME + " not found");
	}

	// private IType findTargetType(ICompilationUnit icu)
	// throws JavaModelException {
	// for (IType type : icu.getTypes()) {
	// if (type.getElementName().equals(TARGET_CLASS_NAME)) {
	// return type;
	// }
	// for (IType innerType : type.getTypes()) {
	// if (innerType.getElementName().equals(TARGET_CLASS_NAME)) {
	// return type;
	// }
	// for (IMethod method : innerType.getMethods()) {
	// try {
	// return method.getType(TARGET_CLASS_NAME, 1);
	// } catch (Exception e) {
	// // do nothing, local type does not exist
	// }
	// }
	// }
	// for (IMethod method : type.getMethods()) {
	// try {
	// return method.getType(TARGET_CLASS_NAME, 1);
	// } catch (Exception e) {
	// // do nothing, local type does not exist
	// }
	// }
	// }
	// throw new RuntimeException("Class " + TARGET_CLASS_NAME + " not found");
	// }

}
