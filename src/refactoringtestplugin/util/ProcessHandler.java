package refactoringtestplugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessHandler extends Thread {

	InputStream inpStr;
	String strType;
	public String output;
	public String error;
	int num;

	public ProcessHandler(InputStream inpStr, String strType, int num) {
		this.inpStr = inpStr;
		this.strType = strType;
		this.num = num;
	}

	public void run() {
		try {
			// We'll print only the first 5 lines
			int counter=0;
			output="_";
			error ="_";			
			InputStreamReader inpStrd = new InputStreamReader(inpStr);
			BufferedReader buffRd = new BufferedReader(inpStrd);
			String line = "";
			while ((counter < 5)  && (line = buffRd.readLine()) != null) {
				//System.out.println(strType + " —> " + line);
				if (strType.equals("ERROR")) {
					error  += line;
				} else if (strType.equals("INPUT")) {
					output += line;
				}
				counter++;
			}
			buffRd.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	




//	public static void main(String args[]) throws Exception {
//		String directory = "C:\\0_Files_Refactored\\test26\\";
//		Process pro = Runtime.getRuntime().exec(
//				"java -cp " + directory + "in" + File.separator + " A");
//
//		/* handling the streams so that dead lock situation never occurs. */
//		ProcessHandler inputStream = new ProcessHandler(pro.getInputStream(),
//				"INPUT", 0);
//		ProcessHandler errorStream = new ProcessHandler(pro.getErrorStream(),
//				"ERROR", 0);
//
//		/* start the stream threads */
//		inputStream.start();
//		errorStream.start();
//		
//		while (inputStream.isAlive() && errorStream.isAlive())
//			pro.waitFor();
//
//		System.out.println("intermedio");
//		Process pro2 = Runtime.getRuntime().exec(
//				"java -cp " + directory + "out" + File.separator + " A");
//		ProcessHandler inputStream2 = new ProcessHandler(pro2.getInputStream(),
//		"INPUT", 1);
//		ProcessHandler errorStream2 = new ProcessHandler(pro2.getErrorStream(),
//		"ERROR", 1);
//
//		/* start the stream threads */
//		inputStream2.start();
//		errorStream2.start();
//		
//		while (inputStream2.isAlive() && errorStream2.isAlive())
//			pro2.waitFor();
//		
//		System.out.println("intermedio");
//		Process pro3 = Runtime.getRuntime().exec(
//				"java -cp " + directory + "outNB" + File.separator + " A");
//		ProcessHandler inputStream3 = new ProcessHandler(pro3.getInputStream(),
//		"INPUT", 2);
//		ProcessHandler errorStream3 = new ProcessHandler(pro3.getErrorStream(),
//		"ERROR", 2);
//
//		/* start the stream threads */
//		inputStream3.start();
//		errorStream3.start();
//		
//		while (inputStream3.isAlive() && errorStream3.isAlive())
//			pro3.waitFor();
//		
//		System.out.println(output[0] + " " + output[1] + " " + output[2]);
//		System.out.println(error[0] + " " + error[1] + " " + error[2]);
//	}
}
