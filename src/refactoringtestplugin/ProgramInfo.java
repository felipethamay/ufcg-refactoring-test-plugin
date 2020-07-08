package refactoringtestplugin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import io.InputManager;
import io.InputManagerASCII;
import refactoringtestplugin.info.Clas;
import refactoringtestplugin.info.Field;
import refactoringtestplugin.info.Method;
import refactoringtestplugin.info.Statement_;

public class ProgramInfo {

	public Clas getClass_(String path) {
		 ASTParser parser = ASTParser.newParser(AST.JLS3);  // handles JDK 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6
//		 parser.setSource(source);

		 StringBuffer read = null;
		try {
			read = read(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 parser.setSource(read.toString().toCharArray());
		 // In order to parse 1.5 code, some compiler options need to be set to 1.5
		 Map options = JavaCore.getOptions();
//		 JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
		 parser.setCompilerOptions(options);
//		 CompilationUnit result = (CompilationUnit) parser.createAST(null);
		 
		 parser.setKind(ASTParser.K_COMPILATION_UNIT);
		 final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		 
		 final Clas c = new Clas();
		 
		 
		 cu.accept(new ASTVisitor() {
		         //by add more visit method like the following below, then all king of statement can be visited.
			 public boolean visit(TypeDeclaration td) {
				 td.accept(new ASTVisitor() {
			         //by add more visit method like the following below, then all king of statement can be visited.
					 	public boolean visit(MethodDeclaration md) {
				 		final Method m = new Method();
				 		m.setName(md.getName().toString());
				 		m.setReturnType(md.getReturnType2().toString());
				 		m.setText(md.toString());
				 		c.addMethodString(md.toString());
//				 		System.out.println(md.toString());
//				 		c.addMethod(md.toString());
				 		 md.accept(new ASTVisitor() {
					         //by add more visit method like the following below, then all king of statement can be visited.
					 	public boolean visit(MethodDeclaration node) {
//					 		System.out.println(node.toString());
					 		List parameters = node.parameters();
					 		for (Object md : parameters) {
								m.addParameter(md.toString());
							}
					 		List<Object> modifiers = node.modifiers();
					 		for (Object bd : modifiers) {
								m.addModifier(bd.toString());
								if (bd.toString().contains("abstract")) {
									System.out.println("m "+bd.toString());
								}
//								
							}
					 		Block body = node.getBody();
					 		if (body == null) return false;
					 		List<Statement> statements = body.statements();
					 		
					 		for (Statement statement : statements) {
								statement.accept(new ASTVisitor() {
									public boolean visit(VariableDeclarationStatement es) {
										m.addStatement(new Statement_(es.toString()));
//										m.addStatement("int a = 2");
										return false;
									}
						 			public boolean visit(ReturnStatement es) {
						 				Expression expression = es.getExpression();
						 				expression.accept(new ASTVisitor() {
						 					public boolean visit(NumberLiteral e) {
						 						m.addStatement(new Statement_(e.toString()));
//						 						m.addStatement("literal");
						 						return false;
						 					}
						 					public boolean visit(SuperMethodInvocation e) {
						 						m.addStatement(new Statement_(e.toString()));
//						 						m.addStatement("super.m");
						 						return false;
						 					}
						 					public boolean visit(SuperFieldAccess e) {
						 						m.addStatement(new Statement_(e.toString()));
//						 						m.addStatement("super.f");
						 						return false;
						 					}
						 					public boolean visit(SimpleName e) {
						 						m.addStatement(new Statement_(e.toString()));
//						 						m.addStatement("f");
						 						return false;
						 					}
						 					public boolean visit(MethodInvocation e) {
						 						Expression e2 = e.getExpression();
						 						if (e2 != null) {
							 						e2.accept(new ASTVisitor() {
							 							public boolean visit(ThisExpression e3) {
							 								if (e3.getQualifier() != null) {
//							 									String className = e3.getQualifier().getFullyQualifiedName().toString();
//							 									if (className.contains(".")) {
//							 										m.addStatement("p.A.this.m");
//							 									} else {
//							 										m.addStatement("A.this.m");
//							 									}
							 									m.addStatement(new Statement_(e3.toString()));
							 								} else {
							 									m.addStatement(new Statement_(e3.toString()));
//							 									m.addStatement("this.m");
							 								}
							 								return false;
							 							}
							 							public boolean visit(ClassInstanceCreation e3) {
							 								String className = e3.getType().toString();
						 									if (className.contains(".")) {
						 										m.addStatement(new Statement_(e3.toString()));
//						 										m.addStatement("new p.A().m");
						 									} else {
						 										m.addStatement(new Statement_(e3.toString()));
//						 										m.addStatement("new A().m");
						 									}
							 								
							 								// aqui pode ser new A().m() ou new p1.A.m()
							 								return false;
							 							}
							 							public boolean visit(SimpleName e3) {
							 								m.addStatement(new Statement_(e3.toString()));
//							 								m.addStatement("f.m");
							 								return false;
							 							}
							 							public boolean visit(SuperFieldAccess e3) {
							 								m.addStatement(new Statement_(e3.toString()));
//							 								m.addStatement("super.f.m");
							 								return false;
							 							}
							 							public boolean visit(FieldAccess e3) {
							 								Expression e = e3.getExpression();
							 							
								 								e3.accept(new ASTVisitor() {
								 									public boolean visit(ThisExpression e4) {
										 								if (e4.getQualifier() != null) {
										 									String className = e4.getQualifier().getFullyQualifiedName().toString();
										 									m.addStatement(new Statement_(e4.toString()));
//										 									if (className.contains(".")) {
//										 										m.addStatement("p.A.this.f.m");
//										 									} else {
//										 										m.addStatement("A.this.f.m");
//										 									}
										 								} else {
										 									m.addStatement(new Statement_(e4.toString()));
//										 									m.addStatement("this.f.m");
										 								}
										 								return false;
										 							}
										 							public boolean visit(ClassInstanceCreation e4) {
										 								m.addStatement(new Statement_(e4.toString()));
//										 								String className = e4.getType().toString();
//									 									if (className.contains(".")) {
//									 										m.addStatement("new p.A().f.m");
//									 									} else {
//									 										m.addStatement("new A().f.m");
//									 									}
										 								
										 								return false;
										 							}
								 								});
							 								
							 								return false;
							 							}
							 						});
						 						} else {
						 							m.addStatement(new Statement_(e.toString()));
//						 							m.addStatement("m");
						 						}
						 						return false;
						 					}
						 					
						 				});
						 				return false;
						 			}
						 			
						 		});
								
							}
					 		
					 		
					 		return false;
					 	}
				 		 });
				 		c.addMethod(m);
//				 		m.print();
				 	
				 		return false;
				 	}
				 });
				return false;
				 
			 }
			public boolean visit(FieldDeclaration node) {
				Field f = new Field();
				f.setName(node.fragments().toString());
				f.setReturnType(node.getType().toString());
				f.setModifiers(node.modifiers().toString());
				f.setText(node.toString());
				c.addFieldString(node.toString());
				c.addField(f);		 		
		 		return false;
		 	}
			
			public boolean visit(PackageDeclaration node) {
//		 		System.out.println(node.toString());
		 		c.setPackageName(node.toString());
		 		return false;
		 	}
			
		 	public boolean visit(ImportDeclaration node) {
//		 		System.out.println(node.toString());
		 		c.addImport(node.toString());
		 		return false;
		 	}
		 	});
		 
		 cu.accept(new ASTVisitor() {

	 	public boolean visit(TypeDeclaration node) {
	 		c.setClassName(node.getName().toString());
	 		Type superclassType = node.getSuperclassType();
	 		if (superclassType != null)
	 			c.setSuperClass(node.getSuperclassType().toString());
	 		List modifiers = node.modifiers();
	 		for (Object bd : modifiers) {
				c.addModifier(bd.toString());
				if (bd.toString().contains("abstract")) {
					System.out.println("c "+bd.toString());
				}
//				System.out.println(bd.toString());
			}
	 		return false;
	 	}
	 	});
		 return c;
	}
	
	
//	public Method getMethod(String method) {
//		
//		 ASTParser parser = ASTParser.newParser(AST.JLS3);  // handles JDK 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6
////		 parser.setSource(source);
//
//
//		 parser.setSource(method.toCharArray());
//		 // In order to parse 1.5 code, some compiler options need to be set to 1.5
//		 Map options = JavaCore.getOptions();
////		 JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
//		 parser.setCompilerOptions(options);
////		 CompilationUnit result = (CompilationUnit) parser.createAST(null);
//		 
//		 parser.setKind(ASTParser.K_COMPILATION_UNIT);
//		 final MethodDeclaration md = (MethodDeclaration) parser.createAST(null);
//		 
//		 final Method m = new Method();
//		 
//		 
//		 md.accept(new ASTVisitor() {
//		         //by add more visit method like the following below, then all king of statement can be visited.
//		 	public boolean visit(MethodDeclaration node) {
////		 		System.out.println(node.toString());
//		 		List parameters = node.parameters();
//		 		List modifiers = node.modifiers();
//		 		Block body = node.getBody();
//		 		List<Statement> statements = body.statements();
//		 		
//		 		for (Statement statement : statements) {
//					statement.accept(new ASTVisitor() {
//			 			public boolean visit(ExpressionStatement es) {
//			 				Expression expression = es.getExpression();
//			 				expression.accept(new ASTVisitor() {
//			 					public boolean visit(ThisExpression e) {
//			 						Name qualifier = e.getQualifier();
//			 						System.out.println(qualifier.toString());
//			 						return false;
//			 					}
//			 					public boolean visit(SuperMethodInvocation e) {
//			 						System.out.println(e.getName().toString());
//			 						return false;
//			 					}
//			 					public boolean visit(MethodInvocation e) {
//			 						System.out.println(e.getName().toString());
//			 						return false;
//			 					}
//			 					public boolean visit(SuperFieldAccess e) {
//			 						System.out.println(e.getName().toString());
//			 						return false;
//			 					}
//			 					public boolean visit(VariableDeclarationExpression e) {
//			 						System.out.println(e.fragments().toString());
//			 						return false;
//			 					}
//			 					public boolean visit(Assignment e) {
//			 						System.out.println();
//			 						return false;
//			 					}
//			 					public boolean visit(ClassInstanceCreation e) {
//			 						System.out.println();
//			 						return false;
//			 					}	
//			 					public boolean visit(FieldAccess e) {
//			 						System.out.println();
//			 						return false;
//			 					}
//			 					
//			 				});
//			 				return false;
//			 			}
//			 			
//			 		});
//				}
//		 		
////		 		c.addMethod(node.toString());
//		 		
//		 		
//		 		return false;
//		 	}
//
////			public boolean visit(FieldDeclaration node) {
//////		 		System.out.println(node.toString());
////				
////				c.addField(node.toString());		 		
////		 		return false;
////		 	}
////			
////			public boolean visit(PackageDeclaration node) {
//////		 		System.out.println(node.toString());
////		 		c.setPackageName(node.toString());
////		 		return false;
////		 	}
////			
////		 	public boolean visit(ImportDeclaration node) {
//////		 		System.out.println(node.toString());
////		 		c.addImport(node.toString());
////		 		return false;
////		 	}
//		 	});
//		 
////		 cu.accept(new ASTVisitor() {
////
////	 	public boolean visit(TypeDeclaration node) {
////	 		c.setClassName(node.getName().toString());
////	 		Type superclassType = node.getSuperclassType();
////	 		if (superclassType != null)
////	 			c.setSuperClass(node.getSuperclassType().toString());
////	 		return false;
////	 	}
////	 	});
//		 return m;
//	}
	public boolean compare(String path1, String path2)  {
		
		StringBuffer b1 = null;
		StringBuffer b2 = null;
		
		try {
			b1 = read(path1);
			b2 = read(path2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b1.toString().equals(b2.toString());
	}
	
	public StringBuffer read(String path) throws IOException {
		InputManager file = new InputManagerASCII(path);
		StringBuffer b = new StringBuffer();
		file.openFile();
		while (!file.isEndOfFile()) {
			String line = file.readLine();
//			line = trim(line);
			b.append(line);
		}
		file.closeFile();
		return b;
	}
	
	public String trim(String string) {
		String result = ""; 
		char[] charArray = string.toCharArray();
		for (char c : charArray) {
			if (c != ' ' && c != '\t') {
				result += c;
			}
		}
		return result;
		
	}
	
	public static void main(String[] args) {
		String path1 = "C:/Users/Felipe/Documents/workspace6/testS/src/p1/BB.java";
		String path2 = "C:/Users/Felipe/Documents/workspace6/testT/src/p1/BB.java";
//		System.out.println(new ComparePrograms().compare(path1, path2));
		
//		String method = " public long m_0(){return this.methodid_1(2);}";
//		new ProgramInfo().getMethod(method);
		
		path1 = "/home/spg-experiment-3/Documents/jrrt/pushdownmethod/skip1/test18/out/jrrt/Package_0/ClassId_1.java";
//		path1 = "/home/spg-experiment-3/Documents/jrrt/movemethod/skip1/test616/out/jrrt/Package_1/A_0.java";
//		path1 = "/home/spg-experiment-3/Documents/eclipse/movemethod/skip1/test8807/out/eclipse/Package_1/ClassId_1.java";
		path1 = "/home/spg-experiment-3/Documents/jrrt/pushdownmethod/skip1/test14497/out/jrrt/Package_1/ClassId_2.java";
		path1 = "/home/spg-experiment-3/Documents/workspace/asource/src/asource/A.java";
//		path1 = "/home/spg-experiment-3/Documents/workspace/atarget/src/atarget/A.java";
		Clas c1 = new ProgramInfo().getClass_(path1);
//		Clas c2 = new ProgramInfo().getClass_(path2);
//		
//		System.out.println(c1.equals(c2));
		
		
	}
}
