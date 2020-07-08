package saferefactor.core.execution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.ProjectHelper;

import saferefactor.core.Parameters;
import saferefactor.core.util.Constants;
import saferefactor.core.util.Project;

public class AntJunitRunnerST implements TestExecutor {

	private final Project projectSource;
	private final Project projectTarget;

	private String tests;
	private final String tmpDir;
	private String reportPathSource;
	private String reportPathTarget;

	public AntJunitRunnerST(Project projectSource,Project projectTarget, String tmpDir) {
		this.projectSource = projectSource;
		this.projectTarget = projectTarget;
		this.tmpDir = tmpDir;
	}

	@Deprecated
	public void executeTests(String tests) throws FileNotFoundException {

		// File buildFile = new File("ant" + "/" + "build_run.xml");
		String path = Parameters.workspacePath;
		 URL buildFile = null;
			try {
				buildFile = new File(path
						+  "build_run.xml").toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		org.apache.tools.ant.Project p = new org.apache.tools.ant.Project();
		p.setProperty("basedir", path);

		p.setProperty("binClasspathSource", projectSource.getBuildFolder()
				.getAbsolutePath());

		if (projectSource.getLibFolder() != null)
			p.setProperty("libClasspathSource", projectSource.getLibFolder()
					.getAbsolutePath());
		else
			p.setProperty("libClasspathSource", projectSource.getBuildFolder()
					.getAbsolutePath());
		p.setProperty("binClasspathTarget", projectTarget.getBuildFolder()
				.getAbsolutePath());

		if (projectTarget.getLibFolder() != null)
			p.setProperty("libClasspathTarget", projectTarget.getLibFolder()
					.getAbsolutePath());
		else
			p.setProperty("libClasspathTarget", projectTarget.getBuildFolder()
					.getAbsolutePath());

		p.setProperty("tests.dir", tests);
		p.setProperty("report.dir.source", reportPathSource);
		p.setProperty("report.dir.target", reportPathTarget);

//		DefaultLogger consoleLogger = new DefaultLogger();
//		consoleLogger
//				.setMessageOutputLevel(org.apache.tools.ant.Project.MSG_INFO);
//		FileOutputStream fileOutputStream = new FileOutputStream(tmpDir
//				+ Constants.SEPARATOR + "log_saferefactor_testrunner.txt");
//		PrintStream ps = new PrintStream(fileOutputStream);
//		consoleLogger.setOutputPrintStream(ps);
//		consoleLogger.setErrorPrintStream(ps);
//
//		p.addBuildListener(consoleLogger);

		p.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		p.addReference("ant.projectHelper", helper);
		helper.parse(p, buildFile);

		p.executeTarget(p.getDefaultTarget());

	}

	public void setReportPathSource(String reportPath) {
		this.reportPathSource = reportPath;
	}

	public String getReportPathSource() {
		return reportPathSource;
	}
	
	public void setReportPathTarget(String reportPath) {
		this.reportPathTarget = reportPath;
	}

	public String getReportPathTarget() {
		return reportPathTarget;
	}

	public void run() {
		// File buildFile = new File("ant" + "/" + "build_run.xml");
//		URL buildFile = this.getClass().getResource("/build_run.xml");
//		String path = System.getProperty("user.dir");
		String path = Parameters.workspacePath;
		 URL buildFile = null;
			try {
				buildFile = new File(path
						+  "build_run_st.xml").toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		org.apache.tools.ant.Project p = new org.apache.tools.ant.Project();
		p.setProperty("basedir", path);

		p.setProperty("binClasspathSource", projectSource.getBuildFolder()
				.getAbsolutePath());

		if (projectSource.getLibFolder() != null)
			p.setProperty("libClasspathSource", projectSource.getLibFolder()
					.getAbsolutePath());
		else
			p.setProperty("libClasspathSource", projectSource.getBuildFolder()
					.getAbsolutePath());
		p.setProperty("binClasspathTarget", projectTarget.getBuildFolder()
				.getAbsolutePath());

		if (projectTarget.getLibFolder() != null)
			p.setProperty("libClasspathTarget", projectTarget.getLibFolder()
					.getAbsolutePath());
		else
			p.setProperty("libClasspathTarget", projectTarget.getBuildFolder()
					.getAbsolutePath());
		
		p.setProperty("tests.dir", getTests());
		p.setProperty("report.dir.source", reportPathSource);
		p.setProperty("report.dir.target", reportPathTarget);
		
//		DefaultLogger consoleLogger = new DefaultLogger();
//		consoleLogger
//				.setMessageOutputLevel(org.apache.tools.ant.Project.MSG_INFO);
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(tmpDir
					+ Constants.SEPARATOR + "log_saferefactor_testrunner" + System.currentTimeMillis() + ".txt");

			PrintStream ps = new PrintStream(fileOutputStream);
//			consoleLogger.setOutputPrintStream(ps);
//			consoleLogger.setErrorPrintStream(ps);
//			p.addBuildListener(consoleLogger);
			p.init();
			ProjectHelper helper = ProjectHelper.getProjectHelper();
			p.addReference("ant.projectHelper", helper);
			helper.parse(p, buildFile);
			p.executeTarget(p.getDefaultTarget());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public String getTests() {
		return tests;
	}

	public void setTests(String tests) {
		this.tests = tests;
	}

	public String getReportPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setReportPath(String reportPath) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public String getReportPath() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void setReportPath(String reportPath) {
//		// TODO Auto-generated method stub
//		
//	}

}
