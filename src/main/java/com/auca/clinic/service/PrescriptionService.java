package com.auca.clinic.service;

import com.auca.clinic.entity.Appointment;
import com.auca.clinic.entity.AppointmentStatus;
import com.auca.clinic.entity.Prescription;
import com.auca.clinic.repository.AppointmentRepository;
import com.auca.clinic.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                                AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // C1 — write a prescription
    public String save(Long appointmentId, Prescription prescription) {
        Optional<Appointment> apptOpt = appointmentRepository.findById(appointmentId);
        if (apptOpt.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment appointment = apptOpt.get();
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return "Prescriptions are only for COMPLETED appointments";
        }
        prescription.setIssuedDate(LocalDate.now());
        prescription.setAppointment(appointment);
        prescriptionRepository.save(prescription);
        return "Prescription saved successfully";
    }

    // C2 — prescriptions of an appointment
    public Object getByAppointment(Long appointmentId) {
        Optional<Appointment> apptOpt = appointmentRepository.findById(appointmentId);
        if (apptOpt.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        return prescriptionRepository.findByAppointment(apptOpt.get());
    }

    // C3 — prescription history of a patient (JPQL)
    public List<Prescription> getByPatient(String ssNumber) {
        return prescriptionRepository.findByPatientSsNumber(ssNumber);
    }

    // C4 — update prescription
    public String update(UUID id, Prescription updated) {
        Optional<Prescription> opt = prescriptionRepository.findById(id);
        if (opt.isEmpty()) {
            return "The prescription with that id does not exist";
        }
        Prescription existing = opt.get();
        existing.setMedicineName(updated.getMedicineName());
        existing.setDosage(updated.getDosage());
        existing.setDurationDays(updated.getDurationDays());
        prescriptionRepository.save(existing);
        return "Prescription updated successfully";
    }

    // C4 — delete prescription
    public String delete(UUID id) {
        if (!prescriptionRepository.existsById(id)) {
            return "The prescription with that id does not exist";
        }
        prescriptionRepository.deleteById(id);
        return "Prescription deleted successfully";
    }

    // Bonus — top medicines
    public List<Object[]> getTopMedicines() {
        return prescriptionRepository.findTopMedicines();
    }
}
