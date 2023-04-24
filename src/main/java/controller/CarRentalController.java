package controller;

import hu.inf.unideb.CarDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Car;
import model.Car.State;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.time.LocalDate;
import java.util.List;

public class CarRentalController {
    @FXML
    private TableView<Car> carTable;

    @FXML
    private TableColumn<Car, String> plateColumn;

    @FXML
    private TableColumn<Car, String> makeColumn;

    @FXML
    private TableColumn<Car, String> modelColumn;

    @FXML
    private TableColumn<Car, Integer> yearColumn;

    @FXML
    private TableColumn<Car, LocalDate> rentalStartDateColumn;

    @FXML
    private TableColumn<Car, State> stateColumn;

    @FXML
    private TextField plateTextField;

    @FXML
    private TextField makeTextField;

    @FXML
    private TextField modelTextField;

    @FXML
    private TextField yearTextField;

    @FXML
    private DatePicker rentalStartDatePicker;

    @FXML
    private Button addButton;

    public static final String dbPath = "/home/adenes/projects/java/car-rental-app/carpool.db";

    @FXML
    private void initialize() {
        plateColumn.setCellValueFactory(new PropertyValueFactory<>("plate"));
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        rentalStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentalStartDate"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        var jdbi = Jdbi.create("jdbc:sqlite:" + dbPath);
        jdbi.installPlugin(new SqlObjectPlugin());

        try (var handle = jdbi.open()) {
            var cd = handle.attach(CarDao.class);
            List<Car> cars = cd.getCars();
            carTable.getItems().addAll(cars);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addCar() {
//        try {
//            String plate = plateField.getText();
//            String make = makeField.getText();
//            String model = modelField.getText();
//            int year = Integer.parseInt(yearField.getText());
//            LocalDate rentalStartDate = rentalStartDatePicker.getValue();
//            Car.State state = availableRadioButton.isSelected() ? Car.State.AVAILABLE : Car.State.RENTED;
//
//            Car car = new Car(plate, make, model, year, rentalStartDate, state);
//            carDao.insert(car);
//
//            refreshCarList();
//        } catch (NumberFormatException e) {
//            // handle invalid year input
//        }
//        clearFields();
    }

    private void clearFields() {
        plateTextField.clear();
        makeTextField.clear();
        modelTextField.clear();
        yearTextField.clear();
        rentalStartDatePicker.setValue(null);
    }
}

