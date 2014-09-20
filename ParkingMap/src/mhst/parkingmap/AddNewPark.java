package mhst.parkingmap;


import DataBaseHandler.TestAdapter;
import Entity.ParkingLocation;
import Globa.GlobaVariables;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class AddNewPark extends FragmentActivity {

	ViewPager mViewPager;
	// PagerInfo mPagerInfor;
	EditText etTen;
	EditText etSoDT;
	EditText etSonha;
	EditText etTongsocho;
	AutoCompleteTextView etDuong;
	AutoCompleteTextView etPhuong;
	AutoCompleteTextView etQuan;
	AutoCompleteTextView etThanhpho;
	ImageButton ibCamera;
	ImageButton ibGallery;
	ImageView showParkingImage;
	String getLocationStringFromMap;
	String arr[];
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	customDialogSearch cds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_park);
		etTen = (EditText) findViewById(R.id.etname);
		etSoDT = (EditText) findViewById(R.id.etDienthoai);
		etSonha = (EditText) findViewById(R.id.etSonha);
		etDuong = (AutoCompleteTextView) findViewById(R.id.etDuong);
		etPhuong = (AutoCompleteTextView) findViewById(R.id.etPhuong);
		etQuan = (AutoCompleteTextView) findViewById(R.id.etQuan);
		etThanhpho = (AutoCompleteTextView) findViewById(R.id.etThanhpho);
		ibCamera = (ImageButton) findViewById(R.id.ibCamera);
		ibGallery = (ImageButton) findViewById(R.id.ibGallery);
		showParkingImage = (ImageView) findViewById(R.id.showParkingImage);

		etDuong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				cds = new customDialogSearch(
						AddNewPark.this, view.getId());
				cds.show();
				ListView lv = (ListView) cds.findViewById(R.id.listSuggest);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						etDuong.setText(parent.getItemAtPosition(position).toString());
						cds.dismiss();
					}
				});
			}
		});
		etPhuong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				cds = new customDialogSearch(
						AddNewPark.this, view.getId());
				cds.show();
				ListView lv = (ListView) cds.findViewById(R.id.listSuggest);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						etPhuong.setText(parent.getItemAtPosition(position).toString());
						cds.dismiss();
					}
				});
			}
		});

		etQuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				cds = new customDialogSearch(
						AddNewPark.this, view.getId());
				cds.show();
				ListView lv = (ListView) cds.findViewById(R.id.listSuggest);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						etQuan.setText(parent.getItemAtPosition(position).toString());
						cds.dismiss();
					}
				});
			}
		});
		etThanhpho.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				cds = new customDialogSearch(
						AddNewPark.this, view.getId());
				cds.show();
				ListView lv = (ListView) cds.findViewById(R.id.listSuggest);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						etThanhpho.setText(parent.getItemAtPosition(position).toString());
						cds.dismiss();
					}
				});
			}
		});

		etTongsocho = (EditText) findViewById(R.id.etTongso);
		ibCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					startActivityForResult(takePictureIntent,
							CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				}
			}
		});

		Intent t = getIntent();
		this.setTitle("Thêm bãi gửi xe mới");

		getActionBar().setDisplayHomeAsUpEnabled(true);

		getLocationStringFromMap = (String) t
				.getSerializableExtra("parkingLocation");
		arr = getLocationStringFromMap.split("_");

		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Button addParking = (Button) findViewById(R.id.them);
		addParking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Entity.ParkingLocation pl = new ParkingLocation();
				TestAdapter mDbHelper = new TestAdapter(getApplicationContext());         
		    	mDbHelper.createDatabase();       
		    	mDbHelper.open();
				
				if (mDbHelper.AddParking(etTen.getText().toString(),
									etSoDT.getText().toString(),
									etSonha.getText().toString(),
									etTongsocho.getText().toString(),
									1,
									0, 
									etDuong.getText().toString(), 
									getLocationStringFromMap, 
									null) == true) {
					Toast.makeText(getApplicationContext(), "Thêm thành công !",
							Toast.LENGTH_LONG).show();
					pl.setMa_parking(getLocationStringFromMap);
					pl.setSdt(etSoDT.getText().toString());
					pl.setDiachi(etSonha.getText() + " - " + etDuong.getText() + " - " + etPhuong.getText() + " - " + etQuan.getText());
					pl.setLike(1);					
					pl.setDislike(0);
					pl.setVitri(getLocationStringFromMap);
					pl.setTen_parking(etTen.getText().toString());
					pl.setTong_socho(etTongsocho.getText().toString());
					GlobaVariables.listParking.add(pl);				
					Intent t = new Intent(getApplicationContext(),
							MainActivity.class);
					t.putExtra("comeBackID", getLocationStringFromMap);
					startActivity(t);
				} else {
					Toast.makeText(getApplicationContext(), "Thất bại !",
							Toast.LENGTH_LONG).show();
				}
				
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK) {
			Bundle extras = intent.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			showParkingImage.setImageBitmap(imageBitmap);

		}
	}
}
