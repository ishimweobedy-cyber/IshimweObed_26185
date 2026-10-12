package com.auca.clinic.repository;

import com.auca.clinic.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Long> {

    Optional<Office> findByOfficeNumber(int officeNumber);

    // A3 — JPQL: offices not occupied by any doctor, sorted by office number
    @Query("SELECT o FROM Office o WHERE NOT EXISTS " +
           "(SELECT d FROM Doctor d WHERE d.office = o) ORDER BY o.officeNumber ASC")
    List<Office> findFreeOffices();
}
