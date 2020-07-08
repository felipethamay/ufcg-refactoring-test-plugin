package refactoringtestplugin.pushdownfield;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringAvailabilityTester;
import org.eclipse.jdt.internal.corext.refactoring.structure.PullUpRefactoringProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.PushDownRefactoringProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.PushDownRefactoringProcessor.MemberActionInfo;
import org.eclipse.jdt.internal.corext.refactoring.util.JavaElementUtil;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class TestPushDownField {
	
	public static String[] fields;
	public static boolean deleteAllInSourceType;
	public static boolean deleteAllMatchingMethods;
	public static String CLASS_NAME = "A";
	
	private static final String[] NO_ARGUMENTS= new String[0];
	
	


	private void helper(String[] selectedMethodNames, String[][] selectedMethodSignatures,
			String[] selectedFieldNames,
			String[] namesOfMethodsToPullUp, String[][] signaturesOfMethodsToPullUp,
			String[] namesOfFieldsToPullUp, String[] namesOfMethodsToDeclareAbstract,
			String[][] signaturesOfMethodsToDeclareAbstract, String[] additionalCuNames, String[] additionalPackNames) throws Exception{
		
		this.CLASS_NAME = "A";
		this.fields = selectedFieldNames;
		this.deleteAllInSourceType  = deleteAllInSourceType;
		this.deleteAllMatchingMethods = deleteAllMatchingMethods;
		
//		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
//
//		IPackageFragment[] additionalPacks= createAdditionalPackages(additionalCuNames, additionalPackNames);
//		ICompilationUnit[] additionalCus= createAdditionalCus(additionalCuNames, additionalPacks);
//
//		Refactoring ref= createRefactoringPrepareForInputCheck(selectedMethodNames, selectedMethodSignatures, selectedFieldNames, namesOfMethodsToPullUp, signaturesOfMethodsToPullUp,
//				namesOfFieldsToPullUp, namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract, cuA);
//
//		RefactoringStatus checkInputResult= ref.checkFinalConditions(new NullProgressMonitor());
//		assertTrue("precondition was supposed to pass but got " + checkInputResult.toString(), !checkInputResult.hasError());
//		performChange(ref, false);
//
//		String expected= getFileContents(getOutputTestFileName("A"));
//		String actual= cuA.getSource();
//		assertEqualLines("A.java", expected, actual);
//
//		for (int i= 0; i < additionalCus.length; i++) {
//			ICompilationUnit unit= additionalCus[i];
//			String expectedS= getFileContents(getOutputTestFileName(additionalCuNames[i]));
//			String actualS= unit.getSource();
//			assertEqualLines(unit.getElementName(), expectedS, actualS);
//		}

	}




	private static List<IMember> getMembersToPushDown(PushDownRefactoringProcessor processor) {
		MemberActionInfo[] infos= processor.getMemberActionInfos();
		List<IMember> result= new ArrayList<>(infos.length);
		for (int i= 0; i < infos.length; i++) {
			if (infos[i].isToBePushedDown())
				result.add(infos[i].getMember());
		}
		return result;
	}

	//--------------------------------------------------------

	
	public void test12() throws Exception{
		String[] selectedMethodNames= {};
		String[][] selectedMethodSignatures= {};
		String[] selectedFieldNames= {"f"};
		String[] namesOfMethodsToPushDown= {};
		String[][] signaturesOfMethodsToPushDown= {};
		String[] namesOfFieldsToPushDown= {"f"};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		helper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				namesOfMethodsToPushDown, signaturesOfMethodsToPushDown,
				namesOfFieldsToPushDown,
				namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract, null, null);
	}

	public void test13() throws Exception{
		String[] selectedMethodNames= {};
		String[][] selectedMethodSignatures= {};
		String[] selectedFieldNames= {"f"};
		String[] namesOfMethodsToPushDown= {};
		String[][] signaturesOfMethodsToPushDown= {};
		String[] namesOfFieldsToPushDown= {"f"};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		helper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				namesOfMethodsToPushDown, signaturesOfMethodsToPushDown,
				namesOfFieldsToPushDown,
				namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract, null, null);
	}

	
	public void test22() throws Exception{
		String[] selectedMethodNames= {};
		String[][] selectedMethodSignatures= {};
		String[] selectedFieldNames= {"bar"};
		String[] namesOfMethodsToPushDown= {};
		String[][] signaturesOfMethodsToPushDown= {};
		String[] namesOfFieldsToPushDown= selectedFieldNames;
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		helper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				namesOfMethodsToPushDown, signaturesOfMethodsToPushDown,
				namesOfFieldsToPushDown,
				namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract, null, null);
	}

	public void test23() throws Exception{
		String[] selectedMethodNames= {};
		String[][] selectedMethodSignatures= {};
		String[] selectedFieldNames= {"bar"};
		String[] namesOfMethodsToPushDown= {};
		String[][] signaturesOfMethodsToPushDown= {};
		String[] namesOfFieldsToPushDown= selectedFieldNames;
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		helper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				namesOfMethodsToPushDown, signaturesOfMethodsToPushDown,
				namesOfFieldsToPushDown,
				namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract, null, null);
	}

	public void test24() throws Exception{
		String[] selectedMethodNames= {};
		String[][] selectedMethodSignatures= {};
		String[] selectedFieldNames= {"foo", "bar"};
		String[] namesOfMethodsToPushDown= {};
		String[][] signaturesOfMethodsToPushDown= {};
		String[] namesOfFieldsToPushDown= selectedFieldNames;
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		helper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				namesOfMethodsToPushDown, signaturesOfMethodsToPushDown,
				namesOfFieldsToPushDown,
				namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract, null, null);
	}

	

	public void test28() throws Exception{
//		if (true){
//			printTestDisabledMessage("37175");
//			return;
//		}
		String[] selectedMethodNames= {};
		String[][] selectedMethodSignatures= {};
		String[] selectedFieldNames= {"i", "j"};
		String[] namesOfMethodsToPushDown= selectedMethodNames;
		String[][] signaturesOfMethodsToPushDown= selectedMethodSignatures;
		String[] namesOfFieldsToPushDown= {"i", "j"};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		helper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				namesOfMethodsToPushDown, signaturesOfMethodsToPushDown,
				namesOfFieldsToPushDown,
				namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract, null, null);
	}



	public void testGenerics12() throws Exception{
		String[] selectedMethodNames= {};
		String[][] selectedMethodSignatures= {};
		String[] selectedFieldNames= {"f"};
		String[] namesOfMethodsToPushDown= {};
		String[][] signaturesOfMethodsToPushDown= {};
		String[] namesOfFieldsToPushDown= {"f"};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		helper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				namesOfMethodsToPushDown, signaturesOfMethodsToPushDown,
				namesOfFieldsToPushDown,
				namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract, null, null);
	}

	public void testGenerics13() throws Exception{
		String[] selectedMethodNames= {};
		String[][] selectedMethodSignatures= {};
		String[] selectedFieldNames= {"f"};
		String[] namesOfMethodsToPushDown= {};
		String[][] signaturesOfMethodsToPushDown= {};
		String[] namesOfFieldsToPushDown= {"f"};
		String[] namesOfMethodsToDeclareAbstract= {};
		String[][] signaturesOfMethodsToDeclareAbstract= {};

		helper(selectedMethodNames, selectedMethodSignatures,
				selectedFieldNames,
				namesOfMethodsToPushDown, signaturesOfMethodsToPushDown,
				namesOfFieldsToPushDown,
				namesOfMethodsToDeclareAbstract, signaturesOfMethodsToDeclareAbstract, null, null);
	}

	

	
	public static void main(String[] args) throws Exception {
		
		String inout = "in";
		for (int k = 0; k<= 1; k++) {
			if (k == 1) {
				inout = "out";
			}
		
			String p = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/pushdownfield/";
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
