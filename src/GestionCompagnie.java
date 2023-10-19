import java.io.File;

import javax.swing.*;

public class GestionCompagnie {
    public static int menu(String nomCompagnie) {
        String choix;
        String texteMenu = "\tGESTION DES VOLS\n\n1-Liste des vols\n2-Ajout d'un vol\n3-Retrait d'un vol\n4-Modification de la date de départ\n5-Réservation d'un vol\n";
        texteMenu += "0-Terminer\n\nFaites votre choix : ";
        choix = JOptionPane.showInputDialog(null, texteMenu, nomCompagnie, JOptionPane.PLAIN_MESSAGE);
        return Integer.parseInt(choix);
    }

    public static void message(String msg) {
        JOptionPane.showMessageDialog(null, msg, "MESSAGES", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) throws Exception {
       Compagnie airRelax = new Compagnie("Cie_Air_Relax");
       int choix;
       File f = new File(Compagnie.FICHIER_VOLS_OBJ);
		if (f.exists()&& f.canRead()) {
			airRelax.chargerVolsObj();
		}else {
			airRelax.chargerVols();
        }
		
       do {
           choix = menu(airRelax.getNomCompagnie());
           switch (choix) {
               case 1:
               Compagnie.sortie = new JTextArea();
               Compagnie.listerVol();
               JOptionPane.showMessageDialog(null, Compagnie.sortie, null, JOptionPane.PLAIN_MESSAGE);
               break;
                   
               case 2:
                    airRelax.ajouterVol();
                   break;
               case 3:
                   airRelax.supprimerVol();
                   break;
               case 4:
                   airRelax.changerDateVol();
                   break;
               case 5:
                   airRelax.reserverVol();
                   break;
               case 0:
                   airRelax.enregistrerVols();
                   airRelax.sauvegarderVolsObjets();
                   message("Merci d'avoir utilisé notre système.");
                   break;
               default:
                   message("Votre choix est invalide.\nEssayez de nouveau. ");
                   break;
           }
       } while (choix != 0);
    }

}
