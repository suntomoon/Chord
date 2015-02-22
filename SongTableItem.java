import java.math.BigInteger;


public class SongTableItem {
	private String title;
	private BigInteger ip;
	
	public SongTableItem() {}
	
	public SongTableItem(String title, BigInteger ip) {
		this.title = title;
		this.ip = ip;
	}
	
	public String getTitle() {
		return title;
	}
	
	public BigInteger getIP() {
		return ip;
	}
}
