package Entity;

public class ParkingLocation {
	private String ma_parking;
	private String ten_parking;
	private String sdt;
	private String diachi;
	private String tong_socho;
	private int like;
	private int dislike;
	
	private String vitri;
	
	public ParkingLocation(){
		
	}

	public String getMa_parking() {
		return ma_parking;
	}

	public void setMa_parking(String ma_parking) {
		this.ma_parking = ma_parking;
	}

	public String getTen_parking() {
		return ten_parking;
	}

	public void setTen_parking(String ten_parking) {
		this.ten_parking = ten_parking;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}
	
	public String getTong_socho() {
		return tong_socho;
	}

	public void setTong_socho(String tong_socho) {
		this.tong_socho = tong_socho;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public int getDislike() {
		return dislike;
	}

	public void setDislike(int dislike) {
		this.dislike = dislike;
	}

	public String getVitri() {
		return vitri;
	}

	public void setVitri(String vitri) {
		this.vitri = vitri;
	}

	public String getDiachi() {
		return diachi;
	}

	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}
}
