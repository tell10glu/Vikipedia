import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkReader implements Runnable {
	private String link; // suanki link
	private String comingFrom; // onceki sayfanin basligi
	public String getLink(){
		return link;
	}
	public LinkReader(String link,String comingFrom) {
		if(!link.startsWith("https://"))
			this.link  = "https://tr.wikipedia.org"+link;
		else
			this.link = link;
		this.comingFrom = comingFrom;
	}
	
	@Override
	public void run() {	
		Document doc = null;
		try {
			doc = Jsoup.connect(link).get();
		} catch (Exception  e) {
			e.printStackTrace();
			return;
		}
		Elements divs = doc.select("div#bodyContent");
		Elements paragraphs = divs.select("p");
		Elements links = paragraphs.select("a");
		Elements headerContainer = doc.select("h1#firstHeading");;
		String header = headerContainer.first().html();
		ApplicationHelper.getInstance().addNewWord(comingFrom, header);//gelinen sayfaya bu kelime ekleniyor
		System.out.println("header :"+header);
	    for (Element link : links) {
	    	if(link.attr("href").startsWith("/wiki/")){
	    		ApplicationHelper.getInstance().addNewLink(new LinkReader(link.attr("href"), header));
	    		// Bu sayfadaki tum linkler icin thread olusturuluyor.
	    	}
	    }
	    
	}
	@Override
	public String toString() {
		return "LinkOkuma [link=" + link + ", comingFrom=" + comingFrom + "]";
	}
}
