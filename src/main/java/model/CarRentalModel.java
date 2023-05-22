package model;

import controller.CarRentalController;
import hu.unideb.inf.CarDao;
import hu.unideb.inf.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.tinylog.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarRentalModel {
    public enum State {
        RENTED,
        AVAILABLE
    }

    public static final String carpool = "carpool";
    public static final Jdbi jdbi = Jdbi.create("jdbc:sqlite:carpool.db");

    private String plate;
    private String make;
    private String model;
    private int year;
    private LocalDate rentalStartDate;
    private State state;

    static {
        jdbi.installPlugin(new SqlObjectPlugin());
    }

    public static List<CarRentalModel> loadDb() {
        return jdbi.withHandle(handle -> handle.attach(CarDao.class).getCars(carpool));
    }

    public static void addOrUpdateCar(CarRentalModel car) {
        jdbi.useHandle(handle -> {
            CarDao cd = handle.attach(CarDao.class);
            Optional<CarRentalModel> exists = cd.getCarByPlate(carpool, car.getPlate());
            if (exists.isEmpty()) {
                // create a new car record
                cd.insertCar(CarRentalModel.carpool, car);
                Logger.debug("Creating new car: " + car);
            } else {
                // update the record
                exists.get().setMake(car.getMake());
                exists.get().setModel(car.getModel());
                exists.get().setYear(car.getYear());
                exists.get().setRentalStartDate(car.getRentalStartDate());
                exists.get().setState(car.getState());

                cd.updateCar(carpool, exists.get());
                Logger.debug("Updating car: " + exists.get());
            }
        });
    }

    public static void deleteRow(String plateToDelete) throws CarRentalException {
        jdbi.useHandle(handle -> {
            CarDao cd = handle.attach(CarDao.class);
            Optional<CarRentalModel> exists = cd.getCarByPlate(carpool, plateToDelete);
            if (exists.isPresent()) {
                cd.deleteCarByPlate(carpool, plateToDelete);
                Logger.debug("Row deleted from table: " + plateToDelete);
            } else {
                Logger.error("There is no such car in the database: " + plateToDelete);
                throw new CarRentalException("There is no such car in the database!");
            }
        });
    }

    public static void rentCar(String plateToRent, LocalDate startDate) throws CarRentalException {
        jdbi.useHandle(handle -> {
            CarDao cd = handle.attach(CarDao.class);
            Optional<CarRentalModel> exists = cd.getCarByPlate(carpool, plateToRent);

            if (exists.isEmpty()) {
                Logger.error("Car doesn't exist with plate: " + plateToRent);
                throw new CarRentalException("There is no such car in the database!");
            } else if (exists.get().getRentalStartDate() != null) {
                Logger.error("Car already rented since: " + exists.get().getRentalStartDate());
                throw new CarRentalException("This car is not available for renting at the moment!");
            } else {
                CarRentalModel car = exists.get();
                car.setState(State.RENTED);
                car.setRentalStartDate(startDate);

                cd.updateCar(carpool, car);
                Logger.debug("Car rented from " + startDate + ", state changed to RENTED");
            }
        });
    }

    public static void returnCar(String plateToReturn) throws CarRentalException {
        jdbi.useHandle(handle -> {
            CarDao cd = handle.attach(CarDao.class);
            Optional<CarRentalModel> exists = cd.getCarByPlate(carpool, plateToReturn);
            if (exists.isPresent()) {
                exists.get().setState(State.AVAILABLE);
                exists.get().setRentalStartDate(null);
                cd.updateCar(carpool, exists.get());
                Logger.debug("Car has been successfully returned to the pool: " + plateToReturn);
            } else {
                Logger.error("There is no such car in the database: " + plateToReturn);
                throw new CarRentalException("There is no such car in the database!");
            }
        });
    }
}