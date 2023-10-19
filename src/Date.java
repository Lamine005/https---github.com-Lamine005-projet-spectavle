import java.io.Serializable;
import java.text.DecimalFormat;

public class Date implements Serializable {
    private int jour;
    private int mois;
    private int annee;

    Date(){}
    Date(int jour, int mois, int annee){
        setJour(jour);
        setMois(mois);
        setAnnee(annee);
    }

    public int getJour(){
        return this.jour;
    }
    
    public int getMois() {
        return this.mois;
    }
    
    public int getAnnee() {
        return this.annee;
    }

    public void setJour(int jour){
        if(jour >= 1 && jour <= 31){
            this.jour = jour;
        }else {
            System.out.println("Jour inavalide");
        }
    }

    public static boolean estBissextile(int an)
	{
		return (an % 4 == 0 && an % 100 != 0) || (an % 400 == 0);
	}
    
    public void setMois(int mois) {
        if (mois >= 1 && mois <= 12) {
            this.mois = mois;
        } else {
            System.out.println("Mois inavalide");
        }
    }

    public void setAnnee(int annee) {
        if (annee >= 1000) {
            this.annee = annee;
        } else {
            System.out.println("Annee inavalide");
        }
    }

    public static int determinerNbJoursMois(int mois, int an)
	{
		int nbJours;
		int tabJrMois[] = {0,31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if (mois == 2 && Date.estBissextile(an))
			nbJours = 29;
		else
			nbJours = tabJrMois[mois];
		return nbJours;
	}


    public static boolean validerFormatDate(String date) {
        int taille = date.length();
        if (taille == 10) {
            for (int i = 0; i < taille; i++) {
                switch (i) {
                    case 2:
                    case 5:
                        if (date.charAt(i) != '/') {
                            return false;
                        }
                        break;
                    case 0:
                    case 1:
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        if (date.charAt(i) < '0' || date.charAt(i) > '9') {
                            return false;
                        }
                        break;
                }
            }
        } else {
            return false;
        }
        String[] dates = date.split("/");
        int annee = Integer.parseInt(dates[2]);
        int mois = Integer.parseInt(dates[1]);
        int jour = Integer.parseInt(dates[0]);
        if (annee < 2023) return false;
        if (mois < 1 || mois > 12) return false;
        if (jour < 1 || jour > Date.determinerNbJoursMois(mois, annee)) return false;
        return true;
        // return !(jour < 1 || jour > Date.determinerNbJoursMois(mois, annee));
    }

    public String toString(){
        DecimalFormat decimalFormat = new DecimalFormat("00");
        
        String jourFormat = decimalFormat.format(jour);
        String moisFormat = decimalFormat.format(mois);
        return jourFormat+ "/" + moisFormat + "/" + annee;
    }
}
