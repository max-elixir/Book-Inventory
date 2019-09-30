package cardemo;

public class Make {
	private int id;
	private String make;
	
	public Make() {
		id = 0;
		make = "";
	}
	
	public Make(int id, String make) {
		this.id = id;
		this.make = make;
	}

	@Override
	public String toString() {
		return make;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}
}
