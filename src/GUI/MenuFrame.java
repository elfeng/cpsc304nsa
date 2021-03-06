package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;

import queries.Aggregation;
import queries.Deletion;
import queries.Division;
import queries.Join;
import queries.NestedAggregationWithGroupBy;
import queries.Projection;
import queries.Selection;
import queries.Update;

public class MenuFrame {

	private Connection con;
	private JFrame frame;
	
	public MenuFrame(Connection con, int userVal) {
		this.con = con;
		initComponents(userVal);
	}
	private void initComponents(int userVal) {

		boolean isAdmin = userVal == 0;
		boolean isCIA = userVal == 1;
		boolean isFBI = userVal == 2;

		frame = new JFrame("Menu");
		frame.setLayout(new FlowLayout());
		
		JButton selectButton = new JButton();
		selectButton.setText("Data by country");
		selectButton.addActionListener(e -> selectAction(e));
		frame.add(selectButton);

		JButton projectButton = new JButton();
		projectButton.setText("Owners of devices by type");
		projectButton.addActionListener(e -> projectAction(e));
		frame.add(projectButton);

		JButton joinButton = new JButton();
		joinButton.setText("Data and associated owners");
		joinButton.addActionListener(e -> joinAction(e));
		frame.add(joinButton);

		JButton aggrButton = new JButton();
		aggrButton.setText("Suspicious data per country");
		aggrButton.addActionListener(e -> aggrAction(e));
		if(isAdmin || isCIA || isFBI) {
			frame.add(aggrButton);
		}

		JButton nestedAggrButton = new JButton();
		nestedAggrButton.setText("Country with min/max average transaction");
		nestedAggrButton.addActionListener(e -> nestedAggrAction(e));
		if (isAdmin || isCIA) {
			frame.add(nestedAggrButton);
		}
		
		JButton divisionButton = new JButton();
		divisionButton.setText("New potential persons of interest");
		divisionButton.addActionListener(e -> divisionAction(e));
		frame.add(divisionButton);

		JButton deleteButton = new JButton();
		deleteButton.setText("Stop tracking a device");
		deleteButton.addActionListener(e -> deleteAction(e));
		if (isAdmin) {
			frame.add(deleteButton);
		}
		
		JButton deleteButton2 = new JButton();
		deleteButton2.setText("Nuke data");
		deleteButton2.addActionListener(e -> deleteAction2(e));
		if (isAdmin) {
			frame.add(deleteButton2);
		}
		
		JButton updateButton = new JButton();
		updateButton.setText("Falsify evidence");
		updateButton.addActionListener(e -> updateAction(e));
		if (isAdmin) {
			frame.add(updateButton);		
		}

		JButton logoutButton = new JButton();
		logoutButton.setText("Logout");
		logoutButton.addActionListener(e -> logoutAction(e));
		frame.add(logoutButton);

		
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private void divisionAction(ActionEvent e) {
		Division d = new Division(con);
		Object[][] empty = {{""}};
		String[] header = {"person_of_interest"};
		JTable table = new JTable(empty, header);
		new TableFrame(table, "find", "", d);
	}
	private void updateAction(ActionEvent e) {
		Update u = new Update(con);
		JTable table = u.selectAllData();
		new UpdateTableFrame(table, "update", "data id", "YYYYMMDD", u);
	}
	
	private void deleteAction(ActionEvent e) {
		Deletion d = new Deletion(con);
		JTable table = d.selectAllDevices();
		new TableFrame(table, "delete", "device id", d);
	}
	
	private void deleteAction2(ActionEvent e) {
		Deletion d = new Deletion(con);
		JTable table = d.selectAllData();
		new TableFrame(table, "delete", "data id", d);
	}
	
	private void nestedAggrAction(ActionEvent e) {
		NestedAggregationWithGroupBy n = new NestedAggregationWithGroupBy(con);
		Object[][] empty = {{"",""}};
		String[] header = {"country", "max_avg_suspicious_amount"};
		JTable table = new JTable(empty, header);
		new TableFrame(table, "count", "min/max", n); // TODO whip up a custom form with a min/max selector instead of a text input field
	}
	private void aggrAction(ActionEvent e) {
		Aggregation a = new Aggregation(con);
		Object[][] empty = {{"",""}};
		String[] header = {"country", "suspicious_data_count"};
		JTable table = new JTable(empty, header);
		new TableFrame(table, "count", "country", a);
	}
	private void selectAction(ActionEvent e) {
		Selection s = new Selection(con);
		Object[][] empty = {{"","","","","",""}};
		String[] header ={"data_id", "date", "suspicious", "lat", "lng", "device_id"};
		JTable table = new JTable(empty, header);
		new TableFrame(table, "select", "country", s);
		}
	
	private void projectAction(ActionEvent e) {
		Projection p = new Projection(con);
		Object[][] empty = {{"",""}};
		String[] header = {"device_type", "owner"};
		JTable table = new JTable(empty, header);
		new TableFrame(table, "project", "device type", p);
	}
	
	private void joinAction(ActionEvent e) {
		Join j = new Join(con);
		Object[][] empty = {{"","","",""}};
		String[] header = {"owner", "data_id", "date", "suspicious"};
		JTable table = new JTable(empty, header);
		new TableFrame(table, "join", "", j);
	}

	private void logoutAction(ActionEvent e){
		this.frame.dispose();
		new Login2(con).setVisible(true);
	}
}
