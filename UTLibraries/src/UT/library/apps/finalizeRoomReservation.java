package UT.library.apps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;


public class finalizeRoomReservation extends Activity {

	String roomId="";
	String date="";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.finalize_room_reservation);
		Bundle bundle = getIntent().getExtras();

		//get times, date, room id from previous screen

		roomId = bundle.getString("roomID");
		date = String.format("%s-%s-%s", bundle.getString("year"), bundle.getString("month"), bundle.getString("day"));
	}

	public void finalizeReservation(View view)
	{

		EditText et = (EditText)findViewById(R.id.groupNameEnter);
		String groupName = et.getText().toString();
		Spinner spinner = (Spinner) findViewById(R.id.startHourSpinner);
		String startHour = 1 + spinner.getSelectedItemPosition() + ""; //time is 0 indexed
		spinner = (Spinner) findViewById(R.id.endHourSpinner);
		String endHour = 1 + spinner.getSelectedItemPosition() + ""; //time is 0 indexed
		spinner = (Spinner) findViewById(R.id.startMinuteSpinner);
		String startMinute = 15*spinner.getSelectedItemPosition() + ""; //time is 0 indexed
		spinner = (Spinner) findViewById(R.id.endMinuteSpinner);
		String endMinute = 15*spinner.getSelectedItemPosition() + ""; //time is 0 indexed
		spinner = (Spinner) findViewById(R.id.startPMSpinner);
		String startPM = (spinner.getSelectedItemPosition()==0)?"PM":"AM";
		spinner = (Spinner) findViewById(R.id.endPMSpinner);
		String endPM = (spinner.getSelectedItemPosition()==0)?"PM":"AM";

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("reservationName", groupName));
		nameValuePairs.add(new BasicNameValuePair("startHour", startHour));
		nameValuePairs.add(new BasicNameValuePair("startMinute", startMinute));
		nameValuePairs.add(new BasicNameValuePair("isStartPM", startPM));
		nameValuePairs.add(new BasicNameValuePair("endHour", endHour));
		nameValuePairs.add(new BasicNameValuePair("endMinute", endMinute));
		nameValuePairs.add(new BasicNameValuePair("isEndPM", endPM));

		nameValuePairs.add(new BasicNameValuePair("date", date));
		nameValuePairs.add(new BasicNameValuePair("roomID", roomId));

		String url = "http://www.lib.utexas.edu/studyrooms/reservations/submit.php";
		HttpPost httppost = new HttpPost (url);
		DefaultHttpClient client = new DefaultHttpClient();
		shared.logIntoUTDirect(this,client);

		String html = "";

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.ASCII));
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
			while(next!=null){
				//	System.out.println(next);
				html+=next;
				next = in.readLine();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("finalizeRoomReservation", html);
	}
}
