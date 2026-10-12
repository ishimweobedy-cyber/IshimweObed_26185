package com.auca.clinic.repository;

import com.auca.clinic.entity.Appointment;
import com.auca.clinic.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    // C2 — DERIVED: prescriptions by appointment
    List<Prescription> findByAppointment(Appointment appointment);

    // C4 — DERIVED: check if an appointment has any prescriptions (for delete protection)
    boolean existsByAppointment(Appointment appointment);

    // C3 — JPQL: prescription history of a patient across all appointments, newest first
    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.ssNumber = :ssNumber " +
           "ORDER BY p.issuedDate DESC")
    List<Prescription> findByPatientSsNumber(@Param("ssNumber") String ssNumber);

    // Bonus: top prescribed medicines
    @Query("SELECT p.medicineName, COUNT(p) FROM Prescription p " +
           "GROUP BY p.medicineName ORDER BY COUNT(p) DESC")
    List<Object[]> findTopMedicines();
}
