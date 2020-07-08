package refactoringtestplugin.renametype;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class TestRenameT {
	
	
//	public static boolean createDelegate;
	public static boolean fUpdateReferences= true;
	public static boolean fUpdateTextualMatches= true;
	public static boolean fUpdateSimilar= true;
	public static String TYPE_TO_RENAME = "m";
	public static String NEW_TYPE_NAME = "k";
	
	private static final String[] NO_ARGUMENTS= new String[0];
	



	private String[] helperWithTextual(String oldCuName, String oldName, String newName, String newCUName, boolean updateReferences, boolean updateTextualMatches) throws Exception{
		this.TYPE_TO_RENAME = oldName;
		this.NEW_TYPE_NAME = newName;
		this.fUpdateReferences = updateReferences;
		this.fUpdateTextualMatches = updateTextualMatches;
		
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), oldCuName);
//		IType classA= getType(cu, oldName);
//		IJavaElement[] classAMembers= classA.getChildren();
//
//		IPackageFragment pack= (IPackageFragment)cu.getParent();
//		String[] renameHandles= null;
//		if (classA.getDeclaringType() == null && cu.getElementName().startsWith(classA.getElementName())) {
//			renameHandles= ParticipantTesting.createHandles(classA, cu, cu.getResource());
//		} else {
//			renameHandles= ParticipantTesting.createHandles(classA);
//		}
//		RenameJavaElementDescriptor descriptor= createRefactoringDescriptor(classA, newName);
//		descriptor.setUpdateReferences(updateReferences);
//		descriptor.setUpdateTextualOccurrences(updateTextualMatches);
//		Refactoring refactoring= createRefactoring(descriptor);
//		assertEquals("was supposed to pass", null, performRefactoring(refactoring));
//		ICompilationUnit newcu= pack.getCompilationUnit(newCUName + ".java");
//		assertTrue("cu " + newcu.getElementName()+ " does not exist", newcu.exists());
//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName(newCUName)), newcu.getSource());
//
//		INameUpdating nameUpdating= refactoring.getAdapter(INameUpdating.class);
//		IType newElement = (IType) nameUpdating.getNewElement();
//		assertTrue("new element does not exist:\n" + newElement.toString(), newElement.exists());
//
//		checkMappers(refactoring, classA, newCUName + ".java", classAMembers);
//
//		return renameHandles;
		return null;
	}

	private String[] helper2_0(String oldName, String newName, String newCUName, boolean updateReferences) throws Exception{
		return helperWithTextual(oldName, oldName, newName, newCUName, updateReferences, false);
	}

	private void helper2(String oldName, String newName, boolean updateReferences) throws Exception{
		helper2_0(oldName, newName, newName, updateReferences);
	}

	private String[] helper2(String oldName, String newName) throws Exception{
		return helper2_0(oldName, newName, newName, true);
	}

	// <--------------------- Similarly named elements ---------------------------->

//	@Override
//	protected void setUp() throws Exception {
//		super.setUp();
//		setSomeFieldOptions(getPackageP().getJavaProject(), "f", "Suf1", false);
//		setSomeFieldOptions(getPackageP().getJavaProject(), "fs", "_suffix", true);
//		setSomeLocalOptions(getPackageP().getJavaProject(), "lv", "_lv");
//		setSomeArgumentOptions(getPackageP().getJavaProject(), "pm", "_pm");
//		fIsPreDeltaTest= true;
//	}

	private void helper3(String oldName, String newName, boolean updateRef, boolean updateTextual, boolean updateSimilar) throws JavaModelException, CoreException, IOException, Exception {
		helper3(oldName, newName, updateRef, updateTextual, updateSimilar, null);
	}

	private void helper3(String oldName, String newName, boolean updateRef, boolean updateTextual, boolean updateSimilar, String nonJavaFiles) throws JavaModelException, CoreException, IOException, Exception {
		this.TYPE_TO_RENAME = oldName;
		this.NEW_TYPE_NAME = newName;
		this.fUpdateReferences = updateRef;
		this.fUpdateTextualMatches = updateTextual;
		this.fUpdateSimilar = updateSimilar;
//		RefactoringDescriptor descriptor= initWithAllOptions(oldName, oldName, newName, updateRef, updateTextual, updateSimilar, nonJavaFiles, RenamingNameSuggestor.STRATEGY_EMBEDDED);
//		Refactoring ref= createRefactoring(descriptor);
//		RefactoringStatus status= performRefactoring(ref);
//		assertNull("was supposed to pass", status);
//		checkResultInClass(newName);
//		checkMappedSimilarElementsExist(ref);
	}

	private void helper3_inner(String oldName, String oldInnerName, String newName, String innerNewName, boolean updateRef, boolean updateTextual, boolean updateSimilar, String nonJavaFiles) throws JavaModelException, CoreException, IOException, Exception {
		this.TYPE_TO_RENAME = oldName;
		this.NEW_TYPE_NAME = newName;
		this.fUpdateReferences = updateRef;
		this.fUpdateTextualMatches = updateTextual;
		this.fUpdateSimilar = updateSimilar;
		
//		RefactoringDescriptor descriptor= initWithAllOptions(oldName, oldInnerName, innerNewName, updateRef, updateTextual, updateSimilar, nonJavaFiles, RenamingNameSuggestor.STRATEGY_EMBEDDED);
//		Refactoring ref= createRefactoring(descriptor);
//		assertNull("was supposed to pass", performRefactoring(ref));
//		checkResultInClass(newName);
//		checkMappedSimilarElementsExist(ref);
	}

