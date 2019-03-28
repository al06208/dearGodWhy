package SQLTesting;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	private JFrame mainFrame;
	private JTable results;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Main window = new Main();
				window.mainFrame.setVisible(true);
			}
		});
	}
	
	public Main() {
		initialize();
	}
	
	private void initialize() {
		mainFrame = new JFrame("Test Program");
		mainFrame.setBounds(100,100,500,550);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		String connectionUrl = "jdbc:sqlserver://den1.mssql8.gear.host;databaseName=gsuprojectdb;user=gsuprojectdb;password=thisisbullshit123!";
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT COUNT(CustomerID) as numCustomers FROM customers;";
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            int numRows = Integer.parseInt(rs.getString("numCustomers"));
            Object[][] content = new Object[numRows][3];
            SQL = "SELECT * FROM customers;";
            Statement stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery(SQL);
            int index = 0;
            while(rs2.next()) {
            	content[index][0] = rs2.getString("CustomerID");
            	content[index][1] = rs2.getString("FirstName");
            	content[index][2] = rs2.getString("LastName");
            	index++;
            }
            String[] columns = {"CustId","CustFirst","CustLast"};
            results = new JTable(content, columns);

    		
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
		results.setBounds(10,10,400,400);
        results.setPreferredScrollableViewportSize(new Dimension(400,200));
		mainFrame.getContentPane().add(results);		
	}

}
