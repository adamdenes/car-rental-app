package hu.inf.unideb;

import javafx.application.Application;
import model.Car;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.time.LocalDate;

public class Main {

    public static final String dbPath = "/home/adenes/projects/java/car-rental-app/carpool.db";

    public static void main(String[] args) {
        // SQL will be called from CarRentalApp FX class
        /*var jdbi = Jdbi.create("jdbc:sqlite:" + dbPath);
        jdbi.installPlugin(new SqlObjectPlugin());

        try (var handle = jdbi.open()) {
            var cd = handle.attach(CarDao.class);

            if (cd.checkTableExists()) {
                cd.getCarByPlate("GHI789").ifPresent(System.out::println);
                System.out.println("Table already exists!");
                cd.dropTable();
                System.out.println("Table dropped!");
            }

            cd.createTable();
            cd.insertCar(new Car("ABC123", "Toyota", "Corolla", 2015, null, Car.State.AVAILABLE));
            cd.insertCar(new Car("DEF456", "Toyota", "Yaris", 2017, LocalDate.now(), Car.State.RENTED));
            cd.insertCar(new Car("GHI789", "Toyota", "Highlander", 2019, null, Car.State.AVAILABLE));
            cd.insertCar(new Car("JKL101", "Toyota", "Camry", 2023, null, Car.State.AVAILABLE));
            cd.insertCar(new Car("MNO110", "Toyota", "C-HR", 2022, LocalDate.now(), Car.State.RENTED));
        }*/

        Application.launch(SceneSwitchingApplication.class, args);
    }

}
