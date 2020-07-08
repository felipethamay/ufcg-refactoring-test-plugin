package refactoringtestplugin.renamemethod;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.internal.core.refactoring.descriptors.RefactoringSignatureDescriptorFactory;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameNonVirtualMethodProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import refactoringtestplugin.ProblemReport;
import refactoringtestplugin.RefactoringTest;

public abstract class RenameMethodTest_ extends RefactoringTest {

	public static boolean updateRef = true;
	public static boolean createDelegate = false;
	public static String METHOD_TO_RENAME = "m";
	public static String NEW_METHOD_NAME = "k";
	public static String CLASS_NAME = "I";
//	protected static final String METHOD_TO_RENAME = "n_0";
//	protected static final String NEW_METHOD_NAME = "k_0";
	
//	@Override
	protected String getLogDirPath() {
		return super.getSystemTempDir() + "/renamemethod";
	}

	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus)
	throws JavaModelException {
		
		RenameJavaElementDescriptor processor = RefactoringSignatureDescriptorFactory.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_METHOD);
		processor.setJavaElement(findMethod(icus));
		processor.setUpdateReferences(this.updateRef);
		processor.setNewName(NEW_METHOD_NAME);
		processor.setKeepOriginal(createDelegate);
		processor.setDeprecateDelegate(true);
		
		RefactoringStatus status= new RefactoringStatus();
		Refactoring refactoring = null;
		try {
			refactoring = processor.createRefactoring(status);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//				RenameNonVirtualMethodProcessor processor = new RenameNonVirtualMethodProcessor(findMethod(icus));
//		processor.setNewElementName(NEW_METHOD_NAME);
//		
//		processor.setUpdateReferences(this.updateRef);
//		System.out.println(this.updateRef +" update references");
//		processor.setDeprecateDelegates(true);
//		processor.setDelegateUpdating(this.createDelegate);
//		
//		
//		processor.checkNewElementName(NEW_METHOD_NAME);
//		
//		RenameRefactoring refactoring = new RenameRefactoring(processor);
//		processor.checkFinalConditions(new NullProgressMonitor(), context)
		return refactoring;
	}
	@Override
	protected String getRefactoredClassName() {
		// TODO Auto-generated method stub
		return null;
	}
	private IMethod findMethod(List<ICompilationUnit> icus) {
		try {
			for (ICompilationUnit icu : icus) {
				
				for (IType type : icu.getAllTypes()) {
					
					if (type.getElementName().equals(CLASS_NAME)) {
						for (IMethod method : type.getMethods()) {
							if (method.getElementName().equals(METHOD_TO_RENAME)
							 	/* && method.getNumberOfParameters() == 0 */) {
								return method;
							}
						}
					}
				}	
			}
			
		} 
		catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
		throw new RuntimeException("Method " + METHOD_TO_RENAME + " was not found"); 

	}
	
	@Override
	protected ProblemReport checkCustomPostconditions(ICompilationUnit icu) throws Exception {
		// TODO: Check that all method names have been replaced (and not the referencing method)
		return new ProblemReport();
	}

}
