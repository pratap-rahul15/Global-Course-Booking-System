package com.undoo.booking.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddSessionsRequest {

    @NotBlank
    private String timezone;

    @NotEmpty
    private List<SessionRequest> sessions;
}