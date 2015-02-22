import java.math.BigInteger;


public class FingerTableItem {
	private int index;
	private BigInteger jThSuccessor;
	private BigInteger successor;
	
	public FingerTableItem() {}
	
	public FingerTableItem(int index, BigInteger jThSuccessor, BigInteger successor) {
		this.index = index;
		this.jThSuccessor = jThSuccessor;
		this.successor = successor;
	}
	
	public int getIndex() {
		return index;
	}
	
	public BigInteger getJThSuccessor() {
		return jThSuccessor;
	}
	
	public BigInteger getSuccessor() {
		return successor;
	}
	
	public void setSuccessor(BigInteger successor) {
		this.successor = successor;
	}
}
