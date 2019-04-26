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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.factories.DefaultComponentFactory;

public class EmployeeViewer extends JFrame {


	private JPanel contentPane;
	private String[] columns = {"Fname","Lname"};
	private String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeViewer frame = new EmployeeViewer();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//Create these labels for convenience
	JLabel lblPos = DefaultComponentFactory.getInstance().createLabel("Position");
	JLabel lblLname = DefaultComponentFactory.getInstance().createLabel("Last");
	JLabel lblSalary = DefaultComponentFactory.getInstance().createLabel("Salary");
	JLabel lblFname = DefaultComponentFactory.getInstance().createLabel("First");
	JTable mainTable;
	/**
	 * Create the frame.
	 */
	public EmployeeViewer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 273, 381);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//actually build mainTable
		DefaultTableModel dtm = new DefaultTableModel(columns, 1);
		fetchNEdit(dtm,"Employees",columns);
		mainTable = new JTable(dtm);
		mainTable.setRowSelectionAllowed(true);
		mainTable.setColumnSelectionAllowed(false);
		mainTable.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
				  JTable target = (JTable)e.getSource();
				  String employeeFname = (String)target.getValueAt(target.getSelectedRow(),0);
			      showDetails(employeeFname);
			  }
			});
		JScrollPane scrollPane = new JScrollPane(mainTable);
		scrollPane.setBounds(10, 11, 238, 211);
		contentPane.add(scrollPane);
		//Add those labels we made earlier
		lblFname.setBounds(10, 233, 238, 14);
		contentPane.add(lblFname);
		lblSalary.setBounds(10, 308, 238, 14);
		contentPane.add(lblSalary);
		lblLname.setBounds(10, 258, 238, 14);
		contentPane.add(lblLname);
		lblPos.setBounds(10, 283, 238, 14);
		contentPane.add(lblPos);
	}
	
	//Returns the number of rows in the table
	private int getNumber(String table) {
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT COUNT(*) as numKeys FROM "+table+";";
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
            String SQL = "SELECT Fname, Lname FROM "+table+";";
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
	private void showDetails(String fname) {
        lblPos.setText("...");
        lblLname.setText("...");
        lblFname.setText("...");
        lblSalary.setText("...");
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
			//SQL Statement to get all details of selected station
            String SQL = ("SELECT * FROM Employees LEFT JOIN Station ON Employees.StationID = Station.StationID WHERE Employees.Fname = '"+fname+"';");
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            lblPos.setText(rs.getString("Position"));
            lblLname.setText(rs.getString("Lname"));
            lblFname.setText(rs.getString("Fname"));
            lblSalary.setText(rs.getString("Salary"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
	}

}
