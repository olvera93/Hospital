package com.olvera.hospital.service;

import com.olvera.hospital.dto.AppointmentDto;
import com.olvera.hospital.entity.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {

    public Appointment createAppointment(AppointmentDto appointmentDto);

    List<AppointmentDto> filterAppointments(Long doctorId, Long consultingRoomId, LocalDateTime start, LocalDateTime end);

    public boolean cancelAppointment(Long appointmentId);

    public AppointmentDto editAppointment(Long appointmentId, AppointmentDto appointmentDto);



}
