package cardemo;

/**
 * model for Car object
 * might be an instance of real physical car 
 * @author marcos
 *
 */
public class Car {
	private int id;
	//private String make;
	private Make make;
	private String model;
	private int year;
	
	private CarGateway gateway;
	
	public Car() {
		//set default values
		make = new Make();
		model = "";
		year = 0;
		gateway = null;
	}

	public Car(Make make, String model, int year) {
		this();
		this.make = make;
		this.model = model;
		this.year = year;
	}
	

	public void save() throws GatewayException, ValidationException {
		//call validators
		if(!isValidId(getId()))
			throw new ValidationException("Invalid Id: " + getId());
		if(!isValidMake(getMake()))
			throw new ValidationException("Invalid Make: " + getMake());

		//only handle update for now
		gateway.updateCar(this);
	}
	
	// accessors 
	
	public boolean isValidId(int id) {
		if(id < 0)
			return false;
		return true;
	}
	
	public boolean isValidMake(Make make) {
		if(make == null || make.getId() == 0)
			return false;
		return true;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Make getMake() {
		return make;
	}

	public void setMake(Make make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public CarGateway getGateway() {
		return gateway;
	}

	public void setGateway(CarGateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public String toString() {
		return id + " : " + make + " " + model;
	}
	
	
}
