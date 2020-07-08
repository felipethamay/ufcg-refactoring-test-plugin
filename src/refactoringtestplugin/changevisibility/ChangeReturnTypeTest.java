package refactoringtestplugin.changevisibility;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.corext.refactoring.structure.ChangeSignatureProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import refactoringtestplugin.classes.RefactoringTest_;

public abstract class ChangeReturnTypeTest extends RefactoringTest_ {

	private static final String TARGET_METHOD_NAME = "k_0";

	@Override
	protected String getLogDirPath() {
		return getSystemTempDir() + "/changevisibility";
	}

	@Override
	protected String getRefactoredClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus)
			throws JavaModelException {
		ChangeSignatureProcessor processor = new ChangeSignatureProcessor(
				findTargetMethod(icus));
		Refactoring refactoring = new ProcessorBasedRefactoring(processor);

		int modifier = processor.getVisibility();
		if (Modifier.isPrivate(modifier) || Modifier.isProtected(modifier))
			modifier = Modifier.PUBLIC;
		else if (Modifier.isPublic(modifier))
			modifier = Modifier.PRIVATE;

		// String returnType = processor.getReturnTypeString();
		// String newReturnType;
		// if (returnType.equals("void")) {
		// // add a return type if none exists
		// newReturnType = "Object";
		// }
		// else if (returnType.equals("Object")){
		// // remove return type
		// newReturnType = "void";
		// }
		// else if (returnType.equals("Integer")){
		// // narrow return type
		// newReturnType = "Byte";
		// }
		// else {
		// // generalize return type
		// newReturnType = "Object";
		// }
		// processor.setNewReturnTypeName(newReturnType);
		processor.setVisibility(modifier);

		return refactoring;

	}

	private IMethod findTargetMethod(List<ICompilationUnit> icus)
			throws JavaModelException {
		// this only works because the generator will always put the target
		// method first in the class, even if there is another method
		// with the same name that references it.
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
				for (IMethod method : type.getMethods()) {
					if (method.getElementName().equals(TARGET_METHOD_NAME)) {
						int flags = method.getFlags();
						if (flags == Modifier.PRIVATE)
							return method;
//						String[] parameterTypes = method.getParameterTypes();
//						if (parameterTypes[0].equals("I"))
							
					}
				}
			}
		}
		throw new RuntimeException("Method " + TARGET_METHOD_NAME
				+ " was not found"); // TODO: better exception

	}

}
