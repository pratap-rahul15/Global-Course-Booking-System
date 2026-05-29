package com.undoo.booking.services;

import com.undoo.booking.dtos.BookOfferingRequest;
import com.undoo.booking.dtos.BookingResponse;

public interface BookingService {

    BookingResponse bookOffering(BookOfferingRequest request);
}