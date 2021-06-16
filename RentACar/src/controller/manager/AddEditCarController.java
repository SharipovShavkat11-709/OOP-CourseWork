package controller.manager;

import data.Car;
import data.CarBrand;
import data.CarCenter;
import data.CarType;
import data.FuelType;
import data.CarStatus;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sql.QueryCarBrand;
import sql.QueryCarCenter;
import sql.QueryCarType;
import sql.QueryFuelType;

/**
 * FXML Controller class - Добавление/изменение авто
 *
 * @author Admin
 */
public class AddEditCarController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseAddEditDictionary;
    @FXML
    private ComboBox<CarBrand> comboBoxCarBrand;
    @FXML
    private TextField textFieldCarModel;
    @FXML
    private ComboBox<CarType> comboBoxCarType;
    @FXML
    private TextField textFieldColor;
    @FXML
    private TextField textFieldCarNumber;
    @FXML
    private TextField textFieldCarRegion;
    @FXML
    private TextField textFieldPrice;
    @FXML
    private ComboBox<FuelType> comboBoxFuelType;
    @FXML
    private ComboBox<CarCenter> comboBoxCarCenter;
    @FXML
    private ComboBox<CarStatus> comboBoxStatus;
    @FXML
    private Button buttonAddPhoto;
    @FXML
    private Label labelPhotoPath;
    @FXML
    private TextField textFieldConsumption;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonClose;

    private Car car;

    private ObservableList<CarBrand> listCarBrand;
    private ObservableList<CarType> listCarType;
    private ObservableList<FuelType> listFuelType;
    private ObservableList<CarCenter> listCarCenter;
    private ObservableList<CarStatus> listStatus;

    private QueryCarBrand queryCarBrand;
    private QueryCarType queryCarType;
    private QueryFuelType queryFuelType;
    private QueryCarCenter queryCarCenter;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        buttonAdd.disableProperty().bind(comboBoxCarBrand.getSelectionModel().selectedItemProperty().isNull()
                .or(textFieldCarModel.textProperty().isEmpty())
                .or(comboBoxCarType.getSelectionModel().selectedItemProperty().isNull())
                .or(textFieldColor.textProperty().isEmpty())
                .or(textFieldCarNumber.textProperty().isEmpty())
                .or(textFieldCarRegion.textProperty().isEmpty())
                .or(textFieldConsumption.textProperty().isEmpty())
                .or(comboBoxFuelType.getSelectionModel().selectedItemProperty().isNull())
                .or(comboBoxCarCenter.getSelectionModel().selectedItemProperty().isNull())
                .or(textFieldPrice.textProperty().isEmpty())
                .or(comboBoxStatus.getSelectionModel().selectedItemProperty().isNull()));

        queryCarBrand = new QueryCarBrand();
        queryCarType = new QueryCarType();
        queryFuelType = new QueryFuelType();
        queryCarCenter = new QueryCarCenter();

        labelPhotoPath.setText("");

        initComboBox();

    }

    private void initComboBox() {
        listCarBrand = FXCollections.observableArrayList(queryCarBrand.getAll());
        listCarType = FXCollections.observableArrayList(queryCarType.getAll());
        listFuelType = FXCollections.observableArrayList(queryFuelType.getAll());
        listCarCenter = FXCollections.observableArrayList(queryCarCenter.getAll());
        listStatus = FXCollections.observableArrayList(CarStatus.Свободен, CarStatus.Бронь, CarStatus.Занят);

        comboBoxCarBrand.setItems(listCarBrand);
        comboBoxCarType.setItems(listCarType);
        comboBoxFuelType.setItems(listFuelType);
        comboBoxCarCenter.setItems(listCarCenter);
        comboBoxStatus.setItems(listStatus);
    }

    /**
     * Метод - слушатель нажатия на кнопку Ок
     */
    @FXML
    private void buttonAddOnAction() {

        //если объект равен null, то добавляем новый авто
        if (car == null) {

            String path = null;
            if (!labelPhotoPath.getText().isEmpty()) {
                path = labelPhotoPath.getText();
            }
            //создание новый объект
            car = new Car(comboBoxCarBrand.getValue(), textFieldCarModel.getText(),
                    comboBoxCarType.getValue(), textFieldColor.getText(),
                    Float.parseFloat(textFieldConsumption.getText()),
                    comboBoxFuelType.getValue(), textFieldCarNumber.getText() + " " + textFieldCarRegion.getText(), comboBoxCarCenter.getValue(), Float.parseFloat(textFieldPrice.getText()),
                    comboBoxStatus.getValue().getStatus(), path);

        } else {
            //перезадать поля объекту
            car.setCarBrand(comboBoxCarBrand.getValue());
            car.setCarModel(textFieldCarModel.getText());
            car.setCarType(comboBoxCarType.getValue());
            car.setColor(textFieldColor.getText());
            car.setConsumption(Float.parseFloat(textFieldConsumption.getText()));
            car.setFuelType(comboBoxFuelType.getValue());
            car.setRegistrationNumber(textFieldCarNumber.getText() + " " + textFieldCarRegion.getText());
            car.setCarCenter(comboBoxCarCenter.getValue());
            car.setPrice(Float.parseFloat(textFieldPrice.getText()));
            car.setStatus(comboBoxStatus.getValue().getStatus());

        }
        if (!labelPhotoPath.getText().isEmpty()) {
            car.setPhotoPath(labelPhotoPath.getText());
        }
        closeWindow();

    }

    /**
     * Слушатель нажатия на кнопку Отмена
     */
    @FXML
    private void buttonCloseOnAction() {
        car = null;
        closeWindow();
    }

    @FXML
    private void buttonAddPhotoOnAction() {
        //Диалоговое окно
        FileChooser fc = new FileChooser();
        fc.setTitle("Окно выбора файла");
        //Путь к файлу
        File file = fc.showOpenDialog((Stage) anchorPaneBaseAddEditDictionary.getScene().getWindow());
        if (file != null) {
            //car.setPhotoPath(file.getAbsolutePath());
            labelPhotoPath.setText(file.getAbsolutePath());
        }
    }

    /**
     * Слушатеть событий типа нажатой клавиши в текстовом поле Расход топлива.
     * Сравнивается введенный текст + нажатая клавиша с регулярным выражением, и
     * если оно не подходит по условию, - то событие прерывается ^ - начало
     * строки [0-9]* - 0 и более цифр [.] - точка([,.]? - одна или ноль запятых
     * или точек) [0-9]{0,2} - 0-2 цифры $ - конец строки.
     *
     * @param event
     */
    @FXML
    private void textFieldConsumptionKeyTyped(KeyEvent event) {
        //Получение массива значений введенного символа
        char[] ch = event.getCharacter().toCharArray();
        String text = textFieldConsumption.getText();
        text += event.getCharacter().toCharArray()[0];
        System.out.println("ENTER");
        if (!text.matches("^[0-9]*[.]?[0-9]{0,2}$")) {
            //Прерывание события
            System.out.println("ENTER_IF");
            event.consume();

        }
    }

    /**
     * Слушатеть событий типа нажатой клавиши в текстовом поле Цена.
     * Сравнивается введенный текст + нажатая клавиша с регулярным выражением, и
     * если оно не подходит по условию, - то событие прерывается ^ - начало
     * строки [0-9]* - 0 и более цифр [.] - точка([,.]? - одна или ноль запятых
     * или точек) [0-9]{0,2} - 0-2 цифры $ - конец строки.
     *
     * @param event
     */
    @FXML
    private void textFieldPriceKeyTyped(KeyEvent event) {
        //Получение массива значений введенного символа
        char[] ch = event.getCharacter().toCharArray();
        String text = textFieldPrice.getText();
        text += event.getCharacter().toCharArray()[0];
        if (!text.matches("^[0-9]*[.]?[0-9]{0,2}$")) {
            //Прерывание события
            event.consume();
        }
    }

    /**
     * Метод закрытия диалогового окна
     */
    private void closeWindow() {
        Stage stage = (Stage) anchorPaneBaseAddEditDictionary.getScene().getWindow();
        stage.close();
    }

    /**
     * Установка автомобиля
     *
     * @param car автомобиль
     */
    public void setCar(Car car) {
        this.car = car;
        comboBoxCarBrand.setValue(car.getCarBrand());
        textFieldCarModel.setText(car.getCarModel());
        comboBoxCarType.setValue(car.getCarType());
        textFieldColor.setText(car.getColor());
        textFieldConsumption.setText("" + car.getConsumption());
        comboBoxFuelType.setValue(car.getFuelType());
        textFieldCarNumber.setText(car.getRegistrationNumber().split(" ")[0]);
        textFieldCarRegion.setText(car.getRegistrationNumber().split(" ")[1]);
        comboBoxCarCenter.setValue(car.getCarCenter());
        textFieldPrice.setText("" + car.getPrice());
        switch (car.getStatus()) {
            case 1:
                comboBoxStatus.setValue(CarStatus.Свободен);
                break;
            case 2:
                comboBoxStatus.setValue(CarStatus.Бронь);
                break;
            default:
                comboBoxStatus.setValue(CarStatus.Занят);
                break;
        }
    }

    /**
     * Получение автомобиля
     *
     * @return объет типа Car
     */
    public Car getCar() {
        return this.car;
    }

}
