import java.sql.*;

public class Final {

	public static void main(String args[]) throws SQLException, IOException, ClassNotFoundException {
		// Load the MySQL driver
		Class.forName("com.mysql.jdbc.Driver");
		
		String myUrl = "jdbc:mysql://localhost:3306/test";
		
		// Connect to the database
		
		Connection conn = DriverManager.getConnection(myUrl, "root", "password"); 
		//If you got error about java.sql.SQLException: The server time zone value 'EDT' is unrecognized or represents more than one time zone.
		//You can add "?useLegacyDatetimeCode=false&serverTimezone=America/New_York" after your database name. 
		// Like conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?useLegacyDatetimeCode=false&serverTimezone=America/New_York");
		
		// For atomicity
		conn.setAutoCommit(false);
		
		// For isolation 
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); 
		
		
		Statement stmt = null;
		try {
			// create statement object stmt;
			stmt = conn.createStatement();
			
			// Drop the table if this table exist;
			// If the table is already created and set primary key, we cannot alter the primary key value; 
			stmt.executeUpdate("DROP TABLE IF EXISTS Stock CASCADE");
			stmt.executeUpdate("DROP TABLE IF EXISTS Product CASCADE");
			stmt.executeUpdate("DROP TABLE IF EXISTS Depot CASCADE");
			
			//Create Table
			// Using IF NOT EXISTS to check the table is exist or not when we create table;
			// Using stmt.executeUpdate to create three tables.
			stmt.executeUpdate("Create table IF NOT EXISTS Product("
					+ "prod_id CHAR(10),"
					+ "pname VARCHAR(128),"
					+ "price DECIMAL,"
					+ "PRIMARY KEY (prod_id),"
					+ "CHECK (price > 0)"
					+ ")");
			stmt.executeUpdate("Create table IF NOT EXISTS Depot("
					+ "dep_id CHAR(10),"
					+ "addr VARCHAR(128),"
					+ "volume INTEGER,"
					+ "PRIMARY KEY (dep_id),"
					+ "CHECK (volume >= 0)"
					+ ")");
			stmt.executeUpdate("Create table IF NOT EXISTS Stock("
					+ "prod_id CHAR(10),"
					+ "dep_id CHAR(10),"
					+ "quantity INTEGER,"
					+ "PRIMARY KEY (prod_id, dep_id),"
					+ "FOREIGN KEY (prod_id) REFERENCES Product (prod_id) ON UPDATE CASCADE," 
					+ "FOREIGN KEY (dep_id) REFERENCES Depot (dep_id) ON UPDATE CASCADE"
					+ ")");
			// Set FOREIGN KEY (prod_id) and FOREIGN KEY (dep_id) to table Product and Depot.
			
			// Insert value into tables;
			stmt.executeUpdate("INSERT INTO Product (prod_id, pname, price) Values" 
					+ "('p1', 'tape', 2.5)," 
					+ "('p2', 'tv', 250), "
					+ "('p3', 'vcr', 80);");
			stmt.executeUpdate("INSERT INTO Depot (dep_id, addr, volume) Values" 
					+ "('d1', 'New Yrok', 9000)," 
					+ "('d2', 'Syracuse', 6000), "
					+ "('d4', 'New Yrok', 2000);");
			stmt.executeUpdate("INSERT INTO Stock (prod_id, dep_id, quantity) Values" 
					+ "('p1', 'd2', -100)," 
					+ "('p1', 'd4', 1200)," 
					+ "('p3', 'd1', 3000)," 
					+ "('p3', 'd4', 2000)," 
					+ "('p2', 'd4', 1500)," 
					+ "('p2', 'd1', -400)," 
					+ "('p2', 'd2', 2000);");

			// Print out the tables by using ResultSet.get+datatype of value;
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
			
			
			System.out.println("\n******After Update******");
			// Alter the prod_id from p1 to pp1; 
			// Since we have the reactive constraint "FOREIGN KEY (prod_id) REFERENCES Product (prod_id) ON UPDATE CASCADE" in table stock;
			// When we alter the prod_id in table product, the prod_id in table stock will alter automatic.
			stmt.executeUpdate("Update Product SET prod_id = 'pp1' where prod_id = 'p1'");
			
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
			// Print out the tables; 
			while(rs.next()) {
				System.out.println(rs.getString("Prod_Id") 
						+ "\t " + rs.getString("Dep_Id") 
						+ "\t " + rs.getInt("quantity"));
			} 
			
		} catch (SQLException e) {
			System.out.println("catch Exception: " + e);
		// If error happens, calls the Connection method rollback to aborts a transaction and restores values to what they were before the attempted update;
			conn.rollback();
		//Close the Statement immediately;
			stmt.close();
		// Close the connection immediately;
			conn.close();
			return;
		} 
		//After the auto-commit mode is disabled by default, no SQL statements are committed until you call the method commit explicitly.
		conn.commit();
		//Close the Statement immediately;
		stmt.close();
		// Close the connection immediately;
		conn.close();
	}
}
