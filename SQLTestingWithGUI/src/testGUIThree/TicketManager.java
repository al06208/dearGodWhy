package testGUIThree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;

public class TicketManager extends JFrame {

	private JPanel contentPane;
	private String email;
	private String pid;
	private String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";
	
	/**
	 * Create the frame.
	 */
	public TicketManager(String email) {
		setTitle("Ticket Manager");
		this.email = email;
		this.pid = getPID(email);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 503, 270);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//GET TABLE DATA
		String[] columns = {"Price","JourneyDate","DepartureTime","ArrivalTime"};	
        int numRows = getNumber("Tickets");
        DefaultTableModel resTable = new DefaultTableModel(columns, numRows);
        fetchNEdit(resTable, columns);
		JTable results = new JTable(resTable);
		//ADD TO GUi
		results.setBounds(10,10,400,400);
        results.setPreferredScrollableViewportSize(new Dimension(400,200));
        JScrollPane pane = new JScrollPane(results);
		pane.setBounds(10, 11, 464, 173);
		contentPane.setLayout(null);
		contentPane.setLayout(null);
		contentPane.add(pane);
		
		//Simple refresh button
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fetchNEdit(resTable, columns);
			}
		});
		btnRefresh.setBounds(10, 195, 148, 23);
		contentPane.add(btnRefresh);
		
		JButton btnNewTicket = new JButton("New Ticket");
		btnNewTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable()
			    {
			        @Override
			        public void run()
			        {
			            JFrame frame = new TicketBuilder(email, pid);
			            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			            frame.setLocationByPlatform(true);
			            frame.setVisible(true);
			            frame.setResizable(false);
			        }
			    });
			}
		});
		btnNewTicket.setBounds(329, 195, 148, 23);
		contentPane.add(btnNewTicket);

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
	
	//Fetches the state of the table, then updates the given data model to match it
	private void fetchNEdit(DefaultTableModel dtl, String[] columns) {
		int numrows = getNumber("Tickets");
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT Tickets.Price, Tickets.JourneyDate, Tickets.DepartureTime, Tickets.ArrivalTime FROM Tickets LEFT JOIN Passengers ON Tickets.PassengerID = Passengers.PassengerID WHERE Passengers.EmailAddress = '"+email+"';";
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
	
	private String getPID(String email) {
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = ("SELECT emailAddress, passengerid FROM Passengers WHERE EmailAddress = '"+email+"';");
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            String numRows = rs.getString("passengerID");
            return numRows;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return "something is messed up";
        }
	}
}
