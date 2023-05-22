package controller;

import hu.unideb.inf.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CarRentalModel;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class AddCarController implements SceneSwitcher {
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField plateField;

    @FXML
    private TextField makeField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField yearField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private RadioButton availableRadioButton;

    @FXML
    private void handleSaveButton() throws IOException {

        try {
            String plate = plateField.getText();

            if (plate.isEmpty()) {
                CarRentalController.sendAlert("Invalid input", "Please enter the plate number to add/update a car!");
                return;
            }

            String make = makeField.getText();
            String model = modelField.getText();
            int year = Integer.parseInt(yearField.getText());
            LocalDate rentalStartDate = datePicker.getValue();
            CarRentalModel.State state = availableRadioButton.isSelected() ? CarRentalModel.State.AVAILABLE : CarRentalModel.State.RENTED;

            CarRentalModel car = new CarRentalModel(plate, make, model, year, rentalStartDate, state);

            if (isRented(car)) {
                car.setState(CarRentalModel.State.RENTED);
                Logger.debug("Setting `Car.State` to RENTED");
            } else {
                car.setState(CarRentalModel.State.AVAILABLE);
                Logger.debug("Setting `Car.State` to AVAILABLE");
            }

            CarRentalModel.addOrUpdateCar(car);

        } catch (NumberFormatException e) {
            CarRentalController.sendAlert("Invalid input", "Please fill out the year field (YYYY)!");
            return;
        }

        switchSceneTo("/fxml/carrental.fxml");
    }

    @FXML
    public void handleCancelButton(ActionEvent ignoredActionEvent) throws IOException {
        Logger.info("Add/Update operation aborted");
        switchSceneTo("/fxml/carrental.fxml");
    }

    private boolean isRented(CarRentalModel car) {
        return car.getRentalStartDate() != null;
    }

    public void switchSceneTo(String path) throws IOException {
        // switch back to `path` scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Logger.info("Switching scene to: " + stage.getTitle());
    }

}