package refactoringtestplugin.pushdownmethod;

import java.util.List;

import refactoringtestplugin.info.AnalyzeTransformationInfo;
import refactoringtestplugin.info.Clas;
import refactoringtestplugin.info.Method;
import refactoringtestplugin.info.MustNotDo;
import refactoringtestplugin.info.Statement_;

public class AnalyzeTransformation extends AnalyzeTransformationInfo{
	
	
	
	String methodName;
	Method mSource;
	Method mTarget;
	Clas cSource;
	Clas cTarget;
	StringBuffer problem = new StringBuffer();
	String className_ = "";
	
	public  AnalyzeTransformation() {
		
	}
	
	public boolean isTransformationBug(String source, String target, String methodName, String className) {
		
		this.source = source;
		this.target = target;
		this.methodName = methodName;
		this.className_ = className;
		getInfo();
		
		getInfoSource();
		getInfoTarget();
		
		boolean transformationBug = true;
		
		//as classes tem que ser diferentes
		if (cTarget == null) {
			problem.append("o metodo foi removido\n");
			System.out.println(problem);
			transformationBug = true;
		}
		if (cTarget != null) {
			if (cSource.getFullName().equals(cTarget.getFullName())) {
				
				problem.append("o metodo nao saiu da superclasse. A classe do source é abstrata: "+cSource.isAbstract()+
						" a classe do target é abstract: "+cTarget.isAbstract()+"O metodo do source é abstrato: "+isAbstract(mSource)+
						" o metodo do target é abstract: "+isAbstract(mTarget)+"\n");
				
				System.out.println(problem);
				transformationBug = true;
			}
			
			//a classe do target tem que ser subclasse da classe do source
			String superClass = cTarget.getSuperClass();
			if (!superClass.equals(cSource.getClassName())) {
				System.out.println("a classe que o metodo foi nao é subclasse");
				problem.append("a classe que o metodo foi nao é subclasse\n");
				transformationBug = true;
			}
			
			//o metodo nao pode mais estar na classe de origem
			Clas classTarget = getClass(cSource.getFullName(),classesT);
			if (containsMethod(classTarget.getMethods(), methodName)) {
				System.out.println("o metodo nao saiu da classe de origem");
				problem.append("o metodo nao saiu da classe de origem\n");
				transformationBug = true;
			}
			
			if (containsAbstractClassWithoutAbstractMethod()) {
				problem.append("contem abstract class sem abstract method\n");
				System.out.println(problem);
				transformationBug = true;
			}
			
			if (MustNotDo.addEntityM(classesS, classesT, this.cTarget.getFullName() , this.methodName)) {
				System.out.println("add entity");
				problem.append("add entity\n");
				transformationBug = true;
			}
			
			if (MustNotDo.decreaseAccessibility(classesS, classesT)) {
				System.out.println("diminuiu visibilidade");
				problem.append("diminuiu visibilidade\n");
				transformationBug = true;
			}
			
			if (MustNotDo.makeClassOrMethodAbstract(classesS, classesT)) {
				System.out.println("tornou classe ou metodo abstrato");
				problem.append("tornou classe ou metodo abstrato\n");
				transformationBug = true;
			}
			
			if (MustNotDo.removeClass(classesS, classesT)) {
				System.out.println("removeu classe");
				problem.append("removeu classe\n");
				transformationBug = true;
			}
		}
		return transformationBug;
	
	}
	
	public boolean containsAbstractClassWithoutAbstractMethod() {
		for (Clas clas : classesT) {
			boolean ok = false;
			if (clas.isAbstract()) {
				List<Method> methods = clas.getMethods();
				if (methods.size() == 0) return true;
				for (Method method : methods) {
					if (isAbstract(method)) {
						ok = true;
					}
				}
				if (!ok) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean containsMethod(List<Method> list, String methodName) {
		for (Method method : list) {
			if (method.getName().equals(methodName)) {
				return true;
			}
		}
		return false;
	}
	
	public Clas getClass(String fullName, List<Clas> list) {
		for (Clas clas : list) {
			if (clas.getFullName().equals(fullName)) {
				return clas;
			}
		}
		return null;
	}
	
	public void getInfoSource() {
		for (Clas clas : classesS) {
			if (clas.getClassName().equals(this.className_)) {
				List<Method> methods = clas.getMethods();
				for (Method method : methods) {
					if (method.getName().equals(methodName)) {
						this.mSource = method;
						this.cSource = clas;
						return;
					}
				}
			}
		}
	}
	
	private boolean isAbstract(Method m) {
		List<String> modifiers = m.getModifiers();
		for (String mod : modifiers) {
			if (mod.contains("abstract")) {
				return true;
			}
		}
		return false;
	}
	
	
	
	public void getInfoTarget() {
		for (Clas clas : classesT) {
			List<Method> methods = clas.getMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName) && method.getParameters().toString().equals(mSource.getParameters().toString()) &&
						(isAbstract(mSource) == isAbstract(method)) && mSource.getStatements().size() == method.getStatements().size()) {
					this.mTarget = method;
					this.cTarget = clas;
					return;
				}
			}
		}
	}
	public String getProblem() {
		return problem.toString();
	}

	public void setProblem(StringBuffer problem) {
		this.problem = problem;
	}

	public static void main(String[] args) {
		String source = "C:/Users/Felipe/Documents/workspace/source/src/";
		String target = "C:/Users/Felipe/Documents/workspace/target/src/";
		
//		source = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/PushDownMethod1/Alloy/test1/in/";
//		target = "C:/Users/Felipe/Documents/doutorado/bug_transf/results_exp/PushDownMethod1/Alloy/test1/out/eclipse/";
		AnalyzeTransformation a = new AnalyzeTransformation();
		System.out.println(a.isTransformationBug(source, target, "m", "A"));
	}

}
