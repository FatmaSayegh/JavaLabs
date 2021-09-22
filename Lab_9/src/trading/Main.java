package trading;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		Random r = new Random();
		List<Trader> traders = Arrays.asList(new Trader(), new Trader());
		for (int j = 0; j < 5; j++) {
			System.out.println("\nNEW CITIZEN: " + j);
			Citizen c = new Citizen(j*10);

			
			for (int i = 0; i < 24; i++) {
				Trader trader = traders.get(r.nextInt(traders.size()));
				Trade t = trader.getTrades().get(r.nextInt(trader.getTrades().size()));
				
				System.out.println(t.toString());
				t.execute(trader, c);
				System.out.println("\tGems " + c.getGems());
				System.out.println("\tTrades " + trader.getTrades().size());

			}

			System.out.println("END GOODS: ");
			for (Goods g : Goods.values()) {
				System.out.println(g.toString() + " " + c.getAmount(g));
			}

		}
		System.out.println("END ALl: ");
		for(Trader t: traders) {
			System.out.println("Trades: " + t.getTrades().size());
			for(Trade tt: t.getTrades()) {
				System.out.println("\t" + tt.toString());

			}

		}
	}

}
