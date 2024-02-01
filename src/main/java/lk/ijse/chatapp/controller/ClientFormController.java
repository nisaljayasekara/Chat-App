package lk.ijse.chatapp.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lk.ijse.chatapp.emoji.EmojiPicker;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class ClientFormController {

    @FXML
    public AnchorPane Pane;

    @FXML
    public ScrollPane ScrollPain;

    @FXML
    public Button emojiButton;

    @FXML
    public TextField txtMsg;

    @FXML
    private Label txtName;

    @FXML
    public VBox vBox;

    private Socket socket;

    private DataInputStream dataInputStream;

    private DataOutputStream dataOutputStream;

    private String clientName;

    public void initialize(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("localhost", 12345);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Client connected");
                    ServerFormController.receiveMessage(clientName + "joined.");

                    while (socket.isConnected()){
                        String receivingMsg = dataInputStream.readUTF();
                        receiveMessage(receivingMsg,ClientFormController.this.vBox);
                    }
                } catch (IOException e) {
                   e.printStackTrace();
                    // throw new RuntimeException(e);
                }

            }
        } ).start();
        emoji();
    }

   @FXML
    void btnEmojiOnAction(ActionEvent event) {


    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        sendMsg(txtMsg.getText());
    }

    @FXML
    void txtMessageOnAction(ActionEvent actionEvent) {
        sendButtonOnAction(actionEvent);
    }

    private void sendButtonOnAction(ActionEvent actionEvent) { sendMsg(txtMsg.getText()); }


    private void sendMsg(String msgToSend) {
        if (!msgToSend.isEmpty()){
            if (!msgToSend.matches(".*\\.(png|jpe?g|gif)$")){

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 0, 10));

                Text text = new Text(msgToSend);
                text.setStyle("-fx-font-size: 13");
                TextFlow textFlow = new TextFlow(text);


                textFlow.setStyle("-fx-background-color: #57e306; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 20px");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                text.setFill(Color.color(1, 1, 1));

                hBox.getChildren().add(textFlow);

                HBox hBoxTime = new HBox();
                hBoxTime.setAlignment(Pos.CENTER_RIGHT);
                hBoxTime.setPadding(new Insets(0, 5, 5, 10));
                String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                Text time = new Text(stringTime);
                time.setStyle("-fx-font-size: 8");

                hBoxTime.getChildren().add(time);

                vBox.getChildren().add(hBox);
                vBox.getChildren().add(hBoxTime);


                try {
                    dataOutputStream.writeUTF(clientName + "-" +msgToSend);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                txtMsg.clear();
            }
        }

    }


    public void shutdown() {
        ServerFormController.receiveMessage(clientName+" left.");//cleanup code
    }
   public static void receiveMessage (String msg, VBox vBox){
       if (msg.matches(".*\\.(png|jpe?g|gif)$")){
            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            Text textName = new Text(msg.split("[-]")[0]);
            TextFlow textFlowName = new TextFlow(textName);
            hBoxName.getChildren().add(textFlowName);

            Image image = new Image(msg.split("[-]")[1]);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5,5,5,10));
            hBox.getChildren().add(imageView);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBox);
                }
            });

        }else {
            String name = msg.split("-")[0];
            String msgFromServer = msg.split("-")[1];

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5,5,5,10));

            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            Text textName = new Text(name);
            TextFlow textFlowName = new TextFlow(textName);
            hBoxName.getChildren().add(textFlowName);

            Text text = new Text(msgFromServer);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: #08d1a6; -fx-font-weight: bold; -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0,0,0));

            hBox.getChildren().add(textFlow);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBox);
                }
            });
        }

    }

    @FXML
    void btnimageOnAction(ActionEvent event) {
      /*  System.out.println("nisal");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select File to Open");
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();

            sendImage(fileToOpen.getPath());
            //sendImage(file);
            System.out.println(fileToOpen.getPath() + " chosen.");
        }*/
    }

    @FXML
    void imageOnAction(ActionEvent event) {
        System.out.println("nisal");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select File to Open");
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();

            sendImage(fileToOpen.getPath());
            //sendImage(file);
            System.out.println(fileToOpen.getPath() + " chosen.");
        }
    }

    private void sendImage(String file) {


        File imageFile = new File(file);
        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 5, 5, 10));
        hBox.getChildren().add(imageView);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        vBox.getChildren().add(hBox);

        try {
            dataOutputStream.writeUTF(file);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClientName(String name) {
        txtName.setText(name);
        clientName = name;

    }
    private void emoji() {
        // Create the EmojiPicker
        EmojiPicker emojiPicker = new EmojiPicker();

        VBox vBox = new VBox(emojiPicker);
        vBox.setPrefSize(150,300);
        vBox.setLayoutX(200);//400
        vBox.setLayoutY(390);//175
        vBox.setStyle("-fx-font-size: 30");

        Pane.getChildren().add(vBox);

        // Set the emoji picker as hidden initially
        emojiPicker.setVisible(false);

        // Show the emoji picker when the button is clicked
        emojiButton.setOnAction(event -> {
            if (emojiPicker.isVisible()){
                emojiPicker.setVisible(false);
            }else {
                emojiPicker.setVisible(true);
            }
        });

        // Set the selected emoji from the picker to the text field
        emojiPicker.getEmojiListView().setOnMouseClicked(event -> {
            String selectedEmoji = emojiPicker.getEmojiListView().getSelectionModel().getSelectedItem();
            if (selectedEmoji != null) {
                txtMsg.setText(txtMsg.getText()+selectedEmoji);
            }
            emojiPicker.setVisible(false);
        });
    }
}
