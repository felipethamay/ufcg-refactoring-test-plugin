package refactoringtestplugin.renamemethod;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class TestStatic {
	
	
	public static boolean updateRef = false;
	public static boolean createDelegate;
	public static String METHOD_TO_RENAME = "m";
	public static String NEW_METHOD_NAME = "k";
	public static String CLASS_NAME = "A";
	
	
	


	private void helper2_0(String methodName, String newMethodName, String[] signatures, boolean updateReferences, boolean createDelegate) throws Exception{
		this.METHOD_TO_RENAME = methodName;
		this.NEW_METHOD_NAME = newMethodName;
		this.updateRef  = updateReferences;
		this.createDelegate = createDelegate;
		this.CLASS_NAME = "A";
		
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType classA= getType(cu, "A");
//		IMethod method= classA.getMethod(methodName, signatures);
//		RenameJavaElementDescriptor descriptor= RefactoringSignatureDescriptorFactory.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_METHOD);
//		descriptor.setUpdateReferences(updateReferences);
//		descriptor.setJavaElement(method);
//		descriptor.setNewName(newMethodName);
//		descriptor.setKeepOriginal(createDelegate);
//		descriptor.setDeprecateDelegate(true);
//
//		assertEquals("was supposed to pass", null, performRefactoring(descriptor));
//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName("A")), cu.getSource());
//
//		assertTrue("anythingToUndo", RefactoringCore.getUndoManager().anythingToUndo());
//		assertTrue("! anythingToRedo", !RefactoringCore.getUndoManager().anythingToRedo());
//		//assertEquals("1 to undo", 1, Refactoring.getUndoManager().getRefactoringLog().size());
//
//		RefactoringCore.getUndoManager().performUndo(null, new NullProgressMonitor());
//		assertEqualLines("invalid undo", getFileContents(getInputTestFileName("A")), cu.getSource());
//
//		assertTrue("! anythingToUndo", !RefactoringCore.getUndoManager().anythingToUndo());
//		assertTrue("anythingToRedo", RefactoringCore.getUndoManager().anythingToRedo());
//		//assertEquals("1 to redo", 1, Refactoring.getUndoManager().getRedoStack().size());
//
//		RefactoringCore.getUndoManager().performRedo(null, new NullProgressMonitor());
//		assertEqualLines("invalid redo", getFileContents(getOutputTestFileName("A")), cu.getSource());
	}
	private void helper2_0(String methodName, String newMethodName, String[] signatures) throws Exception{
		helper2_0(methodName, newMethodName, signatures, true, false);
	}

	private void helper2(boolean updateReferences) throws Exception{
		helper2_0("m", "k", new String[0], updateReferences, false);
	}

	private void helper2() throws Exception{
		helper2(true);
	}

	private void helperDelegate() throws Exception{
		helper2_0("m", "k", new String[0], true, true);
	}

	
	public void test0() throws Exception{
		helper2();
	}

	public void test1() throws Exception{
		helper2();
	}

	public void test2() throws Exception{
		helper2();
	}

	public void test3() throws Exception{
		helper2();
	}

	public void test4() throws Exception{
		helper2();
	}

	public void test5() throws Exception{
		helper2();
	}

	public void test6() throws Exception{
		helper2();
	}

	public void test7() throws Exception{
		helper2_0("m", "k", new String[]{Signature.SIG_INT});
	}

	public void test8() throws Exception{
		helper2_0("m", "k", new String[]{Signature.SIG_INT});
	}

	public void test9() throws Exception{
		helper2_0("m", "k", new String[]{Signature.SIG_INT}, false, false);
	}

	public void test10() throws Exception{
//		printTestDisabledMessage("bug 40628");
//		if (true)	return;
		
		this.METHOD_TO_RENAME = "method";
		this.NEW_METHOD_NAME = "newmethod";
		this.updateRef  = true;
		this.createDelegate = createDelegate;
		this.CLASS_NAME = "B";
		
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
//		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");
//
//		IType classB= getType(cuB, "B");
//		IMethod method= classB.getMethod("method", new String[0]);
//		RenameJavaElementDescriptor descriptor= RefactoringSignatureDescriptorFactory.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_METHOD);
//		descriptor.setUpdateReferences(true);
//		descriptor.setJavaElement(method);
//		descriptor.setNewName("newmethod");
//
//		assertEquals("was supposed to pass", null, performRefactoring(descriptor));
//		assertEqualLines("invalid renaming in A", getFileContents(getOutputTestFileName("A")), cuA.getSource());
//		assertEqualLines("invalid renaming in B", getFileContents(getOutputTestFileName("B")), cuB.getSource());
	}

	public void test11() throws Exception{
//		printTestDisabledMessage("bug 40452");
//		if (true)	return;
		this.METHOD_TO_RENAME = "method2";
		this.NEW_METHOD_NAME = "fred";
		this.updateRef  = true;
		this.createDelegate = createDelegate;
		this.CLASS_NAME = "A";
		
		
//		IPackageFragment packageA= getRoot().createPackageFragment("a", false, new NullProgressMonitor());
//		IPackageFragment packageB= getRoot().createPackageFragment("b", false, new NullProgressMonitor());
//		ICompilationUnit cuA= createCUfromTestFile(packageA, "A");
//		ICompilationUnit cuB= createCUfromTestFile(packageB, "B");
//
//		IType classA= getType(cuA, "A");
//		IMethod method= classA.getMethod("method2", new String[0]);
//		RenameJavaElementDescriptor descriptor= RefactoringSignatureDescriptorFactory.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_METHOD);
//		descriptor.setUpdateReferences(true);
//		descriptor.setJavaElement(method);
//		descriptor.setNewName("fred");
//
//		assertEquals("was supposed to pass", null, performRefactoring(descriptor));
//		assertEqualLines("invalid renaming in A", getFileContents(getOutputTestFileName("A")), cuA.getSource());
//		assertEqualLines("invalid renaming in B", getFileContents(getOutputTestFileName("B")), cuB.getSource());
	}

	public void testUnicode01() throws Exception{
		helper2_0("e", "f", new String[]{});
	}



	public void testStaticImport1() throws Exception {
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "C");
		helper2();
//		assertEqualLines("invalid renaming in C", getFileContents(getOutputTestFileName("C")), cuA.getSource());
	}

	public void testStaticImport2() throws Exception {
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "C");
		helper2();
