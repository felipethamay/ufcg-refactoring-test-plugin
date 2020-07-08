package refactoringtestplugin;
import java.io.File;

import saferefactor.util.SRImpact;

public class Testsss {
	public static boolean runSRI(String sourcePath, String targetPath) {
		SRImpact sri = new SRImpact("", sourcePath, targetPath, "target/lib/", "30", "target/classes/", true);
//		SRImpact sri = new SRImpact("", sourcePath, targetPath, "", "1", "", true);
//		SRImpact sri = new SRImpact("", sourcePath, targetPath, "core/build/libs/", "30", "core/build/classes/", true);
		System.out.println("================is refactoring " + sri.isRefactoring());
		return sri.isRefactoring();
	}

	public static void main(String[] args) {

		String s = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/results/RenameMethod/virtual/test37/in/";
		String t = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/results/RenameMethod/virtual/test37/out/eclipse/";
		s = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/results/ExtractMethod/0/test83/in/";
		t = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/results/ExtractMethod/0/test83/out/eclipse/";
		s = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/extractmethod/A_test752/in/";
		t = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/extractmethod/A_test752/out/";
		Testsss.runSRI(s, t);
		if (true)
			return;

		System.out.println("a");
		StringBuffer sb = new StringBuffer();
		String path = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/jrrt/renametype/";
		File f = new File(path);
		String[] list = f.list();
		for (String string : list) {
			// if (string.equals("test2") || string.equals("test11")) continue;
			if (string.startsWith("."))
				continue;
			String source = path + string + "/in/";
			String target = path + string + "/out/";
			if (!new File(target).exists())
				continue;
			if (!Testsss.runSRI(source, target)) {
				sb.append("bug BC " + source + "\n");
				System.out.println("bug " + source);
			}
			System.out.println(" " + source);
		}
		System.out.println(sb.toString());
	}
}
