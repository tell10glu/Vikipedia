import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Bu sinif daha tamamlanmadi. Program simdilik butun linklere giriyor ve file icerisinde overwrite yapabiliyor.
 * Bu sinifin amaci girilen linkleri es gecmek.
 * @author abdullahtellioglu
 *
 */
public class LinkSaver {
	private ArrayList<String> links;
	File file;
	public LinkSaver(){
		links = new ArrayList<>();
		try {
			String pth  = new File(".").getCanonicalPath()+"links";
			file = new File(pth);
			if(file.exists()){
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String str = null;
				while((str=reader.readLine())!=null){
					links.add(str);
				}
				reader.close();
			}else{
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addLink(String link){
		BufferedWriter yazici = null;
		try {
			yazici = new BufferedWriter(new FileWriter(file,true));
			yazici.append(link+"\n");
			yazici.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		links.add(link);
		
	}
	public boolean isLinkExists(String link){
		for(int i=0;i<links.size();i++){
			if(links.get(i).equals(link)){
				return true;
			}
		}
		return false;
	}
}
