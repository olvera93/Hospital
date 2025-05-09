package com.olvera.hospital.service;

import com.olvera.hospital.dto.AppointmentDto;
import com.olvera.hospital.entity.Appointment;

public interface IAppointmentService {

    /**
     * This method is used to create an appointment.
     *
     * @param appointmentDto - AppointmentDto object containing appointment details
     * @return Appointment - The created appointment object
     */
    public Appointment createAppointment(AppointmentDto appointmentDto);

}
