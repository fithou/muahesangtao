package mhst.parkingmap;

import java.io.File;
import java.net.URI;
import java.util.List;

import Entity.ParkingLocation;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
<<<<<<< HEAD
import android.os.Build;
import android.os.Environment;
=======
>>>>>>> origin/master
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookmarkItemAdapter extends ArrayAdapter<ParkingLocation> {

	private final Context context;
	int layoutResourceId;
	private final List<ParkingLocation> data;

	public BookmarkItemAdapter(Context context, List<ParkingLocation> objects) {
		super(context, R.layout.window_info_layout, objects);
		this.context = context;
		this.data = objects;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
<<<<<<< HEAD
		File mediaStorageDir;
		
		if (Build.VERSION.SDK_INT > 8) {
			mediaStorageDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		} else {
			mediaStorageDir = new File(
					Environment.getExternalStorageDirectory(), "Pictures");
		}
=======
>>>>>>> origin/master
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.window_info_layout, parent,
				false);
		TextView tvTen = (TextView) rowView.findViewById(R.id.tvTen);
		TextView tvDiachi = (TextView) rowView.findViewById(R.id.tvDiachi);
		ImageView ivAnh = (ImageView) rowView.findViewById(R.id.ivAnh);
		tvTen.setText(data.get(position).getTen_parking());
		tvDiachi.setText(data.get(position).getDiachi());
		// Change the icon for Windows and iPhone
		
		ivAnh.setImageResource(R.drawable.giuxe);
<<<<<<< HEAD
		File file = getFileFromUri("file://" + mediaStorageDir.getPath() + "/" + data.get(position).getImageUri());
=======
		File file = getFileFromUri(data.get(position).getImageUri());
>>>>>>> origin/master

		if (file != null) {
			Bitmap bm = decodeSampledBitmapFromFile(file, 500, 500);
			ivAnh.setImageBitmap(bm);
		}
		return rowView;
	}
<<<<<<< HEAD
	
	/*
	 * Chuyển file sang dạng Bitmap
	 */
=======

>>>>>>> origin/master
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
<<<<<<< HEAD
	
	/*
	 * Tạo file từ Uri
	 */
=======

>>>>>>> origin/master
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

<<<<<<< HEAD
	/*
	 * Định lại kích thước file ảnh
	 */
=======
	/** Calculate the scaling factor */
>>>>>>> origin/master
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

}
