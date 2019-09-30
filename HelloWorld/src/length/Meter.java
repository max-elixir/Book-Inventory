package length;

/**
 * A Meter is a subclass of Length and holds a length and sets the unit to be
 * "meter" or "meters". Other Length objects can be added to this object.
 */
public class Meter extends Length {

	/**
	 * Constructor to store length
	 * 
	 * @param length
	 */
	public Meter(double length) {
		super(length);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Add a Length object's length to this object's length
	 * 
	 * @param other
	 */
	@Override
	public void add(Length other) {
		// Unknown length object is converted to meters.
		// Add other's length in meters to this length.
		double added = this.getLength() + other.toMeters();
		this.setLength(added);
	}

	/**
	 * Determine if the unit should be printed in singular or plural form
	 */
	@Override
	public String getUnit() {
		String unit;
		if (this.getLength() > 1.0) {
			unit = "meters";
		} else {
			unit = "meter";
		}
		return unit;
	}

	/**
	 * Return this length but converted to meters.
	 */
	@Override
	public double toMeters() {
		// no conversion needed
		double inMeters = this.getLength();
		return inMeters;
	}

} // end class Meter
