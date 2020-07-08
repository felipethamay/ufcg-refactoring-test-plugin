package refactoringtestplugin.renamemethod;

import java.io.File;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class TestPrivate {
	
	
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



	private void helper2_0(String methodName, String newMethodName, String[] signatures, boolean updateReferences, boolean createDelegate) throws Exception{
		this.METHOD_TO_RENAME = methodName;
		this.NEW_METHOD_NAME = newMethodName;
		this.updateRef  = updateReferences;
		this.createDelegate = createDelegate;
		this.CLASS_NAME = "A";
		
//		ParticipantTesting.reset();
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType classA= getType(cu, "A");
//		IMethod method= classA.getMethod(methodName, signatures);
//		String[] handles= ParticipantTesting.createHandles(method);
//		RenameMethodProcessor processor= new RenameNonVirtualMethodProcessor(method);
//		RenameRefactoring refactoring= new RenameRefactoring(processor);
//		processor.setUpdateReferences(updateReferences);
//		processor.setNewElementName(newMethodName);
//		processor.setDelegateUpdating(createDelegate);
//		assertEquals("was supposed to pass", null, performRefactoring(refactoring));
//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName("A")), cu.getSource());
//
//		ParticipantTesting.testRename(
//			handles,
//			new RenameArguments[] {
//				new RenameArguments(newMethodName, updateReferences)});
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

	public void test10() throws Exception{
		helper2();
	}

	public void test11() throws Exception{
		helper2();
	}

	public void test12() throws Exception{
		helper2();
	}

	public void test13() throws Exception{
		helper2();
	}

	public void test14() throws Exception{
		helper2();
	}

	public void test15() throws Exception{
		helper2_0("m", "k", new String[]{"I"});
	}

	public void test16() throws Exception{
		helper2_0("m", "fred", new String[]{"I"});
	}

	public void test17() throws Exception{
		helper2_0("m", "kk", new String[]{"I"});
	}

	public void test18() throws Exception{
		
		this.METHOD_TO_RENAME = "m";
		this.NEW_METHOD_NAME = "kk";
		this.updateRef  = true;
		this.createDelegate = createDelegate;
		this.CLASS_NAME = "B";
		
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		ICompilationUnit cuC= createCUfromTestFile(getPackageP(), "C");
//
//		IType classB= getType(cu, "B");
//		RenameMethodProcessor processor= new RenameNonVirtualMethodProcessor(classB.getMethod("m", new String[]{"I"}));
//		RenameRefactoring refactoring= new RenameRefactoring(processor);
//		processor.setNewElementName("kk");
//
//		assertEquals("was supposed to pass", null, performRefactoring(refactoring));
//		assertEqualLines("invalid renaming A", getFileContents(getOutputTestFileName("A")), cu.getSource());
//		assertEqualLines("invalid renaming C", getFileContents(getOutputTestFileName("C")), cuC.getSource());

	}

	public void test2() throws Exception{
		helper2_0("m", "fred", new String[0]);
	}

	public void test20() throws Exception{
		helper2_0("m", "fred", new String[]{"I"});
	}

	public void test23() throws Exception{
		helper2_0("m", "k", new String[0]);
	}

	public void test24() throws Exception{
		helper2_0("m", "k", new String[]{"QString;"});
	}

	public void test25() throws Exception{
		helper2_0("m", "k", new String[]{"[QString;"});
	}

	public void test26() throws Exception{
		helper2_0("m", "k", new String[0]);
	}

	public void test27() throws Exception{
		helper2_0("m", "k", new String[0], false, false);
	}

	public void testAnon0() throws Exception{
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
		
			String p = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/rename_privatemethod/";
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
