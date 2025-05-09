package com.olvera.hospital.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long doctorId;

    private String firstName;

    private String lastName;

    private String middleName;

    private String specialty;

}
