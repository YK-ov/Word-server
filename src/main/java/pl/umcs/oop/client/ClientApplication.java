package pl.umcs.oop.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ClientApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("/pl/umcs/oop/view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat for clients");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}