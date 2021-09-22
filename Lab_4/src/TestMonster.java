
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMonster {
	@Test
	public void typedItemIsPublicInterface() {
		Assertions.assertAll("TypedItem not correctly declared",
				() -> Assertions.assertTrue(TypedItem.class.isInterface(), "Not declared as an interface"),
				() -> Assertions.assertTrue(Modifier.isPublic(TypedItem.class.getModifiers()), "Not public"));
	}

	@Test
	public void typedItemHasCorrectStaticMethods() {
		Method[] methods = TypedItem.class.getDeclaredMethods();
		boolean vtFound = false, geFound = false;
		for (Method m : methods) {
			if (Modifier.isStatic(m.getModifiers())) {
				String name = m.getName();
				switch (name) {
				case "isValidType":
					Assertions.assertAll("isValidType signature incorrect",
							() -> Assertions.assertEquals(boolean.class, m.getReturnType(), "Wrong return type"),
							() -> Assertions.assertArrayEquals(new Class[] { String.class }, m.getParameterTypes(),
									"Wrong parameters"));
					vtFound = true;
					break;

				case "getEffectiveness":
					Assertions.assertAll("getEffectiveness signature incorrect",
							() -> Assertions.assertEquals(double.class, m.getReturnType(), "Wrong return type"),
							() -> Assertions.assertArrayEquals(new Class[] { String.class, String.class },
									m.getParameterTypes(), "Wrong parameters"));
					geFound = true;
					break;

				default:
					Assertions.fail("Unexpected static method: " + m);
				}
			}
		}
		Assertions.assertTrue(vtFound && geFound, "Static method(s) missing from TypedItem");
	}

	@Test
	public void typedItemHasCorrectAbstractMethods() {
		Method[] methods = TypedItem.class.getDeclaredMethods();
		boolean htFound = false, gtFound = false;
		for (Method m : methods) {
			if (!Modifier.isStatic(m.getModifiers())) {
				String name = m.getName();
				switch (name) {
				case "hasType":
					Assertions.assertAll("hasType signature incorrect",
							() -> Assertions.assertEquals(boolean.class, m.getReturnType(), "Wrong return type"),
							() -> Assertions.assertArrayEquals(new Class[] { String.class }, m.getParameterTypes(),
									"Wrong parameters"));
					htFound = true;
					break;

				case "getTypes":
					Assertions.assertAll("getEffectiveness signature incorrect",
							() -> Assertions.assertEquals(String[].class, m.getReturnType(), "Wrong return type"),
							() -> Assertions.assertArrayEquals(new Class[0], m.getParameterTypes(),
									"Wrong parameters"));
					gtFound = true;
					break;

				default:
					Assertions.fail("Unexpected non-static method: " + m);
				}
			}
		}
		Assertions.assertTrue(htFound && gtFound, "Abstract method(s) missing from TypedItem");
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void monsterAndMoveImplementTypedItem() {
		List<Class> mnInterfaces = Arrays.asList(Monster.class.getInterfaces());
		List<Class> mvInterfaces = Arrays.asList(Move.class.getInterfaces());
		Assertions.assertAll("Class(es) do not implement TypedItem",
				() -> Assertions.assertTrue(mnInterfaces.contains(TypedItem.class), "Monster"),
				() -> Assertions.assertTrue(mvInterfaces.contains(TypedItem.class), "Move"));
	}

	private static final String[] TYPES = { "Normal", "Fire", "Water", "Electric", "Grass" };

	@Test
	public void isValidTypeRecognisesValidTypes() {
		for (String type : TYPES) {
			Assertions.assertTrue(TypedItem.isValidType(type),
					"isValidType does not recognise valid type \"" + type + "\"");
		}
	}

	@Test
	public void isValidTypeRecognisesInvalidTypes() {
		Assertions.assertFalse(TypedItem.isValidType(null), "isValidType does not recognise invalid type null");
		Assertions.assertFalse(TypedItem.isValidType(""), "isValidType does not recognise invalid type \"\"");
		Assertions.assertFalse(TypedItem.isValidType("foobar"),
				"isValidType does not recognise invalid type \"foobar\"");
	}

	private void testEffectiveness(String type1, String type2, double expected) {
		Assertions.assertEquals(expected, TypedItem.getEffectiveness(type1, type2),
				"getEffectiveness(" + type1 + "," + type2 + ")");
	}

	@Test
	public void getEffectivenessCoversAllOptions() {
		double low = 0.5;
		double medium = 1.0;
		double high = 2.0;

		// Normal is easy
		for (String type : TYPES) {
			testEffectiveness("Normal", type, medium);
			testEffectiveness(type, "Normal", medium);
		}

		// For our subset, every type is weak against itself
		for (String type : Arrays.copyOfRange(TYPES, 1, TYPES.length)) {
			testEffectiveness(type, type, low);
		}

		// Okay, now do the rest of them (ugh)
		testEffectiveness("Fire", "Water", low);
		testEffectiveness("Fire", "Electric", medium);
		testEffectiveness("Fire", "Grass", high);
		testEffectiveness("Water", "Fire", high);
		testEffectiveness("Water", "Electric", medium);
		testEffectiveness("Water", "Grass", low);
		testEffectiveness("Electric", "Fire", medium);
		testEffectiveness("Electric", "Water", high);
		testEffectiveness("Electric", "Grass", low);
		testEffectiveness("Grass", "Fire", low);
		testEffectiveness("Grass", "Water", high);
		testEffectiveness("Grass", "Electric", medium);
	}

	@Test
	public void moveConstructorThrowsExceptionOnBadInput() {
		Assertions.assertAll("Move constructor should throw exception on bad input",
				() -> Assertions.assertThrows(IllegalArgumentException.class, () -> {
					new Move("name", "foobar", 100);
				}, "Invalid type"), () -> Assertions.assertThrows(IllegalArgumentException.class, () -> {
					new Move("name", "Fire", -5);
				}, "Negative power"), () -> Assertions.assertThrows(IllegalArgumentException.class, () -> {
					new Move("name", "Fire", 300);
				}, "Power out of range"));
	}

	@Test
	public void moveConstructorAcceptsGoodInput() {
		// All types
		for (String type : TYPES) {
			Assertions.assertDoesNotThrow(() -> {
				new Move("abc", type, 100);
			});
		}
		// Edge cases on power
		Assertions.assertDoesNotThrow(() -> {
			new Move("abc", "Normal", 0);
		});
		Assertions.assertDoesNotThrow(() -> {
			new Move("abc", "Normal", 180);
		});
	}

	@Test
	public void monsterConstructorThrowsExceptionOnBadInput() {
		Assertions.assertAll("Monster constructor should throw exception on bad type",
				() -> Assertions.assertThrows(IllegalArgumentException.class, () -> {
					new Monster("name", "foobar");
				}), () -> Assertions.assertThrows(IllegalArgumentException.class, () -> {
					new Monster("name", "Fire", "foobar");
				}), () -> Assertions.assertThrows(IllegalArgumentException.class, () -> {
					new Monster("name", "foobar", "Fire");
				}));
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Monster("name", "Water", "Water");
		}, "Monster constructor should throw exception on duplicate type");
	}

	@Test
	public void monsterConstructorAcceptsGoodInput() {
		// All types
		for (String type : TYPES) {
			Assertions.assertDoesNotThrow(() -> {
				new Monster("abc", type);
			});
			for (String type2 : TYPES) {
				if (!type2.equals(type)) {
					Assertions.assertDoesNotThrow(() -> {
						new Monster("abc", type, type2);
					});
				}
			}
		}
	}

	@Test
	public void getSetMoveThrowCorrectException() {
		Monster m = new Monster("aaa", "Electric");
		Move mv = new Move("bbb", "Electric", 100);

		Assertions.assertAll("getMove should throw IllegalArgumentException",
				() -> Assertions.assertThrows(IllegalArgumentException.class, () -> {
					m.getMove(4);
				}), () -> Assertions.assertThrows(IllegalArgumentException.class, () -> {
					m.getMove(-1);
				}));
		Assertions.assertAll("setMove should throw IllegalArgumentException",
				() -> Assertions.assertThrows(IllegalArgumentException.class, () -> {
					m.setMove(4, mv);
				}), () -> Assertions.assertThrows(IllegalArgumentException.class, () -> {
					m.setMove(-1, mv);
				}));
	}

	@Test
	public void getEffectivePowerComputesCorrectly() {
		// Create some monsters and moves -- made up names and types
		Monster m1 = new Monster("aaa", "Normal");
		Monster m2 = new Monster("bbb", "Electric");
		Monster m3 = new Monster("ccc", "Fire");
		Monster m4 = new Monster("ddd", "Grass", "Water");
		Monster m5 = new Monster("eee", "Water");

		// Moves
		Move fire = new Move("ff", "Fire", 100);
		Move normal = new Move("nn", "Normal", 100);
		Move water = new Move("ww", "Water", 100);
		Move grass = new Move("gg", "Grass", 100);
		Move electric = new Move("ee", "Electric", 100);

		// STAB
		Assertions.assertEquals(150, m1.getEffectivePower(normal, m2), "STAB not computed correctly");

		// Double not effective
		Assertions.assertEquals(25, m1.getEffectivePower(water, m4), "Double weakness not computed correctly");

		// Effective and not effective at the same time
		Assertions.assertEquals(100, m1.getEffectivePower(electric, m4), "Super + weak not combined correctly");

		// Super effective + STAB
		Assertions.assertEquals(300, m2.getEffectivePower(electric, m5), "Super + STAB not combined correctly");

		// Not very effective + STAB
		Assertions.assertEquals(75, m3.getEffectivePower(fire, m3), "Weakness + STAB not combined correctly");
	}

}
