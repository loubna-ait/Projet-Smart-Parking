package ma.emsi.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import ma.emsi.App;
import ma.emsi.entities.CapteurIoT;
import ma.emsi.entities.Parking;
import ma.emsi.entities.PlaceStationnement;
import ma.emsi.entities.Reservation;
import ma.emsi.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Calendar;
import java.util.Date;
import javafx.scene.paint.Color;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

public class MainController {

    @FXML
    private Label statusLabel;

    private final ma.emsi.dao.ParkingDAO parkingDAO = new ma.emsi.dao.ParkingDAO();
    private Set<Long> myReservedPlaceIds = new HashSet<>();

    // Tables
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab parkingTab;
    @FXML
    private Tab placesTab;
    @FXML
    private Tab capteursTab;

    // Parking Table
    @FXML
    private TableView<Parking> parkingTable;
    @FXML
    private TableColumn<Parking, Long> parkingIdCol;
    @FXML
    private TableColumn<Parking, String> parkingNomCol;
    @FXML
    private TableColumn<Parking, String> parkingAdresseCol;
    @FXML
    private TableColumn<Parking, Integer> parkingCapaciteCol;
    @FXML
    private TableColumn<Parking, String> parkingLibreCol;

    // Places Table
    @FXML
    private TableView<PlaceStationnement> placeTable;
    @FXML
    private TableColumn<PlaceStationnement, Long> placeIdCol;
    @FXML
    private TableColumn<PlaceStationnement, String> placeNumeroCol;
    @FXML
    private TableColumn<PlaceStationnement, String> placeTypeCol;
    @FXML
    private TableColumn<PlaceStationnement, String> placeStatusCol;
    @FXML
    private TableColumn<PlaceStationnement, String> placeParkingCol;

    // Capteurs Table
    @FXML
    private TableView<CapteurIoT> capteurTable;
    @FXML
    private TableColumn<CapteurIoT, Long> capteurIdCol;
    @FXML
    private TableColumn<CapteurIoT, String> capteurReferenceCol;
    @FXML
    private TableColumn<CapteurIoT, String> capteurTypeCol;
    @FXML
    private TableColumn<CapteurIoT, String> capteurEtatCol;

    @FXML
    private Button addParkingBtn;
    @FXML
    private Button updateParkingBtn;
    @FXML
    private Button deleteParkingBtn;
    @FXML
    private Button reserveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField searchField;

