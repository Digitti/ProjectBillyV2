package src;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MainApplication implements MyFrame {

	private static boolean RequestReceptionFlag;
	private static boolean ResponseReceptionFlag;
	private static boolean ConfirmationReceptionFlag;
	private static boolean UserRequestFlag;
	
	private String dir;
	private InetAddress LocalAdress;
	private int LocalportUDP;
	private int LocalportTCP;
	
	private InetAddress ClientAdress;
	private int ClientPortUDP;
	private int ClientPortTCP;
	
	private static String Filename;
	
	/**
	 * Main Cote serveur
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {}

	public MainApplication (String dir, String LocalAdress, int LocalportUDP, int LocalportTCP, String ClientAdress, int ClientPortUDP,  int ClientPortTCP ) throws UnknownHostException
	{
		this.dir = dir;
		
		this.LocalAdress =  InetAddress.getByName(LocalAdress);
		this.LocalportUDP = LocalportUDP;
		this.LocalportTCP = LocalportTCP;
		
		this.ClientAdress = InetAddress.getByName(LocalAdress);
		this.ClientPortUDP = ClientPortUDP;
		this.ClientPortTCP = ClientPortTCP;
	}
	
		public void LancementRoutine () throws NoSuchAlgorithmException, IOException {

		/* Liste des objets utiles a l'application */
		Tank t = new Tank(dir);
		Server s = new Server(LocalAdress,LocalportUDP,LocalportTCP);
		Host h = new Host(ClientAdress,ClientPortUDP, ClientPortTCP);
		//IHM i = new IHM();
		
		/* lancement du reservoir de fichier */
		t.initialiseTank();

		/* lancement de la socket pour l ecoute dans un thread */ 
		Thread threadUdpServer = new Thread() {

			public void run() {

				s.udpServer ();
			}
		};
		threadUdpServer.start();
		
		 RequestReceptionFlag = false;
		 ResponseReceptionFlag = false;
		 ConfirmationReceptionFlag = false;
		 UserRequestFlag = false;
		 
		 System.out.println("Lancement logiciel");

		/* ---------------------------------- routine principale du logiciel Billy ---------------------------------- */
		while (true){
			
			/* ---------------------------------- Reception d une commande utilisateur ---------------------------------- */
			if (UserRequestFlag == true){

				UserRequestFlag = false;
				
				System.out.println("Requete recu");

				/* renseignement de la requete */
				frameUdpRequest Request = new frameUdpRequest();
				
				//Request = i.GetRequestReception();

				Request.RequestType = REQUESTTYPE.NameRequest;		/* pour le moment on choisis une requete de type name */

				/* encodage de nom de fichier en UTF-8 */
				byte[] encodedHfWithUTF8 = null;
				try {
					encodedHfWithUTF8 = Filename.getBytes("UTF-8");
	
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Request.nameOrHash = encodedHfWithUTF8;	/* pour le moment on desire telecharger le fichier bd */
				Request.lenght = 1;
				Request.IpType = IPTYPE.IPV4;
				Request.addr = LocalAdress.getAddress(); // 127.0.0.1
				Request.port = ClientPortUDP;

				/* On transmet la requete */
				h.udpHost(Request);

			}

			/* ---------------------------------- Reception d une requete ---------------------------------- */
			if (RequestReceptionFlag == true)
			{
				RequestReceptionFlag = false;				

				/* Traitement de la requete UDP */
				frameUdpRequest Request = new frameUdpRequest();
				Request = MyFrame.CastToframeUdpRequest(s.GetReceiveBuffer());

				
				if (Request.RequestType == REQUESTTYPE.NameRequest){

					/* decodage de nom via le codage UTF-8 */
					String decodedHfName = new String(Request.nameOrHash, 0, Request.nameOrHash.length, "UTF-8");

					/* test l'existence d'un dossier portant le meme nom quedans la requete */		
					/* verification positive : on prepare la reponse positive et le telechargement
					 * verification negative : on prepare la reponse negative */
					if (t.FileNameTestEquals(decodedHfName) == true){

						/* on instancie et remplit l'objet/trame de reponse */
						frameUdpResponse Response = new frameUdpResponse();

						Response.Object = OBJECT.response;
						Response.RequestType = REQUESTTYPE.NameRequest;

						byte[] encodedHfWithUTF8 = null;
						try {
							encodedHfWithUTF8 = decodedHfName.getBytes("UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Response.nameOrHash = encodedHfWithUTF8;
						Response.nbrFile = t.GetNumberOfFiles(decodedHfName);
						Response.racineHash = t.getHashFile(decodedHfName);
						Response.nameFile = encodedHfWithUTF8;

						
						/* envoi le reponse UDP */
						h.ResponseUdp(Response);
					}
					else{
						/* le fichier n'est pas repertorie dans le reservoir de fichier 
						 * on transmet donc la requete a un autre pair
						 */
					}
				}
				else if (Request.RequestType == REQUESTTYPE.MerkleRequest){
					
					/* Traitement de la requete UDP */
				}
			}
			
			/* traiter la reponse 
			 * test le hash code du reservoir et celui recu
			 * 		si le hash recu est bon, on envoi la requete initialise a  un autre pair
			 * 		si le hash recu n'est pas Ã©gal au notre on demande de lancer le telechargement
			 * */
			
			/* ---------------------------------- Reception d une Reponse ---------------------------------- */
			if (ResponseReceptionFlag == true){
				
				ResponseReceptionFlag = false;
				
				frameUdpResponse Response = new frameUdpResponse();
				Response = MyFrame.CastToframeUdpResponse(s.GetReceiveBuffer());
				
				if (t.HashTestEquals(Response.racineHash, new String(Response.nameFile, 0, Response.nameFile.length, "UTF-8")) == true){
					/* transmet la requete initiale a un autre pair */
				}	
				else{
					/* On informe le pair que l'on souhaite procéder au téléchargement */
					frameUdpConfirmation Confirmation = new frameUdpConfirmation();
					
					Confirmation.Object = OBJECT.DownloadConfirmation;
					Confirmation.RequestType = REQUESTTYPE.NameRequest;
					Confirmation.nameFile = Response.nameFile;
					Confirmation.IpType = IPTYPE.IPV4;
					Confirmation.addr = s.GetLocalAdress();
					Confirmation.port = s.GetLocalportTCP();
									
					/* envoi la reponse confirmation */
					h.ConfirmationUdp(Confirmation);
					
					/* ecoute du port tcp afin de receptionner le fichier */
					s.tcpServer(Response.nameFile.toString());
				}
			}
			
			/* ---------------------------------- Reception d une confirmation ---------------------------------- */
			if (ConfirmationReceptionFlag == true){
				
				ConfirmationReceptionFlag = false;
				
				frameUdpConfirmation confirmation = new frameUdpConfirmation();
				confirmation = MyFrame.CastToframeUdpConfirmation(s.GetReceiveBuffer());
				
				/* decodage de nom via le codage UTF-8 */
				String decodedHfName = new String(confirmation.nameFile, 0, confirmation.nameFile.length, "UTF-8");
				
				/* on lance l envoi du fichier en mode tcp */		
				h.tcpHost(InetAddress.getByAddress(confirmation.addr), confirmation.port, t.getFileAsked(decodedHfName));
				
			}	
		}	
	}

	static public void SetRequestReceptionFlag (){
		RequestReceptionFlag = true;
	}

	static public void SetResponseReceptionFlag (){
		ResponseReceptionFlag = true;
	}
	
	static public void SetConfirmationReceptionFlag (){
		ConfirmationReceptionFlag = true;
	}

	static public void SetUserRequestFlag (){
		UserRequestFlag = true;
	}

	static public void SetUserNameRequest (String name){
		Filename = name ;
	}
}
