package com.undoo.booking.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfferingResponse {

    private Long offeringId;
    private String title;
    private String courseName;
    private String teacherName;
}