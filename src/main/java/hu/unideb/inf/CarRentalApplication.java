package hu.unideb.inf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.Objects;

public class CarRentalApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Logger.info("Starting CarRentalApplication");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/carrental.fxml")));
        stage.setTitle("Car Rental Application");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
