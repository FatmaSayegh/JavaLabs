package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import monster.Monster;
import monster.Move;

/**
 * A dialog box that can be used to specify the properties of a Monster and add
 * it to the list of a Trainer.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 * @see monster#Monster
 */
// Silence an annoying Eclipse warning
@SuppressWarnings("serial")
public class AddMonsterDialog extends JDialog implements ActionListener {
	// The current list of monsters as a ListModel
	private DefaultListModel<Monster> model;

	// GUI elements for the Monster
	private JTextField nameField;
	private JComboBox<String> type1Combo, type2Combo;
	private JSpinner hitPointSpinner;

	// Avoid "magic numbers" in the moves section
	private static final int NUM_MOVES = 4;

	// GUI elements for moves (x 4)
	private JSpinner[] movePowerSpinners = new JSpinner[NUM_MOVES];
	private JTextField[] moveNameFields = new JTextField[NUM_MOVES];
	// You can't do an array of generic types in a type-safe way, so just shut up
	// the warning :(
	// See
	// https://www.oreilly.com/library/view/learning-java-4th/9781449372477/ch08s10.html
	@SuppressWarnings("unchecked")
	private JComboBox<String>[] moveTypeCombos = new JComboBox[NUM_MOVES];

	// Buttons at the bottom
	private JButton okButton;
	private JButton cancelButton;

	// Used for all type combo boxes
	private static final String[] VALID_TYPES = { "Normal", "Fire", "Water", "Electric", "Grass" };

	/**
	 * Creates a new AddMonsterDialog which can be used to add a monster to the
	 * given trainer, and to update the model on screen as well.
	 * 
	 * @param frame   The parent Frame of this dialog
	 * @param trainer The trainer to add the monster to
	 * @param model   The ListModel to add the monster to
	 */
	public AddMonsterDialog(JFrame frame, DefaultListModel<Monster> model) {
		super(frame, "Add Monster", true);
		setLocationRelativeTo(frame);

		// Store the model so it can be used in the event handler
		this.model = model;

		// Fields for entering monster properties
		nameField = new JTextField(20);
		type1Combo = new JComboBox<>(VALID_TYPES);
		type2Combo = new JComboBox<>(VALID_TYPES);
		type2Combo.addItem("");
		type2Combo.setSelectedItem("");
		hitPointSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 300, 10));

		// Fields for entering moves
		for (int i = 0; i < NUM_MOVES; i++) {
			movePowerSpinners[i] = new JSpinner(new SpinnerNumberModel(100, 0, 180, 10));
			moveTypeCombos[i] = new JComboBox<>(VALID_TYPES);
			moveNameFields[i] = new JTextField(10);
		}

		// Layout the fields
		Box fieldBox = Box.createVerticalBox();

		// Layout monster fields
		Box monsterBox = Box.createVerticalBox();
		monsterBox.setBorder(new TitledBorder("Monster"));
		Box nameBox = Box.createHorizontalBox();
		nameBox.add(new JLabel("Name"));
		nameBox.add(nameField);
		nameBox.add(new JLabel("HP"));
		nameBox.add(hitPointSpinner);
		monsterBox.add(nameBox);

		Box typeBox = Box.createHorizontalBox();
		typeBox.add(new JLabel("Types"));
		typeBox.add(type1Combo);
		typeBox.add(type2Combo);
		monsterBox.add(typeBox);

		fieldBox.add(monsterBox);

		// Layout move fields x NUM_MOVES
		for (int i = 0; i < NUM_MOVES; i++) {
			Box moveBox = Box.createHorizontalBox();
			moveBox.setBorder(new TitledBorder("Move #" + i));

			moveBox.add(new JLabel("Name"));
			moveBox.add(moveNameFields[i]);

			moveBox.add(new JLabel("Type"));
			moveBox.add(moveTypeCombos[i]);

			moveBox.add(new JLabel("Power"));
			moveBox.add(movePowerSpinners[i]);

			fieldBox.add(moveBox);
		}

		// Create buttons and add the current class as an ActionListener
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		// Bottom row of GUI: buttons
		Box bottomBox = Box.createHorizontalBox();
		bottomBox.add(Box.createHorizontalGlue());
		bottomBox.add(okButton);
		bottomBox.add(Box.createHorizontalGlue());
		bottomBox.add(cancelButton);
		bottomBox.add(Box.createHorizontalGlue());

		// Put it all together
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(fieldBox);
		getContentPane().add(Box.createVerticalStrut(10));
		getContentPane().add(bottomBox);
		pack();
	}

	/**
	 * Responds to a click on one of the buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			// Just close the window without doing anything
			dispose();

		} else if (e.getSource() == okButton) {
			String name = nameField.getText();
			int hp = (int) hitPointSpinner.getValue();

			String type1 = this.type1Combo.getSelectedItem().toString();
			String type2 = this.type2Combo.getSelectedItem().toString();
			Monster m = null;
			if (type2.trim().length() == 0) {
				m = new Monster(name, type1, hp);
			} else {
				m = new Monster(name, type1, type2, hp);
			}
			for (int i = 0; i < 4; i++) {
				m.setMove(i, new Move(moveNameFields[i].getText(), moveTypeCombos[i].getSelectedItem().toString(),
						(int) movePowerSpinners[i].getValue()));
			}

			this.model.addElement(m);

			dispose();
		}
	}

}
