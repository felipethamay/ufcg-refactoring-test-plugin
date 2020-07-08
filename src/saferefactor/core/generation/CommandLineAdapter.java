package saferefactor.core.generation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;

import randoop.main.Main;
import saferefactor.core.util.Constants;
import saferefactor.core.util.FileUtil;
import saferefactor.core.util.Project;
import saferefactor.core.util.ast.ConstructorImp;
import saferefactor.core.util.ast.Method;
import saferefactor.core.util.ast.MethodImp;

public class CommandLineAdapter extends AbstractTestGeneratorAdapter{

	public static String methodsToTest = "methodToTest.txt";
	private final String tmpDir;
	protected String impactedList = "";
	private List<String> additionalParameters;
	private double timeLimit;
	
	public CommandLineAdapter(Project projectToTest, String tmpDir) {
		super(projectToTest);
		this.tmpDir = tmpDir;
	}

	@Override
	public void generateTestsForMethodList(List<Method> methods,
			double timeLimit, List<String> additionalParameters,
			String impactedList) throws FileNotFoundException {
		this.timeLimit = timeLimit;
		this.additionalParameters = additionalParameters;
		this.impactedList = impactedList;
		generateMethodListFile(methods);
		
		runRandoopThroughCommandLine();
		
	}

	private void runRandoopThroughCommandLine() {
		
		String params = "";
		for (String param : additionalParameters) {
			if (param.startsWith("fork")) {
				String[] forkParameter = param.split("=");
//				isFork = forkParameter[1];
			} else {
				params += param;
				params+=";";
			}
		}
		
		for (String parameter : additionalParameters) {
			

		}
		
		if (params.equals("")) {
			params = ".";
		}
		if (impactedList.equals("")) {
			impactedList = ".";
		} else {
			String[] split = impactedList.split(" ");
			impactedList = "";
			for (String string : split) {
				impactedList += string+"*";
			}
			impactedList = impactedList.substring(0, impactedList.length()-1);
		}
		
		String command = "java -classpath ."+Constants.SEPARATOR+"lib"+Constants.SEPARATOR+"*:"+
		project.getBuildFolder().getAbsolutePath()+":."+Constants.SEPARATOR+"bin";
		if (project.getLibFolder() != null) {
			command+=":"+project.getLibFolder()+Constants.SEPARATOR+"* ";
		} else {
			command+=" ";
		}
		command+="saferefactor.core.generation.CommandLineAdapter "+this.timeLimit+" "+
		this.tmpDir+" "+params+" "+this.impactedList;
		try {
			Process exec = Runtime.getRuntime().exec(command);
			BufferedReader input = new BufferedReader(
            new InputStreamReader(exec.getInputStream()));
        String line;
		while ((line = input.readLine()) != null) {
          System.out.println(line);
        }
        input.close();
			exec.waitFor();
			exec.getInputStream().close();
			exec.getErrorStream().close();
			exec.getOutputStream().close(); 
			exec.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//java -classpath /Users/melmongiovi/git3/saferefactorimpact/lib/*:/Users/melmongiovi/git3/saferefactorimpact/test/subjects/bugModifiedFieldS/bin/:/Users/melmongiovi/git3/saferefactorimpact/bin/ saferefactor.core.generation.RandoopAntAdapter 1 /var/folders/x3/bvxnl_gd10l1v_1pvgpgf4gw0000gn/T/SafeRefactor11/tests/ рс рA.teste() A.m(long) A.m(int)с 
		
	}

	
	@Override
	public List<File> getTestFiles() {
		List<File> result = new ArrayList<File>();

		return result;
	}
	
	private void generateMethodListFile(List<Method> methods) {

		Random random = new Random();
//		int choice = random.nextInt(2);		
		StringBuffer lines = new StringBuffer();
//		if (choice == 0) {
//			for (Method method : methods) {
//				lines.append(method + "\n");
//			}
//		} 
//		else {
			for (Method method : methods) {
				if (method instanceof ConstructorImp)
					lines.append(method + "\n");
			}
			for (Method method : methods) {
				if (method instanceof MethodImp)
					lines.append(method + "\n");
			}

//		}

		FileUtil.makeFile(tmpDir + Constants.SEPARATOR + methodsToTest, lines.toString());

	}
	
	public static void main(String[] args) {
		
		
		
		String timeout = args[0];
		String tmpDir = args[1];
		
		String randoopParameters = "";
		String impactedList = "";
		
		if (!args[2].equals(".")) {
			randoopParameters = args[2];
		} 
		
		if (!args[3].equals(".")) {
			impactedList = args[3];
			impactedList = impactedList.replaceAll("\\*", " ");
		}
		
	//	System.out.println("argsss a1 "+args[0]+" a2 "+args[1]+" a3 "+args[2]+" a4 "+args[3]);
		
//		if (args.length > 2) randoopParameters = args[2];
//		String impactedList  = args[3];

		Main main2 = new Main();
		String[] argsRandoop = {
				"gentests",
				"--methodlist=" + tmpDir + Constants.SEPARATOR + methodsToTest,
				"--timelimit=" + timeout,
				"--log=filewriter",
				"--junit-output-dir="
						+ tmpDir,
				"--output-nonexec=true" };

		if (randoopParameters.length() > 0) {
			String[] listRandoopParameters = randoopParameters.split(";");

			if (listRandoopParameters.length > 0)
				argsRandoop = (String[]) ArrayUtils.addAll(argsRandoop,
						listRandoopParameters);

		}
		
		ArrayList<String> impactedMethods = new ArrayList<String>();
		String[] split = impactedList.split(" ");
		for (String string : split) {
			if (!string.equals("")) {
//				System.out.println(string.trim());
				impactedMethods.add(string);
				System.out.println("impacted Method "+string);
			}
		}
		
		
//		main2.nonStaticMain(argsRandoop);
//		TODO - Verificar chamada a funcao abaixo
//		main2.nonStaticMainAJ(argsRandoop, impactedMethods);
		
//		ThreadGroup rootGroup = Thread.currentThread( ).getThreadGroup( );
//		ThreadGroup parentGroup;
//		while ( ( parentGroup = rootGroup.getParent() ) != null ) {
//		    rootGroup = parentGroup;
//		    Thread[] threads = new Thread[ rootGroup.activeCount() ];
//		    while ( rootGroup.enumerate( threads, true ) == threads.length ) {
//		        threads = new Thread[ threads.length * 2 ];
//		        for (Thread thread : threads) {
//		        	System.out.println("matando threads");
//					thread.interrupt();
//				}
//		    }
//
//		}
//		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//		for (Thread thread : threadSet) {
//			System.out.println("matando threads");
//			thread.interrupt();
//			
//		}


		
		System.exit(0);
	}

}
