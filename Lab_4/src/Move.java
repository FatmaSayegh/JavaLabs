/**
 * Represents a single move in a monster battling game.
 * 
 * Model solution to JP2 Lab 3, 2020.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 */

public class Move implements TypedItem{
	
	/** The move type */
	private String type;
	/** The move's name */
	private String name;
	/** The move's power */
	private int power;
	
	/**
	 * Creates a new Move with the given parameters
	 * @param name The name to use
	 * @param type The type to use
	 * @param power The power to use
	 */
	public Move(String name, String type, int power) {
		
		// validate
		if (!TypedItem.isValidType(type)){
			throw new IllegalArgumentException(String.format("Invalid move type %s", type));
		}
		
		if (power < 0 ||power > 180) {
			throw new IllegalArgumentException(String.format("Power must be between 0 and 180"));
		}
		
		this.type = type;
		this.name = name;
		this.power = power;
	}
	/**
	 * @return The name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return The power
	 */
	public int getPower() {
		return this.power;
	}
	
	/**
	 * Superclass
	 */

	@Override
	public boolean hasType(String type) {
		return this.type == type;
	}

	@Override
	public String[] getTypes() {
		return new String[]{type};
	}
	
	
	/**
	 * Returns a well formatted string representing this Move.
	 */
	@Override
	public String toString() {
		return name + " (" + type + "): " + power;
	}

}
