package controller.manager;

import data.Car;
import data.CarCenter;
import data.Client;
import data.Rental;
import data.RentalStatus;
import data.Reservation;
import data.ReservationStatus;
import dialog.Alarm;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sql.QueryCar;
import sql.QueryCarCenter;
import sql.QueryClient;
import sql.QueryDiscount;
import sql.QueryRental;
import sql.QueryReservation;

/**
 * FXML Controller class - Главное окно менеджера
 *
 * @author Admin
 */
public class ManagerStartWindowController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseManagerStart;
    @FXML
    private MenuItem menuItemExit;
    @FXML
    private MenuItem menuItemCar;
    @FXML
    private MenuItem menuItemBrandCar;
    @FXML
    private MenuItem menuItemTypeCar;
    @FXML
    private MenuItem menuItemFuelType;
    @FXML
    private MenuItem menuItemCarCenter;
    @FXML
    private MenuItem menuItemClient;
    @FXML
    private MenuItem menuItemDiscount;
    @FXML
    private MenuItem menuItemAbout;

    private final String PATH_FUEL_TYPE = "/fxml/manager/fuelType.fxml";
    private final String PATH_CAR_TYPE = "/fxml/manager/carType.fxml";
    private final String PATH_CAR_BRAND = "/fxml/manager/carBrand.fxml";
    private final String PATH_CAR_CENTER = "/fxml/manager/carCenter.fxml";
    private final String PATH_DISCOUNT = "/fxml/manager/discount.fxml";
    private final String PATH_CAR = "/fxml/manager/car.fxml";
    private final String PATH_SHOW_CLIENT = "/fxml/manager/showClient.fxml";
    private final String PATH_ABOUT = "/fxml/main/about.fxml";

    @FXML
    private TableView<Reservation> tableViewApplication;
    @FXML
    private TableColumn<Reservation, Long> tableColumnId;
    @FXML
    private TableColumn<Reservation, LocalDate> tableColumnDateApplication;
    @FXML
    private TableColumn<Reservation, LocalDate> tableColumnDateReceiving;
    @FXML
    private TableColumn<Reservation, LocalDate> tableColumnDateReturn;
    @FXML
    private TableColumn<Reservation, ReservationStatus> tableColumnReservationStatus;
    @FXML
    private ComboBox<ReservationStatus> comboBoxReservationStatus;
    @FXML
    private Button buttonApplicationCancel;
    @FXML
    private Button buttonApplicationOk;
    @FXML
    private Button buttonRefresh;

    @FXML
    private Label labelClientFIO;
    @FXML
    private Label labelClientPassport;
    @FXML
    private Label labelClientBirthday;
    @FXML
    private Label labelClientPhone;
    @FXML
    private Label labelApplicationCarCenterReceiving;
    @FXML
    private Label labelApplicationCarCenterReturn;
    @FXML
    private Label labelApplicationTotalPrice;
    @FXML
    private Label labelCarBrand;
    @FXML
    private Label labelCarModel;
    @FXML
    private Label labelCarType;
    @FXML
    private Label labelCarColor;
    @FXML
    private Label labelCarNumber;
    @FXML
    private Label labelCarCarCenter;
    @FXML
    private Label labelCarPrice;

    private ObservableList<Reservation> listTableApplication;
    private QueryReservation queryReservation;

    private ObservableList<ReservationStatus> listComboBoxReservationStatus;

    private QueryClient queryClient;
    private QueryCar queryCar;
    private QueryCarCenter queryCarCenter;
    private QueryDiscount queryDiscount;
    private QueryRental queryRental;

    private Car car;
    @FXML
    private AnchorPane anchorPaneCarReservationManager;
    @FXML
    private AnchorPane anchorPaneCarRentalManager;
    @FXML
    private TableView<Rental> tableViewRental;
    @FXML
    private TableColumn<Rental, Long> tableColumnRentalId;
    @FXML
    private TableColumn<Rental, Long> tableColumnRentalIdReservation;
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
    private CheckBox checkBoxDebtors;
    @FXML
    private Button buttonAddCash;
    @FXML
    private Label labelReservationDateReturn;
    @FXML
    private Accordion accordionBaseManagerStartWindow;
    @FXML
    private AnchorPane anchorPaneManagerReservationInfo;

    //private float totalPrice = -1;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        buttonCarReturn.disableProperty().bind(tableViewRental.getSelectionModel().selectedItemProperty().isNull());
        buttonAddCash.disableProperty().bind(tableViewRental.getSelectionModel().selectedItemProperty().isNull().or(checkBoxDebtors.selectedProperty().not()));

        labelReservationDateReturn.setText("");

        //отобразить после запуска вкладку с авто в прокате
        accordionBaseManagerStartWindow.setExpandedPane(accordionBaseManagerStartWindow.getPanes().get(1));

        queryReservation = new QueryReservation();
        queryClient = new QueryClient();
        queryCar = new QueryCar();
        queryCarCenter = new QueryCarCenter();
        queryDiscount = new QueryDiscount();
        queryRental = new QueryRental();

        initComboBox();
        initTableViewApplication();
        initTableRental();
        createListeners();

        showInformation(null);

    }

    /**
     * Инициализация таблицы заявок (забронированных авто)
     */
    private void initTableViewApplication() {

        buttonApplicationOk.disableProperty().bind(tableViewApplication.getSelectionModel().selectedItemProperty().isNull());
        buttonApplicationCancel.disableProperty().bind(tableViewApplication.getSelectionModel().selectedItemProperty().isNull());

        //установка значений для колонок таблицы
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnDateApplication.setCellValueFactory(new PropertyValueFactory<>("date_application"));
        tableColumnDateReceiving.setCellValueFactory(new PropertyValueFactory<>("date_receiving"));
        tableColumnDateReturn.setCellValueFactory(new PropertyValueFactory<>("date_return"));
        tableColumnReservationStatus.setCellValueFactory(new PropertyValueFactory<>("enumReservationStatus"));

        tableViewApplicationDataRefresh(ReservationStatus.Бронь);
        tableViewApplication.setPlaceholder(new Label("Данные отсутствуют"));
    }

    /**
     * Инициализация комбо-боксов
     */
    private void initComboBox() {
        //инициализация комбобоксов бронирования
        listComboBoxReservationStatus = FXCollections.observableArrayList(ReservationStatus.Бронь, ReservationStatus.Одобрено, ReservationStatus.Обработан, ReservationStatus.Отказ);
        comboBoxReservationStatus.setItems(listComboBoxReservationStatus);
        comboBoxReservationStatus.setValue(ReservationStatus.Бронь);

        //инициализация комбобоксов прокатов
        listComboBoxRentalStatus = FXCollections.observableArrayList(RentalStatus.Открыт, RentalStatus.Закрыт);
        comboBoxRentalStatus.setItems(listComboBoxRentalStatus);
        comboBoxRentalStatus.setValue(RentalStatus.Открыт);
    }

    /**
     * Метод добавления слушателей для элементов управления
     */
    private void createListeners() {
        //слушатель выделения строки в таблице заявок (брони)
        tableViewApplication.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Reservation> observable, Reservation oldValue, Reservation newValue) -> {
            showInformation(newValue);
        });

        //слушатель выделения строки в таблице прокатов
        tableViewRental.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Rental> observable, Rental oldValue, Rental newValue) -> {
            if (newValue != null) {
                //получение объекта бронирования
                Reservation reservation = queryReservation.getEntityById(newValue.getReservationId());
                //если текущая дата больше даты возврата, то
                if (LocalDate.now().until(reservation.getDate_return(), ChronoUnit.DAYS) < 0) {
                    labelReservationDateReturn.setStyle("-fx-background-color: RED;");
                } else {
                    labelReservationDateReturn.setStyle("-fx-background-color: BLACK;");
                }
                labelReservationDateReturn.setStyle(reservation.getDate_return().toString());
                //иначе ничего не отображать
            } else {
                labelReservationDateReturn.setText("");
            }
        });

        //слушатель выбора статуса брони
        comboBoxReservationStatus.valueProperty().addListener((ObservableValue<? extends ReservationStatus> observable, ReservationStatus oldValue, ReservationStatus newValue) -> {
            tableViewApplicationDataRefresh(newValue);
        });

        //слушатель выбора статуса проката
        comboBoxRentalStatus.valueProperty().addListener((ObservableValue<? extends RentalStatus> observable, RentalStatus oldValue, RentalStatus newValue) -> {
            tableViewRentalDataRefresh(newValue);
        });

        checkBoxDebtors.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                tableViewRentalDataRefresh(RentalStatus.Закрыт);
                comboBoxRentalStatus.setDisable(true);
                buttonRentalRefresh.setDisable(true);
            } else {
                tableViewRentalDataRefresh(comboBoxRentalStatus.getValue());
                comboBoxRentalStatus.setDisable(false);
                buttonRentalRefresh.setDisable(false);
            }
        });
    }

    /**
     * Инициализация таблицы прокатов (авто в прокате)
     */
    private void initTableRental() {
        //установка значений для колонок таблицы
        tableColumnRentalId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnRentalIdReservation.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        tableColumnRentalCar.setCellValueFactory(new PropertyValueFactory<>("carText"));
        tableColumnRentalDateReceiving.setCellValueFactory(new PropertyValueFactory<>("dateReceiving"));
        tableColumnRentalDateReturn.setCellValueFactory(new PropertyValueFactory<>("dateReturn"));
        tableColumnRentalPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableColumnRentalTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        tableColumnRentalStatus.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));

        tableViewRental.setRowFactory(param -> new ColorRowRental());

        tableViewRentalDataRefresh(RentalStatus.Открыт);

        tableViewRental.setPlaceholder(new Label("Данные отсутствуют"));

    }

    /**
     * Обновление данных в таблице прокатов
     *
     * @param rentalStatus статус проката
     */
    private void tableViewRentalDataRefresh(RentalStatus rentalStatus) {
        listTableRental = FXCollections.observableArrayList(getListRentalWithCar(rentalStatus));
        //tableViewRental.setRowFactory(param -> new ColorRowRental());
        //установка значений для таблицы
        tableViewRental.setItems(listTableRental);
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

        list = queryRental.getRentalByClientAndStatus(0, rentalStatus.getStatus());

        //добавление авто в объекты бронирования списка
        for (int i = 0; i < list.size(); i++) {
            Reservation reservation = queryReservation.getEntityById(list.get(i).getReservationId());
            list.get(i).setCarText(queryCar.getEntityById(reservation.getCar_id()).toString());
        }
        //оставляем только те прокаты, у которых итоговая оплата = 0 и статус Закрыт
        if (checkBoxDebtors.isSelected()) {
            Iterator<Rental> iterator = list.iterator();
            while (iterator.hasNext()) {
                Rental next = iterator.next();
                if (!(next.getTotalPrice() <= 0 && next.getRentalStatus() == RentalStatus.Закрыт)) {
                    iterator.remove();
                }
            }
        }
        return list;
    }

    /**
     * Обновление данных в таблице заявок на получение авто
     *
     * @param reservationStatus статус заявки
     */
    private void tableViewApplicationDataRefresh(ReservationStatus reservationStatus) {
        listTableApplication = FXCollections.observableArrayList(queryReservation.getReservationByClientAndStatus(0, reservationStatus.getStatus()));
        //установка значений для таблицы
        tableViewApplication.setItems(listTableApplication);
    }

    /**
     * Метод отображения расширенной информации о брони. Если reservation =
     * null, то вся информация очистится
     *
     * @param reservation объект бронирования
     */
    private void showInformation(Reservation reservation) {
        if (reservation != null) {
            //получение всех данных из БД
            Client client = queryClient.getEntityById(reservation.getClient_id());
            car = queryCar.getEntityById(reservation.getCar_id());
            CarCenter carCenterReceiving = queryCarCenter.getEntityById(reservation.getCar_center_receiving_id());
            CarCenter carCenterReturn = queryCarCenter.getEntityById(reservation.getCar_center_return_id());

            //заполнение всех полей
            labelClientFIO.setText(client.getFirstName() + " " + client.getPatronymic() + " " + client.getLastName());
            labelClientPassport.setText(client.getPassportSeries() + " " + client.getPassport_id());
            labelClientBirthday.setText(client.getBirthday().toString());
            labelClientPhone.setText(client.getPhone());

            labelCarBrand.setText(car.getCarBrand().toString());
            labelCarModel.setText(car.getCarModel());
            labelCarType.setText(car.getCarType().toString());
            labelCarColor.setText(car.getColor());
            labelCarNumber.setText(car.getRegistrationNumber());
            labelCarCarCenter.setText(car.getCarCenter().toString());
            labelCarPrice.setText(car.getPrice() + "");

            labelApplicationCarCenterReceiving.setText(carCenterReceiving.toString());
            if (carCenterReceiving.equals(car.getCarCenter())) {
                labelApplicationCarCenterReceiving.setStyle("-fx-text-fill: BLACK;");
            } else {
                labelApplicationCarCenterReceiving.setStyle("-fx-text-fill: RED;");
            }
            labelApplicationCarCenterReturn.setText(carCenterReturn.toString());
            labelApplicationTotalPrice.setText(reservation.getPayment() + "руб (-" + reservation.getDiscountPercent() + "%)");
        } else {
            labelClientFIO.setText("");
            labelClientPassport.setText("");
            labelClientBirthday.setText("");
            labelClientPhone.setText("");

            labelCarBrand.setText("");
            labelCarModel.setText("");
            labelCarType.setText("");
            labelCarColor.setText("");
            labelCarNumber.setText("");
            labelCarCarCenter.setText("");
            labelCarPrice.setText("");
            labelApplicationCarCenterReceiving.setText("");
            labelApplicationCarCenterReturn.setText("");
            labelApplicationTotalPrice.setText("");
            //totalPrice = -1;
        }
    }

    @FXML
    private void menuItemExitOnAction() {
        Platform.exit();
    }

    @FXML
    private void menuItemCarOnAction() {
        showAddEditDialog(PATH_CAR, "Автомобили");
    }

    @FXML
    private void menuItemFuelTypeOnAction() {
        showAddEditDialog(PATH_FUEL_TYPE, "Типы топлива");
    }

    @FXML
    private void menuItemCarTypeOnAction() {
        showAddEditDialog(PATH_CAR_TYPE, "Типы авто");
    }

    @FXML
    private void menuItemCarBrandOnAction() {
        showAddEditDialog(PATH_CAR_BRAND, "Марки авто");
    }

    @FXML
    private void menuItemCarCenterOnAction() {
        showAddEditDialog(PATH_CAR_CENTER, "Центры проката");
    }

    @FXML
    private void menuItemDiscountOnAction() {
        showAddEditDialog(PATH_DISCOUNT, "Система скидок");
    }

    @FXML
    private void menuItemClientOnAction() {
        showAddEditDialog(PATH_SHOW_CLIENT, "Список клиентов");
    }

    @FXML
    private void menuItemAboutOnAction() {
        showAddEditDialog(PATH_ABOUT, "О программе");
    }

    /**
     * Метод-слушатель нажатия на кнопку "Одобрить"
     */
    @FXML
    private void buttonApplicationOkOnAction() {
        Reservation selectedItem = tableViewApplication.getSelectionModel().getSelectedItem();
        if (selectedItem.getEnumReservationStatus() == ReservationStatus.Бронь) {
            //подтверждение одобрения бронирования
            boolean confirmation = Alarm.showAlertConfirmation("Бронирование", "Одобрить бронирование данного авто?");

            if (confirmation) {
                boolean updateStatus;
                //если авто находится в центре получения авто, то
                if (car.getCarCenter().getId() == selectedItem.getCar_center_receiving_id()) {
                    updateStatus = queryReservation.updateStatus(selectedItem.getId(), 2, 0, 0, 0);
                    //иначе отправить авто в другой центр
                } else {
                    updateStatus = queryReservation.updateStatus(selectedItem.getId(), 2, car.getId(), 0, selectedItem.getCar_center_receiving_id());
                }
                if (updateStatus) {
                    tableViewApplicationDataRefresh(comboBoxReservationStatus.getValue());
                    Alarm.showAlert(Alert.AlertType.INFORMATION, "Бронирование", "Одобрение бронирования", "Бронирование успешно одобрено");
                } else {
                    Alarm.showAlert(Alert.AlertType.ERROR, "Бронирование", "Одобрение бронирования", "Ошибка работы с БД");
                }
            }
        } else {
            Alarm.showAlert(Alert.AlertType.INFORMATION, "Бронирование", "Одобрение бронирования", "Одобрить можно только заявки со статусом \'Бронь\'.");
        }
    }

    /**
     * Метод-слушатель нажатия на кнопку "Отказать"
     */
    @FXML
    private void buttonApplicationCancelOnAction() {
        if (tableViewApplication.getSelectionModel().getSelectedItem().getEnumReservationStatus() == ReservationStatus.Бронь) {
            //подтверждение отказа бронирования
            boolean confirmation = Alarm.showAlertConfirmation("Бронирование", "Отказать в бронировании авто?");

            if (confirmation) {
                boolean updateStatus = queryReservation.updateStatus(tableViewApplication.getSelectionModel().getSelectedItem().getId(), 3, tableViewApplication.getSelectionModel().getSelectedItem().getCar_id(), 1, 0);
                if (updateStatus) {
                    tableViewApplicationDataRefresh(comboBoxReservationStatus.getValue());
                    Alarm.showAlert(Alert.AlertType.INFORMATION, "Бронирование", "Отказ бронирования", "Отказ в бронировании успешно выполнен");
                } else {
                    Alarm.showAlert(Alert.AlertType.ERROR, "Бронирование", "Отказ в бронировании", "Ошибка работы с БД");
                }
            }
        } else {
            Alarm.showAlert(Alert.AlertType.ERROR, "Бронирование", "Отказ бронирования", "Заявка уже обработана. Отказ невозможен.");
        }
    }

    /**
     * Метод-слушатель нажатия на кнопку "Обновить"
     */
    @FXML
    private void buttonRefreshOnAction() {
        tableViewApplicationDataRefresh(comboBoxReservationStatus.getValue());
    }

    /**
     * Метод отображения диалогового окна со справочником
     *
     * @param path путь к графическому файлу справочника
     * @param title название окна
     */
    private void showAddEditDialog(String path, String title) {
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
            dialogStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ManagerStartWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Метод-слушатель нажатия на кнопку "Вернуть авто"
     *
     */
    @FXML
    private void buttonCarReturnCloseOnAction() {
        //получение объекта проката
        Rental rental = tableViewRental.getSelectionModel().getSelectedItem();
        if (rental.getRentalStatus() == RentalStatus.Открыт) {
            boolean confirmation = Alarm.showAlertConfirmation("Возврат авто", "Вы точно хотите закрыть заявку и вернуть авто?");
            if (confirmation) {
                LocalDate now = LocalDate.now();

                //получение объекта бронирования
                Reservation reservation = queryReservation.getEntityById(rental.getReservationId());
                if (reservation == null) {
                    Alarm.showAlert(Alert.AlertType.ERROR, "Ошибка", "Получение данных", "Ошибка получения данных.");
                    return;
                }

                //если заявленная дата возврата еще наступила (вычислениче разницы между текущей датой и заявленной меньше 0), то
                if (now.until(reservation.getDate_return(), ChronoUnit.DAYS) < 0) {
                    rental.setTotalPrice(0);
                    //получение объекта авто
                    Car car = queryCar.getEntityById(reservation.getCar_id());
                    if (car == null) {
                        Alarm.showAlert(Alert.AlertType.ERROR, "Ошибка", "Получение данных", "Ошибка получения данных.");
                        return;
                    }
                    //установить фактическую дату возврата авто
                    rental.setDateReturn(now);
                    rental.setRentalStatus(RentalStatus.Закрыт);
                    //вернуть авто - изменить данные в БД
                    boolean close = queryRental.closeRental(rental, reservation.getCar_id());
                    //если изменения в БД прошли успешно, то
                    if (close) {
                        Alarm.showAlert(Alert.AlertType.INFORMATION, "Прокат авто", "Возврат авто", "Заявка успешно закрыта");
                        tableViewRentalDataRefresh(comboBoxRentalStatus.getValue());
                        tableViewRentalDataRefresh(comboBoxRentalStatus.getValue());
                    } else {
                        Alarm.showAlert(Alert.AlertType.ERROR, "Прокат авто", "Возврат авто", "Ошибка возврата авто.");
                    }
                }
            } else {
                Alarm.showAlert(Alert.AlertType.WARNING, "Прокат авто", "Возврат авто", "Заявленная дата возврата больше текущей.");
            }
        } else {
            Alarm.showAlert(Alert.AlertType.ERROR, "Прокат авто", "Возврат авто", "Данная заявка закрыта. Автомобиль уже вернули.");
        }
    }

    /**
     * Метод-слушатель нажатия на кнопку "Обновить" для таблицы прокатов
     *
     */
    @FXML
    private void buttonRentalRefreshOnAction() {
        tableViewRentalDataRefresh(comboBoxRentalStatus.getValue());
    }

    /**
     * Метод-слушатель нажатия на кнопку "Доплатить" для таблицы прокатов
     *
     */
    @FXML
    private void buttonAddCashOnAction() {
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
            boolean confirmation = Alarm.showAlertConfirmation("Штраф", "Штраф за несвоевременный возврат авто\nсоставляет " + fine + " руб.\n Доплатить и завершить прокат?");
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
     * методом для выделения строк в таблице "Автомобили в прокате"
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
