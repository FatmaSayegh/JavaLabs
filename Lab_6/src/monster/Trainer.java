package monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a Trainer who has a collection of battle monsters.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 */
public class Trainer {
	/** The trainer's name */
	private String name;
	/** The trainer's set of monsters */
	private Set<Monster> monsters;
	
	/**
	 * Creates a new Trainer with the specified name and an empty set of monsters.
	 * 
	 * @param name The name to use
	 */
	public Trainer (String name) {
		this.name = name;
		this.monsters = new HashSet<>();
	}
	
	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Updates the trainer's name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Adds a new monster to the set. Return value indicates whether the monster was already in the set.
	 * 
	 * @param m The monster to add
	 * @return Whether the monster was already in the set
	 */
	public boolean addMonster (Monster m) {
		if (!monsters.contains(m)) {
			monsters.add(m);
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a monster from the set. Return value indicates whether the monster was in the set previously.
	 * 
	 * @param m The monster to remove
	 * @return Whether the monster was in the set before
	 */
	public boolean removeMonster (Monster m) {
		return monsters.remove(m);
	}
	
	/**
	 * Returns all of this trainer's monsters.
	 * 
	 * @return The full collection of monsters
	 */
	public Collection<Monster> getMonsters() {
		return monsters;
	}
	
	/**
	 * Chooses a non-fainted monster for battle. If there are no non-fainted monsters, returns null.
	 * 
	 * @return A battle-ready monster, or null if there are none
	 * @see Monster#isFainted()
	 */
	public Monster chooseBattleMonster() {
		for (Monster m : monsters) {
			if (!m.isFainted()) {
				return m;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Simulates a battle between the two trainers. Each trainer in turn chooses a
	 * monster for battle, and then attacks the other. If a trainer has no available
	 * monsters, the other one wins.
	 * 
	 * @param trainer1 The first trainer in battle -- will attack first
	 * @param trainer2 The second trainer in battle -- will defend first
	 * @return The winning trainer after the battle
	 * @see #chooseBattleMonster()
	 */
	public static Trainer doBattle (Trainer trainer1, Trainer trainer2) {
		Trainer attacker = trainer1, defender = trainer2;
		while (true) {
			// Keep going until one trainer has no battle-ready monsters
			Monster attackMon = attacker.chooseBattleMonster();
			Monster defendMon = defender.chooseBattleMonster();
			if (attackMon == null) {
				return defender;
			} else if (defendMon == null) {
				return attacker;
			}
			
			// Do the attack
			attackMon.attack(defendMon);
			
			// Swap roles
			Trainer t = attacker;
			attacker = defender;
			defender = t;
		}	
	}
	
	public List<String> toStringForFile() {
		List<String> s = new ArrayList<String>();
		s.add(getName());
		for (Monster m: this.monsters) {
			String mons = "";
			for (String ms: m.toStringForFile()) {
				if (mons == "") {
					mons = ms;
					continue;
				}
				mons += "+" + ms;
			}
			s.add(mons);
		}
		return s;
	}
	
	
	public static Trainer parseTrainer (List<String> spec) {
		if (spec.isEmpty())
			return null;
		Trainer t = new Trainer(spec.get(0));
		for (String s: spec) {
			if(spec.indexOf(s) == 0)
				continue;
			Monster m = Monster.parseMonster(Arrays.asList(s.split("\\+")));
			t.addMonster(m);
		}
		return t;
	}
}
