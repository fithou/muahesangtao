package com.example.maptest3;

import Entity.ParkingLocation;
import Globa.GlobaVariables;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BookmarkParking extends ActionBarActivity {
	
	private ListView bookmarkList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_parking);
		bookmarkList = (ListView) findViewById(R.id.listbookmark);
		ParkingLocation[] pl = new ParkingLocation[GlobaVariables.listParking.size()];
		ParkingItemAdapter adapter = new ParkingItemAdapter(this, R.layout.activity_bookmark_parking, pl);
		int n = 0;
		for (ParkingLocation parkingLocation : GlobaVariables.listParking) {
			n++;
			pl[n] = parkingLocation;			
		}
		bookmarkList.setAdapter(adapter);
		bookmarkList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Hello ", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bookmark_parking, menu);
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
}
