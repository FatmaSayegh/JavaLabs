package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import monster.Monster;
import monster.Trainer;

/**
 * The toplevel window to present a Trainer, allowing it to be loaded and saved
 * and for Monsters to be added and deleted.
 * 
 * @author Mary Ellen Foster <MaryEllen.Foster@glasgow.ac.uk>
 * @see monster#Trainer
 */
// Suppress an annoying and (in this case) irrelevant Eclipse compiler warning
@SuppressWarnings("serial")
public class TrainerFrame extends JFrame implements ActionListener {
	// The buttons
	private JButton addButton, deleteButton, loadButton, saveButton;
	
	// Field to display and edit the monster name
	private JTextField nameField;
	
	// The monster list and its underlying model
	private DefaultListModel<Monster> monsterModel;
	private JList<Monster> monsterList;
	
	/**
	 * Creates a new TrainerFrame.
	 */
	public TrainerFrame() {
		// Basic window features
		super("Trainer manager");
		setLocationByPlatform(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Trainer name
		Box nameBox = Box.createHorizontalBox();
		nameField = new JTextField(20);
		nameBox.add(new JLabel("Trainer name:"));
		nameBox.add(nameField);
		
		// Create the JList and its corresponding list model
		monsterModel = new DefaultListModel<>();
		monsterList = new JList<>(monsterModel);
		
		// Add titled scrolling pane to hold the list, and add it to the layout
		JScrollPane trainerScroll = new JScrollPane(monsterList);
		trainerScroll.setBorder(new TitledBorder("Monsters"));
		JPanel listPanel = new JPanel();
		listPanel.add(trainerScroll);
		
		// Create the buttons and add the current object as a listener to all of them
		addButton = new JButton("Add Monster");
		addButton.addActionListener(this);
		deleteButton = new JButton("Delete Monster");
		deleteButton.addActionListener(this);
		loadButton = new JButton("Load Trainer");
		loadButton.addActionListener(this);
		saveButton = new JButton("Save Trainer");
		saveButton.addActionListener(this);
		
		// Lay out the buttons in a row
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(addButton);
		buttonBox.add(deleteButton);
		buttonBox.add(loadButton);
		buttonBox.add(saveButton);
		
		// Lay out the whole window
		getContentPane().setLayout(new BorderLayout()); 
		getContentPane().add(BorderLayout.NORTH, nameBox);
		getContentPane().add(listPanel);
		getContentPane().add(BorderLayout.SOUTH, buttonBox);
		pack();
	}
	
	/**
	 * Responds to a click on one of the buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == addButton) {
			// Pop up a dialog box to add a monster to the trainer
			new AddMonsterDialog(this, monsterModel).setVisible(true);

		} else if (source == deleteButton) {
			if (this.monsterList.isSelectionEmpty())
				return;
			
			this.monsterModel.remove(this.monsterList.getSelectedIndex());
		} else if (source == loadButton) {
			Path p = Paths.get("trainer.txt");
			try {
				Trainer t = Trainer.parseTrainer(Files.readAllLines(p));
				this.monsterModel.clear();
				for (Monster m: t.getMonsters()) {
					this.monsterModel.addElement(m);
				}
				this.nameField.setText(t.getName());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
		} else if (source == saveButton) {
			// Create a Trainer object based on the fields and save it to "trainer.txt"
			try {
				Path p = Paths.get("trainer.txt");
				
				if (!Files.exists(p)) 
					Files.createFile(p);
				
				PrintWriter pw = new PrintWriter (Files.newBufferedWriter(p));
				Trainer trainer = new Trainer(nameField.getText());
				for (int i = 0; i < monsterModel.size(); i++) {
					trainer.addMonster(monsterModel.get(i));
				}
				for (String line : trainer.toStringForFile()) {
					pw.println (line);
				}
				pw.close();
			} catch (IOException ex) {
				// This is how to show an error message to the user in a GUI
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Couldn't save to file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Main method: just displays the toplevel window.
	 * 
	 * @param args Command-line arguments (not used)
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TrainerFrame().setVisible(true);
			}
		});
	}

}