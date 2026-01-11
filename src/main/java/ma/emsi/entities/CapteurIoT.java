package ma.emsi.entities;

import java.io.Serializable;

public class CapteurIoT implements Serializable {
    private Long id;
    private String ipAddress;
    private int etatBatterie;
    private boolean statutOperationnel;

    private PlaceStationnement place;

    public CapteurIoT() {}

    public CapteurIoT(String ipAddress, int etatBatterie, boolean statutOperationnel, PlaceStationnement place) {
        this.ipAddress = ipAddress;
        this.etatBatterie = etatBatterie;
        this.statutOperationnel = statutOperationnel;
        this.place = place;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public int getEtatBatterie() { return etatBatterie; }
    public void setEtatBatterie(int etatBatterie) { this.etatBatterie = etatBatterie; }

    public boolean isStatutOperationnel() { return statutOperationnel; }
    public void setStatutOperationnel(boolean statutOperationnel) { this.statutOperationnel = statutOperationnel; }

    public PlaceStationnement getPlace() { return place; }
    public void setPlace(PlaceStationnement place) { this.place = place; }

    @Override
    public String toString() {
        return "CapteurIoT{" +
                "id=" + id +
                ", ipAddress='" + ipAddress + '\'' +
                ", etatBatterie=" + etatBatterie +
                '}';
    }
}
