package refactoringtestplugin.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.Field;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;

public class ExtractJavaCodeToyExample {

	// private static final String ALLOY_MODULE_NAME = "javametamodel";

	public ExtractJavaCodeToyExample(A4Solution ans) {
		this.ans = ans;
	}

	private static AST ast = AST.newAST(AST.JLS3);

	private A4Solution ans;

	public String getJavaCode() {
		
		StringBuffer sb = new StringBuffer();
		CompilationUnit compilationUnit = ast.newCompilationUnit();
		for (String c : getNodes()) {

			TypeDeclaration typeDeclaration = getClass(c);
			sb.append(typeDeclaration.toString());
			sb.append("\n");

		}
		// adiciona inner
		// for (CompilationUnit cu : result) {

		// }
		return sb.toString();
	}

	private ImportDeclaration importPacakge(Name packageToCompare) {
		ImportDeclaration importDeclaration = ast.newImportDeclaration();
		importDeclaration.setName(ast.newSimpleName(packageToCompare
				.getFullyQualifiedName()));
		importDeclaration.setOnDemand(true);
		return importDeclaration;
	}

	private List<String> getNodes() {
		List<String> result = new ArrayList<String>();

		Sig sig = getSig("Node");
		List<String> classInstances = extractInstances(ans.eval(sig).toString());
		result.addAll(classInstances);

		return result;
	}

	private TypeDeclaration getClass(String classId) {
		TypeDeclaration result = ast.newTypeDeclaration();
		result.setName(ast.newSimpleName(classId));

		Sig classSig = getSig("Node");
		SafeList<Field> classFields = classSig.getFields();
		Field classSuperRelations = getField("supertypes", classFields);
		Map<String, List<String>> classSuperRel = getRelations(classSuperRelations);

		Field isClassRels = getField("isClass", classFields);
		Map<String, List<String>> isClassRel = getRelations(isClassRels);

		// eh classe?
		String isClass = isClassRel.get(classId).get(0);
		if (isClass.equals("boolean/True_0"))
			result.setInterface(false);
		else
			result.setInterface(true);

		// result.setName(ast.newSimpleName(className));

		// herança
		List<String> superClass = classSuperRel.get(classId);
		if (superClass != null) {
			if (result.isInterface()) {
				for (String node : superClass)
					result.superInterfaceTypes().add(
							ast.newSimpleType(ast.newSimpleName(node)));
			} else {
				for (String node : superClass) {
					isClass = isClassRel.get(node).get(0);
					if (isClass.equals("boolean/True_0"))
						result.setSuperclassType(ast.newSimpleType(ast
								.newSimpleName(node)));
					else
						result.superInterfaceTypes().add(
								ast.newSimpleType(ast.newSimpleName(node)));
				}
			}
			
		}
		//
		// // atributos
		// List<FieldDeclaration> fields = getClassFields(classFields, classId);
		// for (FieldDeclaration fieldDeclaration : fields) {
		// result.bodyDeclarations().add(fieldDeclaration);
		// }
		//
		// // methods
		// List<MethodDeclaration> methods = getClassMethods(classFields,
		// classId);
		// for (MethodDeclaration methodDeclaration : methods) {
		// result.bodyDeclarations().add(methodDeclaration);
		// }
		//
		// // innerClass
		// // List<TypeDeclaration> classes = getInnerClasses(classFields,
		// // classId);
//		System.out.println(result.toString());
		return result;
	}

	private List<TypeDeclaration> getInnerClasses(SafeList<Field> classFields,
			String classId) {

		List<TypeDeclaration> result = new ArrayList<TypeDeclaration>();
		Field innerRel = getField("inner", classFields);

		return null;
	}