//	private void checkMappedSimilarElementsExist(Refactoring ref) {
//		RenameTypeProcessor rtp= (RenameTypeProcessor) ((RenameRefactoring) ref).getProcessor();
//		IJavaElementMapper mapper= rtp.getAdapter(IJavaElementMapper.class);
//		IJavaElement[] similarElements= rtp.getSimilarElements();
//		if (similarElements == null)
//			return;
//		for (int i= 0; i < similarElements.length; i++) {
//			IJavaElement element= similarElements[i];
//			if (! (element instanceof ILocalVariable)) {
//				IJavaElement newElement= mapper.getRefactoredJavaElement(element);
//				assertTrue(newElement.exists());
//				assertFalse(element.exists());
//			}
//		}
//
//	}

	

//	private RefactoringDescriptor initWithAllOptions(String oldName, String innerOldName, String innerNewName, boolean updateReferences, boolean updateTextualMatches, boolean updateSimilar, String nonJavaFiles, int matchStrategy) throws Exception, JavaModelException, CoreException {
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), oldName);
//		IType classA= getType(cu, innerOldName);
//		RenameJavaElementDescriptor descriptor= createRefactoringDescriptor(classA, innerNewName);
//		setTheOptions(descriptor, updateReferences, updateTextualMatches, updateSimilar, nonJavaFiles, matchStrategy);
//		return descriptor;
//	}

//	private void setTheOptions(RenameJavaElementDescriptor descriptor, boolean updateReferences, boolean updateTextualMatches, boolean updateSimilar, String nonJavaFiles, int matchStrategy) {
//		descriptor.setUpdateReferences(updateReferences);
//		descriptor.setUpdateTextualOccurrences(updateTextualMatches);
//		if (nonJavaFiles!=null) {
//			descriptor.setUpdateQualifiedNames(true);
//			descriptor.setFileNamePatterns(nonJavaFiles);
//		}
//		descriptor.setUpdateSimilarDeclarations(updateSimilar);
//		descriptor.setMatchStrategy(matchStrategy);
//	}

//	private void checkResultInClass(String typeName) throws JavaModelException, IOException {
//		ICompilationUnit newcu= getPackageP().getCompilationUnit(typeName + ".java");
//		assertTrue("cu " + newcu.getElementName()+ " does not exist", newcu.exists());
//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName(typeName)), newcu.getSource());
//	}

//	private void setSomeFieldOptions(IJavaProject project, String prefixes, String suffixes, boolean forStatic) {
//		if (forStatic) {
//			project.setOption(JavaCore.CODEASSIST_STATIC_FIELD_PREFIXES, prefixes);
//			project.setOption(JavaCore.CODEASSIST_STATIC_FIELD_SUFFIXES, suffixes);
//		}
//		else {
//			project.setOption(JavaCore.CODEASSIST_FIELD_PREFIXES, prefixes);
//			project.setOption(JavaCore.CODEASSIST_FIELD_SUFFIXES, suffixes);
//		}
//	}

