package testGUIThree;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class PassengerSublauncher extends JFrame {
	
	boolean isChecked = false; //if true, matching email found in the db

	private JPanel contentPane;
	private JTextField txtEmailaddress;
	private String connectionUrl = "jdbc:sqlserver://den1.mssql7.gear.host;databaseName=gsuprojectdbnew;user=gsuprojectdbnew;password=thisisbullshit123!";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PassengerSublauncher frame = new PassengerSublauncher();
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
	public PassengerSublauncher() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 170, 214);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewUser = new JButton("New User");
		btnNewUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    EventQueue.invokeLater(new Runnable()
			    {
			        @Override
			        public void run()
			        {
			            JFrame frame = new PassengerMaker();
			            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			            frame.setLocationByPlatform(true);
			            frame.setVisible(true);
			            frame.setResizable(false);
			        }
			    });
			}
		});
		btnNewUser.setBounds(10, 11, 131, 23);
		contentPane.add(btnNewUser);
		
		JButton btnEdit = new JButton("Edit Profile");
		btnEdit.setEnabled(false);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    EventQueue.invokeLater(new Runnable()
			    {
			        @Override
			        public void run()
			        {
			            JFrame frame = new PassengerEditor(txtEmailaddress.getText());
			            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			            frame.setLocationByPlatform(true);
			            frame.setVisible(true);
			            frame.setResizable(false);
			        }
			    });
			}
		});
		btnEdit.setBounds(10, 110, 131, 23);
		contentPane.add(btnEdit);
		
		txtEmailaddress = new JTextField();
		txtEmailaddress.setText("Email Address");
		txtEmailaddress.setBounds(10, 45, 131, 20);

		contentPane.add(txtEmailaddress);
		txtEmailaddress.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(10, 76, 131, 23);
		contentPane.add(btnLogin);
		
		JButton btnTickets = new JButton("Tickets");
		btnTickets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable()
			    {
			        @Override
			        public void run()
			        {
			            JFrame frame = new TicketManager(txtEmailaddress.getText());
			            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			            frame.setLocationByPlatform(true);
			            frame.setVisible(true);
			            frame.setResizable(false);
			        }
			    });
			}
		});
		btnTickets.setEnabled(false);
		btnTickets.setBounds(10, 144, 131, 23);
		contentPane.add(btnTickets);
		txtEmailaddress.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				//If the user edits the email field, they must login to be able to access edit or ticket windows
				btnTickets.setEnabled(false);
				btnEdit.setEnabled(false);
				btnLogin.setEnabled(true);
			}
		});
		//this is the login method, which ensures there is an existing email address that matches the email field
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
		            String SQL = ("SELECT * FROM PASSENGERS WHERE EmailAddress = '"+txtEmailaddress.getText()+"';");
		            ResultSet rs = stmt.executeQuery(SQL);
		            //if the email is found, enable the ticket/edit buttons and disable login
		            if (rs.next()) {
		            	btnLogin.setEnabled(false);
		            	btnTickets.setEnabled(true);
		            	btnEdit.setEnabled(true);
		            }
		            else {
		            	//if not, notify.
		            	JOptionPane.showMessageDialog(null, "Email Address not found in database. Have you registered?");
		            }
		        }
		        catch (SQLException ex) {
		            ex.printStackTrace();
		        }
				
			}
		});
	}
}
