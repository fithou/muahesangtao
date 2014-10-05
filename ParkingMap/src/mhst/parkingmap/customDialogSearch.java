package mhst.parkingmap;

import java.util.ArrayList;





import Globa.GlobaVariables;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class customDialogSearch extends Dialog {
	Context context;
	ArrayAdapter<String> adapter;
	int etClick;

	public customDialogSearch(Context context, int etClick) {
		super(context);
		this.context = context;
		this.etClick = etClick;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		/*
		 * TẠO Adapter cho cho các AutoCompleteTextView
		 */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_search_layout);
		EditText etCustomDialog = (EditText) findViewById(R.id.evCustomDialog);
		ListView lv = (ListView) findViewById(R.id.listSuggest);
		lv.setTextFilterEnabled(true);
		switch (etClick) {
		case R.id.etDuong:
			this.setTitle("Tên đường");
			adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, new ArrayList<String>(
							GlobaVariables.getDuong));
			lv.setAdapter(adapter);
			break;
		case R.id.etPhuong:
			this.setTitle("Tên phường");
			adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, new ArrayList<String>(
							GlobaVariables.getPhuong));
			lv.setAdapter(adapter);
			break;
		case R.id.etQuan:
			this.setTitle("Tên quận");
			adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, new ArrayList<String>(
							GlobaVariables.getQuan));
			lv.setAdapter(adapter);
			break;
		case R.id.etThanhpho:
			this.setTitle("Tên thành phố");
			adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, new ArrayList<String>(
							GlobaVariables.getTinhthanh));
			lv.setAdapter(adapter);
			break;

		default:
			break;
		}

		etCustomDialog.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				adapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

}
