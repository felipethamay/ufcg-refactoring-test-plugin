package refactoringtestplugin.renamefield;

import java.io.File;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class TestNonPrivate {
	
	
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
	
	private void helper2(String fieldName, String newFieldName) throws Exception {
		helper2(fieldName, newFieldName, false);
	}

	private void helper2(String fieldName, String newFieldName, boolean createDelegates) throws Exception{
		this.FIELD_TO_RENAME = fieldName;
		this.NEW_FIELD_NAME = newFieldName;
//		this.updateRef  = updateReferences;
		this.createDelegate = createDelegate;
		this.CLASS_NAME = "A";
		
//		ParticipantTesting.reset();
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType classA= getType(cu, "A");
//		IField field= classA.getField(fieldName);
//		boolean isEnum= JdtFlags.isEnum(field);
//		String id= isEnum ? IJavaRefactorings.RENAME_ENUM_CONSTANT : IJavaRefactorings.RENAME_FIELD;
//		RenameJavaElementDescriptor descriptor= RefactoringSignatureDescriptorFactory.createRenameJavaElementDescriptor(id);
//		descriptor.setJavaElement(field);
//		descriptor.setNewName(newFieldName);
//		descriptor.setUpdateReferences(fUpdateReferences);
//		descriptor.setUpdateTextualOccurrences(fUpdateTextualMatches);
//		if (!isEnum) {
//			descriptor.setRenameGetters(fRenameGetter);
//			descriptor.setRenameSetters(fRenameSetter);
//			descriptor.setKeepOriginal(createDelegates);
//			descriptor.setDeprecateDelegate(true);
//		}
//		RenameRefactoring refactoring= (RenameRefactoring) createRefactoring(descriptor);
//		RenameFieldProcessor processor= (RenameFieldProcessor) refactoring.getProcessor();
//
//		List<IAnnotatable> elements= new ArrayList<>();
//		elements.add(field);
//		List<RenameArguments> args= new ArrayList<>();
//		args.add(new RenameArguments(newFieldName, fUpdateReferences));
//		if (fRenameGetter) {
//			elements.add(processor.getGetter());
//			args.add(new RenameArguments(processor.getNewGetterName(), fUpdateReferences));
//		}
//		if (fRenameSetter) {
//			elements.add(processor.getSetter());
//			args.add(new RenameArguments(processor.getNewSetterName(), fUpdateReferences));
//		}
//		String[] renameHandles= ParticipantTesting.createHandles(elements.toArray());
//
//		RefactoringStatus result= performRefactoring(refactoring);
//		assertEquals("was supposed to pass", null, result);
//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName("A")), cu.getSource());
//
//		ParticipantTesting.testRename(
//				renameHandles,
//				args.toArray(new RenameArguments[args.size()]));
//
//		assertTrue("anythingToUndo", RefactoringCore.getUndoManager().anythingToUndo());
//		assertTrue("! anythingToRedo", !RefactoringCore.getUndoManager().anythingToRedo());
//
//		RefactoringCore.getUndoManager().performUndo(null, new NullProgressMonitor());
//		assertEqualLines("invalid undo", getFileContents(getInputTestFileName("A")), cu.getSource());
//
//		assertTrue("! anythingToUndo", !RefactoringCore.getUndoManager().anythingToUndo());
//		assertTrue("anythingToRedo", RefactoringCore.getUndoManager().anythingToRedo());
//
//		RefactoringCore.getUndoManager().performRedo(null, new NullProgressMonitor());
//		assertEqualLines("invalid redo", getFileContents(getOutputTestFileName("A")), cu.getSource());
	}

	private void helper2() throws Exception{
		helper2("f", "g");
	}

	
	// ------
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
		//printTestDisabledMessage("1GKZ8J6: ITPJCORE:WIN2000 - search: missing field occurrecnces");
	}

	public void test5() throws Exception{
		helper2();
	}

	public void test6() throws Exception{
		//printTestDisabledMessage("1GKB9YH: ITPJCORE:WIN2000 - search for field refs - incorrect results");
		helper2();
	}

	public void test7() throws Exception{
		helper2();
	}

	public void test8() throws Exception{
		//printTestDisabledMessage("1GD79XM: ITPJCORE:WINNT - Search - search for field references - not all found");
		helper2();
	}

	public void test9() throws Exception{
		helper2();
	}

	public void test10() throws Exception{
		helper2();
	}

	public void test11() throws Exception{
		helper2();
	}

	public void test12() throws Exception{
		//System.out.println("\nRenameNonPrivateField::" + name() + " disabled (1GIHUQP: ITPJCORE:WINNT - search for static field should be more accurate)");
		helper2();
	}

	public void test13() throws Exception{
		//System.out.println("\nRenameNonPrivateField::" + name() + " disabled (1GIHUQP: ITPJCORE:WINNT - search for static field should be more accurate)");
		helper2();
	}

	public void test14() throws Exception{
		fUpdateReferences= false;
		fUpdateTextualMatches= false;
		helper2();
	}

	public void test15() throws Exception{
		fUpdateReferences= false;
		fUpdateTextualMatches= false;
		helper2();
	}

	public void test16() throws Exception{
//		printTestDisabledMessage("text for bug 20693");
		helper2();
	}

	public void test17() throws Exception{
//		printTestDisabledMessage("test for bug 66250, 79131 (corner case: reference "A.f" to p.A#f)");
		fUpdateReferences= false;
		fUpdateTextualMatches= true;
		helper2("f", "g");
	}

	public void test18() throws Exception{
//		printTestDisabledMessage("test for 79131 (corner case: reference "A.f" to p.A#f)");
		fUpdateReferences= false;
		fUpdateTextualMatches= true;
		helper2("field", "member");
	}

