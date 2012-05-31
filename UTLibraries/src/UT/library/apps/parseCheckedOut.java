package UT.library.apps;

import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class parseCheckedOut {
	
	public static ArrayList<cBook> parseCheckedOutBooks(String html)
	{
		ArrayList<cBook> books = new ArrayList<cBook>();
		Source page = new Source(html);
		List<Element> elems = page.getAllElementsByClass("patFuncEntry");
		for (Element elem: elems)
		{
			Source epage = new Source(elem.getContent());
			cBook b = new cBook();
			b.title = epage.getFirstElementByClass("patFuncTitle").getTextExtractor().toString();
			b.barcode = epage.getFirstElementByClass("patFuncBarcode").getTextExtractor().toString();
			b.status = epage.getFirstElementByClass("patFuncStatus").getTextExtractor().toString();
			b.callNumber = epage.getFirstElementByClass("patFuncCallNo").getTextExtractor().toString();
			books.add(b);
		}
		return books;
	}

}
