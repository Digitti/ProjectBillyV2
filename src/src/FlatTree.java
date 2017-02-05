package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * @author Arnold
 * Classe dediee au réservoir arbre plat d'empreintes. Elle a comme fonction :
 * - la création du dossier contenant les fichier du reservoir FlatTree
 * - la création de l'arbre lui-meme (hachage de la concaténation des fichiers splités haches, voir la solution numéro 2 du sujet.   
 */
public class FlatTree {

	ArrayList<String> arrayHashFolder = new ArrayList<String>();
	ArrayList<String> arrayPathFlatTree = new ArrayList<String>();

	String CheminDossierCommun;
	String CheminDossierCommunFlatTree;
	
	/**
	 * @author Arnold
	 * Constructeur de la classe permettant de renseigner les attributs de la classe, a savoir :
	 *  - Le vecteur des fichiers splites arrayHashFolder
	 *  - Le chemin commun a tous les dossiers CheminDossierCommun
	 */
	public FlatTree(ArrayList<String> arrayPathSplitHash,String args){
		arrayHashFolder = arrayPathSplitHash;
		CheminDossierCommun = args;
	}

	/**
	 * @author Arnold
	 * Méthode permettant de creer l'arbe plat en lui-meme. Cela consiste tout d'abord à parcour l'ensemble des fichier
	 * du dossier des fichiers splites haches, puis de concaténer tous les fichier hachés d'une meme source, pour enfin
	 * haché cette con,concatenation. Cela constitue la racine de l'arbre.
	 */
	public void CreationFlatTree() throws IOException
	{
		Tank t = new Tank();
		/* Il s'agit dans cette partie de parcourir l'ensemble des fichiers hash du dossier hash pour calculer les hashs de chaque fichiers */
		for(int i = 0; i < arrayHashFolder.size(); i++)
		{
			File DossierCourant = new File(arrayHashFolder.get(i));

			/* creation du dossier contenant les empreintes des morceaux d'un même fichiers */
			File DossierFlatTreeCourant = new File(CheminDossierCommunFlatTree+t.getOperatinSystem()+DossierCourant.getName());

			if(DossierFlatTreeCourant.mkdir())
			{
				System.out.println("Out Directory : " + DossierFlatTreeCourant.getName() );
			}
			else
			{
				System.out.println("Output directory already exists");
			}	
			
			OutputStream out = new FileOutputStream(DossierFlatTreeCourant.getAbsolutePath()+t.getOperatinSystem()+DossierCourant.getName()+".FullHash");

			/* On parcours le dossier courant de fichier hash */
			for(File FichierHashcourant : DossierCourant.listFiles())
			{
				/* mis à jour des flux d'entres et de sortie */		
				InputStream in = new FileInputStream(FichierHashcourant);
						
				writeFusionFiles(out,in);
				
				in.close();
			}
			
			out.flush();
			out.close();
			
			/* mis à jour des flux d'entres et de sortie */
			FileInputStream HashComplet = new FileInputStream(DossierFlatTreeCourant.getAbsolutePath()+t.getOperatinSystem()+DossierCourant.getName()+".FullHash");
			
			//String NomFichierSplitCourantRaccourcis = FichierSplitCourant.getName().substring(0, FichierSplitcourant.getName().length()-7);
			
			FileOutputStream FichierFlatTree = new FileOutputStream(DossierFlatTreeCourant.getAbsolutePath()+t.getOperatinSystem()+DossierCourant.getName()+".FlatTree"); 
			
			/* Bloc réalisant le hash de chaque morceaux (l'entrée à hasher est renseigné et mis à jour par la variable : FichierSplitCourant ) */
			String NomFichierSplitCourant = HashComplet.toString();
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA-512");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] bytSHA = md.digest(NomFichierSplitCourant.getBytes());
			FichierFlatTree.write(bytSHA);
			
			/* la creation de hash est terminée. le numéro de hash est mis a jour, les flux de sortie et d'entrée sont refermes */
			HashComplet.close();
			FichierFlatTree.close();
			
		}
	}	

	/**
	 * @author Arnold
	 * Méthode permettant de creer le dossier qui contiendra l'arbre plat
	 */
	public String CreationDossierPlatTree() throws FileNotFoundException
	{	
		Tank t = new Tank();
		/**
		 *  Creation du dossier part en fonction du dossier d'entrée 
		 */
		File pathFolder = new File(CheminDossierCommun);
		File pathOutput = new File(pathFolder.getParent()+t.getOperatinSystem()+pathFolder.getName()+"FlatTree");

		if(pathOutput.mkdir())
		{
			System.out.println("Out Split Directory : " + pathOutput.getPath() );
		}
		else
		{
			System.out.println("Output directory already exists");
		}

		CheminDossierCommunFlatTree = pathOutput.getPath();
		return CheminDossierCommunFlatTree;
	}

	/**
	 * @author Arnold
	 * Méthode utilisee pour concaténer deux fichiers ensemble
	 */
	public void writeFusionFiles(OutputStream out1, InputStream sourceFiles1) throws IOException {
		
		/* On renseigne le fichier de sortie */
		OutputStream out = out1;
		
		try {

			byte[] buf = new byte [8192]; // buffer de copie par bloc
			int len; // compteur de byte lu

				InputStream in = sourceFiles1;
				try {
					// On lit dans le buffer (bloc de 8192 bytes max)
					while ( (len=in.read(buf)) >= 0 ) {
						out.write(buf, 0, len); // et on copie ce qu'on a lu
					}
				} finally {
					//in.close();
				}

		} finally {
			//out.close();
		}
	}
}
