package refactoringtestplugin.movemethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.SourceRange;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
//import org.eclipse.jdt.ui.tests.refactoring.infra.RefactoringTestPlugin;
//import org.eclipse.jdt.ui.tests.refactoring.infra.TextRangeUtil;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CreateChangeOperation;
import org.eclipse.ltk.core.refactoring.IUndoManager;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;

import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;
import refactoringtestplugin.Refact;

public class Test {

	public static final int PARAMETER= 0;
	public static final int FIELD= 1;
	private boolean toSucceed;
	protected static final String TEST_INPUT_INFIX= "/in/";
	protected static final String TEST_OUTPUT_INFIX= "/out/";
	public static ICompilationUnit[] cus_;
	public static String p = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/movemethod/test";
	public static boolean error = false;
	public static boolean activation = false;
	
	public static  String TARGET_METHOD_NAME = "";
	public static  String CLASS_NAME = "";

	
	protected void helper1(int num,String[] cuQNames, String selectionCuQName, int startLine, int startColumn, int endLine, int endColumn, int newReceiverType, String newReceiverName,
			boolean inlineDelegator, boolean removeDelegator) throws Exception {
		int selectionCuIndex= firstIndexOf(selectionCuQName, cuQNames);
		Assert.isTrue(selectionCuIndex != -1, "parameter selectionCuQName must match some String in cuQNames.");
		helper1(num, selectionCuQName,cuQNames, selectionCuIndex, startLine, startColumn, endLine, endColumn, newReceiverType, newReceiverName, null, inlineDelegator, removeDelegator);
	}
	
	private void helper1(int num,String[] cuQNames, String selectionCuQName, int startLine, int startColumn, int endLine, int endColumn, int newReceiverType, String newReceiverName, boolean inlineDelegator, boolean removeDelegator, boolean deprecate) throws Exception {
		int selectionCuIndex= firstIndexOf(selectionCuQName, cuQNames);
		Assert.isTrue(selectionCuIndex != -1, "parameter selectionCuQName must match some String in cuQNames.");
		helper1(num,selectionCuQName,cuQNames, selectionCuIndex, startLine, startColumn, endLine, endColumn, newReceiverType, newReceiverName, null, inlineDelegator, removeDelegator, deprecate);
	}
	
	private int firstIndexOf(String one, String[] others) {
		for (int i= 0; i < others.length; i++)
			if (one == null && others[i] == null || one.equals(others[i]))
				return i;
		return -1;
	}
	
//	private ICompilationUnit[] createCUs(String[] qualifiedNames) throws Exception {
//		ICompilationUnit[] cus= new ICompilationUnit[qualifiedNames.length];
//		for (int i= 0; i < qualifiedNames.length; i++) {
//			Assert.isNotNull(qualifiedNames[i]);
//			cus[i]= createCUfromTestFile(getRoot().createPackageFragment(getQualifier(qualifiedNames[i]), true, null), getSimpleName(qualifiedNames[i]));
//		}
//		return cus;
//	}
	
//	protected IPackageFragmentRoot getRoot() {
//		return fRoot;
//	}
	public static ISourceRange getSelection(ICompilationUnit cu, int startLine, int startColumn, int endLine, int endColumn) throws Exception {
		IDocument document= new Document(cu.getSource());
		int offset= getOffset(document, startLine, startColumn);
		int end= getOffset(document, endLine, endColumn);
		return new SourceRange(offset, end - offset);
	}
	
	private static int getOffset(IDocument document, int line, int column) throws BadLocationException {
		int r= document.getLineInformation(line - 1).getOffset();
		IRegion region= document.getLineInformation(line - 1);
		int lineTabCount= calculateTabCountInLine(document.get(region.getOffset(), region.getLength()), column);
		r += (column - 1) - (lineTabCount * getTabWidth()) + lineTabCount;
		return r;
	}
	
