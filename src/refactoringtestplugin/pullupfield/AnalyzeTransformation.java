package refactoringtestplugin.pullupfield;

import java.util.List;

import refactoringtestplugin.info.AnalyzeTransformationInfo;
import refactoringtestplugin.info.Clas;
import refactoringtestplugin.info.Field;
import refactoringtestplugin.info.Method;
import refactoringtestplugin.info.MustNotDo;

public class AnalyzeTransformation extends AnalyzeTransformationInfo{
	
	
	
	String fieldName;
	Field fSource;
	Field fTarget;
	Clas cSource;
	Clas cTarget;
	StringBuffer problem = new StringBuffer();
	boolean transformationBug = false;
	String className_;
	
	public  AnalyzeTransformation() {
		
	}
	
	public boolean isTransformationBug(String source, String target, String fieldName, String clasName) {
		this.source = source;
		this.target = target;
		this.fieldName = fieldName;
		this.className_ = clasName;
		getInfo();
		
		getInfoSource();
		getInfoTarget();
		
		//as classes tem que ser diferentes
		if (cTarget == null) {
			problem.append("o field foi removido\n");
			transformationBug = true;
		}
		if (cTarget != null) {
			if (cSource.getFullName().equals(cTarget.getFullName())) {
				problem.append("o field nao saiu da subclasse. A classe do source é abstrata: "+cSource.isAbstract()+
						" a classe do target é abstract: "+cTarget.isAbstract()+"\n");
//				System.out.println(problem);
				transformationBug = true;
			}
			
			//a classe do target tem que ser superclasse da classe do source
			String superClass = cSource.getSuperClass();
			if (!superClass.equals(cTarget.getClassName())) {
				problem.append("a classe que o field foi nao é superclasse\n");
				transformationBug = true;
			}
			
			//o metodo nao pode mais estar na classe de origem
			Clas classTarget = getClass(cSource.getFullName(),classesT);
			if (containsfield(classTarget.getFields(), fTarget)) {
				problem.append("o field nao saiu da classe de origem\n");
				transformationBug = true;
			}
			
			if (!checkOtherFieldsPullUp()) {
				problem.append("outro field que fica em uma outra subclasse da classe target nao subiu\n");
				transformationBug = true;
			}
			
			if (MustNotDo.addEntityM(classesS, classesT, this.cTarget.getFullName() , this.fieldName)) {
				problem.append("add entity\n");
				transformationBug = true;
			}
			
			if (MustNotDo.decreaseAccessibility(classesS, classesT)) {
				problem.append("diminuiu visibilidade\n");
				transformationBug = true;
			}
			
			if (MustNotDo.makeClassOrMethodAbstract(classesS, classesT)) {
				problem.append("tornou classe ou metodo abstrato\n");
				transformationBug = true;
			}
			
			if (MustNotDo.removeClass(classesS, classesT)) {
				problem.append("removeu classe\n");
				transformationBug = true;
			}
			if (MustNotDo.removeField(classesS, classesT, this.fSource, this.cSource)) {
				problem.append("removeu field\n");
				transformationBug = true;
			}
		}
		
		return transformationBug;
	
	}
	
	public boolean checkOtherFieldsPullUp() {
		for (Clas clas : classesS) {
			if (clas.getSuperClass().equals(cTarget.getClassName()) && !clas.getClassName().equals(cSource)) {
				List<Field> fields = clas.getFields();
				for (Field field : fields) {
					if (field.getName().equals(this.fieldName) && field.getReturnType().equals(this.fSource.getReturnType())) {
						Clas classTarget = getClass(clas.getFullName(),classesT);
						if (containsfield(classTarget.getFields(), fTarget)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public boolean containsfield(List<Field> list, Field fieldName) {
		for (Field field : list) {
			if (field.getName().equals(fieldName) && field.getReturnType().equals(fieldName.getReturnType())) {
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
			List<Field> fields = clas.getFields();
			for (Field field : fields) {
				if (field.getName().equals(fieldName) && clas.getClassName().contains(this.className_)) {
					this.fSource = field;
					this.cSource = clas;
					return;
				}
			}
		}
		
	}
	
	//rever isso aqui porque se tiver dois fields com o mesmo nome em classes diferentes pode confundir
	public void getInfoTarget() {
		for (Clas clas : classesT) {
			List<Field> fields = clas.getFields();
			for (Field field : fields) {
				if (field.getName().equals(fieldName)) {
					this.fTarget = field;
					this.cTarget = clas;
					return;
				}
			}
		}
	}

	
//	private boolean isAbstract(Method m) {
//		List<String> modifiers = m.getModifiers();
//		for (String mod : modifiers) {
//			if (mod.contains("abstract")) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	

	public String getProblem() {
		return problem.toString();
	}

	public void setProblem(StringBuffer problem) {
		this.problem = problem;
	}

	public static void main(String[] args) {
		String source = "C:/Users/Felipe/Documents/workspace/source/src/";
		String target = "C:/Users/Felipe/Documents/workspace/target/src/";
		
		AnalyzeTransformation a = new AnalyzeTransformation();
		System.out.println(a.isTransformationBug(source, target, "f", "B"));
		System.out.println(a.getProblem());
	}

}
