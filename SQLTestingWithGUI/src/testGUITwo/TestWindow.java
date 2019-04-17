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
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.jgoodies.forms.factories.DefaultComponentFactory;

public class TestWindow {

	private JFrame mainFrame;
	private JTable results;
	private JTextField textField;
	
	
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
		mainFrame.setBounds(100,100,500,550);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		//mainFrame.getContentPane().setLayout(null);
		
		String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT COUNT(primaryKey) as numKeys FROM testTable;";
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            int numRows = Integer.parseInt(rs.getString("numKeys"));
            Object[][] content = new Object[numRows][3];
            SQL = "SELECT * FROM testTable;";
            Statement stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery(SQL);
            int index = 0;
            while(rs2.next()) {
            	content[index][0] = rs2.getString("primaryKey");
            	content[index][1] = rs2.getString("testString");
            	index++;
            }
            String[] columns = {"primaryKey","testString"};
            results = new JTable(content, columns);

    		
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
		panel.setLayout(null);
		results.setBounds(10,10,400,400);
        results.setPreferredScrollableViewportSize(new Dimension(400,200));
		//mainFrame.getContentPane().add(results);
		JScrollPane pane = new JScrollPane(results);
		pane.setBounds(10, 11, 464, 314);
		panel.add(pane);
		mainFrame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JButton btnYeet = new JButton("YEET");
		btnYeet.setBounds(325, 396, 121, 40);
		textField = new JTextField();
		textField.setBounds(37, 406, 208, 20);
		panel.add(textField);
		textField.setColumns(10);
		btnYeet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try(Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();){
					int col = results.getSelectedColumn();
					int row = results.getSelectedRow();
					String resu = ("COLUMN "+ col + " ROW "+ row + " CHANGED");
					Statement change = con.createStatement();
					String colname = textField.getText();
					String query;
					if(col == 0) {
						query = ("UPDATE testTable SET PrimaryKey = "+textField.getText() + " WHERE PrimaryKey = " + (String)results.getValueAt(row, col) + ";"); 
						JOptionPane.showMessageDialog(null, query);
						change.execute(query);
					}
					else colname = "testString";
					
					
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
		lblNewName.setBounds(84, 381, 92, 14);
		panel.add(lblNewName);
	}
}