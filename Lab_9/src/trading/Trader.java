package trading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class allows the implementation of a Trader object
 * <br>
 * A trader is an object which has a list of trades
 * The trader object allows the following:
 * <ul>
 *  <li>Creation of a trader</li>
 *  <li>Getting the available trades</li>
 *  <li>adding a random trade</li>
 * </ul>
 * The execution of trades is done in the {@link Trade} class
 * 
 * @author Fatma alsayegh
 *
 */
public class Trader {

	private List<Trade> trades;
	private Random random;

	/**
	 * Creates a new trader with one random trade
	 */
	public Trader() {
		trades = new ArrayList<>();
		random = new Random();
		this.addRandomTrade();
	}

	/**
	 * The trader has a list of trades
	 * <br>
	 * a new random trade is added once 
	 * @return The current list of trades
	 */
	public List<Trade> getTrades() {
		return this.trades;
	}

	/**
	 * This method adds a new random trade to the trader
	 * <br>
	 * Duplicate trades are allowed
	 * <br>
	 * Called when {@link Trade#execute(Trader, Citizen)} is executed successfully
	 * <br>
	 * Adds a new random trade with the following randomness:
	 * <ul>
	 *   <li> Sets {@link Trade#gems} between 1 and 5 </li>
	 *   <li> Sets {@link Trade#amount} between 1 and 5 </li>
	 *   <li> Sets {@link Trade#good} to a random good </li>
	 * </ul>
	 */
	public void addRandomTrade() {
		Goods[] goods = Goods.values();
		trades.add(new Trade(random.nextInt(5) + 1, random.nextInt(5) + 1, goods[random.nextInt(goods.length)]));
	}

	
}
