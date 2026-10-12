package com.auca.clinic.repository;

import com.auca.clinic.entity.Doctor;
import com.auca.clinic.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // A4 — DERIVED: check if another doctor already occupies this office
    boolean existsByOffice(Office office);

    // D3 — JPQL: search by first or last name, case-insensitive, sorted by last name
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY d.lastName ASC")
    List<Doctor> searchByName(@Param("name") String name);
}
