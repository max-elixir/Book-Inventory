package misc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.cj.jdbc.MysqlDataSource;

import model.Publisher;

public class PublisherTableGateway {
	private static Logger logger = LogManager.getLogger();
	private static Connection conn;
	
	public PublisherTableGateway() throws Exception {
		
	}
	
	public static List<Publisher> getPublishers() throws Exception{
		conn = null;
		
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
			// TODO Make Exception class
			e.printStackTrace();
			throw e;
		}
		
		logger.info("Getting publishers from database");
		List<Publisher> publishers = new ArrayList<Publisher>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select a.id, a.publisher_name, a.date_added "
					+ "from Publisher a order by a.id ");
			rs = st.executeQuery();
			
			while (rs.next()) {
				Publisher dbPublisher = new Publisher(rs.getString("publisher_name"),  
						rs.getTimestamp("date_added"), rs.getInt("id"));
				//dbPublisher.setGateway(this);
				publishers.add(dbPublisher);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
		
		return publishers;
	}
}