	public static int calculateTabCountInLine(String lineSource, int lastCharOffset){
		int acc= 0;
		int charCount= 0;
		for(int i= 0; charCount < lastCharOffset - 1; i++){
			if ('\t' == lineSource.charAt(i)){
				acc++;
				charCount += getTabWidth();
			}	else
				charCount += 1;
		}
		return acc;
	}
	private static final int getTabWidth(){
		return 4;
	}
	
	private static IMethod getMethod(ICompilationUnit cu, String methodName) throws JavaModelException {
		IType[] allTypes = cu.getAllTypes();
System.out.println();
		for (IType iType : allTypes) {
			IMethod[] methods = iType.getMethods();
			for (IMethod iMethod : methods) {
				if (iMethod.getElementName().equals(methodName)) {
					return iMethod;
				}
			}
		}
		return null;
	}
	private static IMethod getMethod(ICompilationUnit cu, ISourceRange sourceRange) throws JavaModelException {
		IJavaElement[] jes= cu.codeSelect(sourceRange.getOffset(), sourceRange.getLength());
		if (jes.length != 1 || !(jes[0] instanceof IMethod))
			return null;
		return (IMethod) jes[0];
	}
	
	public static void chooseNewTarget(MoveInstanceMethodProcessor processor, int newTargetType, String newTargetName) {
		IVariableBinding target= null;
		IVariableBinding[] targets= processor.getPossibleTargets();
		for (int i= 0; i < targets.length; i++) {
			IVariableBinding candidate= targets[i];
			if (candidate.getName().equals(newTargetName) && typeMatches(newTargetType, candidate)) {
				target= candidate;
				break;
			}
		}
//		assertNotNull("Expected new target not available.", target);
		processor.setTarget(target);
	}
	
	private static boolean typeMatches(int newTargetType, IVariableBinding newTarget) {
		return newTargetType == PARAMETER && !newTarget.isField() || newTargetType == FIELD && newTarget.isField();
	}

	protected final Change performChange(Refactoring refactoring, boolean storeUndo) throws Exception {
		CreateChangeOperation create= new CreateChangeOperation(refactoring);
		PerformChangeOperation perform= new PerformChangeOperation(create);
		if (storeUndo) {
			perform.setUndoManager(getUndoManager(), refactoring.getName());
		}
		ResourcesPlugin.getWorkspace().run(perform, new NullProgressMonitor());
//		assertTrue("Change wasn't executed", perform.changeExecuted());
		perform.changeExecuted();
		return perform.getUndoChange();
	}
	
	protected IUndoManager getUndoManager() {
		IUndoManager undoManager= RefactoringCore.getUndoManager();
		undoManager.flush();
		return undoManager;
	}
	