	private List<MethodDeclaration> getClassMethods(
			SafeList<Field> classFields, String classId) {
		List<MethodDeclaration> result = new ArrayList<MethodDeclaration>();

		Field methodsRel = getField("methods", classFields);

		Map<String, List<String>> methodsMap = getRelations(methodsRel);

		List<String> methods = methodsMap.get(classId);

		if (methods != null) {
			Sig methodSig = getSig("Method");
			SafeList<Field> mFields = methodSig.getFields();
			Field mIdRelations = getField("id", mFields);
			Map<String, List<String>> idRel = getRelations(mIdRelations);
			Field mArgRelations = getField("param", mFields);
			Map<String, List<String>> argRel = getRelations(mArgRelations);
			Field mVisRelations = getField("acc", mFields);
			Map<String, List<String>> visRel = getRelations(mVisRelations);
			Field mBodyRelations = getField("b", mFields);

			Map<String, List<String>> bodyRel = getRelations(mBodyRelations);

			for (String method : methods) {
				String id = idRel.get(method).get(0);
				String arg = "";
				if (argRel.containsKey(method))
					arg = argRel.get(method).get(0);

				String vis = "";
				if (visRel.containsKey(method))
					vis = visRel.get(method).get(0);

				MethodDeclaration methodDeclaration = ast
						.newMethodDeclaration();
				methodDeclaration.setName(ast.newSimpleName(id.toLowerCase()));
				Modifier m = getAccessModifier(vis);
				if (m != null)
					methodDeclaration.modifiers().add(m);

				if (arg.length() > 0) {
					SingleVariableDeclaration parameter = ast
							.newSingleVariableDeclaration();
					parameter.setName(ast.newSimpleName("a"));
					parameter.setType(getType(arg));
					methodDeclaration.parameters().add(parameter);
				}

				// TODO: consertar para pegar de alloy
				methodDeclaration.setReturnType2(ast
						.newPrimitiveType(PrimitiveType.VOID));

				// returnStatement.setExpression(methodBody);
				methodDeclaration.setBody(ast.newBlock());

				List<String> listOfBodies = bodyRel.get(method);

				String body = listOfBodies.get(0);
				// ReturnStatement returnStatement =
				// ast.newReturnStatement();
				Expression methodBody = getMethodBody(body, classId);

				if (methodBody != null) {
					ExpressionStatement newExpressionStatement = ast
							.newExpressionStatement(methodBody);
					methodDeclaration.getBody().statements()
							.add(newExpressionStatement);
				}

				result.add(methodDeclaration);
			}
		}

		return result;
	}

