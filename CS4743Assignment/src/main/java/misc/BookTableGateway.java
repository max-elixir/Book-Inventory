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

import model.*;

public class BookTableGateway {
	private static Logger logger = LogManager.getLogger();
	private Connection conn;
	
	public BookTableGateway() throws Exception {
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
	}
	
	public int insertBook(Book book) throws GatewayException, BookException {
		logger.info("Inserting new book "+book+" into database");
		PreparedStatement st = null, st2 = null;
		ResultSet rs = null;
		int idReturn = 0;
		
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("insert into Books "
					+ " (title, summary, year_published, isbn) "
					+ "values"
					+ "(?, ?, ?, ?)");
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYear());
			st.setString(4, book.getISBN());
			st.executeUpdate();
			conn.commit();
			
			st2 = conn.prepareStatement("SELECT * FROM Books b WHERE b.title = ?");
			st2.setString(1, book.getTitle());
			rs = st2.executeQuery();
			conn.commit();
			
			while(rs.next()) {
				idReturn = rs.getInt("id");
			} 
		} catch(SQLException e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			throw new GatewayException(e);
		} finally {
			try {
				if(st != null) 
					st.close();
				
				if(st2 != null) 
					st2.close();
				
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
		if(idReturn > 0) {
			return idReturn;
		} else {
			throw new BookException("Couldnt find ID by an SQL error");
		}
	}
	
	public List<Book> getBooks() {
		logger.info("Getting books from database");
		List<Book> books = new ArrayList<Book>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select a.id, a.title, "
					+ "a.year_published, a.summary, a.isbn "
					+ "from Books a order by a.id ");
			rs = st.executeQuery();
			
			while (rs.next()) {
				Book dbBook = new Book(rs.getString("title"), 
						rs.getString("summary"), rs.getInt("year_published"), 
						rs.getString("isbn"), rs.getInt("id"));
				dbBook.setGateway(this);
				books.add(dbBook);
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
		
		return books;
	}

	public void updateBook(Book book) throws GatewayException {
		logger.info("Updating book "+ book +" in database");
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("update Books "
					+ " set title = ? "
					+ " , year_published = ? "
					+ " , isbn = ? "
					+ " , summary = ? "
					+ " where id = ?");
			st.setString(1, book.getTitle());
			st.setInt(2, book.getYear());
			st.setString(3, book.getISBN());
			st.setString(4, book.getSummary());
			st.setInt(5, book.getId());
			st.executeUpdate();
				
			conn.commit();
		} catch(SQLException e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(e1.getMessage());
				e1.printStackTrace();
			}

			throw new GatewayException(e);
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
	
	public void deleteBook(Book book) throws GatewayException {
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("Delete from Books where id = ?");
			st.setInt(1, book.getId());
			st.executeUpdate();
				
			conn.commit();
		} catch(SQLException e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(e1.getMessage());
				e1.printStackTrace();
			}
			throw new GatewayException(e);
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
	
	public void close() {
		logger.info("Closing the gate");
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
	}

	public void lockBook(Book book) throws GatewayException {
		PreparedStatement st = null;
		try {
			logger.info("Attempting to lock " + book + " for updating.");
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			conn.setAutoCommit(false);
			st = conn.prepareStatement("Select * from Books where id = ? for update");
			st.setInt(1, book.getId());
			st.setQueryTimeout(10);
			st.executeQuery();
				
		} catch(SQLException e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(e1.getMessage());
				e1.printStackTrace();
			}

			throw new GatewayException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
		
	}

	

}
