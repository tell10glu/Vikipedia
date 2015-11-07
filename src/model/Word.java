package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import helpers.DatabaseHelper;
/**
 * Kelimelerin olusturuldugu sinif.
 * Siniflara sadece veritabanindan veri okunabilir.
 * Bu yuzden constructorlari yok
 * Statik metodlar sayesinde veri cekilicek ve getterlar ile kullanilicak.
 * @author abdullahtellioglu
 *
 */
public class Word {
	private int id;
	private String word;
	
	public int getId() {
		return id;
	}
	public String getWord() {
		return word;
	}
	
	private Word(int id, String word) {
		super();
		this.id = id;
		this.word = word;
	}
	public static Word getWord(int id){
		Connection con = null;
		Word word = null;
		try {
			con = DatabaseHelper.getDatabaseConnection();
			PreparedStatement st = con.prepareStatement("Select * from Word where id = ?");
			st.setInt(1, id);
			ResultSet set = st.executeQuery();
			if(set.next()){
				word = new Word(set.getInt(1), set.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return word;
	}
	public static Word getWord(String word){
		Connection con = null;
		Word mword = null;
		try {
			con = DatabaseHelper.getDatabaseConnection();
			PreparedStatement st = con.prepareStatement("Select * from Word where word = ?");
			st.setString(1, word);
			ResultSet set = st.executeQuery();
			if(set.next()){
				mword = new Word(set.getInt(1), set.getString(2));
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return mword;
	}
	public static ArrayList<Word> getWords(){
		Connection con = null;
		ArrayList<Word> lst = new ArrayList<>();
		try {
			con = DatabaseHelper.getDatabaseConnection();
			Statement st = con.createStatement();
			
			ResultSet set = st.executeQuery("Select * from Word");
			while(set.next()){
				lst.add(new Word(set.getInt(1), set.getString(2)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lst;
	}
	/**
	 * Yeni kelime eklemek icin kullanilan method
	 * @param word // eklenecek kelime nesnesi
	 * @return kelimeyi ekler,  kelime var ise eklemez kelimeyi dondurur , hata olur ise null
	 */
	public static Word addNewWord(String word){
		Connection con = null;
		Word nWord = null;
		try {
			con = DatabaseHelper.getDatabaseConnection();
			PreparedStatement st = con.prepareStatement("Insert into Word(word) VALUES (?)",Statement.RETURN_GENERATED_KEYS);
			st.setString(1, word);
			long wid = st.executeUpdate();
			if(wid!=0){
				nWord = new Word((int)wid, word);
			}
				
		}  catch (MySQLIntegrityConstraintViolationException e) {
			System.out.println("Duplicate entry for a key");// kelime var ise okuma yapilmiyo 
			nWord = getWord(word);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		finally{
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return nWord;
	}
	
}
