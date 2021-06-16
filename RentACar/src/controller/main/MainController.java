package controller.main;

import data.Client;
import data.Manager;
import dialog.Alarm;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;
import sql.MySQLConnection;
import sql.QueryEnterUser;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseMain;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private Button buttonClose;
    @FXML
    private CheckBox checkBoxManager;
    @FXML
    private Hyperlink hyperlinkRegistration;
    @FXML
    private Circle circleConnection;
    @FXML
    private Button buttonEnter;
    
    @FXML
    private Label labelConnectionOK;
    
    private boolean connected = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkConnection();
        
        buttonEnter.disableProperty().bind(textFieldLastName.textProperty().isEmpty()
                .or(passwordFieldPassword.textProperty().isEmpty()).or(labelConnectionOK.textProperty().isEqualTo("NO")));
    }

    /**
     * Метод проверки связи с базой данных
     */
    private void checkConnection() {
        Connection connection = MySQLConnection.getConnection(Main.LOGIN, Main.PASSWORD);
        if (connection != null) {
            circleConnection.setFill(Color.GREEN);
            circleConnection.setStroke(Color.GREEN);
            labelConnectionOK.setText("OK");
            connected = true;
        } else {
            circleConnection.setFill(Color.RED);
            circleConnection.setStroke(Color.RED);
            labelConnectionOK.setText("NO");
        }
    }
    
    /**
     * Метод-слушатель нажатия на кнопку Войти
     */
    @FXML
    private void buttonEnterOnAction(){
        if(connected){
        QueryEnterUser qeu = new QueryEnterUser();
        //если указан менеджер, то
        if(checkBoxManager.isSelected()){
            //поиск в БД менеджера
            Manager enterManager = qeu.enterManager(textFieldLastName.getText(), passwordFieldPassword.getText());
            //если менеджер найден, то
            if(enterManager != null){
                Main.manager = enterManager;
                //загрузить стартовое окно менеджера
                showStartWindow(true);
            } else {
                //отобразить диалоговое окно с ошибкой входа
                Alarm.showAlert(Alert.AlertType.WARNING, "Вход в систему", "Ошибка входа", "В БД такой менеджер отсутствует!");
            }
            //если это вход клиента, то
        } else {
            //поиск в БД клиента
            Client enterClient = qeu.enterClient(textFieldLastName.getText(), passwordFieldPassword.getText());
            //если клиент найден, то
            if(enterClient != null){
                Main.client = enterClient;
                //загрузить стартовое окно клиента
                showStartWindow(false);
            } else {
                //отобразить диалоговое окно с ошибкой входа
                Alarm.showAlert(Alert.AlertType.WARNING, "Вход в систему", "Ошибка входа", "В БД такой клиент отсутствует! Пройдите регистрацию в системе!");
            }
        }
        } else {
            Alarm.showAlert(Alert.AlertType.ERROR, "Связь с БД", "Ошибка соединения", "Отсутствует соединение с БД");
        }
    }
    
    /**
     * Метод-слушатель нажатия на кнопку Закрыть
     */
    @FXML
    private void buttonCloseOnAction(){
        Platform.exit();
    }
    
    @FXML
    private void hyperlinkRegistrationOnAction(){
        showRegistrationDialog();
    }
    
    /**
     * Метод загрузки стартового окна для менеджеда или клиента
     * @param manager true - загрузка окна для менеджера, иначе - для клиента
     */
    private void showStartWindow(boolean manager) {
        //путь к графическому файлу
        String path = null;
        //если указан менеджер, то
        if(manager){
            path = "/fxml/manager/managerStartWindow.fxml";
            
        //если указан клиент, то
        } else {
            path = "/fxml/client/clientStartWindow.fxml";
        }
        Parent node = null;
        try {
            node = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error load Node");
        }
        Scene s = new Scene(node);
        Stage stage = (Stage) anchorPaneBaseMain.getScene().getWindow();
        stage.setScene(s);
        if(manager){
            stage.setTitle("Аренда автомобилей >>>Менеджер<<<");
        } else {
            stage.setTitle("Аренда автомобилей >>>Клиент<<<");
        }
        stage.setResizable(true);
    }
    
    /**
     * Метод отображения диалогового окна регистрации клиента
     */
    private void showRegistrationDialog() {
        try {
            //класс-загрузчик fxml файлов
            FXMLLoader loader = new FXMLLoader();
            //загружаем файл
            loader.setLocation(getClass().getResource("/fxml/client/registrationEditClient.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();

            dialogStage.setTitle("Регистрация клиента");

            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            //запрет изменения размеров окна
            dialogStage.setResizable(false);
            //Стиль рамки
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
