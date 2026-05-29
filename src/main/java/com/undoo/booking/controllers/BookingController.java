package com.undoo.booking.controllers;

import com.undoo.booking.dtos.BookOfferingRequest;
import com.undoo.booking.dtos.BookingResponse;
import com.undoo.booking.dtos.ParentBookingResponse;
import com.undoo.booking.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse bookOffering(@Valid @RequestBody BookOfferingRequest request) {
        return bookingService.bookOffering(request);
    }

    @GetMapping("/parent/{parentId}")
    public List<ParentBookingResponse> getBookings(
            @PathVariable Long parentId) {

        return bookingService.getBookings(parentId);
    }
}