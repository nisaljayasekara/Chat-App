package lk.ijse.chatapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFromController {

    @FXML
    private Button loginId;


    @FXML
    private TextField txtName;

    public void initialize(){}
    @FXML
    void BtnLoginOnAction(ActionEvent event) throws IOException {
        if (!txtName.getText().isEmpty()&&txtName.getText().matches("[A-Za-z0-9]+")){
            Stage primaryStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ClientForm.fxml"));
            Parent root = fxmlLoader.load();


            ClientFormController controller = fxmlLoader.getController();
            controller.setClientName(txtName.getText()); // Set the parameter
            fxmlLoader.setController(controller);

            primaryStage.setScene(new Scene(root));

            primaryStage.setTitle(txtName.getText());
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.setOnCloseRequest(windowEvent -> {
                controller.shutdown();
            });
            primaryStage.show();

            txtName.clear();
        }else{
            new Alert(Alert.AlertType.ERROR, "Please enter your name").show();
        }

    }

}
