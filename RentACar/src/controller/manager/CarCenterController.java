package controller.manager;

import data.CarCenter;
import dialog.Alarm;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sql.QueryCarCenter;

/**
 * FXML Controller class - Центр проката авто
 *
 * @author Admin
 */
public class CarCenterController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseDictionary;
    @FXML
    private TableView<CarCenter> tableViewDictionary;
    @FXML
    private TableColumn<CarCenter, Long> tableColumnID;
    @FXML
    private TableColumn<CarCenter, String> tableColumnName;
    @FXML
    private TableColumn<CarCenter, String> tableColumnCity;
    @FXML
    private TableColumn<CarCenter, String> tableColumnStreet;
    @FXML
    private TableColumn<CarCenter, String> tableColumnHouse;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonClose;

    QueryCarCenter query;
    private ObservableList<CarCenter> listTableView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //объект для работы с запросами в БД
        query = new QueryCarCenter();

        buttonEdit.disableProperty().bind(tableViewDictionary.getSelectionModel().selectedItemProperty().isNull());
        buttonDelete.disableProperty().bind(tableViewDictionary.getSelectionModel().selectedItemProperty().isNull());

        initTable();
    }

    /**
     * Метод инициализации таблицы
     */
    private void initTable() {
        //установка значений для колонок таблицы
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        tableColumnStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        tableColumnHouse.setCellValueFactory(new PropertyValueFactory<>("house"));


        //создание следящего списка со значениями для таблицы
        listTableView = FXCollections.observableArrayList(query.getAll());
        //установка значений для таблицы
        tableViewDictionary.setItems(listTableView);
        
        tableViewDictionary.setPlaceholder(new Label("Данные отсутствуют"));
    }

    /**
     * Метод-слушатель нажатия на кнопку Добавить
     */
    @FXML
    private void buttonAddOnAction() {
        //отобразить диалоговое окно для добавления новой позиции
        CarCenter addCarCenter = showAddEditDialog(true);
        //если значение не равно null, то
        if (addCarCenter != null) {
            //добавить данные в БД
            boolean add = query.add(addCarCenter);
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
        CarCenter editCarCenter = showAddEditDialog(false);
        //если позиция отредактирована, то
        if (editCarCenter != null) {
            //обновить данные в БД
            boolean update = query.update(editCarCenter);
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
            CarCenter deleteCarCenter = tableViewDictionary.getSelectionModel().getSelectedItem();
            //удалить данные из БД
            boolean delete = query.delete(deleteCarCenter.getId());
            //если произошла ошибка удаления данных, то
            if (!delete) {
                Alarm.showAlert(Alert.AlertType.ERROR, "Работа с БД", "Ошибка", "Ошибка удаления данных в БД");
            } else {
                listTableView.remove(deleteCarCenter);
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
     * Метод отображения диалогового окна для добавления/редактироватия типа
     * топлива
     *
     * @param add маркер добавления или редактирования. true - добавить, иначе -
     * редактировать
     * @return в случае успеха - объект типа CarCenter, иначе - null
     */
    private CarCenter showAddEditDialog(boolean add) {
        try {
            //класс-загрузчик fxml файлов
            FXMLLoader loader = new FXMLLoader();
            //загружаем файл
            loader.setLocation(getClass().getResource("/fxml/manager/addEditCarCenter.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();

            if (add) {
                dialogStage.setTitle("Добавить центр проката");
            } else {
                dialogStage.setTitle("Изменить центр проката");
            }

            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            //запрет изменения размеров окна
            dialogStage.setResizable(false);
            //Стиль рамки
            dialogStage.initStyle(StageStyle.DECORATED);
            AddEditCarCenterController controller = (AddEditCarCenterController) loader.getController();

            if (!add) {
                controller.setCarCenter(tableViewDictionary.getSelectionModel().getSelectedItem());
            }

            //Отображаем диалоговое окно и ждем, пока пользователь его не закроет
            dialogStage.showAndWait();
            return controller.getCarCenter();
        } catch (IOException ex) {
            Logger.getLogger(CarCenterController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
