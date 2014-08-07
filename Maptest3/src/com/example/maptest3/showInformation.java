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

import android.support.v7.app.ActionBarActivity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.FragmentTransaction;
import android.content.Intent;

import android.location.Location;
import android.os.Bundle;


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
	private CharSequence mTitle;
	private GoogleMap mmap;

	String arr[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		this.setTitle("Thông tin bãi gửi xe");
		Intent t = getIntent();
		String s = (String) t.getSerializableExtra("MarkerInfo");
		arr = s.split(" ");
		MapFragment mMapFragment = MapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.showmap, mMapFragment);
		fragmentTransaction.commit();
		MapFragment mr = (MapFragment) getFragmentManager().findFragmentById(R.id.showmap); 
		mTitle = getTitle();
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Set up the drawer.

		
		// Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
		TextView tvTen = (TextView) findViewById(R.id.tvTen);
		TextView tvDienthoai = (TextView) findViewById(R.id.tvDienthoai);
		TextView tvDiachi = (TextView) findViewById(R.id.tvDiachi);
		TextView tvSonha = (TextView) findViewById(R.id.tvSonha);
		TextView tvVitri = (TextView) findViewById(R.id.tvVitri);
		TextView tvSocho = (TextView) findViewById(R.id.tvSocho);
		for (ParkingLocation p : GlobaVariables.listParking) {

			if (p.getVitri().equals(s)) {
				tvTen.setText(p.getTenbaidoxe());
				tvDiachi.setText(p.getDiachi());
				tvDienthoai.setText(p.getSodienthoai());
				tvSonha.setText(p.getSonha() + " ");
				tvVitri.setText(p.getVitri());
				tvSocho.setText(p.getTongsocho() + " ");
				// Toast.makeText(getApplicationContext(), "Da tim thay " +
				// p.getTenbaidoxe(), Toast.LENGTH_LONG).show();

				break;
			}
		}

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

}
