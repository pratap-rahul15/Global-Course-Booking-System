package com.undoo.booking.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ParentBookingResponse {

    private Long bookingId;
    private String offeringTitle;
    private String courseName;
    private List<ParentSessionInfo> sessions;
}