package refactoringtestplugin.info;

import java.util.ArrayList;
import java.util.List;

public class Clas {
	
	String className = "";
	String superClass = "";
	String packageName = "";
	String acc = "";
	List<Method> methods = new ArrayList<Method>();
	List<Field> fields = new ArrayList<Field>();
	List<String> imports = new ArrayList<String>();
	List<String> modifiers = new ArrayList<String>();
	

	List<String> methodsString = new ArrayList<String>();
	List<String> fieldsString = new ArrayList<String>();
	String isAbstract = null;
	
	
	public boolean isAbstract() {
		if (isAbstract == null) {
			for (String mod : modifiers) {
				if (mod.contains("abstract")) {
					isAbstract = "true";
				}
			}
			if (isAbstract == null) {
				isAbstract = "false";
			}
		} 
		return isAbstract.equals("true");
		
	}
	
	public void addMethod(Method method) {
		methods.add(method);
	}
	
	public String getFullName() {
		return this.packageName+"."+this.className;
	}
	
	public void addModifier(String mod) {
		modifiers.add(mod);
	}
	
	public void addField(Field field) {
		fields.add(field);
	}
	
	public void addMethodString(String method) {
		methodsString.add(method);
	}
	
	public void addFieldString(String field) {
		fieldsString.add(field);
	}
	
