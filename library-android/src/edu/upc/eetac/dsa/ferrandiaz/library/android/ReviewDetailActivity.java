package edu.upc.eetac.dsa.ferrandiaz.library.android;

import java.text.SimpleDateFormat;

import edu.upc.eetac.dsa.ferrandiaz.library.android.api.LibraryAPI;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.LibraryAndroidException;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.Review;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ReviewDetailActivity extends Activity {
	private final static String TAG = ReviewDetailActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_detail_layout);
		String urlReview = (String) getIntent().getExtras().get("url");
		(new FetchReviewTask()).execute(urlReview);

	}

	private void loadReview(Review review) {
		TextView tvDetailReview = (TextView) findViewById(R.id.tvDetailReview);
		TextView tvDetailUsername = (TextView) findViewById(R.id.tvDetailUsername);
		TextView tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);

		tvDetailReview.setText(review.getReview());
		tvDetailUsername.setText(review.getUsername());
		tvDetailDate.setText(SimpleDateFormat.getInstance().format(
				review.getLastModified()));
	}

	private class FetchReviewTask extends AsyncTask<String, Void, Review> {
		private ProgressDialog pd;

		@Override
		protected Review doInBackground(String... params) {
			Review review = null;
			try {
				review = LibraryAPI.getInstance(ReviewDetailActivity.this)
						.getReview(params[0]);
			} catch (LibraryAndroidException e) {

				e.printStackTrace();
			}
			return review;
		}

		@Override
		protected void onPostExecute(Review result) {
			loadReview(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ReviewDetailActivity.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
}
