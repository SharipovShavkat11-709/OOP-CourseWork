package controller.manager;

import data.Discount;
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
public class AddEditDiscountController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseAddEditDictionary;
    @FXML
    private TextField textFieldDays;
    @FXML
    private TextField textFieldPercent;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonClose;
    
    private Discount discount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        
        buttonAdd.disableProperty().bind(textFieldDays.textProperty().isEmpty()
                .or(textFieldPercent.textProperty().isEmpty()));
        
    }
    
    /**
     * Метод - слушатель нажатия на кнопку Ок
     */
    @FXML
    private void buttonAddOnAction() {
        
        //если объект равен null, то добавляем новый тип
        if (discount == null) {
            //создание новый объект
            discount = new Discount(Integer.parseInt(textFieldDays.getText()), Float.parseFloat(textFieldPercent.getText()));           
        } else {
            discount.setDays(Integer.parseInt(textFieldDays.getText()));
            discount.setPercent(Float.parseFloat(textFieldPercent.getText()));
        }
        closeWindow();
        
    }

    /**
     * Слушатель нажатия на кнопку Отмена
     */
    @FXML
    private void buttonCloseOnAction() {
        discount = null;
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
     * Установка скидки
     * @param discount скидка
     */
    public void setDiscount(Discount discount){
        this.discount = discount;
        textFieldDays.setText("" + discount.getDays());
        textFieldPercent.setText("" + discount.getPercent());
    }
    
    /**
     * Получение скидки
     * @return объет типа Discount
     */
    public Discount getDiscount(){
        return this.discount;
    }
    
}