	public void addImport(String import_) {
		imports.add(import_);
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSuperClass() {
		return superClass;
	}
	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public List<Method> getMethods() {
		return methods;
	}
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	public List<String> getImports() {
		return imports;
	}
	public void setImports(List<String> imports) {
		this.imports = imports;
	}
	
	public boolean compareLists(List<String> l1, List<String> l2) {
		
		if (l1.size() != l2.size()) return false;
		for (String string : l1) {
			if (!l2.contains(string)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean compareListsMethod(List<Method> l1, List<Method> l2) {
		
		if (l1.size() != l2.size()) return false;
		for (Method m1 : l1) {
			boolean ok = false;
			for (Method m2 : l2) {
				if (m1.getText().equals(m2.getText())) {
					ok = true;
				}
			}
			if (!ok) return false;
		}
		return true;
	}
	
	public boolean compareListsField(List<Field> l1, List<Field> l2) {
		
		if (l1.size() != l2.size()) return false;
		for (Field f1 : l1) {
			boolean ok = false;
			for (Field f2 : l2) {
				if (f1.getText().equals(f2.getText())) {
					ok = true;
				}
			}
			if (!ok) return false;
		}
		return true;
	}
	
	public String getDiff(List<String> l1, List<String> l2) {
		
		String diff = "";
		String diff2 = "";
		for (String string : l1) {
			if (!l2.contains(string)) {
				diff+= "l1 has an import that l2 does not have\n";
				diff2+= " "+string;
			}
		}
		
		for (String string : l2) {
			if (!l1.contains(string)) {
				diff+= "l2 has an import that l1 does not have\n";
				diff2+= " "+string;
			}
		}
		return diff;
	}
	
	public String getDiffMethods(List<Method> l1, List<Method> l2) {
		
		String diff = "";
		
		Method mDiff = null; 
		
		for (Method m1 : l1) {
			boolean ok = false;
			for (Method m2 : l2) {
				if (m1.getName().equals(m2.getName()) && m1.getParameters().toString().equals(m2.getParameters().toString())) {
					ok = true;
					diff += m1.getDiff(m2);
				}
			}
			if (!ok) {
				diff+= "l1 has a method that l2 does not have"+"\n";
//				diff+=m1.getText();
			}
		}
		
		for (Method m2 : l2) {
			boolean ok = false;
			for (Method m1 : l1) {
				if (m1.getName().equals(m2.getName()) && m1.getParameters().toString().equals(m2.getParameters().toString())) {
					ok = true;
				}
			}
			if (!ok) {
				diff+= "l2 has a method that l1 does not have"+"\n";
//				diff+=m2.getText();
			}
		}
		return diff;
	}
	
	public String getDiffModifiers(List<String> l1, List<String> l2) {
		String diff = "";
		String diff2 = "";
		for (String string : l1) {
			if (!l2.contains(string)) {
				diff+= "l1 has a modifier "+string+" that l2 does not have\n";
				diff2+= " "+string;
			}
		}
		
		for (String string : l2) {
			if (!l1.contains(string)) {
				diff+=  "l2 has a modifier "+string+" that l1 does not have\n";
				diff2+= " "+string;
			}
		}
		return diff;
	}
	
	
	public String getDiffFields(List<Field> l1, List<Field> l2) {
		
		String diff = "";
		for (Field f1 : l1) {
			boolean ok = false;
			for (Field f2 : l2) {
				if (f1.getName().equals(f2.getName())) {
					diff+=f1.getDiff(f2);
					ok = true;
				}
			}
			if (!ok) {
				diff+= "l1 has a field that l2 does not have\n"+"\n";
			}
		}
		
		for (Field f2 : l2) {
			boolean ok = false;
			for (Field f1 : l1) {
				if (f1.getName().equals(f2.getName())) {
					ok = true;
				}
			}
			if (!ok) {
				diff+= "l2 has a field that l1 does not have"+"\n";
			}
		}
		return diff;
	}
	
	public String getIndividualDiffs(List<String> l1, List<String> l2) {
		
		String diff = "";
		for (String string : l1) {
			if (!l2.contains(string)) {
				diff+= " "+string;
			}
		}
		
		for (String string : l2) {
			if (!l1.contains(string)) {
				
				diff+= " "+string;
			}
		}
		return diff;
	}
	
	public String getDiff(Clas c) {
		
		String diff = "";
		String diff2 = "";
		
		if (!c.getClassName().equals(this.className)) diff2+= " 1 "+c.getClassName() + "2 "+this.className;
		if (!c.getPackageName().equals(this.packageName)) {
			diff+= "change package\n";
			diff2+= " 1 "+c.getPackageName() + "2 "+this.packageName;
		}
		if (!c.getSuperClass().equals(this.superClass)) {
			diff+= "change super class\n";
			diff2+= " 1 "+c.getSuperClass() + "2 "+this.superClass;
		}
		diff+= getDiffMethods(c.getMethods(), this.methods);
		diff+= getDiffFields(c.getFields(), this.fields);
		diff+= getDiff(c.getImports(), this.imports);
		diff+= getDiffModifiers(c.getModifiers(), this.modifiers);
		
		diff2+= getDiff(c.getMethodsString(), this.methodsString);
		diff2+= getDiff(c.getFieldsString(), this.fieldsString);
		diff2+= getDiff(c.getImports(), this.imports);
		
		return diff;
	}
	
	public boolean equals(Clas c) {
		if (!c.getClassName().equals(this.className)) return false;
		if (!c.getPackageName().equals(this.packageName)) return false;
		if (!c.getSuperClass().equals(this.superClass)) return false;
		if (!compareListsMethod(c.getMethods(), this.methods)) return false;
		if (!compareListsField(c.getFields(), this.fields)) return false;
		if (!compareLists(c.getImports(), this.imports)) return false;
		if (!compareLists(c.getModifiers(), this.modifiers)) return false;
		return true;
	}

	public List<String> getMethodsString() {
		return methodsString;
	}

	public void setMethodsString(List<String> methodsString) {
		this.methodsString = methodsString;
	}

	public List<String> getFieldsString() {
		return fieldsString;
	}

	public void setFieldsString(List<String> fieldsString) {
		this.fieldsString = fieldsString;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public String getAcc() {
		return acc;
	}

	public void setAcc(String acc) {
		this.acc = acc;
	}
	
	public String toString() {
		return this.getFullName();
	}
}
