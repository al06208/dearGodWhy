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

public class PassengerMaker extends JFrame {

	private JPanel contentPane;
	String email;
	String fname;
	String lname;
	private JTextField txtEmailaddress;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PassengerMaker frame = new PassengerMaker();
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
	public PassengerMaker() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 190, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtEmailaddress = new JTextField();
		txtEmailaddress.setText("EmailAddress");
		txtEmailaddress.setBounds(10, 11, 154, 20);
		contentPane.add(txtEmailaddress);
		txtEmailaddress.setColumns(10);
		
		txtFirstName = new JTextField();
		txtFirstName.setText("First Name");
		txtFirstName.setBounds(10, 42, 154, 20);
		contentPane.add(txtFirstName);
		txtFirstName.setColumns(10);
		
		txtLastName = new JTextField();
		txtLastName.setText("Last Name");
		txtLastName.setBounds(10, 73, 154, 20);
		contentPane.add(txtLastName);
		txtLastName.setColumns(10);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int pid = -1;
				//first, get the proper next PID
				try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
		            String SQL = ("SELECT TOP 1 PassengerID FROM passengers ORDER BY PassengerID desc;");
		            ResultSet returned = stmt.executeQuery(SQL);
		            returned.next();
		            pid = Integer.parseInt(returned.getString("PassengerID")) + 1;
		        }
		        catch (SQLException ex) {
		            ex.printStackTrace();
		        }
				//then, use that to build the new passenger's entry
				try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
		            String SQL = ("INSERT INTO Passengers(PassengerID, Fname, Lname, EmailAddress) VALUES ("+pid+", '"+txtFirstName.getText()+"', '"+txtLastName.getText()+"', '"+txtEmailaddress.getText()+"');");
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
		btnRegister.setEnabled(false);
		btnRegister.setBounds(10, 138, 154, 23);
		contentPane.add(btnRegister);
		
		JButton btnCheck = new JButton("Check Email");
		btnCheck.setBounds(10, 104, 154, 23);
		contentPane.add(btnCheck);
		
		//checks to make sure the entered email is not present
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
		            String SQL = ("SELECT * FROM PASSENGERS WHERE EmailAddress = '"+txtEmailaddress.getText()+"';");
		            ResultSet rs = stmt.executeQuery(SQL);
		            //if the email is found, tell the user
		            if (rs.next()) {
		            	JOptionPane.showMessageDialog(null,"EmailAddress already taken.");
		            }
		            else {
		            	btnCheck.setEnabled(false);
		            	btnRegister.setEnabled(true);
		            }
		        }
		        catch (SQLException ex) {
		            ex.printStackTrace();
		        }
			}
		});
		
		txtEmailaddress.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				//If the user edits the email field, they must check it again
				btnRegister.setEnabled(false);
				btnCheck.setEnabled(true);
			}
		});
	}

}
