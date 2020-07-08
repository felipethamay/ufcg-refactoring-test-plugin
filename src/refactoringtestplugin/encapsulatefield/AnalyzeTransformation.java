package refactoringtestplugin.encapsulatefield;

import java.util.HashMap;
import java.util.List;

import refactoringtestplugin.info.AnalyzeTransformationInfo;
import refactoringtestplugin.info.AssignmentSt;
import refactoringtestplugin.info.Clas;
import refactoringtestplugin.info.Field;
import refactoringtestplugin.info.Method;
import refactoringtestplugin.info.ReturnSt;
import refactoringtestplugin.info.Statement_;

public class AnalyzeTransformation extends AnalyzeTransformationInfo{
	
	String fieldName;
	String clasName;
	String clasName2;
	Field fSource;
	Field fTarget;
	Clas cSource;
	Clas cTarget;
	String getMethodName = "";
	String setMethodName = "";
	Method getMethod;
	Method setMethod;
	StringBuffer problem = new StringBuffer();
	
	public  AnalyzeTransformation() {
		
	}
	
	public boolean isTransformationBug(String source, String target, String fieldName, String clasName, String clasName2) {
		
		boolean isTransformationBug = false;
		
		this.source = source;
		this.target = target;
		this.fieldName = fieldName;
		this.clasName = clasName;
		this.clasName2 = clasName2;
		String firstLetter = fieldName.substring(0,1);
		String nextLetters = "";
		if (fieldName.length()>1) {
			nextLetters = fieldName.substring(1);
		}
		this.getMethodName = "get"+firstLetter.toUpperCase()+nextLetters;
		this.setMethodName = "set"+firstLetter.toUpperCase()+nextLetters;
		getInfo();
		
		getInfoSource();
		getInfoTarget();
		
		//as classes tem que ser iguais
		if (!cSource.getFullName().equals(cTarget.getFullName())) {
			problem.append("field em classes diferentes\n");
			isTransformationBug = true;
		}
		
		//o field é privado?
		if (!isNonPrivateField()) {
			problem.append("field privado\n");
			isTransformationBug = true;
		}
		
		//o field no target é ficou privado?
		if (!theFieldBecamePrivate()) {
			problem.append("field no target nao ficou privado\n");
			isTransformationBug = true;
		}
		//criou os metodos get/set? 
		if (!createGetSetMethods()) {
			problem.append("nao criou o get ou set\n");
			isTransformationBug = true;
		}
		
		if (this.getMethod != null && this.setMethod != null) {
			//checar o formato getMethod
			String checkGetMethod = checkGetMethod();
			if (!checkGetMethod.equals("ok")) {
				problem.append("formato get method errado\n");
				problem.append(checkGetMethod+"\n");
				isTransformationBug = true;
			}
			
			
			//checar o formato setMethod
			String checkSetMethod = checkSetMethod();
			if (!checkSetMethod.equals("ok")) {
				problem.append("formato set method errado\n");
				problem.append(checkSetMethod+"\n");
				isTransformationBug = true;
			}
			
			
			//atualizou todas as referencias ao field?
			if (!updateAllReferences()) {
				problem.append("referencias nao atualizadas corretamente\n");
				isTransformationBug = true;
			}
		}
		return isTransformationBug;
	
	}
	
	public boolean isNonPrivateField() {
		return !this.fSource.getAcc().equals("private");
	}
	
	
	public boolean theFieldBecamePrivate() {
		return this.fTarget.getAcc().equals("private");
	}
	
	private HashMap<Method,Method> getAllMethodsThatCallField() {
		HashMap<Method,Method> list = new HashMap<Method,Method>();
			for (Method method : methodsS) {
				List<Statement_> statements = method.getStatements();
				for (Statement_ st : statements) {
					if (st.getExpression().contains(this.fieldName)) {
						Method mt = null;
						for (Method methodT : methodsT) {
							if (method.getFullName().equals(methodT.getFullName())) {
								mt = methodT;
								break;
							}
						}
						list.put(method, mt);
					}
				}
			}
		return list;
	}
	
	
	
