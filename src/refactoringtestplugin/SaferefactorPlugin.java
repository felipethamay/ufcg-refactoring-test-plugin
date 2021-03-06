package refactoringtestplugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import br.edu.ufcg.saferefactor.core.Report;
import br.edu.ufcg.saferefactor.core.Saferefactor;
import br.edu.ufcg.saferefactor.core.util.FileUtil;
import br.edu.ufcg.saferefactor.refactoring.Constants;







public class SaferefactorPlugin {
	

	
	

	private final String source;
	private final String target;
	private final String bin;
	private final String src;
	private final String lib;
	private final String timelimit;


	public SaferefactorPlugin(String source, String target, String bin,
			String src, String lib, String timelimit) {
				this.source = source;
				this.target = target;
				this.bin = bin;
				this.src = src;
				this.lib = lib;
				this.timelimit = timelimit;
		
		
	}

	
	public void run() {
		
		FileUtil.createFolders();
		
		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			AntRunner runner = new AntRunner();

			//propriedades
			Map<String, String> properties = new HashMap<String, String>();						
			properties.put("source", source);
			properties.put("target", target);	
			
			properties.put("timeout", timelimit);			
			properties.put("bin", bin);
			properties.put("lib", lib);
			properties.put("src", src);
			properties.put("tests.folder", Constants.TEST);
			
			

			runner.setBuildFileLocation("build.xml");
			runner.setArguments("-Dmessage=Building -verbose");
			runner.addUserProperties(properties);
			
			runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");
			
			String tempdir = System.getProperty("java.io.tmpdir");
			runner.setArguments("-logfile "	+ tempdir + "/logAnt_analyzer2.txt");
			runner.run(monitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
