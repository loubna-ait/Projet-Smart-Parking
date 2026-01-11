package ma.emsi.entities;

import java.io.Serializable;
import java.math.BigDecimal;

public class TarifDynamique implements Serializable {
    private Long id;
    private String typeJour; // 'SEMAINE', 'WEEKEND', 'FERIE'
    private int heureDebut;
    private int heureFin;
    private BigDecimal tarifHoraire;

    public TarifDynamique() {}

    public TarifDynamique(String typeJour, int heureDebut, int heureFin, BigDecimal tarifHoraire) {
        this.typeJour = typeJour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.tarifHoraire = tarifHoraire;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTypeJour() { return typeJour; }
    public void setTypeJour(String typeJour) { this.typeJour = typeJour; }

    public int getHeureDebut() { return heureDebut; }
    public void setHeureDebut(int heureDebut) { this.heureDebut = heureDebut; }

    public int getHeureFin() { return heureFin; }
    public void setHeureFin(int heureFin) { this.heureFin = heureFin; }

    public BigDecimal getTarifHoraire() { return tarifHoraire; }
    public void setTarifHoraire(BigDecimal tarifHoraire) { this.tarifHoraire = tarifHoraire; }

    @Override
    public String toString() {
        return "TarifDynamique{" +
                "id=" + id +
                ", typeJour='" + typeJour + '\'' +
                ", tarifHoraire=" + tarifHoraire +
                '}';
    }
}
