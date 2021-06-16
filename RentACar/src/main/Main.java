package main;

import data.Client;
import data.Manager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Admin
 */
public class Main extends Application {
    
    public static final String LOGIN = "root";
    public static final String PASSWORD = "root";
    
    public static Manager manager;
    public static Client client;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main/main.fxml"));

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Аренда автомобилей");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
