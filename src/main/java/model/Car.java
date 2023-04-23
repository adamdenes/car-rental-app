package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    public enum State {
        RENTED,
        AVAILABLE
    }

    private String plate;
    private String make;
    private String model;
    private int year;
    private LocalDate rentalStartDate;
    private State state;
}