//	private void setSomeLocalOptions(IJavaProject project, String prefixes, String suffixes) {
//			project.setOption(JavaCore.CODEASSIST_LOCAL_PREFIXES, prefixes);
//			project.setOption(JavaCore.CODEASSIST_LOCAL_SUFFIXES, suffixes);
//	}
//
//	private void setSomeArgumentOptions(IJavaProject project, String prefixes, String suffixes) {
//		project.setOption(JavaCore.CODEASSIST_ARGUMENT_PREFIXES, prefixes);
//		project.setOption(JavaCore.CODEASSIST_ARGUMENT_SUFFIXES, suffixes);
//	}

	// </------------------------------------ Similarly named elements --------------------------------->



	

	public void test0() throws Exception {
//		ParticipantTesting.reset();
		String newName= "B";
		String[] renameHandles= helper2("A", newName);
//		ParticipantTesting.testRename(
//			renameHandles,
//			new RenameArguments[] {
//				new RenameArguments(newName, true),
//				new RenameArguments(newName + ".java", true),
//				new RenameArguments(newName + ".java", true)});
	}

	public void test1() throws Exception {
		helper2("A", "B");
	}

	public void test10() throws Exception {
		helper2("A", "B");
	}

	public void test12() throws Exception {
		helper2("A", "B");
	}

	public void test13() throws Exception {
		helper2("A", "B");
	}

	public void test14() throws Exception {
		helper2("A", "B");
	}

	public void test15() throws Exception {
		helper2("A", "B");
	}

	public void test16() throws Exception {
		helper2("A", "B");
	}

	public void test17() throws Exception {
		helper2("A", "B");
	}

	public void test18() throws Exception {
		helper2("A", "B");
	}

	public void test19() throws Exception {
		helper2("A", "B");
	}

	public void test2() throws Exception {
		helper2("A", "B");
	}

	public void test20() throws Exception {
		//printTestDisabledMessage("failb because of bug#9479");
		//if (true)
		//	return;
		
		this.TYPE_TO_RENAME = "A";
		this.NEW_TYPE_NAME = "B";
		
//		IPackageFragment packageA= getRoot().createPackageFragment("A", true, null);
//
//		ICompilationUnit cu= createCUfromTestFile(packageA, "A");
//		IType classA= getType(cu, "A");
//
//		assertEquals("was supposed to pass", null, performRefactoring(createRefactoringDescriptor(classA, "B")));
//
//		ICompilationUnit newcu= packageA.getCompilationUnit("B.java");
//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName("B")), newcu.getSource());
	}

	public void test21() throws Exception {
		helper2("A", "B");
	}

	public void test22() throws Exception {
		helper2("A", "B");
	}

	public void test23() throws Exception {
		helper2("A", "B");
	}

	public void test24() throws Exception {
		helper2("A", "B");
	}

	public void test25() throws Exception {
		helper2("A", "B");
	}

	public void test26() throws Exception {
		helper2("A", "B");
	}

	public void test27() throws Exception {
		helper2("A", "B");
	}

	public void test28() throws Exception {
		helper2("A", "B");
	}

	public void test29() throws Exception {
		
		this.TYPE_TO_RENAME = "A";
		this.NEW_TYPE_NAME = "B";
//		IPackageFragment packageP1= getRoot().createPackageFragment("p1", true, null);
//		createCUfromTestFile(packageP1, "C");
//
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType classA= getType(cu, "A");
//
//		assertEquals("was supposed to pass", null, performRefactoring(createRefactoringDescriptor(classA, "B")));
//
//		ICompilationUnit newcu= getPackageP().getCompilationUnit("B.java");
//		ICompilationUnit newcuC= packageP1.getCompilationUnit("C.java");
//		assertEqualLines("invalid renaming A", getFileContents(getOutputTestFileName("B")), newcu.getSource());
//		assertEqualLines("invalid renaming C", getFileContents(getOutputTestFileName("C")), newcuC.getSource());

	}

	public void test3() throws Exception {
		helper2("A", "B");
	}

	public void test30() throws Exception {
		this.TYPE_TO_RENAME = "A";
		this.NEW_TYPE_NAME = "B";
//		createCUfromTestFile(getPackageP(), "AA");
//
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType classA= getType(cu, "A");
//
//		assertEquals("was supposed to pass", null, performRefactoring(createRefactoringDescriptor(classA, "B")));
//
//		ICompilationUnit newcu= getPackageP().getCompilationUnit("B.java");
//		ICompilationUnit newcuAA= getPackageP().getCompilationUnit("AA.java");
//		assertEqualLines("invalid renaming A", getFileContents(getOutputTestFileName("B")), newcu.getSource());
//		assertEqualLines("invalid renaming AA", getFileContents(getOutputTestFileName("AA")), newcuAA.getSource());
	}
	public void test31() throws Exception {
		this.TYPE_TO_RENAME = "A";
		this.NEW_TYPE_NAME = "B";
//		createCUfromTestFile(getPackageP(), "AA");
//
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType classA= getType(cu, "A");
//
//		assertEquals("was supposed to pass", null, performRefactoring(createRefactoringDescriptor(classA, "B")));
//
//		ICompilationUnit newcu= getPackageP().getCompilationUnit("B.java");
//		ICompilationUnit newcuAA= getPackageP().getCompilationUnit("AA.java");
//		assertEqualLines("invalid renaming A", getFileContents(getOutputTestFileName("B")), newcu.getSource());
//		assertEqualLines("invalid renaming AA", getFileContents(getOutputTestFileName("AA")), newcuAA.getSource());
	}
	public void test32() throws Exception {
		helper2("A", "B");
	}

	public void test33() throws Exception {
		helper2("A", "B");
	}

	public void test34() throws Exception {
		helper2("A", "B");
	}

	public void test35() throws Exception {
		helper2("A", "B");
	}

	public void test36() throws Exception {
		helper2("A", "B");
	}

	public void test37() throws Exception {
		helper2("A", "B");
	}

	public void test38() throws Exception {
		helper2("A", "B");
	}

	public void test39() throws Exception {
		helper2("A", "B");
	}

	public void test4() throws Exception {
		helper2("A", "B");
	}

	public void test40() throws Exception {
		//printTestDisabledMessage("search engine bug");
		helper2("A", "B");
	}

	public void test41() throws Exception {
		helper2("A", "B");
	}

	public void test42() throws Exception {
		helper2("A", "B");
	}

	public void test43() throws Exception {
		helper2("A", "B");
	}

	public void test44() throws Exception {
		helper2("A", "B");
	}

	public void test45() throws Exception {
		helper2("A", "B");
	}

	public void test46() throws Exception {
		
		this.TYPE_TO_RENAME = "A";
		this.NEW_TYPE_NAME = "B";
//		IPackageFragment packageP1= getRoot().createPackageFragment("p1", true, null);
//		createCUfromTestFile(packageP1, "C");
//
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType classA= getType(cu, "A");
//
//		assertEquals("was supposed to pass", null, performRefactoring(createRefactoringDescriptor(classA, "B")));
//
//		ICompilationUnit newcu= getPackageP().getCompilationUnit("B.java");
//		ICompilationUnit newcuC= packageP1.getCompilationUnit("C.java");
//		assertEqualLines("invalid renaming A", getFileContents(getOutputTestFileName("B")), newcu.getSource());
//		assertEqualLines("invalid renaming C", getFileContents(getOutputTestFileName("C")), newcuC.getSource());
	}

	public void test47() throws Exception {
		helper2("A", "B");
	}

	public void test48() throws Exception {
		helper2("A", "B");
	}

	public void test49() throws Exception {
		helper2("A", "B");
	}

	public void test50() throws Exception {
//		printTestDisabledMessage("https://bugs.eclipse.org/bugs/show_bug.cgi?id=54948");
//		if (BUG_54948)
			helper2("A", "B");
	}

	public void test51() throws Exception {
		this.TYPE_TO_RENAME = "A";
		this.NEW_TYPE_NAME = "B";
//		IPackageFragment packageP1= getRoot().createPackageFragment("p1", true, null);
//		createCUfromTestFile(packageP1, "C");
//
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType classA= getType(cu, "A");
//
//		assertEquals("was supposed to pass", null, performRefactoring(createRefactoringDescriptor(classA, "B")));
//
//		ICompilationUnit newcu= getPackageP().getCompilationUnit("B.java");
//		ICompilationUnit newcuC= packageP1.getCompilationUnit("C.java");
//		assertEqualLines("invalid renaming A", getFileContents(getOutputTestFileName("B")), newcu.getSource());
//		assertEqualLines("invalid renaming C", getFileContents(getOutputTestFileName("C")), newcuC.getSource());
	}

	public void test52() throws Exception {
		//printTestDisabledMessage("1GJY2XN: ITPJUI:WIN2000 - rename type: error when with reference");
		helper2("A", "B");
	}

	public void test53() throws Exception {
		helper2("A", "B", false);
	}

	public void test54() throws Exception {
		//printTestDisabledMessage("waiting for: 1GKAQJS: ITPJCORE:WIN2000 - search: incorrect results for nested types");
		helperWithTextual("A", "X", "XYZ", "A", true, false);
	}

	public void test55() throws Exception {
		//printTestDisabledMessage("waiting for: 1GKAQJS: ITPJCORE:WIN2000 - search: incorrect results for nested types");
		helperWithTextual("A", "X", "XYZ", "A", false, false);
	}

	public void test57() throws Exception {
		helperWithTextual("A", "A", "B", "B", true, true);
	}

	public void test58() throws Exception {
		//printTestDisabledMessage("bug#16751");
		helper2("A", "B");
	}

	public void test59() throws Exception {
//		printTestDisabledMessage("bug#22938");
		helper2("A", "B");
	}

	public void test60() throws Exception {
//		printTestDisabledMessage("test for bug 24740");
		helperWithTextual("A", "A", "B", "B", true, true);
	}

	public void test61() throws Exception {
		
//		this.TYPE_TO_RENAME = "A";
//		this.NEW_TYPE_NAME = "B";
//		
//		ParticipantTesting.reset();
		String[] renameHandles= helperWithTextual("A" , "Inner", "InnerB", "A", true, false);
//		ParticipantTesting.testRename(renameHandles,
//			new RenameArguments[] {
//				new RenameArguments("InnerB", true),
//			});
	}

	public void test62() throws Exception {
//		printTestDisabledMessage("test for bug 66250");
		helperWithTextual("A", "A", "B", "B", false, true);
	}

	public void test63() throws Exception {
//		printTestDisabledMessage("test for bug 79131");
//		IPackageFragment anotherP= getRoot().createPackageFragment("another.p", true, null);
//		String folder= "anotherp/";
//		String type= "A";
//		ICompilationUnit cu= createCUfromTestFile(anotherP, type, folder);

		helperWithTextual("A", "A", "B", "B", true, true);

//		assertEqualLines("invalid renaming in another.p.A", getFileContents(getOutputTestFileName(type, folder)), cu.getSource());
	}

	public void test64() throws Exception {
//		printTestDisabledMessage("test for bug 79131");
//		IPackageFragment p2= getRoot().createPackageFragment("p2", true, null);
//		String folder= "p2/";
		String type= "A";
//		ICompilationUnit cu= createCUfromTestFile(p2, type, folder);

		helperWithTextual(type, type, "B", "B", true, true);

//		assertEqualLines("invalid renaming in p2.A", getFileContents(getOutputTestFileName(type, folder)), cu.getSource());
	}

	public void test65() throws Exception {
		// https://bugs.eclipse.org/356677
		
		this.TYPE_TO_RENAME = "A";
		this.NEW_TYPE_NAME = "NewC";
		
//		IPackageFragment packageP1= getRoot().createPackageFragment("p1", true, null);
//		IPackageFragment packageP2= getRoot().createPackageFragment("p2", true, null);
//		
//		IType classA= getClassFromTestFile(packageP1, "A");
//		IType classB= getClassFromTestFile(packageP1, "B");
//		IType classC= getClassFromTestFile(packageP2, "C");
//		
//		RefactoringStatus result= performRefactoring(createRefactoringDescriptor(classA, "C"));
//		assertEquals("was supposed to pass", null, result);
//
//		assertEqualLines("invalid renaming A", getFileContents(getOutputTestFileName("NewC")), packageP1.getCompilationUnit("C.java").getSource());
//		assertEqualLines("invalid renaming in B", getFileContents(getOutputTestFileName("B")), classB.getCompilationUnit().getSource());
//		assertEqualLines("invalid renaming in C", getFileContents(getOutputTestFileName("C")), classC.getCompilationUnit().getSource());
	}
	
	public void test66() throws Exception {
		//https://bugs.eclipse.org/bugs/show_bug.cgi?id=365380
		helperWithTextual("B", "A", "B", "B", true, true);
	}

	public void test5() throws Exception {
		helper2("A", "B");
	}

	public void test6() throws Exception {
		helper2("A", "B");
	}

	public void test7() throws Exception {
		helper2("A", "B");
	}

	public void test8() throws Exception {
		helper2("A", "B");
	}

	public void test9() throws Exception {
		helper2("A", "B");
	}

	public void testUnicode01() throws Exception {
		helper2("B", "C");
		//TODO: test undo!
	}

	public void testQualifiedName1() throws Exception {
		helperQualifiedName("A", "B", "build.xml", "*.xml");
	}

	public void testQualifiedName2() throws Exception {
		helperQualifiedName("Transient", "TransientEquipment", "mapping.hbm.xml", "*.xml");
	}

	private void helperQualifiedName(String oldName, String newName, String textFileName, String filePatterns) throws Exception {

		this.TYPE_TO_RENAME = oldName;
		this.NEW_TYPE_NAME = newName;
//		this.fUpdateReferences = updateRef;
//		this.fUpdateTextualMatches = updateTextual;
//		this.fUpdateSimilar = updateSimilar;
		
		//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), oldName);
//		IType classA= getType(cu, oldName);
//
//		String content= getFileContents(getTestPath() + getName() + TEST_INPUT_INFIX + textFileName);
//		IProject project= classA.getJavaProject().getProject();
//		IFile file= project.getFile(textFileName);
//		file.create(new ByteArrayInputStream(content.getBytes()), true, null);
//
//		RenameJavaElementDescriptor descriptor= createRefactoringDescriptor(classA, newName);
//		descriptor.setUpdateQualifiedNames(true);
//		descriptor.setFileNamePatterns(filePatterns);
//
//		assertEquals("was supposed to pass", null, performRefactoring(descriptor));
//
//		ICompilationUnit newcu= getPackageP().getCompilationUnit(newName + ".java");
//		assertEqualLines("invalid renaming", getFileContents(getOutputTestFileName(newName)), newcu.getSource());
//		InputStreamReader reader= new InputStreamReader(file.getContents(true));
//		StringBuffer newContent= new StringBuffer();
//		try {
//			int ch;
//			while((ch= reader.read()) != -1)
//				newContent.append((char)ch);
//		} finally {
//			reader.close();
//		}
//		String definedContent= getFileContents(getTestPath() + getName() + TEST_OUTPUT_INFIX + textFileName);
//		assertEqualLines("invalid updating", definedContent, newContent.toString());
	}

	public void testGenerics1() throws Exception {
		helper2("A", "B");
	}

	public void testGenerics2() throws Exception {
		helper2("A", "B");
	}

	public void testGenerics3() throws Exception {
		helper2("A", "B");
	}

	public void testGenerics4() throws Exception {
		helper2("A", "B");
	}

	public void testEnum1() throws Exception {
//		IPackageFragment p2= getRoot().createPackageFragment("p2", true, null);
//		String folder= "p2/";
//		String type= "A";
//		ICompilationUnit cu= createCUfromTestFile(p2, type, folder);

		helper2("A", "B");

//		assertEqualLines("invalid renaming in p2.A", getFileContents(getOutputTestFileName(type, folder)), cu.getSource());
	}

	public void testEnum2() throws Exception {
		helper2("A", "B");
	}

	public void testEnum3() throws Exception {
//		ICompilationUnit enumbered= createCUfromTestFile(getPackageP(), "Enumbered");
		helper2("A", "B");
//		assertEqualLines("invalid renaming in Enumbered", getFileContents(getOutputTestFileName("Enumbered")), enumbered.getSource());
	}

	public void testEnum4() throws Exception {
		helperWithTextual("A" , "A", "B", "A", true, false);
	}

	public void testEnum5() throws Exception {
		helper2("A", "B");
	}

	public void testAnnotation1() throws Exception {
		helper2("A", "B");
	}

	public void testAnnotation2() throws Exception {
		helper2("A", "B");
	}

	public void testAnnotation3() throws Exception {
		helperWithTextual("A" , "A", "B", "A", true, true);
	}

	// --------------- Similarly named elements -----------------

	public void testSimilarElements00() throws Exception {
		// Very basic test, one field, two methods
		helper3("SomeClass", "SomeClass2", true, false, true);
	}

	

	public void testSimilarElements03() throws Exception {
		// more methods
		helper3("SomeClass", "SomeClass2", true, false, true);
	}
	public void testSimilarElements04() throws Exception {
		//Additional field with exactly the same name and getters and setters in another class
//		getClassFromTestFile(getPackageP(), "SomeOtherClass");
		helper3("SomeClass", "SomeClass2", true, false, true);
//		checkResultInClass("SomeOtherClass");
	}

	public void testSimilarElements05() throws Exception {
		//qualified name updating
		//includes textual updating
//		String content= getFileContents(getTestPath() + "testSimilarElements05/in/test.html");
//		IProject project= getPackageP().getJavaProject().getProject();
//		IFile file= project.getFile("test.html");
//		file.create(new ByteArrayInputStream(content.getBytes()), true, null);

		helper3("SomeClass", "SomeDifferentClass", true, true, true, "test.html");

//		InputStreamReader reader= new InputStreamReader(file.getContents(true));
//		StringBuffer newContent= new StringBuffer();
//		try {
//			int ch;
//			while((ch= reader.read()) != -1)
//				newContent.append((char)ch);
//		} finally {
//			reader.close();
//		}
//		String definedContent= getFileContents(getTestPath() + "testSimilarElements05/out/test.html");
//		assertEqualLines("invalid updating test.html", newContent.toString(), definedContent);

	}

	public void testSimilarElements06() throws Exception {
		//Additional field with exactly the same name and getters and setters in another class
		//includes textual updating
		// printTestDisabledMessage("potential matches in comments issue (bug 111891)");
//		getClassFromTestFile(getPackageP(), "SomeNearlyIdenticalClass");
		helper3("SomeClass", "SomeOtherClass", true, true, true);
//		checkResultInClass("SomeNearlyIdenticalClass");
	}

	public void testSimilarElements07() throws Exception {
		//Test 4 fields in one file, different suffixes/prefixes, incl. 2x setters/getters
		//includes textual updating
		helper3("SomeClass", "SomeDiffClass", true, true, true);
	}

	public void testSimilarElements08() throws Exception {
		//Interface renaming fun, this time without textual
		helper3("ISomeIf", "ISomeIf2", true, false, true);
	}

	public void testSimilarElements09() throws Exception {
		//Some inner types
		//includes textual updating
//		getClassFromTestFile(getPackageP(), "SomeOtherClass");
		helper3_inner("SomeClass", "SomeInnerClass", "SomeClass", "SomeNewInnerClass", true, true, true, null);
//		checkResultInClass("SomeOtherClass");
	}

	public void testSimilarElements10() throws Exception {
		//Two static fields
//		getClassFromTestFile(getPackageP(), "SomeOtherClass");
		helper3("SomeClass", "SomeClass2", true, false, true, null);
//		checkResultInClass("SomeOtherClass");
	}

	public void testSimilarElements11() throws Exception {
		//Assure participants get notified of normal stuff (type rename
		//and resource changes) AND similarly named elements.
		
		this.TYPE_TO_RENAME = "SomeClass";
		this.NEW_TYPE_NAME = "SomeOtherClass";
		this.fUpdateSimilar = true;
		this.fUpdateReferences = true;
		this.fUpdateTextualMatches = false;
		
//		ParticipantTesting.reset();
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "SomeClass");
//		IType someClass= getType(cu, "SomeClass");
//		IType other= getClassFromTestFile(getPackageP(), "SomeOtherClass");
//
//		List<IAdaptable> handleList= new ArrayList<>();
//		List<RenameArguments> argumentList= new ArrayList<>();
//
//		List<String> similarOldHandleList= new ArrayList<>();
//		List<String> similarNewNameList= new ArrayList<>();
//		List<String> similarNewHandleList= new ArrayList<>();
//
//		final String newName= "SomeNewClass";
//
//		// f-Field + getters/setters
//		IField f3= other.getField("fSomeClass");
//		similarOldHandleList.add(f3.getHandleIdentifier());
//		similarNewHandleList.add("Lp/SomeOtherClass;.fSomeNewClass");
//		similarNewNameList.add("fSomeNewClass");
//
//		IMethod m3= other.getMethod("getSomeClass", new String[0]);
//		similarOldHandleList.add(m3.getHandleIdentifier());
//		similarNewNameList.add("getSomeNewClass");
//		similarNewHandleList.add("Lp/SomeOtherClass;.getSomeNewClass()V");
//		IMethod m4= other.getMethod("setSomeClass", new String[] {"QSomeClass;"});
//		similarOldHandleList.add(m4.getHandleIdentifier());
//		similarNewNameList.add("setSomeNewClass");
//		similarNewHandleList.add("Lp/SomeOtherClass;.setSomeNewClass(QSomeNewClass;)V");
//
//		// non-f-field + getter/setters
//		IField f1= someClass.getField("someClass");
//		similarOldHandleList.add(f1.getHandleIdentifier());
//		similarNewNameList.add("someNewClass");
//		similarNewHandleList.add("Lp/SomeNewClass;.someNewClass");
//		IMethod m1= someClass.getMethod("getSomeClass", new String[0]);
//		similarOldHandleList.add(m1.getHandleIdentifier());
//		similarNewNameList.add("getSomeNewClass");
//		similarNewHandleList.add("Lp/SomeNewClass;.getSomeNewClass()V");
//		IMethod m2= someClass.getMethod("setSomeClass", new String[] {"QSomeClass;"});
//		similarOldHandleList.add(m2.getHandleIdentifier());
//		similarNewNameList.add("setSomeNewClass");
//		similarNewHandleList.add("Lp/SomeNewClass;.setSomeNewClass(QSomeNewClass;)V");
//
//		// fs-field
//		IField f2= someClass.getField("fsSomeClass");
//		similarOldHandleList.add(f2.getHandleIdentifier());
//		similarNewNameList.add("fsSomeNewClass");
//		similarNewHandleList.add("Lp/SomeNewClass;.fsSomeNewClass");
//
//		// Type Stuff
//		handleList.add(someClass);
//		argumentList.add(new RenameArguments(newName, true));
//		handleList.add(cu);
//		argumentList.add(new RenameArguments(newName + ".java", true));
//		handleList.add(cu.getResource());
//		argumentList.add(new RenameArguments(newName + ".java", true));
//
//		String[] handles= ParticipantTesting.createHandles(handleList.toArray());
//		RenameArguments[] arguments= argumentList.toArray(new RenameArguments[0]);
//
//		RenameJavaElementDescriptor descriptor= createRefactoringDescriptor(someClass, newName);
//		setTheOptions(descriptor, true, false, true, null, RenamingNameSuggestor.STRATEGY_EMBEDDED);
//		RefactoringStatus status= performRefactoring(descriptor);
//		assertNull("was supposed to pass", status);
//
//		checkResultInClass(newName);
//		checkResultInClass("SomeOtherClass");
//
//		ParticipantTesting.testRename(handles, arguments);
//		ParticipantTesting.testSimilarElements(similarOldHandleList, similarNewNameList, similarNewHandleList);
	}

	public void testSimilarElements12() throws Exception {
		// Test updating of references
		helper3("SomeFieldClass", "SomeOtherFieldClass", true, false, true);
	}

	public void testSimilarElements13() throws Exception {
		// Test various locals and parameters with and without prefixes.
		// tests not renaming parameters with local prefixes and locals with parameter prefixes
		helper3("SomeClass", "SomeOtherClass", true, false, true);
	}

	public void testSimilarElements14() throws Exception {
		// Test for loop variables
		helper3("SomeClass2", "SomeOtherClass2", true, false, true);
	}

	public void testSimilarElements15() throws Exception {
		// Test catch block variables (exceptions)
		helper3("SomeClass3", "SomeOtherClass3", true, false, true);
	}

	public void testSimilarElements16() throws Exception {
		// Test updating of references
		helper3("SomeClass4", "SomeOtherClass4", true, false,  true);
	}



	public void testSimilarElements18() throws Exception {
		// factory method
		helper3("SomeClass", "SomeOtherClass", true, false, true);
	}

	

	public void testSimilarElements20() throws Exception {
		// Overridden method, check both are renamed
//		getClassFromTestFile(getPackageP(), "OtherClass");
		helper3("OverriddenMethodClass", "ThirdClass", true, false, true);
//		checkResultInClass("OtherClass");
	}

	public void testSimilarElements21() throws Exception {
		// Constructors may not be renamed
//		getClassFromTestFile(getPackageP(), "SomeClassSecond");
		helper3("SomeClass", "SomeNewClass", true, false, true);
//		checkResultInClass("SomeClassSecond");
	}

	public void testSimilarElements22() throws Exception {
		// Test transplanter for fields in types inside of initializers

		this.TYPE_TO_RENAME = "SomeClass";
		this.NEW_TYPE_NAME = "SomeNewClass";
		this.fUpdateSimilar = true;
		this.fUpdateReferences = true;
		this.fUpdateTextualMatches = false;
		
//		ParticipantTesting.reset();
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "SomeClass");
//		IType someClass= getType(cu, "SomeClass");
//
//		List<IAdaptable> handleList= new ArrayList<>();
//		List<RenameArguments> argumentList= new ArrayList<>();
//
//		List<String> similarOldHandleList= new ArrayList<>();
//		List<String> similarNewNameList= new ArrayList<>();
//		List<String> similarNewHandleList= new ArrayList<>();
//
//		final String newName= "SomeNewClass";
//
//		// field in class in initializer
//		IField inInitializer= someClass.getInitializer(1).getType("InInitializer", 1).getField("someClassInInitializer");
//		similarOldHandleList.add(inInitializer.getHandleIdentifier());
//		similarNewNameList.add("someNewClassInInitializer");
//		similarNewHandleList.add("Lp/SomeNewClass$InInitializer;.someNewClassInInitializer");
//
//		// Type Stuff
//		handleList.add(someClass);
//		argumentList.add(new RenameArguments(newName, true));
//		handleList.add(cu);
//		argumentList.add(new RenameArguments(newName + ".java", true));
//		handleList.add(cu.getResource());
//		argumentList.add(new RenameArguments(newName + ".java", true));
//
//		String[] handles= ParticipantTesting.createHandles(handleList.toArray());
//		RenameArguments[] arguments= argumentList.toArray(new RenameArguments[0]);
//
//		RenameJavaElementDescriptor descriptor= createRefactoringDescriptor(someClass, newName);
//		setTheOptions(descriptor, true, false, true, null, RenamingNameSuggestor.STRATEGY_EMBEDDED);
//		RefactoringStatus status= performRefactoring(descriptor);
//		assertNull("was supposed to pass", status);
//
//		checkResultInClass(newName);
//
//		ParticipantTesting.testRename(handles, arguments);
//		ParticipantTesting.testSimilarElements(similarOldHandleList, similarNewNameList, similarNewHandleList);

	}

	public void testSimilarElements23() throws Exception {
		// Test transplanter for elements inside types inside fields

		this.TYPE_TO_RENAME = "SomeClass";
		this.NEW_TYPE_NAME = "SomeNewClass";
		this.fUpdateSimilar = true;
		this.fUpdateReferences = true;
		this.fUpdateTextualMatches = false;
		
//		ParticipantTesting.reset();
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "SomeClass");
//		IType someClass= getType(cu, "SomeClass");
//
//		List<IAdaptable> handleList= new ArrayList<>();
//		List<RenameArguments> argumentList= new ArrayList<>();
//
//		List<String> similarOldHandleList= new ArrayList<>();
//		List<String> similarNewNameList= new ArrayList<>();
//		List<String> similarNewHandleList= new ArrayList<>();
//
//		final String newName= "SomeNewClass";
//
//		// some field
//		IField anotherSomeClass= someClass.getField("anotherSomeClass");
//		similarOldHandleList.add(anotherSomeClass.getHandleIdentifier());
//		similarNewNameList.add("anotherSomeNewClass");
//		similarNewHandleList.add("Lp/SomeNewClass;.anotherSomeNewClass");
//
//		// field in class in method in field declaration ;)
//		IField inInner= anotherSomeClass.getType("", 1).getMethod("foo", new String[0]).getType("X", 1).getField("someClassInInner");
//		similarOldHandleList.add(inInner.getHandleIdentifier());
//		similarNewNameList.add("someNewClassInInner");
//		similarNewHandleList.add("Lp/SomeNewClass$1$X;.someNewClassInInner");
//
//		// Type Stuff
//		handleList.add(someClass);
//		argumentList.add(new RenameArguments(newName, true));
//		handleList.add(cu);
//		argumentList.add(new RenameArguments(newName + ".java", true));
//		handleList.add(cu.getResource());
//		argumentList.add(new RenameArguments(newName + ".java", true));
//
//		String[] handles= ParticipantTesting.createHandles(handleList.toArray());
//		RenameArguments[] arguments= argumentList.toArray(new RenameArguments[0]);
//
//		RenameJavaElementDescriptor descriptor= createRefactoringDescriptor(someClass, newName);
//		setTheOptions(descriptor, true, false, true, null, RenamingNameSuggestor.STRATEGY_EMBEDDED);
//		RefactoringStatus status= performRefactoring(descriptor);
//		assertNull("was supposed to pass", status);
//
//		checkResultInClass(newName);
//
//		ParticipantTesting.testRename(handles, arguments);
//		ParticipantTesting.testSimilarElements(similarOldHandleList, similarNewNameList, similarNewHandleList);
	}

	public void testSimilarElements24() throws Exception {
		// Test transplanter for ICompilationUnit and IFile

		this.TYPE_TO_RENAME = "SomeClass";
		this.NEW_TYPE_NAME = "SomeNewClass";
		this.fUpdateSimilar = true;
		this.fUpdateReferences = true;
		this.fUpdateTextualMatches = false;
		
//		ParticipantTesting.reset();
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "SomeClass");
//		IType someClass= getType(cu, "SomeClass");
//		IJavaElement[] someClassMembers= someClass.getChildren();
//
//		RenameJavaElementDescriptor descriptor= createRefactoringDescriptor(someClass, "SomeNewClass");
//		setTheOptions(descriptor, true, false, true, null, RenamingNameSuggestor.STRATEGY_EMBEDDED);
//		Refactoring ref= createRefactoring(descriptor);
//		RefactoringStatus status= performRefactoring(ref);
//		assertNull("was supposed to pass", status);
//
//		checkResultInClass("SomeNewClass");
//
//		checkMappers(ref, someClass, "SomeNewClass.java", someClassMembers);
	}

