package com.undoo.booking.services;

import com.undoo.booking.dtos.AddSessionsRequest;
import com.undoo.booking.dtos.CreateOfferingRequest;
import com.undoo.booking.dtos.OfferingResponse;

import java.util.List;

public interface OfferingService {

    OfferingResponse createOffering(CreateOfferingRequest request);

    void addSessions(Long offeringId, AddSessionsRequest request);

    List<OfferingResponse> getTeacherOfferings(Long teacherId);
}