	private void helper1(int num, String classFulName, String[] cuQNames, int selectionCuIndex, int startLine, int startColumn, int endLine, int endColumn, int newTargetType, String newTargetName, String newMethodName, boolean inlineDelegator, boolean removeDelegator) throws Exception {
		error = false;
		activation = false;
		Assert.isTrue(0 <= selectionCuIndex && selectionCuIndex < cuQNames.length);

		toSucceed= true;
//		ICompilationUnit[] cus= createCUs(cuQNames);
		
		String path = p +num+"/in/";
		ICompilationUnit[] cus= new Refact().createCus(path);
		ICompilationUnit selectionCu= cus[selectionCuIndex];
		for (ICompilationUnit iCompilationUnit : cus) {
			if ((classFulName+".java").contains(iCompilationUnit.getElementName())) {
				selectionCu = iCompilationUnit;
				break;
			}
		}

//		ISourceRange selection= getSelection(selectionCu, startLine, startColumn, endLine, endColumn);
//		IMethod method= getMethod(selectionCu, selection);
		String className = getClassName(classFulName);
		
		CLASS_NAME = getClassNameNoExtension(classFulName);
		
		String methodName = getMethodName(path+className, startLine, startColumn, endColumn);
		
		TARGET_METHOD_NAME = methodName;
		if (true) return;
		
		IMethod method= getMethod(selectionCu,methodName );
		
//		assertNotNull(method);
		MoveInstanceMethodProcessor processor= new MoveInstanceMethodProcessor(method, JavaPreferencesSettings.getCodeGenerationSettings(selectionCu.getJavaProject()));
		Refactoring ref= new MoveRefactoring(processor);

//		assertNotNull("refactoring should be created", ref);
		RefactoringStatus preconditionResult= ref.checkInitialConditions(new NullProgressMonitor());

		if (!preconditionResult.isOK()) {
			System.out.println("activation was supposed to be successful");
			activation = true;
		}
//		assertTrue("activation was supposed to be successful", preconditionResult.isOK());

		chooseNewTarget(processor, newTargetType, newTargetName);

		processor.setInlineDelegator(inlineDelegator);
		processor.setRemoveDelegator(removeDelegator);
		processor.setDeprecateDelegates(false);
		if (newMethodName != null)
			processor.setMethodName(newMethodName);

		preconditionResult.merge(ref.checkFinalConditions(new NullProgressMonitor()));

//		assertTrue("precondition was supposed to pass", !preconditionResult.hasError());
		if (preconditionResult.hasError()) {
			System.out.println("precondition was supposed to pass");
			error = true;
		}
		
		performChange(ref, false);

		cus_ = cus;
		for (int i= 0; i < cus.length; i++) {
			System.out.println(cus[i].getElementName());
			System.out.println(cus[i].getPath());
			
//			System.out.println(cus[i].getPackageDeclarations()[0].getElementName());
			System.out.println(cus[i].getSource());
			
//			String outputTestFileName= getOutputTestFileName(getSimpleName(cuQNames[i]));
//			assertEqualLines("Incorrect inline in " + outputTestFileName, getFileContents(outputTestFileName), cus[i].getSource());
		}
	}
	
	
	private void helper1(int num, String classFulName,  String[] cuQNames, int selectionCuIndex, int startLine, int startColumn, int endLine, int endColumn, int newTargetType, String newTargetName, String newMethodName, boolean inlineDelegator, boolean removeDelegator, boolean deprecate) throws Exception {
		error = false;
		activation = false;
		Assert.isTrue(0 <= selectionCuIndex && selectionCuIndex < cuQNames.length);

		toSucceed= true;
//		ICompilationUnit[] cus= createCUs(cuQNames);
		
		String path = p +num+"/in/";
		
		ICompilationUnit[] cus= new Refact().createCus(path);
		ICompilationUnit selectionCu= cus[selectionCuIndex];
		for (ICompilationUnit iCompilationUnit : cus) {
			if ((classFulName+".java").contains(iCompilationUnit.getElementName())) {
				selectionCu = iCompilationUnit;
				break;
			}
		}
		
		

//		ISourceRange selection= getSelection(selectionCu, startLine, startColumn, endLine, endColumn);
//		IMethod method= getMethod(selectionCu, selection);
	String className = getClassName(classFulName);
		
	CLASS_NAME = getClassNameNoExtension(classFulName);
		
		String methodName = getMethodName(path+className, startLine, startColumn, endColumn);
		
		TARGET_METHOD_NAME = methodName;
		
		IMethod method= getMethod(selectionCu, methodName);
		if (true) return;
		
//		assertNotNull(method);
		MoveInstanceMethodProcessor processor= new MoveInstanceMethodProcessor(method, JavaPreferencesSettings.getCodeGenerationSettings(selectionCu.getJavaProject()));
		Refactoring ref= new MoveRefactoring(processor);

//		assertNotNull("refactoring should be created", ref);
		RefactoringStatus preconditionResult= ref.checkInitialConditions(new NullProgressMonitor());

		if (!preconditionResult.isOK()) {
			System.out.println("activation was supposed to be successful");
			activation = true;
		}
//		assertTrue("activation was supposed to be successful", preconditionResult.isOK());

		chooseNewTarget(processor, newTargetType, newTargetName);

		processor.setInlineDelegator(inlineDelegator);
		processor.setRemoveDelegator(removeDelegator);
		processor.setDeprecateDelegates(deprecate);
		if (newMethodName != null)
			processor.setMethodName(newMethodName);

		preconditionResult.merge(ref.checkFinalConditions(new NullProgressMonitor()));

//		assertTrue("precondition was supposed to pass", !preconditionResult.hasError());
		if (preconditionResult.hasError()) {
			System.out.println("precondition was supposed to pass");
			error = true;
		}

		performChange(ref, false);

		cus_ = cus;
		for (int i= 0; i < cus.length; i++) {
			System.out.println(cus[i].getElementName());
			System.out.println(cus[i].getPath());
//			System.out.println(cus[i].getPackageDeclarations()[0].getElementName());
			System.out.println(cus[i].getSource());
			
//			String outputTestFileName= getOutputTestFileName(getSimpleName(cuQNames[i]));
//			assertEqualLines("Incorrect inline in " + outputTestFileName, getFileContents(outputTestFileName), cus[i].getSource());
		}
	}

