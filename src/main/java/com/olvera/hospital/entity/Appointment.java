package com.olvera.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private ConsultingRoom consultingRoom;

    private LocalDateTime consultationTime;

    private String patientName;
}
