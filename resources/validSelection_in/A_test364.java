package validSelection_in; 
 
 
public class A_test364 { 
	public int i(){  
		return 0; 
	} 
	public void m(){ 
		/*[*/i(); 
		m();/*]*/ 
	} 
} 
 

