import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Programin ana kısmı.
 * linkService ile oluşturulan threadler saklaniyor.
 * kuyrukta kelimleer ekleniyor. ve while true icerisinde surekli calisiyor.
 * Singleton class
 * 
 *  
 * @author abdullahtellioglu
 *
 */
public class ApplicationHelper implements Runnable {
	private static ApplicationHelper helper = null;
	public static ApplicationHelper getInstance(){
		if(helper ==null)
			helper = new ApplicationHelper();
		return helper;
	}
	private String path;
	private ExecutorService linkService;
	private Queue<LinkReader> queueWords;
	private ArrayList<String> words;
	private LinkSaver linkSaver;
	private ApplicationHelper(){
		linkService = Executors.newCachedThreadPool();
		linkSaver = new LinkSaver();
		queueWords = new LinkedList<>();
		try {
			path = new File(".").getCanonicalPath()+"/kelimeler";
			if(!new File(path).exists()){
				new File(path).mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		words = new ArrayList<>();
		readWords();
	}
	/**
	 *  yeni kelime eklendiginde bu metod cagiriliyor
	 * @param eklenecek bulunulan sayfa
	 * @param yeni sayfa altindaki linkler
	 */
	public void addNewWord(String eklenecek,String yeni){
		File file = new File(path+"/"+eklenecek);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedWriter yazici = null;
		try {
			yazici = new BufferedWriter(new FileWriter(file,true));
			yazici.append(yeni+"\n");
			yazici.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Kelimelerin hepsinin okundugu sinif
	 */
	private void readWords(){
		try {
			File[] files = new File(path).listFiles();
			for(int i =0;i<files.length;i++){
				words.add(files[i].getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void addNewLink(LinkReader yeniLink){
		if(!linkSaver.isLinkExists(yeniLink.getLink())){
			this.queueWords.add(yeniLink);
		}
	}
	
	@Override
	public void run() {
		while(true){
			try {
				if(!queueWords.isEmpty())
					linkService.execute(queueWords.poll());
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
