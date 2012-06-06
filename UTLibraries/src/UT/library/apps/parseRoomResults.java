package UT.library.apps;
import java.util.ArrayList;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.util.Log;


public class parseRoomResults {

	static ArrayList<Room> extractRooms(String html)
	{
		try {
			ArrayList<Room> allRooms = new ArrayList<Room>();

			Source page = new Source(html);
			for (Element tableElem:page.getAllElements(HTMLElementName.TABLE)){
				String saveLocation = null;
				String location = null;
				for (Element rowElement : tableElem.getAllElements(HTMLElementName.TR)) {

					String loctemp = rowElement.getTextExtractor().toString();
					int reqIndex = loctemp.indexOf("Room Requested Features"); //2 purposes: to ignore rows that do not have rooms
					// and to extact library location
					if (reqIndex >= 0) //this row has the column headers, so need previous row for table name
					{
						//System.out.println(location);
						loctemp = loctemp.substring(0, reqIndex).trim();
						location = saveLocation;
						continue;
					}
					saveLocation = loctemp;
					Room room = new Room();
					room.location = location;
					int colNum=0;//column element for each row
					for (Element tdElement : rowElement.getAllElements(HTMLElementName.TD)) {
						String val = tdElement.getTextExtractor().toString();
						switch(colNum)
						{
						case 0: room.room = val;break;
						case 1: room.reqFeatures = val;break;
						case 2: room.seating = val;break;
						case 3: room.groupName = val;break;
						case 4: room.available = val;break;
						case 5:
							try{
								String temp = tdElement.getContent().getURIAttributes().toString(); //returns in following format: [href='url']
								temp = temp.substring(temp.indexOf('\'')+1);
								room.reserveLink = temp.substring(0,temp.indexOf('\''));
							}
							catch(Exception e)
							{
								Log.i("parseRoomResults", "could not parse reserve Link");
							}
							break;
						}
						//					if(colNum==5){System.out.println(room);}
						colNum++;
					}
					if(room.reserveLink!=null) //actually a room
						allRooms.add(room);
				}
			}
			return allRooms;
		} catch (Exception e) {
			Log.e("parseRoomResults", "exception in extractRooms",e);
			return null;
		}
	}
}
