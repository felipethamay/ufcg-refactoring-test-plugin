package refactoringtestplugin.renamemethod;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class TestInterface {
	
	
	public static boolean updateRef = false;
	public static boolean createDelegate;
	public static String METHOD_TO_RENAME = "m";
	public static String NEW_METHOD_NAME = "k";
	public static String CLASS_NAME = "A";
	
	private static final String[] NO_ARGUMENTS= new String[0];
	
//	private void helper1_not_available(String methodName, String[] signatures) throws Exception{
//		IType classA= getType(createCUfromTestFile(getPackageP(), "A"), "A");
//		RenameMethodProcessor processor= new RenameVirtualMethodProcessor(classA.getMethod(methodName, signatures));
//		RenameRefactoring ref= new RenameRefactoring(processor);
//		assertTrue(! ref.isApplicable());
//	}



//	private void helper1_not_available(String methodName, String[] signatures) throws Exception{
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType interfaceI= getType(cu, "I");
//
//		RenameProcessor processor= new RenameVirtualMethodProcessor(interfaceI.getMethod(methodName, signatures));
//		RenameRefactoring ref= new RenameRefactoring(processor);
//		assertTrue(! ref.isApplicable());
//	}




	private void helper2_0(String methodName, String newMethodName, String[] signatures, boolean shouldPass, boolean updateReferences, boolean createDelegate) throws Exception{
		this.METHOD_TO_RENAME = methodName;
		this.NEW_METHOD_NAME = newMethodName;
		this.updateRef  = updateReferences;
		this.createDelegate = createDelegate;
		this.CLASS_NAME = "I";
		
		//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType interfaceI= getType(cu, "I");
//		IMethod method= interfaceI.getMethod(methodName, signatures);
//
//		RenameJavaElementDescriptor descriptor= RefactoringSignatureDescriptorFactory.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_METHOD);
//		descriptor.setJavaElement(method);
//		descriptor.setUpdateReferences(updateReferences);
//		descriptor.setNewName(newMethodName);
//		descriptor.setKeepOriginal(createDelegate);
//		descriptor.setDeprecateDelegate(true);
//
//		assertEquals("was supposed to pass", null, performRefactoring(descriptor));
//		if (!shouldPass){
//			assertTrue("incorrect renaming because of a java model bug", ! getFileContents(getOutputTestFileName("A")).equals(cu.getSource()));
//			return;
//		}
//		assertEqualLines("incorrect renaming", getFileContents(getOutputTestFileName("A")), cu.getSource());
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

	private void helper2(boolean updateReferences) throws Exception{
		helper2_0("m", "k", NO_ARGUMENTS, true, updateReferences, false);
	}

	private void helper2() throws Exception{
		helper2(true);
	}

	private void helperDelegate() throws Exception{
		helper2_0("m", "k", NO_ARGUMENTS, true, true, true);
	}

// --------------------------------------------------------------------------

	public void testAnnotation0() throws Exception{
		helper2_0("name", "ident", NO_ARGUMENTS, true, true, false);
	}

	public void testAnnotation1() throws Exception{
		helper2_0("value", "number", NO_ARGUMENTS, true, true, false);
	}

	public void testAnnotation2() throws Exception{
		helper2_0("thing", "value", NO_ARGUMENTS, true, true, false);
	}

	public void testAnnotation3() throws Exception{
		helper2_0("value", "num", NO_ARGUMENTS, true, true, false);
	}

	public void testAnnotation4() throws Exception{
		// see also bug 83064
		helper2_0("value", "num", NO_ARGUMENTS, true, true, false);
	}

	public void testGenerics01() throws Exception {
		helper2_0("getXYZ", "zYXteg", new String[] {"QList<QSet<QRunnable;>;>;"}, true, true, false);
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
		helper2();
	}
	public void test10() throws Exception{
		helper2();
	}
	public void test11() throws Exception{
		helper2();
	}
	public void test12() throws Exception{
		helper2();
	}

	//test13 became testFail45
	//public void test13() throws Exception{
	//	helper2();
	//}
	public void test14() throws Exception{
		helper2();
	}
	public void test15() throws Exception{
		helper2();
	}
	public void test16() throws Exception{
		helper2();
	}
	public void test17() throws Exception{
		helper2();
	}
	public void test18() throws Exception{
		helper2();
	}
	public void test19() throws Exception{
		helper2();
	}
	public void test20() throws Exception{
		helper2();
	}
	
	public void test22() throws Exception{
		helper2();
	}

	//test23 became testFail45
	//public void test23() throws Exception{
	//	helper2();
	//}

	public void test24() throws Exception{
		helper2();
	}
	public void test25() throws Exception{
		helper2();
	}
	public void test26() throws Exception{
		helper2();
	}
	public void test27() throws Exception{
		helper2();
	}
	public void test28() throws Exception{
		helper2();
	}
	public void test29() throws Exception{
		helper2();
	}
	public void test30() throws Exception{
		helper2();
	}
	//anonymous inner class
	public void test31() throws Exception{
		helper2();
	}
	//anonymous inner class
	public void test32() throws Exception{
		helper2();
	}
	public void test33() throws Exception{
		helper2();
	}
	public void test34() throws Exception{
		helper2();
	}
	public void test35() throws Exception{
		helper2();
	}
	public void test36() throws Exception{
		helper2();
	}
	public void test37() throws Exception{
		helper2();
	}
	public void test38() throws Exception{
		helper2();
	}
	public void test39() throws Exception{
		helper2();
	}
	public void test40() throws Exception{
		helper2();
	}
	public void test41() throws Exception{
		helper2();
	}
	public void test42() throws Exception{
		helper2();
	}
	public void test43() throws Exception{
		helper2();
	}
	public void test44() throws Exception{
		helper2();
	}
	public void test45() throws Exception{
		helper2();
	}
	public void test46() throws Exception{
		helper2(false);
	}
	public void test47() throws Exception{
		helper2();
	}

	public void testDelegate01() throws Exception {
		// simple delegate
		helperDelegate();
	}
	public void testDelegate02() throws Exception {
		// "overridden" delegate
		helperDelegate();
	}
	public static void main(String[] args) throws Exception {
		
		String inout = "in";
		for (int k = 0; k<= 1; k++) {
			if (k == 1) {
				inout = "out";
			}
		
			String p = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/rename_interfacemethod/";
			
			File ff = new File(p);
			String[] list2 = ff.list();
			for (String name : list2) {
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
