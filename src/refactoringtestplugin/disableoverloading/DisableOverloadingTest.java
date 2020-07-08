package refactoringtestplugin.disableoverloading;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.ParameterInfo;
import org.eclipse.jdt.internal.corext.refactoring.structure.ChangeSignatureProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import refactoringtestplugin.classes.RefactoringTest_;

public abstract class DisableOverloadingTest extends RefactoringTest_ {

	protected static final String TARGET_METHOD_NAME = "methodid";
	protected static final String CLASS_NAME = "ClassId";
	
	@Override
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
		ChangeSignatureProcessor processor = new ChangeSignatureProcessor(findTargetMethod(icus));
		Refactoring refactoring = new ProcessorBasedRefactoring(processor);
		
		List parameterInfos = processor.getParameterInfos();
		for (Object object : parameterInfos) {
			ParameterInfo param = (ParameterInfo) object;
			param.markAsDeleted();
		}
		//ParameterInfo param =ParameterInfo.createInfoForAddedParameter("int","i","0");

		
		
		//processor.getParameterInfos().add(param);
		
		

//		for (Object object : parameterInfos) {
//		ParameterInfo param1 = (ParameterInfo) object;
//
//		System.out.println(param1.getOldName());
//		System.out.println(param1.getNewName());
//		System.out.println(param1.isAdded());
//		System.out.println(param1.isRenamed());
//		System.out.println(param1.getOldIndex());
//		System.out.println("------------------");
//		}
		

		
		return refactoring;
	}

	private IMethod findTargetMethod(List<ICompilationUnit> icus) throws JavaModelException {
		// this only works because the generator will always put the target
		// method first in the class, even if there is another method
		// with the same name that references it.
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
//				if (type.getFullyQualifiedName().endsWith(CLASS_NAME))
				for (IMethod method : type.getMethods()) {
			
					if (method.getParameterTypes().length == 1) {
						//System.out.println(method.getParameterTypes()[0]);
//						if (method.getParameterTypes()[0].equals("I"))
						return method;
					}
				}
			}
		}
		
		throw new RuntimeException("Method " + TARGET_METHOD_NAME + " was not found"); // TODO: better exception

	}


}