	private Expression getMethodBody(String bodyId, String classId) {

		// String instanceName = bodyId.replaceAll("_[0-9]", "");

		Sig s = null;
		SafeList<Field> sFields = null;

		// pega o nome da classe que contem o metodo para usar no qualified this
		Sig classSig = getSig("Class");
		SafeList<Field> classFields = classSig.getFields();
		Field classIdRelations = getField("id", classFields);
		Map<String, List<String>> classIdRel = getRelations(classIdRelations);

		if (bodyId.startsWith("MethodInvocation")) {
			s = getSig("MethodInvocation");
			sFields = s.getFields();
			Field idRelations = getField("rMethod", sFields);
			// pega o metodo
			String method = getFieldInstance(idRelations, bodyId);
			// pega o id do metodo
			Sig sMethod = getSig("Method");
			SafeList<Field> mFields = sMethod.getFields();
			Field midRelations = getField("id", mFields);
			String methodId = getFieldInstance(midRelations, method);
			// pega o parametro
			Field paramRelations = getField("param", mFields);
			String param = getFieldInstance(paramRelations, method);

			Field qualRelations = getField("q", sFields);
			String qualifier = getFieldInstance(qualRelations, bodyId);
			String className = classIdRel.get(classId).get(0);
			return getMethodInvocationExpression(methodId, param, qualifier,
					className);

		} else if (bodyId.startsWith("ConstructorMethodInvocation")) {
			s = getSig("ConstructorMethodInvocation");
			sFields = s.getFields();
			Field mRelations = getField("refer", sFields);
			// pega o metodo
			String method = getFieldInstance(mRelations, bodyId);
			// pega o id do metodo
			Sig sMethod = getSig("Method");
			SafeList<Field> mFields = sMethod.getFields();
			Field midRelations = getField("id", mFields);
			String methodId = getFieldInstance(midRelations, method);
			// pega o parametro
			Field paramRelations = getField("param", mFields);
			String param = getFieldInstance(paramRelations, method);
			Field idClassRelations = getField("idClass", sFields);
			String className = getFieldInstance(idClassRelations, bodyId);
			return getConstructorMethodInvocationExpression(methodId, param,
					className);
		} else if (bodyId.startsWith("FieldInvocation")) {
			s = getSig("FieldInvocation");
			sFields = s.getFields();
			Field idRelations = getField("id", sFields);
			String fieldId = getFieldInstance(idRelations, bodyId);
			Field qualRelations = getField("q", sFields);
			String qualifier = getFieldInstance(qualRelations, bodyId);
			String className = classIdRel.get(classId).get(0);
			return getFieldInvocationExpression(fieldId, qualifier, className);
		} else if (bodyId.startsWith("ConstructorFieldInvocation")) {
			s = getSig("ConstructorFieldInvocation");
			sFields = s.getFields();
			Field idRelations = getField("idField", sFields);
			String fieldId = getFieldInstance(idRelations, bodyId);
			Field idClassRelations = getField("idClass", sFields);
			String className = getFieldInstance(idClassRelations, bodyId);
			return getConstructorFieldInvocationExpression(fieldId, className);
		} else if (bodyId.startsWith("LiteralValue")) {
			// System.out.println(bodyId);
			// Random generator = new Random();
			// int value = generator.nextInt(100);
			String value = bodyId.toString();
			value = value.replaceAll("(.)*_", "");
			return ast.newNumberLiteral(value);
		} else {
			return null;
		}

	}

	// TODO: errado
	private boolean isInstanceOfType(String instance, String type) {
		Sig s = getSig(type);
		if (s == null)
			return false;

		SafeList<Field> sFields = s.getFields();
		Field idRelations = getField("id", sFields);
		String methodId = getFieldInstance(idRelations, instance);
		if (!methodId.equals(""))
			return true;
		return false;
	}

	private Expression getConstructorMethodInvocationExpression(
			String methodId, String param, String classId) {
		Expression result = null;
		ClassInstanceCreation instExpr = ast.newClassInstanceCreation();
		instExpr.setType(ast.newSimpleType(ast.newSimpleName(classId)));

		// Expression parameter = createParameter(methodId);

		Expression parameter = null;
		if (param != null && param.equals("Int__0") || param.equals("Long__0")) {
			parameter = ast.newNumberLiteral("2");
		} else if (param != null && param.length() > 0)
			parameter = ast.newNullLiteral();

		SimpleName methodName = ast.newSimpleName(methodId.toLowerCase());

		MethodInvocation methInv = ast.newMethodInvocation();
		methInv.setName(methodName);
		if (parameter != null)
			methInv.arguments().add(parameter);

		methInv.setExpression(instExpr);

		result = methInv;

		return result;
	}

	private Expression getConstructorFieldInvocationExpression(String fieldId,
			String classId) {
		Expression result = null;

		ClassInstanceCreation instExpr = ast.newClassInstanceCreation();
		instExpr.setType(ast.newSimpleType(ast.newSimpleName(classId)));
		FieldAccess fieldAcc = ast.newFieldAccess();
		fieldAcc.setExpression(instExpr);
		fieldAcc.setName(ast.newSimpleName(fieldId.toLowerCase()));
		// result = fieldAcc;
		// parar comparar com o astgen
		Assignment fieldAssignment = ast.newAssignment();
		fieldAssignment.setLeftHandSide(fieldAcc);
		fieldAssignment.setRightHandSide(ast.newNumberLiteral(String
				.valueOf("0")));
		result = fieldAssignment;
		return result;
	}

