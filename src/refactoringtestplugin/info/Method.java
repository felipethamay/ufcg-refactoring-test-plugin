package refactoringtestplugin.info;

import java.util.ArrayList;
import java.util.List;

public class Method {

	List<String> parameters = new ArrayList<String>();
	List<String> modifiers = new ArrayList<String>();
	List<Statement_> statements = new ArrayList<Statement_>();

	String name = "";
	String fullName = "";
	String returnType = "";
	String acc = "";
	String text = "";
	boolean isConstructor = false;
	
	public void print() {
		System.out.println(this.name);
		System.out.println(this.returnType);
		System.out.println(this.parameters);
		System.out.println(this.modifiers);
		System.out.println(this.statements);
	}
	
	public String getDiff(Method m) {
		String diff = "";
		if (!this.getModifiers().equals(m.getModifiers())) {
			diff+= "l1 has a method with modifier "+this.getModifiers().toString()+" and l2 "+m.getModifiers().toString()+"\n";
		}
		if (!this.statements.toString().equals(m.getStatements().toString())) {
			diff+= "l1 has a body with "+this.getStatements().toString()+" and l2 "+m.getStatements().toString()+"\n";
		}
		return diff;
	}
	
	public void addStatement(Statement_ s) {
		statements.add(s);
	}
	public void addParameter(String p) {
		parameters.add(p);
	}
	public void addModifier(String m) {
		modifiers.add(m);
	}
	
	public List<Statement_> getStatements() {
		return statements;
	}

	public void setStatements(List<Statement_> statements) {
		this.statements = statements;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public String getVariableDeclaration() {
		return variableDeclaration;
	}

	public void setVariableDeclaration(String variableDeclaration) {
		this.variableDeclaration = variableDeclaration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
	public String getAcc() {
		return acc;
	}

	public void setAcc(String acc) {
		this.acc = acc;
	}


	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	String variableDeclaration = "";

	public boolean isConstructor() {
		return isConstructor;
	}

	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}
	
	public String toString() {
		return this.getFullName();
	}
	
}
