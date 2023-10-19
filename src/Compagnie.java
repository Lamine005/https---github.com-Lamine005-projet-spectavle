import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.JTextArea;
import java.awt.Font;



public class Compagnie implements Serializable {
    public static final int MAX_VOLS = 340;
    public static final int MAX_PLACES = 340;
    static final String FICHIER_VOLS_TEXTE = "Cie_Air_Relax.txt";
	static final String FICHIER_VOLS_OBJ = "Cie_Air_Relax.obj";
    static ObjectInputStream tmpVolsReadObj;
	static ObjectOutputStream tmpVolsWriteObj;
    static JTextArea sortie;
    private String nomCompagnie;
    public static int nbVolsActifs;
    private static ArrayList<Vol> vols = new ArrayList<>();

    // Constucteurs

    Compagnie(String nomCompagnie) throws IOException {
        this.nomCompagnie = nomCompagnie;
        chargerVols();
    }

    public String getNomCompagnie() {
        return this.nomCompagnie;
    }

    public static void message(String msg) {
        JOptionPane.showMessageDialog(null, msg, "MESSAGES", JOptionPane.PLAIN_MESSAGE);
    }

    

    public void ajouterVol() {
        boolean dateValide = false;
        String elems[] = new String[3];
        if (vols.size() == MAX_VOLS) {
            message("Impossible d'ajouter un nouveau vol la capacité maximale est de " + MAX_VOLS);
        } else { // On peut ajouter un nouveau vol
            String numVol = JOptionPane.showInputDialog(null, "Entrez le numéro du vol", "AJOUT D'UN VOL",
                    JOptionPane.PLAIN_MESSAGE);
            int pos = rechercherVol(numVol);
            if (pos == -1) { // Ce vol de numéro numVol n'existe pas, alors on peut l'ajouter
                String destination = JOptionPane.showInputDialog(null, "Entrez la destination", "AJOUT D'UN VOL",
                        JOptionPane.PLAIN_MESSAGE);
                String date = "";
                while (!dateValide) {
                    date = JOptionPane.showInputDialog(null, "Entrez la date dans le format JJ/MM/AAAA",
                            "AJOUT D'UN VOL",
                            JOptionPane.PLAIN_MESSAGE);
                    dateValide = Date.validerFormatDate(date);
                    if (!dateValide) {
                        message("Format de date invalide, vous devez taper JJ/MM/AAAA. Merci.");
                    }
                }
                elems = date.split("/");
                int jour = Integer.parseInt(elems[0]);
                int mois = Integer.parseInt(elems[1]);
                int annee = Integer.parseInt(elems[2]);
                vols.add(new Vol(numVol, destination, new Date(jour, mois, annee), 0));
                message("Votre vol a été ajouté");
            } else {
                message("Ce numéro " + numVol + " de vol existe déjà.");
            }
        }
    }

    public void changerDateVol() {
        boolean dateValide = false;
        String elems[] = new String[3];
        String numVol = JOptionPane.showInputDialog(null, "Entrez le numéro du vol", "MODIFIER LA DATE D'UN VOL",
                JOptionPane.PLAIN_MESSAGE);

        
        int pos = rechercherVol(numVol);
        if (pos != -1) {// Trouvé
            String date = "";
            while (!dateValide) {
                date = JOptionPane.showInputDialog(null,
                        "La date actuelle du vol " + numVol + " est " + vols.get(pos).getDateVol()
                                + "\nEntrez la nouvelle date dans le format JJ/MM/AAAA",
                        "MODIFIER LA DATE D'UN VOL",
                        JOptionPane.PLAIN_MESSAGE);
                dateValide = Date.validerFormatDate(date);
                if (!dateValide) {
                    message("Format de date invalide, vous devez taper JJ/MM/AAAA. Merci.");
                }
            }
            elems = date.split("/");
            int jour = Integer.parseInt(elems[0]);
            int mois = Integer.parseInt(elems[1]);
            int annee = Integer.parseInt(elems[2]);
            vols.get(pos).setDateVol(new Date(jour, mois, annee));
            message("Votre vol a été modifié");
        } else {
            message("Vol " + numVol + " introuvable!");
        }
    }

    public void enregistrerVols() throws IOException {
        String ligne;
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/donnees/" + this.nomCompagnie + ".txt"));
        for (Vol unVol : Compagnie.vols) {
            if (unVol != null) {
                Date dtv = unVol.getDateVol();
                ligne = unVol.getNumVol() + ";" + unVol.getDestination() + ";";
                ligne += dtv.getJour() + ";" + dtv.getMois() + ";" + dtv.getAnnee() + ";";
                ligne += unVol.getNbReservations();
                bw.write(ligne);
                bw.newLine();
            } else {
                break;
            }
        }
        bw.close();


    }

    public void trierVols() {
        Vol tmp;
        for (int i = 0; i < vols.size() - 1; i++) {
            for (int j = (i + 1); j < vols.size(); j++) {
                if (vols.get(j).getNumVol().compareTo(vols.get(i).getNumVol()) < 0) {
                    tmp = vols.get(i);
                    vols.set(i, vols.get(j));
                    vols.set(j, tmp);
                }
            }
        }
    }

    

