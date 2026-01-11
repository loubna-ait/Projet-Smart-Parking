import ma.emsi.entities.Administrateur;
import ma.emsi.entities.Parking;
import ma.emsi.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MainTest {
    public static void main(String[] args) {
        System.out.println("Début du test Smart Parking...");

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // 1. Création d'un administrateur
            Administrateur admin = new Administrateur();
            admin.setNom("Dupont");
            admin.setPrenom("Jean");
            admin.setEmail("jean.dupont@example.com");
            admin.setMotDePasse("secret123");
            admin.setRole("ADMIN");
            admin.setDepartement("Infrastructure");

            // 2. Création d'un parking
            Parking parking = new Parking();
            parking.setNom("Parking Central Ville");
            parking.setAdresse("123 Avenue Mohamed V, Casablanca");
            parking.setCapaciteTotale(500);
            parking.setLatitude(33.5731);
            parking.setLongitude(-7.5898);

            // 3. Sauvegarde en base
            session.save(admin);
            session.save(parking);

            transaction.commit();
            System.out.println("Succès ! Administrateur et Parking insérés en base.");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtil.shutdown();
        }
    }
}
