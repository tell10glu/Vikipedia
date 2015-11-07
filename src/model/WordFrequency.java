package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import helpers.DatabaseHelper;

public class WordFrequency {
	private int word1;
	private int word2;
	private int freq;
	public int getWord1() {
		return word1;
	}
	public int getWord2() {
		return word2;
	}
	public int getFreq() {
		return freq;
	}
	private WordFrequency(int word1, int word2, int freq) {
		super();
		this.word1 = word1;
		this.word2 = word2;
		this.freq = freq;
	}
	public static ArrayList<WordFrequency> getWordsWithFrequency(){
		Connection con = null;
		ArrayList<WordFrequency> lst = new ArrayList<>();
		try {
			con = DatabaseHelper.getDatabaseConnection();
			Statement st = con.createStatement();
			ResultSet set = st.executeQuery("Select * from WordFrequency");
			while(set.next()){
				lst.add(new WordFrequency(set.getInt(1), set.getInt(2), set.getInt(3)));
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
	 * Eger kelimeler yok ise eklenir ve frekans 0 olarak atanir.
	 * Eger kelimeler var ise frekansi 1 arttirilir.??
	 * 
	 * @param word1
	 * @param word2
	 * @param upgrade
	 */
	public static void bindWords(Word w1,Word w2){
		WordFrequency freq = null;
		if((freq = getFrequeny(w1, w2))!=null){
			if(w1.getWord().compareTo(w2.getWord())<0){

				setFreq(w1, w2,freq.getFreq()+1);
			}else{
				setFreq(w2, w1,freq.getFreq()+1);
			}
		}else{
			if(w1.getWord().compareTo(w2.getWord())<0){

				setFreq(w1, w2,0);
			}else{
				setFreq(w2, w1,0);
			}
		}
	}
	/**
	 * kelimelerin frekanslarini ata.
	 * Append true ise 
	 * Eski frekans + val degeri
	 * Append false ise
	 * Val degeri.
	 * @param w1 kelime 1
	 * @param w2 kelime 2
	 * @param val frekans degeri
	 * @param append eklenecek olan deger ustune mi eklensin yoksa direk mi atansin
	 */
	public static void bindWords(Word w1,Word w2,int val,boolean append){
		int tmpVal = val;
		if(append){
			WordFrequency tmpFreq = null;
			if((tmpFreq =WordFrequency.getFrequeny(w1, w2))!=null)
				tmpVal += tmpFreq.getFreq();
		}
		if(w1.getWord().compareTo(w2.getWord())<0){
			setFreq(w1, w2,tmpVal);
		}else{
			setFreq(w2, w1,tmpVal);	
		}
	}
	/**
	 * iki kelimenin baglantisi yapilir. Kelimeler yok ise eklenir var ise guncelleme yapilir.
	 * @param w1 kelime 1
	 * @param w2 kelime 2
	 * @param freq kelimelerin frekansi
	 */
	private static void setFreq(Word w1,Word w2,int freq){
		Connection con = null;
		try {
			con = DatabaseHelper.getDatabaseConnection();
			PreparedStatement st ;
			if(getFrequeny(w1, w2)==null){
				st = con.prepareStatement("insert into WordFrequency(word1,word2,frequency) VALUES(?,?,?)");
				st.setInt(1, w1.getId());
				st.setInt(2, w2.getId());
				st.setInt(3, freq);
				
			}else{
				st = con.prepareStatement("update WordFrequency SET frequency = ? where word1= ? AND word2 = ?");
				st.setInt(1, freq);
				st.setInt(2, w1.getId());
				st.setInt(3, w2.getId());
			}
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	public static WordFrequency getFrequeny(Word w1,Word w2){
		Connection con = null;
		WordFrequency wordFreq = null;
		try {
			con = DatabaseHelper.getDatabaseConnection();
			PreparedStatement st = con.prepareStatement("Select * from WordFrequency where word1 = ? AND word2 = ?");
			/* 
			 * kontroldeki amac veritabaninda tabloda alfabetik olarak büyük olan ilk sıraya geçicek
			 * mesela word1 ayse, 3 ; word2 fatma,5 geldi
			 * veritabaninda bunların sadece bir kere kayitlarinin tutulmasi gerekir
			 * 3,5,frekans ve 5,3,frekans diye tutulmamasi icin boyle bir kontrol gerekli.
			 * 
			*/
			if(w1.getWord().compareTo(w2.getWord())<0){
				st.setInt(1, w1.getId());
				st.setInt(2, w2.getId());
			}else{
				st.setInt(2, w1.getId());
				st.setInt(1, w2.getId());
			}
			
			ResultSet set = st.executeQuery();
			if(set.next()){
				wordFreq = new WordFrequency(set.getInt(1), set.getInt(2), set.getInt(3));
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
		return wordFreq;
	}
	
}
