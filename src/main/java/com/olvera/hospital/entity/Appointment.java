package com.olvera.hospital.entity;

import com.olvera.hospital.util.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private ConsultingRoom consultingRoom;

    private LocalDateTime consultationTime;

    private String patientName;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
