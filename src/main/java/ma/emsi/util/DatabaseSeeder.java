package ma.emsi.util;

import ma.emsi.entities.Administrateur;
import ma.emsi.entities.Conducteur;
import ma.emsi.entities.Utilisateur; // Added this import
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSeeder {
    public static void seedIfNeeded() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // On vérifie si Loubna existe
            Utilisateur loubnaExist = session.createQuery("from Utilisateur where email = :email", Utilisateur.class)
                    .setParameter("email", "loubnaaithra@gmail.com")
                    .uniqueResult();

            if (loubnaExist == null) {
                System.out.println(">>> Initialisation : Création du compte Conducteur (Loubna)");
                Conducteur loubna = new Conducteur("loubna", "aithra", "loubnaaithra@gmail.com", "123", "CONDUCTEUR",
                        "LX987654");
                session.save(loubna);
            }

            // On vérifie si Hassan existe
            Utilisateur adminExist = session.createQuery("from Utilisateur where email = :email", Utilisateur.class)
                    .setParameter("email", "hassan.alami@smartparking.ma")
                    .uniqueResult();

            if (adminExist == null) {
                System.out.println(">>> Initialisation : Création du compte Admin (Hassan)");
                Administrateur admin = new Administrateur("Alami", "Hassan", "hassan.alami@smartparking.ma", "admin123",
                        "ADMIN", "Infrastructure");
                session.save(admin);
            }

            tx.commit();
            System.out.println(">>> Base de données prête.");
        } catch (Exception e) {
            System.err.println("!!! Erreur initialisation : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
