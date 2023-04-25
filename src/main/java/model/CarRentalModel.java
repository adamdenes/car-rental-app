package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

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

    private String plate;
    private String make;
    private String model;
    private int year;
    private LocalDate rentalStartDate;
    private State state;
}