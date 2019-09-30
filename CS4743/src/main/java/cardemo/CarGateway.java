package cardemo;

import java.util.List;

/**
 * interface for car gateways
 * @author marcos
 *
 */
public interface CarGateway {
	public List<Car> getCars();
	public List<Make> getMakes();
	public void updateCar(Car car) throws GatewayException;
	public void close();
}
