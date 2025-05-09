package com.olvera.hospital.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "ConsultingRoom", description = "Schema to hold consulting room information")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultingRoomDto {


    private String roomNumber;

    private String floor;
}
