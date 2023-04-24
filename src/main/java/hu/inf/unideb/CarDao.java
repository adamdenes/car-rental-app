package hu.inf.unideb;


import model.Car;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Car.class)
public interface CarDao {
    @SqlUpdate("""
            CREATE TABLE carpool (
                "plate" VARCHAR PRIMARY KEY,
                "make" VARCHAR NOT NULL,
                "model" VARCHAR NOT NULL,
                year INTEGER NOT NULL,
                "rentalStartDate" INTEGER,
                "state" VARCHAR NOT NULL
            )
            """
    )
    void createTable();

    @SqlUpdate("DROP TABLE carpool;")
    void dropTable();

    @SqlQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='carpool'")
    boolean checkTableExists();

    @SqlUpdate("INSERT INTO carpool VALUES (:plate, :make, :model, :year, :rentalStartDate, :state)")
    void insertCar(@Bind("plate") String plate,
                   @Bind("make") String make,
                   @Bind("model") String model,
                   @Bind("year") int year,
                   @Bind("rentalStartDate") LocalDate rentalStartDate,
                   @Bind("state") Car.State state);

    @SqlUpdate("INSERT INTO carpool VALUES (:plate, :make, :model, :year, :rentalStartDate, :state)")
    void insertCar(@BindBean Car car);

    @SqlQuery("SELECT * FROM carpool WHERE plate = :plate")
    Optional<Car> getCarByPlate(@Bind("plate") String plate);

    @SqlQuery("SELECT * FROM carpool ORDER BY plate")
    List<Car> getCars();

    @SqlUpdate("DELETE FROM carpool")
    void deleteAllCars();

    @SqlUpdate("DELETE FROM carpool WHERE plate = :plate")
    int deleteCarByPlate(@Bind("plate") String plate);


    @SqlUpdate("UPDATE carpool SET make = :make, model = :model, year = :year, " +
            "rentalStartDate = :rentalStartDate, state = :state WHERE plate = :plate")
    void updateCar(@BindBean Car car);
}