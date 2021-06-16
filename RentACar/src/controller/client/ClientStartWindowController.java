package controller.client;

import data.Car;
import data.CarBrand;
import data.CarCenter;
import data.CarType;
import data.Discount;
import data.FuelType;
import data.Rental;
import data.RentalStatus;
import data.Reservation;
import data.ReservationStatus;
import dialog.Alarm;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;
import sql.QueryCar;
import sql.QueryCarCenter;
import sql.QueryDiscount;
import sql.QueryRental;
import sql.QueryReservation;

/**
 * FXML Controller class - Главное окно клиента
 *
 * @author Admin
 */
public class ClientStartWindowController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseClientStartWindow;
    @FXML
    private MenuItem menuItemExit;
    @FXML
    private MenuItem menuItemMyInfo;
    @FXML
    private MenuItem menuItemEditMyInfo;
    @FXML
    private MenuItem menuItemAbout;

    private final String PATH_MY_INFO = "/fxml/client/info.fxml";
    private final String PATH_EDIT_MY_INFO = "/fxml/client/registrationEditClient.fxml";
    private final String PATH_ABOUT = "/fxml/main/about.fxml";

    @FXML
    private Accordion accordionBaseClientStartWindow;
    @FXML
    private AnchorPane anchorPaneApplicationsMade;
    @FXML
    private AnchorPane anchorPaneCarRental;
    @FXML
    private AnchorPane anchorPaneCarBooking;

    @FXML
    private TableView<Car> tableViewCar;
    @FXML
    private TableColumn<Car, CarBrand> tableColumnBrand;
    @FXML
    private TableColumn<Car, String> tableColumnModel;
    @FXML
    private TableColumn<Car, CarType> tableColumnCarType;
    @FXML
    private TableColumn<Car, String> tableColumnColor;
    @FXML
    private TableColumn<Car, FuelType> tableColumnFuelType;
    @FXML
    private TableColumn<Car, Float> tableColumnConsumption;
    @FXML
    private TableColumn<Car, Float> tableColumnPrice;

    @FXML
    private Label labelCarAddress;

    @FXML
    private ComboBox<CarCenter> comboBoxCarCenterReceiving;
    @FXML
    private ComboBox<CarCenter> comboBoxCarCenterReturn;
    @FXML
    private DatePicker datePickerReceiving;
    @FXML
    private DatePicker datePickerReturn;
    @FXML
    private Button buttonCalc;
    @FXML
    private Label labelTotalSum;
    @FXML
    private Button buttonMakeReservation;
    @FXML
    private TableView<Discount> tableViewDiscount;
    @FXML
    private TableColumn<Discount, Integer> tableColumnDays;
    @FXML
    private TableColumn<Discount, Float> tableColumnPercent;

    @FXML
    private ImageView imageViewCarCenterReceiving;

    private QueryCar queryCar;
    private ObservableList<Car> listTableViewCar;

    private QueryDiscount queryDiscount;
    private ObservableList<Discount> listTableViewDiscount;

    private QueryCarCenter queryCarCenter;
    private ObservableList<CarCenter> listCarCenterReceiving;
    private ObservableList<CarCenter> listCarCenterReturn;

    private QueryReservation queryReservation;
    private QueryRental queryRental;

    private Popup popup;
    private Dimension screenSize;
    private ImageView imageView;

    private Popup popupCarCenterReceiving;

    private float discountPercent;
    private float payment;

    //********************элементы окна заявок**************************************
    @FXML
    private TableView<Reservation> tableViewReservation;
    @FXML
    private TableColumn<Reservation, String> tableColumnCar;
    @FXML
    private TableColumn<Reservation, Float> tableColumnPayment;
    @FXML
    private TableColumn<Reservation, LocalDate> tableColumnDateApplication;
    @FXML
    private TableColumn<Reservation, LocalDate> tableColumnDateReceiving;
    @FXML
    private TableColumn<Reservation, LocalDate> tableColumnDateReturn;
    @FXML
    private TableColumn<Reservation, ReservationStatus> tableColumnReservationStatus;
    @FXML
    private Button buttonDeleteReservation;
    @FXML
    private Button buttonRefreshReservation;
    @FXML
    private ComboBox<ReservationStatus> comboBoxReservationStatus;
    private ObservableList<ReservationStatus> listComboBoxReservationStatus;
    @FXML
    private Button buttonPayment;

    private ObservableList<Reservation> listTableReservation;
    @FXML
    private TableView<Rental> tableViewRental;
    @FXML
    private TableColumn<Rental, String> tableColumnRentalCar;
    @FXML
    private TableColumn<Rental, LocalDate> tableColumnRentalDateReceiving;
    @FXML
    private TableColumn<Rental, LocalDate> tableColumnRentalDateReturn;
    @FXML
    private TableColumn<Rental, Float> tableColumnRentalPrice;
    @FXML
    private TableColumn<Rental, Float> tableColumnRentalTotalPrice;
    @FXML
    private TableColumn<Rental, RentalStatus> tableColumnRentalStatus;
    @FXML
    private ComboBox<RentalStatus> comboBoxRentalStatus;
    @FXML
    private Button buttonCarReturn;
    @FXML
    private Button buttonRentalRefresh;

    private ObservableList<Rental> listTableRental;
    private ObservableList<RentalStatus> listComboBoxRentalStatus;
    @FXML
    private Button buttonTableCarRefresh;
    @FXML
    private Label labelReservationDateReturn;
    @FXML
    private Button buttonRepay;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        comboBoxCarCenterReceiving.disableProperty().bind(tableViewCar.getSelectionModel().selectedItemProperty().isNull());
        comboBoxCarCenterReturn.disableProperty().bind(comboBoxCarCenterReceiving.valueProperty().isNull());
        datePickerReceiving.disableProperty().bind(comboBoxCarCenterReceiving.valueProperty().isNull());
        datePickerReturn.disableProperty().bind(datePickerReceiving.valueProperty().isNull());

        buttonDeleteReservation.disableProperty().bind(tableViewReservation.getSelectionModel().selectedItemProperty().isNull());
        buttonPayment.disableProperty().bind(tableViewReservation.getSelectionModel().selectedItemProperty().isNull());

        buttonCarReturn.disableProperty().bind(tableViewRental.getSelectionModel().selectedItemProperty().isNull());
        buttonRepay.disableProperty().bind(tableViewRental.getSelectionModel().selectedItemProperty().isNull());
        buttonMakeReservation.setDisable(true);

        buttonCalc.setDisable(true);
        labelTotalSum.setText("");
        labelReservationDateReturn.setText("");
        //отобразить после запуска вкладку с авто в прокате
        accordionBaseClientStartWindow.setExpandedPane(accordionBaseClientStartWindow.getPanes().get(2));

        //объект для работы с запросами в БД
        queryCar = new QueryCar();
        queryCarCenter = new QueryCarCenter();
        queryDiscount = new QueryDiscount();
        queryReservation = new QueryReservation();
        queryRental = new QueryRental();

        initPopupPhoto();
        initPopupCarCenterReceiving();

        initTableCarsAndDiscount();
        initTableReservation();
        initTableRental();

        initComboBox();

        createListeners();

    }

    /**
     * Метод инициализации отображающего окна с фото авто
     */
    private void initPopupPhoto() {
        //объект для отображения фото авто
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(400);
        //всплышающее окно с изображением
        popup = new Popup();
        popup.getContent().add(imageView);
        //вычисление размеров экрана
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Метод инициализации подсказки выбора даты получения авто
     */
    private void initPopupCarCenterReceiving() {
        popupCarCenterReceiving = new Popup();
        Label lab = new Label("1 Если авто находится не в том центре, который выбран,\nто дата должна быть выбрана на день позже.\n2 Дата не может быть выбрана раньше текущей.");
        //lab.getStyleClass().add("popup_car_center_receiving");
        lab.setStyle("-fx-background-color: lightblue;");

        popupCarCenterReceiving.getContent().add(lab);
    }

    /**
     * Метод инициализации таблицы с авто и дисконтом
     */
    private void initTableCarsAndDiscount() {
        //установка значений для колонок таблицы
        tableColumnBrand.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
        tableColumnModel.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        tableColumnCarType.setCellValueFactory(new PropertyValueFactory<>("carType"));
        tableColumnColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        tableColumnFuelType.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
        tableColumnConsumption.setCellValueFactory(new PropertyValueFactory<>("consumption"));
        tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableViewCarDataRefresh();

        tableViewCar.setPlaceholder(new Label("Данные отсутствуют"));

        //установка слушателя выделенной строки в таблице
        tableViewCar.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Car> observable, Car oldValue, Car newValue) -> {
            //если строка выделена, то
            if (newValue != null) {
                //задать значения адреса центра авто
                labelCarAddress.setText("Текущий адрес авто: " + newValue.getCarCenter().getName()
                        + ", г." + newValue.getCarCenter().getCity()
                        + ", ул." + newValue.getCarCenter().getStreet()
                        + ", д." + newValue.getCarCenter().getHouse());
                //получаем изображение авто из БД
                Image image = queryCar.getPhotoByCarId(newValue.getId());
                //если изображение есть, то
                if (image != null) {
                    imageView.setImage(image);
                    popup.hide();
                    popup.show(anchorPaneBaseClientStartWindow, screenSize.width - imageView.getFitWidth(), screenSize.height);

                } else {
                    popup.hide();
                }

                checkComboBoxCarCenterReceiving();
                checkComboBoxCarCenterReturn();
                checkDatePickerReceiving();
                checkDatePickerReturn();

            } else {
                //очистить метку с адресом
                labelCarAddress.setText("");
                popup.hide();
            }

            //если не выбрана не отдна строка в таблице, то очистить все элементы выбора авто
            if (newValue == null) {
                comboBoxCarCenterReceiving.getSelectionModel().clearSelection();
                comboBoxCarCenterReturn.getSelectionModel().clearSelection();
                datePickerReceiving.setValue(null);
                datePickerReturn.setValue(null);
            }
        });

        //установка значений для колонок таблицы
        tableColumnDays.setCellValueFactory(new PropertyValueFactory<>("days"));
        tableColumnPercent.setCellValueFactory(new PropertyValueFactory<>("percent"));

        //создание следящего списка со значениями для таблицы
        listTableViewDiscount = FXCollections.observableArrayList(queryDiscount.getAll());
        //установка значений для таблицы
        tableViewDiscount.setItems(listTableViewDiscount);

        tableViewDiscount.setPlaceholder(new Label("Данные отсутствуют"));

    }

    /**
     * Метод инициализации комбобоксов со списком центров авто
     */
    private void initComboBox() {
        listCarCenterReceiving = FXCollections.observableArrayList(queryCarCenter.getAll());
        listCarCenterReturn = FXCollections.observableArrayList(listCarCenterReceiving);

        comboBoxCarCenterReceiving.setItems(listCarCenterReceiving);
        comboBoxCarCenterReturn.setItems(listCarCenterReturn);

        //инициализация комбобокса со статусами броней
        listComboBoxReservationStatus = FXCollections.observableArrayList(ReservationStatus.Бронь, ReservationStatus.Одобрено, ReservationStatus.Обработан, ReservationStatus.Отказ);
        comboBoxReservationStatus.setItems(listComboBoxReservationStatus);
        comboBoxReservationStatus.setValue(ReservationStatus.Бронь);

        //инициализация комбобокса со статусами проката
        listComboBoxRentalStatus = FXCollections.observableArrayList(RentalStatus.Открыт, RentalStatus.Закрыт);
        comboBoxRentalStatus.setItems(listComboBoxRentalStatus);
        comboBoxRentalStatus.setValue(RentalStatus.Открыт);
    }

    /**
     * Метод добавления слушателей изменения значений в элементах при
     * бронировании авто
     */
    private void createListeners() {
        //слушатель выбора центра получения авто
        comboBoxCarCenterReceiving.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends CarCenter> observable, CarCenter oldValue, CarCenter newValue) -> {
                    checkComboBoxCarCenterReceiving();
                });
        //слушатель выбора центра возврата авто
        comboBoxCarCenterReturn.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends CarCenter> observable, CarCenter oldValue, CarCenter newValue) -> {
                    checkComboBoxCarCenterReturn();
                });
        //слушатель выбора даты получения авто
        datePickerReceiving.valueProperty()
                .addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
                    checkDatePickerReceiving();
                });
        //слушатель выбора даты возврата авто        
        datePickerReturn.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            checkDatePickerReturn();
        });

        //слушатель выбора заявок по выбранному статусу
        comboBoxReservationStatus.valueProperty().addListener((ObservableValue<? extends ReservationStatus> observable, ReservationStatus oldValue, ReservationStatus newValue) -> {
            tableViewReservationDataRefresh(newValue);
        });

        //слушатель выбора проката по выбранному статусу
        comboBoxRentalStatus.valueProperty().addListener((ObservableValue<? extends RentalStatus> observable, RentalStatus oldValue, RentalStatus newValue) -> {
            tableViewRentalDataRefresh(newValue);
        });

    }

    /**
     * Метод проверки корректности данных в комбобоксе получения авто
     */
    private void checkComboBoxCarCenterReceiving() {
        if (comboBoxCarCenterReceiving.getValue() != null) {
            //если выбранный центр отличается от центра, в котором находится авто, то
            if (!comboBoxCarCenterReceiving.getValue().equals(tableViewCar.getSelectionModel().getSelectedItem().getCarCenter())) {
                //если элемент не содержит css класс, то
                if (!comboBoxCarCenterReceiving.getStyleClass().contains("combo_bor_car_center_warning")) {
                    //добавить css-класс
                    comboBoxCarCenterReceiving.getStyleClass().add("combo_bor_car_center_warning");
                }
                //если дата получения уже выбрана, то
                if (datePickerReceiving.getValue() != null) {
                    //к текущей дате добавить 1 день (так как авто не в том центре, который выбран)
                    LocalDate dateNow = LocalDate.now().plusDays(1);
                    //сравнение выбранной даты с текущей
                    int compareTo = datePickerReceiving.getValue().compareTo(dateNow);
                    //если выбранная дата меньше текущей, то есть нет одного дня на транспортировку авто в другой центр, то
                    if (compareTo < 0) {
                        //если элемент не содержит css-класс, то
                        if (!datePickerReceiving.getStyleClass().contains("date_error")) {
                            //добавить css-класс
                            datePickerReceiving.getStyleClass().add("date_error");
                        }
                    } else {
                        datePickerReceiving.getStyleClass().remove("date_error");
                    }
                }
            } else {
                //удалить css-класс
                comboBoxCarCenterReceiving.getStyleClass().remove("combo_bor_car_center_warning");
                //если дата получения уже выбрана, то
                if (datePickerReceiving.getValue() != null) {
                    LocalDate dateNow = LocalDate.now();
                    //сравнение выбранной даты с текущей
                    int compareTo = datePickerReceiving.getValue().compareTo(dateNow);
                    //если выбранная дата меньше текущей, то есть нет одного дня на транспортировку авто в другой центр, то
                    if (compareTo < 0) {
                        //если элемент не содержит css-класс, то
                        if (!datePickerReceiving.getStyleClass().contains("date_error")) {
                            //добавить css-класс
                            datePickerReceiving.getStyleClass().add("date_error");
                        }
                    } else {
                        datePickerReceiving.getStyleClass().remove("date_error");
                    }
                }
            }
        }
        //проверка правильно заполненных всех полей
        checkFields();
    }

    /**
     * Метод проверки корректности данных в комбобоксе возврата авто
     */
    private void checkComboBoxCarCenterReturn() {
        //проверка правильно заполненных всех полей
        checkFields();
    }

    /**
     * Метод проверки корректности данных в выборе даты получения авто
     */
    private void checkDatePickerReceiving() {
        if (datePickerReceiving.getValue() != null) {
            //получение текущей даты
            LocalDate dateNow = LocalDate.now();
            //сравнение выбранного центра и центра, где находится авто
            boolean carCenter = comboBoxCarCenterReceiving.getValue().equals(tableViewCar.getSelectionModel().getSelectedItem().getCarCenter());
            //если центры не равны, то
            if (!carCenter) {
                //добавить 1 день к текущей дате
                dateNow = dateNow.plusDays(1);
            }
            //сравнение выбранной даты с текущей
            int compareTo = datePickerReceiving.getValue().compareTo(dateNow);
            //если выбраннная дата меньше текущей, то
            if (compareTo < 0) {
                //если элемент не содержит css-класс, то
                if (!datePickerReceiving.getStyleClass().contains("date_error")) {
                    //добавить css-класс
                    datePickerReceiving.getStyleClass().add("date_error");
                }
            } else {
                //удалить css-класс
                datePickerReceiving.getStyleClass().remove("date_error");
            }
            //если дата возврата уже задана, то
            if (datePickerReturn.getValue() != null) {
                //получение даты возврата
                LocalDate valueReturn = datePickerReturn.getValue();
                //сравнение даты возврата и даты получения
                compareTo = valueReturn.compareTo(datePickerReceiving.getValue());
                //если дата возврата меньше или равна дате получения, то
                if (compareTo <= 0) {
                    //если элемент не содержит css-класс, то
                    if (!datePickerReturn.getStyleClass().contains("date_error")) {
                        //добавить css-класс
                        datePickerReturn.getStyleClass().add("date_error");
                    }
                } else {
                    //удалить css-класс
                    datePickerReturn.getStyleClass().remove("date_error");
                }
            }
        }
        //проверка правильно заполненных всех полей
        checkFields();
    }

    /**
     * Метод проверки корректности данных в выборе даты возврата авто
     */
    private void checkDatePickerReturn() {
        if (datePickerReturn.getValue() != null) {
            //получение даты получения авто
            LocalDate valueReceiving = datePickerReceiving.getValue();
            //сравнение дат возврата и получения
            int compareTo = datePickerReturn.getValue().compareTo(valueReceiving);
            //если дата возврата меньше или равна дате получения, то
            if (compareTo <= 0) {
                //если элемент не содержит css-класс, то
                if (!datePickerReturn.getStyleClass().contains("date_error")) {
                    //добавить css-класс
                    datePickerReturn.getStyleClass().add("date_error");
                }
            } else {
                //удалить css-класс
                datePickerReturn.getStyleClass().remove("date_error");
            }
        }
        //проверка правильно заполненных всех полей
        checkFields();
    }

    /**
     * Метод проверки правильности заполнения всех полей
     */
    private void checkFields() {
        //деактивация кнопок подсчета суммы и очиста метки с итоговой суммой
        buttonCalc.setDisable(true);
        buttonMakeReservation.setDisable(true);
        labelTotalSum.setText("");
        //если хотя вы одно поле вы выбрано, то выйти из метода
        if (comboBoxCarCenterReceiving.getValue() == null
                || comboBoxCarCenterReturn.getValue() == null
                || datePickerReceiving.getValue() == null
                || datePickerReturn.getValue() == null) {
            return;
        }

        //текущая дата
        LocalDate dateNow = LocalDate.now();
        //сравнение выбранного цетра получения авто с текущим где находится авто
        boolean carCenter = comboBoxCarCenterReceiving.getValue().equals(tableViewCar.getSelectionModel().getSelectedItem().getCarCenter());
        //если центры не разные, то
        if (!carCenter) {
            //добавить к текущей дате 1 день для перевозки авто
            dateNow = dateNow.plusDays(1);
        }

        //сравнение даты получения авто с текущей
        int compareTo = datePickerReceiving.getValue().compareTo(dateNow);
        //если дата меньше текущей, то выйти из метода
        if (compareTo < 0) {
            return;
        }

        //сравнение даты возврата с датой получения авто
        LocalDate valueReceiving = datePickerReceiving.getValue();
        int compareTo2 = datePickerReturn.getValue().compareTo(valueReceiving);
        //если дата возврата меньше или равна дате получения, то выйти из метода
        if (compareTo2 <= 0) {
            return;
        }
        //активация кнопки подсчета стоимости
        buttonCalc.setDisable(false);
    }

    /**
     * Обновление данных в таблице с авто
     */
    private void tableViewCarDataRefresh() {
        //создание следящего списка со значениями для таблицы
        listTableViewCar = FXCollections.observableArrayList(queryCar.getCarByStatus(1));
        //установка значений для таблицы
        tableViewCar.setItems(listTableViewCar);
    }

    @FXML
    private void menuItemExitOnAction() {
        Platform.exit();
    }

    @FXML
    private void menuItemMyInfoOnAction() {
        showDialogWindow(PATH_MY_INFO, "Мои регистрационные данные");
    }

    @FXML
    private void menuItemEditMyInfoOnAction() {
        showDialogWindow(PATH_EDIT_MY_INFO, "Изменение регистрационных данных");
    }

    @FXML
    private void menuItemAboutOnAction() {
        showDialogWindow(PATH_ABOUT, "О программе");
    }

    /**
     * Метод-слушатель наведения на иконку-подсказку мышкой
     * (comboBoxCarCenterReceiving)
     */
    @FXML
    private void imageViewCarCenterReceivingOnMouseMoved() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        popupCarCenterReceiving.show(imageViewCarCenterReceiving, location.x + 15, location.y + 15);
    }

    /**
     * Метод-слушатель потери фокуса на иконке-подсказке мышкой
     * (comboBoxCarCenterReceiving)
     */
    @FXML
    private void imageViewCarCenterReceivingOnMouseReleased() {
        popupCarCenterReceiving.hide();
    }

    @FXML
    private void buttonCalcOnAction() {
        //вычисление количества дней авто в аренде
        int quantityDays = (int) datePickerReceiving.getValue().until(datePickerReturn.getValue(), ChronoUnit.DAYS);
        //процент скидки
        discountPercent = queryDiscount.getPercentByDays(quantityDays);
        //стоимость одного дня
        float priceDay = tableViewCar.getSelectionModel().getSelectedItem().getPrice();
        //итоговая стоимость
        payment = priceDay * quantityDays - priceDay * quantityDays * discountPercent / 100;
        //если авто находится не в центре получения, то увеличить итоговую стоимость на сумму 20% одного дня аренды выбранного авто (услуга перевозки авто в другой центр) 
        if (tableViewCar.getSelectionModel().getSelectedItem().getCarCenter().getId() != comboBoxCarCenterReceiving.getValue().getId()) {
            payment += tableViewCar.getSelectionModel().getSelectedItem().getPrice() * 0.2;
        }
        //текстовая места с итоговой стоимостью
        labelTotalSum.setText("ИТОГО: " + String.format("%.2f", payment) + "руб (-" + discountPercent + "%)");

        buttonMakeReservation.setDisable(false);
        buttonCalc.setDisable(true);
    }

    /**
     * Метод-слушатель нажатия на кнопку "Бронирование"
     */
    @FXML
    private void buttonMakeReservationOnAction() {
        boolean showAlertConfirmation = Alarm.showAlertConfirmation("Бронирование авто", "Вы точно хотите совершить бронирование авто?");

        if (showAlertConfirmation) {
            Reservation reservation = new Reservation(Main.client.getId(), tableViewCar.getSelectionModel().getSelectedItem().getId(),
                    comboBoxCarCenterReceiving.getValue().getId(), comboBoxCarCenterReturn.getValue().getId(),
                    LocalDate.now(), datePickerReceiving.getValue(), datePickerReturn.getValue(), 1, discountPercent, payment);
            boolean add = queryReservation.add(reservation);
            if (add) {
                //отменить выбор строки таблицы с авто
                tableViewCar.getSelectionModel().clearSelection();
                //обновление данных в списке авто
                tableViewCarDataRefresh();
                //обновить данные в таблице заявок
                tableViewReservationDataRefresh(comboBoxReservationStatus.getValue());
                Alarm.showAlert(Alert.AlertType.INFORMATION, "Бронирование", "Бронирование авто", "Бронивование авто прошло успешно\nСумма к оплате: " + String.format("%.2f", payment));
            } else {
                Alarm.showAlert(Alert.AlertType.ERROR, "Бронирование авто", "Ошибка бронирования", "Произошла ошибка бронирования авто.\nПовторите попытку или обратитесь к менеджеру.");
            }
        }

    }

    /**
     * Метод-слушатель нажатия на кнопку "Обновить авто"
     */
    @FXML
    private void buttonTableCarRefreshOnAction() {
        tableViewCarDataRefresh();
    }

    /**
     * Метод отображения диалогового окна
     *
     * @param path путь к графическому файлу справочника
     * @param title название окна
     */
    private void showDialogWindow(String path, String title) {
        try {
            //класс-загрузчик fxml файлов
            FXMLLoader loader = new FXMLLoader();
            //загружаем файл
            loader.setLocation(getClass().getResource(path));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();

            dialogStage.setTitle(title);

            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            //запрет изменения размеров окна
            dialogStage.setResizable(false);
            //Стиль рамки
            dialogStage.initStyle(StageStyle.DECORATED);

            if (path.equals(PATH_EDIT_MY_INFO)) {
                RegistrationEditClientController controller = (RegistrationEditClientController) loader.getController();
                controller.setClient(Main.client);
            }

            dialogStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ClientStartWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Инициализация таблицы заявок (забронированных авто)
     */
    private void initTableReservation() {
        //установка значений для колонок таблицы
        tableColumnCar.setCellValueFactory(new PropertyValueFactory<>("carText"));
        tableColumnPayment.setCellValueFactory(new PropertyValueFactory<>("payment"));
        tableColumnDateApplication.setCellValueFactory(new PropertyValueFactory<>("date_application"));
        tableColumnDateReceiving.setCellValueFactory(new PropertyValueFactory<>("date_receiving"));
        tableColumnDateReturn.setCellValueFactory(new PropertyValueFactory<>("date_return"));
        tableColumnReservationStatus.setCellValueFactory(new PropertyValueFactory<>("enumReservationStatus"));

        tableViewReservationDataRefresh(ReservationStatus.Бронь);

        tableViewReservation.setPlaceholder(new Label("Данные отсутствуют"));

    }

    /**
     * Инициализация таблицы прокатов (авто в прокате)
     */
    private void initTableRental() {
        //установка значений для колонок таблицы
        tableColumnRentalCar.setCellValueFactory(new PropertyValueFactory<>("carText"));
        tableColumnRentalDateReceiving.setCellValueFactory(new PropertyValueFactory<>("dateReceiving"));
        tableColumnRentalDateReturn.setCellValueFactory(new PropertyValueFactory<>("dateReturn"));
        tableColumnRentalPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableColumnRentalTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        tableColumnRentalStatus.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));

        tableViewRental.setRowFactory(param -> new ColorRowRental());

        tableViewRentalDataRefresh(RentalStatus.Открыт);

        tableViewRental.setPlaceholder(new Label("Данные отсутствуют"));

        tableViewRental.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Rental> observable, Rental oldValue, Rental newValue) -> {
            if (newValue != null) {
                labelReservationDateReturn.setText("Заявленная дата возврата: " + queryReservation.getEntityById(newValue.getReservationId()).getDate_return().toString());
            } else {
                labelReservationDateReturn.setText("");
            }
        });

    }

    /**
     * Обновление данных в таблице прокатов
     *
     * @param rentalStatus статус проката
     */
    private void tableViewRentalDataRefresh(RentalStatus rentalStatus) {
        listTableRental = FXCollections.observableArrayList(getListRentalWithCar(rentalStatus));
        //установка значений для таблицы
        tableViewRental.setItems(listTableRental);
        //tableViewRental.setRowFactory(param -> new ColorRowRental());
        tableViewRental.refresh();
    }

    /**
     * Получение списка прокатов с текстовым представлением авто
     *
     * @param rentalStatus статус проката
     * @return список прокатов
     */
    private List<Rental> getListRentalWithCar(RentalStatus rentalStatus) {
        List<Rental> list;

        list = queryRental.getRentalByClientAndStatus(Main.client.getId(), rentalStatus.getStatus());

        //добавление авто в объекты бронирования списка
        for (int i = 0; i < list.size(); i++) {
            Reservation reservation = queryReservation.getEntityById(list.get(i).getReservationId());
            list.get(i).setCarText(queryCar.getEntityById(reservation.getCar_id()).toString());
        }
        return list;
    }

    /**
     * Получение списка бронирований с текстовым представлением авто
     *
     * @param reservationStatus статус бронирования
     * @return список броней
     */
    private List<Reservation> getListReservationWithCar(ReservationStatus reservationStatus) {
        List<Reservation> list;

        list = queryReservation.getReservationByClientAndStatus(Main.client.getId(), reservationStatus.getStatus());

        //добавление авто в объекты бронирования списка
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setCarText(queryCar.getEntityById(list.get(i).getCar_id()).toString());
        }
        return list;
    }

    /**
     * Обновление данных в таблице заявок
     *
     * @param reservationStatus статус бронирования
     */
    private void tableViewReservationDataRefresh(ReservationStatus reservationStatus) {
        listTableReservation = FXCollections.observableArrayList(getListReservationWithCar(reservationStatus));
        //установка значений для таблицы
        tableViewReservation.setItems(listTableReservation);
    }

    /**
     * Метод-слушатель нажатия на кнопку отмены бронирования "Обновить"
     */
    @FXML
    private void buttonRefreshReservationOnAction() {
        tableViewReservationDataRefresh(comboBoxReservationStatus.getValue());
    }

    /**
     * Метод-слушатель нажатия на кнопку отмены бронирования "Отменить"
     */
    @FXML
    private void buttonDeleteReservationOnAction() {
        Reservation selectedItem = tableViewReservation.getSelectionModel().getSelectedItem();
        //если установлен статус Бронь, то (есть возможность отмены регистрации)
        if (selectedItem.getEnumReservationStatus() == ReservationStatus.Бронь) {
            //подтверждение отмены бронирования
            boolean cancelReservation = Alarm.showAlertConfirmation("Отмена бронирования", "Вы уверены, что хотите отменить бронирование?");
            //если да, то
            if (cancelReservation) {
                //получение объекта бронирования

                //удаление из БД
                boolean cancel = queryReservation.cancel(selectedItem.getId(), selectedItem.getCar_id());
                //если удаление прошло успешно, то
                if (cancel) {
                    tableViewReservationDataRefresh(comboBoxReservationStatus.getValue());
                    tableViewCarDataRefresh();
                    //если не удалось удалить, то
                } else {
                    Alarm.showAlert(Alert.AlertType.ERROR, "Ошибка", "Отмена бронирования", "Возникла ошибка при отмене бронирования. Свяжитесь с менеджером.");

                }
            }
        } else {
            Alarm.showAlert(Alert.AlertType.WARNING, "Отмена бронирования", "Отказ отмена бронирования", "Данная заявка уже обработана менеджером. Удаление не возможно.");
        }
    }

    /**
     * Метод-слушатель нажатия на кнопку отмены бронирования "Оплатить и
     * получить авто"
     */
    @FXML
    private void buttonPaymentReservationOnAction() {
        Reservation selectedItem = tableViewReservation.getSelectionModel().getSelectedItem();
        if (selectedItem.getEnumReservationStatus() == ReservationStatus.Одобрено) {
            LocalDate now = LocalDate.now();
            //если дата получение авто меньше или равна текущей и дата возврата авто больше текущей, то пожно получить авто в прокат
            //System.out.println("now.until(selectedItem.getDate_receiving(), ChronoUnit.DAYS) = " + now.until(selectedItem.getDate_receiving(), ChronoUnit.DAYS));
            //System.out.println("now.until(selectedItem.getDate_return(), ChronoUnit.DAYS) = " + now.until(selectedItem.getDate_return(), ChronoUnit.DAYS));
            //System.out.println("selectedItem.getDate_receiving() = " + selectedItem.getDate_receiving());
            //System.out.println("selectedItem.getDate_return() = " + selectedItem.getDate_return());
            //System.out.println("now = " + now);
            if (now.until(selectedItem.getDate_receiving(), ChronoUnit.DAYS) <= 0 && now.until(selectedItem.getDate_return(), ChronoUnit.DAYS) > 0) {
                //подтверждение оплаты
                boolean confirmation = Alarm.showAlertConfirmation("Подтверждение оплаты", "Сумма к оплате: " + selectedItem.getPayment() + " руб.\nГотовы внести данную сумму к оплате?");
                if (confirmation) {
                    payment = selectedItem.getPayment();
                    //создание объекта аренды авто
                    Rental rental = new Rental(selectedItem.getId(), payment, RentalStatus.Открыт, now, null);
                    //добавление данных в БД
                    boolean addSetCarStatusBusy = queryRental.addAndSetCarBusyReservationProcessed(rental, selectedItem.getCar_id(), selectedItem.getId());
                    //если данные успешно добавлены, то
                    if (addSetCarStatusBusy) {
                        Alarm.showAlert(Alert.AlertType.INFORMATION, "Оплата", "Оплата и получение авто", "Оплата прошла успешно. Ожидайте получения авто");
                        tableViewReservationDataRefresh(comboBoxReservationStatus.getValue());
                    } else {
                        Alarm.showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка оплаты авто", "Возникла ошибка во время оплаты авто. Свяжитесь с менеджером.");
                    }
                }
            } else {
                if (now.until(selectedItem.getDate_receiving(), ChronoUnit.DAYS) > 0) {
                    Alarm.showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка оплаты авто", "Вы не можете получить авто раньше забронированной даты");
                    return;
                }
                if (now.until(selectedItem.getDate_return(), ChronoUnit.DAYS) < 0) {
                    Alarm.showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка оплаты авто", "Вы не можете получить авто позже забронированной даты возврата");
                    return;
                }
            }
        } else {
            Alarm.showAlert(Alert.AlertType.INFORMATION, "Оплата", "Оплата и получение авто", "Только статус \'Одобрено\' позволяет оплатить и получить авто.");
        }
    }

    /**
     * Метод-слушатель нажатия на кнопку обновления таблицы со списком прокатов
     * "Обновить"
     */
    @FXML
    private void buttonRentalRefreshOnAction() {
        tableViewRentalDataRefresh(comboBoxRentalStatus.getValue());
    }

    /**
     * Метод-слушатель нажатия на кнопку "Вернуть авто"
     */
    @FXML
    private void buttonCarReturnOnAction() {
        LocalDate now = LocalDate.now();
        //получение объекта проката
        Rental rental = tableViewRental.getSelectionModel().getSelectedItem();

        if (rental.getRentalStatus() == RentalStatus.Открыт) {
            //получение объекта бронирования
            Reservation reservation = queryReservation.getEntityById(rental.getReservationId());
            if (reservation == null) {
                Alarm.showAlert(Alert.AlertType.ERROR, "Ошибка", "Получение данных", "Ошибка получения данных. Обратитесь к менеджеру.");
                return;
            }
            //вычисление даты возврата к текущей дате
            long until = reservation.getDate_return().until(now, ChronoUnit.DAYS);
            //если заданная дата возврата меньше или равна фактической дате возврата (текущей), то
            if (until <= 0) {
                //установить итоговую стоимость аренды как первоначальную
                rental.setTotalPrice(rental.getPrice());
                //иначе
            } else {
                //получение объекта авто
                Car car = queryCar.getEntityById(reservation.getCar_id());
                if (car == null) {
                    Alarm.showAlert(Alert.AlertType.ERROR, "Ошибка", "Получение данных", "Ошибка получения данных. Обратитесь к менеджеру.");
                    return;
                }
                //вычисление размера штрафа
                float fine = (float) (car.getPrice() * until * 1.25);
                //подтверждение оплаты штрафа за просроченный возврат
                boolean confirmationError = Alarm.showAlertConfirmation("Штраф", "Штраф за несвоевременный возврат авто\nсоставляет " + fine + " руб.\n Оплатить и вернуть авто?");
                if (!confirmationError) {
                    return;
                }
                //установить итоговую стоимость аренды
                rental.setTotalPrice(rental.getPrice() + fine);
            }
            //установить фактическую дату возврата авто
            rental.setDateReturn(now);
            rental.setRentalStatus(RentalStatus.Закрыт);
            //вернуть авто - изменить данные в БД
            boolean close = queryRental.closeRental(rental, reservation.getCar_id());
            //если изменения в БД прошли успешно, то
            if (close) {
                Alarm.showAlert(Alert.AlertType.INFORMATION, "Прокат авто", "Закрытие заявки", "Заявка успешно закрыта");
                tableViewCarDataRefresh();
                tableViewRentalDataRefresh(comboBoxRentalStatus.getValue());
            } else {
                Alarm.showAlert(Alert.AlertType.ERROR, "Прокат авто", "Закрытие заявки", "Ошибка закрытия заявки. Обратитесь к менеджеру.");
            }
        } else {
            Alarm.showAlert(Alert.AlertType.WARNING, "Прокат авто", "Закрытие заявки", "Данная заявку уже закрыта");
        }
    }

    /**
     * Метод-слушатель нажатия на кнопку "Вернуть долг" для таблицы прокатов
     *
     */
    @FXML
    private void buttonRepayOnAction() {
        LocalDate now = LocalDate.now();
        //получение объекта проката
        Rental rental = tableViewRental.getSelectionModel().getSelectedItem();
        if (rental.getRentalStatus() == RentalStatus.Закрыт && rental.getTotalPrice() <= 0) {
            //получение объекта бронирования
            Reservation reservation = queryReservation.getEntityById(rental.getReservationId());
            if (reservation == null) {
                Alarm.showAlert(Alert.AlertType.ERROR, "Ошибка", "Получение данных", "Ошибка получения данных.");
                return;
            }
            Car car = queryCar.getEntityById(reservation.getCar_id());
            if (car == null) {
                Alarm.showAlert(Alert.AlertType.ERROR, "Ошибка", "Получение данных", "Ошибка получения данных.");
                return;
            }
            //вычисление даты возврата к текущей дате
            long until = reservation.getDate_return().until(now, ChronoUnit.DAYS);

            //вычисление размера штрафа
            float fine = (float) (car.getPrice() * until * 1.25);
            boolean confirmation = Alarm.showAlertConfirmation("Штраф", "Штраф за несвоевременный возврат авто\nсоставляет " + fine + " руб.\n Доплатить за несвоевременный возврат?");
            if (confirmation) {
                boolean updateTotalSum = queryRental.updateTotalSum(rental.getId(), fine);
                //если изменения в БД прошли успешно, то
                if (updateTotalSum) {
                    Alarm.showAlert(Alert.AlertType.INFORMATION, "Прокат авто", "Доплата", "Доплата добавлена. Заявка полность закрыта");
                    tableViewRentalDataRefresh(RentalStatus.Закрыт);
                } else {
                    Alarm.showAlert(Alert.AlertType.ERROR, "Прокат авто", "Доплата", "Ошибка работы с БД.");
                }
            }
        } else {
            if(rental.getRentalStatus() == RentalStatus.Открыт){
                Alarm.showAlert(Alert.AlertType.WARNING, "Доплата", "Доплата за несвоевременный возврат авто", "Данная заявка не просрочена. Доплата не возможна.");
            } else if(rental.getTotalPrice() > 0){
                Alarm.showAlert(Alert.AlertType.WARNING, "Доплата", "Доплата за несвоевременный возврат авто", "Прокат авто полностью оплачен");
            }
        }
    }

    /**
     * Класс, унаследованный от класса TableRow<Rental> с переопределенным
     * методом для выделения строк в таблице "Мои автомобили в прокате"
     */
    private class ColorRowRental extends TableRow<Rental> {

        @Override
        protected void updateItem(Rental item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null) {
                return;
            }

            if (item.getTotalPrice() <= 0 && item.getRentalStatus() == RentalStatus.Закрыт) {
                this.setStyle("-fx-background-color: RED;");
            } else if (item.getRentalStatus() == RentalStatus.Открыт) {
                long until = LocalDate.now().until(queryReservation.getEntityById(item.getReservationId()).getDate_return(), ChronoUnit.DAYS);

                //если сдача авто сегодня, то
                if (until == 0) {
                    this.setStyle("-fx-background-color: BLUE;");
                    //если есть еще 1 день, то
                } else if (until == 1) {
                    this.setStyle("-fx-background-color: ORANGE;");
                    //если есть больше одного дня, то
                } else if (until > 1) {
                    this.setStyle("-fx-background-color: GREEN;");
                    //если просрочена сдача авто, то
                } else if (until < 0) {
                    this.setStyle("-fx-background-color: RED;");
                }

            }
        }

    }

}
