package controller.manager;

import data.Discount;
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
import sql.QueryDiscount;

/**
 * FXML Controller class - Скидки
 *
 * @author Admin
 */
public class DiscountController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseDictionary;
    @FXML
    private TableView<Discount> tableViewDictionary;
    @FXML
    private TableColumn<Discount, Long> tableColumnID;
    @FXML
    private TableColumn<Discount, Integer> tableColumnDays;
    @FXML
    private TableColumn<Discount, Float> tableColumnPercent;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonClose;

    QueryDiscount query;
    private ObservableList<Discount> listTableView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //объект для работы с запросами в БД
        query = new QueryDiscount();

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
        tableColumnDays.setCellValueFactory(new PropertyValueFactory<>("days"));
        tableColumnPercent.setCellValueFactory(new PropertyValueFactory<>("percent"));

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
        Discount addDiscount = showAddEditDialog(true);
        //если значение не равно null, то
        if (addDiscount != null) {
            //добавить данные в БД
            boolean add = query.add(addDiscount);
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
        Discount editDiscount = showAddEditDialog(false);
        //если позиция отредактирована, то
        if (editDiscount != null) {
            //обновить данные в БД
            boolean update = query.update(editDiscount);
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
            Discount deleteDiscount = tableViewDictionary.getSelectionModel().getSelectedItem();
            //удалить данные из БД
            boolean delete = query.delete(deleteDiscount.getId());
            //если произошла ошибка удаления данных, то
            if (!delete) {
                Alarm.showAlert(Alert.AlertType.ERROR, "Работа с БД", "Ошибка", "Ошибка удаления данных в БД");
            } else {
                listTableView.remove(deleteDiscount);
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
     * Метод отображения диалогового окна для добавления/редактироватия скидок
     *
     * @param add маркер добавления или редактирования. true - добавить, иначе -
     * редактировать
     * @return в случае успеха - объект типа Discount, иначе - null
     */
    private Discount showAddEditDialog(boolean add) {
        try {
            //класс-загрузчик fxml файлов
            FXMLLoader loader = new FXMLLoader();
            //загружаем файл
            loader.setLocation(getClass().getResource("/fxml/manager/addEditDiscount.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();

            if (add) {
                dialogStage.setTitle("Добавить скидку");
            } else {
                dialogStage.setTitle("Изменить скидку");
            }

            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            //запрет изменения размеров окна
            dialogStage.setResizable(false);
            //Стиль рамки
            dialogStage.initStyle(StageStyle.DECORATED);
            AddEditDiscountController controller = (AddEditDiscountController) loader.getController();

            if (!add) {
                controller.setDiscount(tableViewDictionary.getSelectionModel().getSelectedItem());
            }

            //Отображаем диалоговое окно и ждем, пока пользователь его не закроет
            dialogStage.showAndWait();
            return controller.getDiscount();
        } catch (IOException ex) {
            Logger.getLogger(DiscountController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    } 
    
}
