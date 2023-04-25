package controller;

import hu.inf.unideb.CarDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

public class CarRentalController {
    @FXML
    public Button deleteButton;
    @FXML
    public Button refreshButton;

    @FXML
    private Button addButton;
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

    @FXML
    private TextField plateDeleteField;
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
            Logger.debug("Filling table with: ");
            cars.forEach(Logger::debug);
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
            jdbi.useHandle(handle -> handle.attach(CarDao.class).deleteCarByPlate(plateToDelete));
            handleRefreshButton(actionEvent);
            Logger.debug("Row deleted from table.");
        }
        Logger.error("Invalid input, `plate` is missing");
    }

    @FXML
    public void handleRefreshButton(ActionEvent actionEvent) {
        carTable.refresh();
        jdbi.useHandle(handle -> {
            List<CarRentalModel> cars = handle.attach(CarDao.class).getCars();
            carTable.setItems(carTable.getItems().filtered(cars::contains));
        });
        Logger.debug("Car table refreshed");
    }
}

