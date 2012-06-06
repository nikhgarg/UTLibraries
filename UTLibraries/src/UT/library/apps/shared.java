package UT.library.apps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

//class for shared data and content, including Log in method, and client for connection
public class shared {

	public static String getHTMLfromURL(String urlstring) throws Exception { //should probably catch exception instead of throwing it
		URL url = new URL(urlstring);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));

		String HTML = "";
		String add = in.readLine();
		while (add != null) {
			HTML += add;
			add = in.readLine();
		}

		return HTML;
	}


	public static boolean loggedIntoCatalog = false;
	public static boolean loggedIntoUTDirect = false;

	public static String retrieveProtectedWebPage (Context context,DefaultHttpClient client, String uri)
	{
		String html = "";

		try{
//			logIntoUTDirect(client);
			HttpGet httpget = new HttpGet();
			httpget.setURI(new URI(uri));
			HttpResponse response = client.execute(httpget);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String next = in.readLine();
			while(next!=null){
			//	System.out.println(next);
				html+=next;
				next = in.readLine();
			}
//			response.getEntity().getContent().close();
			return html;

		}
		catch(Exception e)
		{
			Toast toast = Toast.makeText(context, "Could not retrive web page. Please check network connection and try again later.", Toast.LENGTH_SHORT);
			toast.show();
			Log.i("shared",
					"exception in retrieveProtectedWebPage: " + e.toString());
			return null;
		}
	}

	public static boolean logIntoUTDirect(Context context, DefaultHttpClient client)
	{
		try{
			SharedPreferences loginPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
			String username = loginPreferences.getString("uteid", "");
			String password = loginPreferences.getString("password","");


			HttpPost httppost = new HttpPost ("https://utdirect.utexas.edu/security-443/logon_check.logonform");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("LOGON", username));  			//use my info for testing
			nameValuePairs.add(new BasicNameValuePair("PASSWORDS", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.ASCII));
			HttpResponse response = client.execute(httppost);
//			response.getEntity().getContent().close();
		}
		catch(Exception e)
		{
			Log.i("shared",
					"exception in logIntoUTDirect: " + e.toString());
			loggedIntoUTDirect = false;
			Toast toast = Toast.makeText(context, "Could not Log in. Please check UTEID/Password and network connection.", Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
		loggedIntoUTDirect =  true;
		return true;

	}

	public static boolean logIntoCatalog (Context context,DefaultHttpClient client)
	{
		try{
			SharedPreferences loginPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
			String username = loginPreferences.getString("uteid", "");
			String password = loginPreferences.getString("password","");


			HttpPost httppost = new HttpPost ("https://catalog.lib.utexas.edu/patroninfo/");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("extpatid", username));  			//use my info for testing
			nameValuePairs.add(new BasicNameValuePair("extpatpw", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.ASCII));
			HttpResponse response = client.execute(httppost);
			response.getEntity().getContent().close();

		}
		catch(Exception e)
		{
			Log.i("shared",
					"exception in logIntoCatalog: " + e.toString());
			loggedIntoCatalog = false;
			Toast toast = Toast.makeText(context, "Could not Log in. Please check UTEID/Password and network connection.", Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
		loggedIntoCatalog =  true;
		return true;
	}
}
