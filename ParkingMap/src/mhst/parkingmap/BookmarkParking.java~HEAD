package mhst.parkingmap;


import DataBaseHandler.TestAdapter;
import Globa.GlobaVariables;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class BookmarkParking extends ActionBarActivity {
	
	private ListView bookmarkList;
	BookmarkItemAdapter adapter = null;
	TestAdapter mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_parking);
		bookmarkList = (ListView) findViewById(R.id.listbookmark);
		mDbHelper = new TestAdapter(getApplicationContext());
		adapter = new BookmarkItemAdapter(getApplicationContext(), GlobaVariables.bookmarkParking); // Tạo Adapter cho bookmark list
		//bookmarkList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, GlobaVariables.bookmarkParking));
		bookmarkList.setAdapter(adapter); // Xét Adapter cho bookmark List
		bookmarkList.setOnItemClickListener(new OnItemClickListener() {
			/*
			 * Xét sự kiện OnClick cho từng Item của list 
			 */

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent t = new Intent(getApplicationContext(),
						showInformation.class);

				t.putExtra("MarkerInfo", GlobaVariables.bookmarkParking.get(position).getMa_parking());

				startActivity(t);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		getMenuInflater().inflate(R.menu.bookmark_parking, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_bookmark_delete) {
			AlertDialog.Builder alertDiaglog = new AlertDialog.Builder(BookmarkParking.this);
			alertDiaglog.setTitle("Xác nhận");
			alertDiaglog.setMessage("Bạn có chắc chắn muốn xóa ?");
			
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
					GlobaVariables.bookmarkParking.clear();
					mDbHelper.deleteAllForLocal("tbl_parking");
					adapter.notifyDataSetChanged();
				}
			});
			alertDiaglog.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
