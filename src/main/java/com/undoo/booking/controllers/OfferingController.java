package com.undoo.booking.controllers;

import com.undoo.booking.dtos.AddSessionsRequest;
import com.undoo.booking.dtos.CreateOfferingRequest;
import com.undoo.booking.dtos.OfferingResponse;
import com.undoo.booking.dtos.ParentOfferingResponse;
import com.undoo.booking.services.OfferingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offerings")
@RequiredArgsConstructor
public class OfferingController {

    private final OfferingService offeringService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OfferingResponse createOffering(
            @Valid @RequestBody CreateOfferingRequest request) {

        return offeringService.createOffering(request);
    }

    // Session Offering
    @PostMapping("/{offeringId}/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Long> addSessions(
            @PathVariable Long offeringId,
            @Valid @RequestBody AddSessionsRequest request) {

        return offeringService.addSessions(offeringId, request);
    }

    @GetMapping("/parent/{parentId}")
    public List<ParentOfferingResponse> getAvailableOfferings(
            @PathVariable Long parentId) {

        return offeringService.getAvailableOfferings(parentId);
    }
}