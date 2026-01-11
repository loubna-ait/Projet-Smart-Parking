package ma.emsi.entities;

import java.io.Serializable;
import java.util.Date;

public class Paiement implements Serializable {
    private Long id;
    private Date datePaiement;
    private Double montant;
    private String moyenPaiement;
    private String statut; // 'VALIDE', 'ECHOUE'

    private Reservation reservation;

    public Paiement() {
        this.datePaiement = new Date();
    }

    public Paiement(Double montant, String moyenPaiement, String statut, Reservation reservation) {
        this.datePaiement = new Date();
        this.montant = montant;
        this.moyenPaiement = moyenPaiement;
        this.statut = statut;
        this.reservation = reservation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getDatePaiement() { return datePaiement; }
    public void setDatePaiement(Date datePaiement) { this.datePaiement = datePaiement; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public String getMoyenPaiement() { return moyenPaiement; }
    public void setMoyenPaiement(String moyenPaiement) { this.moyenPaiement = moyenPaiement; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }

    @Override
    public String toString() {
        return "Paiement{" +
                "id=" + id +
                ", montant=" + montant +
                ", statut='" + statut + '\'' +
                '}';
    }
}
