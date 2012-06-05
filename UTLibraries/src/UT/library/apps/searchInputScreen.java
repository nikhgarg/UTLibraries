package UT.library.apps;


import android.graphics.Color;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

//import UT.library.apps.showLibraryTimings.RefreshAction;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class searchInputScreen extends Activity {

	public void onGoButtonClicked(View v) {
		TextView ttest = new TextView(this);
		SearchData fieldsData = new SearchData();

		EditText getEditText = (EditText) findViewById(R.id.searchstringInput);
		fieldsData.searchString = getEditText.getText().toString();

		getEditText = (EditText) findViewById(R.id.publisherInput);
		CheckBox checkbox = (CheckBox) findViewById(R.id.publisherbox);
		fieldsData.usePublisher = checkbox.isChecked();
		fieldsData.publisher= getEditText.getText().toString();

		getEditText = (EditText) findViewById(R.id.yearstartInput);
		checkbox = (CheckBox) findViewById(R.id.yearstartbox);
		fieldsData.useYearStart = checkbox.isChecked();
		if(fieldsData.useYearStart)fieldsData.yearStart= Integer.parseInt(getEditText.getText().toString());

		getEditText = (EditText) findViewById(R.id.yearendInput);
		checkbox = (CheckBox) findViewById(R.id.yearendbox);
		fieldsData.useYearEnd = checkbox.isChecked();
		if (fieldsData.useYearEnd)fieldsData.yearEnd = Integer.parseInt(getEditText.getText().toString());

		//input from Spinners
		Spinner spinner = (Spinner) findViewById(R.id.fieldtypeInput);
		fieldsData.fieldType = spinner.getSelectedItemPosition();
		spinner = (Spinner) findViewById(R.id.languageInput);
		fieldsData.language = spinner.getSelectedItemPosition();
		spinner = (Spinner) findViewById(R.id.locationInput);
		fieldsData.location = spinner.getSelectedItemPosition();
		spinner = (Spinner) findViewById(R.id.materialtypeInput);
		fieldsData.materialType = spinner.getSelectedItemPosition();
		spinner = (Spinner) findViewById(R.id.searchandsortInput);
		fieldsData.searchAndSort = spinner.getSelectedItemPosition();

		checkbox = (CheckBox) findViewById(R.id.onlyavailablebox);
		fieldsData.limitAvailable = checkbox.isChecked();

		//    	ttest.setText(fieldsData.searchString+"\t"+fieldsData.yearStart+"\t"+fieldsData.yearEnd);
		//    	setContentView(ttest);

		Intent intent = new Intent(this, displaySearchResults.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("fieldsData", fieldsData);
		intent.putExtras(bundle);
		startActivity(intent);
	}


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//      TextView ttest = new TextView(this);
		setContentView(R.layout.search);

		//code downloaded from https://github.com/johannilsson/android-actionbar/blob/master/README.md
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		// You can also assign the title programmatically by passing a
		// CharSequence or resource id.
		actionBar.setTitle("Advanced Search");
		actionBar.setBackgroundColor(Color.parseColor("#ff4500"));
		//	actionBar.setHomeLogo(R.drawable.book_image_placeholder);
		actionBar.setHomeAction(new IntentAction(this,new Intent(this, WelcomeScreen.class) , R.drawable.book_image_placeholder)); //go home (already there)
		actionBar.addAction(new IntentAction(this, new Intent(this, settings.class), R.drawable.book_image_placeholder)); //go to settings
		//		actionBar.addAction(new RefreshAction());

	}

}

