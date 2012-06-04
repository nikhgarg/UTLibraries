package UT.library.apps;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class renewBooks extends Activity {

	DefaultHttpClient client;
	ArrayList<cBook> cbooks;

	public void displayCheckedOutBooks() {

		// log into UT library account
		client = new DefaultHttpClient();
		shared.logIntoCatalog(this, client);

		// get HTML for page
		String html = shared.retrieveProtectedWebPage(this,client,
		"https://catalog.lib.utexas.edu/patroninfo~S29/1160546/items");
		Log.i("renewBooks", "html: " + html);

		// store HTML into some sort of structure (with links and all)
		cbooks = new ArrayList<cBook>();
		cbooks = parseCheckedOut.parseCheckedOutBooks(html);
		Log.i("renewBooks", "checked out books: " + cbooks.toString());

		// actually display books (use listview);
		ListView listview = (ListView) findViewById(R.id.checkedOutListView);
		listview.setAdapter(new cBookBaseAdapter(this, cbooks));

	}

	public void renewMarkedBooks(View view) {
		try {

			for (int i = 0; i < cbooks.size(); i++)
				Log.i("renewBooks", "book: " + cbooks.get(i).title
						+ " renew?: " + cbooks.get(i).renew);

			
			HttpPost httppost = new HttpPost(
			"https://catalog.lib.utexas.edu/patroninfo~S29/1160546/items");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			
			//renew books whose values are marked
			for (int i = 0; i < cbooks.size(); i++) {
				cBook b = cbooks.get(i);
				if (b.renew)
					nameValuePairs.add(new BasicNameValuePair("renew" + i,
							b.renewValue));

			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.ASCII));
			HttpResponse response = client.execute(httppost);
			// update listview after renewing books
			displayCheckedOutBooks();
			
		} catch (Exception e) {
			Log.i("renewBooks",
					"exception in renewMarkedBooks: " + e.toString());
		}

	}
	public void renewAllBooks(View view)
	{
		for (int i = 0; i < cbooks.size(); i++) {
			cBook b = cbooks.get(i);
			b.renew = true;
		}
		renewMarkedBooks(view);
	}
	
	public void goHome(View view)
	{
    	Intent intent = new Intent(this, WelcomeScreen.class);
    	startActivity(intent);

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set Content view
		setContentView(R.layout.renew_books);

		displayCheckedOutBooks();

	}

}
