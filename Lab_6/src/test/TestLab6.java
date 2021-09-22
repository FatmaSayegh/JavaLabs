package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gui.AddMonsterDialog;
import gui.TrainerFrame;
import monster.Monster;
import monster.Move;
import monster.Trainer;

public class TestLab6 {

	private static final Random RAND = new Random();
	private static final List<String> TYPES = Arrays.asList("Normal", "Fire", "Water", "Electric", "Grass", null, null,
			null, null, null);

	// https://www.baeldung.com/java-random-string
	private static String getRandomString() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;

		String result = RAND.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		return result.substring(0, 1).toUpperCase() + result.substring(1);
	}

	public static String getRandomType(boolean includeNull) {
		if (includeNull) {
			return TYPES.get(RAND.nextInt(TYPES.size()));
		} else {
			return TYPES.get(RAND.nextInt(TYPES.size() - 5));
		}
	}

	public static Monster getRandomMonster() {
		String name = getRandomString();
		String type1 = getRandomType(false);
		String type2 = getRandomType(true);
		int hp = RAND.nextInt(200);

		Monster m;
		if (type2 == null || type2.equals(type1)) {
			m = new Monster(name, type1, hp);
		} else {
			m = new Monster(name, type1, type2, hp);
		}

		for (int i = 0; i < 4; i++) {
			m.setMove(i, getRandomMove());
		}

		return m;
	}

	public static Move getRandomMove() {
		String type = getRandomType(true);
		if (type == null) {
			return null;
		}
		return new Move(getRandomString(), type, RAND.nextInt(181));
	}

	private TrainerFrame tf;

	@BeforeEach
	public void setUp() {
		tf = new TrainerFrame();
		tf.setVisible(true);
		try {
			Files.deleteIfExists(Paths.get("trainer.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterEach
	public void tearDown() {
		tf.dispose();
		tf = null;
		try {
			Files.deleteIfExists(Paths.get("trainer.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Test the read/write methods
	@Test
	public void monsterMethodsShouldWorkTogether() {
		for (int i = 0; i < 10; i++) {
			Monster m = getRandomMonster();
			List<String> lines = m.toStringForFile();
			Monster m2 = Monster.parseMonster(lines);
			Assertions.assertEquals(m2, m, "Monster methods do not work together");
		}
	}

	@Test
	public void trainerMethodsShouldWorkTogether() {
		for (int i = 0; i < 10; i++) {
			Trainer t = getRandomTrainer();
			List<String> lines = t.toStringForFile();
			Trainer t2 = Trainer.parseTrainer(lines);
			Assertions.assertAll("Trainer methods do not work together",
					() -> Assertions.assertEquals(t2.getName(), t.getName()),
					() -> Assertions.assertEquals(t2.getMonsters(), t.getMonsters())
					);
		}

	}

	private Trainer getRandomTrainer() {
		Trainer t = new Trainer(getRandomString());
		for (int j = 0; j < RAND.nextInt(5) + 1; j++) {
			t.addMonster(getRandomMonster());
		}
		return t;
	}

	private Object getFieldValue(Object obj, String fieldName) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			Assertions.fail("Field '" + fieldName + "' not found or not accessible in " + obj.getClass().getName());
			return null;
		}
	}

	@Test
	public void saveShouldCreateFile() {
		JButton saveButton = (JButton) getFieldValue(tf, "saveButton");
		saveButton.doClick();
		Assertions.assertTrue(Files.exists(Paths.get("trainer.txt")), "trainer.txt does not exist after clicking save");
	}

	@Test
	public void saveShouldCreateCorrectFile() throws IOException {
		Trainer t = getRandomTrainer();
		
		// Fake load the values into the fields
		JTextField nameField = (JTextField)getFieldValue(tf, "nameField");
		nameField.setText(t.getName());
		@SuppressWarnings("unchecked")
		DefaultListModel<Monster> monsterModel = (DefaultListModel<Monster>)getFieldValue(tf, "monsterModel");
		for (Monster m: t.getMonsters()) {
			monsterModel.addElement(m);
			
		}
		
		// Save it
		JButton saveButton = (JButton) getFieldValue(tf, "saveButton");
		saveButton.doClick();
		
		List<String> lines = Files.readAllLines(Paths.get("trainer.txt"));
		Trainer t2 = Trainer.parseTrainer(lines);

		Assertions.assertAll("Save button does not save valid Trainer file",
				() -> Assertions.assertEquals(t2.getName(), t.getName()),
				() -> Assertions.assertEquals(t2.getMonsters(), t.getMonsters())
				);
	}
	
	@Test
	public void loadShouldLoadCorrectly() throws IOException {
		// Create a file from a random trainer
		Trainer t = getRandomTrainer();
		PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get("trainer.txt")));
		for (String line : t.toStringForFile()) {
			pw.println(line);
		}
		pw.close();

		// Load the file into the window
		JButton loadButton = (JButton) getFieldValue(tf, "loadButton");
		loadButton.doClick();
		
		// Make sure the on-screen fields match the ones in the trainer
		@SuppressWarnings("unchecked")
		DefaultListModel<Monster> monsterModel = (DefaultListModel<Monster>)getFieldValue(tf, "monsterModel");
		Set<Monster> allMonsters = new HashSet<>();
		for (int i = 0; i < monsterModel.getSize(); i++) {
			allMonsters.add(monsterModel.get(i));
		}
		JTextField nameField = (JTextField)getFieldValue(tf, "nameField");
		Assertions.assertAll("Load button does not load Trainer file correctly",
				() -> Assertions.assertEquals(nameField.getText(), t.getName()),
				() -> Assertions.assertEquals(allMonsters, t.getMonsters())
				);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void addMonsterShouldWorkProperly() {
		DefaultListModel<Monster> monsterModel = (DefaultListModel<Monster>)getFieldValue(tf, "monsterModel");
		for (int j = 0; j < 6; j++) {
			AddMonsterDialog amd = new AddMonsterDialog(tf, monsterModel);
			
			// Get the fields of the AddMonsterDialog	
			// Top ones
			JTextField nameField = (JTextField) getFieldValue(amd, "nameField");
			JComboBox<String> type1Combo = (JComboBox<String>) getFieldValue(amd, "type1Combo");
			JComboBox<String> type2Combo = (JComboBox<String>) getFieldValue(amd, "type2Combo");
			JSpinner hitPointSpinner = (JSpinner) getFieldValue(amd, "hitPointSpinner");

			// GUI elements for moves (x 4)
			JSpinner[] movePowerSpinners = (JSpinner[]) getFieldValue(amd, "movePowerSpinners");
			JTextField[] moveNameFields = (JTextField[]) getFieldValue(amd, "moveNameFields");
			JComboBox<String>[] moveTypeCombos = (JComboBox<String>[]) getFieldValue(amd, "moveTypeCombos");
			
			// Create a monster and set the fields 
			Monster m = getRandomMonster();
			
			nameField.setText(m.getName());
			String[] types = m.getTypes();
			type1Combo.setSelectedItem(types[0]);
			if (types.length == 2) {
				type2Combo.setSelectedItem(types[1]);
			} else {
				type2Combo.setSelectedItem("");
			}
			
			// Hit points isn't directly exposed so need to go in sideways
			double hitPoints = (double)getFieldValue(m, "hp");
			hitPointSpinner.setValue((int)hitPoints);
			
			for (int i = 0; i < 4; i++) {
				if (m.getMove(i) != null) {
					moveNameFields[i].setText(m.getMove(i).getName());
					movePowerSpinners[i].setValue(m.getMove(i).getPower());
					moveTypeCombos[i].setSelectedItem(m.getMove(i).getTypes()[0]);
				} else {
					moveNameFields[i].setText("");
					movePowerSpinners[i].setValue(100);
					moveTypeCombos[i].setSelectedItem("");
				}
			}
			
			// Click the OK button
			JButton okButton = (JButton)getFieldValue(amd, "okButton");
			okButton.doClick();
			
			// The monster should now be included in the list

		}
	}

}
