package DataBaseHandler;

import java.io.IOException; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Entity.*;
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
 
    public TestAdapter(Context context)  
    { 
        this.mContext = context; 
        mDbHelper = new DataBaseHelper(mContext); 
    } 
 
    public TestAdapter createDatabase() throws SQLException  
    { 
        try  
        { 
            mDbHelper.createDataBase(); 
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
    } 
 
    public List<ParkingLocation> getAllParking(){
		List<ParkingLocation> ParkingList = new ArrayList<ParkingLocation>();
		// Select All Query
		String selectQuery = "SELECT e.ma_parking, e.ten_parking, e.sdt, e.tong_socho, e.vitri,e.like,e.dislike,e.sonha, a.ten_duongpho,b.ten_dmpx,c.ten_dmqh,d.ten_dmtt FROM dm_duongpho a JOIN dm_phuongxa b ON a.ma_dmpx = b.ma_dmpx JOIN dm_quanhuyen c ON b. ma_dmqh = c.ma_dmqh JOIN dm_tinhthanh d ON c.ma_dmtt = d.ma_dmtt JOIN tbl_parking e ON a.ma_duongpho = e.ma_duongpho";
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// Looping through all rows and add to list
		if (cursor.moveToFirst()){
			do {
				ParkingLocation parking = new ParkingLocation();
				parking.setMa_parking(cursor.getString(0));
				parking.setTen_parking(cursor.getString(1));
				parking.setSdt(cursor.getString(2));				
				parking.setTong_socho(cursor.getString(3));
				parking.setVitri(cursor.getString(4));
				parking.setLike(Integer.parseInt(cursor.getString(5)));
				parking.setDislike(Integer.parseInt(cursor.getString(6)));
				parking.setDiachi(cursor.getString(7) + "-" + cursor.getString(8) + "-" + cursor.getString(9) + "-" + cursor.getString(10) + "-" + cursor.getString(11));
				
				// Adding KH to List
				ParkingList.add(parking);
			} while (cursor.moveToNext());
		}
		return ParkingList;
	}
    public HashMap<String, String> getPhuong(){
    	HashMap<String, String> PhuongList = new HashMap<String, String>();
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
				PhuongList.put(key, value);				
			} while (cursor.moveToNext());
		}
		return PhuongList;
	}
    public HashMap<String, String> getDuong(){
    	HashMap<String, String> PhuongList = new HashMap<String, String>();
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
				PhuongList.put(key, value);				
			} while (cursor.moveToNext());
		}
		return PhuongList;
	}
    public HashMap<String, String> getQuan(){
    	HashMap<String, String> PhuongList = new HashMap<String, String>();
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
				PhuongList.put(key, value);				
			} while (cursor.moveToNext());
		}
		return PhuongList;
	}
    public HashMap<String, String> getTinhthanh(){
    	HashMap<String, String> PhuongList = new HashMap<String, String>();
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
				PhuongList.put(key, value);				
			} while (cursor.moveToNext());
		}
		return PhuongList;
	}

    public boolean AddParking(String ten, String sdt, String sonha, String socho, int like, int dis_like, String duongpho, String vitri, String url) {
    	try
 		{
    		String ma_duongpho;
    		String sel_maduongpho = "SELECT ma_duongpho FROM dm_duongpho WHERE ten_duongpho = '" + duongpho + "'";
    		Cursor mCur = mDb.rawQuery(sel_maduongpho, null);
    		if (mCur != null){
    			mCur.moveToNext();
    		}
    		ma_duongpho = mCur.getString(0);
    		
 			ContentValues cv = new ContentValues();
 			cv.put("ma_parking", vitri);
 			cv.put("ten_parking", ten);
 			cv.put("sonha", sonha);
 			cv.put("tong_socho", socho);
 			cv.put("like", like);
 			cv.put("dislike", dis_like);
 			cv.put("ma_duongpho", ma_duongpho);
 			cv.put("vitri", vitri);
 			cv.put("img", url);
 			
 			mDb.insert("tbl_parking", null, cv);
 			return true;
 			
 		}
 		catch(Exception ex)
 		{
 			return false;
 		}
    } 
     
     

} 

