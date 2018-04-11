package Model;
import java.io.*;
import java.sql.*;

/** 
 * This ImportMySQLDB class clears all the information in the dataBase. 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class ImportMySQLDB {
	public void clear() {
		Connection con = null;
		String url = "jdbc:mysql://localhost/";
		String db = "";
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver);
			System.out.println("Driver Loaded");
			con = DriverManager.getConnection(url + db, "scott", "tiger");
			System.out.println("Connection Established");
			Statement st = con.createStatement();

			FileInputStream fstream = new FileInputStream("fastAndFurious.sql");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine = "", strLine1 = "";
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				if (strLine != null && !strLine.trim().equals("")) {
					if ((strLine.trim().indexOf("/*") < 0) && (strLine.trim().indexOf("*/") < 0)) {
						if (strLine.indexOf(';') >= 0) {
							strLine1 += strLine;
							System.out.println(strLine1);
							st.execute(strLine1);
							strLine1 = "";
						} else
							strLine1 += strLine;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
		}
	}
}
