package mhst.parkingmap;




import DataBaseHandler.TestAdapter;
import Globa.GlobaVariables;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.text.AlteredCharSequence;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListShow extends ActionBarActivity {
	private ListView bookmarkList;
	ParkingItemAdapter adapter = null;
	TestAdapter mDbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		/*
		 * Danh sách những bãi đỗ xe từng xem
		 */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_parking);
		mDbHelper = new TestAdapter(getApplicationContext());
		
		bookmarkList = (ListView) findViewById(R.id.listbookmark);

		adapter = new ParkingItemAdapter(getApplicationContext(), GlobaVariables.history); 
		
//		ParkingItemAdapter adapter = new ParkingItemAdapter(this, R.layout.activity_bookmark_parking, GlobaVariables.listParking);		
		bookmarkList.setAdapter(adapter);
		bookmarkList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent t = new Intent(getApplicationContext(),
						showInformation.class);

				t.putExtra("MarkerInfo", GlobaVariables.history.get(position).getMa_parking());

				startActivity(t);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
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
		if (id == R.id.action_history_delete) {
			AlertDialog.Builder alertDiaglog = new AlertDialog.Builder(ListShow.this);
			alertDiaglog.setTitle("Xác nhận");
			alertDiaglog.setMessage("Bạn có chắc chắn muốn xóa?");
			
			alertDiaglog.setNegativeButton("Không", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			alertDiaglog.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					GlobaVariables.history.clear();
					mDbHelper.deleteAllForLocal("tbl_parkingHistory");
					adapter.notifyDataSetChanged();
				}
			});
			alertDiaglog.show();
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
