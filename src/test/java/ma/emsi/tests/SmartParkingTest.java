package ma.emsi.tests;

import ma.emsi.entities.Parking;
import ma.emsi.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SmartParkingTest {

    private static Session session;

    @BeforeAll
    static void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @AfterAll
    static void tearDown() {
        if (session != null) {
            session.close();
        }
        HibernateUtil.shutdown();
    }

    @Test
    @Order(1)
    @DisplayName("Test de connexion à la base de données")
    void testConnection() {
        assertTrue(session.isOpen(), "La session Hibernate devrait être ouverte");
    }

    @Test
    @Order(2)
    @DisplayName("Test CRUD Parking")
    void testCrudParking() {
        Transaction tx = session.beginTransaction();

        // Create
        Parking p = new Parking("Test Parking", "Test Address", 10.0, 20.0, 100);
        session.save(p);
        tx.commit();

        assertNotNull(p.getId(), "L'ID du parking ne devrait pas être nul après la sauvegarde");

        // Read
        Parking found = session.get(Parking.class, p.getId());
        assertEquals("Test Parking", found.getNom());

        // Update
        tx = session.beginTransaction();
        found.setNom("Updated Name");
        session.update(found);
        tx.commit();

        Parking updated = session.get(Parking.class, p.getId());
        assertEquals("Updated Name", updated.getNom());

        // Delete
        tx = session.beginTransaction();
        session.delete(updated);
        tx.commit();

        Parking deleted = session.get(Parking.class, p.getId());
        assertNull(deleted, "Le parking devrait être supprimé");
    }
}
