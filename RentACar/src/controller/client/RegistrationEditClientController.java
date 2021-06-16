package controller.client;

import data.Client;
import dialog.Alarm;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;
import sql.QueryClient;

/**
 * FXML Controller class - Регистрация и изменнеие информации клиента
 *
 * @author Admin
 */
public class RegistrationEditClientController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBaseRegistrationEditClient;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldPatronymic;
    @FXML
    private TextField textFieldClientLastName;
    @FXML
    private TextField textFieldPassportSeries;
    @FXML
    private TextField textFieldPassportId;
    @FXML
    private DatePicker datePickerBirthday;
    @FXML
    private TextField textFieldPhone;
    @FXML
    private PasswordField passwordFieldClientPassword;
    @FXML
    private Button buttonEditPassword;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonClose;

    private Client client;
    private QueryClient queryClient;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonAdd.disableProperty().bind(textFieldFirstName.textProperty().isEmpty()
                .or(textFieldPatronymic.textProperty().isEmpty()
                        .or(textFieldClientLastName.textProperty().isEmpty()
                                .or(textFieldPassportSeries.textProperty().isEmpty()
                                        .or(textFieldPassportId.textProperty().isEmpty()
                                                .or(textFieldPhone.textProperty().isEmpty()
                                                        .or(datePickerBirthday.valueProperty().isNull())))))));

        buttonEditPassword.disableProperty().bind(passwordFieldClientPassword.textProperty().isEmpty());
        
        if(Main.client == null){
            buttonEditPassword.setVisible(false);
        }

        queryClient = new QueryClient();
    }

    /**
     * Метод - слушатель нажатия на кнопку Ок
     */
    @FXML
    private void buttonAddOnAction() {

        //если объект равен null, то добавляем нового клиента
        if (client == null) {
            String checkField = checkField(true);
            if (checkField == null) {
                //создание нового клиента
                client = new Client(textFieldFirstName.getText(),
                        textFieldPatronymic.getText(),
                        textFieldClientLastName.getText(),
                        textFieldPhone.getText(),
                        datePickerBirthday.getValue(),
                        Integer.parseInt(textFieldPassportSeries.getText()),
                        Integer.parseInt(textFieldPassportId.getText()),
                        passwordFieldClientPassword.getText());

                boolean confirmation = Alarm.showAlertConfirmation("Регистрация в системе", "Зарегистрировать Ваши данные в системе?");
                if (confirmation) {
                    boolean add = queryClient.add(client);
                    if (add) {
                        Alarm.showAlert(Alert.AlertType.INFORMATION, "Регистрация", "Регистрация в системе", "Регистрация успешно выполнена");
                        closeWindow();
                    } else {
                        Alarm.showAlert(Alert.AlertType.ERROR, "Регистрация", "Ошибка регистрации", "Повторите попытку или обратитесь к менеджеру");
                    }
                }
            } else {
                Alarm.showAlert(Alert.AlertType.WARNING, "Регистрация в системе", "Ошибка заполнений полей", checkField);
            }
        } else {
            String checkField = checkField(false);
            if (checkField == null) {
                client.setFirstName(textFieldFirstName.getText());
                client.setPatronymic(textFieldPatronymic.getText());
                client.setLastName(textFieldClientLastName.getText());
                client.setPassportSeries(Integer.parseInt(textFieldPassportSeries.getText()));
                client.setPassport_id(Integer.parseInt(textFieldPassportId.getText()));
                client.setBirthday(datePickerBirthday.getValue());
                client.setPhone(textFieldPhone.getText());

                boolean update = queryClient.update(client);
                if (update) {
                    Alarm.showAlert(Alert.AlertType.INFORMATION, "Регистрационные данные", "Изменение данных", "Данные изменены успешно");
                    Main.client = client;
                    closeWindow();
                } else {
                    Alarm.showAlert(Alert.AlertType.ERROR, "Регистрационные данные", "Ошибка изменения данных", "Повторите попытку или обратитесь к менеджеру");
                }
            } else {
                Alarm.showAlert(Alert.AlertType.WARNING, "Регистрация в системе", "Ошибка заполнений полей", checkField);
            }
        }

    }

    @FXML
    private void buttonEditPasswordOnAction() {
        if (passwordFieldClientPassword.getText().length() < 4) {
            Alarm.showAlert(Alert.AlertType.WARNING, "Регистрационные данные", "Ошибка изменения данных", "Пароль должен содержать 4 и более символа");
        } else {
            boolean confirmation = Alarm.showAlertConfirmation("Изменение данных", "Вы действительно хотете изменить пароль?");
            if(confirmation){
                boolean updatePassword = queryClient.updatePassword(Main.client.getId(), passwordFieldClientPassword.getText());
                if(updatePassword){
                    Alarm.showAlert(Alert.AlertType.INFORMATION, "Регистрационные данные", "Изменение данных", "Пароль успешно изменен");
                } else {
                    Alarm.showAlert(Alert.AlertType.ERROR, "Регистрационные данные", "Ошибка изменения данных", "Повторите попытку или обратитесь к менеджеру");
                }
            }
        }
    }

    /**
     * Слушатель нажатия на кнопку Отмена
     */
    @FXML
    private void buttonCloseOnAction() {
        client = null;
        closeWindow();
    }

    /**
     * Метод проверки корректности ввода данных
     *
     * @return текст со списком не корректно введенных значений, null - если все
     * введено верно
     */
    private String checkField(boolean withPassword) {
        String text = "";
        if (!textFieldPassportSeries.getText().matches("^[0-9]{4}$")) {
            text += "Введите корректную серию паспорта.\n";
        }

        if (!textFieldPassportId.getText().matches("^[0-9]{6}$")) {
            text += "Введите корректный номер паспорта.\n";
        }

        if (!textFieldPhone.getText().matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) {
            text += "Введите корректный номер телефона.\n";
        }

        if (withPassword) {
            if (passwordFieldClientPassword.getText().length() < 4) {
                text += "Пароль должен содержать 4 и более символа";
            }
        }

        if (text.length() == 0) {
            return null;
        }
        return text;
    }

    /**
     * Метод закрытия диалогового окна
     */
    private void closeWindow() {
        Stage stage = (Stage) anchorPaneBaseRegistrationEditClient.getScene().getWindow();
        stage.close();
    }

    /**
     * Установка клиента
     *
     * @param client клиент
     */
    public void setClient(Client client) {
        this.client = client;
        textFieldFirstName.setText(client.getFirstName());
        textFieldPatronymic.setText(client.getPatronymic());
        textFieldClientLastName.setText(client.getLastName());
        textFieldPassportSeries.setText("" + client.getPassportSeries());
        textFieldPassportId.setText("" + client.getPassport_id());
        datePickerBirthday.setValue(client.getBirthday());
        textFieldPhone.setText(client.getPhone());
    }

    /**
     * Получение клиента
     *
     * @return объет типа Client
     */
    public Client getClient() {
        return this.client;
    }

}
