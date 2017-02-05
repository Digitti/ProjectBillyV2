package src;

import java.math.BigInteger;
import java.util.Arrays;


/**
 * @author KeviN
 * 
 */
public interface MyFrame {
	
	/**
	 * @author KeviN
	 * Definis la trame de la requete
	 */
	class frameUdpRequest
	{
		public OBJECT Object;
		public byte lenght;							// longueur du chemin, soit le nombre de serveur rencontree
		public IPTYPE IpType;						// type d'adresse IPV4 ou IPV6
		public byte[] addr = new byte[16];			// addresse IP
		public int port;							//numero du port
		public REQUESTTYPE RequestType;				// type de requete par nom ou racine
		public byte[] nameOrHash = new byte[16];	// hash ou nom du fichier demander
	}
	
	/**
	 * @author KeviN
	 * Definis la trame de la reponse
	 */
	class frameUdpResponse
	{
		public OBJECT Object;
		public REQUESTTYPE RequestType;				// type de requete par nom ou racine
		public byte[] nameOrHash = new byte[16];	// hash ou nom du fichier demander
		//Reponse
		public int nbrFile;							// nombre de fichier qui constitue le fichier
		public byte[] racineHash = new byte[16];	// hash du fichier racine
		public byte[] nameFile = new byte[16];		// nom du fichier
	}
	
	class frameUdpConfirmation
	{
		public OBJECT Object;
		public REQUESTTYPE RequestType;				// type de requete par nom ou racine
		public byte[] nameFile = new byte[16];
		public IPTYPE IpType;						// type d'adresse IPV4 ou IPV6
		public byte[] addr = new byte[16];			// addresse IP
		public int port;							//numero du port
	}
	/**
	 * @author Arnold
	 * Enum pour definir le type d'adresse IP
	 */
	public enum IPTYPE {
		  IPV4,
		  IPV6;
		}
	
	/**
	 * @author Arnold
	 * Enum pour definir l'objet de la recherche par empreinte ou nom de fichier
	 */
	public enum REQUESTTYPE {
		  MerkleRequest,
		  NameRequest;
		}
	
	public enum OBJECT {
		  Request,
		  response,
		  DownloadConfirmation;
		}	
	
	/**
	 * @author Arnold
	 * M�thode pour caster du type frameUdpRequest-->byte[]
	 */
	public static byte[] CastToByte(frameUdpRequest TransmitRequest){
		
		if (TransmitRequest.Object == OBJECT.Request){
			
		if (TransmitRequest.IpType == IPTYPE.IPV4){
			
			byte[] tBuffer = new byte[10+TransmitRequest.nameOrHash.length]; 
			
			tBuffer [1] = TransmitRequest.lenght;
			tBuffer[2] = 0;
			
			for (int i=0;i<4;i++){
				tBuffer[3+i] = TransmitRequest.addr[i];
			}
			
			/* decoupe de l'entier sur 2 bytes 
			 * !!!!!!! ne pas d�passer 65535 comme num�ro de port 
			 * sinon augmenter le nombre de byte pour stocker */
			tBuffer[7] = (byte) TransmitRequest.port;
			tBuffer[8] = (byte) (TransmitRequest.port >> 8);
			
			if (TransmitRequest.RequestType == REQUESTTYPE.MerkleRequest){
				tBuffer[9] = 0;
				
			}
			else{
				tBuffer[9] = 1;
			}
			for (int i=0;i<TransmitRequest.nameOrHash.length;i++){
				tBuffer[10+i] = TransmitRequest.nameOrHash[i];
			}
			
			return tBuffer;
		}
		else{
			
			byte[] tBuffer = new byte[22+TransmitRequest.nameOrHash.length]; 
			
			tBuffer [1] = TransmitRequest.lenght;
			tBuffer[2] = 0;
			
			for (int i=0;i<16;i++){
				tBuffer[3+i] = TransmitRequest.addr[i];
			}
			
			/* decoupe de l'entier sur 2 bytes 
			 * !!!!!!! ne pas depasser 65535 comme num�ro de port 
			 * sinon augmenter le nombre de byte pour stocker */
			tBuffer[19] = (byte) TransmitRequest.port;
			tBuffer[20] = (byte) (TransmitRequest.port >> 8);
			
			if (TransmitRequest.RequestType == REQUESTTYPE.MerkleRequest){
				tBuffer[21] = 0;
			}
			else{
				tBuffer[21] = 1;
			}
			
			for (int i=0;i<16;i++){
				tBuffer[22+i] = TransmitRequest.nameOrHash[i];
			}
			
			return tBuffer;
		}
		
		}
		return null;
	}
	
