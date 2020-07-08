package refactoringtestplugin.info;


public class Field {
	
	String name = "";
	String fullName = "";
	String returnType = "";
	String modifiers = "";
	String text = "";
	String acc = "";
	
	
public String getDiff(Field f) {
	String diff = "";
	if (!this.returnType.equals(f.getReturnType())) {
		diff+="l1 has a field with type "+this.returnType+" and l2 "+f.getReturnType()+"\n";
	}
	if (!this.modifiers.equals(f.getModifiers())) {
		diff+="l1 has a field with modifier "+this.modifiers+" and l2 "+f.getModifiers()+"\n";
	}
	return diff;
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
	public String getModifiers() {
		return modifiers;
	}
	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
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

	public String toString() {
		return this.getFullName();
	}
	
}
