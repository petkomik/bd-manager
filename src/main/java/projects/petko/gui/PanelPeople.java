package projects.petko.gui;

import static projects.petko.general.Parameters.*;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import projects.petko.data.Person;
import projects.petko.db.DatabaseHandler;


public class PanelPeople extends JPanel {

	private static final long serialVersionUID = 1L;
	
	DatabaseHandler db;
	GUI mainApp;

	JTable tablePeople;	
	
	JLabel labelCountPpl;
	int countAllPpl, countFilteredPpl;
	
	JTextField searchField;
	JButton searchButton;
	JButton clearSearchButton;
	
	JLabel labelCategory;
	JComboBox<String> filterCategory;
	JLabel labelSubGroup;
	JComboBox<String> filterSubGroup;
	
	JButton filterButton;
	JButton clearFilterButton;
	
	JLabel addEditLabel;
	JButton deleteButton;
	JButton editButton;
	JButton addButton;
	JButton applyButton;
	JTextField inputTextField;
	JButton csvButton;
	
	public PanelPeople(DatabaseHandler db, GUI mainApp) {
		this.db = db;
		this.mainApp = mainApp;
		createPanelComponents();
	}

	protected void createPanelComponents() {

		// Panel tablePanel (tableHeader & tableContent)
		JPanel tablePanel = new JPanel();
		tablePanel.setOpaque(false);
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		
		JPanel tableHeaderPanel = new JPanel();
		tableHeaderPanel.setOpaque(false);
		tableHeaderPanel.setLayout(new BoxLayout(tableHeaderPanel, BoxLayout.X_AXIS));
		
		JLabel tableNameLabel = new JLabel("People");
		tableNameLabel.setFont(new Font(null, Font.BOLD, 14));
		//tableNameLabel.setForeground(new Color(240, 240, 240));

		
		labelCountPpl = new JLabel("Showing XXX out of XXX");
		labelCountPpl.setFont(new Font(null, Font.PLAIN, 12));
		//labelCountPpl.setForeground(new Color(240, 240, 240));

		
		tableHeaderPanel.add(Box.createRigidArea(new Dimension(5, 25)));
		tableHeaderPanel.add(tableNameLabel);
		tableHeaderPanel.add(Box.createHorizontalGlue());
		tableHeaderPanel.add(labelCountPpl);
		tableHeaderPanel.add(Box.createRigidArea(new Dimension(25, 25)));
		
		String[] columnsPeople = { "ID", "FName", "LName", "Nickname", "Category", "Subgroup", "Age", "Birthday"};
		TableModel model = new DefaultTableModel(columnsPeople, 0) {
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       // set all cells as not editable
		       return false;
		    }
		};
		tablePeople = new JTable(model);
		// ID
		tablePeople.getColumnModel().getColumn(0).setMinWidth(30);
		tablePeople.getColumnModel().getColumn(0).setMaxWidth(50);
		tablePeople.getColumnModel().getColumn(0).setPreferredWidth(40);
		// FName
		tablePeople.getColumnModel().getColumn(5).setMinWidth(110);
		tablePeople.getColumnModel().getColumn(5).setMaxWidth(110);
		tablePeople.getColumnModel().getColumn(5).setPreferredWidth(110);
		
		tablePeople.getColumnModel().getColumn(6).setMinWidth(40);
		tablePeople.getColumnModel().getColumn(6).setMaxWidth(50);
		tablePeople.getColumnModel().getColumn(6).setPreferredWidth(40);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		tablePeople.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
		tablePeople.getColumnModel().getColumn(7).setCellRenderer( centerRenderer );
		
		tablePeople.setFillsViewportHeight(true);
		JScrollPane tableContent = new JScrollPane(tablePeople);
		//tableContent.setPreferredSize(new Dimension(900, 500));
		//tableContent.setMaximumSize(new Dimension(900, 500));
		
		tablePanel.add(tableHeaderPanel);
		tablePanel.add(Box.createVerticalStrut(5));
		tablePanel.add(tableContent);
		
		
		// Panel filterAreaPanel (filterHeaderPanel & filterPanel)
		JPanel filterAreaPanel = new JPanel();
		filterAreaPanel.setOpaque(false);
		filterAreaPanel.setLayout(new BoxLayout(filterAreaPanel, BoxLayout.Y_AXIS));
		filterAreaPanel.setMinimumSize(new Dimension(220, 550));
		filterAreaPanel.setPreferredSize(new Dimension(220, 550));
		
