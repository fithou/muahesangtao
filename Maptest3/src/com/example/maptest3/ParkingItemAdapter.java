package com.example.maptest3;

import java.util.List;

import Entity.ObjectDrawerItem;
import Entity.ParkingLocation;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ParkingItemAdapter extends ArrayAdapter<ParkingLocation>{
	Context mContext;
    int layoutResourceId;
    ParkingLocation[] data;

	public ParkingItemAdapter(Context mContext, int layoutResourceId,
			ParkingLocation[] data) {
		super(mContext, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View listItem = convertView;
		 
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);
 
        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.parkingpic);
        TextView textViewName = (TextView) listItem.findViewById(R.id.parkinginfo);
        
        ParkingLocation folder = data[position];
 
        
        imageViewIcon.setImageResource(R.drawable.ic_action_about);
        textViewName.setText(folder.getTenbaidoxe());
        
        return listItem;
	}
	

}
