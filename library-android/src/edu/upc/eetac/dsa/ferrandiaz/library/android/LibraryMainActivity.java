package edu.upc.eetac.dsa.ferrandiaz.library.android;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

import edu.upc.eetac.dsa.ferrandiaz.library.android.api.Book;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.BookCollection;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.LibraryAPI;
import edu.upc.eetac.dsa.ferrandiaz.library.android.api.LibraryAndroidException;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class LibraryMainActivity extends ListActivity {
	/** Called when the activity is first created. */

	private ArrayList<Book> bookList;
	private BookAdapter adapter;
	private final static String TAG = LibraryMainActivity.class.toString();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_layout);
		bookList = new ArrayList<>();
		adapter = new BookAdapter(this, bookList);
		setListAdapter(adapter);
		String Title = (String) getIntent().getExtras().get("title");
		String Author = (String) getIntent().getExtras().get("author");
		(new FetchBooksTask()).execute(Title, Author);
	}

	

	private void addBooks(BookCollection books){
		bookList.addAll(books.getBooks());
		adapter.notifyDataSetChanged();
		
		
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Book book = bookList.get(position);
		Log.d(TAG, book.getLinks().get("self").getTarget());
		
		Intent intent = new Intent(this, BookDetailActivity.class);
		intent.putExtra("url", book.getLinks().get("self").getTarget());
		startActivity(intent);
	

		
	}
	
	private class FetchBooksTask extends AsyncTask<String, Void, BookCollection> {
		private ProgressDialog pd;

		@Override
		protected BookCollection doInBackground(String... params) {
			BookCollection Books = null;
			try {
				Books = LibraryAPI.getInstance(LibraryMainActivity.this)
						.searchBooks(params[0], params[1]);
			} catch (LibraryAndroidException e) {
				e.printStackTrace();
			}
			return Books;
		}

		@Override
		protected void onPostExecute(BookCollection result) {
			addBooks(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(LibraryMainActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
}
