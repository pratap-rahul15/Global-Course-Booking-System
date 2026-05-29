package com.undoo.booking.services;

import com.undoo.booking.dtos.*;

import java.util.List;

public interface OfferingService {

    OfferingResponse createOffering(CreateOfferingRequest request);

    List<Long> addSessions(Long offeringId,
                           AddSessionsRequest request);

    List<TeacherOfferingResponse> getTeacherOfferings(Long teacherId);


    List<ParentOfferingResponse> getAvailableOfferings(Long parentId);
}