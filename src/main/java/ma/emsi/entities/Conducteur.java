package ma.emsi.entities;

public class Conducteur extends Utilisateur {
    private String numPermis;

    public Conducteur() {}

    public Conducteur(String nom, String prenom, String email, String motDePasse, String role, String numPermis) {
        super(nom, prenom, email, motDePasse, role);
        this.numPermis = numPermis;
    }

    public String getNumPermis() { return numPermis; }
    public void setNumPermis(String numPermis) { this.numPermis = numPermis; }

    @Override
    public String toString() {
        return "Conducteur{" +
                "numPermis='" + numPermis + '\'' +
                "} " + super.toString();
    }
}
