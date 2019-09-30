package length;

public class Inch extends Length {

	/**
	 * Constructor to store length
	 * 
	 * @param length
	 */
	public Inch(double length) {
		super(length);
	}

	/**
	 * Add a Length object's length to this object's length
	 * 
	 * @param other
	 */
	@Override
	public void add(Length other) {
		// If the other object is also an Inch object, then don't convert.
		// Just set the length.
		if(other.getUnit().equals("inches") || other.getUnit().equals("inch")){
			this.setLength(this.getLength() + other.getLength());
		}
		else{
		// Unknown length object is converted to meters,
		// then converted to inches.
		// Add other's length in inches to this length.
		double added = other.toMeters() * INCHES_PER_METER;
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
			unit = "inches";
		} else {
			unit = "inch";
		}
		return unit;
	}

	/**
	 * Return this length but converted to meters.
	 */
	@Override
	public double toMeters() {
		double inMeters = (this.getLength()) * METERS_PER_INCH;
		return inMeters;
	}

} // end class Inch
