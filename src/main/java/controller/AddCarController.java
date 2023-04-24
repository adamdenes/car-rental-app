package controller;

import hu.inf.unideb.CarDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Car;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class AddCarController {

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
        Car.State state = availableRadioButton.isSelected() ? Car.State.AVAILABLE : Car.State.RENTED;

        Car car = new Car(plate, make, model, year, rentalStartDate, state);

        if (isRented(car)) {
            car.setState(Car.State.RENTED);
        } else {
            car.setState(Car.State.AVAILABLE);
        }

        CarRentalController.jdbi.useHandle(handle -> {
            CarDao cd = handle.attach(CarDao.class);
            Optional<Car> exists = cd.getCarByPlate(car.getPlate());
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

        // switch back to `carrental` scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/carrental.fxml")));
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private boolean isRented(Car car) {
        return car.getRentalStartDate() != null;
    }

}