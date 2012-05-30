package UT.library.apps;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class parseResults3 {

	public static void parseBookDetails (String HTML, Book b)
	{
		//if (b.detailLabel.size()>0) return; //already been parsed, do not need to reparse

		Source page = new Source(HTML);


		Element e = page.getFirstElementByClass("bibDisplayContentMore");
		String furtherParse = "";
		//	System.out.println(e.getContent());
		for (Element a:e.getChildElements())
			furtherParse+=a.getContent()+"\n";
		Source page2 = new Source(furtherParse);
		String nextKey = "" , nextValue = "";
		for (Element ee: page2.getAllElements())
		{
			//	System.out.println(ee+"\n");
			if (ee.getAttributeValue("class")!=null&&ee.getAttributeValue("class").equals("bibInfoLabel")){
				nextKey = ee.getContent().getTextExtractor().toString();
			}
			else if (ee.getAttributeValue("class")!=null&&ee.getAttributeValue("class").equals("bibInfoData")){
				nextValue = ee.getContent().getTextExtractor().toString();
				b.bookDetails.put(nextKey, nextValue);
				nextKey=nextValue="";
			}
		}
		int num = furtherParse.indexOf("bibInfoLabel");
	}

	static int reccount = 0;

	private static void recElementBookParse(Element e, Book b, Element parent) {

		reccount++;

		//	Element parent = e.getParentElement();
		String val = e.getContent().getTextExtractor().toString();

		boolean hasClass = e.getAttributeValue("class")!=null;
		String classValue = e.getAttributeValue("class");

		if (hasClass && (classValue.equals("bibItemsHeader") || classValue.equals("briefcitCell") || classValue.equals("briefcitClear") || classValue.equals("briefcitJacket")))return;
		boolean classEqualsTitle = hasClass && classValue.equals("briefcitTitle");


		if (parent != null
				&& parent.getAttributeValue("class") != null
				&& parent.getAttributeValue("class").equals(
				"briefcitDetailMain")) {
			if (classEqualsTitle)
				if (e.getAttributeValue("href")!=null) b.detailURL = e.getAttributeValue("href"); //this does not always work for some reason, hence have to parse manually
				else {
					String dettemp = e.getContent().toString();
					if (dettemp.contains("href")){
						b.detailURL = dettemp.substring(dettemp.indexOf("href")+6);
						b.detailURL = "http://catalog.lib.utexas.edu"+ b.detailURL.substring(0,b.detailURL.indexOf("\""));
					}
				}

			if (classEqualsTitle)
				b.title = val;
			else if (val.length() > 0)
				b.publication += e.getContent().getTextExtractor().toString()
				+ "\n";
		} else if (hasClass
				&& e.getAttributeValue("class").equals("bibItemsEntry")) {
			int ind = 0;
			for (Element ebib : e.getChildElements()) {
				String ebibtemp = ebib.getContent().getTextExtractor()
				.toString();
				switch (ind) {
				case 0:
					//	b.location = ebibtemp;
					b.location.add(ebibtemp);
					break;
				case 1:
					//		b.callNo = ebibtemp;
					b.callNo.add(ebibtemp);
					break;
				case 2:
					//	b.currentStatus = ebibtemp;
					b.currentStatus.add(ebibtemp);
					break;
				default:
					b.otherFields += ebibtemp + "\t";
					break;
				}
				ind++;
			}
		} else if (e.getChildElements().size() == 0)
			b.otherFields += e.getContent().getTextExtractor().toString()
			+ "\t";
		else
			for (Element echild : e.getChildElements())
				recElementBookParse(echild, b,e);

		return;
	}

	public static ArrayList<Book> extractBooks(String HTML) {
		Source page = new Source(HTML);
		int position = 0;
		int oldposition = -1;

		String nextImageURL = "";

		List<Element> allElements = page.getAllElements();
		ArrayList<Book> allBooks = new ArrayList<Book>();
		int briefIndex = HTML.indexOf("\"briefcitDetail\"");
		String HTMLtemp = HTML;
		while(briefIndex>=0){
			HTMLtemp = HTMLtemp.substring(briefIndex-11);
			Source pagetemp = new Source(HTMLtemp);
			Element a = pagetemp.getFirstElementByClass("briefcitDetail");
			//		System.out.println(a);
			String elemout = "";
			ArrayList<String> bookdetails = new ArrayList<String>();
			allBooks.add(new Book());
			recElementBookParse(a, allBooks.get(allBooks.size() - 1),null);
			// allBooks.get(allBooks.size() - 1).imageURL = nextImageURL;
			HTMLtemp = HTMLtemp.substring(briefIndex+1); //so find next briefcitDetail
			briefIndex = HTMLtemp.indexOf("\"briefcitDetail\"");
		}
		//		}

		for (Book b : allBooks)
			b.cleanUp();

		// for (Book book: allBooks)
		// out.println(book);
		System.out.println(reccount);
		return allBooks;
	}

	public static int numResults = -1;
	public static String nextPageUrl = null;

	//parses top of page to get total number of Results and the URL for the next Page (to prefetch before displaying results)
	public static void parsePage(String HTML) {

		Source page = new Source(HTML);
		int position = 0;
		int oldposition = -1;

		List<Element> allElements = page.getAllElements();
		outer: for (Element a : allElements) {
			if (a.getName().equals("div")
					&& a.getAttributeValue("class") != null
					&& a.getAttributeValue("class").equals("browseSearchtool")) {
				for (Element aa : a.getChildElements()) {
					if (aa.getName().equals("div")
							&& aa.getAttributeValue("class") != null
							&& aa.getAttributeValue("class").equals(
							"browseSearchtoolMessage")) {
						String temp = aa.getContent().getTextExtractor()
						.toString();
						temp = temp.substring(0, temp.indexOf("results found"));
						String[] tempar = temp.trim().split(" ");
						numResults = Integer
						.parseInt(tempar[tempar.length - 1]);
					}
				}
			} else if (a.getName().equals("tr")
					&& a.getAttributeValue("class") != null
					&& a.getAttributeValue("class").equals("browsePager")) {
				for (Element aa : a.getChildElements()) {
					for (Element aaa : aa.getChildElements()) {
						if (aaa.getContent().getTextExtractor().toString()
								.equals("Next") && aaa.getAttributeValue("href")!=null) {
							nextPageUrl = "http://catalog.lib.utexas.edu" +  aaa.getAttributeValue("href");
							break outer;
						}
					}
				}
			}
		}
	}
}
