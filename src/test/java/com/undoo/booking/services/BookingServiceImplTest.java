package com.undoo.booking.services;

import com.undoo.booking.dtos.BookOfferingRequest;
import com.undoo.booking.dtos.BookingResponse;
import com.undoo.booking.entities.*;
import com.undoo.booking.exceptions.BookingConflictException;
import com.undoo.booking.implementations.BookingServiceImpl;
import com.undoo.booking.repositories.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private OfferingRepository offeringRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    // Offer Booking Testing
    @Test
    void shouldBookOfferingSuccessfully() {

        Parent parent = Parent.builder()
                .id(1L)
                .name("Parent")
                .timezone("Asia/Kolkata")
                .build();

        Offering offering = Offering.builder()
                .id(1L)
                .title("Saturday Batch")
                .build();

        BookOfferingRequest request = new BookOfferingRequest();
        request.setParentId(1L);
        request.setOfferingId(1L);

        when(parentRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(parent));

        when(offeringRepository.findById(1L))
                .thenReturn(Optional.of(offering));

        when(bookingRepository.existsByParentIdAndOfferingId(1L, 1L))
                .thenReturn(false);

        when(sessionRepository.findByOfferingId(1L))
                .thenReturn(List.of());

        when(bookingRepository.findByParentId(1L))
                .thenReturn(List.of());

        when(bookingRepository.save(any()))
                .thenAnswer(invocation -> {

                    Booking booking = invocation.getArgument(0);
                    booking.setId(100L);
                    booking.setBookedAt(Instant.now());

                    return booking;
                });

        BookingResponse response =
                bookingService.bookOffering(request);

        assertEquals("Booking successful",
                response.getMessage());

        assertEquals(100L,
                response.getBookingId());
    }


    // Duplicate Booking Testing

    @Test
    void shouldRejectDuplicateBooking() {

        Parent parent = Parent.builder()
                .id(1L)
                .build();

        Offering offering = Offering.builder()
                .id(1L)
                .build();

        BookOfferingRequest request =
                new BookOfferingRequest();

        request.setParentId(1L);
        request.setOfferingId(1L);

        when(parentRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(parent));

        when(offeringRepository.findById(1L))
                .thenReturn(Optional.of(offering));

        when(bookingRepository.existsByParentIdAndOfferingId(1L, 1L))
                .thenReturn(true);

        assertThrows(
                BookingConflictException.class,
                () -> bookingService.bookOffering(request)
        );
    }

    // Session Overlap testing

    @Test
    void shouldRejectBookingWhenSessionsOverlap() {

        Parent parent = Parent.builder()
                .id(1L)
                .build();

        Offering newOffering = Offering.builder()
                .id(2L)
                .build();

        Offering existingOffering = Offering.builder()
                .id(1L)
                .build();

        Booking existingBooking = Booking.builder()
                .offering(existingOffering)
                .build();

        Session newSession = Session.builder()
                .startTimeUtc(
                        Instant.parse(
                                "2026-06-14T12:30:00Z"))
                .endTimeUtc(
                        Instant.parse(
                                "2026-06-14T13:30:00Z"))
                .build();

        Session existingSession = Session.builder()
                .startTimeUtc(
                        Instant.parse(
                                "2026-06-14T12:00:00Z"))
                .endTimeUtc(
                        Instant.parse(
                                "2026-06-14T13:00:00Z"))
                .build();

        BookOfferingRequest request =
                new BookOfferingRequest();

        request.setParentId(1L);
        request.setOfferingId(2L);

        when(parentRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(parent));

        when(offeringRepository.findById(2L))
                .thenReturn(Optional.of(newOffering));

        when(bookingRepository.existsByParentIdAndOfferingId(1L, 2L))
                .thenReturn(false);

        when(sessionRepository.findByOfferingId(2L))
                .thenReturn(List.of(newSession));

        when(bookingRepository.findByParentId(1L))
                .thenReturn(List.of(existingBooking));

        when(sessionRepository.findByOfferingId(1L))
                .thenReturn(List.of(existingSession));

        assertThrows(
                BookingConflictException.class,
                () -> bookingService.bookOffering(request)
        );
    }


}