package refactoringtestplugin.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Compile {

	public static String compile(String path) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream outError = new ByteArrayOutputStream();

		PrintWriter pw = new PrintWriter(out);
		PrintWriter pwerror = new PrintWriter(outError);

		org.eclipse.jdt.internal.compiler.batch.Main.compile(
				"-classpath rt.jar " + path, pw, pwerror);

		// System.out.println(out.toString());

		// System.out.println(outError.toString());
		return outError.toString();
	}

	public static void main(String[] args) {

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("/Users/gustavo/workspaces/jdolly/generator_util/programs.xml"));

			// normalize text representation
			doc.getDocumentElement().normalize();
//			System.out.println("Root element of the doc is "
//					+ doc.getDocumentElement().getNodeName());

			NodeList listOfPrograms = doc.getElementsByTagName("input");
			int totalPersons = listOfPrograms .getLength();
			System.out.println("Total no of programs : " + listOfPrograms);
			
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// String path =
		// "/private/var/folders/bx/bxpvKGfwF-yg3RjPZ5LRJk+++TI/-Tmp-/pullupfield0";
		// String path =
		// "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupfield/last";
		// File refactoring = new File(path);
		// File[] tests = refactoring.listFiles(new FileFilter() {
		// @Override
		// public boolean accept(File pathname) {
		// if (pathname.getName().startsWith("test"))
		// return true;
		// else
		// return false;
		// }
		//
		// });
		// int i = 0;
		// int j = 0;
		// for (File test : tests) {
		// File out = new File(test,"in");
		// String outputMsg = compile(out.toString());
		//
		// if (outputMsg.contains("ERROR in")) {
		// i++;
		// }
		// j++;
		// }
		// System.out.println("total de programas gerados: " + j);
		// System.out.println("n�mero de erros de compilacao: " + i);
		// double per = (i * 100) / j;
		// System.out.println("porcentagem:" + per);

	}

}
