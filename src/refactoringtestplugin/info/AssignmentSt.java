package refactoringtestplugin.info;

public class AssignmentSt extends Statement_{
	
	String leftHandSinde;
	String rightHandSinde;
	String operator;
	
	
	
	public AssignmentSt(String exp, String leftHandSinde, String rightHandSinde, String operator ) {
		super(exp);
		this.leftHandSinde = leftHandSinde;
		this.rightHandSinde = rightHandSinde;
		this.operator = operator;
		this.expression = exp;
	}
	
	public String getLeftHandSinde() {
		return leftHandSinde;
	}
	public void setLeftHandSinde(String leftHandSinde) {
		this.leftHandSinde = leftHandSinde;
	}
	public String getRightHandSinde() {
		return rightHandSinde;
	}
	public void setRightHandSinde(String rightHandSinde) {
		this.rightHandSinde = rightHandSinde;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
}
