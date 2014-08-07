package Entity;

public class ParkingLocation {
	private String tenbaidoxe;
	private String sodienthoai;
	private String diachi;
	private int sonha;
	private String vitri;
	private int tongsocho;
	public ParkingLocation() {
		super();
	}
	public ParkingLocation(String tenbaidoxe, String sodienthoai,
			String diachi, int sonha, String vitri, int tongsocho) {
		super();
		this.tenbaidoxe = tenbaidoxe;
		this.sodienthoai = sodienthoai;
		this.diachi = diachi;
		this.sonha = sonha;
		this.vitri = vitri;
		this.tongsocho = tongsocho;
	}
	public String getTenbaidoxe() {
		return tenbaidoxe;
	}
	public void setTenbaidoxe(String tenbaidoxe) {
		this.tenbaidoxe = tenbaidoxe;
	}
	public String getSodienthoai() {
		return sodienthoai;
	}
	public void setSodienthoai(String sodienthoai) {
		this.sodienthoai = sodienthoai;
	}
	public String getDiachi() {
		return diachi;
	}
	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}
	public int getSonha() {
		return sonha;
	}
	public void setSonha(int sonha) {
		this.sonha = sonha;
	}
	public String getVitri() {
		return vitri;
	}
	public void setVitri(String vitri) {
		this.vitri = vitri;
	}
	public int getTongsocho() {
		return tongsocho;
	}
	public void setTongsocho(int tongsocho) {
		this.tongsocho = tongsocho;
	}
	
}
