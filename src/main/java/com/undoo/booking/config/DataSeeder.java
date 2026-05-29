package com.undoo.booking.config;

import com.undoo.booking.entities.Course;
import com.undoo.booking.entities.Parent;
import com.undoo.booking.entities.Teacher;
import com.undoo.booking.repositories.CourseRepository;
import com.undoo.booking.repositories.ParentRepository;
import com.undoo.booking.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) {

        if (teacherRepository.count() == 0) {

            teacherRepository.save(
                    Teacher.builder()
                            .name("Rahul Teacher")
                            .timezone("Asia/Kolkata")
                            .build()
            );
        }

        if (parentRepository.count() == 0) {

            parentRepository.save(
                    Parent.builder()
                            .name("Vikash Parent")
                            .timezone("America/New_York")
                            .build()
            );
        }

        if (courseRepository.count() == 0) {

            courseRepository.save(
                    Course.builder()
                            .name("Python Coding")
                            .description("Beginner Python Course")
                            .build()
            );
        }
    }
}