package com.undoo.booking.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOfferingRequest {

    @NotBlank
    private String title;

    @NotNull
    private Long teacherId;

    @NotNull
    private Long courseId;
}