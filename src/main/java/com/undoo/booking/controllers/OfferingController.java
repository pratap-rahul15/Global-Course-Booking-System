package com.undoo.booking.controllers;

import com.undoo.booking.dtos.CreateOfferingRequest;
import com.undoo.booking.dtos.OfferingResponse;
import com.undoo.booking.services.OfferingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}