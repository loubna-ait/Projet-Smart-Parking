package ma.emsi.tests;

import ma.emsi.entities.*;
import ma.emsi.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

/**
 * Classe de test complète pour démontrer toutes les opérations CRUD
 * et les relations entre entités du système Smart Parking.
 */
public class MainTest {

    private static final String SEPARATOR = "=".repeat(80);
    private static final String LINE = "-".repeat(80);

    public static void main(String[] args) {
        printHeader("DÉMARRAGE DES TESTS SMART PARKING");

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // ========== SECTION 1: TESTS CRUD PARKING ==========
            printSection("1. TESTS CRUD - PARKING");
            Parking parking = testCRUDParking(session);

            // ========== SECTION 2: TESTS CRUD PLACE STATIONNEMENT ==========
            printSection("2. TESTS CRUD - PLACE STATIONNEMENT");
            PlaceStationnement place = testCRUDPlaceStationnement(session, parking);

            // ========== SECTION 3: TESTS CRUD UTILISATEURS ==========
            printSection("3. TESTS CRUD - UTILISATEURS");
            Administrateur admin = testCRUDAdministrateur(session);
            Conducteur conducteur = testCRUDConducteur(session);

            // ========== SECTION 4: TESTS CRUD CAPTEUR IOT ==========
            printSection("4. TESTS CRUD - CAPTEUR IOT");
            CapteurIoT capteur = testCRUDCapteur(session, place);

            // ========== SECTION 5: TESTS CRUD TARIF DYNAMIQUE ==========
            printSection("5. TESTS CRUD - TARIF DYNAMIQUE");
            TarifDynamique tarif = testCRUDTarifDynamique(session);

            // ========== SECTION 6: WORKFLOW COMPLET RÉSERVATION + PAIEMENT ==========
            printSection("6. WORKFLOW COMPLET - RÉSERVATION & PAIEMENT");
            testWorkflowReservationPaiement(session, conducteur, place);

            // ========== SECTION 7: TESTS DES RELATIONS ==========
            printSection("7. TESTS DES RELATIONS ENTRE ENTITÉS");
            testRelations(session, parking.getId());

            printSuccess("✅ TOUS LES TESTS TERMINÉS AVEC SUCCÈS !");

        } catch (Exception e) {
            printError("Erreur globale lors des tests", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            HibernateUtil.shutdown();
            printHeader("FIN DES TESTS");
        }
    }

