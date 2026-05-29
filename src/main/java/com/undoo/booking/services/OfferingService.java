package com.undoo.booking.services;

import com.undoo.booking.dtos.AddSessionsRequest;
import com.undoo.booking.dtos.CreateOfferingRequest;
import com.undoo.booking.dtos.OfferingResponse;
import com.undoo.booking.dtos.TeacherOfferingResponse;

import java.util.List;

public interface OfferingService {

    OfferingResponse createOffering(CreateOfferingRequest request);

    List<Long> addSessions(Long offeringId,
                           AddSessionsRequest request);

    List<TeacherOfferingResponse> getTeacherOfferings(Long teacherId);
}