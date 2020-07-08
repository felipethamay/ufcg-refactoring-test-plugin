package refactoringtestplugin;

import io.InputManager;
import io.InputManagerASCII;
import io.OutputManagerASCII;

import java.io.IOException;
import java.util.List;

public class ModifyAlloyFile {
	
	String path = "";
	
	public ModifyAlloyFile(String path) throws IOException {
		
		this.path = path;
	}
	
	public void addFilters(List<String> filters) throws IOException {
		StringBuffer sb = new StringBuffer();
		
		InputManager im = new InputManagerASCII(path);
		im.openFile();
		while (!im.isEndOfFile()) {
			String line = im.readLine();
			if (line.contains("pred show[]")) {
				break;
			} else {
				sb.append(line+"\n");
			}
		}
		im.closeFile();
		OutputManagerASCII out = new OutputManagerASCII(path);
		out.createFile();
		out.writeLine(sb.toString());
		
		out.writeLine("pred show[] {");
		for (String filter : filters) {
			out.writeLine(""+filter);
		}
		out.writeLine("}");
		out.closeFile();
	}
}
