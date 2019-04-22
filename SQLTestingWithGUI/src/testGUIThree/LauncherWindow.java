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

public class LauncherWindow {

	private JFrame frame;
	private JTextField txtManView;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LauncherWindow window = new LauncherWindow();
					window.frame.setVisible(true);
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
		frame = new JFrame();
		frame.setBounds(100, 100, 153, 208);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnTestTable = new JButton("testTable");
		btnTestTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createTestFrame();
			}
		});
		btnTestTable.setBounds(10, 11, 115, 23);
		frame.getContentPane().add(btnTestTable);
		
		JButton btnManual = new JButton("Manual View");
		btnManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createViewFrame(txtManView.getText());
			}
		});
		btnManual.setBounds(10, 45, 115, 23);
		frame.getContentPane().add(btnManual);
		
		txtManView = new JTextField();
		txtManView.setBounds(10, 90, 115, 20);
		frame.getContentPane().add(txtManView);
		txtManView.setColumns(10);
		
		JLabel lblTableToView = DefaultComponentFactory.getInstance().createLabel("Table to view:");
		lblTableToView.setBounds(10, 73, 92, 14);
		frame.getContentPane().add(lblTableToView);
	}


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
