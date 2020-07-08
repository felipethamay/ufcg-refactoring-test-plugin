import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import io.OutputManager;
import io.OutputManagerASCII;

public class RunSRAllPrograms {

	public static boolean compile(String testDirectory) {

		// ProblemReport pr = new ProblemReport();

		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			AntRunner runner = new AntRunner();

			// propriedades
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("source", testDirectory);
			properties.put("bin", "");
			properties.put("lib", "");
			properties.put("src", "");
			properties.put("tests.folder", "");

			// System.out.println(Testsss.class.getResource("/ant/build_compile.xml").toString());
			runner.setBuildFileLocation(
					"C:/Users/Felipe/git-final/eclipse.jdt.ui/org.eclipse.jdt.ui.tests.refactoring/ant/build_compileOnlyOne.xml");
			runner.setArguments("-Dmessage=Building -verbose");
			runner.addUserProperties(properties);

			runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");

			String tempdir = System.getProperty("java.io.tmpdir");
			runner.setArguments("-logfile " + tempdir + "/logAnt_analyzer3.txt");
			runner.run(monitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

		// ResultComparator rc = new ResultComparator(Constants.TESTSRC,
		// Constants.TESTTGT);
		// Report report = rc.generateReport();
		// if (!report.isSameBehavior())
		// pr.addProblem("has behavior change");

		// return ;
	}

	public static void main(String[] args) throws IOException {

		String path = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/extractmethod2/";
		path = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/jrrt/extractmethod/";
		path = "C:/Users/Felipe/Documents/doutorado/experiments/MT/eclipse/encapsulate/";

		// 0,1,6,7,11,16,17,20,26,28,29,30,31,33,34,35,36,40-49
		StringBuffer results = new StringBuffer();
		File f = new File(path);
		String[] list = f.list();
		for (String name : list) {
			// if (name.equals("test0") || name.equals("test1") ||
			// name.equals("test6") || name.equals("test7")
			// || name.equals("test11") || name.equals("test16") ||
			// name.equals("test17") || name.equals("test20")
			// || name.equals("test26") || name.equals("test28") ||
			// name.equals("test29") || name.equals("test30")
			// || name.equals("test31") || name.equals("test33") ||
			// name.equals("test34") || name.equals("test35")
			// || name.equals("test36"))
			// continue;
			// if (name.contains("test4"))
			// continue;
			if (!name.equals("TestCompoundParenthesizedWrite"))
				continue;
			String in = path + name + "/in/";
			String out = path + name + "/out/";
			// if (!RunSRAllPrograms.compile(in)) {
			// results.append("Input nao compila " + in + "\n");
			// continue;
			// }
			// if (!RunSRAllPrograms.compile(out)) {
			// results.append("Output nao compila " + out + "\n");
			// continue;
			// }
			boolean refactoring = Testsss.runSRI(in, out);
			if (!refactoring) {
				results.append("Bug BC " + out + "\n");
			}
		}
		System.out.println(results.toString());
		OutputManager out = new OutputManagerASCII(path + "resultsBC.txt");
		out.createFile();
		out.writeLine(results.toString());
		out.closeFile();

	}

}
