package com.olvera.hospital.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Schema(name = "Appointment", description = "Schema to hold appointment information")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDto {

    @Schema(description = "Doctor ID", example = "1")
    @NotNull(message = "Doctor ID cannot be null or empty")
    private Long doctorId;

    @Schema(description = "Consulting Room ID", example = "1")
    @NotNull(message = "Consulting Room ID cannot be null or empty")
    private Long consultingRoomId;

    @Schema(description = "Consultation Time", format = "" +
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            example = "2025-05-09T10:00:00")
    @NotNull(message = "Consultation Time cannot be null or empty")
    private LocalDateTime consultationTime;

    @Schema(description = "Patient Name", example = "John Doe")
    @NotNull(message = "Patient Name cannot be null or empty")
    private String patientName;
}
