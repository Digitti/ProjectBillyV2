package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.SwingConstants;


public class IHM extends JFrame implements MyFrame {

	private JPanel contentPane;
	
	private JTextField tf_ip;
	private JLabel lblPort;
	private JTextField tf_port;
	private JButton btn_listen;
	private JCheckBox chckbxIpv;
	private JButton btn_cancel;
	
	private boolean portIsOk = false;
	private boolean ipIsOk = false;
	private boolean isIpV6 = false;
	private JTextField tf_name;
	private JTextField tf_print;
	
	private frameUdpRequest requeteUDP;
	

	public IHM(){
		
	}


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IHM i = new IHM(); 
					i.LancementInterface();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @return 
	 */
	public void LancementInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, 700, 700);
		setTitle("Billy PeerToPeer");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Rajouter cette ligne pour tester avec un serveur en meme temps
		//MyJFrameServeur mjs = new MyJFrameServeur();
		
		MainApplication m = new MainApplication();
		
		/*
		 * Ajout de tout les composants
		 */
		JLabel lblip = new JLabel("@IP : ");
		lblip.setBounds(10, 11, 46, 14);
		contentPane.add(lblip);
		
		tf_ip = new JTextField();
		tf_ip.setText("25.105.181.150");
		tf_ip.setBounds(51, 8, 143, 20);
		contentPane.add(tf_ip);
		tf_ip.setColumns(10);
		
		lblPort = new JLabel("Port : ");
		lblPort.setBounds(10, 36, 41, 14);
		contentPane.add(lblPort);
		
		tf_port = new JTextField();
		tf_port.setBounds(51, 33, 60, 20);
		contentPane.add(tf_port);
		tf_port.setColumns(10);
		
		btn_listen = new JButton("SEND");
		btn_listen.setBackground(new Color(0, 255, 102));
		btn_listen.setBounds(264, 11, 86, 33);
		contentPane.add(btn_listen);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 91, 414, 2);
		contentPane.add(separator_1);
		
		JLabel lblDownload = new JLabel("Download");
		lblDownload.setBounds(10, 98, 67, 14);
		contentPane.add(lblDownload);
		
		chckbxIpv = new JCheckBox("IPv6");
		chckbxIpv.setBounds(201, 7, 71, 23);
		contentPane.add(chckbxIpv);
		
		JLabel lblViaNom = new JLabel("Via name :");
		lblViaNom.setBounds(10, 66, 60, 14);
		contentPane.add(lblViaNom);
		
		tf_name = new JTextField();
		tf_name.setBounds(69, 64, 125, 20);
		contentPane.add(tf_name);
		tf_name.setColumns(10);
		
		JLabel lblViaPrint = new JLabel("Via Print :");
		lblViaPrint.setBounds(208, 66, 46, 14);
		contentPane.add(lblViaPrint);
		
		tf_print = new JTextField();
		tf_print.setBounds(264, 63, 152, 20);
		contentPane.add(tf_print);
		tf_print.setColumns(10);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setBackground(new Color(255, 51, 0));
		btn_cancel.setBounds(353, 11, 71, 33);
		contentPane.add(btn_cancel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 263, 414, 2);
		contentPane.add(separator);
		
		JLabel lblLogs = new JLabel("Logs");
		lblLogs.setBounds(10, 270, 46, 14);
		contentPane.add(lblLogs);
		
		JLabel lb_logs = new JLabel("..");
		lb_logs.setBackground(Color.WHITE);
		lb_logs.setFont(new Font("Arial", Font.ITALIC, 10));
		lb_logs.setVerticalAlignment(SwingConstants.TOP);
		lb_logs.setBounds(10, 287, 414, 63);
		contentPane.add(lb_logs);
		
		setResizable(true);
		
		
		
		setVisible(true);
		
		
		
		
		// Listener du field PORT (verifie que l'entre est correcte)
		tf_port.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				//warn();
			}
			public void removeUpdate(DocumentEvent e) {
				warn();
			}
			public void insertUpdate(DocumentEvent e) {
				warn();
			}
	
			public void warn() {
				if ((!tf_port.getText().equals(""))&&(tf_port.getText().matches("[0-9]+"))) {
					if ((Integer.parseInt(tf_port.getText())<=0) | (Integer.parseInt(tf_port.getText())>63000)){
						tf_port.setBackground(Color.RED);
						portIsOk = false;
					} else {
						tf_port.setBackground(Color.WHITE);
						portIsOk = true;
					}
				} else {
					tf_port.setBackground(Color.RED);
					portIsOk = false;
				}
			}
		});
		
		
		
		//Listener du field IP (verifie que la saisie est bien de type @ip)
		tf_ip.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				//warn();
			}
			public void removeUpdate(DocumentEvent e) {
				warn();
			}
			public void insertUpdate(DocumentEvent e) {
				warn();
			}
			
			public void warn() {
				try {
					if ((tf_ip.getText().length()>6) && isIpV6 && (InetAddress.getByName(tf_ip.getText()) instanceof Inet6Address)) {
						ipIsOk = true;
						tf_ip.setBackground(Color.WHITE);
					} else if ((tf_ip.getText().length()>6) && (!isIpV6) && (InetAddress.getByName(tf_ip.getText()) instanceof Inet4Address)) {
						ipIsOk = true;
						tf_ip.setBackground(Color.WHITE);
					} else {
						ipIsOk = false;
						tf_ip.setBackground(Color.RED);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
					ipIsOk = false;
					tf_ip.setBackground(Color.RED);
				}
			}
		});
		
		
		//Listener du checkBox IPvX
		chckbxIpv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				isIpV6 = chckbxIpv.isSelected();
			}
		});
		
		
		
		
		//Listener du bouton SEND
		btn_listen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				setResizable(true);
				
				//Test des entrees port et ip ok
				if (portIsOk && ipIsOk) {
					
					//Test texte dans name
					if (tf_name.getText()!=null && (tf_name.getText()!="")) { //Si ok, lancement requette avec nom
						
						System.out.println("Recherche via name");
						
						/* renseignement de la requête */
						frameUdpRequest Request = new frameUdpRequest();
						
						Request.RequestType = REQUESTTYPE.NameRequest; //Request via name
						
						try {
							Request.nameOrHash = tf_name.getText().getBytes("UTF-8");
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						};
						Request.lenght = 1;
						Request.IpType = IPTYPE.IPV4;
						
						//Transformation de l'adresse en byte, pas de prise en charge de l'ipv6 ici
						//byte[] addr_in_bytes = new byte[4];
						//for (int i =0; i<4; i++) {
						//	int o = Integer.valueOf(tf_ip.getText().split("\\.")[i]);
						//	addr_in_bytes[i] = (byte) o;
						//}
						//Request.addr = addr_in_bytes;
						Request.addr = tf_ip.getText().getBytes();
						Request.port = Integer.valueOf(tf_port.getText());
						
						//Securite/*
						/*if (t!=null) {
							t.stop();
						}
						/* On transmet la requête *//*
						t = new Thread(new Runnable() {
							
							@Override
							public void run() {

								host.udpHost(Request);
							}
						});
						t.start();*/
						m.SetUserRequestFlag();
						lb_logs.setText(lb_logs.getText()+"\nSearch for "+tf_name.getText()+" with "+tf_ip.getText()+"-"+tf_port.getText()+"\n");
						
						
						
					//Sinon test texte dans print
					} else if (tf_print.getText()!=null && (tf_print.getText()!="")) {
						
						System.out.println("Recherche via empreinte");
						
						/* renseignement de la requête */
						frameUdpRequest Request = new frameUdpRequest();
						
						Request.RequestType = REQUESTTYPE.MerkleRequest; //Request via name
						
						try {
							Request.nameOrHash = tf_print.getText().getBytes("UTF-8");
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						};	/* pour le moment on désire télécharger le fichier bd */
						Request.lenght = 1;
						Request.IpType = IPTYPE.IPV4;
						
						//Transformation de l'adresse en byte, pas de prise en charge de l'ipv6 ici
						byte[] addr_in_bytes = new byte[4];
						for (int i =0; i<4; i++) {
							int o = Integer.valueOf(tf_ip.getText().split("\\.")[i]);
							addr_in_bytes[i] = (byte) o;
						}
						Request.addr = addr_in_bytes;
						Request.port = Integer.valueOf(tf_port.getText());
						
						/*
						if (t!=null) {
							t.stop();
						}
						/* On transmet la requête *//*
						t = new Thread(new Runnable() {
							
							@Override
							public void run() {

								host.udpHost(Request);
							}
						});
						t.start();*/
						m.SetUserRequestFlag();
						lb_logs.setText("Search for "+tf_print.getText()+" with "+tf_ip.getText()+"-"+tf_port.getText());
						
					//Sinon erreur
					} else {
						System.out.println("Champs name et print vides");
					}
					
				} else {
					System.out.print("Wrong config");
				}
			}
		});
		
		
		//Bouton cancel, pratique lors d'envoi de message en boucle pendant les tests
		btn_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			/*
			public void actionPerformed(ActionEvent e) {
				if (t!=null) {
					t.stop();
				}
			}*/
		});
	}
	
	public frameUdpRequest GetRequestReception (){
		return this.requeteUDP ;
	}
}
