package com.auca.clinic.service;

import com.auca.clinic.entity.Appointment;
import com.auca.clinic.entity.AppointmentStatus;
import com.auca.clinic.entity.Doctor;
import com.auca.clinic.repository.AppointmentRepository;
import com.auca.clinic.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PrescriptionRepository prescriptionRepository) {
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getById(Long id) {
        return appointmentRepository.findById(id);
    }

    // B1 — safe booking: block past dates, always start as SCHEDULED
    public String save(Appointment appointment) {
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
        return "Appointment saved successfully";
    }

    // B2 — status flow
    public String updateStatus(Long id, AppointmentStatus newStatus) {
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment appointment = opt.get();
        AppointmentStatus current = appointment.getStatus();

        boolean allowed = false;
        if (current == AppointmentStatus.SCHEDULED &&
                (newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELLED)) {
            allowed = true;
        } else if (current == AppointmentStatus.CONFIRMED &&
                (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED)) {
            allowed = true;
        }

        if (!allowed) {
            return "Cannot change status from " + current.name() + " to " + newStatus.name();
        }
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
        return "Appointment status updated successfully";
    }

    // B3 — reschedule
    public String reschedule(Long id, LocalDate newDate) {
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment appointment = opt.get();

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return "A COMPLETED appointment cannot be rescheduled";
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return "A CANCELLED appointment cannot be rescheduled";
        }
        if (newDate.isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }

        Doctor doctor = appointment.getDoctor();
        boolean conflict = appointmentRepository
                .existsByDoctorAndAppointmentDateAndStatusNot(
                        doctor, newDate, AppointmentStatus.CANCELLED);
        if (conflict) {
            return "Doctor is already booked on that date";
        }

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
        return "Appointment rescheduled successfully";
    }

    // C4 — delete with prescription guard
    public String delete(Long id) {
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        if (prescriptionRepository.existsByAppointment(opt.get())) {
            return "That appointment has prescriptions, delete them first";
        }
        appointmentRepository.deleteById(id);
        return "Appointment deleted successfully";
    }

    // General update (status field is intentionally NOT updated here — use /status endpoint)
    public String update(Long id, Appointment updated) {
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment existing = opt.get();
        existing.setAppointmentDate(updated.getAppointmentDate());
        existing.setPatient(updated.getPatient());
        existing.setDoctor(updated.getDoctor());
        // Status is deliberately not touched here
        appointmentRepository.save(existing);
        return "Appointment updated successfully";
    }
}
