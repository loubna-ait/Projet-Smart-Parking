package ma.emsi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ma.emsi.util.DatabaseSeeder;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static String currentUserRole; // "ADMIN" or "CONDUCTEUR"
    private static ma.emsi.entities.Utilisateur currentUser;

    public static ma.emsi.entities.Utilisateur getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(ma.emsi.entities.Utilisateur user) {
        currentUser = user;
    }

    public static String getCurrentUserRole() {
        return currentUserRole;
    }

    public static void setCurrentUserRole(String role) {
        currentUserRole = role;
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Starting application...");
        DatabaseSeeder.seedIfNeeded(); // S'assurer que les utilisateurs existent
        try {
            Parent root = loadFXML("LoginView");
            System.out.println("FXML loaded successfully");
            scene = new Scene(root, 800, 600);
            stage.setTitle("Smart Parking System - Connexion");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static FXMLLoader getFXMLLoader(String fxml) {
        return new FXMLLoader(App.class.getResource("/ma/emsi/" + fxml + ".fxml"));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(fxml);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
