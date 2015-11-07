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

import model.Word;
import model.WordFrequency;

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
	}
	/**
	 *  yeni kelime eklendiginde bu metod cagiriliyor
	 * @param eklenecek bulunulan sayfa
	 * @param yeni sayfa altindaki linkler
	 * @param freq frekans sayisi.
	 */
	public void addNewWord(String eklenecek,String yeni,int freq){
		Word w1 = null,w2 = null;
		if((w1 =Word.getWord(eklenecek))==null){
			w1 = Word.addNewWord(eklenecek);
		}
		w2 = Word.addNewWord(yeni);
		WordFrequency.bindWords(w1, w2, freq, false);
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
