package mhst.parkingmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import junit.framework.Assert;

import parkingPlaces.ConnectionDetector;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import DataBaseHandler.DataBaseHelper;
import DataBaseHandler.TestAdapter;
import DataBaseHandler.imageOnServer;
import Entity.ParkingLocation;
import Entity.Trangthai;
import Globa.GlobaVariables;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

public class showInformation extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */

	String arr[];
	GoogleMap mmap;
	LatLng parkingLocation;
	String phoneNum;
	String s;
	boolean check = false;
	ImageView ivBookmark;
	ImageView ivDirection;
	ImageView ivLike;
	ImageView ivCall;
	private Object mFileUri;
	TestAdapter mDbHelper;
	ParkingLocation bookmark;
	String MACaddress;
	File mediaStorageDir;
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_info);
		this.setTitle("Thông tin bãi gửi xe");
		Intent t = getIntent();
		s = (String) t.getSerializableExtra("MarkerInfo");
		arr = s.split("_");
		mDbHelper = new TestAdapter(getApplicationContext());

		TextView tvTen = (TextView) findViewById(R.id.tvTen);
		TextView tvDienthoai = (TextView) findViewById(R.id.tvDienthoai);
		TextView tvDiachi = (TextView) findViewById(R.id.tvDiachi);
		TextView tvSocho = (TextView) findViewById(R.id.tvSocho);
		ivLike = (ImageView) findViewById(R.id.ivLike);
		ivBookmark = (ImageView) findViewById(R.id.ivBookmark);
		ivDirection = (ImageView) findViewById(R.id.ivDirection);
		ivCall = (ImageView) findViewById(R.id.ivCall);
		cd = new ConnectionDetector(getApplicationContext());

		if (cd.isConnectingToInternet()) {
			WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = manager.getConnectionInfo();
			MACaddress = info.getMacAddress();
			ivLike.setImageResource(R.drawable.likered);
			ivLike.setClickable(true);
		}
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

		kiemtraBookmark();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		parkingLocation = new LatLng(Float.parseFloat(arr[0]),
				Float.parseFloat(arr[1]));

		MapFragment mMapFragment = MapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();

		fragmentTransaction.add(R.id.showmap, mMapFragment);
		RatingBar danhgia = (RatingBar) findViewById(R.id.ratingBar1);
		ImageView anh = (ImageView) findViewById(R.id.imageView1);
		anh.setImageResource(R.drawable.giuxe);
		danhgia.setClickable(false);
		for (ParkingLocation p : GlobaVariables.listParking) {
			if (p.getVitri().equals(s)) {
				// danhgia.setRating(p.getDanhgia());
				bookmark = new ParkingLocation(p.getMa_parking(),
						p.getTen_parking(), p.getSdt(), p.getDiachi(),
						p.getTong_socho(), p.getLike(), p.getImageUri(),
						p.getVitri());
				tvTen.setText(p.getTen_parking());
				tvDiachi.setText(p.getDiachi());
				tvDienthoai.setText(p.getSdt());
				phoneNum = p.getSdt();
				tvSocho.setText(p.getTong_socho() + " ");
				for (ParkingLocation parkingLocation : GlobaVariables.history) {
					if (s.equals(parkingLocation.getVitri())) {
						break;
					}else {
						GlobaVariables.history.add(new ParkingLocation(p.getMa_parking(),
								p.getTen_parking(), p.getSdt(), p.getDiachi(),
								p.getTong_socho(), p.getLike(), p.getImageUri(),
								p.getVitri()));						
					}
				}
				
				File file;
				file = getFileFromUri("file://" + mediaStorageDir.getPath()
						+ "/" + p.getImageUri());

				if (file == null && cd.isConnectingToInternet()) {

					try {
						imageOnServer.downloadFileFromServer(p.getImageUri());
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						Toast.makeText(getApplicationContext(), e.getMessage(),
								Toast.LENGTH_LONG).show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(getApplicationContext(), e.getMessage(),
								Toast.LENGTH_LONG).show();
					}
					file = getFileFromUri("file://" + mediaStorageDir.getPath()
							+ "/" + p.getImageUri());

				} else if (file == null) {
					file = getFileFromUri(p.getImageUri());
				}

				if (file != null) {
					Bitmap bm = decodeSampledBitmapFromFile(file, 500, 500);
					anh.setImageBitmap(bm);
				}

				break;
			}
		}

		if (!phoneNum.isEmpty()) {
			ivCall.setImageResource(R.drawable.call);
			ivCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String intentStr = "tel:" + phoneNum;
					Intent intent = new Intent("android.intent.action.DIAL",
							Uri.parse(intentStr));
					startActivity(intent);
				}
			});
		}

		ivDirection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t = new Intent(getApplicationContext(),
						MainActivity.class);
				t.putExtra("DirectionLocation", s);
				startActivity(t);
			}
		});

		ivBookmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!check) {
					if (ghiBookmark(bookmark)) {
						GlobaVariables.bookmarkParking.add(bookmark);
					}
				} else {
					GlobaVariables.bookmarkParking.remove(s);
				}
			}
		});
		ivLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mDbHelper.addTrangthai(new Trangthai(s, MACaddress, 1))) {
					AsyncHttpClient client = new AsyncHttpClient();
					RequestParams params = new RequestParams();
					params.put("status",
							mDbHelper.composeJSONfromSQLiteStatus());
					Log.d("info", mDbHelper.composeJSONfromSQLiteStatus());
					client.post(GlobaVariables.SERVER_URL + "updParking.php",
							params, new AsyncHttpResponseHandler() {
								public void onSuccess(String response) {

									Toast.makeText(getApplicationContext(),
											response, Toast.LENGTH_LONG).show();
								}

								@Override
								public void onFailure(int statusCode,
										Throwable error, String content) {
									if (statusCode == 404) {
										Toast.makeText(getApplicationContext(),
												"Requested resource not found",
												Toast.LENGTH_LONG).show();
									} else if (statusCode == 500) {
										Toast.makeText(
												getApplicationContext(),
												"Something went wrong at server end",
												Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(
												getApplicationContext(),
												statusCode
														+ "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
												Toast.LENGTH_LONG).show();
									}
								}
							});
				}
			}
		});
	}

	public boolean ghiBookmark(ParkingLocation p) {
		TestAdapter mDbHelper = new TestAdapter(getApplicationContext());
		if (mDbHelper.addBookmark(p)) {
			check = true;
			Toast.makeText(getApplicationContext(), "Bookmark Completed !",
					Toast.LENGTH_LONG).show();
			ivBookmark.setImageResource(R.drawable.bookmark);
			return true;
		}
		return false;
	}

	public void kiemtraBookmark() {
		for (ParkingLocation pb : GlobaVariables.bookmarkParking) {
			if (pb.getMa_parking().equals(s)) {
				ivBookmark.setImageResource(R.drawable.bookmark);
				check = true;
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		mmap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.showmap)).getMap();
		// mmap.setMyLocationEnabled(true);
		Marker m = mmap.addMarker(new MarkerOptions().position(parkingLocation)
				.title("Bạn đang ở vị trí này"));
		m.showInfoWindow();
		mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(parkingLocation,
				14f));
		mmap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				// TODO Auto-generated method stub

				Intent t = new Intent(getApplicationContext(),
						MainActivity.class);
				t.putExtra("comeBackID", s);
				startActivity(t);
			}
		});
		if (cd.isConnectingToInternet()) {
			WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = manager.getConnectionInfo();
			MACaddress = info.getMacAddress();
			ivLike.setClickable(true);
		}
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		// windowInfoAdapter wia = new windowInfoAdapter(getParent(),
		// R.layout.window_info_layout, GlobaVariables.listParking);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

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

	/** Calculate the scaling factor */
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

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);

		return resizedBitmap;
	}
}
