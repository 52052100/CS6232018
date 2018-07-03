import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Group 3 (Lan Yang & Shiyun Zhang) – Topic: The product p1 changes its name to pp1 in Product and Stock.
 */

public class MySQLGroup3 {

	public static void main(String args[]) throws SQLException, IOException, ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connect to Driver Successfully!\n");
		} catch (Exception e) {
			System.out.println("Fail to connect to MySQL Driver");
			System.out.println(e.toString());
		}
		
		Connection conn = null;
		try {
			//For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. 
			//You need either to explicitly disable SSL by setting useSSL=false
			String myUrl = "jdbc:mysql://localhost:3306/test?verifyServerCertificate=false&useSSL=false";
			 conn = DriverManager.getConnection(myUrl, "root", "root"); 
			 System.out.println("Connect to MySQL Database Successfully!\n");
		} catch (Exception e) {
			System.out.println("Fail to connect to MySQL");
			System.out.println(e.toString());
		}
		
		// For atomicity
		conn.setAutoCommit(false);
		
		// For isolation 
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); 
		
		Statement stmt = null;
		
		stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS Stock CASCADE");
		stmt.executeUpdate("DROP TABLE IF EXISTS Product CASCADE");
		stmt.executeUpdate("DROP TABLE IF EXISTS Depot CASCADE");
		
		//Create Table (Note: In MySQL-DDL causes an implicit commit)
		stmt.executeUpdate("Create table Product("
				+ "prod_id CHAR(10),"
				+ "pname VARCHAR(128),"
				+ "price FLOAT,"
				+ "PRIMARY KEY (prod_id),"
				+ "CHECK (price > 0)"
				+ ")");
		stmt.executeUpdate("Create table Depot("
				+ "dep_id CHAR(10),"
				+ "addr VARCHAR(128),"
				+ "volume INTEGER,"
				+ "PRIMARY KEY (dep_id),"
				+ "CHECK (volume >= 0)"
				+ ")");
		stmt.executeUpdate("Create table Stock("
				+ "prod_id CHAR(10),"
				+ "dep_id CHAR(10),"
				+ "quantity INTEGER,"
				+ "PRIMARY KEY (prod_id, dep_id),"
				+ "FOREIGN KEY (prod_id) REFERENCES Product (prod_id) ON UPDATE CASCADE," 
				+ "FOREIGN KEY (dep_id) REFERENCES Depot (dep_id) ON UPDATE CASCADE"
				+ ")");
	
		try {
			/*
			 * INSERT DATA
			 */
			stmt.executeUpdate("INSERT INTO Product (prod_id, pname, price) Values" 
					+ "('p1', 'tape', 2.5)," 
					+ "('p2', 'tv', 250), "
					+ "('p3', 'vcr', 80);");
			System.out.println("Insert data to Product successfully!");
			stmt.executeUpdate("INSERT INTO Depot (dep_id, addr, volume) Values" 
					+ "('d1', 'New Yrok', 9000)," 
					+ "('d2', 'Syracuse', 6000), "
					+ "('d4', 'New Yrok', 2000);");
			System.out.println("Insert data to Depot successfully!");
			stmt.executeUpdate("INSERT INTO Stock (prod_id, dep_id, quantity) Values" 
					+ "('p1', 'd1', 1000)," 
					+ "('p1', 'd2', -100)," 
					+ "('p1', 'd4', 1200)," 
					+ "('p3', 'd1', 3000)," 
					+ "('p3', 'd4', 2000)," 
					+ "('p2', 'd4', 1500)," 
					+ "('p2', 'd1', -400)," 
					+ "('p2', 'd2', 2000);");
			System.out.println("Insert data to Stock successfully!");

			System.out.println("******Before Update******");
			ResultSet rs2 = stmt.executeQuery("select * from Product");
			System.out.println("Table Product");
			System.out.println("Prod_Id  " + "pName " + " Price ");
			while(rs2.next()) {
				System.out.println( rs2.getString("Prod_Id") 
						 + "\t " + rs2.getString("pname")
						 + "\t " + rs2.getInt("Price"));
			} 
			
			ResultSet rs3 = stmt.executeQuery("select * from Stock");
			System.out.println("\nTable Stock");
			System.out.println("Prod_Id  " + "Dep_Id " + " Quantity ");
			while(rs3.next()) {
				System.out.println(rs3.getString("Prod_Id") 
						+ "\t " + rs3.getString("Dep_Id") 
						+ "\t " + rs3.getInt("quantity"));
			} 
			
			/*
			 * UPDATE DATA
			 */
			stmt.executeUpdate("Update Product SET prod_id = 'pp1' where prod_id = 'p1'");
			System.out.println("\nUpdate Data successfully!");
			
			System.out.println("\n******After Update******");
			ResultSet rs1 = stmt.executeQuery("select * from Product");
			System.out.println("Table Product");
			System.out.println("Prod_Id  " + "pName " + " Price ");
			while(rs1.next()) {
				System.out.println( rs1.getString("Prod_Id") 
						+ "\t " + rs1.getString("pname") 
						+ "\t " + rs1.getInt("Price"));
			} 
			
			ResultSet rs = stmt.executeQuery("select * from Stock");
			System.out.println("\nTable Stock");
			System.out.println("Prod_Id  " + "Dep_Id " + " Quantity ");
			while(rs.next()) {
				System.out.println(rs.getString("Prod_Id") 
						+ "\t " + rs.getString("Dep_Id") 
						+ "\t " + rs.getInt("quantity"));
			}
			conn.commit();	
		} catch (SQLException e) {
			System.out.println("catch Exception: \n" + e.toString());
			conn.rollback();
		} finally{
			stmt.close();
			conn.close();
		}
	}
}
