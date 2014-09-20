package mhst.parkingmap;

import java.util.List;


import Entity.ParkingLocation;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ParkingItemAdapter extends ArrayAdapter<ParkingLocation>{
	
	private final Context context;
    int layoutResourceId;
    private final List<ParkingLocation> data;

    public ParkingItemAdapter(Context context, List<ParkingLocation> objects) {
		super(context, R.layout.window_info_layout, objects);
		this.context = context;
		this.data = objects;
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.window_info_layout, parent, false);
		    TextView tvTen = (TextView) rowView.findViewById(R.id.tvTen);
		    TextView tvDiachi = (TextView) rowView.findViewById(R.id.tvDiachi);
		    ImageView ivAnh = (ImageView) rowView.findViewById(R.id.ivAnh);
		    tvTen.setText(data.get(position).getTen_parking());
		    tvDiachi.setText(data.get(position).getDiachi());
		    // Change the icon for Windows and iPhone
		    ivAnh.setImageResource(R.drawable.giuxe);

		    return rowView;
	}

}
