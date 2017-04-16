package src;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Tank {

	/**
	 * @author KeviN
	 * declaration des tableaux contenant les diff�rentes ressources utiles (r�servoir de fichier)   
	 */
	private static ArrayList<String> arrayPathDirectory = new ArrayList<String>();
	private static ArrayList<String> arrayPathSplitFile = new ArrayList<String>();
	private static ArrayList<String> arrayPathSplitHash = new ArrayList<String>();
	private static ArrayList<String> arrayPathFlatTree = new ArrayList<String>();

	private String mainDirectory;

	public Tank(String dir) {
		mainDirectory = dir;
	}

	public Tank() {

	}

	/**
	 * @author KeviN
	 * @param mainDirectory
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * Methode appeler pour realiser les dossier des differentes etapes Split, Hash et FlatTree
	 */

	public void initialiseTank() throws IOException, NoSuchAlgorithmException
	{
		String pathFlatTree;
		//String pathInput = mainDirectory;
		System.out.println("Input Directory : " + mainDirectory );
		performPath(arrayPathDirectory, mainDirectory);

		/**
		 *  Creation du dossier part en fonction du dossier d'entr�e 
		 */
		File pathFolder = new File(mainDirectory);
		String pathParent = pathFolder.getParent();
		File pathOutput = new File(pathParent+getOperatinSystem()+pathFolder.getName()+"Split");
		String pathSplit = pathOutput.getPath();

		if(pathOutput.mkdir())
		{
			System.out.println("Out Split Directory : " + pathSplit );
		}
		else
		{
			System.out.println("Output directory already exists");
		}

		/**
		 *  Cr�ation du dossier contenant les empreintes des morceaux de fichiers
		 */
		File pathOutputHash = new File(pathParent+getOperatinSystem()+pathFolder.getName()+"Hash");	
		String pathHash = pathOutputHash.getPath();

		if(pathOutputHash.mkdir())
		{
			System.out.println("Out Hash Directory : " + pathHash );
		}
		else
		{
			System.out.println("Output directory already exists");
		}

		/**
		 *  Split les documents present dans le dossier 
		 */
		performSplit(arrayPathDirectory, pathSplit);

		/**
		 * Affiche le contenu dans le dossier des fichier splitter
		 */
		performPath(arrayPathSplitFile, pathSplit);

		/**
		 * Genere le hash des fichiers splitter
		 */		
		performHash(arrayPathSplitFile, pathHash);

		/**
		 * Affiche le contenu dans le dossier des fichier splitter
		 */
		performPath(arrayPathSplitHash, pathHash);

		/*MerkleTree ArbreDeMerkle = new MerkleTree(arrayPathSplitHash, args);
		ArbreDeMerkle.CreationDossierMerkle();
		ArbreDeMerkle.CreationArbreMerkle();*/

		/**
		 * realisation de flatTree
		 */
		FlatTree arbreFlatTree = new FlatTree(arrayPathSplitHash, mainDirectory);
		pathFlatTree = arbreFlatTree.CreationDossierPlatTree();
		arbreFlatTree.CreationFlatTree();
		performPath(arrayPathFlatTree, pathFlatTree);
	}


	/**
	 * @author KeviN
	 * @param ListeCheminFichiers
	 * @param path
	 */
	private static void performPath(ArrayList<String> ListeCheminFichiers, String path)
	{
		File directory = new File(path);

		/* verifie si le chemin de fichier existe */
		if(directory.exists())
		{
			/* verifie si ce shemin est un dossier */
			if(directory.isDirectory())
			{
				System.out.println("Ce dossier contient :");

				/* affiche tout les �l�ments du dossier */
				for(File file : directory.listFiles())
				{
					System.out.println(file.getAbsolutePath());
					ListeCheminFichiers.add(file.getPath());
				}
				System.out.println("Ce dossier contient : " + ListeCheminFichiers.size() + " fichiers" );
			}
			else
			{
				/* ce n'est pas un dossier mais un fichier  */
				ListeCheminFichiers.add(directory.getPath());
			}
		}
	}


	/**
	 * @author KeviN
	 * @param file
	 * @param index
	 * @param outputDirectory
	 * @throws IOException
	 */
	private static void performSplit(ArrayList<String> file, String pathSplit) throws IOException
	{
		for(int index = 0; index < file.size(); index++)
		{
			FileInputStream fis;
			FileOutputStream fos;
			int cptSplit = 0;
			File f = new File(file.get(index));
			long lengthFile = f.length();
			System.out.println("Le fichier "+f.getName()+" est de taille : "+ lengthFile + " octets");

			//Cree un dossier pour les split du fichier selectionner
			File splitFolder = new File(pathSplit+getOperatinSystem()+f.getName());
			String pathSplitFile = splitFolder.getPath();

			if(splitFolder.mkdir())
			{
				System.out.println("Out Directory : " + pathSplit );
			}
			else
			{
				System.out.println("Output directory already exists");
			}

			//Decoupe le fichier

			if(lengthFile > 4096)
			{
				fis = new FileInputStream(f);

				/* correction du decoupage de fichier : a tester */
				long Rest = lengthFile % 4096;

				while (Rest > 0){
					
					byte[] buffer = new byte[4096];
					fis.read(buffer, 0, 4096);
					fos = new FileOutputStream(pathSplitFile+getOperatinSystem()+f.getName()+".split"+cptSplit);
					fos.write(buffer);
					fos.flush();
					cptSplit++;
					
					lengthFile = lengthFile - 4096;
					Rest = lengthFile % 4096;
				}
				/*
				for(int j = 0; j < (lengthFile/4096); j++)
				{
					byte[] buffer = new byte[4096];
					fis.read(buffer, 0, 4096);
					fos = new FileOutputStream(pathSplitFile+getOperatinSystem()+f.getName()+".split"+cptSplit);
					fos.write(buffer);
					fos.flush();
					cptSplit++;
				}
				 */
				System.out.println("Il a ete divise en " + cptSplit + " parties");
				System.out.println();

			}
			else
			{
				System.out.println("Il n'a pas ete divise ");
				System.out.println();		
			}
		}
	}


	/**
	 * @author Arnold 
	 * @param file
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	protected static void performHash(ArrayList<String> ListeDossierFichierSplit, String pathHash) throws NoSuchAlgorithmException, IOException
	{
		int NumeroFichierHash =0;

		/* Il s'agit dans cette partie de parcourir l'ensemble des fichiers splits du dossier split pour calculer les hashs de chaque fichiers */
		for(int i = 0; i < ListeDossierFichierSplit.size(); i++)
		{
			File DossierCourant = new File(ListeDossierFichierSplit.get(i));

			/* creation du dossier contenant les empreintes des morceaux d'un m�me fichiers */
			File DossierHashCourant = new File(pathHash+getOperatinSystem()+DossierCourant.getName());

			if(DossierHashCourant.mkdir())
			{
				System.out.println("Out Directory : " + pathHash );
			}
			else
			{
				System.out.println("Output directory already exists");
			}			

			/* On parcours le dossier courant de fichier split */
			for(File FichierSplitcourant : DossierCourant.listFiles())
			{

				/* correction : nouvelle methode pour hacher 
				 * 
				 * appel au constructeur de la classe DigestOutputStream 
				 * possède un seul constructeur qui attend en paramètre 
				 * une instance de type OutputStream et une instance de type 
				 * MessageDigest.L'ecriture des données dans le flux doit se 
				 * faire en utilisant une des surcharges de la méthode write() 
				 * de la classe DigestOutputStream.
				 */
				InputStream is;
				DigestInputStream dis = null;
				FileOutputStream HashCourant = new FileOutputStream(DossierHashCourant.getAbsolutePath()+getOperatinSystem()+DossierHashCourant.getName()+NumeroFichierHash+".hash"+NumeroFichierHash)	;	

				MessageDigest md = MessageDigest.getInstance("SHA-512");

				is = new BufferedInputStream(new FileInputStream(FichierSplitcourant));
				dis = new DigestInputStream(is, md);

				byte[] bytSHA = new byte[64];
				while (dis.read(bytSHA) != -1);

				HashCourant.write(bytSHA);

				/* mis a jour des flux d'entres et de sortie *//*
				FileInputStream FichierSplitCourant = new FileInputStream(FichierSplitcourant);
				String NomFichierSplitCourantRaccourcis = FichierSplitcourant.getName().substring(0, FichierSplitcourant.getName().length()-7);
				FileOutputStream HashCourant = new FileOutputStream(DossierHashCourant.getAbsolutePath()+getOperatinSystem()+NomFichierSplitCourantRaccourcis+".hash"+NumeroFichierHash); 

				/* Bloc realisant le hash de chaque morceaux (l'entree a hasher est renseigne et mis a jour par la variable : FichierSplitCourant ) *//*
				String NomFichierSplitCourant = FichierSplitCourant.toString();
				MessageDigest md = MessageDigest.getInstance("SHA-512");
				byte[] bytSHA = md.digest(NomFichierSplitCourant.getBytes());
				HashCourant.write(bytSHA);

				/* la creation de hash est terminze. le numzro de hash est mis a jour, les flux de sortie et d'entrze sont refermes *//*
				NumeroFichierHash ++;
				FichierSplitCourant.close();
				HashCourant.close();

				System.out.println("le hash genere est : "+bytSHA);
				 */
			}	
			//NumeroFichierHash =0;
		}	
	}

	/**
	 * @author Arnold 
	 * 
	 * Methode permettant de verifier si le fichier demande est dans le reservoir de fichier
	 */
	protected boolean FileNameTestEquals(String decodedHfName){

		/* Il s'agit dans cette partie de parcourir l'ensemble des fichiers splits du dossier split pour calculer les hashs de chaque fichiers */
		for(int i = 0; i < arrayPathFlatTree.size(); i++)
		{
			/* on recupere le nom de chaque dossier que nous avons dans le dossier pour le comparer a la demande */
			File DossierCourant = new File(arrayPathFlatTree.get(i));
			String FolderName = DossierCourant.getName();

			if (decodedHfName.equals(FolderName)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @author Arnold 
	 * 
	 * Methode permettant de recuperer le nombre de ressource initiales
	 */
	protected int GetNumberOfFiles(String decodedHfName){

		int NumberOfFiles =0;


		for (int i = 0; i < arrayPathSplitHash.size(); i++)
		{

			if(arrayPathSplitHash.get(i).contains(decodedHfName))
			{
				File dir = new File(arrayPathSplitHash.get(i));


				for(File FichierSplitcourant : dir.listFiles())
				{
					NumberOfFiles ++;
				}
			}
		}	
		return NumberOfFiles;
	}	

	/**
	 * @author Arnold 
	 * 
	 * Methode permettant de recuperer l'empreinte sur 128 premier bits de la racine d'un fichier
	 *  
	 */
	protected byte[] getHashFile(String decodedHfName) throws IOException{

		/* on parcours le dossier de l'arbre plat */
		for (int i = 0; i < arrayPathFlatTree.size(); i++)
		{
			/* on s�lectionne le sous-dossier correspondant a la requete */
			if(arrayPathSplitHash.get(i).contains(decodedHfName))
			{
				/* une fois le sous-dossier selectionne, on determine l empreinte racine */
				File dir = new File(arrayPathSplitHash.get(i));
				for(File FichierPlatTree : dir.listFiles())
				{
					/* on selectionne la racine */
					if (FichierPlatTree.getName().contains("FlatTree")){

						/* on raccourcis l'empreinte a ses 16 premiers octets */
						FileInputStream FichierFlatTreeCourant = new FileInputStream(FichierPlatTree);
						//String NomFichierMerkleCourantRaccourcis = FichierHashcourant.getName().substring(0, FichierHashcourant.getName().length()-6);
						FileOutputStream FlatTreeCourant = new FileOutputStream(FichierPlatTree.getAbsolutePath());				

						byte[] EmpreinteCourante128bits = new byte[16];
						FichierFlatTreeCourant.read(EmpreinteCourante128bits, 0, 16);

						FlatTreeCourant.close();
						FichierFlatTreeCourant.close();


						return EmpreinteCourante128bits;

					}
				}
			}
		}
		return null;
	}


	public boolean HashTestEquals(byte[] racineHash, String nameFile) throws NoSuchAlgorithmException, IOException
	{

		for (int i = 0; i < arrayPathFlatTree.size(); i++)
		{
			/* on s�lectionne le sous-dossier correspondant a la requete */
			if(arrayPathFlatTree.get(i).contains(nameFile))
			{
				/* une fois le sous-dossier selectionne, on determine l empreinte racine */
				File dir = new File(arrayPathFlatTree.get(i));
				for(File FichierPlatTree : dir.listFiles())
				{
					/* on selectionne la racine */
					if (FichierPlatTree.getName().contains("FlatTree")){

						/* on selectionne la racine source de façon à la comparer avec celle transmise */
						FileInputStream FichierFlatTreeCourant = new FileInputStream(FichierPlatTree);
						byte[] EmpreinteCourante128bits = new byte[16];
						FichierFlatTreeCourant.read(EmpreinteCourante128bits, 0, 16);

						if (EmpreinteCourante128bits == racineHash){
							return true;
						}
						else
							return false;

					}
				}
			}
		}	
		//return NumberOfFiles;

		return false;
	}

	/*
		byte sum[] = null;
		File f = new File(rootFile);
		FileInputStream fis = new FileInputStream(f);
		int i = fis.hashCode();

		if(i == rootHash)
		{
			return true;
		}
		else
		{
			return false;
		}
	 */

	protected String getFileAsked(String decodedHfName) throws IOException{

		/* on parcours le dossier de l'arbre plat */
		for (int i = 0; i < arrayPathDirectory.size(); i++)
		{
			/* une fois le sous-dossier selectionne, on determine l empreinte racine */
			File dir = new File(arrayPathDirectory.get(i));

			for(File FichierFile : dir.listFiles())
			{
				/* on selectionne la racine */
				if (FichierFile.getName().contains(decodedHfName)){

					return FichierFile.getAbsolutePath();

				}
			}
		}
		return null;
	}	

/**
 * @author KeviN
 * @return
 * Retourne le format de chemin selon le systeme d'exploitation
 */
public static String getOperatinSystem()
{
	String TAGWIN = "\\";
	String TAGLIN = "/";
	String nameOS = System.getProperty("os.name");  
	if(nameOS.contains("Windows"))
	{
		return TAGWIN;
	}
	else
	{
		return TAGLIN;
	}
}
}

