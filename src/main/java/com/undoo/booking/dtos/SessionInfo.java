package com.undoo.booking.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionInfo {

    private Integer sessionOrder;
    private String startTimeUtc;
    private String endTimeUtc;


}