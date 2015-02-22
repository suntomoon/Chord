import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class Node implements Comparator, Comparable {
	private BigInteger ip;
	private List<FingerTableItem> fingerTable = new ArrayList<FingerTableItem>();
	private List<SongTableItem> songTable = new ArrayList<SongTableItem>();
	
	public Node() {}
	
	public Node(BigInteger ip) {
		this.ip = ip;
	}
	
	public List<FingerTableItem> getFingerTable() {
		return fingerTable;
	}
		
	public List<SongTableItem> getSongTable() {
		return songTable;
	}
	
	public BigInteger getIP() {
		return ip;
	}

	@Override
	public int compare(Object o1, Object o2) {
		return ((BigInteger)o1).compareTo((BigInteger)o2);
	}

	@Override
	public int compareTo(Object o1) {
		return this.ip.compareTo(((Node)o1).getIP());
	}
}
