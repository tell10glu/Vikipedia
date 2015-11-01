import java.io.IOException;

public class DenemeClass {

	
	public static void main(String[] args) throws IOException {
		ApplicationHelper.getInstance().addNewLink(new LinkReader("https://tr.wikipedia.org/wiki/%C3%96zel:Rastgele", ""));
		ApplicationHelper.getInstance().run();
	}

}
	