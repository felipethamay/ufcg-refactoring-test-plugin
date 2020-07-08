package saferefactor.core;

import java.io.File;
import saferefactor.core.util.Constants;

public class Tools {
	public static boolean deleteDir(File dir) {  
	       if (dir.isDirectory()) {  
	           String[] children = dir.list(); 
	           if (children != null) {
	           for (int i=0; i<children.length; i++) {   
	              boolean success = deleteDir(new File(dir, children[i]));  
	               if (!success) {  
	                   return false;  
	               }  
	           }  
	           }
	       }  
	     
	       // Agora o diretório está vazio, restando apenas deletá-lo.  
	       return dir.delete();  
	   } 
	
	public static void deleteTempDir() {
		
		String tmp = Constants.SAFEREFACTOR_DIR;
		File f = new File(Constants.SAFEREFACTOR_DIR);
		String[] dir = f.list();
		for (String string : dir) {
			if (string.contains("temp") || string.contains("SafeRefactor")) {
				File tmpDir = new File(tmp+Constants.SEPARATOR+string+Constants.SEPARATOR);
				if (tmpDir.isDirectory()) {
					deleteDir(tmpDir);
				} else {
					deleteFile(tmp+Constants.SEPARATOR+string);
				}
			}
		}
	}

	public static void deleteFile(String string) {
		File f = new File(string);
		f.delete();
	}
	public static void main(String[] args) {
		
		Tools.deleteTempDir();
		
	}
}
