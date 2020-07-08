package refactoringtestplugin.util;

import io.InputManager;
import io.InputManagerASCII;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.aspectj.org.eclipse.jdt.internal.core.util.DOMFinder;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.core.ClassFile;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;

public class ProgramInfo {

	private CompilationUnit getCompilationUnit(String path) {
		
		 ASTParser parser = ASTParser.newParser(AST.JLS3);  // handles JDK 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6
//		 parser.setSource(source);

		 StringBuffer read = null;
		try {
			read = read(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 parser.setSource(read.toString().toCharArray());
		 // In order to parse 1.5 code, some compiler options need to be set to 1.5
		 Map options = JavaCore.getOptions();
//		 JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
		 parser.setCompilerOptions(options);
//		 CompilationUnit result = (CompilationUnit) parser.createAST(null);
		 
		 parser.setKind(ASTParser.K_COMPILATION_UNIT);
		 final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		 return cu;
		
	}
	
	
	public List<CompilationUnit> getCompilationUnits(String path) {
		List<CompilationUnit> cus = new ArrayList<CompilationUnit>();
		List<String> classes = getClasses(path, new ArrayList<String>());
		for (String clas : classes) {
			CompilationUnit compilationUnit = getCompilationUnit(clas);
			cus.add(compilationUnit);
		}
		return cus;
	}
	
	public List<String> getClasses(String path, List<String> list) {
		
		File f = new File(path);
		if (f.isDirectory()) {
			String[] subDir = f.list();
			for (String string : subDir) {
				if (!string.startsWith("."))
					getClasses(path + "/"+string, list);
			}
		} else {
			if (path.endsWith(".java")) {
				list.add(path);
			}
		}
		return list;
	}
	
	public boolean compare(String path1, String path2)  {
		
		StringBuffer b1 = null;
		StringBuffer b2 = null;
		
		try {
			b1 = read(path1);
			b2 = read(path2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b1.toString().equals(b2.toString());
	}
	
	public StringBuffer read(String path) throws IOException {
		InputManager file = new InputManagerASCII(path);
		StringBuffer b = new StringBuffer();
		file.openFile();
		while (!file.isEndOfFile()) {
			String line = file.readLine();
//			line = trim(line);
			b.append(line);
		}
		file.closeFile();
		return b;
	}
	
	public String trim(String string) {
		String result = ""; 
		char[] charArray = string.toCharArray();
		for (char c : charArray) {
			if (c != ' ' && c != '\t') {
				result += c;
			}
		}
		return result;
		
	}
	
	public static void main(String[] args) {
		String path1 = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/eclipse/pullupmethod/skip1/test1/in/";
		String path2 = "C:/Users/Felipe/Documents/workspace6/testT/src/p1/BB.java";
//		
		List<String> classes = new ProgramInfo().getClasses(path1, new ArrayList<String>());
		
		System.out.println(classes.toString());
		
		
	}
}
