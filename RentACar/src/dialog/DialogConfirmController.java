package dialog;

import java.net.URL;
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
public class DialogConfirmController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseDialogConfirm;
    @FXML
    private Label labelText;
    @FXML
    private Button buttonOk;
    @FXML
    private Button buttonCancel;
    
    private boolean confirmation;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    private void buttonOkOnAction(){
        confirmation = true;
        closeWindow();
    }
    
    @FXML
    private void buttonCancelOnAction(){
        confirmation = false;
        closeWindow();
    }
    
    private void closeWindow(){
        Stage stage = (Stage) anchorPaneBaseDialogConfirm.getScene().getWindow();
        stage.close();
    }

    public void setTextConfirm(String text){
        labelText.setText(text);
    }
    
    public boolean confirmation(){
        return this.confirmation;
    }
    
}
