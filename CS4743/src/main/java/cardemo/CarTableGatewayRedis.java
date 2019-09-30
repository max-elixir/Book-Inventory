package cardemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class CarTableGatewayRedis implements CarGateway {
	private Jedis jedis;
	
	//return a list of Car objects from the cars table
	public List<Car> getCars() {
		List<Car> cars = new ArrayList<Car>();

		//get all car hash entries with a key that start with "car_"
		Set<String> keys = jedis.keys("car_*");
		
		for(String key : keys) {
			Map<String, String> map = jedis.hgetAll(key);
			
			//id of car is part after _
			int carId = Integer.parseInt(key.substring(key.indexOf('_') + 1));
			
			int makeId = jedis.zscore("makes", map.get("make")).intValue();
			
			Car car = new Car(new Make(makeId, map.get("make")), map.get("model"), Integer.parseInt(map.get("year")));

			car.setId(carId);
			car.setGateway(this);
			cars.add(car);
		}
		return cars;
	}

	public List<Make> getMakes() {
		List<Make> ret = new ArrayList<Make>();
		
		Set<String> makeStrings = jedis.zrange("makes", 0, -1);
		for(String makeString : makeStrings) {
			int makeId = jedis.zscore("makes", makeString).intValue();
			Make make = new Make(makeId, makeString);
			ret.add(make);	
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
		//create a hash map of all car field names and values
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("make", car.getMake().getMake());
		map.put("model", car.getModel());
		map.put("year", Integer.toString(car.getYear()));
		
		//save the map under the car's key
		jedis.hmset("car_" + car.getId(), map);
	}
	
	public CarTableGatewayRedis() throws GatewayException {
		jedis = new Jedis("easel2.fulgentcorp.com");
		jedis.auth("pd6BvDKAEMXhxwUg");
		jedis.select(0);
	}

	public void close() {
		if(jedis != null) {
			jedis.close();
		}
	}
}
