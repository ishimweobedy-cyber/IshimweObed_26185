package com.auca.clinic.repository;

import com.auca.clinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findBySsNumber(String ssNumber);

    boolean existsBySsNumber(String ssNumber);
}
