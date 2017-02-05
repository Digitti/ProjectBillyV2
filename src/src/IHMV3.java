package src;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

import javax.swing.JToggleButton;
import javax.swing.JTextPane;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.DropMode;


public class IHMV3 implements MyFrame {

	private JFrame frmBillyProjectV;
	private JTextField textField_ipServer;
	private JTextField textField_listenPort;
	private JTextField textField_File;
	private JTextField textField_ipLocal;
	private JTable table_Tank;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IHMV3 window = new IHMV3();
					window.frmBillyProjectV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws UnknownHostException 
	 */
	public IHMV3() throws UnknownHostException {
		initialize();
		String ip = InetAddress.getLocalHost().toString();
		textField_ipLocal.setText(ip.substring(ip.indexOf("1")));
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws UnknownHostException 
	 */
	private void initialize() {
		frmBillyProjectV = new JFrame();
		frmBillyProjectV.setTitle("Billy Project V3");
		frmBillyProjectV.setBounds(100, 100, 1065, 463);
		frmBillyProjectV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmBillyProjectV.setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mnMenu.add(mntmQuit);
		
		JPanel panel_Settings = new JPanel();
		panel_Settings.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel_2 = new JLabel("IP Local : ");
		panel_Settings.add(lblNewLabel_2);
		
		textField_ipLocal = new JTextField();
		textField_ipLocal.setEditable(false);
		panel_Settings.add(textField_ipLocal);
		textField_ipLocal.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("IP Server :");
		panel_Settings.add(lblNewLabel);
		
		textField_ipServer = new JTextField();
		panel_Settings.add(textField_ipServer);
		textField_ipServer.setColumns(10);
		
		JCheckBox CheckBox_ipV6 = new JCheckBox("@IPV6");
		panel_Settings.add(CheckBox_ipV6);
		
		JLabel lblNewLabel_1 = new JLabel("Listen Port :");
		panel_Settings.add(lblNewLabel_1);
		
		textField_listenPort = new JTextField();
		panel_Settings.add(textField_listenPort);
		textField_listenPort.setColumns(10);
		
		JLabel lblSearchFile = new JLabel("Search File :");
		panel_Settings.add(lblSearchFile);
		
		textField_File = new JTextField();
		panel_Settings.add(textField_File);
		textField_File.setColumns(10);
		
		JCheckBox CheckBox_hashFile = new JCheckBox("Hash File");
		panel_Settings.add(CheckBox_hashFile);
		
		JPanel panel_Task = new JPanel();
		JLabel lblLog = new JLabel("Log :");
		
		JLabel lblTank = new JLabel("Tank :");
		
		table_Tank = new JTable();
		
		JTextArea textArea_Log = new JTextArea();
		textArea_Log.setEditable(false);
		GroupLayout groupLayout = new GroupLayout(frmBillyProjectV.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_Settings, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTank)
					.addContainerGap(913, Short.MAX_VALUE))
				.addComponent(panel_Task, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLog)
					.addContainerGap(919, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(table_Tank, GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(textArea_Log, GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel_Settings, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblLog)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textArea_Log, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTank)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(table_Tank, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_Task, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
		);
		
		// Declaration de trame de requete
		frameUdpRequest frUR = new frameUdpRequest();
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					// recuperation des donnee et creation de la trame udpRequest
					String ip = textField_ipServer.getText();
					String port = textField_listenPort.getText();
					String nameOrHash = textField_File.getText();
					int pos = 0;
					
					// Verification de l'adresse ip
					if(CheckBox_ipV6.isSelected())
					{
						// verification que l'addresse est au format ipv6
						if(ip.matches("([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)") == true)
						{
							frUR.IpType = IPTYPE.IPV6;
							frUR.addr = ip.getBytes();
						}
						else
						{
							textArea_Log.insert("L'adresse saisie est incorrect, veuillez recommencer \n", pos++);
						}
					}
					else
					{
						// verification que l'addresse est au format ipv4
						if(ip.matches("([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\"
								+ ".([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])") == true)
						{
							frUR.IpType = IPTYPE.IPV4;
							frUR.addr = ip.getBytes();
						}
						else
						{
							textArea_Log.insert("L'adresse saisie est incorrect, veuillez recommencer avec le format XXX.XXX.XXX.XXX \n", pos++);
						}
					}
					
					// verfication du numero de port
					if(Integer.parseInt(port) < 65536)
					{
						frUR.port = Integer.parseInt(port);
					}
					else
					{
						textArea_Log.insert("Le port saisie est incorrect, veuillez recommencer avec une valeur inferieur 65536 \n", pos++);
					}
					
					// verification du type de fichier rechercher
					if(CheckBox_hashFile.isSelected())
					{
						frUR.RequestType = REQUESTTYPE.MerkleRequest;
						frUR.nameOrHash = nameOrHash.getBytes();
					}
					else
					{
						frUR.RequestType = REQUESTTYPE.NameRequest;
						frUR.nameOrHash = nameOrHash.getBytes();
					}
				}
			});
		
		panel_Settings.add(btnSend);
		frmBillyProjectV.getContentPane().setLayout(groupLayout);
	}
}
