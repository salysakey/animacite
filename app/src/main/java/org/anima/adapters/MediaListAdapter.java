package org.anima.adapters;
import org.anima.animacite.R;

import java.util.List;

import org.anima.entities.Author;
import org.anima.entities.Media;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Populates the medialist view
 * @author Momo
 *
 */
public class MediaListAdapter extends ArrayAdapter<Media> {

	Context context;

	public MediaListAdapter(Context context, int resourceId, List<Media> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		//UpdatableImageView updatableImageView;
		ImageView updatableImageView;
		TextView txtTitle;
		TextView txtdescriptif;
		TextView author;
	}

	
	//create the view from the media data 
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		//get the media from the position
		Media rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item1, null);
			holder = new ViewHolder();
			//holder.txtTitle = (TextView) convertView
			//		.findViewById(R.id.mediaListItemTitle);

			//holder.updatableImageView.setImageView(((ImageView) convertView.findViewById(R.id.mediaListItemImage)));
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		String names = "";
		if (rowItem.getAuthors() != null) {
			for (Author a : rowItem.getAuthors()) {
				names += " " + a.getName();

			}
		}

		holder.author.setText(names);
		holder.txtTitle.setText(rowItem.getTitre());
		holder.txtdescriptif.setText(rowItem.getDescription());
		holder.updatableImageView.setImageResource(rowItem.getId());
		//holder.updatableImageView.loadImage(rowItem.getVignetteUrl());

		return convertView;
	}

	private Bitmap convert(int id ){

		Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),
				id );

		return bitmap;
	}

}