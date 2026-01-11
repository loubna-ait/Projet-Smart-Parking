package ma.emsi.dao;

import ma.emsi.entities.PlaceStationnement;
import ma.emsi.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Data Access Object (DAO) pour la gestion des parkings.
 * Centralise les requêtes HQL complexes pour une meilleure maintenabilité.
 */
public class ParkingDAO {

    /**
     * Calcule dynamiquement le nombre de places de stationnement disponibles
     * pour un parking donné en utilisant Hibernate Query Language (HQL).
     * 
     * @param parkingId L'identifiant unique du parking.
     * @return Le nombre de places libres (disponibles).
     */
    public long compterPlacesLibres(Long parkingId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Requête HQL travaillant directement sur l'objet PlaceStationnement
            String hql = "select count(p) from PlaceStationnement p " +
                    "where p.parking.id = :pid " +
                    "and p.estDisponible = true";

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("pid", parkingId);

            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("Erreur dans ParkingDAO.compterPlacesLibres : " + e.getMessage());
            return 0;
        }
    }
}