	private String getFieldInstance(Field f, String key) {
		String result = "";

		Map<String, List<String>> idRel = getRelations(f);
		if (idRel.size() > 0 && idRel.containsKey(key))

			result = idRel.get(key).get(0);

		return result;
	}

	private Expression getMethodInvocationExpression(String methodId,
			String param, String qualifier, String classId) {
		Expression result = null;

		// Expression parameter = createParameter(methodId);
		Expression parameter = null;
		if (param != null && param.equals("Int__0") || param.equals("Long__0")) {
			parameter = ast.newNumberLiteral("2");
		} else if (param != null && param.length() > 0)
			parameter = ast.newNullLiteral();

		SimpleName methodName = ast.newSimpleName(methodId.toLowerCase());

		if (qualifier.equals("super__0")) {
			SuperMethodInvocation superMethInv = ast.newSuperMethodInvocation();
			superMethInv.setName(methodName);
			if (parameter != null)
				superMethInv.arguments().add(parameter);
			result = superMethInv;
		} else {
			MethodInvocation methInv = ast.newMethodInvocation();
			methInv.setName(methodName);
			if (parameter != null)
				methInv.arguments().add(parameter);
			if (qualifier.equals("this__0")) {
				ThisExpression thisExpression = ast.newThisExpression();
				// radom
				// o this pode ser qualificado ou nao
				// Random generator = new Random();
				// int value = generator.nextInt(2);
				// if (value == 1)
				// thisExpression.setQualifier(ast.newSimpleName(classId));
				methInv.setExpression(thisExpression);
			}
			if (qualifier.equals("qthis__0")) {
				ThisExpression thisExpression = ast.newThisExpression();
				thisExpression.setQualifier(ast.newSimpleName(classId));
				methInv.setExpression(thisExpression);
			}
			result = methInv;
		}

		return result;
	}

	private Expression createParameter(String methodId) {
		// pegar parametros dos metodo
		Sig methodSig = getSig("Method");
		SafeList<Field> mFields = methodSig.getFields();
		Field mIdRelations = getField("id", mFields);
		Map<String, List<String>> idRel = getRelations(mIdRelations);
		Field mArgRelations = getField("param", mFields);
		Map<String, List<String>> argRel = getRelations(mArgRelations);

		String parameterType = "";
		for (String s : idRel.keySet()) {
			if (idRel.get(s).size() > 0 && idRel.get(s).get(0).equals(methodId)) {
				if (argRel.containsKey(s)) {
					parameterType = argRel.get(s).get(0);
					break;
				}

			}
		}

		Expression parameter = null;
		if (parameterType.equals("Int__0") || parameterType.equals("Long__0")) {
			parameter = ast.newNumberLiteral("2");
		} else if (parameterType.length() > 0)
			parameter = ast.newNullLiteral();
		return parameter;
	}

	private Expression getFieldInvocationExpression(String fieldId,
			String qualifier, String classId) {

		Expression result = null;

		SimpleName fieldName = ast.newSimpleName(fieldId.toLowerCase());
		System.out.println(fieldName);
		// result = fieldName;
		Assignment fieldAssignment = ast.newAssignment();
		fieldAssignment.setLeftHandSide(fieldName);
		fieldAssignment.setRightHandSide(ast.newNumberLiteral(String
				.valueOf("0")));
		result = fieldAssignment;

		// comaprar com astgen

		if (qualifier.equals("super__0")) {
			SuperFieldAccess superFieldAcc = ast.newSuperFieldAccess();
			superFieldAcc.setName(fieldName);
			// result = superFieldAcc;
			// parar comparar com o astgen
			fieldAssignment = ast.newAssignment();
			fieldAssignment.setLeftHandSide(superFieldAcc);
			fieldAssignment.setRightHandSide(ast.newNumberLiteral(String
					.valueOf("0")));
			result = fieldAssignment;
		} else if (qualifier.equals("this__0") || qualifier.equals("qthis__0")) {
			FieldAccess fieldAcc = ast.newFieldAccess();
			fieldAcc.setName(ast.newSimpleName(fieldId.toLowerCase()));
			if (qualifier.equals("this__0")) {
				ThisExpression thisExpression = ast.newThisExpression();
				fieldAcc.setExpression(thisExpression);
			}
			if (qualifier.equals("qthis__0")) {
				ThisExpression thisExpression = ast.newThisExpression();
				thisExpression.setQualifier(ast.newSimpleName(classId));
				fieldAcc.setExpression(thisExpression);
			}

			// parar comparar com o astgen
			fieldAssignment = ast.newAssignment();
			fieldAssignment.setLeftHandSide(fieldAcc);
			fieldAssignment.setRightHandSide(ast.newNumberLiteral(String
					.valueOf("0")));
			result = fieldAssignment;
		}

		return result;
	}

