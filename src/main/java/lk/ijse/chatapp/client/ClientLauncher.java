package lk.ijse.chatapp.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.chatapp.controller.ClientFormController;

public class ClientLauncher extends Application {

    public static void main(String []args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/ClientForm.fxml"));
        ClientFormController controller = new ClientFormController();
        fxmlLoader.setController(controller);
        primaryStage.setScene(new Scene(fxmlLoader.load()));

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage.getScene().getWindow());
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
}
