package refactoringtestplugin.addparameter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class TestAdd {
	
	
//	public static boolean updateRef = false;
	public static boolean createDelegate;
	public static String METHOD = "k";
	public static String CLASS_NAME = "A";
	
	public static String[] newTypes;
	public static String[] newNames;
	public static String[] newDefaultValues;
	public static String[] signature;
	public static int[] newIndices;
	
	protected void helperAdd(String[] signature, int[] newIndices) throws Exception {
		helperAdd(signature, newIndices, false);
	}

	private void helperAdd(String[] signature, int[] newIndices, boolean createDelegate) throws Exception {
		this.signature = signature;
		this.newIndices = newIndices;
		this.createDelegate = createDelegate;
		this.CLASS_NAME = "A";
		this.METHOD = "m";
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), true, true);
//		IType classA= getType(cu, "A");
//		IMethod method = classA.getMethod("m", signature);
//		assertTrue("method does not exist", method.exists());
//		assertTrue("refactoring not available", RefactoringAvailabilityTester.isChangeSignatureAvailable(method));
//
//		ChangeSignatureProcessor processor= new ChangeSignatureProcessor(method);
//		Refactoring ref= new ProcessorBasedRefactoring(processor);
//
//		processor.setDelegateUpdating(createDelegate);
//		addInfos(processor.getParameterInfos(), newParamInfos, newIndices);
//		RefactoringStatus initialConditions= ref.checkInitialConditions(new NullProgressMonitor());
//		assertTrue("precondition was supposed to pass:"+initialConditions.getEntryWithHighestSeverity(), initialConditions.isOK());
//		JavaRefactoringDescriptor descriptor= processor.createDescriptor();
//		RefactoringStatus result= performRefactoring(descriptor);
//		assertEquals("precondition was supposed to pass", null, result);
//
//		IPackageFragment pack= (IPackageFragment)cu.getParent();
//		String newCuName= getSimpleTestFileName(true, true);
//		ICompilationUnit newcu= pack.getCompilationUnit(newCuName);
//		assertTrue(newCuName + " does not exist", newcu.exists());
//		String expectedFileContents= getFileContents(getTestFileName(true, false));
//		assertEqualLines("invalid renaming", expectedFileContents, newcu.getSource());
//
//		assertParticipant(classA);
	}



//	/*
//	 * Rename method 'A.m(signature)' to 'A.newMethodName(signature)'
//	 */
//	protected void helperRenameMethod(String[] signature, String newMethodName, boolean createDelegate, boolean markAsDeprecated) throws Exception {
//		ICompilationUnit cu= createCUfromTestFile(getPackageP(), true, true);
//		IType classA= getType(cu, "A");
//		IMethod method = classA.getMethod("m", signature);
//		assertTrue("method m does not exist in A", method.exists());
//		assertTrue("refactoring not available", RefactoringAvailabilityTester.isChangeSignatureAvailable(method));
//
//		ChangeSignatureProcessor processor= new ChangeSignatureProcessor(method);
//		Refactoring ref= new ProcessorBasedRefactoring(processor);
//
//		processor.setNewMethodName(newMethodName);
//		processor.setDelegateUpdating(createDelegate);
//		processor.setDeprecateDelegates(markAsDeprecated);
//		ref.checkInitialConditions(new NullProgressMonitor());
//		JavaRefactoringDescriptor descriptor= processor.createDescriptor();
//		RefactoringStatus result= performRefactoring(descriptor);
//		assertEquals("precondition was supposed to pass", null, result);
//
//		IPackageFragment pack= (IPackageFragment)cu.getParent();
//		String newCuName= getSimpleTestFileName(true, true);
//		ICompilationUnit newcu= pack.getCompilationUnit(newCuName);
//		assertTrue(newCuName + " does not exist", newcu.exists());
//		String expectedFileContents= getFileContents(getTestFileName(true, false));
//		assertEqualLines("invalid change of method name", expectedFileContents, newcu.getSource());
//
//		assertParticipant(classA);
//	}

	

	public void testAdd28()throws Exception{
		String[] signature= {"I"};
		String[] newNames= {"x"};
		String[] newTypes= {"int"};
		String[] newDefaultValues= {"0"};
		createNewParamInfos(newTypes, newNames, newDefaultValues);
		int[] newIndices= {1};
		helperAdd(signature,  newIndices, true);
	}

	public void testAdd29()throws Exception{
		String[] signature= {"I"};
		String[] newNames= {"x"};
		String[] newTypes= {"int"};
		String[] newDefaultValues= {"0"};
		createNewParamInfos(newTypes, newNames, newDefaultValues);
		int[] newIndices= {0};
		helperAdd(signature, newIndices, true);
	}

	void createNewParamInfos(String[] newTypes, String[] newNames, String[] newDefaultValues) {
		this.newTypes = newTypes;
		this.newNames = newNames;
		this.newDefaultValues = newDefaultValues;
	}
	
	public void testAdd30()throws Exception{
		String[] signature= {"I"};
		String[] newNames= {"x"};
		String[] newTypes= {"int"};
		String[] newDefaultValues= {"0"};
		createNewParamInfos(newTypes, newNames, newDefaultValues);
		int[] newIndices= {1};
		helperAdd(signature, newIndices, true);
	}

	public void testAdd31()throws Exception{
		String[] signature= {"I"};
		String[] newNames= {"x"};
		String[] newTypes= {"int"};
		String[] newDefaultValues= {"0"};
		createNewParamInfos(newTypes, newNames, newDefaultValues);
		int[] newIndices= {1};
		helperAdd(signature, newIndices, true);
	}

	public void testAdd32()throws Exception{
		String[] signature= {"I"};
		String[] newNames= {"x"};
		String[] newTypes= {"int"};
		String[] newDefaultValues= {"0"};
		createNewParamInfos(newTypes, newNames, newDefaultValues);
		int[] newIndices= {0};
		helperAdd(signature, newIndices, true);
	}

	public void testAdd33()throws Exception{
		String[] signature= {};
		String[] newNames= {"x"};
		String[] newTypes= {"int"};
		String[] newDefaultValues= {"0"};
		createNewParamInfos(newTypes, newNames, newDefaultValues);
		int[] newIndices= {0};
		helperAdd(signature, newIndices, true);
	}


	public static void main(String[] args) throws Exception {
		
		String inout = "in";
		for (int k = 0; k<= 1; k++) {
			if (k == 1) {
				inout = "out";
			}
		
			String p = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/addparameter/";
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
