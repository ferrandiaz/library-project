package edu.upc.eetac.dsa.ferrandiaz.library.android;


import edu.upc.eetac.dsa.ferrandiaz.library.android.api.LibraryAPI;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.LibraryAndroidException;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.Review;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class WriteReviewActivity extends Activity {
	private final static String TAG = WriteReviewActivity.class.getName();
	 
	private class PostReviewTask extends AsyncTask<String, Void, Review> {
		private ProgressDialog pd;
 
		@Override
		protected Review doInBackground(String... params) {
			Review review = null;
			try {
				review = LibraryAPI.getInstance(WriteReviewActivity.this)
						.createReview(params[0], params[1]);
			} catch (LibraryAndroidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return review;
		}
 
		@Override
		protected void onPostExecute(Review result) {
			showReviews();
			if (pd != null) {
				pd.dismiss();
			}
		}
 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(WriteReviewActivity.this);
 
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	}
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_review_layout);
		
 
	}
 
	public void cancel(View v) {
		finish();
	}
 
	public void postReview(View v) {
		EditText etReview = (EditText) findViewById(R.id.etReview);
 
		String review = etReview.getText().toString();
		String urlReview = (String) getIntent().getExtras().get("url");
		(new PostReviewTask()).execute(urlReview, review);
	}
 
	private void showReviews() {
		Intent intent = new Intent(this, ReviewListActivity.class);
		String urlReview = (String) getIntent().getExtras().get("url");
		intent.putExtra("url", urlReview);
		startActivity(intent);
	}
 
}