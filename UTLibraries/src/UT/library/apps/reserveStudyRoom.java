package UT.library.apps;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

public class reserveStudyRoom extends Activity {

	Calendar cal;

	int sMonth;
	int sDay;
	int sYear;
	int sStartHour;
	int sEndHour;
	int sStartMinute;
	int sEndMinute;
	
	int numberOfPeople = 0; //index entry from Spinner

	boolean[] buildingChoice = new boolean[4];// { anyBuilding, finearts,
	// PCLGroup, PCLPresentation};
	boolean[] roomRequirementsChoice = new boolean[4]; //wireless internet, ethernet jacks, power outlets, whiteboard

	public void enterRoomSearch(View view)
	{
		Spinner spinner = (Spinner) findViewById(R.id.numberPeopleSpinner);
		numberOfPeople = spinner.getSelectedItemPosition();
		
    	Intent intent = new Intent(this, displayRoomResults.class);
    	Bundle bundle = new Bundle();
    	bundle.putIntArray("Date", new int[]{sYear, sMonth, sDay, sStartHour, sStartMinute, sEndHour, sEndMinute});
    	bundle.putInt("number", numberOfPeople);
    	bundle.putBooleanArray("building", buildingChoice);
    	bundle.putBooleanArray("room", roomRequirementsChoice);
    	intent.putExtras(bundle);
    	startActivity(intent);
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reserve_room_input2);
		cal = Calendar.getInstance();
		sMonth = cal.get(Calendar.MONTH); // getting current date and time for
		// default pick
		sDay = cal.get(Calendar.DAY_OF_MONTH);
		sYear = cal.get(Calendar.YEAR);
		sStartHour = cal.get(Calendar.HOUR_OF_DAY);
		sStartMinute = cal.get(Calendar.MINUTE);
		sEndHour = (sStartHour + 1) % 24;
		sEndMinute = sStartMinute;
		buildingChoice[0] = true; // default is any building


	}
	
	public void launchDateDialog(View view) {
		showDialog(0);
	}

	public void launchStartTimeDialog(View view) {
		showDialog(1);
	}

	public void launchEndTimeDialog(View view) {
		showDialog(2);
	}

	public void launchBuildingDialog(View view) {
		showDialog(3);
	}
	public void launchRoomDialog(View view) {
		showDialog(4);
	}


	private DatePickerDialog.OnDateSetListener sDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			sYear = year;
			sMonth = monthOfYear;
			sDay = dayOfMonth;
		}
	};
	private TimePickerDialog.OnTimeSetListener sStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			sStartHour = hourOfDay;
			sStartMinute = minute;
		}
	};
	private TimePickerDialog.OnTimeSetListener sEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			sEndHour = hourOfDay;
			sEndMinute = minute;
		}
	};
	private DialogInterface.OnMultiChoiceClickListener sBuildingSetListener = new DialogInterface.OnMultiChoiceClickListener() {
		public void onClick(DialogInterface dialog, int item, boolean isChecked) {
			buildingChoice[item] = isChecked;
		}
	};
	private DialogInterface.OnMultiChoiceClickListener sRoomRequirementsListener = new DialogInterface.OnMultiChoiceClickListener() {
		public void onClick(DialogInterface dialog, int item, boolean isChecked) {
			roomRequirementsChoice[item] = isChecked;
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			return new DatePickerDialog(this, sDateSetListener, sYear, sMonth,
					sDay);
		case 1:
			return new TimePickerDialog(this, sStartTimeSetListener,
					sStartHour, sStartMinute, false);
		case 2:
			return new TimePickerDialog(this, sEndTimeSetListener, sEndHour,
					sEndMinute, false);
		case 3:
			AlertDialog.Builder ald = new AlertDialog.Builder(this);
			ald.setTitle("Please Enter your Room Choice");
			ald.setMultiChoiceItems(R.array.reserveRoomBuildingNames,buildingChoice,sBuildingSetListener);
			ald.setPositiveButton("Done", new OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			return ald.create();
		case 4:
			AlertDialog.Builder ald2 = new AlertDialog.Builder(this);
			ald2.setTitle("Please Enter Room Requirements");
			ald2.setMultiChoiceItems(R.array.reserveRoomRoomRequirements,roomRequirementsChoice,sRoomRequirementsListener);
			ald2.setPositiveButton("Done", new OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			return ald2.create();

		}
		return null;
	}

}