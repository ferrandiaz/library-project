package edu.upc.eetac.dsa.ferrandiaz.library.android;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchBookActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_book_layout);
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("test2", "test2".toCharArray());
			}
		});
	}
	
	public void searchBooks(View v){
		
		EditText etTitle = (EditText) findViewById(R.id.etTitle);
		EditText etAuthor = (EditText) findViewById(R.id.etAuthor);
		String author = etAuthor.getText().toString();
		String title = etTitle.getText().toString();
		Intent intent = new Intent(this, LibraryMainActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("author", author);
		startActivity(intent);
		
	}

}
