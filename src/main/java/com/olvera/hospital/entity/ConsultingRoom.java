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
public class ConsultingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long officeId;

    private String roomNumber;

    private String floor;

}
