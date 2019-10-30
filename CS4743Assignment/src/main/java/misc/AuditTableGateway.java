package misc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;

import model.AuditTrailEntry;
import model.BookException;
import model.GatewayException;

public class AuditTableGateway {
	public static void pushAudit(AuditTrailEntry newEntry) throws BookException, GatewayException {
		Connection conn = null;
		Properties props = new Properties();
		FileInputStream fis = null;
        try {
			fis = new FileInputStream("db.properties");
	        props.load(fis);
	        fis.close();

	        MysqlDataSource ds = new MysqlDataSource();
	        ds.setURL(props.getProperty("MYSQL_DB_URL"));
	        ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
	        ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
			conn = ds.getConnection();
        } catch (IOException | SQLException e) {
			throw new GatewayException(e);
		}
        
        PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("insert into book_audit_trail "
					+ " (book_id, entry_msg) "
					+ "values"
					+ "(?, ?)");
			st.setInt(1, newEntry.getBookId());
			st.setString(2, newEntry.getMessage());
			st.executeUpdate();
			conn.commit();
		} catch(SQLException e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			throw new GatewayException("Gateway exception: " + e.getMessage());
		} finally {
			try {
				if(st != null) 
					st.close();
				
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
	}
	
}
