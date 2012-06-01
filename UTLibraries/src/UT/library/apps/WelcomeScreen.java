package UT.library.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WelcomeScreen extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.main);

		String[] features = { "Search Catalog", "Library Hours", "Settings", "Reserve Study Room", "Checked Out Books" };
		
		setContentView(R.layout.main2);

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
		case 0:
			intent = new Intent(this, searchInputScreen.class);
			break;
		case 1:
			intent = new Intent(this, showLibraryTimings.class);
			break;
		case 2:
			intent = new Intent(this, settings.class); break;
		case 3: intent = new Intent(this,reserveStudyRoom.class); break;
		case 4: intent = new Intent(this, renewBooks.class);break;

		}
		if(intent!=null)
		startActivity(intent);

	}
}