	public static String getContents(InputStream in) throws IOException {
		BufferedReader br= new BufferedReader(new InputStreamReader(in));

		StringBuffer sb= new StringBuffer(300);
		try {
			int read= 0;
			while ((read= br.read()) != -1)
				sb.append((char) read);
		} finally {
			br.close();
		}
		return sb.toString();
	}

	private String getClassName(String fullName) {
		return fullName.substring(fullName.lastIndexOf(".")+1, fullName.length())+".java";
	}
	
	private String getClassNameNoExtension(String fullName) {
		return fullName.substring(fullName.lastIndexOf(".")+1, fullName.length());
	}
	
	
	private String getMethodName(String path, int line, int col1, int col2) throws IOException {
	    InputManager in = new InputManagerASCII(path);
		in.openFile();
		int j = 1;
		String lin = "";
		while(!in.isEndOfFile()) {
			lin = in.readLine();
			if (j == line) {
//				char a = lin.charAt(col1);
				int indexFinal = lin.indexOf("(");
				lin = lin.substring(0,indexFinal);
				lin = lin.substring(lin.lastIndexOf(" ")+1);
//				String ret = lin.substring(col1-4, col2-4);
				return lin;
			}
			j++;
		}
		return null;
	}

	public void test1() throws Exception {
		// printTestDisabledMessage("not implemented yet");
		System.out.println("ookkkkk");
		helper1(1,new String[] { "p1.A", "p2.B", "p3.C"}, "p1.A", 11, 17, 11, 20, PARAMETER, "b", true, false);
	}
	
	public void test0() throws Exception {
		helper1(0,new String[] { "p1.A", "p2.B", "p3.C"}, "p1.A", 11, 17, 11, 20, PARAMETER, "b", false, false);
	}
	
	public void test10() throws Exception {
		helper1(10,new String[] { "p1.A", "p2.B"}, "p1.A", 14, 17, 14, 17, PARAMETER, "b", false, false);
	}

	// move to field, method has parameters, choice of fields, some non-class type fields
	// ("this" is passed as argument)
	public void test11() throws Exception {
		helper1(11,new String[] { "p1.A", "p2.B"}, "p1.A", 17, 17, 17, 17, FIELD, "fB", false, false);
	}

	// move to field - do not pass 'this' because it's unneeded
	public void test12() throws Exception {
		helper1(12,new String[] { "p1.A", "p2.B"}, "p1.A", 14, 17, 14, 20, FIELD, "fB", false, false);
	}

	// junit case
	public void test13() throws Exception {
		helper1(13,new String[] { "p1.TR", "p1.TC", "p1.P"}, "p1.TR", 9, 20, 9, 23, PARAMETER, "test", false, false);
	}

