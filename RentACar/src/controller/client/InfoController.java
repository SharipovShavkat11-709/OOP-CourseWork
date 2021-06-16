package controller.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;

/**
 * FXML Controller class - Информация о клиента
 *
 * @author Admin
 */
public class InfoController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseClientInfo;
    @FXML
    private Label labelFirstName;
    @FXML
    private Label labelPatronymic;
    @FXML
    private Label labelLastName;
    @FXML
    private Label labelPassport;
    @FXML
    private Label labelBirthday;
    @FXML
    private Label labelPhone;
    @FXML
    private Button buttonOk;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelFirstName.setText(Main.client.getFirstName());
        labelPatronymic.setText(Main.client.getPatronymic());
        labelLastName.setText(Main.client.getLastName());
        labelPassport.setText(Main.client.getPassportSeries() + " " + Main.client.getPassport_id());
        labelBirthday.setText(Main.client.getBirthday().toString());
        labelPhone.setText(Main.client.getPhone());
    }    

    @FXML
    private void buttonOkOnAction(ActionEvent event) {
        Stage stage = (Stage)anchorPaneBaseClientInfo.getScene().getWindow();
        stage.close();
    }
    
}
