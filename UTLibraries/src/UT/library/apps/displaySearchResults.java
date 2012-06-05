package UT.library.apps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class displaySearchResults extends Activity {

	int qSize = 51;
	String[] entries = new String[qSize];
	int entriesBeg = 0;
	int entriesEnd = 0;
	boolean allResultsQed = false;
	boolean allResultsParsed = false;
	boolean pageLoading = false;
	int resultsPerPage = 5;
	TextView header;

	private Uri buildURIfromData(SearchData data) {
		try {

			Uri.Builder build = new Uri.Builder();
			build.scheme("http");
			build.path("//catalog.lib.utexas.edu/search/X");

			// Search String and Field Type
			String toSearch = data.searchString;
			if (data.fieldType != 0)
				toSearch = getResources().getStringArray(
						R.array.fieldtypeValues)[data.fieldType]
						                         + "(" + toSearch + ")";
			build.appendQueryParameter("SEARCH", toSearch);

			// Location
			if (data.location != 0)
				build.appendQueryParameter(
						"searchscope",
						getResources()
						.getStringArray(R.array.searchscopeValues)[data.location]);
			// Language
			if (data.language != 0)
				build.appendQueryParameter(
						"l",
						getResources().getStringArray(R.array.langValues)[data.language]);
			// Material Type
			if (data.materialType != 0)
				build.appendQueryParameter(
						"m",
						getResources().getStringArray(
								R.array.materialtypeValues)[data.materialType]);

			// Year Start, Year Start, 2 associated checkboxes
			if (data.useYearStart)
				build.appendQueryParameter("Da", "" + data.yearStart);
			if (data.useYearEnd)
				build.appendQueryParameter("Db", "" + data.yearEnd);
			// limit to available items
			if (data.limitAvailable)
				build.appendQueryParameter("availlim", "1");
			// publisher
			if (data.usePublisher)
				build.appendQueryParameter("p", data.publisher);

			Uri uri = build.build();
			return uri;

		} catch (Exception e) {
			Log.i("displaySearchResults",
					"Exception in buildURIfromData:" + e.toString());
			TextView tv = new TextView(this);
			tv.setText(e.toString());
			setContentView(tv);
			return null;
		}

	}

	boolean startedParseThread = false;
	boolean shownFirst = false;

	// to do: implement locks in both runnable classes (or not needed)
	private class getURIdata implements Runnable {

		URL libraryURL;

		public getURIdata(URL url) {
			libraryURL = url;
		}

		public void run() {
			pageLoading = true;
			String catalogHTML = "";
			int numfound = 0;
			while (activityRunning) {

				try {
					if (libraryURL == null) {
						pageLoading = false;
						allResultsQed = true;
						return;
					}

					while (currentViewNumEnd + resultsPerPage * 10 < allBooks
							.size()) {
					}

					BufferedReader in = new BufferedReader(
							new InputStreamReader(libraryURL.openStream()));
					boolean first = true;
					String temp = "";
					while (temp != null) {
						if(!activityRunning)finish();
						if (temp.contains("briefcitEntryNum")) {
							if (!first) {
								int oldSize = allBooks.size();
								ArrayList<Book> tempBooks = parseResults4
								.extractBooks(catalogHTML);
								numfound += tempBooks.size();
								for (int i = 0; i < tempBooks.size(); i++)
									tempBooks.get(i).numberinorder = i + oldSize +1;
								allBooks.addAll(tempBooks);
								Log.i("displaySearchResults",
										"" + allBooks.size());
								if (allBooks.size() >= resultsPerPage
										&& !shownFirst) {
									displayResults(0);
									shownFirst = true;
								}
							}
							// if first time that seeing "briefcitEntryNum",
							// then it is at the top of the page - need to
							// extract
							// num results and next url from top
							else {
								first = false;
								parseResults4.parsePage(catalogHTML); // parse
								// top

								Log.i("displaySearchResults",
										"parsed page again, book size currently:"
										+ allBooks.size()
										+ "\n next page url:"
										+ parseResults4.nextPageUrl);
								// of page
								// for
								// number of
								// results,
								// link to
								// next page
							}
							catalogHTML = "";
						}

						catalogHTML += temp + "\n";
						temp = in.readLine();
					}
					// for last entry (there is not briefEntryNum after it so is
					// not extracted in the loop)
					int oldSize = allBooks.size();
					ArrayList<Book> tempBooks = parseResults4
					.extractBooks(catalogHTML);
					numfound += tempBooks.size();
					// now do this in BookBaseAdapter
					for (int i = 0; i < tempBooks.size(); i++)
						tempBooks.get(i).numberinorder = i + oldSize +1;
					allBooks.addAll(tempBooks);
					catalogHTML = "";

					in.close();
					if (parseResults4.nextPageUrl != null) {
						libraryURL = new URL(parseResults4.nextPageUrl);
						parseResults4.nextPageUrl = null;
					} else
						libraryURL = null;
					Log.i("displaySearchResults", "next page URL:" + libraryURL);

				} catch (Exception e) {
					Toast toast = Toast.makeText(context, "Could not load web data. Please check network connection and try again later.", Toast.LENGTH_SHORT);
					toast.show();
					Log.i("displaySearchResults", "Exception in getURIdata:"
							+ e.toString());
				}
			}
		}
	}

	ArrayList<Book> allBooks = new ArrayList<Book>();
	int currentViewNumStart = 0; // start index of books currently displayed
	int currentViewNumEnd = 0; // end index of books currently displayed

	public void displayResults(int start) {
		// to do: prettify
		// add display if no results

		try {
			if (start < 0)
				start = 0;
			//			handler.post(new Runnable(){
			//				@Override
			//				public void run() {
			//					// TODO Auto-generated method stub
			//					dialog = ProgressDialog.show(context, "", 
			//							"Loading. Please wait...", true); 
			//				}			
			//			});

			Log.i("displaySearchResults", "inside display Results");
			if (start < allBooks.size()) {
				while(start+resultsPerPage < allBooks.size() && !allResultsQed && shownFirst){
					Log.i("displaySearchResult","waiting for all to load. allBooks.size: " + allBooks.size() + " allResultsQed: " + allResultsQed + " shown First: " +  shownFirst);
					//wait till next page all loaded, or all results have loaded. not for first results
					//because that check occurs elsewhere
				}
				int end = (int) Math.min(start + resultsPerPage,
						allBooks.size());
				currentViewNumStart = start;
				currentViewNumEnd = end;
				Log.i("displaySearchResults",
				"inside display Results if statement");

				listViewData.clear();
				for (int i = start; i < end; i++)
					listViewData.add(allBooks.get(i));
				handler.post(new Runnable() {
					@Override
					public void run() {
						dialog.cancel();
						header.setText(String.format("%d-%d/%d",
								currentViewNumStart + 1, currentViewNumEnd,
								parseResults4.numResults));
						ListView listview = (ListView) findViewById(R.id.searchResultsListView5);
						listview.setAdapter(new BookBaseAdapter(context,
								listViewData));
					}
				});
				// bookAdapter.notifyDataSetChanged();
				Log.i("displaySearchResults",
						"set adaptor for display layout with arraylist size"
						+ listViewData.size());

			}

		} catch (Exception e) {
			Log.e("displaySearchResults",
					"Exception in displayResults: ", e);
		}
	}

	public void nextPage(View view) {
		try {
			if (currentViewNumEnd < allBooks.size()
					&& allBooks.get(currentViewNumEnd).bookDetails.size() != 0) {
				Book b = allBooks.get(currentViewNumEnd);
				Log.i("displaySearchResults", "Book already has detail:"
						+ b.title + " " + b.bookDetails.toString());
			} else if (currentViewNumEnd < allBooks.size()
					&& allBooks.get(currentViewNumEnd).bookDetails.size() == 0){
				//				new Thread(new fetchBookDetail(currentViewNumEnd,		//not fetching details right now - slows down code too much
				//						currentViewNumEnd + resultsPerPage)).start();
			}
			else
				Log.i("displaySearchResults", "book out of range");

		} catch (Exception e) {
			Log.i("displaySearchResults",
					"Exception in nextPage:" + e.toString());
		}
		displayResults(currentViewNumEnd);
	}

	public void prevPage(View view) {
		displayResults(currentViewNumStart - resultsPerPage);
	}

	public void toSearchInput(View view) {
		Intent intent = new Intent(this, searchInputScreen.class);
		startActivity(intent);
	}

	private class fetchBookDetail implements Runnable {

		int start;
		int end;

		public fetchBookDetail(int start1, int end1) {
			this.start = start1;
			this.end = end1;
		}

		public void run() {
			// Log.i("displaySearchResults","inside run method for fetching book details");
			try {
				for (int i = start; i < end; i++) {
					if(!activityRunning) finish();
					// Log.i("displaySearchResults","inside for loop for fetching book details");

					if (i < allBooks.size()) {
						Book b = allBooks.get(i);

						if (b.bookDetails.size() == 0) {

							// URL detailURL = new URL (b.detailURL);
							String HTML = shared.getHTMLfromURL(b.detailURL);
							parseResults4.parseBookDetails(HTML, b);
							Log.i("displaySearchResults",
									"fetched book details for book:" + b.title);
						}
					}
				}
			} catch (Exception e) {
				Log.i("displaySearchResults", "Exception in fetchBookDetail"
						+ e.toString());
			}
		}
	}

	public void displayBookDetail(int position) {
		try {
			Book b = allBooks.get(position + currentViewNumStart);
			String HTML = shared.getHTMLfromURL(b.detailURL);
			if (b.detailURL != null && b.detailURL.length() > 0
					&& b.bookDetails.size() == 0)
				parseResults4.parseBookDetails(HTML, b);
			if (b.bookDetails.size() > 0) {
				setContentView(R.layout.book_detail_layout2);
				LayoutInflater mInflater = LayoutInflater.from(this);

				View view = mInflater.inflate(R.layout.book_detail_layout2,
						null);
				// TableLayout detailsTable = (TableLayout)
				// view.findViewById(R.id.detailsTable);
				LinearLayout detailsTable = (LinearLayout) view
				.findViewById(R.id.detailsTable2);
				detailsTable.removeAllViews();
				// setContentView(detailsTable);
				for (String key : b.bookDetails.keySet()) {
					// TableRow row =
					// (TableRow)mInflater.inflate(R.layout.book_detail_row,
					// null);
					RelativeLayout row = (RelativeLayout) mInflater.inflate(
							R.layout.book_detail_row2, null);

					((TextView) row.findViewById(R.id.detailLabel2))
					.setText(key + ": ");
					((TextView) row.findViewById(R.id.detailValue2))
					.setText(b.bookDetails.get(key));
					detailsTable.addView(row);
				}
				dialog.cancel();
				setContentView(detailsTable);
				// setContentView(R.layout.book_detail_layout2);
			} else
				Log.i("displaySearchResults", "book detail not fetched yet:"
						+ b.title);
		}

		catch (Exception e) {
			Log.i("displaySearchResults", "Exception in displayBookDetail");
			TextView tv = new TextView(this);
			tv.setText(e.toString());
			setContentView(tv);
		}
	}

	ArrayList<Book> listViewData = new ArrayList<Book>();
	BookBaseAdapter bookAdapter;
	Handler handler;
	Context context;
	boolean activityRunning;

	public void onPause()
	{
		super.onPause();
		activityRunning = false;
	}

	public void onResume()
	{
		super.onResume();
		activityRunning = true;
		parseResults4.numResults = -1; //static field, need to reset. - should probably rewrite this field to be nonstatic
		//and localized to each searchResults activity instance.
	}

	ProgressDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.search_results5);
		dialog = ProgressDialog.show(this, "", 
				"Loading. Please wait...", true);
		activityRunning = true;
		header = (TextView) findViewById(R.id.searchResultsHeader);
		context = this;
		ListView listview = (ListView) findViewById(R.id.searchResultsListView5);

		bookAdapter = new BookBaseAdapter(this, listViewData);
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("displaySearchResult", "listview item clicked: " + position);
				dialog = ProgressDialog.show(context, "", 
						"Loading. Please wait...", true);
				(new fetchBookDetail(position,position)).run(); //in UI thread.
//				new Thread(new fetchBookDetail(currentViewNumEnd,		//not fetching details right now - slows down code too much
//						currentViewNumEnd + resultsPerPage)).start();
				displayBookDetail(position);

			}
		});
		
		listview.setAdapter(bookAdapter);

		Bundle bundle = getIntent().getExtras();
		SearchData data = bundle.getParcelable("fieldsData");

		Uri uri = buildURIfromData(data);
		handler = new Handler();

		try {
			HttpGet httpget = new HttpGet(uri.toString());
			URL libraryURL = new URL(httpget.getURI().toString());
			// if (!pageLoading)
			new Thread(new getURIdata(libraryURL)).start();

			if (allResultsQed && !shownFirst) {
				displayResults(0);
				shownFirst = true;
			}
//			new Thread(new fetchBookDetail(0, resultsPerPage)).start(); // fetch
			// book
			// details
			// for
			// first
			// page
		} catch (Exception e) {
			Log.i("displaySearchResults",
					"Exception in getResultsPage:" + e.toString());
		}

	}
}