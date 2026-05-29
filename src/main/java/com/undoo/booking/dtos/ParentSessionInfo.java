package com.undoo.booking.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParentSessionInfo {

    private Integer sessionOrder;
    private String startTime;
    private String endTime;
}