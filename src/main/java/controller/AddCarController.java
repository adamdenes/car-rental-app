package controller;

import hu.inf.unideb.CarDao;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class AddCarController {

    public Button cancelButton;
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
        String plate = plateField.getText();
        String make = makeField.getText();
        String model = modelField.getText();
        int year = Integer.parseInt(yearField.getText());
        LocalDate rentalStartDate = datePicker.getValue();
        CarRentalModel.State state = availableRadioButton.isSelected() ? CarRentalModel.State.AVAILABLE : CarRentalModel.State.RENTED;

        CarRentalModel car = new CarRentalModel(plate, make, model, year, rentalStartDate, state);

        if (isRented(car)) {
            car.setState(CarRentalModel.State.RENTED);
        } else {
            car.setState(CarRentalModel.State.AVAILABLE);
        }

        CarRentalController.jdbi.useHandle(handle -> {
            CarDao cd = handle.attach(CarDao.class);
            Optional<CarRentalModel> exists = cd.getCarByPlate(car.getPlate());
            if (exists.isEmpty()) {
                // create a new car record
                cd.insertCar(car);
            } else {
                // update the record
                exists.get().setMake(make);
                exists.get().setModel(model);
                exists.get().setYear(year);
                exists.get().setRentalStartDate(rentalStartDate);
                exists.get().setState(car.getState());

                cd.updateCar(exists.get());
            }
        });

        switchSceneTo("/fxml/carrental.fxml");
    }

    @FXML
    public void handleCancelButton(ActionEvent actionEvent) throws IOException {
        switchSceneTo("/fxml/carrental.fxml");
    }

    private boolean isRented(CarRentalModel car) {
        return car.getRentalStartDate() != null;
    }

    private void switchSceneTo(String path) throws IOException {
        // switch back to `path` scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}