package refactoringtestplugin.addparameter;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.ParameterInfo;
import org.eclipse.jdt.internal.corext.refactoring.structure.ChangeSignatureProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import refactoringtestplugin.RefactoringTest;

public abstract class AddParameterTest_ extends RefactoringTest {

	// protected static final String TARGET_METHOD_NAME = "methodid_0";
	// protected static final String CLASS_NAME = "Class1_0";
	// protected static final String TARGET_METHOD_NAME = "m";
	// protected static final String CLASS_NAME = "A";

	public static boolean createDelegate;
	public static String METHOD = "k";
	public static String CLASS_NAME = "A";

	public static String[] newTypes;
	public static String[] newNames;
	public static String[] newDefaultValues;
	public static String[] signature;
	public static int[] newIndices;

	// @Override
	protected String getLogDirPath() {
		return getSystemTempDir() + "/removeparameter";
	}

	@Override
	protected String getRefactoredClassName() {
		// TODO Auto-generated method stub
		return CLASS_NAME;
	}

	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus) throws JavaModelException {

		String className = CLASS_NAME;
		IMethod targetMethod = findTargetMethod(icus, className);
		// if (targetMethod == null) {
		// className = "ClassId_1";
		// targetMethod = findTargetMethod(icus, className);
		// if (targetMethod == null) {
		// className = "ClassId_2";
		// targetMethod = findTargetMethod(icus, className);
		// if (targetMethod == null) throw new RuntimeException("Method " +
		// TARGET_METHOD_NAME + " was not found"); // TODO: better exception
		// }
		// }
		ChangeSignatureProcessor processor = new ChangeSignatureProcessor(targetMethod);

		Refactoring refactoring = new ProcessorBasedRefactoring(processor);

		// descomentar aqui depois - melina
		// ParameterInfo param
		// =ParameterInfo.createInfoForAddedParameter("int","i","0");
		// processor.getParameterInfos().add(param);
		// List parameterInfos = processor.getParameterInfos();

		// 5
		// String[] signature= {"QE;"};
		// String[] newNames= {"e2"};
		// String[] newTypes= {"E"};
		// String[] newDefaultValues= {"null"};
		// ParameterInfo[] newParamInfo= createNewParamInfos(newTypes, newNames,
		// newDefaultValues);
		// int[] newIndices= {1};

		ParameterInfo[] newParamInfo = createNewParamInfos(newTypes, newNames, newDefaultValues);
		// int[] newIndices= {0};

		for (int i = newIndices.length - 1; i >= 0; i--) {
			processor.getParameterInfos().add(newIndices[i], newParamInfo[i]);
		}
		// for (ParameterInfo param : newParamInfo) {
		// processor.getParameterInfos().add(param);
		// }
		processor.setDelegateUpdating(this.createDelegate);

		List parameterInfos = processor.getParameterInfos();

		return refactoring;
	}

	static ParameterInfo[] createNewParamInfos(String[] newTypes, String[] newNames, String[] newDefaultValues) {
		if (newTypes == null)
			return new ParameterInfo[0];
		ParameterInfo[] result = new ParameterInfo[newTypes.length];
		for (int i = 0; i < newDefaultValues.length; i++) {
			result[i] = ParameterInfo.createInfoForAddedParameter(newTypes[i], newNames[i], newDefaultValues[i]);
		}
		return result;
	}

	private IMethod findTargetMethod(List<ICompilationUnit> icus, String className) throws JavaModelException {
		// this only works because the generator will always put the target
		// method first in the class, even if there is another method
		// with the same name that references it.
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getAllTypes()) {
				for (IMethod method : type.getMethods()) {
					if (method.getElementName().equals(METHOD) && type.getElementName().equals(className)) {
						// if (method.getParameterNames().length == 0 &&
						// method.getElementName().equals(TARGET_METHOD_NAME) &&
						// type.getElementName().equals(className)) {
						return method;
					}
				}
			}
		}
		return null;
		// throw new RuntimeException("Method " + TARGET_METHOD_NAME + " was not
		// found"); // TODO: better exception

	}

}
