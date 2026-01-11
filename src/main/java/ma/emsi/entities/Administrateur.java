package ma.emsi.entities;

public class Administrateur extends Utilisateur {
    private String departement;

    public Administrateur() {}

    public Administrateur(String nom, String prenom, String email, String motDePasse, String role, String departement) {
        super(nom, prenom, email, motDePasse, role);
        this.departement = departement;
    }

    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }

    @Override
    public String toString() {
        return "Administrateur{" +
                "departement='" + departement + '\'' +
                "} " + super.toString();
    }
}
