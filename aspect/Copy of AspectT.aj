import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public aspect AspectT {
	
	private File logResult = new File("C:/Users/Felipe/Documents/resultados/aspectT.txt");
	private PrintStream ps;
	private BufferedReader input;
	private ArrayList impactedMethods = new ArrayList();
	private List affectedTests = new ArrayList();
	private int test = 0;
	private final String RANDOOP = "Randoop";
	
	public AspectT()  {
		try {
			ps =  new PrintStream(logResult);
			System.setOut(ps);
			System.setErr(ps);
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
	
	
	pointcut advice () : execution (* *.*(..)) && !within(AspectT);
	//&& !within... (colocar classes do aspectos) - pode ser o problema de modificar o target
	
	pointcut adviceC () : execution (*.new(..)) && !within(AspectT) ;
	
	pointcut adviceC2 () : execution (*.*.new(..)) && !within(AspectT) ;
	
//	
//
	after() : adviceC() {
		
		String name = thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
		
		if (!name.contains(RANDOOP)) {
			
			String methodName = thisJoinPointStaticPart.getSignature().getName() ;
			String className =  thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
			Method[] methods = thisJoinPointStaticPart.getSignature().getDeclaringType().getMethods();
			analyzeMethod(methodName,className, methods);
		}
	}
	
	after() : adviceC2() {
		
		String name = thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
		
		if (!name.contains(RANDOOP)) {
			
			String methodName = thisJoinPointStaticPart.getSignature().getName() ;
			String className =  thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
			Method[] methods = thisJoinPointStaticPart.getSignature().getDeclaringType().getMethods();
			analyzeMethod(methodName,className, methods);
		}
	}
	
	before() : advice() {
		String name = thisJoinPointStaticPart.getSignature().toString();
//		System.out.println(name);
		if (name.contains(RANDOOP)) {
				test++;
		}
		
		
	}

	after() : advice() {
		
		String name = thisJoinPointStaticPart.getSignature().toString();
//		System.out.println(name);
		if (name.contains(RANDOOP)) {
			analyzeTest(); 
		} else {
			
			String methodName = thisJoinPointStaticPart.getSignature().getName() ;
			String className =  thisJoinPointStaticPart.getSignature().getDeclaringTypeName();
			Method[] methods = thisJoinPointStaticPart.getSignature().getDeclaringType().getMethods();
			
			analyzeMethod(methodName,className, methods);
		}
	}
	
	private void analyzeMethod(String methodName, String className, Method[] methods) {
		
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
		
		
		if (!affectedTests.contains(""+test)) {
			int size = impactedMethods.size();
			for (int i = 0; i < size; i++) {
				String im = (String)impactedMethods.get(i);
				if (im.contains(className+"."+methodName)) {
//					System.out.println(className+"."+methodName);
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
	}

}
