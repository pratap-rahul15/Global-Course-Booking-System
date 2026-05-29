package com.undoo.booking.services;

import com.undoo.booking.dtos.BookOfferingRequest;
import com.undoo.booking.dtos.BookingResponse;
import com.undoo.booking.dtos.ParentBookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse bookOffering(BookOfferingRequest request);

    List<ParentBookingResponse> getBookings(Long parentId);
}