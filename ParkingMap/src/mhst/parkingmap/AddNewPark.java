package mhst.parkingmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import parkingPlaces.ConnectionDetector;

import junit.framework.Assert;

import DataBaseHandler.TestAdapter;
import DataBaseHandler.imageOnServer;
import Entity.ParkingLocation;
import Globa.GlobaVariables;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class AddNewPark extends FragmentActivity {

	ViewPager mViewPager;
	// PagerInfo mPagerInfor;
	EditText etTen;
	EditText etSoDT;
	EditText etSonha;
	EditText etTongsocho;
	AutoCompleteTextView etDuong;
	AutoCompleteTextView etPhuong;
	AutoCompleteTextView etQuan;
	AutoCompleteTextView etThanhpho;
	ImageButton ibCamera;
	ImageButton ibGallery;
	ImageView showParkingImage;
	String getLocationStringFromMap;
	String arr[];
	String imageUri;
	String imageFileName;
	File mediaStorageDir;
	ConnectionDetector cd;
	String galleryLocation;
	
	private static final String TAG = AddNewPark.class.getSimpleName();
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int RESULT_LOAD_IMAGE = 200;
	private static final String KEY_FILE_URI = "com.cmc.camera_file_uri";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	private Uri mFileUri;
	customDialogSearch cds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(KEY_FILE_URI)) {
				mFileUri = Uri
						.parse(savedInstanceState.getString(KEY_FILE_URI));
			} else {
				mFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
			}
		} else {
			mFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
		}
		// Khởi tạo các View
		cd = new ConnectionDetector(getApplicationContext());
		setContentView(R.layout.add_new_park);
		etTen = (EditText) findViewById(R.id.etname);
		etSoDT = (EditText) findViewById(R.id.etDienthoai);
		etSonha = (EditText) findViewById(R.id.etSonha);
		etDuong = (AutoCompleteTextView) findViewById(R.id.etDuong);
		etPhuong = (AutoCompleteTextView) findViewById(R.id.etPhuong);
		etQuan = (AutoCompleteTextView) findViewById(R.id.etQuan);
		etThanhpho = (AutoCompleteTextView) findViewById(R.id.etThanhpho);
		ibCamera = (ImageButton) findViewById(R.id.ibCamera);
		ibGallery = (ImageButton) findViewById(R.id.ibGallery);
		showParkingImage = (ImageView) findViewById(R.id.showParkingImage);	
		Intent t = getIntent();
		getLocationStringFromMap = (String) t
				.getSerializableExtra("parkingLocation");
		arr = getLocationStringFromMap.split("_");
		this.setTitle("Thêm bãi gửi xe mới");		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Thêm chức năng cho nút Tìm đường
		etDuong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				cds = new customDialogSearch(AddNewPark.this, view.getId());
				cds.show();
				ListView lv = (ListView) cds.findViewById(R.id.listSuggest);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						etDuong.setText(parent.getItemAtPosition(position)
								.toString());
						cds.dismiss();
					}
				});
			}
		});
		
		//Thêm chức năng Click cho nút tìm Phường
		etPhuong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				cds = new customDialogSearch(AddNewPark.this, view.getId());
				cds.show();
				ListView lv = (ListView) cds.findViewById(R.id.listSuggest);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						etPhuong.setText(parent.getItemAtPosition(position)
								.toString());
						cds.dismiss();
					}
				});
			}
		});
		
		
		//Thêm chức năng cho nút tìm Quận
		etQuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				cds = new customDialogSearch(AddNewPark.this, view.getId());
				cds.show();
				ListView lv = (ListView) cds.findViewById(R.id.listSuggest);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						etQuan.setText(parent.getItemAtPosition(position)
								.toString());
						cds.dismiss();
					}
				});
			}
		});
		
		//Thêm chức năng cho nút tìm Thành phố
		etThanhpho.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				cds = new customDialogSearch(AddNewPark.this, view.getId());
				cds.show();
				ListView lv = (ListView) cds.findViewById(R.id.listSuggest);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						etThanhpho.setText(parent.getItemAtPosition(position)
								.toString());
						cds.dismiss();
					}
				});
			}
		});
		
		
		etTongsocho = (EditText) findViewById(R.id.etTongso);
		
		//Chức năng cho nút camera		
		ibCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Assert.assertNotNull("file uri not null before firing intent",
						mFileUri);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// this is the file that the camera app will write to
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
				startActivityForResult(intent,
						CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		
		//Chức năng cho nút lấy ảnh từ Gallery 
		ibGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, RESULT_LOAD_IMAGE);
			}
		});		
		getAddressFromGeocode();
		Button addParking = (Button) findViewById(R.id.them);
		
		/*
		 * Chức năng cho nút thêm một Parking mới
		 */
		addParking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Entity.ParkingLocation pl = new ParkingLocation();
				TestAdapter mDbHelper = new TestAdapter(getApplicationContext());
				mDbHelper.createDatabase();
				mDbHelper.open();				
				if (mDbHelper.AddParking(etTen.getText().toString(), etSoDT
						.getText().toString(),
						etTongsocho.getText().toString(), 1, etSonha.getText() + " - " + etDuong.getText()
						+ " - " + etPhuong.getText() + " - "
						+ etQuan.getText(),
						getLocationStringFromMap, imageFileName) == true) {
					Toast.makeText(getApplicationContext(),
							"Thêm thành công!", Toast.LENGTH_LONG).show();
					pl.setMa_parking(getLocationStringFromMap);
					pl.setSdt(etSoDT.getText().toString());
					pl.setDiachi(etSonha.getText() + " - " + etDuong.getText()
							+ " - " + etPhuong.getText() + " - "
							+ etQuan.getText());
					pl.setLike(0);					
					pl.setVitri(getLocationStringFromMap);
					pl.setTen_parking(etTen.getText().toString());
					pl.setImageUri(imageFileName);
					pl.setTong_socho(etTongsocho.getText().toString());
					
					GlobaVariables.listParking.add(pl);
					Intent t = new Intent(getApplicationContext(),
							MainActivity.class);
					t.putExtra("comeBackID", getLocationStringFromMap);
					startActivity(t);
				} else {
					Toast.makeText(getApplicationContext(), "Thất bại!",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	/*
	 * Lấy ra Uri của file Media
	 * Input : kiểu của file Media
	 * Output : Uri của file
	 */
	private Uri getOutputMediaFileUri(int type) {
		File file = getOutputMediaFile(type);
		Assert.assertNotNull("getOutputMediaFileUri file", file);
		return Uri.fromFile(file);
	}
	
	/*
	 * Lấy ra file Media
	 * Input : kiểu của file Media 
	 */
	private File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		String state = Environment.getExternalStorageState();
		Assert.assertTrue("external media is mounted",
				TextUtils.equals(state, Environment.MEDIA_MOUNTED));

		if (Build.VERSION.SDK_INT > 8) {
			mediaStorageDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		} else {
			mediaStorageDir = new File(
					Environment.getExternalStorageDirectory(), "Pictures");
		}

		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (mediaStorageDir.mkdirs() || mediaStorageDir.isDirectory()) {
				Log.v(TAG, "directory is ok" + mediaStorageDir.getPath());
			} else {
				Assert.fail("failed to create directory");
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
			imageFileName = "IMG_" + timeStamp + ".jpg";
			
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			Assert.fail("Invalid media type");
			return null;
		}
		Assert.assertNotNull("media file is not null", mediaFile);
		Log.v(TAG, "will store file at " + mediaFile.toString());
		return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					if (data.getData() != null) {
						Log.v(TAG, "intent data: " + data.getData().toString());
					}
					if (data.getAction() != null) {
						Log.v(TAG, "intent action: "
								+ data.getAction().toString());
					}
					if (data.getExtras() != null) {
						Log.v(TAG, "intent extras: "
								+ data.getExtras().toString());
					}
				}
				ImageView imageView = (ImageView) findViewById(R.id.showParkingImage);
				Assert.assertNotNull("file uri in onActivityResult", mFileUri);
				Log.v(TAG, "stored file name is " + mFileUri.toString());
				imageUri = mFileUri.toString();
				Log.d("From Camera", imageUri);
				File file = getFileFromUri(imageUri);
				resizeImageSmaller(file);
				if (file != null) {
					Bitmap bm = decodeSampledBitmapFromFile(file, 500, 500);
					
					imageView.setImageBitmap(bm);
				}
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
		} else if (requestCode == RESULT_LOAD_IMAGE) {
			if (resultCode == RESULT_OK && data != null) {
				Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	            Cursor cursor = getContentResolver().query(selectedImage,
	                    filePathColumn, null, null, null);
	            cursor.moveToFirst();
	 
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            imageUri = "file://" + cursor.getString(columnIndex);	            
	            galleryLocation = cursor.getString(columnIndex);
	            Log.d("galleryLocation", galleryLocation);
	            String arr[] = galleryLocation.split("/");
	            imageFileName = arr[arr.length-1];
	            copyFile(cursor.getString(columnIndex) ,imageFileName ,mediaStorageDir.getPath());
	            Log.d("Image Name", imageFileName);
	            cursor.close();
	            Log.d("From Gallery", imageUri);
	            File file = getFileFromUri(imageUri);
	            ImageView imageView = (ImageView) findViewById(R.id.showParkingImage);
				if (file != null) {
					Bitmap bm = decodeSampledBitmapFromFile(file, 500, 500);								
					imageView.setImageBitmap(bm);
				}
				
	            /*ImageView imageView = (ImageView) findViewById(R.id.imgView);
	            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));*/
			}
		}
	}
	
	/*
	 * Chuyển file sang dạng Bitmap
	 */
	private Bitmap decodeSampledBitmapFromFile(File file, int reqWidth,
			int reqHeight) {
		// TODO Auto-generated method stub
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	}
	
	/*
	 * Tạo file từ Uri
	 */
	private File getFileFromUri(String mFileUri) {
		// TODO Auto-generated method stub
		if (mFileUri != null) {
			try {
				URI uri;
				if (mFileUri.toString().startsWith("file://")) {
					// normal path
					uri = URI.create(mFileUri.toString());
				} else {
					// support path
					uri = URI.create("file://" + mFileUri.toString());
				}
				File file = new File(uri);
				if (file != null) {
					if (file.canRead()) {
						return file;
					}
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	/*
	 * Định lại kích thước file ảnh
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	/*
	 * Lấy dữ liệu về Đường - Phường - Quận - Thành phố từ Location xác định
	 */
	public void getAddressFromGeocode(){
		Geocoder gc = new Geocoder(getApplicationContext());
		List<Address> address;
		try {
			address = gc.getFromLocation(Float.parseFloat(arr[0]),
					Float.parseFloat(arr[1]), 5);
			Address place = address.get(0);
			etThanhpho.setText(place.getAdminArea().toString());
			etQuan.setText(place.getSubAdminArea().toString());
			etPhuong.setText(place.getSubLocality().toString());
			etDuong.setText(place.getThoroughfare().toString());
		} catch (Exception e) {
			//Log.d("Loi",e.getMessage());
		}
	}
	private void copyFile(String inputPath,String filename, String outputPath) {

	    InputStream in = null;
	    OutputStream out = null;
	    Log.d("Input", inputPath);
	    Log.d("Output", outputPath);
	    try {

	        //create output directory if it doesn't exist
	    	//URI uri = new URI("file://" + outputPath);
	        File dir = new File (outputPath); 
	        if (!dir.exists())
	        {
	        	Log.d("dir Path", dir.getPath());
	            dir.mkdirs();
	        }


	        in = new FileInputStream(inputPath);        
	        out = new FileOutputStream(outputPath + "/" + filename);

	        byte[] buffer = new byte[1024];
	        int read;
	        while ((read = in.read(buffer)) != -1) {
	            out.write(buffer, 0, read);
	        }
	        in.close();
	        in = null;

	            // write the output file (You have now copied the file)
	            out.flush();
	        out.close();
	        out = null;
	        
	    }  catch (FileNotFoundException fnfe1) {
	        //Log.e("FileNotFound", fnfe1.getMessage());
	    }
	            catch (Exception e) {
	        //Log.e("tag", e.getMessage());
	    }

	}
	public void resizeImageSmaller(File imageFile){		       
        if (imageFile != null) {
        	Bitmap bm = decodeSampledBitmapFromFile(imageFile, 500, 500);
        	 try {
 				FileOutputStream imageOut = new FileOutputStream(imageFile);
 				bm.compress(Bitmap.CompressFormat.PNG, 60, imageOut);
 				imageOut.close();
 			} catch (FileNotFoundException e) {
 				// TODO Auto-generated catch block
 				Log.d(TAG, "File not found: " + e.getMessage());
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				Log.d(TAG, "Error accessing file: " + e.getMessage());
 			}	
		}
	}

}
