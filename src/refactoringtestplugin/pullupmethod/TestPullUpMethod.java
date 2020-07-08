package refactoringtestplugin.pullupmethod;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.manipulation.CodeTemplateContextType;
import org.eclipse.jdt.internal.core.manipulation.StubUtility;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringAvailabilityTester;
import org.eclipse.jdt.internal.corext.refactoring.structure.PullUpRefactoringProcessor;
import org.eclipse.jdt.internal.corext.refactoring.util.JavaElementUtil;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class TestPullUpMethod {
	
	public static String[] METHOD;
	public static boolean deleteAllInSourceType;
	public static boolean deleteAllMatchingMethods;
	public static String CLASS_NAME = "A";
	
	private static final String[] NO_ARGUMENTS= new String[0];
	
	protected static PullUpRefactoringProcessor createRefactoringProcessor(IMember[] methods) throws JavaModelException {
		IJavaProject project= null;
		if (methods != null && methods.length > 0)
			project= methods[0].getJavaProject();
		if (RefactoringAvailabilityTester.isPullUpAvailable(methods)) {
			PullUpRefactoringProcessor processor= new PullUpRefactoringProcessor(methods, JavaPreferencesSettings.getCodeGenerationSettings(project));
			new ProcessorBasedRefactoring(processor);
			return processor;
		}
		return null;
	}
	
	

	

	private IType[] getPossibleTargetClasses(PullUpRefactoringProcessor processor) throws JavaModelException {
		return processor.getCandidateTypes(new RefactoringStatus(), new NullProgressMonitor());
	}

	protected void setSuperclassAsTargetClass(PullUpRefactoringProcessor processor) throws JavaModelException {
		IType[] possibleClasses= getPossibleTargetClasses(processor);
		processor.setDestinationType(possibleClasses[possibleClasses.length - 1]);
	}

	private void setTargetClass(PullUpRefactoringProcessor processor, int targetClassIndex) throws JavaModelException {
		IType[] possibleClasses= getPossibleTargetClasses(processor);
		processor.setDestinationType(getPossibleTargetClasses(processor)[possibleClasses.length - 1 - targetClassIndex]);
	}

	
	
	

	private static IMethod[] getMethods(IMember[] members){
		List<IJavaElement> l= Arrays.asList(JavaElementUtil.getElementsOfType(members, IJavaElement.METHOD));
		return l.toArray(new IMethod[l.size()]);
	}

//	private Refactoring createRefactoringPrepareForInputCheck(String[] selectedMethodNames, String[][] selectedMethodSignatures,
//			String[] selectedFieldNames,
//			String[] selectedTypeNames, String[] namesOfMethodsToPullUp,
//			String[][] signaturesOfMethodsToPullUp,
//			String[] namesOfFieldsToPullUp, String[] namesOfTypesToPullUp,
//			String[] namesOfMethodsToDeclareAbstract, String[][] signaturesOfMethodsToDeclareAbstract,
//			boolean deleteAllPulledUpMethods, boolean deleteAllMatchingMethods, int targetClassIndex, ICompilationUnit cu) throws CoreException {
//		
//		this.CLASS_NAME = "B";
//		this.METHOD = namesOfMethodsToPullUp;
//		this.deleteAllInSourceType  = deleteAllInSourceType;
//		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
//		
//		IType type= getType(cu, "B");
//		IMethod[] selectedMethods= getMethods(type, selectedMethodNames, selectedMethodSignatures);
//		IField[] selectedFields= getFields(type, selectedFieldNames);
//		IType[] selectedTypes= getMemberTypes(type, selectedTypeNames);
//		IMember[] selectedMembers= merge(selectedFields, selectedMethods, selectedTypes);
//
//		PullUpRefactoringProcessor processor= createRefactoringProcessor(selectedMembers);
//		Refactoring ref= processor.getRefactoring();
//
//		assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//
//		setTargetClass(processor, targetClassIndex);
//
//		IMethod[] methodsToPullUp= findMethods(selectedMethods, namesOfMethodsToPullUp, signaturesOfMethodsToPullUp);
//		IField[] fieldsToPullUp= findFields(selectedFields, namesOfFieldsToPullUp);
//		IType[] typesToPullUp= findTypes(selectedTypes, namesOfTypesToPullUp);
//		IMember[] membersToPullUp= merge(methodsToPullUp, fieldsToPullUp, typesToPullUp);
//
//		IMethod[] methodsToDeclareAbstract= findMethods(selectedMethods, namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract);
//
//		processor.setMembersToMove(membersToPullUp);
//		processor.setAbstractMethods(methodsToDeclareAbstract);
//		if (deleteAllPulledUpMethods && methodsToPullUp.length != 0)
//			processor.setDeletedMethods(methodsToPullUp);
//		if (deleteAllMatchingMethods && methodsToPullUp.length != 0)
//			processor.setDeletedMethods(getMethods(processor.getMatchingElements(new NullProgressMonitor(), false)));
//		return ref;
//	}

	

	protected void declareAbstractHelper(String[] selectedMethodNames, String[][] selectedMethodSignatures,
			String[] selectedFieldNames,
			String[] selectedTypeNames, String[] namesOfMethodsToPullUp,
			String[][] signaturesOfMethodsToPullUp, String[] namesOfFieldsToPullUp,
			String[] namesOfMethodsToDeclareAbstract,
			String[][] signaturesOfMethodsToDeclareAbstract, String[] namesOfTypesToPullUp,
			boolean deleteAllPulledUpMethods, boolean deleteAllMatchingMethods, int targetClassIndex) throws Exception{
		
		this.CLASS_NAME = "B";
		this.METHOD = selectedMethodNames;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		Refactoring ref= createRefactoringPrepareForInputCheck(selectedMethodNames, selectedMethodSignatures, selectedFieldNames, selectedTypeNames, namesOfMethodsToPullUp,
//				signaturesOfMethodsToPullUp, namesOfFieldsToPullUp, namesOfTypesToPullUp, namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract, deleteAllPulledUpMethods,
//				deleteAllMatchingMethods, targetClassIndex, cu);
//
//		RefactoringStatus checkInputResult= ref.checkFinalConditions(new NullProgressMonitor());
//		assertTrue("precondition was supposed to pass", !checkInputResult.hasError());
//		performChange(ref, false);
//
//		String expected= getFileContents(getOutputTestFileName("A"));
//		String actual= cu.getSource();
//		assertEqualLines(expected, actual);
	}

	private void helper1(String[] methodNames, String[][] signatures, boolean deleteAllInSourceType, boolean deleteAllMatchingMethods, int targetClassIndex) throws Exception{
		
		this.CLASS_NAME = "B";
		this.METHOD = methodNames;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
		
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), "A");
//		IType type= getType(cu, "B");
//		IMethod[] methods= getMethods(type, methodNames, signatures);
//
//		PullUpRefactoringProcessor processor= createRefactoringProcessor(methods);
//		Refactoring ref= processor.getRefactoring();
//
//		assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//
//		setTargetClass(processor, targetClassIndex);
//
//		if (deleteAllInSourceType)
//			processor.setDeletedMethods(methods);
//		if (deleteAllMatchingMethods)
//			processor.setDeletedMethods(getMethods(processor.getMatchingElements(new NullProgressMonitor(), false)));
//
//		RefactoringStatus checkInputResult= ref.checkFinalConditions(new NullProgressMonitor());
//		assertTrue("precondition was supposed to pass", !checkInputResult.hasError());
//		performChange(ref, false);
//
//		String expected= getFileContents(getOutputTestFileName("A"));
//		String actual= cu.getSource();
//		assertEqualLines(expected, actual);
	}

	



	//------------------ tests -------------

	public void test0() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test1() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test2() throws Exception{
		helper1(new String[]{"mmm", "n"}, new String[][]{new String[0], new String[0]}, true, false, 0);
	}

	public void test3() throws Exception{
		helper1(new String[]{"mmm", "n"}, new String[][]{new String[0], new String[0]}, true, true, 0);
	}

	public void test4() throws Exception{
		String[] methods = {"m"};
		this.CLASS_NAME = "B";
		this.METHOD = methods;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
//		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");
//
//		String[] methodNames= new String[]{"m"};
//		String[][] signatures= new String[][]{new String[]{"QList;"}};
//
//		IType type= getType(cuB, "B");
//		IMethod[] methods= getMethods(type, methodNames, signatures);
//
//		PullUpRefactoringProcessor processor= createRefactoringProcessor(methods);
//		Refactoring ref= processor.getRefactoring();
//
//		assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//		setSuperclassAsTargetClass(processor);
//
//		processor.setDeletedMethods(getMethods(processor.getMatchingElements(new NullProgressMonitor(), false)));
//
//		RefactoringStatus result= performRefactoring(ref);
//		assertTrue("precondition was supposed to pass", result == null || !result.hasError());
//
//		assertEqualLines("A", cuA.getSource(), getFileContents(getOutputTestFileName("A")));
//		assertEqualLines("B", cuB.getSource(), getFileContents(getOutputTestFileName("B")));
	}

	public void test5() throws Exception{
		
		String[] methods = {"m"};
		this.CLASS_NAME = "B";
		this.METHOD = methods;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
//		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");
//
//		String[] methodNames= new String[]{"m"};
//		String[][] signatures= new String[][]{new String[0]};
//
//		IType type= getType(cuB, "B");
//		IMethod[] methods= getMethods(type, methodNames, signatures);
//
//		PullUpRefactoringProcessor processor= createRefactoringProcessor(methods);
//		Refactoring ref= processor.getRefactoring();
//
//		assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//		setSuperclassAsTargetClass(processor);
//
//		processor.setDeletedMethods(getMethods(processor.getMatchingElements(new NullProgressMonitor(), false)));
//
//		RefactoringStatus result= performRefactoring(ref);
//		assertTrue("precondition was supposed to pass", result == null || !result.hasError());
//
//		assertEqualLines("A", cuA.getSource(), getFileContents(getOutputTestFileName("A")));
//		assertEqualLines("B", cuB.getSource(), getFileContents(getOutputTestFileName("B")));
	}

	public void test6() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test7() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test8() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test9() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test10() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test11() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test12() throws Exception{
		//printTestDisabledMessage("bug#6779 searchDeclarationsOfReferencedTyped - missing exception  types");
	
		String[] methods = {"m"};
		this.CLASS_NAME = "B";
		this.METHOD = methods;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
//		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");
//
//		String[] methodNames= new String[]{"m"};
//		String[][] signatures= new String[][]{new String[0]};
//
//		IType type= getType(cuB, "B");
//		IMethod[] methods= getMethods(type, methodNames, signatures);
//
//		PullUpRefactoringProcessor processor= createRefactoringProcessor(methods);
//		Refactoring ref= processor.getRefactoring();
//
//		assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//		setSuperclassAsTargetClass(processor);
//
//		processor.setDeletedMethods(getMethods(processor.getMatchingElements(new NullProgressMonitor(), false)));
//
//		RefactoringStatus result= performRefactoring(ref);
//		assertTrue("precondition was supposed to pass", result == null || !result.hasError());
//
//		assertEqualLines("A", cuA.getSource(), getFileContents(getOutputTestFileName("A")));
//		assertEqualLines("B", cuB.getSource(), getFileContents(getOutputTestFileName("B")));
	}

	public void test13() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test14() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

