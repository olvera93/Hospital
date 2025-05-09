package com.olvera.hospital.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "ConsultingRoom", description = "Schema to hold consulting room information")
public class ConsultingRoomDto {


    private String roomNumber;

    private String floor;
}
