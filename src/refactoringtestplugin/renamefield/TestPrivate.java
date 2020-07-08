package refactoringtestplugin.renamefield;

import java.io.File;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class TestPrivate {
	
	
//	public static boolean updateRef = false;
	public static boolean createDelegate;
	public static boolean fUpdateReferences= true;
	public static boolean fUpdateTextualMatches= true;
	public static boolean fRenameGetter= false;
	public static boolean fRenameSetter= false;
	public static String FIELD_TO_RENAME = "m";
	public static String NEW_FIELD_NAME = "k";
	public static String CLASS_NAME = "A";
	
	private static final String[] NO_ARGUMENTS= new String[0];
	
	private void helper2(String fieldName, String newFieldName, boolean updateReferences, boolean updateTextualMatches,
			boolean renameGetter, boolean renameSetter,
			boolean expectedGetterRenameEnabled, boolean expectedSetterRenameEnabled) throws Exception{
		
		this.FIELD_TO_RENAME = fieldName;
		this.NEW_FIELD_NAME = newFieldName;
//		this.updateRef  = updateReferences;
		this.createDelegate = createDelegate;
		this.CLASS_NAME = "A";
		this.fRenameGetter = renameGetter;
		this.fRenameSetter = renameSetter;
		this.fUpdateReferences = updateReferences;
		this.fUpdateTextualMatches = fUpdateTextualMatches;
//ParticipantTesting.reset();
//ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//IType classA= getType(cu, "A");
//IField field= classA.getField(fieldName);
//RenameJavaElementDescriptor descriptor= RefactoringSignatureDescriptorFactory.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_FIELD);
//descriptor.setJavaElement(field);
//descriptor.setNewName(newFieldName);
//descriptor.setUpdateReferences(updateReferences);
//descriptor.setUpdateTextualOccurrences(updateTextualMatches);
//descriptor.setRenameGetters(renameGetter);
//descriptor.setRenameSetters(renameSetter);
//
//RenameRefactoring refactoring= (RenameRefactoring) createRefactoring(descriptor);
//RenameFieldProcessor processor= (RenameFieldProcessor) refactoring.getProcessor();
//assertEquals("getter rename enabled", expectedGetterRenameEnabled, processor.canEnableGetterRenaming() == null);
//assertEquals("setter rename enabled", expectedSetterRenameEnabled, processor.canEnableSetterRenaming() == null);
//
//String newGetterName= processor.getNewGetterName();
//String newSetterName= processor.getNewSetterName();
//
//List<IAnnotatable> elements= new ArrayList<>();
//elements.add(field);
//List<RenameArguments> args= new ArrayList<>();
//args.add(new RenameArguments(newFieldName, updateReferences));
//if (renameGetter && expectedGetterRenameEnabled) {
//elements.add(processor.getGetter());
//args.add(new RenameArguments(newGetterName, updateReferences));
//}
//if (renameSetter && expectedSetterRenameEnabled) {
//elements.add(processor.getSetter());
//args.add(new RenameArguments(newSetterName, updateReferences));
//}
//String[] renameHandles= ParticipantTesting.createHandles(elements.toArray());
//
//RefactoringStatus result= performRefactoring(refactoring);
//assertEquals("was supposed to pass", null, result);
//assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName("A")), cu.getSource());
//
//ParticipantTesting.testRename(
//renameHandles,
//args.toArray(new RenameArguments[args.size()]));
//
//assertTrue("anythingToUndo", RefactoringCore.getUndoManager().anythingToUndo());
//assertTrue("! anythingToRedo", !RefactoringCore.getUndoManager().anythingToRedo());
//
//RefactoringCore.getUndoManager().performUndo(null, new NullProgressMonitor());
//assertEqualLines("invalid undo", getFileContents(getInputTestFileName("A")), cu.getSource());
//
//assertTrue("! anythingToUndo", !RefactoringCore.getUndoManager().anythingToUndo());
//assertTrue("anythingToRedo", RefactoringCore.getUndoManager().anythingToRedo());
//
//RefactoringCore.getUndoManager().performRedo(null, new NullProgressMonitor());
//assertEqualLines("invalid redo", getFileContents(getOutputTestFileName("A")), cu.getSource());
}

private void helper2(boolean updateReferences) throws Exception{
helper2("f", "g", updateReferences, false, false, false, false, false);
}

private void helper2() throws Exception{
helper2(true);
}


// ------
public void test0() throws Exception{
helper2();
}

public void test1() throws Exception{
helper2();
}

public void test2() throws Exception{
helper2(false);
}

public void test3() throws Exception{
helper2("f", "gg", true, true, false, false, false, false);
}

public void test4() throws Exception{
helper2("fMe", "fYou", true, false, true, true, true, true);
}

public void test5() throws Exception{
//regression test for 9895
helper2("fMe", "fYou", true, false, true, false, true, false);
}

public void test6() throws Exception{
//regression test for 9895 - opposite case
helper2("fMe", "fYou", true, false, false, true, false, true);
}

public void test7() throws Exception{
//regression test for 21292
helper2("fBig", "fSmall", true, false, true, true, true, true);
}

public void test8() throws Exception{
//regression test for 26769
helper2("f", "g", true, false, true, false, true, false);
}

public void test9() throws Exception{
//regression test for 30906
helper2("fBig", "fSmall", true, false, true, true, true, true);
}

public void test10() throws Exception{
//regression test for 81084
//if (BUG_81084) {
//printTestDisabledMessage("BUG_81084");
//return;
//}
helper2("fList", "fElements", true, false, false, false, false, false);
}

public void test11() throws Exception{
//if (BUG_75642_GENERIC_METHOD_SEARCH) {
//printTestDisabledMessage("BUG_75642_GENERIC_METHOD_SEARCH");
//return;
//}
helper2("fList", "fElements", true, false, true, true, true, true);
}

public void testUnicode01() throws Exception{
//regression test for 180331
helper2("field", "feel", true, false, true, true, true, true);
}
	public static void main(String[] args) throws Exception {
		
		String inout = "in";
		for (int k = 0; k<= 1; k++) {
			if (k == 1) {
				inout = "out";
			}
		
			String p = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/renamefield/private/";
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
