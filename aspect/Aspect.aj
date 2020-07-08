import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public aspect Aspect {
	
	private File logResult = new File("C:/Users/Felipe/Documents/resultados/aspectS.txt");
	private File logResultCoverage = new File("C:/Users/Felipe/Documents/resultados/aspectCoverage.txt");
	private PrintStream ps;
	private PrintStream psCoverage;
	private BufferedReader input;
	private ArrayList impactedMethods = new ArrayList();
	private ArrayList coveredImpactedMethods = new ArrayList();
	private List affectedTests = new ArrayList();
	private int test = 0;
	private final String RANDOOP = "Randoop";
	
	public Aspect()  {
		try {
			ps =  new PrintStream(logResult);
			psCoverage =  new PrintStream(logResultCoverage);
			System.setOut(ps);
			System.setErr(psCoverage);
			input   = new BufferedReader( new FileReader("C:/Users/Felipe/Documents/resultados/impact.txt") );
			while (input.ready()) {
//				System.out.println(input.readLine());
				impactedMethods.add(input.readLine());
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	pointcut adviceCall () :  call (* *.*(..))  && !within(Aspect);
	
	pointcut adviceExecution () :  execution (* *.*(..))  && !within(Aspect);
	//&& !within... (colocar classes do aspectos) - pode ser o problema de modificar o target
	
	pointcut adviceC () : execution (*.new(..)) && !within(Aspect) ;
	
	
	pointcut adviceC2 () : execution (*.*.new(..)) && !within(Aspect) ;
	
//	
//
	before() : adviceC() {
		
		String name = thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
//		System.out.println("after "+name);
		if (!name.contains(RANDOOP)) {
			
			String methodName = thisJoinPointStaticPart.getSignature().getName() ;
			String className =  thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
			Method[] methods = thisJoinPointStaticPart.getSignature().getDeclaringType().getMethods();
			analyzeMethod(methodName,className, methods);
			analyzeMethodForCoverage(methodName,className, methods);
		}
	}
	
	before() : adviceC2() {
		
		String name = thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
//		System.out.println("after "+name);
		if (!name.contains(RANDOOP)) {
			
			String methodName = thisJoinPointStaticPart.getSignature().getName() ;
			String className =  thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
			Method[] methods = thisJoinPointStaticPart.getSignature().getDeclaringType().getMethods();
			analyzeMethod(methodName,className, methods);
			analyzeMethodForCoverage(methodName,className, methods);
		}
	}
	
	before() : adviceExecution() {
		String name = thisJoinPointStaticPart.getSignature().toString();
//		System.out.println("before "+name);
		if (name.contains(RANDOOP)) {
				test++;
		}
		
		
	}
	
	after() : adviceExecution() {
		String name = thisJoinPointStaticPart.getSignature().toString();
//		System.out.println("after "+name);
		if (name.contains(RANDOOP)) {
			analyzeTest(); 
		} 
			else {
			
			String methodName = thisJoinPointStaticPart.getSignature().getName() ;
			String className =  thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
			Method[] methods = thisJoinPointStaticPart.getSignature().getDeclaringType().getMethods();
			
			analyzeMethod(methodName,className, methods);
			analyzeMethodForCoverage(methodName,className, methods);
		}
	}
	after() : adviceCall() {
		
		String name = thisJoinPointStaticPart.getSignature().toString();
////		System.out.println("after "+name);
//		if (name.contains(RANDOOP)) {
//			analyzeTest(); 
//		} else {
			
			String methodName = thisJoinPointStaticPart.getSignature().getName() ;
			String className =  thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
			Method[] methods = thisJoinPointStaticPart.getSignature().getDeclaringType().getMethods();
			
//			analyzeMethod(methodName,className, methods);
			analyzeMethodForCoverage(methodName,className, methods);
//		}
	}
	
	private String getMethodFullName(String methodName, String className, Method[] methods) {
		
		boolean containsMethod = false;
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (m.getName().equals(methodName)) {
				containsMethod = true;
				methodName += "(";
				Class[] parameterTypes = m.getParameterTypes();
				for (int j = 0; j < parameterTypes.length; j++) {
					methodName+= parameterTypes[j].getName()+",";
				}
				break;
			}
		}
		if (containsMethod) {
			if (methodName.endsWith(",")) {
				methodName = methodName.substring(0, methodName.length()-1);
			}
		} else {
			methodName += "(";
		}
		methodName += ")";
		
		return className+"."+methodName;
	}
	
	private void analyzeMethodForCoverage(String methodName, String className, Method[] methods) {
		
		methodName = getMethodFullName(methodName, className, methods);
//		System.out.println(methodName);
		
		if (!coveredImpactedMethods.contains(methodName)) {
			int size = impactedMethods.size();
			for (int i = 0; i < size; i++) {
				String im = (String)impactedMethods.get(i);
				if (im.contains(methodName)) {
					coveredImpactedMethods.add(methodName);
					System.err.println("covered impacted methods: "+methodName);
					break;
				}
			}
		}
		
	}
	
	private void analyzeMethod(String methodName, String className, Method[] methods) {
		
		
		
		methodName = getMethodFullName(methodName, className, methods);
		
		if (!affectedTests.contains(""+test)) {
			int size = impactedMethods.size();
			for (int i = 0; i < size; i++) {
				String im = (String)impactedMethods.get(i);
				if (im.contains(methodName)) {
//					System.out.println("metodo impactado: "+className+"."+methodName);
					affectedTests.add(""+test);
					break;
				}
			}
		}
		
	}
	
	
	public void analyzeTest() {
		System.out.println("testes afetados: "+affectedTests.size());
		String t = "";
		int size = affectedTests.size();
		for (int i = 0; i < size; i++) {
			t += (String)affectedTests.get(i)+" ";
		}
		System.out.println(t);
		System.err.println("coverage "+impactedMethods.size()+" "+coveredImpactedMethods.size()+" "+test);
//		System.out.println("coverage "+impactedMethods.size()+" "+coveredImpactedMethods.size());
//		System.out.println(test);
	}

}
