package controller.manager;

import data.CarCenter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class - Добавление/изменение центра проката авто
 *
 * @author Admin
 */
public class AddEditCarCenterController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseAddEditDictionary;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldCity;
    @FXML
    private TextField textFieldStreet;
    @FXML
    private TextField textFieldHouse;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonClose;
    
    private CarCenter carCenter;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        
        buttonAdd.disableProperty().bind(textFieldName.textProperty().isEmpty()
                .or(textFieldCity.textProperty().isEmpty())
                .or(textFieldStreet.textProperty().isEmpty())
                .or(textFieldHouse.textProperty().isEmpty()));
        
    }
    
    /**
     * Метод - слушатель нажатия на кнопку Ок
     */
    @FXML
    private void buttonAddOnAction() {
        
        //если объект равен null, то добавляем новый тип
        if (carCenter == null) {
            //создание новый объект
            carCenter = new CarCenter(textFieldName.getText(), textFieldCity.getText(), 
                    textFieldStreet.getText(), textFieldHouse.getText());
        } else {
            carCenter.setName(textFieldName.getText());
            carCenter.setCity(textFieldCity.getText());
            carCenter.setStreet(textFieldStreet.getText());
            carCenter.setHouse(textFieldHouse.getText());
        }
        closeWindow();
        
    }

    /**
     * Слушатель нажатия на кнопку Отмена
     */
    @FXML
    private void buttonCloseOnAction() {
        carCenter = null;
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
     * Установка типа центра проката
     * @param carCenter центр проката
     */
    public void setCarCenter(CarCenter carCenter){
        this.carCenter = carCenter;
        textFieldName.setText(carCenter.getName());
        textFieldCity.setText(carCenter.getCity());
        textFieldStreet.setText(carCenter.getStreet());
        textFieldHouse.setText(carCenter.getHouse());
    }
    
    /**
     * Получение центра проката
     * @return объет типа CarCenter
     */
    public CarCenter getCarCenter(){
        return this.carCenter;
    }      
    
}
