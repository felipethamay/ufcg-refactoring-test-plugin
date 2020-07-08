package refactoringtestplugin.info;

import java.util.List;

public class MustNotDo {
		
	
	
	//se remover retorna true
	public static boolean removeClass(List<Clas> sourceClasses, List<Clas> targetClasses) {
		
		if (sourceClasses.size() != targetClasses.size()) {
			return true;
		}
		
		for (Clas clas : sourceClasses) {
			boolean ok = false;
			for (Clas clas2 : targetClasses) {
				if (clas.getFullName().equals(clas2.getFullName())) {
					ok = true;
					break;
				}
			}
			if (!ok) return true;
		}
		return false;
	}
	
	public static boolean removeField(List<Clas> sourceClasses, List<Clas> targetClasses, Field f, Clas c) {
		
		for (Clas clas : sourceClasses) {
			boolean ok = false;
			for (Clas clas2 : targetClasses) {
				if (clas.getFullName().equals(clas2.getFullName())) {
					List<Field> fields = clas.getFields();
					List<Field> fields2 = clas2.getFields();
					Field removedField = compareFields(fields, fields2);
					if (removedField != null) {
						if (!(removedField.getName().equals(f.getName()) && removedField.getReturnType().equals(f.getReturnType()) &&
								clas.getFullName().equals(c.getFullName()))) {
									ok = true;
									break;
						}
					}
				}
			}
			if (!ok) return true;
		}
		return false;
	}
	
	public static boolean makeClassOrMethodAbstract(List<Clas> sourceClasses, List<Clas> targetClasses) {
		
		for (Clas clas : sourceClasses) {
			for (Clas clas2 : targetClasses) {
				if (clas.getFullName().equals(clas2.getFullName())) {
					if (!isAbstract(clas) && isAbstract(clas2)) {
						return true;
					}
					List<Method> methodsS = clas.getMethods();
					List<Method> methodsT = clas2.getMethods();
					if (makeMethodAbstract(methodsS,methodsT )) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private static boolean makeMethodAbstract(List<Method> methodsS, List<Method> methodsT) {
		for (Method methodS : methodsS) {
			for (Method methodT : methodsT) {
				if (methodS.getName().equals(methodT.getName()) && methodS.getParameters().toString().equals(methodT.getParameters().toString())) {
					if (!isAbstract(methodS) && isAbstract(methodT)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private static boolean isAbstract(Method m) {
		List<String> modifiers = m.getModifiers();
		for (String string : modifiers) {
			if (string.contains("abstract")) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isAbstract(Clas c) {
		List<String> modifiers = c.getModifiers();
		for (String string : modifiers) {
			if (string.contains("abstract")) {
				return true;
			}
		}
		return false;
	}
	
	private static Method compareMethods(List<Method> list1, List<Method> list2) {
		
		for (Method m1 : list1) {
			boolean ok = false;
			for (Method m2 : list2) {
				if (m1.getName().equals(m2.getName()) && m1.getParameters().toString().equals(m2.getParameters().toString())) {
					ok = true;
					break;
				}
			}
			if (!ok) return m1;
		}
		return null;
	}
	
	private static Field compareFields(List<Field> list1, List<Field> list2) {
		
		for (Field f1 : list1) {
			boolean ok = false;
			for (Field f2 : list2) {
				if (f1.getName().equals(f2.getName()) && f1.getReturnType().equals(f2.getReturnType())) {
					ok = true;
					break;
				}
			}
			if (!ok) return f1;
		}
		return null;
	}
	
	
	public static boolean addEntityM(List<Clas> sourceClasses, List<Clas> targetClasses,  String clasName, String method) {
		if (sourceClasses.size() != targetClasses.size()) {
			return true;
		}
		for (Clas clas : targetClasses) {
			boolean ok = false;
			for (Clas clas2 : sourceClasses) {
				if (clas.getFullName().equals(clas2.getFullName())) {
					List<Method> methodsT = clas.getMethods();
					List<Method> methodsS = clas2.getMethods();
					Method addedMethod = compareMethods(methodsT, methodsS);
					if (addedMethod != null) {
						if (!(clasName.equals(clas.getFullName()) && addedMethod.getName().equals(method))) {
							return true;
						}
					}
					ok = true;
					break;
				}
			}
			if (!ok) return true;
		}
		return false;
	}
	
	public static boolean addEntityF(List<Clas> sourceClasses, List<Clas> targetClasses, String clasName, String field) {
		if (sourceClasses.size() != targetClasses.size()) {
			return true;
		}
		
		for (Clas clas : targetClasses) {
			boolean ok = false;
			for (Clas clas2 : sourceClasses) {
				if (clas.getFullName().equals(clas2.getFullName())) {
					List<Field> fieldsS = clas.getFields();
					List<Field> fieldsT = clas2.getFields();
					Field addedField = compareFields(fieldsT, fieldsS);
					if (addedField != null) {
						if (!(clasName.equals(clas.getClassName()) && addedField.getName().equals(field))) {
							return true;
						}
					}
					ok = true;
					break;
				}
			}
			if (!ok) return true;
		}
		return false;
	}
	
	//acc2 < acc1?
	private static boolean decreaseAcc(String acc1, String acc2) {
		
		if (acc1.equals(acc2)) return false;
		if (acc1.equals("public")) return true;
		if (acc1.equals("")) {
			if (!acc2.equals("public")) return true;
		}
		if (acc1.equals("protected")) {
			if (acc2.equals("private")) return true;
		}
		return false;
	}
	
	private static boolean decreaseAccessibilityM(List<Method> sourceMethods, List<Method> targetMethods) {
		for (Method cs : sourceMethods) {
			for (Method ct : targetMethods) {
				if (cs.getName().equals(ct.getName()) && cs.getParameters().toString().equals(ct.getParameters().toString())) {
					if (decreaseAcc(cs.getAcc(), ct.getAcc())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private static boolean decreaseAccessibilityF(List<Field> sourceFields, List<Field> targetFields) {
		for (Field cs : sourceFields) {
			for (Field ct : targetFields) {
				if (cs.getName().equals(ct.getName())) {
					if (decreaseAcc(cs.getAcc(), ct.getAcc())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean decreaseAccessibility(List<Clas> sourceClasses, List<Clas> targetClasses) {
		
		for (Clas cs : sourceClasses) {
			for (Clas ct : targetClasses) {
				if (cs.getFullName().equals(ct.getFullName())) {
					if (decreaseAcc(cs.getAcc(), ct.getAcc())) {
						return true;
					}
					if (decreaseAccessibilityM(cs.getMethods(), ct.getMethods())) {
						return true;
					}
					if (decreaseAccessibilityF(cs.getFields(), ct.getFields())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
}
