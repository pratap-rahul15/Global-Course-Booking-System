package com.undoo.booking.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ParentOfferingResponse {

    private Long offeringId;
    private String title;
    private String courseName;
    private List<ParentSessionInfo> sessions;
}