    @FXML
    public void initialize() {
        System.out.println("MainController: initializing...");
        try {
            setupTableColumns();
            refreshAllData();
            applyRolePermissions();
        } catch (Exception e) {
            System.err.println("Error in MainController.initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyRolePermissions() {
        String role = App.getCurrentUserRole();
        System.out.println("MainController: applying permissions for role: " + role);

        javafx.application.Platform.runLater(() -> {
            if ("CONDUCTEUR".equals(role)) {
                // Conducteur restrictions
                if (mainTabPane != null && capteursTab != null) {
                    mainTabPane.getTabs().remove(capteursTab);
                }
                if (addParkingBtn != null) {
                    addParkingBtn.setVisible(false);
                    addParkingBtn.setManaged(false);
                }
                if (updateParkingBtn != null) {
                    updateParkingBtn.setVisible(false);
                    updateParkingBtn.setManaged(false);
                }
                if (deleteParkingBtn != null) {
                    deleteParkingBtn.setVisible(false);
                    deleteParkingBtn.setManaged(false);
                }
                if (cancelBtn != null) {
                    cancelBtn.setVisible(true);
                    cancelBtn.setManaged(true);
                }
                statusLabel.setText("Connecté en tant que Conducteur : " + App.getCurrentUser().getNom());
            } else {
                // Admin restrictions
                if (reserveBtn != null) {
                    reserveBtn.setVisible(false);
                    reserveBtn.setManaged(false);
                }
                if (cancelBtn != null) {
                    cancelBtn.setVisible(false);
                    cancelBtn.setManaged(false);
                }
                statusLabel.setText("Connecté en tant que Administrateur : " + App.getCurrentUser().getNom());
            }
        });
    }

    @FXML
    private void handleLogout() throws Exception {
        App.setCurrentUserRole(null);
        App.setRoot("LoginView");
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            handleRefreshParkings();
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Parking> parkings = session.createQuery("from Parking where nom like :query", Parking.class)
                    .setParameter("query", "%" + query + "%")
                    .list();
            parkingTable.setItems(FXCollections.observableArrayList(parkings));
            statusLabel.setText("Recherche : " + parkings.size() + " résultats");
        } catch (Exception e) {
            statusLabel.setText("Erreur de recherche : " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteParking() {
        Parking selected = parkingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Veuillez sélectionner un parking à supprimer.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer le parking " + selected.getNom() + " ?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    session.beginTransaction();
                    session.delete(selected);
                    session.getTransaction().commit();
                    handleRefreshParkings();
                    statusLabel.setText("Parking supprimé.");
                } catch (Exception e) {
                    statusLabel.setText("Erreur de suppression : " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleReserve() {
        PlaceStationnement selected = placeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Veuillez sélectionner une place à réserver.");
            return;
        }

        if (!selected.isEstDisponible()) {
            statusLabel.setText("Cette place est déjà occupée.");
            return;
        }

        ma.emsi.entities.Utilisateur currentUser = App.getCurrentUser();
        if (currentUser == null) {
            statusLabel.setText("Erreur : session utilisateur perdue.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Vérifier si l'utilisateur a déjà une réservation CONFIRMEE pour cette place
            Reservation existing = session.createQuery(
                    "from Reservation where conducteur = :user and place = :place and statut = 'CONFIRMEE'",
                    Reservation.class)
                    .setParameter("user", currentUser)
                    .setParameter("place", selected)
                    .uniqueResult();

            if (existing != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Réservation existante");
                alert.setHeaderText(null);
                alert.setContentText("Vous avez déjà réservé cette place !");
                alert.showAndWait();
                return;
            }

            Transaction tx = session.beginTransaction();

            // Créer la réservation
            Calendar cal = Calendar.getInstance();
            Date start = cal.getTime();
            cal.add(Calendar.HOUR, 2);
            Date end = cal.getTime();

            Reservation res = new Reservation(start, end, 30.0, "CONFIRMEE", currentUser, selected);
            session.save(res);

            // Mettre à jour la place
            selected.setEstDisponible(false);
            session.update(selected);

            tx.commit();

            refreshAllData();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Réservation effectuée");
            alert.setContentText("Place " + selected.getNumero() + " réservée avec succès.");
            alert.showAndWait();

        } catch (Exception e) {
            statusLabel.setText("Erreur de réservation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        PlaceStationnement selected = placeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Veuillez sélectionner une place à annuler.");
            return;
        }

        ma.emsi.entities.Utilisateur currentUser = App.getCurrentUser();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Trouver la réservation active
            Reservation res = session.createQuery(
                    "from Reservation where conducteur = :user and place = :place and statut = 'CONFIRMEE'",
                    Reservation.class)
                    .setParameter("user", currentUser)
                    .setParameter("place", selected)
                    .uniqueResult();

            if (res == null) {
                statusLabel.setText("Aucune réservation active trouvée pour cette place.");
                return;
            }

            Transaction tx = session.beginTransaction();

            res.setStatut("ANNULEE");
            session.update(res);

            selected.setEstDisponible(true);
            session.update(selected);

            tx.commit();
            refreshAllData();
            statusLabel.setText("Réservation annulée.");

        } catch (Exception e) {
            statusLabel.setText("Erreur lors de l'annulation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        // Parking
        parkingIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        parkingNomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        parkingAdresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        parkingCapaciteCol.setCellValueFactory(new PropertyValueFactory<>("capaciteTotale"));
        parkingLibreCol.setCellValueFactory(cellData -> {
            Parking p = cellData.getValue();
            return new SimpleStringProperty(String.valueOf(parkingDAO.compterPlacesLibres(p.getId())));
        });

        // Places
        placeIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        placeNumeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        placeTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        placeStatusCol.setCellValueFactory(cellData -> {
            PlaceStationnement p = cellData.getValue();
            if (myReservedPlaceIds.contains(p.getId())) {
                return new SimpleStringProperty("Ma Réservation");
            }
            return new SimpleStringProperty(p.isEstDisponible() ? "Libre" : "Occupée");
        });

        placeStatusCol.setCellFactory(column -> new TableCell<PlaceStationnement, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Ma Réservation".equals(item)) {
                        setTextFill(Color.RED);
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setTextFill(Color.BLACK);
                        setStyle("");
                    }
                }
            }
        });
        placeParkingCol.setCellValueFactory(cellData -> {
            Parking p = cellData.getValue().getParking();
            return new SimpleStringProperty(p != null ? p.getNom() : "N/A");
        });

        // Capteurs
        capteurIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        capteurReferenceCol.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
        capteurTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty("Pression"));
        capteurEtatCol.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().isStatutOperationnel();
            return new SimpleStringProperty(status ? "Opérationnel" : "En panne");
        });
    }

