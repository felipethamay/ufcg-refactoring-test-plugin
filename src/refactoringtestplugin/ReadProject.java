package refactoringtestplugin;

import io.InputManager;
import io.InputManagerASCII;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;



public class ReadProject {

	
	public List<String> getAllClasses(String path, String className, List<String> classes) {
		File project = new File(path);
		String[] list = project.list();
		for (String string : list) {
			if (string.endsWith(".java")) {
				classes.add(path+"/"+string);
			} else {
				if (new File(path+"/"+string).isDirectory()) {
					getAllClasses(path+"/"+string, string, classes);
				}
			}
		}
		return classes;
	}
	
	public StringBuffer read(String path) {
		StringBuffer b = new StringBuffer();
		try {
			InputManager file = new InputManagerASCII(path);
			file.openFile();
			while (!file.isEndOfFile()) {
				String line = file.readLine();
				b.append(line);
			}
			file.closeFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	
	public IJavaProject getProject(String path) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		ResourcesPlugin plugin = ResourcesPlugin.getPlugin();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(path);
		String sourcePath = project.getLocation().toString() + "/";
		return JavaCore.create(project);

	}
	
	public CompilationUnit getCompilationUnit(String path) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);  // handles JDK 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6
//		 parser.setSource(source);

		 StringBuffer read = null;
		read = read(path);
		 parser.setSource(read.toString().toCharArray());
		 // In order to parse 1.5 code, some compiler options need to be set to 1.5
		 Map options = JavaCore.getOptions();
		 
		 JavaCore.setComplianceOptions(JavaCore.VERSION_1_6, options);
		 parser.setCompilerOptions(options);
//		 CompilationUnit result = (CompilationUnit) parser.createAST(null);
		 
		 parser.setKind(ASTParser.K_COMPILATION_UNIT);
		 
		return (CompilationUnit) parser.createAST(null);
	}
	
	
	public List<CompilationUnit> getCus_2(String path) {
		
		List<CompilationUnit> cus = new ArrayList<CompilationUnit>();
//		List<String> allClasses = getAllClasses(path, "", new ArrayList<String>());
//		for (String cls : allClasses) {
			CompilationUnit compilationUnit = getCompilationUnit(path);
			cus.add(compilationUnit);
//		}
		return cus;
		
	}
	
	public List<CompilationUnit> getCus_(String path) {
		
		List<CompilationUnit> cus = new ArrayList<CompilationUnit>();
		List<String> allClasses = getAllClasses(path, "", new ArrayList<String>());
		for (String cls : allClasses) {
			CompilationUnit compilationUnit = getCompilationUnit(cls);
			cus.add(compilationUnit);
		}
		return cus;
		
	}
	
	public List<ICompilationUnit> getCompilationUnits(String path) throws JavaModelException {
		IJavaProject iJavaProject = getProject(path);
		IPackageFragment[] packages = iJavaProject.getPackageFragments();
		
		List<ICompilationUnit> cus = new ArrayList<ICompilationUnit>();
		
		for (IPackageFragment mypackage : packages) {

			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
//				String packagePath = mypackage.getPath().toString();
//				
//				System.out.println("package------------ "+mypackage.getElementName());
//				packagePath = packagePath.substring(packagePath.indexOf("/")+1);
//				packagePath = packagePath.substring(packagePath.indexOf("/"));
				
				ICompilationUnit[] compilationUnits = mypackage.getCompilationUnits();
				for (ICompilationUnit iCompilationUnit : compilationUnits) {
					cus.add(iCompilationUnit);
				}
				String packageName = mypackage.getElementName();
			}

		}
		return cus;
	}
	
	public List<CompilationUnit> getCus(String path) throws JavaModelException {
		
//		List<String> allClasses = getAllClasses(path, "", new ArrayList<String>());
		List<CompilationUnit> cus = new ArrayList<CompilationUnit>();
		
		List<ICompilationUnit> compilationUnits = getCompilationUnits(path);
		
		for (ICompilationUnit iCompilationUnit : compilationUnits) {
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(iCompilationUnit);
			Map options = JavaCore.getOptions();
			parser.setCompilerOptions(options);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			cus.add(cu);
		}
//		for (String c : allClasses) {
//			if (c.contains("AbstractTool")) 
//				System.out.println();
//			ASTParser parser = ASTParser.newParser(AST.JLS3);
//			StringBuffer read = read(c);
//			
//			
////			parser.setSourceRange(0,read.toString().length()-1);
//			String string = read.toString();
//			char[] charArray = string.toCharArray();
//			parser.setSource(charArray);
//			Map options = JavaCore.getOptions();
//			parser.setCompilerOptions(options);
//			parser.setKind(ASTParser.K_COMPILATION_UNIT);
//			
//			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
//			cus.add(cu);
//		}
		
		return cus;
	}
	
}