//	private void checkMappers(Refactoring refactoring, IType type, String newCUName, IJavaElement[] someClassMembers) {
//		RenameTypeProcessor rtp= (RenameTypeProcessor)((RenameRefactoring) refactoring).getProcessor();
//
//		ICompilationUnit newUnit= (ICompilationUnit)rtp.getRefactoredJavaElement(type.getCompilationUnit());
//		assertTrue(newUnit.exists());
//		assertTrue(newUnit.getElementName().equals(newCUName));
//
//		IFile newFile= (IFile)rtp.getRefactoredResource(type.getResource());
//		assertTrue(newFile.exists());
//		assertTrue(newFile.getName().equals(newCUName));
//
//		if ((type.getParent().getElementType() == IJavaElement.COMPILATION_UNIT)
//				&& type.getCompilationUnit().getElementName().equals(type.getElementName() + ".java")) {
//			assertFalse(type.getCompilationUnit().exists());
//			assertFalse(type.getResource().exists());
//		}
//
//		IPackageFragment oldPackage= (IPackageFragment)type.getCompilationUnit().getParent();
//		IPackageFragment newPackage= (IPackageFragment)rtp.getRefactoredJavaElement(oldPackage);
//		assertEquals(oldPackage, newPackage);
//
//		for (int i= 0; i < someClassMembers.length; i++) {
//			IMember member= (IMember) someClassMembers[i];
//			IJavaElement refactoredMember= rtp.getRefactoredJavaElement(member);
//			if (member instanceof IMethod && member.getElementName().equals(type.getElementName()))
//				continue; // constructor
//			assertTrue(refactoredMember.exists());
//			assertEquals(member.getElementName(), refactoredMember.getElementName());
//			assertFalse(refactoredMember.equals(member));
//		}
//	}

	public void testSimilarElements25() throws Exception {
		// Test renaming of several-in-one field declarations
		helper3("ScrewUp", "ScrewDown", true, false, true);
	}

	public void testSimilarElements26() throws Exception {
		// Test renaming of several-in-one local variable declarations
		helper3("ScrewUp", "ScrewDown", true, false, true);
	}

	public void testSimilarElements27() throws Exception {
		// Test methods are not renamed if the match is
		// not either a parameter or a return type
		helper3("ScrewUp", "ScrewDown", true, false, true);
	}

	public void testSimilarElements28() throws Exception {
		// Test local variables are not renamed if the match is
		// not the type of the local variable itself
		helper3("ScrewUp", "ScrewDown", true, false, true);
	}

	public void testSimilarElements29() throws Exception {
		// Test fields are not renamed if the match is
		// not the type of the field itself
		helper3("ScrewUp", "ScrewDown", true, false, true);
	}

	public void testSimilarElements30() throws Exception {
		// Test local variables in initializers
		helper3("SomeClass", "SomeNewClass", true, false, true);
	}

	public void testSimilarElements31() throws Exception {
		// Test references and textual references to local elements
		helper3("SomeClass", "SomeDiffClass", true, true, true);
	}

	

	public void testSimilarElements33() throws Exception {
		// Test two local variables inside anonymous types do not generate warnings
		helper3("Why", "WhyNot", true, false, true);
	}

	public void testSimilarElements34() throws Exception {
		// Test references in annotations and type parameters
		helper3("Try", "Bla", true, false, true);
	}
	public static void main(String[] args) throws Exception {
		
		String inout = "in";
		for (int k = 0; k<= 1; k++) {
			if (k == 1) {
				inout = "out";
			}
		
			String p = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/renametype/";
			File ff = new File(p);
			String[] list2 = ff.list();
			for (String name : list2) {
				if (!name.startsWith("test")) continue;
				String path = p+name+"/"+inout+"/";
				File f = new File(path);
				String[] list = f.list();
				System.out.println(path);
				
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
