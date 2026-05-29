package com.undoo.booking.dtos;

// Used when teacher adds the sessions
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SessionRequest {

    @NotNull
    private Integer sessionOrder;

    @NotBlank
    private String startTime;

    @NotBlank
    private String endTime;
}