	private List<FieldDeclaration> getClassFields(SafeList<Field> classFields,
			String td) {
		List<FieldDeclaration> result = new ArrayList<FieldDeclaration>();

		Field fieldsRel = getField("fields", classFields);

		Map<String, List<String>> fieldsMap = getRelations(fieldsRel);

		List<String> fields = fieldsMap.get(td);

		if (fields != null) {
			Sig fieldSig = getSig("Field");
			SafeList<Field> fFields = fieldSig.getFields();
			Field fIdRelations = getField("id", fFields);
			Map<String, List<String>> idRel = getRelations(fIdRelations);
			Field fTypeRelations = getField("type", fFields);
			Map<String, List<String>> typeRel = getRelations(fTypeRelations);
			Field fVisRelations = getField("acc", fFields);
			Map<String, List<String>> visRel = getRelations(fVisRelations);

			for (String field : fields) {
				String id = idRel.get(field).get(0);
				String type = typeRel.get(field).get(0);

				String vis = "";
				if (visRel.containsKey(field))
					vis = visRel.get(field).get(0);

				VariableDeclarationFragment fieldId = ast
						.newVariableDeclarationFragment();
				fieldId.setName(ast.newSimpleName(id.toLowerCase()));

				FieldDeclaration fieldDeclaration = ast
						.newFieldDeclaration(fieldId);

				Modifier m = getAccessModifier(vis);
				if (m != null)
					fieldDeclaration.modifiers().add(m);

				Type t = getType(type);
				fieldDeclaration.setType(t);

				// inicializa o field
				VariableDeclarationFragment variable = (VariableDeclarationFragment) fieldDeclaration
						.fragments().get(0);
				Expression inializer;
				if (type.equals("Int__0") || type.equals("Long__0")) {
					// remover random
					Random generator = new Random();
					int value = generator.nextInt(100);
					inializer = ast.newNumberLiteral(String.valueOf(value));
				} else
					inializer = ast.newNullLiteral();
				// remover
				// variable.setInitializer(inializer);

				result.add(fieldDeclaration);
			}
		}

		return result;
	}

	private Type getType(String type) {
		Type result = null;

		// SimpleType newSimpleType = ast.newSimpleType(ast
		// .newName("String");

		if (type.equals("Long__0"))
			result = ast.newPrimitiveType(PrimitiveType.LONG);
		else if (type.equals("Int__0"))
			result = ast.newPrimitiveType(PrimitiveType.INT);
		else if (type.equals("Float__0"))
			result = ast.newPrimitiveType(PrimitiveType.FLOAT);
		else if (type.equals("Boolean__0"))
			result = ast.newPrimitiveType(PrimitiveType.BOOLEAN);
		else if (type.equals("Void__0"))
			result = ast.newPrimitiveType(PrimitiveType.VOID);

		else {
			Sig classSig = getSig("Class");
			SafeList<Field> classFields = classSig.getFields();
			Field classIdRelations = getField("id", classFields);
			Map<String, List<String>> classRel = getRelations(classIdRelations);
			List<String> id = classRel.get(type);
			if (id != null) {
				result = ast.newSimpleType(ast.newName(id.get(0)));
			}
		}
		return result;
	}

