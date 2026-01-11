package ma.emsi.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Parking implements Serializable {
    private Long id;
    private String nom;
    private String adresse;
    private Double latitude;
    private Double longitude;
    private int capaciteTotale;

    // One-to-Many relationship with PlaceStationnement
    private Set<PlaceStationnement> places = new HashSet<>();

    public Parking() {}

    public Parking(String nom, String adresse, Double latitude, Double longitude, int capaciteTotale) {
        this.nom = nom;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capaciteTotale = capaciteTotale;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public int getCapaciteTotale() { return capaciteTotale; }
    public void setCapaciteTotale(int capaciteTotale) { this.capaciteTotale = capaciteTotale; }

    public Set<PlaceStationnement> getPlaces() { return places; }
    public void setPlaces(Set<PlaceStationnement> places) { this.places = places; }

    @Override
    public String toString() {
        return "Parking{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", capaciteTotale=" + capaciteTotale +
                '}';
    }
}
