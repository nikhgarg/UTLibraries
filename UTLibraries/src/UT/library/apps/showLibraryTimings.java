package UT.library.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class showLibraryTimings extends Activity {

	WebView webview;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_hours);
		try {
			webview = (WebView) findViewById(R.id.libraryHours);

			// --------------------------------copied code 			// copied WebView code from http://developer.android.com/reference/android/webkit/WebView.html

			final Activity activity = this;
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

			String url = "http://www.lib.utexas.edu/about/hours/";

			if (savedInstanceState != null){
				webview.restoreState(savedInstanceState);
				Log.i("showLibraryTimings", "restored saved state");
			}
			else
				webview.loadUrl(url);

		} catch (Exception e) {
			Log.i("showLibraryTimings", "exception in onCreate: " + e.toString());

		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		webview.saveState(outState);
	}
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		webview.restoreState(savedInstanceState);
	}


	public void goHome(View view)
	{
		Intent intent = new Intent(this, WelcomeScreen.class);
		startActivity(intent);
	}
	public void refreshPage(View view)
	{
		try{
			String url = "http://www.lib.utexas.edu/about/hours/";
			webview.loadUrl(url);
		}
		catch(Exception e)
		{
			Log.i("showLibraryTimings", "exception in refreshPage: " + e.toString());
		}
	}
}
