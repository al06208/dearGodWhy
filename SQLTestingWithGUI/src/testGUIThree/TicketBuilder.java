package testGUIThree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.JButton;

public class TicketBuilder extends JFrame {

	private JPanel contentPane;
	private String email;
	private String pid;
	private String date = "2019-01-01";
	private String leaveTime = "00:00";
	private String arriveTime = "01:00";
	private String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";
	private String[] columns = {"RouteID","StationName"};

	/**
	 * Create the frame.
	 */
	public TicketBuilder(String email, String pid) {
		setTitle("New Ticket");
		this.email = email;
		this.pid = pid;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 502, 309);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		DefaultTableModel dtm = new DefaultTableModel(columns, 1);
		JTable arrival = new JTable(dtm);
		//ADD TO GUi
		arrival.setBounds(10,10,400,400);
        arrival.setPreferredScrollableViewportSize(new Dimension(400,200));
        JScrollPane pane = new JScrollPane(arrival);
		pane.setBounds(184, 37, 277, 184);
		contentPane.setLayout(null);
		contentPane.setLayout(null);
		contentPane.setLayout(null);
		contentPane.add(pane);
		

		JComboBox comboStart = new JComboBox(getStations().toArray());
		comboStart.setBounds(10, 37, 164, 20);
		contentPane.add(comboStart);
		
		JLabel lblDepartureStation = DefaultComponentFactory.getInstance().createLabel("Departure Station");
		lblDepartureStation.setBounds(10, 12, 120, 14);
		contentPane.add(lblDepartureStation);
		
		JLabel lblArrivalStation = DefaultComponentFactory.getInstance().createLabel("Arrival Station");
		lblArrivalStation.setBounds(291, 12, 92, 14);
		contentPane.add(lblArrivalStation);
		
		JLabel lblTo = DefaultComponentFactory.getInstance().createLabel("to");
		lblTo.setBounds(169, 12, 92, 14);
		contentPane.add(lblTo);
		
		String[] years = {"2019","2020","2021","2022"};
		JComboBox comboYear = new JComboBox(years);
		comboYear.setBounds(114, 93, 60, 20);
		contentPane.add(comboYear);
		
		String[] months = {"01","02","03","04","05","06","06","08","09","10","11","12"};
		JComboBox comboMonth = new JComboBox(months);
		comboMonth.setBounds(10, 93, 43, 20);
		contentPane.add(comboMonth);
		
		String[] days = {"01","02","03","04","05","06","06","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
		JComboBox comboDay = new JComboBox(days);
		comboDay.setBounds(63, 93, 41, 20);
		contentPane.add(comboDay);
		
		JLabel lblDepartureDate = DefaultComponentFactory.getInstance().createLabel("Departure Date (MMDDYYYY)");
		lblDepartureDate.setBounds(10, 68, 164, 14);
		contentPane.add(lblDepartureDate);
		
		String[] hours = {"00","01","02","03","04","05","06","06","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
		JComboBox comboDept = new JComboBox(hours);
		comboDept.setBounds(10, 146, 164, 20);
		contentPane.add(comboDept);
		
		JLabel lblDepartureTime = DefaultComponentFactory.getInstance().createLabel("Departure Time");
		lblDepartureTime.setBounds(10, 124, 92, 14);
		contentPane.add(lblDepartureTime);
		
		JComboBox comboArriv = new JComboBox(hours);
		comboArriv.setBounds(10, 201, 164, 20);
		contentPane.add(comboArriv);
		
		JLabel lblArrivalTime = DefaultComponentFactory.getInstance().createLabel("Arrival Time");
		lblArrivalTime.setBounds(10, 176, 92, 14);
		contentPane.add(lblArrivalTime);
		
		
		//the mother of all button listeners
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to submit this ticket?","Confirm",JOptionPane.YES_NO_OPTION);
				//allow user to cancel
				if (reply == JOptionPane.YES_OPTION) {
					int newid = -1; //the TicketID to insert this new ticket with
					String journeyDate = ((String)comboMonth.getSelectedItem()+"-"+(String)comboDay.getSelectedItem()+"-"+(String)comboYear.getSelectedItem());
					String departureTime = ((String)comboDept.getSelectedItem()+":00");
					String arrivalTime = ((String)comboArriv.getSelectedItem()+":00");
					String routeID = (String)arrival.getValueAt(arrival.getSelectedRow(),0);
					//Gets the current last TicketID so that this one can be inserted after it
					try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
			            String SQL = ("SELECT CAST(TicketID AS int) AS TicketID FROM Tickets ORDER BY TicketID desc;");
			            ResultSet returned = stmt.executeQuery(SQL);
			            returned.next();
			            newid = Integer.parseInt(returned.getString("TicketID")) + 1;
			        }
			        catch (SQLException ex) {
			            ex.printStackTrace();
			        }	
					//If that's successful, attempt to ~insert~
					try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
			            String SQL = ("INSERT INTO Tickets (TicketID, Price, JourneyDate, DepartureTime, ArrivalTime, RouteID, PassengerID) VALUES ("+newid+","+34.25+",'"+journeyDate+"','"+departureTime+"','"+arrivalTime+"',"+routeID+",'"+pid+"');");
			            stmt.execute(SQL);
			            JOptionPane.showMessageDialog(null,"Successful!");
			            JButton urgr = (JButton)e.getSource();
			            JFrame burgr = (JFrame)urgr.getParent().getParent().getParent().getParent();
			            burgr.dispose();
			        }
			        catch (SQLException ex) {
			            ex.printStackTrace();
			        }	
				}
			}
		});
		btnSubmit.setEnabled(false);
		btnSubmit.setBounds(10, 232, 451, 23);
		contentPane.add(btnSubmit);
		
		//when a new station is selected, clear arrival selections and refresh options
		comboStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//fetch and edit 
				arrival.clearSelection();
				btnSubmit.setEnabled(false);
				fetchNEdit(dtm, columns, (String)comboStart.getSelectedItem());
			}
		});
		
		arrival.addMouseListener(new MouseAdapter() {
			//when a new arrival station is selected, enable the submit button
			public void mouseClicked(MouseEvent e) {
				btnSubmit.setEnabled(true);
			}
		});
	}
	
	private ArrayList<String> getStations() {
		ArrayList<String> output = new ArrayList<String>();
		System.out.println("Trying to get station names");
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT DISTINCT StationName FROM Station";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
            	output.add(rs.getString("StationName"));
            }
            return output;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return output;
        }
	}
	
	private int getNumber(String start) {
		//gets the number of stations available from the selected start
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT Count(RouteID) AS numKeys FROM Routes as A LEFT JOIN Station AS B on A.StartStationID = B.StationID LEFT JOIN Station AS C on A.EndStationID = C.StationID WHERE B.StationName = '"+start+"';";
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
	
	//fetches matching arrival stations
	private void fetchNEdit(DefaultTableModel dtl, String[] columns, String startname) {
		int numrows = getNumber(startname);
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT A.RouteID, B.StationName AS StartName, C.StationName AS StationName FROM Routes as A LEFT JOIN Station AS B on A.StartStationID = B.StationID LEFT JOIN Station AS C on A.EndStationID = C.StationID WHERE B.StationName = '"+startname+"';";
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
}
