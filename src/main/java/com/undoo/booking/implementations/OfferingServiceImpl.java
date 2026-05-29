package com.undoo.booking.implementations;

import com.undoo.booking.dtos.*;
import com.undoo.booking.entities.*;
import com.undoo.booking.exceptions.ResourceNotFoundException;
import com.undoo.booking.repositories.*;

import com.undoo.booking.services.OfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.*;
import java.util.ArrayList;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class OfferingServiceImpl implements OfferingService {

    private final OfferingRepository offeringRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final SessionRepository sessionRepository;
    private final ParentRepository parentRepository;

    @Override
    public OfferingResponse createOffering(CreateOfferingRequest request) {

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        Offering offering = Offering.builder()
                .title(request.getTitle())
                .teacher(teacher)
                .course(course)
                .build();

        Offering savedOffering = offeringRepository.save(offering);

        return OfferingResponse.builder()
                .offeringId(savedOffering.getId())
                .title(savedOffering.getTitle())
                .teacherName(teacher.getName())
                .courseName(course.getName())
                .build();
    }

    @Override
    public List<Long> addSessions(Long offeringId,
                                  AddSessionsRequest request) {

        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Offering not found"));

        ZoneId teacherZone = ZoneId.of(request.getTimezone());

        List<Long> sessionIds = new ArrayList<>();

        for (SessionRequest sessionRequest : request.getSessions()) {

            LocalDateTime startLocal =
                    LocalDateTime.parse(sessionRequest.getStartTime());

            LocalDateTime endLocal =
                    LocalDateTime.parse(sessionRequest.getEndTime());

            Instant startUtc =
                    startLocal.atZone(teacherZone).toInstant();

            Instant endUtc =
                    endLocal.atZone(teacherZone).toInstant();

            Session session = Session.builder()
                    .offering(offering)
                    .sessionOrder(sessionRequest.getSessionOrder())
                    .startTimeUtc(startUtc)
                    .endTimeUtc(endUtc)
                    .build();

            Session savedSession = sessionRepository.save(session);

            sessionIds.add(savedSession.getId());
        }

        return sessionIds;
    }

    @Override
    public List<TeacherOfferingResponse> getTeacherOfferings(Long teacherId) {
        return null;
    }

    @Override
    public List<ParentOfferingResponse> getAvailableOfferings(Long parentId) {

        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parent not found"));

        ZoneId parentZone =
                ZoneId.of(parent.getTimezone());

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm");

        return offeringRepository.findAll()
                .stream()
                .map(offering -> {

                    List<ParentSessionInfo> sessions =
                            sessionRepository
                                    .findByOfferingIdOrderBySessionOrder(
                                            offering.getId())
                                    .stream()
                                    .map(session -> {

                                        String start =
                                                session.getStartTimeUtc()
                                                        .atZone(parentZone)
                                                        .format(formatter);

                                        String end =
                                                session.getEndTimeUtc()
                                                        .atZone(parentZone)
                                                        .format(formatter);

                                        return ParentSessionInfo.builder()
                                                .sessionOrder(
                                                        session.getSessionOrder())
                                                .startTime(start)
                                                .endTime(end)
                                                .build();
                                    })
                                    .toList();

                    return ParentOfferingResponse.builder()
                            .offeringId(offering.getId())
                            .title(offering.getTitle())
                            .courseName(
                                    offering.getCourse().getName())
                            .sessions(sessions)
                            .build();
                })
                .toList();
    }


}