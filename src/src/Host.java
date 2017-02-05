package src;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class Host implements MyFrame{
	
	private InetAddress ClientAdress;
	private int ClientPortUDP;
	private int ClientPortTCP;
	
	public Host(InetAddress ClientAdress,int ClientPort, int ClientPortTCP)
	{
		this.ClientAdress = ClientAdress;
		this.ClientPortUDP = ClientPortUDP;
		this.ClientPortTCP = ClientPortTCP;
	}


	/**
	 * @author KeviN
	 * @param server
	 * @param port
	 * @param nameFile
	 * Download des fichiers : telechargement des fichiers apres recherche des fichiers
	 */
	public void tcpHost(InetAddress host, int port, String SendFile)
	{
		try {
			// initialisation d'une connexion cote client TCP
			Socket socket = new Socket(host, port);
			System.out.println("Le telechargement est lance !");
			socket.setSoTimeout(5000);
			
			// lecture de donnee en provenance du serveur
			File file = new File(SendFile);
	        byte[] buffer = new byte[8192]; 
			InputStream in = new FileInputStream(file);
	        OutputStream out = socket.getOutputStream();
	        System.out.print("Envoi du fichier en cours ");
	        
	        int count;
	        while ((count = in.read(buffer)) > 0)
	        {
	          out.write(buffer, 0, count);
	          System.out.print(".");
	        }
	        System.out.println("");
	        System.out.println("Envoi termine !");
	        
	        out.close();
	        in.close();
	        socket.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void ResponseUdp(frameUdpResponse Response)
	{

		try {	

			/* initialisation d'une connexion cote client UDP */
			DatagramSocket Host = new DatagramSocket();

			/* la requete est caste au format d'un tableau de byte pour etre envoye par la socket UDP */
			byte[] tBuffer1 = new byte[(MyFrame.CastToByteResponse(Response).length)];
			tBuffer1 = MyFrame.CastToByteResponse(Response);
			DatagramPacket packet1 = new DatagramPacket(tBuffer1, tBuffer1.length, ClientAdress, ClientPortUDP);

			/* affectation des donnees au packet */
			packet1.setData(tBuffer1);

			/* envoi de la reponse */
			Host.send(packet1);
			System.out.println("Un message vient d'etre envoyer !");

			Host.close();

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public void udpHost(frameUdpRequest request)
	{

		try {	

			/* initialisation d'une connexion cote client UDP */
			DatagramSocket Host = new DatagramSocket();

			/* la requete est caste au format d'un tableau de byte pour etre envoye par la socket UDP */
			byte[] tBuffer1 = new byte[(MyFrame.CastToByte(request).length)];
			tBuffer1 = MyFrame.CastToByte(request);
			DatagramPacket packet1 = new DatagramPacket(tBuffer1, tBuffer1.length, ClientAdress, ClientPortUDP);

			/* affectation des donnees au packet */
			packet1.setData(tBuffer1);

			/* envoi de la reponse */
			Host.send(packet1);
			System.out.println("Un message vient d'etre envoyer !");

			Host.close();

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public void ConfirmationUdp(frameUdpConfirmation confirmation)
	{

		try {	

			/* initialisation d'une connexion cote client UDP */
			DatagramSocket Host = new DatagramSocket();

			/* la requete est caste au format d'un tableau de byte pour etre envoye par la socket UDP */
			byte[] tBuffer1 = new byte[(MyFrame.CastToByteConfirmation(confirmation).length)];
			tBuffer1 = MyFrame.CastToByteConfirmation(confirmation);
			DatagramPacket packet1 = new DatagramPacket(tBuffer1, tBuffer1.length, ClientAdress, ClientPortTCP);

			/* affectation des donnees au packet */
			packet1.setData(tBuffer1);

			/* envoi de la reponse */
			Host.send(packet1);
			System.out.println("Un message vient d'etre envoye !");

			Host.close();

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
