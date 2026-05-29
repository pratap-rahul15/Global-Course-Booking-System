package com.undoo.booking.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookOfferingRequest {

    @NotNull
    private Long parentId;

    @NotNull
    private Long offeringId;
}