package UT.library.apps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class showLibraryTimings extends Activity {

	WebView webview;
	ProgressDialog dialog;
	Context context;

	private class RefreshAction implements Action {
		@Override
		public int getDrawable() {
			return R.drawable.book_image_placeholder; // need to replace with
			// Refresh icon
		}
		@Override
		public void performAction(View view) {

			try {
				dialog = ProgressDialog.show(context, "",
						"Loading. Please wait...", true);
				String url = "http://www.lib.utexas.edu/about/hours/";
				webview.loadUrl(url);
				dialog.cancel();
			} catch (Exception e) {
				Log.i("showLibraryTimings",
						"exception in refreshPage: " + e.toString());
			}
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = ProgressDialog.show(this, "",
				"Loading. Please wait...", true);

		setContentView(R.layout.library_hours);
		context = this;
		//code downloaded from https://github.com/johannilsson/android-actionbar/blob/master/README.md
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		// You can also assign the title programmatically by passing a
		// CharSequence or resource id.
		actionBar.setTitle("Library Hours");
		//	actionBar.setHomeLogo(R.drawable.book_image_placeholder);
		actionBar.setHomeAction(new IntentAction(this,new Intent(this, WelcomeScreen.class) , R.drawable.book_image_placeholder)); //go home (already there)
		actionBar.addAction(new IntentAction(this, new Intent(this, settings.class), R.drawable.book_image_placeholder)); //go to settings
		actionBar.addAction(new RefreshAction());
		//----------------------

		try {
			webview = (WebView) findViewById(R.id.libraryHours);

			// --------------------------------copied code 			// copied WebView code from http://developer.android.com/reference/android/webkit/WebView.html

			final Activity activity = this;
			webview.setWebViewClient(new WebViewClient() {
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					dialog.cancel();
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
			dialog.cancel();

		} catch (Exception e) {
			Log.i("showLibraryTimings", "exception in onCreate: " + e.toString());

		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		webview.saveState(outState);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		webview.restoreState(savedInstanceState);
	}
}
