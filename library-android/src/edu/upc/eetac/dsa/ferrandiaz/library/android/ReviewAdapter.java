package edu.upc.eetac.dsa.ferrandiaz.library.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.upc.eetac.dsa.ferrandiaz.library.android.api.Book;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.Review;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ReviewAdapter extends BaseAdapter {
	private ArrayList<Review> data;
	private LayoutInflater inflater;

	public ReviewAdapter(Context context, ArrayList<Review> data) {
		super();
		inflater = LayoutInflater.from(context);
		this.data = data;
	}
	
	private static class ViewHolder {
		TextView tvUsername;
		TextView tvReviewDate;
	}


	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(((Review)getItem(position)).getReviewid());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_review, null);
			viewHolder = new ViewHolder();
			viewHolder.tvUsername = (TextView) convertView
					.findViewById(R.id.tvUsername);
			viewHolder.tvReviewDate = (TextView) convertView
					.findViewById(R.id.tvReviewDate);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String username = data.get(position).getUsername();
		String date = SimpleDateFormat.getInstance().format(
				data.get(position).getLastModified());
		viewHolder.tvUsername.setText(username);
		viewHolder.tvReviewDate.setText(date);
		return convertView;
	}
	
	
}