//	public void test15() throws Exception{
//		printTestDisabledMessage("must fix - incorrect error");
////		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false);
//	}
//
//	public void test16() throws Exception{
//		printTestDisabledMessage("must fix - incorrect error");
////		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false);
//	}
//
//	public void test17() throws Exception{
//		printTestDisabledMessage("must fix - incorrect error with static method access");
////		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false);
//	}
//
//	public void test18() throws Exception{
//		printTestDisabledMessage("must fix - incorrect error with static field access");
////		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false);
//	}

	public void test19() throws Exception{
//		printTestDisabledMessage("bug 18438");
//		printTestDisabledMessage("bug 23324 ");
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test20() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 1);
	}

	public void test21() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 1);
	}

	public void test22() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test23() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test24() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test25() throws Exception{
//		printTestDisabledMessage("bug in ASTRewrite - extra dimensions 29553");
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void test26() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test27() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test28() throws Exception{
//		printTestDisabledMessage("unimplemented (increase method visibility if declare abstract in superclass)");
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test29() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[]{"[I"}};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test30() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[]{"[I"}};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test31() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[]{"[I"}};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test32() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test33() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= selectedMethodNames;
		String[][] signaturesOfMethodsToPullUp= selectedMethodSignatures;
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {new String[0]};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test34() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test35() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test36() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test37() throws Exception{
		String[] selectedMethodNames= {"m", "f"};
		String[][] selectedMethodSignatures= {new String[0], new String[0]};
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {"m"};
		String[][] signaturesOfMethodsToPullUp= {new String[0]};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= {"f"};
		String[][] signaturesOfMethodsToDeclareAbstract= {new String[0]};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test38() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {"A"};
		String[] namesOfMethodsToPullUp= {"m"};
		String[][] signaturesOfMethodsToPullUp= {new String[0]};
		String[] namesOfFieldsToPullUp= {"A"};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}

	public void test39() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {"A"};
		String[] selectedTypeNames= {"X", "Y"};
		String[] namesOfMethodsToPullUp= {"m"};
		String[][] signaturesOfMethodsToPullUp= {new String[0]};
		String[] namesOfFieldsToPullUp= {"A"};
		String[] namesOfTypesToPullUp= {"X", "Y"};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, true, 0);
	}

	public void test40() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] selectedTypeNames= {};
		String[] namesOfMethodsToPullUp= {"m"};
		String[][] signaturesOfMethodsToPullUp= {new String[0]};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfTypesToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, true, 0);
	}

	public void test41() throws Exception{
		String[] selectedMethodNames= {};
		String[][] selectedMethodSignatures= {};
		String[] selectedFieldNames= {"i"};
		String[] selectedTypeNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {"i"};
		String[] namesOfTypesToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, true, 0);
	}

	public void test42() throws Exception{
		String[] selectedMethodNames= {};
		String[][] selectedMethodSignatures= {};
		String[] selectedFieldNames= {"i", "j"};
		String[] selectedTypeNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {"i", "j"};
		String[] namesOfTypesToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, true, 0);
	}

	public void test43() throws Exception{
//		printTestDisabledMessage("bug 35562 Method pull up wrongly indents javadoc comment [refactoring]");

		String[] selectedMethodNames= {"f"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] selectedTypeNames= {};
		String[] namesOfMethodsToPullUp= selectedMethodNames;
		String[][] signaturesOfMethodsToPullUp= selectedMethodSignatures;
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfTypesToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, true, 0);
	}

	public void test44() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {"A"};
		String[] selectedTypeNames= {"X", "Y"};
		String[] namesOfMethodsToPullUp= {"m"};
		String[][] signaturesOfMethodsToPullUp= {new String[0]};
		String[] namesOfFieldsToPullUp= {"A"};
		String[] namesOfTypesToPullUp= {"X", "Y"};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, true, 0);
	}

	public void test45() throws Exception{
		String[] selectedMethodNames= {"m"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {"A"};
		String[] selectedTypeNames= {"X", "Y"};
		String[] namesOfMethodsToPullUp= {"m"};
		String[][] signaturesOfMethodsToPullUp= {new String[0]};
		String[] namesOfFieldsToPullUp= {"A"};
		String[] namesOfTypesToPullUp= {"X", "Y"};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, true, 0);
	}

	public void test46() throws Exception{
		// for bug 196635

		String[] selectedMethodNames= {"getConst"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {"CONST"};
		String[] selectedTypeNames= {};
		String[] namesOfMethodsToPullUp= {"getConst"};
		String[][] signaturesOfMethodsToPullUp= {new String[0]};
		String[] namesOfFieldsToPullUp= {"CONST"};
		String[] namesOfTypesToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, true, 0);
	}

	public void test47() throws Exception{
		// for bug 211491

		String[] selectedMethodNames= {"method"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] selectedTypeNames= {};
		String[] namesOfMethodsToPullUp= {"method"};
		String[][] signaturesOfMethodsToPullUp= {new String[0]};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfTypesToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, true, 0);
	}

	public void test48() throws Exception{
		// for bug 211491, but with a super class

		String[] selectedMethodNames= {"method"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] selectedTypeNames= {};
		String[] namesOfMethodsToPullUp= {"method"};
		String[][] signaturesOfMethodsToPullUp= {new String[0]};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfTypesToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, false, false, 0);
	}

	public void test49() throws Exception{
		// for bug 228950

		String[] selectedMethodNames= {"g"};
		String[][] selectedMethodSignatures= {new String[0]};
		String[] selectedFieldNames= {};
		String[] selectedTypeNames= {};
		String[] namesOfMethodsToPullUp= {"g"};
		String[][] signaturesOfMethodsToPullUp= {new String[0]};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfTypesToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				selectedTypeNames, namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, false, 0);
	}

	public void test50() throws Exception {
		// for bug 125326

		Template codeTemplate= StubUtility.getCodeTemplate(CodeTemplateContextType.OVERRIDECOMMENT_ID, null);
		try {
			StubUtility.setCodeTemplate(CodeTemplateContextType.OVERRIDECOMMENT_ID, "", null);
			String[] selectedMethodNames= { "m" };
			String[][] selectedMethodSignatures= { new String[0] };
			String[] selectedFieldNames= {};
			String[] selectedTypeNames= {};
			String[] namesOfMethodsToPullUp= { "m" };
			String[][] signaturesOfMethodsToPullUp= { new String[0] };
			String[] namesOfFieldsToPullUp= {};
			String[] namesOfTypesToPullUp= {};
			String[] namesOfMethodsToDeclareAbstract= {};
			String[][] signaturesOfMethodsToDeclareAbstract= {};

			declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
					selectedFieldNames,
					selectedTypeNames, namesOfMethodsToPullUp,
					signaturesOfMethodsToPullUp,
					namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
					signaturesOfMethodsToDeclareAbstract, namesOfTypesToPullUp, true, false, 0);
		} finally {
			StubUtility.setCodeTemplate(CodeTemplateContextType.OVERRIDECOMMENT_ID, codeTemplate.getPattern(), null);
		}

	}

	public void test51() throws Exception {
		String[] methods = {"b"};
		this.CLASS_NAME = "A";
		this.METHOD = methods;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
//		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");
//
//		String[] methodNames= new String[] { "b" };
//		String[][] signatures= new String[][] { new String[0] };
//
//		IType type= getType(cuA, "A");
//		IMethod[] methods= getMethods(type, methodNames, signatures);
//
//		PullUpRefactoringProcessor processor= createRefactoringProcessor(methods);
//		Refactoring ref= processor.getRefactoring();
//
//		assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//		setSuperclassAsTargetClass(processor);
//
//		RefactoringStatus result= performRefactoring(ref);
//		assertTrue("precondition was supposed to pass", result == null || !result.hasError());
//
//		assertEqualLines("B", cuB.getSource(), getFileContents(getOutputTestFileName("B")));
//		assertEqualLines("A", cuA.getSource(), getFileContents(getOutputTestFileName("A")));
	}

	public void test52() throws Exception {
		String[] selectedMethodNames= new String[] { "baz1", "baz2", "baz3", "baz4" };
		String[][] selectedMethodSignatures= new String[][] { new String[0], new String[0], new String[0], new String[0] };

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				new String[] {},
				new String[] {}, new String[] {},
				new String[][] {},
				new String[] {}, selectedMethodNames,
				selectedMethodSignatures, new String[] {}, false, false, 0);
	}

	// bug 396524
	public void test53() throws Exception {
		String[] selectedMethodNames= { "m" };
		String[][] selectedMethodSignatures= { new String[0] };
		String[] selectedFieldNames= {};
		String[] namesOfMethodsToPullUp= {};
		String[][] signaturesOfMethodsToPullUp= {};
		String[] namesOfFieldsToPullUp= {};
		String[] namesOfMethodsToDeclareAbstract= selectedMethodNames;
		String[][] signaturesOfMethodsToDeclareAbstract= selectedMethodSignatures;

		declareAbstractHelper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				new String[0], namesOfMethodsToPullUp,
				signaturesOfMethodsToPullUp,
				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract,
				signaturesOfMethodsToDeclareAbstract, new String[0], true, true, 0);
	}



	

	
	//------------------ tests -------------

	public void testStaticImports0() throws Exception{
		
		String[] methods = {"m"};
		this.CLASS_NAME = "B";
		this.METHOD = methods;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
//		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");
//
//			String[] methodNames= new String[]{"m"};
//			String[][] signatures= new String[][]{new String[] {"QS;"}};
//
//			IType type= getType(cuB, "B");
//			IMethod[] methods= getMethods(type, methodNames, signatures);
//
//			PullUpRefactoringProcessor processor= createRefactoringProcessor(methods);
//			Refactoring ref= processor.getRefactoring();
//
//			assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//			setSuperclassAsTargetClass(processor);
//
//			processor.setDeletedMethods(getMethods(processor.getMatchingElements(new NullProgressMonitor(), false)));
//
//			RefactoringStatus result= performRefactoring(ref);
//			assertTrue("precondition was supposed to pass", result == null || !result.hasError());
//
//			assertEqualLines("A", cuA.getSource(), getFileContents(getOutputTestFileName("A")));
//			assertEqualLines("B", cuB.getSource(), getFileContents(getOutputTestFileName("B")));
		}

	public void testStaticImports1() throws Exception{
		
		String[] methods = {"m"};
		this.CLASS_NAME = "B";
		this.METHOD = methods;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
//		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");

//		String[] methodNames= new String[]{"m"};
//		String[][] signatures= new String[][]{new String[] {"QS;"}};
//
//		IType type= getType(cuB, "B");
//		IMethod[] methods= getMethods(type, methodNames, signatures);
//
//		PullUpRefactoringProcessor processor= createRefactoringProcessor(methods);
//		Refactoring ref= processor.getRefactoring();
//
//		assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//		setSuperclassAsTargetClass(processor);
//
//		processor.setDeletedMethods(getMethods(processor.getMatchingElements(new NullProgressMonitor(), false)));
//
//		RefactoringStatus result= performRefactoring(ref);
//		assertTrue("precondition was supposed to pass", result == null || !result.hasError());
//
//		assertEqualLines("A", cuA.getSource(), getFileContents(getOutputTestFileName("A")));
//		assertEqualLines("B", cuB.getSource(), getFileContents(getOutputTestFileName("B")));
	}

	public void testGenerics0() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void testGenerics1() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void testGenerics2() throws Exception{
		helper1(new String[]{"mmm", "n"}, new String[][]{new String[] {"QT;"}, new String[0]}, true, false, 0);
	}

	public void testGenerics3() throws Exception{
		helper1(new String[]{"mmm", "n"}, new String[][]{new String[] {"QT;"}, new String[0]}, true, true, 0);
	}

	
	public void testGenerics5() throws Exception{
		
		String[] methods = {"m"};
		this.CLASS_NAME = "B";
		this.METHOD = methods;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
//		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");
//
//		String[] methodNames= new String[]{"m"};
//		String[][] signatures= new String[][]{new String[] {"QS;"}};
//
//		IType type= getType(cuB, "B");
//		IMethod[] methods= getMethods(type, methodNames, signatures);
//
//		PullUpRefactoringProcessor processor= createRefactoringProcessor(methods);
//		Refactoring ref= processor.getRefactoring();
//
//		assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//		setSuperclassAsTargetClass(processor);
//
//		processor.setDeletedMethods(getMethods(processor.getMatchingElements(new NullProgressMonitor(), false)));
//
//		RefactoringStatus result= performRefactoring(ref);
//		assertTrue("precondition was supposed to pass", result == null || !result.hasError());
//
//		assertEqualLines("A", cuA.getSource(), getFileContents(getOutputTestFileName("A")));
//		assertEqualLines("B", cuB.getSource(), getFileContents(getOutputTestFileName("B")));
	}

	public void testGenerics6() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void testGenerics7() throws Exception{
//		printTestDisabledMessage("Disabled because of bug ");

//		if (!BUG_91542)
			helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void testGenerics8() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void testGenerics9() throws Exception{
//		printTestDisabledMessage("Disabled because of bug ");

//		if (!BUG_91542)
			helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void testGenerics10() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void testGenerics11() throws Exception{
		helper1(new String[]{"m"}, new String[][]{new String[0]}, true, false, 0);
	}

	public void testGenerics12() throws Exception{
		
		String[] methods = {"m"};
		this.CLASS_NAME = "B";
		this.METHOD = methods;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
//		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");
//
//		String[] methodNames= new String[]{"m"};
//		String[][] signatures= new String[][]{new String[]{"QT;"}};
//
//		IType type= getType(cuB, "B");
//		IMethod[] methods= getMethods(type, methodNames, signatures);
//
//		PullUpRefactoringProcessor processor= createRefactoringProcessor(methods);
//		Refactoring ref= processor.getRefactoring();
//
//		assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//		setSuperclassAsTargetClass(processor);
//
//		processor.setDeletedMethods(getMethods(processor.getMatchingElements(new NullProgressMonitor(), false)));
//
//		RefactoringStatus result= performRefactoring(ref);
//		assertTrue("precondition was supposed to pass", result == null || !result.hasError());
//
//		assertEqualLines("A", cuA.getSource(), getFileContents(getOutputTestFileName("A")));
//		assertEqualLines("B", cuB.getSource(), getFileContents(getOutputTestFileName("B")));
	}

	public void testGenerics13() throws Exception {
		helper1(new String[] { "m"}, new String[][] { new String[0]}, true, false, 0);
	}

	public void testGenerics14() throws Exception {
		helper1(new String[] { "m"}, new String[][] { new String[0]}, true, false, 0);
	}

	public void testGenerics15() throws Exception {
		helper1(new String[] { "m"}, new String[][] { new String[0]}, true, false, 0);
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
