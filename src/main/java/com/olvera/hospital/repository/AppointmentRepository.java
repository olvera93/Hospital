package com.olvera.hospital.repository;

import com.olvera.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    /**
     * Check if an appointment exists for a specific consulting room at a specific time.
     *
     * @param consultingRoomId the ID of the consulting room
     * @param consultationTime the time of the appointment
     * @return true if the appointment exists, false otherwise
     */
    boolean existsByConsultingRoomIdAndConsultationTime(Long consultingRoomId, LocalDateTime consultationTime);

    /**
     * Check if an appointment exists for a specific doctor at a specific time.
     *
     * @param doctorId         the ID of the doctor
     * @param consultationTime the time of the appointment
     * @return true if the appointment exists, false otherwise
     */
    boolean existsByDoctorIdAndConsultationTime(Long doctorId, LocalDateTime consultationTime);

    /**
     * Find all appointments for a specific patient on a specific date.
     *
     * @param patientName the name of the patient
     * @param start        the date to check
     * @return a list of appointments
     */
    List<Appointment> findByPatientNameAndConsultationTimeBetween(String patientName, LocalDateTime start, LocalDateTime end);

    /**
     * Count the number of appointments for a specific doctor on a specific date.
     *
     * @param doctorId the ID of the doctor
     * @param start     the date to check
     * @return the count of appointments
     */

    Long countByDoctorIdAndConsultationTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);


}

