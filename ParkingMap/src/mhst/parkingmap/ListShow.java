package mhst.parkingmap;




import Globa.GlobaVariables;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListShow extends ActionBarActivity {
	private ListView bookmarkList;
	ParkingItemAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_parking);
		
		
		bookmarkList = (ListView) findViewById(R.id.listbookmark);

		adapter = new ParkingItemAdapter(getApplicationContext(), GlobaVariables.listParking); 
		
//		ParkingItemAdapter adapter = new ParkingItemAdapter(this, R.layout.activity_bookmark_parking, GlobaVariables.listParking);		
		bookmarkList.setAdapter(adapter);
		bookmarkList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent t = new Intent(getApplicationContext(),
						showInformation.class);

				t.putExtra("MarkerInfo", GlobaVariables.listParking.get(position).getVitri());

				startActivity(t);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_show, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_list_show,
					container, false);
			return rootView;
		}
	}

}
