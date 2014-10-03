package mhst.parkingmap;

import java.io.File;
import java.net.URI;
import java.util.List;


import Entity.ParkingLocation;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
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
		File mediaStorageDir;
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT > 8) {
			mediaStorageDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		} else {
			mediaStorageDir = new File(
					Environment.getExternalStorageDirectory(), "Pictures");
		}
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.window_info_layout, parent, false);
		    TextView tvTen = (TextView) rowView.findViewById(R.id.tvTen);
		    TextView tvDiachi = (TextView) rowView.findViewById(R.id.tvDiachi);
		    ImageView ivAnh = (ImageView) rowView.findViewById(R.id.ivAnh);
		    tvTen.setText(data.get(position).getTen_parking());
		    tvDiachi.setText(data.get(position).getDiachi());
		    ivAnh.setImageResource(R.drawable.giuxe);
		    // Change the icon for Windows and iPhone
		    File file;
			file = getFileFromUri("file://" + mediaStorageDir.getPath() + "/" + data.get(position).getImageUri());
			Log.d("Url", data.get(position).getImageUri());
			if (file != null) {
				Log.d("File", file.getPath());
				Bitmap bm = decodeSampledBitmapFromFile(file, 500, 500);
				ivAnh.setImageBitmap(bm);
			}
		    

		    return rowView;
	}
	private Bitmap decodeSampledBitmapFromFile(File file, int reqWidth,
			int reqHeight) {
		// TODO Auto-generated method stub
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	}

	private File getFileFromUri(String imgUri) {
		// TODO Auto-generated method stub

		try {
			URI uri = URI.create(imgUri);
			File file = new File(uri);
			if (file != null) {
				if (file.canRead()) {
					return file;
				}
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	/** Calculate the scaling factor */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);

		return resizedBitmap;
	}

}
