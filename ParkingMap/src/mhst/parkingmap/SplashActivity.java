package mhst.parkingmap;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import parkingPlaces.ConnectionDetector;
import parkingPlaces.GPSTracker;
import parkingPlaces.GooglePlaces;
import parkingPlaces.Place;
import parkingPlaces.PlaceDetails;
import parkingPlaces.PlacesList;
import DataBaseHandler.TestAdapter;
import Entity.ParkingLocation;
import Globa.GlobaVariables;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

public class SplashActivity extends Activity {

	ProgressDialog pDialog;
	PlacesList nearPlaces;
	GPSTracker gps;
	GooglePlaces googlePlaces;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	PlaceDetails placeDetails;
	TestAdapter mDbHelper;
	boolean checkExist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		getActionBar().hide();
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
			        new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			}
		mDbHelper = new TestAdapter(getApplicationContext());
		mDbHelper.createDatabase();
		mDbHelper.open();
		// creating GPS Class object

		gps = new GPSTracker(this);
		cd = new ConnectionDetector(getApplicationContext());
		googlePlaces = new GooglePlaces();
		gps = new GPSTracker(this);
		isInternetPresent = cd.isConnectingToInternet();
		if (gps.canGetLocation() && isInternetPresent) {
			
			getDBfromServer();
			new LoadPlaces().execute();
		} else {
			Toast.makeText(getApplicationContext(),
					"Kiểm tra lại kết nối GPS và Internet!", Toast.LENGTH_LONG)
					.show();
			Intent t = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(t);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class LoadPlaces extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SplashActivity.this);
			pDialog.setMessage("Đang lấy thông tin bãi đỗ xe");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			// googlePlaces = new GooglePlaces();

			try {
				// Separeate your place types by PIPE symbol "|"
				// If you want all types places make it as null
				// Check list of types supported by google
				//
				String types = "parking"; // Listing places only cafes,
											// restaurants
				
				// Radius in meters - increase this value if you don't find any
				// places
				double radius = 3000000; // 1000 meters

				// get nearest places
				// Address hanoi = gc.getFromLocationName("Hà Nội", 1).get(0);

				nearPlaces = googlePlaces.search(gps.getLatitude(),
						gps.getLongitude(), radius, types);
				Log.d("My Location",
						gps.getLatitude() + "_" + gps.getLongitude());

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					//syncSQLiteServer();
					getDBfromServer();
					String status = null;
					// Get json response status
					if (nearPlaces != null) {
						status = nearPlaces.status;
					}					
					if (mDbHelper.getAllParking().size()>0) {
						GlobaVariables.listParking = mDbHelper.getAllParking();
						Log.d("Trang thai du lieu","Da co du lieu : " + GlobaVariables.listParking.size());
					}
					
					// Check for all possible status
					if (status.equals("OK")) {
						// Successfully got places details
						if (nearPlaces.results != null) {
							// loop through each place
							
							for (Place p : nearPlaces.results) {
								String s = p.geometry.location.lat + "_"
										+ p.geometry.location.lng;
								Log.d("Parking Location", s);
								if (checkExist) {
									checkExist = false;									
								}
								
								if (GlobaVariables.listParking.size() > 0) {
									for (ParkingLocation parkingLocation : GlobaVariables.listParking) {
										if (s.equals(parkingLocation
												.getMa_parking())) {
											checkExist = true;											
										}
									}									
								}else {
									Log.d("Du lieu rong", "Du lieu rong");
								}
								
								if (checkExist) {
									Log.d(p.name, "Da co");
								}

								if (!checkExist || GlobaVariables.listParking.isEmpty()) {
									try {
										placeDetails = googlePlaces
												.getPlaceDetails(p.reference);
										Log.d("reference", p.reference);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (placeDetails != null) {
										mDbHelper
												.AddParking(
														placeDetails.result.name,
														placeDetails.result.formatted_phone_number,
														"100",
														0,
														placeDetails.result.formatted_address,
														placeDetails.result.geometry.location.lat
																+ "_"
																+ placeDetails.result.geometry.location.lng,
														null);
									} else {
										Log.d("plageDetails", "Null");
									}
								}

							}
						}
					} else if (status.equals("ZERO_RESULTS")) {
						// Zero results found
						Toast.makeText(
								getApplicationContext(),
								"Máy chủ hệ thống đang bảo trì!",
								Toast.LENGTH_LONG).show();
					} else if (status.equals("UNKNOWN_ERROR")) {
						Toast.makeText(getApplicationContext(),
								"Máy chủ hệ thống đang bảo trì!",
								Toast.LENGTH_LONG).show();
					} else if (status.equals("OVER_QUERY_LIMIT")) {

						Toast.makeText(
								getApplicationContext(),
								"Máy chủ hệ thống đang bảo trì!",
								Toast.LENGTH_LONG).show();
					} else if (status.equals("REQUEST_DENIED")) {

						Toast.makeText(getApplicationContext(),
								"Máy chủ hệ thống đang bảo trì!",
								Toast.LENGTH_LONG).show();
					} else if (status.equals("INVALID_REQUEST")) {

						Toast.makeText(getApplicationContext(),
								"Máy chủ hệ thống đang bảo trì!",
								Toast.LENGTH_LONG).show();
					} else {

						Toast.makeText(getApplicationContext(),
								"Máy chủ hệ thống đang bảo trì!",
								Toast.LENGTH_LONG).show();
					}
				}
			});
			//mDbHelper.uploadAnhToServer();
			Intent t = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(t);

		}

	}
	public void syncSQLiteServer() {
		// Create AsycHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("park", mDbHelper.composeJSONfromSQLite());
		params.put("status", mDbHelper.composeJSONfromSQLiteStatus());
		Log.d("info", mDbHelper.composeJSONfromSQLite());
		Log.d("info", mDbHelper.composeJSONfromSQLiteStatus());		
		client.post(GlobaVariables.SERVER_URL + "insParking.php", params,
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {

						Toast.makeText(getApplicationContext(),
								"Đồng bộ thành công!", Toast.LENGTH_LONG)
								.show();
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						if (statusCode == 404) {
							Toast.makeText(getApplicationContext(),
									"Máy chủ hệ thống đang bảo trì!",
									Toast.LENGTH_LONG).show();
						} else if (statusCode == 500) {
							Toast.makeText(getApplicationContext(),
									"Máy chủ hệ thống đang bảo trì!",
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(
									getApplicationContext(),
									"Kiểm tra lại kết nối Internet!",
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}
	public void  getDBfromServer() {
		// Create AsycHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		// Syn Server - SQLite
		params.put("act", "fd");
		client.post("http://tmas.hou.edu.vn/ms/", params,new AsyncHttpResponseHandler() {
			public void onSuccess(String response) {
				// Update SQLite DB with response sent by getParking.php
				updateSQLite(response);
				Log.e("RESPONSE", response);
			}
					
			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				if (statusCode == 404) {
					Toast.makeText(getApplicationContext(),
							"Máy chủ hệ thống đang bảo trì!",
							Toast.LENGTH_LONG).show();
				} else if (statusCode == 500) {
					Toast.makeText(getApplicationContext(),
							"Máy chủ hệ thống đang bảo trì!",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Kiểm tra lại kết nối Internet!",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	// Update SQLite DB
	public void updateSQLite(String response){
		try {
			// Extract JSON array from the reponse
			/*JSONObject jobj = new JSONObject(URLDecoder.decode(response,"UTF-8"));
			JSONArray arr;
			arr = jobj.getJSONArray(null);*/
			JSONArray arr = new JSONArray(response);
			//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
			
			
			// If no of array elements is not zero
			if (arr.length() > 0) {
				mDbHelper.deleteParking();
			// Loop through each array element, get JSON object
				for (int i=0;i<arr.length();++i) {
					// Get JSON object
					JSONObject obj = arr.getJSONObject(i);
					Log.e("Ten Parking: ", i + "-" + obj.get("ten_parking").toString());
					String ma_parking = obj.get("ma_parking").toString();
					String ten_parking = obj.get("ten_parking").toString();
					String sdt = obj.get("sdt").toString();
					String diachi = obj.get("diachi").toString();
					String imageUri = obj.get("img").toString();
					String vitri = obj.get("vitri").toString();
					int like = obj.getInt("like");
					String tong_socho = obj.get("tong_socho").toString();
					mDbHelper.AddParking(ten_parking,
							sdt,
							tong_socho,
							like,
							diachi,
							vitri,
							imageUri);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
