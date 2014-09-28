package mhst.parkingmap;

import java.util.List;

import Entity.ParkingLocation;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchCursorAdapter extends CursorAdapter{
	private List<ParkingLocation> lp;
	private TextView text;

	public SearchCursorAdapter(Context context, Cursor c, List<ParkingLocation> lp) {
		super(context, c, false);
		this.lp = lp;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		text.setText(cursor.getString(cursor.getColumnIndex("")));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
        View view = inflater.inflate(R.layout.thi, parent, false);
 
        text = (TextView) view.findViewById(R.id.item);
 
        return view;
	}


}
