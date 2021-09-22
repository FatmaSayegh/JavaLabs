import java.util.Arrays;

/**
 * Represents a monster in a monster battling game. A monster has a name, one or
 * two types, and up to four moves.
 * 
 * Model solution to JP2 Lab 3, 2020.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 */
public class Monster implements TypedItem {

	/** The Monster's name */
	private String name;
	/** The Monster's types */
	private String[] types;
	/** The moves currently available to this Monster */
	private Move[] moves;

	/**
	 * Creates a new Monster with the given name and a single type.
	 * 
	 * @param name The name to use
	 * @param type The type to use
	 */
	public Monster(String name, String type) {
		this(name, new String[] { type });
	}

	/**
	 * Creates a new Monster with the given name and the two given types.
	 * 
	 * @param name  The name to use
	 * @param type1 The first type to use
	 * @param type2 The second type to use
	 */
	public Monster(String name, String type1, String type2) {
		this(name, new String[] { type1, type2 });
	}

	/**
	 * Private constructor -- used to create a Monster with one or two types.
	 * 
	 * @param name  The name to use
	 * @param types The types to use
	 */
	private Monster(String name, String[] types) {
		this.name = name;
		this.types = types;
		this.validateTypes();
		this.moves = new Move[4];
	}

	/**
	 * Validates the types -- to be called in the main constructor
	 */
	private void validateTypes() {
		for (int i = 0; i < types.length; i++) {
			String type = types[i];
			if (!TypedItem.isValidType(type))
				throw new IllegalArgumentException(String.format("The monster type %s is invalid", type));
			for (int j = i + 1; j < types.length; j++) {
				if (type.equals(types[j])) {
					throw new IllegalArgumentException("The monster cant have two similar types..");
				}
			}
		}

	}

	/**
	 * @return The monster's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the Move at the given position in this monster's list.
	 * 
	 * @param index The index to use (must be between 0 and 3)
	 * @return The Move at that position, or null if there is no such move
	 */
	public Move getMove(int index) {
		if (index < 0 || index > 3)
			throw new IllegalArgumentException("Move index must be between 0 and 3");

		return moves[index];
	}

	/**
	 * Updates the Move at the given position in this monster's list.
	 * 
	 * @param index The index to use (must be between 0 and 3)
	 * @param move  The move to store in that position
	 */
	public void setMove(int index, Move move) {
		if (index < 0 || index > 3)
			throw new IllegalArgumentException("Move index must be between 0 and 3");

		moves[index] = move;
	}

	public double getEffectivePower(Move move, Monster defender) {
		int effectivePower = move.getPower();

		for (String type : defender.getTypes())
			effectivePower *= TypedItem.getEffectiveness(move.getTypes()[0], type);

		if (this.hasType(move.getTypes()[0]))
			effectivePower *= 1.5;

		return effectivePower;
	}

	public Move chooseMove(Monster defender) {
		Move best = null;

		for (Move move : moves) {
			if (move == null)
				continue;
			if (best == null) {
				best = move;
				continue;
			}
			if (this.getEffectivePower(move, defender) > this.getEffectivePower(best, defender)) {
				best = move;
			}
		}
		return best;
	}

	/**
	 * From Superclass
	 */
	@Override
	public String[] getTypes() {
		return this.types;
	}

	@Override
	public boolean hasType(String type) {
		for (String t : types) {
			if (t == type) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a well formatted string representing this Monster.
	 */
	@Override
	public String toString() {
		return name + " " + Arrays.toString(types);
	}

}
