package com.undoo.booking.repositories;

import com.undoo.booking.entities.Offering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferingRepository extends JpaRepository<Offering, Long> {

    List<Offering> findByTeacherId(Long teacherId);
}