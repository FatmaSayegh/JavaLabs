package trading;

import java.util.EnumMap;

/**
 * This class shows the implementation of a Citizen object
 * <br>
 * A citizen is associated with an inventory and some amount of gems
 * <br>
 * This class allows to:
 * <ul>
 * 	<li>create a citizen with some gems</li>
 * 	<li>Get those gems</li>
 *  <li>execute trades</li>
 *  <li>Get amount goods in the inventory</li>
 * </ul>
 * @author Fatma alsayegh
 *
 */
public class Citizen {

	private int gems;
	
	/** 
	 * Use of {@link EnumMap} as its more optimized for {@link Enum} than {@link HashMap}
	 */
	private EnumMap<Goods, Integer> inventory;

	/**
	 * Creates a new citizen with gems
	 * 
	 * @param gems The amount of gems the citizen has
	 */
	public Citizen(int gems) {
		this.gems = gems;
		this.inventory = new EnumMap<> (Goods.class);
	}

	/**
	 * Get the amount of gems this citizen has
	 * @return The amount of gems
	 */
	public int getGems() {
		return this.gems;
	}

	/**
	 * Fetches the amount of a certain good from the
	 * <br>
	 * Citizen's inventory
	 * @param goods The good to get the amount of
	 * @return amount of goods the citizen has
	 */
	public int getAmount(Goods goods) {
		if (!inventory.containsKey(goods))
			return 0;
		return inventory.get(goods);
	}

	/**
	 * Executes a trade for this citizen
	 * <br>
	 * If the citizen doesn't have enough gems, method will return {@code false}
	 * <br>
	 * If enough gems are found, the trade is done and the following happens:
	 * <ul>
	 * <li>amount of gems equivalent to the {@link Trade#getGems()} is deducted from citizen</li>
	 * <li>amount of goods equivalent to the {@link Trade#getGoods()} is added to the citizens inventory</li>
	 * </ul>
	 * @param trade The trade to be executed
	 * @return if the execution is successful or not
	 */
	public boolean executeTrade(Trade trade) {
		if (getGems() < trade.getGems())
			return false;

		this.gems -= trade.getGems();
		inventory.put(trade.getGoods(), getAmount(trade.getGoods()) + trade.getAmount());
		return true;
	}

}
