package pl.umcs.oop.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.umcs.oop.server.Server;
import pl.umcs.oop.server.WordBag;

import java.io.PrintWriter;
import java.net.Socket;

public class ClientThreadGUI extends Application {
    private TextArea chatArea;
    private TextField inputField;
    private PrintWriter out;


    @Override
    public void start(Stage stage) throws Exception {
        Socket socket = new Socket("localhost", 5000);
        out = new PrintWriter(socket.getOutputStream(), true);
        chatArea = new TextArea();
        chatArea.setEditable(false);
        inputField = new TextField();

        ListView<String> userList = new ListView<>();
        userList.getItems().add("User1");
        userList.getItems().add("User2");


        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            System.out.println("Button pressed");
            sendMessage();
        });
        inputField.setOnAction(e -> sendMessage());

        BorderPane pane = new BorderPane();
        pane.setCenter(chatArea);

        HBox bottomPanel = new HBox(inputField, sendButton);
        pane.setBottom(bottomPanel);

        VBox rightPanel = new VBox(new Label("List of users"), userList);
        pane.setRight(rightPanel);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Client");
        stage.show();
    }

    private void sendMessage(){
        String message = inputField.getText().trim();
            if(message.isEmpty()){
             //   chatArea.appendText(message+"\n");
                out.println(message);
                inputField.clear();
            }
    }



}
