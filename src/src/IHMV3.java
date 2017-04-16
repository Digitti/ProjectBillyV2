package src;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;


import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import javax.swing.JTextArea;

import javax.swing.JTable;
import javax.swing.JToggleButton;



public class IHMV3 implements MyFrame {

	private JFrame frmBillyProjectV;
	private JTextField textField_ipDst;
	private JTextField textField_listenUDP;
	private JTextField textField_File;
	private JTextField textField_ipSrc;
	private JTable table_Tank;
	private JTextField textField_listenTCP;
	private JTextField textField_PathDir;
	private JTextField textField_sendUDP;
	private JTextField textField_sendTCP;

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
		
		JPanel panel_file = new JPanel();
		panel_file.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel_2 = new JLabel("IP Source : ");
		panel_file.add(lblNewLabel_2);
		
		textField_ipSrc = new JTextField();
		panel_file.add(textField_ipSrc);
		textField_ipSrc.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("IP Destination :");
		panel_file.add(lblNewLabel);
		
		textField_ipDst = new JTextField();
		panel_file.add(textField_ipDst);
		textField_ipDst.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("UDP Port L :");
		panel_file.add(lblNewLabel_1);
		
		textField_listenUDP = new JTextField();
		panel_file.add(textField_listenUDP);
		textField_listenUDP.setColumns(10);
		
		JLabel lblS = new JLabel("S :");
		panel_file.add(lblS);
		
		textField_sendUDP = new JTextField();
		textField_sendUDP.setColumns(10);
		panel_file.add(textField_sendUDP);
		
		JLabel lblTcpPort = new JLabel("TCP Port L :");
		panel_file.add(lblTcpPort);
		
		textField_listenTCP = new JTextField();
		textField_listenTCP.setColumns(10);
		panel_file.add(textField_listenTCP);
		
		JToggleButton btnSet_Config = new JToggleButton("Set");
		btnSet_Config.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// recuperation des donnee et creation de la trame udpRequest
				String ipSrc = textField_ipSrc.getText();
				String ipDst = textField_ipDst.getText();
				String udpPort = textField_listenUDP.getText();
				String tcpPort = textField_listenTCP.getText();
				String sUdpPort = textField_sendUDP.getText();
				String sTcpPort = textField_sendTCP.getText();
				String pathDir = textField_PathDir.getText();
				
				// UDP
				int listenUdp = Integer.parseInt(udpPort);
				int listenTcp = Integer.parseInt(tcpPort);
				int sendUdp = Integer.parseInt(sUdpPort);
				int sendTcp = Integer.parseInt(sTcpPort);
			
				// set objet mainApplication
				
			}
		});
		
		JLabel lblS_1 = new JLabel("S :");
		panel_file.add(lblS_1);
		
		textField_sendTCP = new JTextField();
		textField_sendTCP.setColumns(10);
		panel_file.add(textField_sendTCP);
		panel_file.add(btnSet_Config);
		JLabel lblLog = new JLabel("Log :");
		
		JLabel lblTank = new JLabel("Tank :");
		
		table_Tank = new JTable();
		
		JTextArea textArea_Log = new JTextArea();
		textArea_Log.setEditable(false);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frmBillyProjectV.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel_file, GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLog)
					.addContainerGap(1015, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(textArea_Log, GroupLayout.DEFAULT_SIZE, 1029, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTank)
					.addContainerGap(1009, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(table_Tank, GroupLayout.DEFAULT_SIZE, 1029, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel_file, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLog)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textArea_Log, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTank)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(table_Tank, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JLabel lblPathDirectory = new JLabel("Path Directory :");
		panel.add(lblPathDirectory);
		
		textField_PathDir = new JTextField();
		panel.add(textField_PathDir);
		textField_PathDir.setColumns(10);
		
		JLabel lblSearchFile = new JLabel("Search File :");
		panel.add(lblSearchFile);
		
		textField_File = new JTextField();
		panel.add(textField_File);
		textField_File.setColumns(10);
		
		JCheckBox CheckBox_hashFile = new JCheckBox("Hash File");
		panel.add(CheckBox_hashFile);
		
		JButton btnSend_File = new JButton("Send");
		panel.add(btnSend_File);
		btnSend_File.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					/*if(CheckBox_ipV6.isSelected())
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
				}*/
				
				
				
			}
		});
		
		// Declaration de trame de requete
		frameUdpRequest frUR = new frameUdpRequest();
		frmBillyProjectV.getContentPane().setLayout(groupLayout);
	}
}
