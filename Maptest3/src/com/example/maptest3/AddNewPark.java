package com.example.maptest3;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import Entity.ParkingLocation;
import Globa.GlobaVariables;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;

import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewPark extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	ViewPager mViewPager;
	// PagerInfo mPagerInfor;
	EditText etTen;
	EditText etSoDT;
	EditText etDiachi;
	EditText etSonha;
	EditText etVitri;
	EditText etTongsocho;
	String getLocationStringFromMap;
	String arr[];
	private GoogleMap mmap;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_section_launchpad);
		etTen = (EditText) findViewById(R.id.etname);
		etDiachi = (EditText) findViewById(R.id.etAddress);
		etSoDT = (EditText) findViewById(R.id.etDienthoai);
		etSonha = (EditText) findViewById(R.id.etSonha);
		etVitri = (EditText) findViewById(R.id.etToado);
		etTongsocho = (EditText) findViewById(R.id.etTongso);		
		Intent t = getIntent();
		MapFragment mMapFragment = MapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.showmap, mMapFragment);
		fragmentTransaction.commit();
		this.setTitle("Thêm bãi gửi xe mới");
				
		getActionBar().setDisplayHomeAsUpEnabled(true);		

		getLocationStringFromMap = (String) t
				.getSerializableExtra("parkingLocation");
		if (getLocationStringFromMap != null) {
			etVitri.setText(getLocationStringFromMap);
		}
		arr = getLocationStringFromMap.split(" ");
		
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Button addParking = (Button) findViewById(R.id.them);
		addParking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Entity.ParkingLocation pl = new ParkingLocation();
				pl.setTenbaidoxe(etTen.getText().toString());
				pl.setDiachi(etDiachi.getText().toString());
				pl.setSodienthoai(etSoDT.getText().toString());
				pl.setSonha(Integer.parseInt(etSonha.getText().toString()));
				pl.setVitri(etVitri.getText().toString());
				pl.setTongsocho(Integer.parseInt(etTongsocho.getText()
						.toString()));
				GlobaVariables.listParking.add(pl);

			}
		});
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.add_new_park, menu);
		return true;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUpMapIfNeeded();
	}
	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mmap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.showmap))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mmap != null) {
                setUpMap();
            }
        }
    }

	private void setUpMap() {
		// TODO Auto-generated method stub
		LatLng parkingLocation = new LatLng(Float.parseFloat(arr[0]),
				Float.parseFloat(arr[1]));
    	mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkingLocation, 16));
    	Marker m = mmap.addMarker(new MarkerOptions().position(parkingLocation)
				.title("Here"));
    	m.showInfoWindow();
	}
}