//		assertEqualLines("invalid renaming in C", getFileContents(getOutputTestFileName("C")), cuA.getSource());
	}

	public void testStaticImport3() throws Exception {
//		if (BUG_83332_SPLIT_SINGLE_IMPORT) {
//			printTestDisabledMessage("BUG_83332_SPLIT_SINGLE_IMPORT");
//			return;
//		}
		helper2();
	}

	public void testStaticImport4() throws Exception {
		helper2();
	}

	public void testStaticImport5() throws Exception {
//		if (BUG_83332_SPLIT_SINGLE_IMPORT) {
//			printTestDisabledMessage("BUG_83332_SPLIT_SINGLE_IMPORT");
//			return;
//		}
		helper2();
	}

	public void testDelegate01() throws Exception  {
		// simple static delegate
		helperDelegate();
	}
	
	public static void main(String[] args) throws Exception {
		
		String inout = "in";
		for (int k = 0; k<= 1; k++) {
			if (k == 1) {
				inout = "out";
			}
		
			String p = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/rename_staticmethod/";
			File ff = new File(p);
			String[] list2 = ff.list();
			for (String name : list2) {
				if (!name.startsWith("test")) continue;
				String path = p+name+"/"+inout+"/";
				File f = new File(path);
				String[] list = f.list();
				
				for (String file : list) {
					if (new File(path + file).isDirectory()) continue;
					InputManager in = new InputManagerASCII(path + file);
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
