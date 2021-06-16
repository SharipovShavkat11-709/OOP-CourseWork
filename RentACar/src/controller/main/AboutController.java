package controller.main;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class AboutController implements Initializable {

    @FXML
    private Label labelDate;
    @FXML
    private AnchorPane anchorPaneAbout;
    @FXML
    private Button buttonOk;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelDate.setText(labelDate.getText() + LocalDate.now().getYear());        
    }  
    
    @FXML
    private void buttonOkOnAction(){
        Stage stage = (Stage) anchorPaneAbout.getScene().getWindow();
        stage.close();
    }
    
}
