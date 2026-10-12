package com.auca.clinic.repository;

import com.auca.clinic.entity.Appointment;
import com.auca.clinic.entity.AppointmentStatus;
import com.auca.clinic.entity.Doctor;
import com.auca.clinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // B3 — DERIVED: check if doctor has a non-CANCELLED appointment on a date
    boolean existsByDoctorAndAppointmentDateAndStatusNot(
            Doctor doctor, LocalDate date, AppointmentStatus status);

    // D1 — DERIVED: doctor's appointments on a date, excluding CANCELLED
    List<Appointment> findByDoctorAndAppointmentDateAndStatusNot(
            Doctor doctor, LocalDate date, AppointmentStatus status);

    // D2 — DERIVED: patient's upcoming appointments (SCHEDULED or CONFIRMED), soonest first
    List<Appointment> findByPatientAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
            Patient patient, LocalDate date, List<AppointmentStatus> statuses);

    // Bonus: doctor workload — COMPLETED appointments in a date range, busiest first
    @Query("SELECT d.firstName, d.lastName, COUNT(a) FROM Appointment a " +
           "JOIN a.doctor d " +
           "WHERE a.status = :status " +
           "AND a.appointmentDate BETWEEN :start AND :end " +
           "GROUP BY d.id, d.firstName, d.lastName " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> findDoctorWorkload(@Param("status") AppointmentStatus status,
                                       @Param("start") LocalDate start,
                                       @Param("end") LocalDate end);
}