	/**
	 * @author Arnold
	 * M�thode pour caster byte[]-->frameUdpRequest
	 */
	public static frameUdpRequest CastToframeUdpRequest(byte[] rBuffer){
		
		
		frameUdpRequest ReceiveRequest = new frameUdpRequest();
		
		ReceiveRequest.lenght = rBuffer[0];
		
		if (rBuffer[1] == 0){
			ReceiveRequest.IpType = IPTYPE.IPV4;
			
			for (int i=0;i<4;i++){
				ReceiveRequest.addr[i] = rBuffer[i+2];
			}
			
			ReceiveRequest.port  = (rBuffer[7] <<8)&0xff00|(rBuffer[6]<< 0)&0x00ff ;
			
			if (rBuffer[8] == 0){
				ReceiveRequest.RequestType = REQUESTTYPE.MerkleRequest;
			}
			else{
				ReceiveRequest.RequestType = REQUESTTYPE.NameRequest;
			}
			
			for (int i=0;i<(rBuffer.length-9);i++){
				ReceiveRequest.nameOrHash[i] = rBuffer[i+9];
			}	
		}
		else{
			ReceiveRequest.IpType = IPTYPE.IPV6;
			
			for (int i=0;i<16;i++){
				ReceiveRequest.addr[i] = rBuffer[i+2];
			}
			ReceiveRequest.port  = (rBuffer[19] <<8)&0xff00|(rBuffer[18]<< 0)&0x00ff ;
			
			if (rBuffer[20] == 0){
				ReceiveRequest.RequestType = REQUESTTYPE.MerkleRequest;
			}
			else{
				ReceiveRequest.RequestType = REQUESTTYPE.NameRequest;
			}
			
			for (int i=0;i<(rBuffer.length-21);i++){
				ReceiveRequest.nameOrHash[i] = rBuffer[i+21];
			}
			
		}
		
		return ReceiveRequest;	
	}	

	/**
	 * @author Arnold
	 * Methode pour caster frameUdpRequest-->byte[]
	 */
	public static byte[] CastToByteResponse(frameUdpResponse TransmitRequest){
		
		byte[] tBuffer = new byte[36];
		
		if (TransmitRequest.Object == OBJECT.response){
			tBuffer[0] = 1;
		}
		else if(TransmitRequest.Object == OBJECT.DownloadConfirmation){
			tBuffer[0] = 2;
		}
			
		if (TransmitRequest.RequestType == REQUESTTYPE.NameRequest){
			tBuffer[1] = 1;
			
			for (int i=0;i<16;i++){
				tBuffer[2+i] = TransmitRequest.nameOrHash[i];
			}
			
			/* deccoupe de l'entier sur 2 bytes 
			* !!!!!!! ne pas depasser 65535 comme num�ro de port 
			* sinon augmenter le nombre de byte pour stocker */
			tBuffer[18] = (byte) TransmitRequest.nbrFile;
			tBuffer[19] = (byte) (TransmitRequest.nbrFile >> 8);	
			
			for (int i=0;i<16;i++){
				tBuffer[20+i] = TransmitRequest.racineHash[i];
			}
			
			for (int i=0;i<16;i++){
				tBuffer[36+i] = TransmitRequest.nameFile[i];
			}
			
		}
		else{
			
			tBuffer[1] = 0;
		}
		
		return tBuffer;
	}
	
	/**
	 * @author Arnold
	 * Methode pour caster byte[]-->frameUdpRequest
	 */
	public static frameUdpResponse CastToframeUdpResponse(byte[] TransmitResponse){
		
		frameUdpResponse Response = new frameUdpResponse();
		
		if (TransmitResponse[1] == 0 ){
			Response.RequestType = REQUESTTYPE.MerkleRequest;
		}
		else{
			Response.RequestType = REQUESTTYPE.NameRequest;
		}
		
		for (int i=0;i<16;i++){
			Response.nameOrHash[i] = TransmitResponse[i+2];
		}
		
		Response.nbrFile  = (TransmitResponse[19] <<8)&0xff00|(TransmitResponse[18]<< 0)&0x00ff ;
		
		for (int i=0;i<16;i++){
			Response.racineHash[i] = TransmitResponse[i+20];
		}
		
		for (int i=0;i<16;i++){
			Response.nameFile[i] = TransmitResponse[i+36];
		}
		
		return Response;
	}
	
	public static byte[] CastToByteConfirmation(frameUdpConfirmation TransmitConfirmation){
		
		byte[] tBuffer = new byte[25];
		
		if (TransmitConfirmation.Object == OBJECT.DownloadConfirmation){
			tBuffer[0] = 1;
		}
		
		if (TransmitConfirmation.RequestType == REQUESTTYPE.NameRequest){
			tBuffer[1] = 1;
		
			
			for (int i=0;i<16;i++){
				tBuffer[2+i] = TransmitConfirmation.nameFile[i];
			}
				
			if (TransmitConfirmation.IpType == IPTYPE.IPV4 ){
				tBuffer[18] = 1;
			}
			
			for (int i=0;i<4;i++){
				tBuffer[19+i] = TransmitConfirmation.addr[i];
			}
			
			tBuffer[24] = (byte) TransmitConfirmation.port;
			tBuffer[25] = (byte) (TransmitConfirmation.port >> 8);
			
		}
		else{
			
			tBuffer[1] = 0;
		}
		
		return tBuffer;
	}
	
	public static frameUdpConfirmation CastToframeUdpConfirmation(byte[] TransmitConfirmation){
		
		frameUdpConfirmation Confirmation = new frameUdpConfirmation();
		
		Confirmation.Object = OBJECT.DownloadConfirmation;
		
		if (TransmitConfirmation[1] == 0 ){
			Confirmation.RequestType = REQUESTTYPE.MerkleRequest;
		}
		else{
			Confirmation.RequestType = REQUESTTYPE.NameRequest;
		}
		
		for (int i=0;i<16;i++){
			Confirmation.nameFile[i] = TransmitConfirmation[i+2];
		}
		
		if (TransmitConfirmation[18] == 0){
			Confirmation.IpType = IPTYPE.IPV4;
		}
		
		 
		
		for (int i=0;i<4;i++){
			Confirmation.addr[i] = TransmitConfirmation[i+19];
		}
		
		Confirmation.port = (TransmitConfirmation[21] <<8)&0xff00|(TransmitConfirmation[20]<< 0)&0x00ff ;
		
		return Confirmation;
		
	}	
}


