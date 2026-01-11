package ma.emsi.entities;

import java.io.Serializable;

public class PlaceStationnement implements Serializable {
    private Long id;
    private String numero;
    private Integer etage;
    private String type; // 'STANDARD', 'HANDICAP', 'ELECTRIQUE'
    private boolean estDisponible;

    // Many-to-One relationship with Parking
    private Parking parking;

    public PlaceStationnement() {
        this.estDisponible = true;
    }

    public PlaceStationnement(String numero, Integer etage, String type, boolean estDisponible, Parking parking) {
        this.numero = numero;
        this.etage = etage;
        this.type = type;
        this.estDisponible = estDisponible;
        this.parking = parking;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Integer getEtage() { return etage; }
    public void setEtage(Integer etage) { this.etage = etage; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isEstDisponible() { return estDisponible; }
    public void setEstDisponible(boolean estDisponible) { this.estDisponible = estDisponible; }

    public Parking getParking() { return parking; }
    public void setParking(Parking parking) { this.parking = parking; }

    @Override
    public String toString() {
        return "PlaceStationnement{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", etage=" + etage +
                ", type='" + type + '\'' +
                ", estDisponible=" + estDisponible +
                '}';
    }
}
