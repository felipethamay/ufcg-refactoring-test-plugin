package refactoringtestplugin.enableoverloading;

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

public abstract class EnableOverloadingTest extends RefactoringTest_ {

	protected static final String TARGET_METHOD_NAME = "methodid_0";
	protected static final String CLASS_NAME = "Class1_0";
	
	@Override
	protected String getLogDirPath() {
		return getSystemTempDir() + "/enableoverriding";
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
		
//		for (Object object : parameterInfos) {
//			ParameterInfo param = (ParameterInfo) object;
//			param.markAsDeleted();
//		}
		ParameterInfo param =ParameterInfo.createInfoForAddedParameter("int","i","0");

		
		
		processor.getParameterInfos().add(param);
		
		List parameterInfos = processor.getParameterInfos();

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
				for (IMethod method : type.getMethods()) {
					
					if (method.getParameterNames().length == 0 && method.getElementName().equals(TARGET_METHOD_NAME)) {
						return method;
					}
				}
			}
		}
		
		throw new RuntimeException("Method " + TARGET_METHOD_NAME + " was not found"); // TODO: better exception

	}


}