	private Modifier getAccessModifier(String vis) {
		Modifier result = null;

		if (vis.equals("private__0"))
			result = ast.newModifier(Modifier.ModifierKeyword.PRIVATE_KEYWORD);
		else if (vis.equals("protected_0"))
			result = ast
					.newModifier(Modifier.ModifierKeyword.PROTECTED_KEYWORD);
		else if (vis.equals("public_0"))
			result = ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD);
		return result;
	}

	private List<ImportDeclaration> getImports(String classId) {
		List<ImportDeclaration> result = new ArrayList<ImportDeclaration>();

		Sig cuSig = getSig("Class");
		SafeList<Field> cuFields = cuSig.getFields();
		Field packageRelations = getField("imports", cuFields);
		Map<String, List<String>> r = getRelations(packageRelations);
		List<String> imported = r.get(classId);

		if (imported != null) {
			for (String importedPackage : imported) {
				ImportDeclaration importDeclaration = ast
						.newImportDeclaration();
				importDeclaration.setName(ast.newSimpleName(importedPackage));
				importDeclaration.setOnDemand(true);
				result.add(importDeclaration);
			}
		}

		return result;
	}

	private PackageDeclaration getPackage(String classId) {
		PackageDeclaration result = ast.newPackageDeclaration();

		Sig classSig = getSig("Class");
		SafeList<Field> cFields = classSig.getFields();
		Field packageRelations = getField("package", cFields);
		Map<String, List<String>> r = getRelations(packageRelations);

		String packageName = r.get(classId).get(0);

		result.setName(ast.newSimpleName(packageName));

		return result;
	}

	// private List<String> getCompilationUnits() {
	// List<String> result = new ArrayList<String>();
	//
	// Sig sig = getSig("CompilationUnit");
	// List<String> cuInstances = extractInstances(ans.eval(sig).toString());
	// result.addAll(cuInstances);
	//
	// return result;
	// }

	private Field getField(String key, SafeList<Field> cuFields) {
		Field result = null;

		for (Field field : cuFields) {
			if (field.toString().contains(key)) {
				result = field;
				break;
			}

		}

		return result;
	}

	private Map<String, List<String>> getRelations(Field f) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		if (f == null)
			return result;

		String relations = ans.eval(f).toString();
		relations = cleanName(relations);

		if (relations.length() > 0) {
			String[] arrayRelation = relations.split(",");

			for (String relation : arrayRelation) {
				String[] r = relation.split("->");
				r[0] = r[0].replaceAll("javametamodel(.)*/", "");
				r[1] = r[1].replaceAll("javametamodel(.)*/", "");

				if (!result.containsKey(r[0])) {
					ArrayList<String> values = new ArrayList<String>();

					values.add(r[1]);
					result.put(r[0], values);
				} else {
					result.get(r[0]).add(r[1]);
				}
			}

		}

		return result;
	}

	private Sig getSig(String s) {
		Sig result = null;

		SafeList<Sig> sigs = ans.getAllReachableSigs();

		for (Sig sig : sigs) {
			String sigName = removeCrap(sig.toString());
			if (sigName.equals(s)) {
				result = sig;
				break;
			}
		}
		return result;
	}

	private String removeCrap(String instance) {
		instance = instance.replaceAll("[^/]*/", "");
		return instance;
	}

	private List<String> extractInstances(String labels) {
		List<String> result = new ArrayList<String>();

		String instances = cleanName(labels);

		if (instances.length() > 0) {

			String[] types = instances.split(",");
			char empty = ' ';
			for (String typeName : types) {
				if (typeName.charAt(0) == empty)
					typeName = typeName.substring(1);
				typeName = typeName.replaceAll("javametamodel(.)*/", "");
				result.add(typeName);

			}
		}
		return result;
	}

	public String cleanName(String name) {
		String removeBraces = name.substring(1, name.length() - 1);
		String replaceDollar = removeBraces.replace("$", "_");
		String removeSpaces = replaceDollar.replaceAll(" ", "");
		return removeSpaces;
	}

}