package ma.emsi.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ma.emsi.App;
import ma.emsi.entities.Administrateur;
import ma.emsi.entities.Conducteur;
import ma.emsi.entities.Utilisateur;
import ma.emsi.util.HibernateUtil;
import org.hibernate.Session;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private RadioButton adminRadio;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String email = emailField.getText() != null ? emailField.getText().trim() : "";
        String password = passwordField.getText();
        boolean isAdmin = adminRadio.isSelected();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("Tentative de connexion : " + email);

            Utilisateur user = session
                    .createQuery("from Utilisateur where lower(email) = lower(:email)", Utilisateur.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (user == null) {
                errorLabel.setText("Erreur : Utilisateur non trouvé.");
                System.out.println("Connexion échouée : email non trouvé.");
                return;
            }

            if (!user.getMotDePasse().equals(password)) {
                errorLabel.setText("Erreur : Mot de passe incorrect.");
                System.out.println("Connexion échouée : mauvais mot de passe.");
                return;
            }

            // On vérifie que le rôle correspond au choix de l'utilisateur
            String dbRole = user.getRole().toUpperCase();
            String selectedRole = isAdmin ? "ADMIN" : "CONDUCTEUR";

            if (!dbRole.equals(selectedRole)) {
                errorLabel.setText("Désolé, votre rôle est " + dbRole +
                        " mais vous avez sélectionné " + selectedRole + ".");
                return;
            }

            System.out.println("Connexion réussie pour : " + email);
            App.setCurrentUser(user);
            App.setCurrentUserRole(dbRole);
            App.setRoot("MainView");
        } catch (Exception e) {
            errorLabel.setText("Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