		JPanel filterHeaderPanel = new JPanel();
		filterHeaderPanel.setOpaque(false);
		filterHeaderPanel.setLayout(new BoxLayout(filterHeaderPanel, BoxLayout.X_AXIS));
		filterHeaderPanel.setMinimumSize(new Dimension(220, 30));
		filterHeaderPanel.setMaximumSize(new Dimension(220, 30));
		filterHeaderPanel.setPreferredSize(new Dimension(220, 30));
		filterHeaderPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel editEntryPanel = new JPanel();
		editEntryPanel.setOpaque(false);
		editEntryPanel.setLayout(new BoxLayout(editEntryPanel, BoxLayout.X_AXIS));
		editEntryPanel.setMinimumSize(new Dimension(220, 30));
		editEntryPanel.setMaximumSize(new Dimension(220, 30));
		editEntryPanel.setPreferredSize(new Dimension(220, 30));
		editEntryPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(150, 25));
		searchField.setMaximumSize(new Dimension(150, 25));
		//searchField.setBorder(new LineBorder(new Color(153, 180, 209), 1));
		
		// KeyListener searchField
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					fillTablePanel_searchedPeople();
				}
			}
		});
		
		ImageIcon icon1 = new ImageIcon(ressourcesDir + iconSearch);
		icon1.setImage(icon1.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));
		searchButton = new JButton(icon1);
		searchButton.setMaximumSize(new Dimension(25, 25));
		searchButton.setPreferredSize(new Dimension(25, 25));
		searchButton.setFocusPainted(false);
		
		// ActionListener searchButton
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterButton.setEnabled(false);
				clearSearchButton.setEnabled(true);
				fillTablePanel_searchedPeople();
			}
		});
		
		ImageIcon icon2 = new ImageIcon(ressourcesDir + iconClearSearch);
		icon2.setImage(icon2.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		clearSearchButton = new JButton(icon2);
		clearSearchButton.setMaximumSize(new Dimension(25, 25));
		clearSearchButton.setPreferredSize(new Dimension(25, 25));
		clearSearchButton.setBorder(BorderFactory.createEmptyBorder());
		clearSearchButton.setContentAreaFilled(false);
		clearSearchButton.setFocusPainted(false);
		clearSearchButton.setEnabled(false);
		
		// ActionListener clearSearchButton
		clearSearchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterButton.setEnabled(true);
				searchField.setText("");
				clearSearchButton.setEnabled(false);
				fillTablePanel_allPeople();
			}
		});
		
		filterHeaderPanel.add(searchField);
		filterHeaderPanel.add(Box.createRigidArea(new Dimension(5, 30)));
		filterHeaderPanel.add(searchButton);
		filterHeaderPanel.add(Box.createRigidArea(new Dimension(5, 30)));
		filterHeaderPanel.add(clearSearchButton);
		
		
		JPanel filterPanel = new JPanel();
		filterPanel.setOpaque(false);
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
		//filterPanel.setMinimumSize(new Dimension(220, 480));
		filterPanel.setMaximumSize(new Dimension(220, 480));
		//filterPanel.setPreferredSize(new Dimension(220, 480));
		filterPanel.setAlignmentY(TOP_ALIGNMENT);
		
		labelCategory = new JLabel("Category");
		labelCategory.setAlignmentX(Component.LEFT_ALIGNMENT);
		//labelCategory.setForeground(new Color(240, 240, 240));

		labelSubGroup = new JLabel("Subgroup");
		labelSubGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
		//labelSubGroup.setForeground(new Color(240, 240, 240));

		Dimension filterSize = new Dimension(220, 20);


		ArrayList<String> categories = new ArrayList<String>();	
		String[] filterValuesCat = db.getUniqueFilterValues("People", "Category");
		for (int i = 0; i < filterValuesCat.length; i++) {
			categories.add(filterValuesCat[i]);
		}
		filterCategory = new JComboBox<>(categories.toArray(new String[categories.size()]));
		filterCategory.setSelectedIndex(-1);
		filterCategory.setMaximumSize(filterSize);
		filterCategory.setAlignmentX(Component.LEFT_ALIGNMENT);


		ArrayList<String> subgroupies = new ArrayList<String>();	
		String[] filterValuesGr = db.getUniqueFilterValues("People", "Subgroup");
		for (int i = 0; i < filterValuesGr.length; i++) {
			subgroupies.add(filterValuesGr[i]);
		}
		filterSubGroup = new JComboBox<>(subgroupies.toArray(new String[subgroupies.size()]));	
		filterSubGroup.setSelectedIndex(-1);		
		filterSubGroup.setMaximumSize(filterSize);
		filterSubGroup.setAlignmentX(Component.LEFT_ALIGNMENT);

		filterButton = new JButton("Apply Filter");
		filterButton.setFocusPainted(false);
		
		// ActionListener filterButton
		filterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchButton.setEnabled(false);
				clearFilterButton.setEnabled(true);
				fillTablePanel_filteredPeople();
			}
		});
		
		ImageIcon icon3 = new ImageIcon(ressourcesDir + iconClearFilter);
		icon3.setImage(icon3.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		clearFilterButton = new JButton(icon3);
		clearFilterButton.setMaximumSize(new Dimension(25, 25));
		clearFilterButton.setPreferredSize(new Dimension(25, 25));
		clearFilterButton.setBorder(BorderFactory.createEmptyBorder());
		clearFilterButton.setFocusPainted(false);
		clearFilterButton.setEnabled(false);
		clearFilterButton.setContentAreaFilled(false);
		
		// ActionListener clearSearchButton
		clearFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterCategory.setSelectedIndex(-1);
				filterSubGroup.setSelectedIndex(-1);
				searchButton.setEnabled(true);
				clearFilterButton.setEnabled(false);
				fillTablePanel_allPeople();
			}
		});
		
		addEditLabel = new JLabel("Add/Edit Contents");
		addEditLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		deleteButton = new JButton("Delete Entry");
		deleteButton.setFocusPainted(false);

		
		editButton = new JButton("Edit Entry");
		editButton.setFocusPainted(false);
		
		// ActionListener filterButton
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				// fill jlabel with content
				// activate apply button
				applyButton.setEnabled(true);
				inputTextField.setEditable(true);


			}
		});
			
		addButton = new JButton("Add Entry");
		addButton.setFocusPainted(false);
		
		// ActionListener filterButton
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				applyButton.setEnabled(true);
				inputTextField.setEditable(true);
				
			}
		});
		
		applyButton = new JButton("Apply Input");
		applyButton.setFocusPainted(false);
		applyButton.setEnabled(false);
		
		// ActionListener filterButton
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyButton.setEnabled(false);
				inputTextField.setEditable(false);
				inputTextField.setText("");

			}
		});
		
		csvButton = new JButton("Export to CSV");
		csvButton.setFocusPainted(false);
		
		inputTextField = new JTextField(18);
		inputTextField.setEditable(false);
		
		
		filterPanel.add(labelCategory);
		filterPanel.add(filterCategory);
		filterPanel.add(Box.createVerticalStrut(10));
		filterPanel.add(labelSubGroup);
		filterPanel.add(filterSubGroup);
		filterPanel.add(Box.createVerticalStrut(10));
		filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		
		JPanel filterButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		filterButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		filterButtonPanel.setOpaque(false);
		filterButtonPanel.add(filterButton);
		filterButtonPanel.add(clearFilterButton);

		JPanel addEditButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		addEditButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		addEditButtonPanel.setOpaque(false);
		addEditButtonPanel.add(editButton);
		addEditButtonPanel.add(Box.createHorizontalStrut(10));
		addEditButtonPanel.add(addButton);
	
		JPanel inputDataPanel = new JPanel();
		inputDataPanel.setLayout(new BoxLayout(inputDataPanel, BoxLayout.Y_AXIS));
		inputDataPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputDataPanel.setOpaque(false);
		inputDataPanel.add(inputTextField);
		inputDataPanel.add(Box.createVerticalStrut(10));
		Box  b1 = Box.createHorizontalBox();
		b1.add(applyButton);
		b1.add(Box.createHorizontalGlue());
		inputDataPanel.add(b1);
		
		
		

		filterPanel.add(filterButtonPanel);
		filterPanel.add(Box.createVerticalStrut(10));
		filterPanel.add(addEditLabel);
		filterPanel.add(Box.createVerticalStrut(5));
		filterPanel.add(deleteButton);
		filterPanel.add(Box.createVerticalStrut(10));
		filterPanel.add(addEditButtonPanel);
		filterPanel.add(Box.createVerticalStrut(10));
		filterPanel.add(inputDataPanel);
		filterPanel.add(Box.createVerticalStrut(10));
		filterPanel.add(csvButton);

		filterAreaPanel.add(filterHeaderPanel);
		filterAreaPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		filterAreaPanel.add(filterPanel);
		filterAreaPanel.add(Box.createVerticalGlue());
		
		
		// add all panels to main panel
		//this.setBackground(Color.WHITE);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createHorizontalStrut(30));
		this.add(tablePanel);
		this.add(Box.createHorizontalStrut(30));
		this.add(filterAreaPanel);
		this.add(Box.createHorizontalStrut(30));
	}



	
	public void fillTablePanel_allPeople() {
		DefaultTableModel model = (DefaultTableModel) tablePeople.getModel();
		model.setRowCount(0);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		List<Person> ppl = db.getAllPeople();
		Object[] row = new Object[8];
		for (int i = 0; i < ppl.size(); i++) {
			row[0] = ppl.get(i).getId();
			row[1] = ppl.get(i).getFirstName();
			row[2] = ppl.get(i).getLastName();
			row[3] = ppl.get(i).getNickname();
			row[4] = ppl.get(i).getCategory();
			row[5] = ppl.get(i).getGroup();
			row[6] = ppl.get(i).getYearsOld();
			if(row[6] instanceof Integer) {
				if(Integer.valueOf(row[6].toString()) < 5) {
					row[6] = "-";
				}
			}
			row[7] = ppl.get(i).getBirthdayDate().format(formatters);
			model.addRow(row);
		}
		countAllPpl = ppl.size();
		labelCountPpl.setText("Showing " + countAllPpl + " out of " + countAllPpl);
	}
	
	
	private String[] getComboBoxFilterValues() {
		String[] filterValues = new String[2];
		filterValues[0] = (filterCategory.getSelectedIndex() == -1) ? "" : filterCategory.getSelectedItem().toString();
		filterValues[1] = (filterSubGroup.getSelectedIndex() == -1) ? "" : filterSubGroup.getSelectedItem().toString();
		return filterValues;
	}
	
	
	public void fillTablePanel_filteredPeople() {
		DefaultTableModel model = (DefaultTableModel) tablePeople.getModel();
		model.setRowCount(0);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		List<Person> ppl = db.getFilteredPeople(getComboBoxFilterValues());
		Object[] row = new Object[9];
		for (int i = 0; i < ppl.size(); i++) {
			row[0] = ppl.get(i).getId();
			row[1] = ppl.get(i).getFirstName();
			row[2] = ppl.get(i).getLastName();
			row[3] = ppl.get(i).getNickname();
			row[4] = ppl.get(i).getCategory();
			row[5] = ppl.get(i).getGroup();
			row[6] = ppl.get(i).getYearsOld();
			if(row[6] instanceof Integer) {
				if(Integer.valueOf(row[6].toString()) < 5) {
					row[6] = "-";
				}
			}
			row[7] = ppl.get(i).getBirthdayDate().format(formatters);
			row[8] = ppl.get(i).getDaysTill();
			model.addRow(row);
		}
		countFilteredPpl = ppl.size();
		labelCountPpl.setText("Showing " + countFilteredPpl + " out of " + countAllPpl);
	}
	
	
	public void fillTablePanel_searchedPeople() {
		DefaultTableModel model = (DefaultTableModel) tablePeople.getModel();
		model.setRowCount(0);
		
		List<Person> ppl;
		if (searchField.getText() == null || searchField.getText().trim().equals("")) {
			ppl = db.getAllPeople();
		} else {
			ppl = db.getSearchedPeople(searchField.getText().trim());
		}
		
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		Object[] row = new Object[9];
		for (int i = 0; i < ppl.size(); i++) {
			row[0] = ppl.get(i).getId();
			row[1] = ppl.get(i).getFirstName();
			row[2] = ppl.get(i).getLastName();
			row[3] = ppl.get(i).getNickname();
			row[4] = ppl.get(i).getCategory();
			row[5] = ppl.get(i).getGroup();
			row[6] = ppl.get(i).getYearsOld();
			if(row[6] instanceof Integer) {
				if(Integer.valueOf(row[6].toString()) < 5) {
					row[6] = "-";
				}
			}
			row[7] = ppl.get(i).getBirthdayDate().format(formatters);
			row[8] = ppl.get(i).getDaysTill();
			model.addRow(row);
		}
		countFilteredPpl = ppl.size();
		labelCountPpl.setText("Showing " + countFilteredPpl + " out of " + countAllPpl);
	}
	


}
