package hu.unideb.inf;

import model.CarRentalModel;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static controller.CarRentalController.jdbi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testCreateTable() {
        assertTrue(dao.checkTableExists("test_table"));
    }

    @Test
    public void testCreateAndRetrieveCar() {
        CarRentalModel car = new CarRentalModel("ABC123", "Toyota", "Camry", 2020, null, CarRentalModel.State.AVAILABLE);
        dao.insertCar("test_table", car);

        Optional<CarRentalModel> retrievedCar = dao.getCarByPlate("test_table", "ABC123");
        assertTrue(retrievedCar.isPresent());
        assertEquals(car, retrievedCar.get());
    }

}