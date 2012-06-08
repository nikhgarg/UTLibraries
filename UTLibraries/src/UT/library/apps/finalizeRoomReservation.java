package UT.library.apps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class finalizeRoomReservation extends Activity {

	String roomId = "";
	String date = "";
	String groupName;
	String startHour;
	String startMinute;
	String startPM;
	String endHour;
	String endMinute;
	String endPM;
	String room;
	String location;
	boolean success;
	Context context;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.finalize_room_reservation);
		context = this;
		Bundle bundle = getIntent().getExtras();

		// get times, date, room id from previous screen

		roomId = bundle.getString("roomID");
		date = String.format("%s-%s-%s", bundle.getString("year"),
				bundle.getString("month"), bundle.getString("day"));
		location = bundle.getString("location");
		room = bundle.getString("room");

	}

	public void finalizeReservation(View view) {

		EditText et = (EditText) findViewById(R.id.groupNameEnter);
		 groupName = et.getText().toString();
		Spinner spinner = (Spinner) findViewById(R.id.startHourSpinner);
		 startHour = 1 + spinner.getSelectedItemPosition() + ""; // time
		// is 0
		// indexed
		spinner = (Spinner) findViewById(R.id.endHourSpinner);
		 endHour = 1 + spinner.getSelectedItemPosition() + ""; // time is
		// 0
		// indexed
		spinner = (Spinner) findViewById(R.id.startMinuteSpinner);
		 startMinute = 15 * spinner.getSelectedItemPosition() + ""; // time
		// is
		// 0
		// indexed
		spinner = (Spinner) findViewById(R.id.endMinuteSpinner);
		 endMinute = 15 * spinner.getSelectedItemPosition() + ""; // time
		// is 0
		// indexed
		spinner = (Spinner) findViewById(R.id.startPMSpinner);
		 startPM = (spinner.getSelectedItemPosition() == 0) ? "PM" : "AM";
		spinner = (Spinner) findViewById(R.id.endPMSpinner);
		 endPM = (spinner.getSelectedItemPosition() == 0) ? "PM" : "AM";

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs
		.add(new BasicNameValuePair("reservationName", groupName));
		nameValuePairs.add(new BasicNameValuePair("startHour", startHour));
		nameValuePairs.add(new BasicNameValuePair("startMinute", startMinute));
		nameValuePairs.add(new BasicNameValuePair("isStartPM", startPM));
		nameValuePairs.add(new BasicNameValuePair("endHour", endHour));
		nameValuePairs.add(new BasicNameValuePair("endMinute", endMinute));
		nameValuePairs.add(new BasicNameValuePair("isEndPM", endPM));

		nameValuePairs.add(new BasicNameValuePair("date", date));
		nameValuePairs.add(new BasicNameValuePair("roomID", roomId));

		String url = "http://www.lib.utexas.edu/studyrooms/reservations/submit.php";
		HttpPost httppost = new HttpPost(url);
		DefaultHttpClient client = new DefaultHttpClient();
		shared.logIntoUTDirect(this, client);

		String html = "";

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.ASCII));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpResponse response;
		try {
			response = client.execute(httppost);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String next = in.readLine();
			while (next != null) {
				// System.out.println(next);
				html += next;
				next = in.readLine();

				// TODO: create alert dialog

			}
			createConfirmationDialog(html);
		} catch (Exception e) {
//			e.printStackTrace();
			Log.e("finalizeRoomReservation", "error in finalizeReservation",e);
		}
		Log.i("finalizeRoomReservation", html);
	}

	String reserveResult = "";

	private void createConfirmationDialog(String html) {
		String result = parseRoomResults.parseConfirmation(html);
		Log.i("finalizeRoomReservation", "in createConfirmationDialog. result = " + result);
		reserveResult = result;
//		showDialog(0);

		Log.i("finalizeRoomReservation", "inside create dialog");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (reserveResult.contains("Error"))
			builder.setTitle("Error");
		else{
			success = true;
			builder.setTitle("Success");
			reserveResult+=String.format("\nLocation:%s\nRoom:%s\nGroup Name:%s\nDate:%s\nStart Time:%s:%02d %s\nEnd Time:%s:%02d %s\n",
						location, room, groupName, date, startHour, Integer.parseInt(startMinute), startPM, endHour, Integer.parseInt(endMinute), endPM);
		}
		builder.setMessage(reserveResult)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.dismiss();
								if (success){
									Intent intent = new Intent(context,
											WelcomeScreen.class);
									startActivity(intent);
								}
							}
						}).show();
		// .setNegativeButton("No", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// dialog.cancel();
		// }
		// });

		// TODO: different buttons for success/failure (need to show
		// reservation, maybe

	}


}
