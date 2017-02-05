package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server implements MyFrame {

	private byte [] BufferReception ;
	private InetAddress LocalAdress;
	private int LocalportUDP;
	private int LocalportTCP;
	MainApplication i;
	
	public Server(InetAddress LocalAdress, int LocalportUDP, int LocalportTCP)
	{
		this.LocalAdress = LocalAdress;
		this.LocalportUDP = LocalportUDP;
		this.LocalportTCP = LocalportTCP;
		i = new MainApplication ();
	}
	
	/**
	 * @author KeviN
	 * @param port
	 * @param sendFile
	 * Telechargement des fichiers : Envoie des fichier apres la recherche des fichiers
	 */
	public void tcpServer(String receiveFile)
	{
		try {
			// creation de la connexion serveur TCP
			ServerSocket serverSocket = new ServerSocket(LocalportTCP);
			serverSocket.setSoTimeout(50000);
			System.out.println("En attente du telechargement ... !");
			
			// accepte une connexion avec le client
			Socket socket = serverSocket.accept();
			System.out.println("connection !");
			
			// ecriture de donnee a destination du client 
			InputStream in = socket.getInputStream();
			
			// fichier a envoye
			OutputStream out =  new FileOutputStream(receiveFile);
			System.out.print("Reception du fichier ... ");
			
			byte[] BufferReception = new byte[8192];
			
			int count;
			while ((count = in.read(BufferReception)) > 0)
			{
			  out.write(BufferReception, 0, count);
			  System.out.print(".");
			}
			System.out.println("");
			System.out.println("Reception termine !");
			
			out.close();
			in.close();
			socket.close();
			serverSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void udpServer ()
	{
		try {
			/* Creation de la socket utilise pour envoyer les donnees au serveur */
			DatagramSocket server = new DatagramSocket(LocalportUDP, LocalAdress);
			System.out.println("La socket d ecoute est lancee");
			
			/* ecoute du port a l infini */
			while(true)
			{
				// packet pour recuperer la requete client 
				byte [] Buffer =  new byte [8196];
				DatagramPacket packet = new DatagramPacket(Buffer, Buffer.length);
				
				// recuperation du packet
				server.receive(packet);
				
				byte [] receiveBuffer =  new byte [packet.getLength()];
				
				for (int i=0; i < packet.getLength(); i++){
					receiveBuffer[i] = packet.getData()[i];
				}
				
				if (receiveBuffer[0] == 0 )
				{
					SetReceiveBuffer(receiveBuffer);	
					i.SetRequestReceptionFlag();
				}
				else if (receiveBuffer[0] == 1 ){
					
					SetReceiveBuffer(receiveBuffer);	
					i.SetResponseReceptionFlag();				
				}
				else if (receiveBuffer[0] == 2 ){
					
					SetReceiveBuffer(receiveBuffer);	
					i.SetConfirmationReceptionFlag();				
				}
			}
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SetReceiveBuffer (byte [] receiveBuffer){
		this.BufferReception = receiveBuffer;
	}
	
	public byte [] GetReceiveBuffer (){
		return this.BufferReception;
	}
	
	public byte [] GetLocalAdress (){
		return this.LocalAdress.getAddress();
	}
	
	public int GetLocalportTCP (){
		return this.LocalportTCP;
	}	
	


}
