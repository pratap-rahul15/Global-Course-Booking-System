package com.undoo.booking.repositories;

import com.undoo.booking.entities.Parent;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Parent p where p.id = :parentId")
    Optional<Parent> findByIdForUpdate(Long parentId);
}