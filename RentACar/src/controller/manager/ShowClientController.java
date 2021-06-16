package controller.manager;

import data.Client;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sql.QueryClient;

/**
 * FXML Controller class - Клиенты
 *
 * @author Admin
 */
public class ShowClientController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseDictionary;
    @FXML
    private TableView<Client> tableViewDictionary;
    @FXML
    private TableColumn<Client, Long> tableColumnID;
    @FXML
    private TableColumn<Client, String> tableColumnFirstName;
    @FXML
    private TableColumn<Client, String> tableColumnPatronymic;
    @FXML
    private TableColumn<Client, String> tableColumnLastName;
    @FXML
    private TableColumn<Client, String> tableColumnPhone;
    @FXML
    private Button buttonClose;
    @FXML
    private Label labelBirthday;
    @FXML
    private Label labelPassport;
    @FXML
    private Button buttonClear;
    @FXML
    private TextField textFieldSearch;

    QueryClient query;
    private ObservableList<Client> listTableView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //объект для работы с запросами в БД
        query = new QueryClient();

        initTable();
        
        labelBirthday.setText("");
        labelPassport.setText("");
    }

    /**
     * Метод инициализации таблицы
     */
    private void initTable() {
        //установка значений для колонок таблицы
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnPatronymic.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));


        //создание следящего списка со значениями для таблицы
        listTableView = FXCollections.observableArrayList(query.getAll());
        //установка значений для таблицы
        tableViewDictionary.setItems(listTableView);
        
        tableViewDictionary.setPlaceholder(new Label("Данные отсутствуют"));
        
        tableViewDictionary.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(ObservableValue<? extends Client> observable, Client oldValue, Client newValue) {
                if(newValue != null){
                    labelBirthday.setText(newValue.getBirthday().toString());
                    labelPassport.setText(newValue.getPassportSeries() + " " + newValue.getPassport_id());
                } else {
                    labelBirthday.setText("");
                    labelPassport.setText("");
                }
            }
        });
    }

    /**
     * Метод-слушатель нажатия на кнопку Закрыть
     *
     * @param event
     */
    @FXML
    private void buttonClearOnAction() {
        textFieldSearch.clear();
        tableViewDictionary.setItems(listTableView);       
    }
    
    @FXML
    private void textFieldSearchOnKeyReleased(){
        //получение строки для поиска
        String text = textFieldSearch.getText();
        //создание нового списка
        List<Client> listSearch = new ArrayList<>();
        //перебор всех элементов
        listTableView.forEach((Client client) -> {
            //если в объект входит строка text, то
            if(client.toString().toLowerCase().contains(text.toLowerCase())){
                //добавить заметку в список
                listSearch.add(client);
            }
        });
        //установить новый список заметок для таблицы
        tableViewDictionary.setItems(FXCollections.observableArrayList(listSearch));
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

}
