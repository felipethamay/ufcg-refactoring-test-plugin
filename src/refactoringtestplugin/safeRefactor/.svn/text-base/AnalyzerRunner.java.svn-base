package refactoringtestplugin.safeRefactor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;


public class AnalyzerRunner {
	public static void execute(String source, String target) {
		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			AntRunner runner = new AntRunner();

			Map<String, String> properties = new HashMap<String, String>();
			properties.put("source_folder", source);
			properties.put("target_folder", target);
			runner.setBuildFileLocation("/Users/gustavo/workspaces/jdolly/RefactoringTestPlugin/ant/build_analyzer.xml");
			runner.setArguments("-Dmessage=Building -verbose");
			runner.addUserProperties(properties);
			
			runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");
			
			String tempdir = System.getProperty("java.io.tmpdir");
			runner.setArguments("-logfile "	+ tempdir + "/logAnt_analyzer.txt");
			runner.run(monitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		 Project project = new Project();
//		  project.init();
//		  project.setProperty( "source_folder", source);
//		  project.setProperty( "target_folder", target);
//		  
//		  File buildFile = new File( "ant/", "build_analyzer.xml");
//		  ProjectHelper.configureProject( project, buildFile);
//		  project.addBuildListener( new DefaultLogger()); 
//		  project.setInputHandler( new PropertyFileInputHandler());
//		  project.executeTarget( "test");
	}
	
	
	
	public static void main(String[] args) {

	}
}
