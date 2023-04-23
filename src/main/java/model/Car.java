package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "carpool")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, name = "make")
    private String make;
    @Column(nullable = false, name = "model")
    private String model;
    @Column(nullable = false, name = "year")
    private int year;
    @Column(name = "rental_start")
    private LocalDate rentalStartDate;
    @Column(nullable = false, name = "state")
    private String state;
}