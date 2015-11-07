package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Veritabani ile alakali ekstra verilerin tutulacagi yer
 * Connection Path burada bulunuyor.
 * @author abdullahtellioglu
 *
 */
public class DatabaseHelper {
	
	public static Connection getDatabaseConnection() throws SQLException{
		return  (Connection)DriverManager.getConnection("jdbc:mysql://127.0.0.1/Vikipedia?useUnicode=true"
				+ "&characterEncoding=utf-8", "root", "tellioglu");
	}
}
