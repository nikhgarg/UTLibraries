package UT.library.apps;

import java.util.ArrayList;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class displayRoomResults extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();


		//log into UT direct with new client
		DefaultHttpClient client = new DefaultHttpClient();
		shared.logIntoUTDirect(this,client);

		String uri = createURIfromData(bundle);
		String html = shared.retrieveProtectedWebPage(this,client, uri);

		//parse rooms page
		ArrayList<Room> allRooms = new ArrayList<Room>();

		//TODO:	display prettily

		//TODO: parse room reservation page - send data including start/end time and group name
			//can be done use post method

		TextView tv = new TextView(this);
		tv.setText(html);
		setContentView(tv);


	}
	public String createURIfromData(Bundle bundle)
	{
		int [] date = bundle.getIntArray("Date");
		int number = bundle.getInt("number");
		boolean [] building = bundle.getBooleanArray("building");
		boolean [] roomreq = bundle.getBooleanArray("room");

		try {

			Uri.Builder build = new Uri.Builder();
			build.scheme("http");
			build.path("//www.lib.utexas.edu/studyrooms/index.php");

			build.appendQueryParameter("year", ""+date[0]);

			date[1]++; //add 1 (month was 0 indexed, need 1 indexed)
			String month =""+ ((date[1]>9)?date[1]:"0"+date[1]); //add 0 to month if less than 10
			build.appendQueryParameter("month", month);

			String day =""+ ((date[2]>9)?date[2]:"0"+date[2]); //add 0 to day if less than 10
			build.appendQueryParameter("day", day);

			int startHour = date[3];

			//round startMinute to nearest 15 minutes
			int startMinute = date[4];
			startMinute = (int) (Math.round(startMinute/15.0))*15;
			if (startMinute==60){
				startMinute=0;
				startHour = (startHour+1)%24;
			}
			String startPm = "am";
			if (startHour>=12)
			{
				startPm = "pm";
				if (startHour>12)
					startHour-=12;
			}
			else if (startHour==0)
				startHour = 12;
			build.appendQueryParameter("startHour", ""+startHour);


			build.appendQueryParameter("startMinute", ""+startMinute);

			build.appendQueryParameter("isStartPM", startPm);

			int endHour = date[5];

			//round endMinute to nearest 15 minutes
			int endMinute = date[6];
			endMinute = (int) (Math.round(endMinute/15.0))*15;
			if(endMinute==60)
			{
				endMinute = 0;
				endHour = (endHour +1)%24;
			}

			String endPm = "am";
			if (endHour>=12)
			{
				endPm = "pm";
				if (endHour>12)
					endHour-=12;
			}
			else if (endHour==0)
				endHour = 12;
			build.appendQueryParameter("endHour", ""+endHour);


			build.appendQueryParameter("endMinute", ""+endMinute);

			build.appendQueryParameter("isEndPM", endPm);

			if (building[0])
				build.appendQueryParameter("building", "any");
			else
				for(int i=1;i<building.length;i++)
					if (building[i])
					{
						build.appendQueryParameter("building" + (i-1), "on");
					}

			build.appendQueryParameter("numPeople", ""+(number==0?0:(number+1)));

			for(int i=0;i<roomreq.length;i++)
				if (roomreq[i])
				{
					build.appendQueryParameter("option" + (i), "on");
				}

			build.appendQueryParameter("mode", "search");


			Uri uri = build.build();
			return uri.toString();

		} catch (Exception e) {
			TextView tv = new TextView(this);
			tv.setText(e.toString());
			setContentView(tv);
			return null;
		}
	}
}