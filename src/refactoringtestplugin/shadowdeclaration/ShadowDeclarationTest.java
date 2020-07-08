package refactoringtestplugin.shadowdeclaration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import refactoringtestplugin.ProblemReport;
import refactoringtestplugin.RefactoringTest;

public abstract class ShadowDeclarationTest extends RefactoringTest {
	protected final static String ORIGINAL_TYPE_NAME = "ClassId_2";
	protected final static String NEW_TYPE_NAME = "ClassId_0";
	protected final static String REFERENCING_TYPE_NAME = "B";
	
	@Override
	protected String getLogDirPath() {
		return getSystemTempDir() + "/renametype";
	}

	@Override
	public String getDescription() {
		return String.format("Rename type %s to %s. Any references in %s should be changed.", 
				ORIGINAL_TYPE_NAME, 
				NEW_TYPE_NAME, 
				REFERENCING_TYPE_NAME);
	}

	private PackageFragment savedParentPackage;
	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus) throws JavaModelException {
		
		ICompilationUnit cu = null;
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getTypes()) {
				if (type.getElementName().equals("ClassId_2")) {
					cu = icu;
					continue;
				}
						
			}
		}
		savedParentPackage = (PackageFragment)cu.getParent();
		RenameTypeProcessor processor = new RenameTypeProcessor(cu.getType(ORIGINAL_TYPE_NAME));
		RenameRefactoring refactoring = new RenameRefactoring(processor);
		processor.setNewElementName(NEW_TYPE_NAME);
		return refactoring;
	}
	
	@Override
	protected ICompilationUnit getPostRefactoringICompilationUnit() {
		return savedParentPackage.getCompilationUnit(NEW_TYPE_NAME + ".java");
	}
	
	
	@Override
	protected ProblemReport checkCustomPostconditions(ICompilationUnit icu) throws Exception {
		// TODO Auto-generated method stub
		return super.checkCustomPostconditions(icu);
	}	
}
