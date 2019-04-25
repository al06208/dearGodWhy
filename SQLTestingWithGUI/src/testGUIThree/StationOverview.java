package testGUIThree;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;

public class StationOverview extends JFrame {

	private JPanel contentPane;
	private String[] columns = {"StationID","StationName","StreetAddress","CityID"};
	private String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StationOverview frame = new StationOverview();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//Create these labels for convenience
	JLabel lblState = DefaultComponentFactory.getInstance().createLabel("State");
	JLabel lblCity = DefaultComponentFactory.getInstance().createLabel("City");
	JLabel lblStreetaddress = DefaultComponentFactory.getInstance().createLabel("StreetAddress");
	JLabel lblStationname = DefaultComponentFactory.getInstance().createLabel("StationName");
	JTable mainTable;
	/**
	 * Create the frame.
	 */
	public StationOverview() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 381);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//actually build mainTable
		DefaultTableModel dtm = new DefaultTableModel(columns, 1);
		fetchNEdit(dtm,"Station",columns);
		mainTable = new JTable(dtm);
		mainTable.setRowSelectionAllowed(true);
		mainTable.setColumnSelectionAllowed(false);
		mainTable.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
				  JTable target = (JTable)e.getSource();
			      int stationID = target.getSelectedRow() + 1;
			      showDetails(stationID);
			  }
			});
		JScrollPane scrollPane = new JScrollPane(mainTable);
		scrollPane.setBounds(10, 11, 414, 211);
		contentPane.add(scrollPane);
		//Add those labels we made earlier
		lblStationname.setBounds(10, 233, 414, 14);
		contentPane.add(lblStationname);
		lblStreetaddress.setBounds(10, 308, 414, 14);
		contentPane.add(lblStreetaddress);
		lblCity.setBounds(10, 258, 414, 14);
		contentPane.add(lblCity);
		lblState.setBounds(10, 283, 414, 14);
		contentPane.add(lblState);
	}
	
	//Returns the number of rows in the table
	private int getNumber(String table) {
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT COUNT(*) as numKeys FROM "+table+";";
            JOptionPane.showMessageDialog(null, SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            int numRows = Integer.parseInt(rs.getString("numKeys"));
            return numRows;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
	}
	//Fetches the state of the specified table, then updates the given data model to match it
	private void fetchNEdit(DefaultTableModel dtl, String table, String[] columns) {
		int numrows = getNumber(table);
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT * FROM "+table+";";
            JOptionPane.showMessageDialog(null, SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            dtl.setRowCount(numrows);
            int index = 0;
            int indexTwo = 0;
	        while(rs.next()) {
	        	indexTwo = 0;
	        	for(String s:columns) {
	        		dtl.setValueAt(rs.getString(s), index, indexTwo);
	        		indexTwo++;
	        	}
	        	index++;
	        }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
	}
	//Queries the DB for the details of the selected station, then writes them to the labels
	private void showDetails(int id) {
        lblState.setText("...");
        lblCity.setText("...");
        lblStationname.setText("...");
        lblStreetaddress.setText("...");
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
			//SQL Statement to get all details of selected station
            String SQL = ("SELECT Station.StationID, Station.StationName, Station.StreetAddress, City.CityID, States.StateName FROM Station LEFT JOIN City ON Station.CityID = City.CityID LEFT JOIN States ON City.StateID = States.StateID WHERE Station.StationID = "+id+";");
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            lblState.setText(rs.getString("StateName"));
            lblCity.setText(rs.getString("CityID"));
            lblStationname.setText(rs.getString("StationName"));
            lblStreetaddress.setText(rs.getString("StreetAddress"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
