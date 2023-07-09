package projects.petko.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import projects.petko.data.Person;
import projects.petko.db.DatabaseHandler;


public class PanelNextUp extends JPanel {

	private static final long serialVersionUID = 1L;
	
	DatabaseHandler db;
	GUI mainApp;

	JTable tableUpNext;
	JButton bewerbungsButton;
	JButton changeStatusButton;
	JButton deleteButton;
	JButton saveButton;

	
	public PanelNextUp(DatabaseHandler db, GUI mainApp) {
		this.db = db;
		this.mainApp = mainApp;
		createPanelComponents();
	}


	protected void createPanelComponents() {
		
		// Tabelle Up Next
		JPanel tablePanel = new JPanel();
		tablePanel.setOpaque(false);
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		
		JPanel tableHeaderPanel = new JPanel();
		tableHeaderPanel.setOpaque(false);
		tableHeaderPanel.setLayout(new BoxLayout(tableHeaderPanel, BoxLayout.X_AXIS));
		JLabel tableNameLabel = new JLabel("Upcoming");
		tableNameLabel.setFont(new Font(null, Font.BOLD, 14));
		tableHeaderPanel.add(Box.createRigidArea(new Dimension(5, 25)));
		tableHeaderPanel.add(tableNameLabel);
		tableHeaderPanel.add(Box.createHorizontalGlue());
		//tableNameLabel.setForeground(new Color(240, 240, 240));
		
		String[] columnsNextUp = { "ID", "FName", "LName", "Nickname", "Category", "Subgroup", "Age", "Birthday", "Days" };
		@SuppressWarnings("serial")
		TableModel model = new DefaultTableModel(columnsNextUp, 0) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       // set all table cells as not editable
		       return false;
		    }
		};
		tableUpNext = new JTable(model);
		// Nr
		tableUpNext.getColumnModel().getColumn(0).setMinWidth(30);
		tableUpNext.getColumnModel().getColumn(0).setMaxWidth(50);
		tableUpNext.getColumnModel().getColumn(0).setPreferredWidth(40);
		// Datum
		tableUpNext.getColumnModel().getColumn(1).setMinWidth(100);
		tableUpNext.getColumnModel().getColumn(1).setMaxWidth(100);
		tableUpNext.getColumnModel().getColumn(1).setPreferredWidth(100);
		// Status
		tableUpNext.getColumnModel().getColumn(2).setMinWidth(100);
		tableUpNext.getColumnModel().getColumn(2).setMaxWidth(100);
		tableUpNext.getColumnModel().getColumn(2).setPreferredWidth(100);
		// Trägerschaft
		tableUpNext.getColumnModel().getColumn(6).setMinWidth(40);
		tableUpNext.getColumnModel().getColumn(6).setMaxWidth(50);
		tableUpNext.getColumnModel().getColumn(6).setPreferredWidth(40);
		
		tableUpNext.getColumnModel().getColumn(8).setMinWidth(60);
		tableUpNext.getColumnModel().getColumn(8).setMaxWidth(70);
		tableUpNext.getColumnModel().getColumn(8).setPreferredWidth(60);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tableUpNext.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
		tableUpNext.getColumnModel().getColumn(7).setCellRenderer( centerRenderer );
		tableUpNext.getColumnModel().getColumn(8).setCellRenderer( centerRenderer );

		tableUpNext.setFocusable(false);
		tableUpNext.setRowSelectionAllowed(false);
		
		tableUpNext.setFillsViewportHeight(true);
		JScrollPane tableContent = new JScrollPane(tableUpNext);		
		
		tablePanel.add(tableHeaderPanel);
		tablePanel.add(Box.createVerticalStrut(5));
		tablePanel.add(tableContent);
		
		
		//this.setBackground(Color.WHITE);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createHorizontalStrut(30));
		this.add(tablePanel);
		this.add(Box.createHorizontalStrut(30));
		this.add(Box.createHorizontalStrut(30));
	}

	public void fillTablePanel_NextUpJoined() {
		DefaultTableModel model = (DefaultTableModel) tableUpNext.getModel();
		model.setRowCount(0);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		List<Person> nextUp = db.getNextSeven();
		Object[] row = new Object[9];
		if (nextUp != null) {
			for (int i = 0; i < nextUp.size(); i++) {
				row[0] = nextUp.get(i).getId();
				row[1] = nextUp.get(i).getFirstName();
				row[2] = nextUp.get(i).getLastName();
				row[3] = nextUp.get(i).getNickname();
				row[4] = nextUp.get(i).getCategory();
				row[5] = nextUp.get(i).getGroup();
				row[6] = nextUp.get(i).getYearsOld();
				if(row[6] instanceof Integer) {
					if(Integer.valueOf(row[6].toString()) < 5) {
						row[6] = "-";
					}
				}
				row[7] = nextUp.get(i).getBirthdayDate().format(formatters);
				row[8] = nextUp.get(i).getDaysTill();
				model.addRow(row);
			}
		}
	}

}
