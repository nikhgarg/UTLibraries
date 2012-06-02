package UT.library.apps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class libraryMaps extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] locations = { "PCL Map", "PCL Locations Guide",
				"Architecture Library Maps",
				"Benson Latin American Collection Map",
				"Benson Latin American Collection Locations Guide",
				"Chemistry (Mallet) Library Map",
				"Chemistry (Mallet) Library Locations Guide",
				"Classics Library Map", "Classics Library Locations Guide",
				"Engineering (McKinney) Library Map",
				"Engineering (McKinney) Library Map Location Guide",
				"Fine Arts Library Map", "Fine Arts Library Location Guide",
				"Geology (Walter) Library Map", "Life Science Library Map",
				"Life Science Library Location Guide",
				"Kuehne Physics Mathematics Astronomy Library (PMA) Map",
				"Kuehne Physics Mathematics Astronomy Library (PMA) Locaton Guide" };

		setContentView(R.layout.main2);

		ListView listview = (ListView) findViewById(R.id.mainPageListView);
		listview.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, locations));

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				launchView(position);

			}
		});
	}

	public void launchView(int position) {

		switch (position) {
		case 0:
			final int[] images = { R.drawable.pcl_01, R.drawable.pcl_02,
					R.drawable.pcl_03, R.drawable.pcl_04, R.drawable.pcl_05,
					R.drawable.pcl_06 };
			final String[] title = { "1st Floor", "2nd Floor", "3rd Floor",
					"4th Floor", "5th Floor", "6th Floor" };
			showImages(images, title);
			break;
		case 1:
			final int[] images1 = { R.drawable.pcl_book1, R.drawable.pcl_book2,
					R.drawable.pcl_book3, R.drawable.pcl_book4,
					R.drawable.pcl_book5, R.drawable.pcl_book6,
					R.drawable.pcl_book7 };
			final String[] title1 = { "Call Nos: A-GV", "Call Nos: H-HX",
					"Call Nos: J-PK", "Call Nos: PL-ZA", "Dewey Decimal #s",
					"Oversize Materials", "Special Materials" };
			showImages(images1, title1);
			break;
		case 2:
			final int[] images2 = { R.drawable.apl_0 };
			final String[] title2 = { "Archit. Planning Lib. Map" };
			showImages(images2, title2);
			break;
		case 3:
			final int[] images3 = { R.drawable.blac_01, R.drawable.blac_02,
					R.drawable.blac_03, R.drawable.blac_04, };
			final String[] title3 = { "1st Floor", "2nd Floor", "3rd Floor",
					"4th Floor" };
			showImages(images3, title3);
			break;
		case 4:
			final int[] images4 = { R.drawable.blac_loc1, R.drawable.blac_loc2 };
			final String[] title4 = { "Library of Congress", "Other" };
			showImages(images4, title4);
			break;
		case 5:
			final int[] images5 = { R.drawable.chemistry_01 };
			final String[] title5 = { "1st Floor" };
			showImages(images5, title5);
			break;
		case 6:
			final int[] images6 = { R.drawable.chemistry_loc1 };
			final String[] title6 = { "Chem. Loc. Guide" };
			showImages(images6, title6);
			break;
		case 7:
			final int[] images7 = { R.drawable.classics_01 };
			final String[] title7 = { "1st Floor" };
			showImages(images7, title7);
			break;
		case 8:
			final int[] images8 = { R.drawable.classics_loc1,
					R.drawable.classics_loc2 };
			final String[] title8 = { "Locations Guide", "Locations Guide Cont" };
			showImages(images8, title8);
			break;
		case 9:
			final int[] images9 = { R.drawable.engineering_01,
					R.drawable.engineering_02 };
			final String[] title9 = { "1st Floor", "Mezzanine Level" };
			showImages(images9, title9);
			break;
		case 10:
			final int[] images10 = { R.drawable.engineering_loc1 };
			final String[] title10 = { "Locations Guide" };
			showImages(images10, title10);
			break;
		case 11:
			final int[] images11 = { R.drawable.finearts_03,
					R.drawable.finearts_04, R.drawable.finearts_05 };
			final String[] title11 = { "3rd Floor", "4th Floor", "5th Floor" };
			showImages(images11, title11);
			break;
		case 12:
			final int[] images12 = { R.drawable.finearts_loc03,
					R.drawable.finearts_loc04, R.drawable.finearts_loc05 };
			final String[] title12 = { "3rd Floor Guide", "4th Floor Guide",
					"5th Floor Guide" };
			showImages(images12, title12);
			break;
		case 13:
			final int[] images13 = { R.drawable.geology };
			final String[] title13 = { "Geology 1st Floor Guide" };
			showImages(images13, title13);
			break;

		case 14:
			final int[] images16 = { R.drawable.finearts_03,
					R.drawable.finearts_04, R.drawable.finearts_05 };
			final String[] title16 = { "3rd Floor", "4th Floor", "5th Floor" };
			showImages(images16, title16);
			break;
		case 15:
			final int[] images17 = { R.drawable.finearts_loc03,
					R.drawable.finearts_loc04, R.drawable.finearts_loc05 };
			final String[] title17 = { "3rd Floor Guide", "4th Floor Guide",
					"5th Floor Guide" };
			showImages(images17, title17);
			break;
		case 16:
			final int[] images14 = { R.drawable.pma_01, R.drawable.pma_02 };
			final String[] title14 = { "Main Level", "Upper Level" };
			showImages(images14, title14);
			break;
		case 17:
			final int[] images15 = { R.drawable.pma_loc };
			final String[] title15 = { "Stacks Guide" };
			showImages(images15, title15);
			break;

		}
	}

	public void showImages(final int[] images, final String[] title) {
		final int[] pos = new int[1]; // need array because it is a mutable
		// final object. int and Integer are
		// immutable.
		setContentView(R.layout.librarymap);
		final TextView textview = (TextView) findViewById(R.id.libraryMapHeader);
		final ImageView imageview = (ImageView) findViewById(R.id.mapimage);
		imageview.setImageResource(images[pos[0]]);
		textview.setText(title[pos[0]]);
		Button next = (Button) findViewById(R.id.nextMapButton);
		Button prev = (Button) findViewById(R.id.prevMapButton);

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pos[0] < images.length - 1)
					pos[0]++;
				imageview.setImageResource(images[pos[0]]);
				textview.setText(title[pos[0]]);
			}
		});

		prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pos[0] > 0)
					pos[0]--;
				imageview.setImageResource(images[pos[0]]);
				textview.setText(title[pos[0]]);
			}

		});

	}

}
