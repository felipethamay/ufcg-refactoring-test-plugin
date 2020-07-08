package saferefactor.core.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import saferefactor.core.SafeRefactorException;

public class CommandLineCompiler implements Compiler{

	private String binClasspath = "";
	private String libClasspath = "";
//	private String path = System.getProperty("java.home") + "/bin/javac";
//	private String buildPath;
	private String tmpDir = "";
	
	public CommandLineCompiler(String tmpFolder) {
		this.tmpDir = tmpFolder+Constants.SEPARATOR+"tests";
		
	}

	public void compile(String src, String dest) throws MalformedURLException,
			FileNotFoundException, SafeRefactorException {
		
//		String command = "javac -classpath " +
//				"/var/folders/x3/bvxnl_gd10l1v_1pvgpgf4gw0000gn/T/SafeRefactor11/tests/:" +
//				"/Users/melmongiovi/git3/saferefactorimpact/test/subjects/bugModifiedFieldS/bin/:" +
//				"./lib/* " +
//				"/var/folders/x3/bvxnl_gd10l1v_1pvgpgf4gw0000gn/T/SafeRefactor11/tests/RandoopTest.java " +
//				"-d /var/folders/x3/bvxnl_gd10l1v_1pvgpgf4gw0000gn/T/SafeRefactor11/tests/tests/bin_source";
		
		String command3 = "mkdir -p "+dest;
		//		String command4 = "mkdir "+this.tmpDir+Constants.SEPARATOR+"tests"+Constants.SEPARATOR+"bin_source";
//		
		String command1 = "javac -classpath " +
		this.tmpDir+":" +
		this.binClasspath+":" +
		"."+Constants.SEPARATOR+"lib"+Constants.SEPARATOR+"*";
		if (!libClasspath.equals("")) {
			command1+=":"+libClasspath+Constants.SEPARATOR+"* ";
		} else {
			command1+=" ";
		}
		
		command1+=this.tmpDir+Constants.SEPARATOR+"RandoopTest.java " +
		"-d "+dest;
		
		
		
		try {
			String line = "";
			Process exec = Runtime.getRuntime().exec(command3);
			exec.waitFor();
			exec.getInputStream().close();
			exec.getErrorStream().close();
			exec.getOutputStream().close(); 
			exec.destroy();
//			System.out.println("okkkkkkkkkkkk");
//			BufferedReader input = new BufferedReader(
//		            new InputStreamReader(exec.getInputStream()));
//		        while ((line = input.readLine()) != null) {
//		          System.out.println(line);
//		        }
//		        input.close();
			
//			Runtime.getRuntime().exec(command4);
			Process exec2 = Runtime.getRuntime().exec(command1);
			try {
				exec2.waitFor();
				exec2.getInputStream().close();
				exec2.getErrorStream().close();
				exec2.getOutputStream().close(); 
				exec2.destroy();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Runtime.getRuntime().exec(command2);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	@Override
//	public void setBinClasspath(String binClasspath) {
//		this.binClasspath = binClasspath;
//	}
//
//	@Override
//	public void setLibClasspath(String libClasspath) {
//		this.libClasspath = libClasspath;
//	}
	public static void main(String[] args) throws MalformedURLException, FileNotFoundException, SafeRefactorException {
		
		CommandLineCompiler c = new CommandLineCompiler("");
		c.compile("","/private/var/folders/x3/bvxnl_gd10l1v_1pvgpgf4gw0000gn/T/SafeRefactor32/tests/tests/bin_source");
	}

	public void setBinClasspath(String binClasspath) {
		// TODO Auto-generated method stub
		
	}

	public void setLibClasspath(String libClasspath) {
		// TODO Auto-generated method stub
		
	}
}
