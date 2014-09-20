package mhst.parkingmap;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.w3c.dom.Document;

import DataBaseHandler.TestAdapter;
import Entity.ObjectDrawerItem;
import Entity.ParkingLocation;
import Globa.GlobaVariables;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;



public class MainActivity extends ActionBarActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		LocationListener {

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
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	// Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    boolean mUpdatesRequested;
    
    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;
 // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        // Open the shared preferences
        mPrefs = getSharedPreferences("SharedPreferences",
                Context.MODE_PRIVATE);
        // Get a SharedPreferences editor
        mEditor = mPrefs.edit();
        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /*
         *  DataBase Hanlder
         */
        TestAdapter mDbHelper = new TestAdapter(getApplicationContext());
		mDbHelper.createDatabase();
		mDbHelper.open();
		if (GlobaVariables.listParking.isEmpty()) {
			GlobaVariables.listParking = mDbHelper.getAllParking();
			//Toast.makeText(getApplicationContext(), "Loaded Parking", Toast.LENGTH_LONG).show();
		}
		if (GlobaVariables.getDuong.isEmpty()) {
			GlobaVariables.getDuong = mDbHelper.getDuong();
			//Toast.makeText(getApplicationContext(), "Loaded Duong", Toast.LENGTH_LONG).show();
		}
		if (GlobaVariables.getPhuong.isEmpty()) {
			GlobaVariables.getPhuong = mDbHelper.getPhuong();
			//Toast.makeText(getApplicationContext(), "Loaded Phuong", Toast.LENGTH_LONG).show();
		}
		if (GlobaVariables.getQuan.isEmpty()) {
			GlobaVariables.getQuan = mDbHelper.getQuan();
			//Toast.makeText(getApplicationContext(), "Loaded Quan", Toast.LENGTH_LONG).show();
		}
		if (GlobaVariables.getTinhthanh.isEmpty()) {
			GlobaVariables.getTinhthanh = mDbHelper.getTinhthanh();
			//Toast.makeText(getApplicationContext(), "Loaded Thanh Pho", Toast.LENGTH_LONG).show();
		}        
		getResources().getStringArray(
				R.array.navigation_drawer_items_array);
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

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[6];
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
		drawerItem[5] = new ObjectDrawerItem(R.drawable.ic_action_about,
				"Thông tin");
		
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mTitle = mDrawerTitle = getTitle();
		
		

		/*
		 * ========================================= Menu
		 * ================================
		 */

		myLocation = new LocationClient(this, this, this);
		MapFragment mMapFragment = MapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();

		fragmentTransaction.add(R.id.map, mMapFragment);
		File file = new File( Environment.getExternalStorageDirectory() + "/"
				  + "dulieu.txt");		
		if (file.exists()) {		
			try {
				FileInputStream in = new FileInputStream(file);
				byte[] buffer = new byte[in.available()];
				
				in.read(buffer);
				String chuoi = new String(buffer);
				GlobaVariables.bookmarkParking.add(chuoi.trim());
				in.close();
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), "không đọc được",
						Toast.LENGTH_SHORT).show();
			}
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
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * if (!mNavigationDrawerFragment.isDrawerOpen()) { // Only show items
		 * in the action bar relevant to this screen // if the drawer is not
		 * showing. Otherwise, let the drawer // decide what to show in the
		 * action bar. getMenuInflater().inflate(R.menu.main, menu);
		 * restoreActionBar(); return true; }
		 */
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_icon, menu);
		// menu.add("Hello").setIcon(R.drawable.abc_ic_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		MenuItem menuItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView)MenuItemCompat.getActionView(menuItem);
		searchView.setSuggestionsAdapter(new CursorAdapter(null, null) {
			
			@Override
			public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void bindView(View arg0, Context arg1, Cursor arg2) {
				// TODO Auto-generated method stub
				
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
	            	result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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
	             * If no resolution is available, display a dialog to the
	             * user with the error.
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
				//marker.showInfoWindow();
				
				marker.showInfoWindow();
				return true;
			}
		});

		ls = myLocation.getLastLocation();
		for (ParkingLocation park : Globa.GlobaVariables.listParking) {
			String arr[] = park.getVitri().split("_");
			// Toast.makeText(getApplicationContext(), arr[0] + "_" + arr[1],
			// Toast.LENGTH_LONG).show();
			LatLng parkingLocation = new LatLng(Float.parseFloat(arr[0]),
					Float.parseFloat(arr[1]));

			mmap.addMarker(new MarkerOptions().position(parkingLocation)
					.title(park.getTen_parking() + "\n" + park.getSdt())
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.a))
					.snippet(park.getVitri()));
		
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

					TextView tvTen = (TextView) rowView
							.findViewById(R.id.tvTen);
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

		}
		
		Intent t = getIntent();
		String s = (String) t.getSerializableExtra("comeBackID");		
		if (s != null) {
			String arr[] = s.split("_");
			LatLng myLocation = new LatLng(Float.parseFloat(arr[0]),
					Float.parseFloat(arr[1]));
			mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
			// mmap.getCameraPosition().fromLatLngZoom(myLocation, 5f);
		} else if (ls != null) {
			LatLng myLocation = new LatLng(ls.getLatitude(), ls.getLongitude());
			//mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
			mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
			String directionInfo = (String)t.getSerializableExtra("DirectionLocation");
			if (directionInfo != null) {
				String arr[] = directionInfo.split("_");
				Direction md = new Direction();
				LatLng start = new LatLng(ls.getLatitude(), ls.getLongitude());			
				LatLng end = new LatLng(Float.parseFloat(arr[0]),
						Float.parseFloat(arr[1])); 
		        Document doc = md.getDocument(start, end);
		        ArrayList<LatLng> directionPoint = md.getDirection(doc);
		        PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED); // Màu và độ rộng
		 
		        for(int i = 0 ; i < directionPoint.size() ; i++) {         
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
		String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
		if (mPrefs.contains("KEY_UPDATES_ON")) {
            mUpdatesRequested =
                    mPrefs.getBoolean("KEY_UPDATES_ON", false);

        // Otherwise, turn off location updates
        } else {
            mEditor.putBoolean("KEY_UPDATES_ON", false);
            mEditor.commit();
        }
	}
}
