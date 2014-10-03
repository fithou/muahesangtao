package DataBaseHandler;

<<<<<<< HEAD
import java.io.File;
import java.io.IOException;
import java.net.URI;
=======
import java.io.IOException; 
>>>>>>> origin/master
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Entity.*;
<<<<<<< HEAD
import Globa.GlobaVariables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class TestAdapter {
	protected static final String TAG = "DataAdapter";

	private final Context mContext;
	@SuppressWarnings("unused")
	private SQLiteDatabase mDb;
	private DataBaseHelper mDbHelper;
	private BRDataBaseHelper mBRDbHelper;

	public TestAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
		mBRDbHelper = new BRDataBaseHelper(mContext);
	}

	public TestAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
			mBRDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public TestAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mBRDbHelper.openDataBase();
			mBRDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
		mBRDbHelper.close();
	}

	public List<ParkingLocation> getAllBoomarkParking() {
		List<ParkingLocation> bookmarkParkingList = new ArrayList<ParkingLocation>();
		String selectQuery = "Select * from tbl_parking";
		SQLiteDatabase db = mBRDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Looping through all rows and add to list
		if (cursor.moveToFirst()) {
=======
import android.content.ContentValues;
import android.content.Context; 
import android.database.Cursor; 
import android.database.SQLException; 
import android.database.sqlite.SQLiteDatabase; 
import android.util.Log; 
 
public class TestAdapter  
{ 
    protected static final String TAG = "DataAdapter"; 
 
    private final Context mContext; 
    @SuppressWarnings("unused")
	private SQLiteDatabase mDb; 
    private DataBaseHelper mDbHelper; 
    private BRDataBaseHelper mBRDbHelper;
 
    public TestAdapter(Context context)  
    { 
        this.mContext = context; 
        mDbHelper = new DataBaseHelper(mContext);
        mBRDbHelper = new BRDataBaseHelper(mContext);
    } 
 
    public TestAdapter createDatabase() throws SQLException  
    { 
        try  
        { 
            mDbHelper.createDataBase();
            mBRDbHelper.createDataBase();
        }  
        catch (IOException mIOException)  
        { 
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase"); 
            throw new Error("UnableToCreateDatabase"); 
        } 
        return this; 
    } 
 
    public TestAdapter open() throws SQLException  
    { 
        try  
        { 
            mDbHelper.openDataBase(); 
            mDbHelper.close(); 
            mBRDbHelper.openDataBase();
            mBRDbHelper.close();
            mDb = mDbHelper.getReadableDatabase(); 
        }  
        catch (SQLException mSQLException)  
        { 
            Log.e(TAG, "open >>"+ mSQLException.toString()); 
            throw mSQLException; 
        } 
        return this; 
    } 
 
    public void close()  
    { 
        mDbHelper.close(); 
        mBRDbHelper.close();
    }
   public List<ParkingLocation> getAllBoomarkParking(){
	   List<ParkingLocation> bookmarkParkingList = new ArrayList<ParkingLocation>();
	   String selectQuery = "Select * from tbl_parking";
	   SQLiteDatabase db = mBRDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// Looping through all rows and add to list
		if (cursor.moveToFirst()){
>>>>>>> origin/master
			do {
				ParkingLocation bookmark = new ParkingLocation();
				bookmark.setMa_parking(cursor.getString(0));
				bookmark.setTen_parking(cursor.getString(1));
				bookmark.setDiachi(cursor.getString(2));
				bookmark.setImageUri(cursor.getString(3));
				bookmarkParkingList.add(bookmark);
			} while (cursor.moveToNext());
		}
<<<<<<< HEAD
		return bookmarkParkingList;
	}

	public List<ParkingLocation> getAllParking() {
		List<ParkingLocation> ParkingList = new ArrayList<ParkingLocation>();
		// Select All Query
		String selectQuery = "SELECT * FROM tbl_parking";

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Looping through all rows and add to list
		if (cursor.moveToFirst()) {
=======
	   return bookmarkParkingList;
   }
 
    public List<ParkingLocation> getAllParking(){
		List<ParkingLocation> ParkingList = new ArrayList<ParkingLocation>();
		// Select All Query
		String selectQuery = "SELECT * FROM tbl_parking";
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// Looping through all rows and add to list
		if (cursor.moveToFirst()){
>>>>>>> origin/master
			do {
				ParkingLocation parking = new ParkingLocation();
				parking.setMa_parking(cursor.getString(0));
				parking.setTen_parking(cursor.getString(1));
<<<<<<< HEAD
				parking.setSdt(cursor.getString(2));
				parking.setTong_socho(cursor.getString(3));
				parking.setVitri(cursor.getString(6));
				parking.setLike(Integer.parseInt(cursor.getString(4)));

				parking.setDiachi(cursor.getString(5));
				parking.setImageUri(cursor.getString(7));

=======
				parking.setSdt(cursor.getString(2));				
				parking.setTong_socho(cursor.getString(3));
				parking.setVitri(cursor.getString(6));
				parking.setLike(Integer.parseInt(cursor.getString(4)));
				
				parking.setDiachi(cursor.getString(5));
				parking.setImageUri(cursor.getString(7));
				
>>>>>>> origin/master
				// Adding KH to List
				ParkingList.add(parking);
			} while (cursor.moveToNext());
		}
		return ParkingList;
	}
<<<<<<< HEAD

	public List<String> getPhuong() {
		List<String> PhuongList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT*FROM dm_phuongxa";

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Looping through all rows and add to list
		if (cursor.moveToFirst()) {
			do {
				String value = cursor.getString(0);
				String key = cursor.getString(1);
				// Adding KH to List
				PhuongList.add(key);
=======
    public List<String> getPhuong(){
    	List<String> PhuongList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT*FROM dm_phuongxa";
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// Looping through all rows and add to list
		if (cursor.moveToFirst()){
			do {				
				String value = cursor.getString(0);
				String key = cursor.getString(1);
				// Adding KH to List
				PhuongList.add(key);				
>>>>>>> origin/master
			} while (cursor.moveToNext());
		}
		return PhuongList;
	}
<<<<<<< HEAD

	public List<String> getDuong() {
		List<String> PhuongList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT*FROM dm_duongpho";

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Looping through all rows and add to list
		if (cursor.moveToFirst()) {
			do {
				String value = cursor.getString(0);
				String key = cursor.getString(1);
				// Adding KH to List
				PhuongList.add(key);
=======
    public List<String> getDuong(){
    	List<String> PhuongList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT*FROM dm_duongpho";
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// Looping through all rows and add to list
		if (cursor.moveToFirst()){
			do {				
				String value = cursor.getString(0);
				String key = cursor.getString(1);
				// Adding KH to List
				PhuongList.add(key);				
>>>>>>> origin/master
			} while (cursor.moveToNext());
		}
		return PhuongList;
	}
<<<<<<< HEAD

	public List<String> getQuan() {
		List<String> PhuongList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT*FROM dm_quanhuyen";

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Looping through all rows and add to list
		if (cursor.moveToFirst()) {
			do {
				String value = cursor.getString(0);
				String key = cursor.getString(1);
				// Adding KH to List
				PhuongList.add(key);
=======
    public List<String> getQuan(){
    	List<String> PhuongList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT*FROM dm_quanhuyen";
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// Looping through all rows and add to list
		if (cursor.moveToFirst()){
			do {				
				String value = cursor.getString(0);
				String key = cursor.getString(1);
				// Adding KH to List
				PhuongList.add(key);				
>>>>>>> origin/master
			} while (cursor.moveToNext());
		}
		return PhuongList;
	}
<<<<<<< HEAD

	public List<String> getTinhthanh() {
		List<String> PhuongList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT*FROM dm_tinhthanh";

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Looping through all rows and add to list
		if (cursor.moveToFirst()) {
			do {
				String value = cursor.getString(0);
				String key = cursor.getString(1);
				// Adding KH to List
				PhuongList.add(key);
=======
    public List<String> getTinhthanh(){
    	List<String> PhuongList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT*FROM dm_tinhthanh";
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// Looping through all rows and add to list
		if (cursor.moveToFirst()){
			do {				
				String value = cursor.getString(0);
				String key = cursor.getString(1);
				// Adding KH to List
				PhuongList.add(key);				
>>>>>>> origin/master
			} while (cursor.moveToNext());
		}
		return PhuongList;
	}

<<<<<<< HEAD
	public boolean AddParking(String ten, String sdt, String socho, int like,
			String diachi, String vitri, String url) {
		try {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("ma_parking", vitri);
			cv.put("ten_parking", ten);
			cv.put("tong_socho", socho);
			cv.put("like", like);
			cv.put("sdt", sdt);
			cv.put("vitri", vitri);
			cv.put("img", url);
			String insert = "INSERT INTO tbl_parking VALUES('" + vitri + "','"
					+ ten + "','" + sdt + "'," + socho + "," + like + ",'"
					+ diachi + "','" + vitri + "','" + url + "')";
			Log.d("Insert", insert);
			db.execSQL(insert);
			// db.insert("tbl_parking", null, cv);
			db.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean addBookmark(ParkingLocation p) {
		try {
			String insert = "INSERT INTO tbl_parking VALUES('"
					+ p.getMa_parking() + "','" + p.getTen_parking() + "','"
					+ p.getDiachi() + "','" + p.getImageUri() + "')";
			Log.d("Insert Bookmark", insert);
			SQLiteDatabase db = mBRDbHelper.getWritableDatabase();
			db.execSQL(insert);
			db.close();
=======
    public boolean AddParking(String ten, String sdt, String socho, int like, String diachi, String vitri, String url) {
    	try
 		{
    		SQLiteDatabase db = mDbHelper.getWritableDatabase();    		
 			ContentValues cv = new ContentValues();
 			cv.put("ma_parking", vitri);
 			cv.put("ten_parking", ten); 			
 			cv.put("tong_socho", socho);
 			cv.put("like", like); 			
 			cv.put("sdt", sdt); 			
 			cv.put("vitri", vitri);
 			cv.put("img", url);
 			String insert = "INSERT INTO tbl_parking VALUES('"+ vitri + "','" + ten + "','" + sdt + "'," + socho + "," + like + ",'" + diachi + "','" + vitri + "','" + url +"')";
 			Log.d("Insert", insert);
 			db.execSQL(insert);
 			//db.insert("tbl_parking", null, cv);
 			db.close();
 			return true; 			
 		}
 		catch(Exception ex)
 		{
 			return false;
 		}
    }
    public boolean addBookmark(ParkingLocation p){
    	try {
    		String insert = "INSERT INTO tbl_parking VALUES('"+ p.getMa_parking() + "','" + p.getTen_parking() + "','" + p.getDiachi() + "','" + p.getImageUri() +"')";    		
    		Log.d("Insert Bookmark",insert);
    		SQLiteDatabase db = mBRDbHelper.getWritableDatabase();
    		db.execSQL(insert);
    		db.close();
>>>>>>> origin/master
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
<<<<<<< HEAD
	}

	public boolean addTrangthai(Trangthai p) {
		try {
			String insert = "INSERT INTO tbl_trangthai VALUES('"
					+ p.getMa_parking() + "','" + p.getMa_MAC() + "'," + 1
					+ ")";
			Log.d("Insert Bookmark", insert);
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			db.execSQL(insert);
			db.close();
=======
    }
    public boolean addTrangthai(Trangthai p){
    	try {
    		String insert = "INSERT INTO tbl_trangthai VALUES('"+ p.getMa_parking() + "','" + p.getMa_MAC() + "'," + 1 + ")";    		
    		Log.d("Insert Bookmark",insert);
    		SQLiteDatabase db = mDbHelper.getWritableDatabase();
    		db.execSQL(insert);
    		db.close();
>>>>>>> origin/master
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
<<<<<<< HEAD
	}

	/**
	 * Compose JSON out of SQLite records
	 * 
	 * @return
	 */
	public String composeJSONfromSQLite() {		
		ArrayList<ParkingLocation> wordList;
		wordList = new ArrayList<ParkingLocation>();
		String selectQuery = "SELECT ma_parking, ten_parking, sdt, tong_socho, diachi, like , vitri, img  FROM tbl_parking";
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				ParkingLocation pl = new ParkingLocation();
				pl.setImageUri(cursor.getString(7));				
				pl.setVitri(cursor.getString(6));
				pl.setLike(Integer.parseInt(cursor.getString(5)));
				pl.setTong_socho(cursor.getString(3));
				pl.setDiachi(cursor.getString(4));
				pl.setSdt(cursor.getString(2));
				pl.setTen_parking(cursor.getString(1));
				pl.setMa_parking(cursor.getString(0));
				wordList.add(pl);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// mDb.close();
		Gson gson = new GsonBuilder().create();
		// Use GSON to serialize Array List to JSON
		return gson.toJson(wordList);
	}
	public void uploadAnhToServer(){
		File mediaStorageDir;
		if (Build.VERSION.SDK_INT > 8) {
			mediaStorageDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		} else {
			mediaStorageDir = new File(
					Environment.getExternalStorageDirectory(), "Pictures");
		}
		String selectQuery = "SELECT img  FROM tbl_parking";
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				if (cursor.getString(0) != null){
					imageOnServer.uploadFile(mediaStorageDir + "/" + cursor.getString(0));
					Log.d("Url", mediaStorageDir + "/" + cursor.getString(0));
				}				
			} while (cursor.moveToNext());
		}
	}

	public String composeJSONfromSQLiteStatus() {
		ArrayList<Trangthai> wordList;
		wordList = new ArrayList<Trangthai>();
		String selectQuery = "SELECT * FROM tbl_trangthai";
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Trangthai tt = new Trangthai(cursor.getString(0),
						cursor.getString(1), 1);
				wordList.add(tt);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// mDb.close();
		Gson gson = new GsonBuilder().create();
		// Use GSON to serialize Array List to JSON
		return gson.toJson(wordList);
	}

	public List<ParkingLike> getListParkingLike() {
		List<ParkingLike> lpl = new ArrayList<ParkingLike>();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String query = "Select * from tbl_trangthai";
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				ParkingLike pl = new ParkingLike(cursor.getString(0),
						cursor.getString(1));
				lpl.add(pl);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return lpl;
	}
	public boolean removeParkingLike(String ma_parking) {
		try {
			String delete = "DELETE FROM tbl_trangthai WHERE ma_parking = " + ma_parking;
			Log.d("Delete Bookmark", delete);
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			db.execSQL(delete);
			db.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
    public boolean deleteParking() {
    	try {
	    	String del = "DELETE FROM tbl_parking";
	    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
	    	db.execSQL(del);
	    	db.close();
	    	return true;
    	} catch (Exception e) {
    		return false;
    	}
    }
    public boolean addParkingHistory(ParkingLocation p) {
    	try {
    		String imageName;
    		File file = getFileFromUri(p.getImageUri());
    		if (file != null) {
    			String temp[] = p.getImageUri().split("/");
				imageName = temp[temp.length - 1];
				Log.d("Image name", imageName);
			}else {
				imageName = p.getImageUri();
				Log.d("Image name", imageName);
			}
    		String insert = "INSERT INTO tbl_parkingHistory VALUES('"
					+ p.getMa_parking() + "','" + p.getTen_parking() + "','"
					+ p.getDiachi() + "','" + imageName + "')";
			Log.d("Insert Bookmark", insert);
			SQLiteDatabase db = mBRDbHelper.getWritableDatabase();
			db.execSQL(insert);
			db.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
    }
    public List<ParkingLocation> getAllParkingHistory() {
		List<ParkingLocation> parkingHistoryList = new ArrayList<ParkingLocation>();
		String selectQuery = "Select * from tbl_parkingHistory";
		SQLiteDatabase db = mBRDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Looping through all rows and add to list
		if (cursor.moveToFirst()) {
			do {
				ParkingLocation bookmark = new ParkingLocation();
				bookmark.setMa_parking(cursor.getString(0));
				bookmark.setTen_parking(cursor.getString(1));
				bookmark.setDiachi(cursor.getString(2));
				bookmark.setImageUri(cursor.getString(3));
				parkingHistoryList.add(bookmark);
			} while (cursor.moveToNext());
		}
		return parkingHistoryList;
	}
    public void deleteAllForLocal(String tableName){
    	try {
	    	String del = "DELETE FROM " + tableName;
	    	SQLiteDatabase db = mBRDbHelper.getWritableDatabase();
	    	db.execSQL(del);
	    	db.close();
	    	
    	} catch (Exception e) {
    		
    	}
    }
	private File getFileFromUri(String imgUri) {
		// TODO Auto-generated method stub

		try {
			URI uri = URI.create(imgUri);
			File file = new File(uri);
			if (file != null) {
				if (file.canRead()) {
					return file;
				}
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}

}
=======
    }
    
   /**
	 * Compose JSON out of SQLite records
	 * @return
	 */
   public String composeJSONfromSQLite() {
   	ArrayList<ParkingLocation> wordList;
   	wordList = new ArrayList<ParkingLocation>();
   	String selectQuery = "SELECT ma_parking, ten_parking, sdt, tong_socho, diachi, like , vitri, img  FROM tbl_parking";
   	SQLiteDatabase db = mDbHelper.getReadableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
   	if (cursor.moveToFirst()) {
   		do {
   			ParkingLocation pl = new ParkingLocation();   			
   			pl.setImageUri(cursor.getString(7));
   			pl.setVitri(cursor.getString(6));   			
   			pl.setLike(Integer.parseInt(cursor.getString(5)));
   			pl.setTong_socho(cursor.getString(3));
   			pl.setDiachi(cursor.getString(4));
   			pl.setSdt(cursor.getString(2));
   			pl.setTen_parking(cursor.getString(1));
   			pl.setMa_parking(cursor.getString(0));
   			wordList.add(pl);
   		} while (cursor.moveToNext());
   	}
   	cursor.close();
   	//mDb.close();
   	Gson gson = new GsonBuilder().create();
   	//Use GSON to serialize Array List to JSON
		return gson.toJson(wordList);
   }
   public String composeJSONfromSQLiteStatus() {
	   	ArrayList<Trangthai> wordList;
	   	wordList = new ArrayList<Trangthai>();
	   	String selectQuery = "SELECT * FROM tbl_trangthai";
	   	SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
	   	if (cursor.moveToFirst()) {
	   		do {
	   			Trangthai tt = new Trangthai(cursor.getString(0), cursor.getString(1), 1);
	   			wordList.add(tt);
	   		} while (cursor.moveToNext());
	   	}
	   	cursor.close();
	   	//mDb.close();
	   	Gson gson = new GsonBuilder().create();
	   	//Use GSON to serialize Array List to JSON
			return gson.toJson(wordList);
	   }
     

} 

>>>>>>> origin/master
