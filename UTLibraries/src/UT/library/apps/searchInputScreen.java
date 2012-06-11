package UT.library.apps;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

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
		spinner = (Spinner) findViewById(R.id.locationInput);
		fieldsData.location = spinner.getSelectedItemPosition();
		spinner = (Spinner) findViewById(R.id.searchandsortInput);
		fieldsData.searchAndSort = spinner.getSelectedItemPosition();


		fieldsData.language = languageType;
		fieldsData.materialType = materialType;
		fieldsData.langLength = languageType.length;
		fieldsData.matLength = materialType.length;

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

	public void chooseMaterialType(View view){
		showDialog(0);
	}
	public void chooseLanguage(View view){
		showDialog(1);
	}
	boolean[] materialType;
	boolean[] languageType;

	private DialogInterface.OnMultiChoiceClickListener MaterialTypeListener = new DialogInterface.OnMultiChoiceClickListener() {
		public void onClick(DialogInterface dialog, int item, boolean isChecked) {
			materialType[item] = isChecked;
		}
	};
	private DialogInterface.OnMultiChoiceClickListener LanguageListener = new DialogInterface.OnMultiChoiceClickListener() {
		public void onClick(DialogInterface dialog, int item, boolean isChecked) {
			languageType[item] = isChecked;
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case 0:
			AlertDialog.Builder ald = new AlertDialog.Builder(this);
			ald.setTitle("Please Enter Material Type");
			ald.setMultiChoiceItems(R.array.MaterialTypeTable, materialType,MaterialTypeListener);
			ald.setPositiveButton("Done", new OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			return ald.create();
		case 1:
			AlertDialog.Builder ald2 = new AlertDialog.Builder(this);
			ald2.setTitle("Please Enter Language");
			ald2.setMultiChoiceItems(R.array.LanguageTable,languageType,LanguageListener);
			ald2.setPositiveButton("Done", new OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			return ald2.create();

		}
		return null;
	}



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Resources res = getResources();
		materialType = new boolean [res.getStringArray(R.array.MaterialTypeTable).length];
		languageType = new boolean [res.getStringArray(R.array.LanguageTable).length];
		materialType[0] = languageType[0] = true; //setting anylanguage and anymaterialtype true by default


		//      TextView ttest = new TextView(this);
		setContentView(R.layout.advanced_search_input);

		//code downloaded from https://github.com/johannilsson/android-actionbar/blob/master/README.md
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Advanced Search");
		actionBar.setHomeAction(new IntentAction(this,new Intent(this, WelcomeScreen.class) , R.drawable.home)); //go home (already there)
		actionBar.addAction(new IntentAction(this, new Intent(this, settings.class), R.drawable.gear)); //go to settings
	}
}

