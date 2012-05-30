package UT.library.apps;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;

public class renewBooks extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set Content view
		setContentView(R.layout.renew_books);
		//log into UT Direct
		DefaultHttpClient client = new DefaultHttpClient();
		shared.logIntoUTDirect(client);
		
		//get HTML for page
		
		//parse HTML and display
			//store HTML into some sort of structure (with links and all)
		
		
	}

}
