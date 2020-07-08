import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class A {

	public String getContents(String in) throws IOException {

		InputManager im = new InputManagerASCII(in);
		String contents = "";
		im.openFile();
		while (!im.isEndOfFile()) {
			contents += im.readLine() + "\n";
		}
		im.closeFile();
		return contents;
	}

	// copia o arquivo in para o out
	public void criaArquivo(String in, String out) throws IOException {
		String contents = getContents(in);
		OutputManager om = new OutputManagerASCII(out);
		om.createFile();
		om.writeLine(contents);
		om.closeFile();
	}

	public String getFileName(String in) {
		return in.substring(in.lastIndexOf("/") + 1);
	}

	public String getPastaName(String in) {
		return in.substring(in.lastIndexOf("/") + 1, in.indexOf(".java"));
	}

	public void criaTudo2(String pastaOrig, String pastaDest) throws IOException {

		File f = new File(pastaOrig + "/");
		String[] list = f.list();
		for (String fileName : list) {
			if (!fileName.startsWith("."))
				criaPasta(pastaOrig + "/" + fileName, pastaOrig + "_out/" + fileName, pastaDest);
		}

	}

	public void criaPasta(String in, String out, String pasta) throws IOException {
		String pastaName = getPastaName(in);
		String fileName = getFileName(in);
		if (!new File(out).exists()) {
			return;
		}
		if ((new File(pasta + pastaName + "/out/" + fileName).exists())) {
			return;
		}
		File f = new File(pasta + pastaName + "/in/");
		if (f.exists()) {
			System.out.println("Já existe!!! " + f.getAbsolutePath());
			return;
		}
		f.mkdirs();
		f = new File(pasta + pastaName + "/out/");
		f.mkdirs();

		criaArquivo(in, pasta + pastaName + "/in/" + fileName);
		criaArquivo(out, pasta + pastaName + "/out/" + fileName);
	}

	public void criaPasta(String in, String pasta, String pastaName, boolean nothing) throws IOException {
		String fileName = getFileName(in);
		// if (!(new File(pasta+pastaName+"/out/"+fileName).exists())) {
		// return;
		// }
		File f = new File(pasta + pastaName);
		// if (f.exists()) {
		// System.out.println("Já existe!!! "+f.getAbsolutePath());
		// return;
		// }
		if (!f.exists())
			f.mkdirs();

		criaArquivo(in, pasta + pastaName + "/" + fileName);
	}

	public void criaTudo(String pastaOrig, String pastaDest) throws IOException {

		File f = new File(pastaOrig + "_in");
		String[] list = f.list();
		for (String fileName : list) {
			if (!fileName.startsWith("."))
				criaPasta(pastaOrig + "_in/" + fileName, pastaOrig + "_out/" + fileName, pastaDest);
		}

	}

	public void criaTudo2(String pastaOrig, String pastaDest, String pastaName) throws IOException {

		File f = new File(pastaOrig + "_in");
		String[] list = f.list();
		for (String fileName : list) {
			if (!fileName.startsWith("."))
				criaPasta(pastaOrig + "_in/" + fileName, pastaDest, pastaName, true);
		}

	}

	public static void main(String[] args) throws IOException {

		// String pasta =
		// "/Users/melmongiovi/Documents/doutorado/experiments/OWC/eclipse/extractmethod/";
		// String in =
		// "/Users/melmongiovi/git2/eclipse.jdt.ui/org.eclipse.jdt.ui.tests.refactoring/resources/ExtractMethodWorkSpace/ExtractMethodTests/duplicates_in/A_test950.java";
		// String out =
		// "/Users/melmongiovi/git2/eclipse.jdt.ui/org.eclipse.jdt.ui.tests.refactoring/resources/ExtractMethodWorkSpace/ExtractMethodTests/duplicates_out/A_test950.java";
		// new A().criaPasta(in, out, pasta);
		// System.out.println(new
		// A().getFileName("/Users/melmongiovi/git2/eclipse.jdt.ui/org.eclipse.jdt.ui.tests.refactoring/resources/ExtractMethodWorkSpace/ExtractMethodTests/A_test450.java"));

		ArrayList<String> pastas = new ArrayList<String>();
		// pastas.add("branch");
		// pastas.add("defaultMethods18");
		// pastas.add("destination");
		// pastas.add("destination18");
		// pastas.add("duplicates");
		// pastas.add("enums");
		// pastas.add("error");
		// pastas.add("expression");
		// pastas.add("fieldInitializer");
		// pastas.add("generics");
		// pastas.add("initializer");
		// pastas.add("lambdaExpression18");
		// pastas.add("locals");
		// pastas.add("nested");
		// pastas.add("parameterName");
		// pastas.add("return");
		// pastas.add("semicolon");
		// pastas.add("staticMethods18");
		// pastas.add("try");
		// pastas.add("try17");
		// pastas.add("validSelection");
		// pastas.add("varargs");
		// pastas.add("wiki");

		// pastas.add("invalidSelection");
		// pastas.add("invalidSelection17");

		// encapsulate
		pastas.add("base");
		pastas.add("existingmethods");
		pastas.add("object");
		pastas.add("static");
		pastas.add("static_ref");

		// String pastaDest =
		// "/Users/melmongiovi/Documents/doutorado/experiments/OWC/eclipse/extractmethod2/";
		String pastaDest = "C:/Users/Felipe/Documents/doutorado/experiments/MT/eclipse/encapsulate/";
		// pastaDest =
		// "/Users/melmongiovi/Documents/workspace6/RefactoringTestPluginASM/resources/";
		String pastaPath = "C:/Users/Felipe/git2/eclipse.jdt.ui/org.eclipse.jdt.ui.tests.refactoring/resources/SefWorkSpace/SefTests/";
		String pastaDestAux = "";
		for (String pastaName : pastas) {
			System.out.println(pastaName);
			// pastaDestAux = pastaDest + pastaName + "/";
			String pastaOrig = pastaPath + pastaName;
			// new A().criaTudo2(pastaOrig, pastaDest, pastaName + "_in");
			new A().criaTudo(pastaOrig, pastaDest);

		}

	}
}
