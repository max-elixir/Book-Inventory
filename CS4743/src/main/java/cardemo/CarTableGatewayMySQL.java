package cardemo;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;

//import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class CarTableGatewayMySQL implements CarGateway {
	private Connection conn;
	
	//return a list of Car objects from the cars table
	public List<Car> getCars() {
		List<Car> cars = new ArrayList<Car>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select a.id as car_id "
					+ " , a.model, a.year, a.make_id, ifnull(b.make, '') as make "
					+ " from car a left join make b on a.make_id = b.id "
					+ " order by a.id ");
			rs = st.executeQuery();
			
			while(rs.next()) {
				Car car = new Car(new Make(rs.getInt("make_id"), rs.getString("make")), rs.getString("model"), rs.getInt("year"));
				car.setId(rs.getInt("car_id"));
				car.setGateway(this);
				cars.add(car);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cars;
	}

	public List<Make> getMakes() {
		List<Make> ret = new ArrayList<Make>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select * from make ");
			rs = st.executeQuery();
			
			while(rs.next()) {
				Make make = new Make(rs.getInt("id"), rs.getString("make"));
				ret.add(make);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * update car record in the database with the given car model data
	 * NOTE: that this method is missing audit trail record insertion and optimistic locking
	 * @param car
	 * @throws GatewayException
	 */
	public void updateCar(Car car) throws GatewayException {
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);

			st = conn.prepareStatement("update car "
				+ " set model = ? "
				+ " , year = ? "
				+ " , make_id = ? " 
				+ " where id = ?");
			st.setString(1, car.getModel());
			st.setInt(2, car.getYear());
			st.setInt(3, car.getMake().getId());
			st.setInt(4, car.getId());
			st.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			throw new GatewayException(e);
		} finally {
			//clean up
			try {
				if(st != null)
					st.close();
				
				conn.setAutoCommit(true);
				
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
	}
	
	public CarTableGatewayMySQL() throws GatewayException {
		conn = null;
		
		//connect to data source and create a connection instance
		//read db credentials from properties file
		Properties props = new Properties();
		FileInputStream fis = null;
        try {
			fis = new FileInputStream("db.properties");
	        props.load(fis);
	        fis.close();

	        //create the datasource
	        MysqlDataSource ds = new MysqlDataSource();
	        ds.setURL(props.getProperty("MYSQL_DB_URL"));
	        ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
	        ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
	        
			//create the connection
			conn = ds.getConnection();

        } catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new GatewayException(e);
		}
        
	}

	public void close() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
