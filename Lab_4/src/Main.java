
public class Main {

	/**
	 * A main method that can be used to test your implementation. Feel free to edit
	 * this method as much as you like -- you can add method calls, change
	 * parameters, whatever makes it easy to test your own code. You do not need to
	 * submit this class as part of the lab.
	 */
	public static void main(String[] args) {
		// Here are some sample method calls -- try your own too!
		System.out.println(TypedItem.getEffectiveness("Fire", "Water"));

		System.out.println(TypedItem.isValidType("Fire"));
		System.out.println(TypedItem.isValidType("FFFFire"));

		// Here are some valid monsters and moves you can use to test things
		Monster heliolisk = new Monster("Heliolisk", "Electric", "Normal");
		Monster ludicolo = new Monster("Ludicolo", "Grass", "Water");
		Monster cinderace = new Monster("Cinderace", "Fire");

		Move thunderbolt = new Move("Thunderbolt", "Electric", 90);
		Move quickAttack = new Move("Quick Attack", "Normal", 40);
		Move hyperBeam = new Move("Hyper Beam", "Normal", 150);

		heliolisk.setMove(0, thunderbolt);
		heliolisk.setMove(1, quickAttack);
		heliolisk.setMove(2, hyperBeam);

		Move energyBall = new Move("Energy Ball", "Grass", 100);
		Move hydroPump = new Move("Hydro Pump", "Water", 110);
		Move firePunch = new Move("Fire Punch", "Fire", 75);
		Move megaPunch = new Move("Mega Punch", "Normal", 80);

		ludicolo.setMove(0, energyBall);
		ludicolo.setMove(1, hydroPump);
		ludicolo.setMove(2, firePunch);
		ludicolo.setMove(3, megaPunch);

		Move pyroBall = new Move("Pyro Ball", "Fire", 120);

		cinderace.setMove(0, pyroBall);

		// Some things to try with the monsters
		System.out.println(ludicolo.chooseMove(heliolisk));
		System.out.println(heliolisk.chooseMove(cinderace));
		System.out.println(cinderace.chooseMove(ludicolo));

		System.out.println(ludicolo.getEffectivePower(firePunch, cinderace));
		System.out.println(ludicolo.getEffectivePower(hydroPump, cinderace));
	}

}
