package controller.manager;

import data.CarType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class - Добавление/изменение типа авто
 *
 * @author Admin
 */
public class AddEditCarTypeController implements Initializable {

    private CarType carType;
    
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
        if (carType == null) {
            //создание новый объект
            carType = new CarType(textFieldName.getText());           
        } else {
            carType.setName(textFieldName.getText());
        }
        closeWindow();
        
    }

    /**
     * Слушатель нажатия на кнопку Отмена
     */
    @FXML
    private void buttonCloseOnAction() {
        carType = null;
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
     * Установка типа авто
     * @param carType тип авто
     */
    public void setCarType(CarType carType){
        this.carType = carType;
        textFieldName.setText(carType.getName());
    }
    
    /**
     * Получение типа авто
     * @return объет типа CarType
     */
    public CarType getCarType(){
        return this.carType;
    }   
    
}
