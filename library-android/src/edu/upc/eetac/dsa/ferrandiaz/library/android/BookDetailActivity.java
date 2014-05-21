package edu.upc.eetac.dsa.ferrandiaz.library.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.upc.eetac.dsa.ferrandiaz.library.android.api.Book;
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
import android.view.View;
import android.widget.TextView;

public class BookDetailActivity extends Activity {
	

	private final static String TAG = BookDetailActivity.class.getName();
	private static String urlReview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_detail_layout);
		String urlBook = (String) getIntent().getExtras().get("url");
		urlReview = urlBook;
		(new FetchBookTask()).execute(urlBook);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.library_actions, menu);
		return true;
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miReview:

			Intent intent = new Intent(this, ReviewListActivity.class);
			urlReview = urlReview + "/review";
			System.out.println(urlReview);
			intent.putExtra("url", urlReview);
			startActivity(intent);
			
			
			
			return true;
	 
		default:
			return super.onOptionsItemSelected(item);
		}
	 
	}
	
	private void loadBook(Book book) {
		TextView tvDetailTitle = (TextView) findViewById(R.id.tvDetailTitle);
		TextView tvDetailAuthor = (TextView) findViewById(R.id.tvDetailAuthor);
		TextView tvDetailEditorial = (TextView) findViewById(R.id.tvDetailEditorial);
		TextView tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
	 
		tvDetailTitle.setText(book.getTitle());
		tvDetailAuthor.setText(book.getAuthor());
		tvDetailEditorial.setText(book.getEditorial());
		tvDetailDate.setText(book.getEd_date());
	}
	
	
	private class FetchBookTask extends AsyncTask<String, Void, Book> {
		private ProgressDialog pd;
	 
		@Override
		protected Book doInBackground(String... params) {
			Book book = null;
			try {
				book = LibraryAPI.getInstance(BookDetailActivity.this)
						.getBook(params[0]);
			} catch (LibraryAndroidException e) {
				
				e.printStackTrace();
			}
			return book;
		}
	 
		@Override
		protected void onPostExecute(Book result) {
			loadBook(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(BookDetailActivity.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
	
	
 
}
