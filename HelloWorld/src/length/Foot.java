package length;

public class Foot extends Length {

	/**
	 * Constructor to store length
	 * 
	 * @param length
	 */
	public Foot(double length) {
		super(length);
	}

	/**
	 * Add a Length object's length to this object's length
	 * 
	 * @param other
	 */
	@Override
	public void add(Length other) {
		// If the other object is also a Foot object, then don't convert.
		// Just set the length.
		if(other.getUnit().equals("foot") || other.getUnit().equals("feet")){
			this.setLength(this.getLength() + other.getLength());
		}
		else{
		// Unknown length object is converted to meters,
		// then converted to feet.
		// Add other's length in feet to this length.
		double added = other.toMeters() * FEET_PER_METER;
		this.setLength(this.getLength() + added);
		}
	}

	/**
	 * Determine if the unit should be printed in singular or plural form
	 */
	@Override
	public String getUnit() {
		String unit;
		if (this.getLength() > 1.0) {
			unit = "feet";
		} else {
			unit = "foot";
		}
		return unit;
	}

	/**
	 * Return this length but converted to meters.
	 */
	@Override
	public double toMeters() {
		double inMeters = (this.getLength()) * METERS_PER_FOOT;
		return inMeters;
	}

} // end class Foot
