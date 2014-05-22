package edu.upc.eetac.dsa.ferrandiaz.library.android;

import java.util.ArrayList;

import edu.upc.eetac.dsa.ferrandiaz.library.android.api.Book;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.BookCollection;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.LibraryAPI;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.LibraryAndroidException;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.Review;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.ReviewCollection;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class ReviewListActivity extends ListActivity {
	private ArrayList<Review> reviewList;
	private ReviewAdapter adapter;
	

	private final static String TAG = ReviewListActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_layout);
		String urlReview = (String) getIntent().getExtras().get("url");
		// System.out.println(urlReview);
		reviewList = new ArrayList<>();
		adapter = new ReviewAdapter(this, reviewList);
		setListAdapter(adapter);
		(new FetchReviewsTask()).execute(urlReview);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Review review = reviewList.get(position);
		System.out.println(review.getLinks().get("self").getTarget());
		Log.d(TAG, review.getLinks().get("self").getTarget());

		Intent intent = new Intent(this, ReviewDetailActivity.class);
		intent.putExtra("url", review.getLinks().get("self").getTarget());

		startActivity(intent);

	}

	private void addReviews(ReviewCollection reviews) {
		reviewList.addAll(reviews.getReviews());
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.review_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miWrite:
			String urlReview = (String) getIntent().getExtras().get("url");
			Intent intent = new Intent(this, WriteReviewActivity.class);
			intent.putExtra("url", urlReview);
			startActivity(intent);

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private class FetchReviewsTask extends
			AsyncTask<String, Void, ReviewCollection> {
		private ProgressDialog pd;

		@Override
		protected ReviewCollection doInBackground(String... params) {
			ReviewCollection reviews = null;
			try {
				reviews = LibraryAPI.getInstance(ReviewListActivity.this)
						.getReviews(params[0]);
			} catch (LibraryAndroidException e) {
				e.printStackTrace();
			}
			return reviews;
		}

		@Override
		protected void onPostExecute(ReviewCollection result) {
			addReviews(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ReviewListActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
}