	// simplified junit case
	public void test14() throws Exception {
		helper1(14,new String[] { "p1.TR", "p1.TC"}, "p1.TR", 9, 20, 9, 23, PARAMETER, "test", false, false);
	}

	// move to type in same cu
	public void test15() throws Exception {
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=40120
		helper1(15,new String[] { "p.A"}, "p.A", 17, 18, 17, 18, PARAMETER, "s", false, false);
	}

	// move to inner type in same cu
	public void test16() throws Exception {
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=40120
		helper1(16,new String[] { "p.B"}, "p.B", 15, 17, 15, 22, PARAMETER, "s", false, false);
	}

	// don't generate parameter for unused field (bug 38310)
	public void test17() throws Exception {
		helper1(17,new String[] { "p.Shape", "p.Rectangle"}, "p.Shape", 11, 16, 11, 20, FIELD, "fBounds", false, false);
	}

	// generate parameter for used field (bug 38310)
	public void test18() throws Exception {
		helper1(18,new String[] { "p.Shape", "p.Rectangle"}, "p.Shape", 17, 22, 17, 22, FIELD, "fInnerBounds", false, false);
	}

	// generate parameter for used field (bug 38310)
	public void test19() throws Exception {
		helper1(19,new String[] { "p.Shape", "p.Rectangle"}, "p.Shape", 22, 20, 22, 33, PARAMETER, "rect", false, false);
	}

	// // Move mA1 to parameter b, inline delegator, remove delegator
	public void test2() throws Exception {
		// printTestDisabledMessage("not implemented yet");
		helper1(2,new String[] { "p1.A", "p2.B", "p3.C"}, "p1.A", 12, 17, 12, 20, PARAMETER, "b", true, true);
	}

	// Can move if "super" is used in inner class
	public void test20() throws Exception {
		helper1(20,new String[] { "p.A", "p.B", "p.StarDecorator"}, "p.A", 14, 17, 14, 22, PARAMETER, "b", false, false);
	}

	// Arguments of method calls preserved in moved body (bug 41468)
	public void test21() throws Exception {
		helper1(21,new String[] { "p.A", "p.Second"}, "p.A", 5, 17, 5, 22, FIELD, "s", false, false);
	}

	// arguments of method calls preserved in moved body (bug 41468),
	// use "this" instead of field (bug 38310)
	public void test22() throws Exception {
		helper1(22,new String[] { "p.A", "p.Second"}, "p.A", 5, 17, 5, 22, FIELD, "s", false, false);
	}

	// "this"-qualified field access: this.s -> this (bug 41597)
	public void test23() throws Exception {
		helper1(23,new String[] { "p.A", "p.Second"}, "p.A", 5, 17, 5, 22, FIELD, "s", false, false);
	}

	// move local class (41530)
	public void test24() throws Exception {
		helper1(24,new String[] { "p1.A", "p1.B", "p1.StarDecorator"}, "p1.A", 9, 17, 9, 22, PARAMETER, "b", false, false);
	}

	// extended junit case
	public void test25() throws Exception {
		helper1(25,new String[] { "p1.TR", "p1.TC", "p1.P"}, "p1.TR", 4, 20, 4, 23, PARAMETER, "test", false, false);
	}

	// extended junit case with generics (bug 77653)
	public void test26() throws Exception {
		helper1(26,new String[] { "p1.TR", "p1.TC", "p1.P"}, "p1.TR", 9, 20, 9, 23, PARAMETER, "test", false, false);
	}

	// extended junit case with generics and deprecation message
	public void test27() throws Exception {
		helper1(27,new String[] { "p1.TR", "p1.TC", "p1.P"}, "p1.TR", 9, 20, 9, 23, PARAMETER, "test", false, false, true);
	}

	// extended junit case with generics, enums and deprecation message
	public void test28() throws Exception {
		helper1(28,new String[] { "p1.TR", "p1.TC", "p1.P"}, "p1.TR", 9, 20, 9, 23, PARAMETER, "test", false, false, true);
	}

