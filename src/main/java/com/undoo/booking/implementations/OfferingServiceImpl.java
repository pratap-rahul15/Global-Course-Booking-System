package com.undoo.booking.implementations;

import com.undoo.booking.dtos.AddSessionsRequest;
import com.undoo.booking.dtos.CreateOfferingRequest;
import com.undoo.booking.dtos.OfferingResponse;
import com.undoo.booking.entities.*;
import com.undoo.booking.exceptions.ResourceNotFoundException;
import com.undoo.booking.repositories.*;

import com.undoo.booking.services.OfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferingServiceImpl implements OfferingService {

    private final OfferingRepository offeringRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final SessionRepository sessionRepository;

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
    public void addSessions(Long offeringId, AddSessionsRequest request) {

    }

    @Override
    public List<OfferingResponse> getTeacherOfferings(Long teacherId) {
        return null;
    }
}