	public boolean updateAllReferences() {
		
		HashMap<Method, Method> methodsThatCallField = getAllMethodsThatCallField();
		
		for (Method sm : methodsThatCallField.keySet()) {
			Method tm = methodsThatCallField.get(sm);
			if (tm != null) {
				List<Statement_> statementsS = sm.getStatements();
				Statement_ stS = null;
				for (Statement_ st : statementsS) {
					if (st.getExpression().contains(this.fieldName)) {
						stS = st;
						break;
					}
				}
				List<Statement_> statementsT = tm.getStatements();
				for (Statement_ statement_ : statementsT) {
					if (statement_.getExpression().contains(this.fieldName)) {
						if (stS instanceof ReturnSt) {
							String expressionT = statement_.getExpression();
							String expressionS = stS.getExpression();
							if (!expressionT.equals(expressionS.replace(this.fieldName, this.getMethodName+"()"))) {
								return false;
							}
						} else if (stS instanceof AssignmentSt) {
							String expressionT = statement_.getExpression();
							String expressionS = stS.getExpression();
							String leftHandSinde = ((AssignmentSt) stS).getLeftHandSinde();
							String rightHandSinde = ((AssignmentSt) stS).getRightHandSinde();
							String operator = ((AssignmentSt) stS).getOperator();
							if (leftHandSinde.contains(this.fieldName)) {
								if (!expressionT.equals(this.setMethodName+"("+rightHandSinde+")")) {
									return false;
								}
							} else if (rightHandSinde.contains(this.fieldName)) {
								if (!expressionT.equals(expressionS.replace(this.fieldName, this.getMethodName+"()"))) {
									return false;
								}
							}
							
						}
					}
					
				}
			} else {
				return false;
			}
		}
		return true;
		
	}
	
	
	public String checkGetMethod() {
		
		List<Statement_> statements = this.getMethod.getStatements();
		if (statements.size() == 1) {
			Statement_ st = statements.get(0);
			if (st.getExpression().equals(this.fieldName) || st.getExpression().equals("this."+this.fieldName) ) {
				return "ok";
			}
		}
		String sts = "";
		for (Statement_ statement_ : getMethod.getStatements()) {
			sts+=statement_.getExpression();
		}
		return sts;
	}
	
	public String checkSetMethod() {
		
		List<Statement_> statements = this.setMethod.getStatements();
		if (statements.size() == 1) {
			Statement_ st = statements.get(0);
			if (st.getExpression().equals("this."+this.fieldName+"="+this.fieldName)) {
				return "ok";
			}
		}
		String sts = "";
		for (Statement_ statement_ : setMethod.getStatements()) {
			sts+=statement_.getExpression();
		}
		return sts;
	}
	
	public boolean createGetSetMethods() {
		for (Clas clas : classesT) {
			if (clas.getClassName().equals(this.clasName)) {
				Method getMethod = getMethod(clas.getMethods(), this.getMethodName);
				if (getMethod != null) {
					if (getMethod.getParameters().size() == 0) {
						this.getMethod = getMethod;
					}
				} else {
					return false;
				}
				Method setMethod = getMethod(clas.getMethods(), this.setMethodName);
				if (setMethod != null) {
					String parameters = setMethod.getParameters().toString();
					parameters = parameters.replace("[", "");
					parameters = parameters.replace("]", "");
					if (parameters.equals(this.fTarget.getReturnType())) {
						this.setMethod = setMethod;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	public Method getMethod(List<Method> list, String methodName) {
		for (Method method : list) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}
	
	public boolean containsMethod(List<Method> list, String methodName) {
		for (Method method : list) {
			if (method.getName().equals(methodName)) {
				return true;
			}
		}
		return false;
	}
	
//	public Clas getClass(String fullName, List<Clas> list) {
//		for (Clas clas : list) {
//			if (clas.getFullName().equals(fullName)) {
//				return clas;
//			}
//		}
//		return null;
//	}
	
	public void getInfoSource() {
		boolean ok = false;
		for (Clas clas : classesS) {
			List<Field> fields = clas.getFields();
			for (Field field : fields) {
				if (field.getName().equals(fieldName) && clas.getClassName().contains(this.clasName)) {
					this.fSource = field;
					this.cSource = clas;
					ok = true;
					return;
				}
			}
		}
		if (!ok) {
			for (Clas clas : classesS) {
				List<Field> fields = clas.getFields();
				for (Field field : fields) {
					if (field.getName().equals(fieldName) && clas.getClassName().contains(this.clasName2)) {
						this.clasName = clasName2;
						this.fSource = field;
						this.cSource = clas;
						return;
					}
				}
			}
		}
	}
	
	public void getInfoTarget() {
		for (Clas clas : classesT) {
			List<Field> fields = clas.getFields();
			for (Field field : fields) {
				if (field.getName().equals(fieldName) && clas.getClassName().contains(this.clasName)) {
					this.fTarget = field;
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
		AnalyzeTransformation a = new AnalyzeTransformation();
		System.out.println(a.isTransformationBug(source, target, "f", "A","B"));
		System.out.println(a.getProblem());
	}

}
