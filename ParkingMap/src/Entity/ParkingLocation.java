package Entity;

import java.util.ArrayList;

import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ParkingLocation {
	private String ma_parking;
	private String ten_parking;
	private String sdt;
	private String diachi;
	private String tong_socho;
	private int like;	
	private String imageUri;
	
	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

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

	public ParkingLocation(String ma_parking, String ten_parking, String sdt,
			String diachi, String tong_socho, int like,
			String imageUri, String vitri) {
		super();
		this.ma_parking = ma_parking;
		this.ten_parking = ten_parking;
		this.sdt = sdt;
		this.diachi = diachi;
		this.tong_socho = tong_socho;
		this.like = like;		
		this.imageUri = imageUri;
		this.vitri = vitri;
	}


}
