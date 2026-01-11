package ma.emsi.entities;

import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable {
    private Long id;
    private Date dateCreation;
    private Date dateDebut;
    private Date dateFin;
    private Double coutEstime;
    private String statut; // 'EN_ATTENTE', 'CONFIRMEE', 'TERMINEE', 'ANNULEE'

    private Utilisateur conducteur; // Can be cast to Conducteur if needed, or mapped specifically
    private PlaceStationnement place;

    public Reservation() {
        this.dateCreation = new Date();
    }

    public Reservation(Date dateDebut, Date dateFin, Double coutEstime, String statut, Utilisateur conducteur, PlaceStationnement place) {
        this.dateCreation = new Date();
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.coutEstime = coutEstime;
        this.statut = statut;
        this.conducteur = conducteur;
        this.place = place;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public Double getCoutEstime() { return coutEstime; }
    public void setCoutEstime(Double coutEstime) { this.coutEstime = coutEstime; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Utilisateur getConducteur() { return conducteur; }
    public void setConducteur(Utilisateur conducteur) { this.conducteur = conducteur; }

    public PlaceStationnement getPlace() { return place; }
    public void setPlace(PlaceStationnement place) { this.place = place; }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", statut='" + statut + '\'' +
                '}';
    }
}
