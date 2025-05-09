package com.olvera.hospital.mapper;

import com.olvera.hospital.dto.AppointmentDto;
import com.olvera.hospital.entity.Appointment;

public class AppointmentMapper {

    public static AppointmentDto toDto (Appointment appointment) {

        return AppointmentDto.builder()
                .doctorId(appointment.getDoctor().getId())
                .consultingRoomId(appointment.getConsultingRoom().getId())
                .patientName(appointment.getPatientName() )
                .build();
    }

    public static Appointment toEntity (AppointmentDto appointmentDto) {




        return Appointment.builder()

                .patientName(appointmentDto.getPatientName())
                .build();
    }
}