    private int rechercherVol(String numVol) {
        return vols.indexOf(
                vols.stream().filter(vol -> vol.getNumVol().equalsIgnoreCase(numVol)).findFirst().orElse(null));
       
    }

    public void reserverVol() {
        String numVol = JOptionPane.showInputDialog(null, "Entrez le numéro du vol à reserver", "RÉSERVATION D'UN VOL",
                JOptionPane.PLAIN_MESSAGE);
        int pos = rechercherVol(numVol);
        if (pos != -1) {// Trouvé
            int placesDisponibles = MAX_PLACES - vols.get(pos).getNbReservations();
            if (placesDisponibles == 0) {
                message("Désolé le vol est plein");
                return;
            }
            int placesAReserver;
            do {
                placesAReserver = Integer.parseInt(JOptionPane.showInputDialog(null,
                        "Le nombre de places disponibles pour le vol " + numVol + " est " + placesDisponibles
                                + "\nEntrez le nombre de places à réserver ou 0 (zéro) pour quitter",
                        "RÉSERVATION D'UN VOL",
                        JOptionPane.PLAIN_MESSAGE));
                if (placesAReserver > placesDisponibles)
                    message("Le nombre de places entrées dépasse le nombre de places disponibles");
            } while (placesAReserver > placesDisponibles || placesAReserver < 0);
            vols.get(pos).setNbReservations(vols.get(pos).getNbReservations() + placesAReserver);
            message("Votre vol a été reservé");
        } else
            message("Vol " + numVol + " introuvable!");
    }

    public void supprimerVol() {
        String numVol = JOptionPane.showInputDialog(null, "Entrez le numéro du vol", "SUPPRESSION D'UN VOL",
                JOptionPane.PLAIN_MESSAGE);
        int pos = rechercherVol(numVol);
        if (pos != -1){
            vols.remove(vols.get(pos));
            message("Votre vol a été supprimé");}
        else{
            message("Vol " + numVol + " introuvable!");}
    }

    public static void afficherEntete(String suiteEntete) {
		sortie.setFont(new Font("monospace", Font.PLAIN, 12));
		sortie.append("\n\n\tLISTE DES LIVRES\n\n");
		sortie.append("Numéro\tDestination\t      Date\t  Réservations\n");
		sortie.append(suiteEntete);
	}

	public static void listerVol() {
		
		afficherEntete("\n");
		vols.forEach((unVol) -> {
			Vol leVol;
			if (unVol instanceof Vol) {
				leVol = (Vol) unVol;
				sortie.append(leVol.toString() + "\n");
				nbVolsActifs++;
			}
		});
		sortie.append("Nombre de vols actif = " + nbVolsActifs);
	}

    public void chargerVols() throws IOException {
        String ligne;
        BufferedReader br = new BufferedReader(new FileReader("src/donnees/" + nomCompagnie + ".txt"));
        while ((ligne = br.readLine()) != null && vols.size() < MAX_VOLS) {
            String[] elems = ligne.split(";");
            vols.add(new Vol(elems[0], elems[1],
                    new Date(Integer.parseInt(elems[2]), Integer.parseInt(elems[3]), Integer.parseInt(elems[4])),
                    Integer.parseInt(elems[5])));
        }
        br.close();
    }

    public void sauvegarderVolsObjets() throws IOException {
        ObjectOutputStream tmpLivresWriteObj = null;
        try {
			tmpLivresWriteObj = new ObjectOutputStream(new FileOutputStream(FICHIER_VOLS_OBJ));
			tmpLivresWriteObj.writeObject(vols);
		} catch (FileNotFoundException e) {
			System.out.println("Fichier introuvable. Vérifiez le chemin et nom du fichier.");
		} catch (IOException e) {
			System.out.println("Un probléme est arrivé lors de la manipulation du fichier. Vérifiez vos donn�es.");
		} catch (Exception e) {
			System.out.println("Un probléme est arrivé lors de la sauvegarde du fichier. Contactez l'administrateur.");
		} finally {
			tmpLivresWriteObj.close();
		}
    }

    public  void chargerVolsObj() throws Exception {
        
		ObjectInputStream tmpVolsReadObj = null;
		
		try {
			tmpVolsReadObj = new ObjectInputStream(new FileInputStream(FICHIER_VOLS_OBJ));
			vols = (ArrayList<Vol>) tmpVolsReadObj.readObject();
		} catch(FileNotFoundException e) {
			System.out.println("Fichier introuvable. Vérifiez le chemin et nom du fichier.");
		} catch(IOException e) {
			System.out.println("Un problème est arrivé lors de la manipulation du fichier. Vérifiez vos données.");
		} catch(Exception e) {
			System.out.println("Un problème est arrivé lors du chargement du fichier. Contactez l'administrateur.");
		} finally {
			if(tmpVolsReadObj != null) {
				tmpVolsReadObj.close();
			}
		}
	}


    
}


