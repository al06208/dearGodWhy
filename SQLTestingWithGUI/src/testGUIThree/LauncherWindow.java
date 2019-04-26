package testGUIThree;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.JComboBox;

public class LauncherWindow {

	private JFrame frmTrainviewLauncher;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LauncherWindow window = new LauncherWindow();
					window.frmTrainviewLauncher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LauncherWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTrainviewLauncher = new JFrame();
		frmTrainviewLauncher.setTitle("TrainView Launcher");
		frmTrainviewLauncher.setBounds(100, 100, 183, 236);
		frmTrainviewLauncher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTrainviewLauncher.getContentPane().setLayout(null);
		
		//Combobox of the different tables
		String[] comboTables = {"CargoTrains","City","Conductors","Employees","Passengers","PassengerTrains","Routes","ShipmentDetails","Shipments","States","Station","Tickets","Trains"};
		JComboBox comboBox = new JComboBox(comboTables);
		comboBox.setBounds(10, 164, 143, 20);
		frmTrainviewLauncher.getContentPane().add(comboBox);
		//Button to launch a direct view of the table selected
		JButton btnManual = new JButton("Manual View");
		btnManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createViewFrame((String)comboBox.getSelectedItem());
			}
		});
		btnManual.setBounds(10, 113, 143, 23);
		frmTrainviewLauncher.getContentPane().add(btnManual);
		
		JLabel lblTableToView = DefaultComponentFactory.getInstance().createLabel("Table to view:");
		lblTableToView.setBounds(10, 147, 92, 14);
		frmTrainviewLauncher.getContentPane().add(lblTableToView);
		//Listener invokes station frame
		JButton btnStationView = new JButton("Station View");
		btnStationView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						JFrame frame = new StationOverview();
			            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			            frame.setLocationByPlatform(true);
			            frame.setVisible(true);
			            frame.setResizable(false);
					}
				});
			}
		});
		btnStationView.setBounds(10, 11, 143, 23);
		frmTrainviewLauncher.getContentPane().add(btnStationView);
		
		JButton btnPassengers = new JButton("Passengers");
		btnPassengers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						JFrame frame = new PassengerSublauncher();
			            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			            frame.setLocationByPlatform(true);
			            frame.setVisible(true);
			            frame.setResizable(false);
					}
				});
			}
		});
		btnPassengers.setBounds(10, 45, 143, 23);
		frmTrainviewLauncher.getContentPane().add(btnPassengers);
		
		JButton btnEmployeeView = new JButton("Employee View");
		btnEmployeeView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable()
			    {
			        @Override
			        public void run()
			        {
			            JFrame frame = new EmployeeViewer();
			            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			            frame.setLocationByPlatform(true);
			            frame.setVisible(true);
			            frame.setResizable(false);
			        }
			    });
			}
		});
		btnEmployeeView.setBounds(10, 79, 143, 23);
		frmTrainviewLauncher.getContentPane().add(btnEmployeeView);

	}

//TODO: Remove test methods
	//Creates a window that specifically reads the testTable
public static void createTestFrame()
{
    EventQueue.invokeLater(new Runnable()
    {
        @Override
        public void run()
        {
            JFrame frame = new TestTableWindow();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
            frame.setResizable(false);
        }
    });
}

//Creates a generic SQL Table Viewer with no edit functionality
public static void createViewFrame(String table) {
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			JFrame frame = new DisplayOnlyWindow(table);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
            frame.setResizable(false);
		}
	});
}
}
