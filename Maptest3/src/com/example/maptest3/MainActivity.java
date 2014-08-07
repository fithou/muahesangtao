package com.example.maptest3;


import Entity.ObjectDrawerItem;
import Entity.ParkingLocation;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	//private NavigationDrawerFragment mNavigationDrawerFragment;
	private GoogleMap mmap;
	private LocationClient myLocation;
	private Location ls;
	private String[] mNavigationDrawerItemTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*=========================================Menu================================*/
		mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(
		        this,
		        mDrawerLayout,
		        R.drawable.ic_drawer,
		        R.string.navigation_drawer_open,
		        R.string.navigation_drawer_close
		        ) {
		    
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
		//getActionBar().setHomeButtonEnabled(true);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[6];
		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);				
		drawerItem[0] = new ObjectDrawerItem(R.drawable.abc_ic_search, "Tìm kiếm");
		drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_action_directions, "Chỉ đường");
		drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_action_new, "Thêm địa điểm");		
		drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_action_location_found, "Vị trí");
		drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_action_time, "Lịch sử");
		drawerItem[5] = new ObjectDrawerItem(R.drawable.bookmarkicon, "Đánh dấu");		
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mTitle = mDrawerTitle = getTitle();
		
		/*========================================= Menu ================================*/
		// Globa.GlobaVariables.listParking = new ArrayList<ParkingLocation>();
		Globa.GlobaVariables.listParking
				.add(new ParkingLocation("Nguyen Duy Phong 1", "0123456789",
						"Nam Du - Hoang Mai - Ha Noi", 27,
						"20.977349 105.895397", 1000));
		Globa.GlobaVariables.listParking
				.add(new ParkingLocation("Nguyen Duy Phong 2", "0123456789",
						"Nam Du - Hoang Mai - Ha Noi", 27,
						"20.977604 105.894759", 1000));
		Globa.GlobaVariables.listParking
				.add(new ParkingLocation("Nguyen Duy Phong 3", "0123456789",
						"Nam Du - Hoang Mai - Ha Noi", 27,
						"20.975916 105.894668", 1000));
		Globa.GlobaVariables.listParking
				.add(new ParkingLocation("Nguyen Duy Phong 4", "0123456789",
						"Nam Du - Hoang Mai - Ha Noi", 27,
						"20.97792 105.894672", 1000));
		Globa.GlobaVariables.listParking
				.add(new ParkingLocation("Nguyen Duy Phong 5", "0123456789",
						"Nam Du - Hoang Mai - Ha Noi", 27,
						"20.974253 105.893943", 1000));
		Globa.GlobaVariables.listParking
				.add(new ParkingLocation("Nguyen Duy Phong 6", "0123456789",
						"Nam Du - Hoang Mai - Ha Noi", 27,
						"20.975495 105.895348", 1000));
		Globa.GlobaVariables.listParking
				.add(new ParkingLocation("Nguyen Duy Phong 7", "0123456789",
						"Nam Du - Hoang Mai - Ha Noi", 27,
						"20.977108 105.893621", 1000));
		Globa.GlobaVariables.listParking
				.add(new ParkingLocation("Nguyen Duy Phong 8", "0123456789",
						"Nam Du - Hoang Mai - Ha Noi", 27,
						"20.974754 105.892634", 1000));
		Globa.GlobaVariables.listParking
				.add(new ParkingLocation("Nguyen Duy Phong 9", "0123456789",
						"Nam Du - Hoang Mai - Ha Noi", 27,
						"20.974413 105.893535", 1000));
		Globa.GlobaVariables.listParking
				.add(new ParkingLocation("Nguyen Duy Phong 10", "0123456789",
						"Nam Du - Hoang Mai - Ha Noi", 27,
						"20.974113 105.895917", 1000));

		myLocation = new LocationClient(this, this, this);
		MapFragment mMapFragment = MapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();

		fragmentTransaction.add(R.id.map, mMapFragment);
		mmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mmap.setMyLocationEnabled(true);

		/*mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		// mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
*/
		mmap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {
				// TODO Auto-generated method stub
				Intent t = new Intent(getApplicationContext(),AddNewPark.class);
				String parkingLocation = point.latitude + " " + point.longitude; 
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

				t.putExtra("MarkerInfo", marker.getTitle());

				startActivity(t);
			}
		});

		mmap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub

				marker.showInfoWindow();
				return true;
			}
		});

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
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		ls = myLocation.getLastLocation();
		Marker m = null;
		for (ParkingLocation park : Globa.GlobaVariables.listParking) {
			String arr[] = park.getVitri().split(" ");
			// Toast.makeText(getApplicationContext(), arr[0] + " " + arr[1],
			// Toast.LENGTH_LONG).show();
			LatLng parkingLocation = new LatLng(Float.parseFloat(arr[0]),
					Float.parseFloat(arr[1]));
			m = mmap.addMarker(new MarkerOptions().position(parkingLocation)
					.title(park.getVitri()));

		}
		if (ls != null) {
			LatLng myLocation = new LatLng(ls.getLatitude(), ls.getLongitude());
			mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
		}

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}
	public class DrawerItemClickListener implements ListView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			selectItem(position);
			
		}

		private void selectItem(int position) {
			// TODO Auto-generated method stub
			Fragment fragment = null;
		    
		    switch (position) {
/*		    case 0:
		        fragment = new CreateFragment();
		        break;
		    case 1:
		        fragment = new ReadFragment();
		        break;*/
		    case 2:
		        Toast.makeText(getApplicationContext(), "Nhấn và giữ vào vị trí trên bản đồ để tạo vị trí mới!", Toast.LENGTH_SHORT).show();
		        break;
/*		    case 3:
		        fragment = new HelpFragment();
		        break;
*/		    case 4:
		        Intent t = new Intent(getApplicationContext(),BookmarkParking.class);
		        startActivity(t);
		        break;
		    /*case 5:
		        fragment = new HelpFragment();
		        break;*/
		 
		    default:
		    	Toast.makeText(getApplicationContext(), "Developing", Toast.LENGTH_LONG).show();
		        break;
		    }
		    
		    if (fragment != null) {
		        android.app.FragmentManager fragmentManager = getFragmentManager();
		        fragmentManager.beginTransaction().replace(R.id.map, fragment).commit();
		 
		        mDrawerList.setItemChecked(position, true);
		        mDrawerList.setSelection(position);
		        getActionBar().setTitle(mNavigationDrawerItemTitles[position]);
		        mDrawerLayout.closeDrawer(mDrawerList);
		        
		    } else {
		        Log.e("MainActivity", "Error in creating fragment");
		    }
		}
	}

}