    private void refreshAllData() {
        handleRefreshParkings();
        handleRefreshPlaces();
        handleRefreshCapteurs();
    }

    @FXML
    private void handleRefreshParkings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Parking> parkings = session.createQuery("from Parking", Parking.class).list();
            parkingTable.setItems(FXCollections.observableArrayList(parkings));
            statusLabel.setText("Parkings actualisés (" + parkings.size() + ")");
        } catch (Exception e) {
            statusLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void handleRefreshPlaces() {
        myReservedPlaceIds.clear();
        ma.emsi.entities.Utilisateur currentUser = App.getCurrentUser();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Identifier les places réservées par l'utilisateur actuel
            if (currentUser != null) {
                List<Long> ids = session.createQuery(
                        "select r.place.id from Reservation r where r.conducteur.id = :uid and r.statut = 'CONFIRMEE'",
                        Long.class)
                        .setParameter("uid", currentUser.getId())
                        .list();
                myReservedPlaceIds.addAll(ids);
            }

            List<PlaceStationnement> places = session.createQuery(
                    "select p from PlaceStationnement p left join fetch p.parking",
                    PlaceStationnement.class).list();
            placeTable.setItems(FXCollections.observableArrayList(places));
            statusLabel.setText("Places actualisées (" + places.size() + ")");
        } catch (Exception e) {
            statusLabel.setText("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefreshCapteurs() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<CapteurIoT> capteurs = session.createQuery(
                    "select c from CapteurIoT c left join fetch c.place p left join fetch p.parking",
                    CapteurIoT.class).list();
            capteurTable.setItems(FXCollections.observableArrayList(capteurs));
            statusLabel.setText("Capteurs actualisés (" + capteurs.size() + ")");
        } catch (Exception e) {
            statusLabel.setText("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddParking() {
        // Create a custom dialog
        Dialog<Parking> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Parking");
        dialog.setHeaderText("Entrez les informations du nouveau parking");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        TextField adresseField = new TextField();
        adresseField.setPromptText("Adresse");
        TextField capaciteField = new TextField();
        capaciteField.setPromptText("Capacité");

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Adresse:"), 0, 1);
        grid.add(adresseField, 1, 1);
        grid.add(new Label("Capacité:"), 0, 2);
        grid.add(capaciteField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a parking object when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int cap = Integer.parseInt(capaciteField.getText());
                    return new Parking(nomField.getText(), adresseField.getText(), 0.0, 0.0, cap);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(parking -> {
            if (parking != null) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    session.beginTransaction();
                    session.save(parking);
                    session.getTransaction().commit();
                    handleRefreshParkings();
                    statusLabel.setText("Parking ajouté avec succès !");
                } catch (Exception e) {
                    statusLabel.setText("Erreur lors de l'ajout : " + e.getMessage());
                }
            } else {
                statusLabel.setText("Données invalides. Veuillez réessayer.");
            }
        });
    }

    @FXML
    private void handleUpdateParking() {
        Parking selected = parkingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Veuillez sélectionner un parking à modifier.");
            return;
        }

        // Dialogue de modification
        Dialog<Parking> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Parking");
        dialog.setHeaderText("Modifiez les informations du parking : " + selected.getNom());

        ButtonType updateButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField nomField = new TextField(selected.getNom());
        TextField adresseField = new TextField(selected.getAdresse());
        TextField capaciteField = new TextField(String.valueOf(selected.getCapaciteTotale()));

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Adresse:"), 0, 1);
        grid.add(adresseField, 1, 1);
        grid.add(new Label("Capacité:"), 0, 2);
        grid.add(capaciteField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    int cap = Integer.parseInt(capaciteField.getText());
                    selected.setNom(nomField.getText());
                    selected.setAdresse(adresseField.getText());
                    selected.setCapaciteTotale(cap);
                    return selected;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(parking -> {
            if (parking != null) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    session.beginTransaction();
                    session.update(parking);
                    session.getTransaction().commit();
                    handleRefreshParkings();
                    statusLabel.setText("Parking modifié avec succès !");
                } catch (Exception e) {
                    statusLabel.setText("Erreur lors de la modification : " + e.getMessage());
                }
            } else {
                statusLabel.setText("Données invalides. Modification annulée.");
            }
        });
    }

    // Cette logique a été déplacée dans ma.emsi.dao.ParkingDAO
    // pour respecter les bonnes pratiques de séparation des responsabilités.
}
