package UT.library.apps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class renewBooks extends Activity {

	DefaultHttpClient client;
	ArrayList<cBook> cbooks;

	public void displayCheckedOutBooks() {
		// log into UT library account
		client = new DefaultHttpClient();
		shared.logIntoCatalog(client);
		// get HTML for page
		String html = shared.retrieveProtectedWebPage(client,
				"https://catalog.lib.utexas.edu/patroninfo~S29/1160546/items");
		Log.i("renewBooks", "html: " + html);
		// store HTML into some sort of structure (with links and all)
		cbooks = new ArrayList<cBook>();
		cbooks = parseCheckedOut.parseCheckedOutBooks(html);
		Log.i("renewBooks", "checked out books: " + cbooks.toString());

		// actually display books (use listview);
		
//		TextView ttest = new TextView(this);
//		ttest.setText("checked out books: " + cbooks.toString());
//		setContentView(ttest);

		ListView listview = (ListView) findViewById(R.id.checkedOutListView);
		listview.setAdapter(new cBookBaseAdapter(this, cbooks));

	}

	public void renewMarkedBooks(View view) {
		try {

			// fix so books that are checked out are not hardcoded

			HttpPost httppost = new HttpPost(
					"https://catalog.lib.utexas.edu/patroninfo~S29/1160546/items");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("renew0", "i17766174"));
			nameValuePairs.add(new BasicNameValuePair("renew1", "i14030609"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.ASCII));
			HttpResponse response = client.execute(httppost);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String next = in.readLine();

			// update listview after renewing books
		} catch (Exception e) {
			Log.i("renewBooks",
					"exception in renewMarkedBooks: " + e.toString());
		}

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set Content view
		setContentView(R.layout.renew_books);

		displayCheckedOutBooks();

		// parse HTML and display

	}

}
