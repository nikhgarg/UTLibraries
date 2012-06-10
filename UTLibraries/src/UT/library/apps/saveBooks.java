package UT.library.apps;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class saveBooks extends Activity {

	ArrayList<Book> saved = new ArrayList<Book>();
	sBookBaseAdapter adapter;
	int oldSize = 0; //size of saved, enables monitoring of book deletions
	Context context;
	boolean activityRunning = false; //to break out of threads.

	// retrieve books from file
	//returns true if there are books saved, false otherwise
	@SuppressWarnings("unchecked")
	public boolean retrieveSavedBooks(){
		try{
			FileInputStream fileIn = this.openFileInput("Saved_Books");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			//			if (fileIn.read()!=-1) //if file has at least 1 object
			//			{
			Object nextObject = in.readObject();
			if (nextObject!=null && nextObject instanceof ArrayList<?>){
				Log.i("saveBooks", nextObject.toString() + "\n" + nextObject.getClass());
				saved = (ArrayList<Book>)nextObject;
				Log.i("saveBooks", saved.toString());

				oldSize = saved.size();
			}
			else
			{
				TextView ttest = new TextView(this);
				ttest.setText("Sorry. You have not saved any books yet");
				setContentView(ttest);
				return false;
			}
			in.close();
			fileIn.close();
			//add an else to display "no saved books"
		}
		catch(java.io.FileNotFoundException e)
		{
			TextView ttest = new TextView(this);
			ttest.setText("Sorry. You have not saved any books yet");
			setContentView(ttest);
			return false;
		}
		catch(Exception e)
		{
			Log.e("saveBooks", "exception in retrieveSavedBooks", e);
		}
		return true;
	}
	// display retrieved books
	public void displaySavedBooks() {
		// to do: prettify
		// add display if no results
		try {
			adapter=new sBookBaseAdapter(this, saved);
			ListView listview = (ListView) findViewById(R.id.savedBooksListView);
			listview.setAdapter(adapter);
			dialog.cancel();
		} catch (Exception e) {
			Log.e("saveBooks", "Exception in displaySavedBooks", e);
		}
	}

	// rewrite the file (so to remove books that were deleted)
	public void updateSaveFile() {
		try {
			String FILENAME = "Saved_Books";
			FileOutputStream fos = null;
			ObjectOutputStream oos = null;
			fos = this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			for (Book b : saved) {
				oos.writeObject(b);
				Log.i("saveBooks", "saved Book title: " + b.title);
			}
			oos.close();
			fos.close();
		} catch (Exception e) {
			Log.e("saveBooks", "exception updateSaveFile: ", e);
		}

	}

	//new thread that continuosly checks sBookBaseAdapter to see if its array has changed. If it has, rewrite the file
	//with new version of book array
	private class checkArrayChanged implements Runnable{

		@Override
		public void run() {
			while(activityRunning)
			{
				if (oldSize!=saved.size())
				{
					Log.i("saveBooks", "array changed. oldSize: " + oldSize + ". new Array: " + saved.toString());
					//overwrite saved book file
					try{
						String FILENAME = "Saved_Books";
						FileOutputStream fos = null;
						ObjectOutputStream oos = null;

						fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
						oos = new ObjectOutputStream(fos);
						oos.writeObject(saved);
						oldSize = saved.size();
						oos.close();
						fos.close();
					}
					catch(Exception e)
					{
						Log.e("saveBooks", "exception in checkArrayChanged: ",e);
					}
				}
			}
		}
	}

	public void onPause()
	{
		super.onPause();
		activityRunning = false;
	}
	public void onResume()
	{
		super.onResume();
		activityRunning = true;
	}
	ProgressDialog dialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = ProgressDialog.show(this, "",
				"Loading. Please wait...", true);
		setContentView(R.layout.saved_books);
		context = this;
		activityRunning = true;

		//code downloaded from https://github.com/johannilsson/android-actionbar/blob/master/README.md
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		// You can also assign the title programmatically by passing a CharSequence or resource id.
		actionBar.setTitle("Saved Books");
		actionBar.setHomeAction(new IntentAction(this, new Intent(this,
				WelcomeScreen.class), R.drawable.home)); // go	// home
		actionBar.addAction(new IntentAction(this, new Intent(this, settings.class), R.drawable.book_image_placeholder)); //go to settings
		//----------------------


		boolean booksSaved = retrieveSavedBooks();
		if(booksSaved){
			displaySavedBooks();
			new Thread(new checkArrayChanged()).start();
		}
	}
}
