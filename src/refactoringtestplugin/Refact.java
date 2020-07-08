package refactoringtestplugin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import refactoringtestplugin.ReadProject;
import refactoringtestplugin.util.JavaProjectHelper;

public class Refact {

    protected IJavaProject project;


	
	public ICompilationUnit[] createCus(String path) throws Exception {
		createProject();
		 IPackageFragmentRoot packageRoot = createPackageRoot(project);
		ReadProject rp = new ReadProject();
		List<CompilationUnit> cus = rp.getCus_(path);
		ICompilationUnit[] icus = new ICompilationUnit[cus.size()];
		int i = 0;
         for (CompilationUnit cu : cus) {
             String cuName = getPrimaryTypeName(cu);
             
             ICompilationUnit icu = createICompilationUnit(packageRoot,
                     cu, cuName);
             icus[i] = icu;
             i++;
         }
         return icus;
	}
	
    public void createProject() throws Exception {
        project = JavaProjectHelper.createJavaProject(
                "TestProject" + System.currentTimeMillis(), "bin");
        JavaProjectHelper.addRTJar(project);
    }
    protected IPackageFragmentRoot createPackageRoot(IJavaProject project)
            throws CoreException {
        String packageRootName = "src";
        return JavaProjectHelper.addSourceContainer(project, packageRootName);
    }
    
    protected String getPackageNameFrom(CompilationUnit cu) {
        PackageDeclaration packDecl = cu.getPackage();
        if (packDecl == null) {
            return "";
        }
        return packDecl.getName().toString();
    }
	  protected ICompilationUnit createICompilationUnit(
	            IPackageFragmentRoot packFragRoot, CompilationUnit cu, String cuName)
	            throws JavaModelException {
	        String packageName = getPackageNameFrom(cu);
	        IPackageFragment packFrag = packFragRoot.createPackageFragment(
	                packageName, true, null);

	        ICompilationUnit icu = packFrag.createCompilationUnit(cuName + ".java", // CU
	                // name
	                cu.toString(), // CU source
	                true, // overwrite if CU exists
	                new NullProgressMonitor()); // ignore progress
	        return icu;
	    }

    protected String getPrimaryTypeName(CompilationUnit cu) {
        String first = null;
        
        for (Object type : cu.types()) {
            if (type instanceof TypeDeclaration) {
                String typeName = ((TypeDeclaration) type).getName().toString();
                if (first == null) {
                    first = typeName;
                }
                for (Object modifier : ((TypeDeclaration) type).modifiers()) {
                    if (((Modifier) modifier).getKeyword() == Modifier.ModifierKeyword.PUBLIC_KEYWORD) {
                        return typeName;
                    }
                }
            } else if (type instanceof EnumDeclaration) {
            	
            	 String typeName = ((EnumDeclaration) type).getName().toString();
            	 if (first == null) {
                     first = typeName;
                 }
                 for (Object modifier : ((EnumDeclaration) type).modifiers()) {
                     if (((Modifier) modifier).getKeyword() == Modifier.ModifierKeyword.PUBLIC_KEYWORD) {
                         return typeName;
                     }
                 }
            }
        }
        return first;
    }
}
