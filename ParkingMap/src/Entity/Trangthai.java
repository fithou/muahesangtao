package Entity;

public class Trangthai {
	String ma_parking;
	String ma_MAC;
	int trangthai;
	
	public String getMa_parking() {
		return ma_parking;
	}
	public void setMa_parking(String ma_parking) {
		this.ma_parking = ma_parking;
	}
	public String getMa_MAC() {
		return ma_MAC;
	}
	public void setMa_MAC(String ma_MAC) {
		this.ma_MAC = ma_MAC;
	}
	public int getTrangthai() {
		return trangthai;
	}
	public void setTrangthai(int trangthai) {
		this.trangthai = trangthai;
	}
	public Trangthai(String ma_parking, String ma_MAC, int trangthai) {
		super();
		this.ma_parking = ma_parking;
		this.ma_MAC = ma_MAC;
		this.trangthai = trangthai;
	}
	public Trangthai() {
		super();
	}
	
	
}
