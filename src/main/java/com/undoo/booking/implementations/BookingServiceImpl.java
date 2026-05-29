package com.undoo.booking.implementations;

import com.undoo.booking.dtos.BookOfferingRequest;
import com.undoo.booking.dtos.BookingResponse;
import com.undoo.booking.entities.*;
import com.undoo.booking.exceptions.BookingConflictException;
import com.undoo.booking.exceptions.ResourceNotFoundException;
import com.undoo.booking.repositories.*;

import com.undoo.booking.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ParentRepository parentRepository;
    private final OfferingRepository offeringRepository;
    private final BookingRepository bookingRepository;
    private final SessionRepository sessionRepository;

    @Override
    @Transactional
    public BookingResponse bookOffering(BookOfferingRequest request) {

        Parent parent = parentRepository.findByIdForUpdate(
                        request.getParentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parent not found"));

        Offering offering = offeringRepository.findById(
                        request.getOfferingId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Offering not found"));

        if (bookingRepository.existsByParentIdAndOfferingId(
                parent.getId(),
                offering.getId())) {

            throw new BookingConflictException(
                    "Offering already booked");
        }

        List<Session> newOfferingSessions =
                sessionRepository.findByOfferingId(
                        offering.getId());

        List<Booking> existingBookings =
                bookingRepository.findByParentId(
                        parent.getId());

        for (Booking booking : existingBookings) {

            List<Session> existingSessions =
                    sessionRepository.findByOfferingId(
                            booking.getOffering().getId());

            for (Session newSession : newOfferingSessions) {

                for (Session existingSession : existingSessions) {

                    if (hasOverlap(
                            newSession,
                            existingSession)) {

                        throw new BookingConflictException(
                                "Booking conflict detected");
                    }
                }
            }
        }

        Booking savedBooking =
                bookingRepository.save(
                        Booking.builder()
                                .parent(parent)
                                .offering(offering)
                                .bookedAt(java.time.Instant.now())
                                .build()
                );

        return BookingResponse.builder()
                .bookingId(savedBooking.getId())
                .parentId(parent.getId())
                .offeringId(offering.getId())
                .message("Booking successful")
                .build();

    }

    private boolean hasOverlap(
            Session newSession,
            Session existingSession) {

        return newSession.getStartTimeUtc()
                .isBefore(existingSession.getEndTimeUtc())
                &&
                newSession.getEndTimeUtc()
                        .isAfter(existingSession.getStartTimeUtc());
    }
}