	// extended junit case with generics, enums and deprecation message
	public void test29() throws Exception {
		helper1(29,new String[] { "p1.TR", "p1.TC", "p1.P"}, "p1.TR", 9, 20, 9, 23, PARAMETER, "test", false, false, true);
	}

	// extended junit case with generics, enums, static imports and deprecation message
	public void test30() throws Exception {
		helper1(30,new String[] { "p1.TR", "p1.TC", "p1.P"}, "p1.TR", 10, 21, 10, 21, PARAMETER, "test", false, false, true);
	}

	// extended junit case with generics, enums, static imports and deprecation message
	public void test31() throws Exception {
//		printTestDisabledMessage("disabled due to missing support for statically imported methods");
//		helper1(new String[] { "p1.TR", "p1.TC", "p1.P"}, "p1.TR", 10, 21, 10, 21, PARAMETER, "test", false, false, true);
	}

	public void test32() throws Exception {
		helper1(32,new String[] { "p1.A"}, "p1.A", 9, 25, 9, 26, PARAMETER, "p", true, true);
	}

	// Test visibility of a target field is not affected.
	public void test33() throws Exception {
		helper1(33,new String[] { "p.Foo", "p.Bar" }, "p.Foo", 6, 18, 6, 21, FIELD, "_bar", false, false);
	}

	// Test visibility of target field is changed to public
	// in case a caller is in another package (bug 117465).
	public void test34() throws Exception {
		helper1(34,new String[] { "test1.TestTarget", "test1.Test1", "test2.Test2"}, "test1.Test1", 3, 21, 3, 33, FIELD, "target", true, true);
	}

	// Test visibility of target field is changed to default
	// in case a caller is in the same package (bug 117465).
	public void test35() throws Exception {
		helper1(35,new String[] { "test1.TestTarget", "test1.Test1", "test1.Test2"}, "test1.Test1", 3, 21, 3, 33, FIELD, "target", true, true);
	}

	// Test search engine for secondary types (bug 108030).
	public void test36() throws Exception {
		helper1(36,new String[] { "p.A", "p.B" }, "p.A", 9, 17, 9, 27, FIELD, "fB", false, false);
	}

	// Test name conflicts in the moved method between fields and parameters (bug 227876)
	public void test37() throws Exception {
		helper1(37,new String[] { "p.A", "p.B" }, "p.A", 4, 17, 4, 42, FIELD, "destination", true, true);
	}

	// Test problem with parameter order (bug 165697)
	public void test38() throws Exception {
		helper1(38,new String[] { "p.A", "p.B" }, "p.A", 4, 17, 4, 35, FIELD, "target", true, true);
	}

	// Test problem with qualified accesses (bug 149316)
	public void test39() throws Exception {
		helper1(39,new String[] { "p.A", "p.B" }, "p.A", 4, 13, 4, 25, PARAMETER, "p", true, true);
	}

	// Test problem with qualified accesses (bug 149316)
	public void test40() throws Exception {
		helper1(40,new String[] { "p.A", "p.B" }, "p.A", 4, 13, 4, 25, PARAMETER, "p", true, true);
	}

	// Test problem with missing bindings (bug 328554)
	public void test41() throws Exception {
		helper1(41,new String[] { "p.A" }, "p.A", 4, 10, 4, 10, PARAMETER, "b", true, true);
	}
	
	// Test problem with parameterized nested class (bug 342074)
	public void test42() throws Exception {
		helper1(42,new String[] { "p.A", "p.B", "p.Outer" }, "p.A", 6, 17, 6, 20, PARAMETER, "b", true, true);
	}
	
	// Test problem with enum (bug 339980)
	public void test43() throws Exception {
		helper1(43,new String[] { "p.A" }, "p.A", 10, 10, 10, 20, PARAMETER, "fooBar", true, true);
	}

