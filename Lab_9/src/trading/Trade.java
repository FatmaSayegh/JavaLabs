package trading;

/**
* The Trade class aims to implement a trading object
* <br>
* It allows the creation of a Trade object which links
* a certain amount of some good to a price
* <br>
* The class also has functionality to allow:
* <ul>
*  <li>Create a trade consisting of an amount of {@link Goods} and its price</li>
*  <li>Getting the price of this trade</li>
*  <li>Executing this trade</li>
* 
* </ul>
* 
*
* @author  Fatma alsayegh
*/

public class Trade {

	private int gems;
	private int amount;
	private Goods goods;

	/**
	 * Creates a new trade
	 * 
	 * @param gems The price of this trade
	 * @param amount The amount of foods to be given on a successful trade
	 * @param good The type of good to be given
	 */
	public Trade(int gems, int amount, Goods goods) {
		this.gems = gems;
		this.amount = amount;
		this.goods = goods;
	}

	/**
	 * Every trade has a certain price in gems 
	 * <br>
	 * this price is defined in the constructor
	 * @return The price of this trade
	 */
	public int getGems() {
		return gems;
	}

	/**
	 * This method gets the amount of goods that this trade provides
	 * 
	 * @return The amount of goods to be given
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Gets the type of goods this trade has
	 * @return The type of goods associated with this trade
	 */
	public Goods getGoods() {
		return goods;
	}

	/**
	 * Executes this trade from a {@link Trader} to a {@link Citizen}
	 * <br>
	 * throws {@link IllegalArgumentException} if  trader doesn't have this trade
	 * <br>
	 * does nothing if the citizen doesn't execute trade successfully
	 * 
	 * @param trader The trader which has the current trade
	 * @param citizen the citizen to perform the trade on
	 */
	public void execute(Trader trader, Citizen citizen) {
		if (!trader.getTrades().contains(this))
			throw new IllegalArgumentException("Trader does not contain this trade!");
		if (!citizen.executeTrade(this))
			return;
		trader.addRandomTrade();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + gems;
		result = prime * result + ((goods == null) ? 0 : goods.hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trade other = (Trade) obj;
		if (amount != other.amount)
			return false;
		if (gems != other.gems)
			return false;
		if (goods != other.goods)
			return false;
		return true;
	}

	/**
	 * Shows a nice string representation as follows:
	 * X gems for Y GOOD
	 */
	@Override
	public String toString() {
		return this.gems + " gems for " + this.amount + " " + this.goods.toString();
	}

}
