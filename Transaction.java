import java.sql.*;
public class Transaction {
	static Connection conn;
	static Statement stmt;
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			 conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?useLegacyDatetimeCode=false&serverTimezone=America/New_York","root","52052100lkh");
		    System.out.println("Connected database successfully");
		    
		    conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			stmt = conn.createStatement();
			stmt.executeUpdate( "Create table IF NOT EXISTS Product("
					+ "prod_id  char(10) NOT NULL, "
					+ "pname varchar(30),"
					+ "price double,"
					+ "PRIMARY KEY (prod_id))");
			stmt.executeUpdate ("Create table IF NOT EXISTS Depot( "
					+ "dep_id  char(10) NOT NULL, "
					+ "addr varchar(30), "
					+ "volume decimal,"
					+ "PRIMARY KEY (dep_id ))");
			stmt.executeUpdate ("Create table IF NOT EXISTS Stock("
					+ "prod_id  char(10) NOT NULL,"
					+ "dep_id  char(10) NOT NULL,"
					+ "quantity INTEGER, "
					+ "FOREIGN KEY (prod_id) REFERENCES Product (prod_id), "
					+ "FOREIGN KEY (dep_id) REFERENCES Depot (dep_id))");
			
			stmt.executeUpdate ("insert into Product values  ('p1','tape',2.5)");
			stmt.executeUpdate ("insert into Product values  ('p2','tv',250)");
			stmt.executeUpdate ("insert into Product values  ('p3','vcr',80)");

			stmt.executeUpdate ("insert into Depot values ('d1','New York',9000)");
			stmt.executeUpdate ("insert into Depot values ('d2','Syracuse',6000)");
			stmt.executeUpdate ("insert into Depot values ('d4','New York',2000)");
			
			stmt.executeUpdate ("insert into Stock values ('p1','d1', 1000)");
			stmt.executeUpdate ("insert into Stock values ('p1','d2', -100)");
			stmt.executeUpdate ("insert into Stock values ('p1','d4', 1200)");
			stmt.executeUpdate ("insert into Stock values ('p3','d1', 3000)");
			stmt.executeUpdate ("insert into Stock values ('p3','d4', 2000)");
			stmt.executeUpdate ("insert into Stock values ('p2','d4', 1500)");
			stmt.executeUpdate ("insert into Stock values ('p2','d1', -400)");
			stmt.executeUpdate ("insert into Stock values ('p2','d2', 2000)");

			 ResultSet rs1 = stmt.executeQuery ("select *  from  Product"); 
			 while (rs1.next() ){ 
					System.out.print(rs1.getString("prod_id")+" ");  
					System.out.print(rs1.getString("pname")+" "); 
					System.out.print(rs1.getDouble("price")+" ");  
					System.out.println();
				}
			 ResultSet rs2 = stmt.executeQuery ("select *  from  Depot"); 

			 while (rs2.next() ){ 
					System.out.print(rs2.getString("dep_id")+" ");  
					System.out.print(rs2.getString("addr")+" "); 
					System.out.print(rs2.getBigDecimal("volume")+" ");  
					System.out.println();
				}
			 ResultSet rs3 = stmt.executeQuery ("select *  from  Stock"); 

			 while (rs3.next() ){ 
					System.out.print(rs3.getString("prod_id")+" ");  
					System.out.print(rs3.getString("dep_id")+" "); 
					System.out.print(rs3.getInt("quantity")+" ");  
					System.out.println();
				}


		} catch (SQLException e) {
			System.out.println("catch Exception");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}		
			}
		
		finally {
			try {
				conn.commit();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		

	}

}

