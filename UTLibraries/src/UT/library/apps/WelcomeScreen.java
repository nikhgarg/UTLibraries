package UT.library.apps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class WelcomeScreen extends Activity {

	Context context;

	public void onResume()
	{
		super.onResume();
	}
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		// setContentView(R.layout.main);

		String[] features = { "Search Catalog", "Library Hours", "Reserve Study Room", "Checked Out Books", "Saved Books", "Library Maps" };

		setContentView(R.layout.main2);

		//code downloaded from https://github.com/johannilsson/android-actionbar/blob/master/README.md
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		// You can also assign the title programmatically by passing a
		// CharSequence or resource id.
		actionBar.setTitle("\t\t\tUTLibraries");
		actionBar.setGravity(Gravity.CENTER);
		actionBar.setHomeLogo(R.drawable.home);
		actionBar.addAction(new IntentAction(this, new Intent(this, settings.class), R.drawable.gear)); //go to settings
		//----------------------

		if (shared.checkInternetConnection(this, !shared.displayedInternetCheck, "You are not connected to the internet. App functionality will be limited."))
			shared.checkLogInCredentials(this, !shared.displayedLogInCheck);

		ListView listview = (ListView) findViewById(R.id.mainPageListView);
		listview.setAdapter(new ArrayAdapter<String>(this,R.layout.main_list_item, features));

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				launchActivity(position);

			}
		});

		// GridView gridview = (GridView) findViewById(R.id.gridview);
		// gridview.setAdapter(new ImageAdapter(this));
		//
		// gridview.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View v, int position,
		// long id) {
		// // Toast.makeText(WelcomeScreen.this, "" + position,
		// Toast.LENGTH_SHORT).show();
		// launchActivity(position);
		// }
		// });
	}

	public void launchActivity(int position) {

		Intent intent=null;
		switch (position) {
		case 0:	intent = new Intent(this, searchInputScreen.class);	break;
		case 1: intent = new Intent(this, showLibraryTimings.class);break;
		//		case 2:	intent = new Intent(this, settings.class); break;
		case 2: intent = new Intent(this,reserveStudyRoom.class); break;
		case 3: intent = new Intent(this, renewBooks.class);break;
		case 4: intent = new Intent(this, saveBooks.class);break;
		case 5: intent = new Intent(this, libraryMaps.class); break;

		}
		if(intent!=null)
			startActivity(intent);

	}
}