//--- test 1.5 features: ---
	public void test19() throws Exception{
		fRenameGetter= true;
		fRenameSetter= true;
		helper2("list", "items");
	}

	public void test20() throws Exception{
		helper2("list", "items");
	}

	public void test21() throws Exception{
		helper2("fValue", "fOrdinal");
	}

	public void test22() throws Exception{
		fRenameGetter= true;
		fRenameSetter= true;
		helper2("tee", "thing");
	}

	public void test23() throws Exception{
		fRenameGetter= true;
		fRenameSetter= true;
		helper2();
	}

//--- end test 1.5 features. ---

	public void testBug5821() throws Exception{
		helper2("test", "test1");
	}

	public void testStaticImport() throws Exception{
		//bug 77622
//		IPackageFragment test1= getRoot().createPackageFragment("test1", true, null);
//		ICompilationUnit cuC= null;
//		ICompilationUnit cuB= createCUfromTestFile(test1, "B");
//		cuC= createCUfromTestFile(getRoot().getPackageFragment(""), "C");

		helper2("PI", "e");

//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName("B")), cuB.getSource());
//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName("C")), cuC.getSource());
	}

	public void testEnumConst() throws Exception {
		//bug 77619
//		IPackageFragment test1= getRoot().createPackageFragment("test1", true, null);
//		ICompilationUnit cuC= null;
//		ICompilationUnit cuB= createCUfromTestFile(test1, "B");
//		cuC= createCUfromTestFile(getRoot().getPackageFragment(""), "C");

		helper2("RED", "REDDISH");

//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName("B")), cuB.getSource());
//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName("C")), cuC.getSource());
	}

	public void testGenerics1() throws Exception {
		helper2();
	}

	public void testGenerics2() throws Exception {
		fRenameSetter= true;
		fRenameGetter= true;
		helper2();
	}

	public void testGenerics3() throws Exception {
//		if (BUG_79990_CORE_SEARCH_METHOD_DECL) {
//			printTestDisabledMessage("BUG_79990_CORE_SEARCH_METHOD_DECL");
//			return;
//		}
		fRenameSetter= true;
		fRenameGetter= true;
		helper2();
	}

	public void testGenerics4() throws Exception {
		fRenameSetter= true;
		fRenameGetter= true;
		helper2("count", "number");
	}

	public void testEnumField() throws Exception {
		fRenameSetter= true;
		fRenameGetter= true;
		helper2("buddy", "other");
	}

	public void testAnnotation1() throws Exception {
		helper2("ZERO", "ZORRO");
	}

	public void testAnnotation2() throws Exception {
		helper2("ZERO", "ZORRO");
	}

	public void testDelegate01() throws Exception {
		// a simple delegate
		helper2("f", "g", true);
	}

	public void testDelegate02() throws Exception {
		// nonstatic field, getter and setter
		fRenameSetter= true;
		fRenameGetter= true;
		helper2("f", "g", true);
	}

	public void testDelegate03() throws Exception {
		// create delegates for the field and a getter
		fRenameGetter= true;
		helper2("f", "g", true);
	}

	public void testRenameNLSAccessor01() throws Exception {
//		IFile file= createPropertiesFromTestFile("messages");

		helper2("f", "g");

//		assertEqualLines(getExpectedFileConent("messages"), getContents(file));
	}

//	private String getExpectedFileConent(String propertyName) throws IOException {
//		String fileName= getOutputTestFileName(propertyName);
//		fileName= fileName.substring(0, fileName.length() - ".java".length()) + ".properties";
//		return getContents(getFileInputStream(fileName));
//	}

//	private IFile createPropertiesFromTestFile(String propertyName) throws IOException, CoreException {
//		IFolder pack= (IFolder) getPackageP().getResource();
//		IFile file= pack.getFile(propertyName + ".properties");
//
//		String fileName= getInputTestFileName(propertyName);
//		fileName= fileName.substring(0, fileName.length() - ".java".length()) + ".properties";
//		InputStream inputStream= getFileInputStream(fileName);
//		file.create(inputStream, true, null);
//
//		return file;
//	}
	public static void main(String[] args) throws Exception {
		
		String inout = "in";
		for (int k = 0; k<= 1; k++) {
			if (k == 1) {
				inout = "out";
			}
		
			String p = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/renamefield/nonprivate/";
//			String p = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/extractmethod/";
			File ff = new File(p);
			String[] list2 = ff.list();
			for (String name : list2) {
				if (!name.startsWith("test")) continue;
//				if (name.startsWith(".")) continue;
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
