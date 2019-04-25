package testGUIThree;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

public class TestTableWindow extends JFrame {

	private String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestTableWindow frame = new TestTableWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestTableWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 503, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		//BUILD TABLE
        String[] columns = {"primaryKey","testString"};
        int numRows = getNumber("testTable","primaryKey");
        DefaultTableModel resTable = new DefaultTableModel(columns, numRows);
        fetchNEdit(resTable, "testTable");
		JTable results = new JTable(resTable);
		results.setBounds(10,10,400,400);
        results.setPreferredScrollableViewportSize(new Dimension(400,200));
        JScrollPane pane = new JScrollPane(results);
		pane.setBounds(10, 11, 464, 314);
		contentPane.setLayout(null);
		contentPane.setLayout(null);
		contentPane.add(pane);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fetchNEdit(resTable, "testTable");
			}
		});
		btnRefresh.setBounds(385, 336, 89, 23);
		contentPane.add(btnRefresh);
		
        
	}
	
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
	
	private void fetchNEdit(DefaultTableModel dtl, String table) {
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT * FROM "+table+";";
            JOptionPane.showMessageDialog(null, SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            int index = 0;
	        while(rs.next()) {
	        	dtl.setValueAt(rs.getString("primaryKey"), index, 0);
	        	dtl.setValueAt(rs.getString("testString"), index, 1);
	        	index++;
	        }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
	}
	

}
