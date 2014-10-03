package Entity;

public class ParkingLike {
	String maParking;
	String maMac;
	public String getMaParking() {
		return maParking;
	}
	public void setMaParking(String maParking) {
		this.maParking = maParking;
	}
	public String getMaMac() {
		return maMac;
	}
	public void setMaMac(String maMac) {
		this.maMac = maMac;
	}
	public ParkingLike(String maParking, String maMac) {
		super();
		this.maParking = maParking;
		this.maMac = maMac;
	}
	
}
