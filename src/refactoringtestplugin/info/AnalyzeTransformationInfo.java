package refactoringtestplugin.info;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeTransformationInfo {

	public List<Clas> classesS = new ArrayList<Clas>();
	public List<Clas> classesT = new ArrayList<Clas>();
	
	public List<Method> methodsS = new ArrayList<Method>();
	public List<Method> methodsT = new ArrayList<Method>();
	
	public List<Field> fieldsS = new ArrayList<Field>();
	public List<Field> fieldsT = new ArrayList<Field>();
	
	public String source;
	public String target;
	
	
	
	public void getInfo() {
		List<String> classesName = new ArrayList<String>();
		ComparePrograms.getAllClasses(source, "", classesName);
		
		for (String c : classesName) {
			Clas class_ = new ProgramInfo().getClass_(source+c);
			List<Method> methods = class_.getMethods();
			for (Method method : methods) {
				method.setFullName(class_.getFullName()+"."+method.getName());
				methodsS.add(method);
			}
			List<Field> fields = class_.getFields();
			for (Field field : fields) {
				field.setFullName(class_.getFullName()+"."+field.getName());
				fieldsS.add(field);
			}
			classesS.add(class_);
		}
		classesName = new ArrayList<String>();
		ComparePrograms.getAllClasses(target, "", classesName);
		
		
		for (String c : classesName) {
			Clas class_ = new ProgramInfo().getClass_(target+c);
			List<Method> methods = class_.getMethods();
			for (Method method : methods) {
				method.setFullName(class_.getFullName()+"."+method.getName());
				methodsT.add(method);
			}
			List<Field> fields = class_.getFields();
			for (Field field : fields) {
				field.setFullName(class_.getFullName()+"."+field.getName());
				fieldsT.add(field);
			}
			classesT.add(class_);
		}
	}



	
}
