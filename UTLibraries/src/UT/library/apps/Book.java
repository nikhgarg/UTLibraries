package UT.library.apps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Book {

	public String title="";
	public String author="";
	public String year="";
	//	public String location="";
	//	public String callNo="";
	//	public String currentStatus="";
	public String publication="";
	public String otherFields="";
	public String imageURL = "";
	public int number = -1;

	public ArrayList<String> location = new ArrayList<String>();
	public ArrayList<String> callNo = new ArrayList<String>();
	public ArrayList<String> currentStatus = new ArrayList<String>();
	public String detailURL="";
	
	public HashMap<String, String> bookDetails = new LinkedHashMap<String, String>();


	public void cleanUp()
	{
		publication = publication.substring(0,publication.length()-1);

		String [] toPrint = {title, publication, 
				//location, callNo, 
				otherFields, 
				//currentStatus
		};
		String [] ignoreWords = {"Location", "Call No.", "Current Status", "Add to Clipboard"}; 


		for (int i=0;i<toPrint.length;i++)
			for (int ii=0;ii<ignoreWords.length;ii++){
				toPrint[i] = toPrint[i].trim();
				toPrint[i] = toPrint[i].replaceAll(ignoreWords[ii], "");
				toPrint[i] = toPrint[i].replaceAll("\n+", "\n");
				toPrint[i] = toPrint[i].replaceAll(" +", " ");
			}
		title = toPrint[0];
		publication = toPrint[1];
	//	location = toPrint[2];
	//	callNo = toPrint[3];
		otherFields = toPrint[2];
	//	currentStatus = toPrint[5];

		publication = (publication.substring(publication.length()-1, publication.length()).equals("\n"))? publication.substring(0,publication.length()-1): publication;



	}

	public String toString(){

		cleanUp();

		Object [] toPrint = {title, publication, location, callNo, currentStatus,imageURL, detailURL};
		String [] labels = {"Title", "Publication Info", "Location", "Call No.", "Current Status","Image URL", "detail URL"};

		String ret="";
		int ind=0;
		for (Object temp:toPrint){
			if (temp instanceof String && ((String) temp).length()>0)
				ret +=String.format("%s: %s\n", labels[ind],toPrint[ind]);
			else if (temp instanceof ArrayList<?> && ((ArrayList<String>) temp).size()>0)
				ret +=String.format("%s: %s\n", labels[ind],toPrint[ind].toString());
			ind++;
		}
		return ret;
		//return String.format("Title: %s\nAuthor: %s\nYear: %s\nPublication Info: %s\nLocation: %s\nCall #: %s\nCurrentStatus: %s\nOther Fields: %s\n", title, author, year, publication, location,callNo, currentStatus, otherFields);


	}
}


