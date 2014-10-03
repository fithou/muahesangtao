package DataBaseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BRDataBaseHelper extends SQLiteOpenHelper{
	
	private static String TAG = "BRDataBaseHelper"; // Tag just for the LogCat window 
	//destination path (location) of our database on device 
	private static String DB_PATH = "";  
	private static String DB_NAME ="brdatabasehelper.sqlite";// Database name 
	private SQLiteDatabase mDataBase;  
	private final Context mContext; 

	public BRDataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);// 1? its Database Version 
	    DB_PATH = "/data/data/" + context.getPackageName() + "/databases/"; 
	    this.mContext = context; 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	public void createDataBase() throws IOException 
	{ 
	    //If database not exists copy it from the assets 
	 
	    boolean mDataBaseExist = checkDataBase(); 
	    if(!mDataBaseExist) 
	    { 
	        this.getReadableDatabase(); 
	        this.close(); 
	        try  
	        { 
	            //Copy the database from assests 
	            copyDataBase(); 
	            Log.e(TAG, "createDatabase database created"); 
	        }  
	        catch (IOException mIOException)  
	        { 
	            throw new Error("ErrorCopyingDataBase"); 
	        } 
	    } 
	} 
	//Copy the database from assets 
    private void copyDataBase() throws IOException 
    { 
        InputStream mInput = mContext.getAssets().open(DB_NAME); 
        String outFileName = DB_PATH + DB_NAME; 
        OutputStream mOutput = new FileOutputStream(outFileName); 
        byte[] mBuffer = new byte[1024]; 
        int mLength; 
        while ((mLength = mInput.read(mBuffer))>0) 
        { 
            mOutput.write(mBuffer, 0, mLength); 
        } 
        mOutput.flush(); 
        mOutput.close(); 
        mInput.close(); 
    }
    
    public boolean openDataBase() throws SQLException 
    { 
        String mPath = DB_PATH + DB_NAME; 
        //Log.v("mPath", mPath); 
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY); 
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS); 
        return mDataBase != null; 
    } 
    
  //Check that the database exists here: /data/data/your package/databases/Da Name 
    private boolean checkDataBase() 
    { 
        File dbFile = new File(DB_PATH + DB_NAME); 
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists()); 
        return dbFile.exists(); 
    } 

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
		if(mDataBase != null) 
            mDataBase.close(); 
	} 

}
