package length;

public class Yard extends Length {

	/**
	 * Constructor to store length
	 * 
	 * @param length
	 */
	public Yard(double length) {
		super(length);
	}

	/**
	 * Add a Length object's length to this object's length
	 * 
	 * @param other
	 */
	@Override
	public void add(Length other) {
		// If the other object is also a Yard object, then don't convert.
		// Just set length
		if(other.getUnit().equals("yards") || other.getUnit().equals("yard")){
			this.setLength(this.getLength() + other.getLength());
		}
		else{
		// Unknown length object is converted to meters,
		// then converted to yards.
		// Add other's length in yards to this length.
		double added = other.toMeters() * YARDS_PER_METER;
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
			unit = "yards";
		} else {
			unit = "yard";
		}
		return unit;
	}

	/**
	 * Return this length but converted to meters.
	 */
	@Override
	public double toMeters() {
		double inMeters = (this.getLength()) * METERS_PER_YARD;
		return inMeters;
	}

} // end class Yard
