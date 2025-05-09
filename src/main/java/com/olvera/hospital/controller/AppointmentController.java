package com.olvera.hospital.controller;

import com.olvera.hospital.dto.AppointmentDto;
import com.olvera.hospital.dto.ErrorResponseDto;
import com.olvera.hospital.entity.Appointment;
import com.olvera.hospital.service.IAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Tag(
        name = "CRUD REST APIs for Appointments",
        description = "CRUD REST APIs in Appointments to CREATE, UPDATE, FETCH AND DELETE card details"
)
@RestController
@RequestMapping(path = "/api/appointment", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class AppointmentController {

    private IAppointmentService appointmentService;

    @Operation(
            summary = "Create Appointment REST API",
            description = "REST API to create new Appointment"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.createAppointment(appointmentDto);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Appointment REST API",
            description = "REST API to get appointmens"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/filter")
    public ResponseEntity<List<AppointmentDto>> queryAppointments(
            @RequestParam(value = "doctorId", required = false) Long doctorId,
            @RequestParam(value = "consultingRoomId", required = false) Long consultingRoomId,
            @RequestParam(value = "consultationDate", required = false) String consultationDate) {

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (consultationDate != null) {
            LocalDate date = LocalDate.parse(consultationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            startDateTime = date.atStartOfDay();
            endDateTime = date.plusDays(1).atStartOfDay();
        }

        List<AppointmentDto> appointments = appointmentService.filterAppointments(doctorId, consultingRoomId, startDateTime, endDateTime);

        return appointments.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(appointments);
    }

    @Operation(
            summary = "Cancel Appointment REST API",
            description = "REST API to cancel an appointment"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
        boolean isCancelled = appointmentService.cancelAppointment(appointmentId);

        if (isCancelled) {
            return ResponseEntity.ok("Appointment cancelled successfully.");
        } else {
            return ResponseEntity.status(400).body("Appointment cannot be cancelled. It may not be pending or doesn't exist.");
        }
    }

    @Operation(
            summary = "Edit Appointment REST API",
            description = "REST API to edit an appointment"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<AppointmentDto> edit(@PathVariable Long id, @Valid @RequestBody AppointmentDto dto) {
        return ResponseEntity.ok(appointmentService.editAppointment(id, dto));
    }


    private String getQueryType(Long doctorId, Long consultingRoomId, String consultationDate) {
        if (doctorId != null && consultingRoomId != null && consultationDate != null) {
            return "DOCTOR_CONSULTING_ROOM";
        } else if (doctorId != null) {
            return "DOCTOR";
        } else if (consultingRoomId != null) {
            return "CONSULTING_ROOM";
        } else if (consultationDate != null) {
            return "DATE";
        } else {
            return "DEFAULT";
        }
    }

}
