package dialog;

import controller.manager.FuelTypeController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Класс - Диалоговые окна
 * @author Admin
 */
public class Alarm {

    /**
     * Метод отображения на экране окна предупреждения
     *
     * @param alType тип сообщения (NONE, INFORMATION, WARNING, CONFIRMATION, ERROR)
     * @param title название окна
     * @param headerText название предупреждения
     * @param contentText описание
     */
    public static void showAlert(Alert.AlertType alType, String title, String headerText, String contentText) {

        Alert alert = new Alert(alType);
        alert.initOwner(null);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();

    }

    /**
     * Метод отображения на экране окна подтверждения
     * @param title название окна
     * @param text описание
     * @return true - в случае подтверждения, иначе - false
     */
    public static boolean showAlertConfirmation(String title, String text) {

        try {
            //класс-загрузчик fxml файлов
            FXMLLoader loader = new FXMLLoader();
            //загружаем файл
            loader.setLocation(Alarm.class.getResource("dialogConfirm.fxml"));
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
            DialogConfirmController controller = (DialogConfirmController) loader.getController();
            controller.setTextConfirm(text);
            //Отображаем диалоговое окно и ждем, пока пользователь его не закроет
            dialogStage.showAndWait();
            return controller.confirmation();
        } catch (IOException ex) {
            Logger.getLogger(FuelTypeController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
