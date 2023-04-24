package model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class CarRentalModel {
    private final StringProperty plate = new SimpleStringProperty();
    private final StringProperty make = new SimpleStringProperty();
    private final StringProperty model = new SimpleStringProperty();
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> rentalStartDate = new SimpleObjectProperty<>();
    private final ObjectProperty<Car.State> state = new SimpleObjectProperty<>();

    public String getPlate() {
        return plate.get();
    }

    public void setPlate(String plate) {
        this.plate.set(plate);
    }

    public StringProperty plateProperty() {
        return plate;
    }

    public String getMake() {
        return make.get();
    }

    public void setMake(String make) {
        this.make.set(make);
    }

    public StringProperty makeProperty() {
        return make;
    }

    public String getModel() {
        return model.get();
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public StringProperty modelProperty() {
        return model;
    }

    public int getYear() {
        return year.get();
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public LocalDate getRentalStartDate() {
        return rentalStartDate.get();
    }

    public void setRentalStartDate(LocalDate rentalStartDate) {
        this.rentalStartDate.set(rentalStartDate);
    }

    public ObjectProperty<LocalDate> rentalStartDateProperty() {
        return rentalStartDate;
    }

    public Car.State getState() {
        return state.get();
    }

    public void setState(Car.State state) {
        this.state.set(state);
    }

    public ObjectProperty<Car.State> stateProperty() {
        return state;
    }
}
