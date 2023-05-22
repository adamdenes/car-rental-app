package hu.unideb.inf;


import model.CarRentalModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(CarRentalModel.class)
public interface CarDao {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS <tableName> ("
            + "plate VARCHAR PRIMARY KEY, "
            + "make VARCHAR NOT NULL, "
            + "model VARCHAR NOT NULL, "
            + "year INTEGER NOT NULL, "
            + "rentalStartDate INTEGER, "
            + "state VARCHAR NOT NULL)")
    void createTable(@Define("tableName") String tableName);

    @SqlUpdate("DROP TABLE IF EXISTS <tableName>")
    void dropTable(@Define("tableName") String tableName);

    @SqlQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name=:tableName")
    boolean checkTableExists(@Bind("tableName") String tableName);

    @SqlUpdate("INSERT INTO <tableName> VALUES (:plate, :make, :model, :year, :rentalStartDate, :state)")
    void insertCar(@Define("tableName") String tableName, @BindBean CarRentalModel car);

    @SqlQuery("SELECT * FROM <tableName> WHERE plate = :plate")
    Optional<CarRentalModel> getCarByPlate(@Define("tableName") String tableName, @Bind("plate") String plate);

    @SqlQuery("SELECT * FROM <tableName> ORDER BY plate")
    List<CarRentalModel> getCars(@Define("tableName") String tableName);

    @SqlUpdate("DELETE FROM <tableName>")
    void deleteAllCars(@Define("tableName") String tableName);

    @SqlUpdate("DELETE FROM <tableName> WHERE plate = :plate")
    void deleteCarByPlate(@Define("tableName") String tableName, @Bind("plate") String plate);


    @SqlUpdate("UPDATE <tableName> SET make = :make, model = :model, year = :year, "
            + "rentalStartDate = :rentalStartDate, state = :state WHERE plate = :plate")
    void updateCar(@Define("tableName") String tableName, @BindBean CarRentalModel car);

}