    /**
     * Test CRUD complet pour l'entité Parking
     */
    private static Parking testCRUDParking(Session session) {
        Transaction tx = null;
        Parking parking = null;

        try {
            // CREATE
            tx = session.beginTransaction();
            parking = new Parking();
            parking.setNom("Parking Central Casablanca");
            parking.setAdresse("123 Avenue Mohamed V, Casablanca");
            parking.setCapaciteTotale(500);
            parking.setLatitude(33.5731);
            parking.setLongitude(-7.5898);
            session.save(parking);
            tx.commit();
            printInfo("✓ CREATE: Parking créé avec ID " + parking.getId());

            // READ
            tx = session.beginTransaction();
            Parking foundParking = session.get(Parking.class, parking.getId());
            tx.commit();
            printInfo("✓ READ: Parking récupéré: " + foundParking.getNom());

            // UPDATE
            tx = session.beginTransaction();
            foundParking.setCapaciteTotale(600);
            session.update(foundParking);
            tx.commit();
            printInfo("✓ UPDATE: Capacité du parking modifiée à 600 places");

            // Vérification de la mise à jour
            tx = session.beginTransaction();
            Parking updatedParking = session.get(Parking.class, parking.getId());
            tx.commit();
            printInfo("  Vérification: Nouvelle capacité = " + updatedParking.getCapaciteTotale());

            return parking;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            printError("Erreur dans testCRUDParking", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Test CRUD complet pour l'entité PlaceStationnement
     */
    private static PlaceStationnement testCRUDPlaceStationnement(Session session, Parking parking) {
        Transaction tx = null;
        PlaceStationnement place = null;

        try {
            // CREATE
            tx = session.beginTransaction();
            place = new PlaceStationnement();
            place.setNumero("A-001");
            place.setEtage(1);
            place.setType("STANDARD");
            place.setEstDisponible(true);
            place.setParking(parking);
            session.save(place);
            tx.commit();
            printInfo("✓ CREATE: Place créée avec numéro " + place.getNumero());

            // CREATE - Place handicapé
            tx = session.beginTransaction();
            PlaceStationnement placeHandicap = new PlaceStationnement();
            placeHandicap.setNumero("A-002");
            placeHandicap.setEtage(1);
            placeHandicap.setType("HANDICAP");
            placeHandicap.setEstDisponible(true);
            placeHandicap.setParking(parking);
            session.save(placeHandicap);
            tx.commit();
            printInfo("✓ CREATE: Place handicapé créée avec numéro " + placeHandicap.getNumero());

            // READ
            tx = session.beginTransaction();
            PlaceStationnement foundPlace = session.get(PlaceStationnement.class, place.getId());
            tx.commit();
            printInfo("✓ READ: Place récupérée: " + foundPlace.getNumero() + " (Type: " + foundPlace.getType() + ")");

            // UPDATE
            tx = session.beginTransaction();
            foundPlace.setEstDisponible(false);
            session.update(foundPlace);
            tx.commit();
            printInfo("✓ UPDATE: Place marquée comme occupée");

            return place;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            printError("Erreur dans testCRUDPlaceStationnement", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Test CRUD pour l'entité Administrateur
     */
    private static Administrateur testCRUDAdministrateur(Session session) {
        Transaction tx = null;
        Administrateur admin = null;

        try {
            // CREATE
            tx = session.beginTransaction();
            admin = new Administrateur();
            admin.setNom("Alami");
            admin.setPrenom("Hassan");
            admin.setEmail("hassan.alami@smartparking.ma");
            admin.setMotDePasse("admin123");
            admin.setRole("ADMIN");
            admin.setDepartement("Infrastructure");
            session.save(admin);
            tx.commit();
            printInfo("✓ CREATE: Administrateur créé: " + admin.getNom() + " " + admin.getPrenom());

            // READ
            tx = session.beginTransaction();
            Administrateur foundAdmin = session.get(Administrateur.class, admin.getId());
            tx.commit();
            printInfo("✓ READ: Administrateur récupéré, Département: " + foundAdmin.getDepartement());

            // UPDATE
            tx = session.beginTransaction();
            foundAdmin.setDepartement("Opérations");
            session.update(foundAdmin);
            tx.commit();
            printInfo("✓ UPDATE: Département modifié en 'Opérations'");

            return admin;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            printError("Erreur dans testCRUDAdministrateur", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Test CRUD pour l'entité Conducteur
     */
    private static Conducteur testCRUDConducteur(Session session) {
        Transaction tx = null;
        Conducteur conducteur = null;

        try {
            // CREATE
            tx = session.beginTransaction();
            conducteur = new Conducteur();
            conducteur.setNom("Benali");
            conducteur.setPrenom("Fatima");
            conducteur.setEmail("fatima.benali@email.com");
            conducteur.setMotDePasse("pass123");
            conducteur.setRole("CONDUCTEUR");
            conducteur.setNumPermis("AB123456");
            session.save(conducteur);
            tx.commit();
            printInfo("✓ CREATE: Conducteur créé: " + conducteur.getNom() + " " + conducteur.getPrenom());

            // CREATE ANOTHER CONDUCTEUR (LOUBNA)
            tx = session.beginTransaction();
            Conducteur loubna = new Conducteur();
            loubna.setNom("loubna");
            loubna.setPrenom("aithra");
            loubna.setEmail("loubnaaithra@gmail.com");
            loubna.setMotDePasse("123");
            loubna.setRole("CONDUCTEUR");
            loubna.setNumPermis("LX987654");
            session.save(loubna);
            tx.commit();
            printInfo("✓ CREATE: Conducteur Loubna créé avec succès");

            // READ
            tx = session.beginTransaction();
            Conducteur foundConducteur = session.get(Conducteur.class, conducteur.getId());
            tx.commit();
            printInfo("✓ READ: Conducteur récupéré, Permis: " + foundConducteur.getNumPermis());

            return conducteur;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            printError("Erreur dans testCRUDConducteur", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Test CRUD pour l'entité CapteurIoT
     */
    private static CapteurIoT testCRUDCapteur(Session session, PlaceStationnement place) {
        Transaction tx = null;
        CapteurIoT capteur = null;

        try {
            // CREATE
            tx = session.beginTransaction();
            capteur = new CapteurIoT();
            capteur.setIpAddress("192.168.1.101");
            capteur.setEtatBatterie(85);
            capteur.setStatutOperationnel(true);
            capteur.setPlace(place);
            session.save(capteur);
            tx.commit();
            printInfo("✓ CREATE: Capteur IoT créé avec IP " + capteur.getIpAddress());

            // READ
            tx = session.beginTransaction();
            CapteurIoT foundCapteur = session.get(CapteurIoT.class, capteur.getId());
            tx.commit();
            printInfo("✓ READ: Capteur récupéré, Batterie: " + foundCapteur.getEtatBatterie() + "%");

            // UPDATE
            tx = session.beginTransaction();
            foundCapteur.setEtatBatterie(75);
            session.update(foundCapteur);
            tx.commit();
            printInfo("✓ UPDATE: Niveau batterie mis à jour à 75%");

            return capteur;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            printError("Erreur dans testCRUDCapteur", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Test CRUD pour l'entité TarifDynamique
     */
    private static TarifDynamique testCRUDTarifDynamique(Session session) {
        Transaction tx = null;
        TarifDynamique tarif = null;

        try {
            // CREATE
            tx = session.beginTransaction();
            tarif = new TarifDynamique();
            tarif.setTypeJour("SEMAINE");
            tarif.setHeureDebut(8);
            tarif.setHeureFin(18);
            tarif.setTarifHoraire(BigDecimal.valueOf(15.0));
            session.save(tarif);
            tx.commit();
            printInfo("✓ CREATE: Tarif dynamique créé: " + tarif.getTarifHoraire() + " DH/h");

            // CREATE - Tarif weekend
            tx = session.beginTransaction();
            TarifDynamique tarifWeekend = new TarifDynamique();
            tarifWeekend.setTypeJour("WEEKEND");
            tarifWeekend.setHeureDebut(10);
            tarifWeekend.setHeureFin(20);
            tarifWeekend.setTarifHoraire(BigDecimal.valueOf(20.0));
            session.save(tarifWeekend);
            tx.commit();
            printInfo("✓ CREATE: Tarif weekend créé: " + tarifWeekend.getTarifHoraire() + " DH/h");

            // READ
            tx = session.beginTransaction();
            TarifDynamique foundTarif = session.get(TarifDynamique.class, tarif.getId());
            tx.commit();
            printInfo("✓ READ: Tarif récupéré pour " + foundTarif.getTypeJour());

            // UPDATE
            tx = session.beginTransaction();
            foundTarif.setTarifHoraire(BigDecimal.valueOf(18.0));
            session.update(foundTarif);
            tx.commit();
            printInfo("✓ UPDATE: Tarif horaire modifié à 18.0 DH/h");

            return tarif;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            printError("Erreur dans testCRUDTarifDynamique", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Test du workflow complet : Réservation + Paiement
     */
    private static void testWorkflowReservationPaiement(Session session, Conducteur conducteur,
            PlaceStationnement place) {
        Transaction tx = null;

        try {
            // CREATE RESERVATION
            tx = session.beginTransaction();

            Calendar cal = Calendar.getInstance();
            Date dateDebut = cal.getTime();
            cal.add(Calendar.HOUR, 3);
            Date dateFin = cal.getTime();

            Reservation reservation = new Reservation();
            reservation.setDateCreation(new Date());
            reservation.setDateDebut(dateDebut);
            reservation.setDateFin(dateFin);
            reservation.setCoutEstime(45.0);
            reservation.setStatut("CONFIRMEE");
            reservation.setConducteur(conducteur);
            reservation.setPlace(place);
            session.save(reservation);
            tx.commit();
            printInfo("✓ CREATE RESERVATION: Réservation #" + reservation.getId() + " pour 3h (45 DH)");

            // CREATE PAIEMENT
            tx = session.beginTransaction();
            Paiement paiement = new Paiement();
            paiement.setDatePaiement(new Date());
            paiement.setMontant(45.0);
            paiement.setMoyenPaiement("CARTE_BANCAIRE");
            paiement.setStatut("VALIDE");
            paiement.setReservation(reservation);
            session.save(paiement);
            tx.commit();
            printInfo("✓ CREATE PAIEMENT: Paiement #" + paiement.getId() + " validé (45 DH)");

            // READ - Vérifier la relation
            tx = session.beginTransaction();
            Reservation foundReservation = session.get(Reservation.class, reservation.getId());
            tx.commit();
            printInfo("✓ WORKFLOW: Réservation complète pour " + foundReservation.getConducteur().getNom());
            printInfo("  - Place: " + foundReservation.getPlace().getNumero());
            printInfo("  - Durée: 3 heures");
            printInfo("  - Statut: " + foundReservation.getStatut());

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            printError("Erreur dans testWorkflowReservationPaiement", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Test des relations entre entités
     */
    private static void testRelations(Session session, Long parkingId) {
        Transaction tx = null;

        try {
            // Test relation Parking -> Places
            tx = session.beginTransaction();
            Parking parking = session.get(Parking.class, parkingId);
            int nbPlaces = parking.getPlaces().size();
            tx.commit();
            printInfo("✓ RELATION Parking->Places: " + nbPlaces + " place(s) trouvée(s)");

            // Test requête HQL - Compter les réservations par statut
            tx = session.beginTransaction();
            Long countReservations = (Long) session.createQuery(
                    "SELECT COUNT(r) FROM Reservation r WHERE r.statut = :statut")
                    .setParameter("statut", "CONFIRMEE")
                    .uniqueResult();
            tx.commit();
            printInfo("✓ REQUÊTE HQL: " + countReservations + " réservation(s) confirmée(s)");

            // Test requête HQL - Places disponibles
            tx = session.beginTransaction();
            List<PlaceStationnement> placesDisponibles = session.createQuery(
                    "FROM PlaceStationnement p WHERE p.estDisponible = true", PlaceStationnement.class)
                    .list();
            tx.commit();
            printInfo("✓ REQUÊTE HQL: " + placesDisponibles.size() + " place(s) disponible(s)");

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            printError("Erreur dans testRelations", e);
            throw new RuntimeException(e);
        }
    }

    // ========== MÉTHODES UTILITAIRES POUR L'AFFICHAGE ==========

    private static void printHeader(String title) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  " + title);
        System.out.println(SEPARATOR + "\n");
    }

    private static void printSection(String title) {
        System.out.println("\n" + LINE);
        System.out.println("  " + title);
        System.out.println(LINE);
    }

    private static void printInfo(String message) {
        System.out.println("  " + message);
    }

    private static void printSuccess(String message) {
        System.out.println("\n🎉 " + message + "\n");
    }

    private static void printError(String message, Exception e) {
        System.err.println("\n❌ " + message);
        System.err.println("   Détails: " + e.getMessage());
        e.printStackTrace();
    }
}
