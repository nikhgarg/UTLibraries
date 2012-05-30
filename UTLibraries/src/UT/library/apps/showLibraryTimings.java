package UT.library.apps;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class showLibraryTimings extends Activity {
	
	WebView webview;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_hours);
		try {

//			getWindow().requestFeature(Window.FEATURE_PROGRESS); // display the
			// progress in the activity title bar

			webview = (WebView) findViewById(R.id.libraryHours);

			// --------------------------------copied code 			// copied WebView code from http://developer.android.com/reference/android/webkit/WebView.html

			webview.getSettings().setJavaScriptEnabled(true);
			final Activity activity = this;
			webview.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					// Activities and WebViews measure progress with different
					// scales.
					// The progress meter will automatically disappear when we
					// reach
					// 100%
					activity.setProgress(progress * 1000);
				}
			});
			webview.setWebViewClient(new WebViewClient() {
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					Toast.makeText(
							activity,
							"Page Could Not Load. Please Try Again Later. "
							+ description, Toast.LENGTH_SHORT).show();
				}
			});
			// --------------------------------------end Copied

			SharedPreferences hourspref = this.getSharedPreferences(
					"libraryhours", this.MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = hourspref.edit();
			String hoursHTML = hourspref.getString("hoursHTML", "");
			if (hoursHTML.length() == 0) {
				hoursHTML = shared.getHTMLfromURL("http://www.lib.utexas.edu/about/hours/");
				prefEditor.putString("hoursHTML", hoursHTML);
			}
			webview.loadData(hoursHTML, "text/css", null);
			String url = "http://www.lib.utexas.edu/about/hours/";
			webview.loadUrl(url);
			
		} catch (Exception e) {
			TextView tv = new TextView(this);
			tv.setText(e.toString());
			setContentView(tv);
		}
	}
	public void goHome(View view)
	{
		Intent intent = new Intent(this, WelcomeScreen.class);
		startActivity(intent);
	}
	public void refreshPage(View view)
	{
		try{
			SharedPreferences hourspref = this.getSharedPreferences(
					"libraryhours", this.MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = hourspref.edit();
//			WebView webview = (WebView) findViewById(R.id.libraryHours);
			String url = "http://www.lib.utexas.edu/about/hours/";
//			webview.loadUrl(url);
			String html = shared.getHTMLfromURL(url);
			webview.loadData(html, "text/html", null);
			prefEditor.putString("hoursHTML", html);

		}
		catch(Exception e)
		{
			TextView tv = new TextView(this);
			tv.setText(e.toString());
			setContentView(tv);
		}
	}
}
