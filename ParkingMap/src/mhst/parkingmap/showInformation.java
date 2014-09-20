package mhst.parkingmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import Entity.ParkingLocation;
import Globa.GlobaVariables;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import android.app.FragmentTransaction;
import android.content.Intent;

import android.net.Uri;
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
	int check = 0;
	ImageView ivBookmark;
	ImageView ivDirection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_info);
		this.setTitle("Thông tin bãi gửi xe");
		Intent t = getIntent();
		s = (String) t.getSerializableExtra("MarkerInfo");
		arr = s.split("_");
		kiemtraBookmark();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		parkingLocation = new LatLng(Float.parseFloat(arr[0]),
				Float.parseFloat(arr[1]));

		MapFragment mMapFragment = MapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();

		fragmentTransaction.add(R.id.showmap, mMapFragment);

		TextView tvTen = (TextView) findViewById(R.id.tvTen);
		TextView tvDienthoai = (TextView) findViewById(R.id.tvDienthoai);
		TextView tvDiachi = (TextView) findViewById(R.id.tvDiachi);
		TextView tvSocho = (TextView) findViewById(R.id.tvSocho);

		// tvTen.setText(s);
		RatingBar danhgia = (RatingBar) findViewById(R.id.ratingBar1);
		ImageView anh = (ImageView) findViewById(R.id.imageView1);
		anh.setImageResource(R.drawable.giuxe);
		danhgia.setClickable(false);
		for (ParkingLocation p : GlobaVariables.listParking) {
			if (p.getVitri().equals(s)) {
				//danhgia.setRating(p.getDanhgia());
				tvTen.setText(p.getTen_parking());
				tvDiachi.setText(p.getDiachi());
				tvDienthoai.setText(p.getSdt());
				phoneNum = p.getSdt();
				tvSocho.setText(p.getTong_socho() + " ");
				break;
			}
		}
		
		ImageView ivCall = (ImageView) findViewById(R.id.ivCall);
		if (!phoneNum.isEmpty()) {
			ivCall.setImageResource(R.drawable.ic_action_call);
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
		ivDirection = (ImageView) findViewById(R.id.ivDirection);
		ivDirection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t = new Intent(getApplicationContext(), MainActivity.class);
				t.putExtra("DirectionLocation", s);
				startActivity(t);
			}
		});
		ivBookmark = (ImageView) findViewById(R.id.ivBookmark);
		ivBookmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check == 0) {
					ghiBookmark(ivBookmark);					
				}else {
					GlobaVariables.bookmarkParking.remove(check);					
				}				
			}
		});
	}
	public void ghiBookmark(ImageView ivBookmark){		
		File file = new File( Environment.getExternalStorageDirectory() + "/"
				  + "dulieu.txt");
		if (file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream fop;
		try {
			fop = new FileOutputStream(file, true);
			String location = arr[0] + "_" + arr[1] + "\n";
			fop.write(location.getBytes());
			fop.flush();
			fop.close();
			GlobaVariables.bookmarkParking.add(location.trim());
			Toast.makeText(getApplicationContext(), "Bookmark thành công !", Toast.LENGTH_LONG).show();
			
			ivBookmark.setImageResource(R.drawable.ic_action_favorite);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "không ghi được",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	public void kiemtraBookmark(){
		for (String s : GlobaVariables.bookmarkParking) {
			if (s.equals(arr[0] + "_" + arr[1])) {
				ivBookmark = (ImageView) findViewById(R.id.ivBookmark);				
				break;
			}
			check++;
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
		mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(parkingLocation, 14f));
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

}
