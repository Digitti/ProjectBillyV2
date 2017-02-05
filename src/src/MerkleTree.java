package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Arnold
 * Classe dédiée au reservoir arbre de Merkle d'empreintes. Elle a comme fonction :
 * - la création du dossier contenant les fichier du réservoir MerkleTree
 * - la création de l'arbre lui-meme.   
 */
public class MerkleTree {
	
	
	ArrayList<String> arrayHashFolder = new ArrayList<String>();
	ArrayList<String> arrayConcatenationFolder = new ArrayList<String>();
	ArrayList<String> arrayConcatenationHashFolder = new ArrayList<String>();
	
	String CheminDossierCommun;
	String CheminDossierCommunMerkle;

	/**
	 * @author Arnold
	 * Constructeur de la classe permettant de renseigner les attributs de la classe, à savoir :
	 *  - Le vecteur des fichiers splités arrayHashFolder
	 *  - Le chemin commun à tous les dossiers CheminDossierCommun
	 */
	public MerkleTree(ArrayList<String> arrayPathSplitHash,String[] args)
	{
		arrayHashFolder = arrayPathSplitHash;
		CheminDossierCommun = args[0];
	}

	/**
	 * @author Arnold
	 * Méthode permettant de créer l'arbe plat en lui-même.
	 */
	public void CreationArbreMerkle() throws IOException
	{
		int NumeroFichierMerkle =0; 
		
		/* Il s'agit dans cette partie de parcourir l'ensemble des fichiers hash du dossier hash pour calculer les hashs de chaque fichiers */
		for(int i = 0; i < arrayHashFolder.size(); i++)
		{
			File DossierCourant = new File(arrayHashFolder.get(i));
			
			/* creation du dossier contenant les empreintes des morceaux d'un même fichiers */
			File DossierMerkleCourant = new File(CheminDossierCommunMerkle+"\\"+DossierCourant.getName());
			
			if(DossierMerkleCourant.mkdir())
			{
				System.out.println("Out Directory : " + DossierMerkleCourant.getName() );
			}
			else
			{
				System.out.println("Output directory already exists");
			}	
			
			/* On parcours le dossier courant de fichier hash */
			for(File FichierHashcourant : DossierCourant.listFiles())
			{
				/* mis à jour des flux d'entres et de sortie */
				FileInputStream FichierHashCourant = new FileInputStream(FichierHashcourant);
				String NomFichierMerkleCourantRaccourcis = FichierHashcourant.getName().substring(0, FichierHashcourant.getName().length()-6);
				FileOutputStream HashCourant = new FileOutputStream(DossierMerkleCourant.getAbsolutePath()+"\\"+NomFichierMerkleCourantRaccourcis+".hash"+NumeroFichierMerkle);				
				
				byte[] EmpreinteCourante128bits = new byte[16];
				FichierHashCourant.read(EmpreinteCourante128bits, 0, 16);
				HashCourant.write(EmpreinteCourante128bits);
				HashCourant.flush();
				
				FichierHashCourant.close();
				HashCourant.close();
				NumeroFichierMerkle ++;
			}
			NumeroFichierMerkle = 0;
		}
	}
	
	/**
	 * @author Arnold
	 * Méthode permettant de créer le dossier qui contiendra l'arbre plat
	 */
	public void CreationDossierMerkle() throws FileNotFoundException
	{	
		/**
		 *  Creation du dossier part en fonction du dossier d'entrée 
		 */
		File pathFolder = new File(CheminDossierCommun);
		File pathOutput = new File(pathFolder.getParent()+"\\"+pathFolder.getName()+"Merkle");
		
		if(pathOutput.mkdir())
		{
			System.out.println("Out Split Directory : " + pathOutput.getPath() );
		}
		else
		{
			System.out.println("Output directory already exists");
		}
		
		CheminDossierCommunMerkle = pathOutput.getPath();
	}
}
