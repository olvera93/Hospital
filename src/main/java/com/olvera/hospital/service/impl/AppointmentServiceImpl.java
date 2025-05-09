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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.olvera.hospital.util.AppointmentStatus.CANCELLED;
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

        LocalDateTime requestedTime = appointmentDto.getConsultationTime();
        LocalDate date = requestedTime.toLocalDate();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        LocalDateTime time = appointmentDto.getConsultationTime();

        if (appointmentRepository.existsByConsultingRoomIdAndConsultationTime(appointmentDto.getConsultingRoomId(), time)) {
            throw new ResourceAlreadyExistsException("Consulting room is already occupied at this time.");
        }

        if (appointmentRepository.existsByDoctorIdAndConsultationTime(appointmentDto.getDoctorId(), time)) {
            throw new ResourceAlreadyExistsException("Doctor is already booked at this time.");
        }

        List<Appointment> sameDayAppointments = appointmentRepository
                .findByPatientNameAndConsultationTimeBetween(appointmentDto.getPatientName(), startOfDay, endOfDay);

        for (Appointment existing : sameDayAppointments) {
            long minutes = Math.abs(ChronoUnit.MINUTES.between(existing.getConsultationTime(), time));
            if (minutes < 120) {
                throw new IllegalArgumentException("Patient must have at least 2 hours between appointments on the same day.");
            }
        }

        Long doctorsCount = appointmentRepository.countByDoctorIdAndConsultationTimeBetween(appointmentDto.getDoctorId(), startOfDay, endOfDay);

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


    @Override
    public List<AppointmentDto> filterAppointments(Long doctorId, Long consultingRoomId, LocalDateTime start, LocalDateTime end) {
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream()
                .filter(a -> doctorId == null || a.getDoctor().getId().equals(doctorId))
                .filter(a -> consultingRoomId == null || a.getConsultingRoom().getId().equals(consultingRoomId))
                .filter(a -> start == null ||
                        (a.getConsultationTime().isEqual(start) || a.getConsultationTime().isAfter(start)) &&
                                a.getConsultationTime().isBefore(end))
                .map(
                        appointment -> AppointmentDto.builder()
                                .doctorId(appointment.getDoctor().getId())
                                .consultingRoomId(appointment.getConsultingRoom().getId())
                                .consultationTime(appointment.getConsultationTime())
                                .patientName(appointment.getPatientName())
                                .build()
                )
                .toList();
    }

    @Override
    public boolean cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

        if (appointment == null || !appointment.getStatus().equals(PENDING)) {
            return false;
        }

        appointment.setStatus(CANCELLED);
        appointmentRepository.delete(appointment);
        return true;
    }

    @Override
    public AppointmentDto editAppointment(Long id, AppointmentDto dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Verifica que la nueva hora no sea pasada
        if (dto.getConsultationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot edit to a past consultation time");
        }

        // Verifica si el doctor tiene ya 8 citas ese día (sin contar la actual)
        LocalDate date = dto.getConsultationTime().toLocalDate();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        long existingAppointments = appointmentRepository.countByDoctorIdAndConsultationTimeBetween(dto.getDoctorId(), start, end);
        if (!appointment.getConsultationTime().toLocalDate().equals(date)) {
            // Solo cuenta si cambió el día
            if (existingAppointments >= 8) {
                throw new RuntimeException("Doctor already has 8 appointments that day");
            }
        }

        // Actualiza los campos
        appointment.setConsultationTime(dto.getConsultationTime());
        appointment.setPatientName(dto.getPatientName());
        appointment.setDoctor(new Doctor(dto.getDoctorId(), null, null, null, null));
        appointment.setConsultingRoom(new ConsultingRoom(dto.getConsultingRoomId(), null, null));

        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentDto.builder()
                .doctorId(saved.getDoctor().getId())
                .consultingRoomId(saved.getConsultingRoom().getId())
                .consultationTime(saved.getConsultationTime())
                .patientName(saved.getPatientName())
                .build();
    }



}