	// Test problem with enum (bug 339980)
	public void test44() throws Exception {
		helper1(44,new String[] { "p.A", "p.MyEnum" }, "p.A", 8, 10, 8, 20, PARAMETER, "fooBar", true, true);
	}

	// Test problem with enum (bug 339980)
	public void test45() throws Exception {
		helper1(45,new String[] { "p.A" }, "p.A", 8, 10, 8, 20, PARAMETER, "fooBar", true, true);
	}

	// bug 385989
	public void test46() throws Exception {
		helper1(46,new String[] { "p.A", "p.B" }, "p.A", 6, 10, 6, 13, PARAMETER, "b", true, true);
	}
	
	// bug 385550
	public void test47() throws Exception {
		helper1(47,new String[] { "p.A" }, "p.A", 8, 17, 8, 17, PARAMETER, "target", true, true);
	}

	// bug 411529
	public void test48() throws Exception {
		helper1(48,new String[] { "p.A", "p.B", "q.C" }, "p.B", 3, 17, 3, 17, PARAMETER, "c", true, true);
	}
	
	//bug 356687
	public void test49() throws Exception {
		helper1(49,new String[] {"p.A"}, "p.A", 5, 10, 5, 11, FIELD, "b", true, true);
	}
	
	//bug 356687
	public void test50() throws Exception {
		helper1(50,new String[] {"p.A"}, "p.A", 4, 10, 4, 11, PARAMETER, "b", true, true);
	}

	//bug 356687
	public void test51() throws Exception {
		helper1(51,new String[] {"p.A"}, "p.A", 4, 10, 4, 11, PARAMETER, "b", true, true);
	}

	//bug 356687
	public void test52() throws Exception {
		helper1(52,new String[] {"p.A"}, "p.A", 5, 10, 5, 11, FIELD, "b", true, true);
	}
	
	//bug 356687
	public void test53() throws Exception {
		helper1(53,new String[] {"p.A"}, "p.A", 4, 10, 4, 11, PARAMETER, "b", true, true);
	}
	
	//bug 356687
	public void test54() throws Exception {
		helper1(54,new String[] {"p.A"}, "p.A", 4, 15, 4, 16, PARAMETER, "b", false, false);
	}
	
	//bug 356687
	public void test55() throws Exception {
		helper1(55,new String[] {"p.A"}, "p.A", 4, 17, 4, 18, FIELD, "b", true, true);
	}
	
	//bug 356687
	public void test56() throws Exception {
		helper1(56,new String[] {"p.A"}, "p.A", 3, 17, 3, 18, PARAMETER, "b", true, true);
	}
	
	//bug 356687
	public void test57() throws Exception {
		helper1(57,new String[] { "p.A" }, "p.A", 5, 17, 5, 18, PARAMETER, "b", true, true);
	}

	//bug 217753
	public void test58() throws Exception {
		helper1(58,new String[] { "p.A", "p.B" }, "p.A", 8, 25, 8, 28, PARAMETER, "b", true, true);
	}

	// bug 217753
	public void test59() throws Exception {
		helper1(59,new String[] { "p.A", "p.B" }, "p.A", 5, 14, 5, 15, PARAMETER, "b", true, true);
	}

	// bug 217753
	public void test60() throws Exception {
		helper1(60,new String[] { "p.A" }, "p.A", 5, 14, 5, 15, PARAMETER, "b", true, true);
	}

	// bug 217753
	public void test61() throws Exception {
		helper1(61,new String[] { "p.A" }, "p.A", 5, 14, 5, 15, PARAMETER, "b", true, true);
	}

	// bug 217753
	public void test62() throws Exception {
		helper1(62,new String[] { "p.A", "q.B" }, "p.A", 8, 14, 8, 15, PARAMETER, "c", true, true);
	}

	// bug 217753
	public void test63() throws Exception {
		helper1(63,new String[] { "A" }, "A", 2, 10, 2, 11, PARAMETER, "b", true, true);
	}

