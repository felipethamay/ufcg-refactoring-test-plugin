package refactoringtestplugin.extractmethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.naming.ConfigurationException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.eclipse.jdt.internal.corext.refactoring.structure.PullUpRefactoringProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;
import refactoringtestplugin.RefactoringTest;
import refactoringtestplugin.util.JavaProjectHelper;


public abstract class ExtractMethodTest extends RefactoringTest {

	public static final String SQUARE_BRACKET_OPEN= "/*[*/";
	public static final int    SQUARE_BRACKET_OPEN_LENGTH= SQUARE_BRACKET_OPEN.length();
	public static final String SQUARE_BRACKET_CLOSE=   "/*]*/";
	public static final int    SQUARE_BRACKET_CLOSE_LENGTH= SQUARE_BRACKET_CLOSE.length();
	public static String codeToExtract = "";
	
	
//	protected static final String METHOD_TO_REFACTOR = "m_0";
//	protected static final String CLASS_NAME = "ClassId_0";
	
//	protected static final String METHOD_TO_REFACTOR = "m";
//	public static  String[] METHOD_TO_REFACTOR = {"m"};
	public static  String CLASS_NAME = "A_test1105";
	public static  String path = "";
	public static int[] fSelection;
	public static boolean fIgnoreSelectionMarker = false;
	public static int visibility = Modifier.PUBLIC;
//	public static String[] newNames;
//	public static int[]  newOrder;
	public static int destination = 0;
	public static String pathSource = "";
	public static final String CONTAINER= "src";
	public static IJavaProject fgJavaTestProject;
	public static String packName = "expression_in";

	
//	@Override
//	protected String getLogDirPath() {
//		return super.getSystemTempDir() + "/pullupmethod";
//	}

	@Override
	protected String getRefactoredClassName() {
		return CLASS_NAME;
	}

	protected IPackageFragmentRoot addRTJar(IJavaProject project) throws CoreException, ConfigurationException {
		return JavaProjectHelper.addRTJar(project);
	}
	
	protected ICompilationUnit createCUWithPath(IPackageFragment pack, String name, String path) throws Exception {
		name= adaptName(name);
		return createCU(pack, name, getFileInputStream(getFilePath(pack, name), path));
	}

	protected InputStream getFileInputStream(String fileName, String path2) throws IOException {
		path2 = "/resources/";
//		path2 = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/extractmethod/A_test1105/in/";
//		path2 = "/resources/generics_in/";
//		URL resource = this.getClass().getResource("/resources/");
//		path2 = resource.getPath();
		IPath path= new Path(path2).append(fileName);
		URL url= new URL(getBundle().getEntry("/"), path.toString());
		return url.openStream();
//		return RefactoringTestPlugin.getDefault().getTestResourceStream(fileName);
	}
	
	public final Bundle getBundle() {
		
		Bundle bundle;
		ClassLoader cl = getClass().getClassLoader();
		if (cl instanceof BundleReference)
			return ((BundleReference) cl).getBundle();
		return null;
	}
	protected String adaptName(String name) {
		return name + ".java";
	}
	
	protected ICompilationUnit createCU(IPackageFragment pack, String name, InputStream contents) throws Exception {
		return createCU(pack, name, getFileContents(contents));
	}
	

	protected ICompilationUnit createCU(IPackageFragment pack, String name, String contents) throws Exception {
		ICompilationUnit cu= pack.createCompilationUnit(name, contents, true, null);
		cu.save(null, true);
		return cu;
	}
	
	private String getFilePath(IPackageFragment pack, String name) {
		return getFilePath(pack.getElementName(), name);
	}
	
	private String getFilePath(String path, String name) {
		return getResourceLocation() + path + "/" + name;
	}
	
	protected String getResourceLocation() {
		return "";
	}
	
	protected String getFileContents(InputStream in) throws IOException {
		BufferedReader br= new BufferedReader(new InputStreamReader(in));

		StringBuffer sb= new StringBuffer();
		try {
			int read= 0;
			while ((read= br.read()) != -1)
				sb.append((char) read);
		} finally {
			br.close();
		}
		return sb.toString();
	}
	
//	protected ICompilationUnit createCU(IPackageFragment pack, String name, String contents) throws Exception {
//		ICompilationUnit cu= pack.createCompilationUnit(name, contents, true, null);
//		cu.save(null, true);
//		return cu;
//	}
	
