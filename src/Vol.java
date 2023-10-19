import java.io.Serializable;

public class Vol implements Serializable{
    private String numVol;
    private String destination;
    private Date dateVol;
    private int nbReservations;

    Vol(String numVol, String destination, Date dateVol, int nbReservations){
        this.numVol = numVol;
        this.destination = destination;
        this.dateVol = dateVol;
        this.nbReservations = nbReservations;
    }

    // Autres méthodes get, set  toString et autres selon l'énoncé
    public String getNumVol(){
        return this.numVol;
    }

    public String getDestination() {
        return this.destination;
    }
    
    public Date getDateVol() {
        return this.dateVol;
    }

    public int getNbReservations() {
        return this.nbReservations;
    }

    public void setNbReservations(int nbReservations) {
        this.nbReservations = nbReservations;
    }

    public void setDateVol(Date dateVol) {
        this.dateVol = dateVol;
    }

    public String toString() {
        return String.format("%-10s%-20s%-15s%3d", this.numVol, this.destination, this.dateVol, this.nbReservations);
    }
}
