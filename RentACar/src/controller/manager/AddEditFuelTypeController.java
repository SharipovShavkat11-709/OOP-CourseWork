package controller.manager;

import data.FuelType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class - Добавление/изменение типа топлива
 *
 * @author Admin
 */
public class AddEditFuelTypeController implements Initializable {
    
    private FuelType fuelType;
    @FXML
    private AnchorPane anchorPaneBaseAddEditDictionary;
    @FXML
    private TextField textFieldName;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonClose;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        
        buttonAdd.disableProperty().bind(textFieldName.textProperty().isEmpty());
        
    }
    
    /**
     * Метод - слушатель нажатия на кнопку Ок
     */
    @FXML
    private void buttonAddOnAction() {
        
        //если объект равен null, то добавляем новый тип
        if (fuelType == null) {
            //создание новый объект
            fuelType = new FuelType(textFieldName.getText());           
        } else {
            fuelType.setName(textFieldName.getText());
        }
        closeWindow();
        
    }

    /**
     * Слушатель нажатия на кнопку Отмена
     */
    @FXML
    private void buttonCloseOnAction() {
        fuelType = null;
        closeWindow();
    }
    
    /**
     * Метод закрытия диалогового окна
     */
    private void closeWindow(){
        Stage stage = (Stage) anchorPaneBaseAddEditDictionary.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Установка типа топлива
     * @param fuelType тип топлива
     */
    public void setFuelType(FuelType fuelType){
        this.fuelType = fuelType;
        textFieldName.setText(fuelType.getName());
    }
    
    /**
     * Получение типа топлива
     * @return объет типа FuelType
     */
    public FuelType getFuelType(){
        return this.fuelType;
    }
    
}
