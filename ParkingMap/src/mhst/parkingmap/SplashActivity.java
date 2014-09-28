package mhst.parkingmap;

import java.util.HashMap;
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
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SlidingDrawer;
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
			new LoadPlaces().execute();
		} else {
			Toast.makeText(getApplicationContext(),
					"Hãy kiểm tra GPS và Internet của bạn", Toast.LENGTH_LONG)
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
					// Get json response status
					String status = nearPlaces.status;
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
								"Sorry no places found. Try to change the types of places",
								Toast.LENGTH_LONG).show();
					} else if (status.equals("UNKNOWN_ERROR")) {
						Toast.makeText(getApplicationContext(),
								"Sorry unknown error occured.",
								Toast.LENGTH_LONG).show();
					} else if (status.equals("OVER_QUERY_LIMIT")) {

						Toast.makeText(
								getApplicationContext(),
								"Sorry query limit to google places is reached",
								Toast.LENGTH_LONG).show();
					} else if (status.equals("REQUEST_DENIED")) {

						Toast.makeText(getApplicationContext(),
								"Sorry error occured. Request is denied",
								Toast.LENGTH_LONG).show();
					} else if (status.equals("INVALID_REQUEST")) {

						Toast.makeText(getApplicationContext(),
								"Sorry error occured. Invalid Request",
								Toast.LENGTH_LONG).show();
					} else {

						Toast.makeText(getApplicationContext(),
								"Sorry error occured.", Toast.LENGTH_LONG)
								.show();
					}
				}
			});
			Intent t = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(t);

		}

	}
}
