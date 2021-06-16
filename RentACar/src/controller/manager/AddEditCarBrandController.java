package controller.manager;

import data.CarBrand;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class - Добавление/изменение марки авто
 *
 * @author Admin
 */
public class AddEditCarBrandController implements Initializable {

    private CarBrand carBrand;
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
        if (carBrand == null) {
            //создание новый объект
            carBrand = new CarBrand(textFieldName.getText());           
        } else {
            carBrand.setName(textFieldName.getText());
        }
        closeWindow();
        
    }

    /**
     * Слушатель нажатия на кнопку Отмена
     */
    @FXML
    private void buttonCloseOnAction() {
        carBrand = null;
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
     * Установка марки авто
     * @param carBrand марка авто
     */
    public void setCarBrand(CarBrand carBrand){
        this.carBrand = carBrand;
        textFieldName.setText(carBrand.getName());
    }
    
    /**
     * Получение марки авто
     * @return объет типа CarBrand
     */
    public CarBrand getCarBrand(){
        return this.carBrand;
    }    
    
}
