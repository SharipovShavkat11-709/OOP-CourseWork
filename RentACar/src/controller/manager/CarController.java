package controller.manager;

import data.Car;
import data.CarBrand;
import data.CarType;
import data.FuelType;
import dialog.Alarm;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sql.QueryCar;

/**
 * FXML Controller class - Автомобиль
 *
 * @author Admin
 */
public class CarController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseDictionary;
    @FXML
    private AnchorPane anchorPaneManagerShowPhoto;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonAddEditPhoto;
    @FXML
    private Button buttonClose;
    @FXML
    private TableView<Car> tableViewDictionary;
    @FXML
    private TableColumn<Car, Long> tableColumnID;
    @FXML
    private TableColumn<Car, CarBrand> tableColumnBrand;
    @FXML
    private TableColumn<Car, String> tableColumnModel;
    @FXML
    private TableColumn<Car, CarType> tableColumnCarType;
    @FXML
    private TableColumn<Car, String> tableColumnColor;
    @FXML
    private TableColumn<Car, String> tableColumnNumber;
    @FXML
    private TableColumn<Car, FuelType> tableColumnFuelType;
    @FXML
    private TableColumn<Car, Float> tableColumnConsumption;
    @FXML
    private TableColumn<Car, Float> tableColumnPrice;
    @FXML
    private TableColumn<Car, String> tableColumnBusy;
    @FXML
    private SplitPane splitPaneCar;
    @FXML
    private ImageView imageViewCarPhoto;
    @FXML
    private Hyperlink hyperlinkPhotoHide;
    @FXML
    private Hyperlink hyperlinkPhotoDelete;
    @FXML
    private Label labelCarCenterName;
    @FXML
    private Label labelCarCenterCity;
    @FXML
    private Label labelCarCenterStreet;
    @FXML
    private Label labelCarCenterHouse;

    QueryCar query;
    private ObservableList<Car> listTableView;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        splitPaneCar.setDividerPosition(0, 1);

        //объект для работы с запросами в БД
        query = new QueryCar();

        buttonAddEditPhoto.disableProperty().bind(tableViewDictionary.getSelectionModel().selectedItemProperty().isNull());
        buttonEdit.disableProperty().bind(tableViewDictionary.getSelectionModel().selectedItemProperty().isNull());
        buttonDelete.disableProperty().bind(tableViewDictionary.getSelectionModel().selectedItemProperty().isNull());

        hyperlinkPhotoHide.visibleProperty().bind(tableViewDictionary.getSelectionModel().selectedItemProperty().isNotNull());
        hyperlinkPhotoDelete.visibleProperty().bind(tableViewDictionary.getSelectionModel().selectedItemProperty().isNotNull());
        
        initTable();
    }

    /**
     * Метод инициализации таблицы
     */
    private void initTable() {
        //установка значений для колонок таблицы
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnBrand.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
        tableColumnModel.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        tableColumnCarType.setCellValueFactory(new PropertyValueFactory<>("carType"));
        tableColumnColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        tableColumnNumber.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        tableColumnFuelType.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
        tableColumnConsumption.setCellValueFactory(new PropertyValueFactory<>("consumption"));
        tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableColumnBusy.setCellValueFactory(new PropertyValueFactory<>("textBusy"));

        //создание следящего списка со значениями для таблицы
        listTableView = FXCollections.observableArrayList(query.getAll());
        //установка значений для таблицы
        tableViewDictionary.setItems(listTableView);
        
        tableViewDictionary.setPlaceholder(new Label("Данные отсутствуют"));

        //установка слушателя выделенной строки в таблице
        tableViewDictionary.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Car> observable, Car oldValue, Car newValue) -> {
            //если строка выделена, то
            if (newValue != null) {
                //задать значения для центра авто
                labelCarCenterName.setText(newValue.getCarCenter().getName());
                labelCarCenterCity.setText(newValue.getCarCenter().getCity());
                labelCarCenterStreet.setText(newValue.getCarCenter().getStreet());
                labelCarCenterHouse.setText(newValue.getCarCenter().getHouse());
                //получаем изображение авто из БД
                Image image = query.getPhotoByCarId(tableViewDictionary.getSelectionModel().getSelectedItem().getId());
                //если изображение есть, то
                if (image != null) {
                    //добавляем изображение и отображает\м (сдвигом разделителя)
                    imageViewCarPhoto.setImage(image);                   
                    splitPaneCar.setDividerPosition(0, 1 - imageViewCarPhoto.getFitWidth() / anchorPaneBaseDictionary.getPrefWidth());
                } else {
                    //если изображения нет - скрываем разделителем
                    splitPaneCar.setDividerPosition(0, 1);
                }

            } else {
                //очистить все метки
                labelCarCenterName.setText("");
                labelCarCenterCity.setText("");
                labelCarCenterStreet.setText("");
                labelCarCenterHouse.setText("");
                //исрываем разделитель и удаляем изображение
                splitPaneCar.setDividerPosition(0, 1);
                imageViewCarPhoto.setImage(null);
            }
        });
    }

    /**
     * Метод-слушатель нажатия на кнопку Добавить/Изменить фото
     */
    @FXML
    private void buttonAddEditPhotoOnAction() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Окно выбора файла");
        //Путь к файлу
        File file = fc.showOpenDialog((Stage) anchorPaneBaseDictionary.getScene().getWindow());
        if (file != null) {
            //получить подтверждение о действии
            boolean confirmation = Alarm.showAlertConfirmation("Добавить фото", "Вы уверены, что хотите изменить фото?");
            //если Да, то
            if (confirmation) {
                //обновить фото в базе
                boolean updatePhoto = query.updatePhoto(tableViewDictionary.getSelectionModel().getSelectedItem().getId(), file.getAbsolutePath());
                //если фото обновлено, то
                if (updatePhoto) {
                    //задать изображение
                    Image image = query.getPhotoByCarId(tableViewDictionary.getSelectionModel().getSelectedItem().getId());
                    splitPaneCar.setDividerPosition(0, 1 - imageViewCarPhoto.getFitWidth() / anchorPaneBaseDictionary.getPrefWidth());
                    imageViewCarPhoto.setImage(image);
                } else {
                    //иначе отобразить диалоговое окно с ошибкой добавления данных в БД
                    Alarm.showAlert(Alert.AlertType.ERROR, "Работа с БД", "Добавление данных в БД", "Ошибка добавления данных в БД");
                }
            }
        }
    }

    @FXML
    private void hyperlinkPhotoHideOnAction() {
        splitPaneCar.setDividerPosition(0, 1);
        //tableViewDictionary.getSelectionModel().clearSelection();
    }

    @FXML
    private void hyperlinkPhotoDeleteOnAction() {
        //подтверждение удаления фото
        boolean confirmation = Alarm.showAlertConfirmation("Удалить фото", "Вы уверены, что хотите удалить фото?");
        if (confirmation) {
            boolean updatePhoto = query.updatePhoto(tableViewDictionary.getSelectionModel().getSelectedItem().getId(), null);
            if (updatePhoto) {
                splitPaneCar.setDividerPosition(0, 1);
                tableViewDictionary.getSelectionModel().clearSelection();
            } else {
                Alarm.showAlert(Alert.AlertType.ERROR, "Работа с БД", "Удаление данных из БД", "Ошибка удаления данных из БД");
            }
        }

    }

    /**
     * Метод-слушатель нажатия на кнопку Добавить
     */
    @FXML
    private void buttonAddOnAction() {
        //отобразить диалоговое окно для добавления новой позиции
        Car addCar = showAddEditDialog(true);
        //если значение не равно null, то
        if (addCar != null) {
            //добавить данные в БД
            boolean add = query.add(addCar);
            //если данные добавлены, то
            if (add) {
                //обновить данные в списке
                listTableView = FXCollections.observableArrayList(query.getAll());
                tableViewDictionary.setItems(listTableView);
            } else {
                //иначе отобразить диалоговое окно с ошибкой добавления данных в БД
                Alarm.showAlert(Alert.AlertType.ERROR, "Работа с БД", "Добавление данных в БД", "Ошибка добавления данных в БД");
            }
        }
    }

    /**
     * Метод-слушатель нажатия на кнопку Изменить
     */
    @FXML
    private void buttonEditOnAction() {
        //отобразить диалоговое окно для редактирования позиции
        Car editCar = showAddEditDialog(false);
        //если позиция отредактирована, то
        if (editCar != null) {
            //обновить данные в БД
            boolean update = query.update(editCar);
            //если данные успешно обновлены, то
            if (update) {
                //обновить данные в таблице
                tableViewDictionary.refresh();
            } else {
                //иначе отобразить диалоговое окно с ошибкой обновления данных в БД
                Alarm.showAlert(Alert.AlertType.ERROR, "Работа с БД", "Изменение данных в БД", "Ошибка изменения данных в БД");
            }
        }
    }

    /**
     * Метод-слушатель нажатия на кнопку Удалить
     *
     */
    @FXML
    private void buttonDeleteOnAction() {

        boolean confirmation = Alarm.showAlertConfirmation("Подтверждение удаления", "Вы уверены в удалении данной позиции?");
        if (confirmation) {
            //получение объекта из выделенной строки таблицы
            Car deleteCar = tableViewDictionary.getSelectionModel().getSelectedItem();
            //удалить данные из БД
            boolean delete = query.delete(deleteCar.getId());
            //если произошла ошибка удаления данных, то
            if (!delete) {
                Alarm.showAlert(Alert.AlertType.ERROR, "Работа с БД", "Ошибка", "Ошибка удаления данных в БД");
            } else {
                listTableView.remove(deleteCar);
                //baseController.updateTable();
            }
        }

    }

    /**
     * Метод-слушатель нажатия на кнопку Закрыть
     *
     * @param event
     */
    @FXML
    private void buttonCloseOnAction() {
        //получение stage
        Stage stage = (Stage) anchorPaneBaseDictionary.getScene().getWindow();
        //закрытие окна
        stage.close();
    }

    /**
     * Метод отображения диалогового окна для добавления/редактироватия
     * автомобиля
     *
     * @param add маркер добавления или редактирования. true - добавить, иначе -
     * редактировать
     * @return в случае успеха - объект типа Car, иначе - null
     */
    private Car showAddEditDialog(boolean add) {
        try {
            //класс-загрузчик fxml файлов
            FXMLLoader loader = new FXMLLoader();
            //загружаем файл
            loader.setLocation(getClass().getResource("/fxml/manager/addEditCar.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();

            if (add) {
                dialogStage.setTitle("Добавить авто");
            } else {
                dialogStage.setTitle("Изменить авто");
            }

            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            //запрет изменения размеров окна
            dialogStage.setResizable(false);
            //Стиль рамки
            dialogStage.initStyle(StageStyle.DECORATED);
            AddEditCarController controller = (AddEditCarController) loader.getController();

            if (!add) {
                controller.setCar(tableViewDictionary.getSelectionModel().getSelectedItem());
            }

            //Отображаем диалоговое окно и ждем, пока пользователь его не закроет
            dialogStage.showAndWait();
            return controller.getCar();
        } catch (IOException ex) {
            Logger.getLogger(CarBrandController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
