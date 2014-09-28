package mhst.parkingmap;

import java.util.ArrayList;
import org.w3c.dom.Document;

import parkingPlaces.ConnectionDetector;
import parkingPlaces.GPSTracker;
import parkingPlaces.GooglePlaces;
import parkingPlaces.PlaceDetails;
import parkingPlaces.PlacesList;

import DataBaseHandler.TestAdapter;
import Entity.ObjectDrawerItem;
import Entity.ParkingLocation;
import Globa.GlobaVariables;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends ActionBarActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	// private NavigationDrawerFragment mNavigationDrawerFragment;
	private GoogleMap mmap;
	private LocationClient myLocation;
	private Location ls;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	SearchView searchView;
	ListView forSearch;
	private boolean doubleBackToExitPressedOnce = false;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	    if (doubleBackToExitPressedOnce) {
	        super.onBackPressed();
	        return;
	    }

	    this.doubleBackToExitPressedOnce = true;
	    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

	    new Handler().postDelayed(new Runnable() {

	        @Override
	        public void run() {
	            doubleBackToExitPressedOnce=false;                       
	        }
	    }, 2000);
	}

	View searchList;
	Geocoder gc;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	// Milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;
	boolean mUpdatesRequested;
	Menu menu;
	LatLngBounds lb;
	TestAdapter mDbHelper;

	// Define an object that holds accuracy and frequency parameters
	LocationRequest mLocationRequest;
	// Handle to SharedPreferences for this app
	SharedPreferences mPrefs;

	// Handle to a SharedPreferences editor
	SharedPreferences.Editor mEditor;
	ProgressDialog pDialog;
	PlacesList nearPlaces;
	GPSTracker gps;
	GooglePlaces googlePlaces;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	PlaceDetails placeDetails;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Create the LocationRequest object
		gc = new Geocoder(getApplicationContext());
		mLocationRequest = LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		// Open the shared preferences
		mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		// Get a SharedPreferences editor
		mEditor = mPrefs.edit();
		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		/*
		 * DataBase Hanlder
		 */

		mDbHelper = new TestAdapter(getApplicationContext());
		mDbHelper.createDatabase();
		mDbHelper.open();
		// creating GPS Class object

		cd = new ConnectionDetector(getApplicationContext());
		googlePlaces = new GooglePlaces();

		myLocation = new LocationClient(this, this, this);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.navigation_drawer_open,
				R.string.navigation_drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
			}
		};

		if (GlobaVariables.bookmarkParking.isEmpty()) {
			GlobaVariables.bookmarkParking = mDbHelper.getAllBoomarkParking();
			// Toast.makeText(getApplicationContext(), "Loaded Parking",
			// Toast.LENGTH_LONG).show();
		}

		if (GlobaVariables.listParking.isEmpty()) {
			GlobaVariables.listParking = mDbHelper.getAllParking();
			// Toast.makeText(getApplicationContext(), "Loaded Parking",
			// Toast.LENGTH_LONG).show();
		}
		if (GlobaVariables.getDuong.isEmpty()) {
			GlobaVariables.getDuong = mDbHelper.getDuong();
			// Toast.makeText(getApplicationContext(), "Loaded Duong",
			// Toast.LENGTH_LONG).show();
		}
		if (GlobaVariables.getPhuong.isEmpty()) {
			GlobaVariables.getPhuong = mDbHelper.getPhuong();
			// Toast.makeText(getApplicationContext(), "Loaded Phuong",
			// Toast.LENGTH_LONG).show();
		}
		if (GlobaVariables.getQuan.isEmpty()) {
			GlobaVariables.getQuan = mDbHelper.getQuan();
			// Toast.makeText(getApplicationContext(), "Loaded Quan",
			// Toast.LENGTH_LONG).show();
		}
		if (GlobaVariables.getTinhthanh.isEmpty()) {
			GlobaVariables.getTinhthanh = mDbHelper.getTinhthanh();
			// Toast.makeText(getApplicationContext(), "Loaded Thanh Pho",
			// Toast.LENGTH_LONG).show();
		}

		getResources().getStringArray(R.array.navigation_drawer_items_array);

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[7];
		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
				R.layout.listview_item_row, drawerItem);
		drawerItem[0] = new ObjectDrawerItem(R.drawable.abc_ic_search,
				"Tìm kiếm");

		drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_action_new,
				"Thêm địa điểm");
		drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_action_time,
				"Lịch sử");
		drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_action_view_as_list,
				"Đánh dấu");
		drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_action_settings,
				"Cài đặt");
		drawerItem[5] = new ObjectDrawerItem(R.drawable.ic_action_refresh,
				"Đồng bộ dữ liệu");
		drawerItem[6] = new ObjectDrawerItem(R.drawable.ic_action_about,
				"Thông tin");
		searchList = findViewById(R.id.listSearch);
		searchList.setVisibility(View.INVISIBLE);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mTitle = mDrawerTitle = getTitle();

		/*
		 * ========================================= Menu
		 * ================================
		 */

		MapFragment mMapFragment = MapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();

		fragmentTransaction.add(R.id.map, mMapFragment);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a search query
			String query = intent.getStringExtra(SearchManager.QUERY);
			Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		super.onStop();
		if (myLocation.isConnected()) {
		}
		myLocation.disconnect();
		super.onStop();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		myLocation.connect();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {

		this.menu = menu;

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_icon, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		forSearch = (ListView) findViewById(R.id.forMainSearch);
		final ArrayAdapter<String> forSearchAdapter = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_list_item_1,
				GlobaVariables.getDuong);
		forSearch.setAdapter(forSearchAdapter);
		forSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(),
				// forSearch.getItemAtPosition(position).toString(),
				// Toast.LENGTH_LONG).show();

				try {
					Address place = gc
							.getFromLocationName(
									forSearch.getItemAtPosition(position)
											.toString(), 1).get(0);
					LatLng l = new LatLng(place.getLatitude(), place
							.getLongitude());
					mmap.animateCamera(CameraUpdateFactory
							.newLatLngZoom(l, 15f));
					Marker m = mmap.addMarker(new MarkerOptions().position(l)
							.title(forSearch.getItemAtPosition(position)
									.toString()));

					
				} catch (Exception e) {

				}
				searchView.onActionViewCollapsed();
				searchList.setVisibility(View.INVISIBLE);
			}
		});
		searchView.setOnSearchClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchList.setVisibility(View.VISIBLE);
			}
		});

		searchView.setOnCloseListener(new OnCloseListener() {

			@Override
			public boolean onClose() {
				// TODO Auto-generated method stub
				searchList.setVisibility(View.INVISIBLE);
				return false;
			}
		});

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				searchView.onActionViewCollapsed();
				searchList.setVisibility(View.INVISIBLE);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String query) {
				// TODO Auto-generated method stub
				forSearchAdapter.getFilter().filter(query);
				return false;
			}

		});

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		if (result.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				result.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */

		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		// If already requested, start periodic updates
		if (mUpdatesRequested) {
			myLocation.requestLocationUpdates(mLocationRequest, this);
		}

		mmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		if (mmap != null) {
			mmap.setMyLocationEnabled(true);
		}
		mmap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {
				// TODO Auto-generated method stub
				Intent t = new Intent(getApplicationContext(), AddNewPark.class);
				String parkingLocation = point.latitude + "_" + point.longitude;
				t.putExtra("parkingLocation", parkingLocation);
				startActivity(t);
			}
		});
		mmap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				// TODO Auto-generated method stub
				Intent t = new Intent(getApplicationContext(),
						showInformation.class);
				t.putExtra("MarkerInfo", marker.getSnippet());
				startActivity(t);
			}
		});

		mmap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				// marker.showInfoWindow();

				marker.showInfoWindow();

				return true;
			}
		});

		ls = myLocation.getLastLocation();
		// LatLngBounds bounds =
		// mmap.getProjection().getVisibleRegion().latLngBounds;

		mmap.setInfoWindowAdapter(new InfoWindowAdapter() {

			@Override
			public View getInfoWindow(Marker marker) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				View rowView = getLayoutInflater().inflate(
						R.layout.window_info_layout, null);

				TextView tvTen = (TextView) rowView.findViewById(R.id.tvTen);
				TextView tvDiachi = (TextView) rowView
						.findViewById(R.id.tvDiachi);

				for (ParkingLocation location : GlobaVariables.listParking) {
					if (location.getVitri().equals(marker.getSnippet())) {
						// imageViewIcon.setImageResource(R.drawable.giuxe);
						tvTen.setText(location.getTen_parking());
						tvDiachi.setText(location.getDiachi());
						break;
					}

				}
				// ParkingLocation folder = GlobaVariables.listParking;
				return rowView;
			}
		});

		Intent t = getIntent();
		String s = (String) t.getSerializableExtra("comeBackID");
		mmap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition position) {
				// TODO Auto-generated method stub
				lb = mmap.getProjection().getVisibleRegion().latLngBounds;
				for (ParkingLocation park : Globa.GlobaVariables.listParking) {
					String arr[] = park.getVitri().split("_");
					// Toast.makeText(getApplicationContext(), arr[0] + "_" +
					// arr[1],
					// Toast.LENGTH_LONG).show();
					LatLng parkingLocation = new LatLng(Float
							.parseFloat(arr[0]), Float.parseFloat(arr[1]));
					if (lb != null && lb.contains(parkingLocation)) {
						mmap.addMarker(new MarkerOptions()
								.position(parkingLocation)
								.title(park.getTen_parking() + "\n"
										+ park.getSdt())
								.icon(BitmapDescriptorFactory
										.fromResource(R.drawable.a))
								.snippet(park.getVitri()));
					}

				}
			}
		});
		if (s != null) {
			String arr[] = s.split("_");
			LatLng myLocation = new LatLng(Float.parseFloat(arr[0]),
					Float.parseFloat(arr[1]));
			mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
					15f));

			// mmap.getCameraPosition().fromLatLngZoom(myLocation, 5f);
		} else if (ls != null) {
			LatLng myLocation = new LatLng(ls.getLatitude(), ls.getLongitude());
			// mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
			// 16));

			mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
					15f));

			String directionInfo = (String) t
					.getSerializableExtra("DirectionLocation");
			if (directionInfo != null) {
				String arr[] = directionInfo.split("_");
				Direction md = new Direction();
				LatLng start = new LatLng(ls.getLatitude(), ls.getLongitude());
				LatLng end = new LatLng(Float.parseFloat(arr[0]),
						Float.parseFloat(arr[1]));
				Document doc = md.getDocument(start, end);
				ArrayList<LatLng> directionPoint = md.getDirection(doc);
				PolylineOptions rectLine = new PolylineOptions().width(5)
						.color(Color.MAGENTA); // Màu và độ rộng

				for (int i = 0; i < directionPoint.size(); i++) {
					rectLine.add(directionPoint.get(i));
				}
				mmap.addPolyline(rectLine);
			}
		}

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	public class DrawerItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			selectItem(position);

		}

		private void selectItem(int position) {
			switch (position) {
			case 0:
				break;

			case 1:
				Toast.makeText(getApplicationContext(),
						"Nhấn và giữ vào bản đồ để thêm vị trí mới!",
						Toast.LENGTH_SHORT).show();
				mDrawerLayout.closeDrawers();
				break;
			case 2:
				Intent t = new Intent(getApplicationContext(), ListShow.class);
				startActivity(t);
				break;
			case 3:
				Intent bookmarkIntent = new Intent(getApplicationContext(),
						BookmarkParking.class);
				startActivity(bookmarkIntent);
				break;
			case 4:
				Intent settingGPSIntent = new Intent(getApplicationContext(),
						SettingsGPS.class);
				startActivity(settingGPSIntent);
				break;
			case 5:
				syncSQLiteServer();
				break;
			case 6:
				Intent aboutIntent = new Intent(getApplicationContext(),
						About.class);
				startActivity(aboutIntent);
				break;

			default:
				Toast.makeText(getApplicationContext(), "Developing",
						Toast.LENGTH_LONG).show();
				mDrawerLayout.closeDrawers();
				break;
			}

		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		String msg = "Updated Location: "
				+ Double.toString(location.getLatitude()) + ","
				+ Double.toString(location.getLongitude());
		// Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
		mEditor.commit();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (ls != null) {
			LatLng myLocation = new LatLng(ls.getLatitude(), ls.getLongitude());
			 mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
			 15f));
		}
		

		/*mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
				15f));*/
		if (mPrefs.contains("KEY_UPDATES_ON")) {
			mUpdatesRequested = mPrefs.getBoolean("KEY_UPDATES_ON", false);

			// Otherwise, turn off location updates
		} else {
			mEditor.putBoolean("KEY_UPDATES_ON", false);
			mEditor.commit();
		}
	}

	public void syncSQLiteServer() {
		// Create AsycHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		// TestAdapter mDbHelper = new TestAdapter(getApplicationContext());

		// ArrayList<ParkingDB> list = (ArrayList<ParkingDB>)
		// mDbHelper.getAllParkingDB();
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
						mDrawerLayout.closeDrawers();

					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						if (statusCode == 404) {
							Toast.makeText(getApplicationContext(),
									"Requested resource not found",
									Toast.LENGTH_LONG).show();
						} else if (statusCode == 500) {
							Toast.makeText(getApplicationContext(),
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
