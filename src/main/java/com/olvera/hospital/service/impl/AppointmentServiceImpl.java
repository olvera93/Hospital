package com.olvera.hospital.service.impl;

import com.olvera.hospital.dto.AppointmentDto;
import com.olvera.hospital.entity.Appointment;
import com.olvera.hospital.entity.ConsultingRoom;
import com.olvera.hospital.entity.Doctor;
import com.olvera.hospital.exception.ResourceAlreadyExistsException;
import com.olvera.hospital.exception.ResourceNotFoundException;
import com.olvera.hospital.repository.AppointmentRepository;
import com.olvera.hospital.repository.ConsultingRoomRepository;
import com.olvera.hospital.repository.DoctorRepository;
import com.olvera.hospital.service.IAppointmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.olvera.hospital.util.AppointmentStatus.PENDING;

@Service
@Slf4j
@AllArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {

    private AppointmentRepository appointmentRepository;

    private DoctorRepository doctorRepository;

    private ConsultingRoomRepository consultingRoomRepository;

    @Override
    public Appointment createAppointment(AppointmentDto appointmentDto) {

        LocalDateTime date =appointmentDto.getConsultationTime().toLocalDate().atStartOfDay();
        LocalDateTime time = appointmentDto.getConsultationTime();

        if (appointmentRepository.existsByConsultingRoomIdAndConsultationTime(appointmentDto.getConsultingRoomId(), time)) {
            throw new ResourceAlreadyExistsException("Consulting room is already occupied at this time.");
        }

        if (appointmentRepository.existsByDoctorIdAndConsultationTime(appointmentDto.getDoctorId(), time)) {
            throw new ResourceAlreadyExistsException("Doctor is already booked at this time.");
        }

        List<Appointment> sameDayAppointments = appointmentRepository
                .findByPatientNameAndConsultationTime(appointmentDto.getPatientName(), date);

        for (Appointment existing : sameDayAppointments) {
            long minutes = Math.abs(ChronoUnit.MINUTES.between(existing.getConsultationTime(), time));
            if (minutes < 120) {
                throw new IllegalArgumentException("Patient must have at least 2 hours between appointments on the same day.");
            }
        }

        Long doctorsCount = appointmentRepository.countByDoctorIdAndConsultationTime(appointmentDto.getDoctorId(), date);

        if (doctorsCount >= 8) {
            throw new IllegalArgumentException("Doctor cannot have more than 8 appointments per day.");
        }

        Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId()).orElseThrow(
                () -> new ResourceNotFoundException("Doctor", "doctorId", appointmentDto.getDoctorId().toString())
        );

        ConsultingRoom consultingRoom = consultingRoomRepository.findById(appointmentDto.getConsultingRoomId()).orElseThrow(
                () -> new ResourceNotFoundException("Consulting room", "consultingRoomId", appointmentDto.getConsultingRoomId().toString())
        );

        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .consultingRoom(consultingRoom)
                .consultationTime(appointmentDto.getConsultationTime())
                .patientName(appointmentDto.getPatientName())
                .status(PENDING)
                .build();

        Appointment appointmentSaved = appointmentRepository.save(appointment);
        log.info("Appointment created successfully with ID: {}", appointmentSaved.getId());

        return appointmentSaved;
    }




}
