package UT.library.apps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

public class settings extends Activity {

	
	public static SharedPreferences loginPreferences; 
	public static SharedPreferences.Editor editor; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		loginPreferences = this.getSharedPreferences("login", MODE_PRIVATE); 
		editor = loginPreferences.edit();
	}
	
	
	public void launchUteidDialog(View view)
	{
		String savedUsername = loginPreferences.getString("uteid", "");
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		input.setText(savedUsername);
		alt_bld.setView(input);
		alt_bld.setMessage("Please Enter Your UTEID")
				.setCancelable(true)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							//	storeUsername();
								String username = input.getText().toString();
								editor.putString("uteid", username);
								editor.commit();
							}
						})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Action for ‘NO’ Button
						dialog.cancel();
					}
				});
		
		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		//alert.setTitle("AlertDialogExample");
		// Icon for AlertDialog
		//alert.setIcon(R.drawable.ic_launcher);
		alert.show();
		
	}
	public void launchPasswordDialog(View view)
	{
		String savedPassword = loginPreferences.getString("password", "");
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		input.setText(savedPassword);
		alt_bld.setView(input);
		alt_bld.setMessage("Please Enter Your Password")
				.setCancelable(true)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							//	storeUsername();
								String password = input.getText().toString();
								editor.putString("password", password);
								editor.commit();
							}
						})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Action for ‘NO’ Button
						dialog.cancel();
					}
				});
		
		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		//alert.setTitle("AlertDialogExample");
		// Icon for AlertDialog
		//alert.setIcon(R.drawable.ic_launcher);
		alert.show();
		
	}

}