	@Override
	protected Refactoring getRefactoring(List<ICompilationUnit> icus)
			throws JavaModelException {
		
		
		try {
//			IJavaProject fgJavaTestProject = JavaProjectHelper.createJavaProject("TestProject"+System.currentTimeMillis(), "bin");
			IPackageFragmentRoot fgJRELibrary = addRTJar(fgJavaTestProject);
			IPackageFragmentRoot root= JavaProjectHelper.addSourceContainer(fgJavaTestProject, CONTAINER);
			
//			IPackageFragment fSelectionPackage= root.createPackageFragment("generics_in", true, null);
			IPackageFragment fSelectionPackage= root.createPackageFragment(packName , true, null);
			
			ICompilationUnit unit= createCUWithPath(fSelectionPackage, CLASS_NAME, path);
			int[] selection= fSelection;
			ExtractMethodRefactoring refactoring= new ExtractMethodRefactoring(unit, selection[0], selection[1]);
			refactoring.setMethodName("extracted");
			refactoring.setVisibility(visibility);
//			TestModelProvider.clearDelta();
			RefactoringStatus status= refactoring.checkInitialConditions(new NullProgressMonitor());
			refactoring.setDestination(destination);
			return refactoring;
		} catch (CoreException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
		
//			String source = null;
//			try {
//				source = getSource(pathSource);
//				if (source == null) {
//					return null;
//				}
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
		
//		getSelectionFromNewSource(source);
//		int[] selection= fSelection;
//		System.out.println(selection[0]+" "+selection[1]);
//		ICompilationUnit icu = null;
//		try{
//		 icu = findICU(icus);
//		}catch(NotCompileException e) {
//			return null;
//		}
//		ExtractMethodRefactoring refactoring= new ExtractMethodRefactoring(icu, selection[0], selection[1]);
//		refactoring.setMethodName("extracted");
//		refactoring.setVisibility(visibility);
////		TestModelProvider.clearDelta();
//		try {
//			RefactoringStatus status= refactoring.checkInitialConditions(new NullProgressMonitor());
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		List<ParameterInfo> parameters= refactoring.getParameterInfos();
////		if (newNames != null && newNames.length > 0) {
////			for (int i= 0; i < newNames.length; i++) {
////				if (newNames[i] != null)
////					parameters.get(i).setNewName(newNames[i]);
////			}
////		}
////		if (newOrder != null && newOrder.length > 0) {
//////			assertTrue(newOrder.length == parameters.size());
////			List<ParameterInfo> current= new ArrayList<>(parameters);
////			for (int i= 0; i < newOrder.length; i++) {
////				parameters.set(newOrder[i], current.get(i));
////			}
////		}
//		refactoring.setDestination(destination);
	
//		return refactoring;
	}

	/* Copied from org.eclipse.jdt.ui.tests.refactoring.PullUpTests. */
	private IType[] getPossibleTargetClasses(PullUpRefactoringProcessor ref)
			throws JavaModelException {
		return ref.getCandidateTypes(new RefactoringStatus(),
				new NullProgressMonitor());
	}

	private void setSuperclassAsTargetClass(PullUpRefactoringProcessor ref)
			throws JavaModelException {
		IType[] possibleClasses = getPossibleTargetClasses(ref);
		ref.setDestinationType(possibleClasses[possibleClasses.length - 1]);
	}

	/* End copy. */

	private ICompilationUnit findICU(List<ICompilationUnit> icus) throws JavaModelException, NotCompileException {
		for (ICompilationUnit icu : icus) {
			for (IType type : icu.getAllTypes()) {
				if (type.getElementName().equals(CLASS_NAME)) {
					return icu;
				}
			}
		}
		throw new NotCompileException("Program may not compile");
	}
	
	

	public static IMethod[] getMethods(IType type, String[] names,
			String[][] signatures) throws JavaModelException {
		if (names == null || signatures == null)
			return new IMethod[0];
		List methods = new ArrayList(names.length);
		for (int i = 0; i < names.length; i++) {
			IMethod method = type.getMethod(names[i], signatures[i]);
			// assertTrue("method " + method.getElementName() +
			// " does not exist", method.exists());
			if (!methods.contains(method))
				methods.add(method);
		}
		return (IMethod[]) methods.toArray(new IMethod[methods.size()]);
	}

	protected IType getType(List<ICompilationUnit> icus, String name)
			throws JavaModelException {
		name = "P2_0.Class2_0";
		for (ICompilationUnit cu : icus) {
			IType[] types = cu.getAllTypes();
			for (int i = 0; i < types.length; i++) {
				String fullyQualifiedName = types[i].getFullyQualifiedName();
				if (fullyQualifiedName.equals(name)
						|| types[i].getElementName().equals(name))
					return types[i];
			}

		
		}
		

		return null;
	}

	public static String getSource(String path) throws IOException {
			
		if (!new File(path).exists()) {
			return null;
		}
		
			InputManager im = new InputManagerASCII(path);
		
			String contents = "";
			im.openFile();
			while (!im.isEndOfFile()) {
				contents += im.readLine()+"\n";
			}
			im.closeFile();
			return contents;
	}
	
	public static void initializeSelection(String path) throws IOException {
		
		String source = getSource(path);
		int start= -1;
		int end= -1;
		int includingStart= source.indexOf(SQUARE_BRACKET_OPEN);
		int excludingStart= source.indexOf(SQUARE_BRACKET_CLOSE);
		int includingEnd= source.lastIndexOf(SQUARE_BRACKET_CLOSE);
		int excludingEnd= source.lastIndexOf(SQUARE_BRACKET_OPEN);

		if (includingStart > excludingStart && excludingStart != -1) {
			includingStart= -1;
		} else if (excludingStart > includingStart && includingStart != -1) {
			excludingStart= -1;
		}

		if (includingEnd < excludingEnd) {
			includingEnd= -1;
		} else if (excludingEnd < includingEnd) {
			excludingEnd= -1;
		}

		if (includingStart != -1) {
			start= includingStart;
		} else {
			start= excludingStart + SQUARE_BRACKET_CLOSE_LENGTH;
		}

		if (excludingEnd != -1) {
			end= excludingEnd;
		} else {
			end= includingEnd + SQUARE_BRACKET_CLOSE_LENGTH;
		}
		
		if (!(start >= 0 && end >= 0 && end >= start)) {
			throw new RuntimeException("Invalid selection"); // TODO: better exception
		}


		fSelection= new int[] {
			start - (fIgnoreSelectionMarker ? SQUARE_BRACKET_CLOSE_LENGTH : 0),
			end - start
		};
//		if (start+5 >= 0 && (end -5)<source.length())
			codeToExtract = source.substring(start,end);
			if (codeToExtract.startsWith(SQUARE_BRACKET_OPEN)) {
				codeToExtract = source.substring(start+5,end-5);
			}
			codeToExtract= codeToExtract.replaceAll("/\\*\\[\\*/", ""); 
			codeToExtract= codeToExtract.replaceAll("/\\*\\*/ ", "");
			codeToExtract= codeToExtract.replaceAll("/\\*\\]\\*/", "");
//			codeToExtract = codeToExtract.replaceAll("/**/", "");
			
			System.out.println(codeToExtract);
//			System.out.println("code 1 "+codeToExtract);
//			System.out.println("code 2 "+source.substring(start+5,end-5));
		
//		System.out.println((start+5)+" "+(end-5)+" "+codeToExtract);
		
//		System.out.println(source.indexOf(codeToExtract)+" "+source.indexOf(codeToExtract)+codeToExtract.length());
		// System.out.println("|"+ source.substring(result[0], result[0] + result[1]) + "|");
	}
	
//	public static void getSelectionFromNewSource(String source) {
//		int start = source.indexOf(codeToExtract);
//		int end = codeToExtract.length();
//		fSelection = new int[] {start,end};
//	}
	
public static void main(String[] args) throws Exception {
		
		String inout = "in";
////		for (int k = 0; k<= 1; k++) {
//			if (k == 1) {
//				inout = "out";
//			}
		
			String p = "C:/Users/Felipe/Documents/workspace6/RefactoringTestPluginASM/resources/";
			
			File ff = new File(p);
			String[] list2 = ff.list();
			for (String name : list2) {
				String path = p+name+"/";
				File f = new File(path);
				String[] list = f.list();
				for (String file : list) {
					if (new File(path + file).isDirectory()) continue;
					InputManager in = new InputManagerASCII(path+ file);
					in.openFile();
					String contents = "";
					boolean ok = false;
//					int j = 1;
					while(!in.isEndOfFile()) {
						String line = in.readLine();
						if (line.contains("//")) {
							line = line.substring(0,line.indexOf("//"));
						}
//						if (!ok && !line.startsWith("//")) {
//							break;
//						}
//						if (!ok && line.startsWith("//")) {
//							ok = true;
//						}
//						if (ok) {
							contents += line+" \n";
//						}
//						ok = true;
//						if (j == 8) {
//							System.out.println(line.subSequence(9, 19));
//						}
//						j++;
					}
//					if (!ok) continue;
					in.closeFile();
					OutputManager out = new OutputManagerASCII(path+ file);
					out.createFile();
					out.writeLine(contents);
					out.closeFile();
				}
//			}
			}
//		new Test().test1();
	}
}