	// bug 404477
	public void test64() throws Exception {
		helper1(64,new String[] { "A" }, "A", 3, 17, 3, 18, PARAMETER, "b", true, true);
	}

	// bug 404471
	public void test65() throws Exception {
		helper1(65,new String[] { "A" }, "A", 3, 17, 3, 18, PARAMETER, "c", false, false);
	}

	// bug 426112
	public void test66() throws Exception {
		helper1(66,new String[] { "A" }, "A", 2, 17, 2, 20, PARAMETER, "a", true, true);
	}

	// bug 436997 - references enclosing generic type
	public void test67() throws Exception {
		helper1(67,new String[] { "A" }, "A", 6, 14, 6, 15, FIELD, "b", true, true);
	}
	
	// Move mA1 to field fB, do not inline delegator
	public void test3() throws Exception {
		helper1(3,new String[] { "p1.A", "p2.B", "p3.C"}, "p1.A", 9, 17, 9, 20, FIELD, "fB", false, false);
	}

	// // Move mA1 to field fB, inline delegator, remove delegator
	public void test4() throws Exception {
		// printTestDisabledMessage("not implemented yet");
		helper1(4,new String[] { "p1.A", "p2.B", "p3.C"}, "p1.A", 9, 17, 9, 20, FIELD, "fB", true, true);
	}

	// Move mA1 to field fB, unqualified static member references are qualified
	public void test5() throws Exception {
		helper1(5,new String[] { "p1.A", "p2.B"}, "p1.A", 15, 19, 15, 19, FIELD, "fB", false, false);
	}

	// class qualify referenced type name to top level, original receiver not used in method
	public void test6() throws Exception {
		helper1(6,new String[] { "p1.Nestor", "p2.B"}, "p1.Nestor", 11, 17, 11, 17, PARAMETER, "b", false, false);
	}

	public void test7() throws Exception {
		helper1(7,new String[] { "p1.A", "p2.B", "p3.N1"}, "p1.A", 8, 17, 8, 18, PARAMETER, "b", false, false);
	}

	// access to fields, non-void return type
	public void test8() throws Exception {
		helper1(8,new String[] { "p1.A", "p2.B"}, "p1.A", 15, 19, 15, 20, PARAMETER, "b", false, false);
	}

	// multiple parameters, some left of new receiver parameter, some right of it,
	// "this" is passed as argument
	public void test9() throws Exception {
		helper1(9,new String[] { "p1.A", "p2.B"}, "p1.A", 6, 17, 6, 17, PARAMETER, "b", false, false);
	}
	public static void main(String[] args) throws Exception {
		
		String inout = "in";
		for (int k = 0; k<= 1; k++) {
			if (k == 1) {
				inout = "out";
			}
		
		for (int i = 0; i<= 67; i++) {
			
			String path = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/movemethod/test"+i+"/"+inout+"/";
			File f = new File(path);
			String[] list = f.list();
			for (String file : list) {
				
				InputManager in = new InputManagerASCII(path + file);
				in.openFile();
				String contents = "";
				boolean ok = false;
//				int j = 1;
				while(!in.isEndOfFile()) {
					String line = in.readLine();
					if (line.contains("//")) {
						line = line.substring(0,line.indexOf("//"));
					}
//					if (!ok && !line.startsWith("//")) {
//						break;
//					}
//					if (!ok && line.startsWith("//")) {
//						ok = true;
//					}
//					if (ok) {
						contents += line+" \n";
//					}
//					ok = true;
//					if (j == 8) {
//						System.out.println(line.subSequence(9, 19));
//					}
//					j++;
				}
//				if (!ok) continue;
				in.closeFile();
				OutputManager out = new OutputManagerASCII(path + file);
				out.createFile();
				out.writeLine(contents);
				out.closeFile();
			}
		}
		}
//		new Test().test1();
	}
}
