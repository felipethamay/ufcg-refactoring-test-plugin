import refactoringtestplugin.Testsss;

public class B {
public static void main(String[] args) {
	String path = "C:/Users/Felipe/Documents/doutorado/experiments/OWC/eclipse/movemethod/test34/";
	
	String in = "/Volumes/Melina/Seagate/projetos_rio/elastic/elasticsearch_v0/";
	in = "/Volumes/Melina/Seagate/projetos_rio/elastic/elasticsearch303282v0/";
	String out = "/Volumes/Melina/Seagate/projetos_rio/elastic/elasticsearch_v1/";
	out = "/Volumes/Melina/Seagate/projetos_rio/elastic/elasticsearch303282v1/";
	
	in = "/Volumes/Melina/Seagate/projetos_rio/elastic/elasticsearch0056v0/";
	out = "/Volumes/Melina/Seagate/projetos_rio/elastic/elasticsearch0056v1/";
	
//	in = "/Users/melmongiovi/Documents/doutorado/experiments/OWC/eclipse/movemethod/test33/in/";
//	out = "/Users/melmongiovi/Documents/doutorado/experiments/OWC/eclipse/movemethod/test33/out/";
	
//	boolean refactoring = Testsss.runSRI(path+"in/", path+"out/");
	boolean refactoring = Testsss.runSRI(in, out);
	if (!refactoring) {
		System.out.println("false");
	} else {
		System.out.println("true");
	}
}
}
