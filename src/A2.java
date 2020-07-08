import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManager;
import io.OutputManagerASCII;

public class A2 {

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

	public void criaPastaOSC(String in, String pasta) throws IOException {
		String pastaName = getPastaName(in);
		pastaName = pastaName.replace("_in","");
		String fileName = getFileName(in);
		
		
		File f = new File(pasta + pastaName + "/in/");
		if (f.exists()) {
			System.out.println("Já existe!!! " + f.getAbsolutePath());
			return;
		}
		f.mkdirs();
		

		criaArquivo(in, pasta + pastaName + "/in/" + fileName);
	}
	
	public void criaPasta(String in, String out, String pasta) throws IOException {
		String pastaName = getPastaName(in);
		pastaName = pastaName.replace("_in","");
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
		criaArquivo(out, pasta + pastaName + "/out/" + fileName.replace("_in", "_out"));
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



		// String pastaDest =
		// "/Users/melmongiovi/Documents/doutorado/experiments/OWC/eclipse/extractmethod2/";
		String pastaDest = "C:/Users/Felipe/Documents/doutorado/experiments/MT/eclipse/extractConstant/";
		// pastaDest =
		// "/Users/melmongiovi/Documents/workspace6/RefactoringTestPluginASM/resources/";
		String pastaPath = "C:/Users/Felipe/git-jdt3/eclipse.jdt.ui/org.eclipse.jdt.ui.tests.refactoring/resources/ExtractConstant/cannotExtract/";
		
		A2 a = new A2();
		File f = new File(pastaPath);
		String[] list = f.list();
		for (String string : list) {
			if (string.contains("java")) {
//				a.criaPasta(pastaPath+string, pastaPath+string.replace("_in","_out"), pastaDest);
				a.criaPastaOSC(pastaPath+string,  pastaDest);
			}
		}
//		String pastaDestAux = "";
			// pastaDestAux = pastaDest + pastaName + "/";
			// new A().criaTudo2(pastaOrig, pastaDest, pastaName + "_in");
//			new A2().criaTudo(pastaPath, pastaDest);


	}
}
