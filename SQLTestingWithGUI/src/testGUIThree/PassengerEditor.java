package testGUIThree;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class PassengerEditor extends JFrame {

	private JPanel contentPane;
	private JTextField txtEmailaddress;
	private String emailAddress;
	private JTextField txtFirstname;
	private JTextField txtLastname;
	private JTextField txtPid;
	private JButton btnSave;
	private JButton btnRefresh;
	private JButton btnDelete;
	
	private String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { PassengerEditor frame = new
	 * PassengerEditor(); frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

	/**
	 * Create the frame.
	 */
	public PassengerEditor(String email) {
		this.emailAddress = email;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 231, 244);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		txtEmailaddress = new JTextField();
		txtEmailaddress.setText("Email Address");
		txtEmailaddress.setBounds(10, 11, 191, 20);
		txtEmailaddress.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				btnSave.setEnabled(true);
			}
		});
		contentPane.add(txtEmailaddress);
		txtEmailaddress.setColumns(10);
		
		txtFirstname = new JTextField();
		txtFirstname.setText("First Name");
		txtFirstname.setBounds(10, 42, 154, 20);
		txtFirstname.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				btnSave.setEnabled(true);
			}
		});
		contentPane.add(txtFirstname);
		txtFirstname.setColumns(10);
		
		txtLastname = new JTextField();
		txtLastname.setText("Last Name");
		txtLastname.setBounds(10, 73, 191, 20);
		txtLastname.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				btnSave.setEnabled(true);
			}
		});
		contentPane.add(txtLastname);
		txtLastname.setColumns(10);
		
		txtPid = new JTextField();
		txtPid.setEditable(false);
		txtPid.setText("PID");
		txtPid.setBounds(177, 42, 24, 20);
		contentPane.add(txtPid);
		txtPid.setColumns(10);
		
		btnSave = new JButton("Save");
		
		//when clicked, updates the database and closes the window
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
		            String SQL = ("UPDATE Passengers SET Fname = '"+txtFirstname.getText()+"', Lname = '"+txtLastname.getText()+"', EmailAddress = '"+txtEmailaddress.getText()+"' WHERE PassengerID ="+txtPid.getText()+";");
		            stmt.execute(SQL);
		            JOptionPane.showMessageDialog(null, "Successfully saved changes.");
		            JButton urgr = (JButton)e.getSource();
		            JFrame burgr = (JFrame)urgr.getParent().getParent().getParent().getParent();
		            burgr.dispose();
		        }
		        catch (SQLException ex) {
		            ex.printStackTrace();
		        }
			}
		});
		btnSave.setEnabled(false);
		btnSave.setBounds(10, 104, 191, 23);
		contentPane.add(btnSave);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
		            String SQL = ("SELECT * FROM PASSENGERS WHERE PassengerID = "+txtPid.getText()+";");
		            JOptionPane.showMessageDialog(null, SQL);
		            ResultSet rs = stmt.executeQuery(SQL);
		            rs.next();
		            txtFirstname.setText(rs.getString("FName"));
		            txtLastname.setText(rs.getString("Lname"));
		            txtEmailaddress.setText(emailAddress);
		            txtPid.setText(rs.getString("PassengerID"));
		        }
		        catch (SQLException ex) {
		            ex.printStackTrace();
		        }
			}
		});
		btnRefresh.setBounds(10, 138, 191, 23);
		contentPane.add(btnRefresh);
		
		btnDelete = new JButton("Delete");
		
		btnDelete.setBounds(10, 172, 191, 23);
		contentPane.add(btnDelete);
		JOptionPane.showMessageDialog(null, "trying to get stuff");
		try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = ("SELECT * FROM PASSENGERS WHERE EmailAddress = '"+emailAddress+"';");
            JOptionPane.showMessageDialog(null, SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            txtFirstname.setText(rs.getString("FName"));
            txtLastname.setText(rs.getString("Lname"));
            txtEmailaddress.setText(emailAddress);
            txtPid.setText(rs.getString("PassengerID"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
		
		//deletes the entry corresponding to the pid 
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
		            String SQL = ("DELETE FROM PASSENGERS WHERE PassengerID = "+txtPid.getText()+";");
		            stmt.execute(SQL);
		            JOptionPane.showMessageDialog(null, "Successfully deleted.");
		            JButton urgr = (JButton)e.getSource();
		            JFrame burgr = (JFrame)urgr.getParent().getParent().getParent().getParent();
		            burgr.dispose();
		        }
		        catch (SQLException ex) {
		            ex.printStackTrace();
		        }
			}
		});
		
	}
}
