package hu.unideb.inf;

import model.CarRentalModel;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static controller.CarRentalController.jdbi;
import static org.junit.jupiter.api.Assertions.*;

public class CarDaoTest {

    private static CarDao dao;

    @BeforeEach
    public void setupDao() {
        jdbi.installPlugin(new SqlObjectPlugin());
        dao = jdbi.onDemand(CarDao.class);
        dao.createTable("test_table");
    }

    @AfterEach
    public void tearDownDao() {
        dao.deleteAllCars("test_table");
    }

    @AfterAll
    public static void teardown() {
        dao.dropTable("test_table");
    }

    @Test
    @DisplayName("Test createTable method")
    void testCreateTable() {
        assertTrue(dao.checkTableExists("test_table"));
    }

    @Test
    @DisplayName("Test insertCar and getCarByPlate methods")
    public void testCreateAndRetrieveCar() {
        CarRentalModel car = new CarRentalModel("ABC123", "Toyota", "Camry", 2020, null, CarRentalModel.State.AVAILABLE);
        dao.insertCar("test_table", car);

        Optional<CarRentalModel> retrievedCar = dao.getCarByPlate("test_table", "ABC123");
        assertTrue(retrievedCar.isPresent());
        assertEquals(car, retrievedCar.get());
    }

    @Test
    @DisplayName("Test deleteCarByPlate method")
    public void testDeleteCarByPlate() {
        CarRentalModel car = new CarRentalModel("ABC-123", "Opel", "Astra", 2010,
                LocalDate.of(2022, 4, 25), CarRentalModel.State.AVAILABLE);

        dao.insertCar("test_table", car);
        dao.deleteCarByPlate("test_table", "ABC-123");

        Optional<CarRentalModel> actualCar = dao.getCarByPlate("test_table", "ABC-123");
        assertFalse(actualCar.isPresent());
    }

    @Test
    @DisplayName("Test updateCar method")
    public void testUpdateCar() {
        CarRentalModel car = new CarRentalModel("ABC-123", "Opel", "Astra", 2010,
                LocalDate.of(2022, 4, 25), CarRentalModel.State.AVAILABLE);
        dao.insertCar("test_table", car);

        CarRentalModel updatedCar = new CarRentalModel("ABC-123", "Opel", "Astra", 2010,
                LocalDate.of(2022, 4, 26), CarRentalModel.State.RENTED);
        dao.updateCar("test_table", updatedCar);

        Optional<CarRentalModel> actualCar = dao.getCarByPlate("test_table", "ABC-123");
        assertNotEquals(car.getRentalStartDate(), actualCar.get().getRentalStartDate());
        assertNotEquals(car.getState(), actualCar.get().getState());
        assertEquals(LocalDate.of(2022, 4, 26), actualCar.get().getRentalStartDate());
        assertEquals(CarRentalModel.State.RENTED, actualCar.get().getState());
    }

    @Test
    @DisplayName("Test deleteAllCars method")
    public void testDeleteAllCars() {
        dao.insertCar("test_table", new CarRentalModel("AB1234", "Ford", "Mustang", 2022, null, CarRentalModel.State.AVAILABLE));
        dao.insertCar("test_table", new CarRentalModel("CD5678", "Tesla", "Model 3", 2021, null, CarRentalModel.State.RENTED));

        dao.deleteAllCars("test_table");

        List<CarRentalModel> cars = dao.getCars("test_table");
        assertEquals(0, cars.size());
    }

}