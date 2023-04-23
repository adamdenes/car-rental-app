package hu.inf.unideb;

import model.Car;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        var jdbi = Jdbi.create("jdbc:sqlite:/home/adenes/projects/java/car-rental-app/carpool.db");
        jdbi.installPlugin(new SqlObjectPlugin());

        try (var handle = jdbi.open()) {
            var cd = handle.attach(CarDao.class);

            if (cd.checkTableExists()) {
                cd.getCarByPlate("GHI789").ifPresent(System.out::println);
                System.out.println("Table already exists!");
            } else {
                cd.createTable();
                cd.insertCar(new Car("ABC123","Toyota", "Corolla", 2015, null, Car.State.AVAILABLE));
                cd.insertCar(new Car("DEF456","Toyota", "Yaris", 2017, LocalDate.now(), Car.State.RENTED));
                cd.insertCar(new Car("GHI789","Toyota", "Highlander", 2019, null, Car.State.AVAILABLE));
                cd.insertCar(new Car("JKL101","Toyota", "Camry", 2023, null, Car.State.AVAILABLE));
                cd.insertCar(new Car("MNO110","Toyota", "C-HR", 2022, LocalDate.now(), Car.State.RENTED));
            }
        }
    }

}
