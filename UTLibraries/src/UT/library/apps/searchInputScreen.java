package UT.library.apps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class searchInputScreen extends Activity {

	Context context;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		if (!shared.connectedToInternet)
		{
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(this, "This feature requires an internet connection. Please try again later.", duration);
			toast.show();
		}


		//      TextView ttest = new TextView(this);
		setContentView(R.layout.search_input_header);

		//		LinearLayout linlayout = (LinearLayout) findViewById(R.id.searchInputHeader);
		//		View searchType = (Layout) getResources().getLayout(R.layout.advanced_search_input);
		//		linlayout.addView(searchType);

		//code downloaded from https://github.com/johannilsson/android-actionbar/blob/master/README.md
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Catalog Search");
		actionBar.setHomeAction(new IntentAction(this,new Intent(this, WelcomeScreen.class) , R.drawable.home)); //go home (already there)
		actionBar.addAction(new IntentAction(this, new Intent(this, settings.class), R.drawable.gear)); //go to settings


		Spinner searchType = (Spinner) findViewById(R.id.searchInputSpinner);
		searchType.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Intent intent=null;
				switch(position){
				case 0:
					intent = new Intent(context, searchInputScreen_advanced.class); break;
				}
				if (intent!=null)
					startActivity(intent);
				//TODO: change search input based on item position selected

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}});
	}
}