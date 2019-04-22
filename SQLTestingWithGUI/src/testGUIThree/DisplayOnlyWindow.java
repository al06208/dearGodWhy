package testGUIThree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DisplayOnlyWindow extends JFrame {

	private String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public DisplayOnlyWindow(String tableName) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 503, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//GET TABLE DATA
		int colNum;
		ArrayList<String> colNamesAL = new ArrayList<String>();
		//This gets the number of columns, then uses that to get the column names from the metadata
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT * FROM "+tableName+";";
            JOptionPane.showMessageDialog(null, SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            ResultSetMetaData rsmd = rs.getMetaData();
            colNum = rsmd.getColumnCount();
            for(int i=1;i<=colNum;i++) {
            	// TODO: DELETE JOptionPane.showMessageDialog(null, "column name is "+rsmd.getColumnName(i));
            	colNamesAL.add(rsmd.getColumnName(i));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
		
		//BUILD TABLE
		//String[] columns = (String[])colNamesAL.toArray();
		String[] columns = Arrays.copyOf(colNamesAL.toArray(), colNamesAL.size(), String[].class);

		
        int numRows = getNumber("testTable");
        DefaultTableModel resTable = new DefaultTableModel(columns, numRows);
        fetchNEdit(resTable, tableName, columns);
		JTable results = new JTable(resTable);
		//ADD TO GUi
		results.setBounds(10,10,400,400);
        results.setPreferredScrollableViewportSize(new Dimension(400,200));
        JScrollPane pane = new JScrollPane(results);
		pane.setBounds(10, 11, 464, 314);
		contentPane.setLayout(null);
		contentPane.setLayout(null);
		contentPane.add(pane);
		
		//Simple refresh button
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fetchNEdit(resTable, tableName, columns);
			}
		});
		btnRefresh.setBounds(385, 336, 89, 23);
		contentPane.add(btnRefresh);
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
//	        	dtl.setValueAt(rs.getString("primaryKey"), index, 0);
//	        	dtl.setValueAt(rs.getString("testString"), index, 1);
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
