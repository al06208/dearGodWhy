package testGUITwo;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JTable;

import SQLTesting.Main;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.jgoodies.forms.factories.DefaultComponentFactory;

public class TestWindow {

	private JFrame mainFrame;
	private JTable results;
	private JTextField textField;
	private String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				TestWindow window = new TestWindow();
				window.mainFrame.setVisible(true);
			}
		});
	}
	
	public TestWindow() {
		initialize();
	}
	
	private void initialize() {
		mainFrame = new JFrame("Test Program");
		mainFrame.setBounds(100,100,500,440);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		
		//BUILD THE TABLE
        String[] columns = {"primaryKey","testString"};
        int numRows = getNumber("testTable","primaryKey");
        Object[][] content = getAll("testTable", "primaryKey", numRows);
        //Hardcoded example, uses testTable and its two columns
        //uses the 2d array to populate the table
        DefaultTableModel resTable = new DefaultTableModel(content, columns);
        results = new JTable(resTable);
		panel.setLayout(null);
		results.setBounds(10,10,400,400);
        results.setPreferredScrollableViewportSize(new Dimension(400,200));
		//mainFrame.getContentPane().add(results);
		JScrollPane pane = new JScrollPane(results);
		pane.setBounds(10, 11, 464, 314);
		panel.add(pane);
		mainFrame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JButton btnYeet = new JButton("YEET");
		btnYeet.setBounds(251, 351, 121, 40);
		textField = new JTextField();
		textField.setBounds(33, 361, 208, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		//update button
		btnYeet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try(Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();){
					int col = results.getSelectedColumn();
					int row = results.getSelectedRow();
					String resu = ("COLUMN "+ col + " ROW "+ row + " CHANGED");
					Statement change = con.createStatement();
					String newval = textField.getText();
					String query;
					if(col == 0) {
						query = ("UPDATE testTable SET PrimaryKey = "+ newval + " WHERE PrimaryKey = " + (String)results.getValueAt(row, 0) + ";"); 
						JOptionPane.showMessageDialog(null, query);
						change.execute(query);
						DefaultTableModel dtm = (DefaultTableModel)results.getModel();
						dtm.setValueAt(newval, row, col);
					}
					else {
						query = ("UPDATE testTable SET testString = '"+ newval + "' WHERE PrimaryKey = " + (String)results.getValueAt(row, 0) + ";"); 
						JOptionPane.showMessageDialog(null, query);
						change.execute(query);
						DefaultTableModel dtm = (DefaultTableModel)results.getModel();
						dtm.setValueAt(newval, row, col);
					}
					JOptionPane.showMessageDialog(null, resu);
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(null, "FUCK");
					e.printStackTrace();
				}
			}
		});
		panel.add(btnYeet);
		JLabel lblNewName = DefaultComponentFactory.getInstance().createLabel("NEW NAME");
		lblNewName.setBounds(98, 336, 92, 14);
		panel.add(lblNewName);
		
		JButton btnRefresh = new JButton("REFRESH");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int num = getNumber("testTable","primaryKey");
				fetchNEdit((DefaultTableModel)results.getModel(),"testTable", num);
			}
		});
		btnRefresh.setBounds(385, 360, 89, 23);
		panel.add(btnRefresh);
	}
	//END OF INITIALIZE METHOD
	//RETURN NUMBER OF ROWS IN THE TABLE
	private int getNumber(String table, String primaryKey) {
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT COUNT("+primaryKey+") as numKeys FROM "+table+";";
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
	
	//returns a 2d array of stuff from the query
	//TODO: un-hard-code
	private Object[][] getAll(String table, String primaryKey, int numRows) {
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT * FROM "+table+";";
            JOptionPane.showMessageDialog(null, SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            Object[][] content = new Object[numRows][2];
            int index = 0;
	        while(rs.next()) {
	        	content[index][0] = rs.getString("primaryKey");
	        	content[index][1] = rs.getString("testString");
	        	index++;
	        }
	        return content;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	//update table based on query
	//TODO: un-hard-code
	private void fetchNEdit(DefaultTableModel dtl, String table, int numRows) {
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT * FROM "+table+";";
            JOptionPane.showMessageDialog(null, SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            //Object[][] content = new Object[numRows][2];
            int index = 0;
	        while(rs.next()) {
	        	//content[index][0] = rs.getString("primaryKey");
	        	dtl.setValueAt(rs.getString("primaryKey"), index, 0);
	        	//content[index][1] = rs.getString("testString");
	        	dtl.setValueAt(rs.getString("testString"), index, 1);
	        	index++;
	        }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
	}

}