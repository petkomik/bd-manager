package projects.petko.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme;

import projects.petko.data.Person;
import projects.petko.db.DatabaseHandler;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	DatabaseHandler db = new DatabaseHandler();
	
	Container c;
	PanelPeople peopleIn;
	PanelNextUp nextUp;
	
	static Person selectedPerson;
	static Person selectedNextUP;
	
	//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	//int width = (int) (screenSize.getWidth()*0.85);
	//int height = (int) (screenSize.getHeight()*0.85);
	int width = 1200;
	int height = 850;
	int selectedTextField;

	
	public GUI() {
		super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void	windowClosing(WindowEvent e) {
				if (db != null) {
					db.disconnect();
				}
				System.exit(0);
			}

		});
		super.setSize(new Dimension(width, height));
		super.setPreferredSize(new Dimension(width, height));
		super.setTitle("Birthday Manager");
		super.setLocationRelativeTo(null);
		createComponents();
		pack();
		initTables();
	}

	
	private void initTables() {
		peopleIn.fillTablePanel_allPeople();
		nextUp.fillTablePanel_NextUpJoined();
	}
	

	private void createComponents() {
		
		// Panel People
		peopleIn = new PanelPeople(db, this);

		// ActionListener People Tabelle
		ListSelectionModel selectedModelStd = peopleIn.tablePeople.getSelectionModel();
		selectedModelStd.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!selectedModelStd.isSelectionEmpty()) {
					if (!e.getValueIsAdjusting()) {
						int id = (int) peopleIn.tablePeople.getValueAt(selectedModelStd.getMinSelectionIndex(), 0);
						selectedPerson = db.getPersonWithID(id);
						nextUp.tableUpNext.clearSelection();
					}
				}
			}
		});
		
		
		// Panel Next Up
		nextUp = new PanelNextUp(db, this);
		
		// ActionListener Next Up Tabelle
		ListSelectionModel selectedModelBwb = nextUp.tableUpNext.getSelectionModel();
		selectedModelBwb.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!selectedModelBwb.isSelectionEmpty()) {
					if (!e.getValueIsAdjusting()) {
						int id = (int) nextUp.tableUpNext.getValueAt(selectedModelBwb.getMinSelectionIndex(), 0);
						selectedNextUP = db.getPersonWithID(id);
						
					}
				}
			}
		});
		
		peopleIn.editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!(selectedPerson == null)) {
					String text = selectedPerson.toString();
					peopleIn.inputTextField.setText(text);
					selectedTextField = selectedPerson.getId();

				}
			}
		});
		
		peopleIn.addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = db.getAllPeople().get(db.getAllPeople().size() - 1).getId();
				db.addPerson();
				peopleIn.fillTablePanel_allPeople();
				peopleIn.tablePeople.setRowSelectionInterval(i, i);
				String text = selectedPerson.toString();
				peopleIn.inputTextField.setText(text);
				selectedTextField = selectedPerson.getId();

			}
		});
		
		peopleIn.deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!(selectedPerson == null)) {
					db.delPerson(selectedPerson.getId());
					peopleIn.fillTablePanel_allPeople();


				}

			}
		});
		
		peopleIn.applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = peopleIn.inputTextField.getText();
				int id = selectedTextField;
				db.editPerson(id, s);
				peopleIn.inputTextField.setText("");
				peopleIn.fillTablePanel_allPeople();

			}
		});
		
		peopleIn.csvButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				db.updateCSV();
			}
		});
		
	
		// ContentPane
		c = getContentPane();
		//c.setBackground(new Color(30, 30, 30));
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		c.add(Box.createVerticalStrut(30));
		c.add(peopleIn);
		c.add(Box.createVerticalStrut(30));
		c.add(nextUp);
		c.add(Box.createVerticalStrut(30));
	}


	public static void main(String args[]) {
		FlatMaterialDarkerIJTheme.setup();
		GUI mainApp = new GUI();
		mainApp.setVisible(true);
	}
	
}
