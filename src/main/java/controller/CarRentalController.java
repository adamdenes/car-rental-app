package controller;

import hu.inf.unideb.CarDao;
import hu.inf.unideb.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.CarRentalModel;
import model.CarRentalModel.State;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CarRentalController implements SceneSwitcher {
    @FXML
    public Button returnButton;
    @FXML
    public TextField plateReturnField;
    @FXML
    private Button addButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button rentButton;
    @FXML
    private TextField plateDeleteField;
    @FXML
    public TextField plateRentField;
    @FXML
    public DatePicker datePickerField;
    @FXML
    private TableView<CarRentalModel> carTable;

    @FXML
    private TableColumn<CarRentalModel, String> plateColumn;

    @FXML
    private TableColumn<CarRentalModel, String> makeColumn;

    @FXML
    private TableColumn<CarRentalModel, String> modelColumn;

    @FXML
    private TableColumn<CarRentalModel, Integer> yearColumn;

    @FXML
    private TableColumn<CarRentalModel, LocalDate> rentalStartDateColumn;

    @FXML
    private TableColumn<CarRentalModel, State> stateColumn;

    private static final String dbPath = "/home/adenes/projects/java/car-rental-app/carpool.db";
    public static final Jdbi jdbi = Jdbi.create("jdbc:sqlite:" + dbPath);

    @FXML
    private void initialize() {
        Logger.info("Initializing CarRentalController");
        plateColumn.setCellValueFactory(new PropertyValueFactory<>("plate"));
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        rentalStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentalStartDate"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        CarRentalController.jdbi.installPlugin(new SqlObjectPlugin());

        jdbi.useHandle(handle -> {
            List<CarRentalModel> cars = handle.attach(CarDao.class).getCars();
            carTable.getItems().addAll(cars);
        });
    }


    @FXML
    private void handleAddButton(ActionEvent actionEvent) throws IOException {
        Logger.info("Opening `Add Car` window");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/addcar.fxml")));
        stage.setScene(new Scene(root));
        stage.setTitle("Add Car");
        stage.show();
    }

    @FXML
    public void handleDeleteButton(ActionEvent actionEvent) {
        String plateToDelete = plateDeleteField.getText();

        if (!plateToDelete.isEmpty()) {
            jdbi.useHandle(handle -> {
                CarDao cd = handle.attach(CarDao.class);
                Optional<CarRentalModel> exists = cd.getCarByPlate(plateToDelete);
                if (exists.isPresent()) {
                    cd.deleteCarByPlate(plateToDelete);
                    Logger.debug("Row deleted from table");
                } else {
                    sendAlert("Delete error", "There is no such car in the database!");
                    Logger.error("There is no such car in the database");
                }
            });
            handleRefreshButton(actionEvent);
        } else {
            sendAlert("Delete error", "Please enter a plate number to delete!");
            Logger.error("Invalid input");
        }
    }

    @FXML
    public void handleRefreshButton(ActionEvent actionEvent) {
        clearFields();
        carTable.refresh();
        jdbi.useHandle(handle -> {
            List<CarRentalModel> cars = handle.attach(CarDao.class).getCars();
            carTable.setItems(carTable.getItems().filtered(cars::contains));
        });
        Logger.debug("Car table refreshed");
    }

    @FXML
    public void handleRentButton(ActionEvent actionEvent) throws IOException {
        String plate = plateRentField.getText();
        LocalDate startDate = datePickerField.getValue();

        if (!plate.isEmpty() && startDate != null) {
            jdbi.useHandle(handle -> {
                CarDao cd = handle.attach(CarDao.class);
                Optional<CarRentalModel> exists = cd.getCarByPlate(plate);

                if (exists.isEmpty()) {
                    sendAlert("Renting error", "There is no such car in the database!");
                    Logger.error("Car doesn't exist with plate: " + plate);
                } else if (exists.get().getRentalStartDate() != null) {
                    sendAlert("Renting error", "This car is not available for renting at the moment!");
                    Logger.error("Car already rented since: " + exists.get().getRentalStartDate());
                } else {
                    CarRentalModel car = exists.get();
                    car.setState(State.RENTED);
                    car.setRentalStartDate(startDate);

                    cd.updateCar(car);
                    Logger.debug("Car rented from " + startDate + ", state changed to RENTED");
                }
            });
            // might be overkill
            handleRefreshButton(actionEvent);

            // dirty trick to refresh the scene, because
            // the `updateCar` removes the table row...
            switchSceneTo("/fxml/carrental.fxml");
        } else {
            sendAlert("Renting error", "Please enter the plate number and chose a suitable start date!");
            Logger.error("Invalid input");
        }
    }

    @FXML
    public void handleReturnButton(ActionEvent actionEvent) throws IOException {
        String plateToReturn = plateReturnField.getText();

        if (!plateToReturn.isEmpty()) {
            jdbi.useHandle(handle -> {
                CarDao cd = handle.attach(CarDao.class);
                Optional<CarRentalModel> exists = cd.getCarByPlate(plateToReturn);
                if (exists.isPresent()) {
                    exists.get().setState(State.AVAILABLE);
                    exists.get().setRentalStartDate(null);
                    cd.updateCar(exists.get());
                    Logger.debug("Car has been successfully returned to the pool");
                } else {
                    sendAlert("Return error", "There is no such car in the database!");
                    Logger.error("There is no such car in the database");
                }
            });
            // might be overkill
            handleRefreshButton(actionEvent);

            // dirty trick to refresh the scene, because
            // the `updateCar` removes the table row...
            switchSceneTo("/fxml/carrental.fxml");
        } else {
            sendAlert("Return error", "Please enter a plate number to return a car to the pool!");
            Logger.error("Invalid input");
        }
    }

    public void clearFields() {
        plateRentField.setText("");
        datePickerField.setValue(null);
        plateDeleteField.setText("");
    }

    public void switchSceneTo(String path) throws IOException {
        // switch back to `path` scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Logger.info("Switching scene to: " + stage.getTitle());
    }

    public static void sendAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

}

