package UT.library.apps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class searchInputScreen extends Activity {

	Context context;
	LayoutInflater mInflater;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		mInflater = LayoutInflater.from(context);
		View current;
		if (!shared.connectedToInternet)
		{
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(this, "This feature requires an internet connection. Please try again later.", duration);
			toast.show();
		}


		//      TextView ttest = new TextView(this);
		setContentView(R.layout.search_input_header);

		final FrameLayout frame = (FrameLayout) findViewById(R.id.frame);

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
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				//				if (current!=null) removeView(current);
				int count = frame.getChildCount();
				View child;
				//really bad way to do it, need to do with visibility
				for (int i=0;i<count;i++)
				{
					 child = frame.getChildAt(i);
					if(child!=null)
						child.setVisibility(View.GONE);
				}
				switch(position){
				case 0:
					mInflater.inflate(R.layout.advanced_search_input, frame, true); break;
				case 1:
					mInflater.inflate(R.layout.search_input_numbers, frame,true);break;
				case 2:
					mInflater.inflate(R.layout.search_input_simple, frame,true);break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}});
	}
}