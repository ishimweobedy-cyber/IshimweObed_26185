package com.auca.clinic.service;

import com.auca.clinic.entity.AppointmentStatus;
import com.auca.clinic.entity.Patient;
import com.auca.clinic.repository.AppointmentRepository;
import com.auca.clinic.repository.PatientRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getById(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> getBySsNumber(String ssNumber) {
        return patientRepository.findBySsNumber(ssNumber);
    }

    public String save(Patient patient) {
        patientRepository.save(patient);
        return "Patient saved successfully";
    }

    public String delete(Long id) {
        if (!patientRepository.existsById(id)) {
            return "That patient does not exist";
        }
        patientRepository.deleteById(id);
        return "Patient deleted successfully";
    }

    // D2 — upcoming appointments for a patient
    public Object getUpcomingAppointments(String ssNumber) {
        Optional<Patient> patientOpt = patientRepository.findBySsNumber(ssNumber);
        if (patientOpt.isEmpty()) {
            return "That patient with that ssnumber does not exist";
        }
        List<AppointmentStatus> statuses = List.of(
                AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED);
        return appointmentRepository
                .findByPatientAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
                        patientOpt.get(), LocalDate.now(), statuses);
    }
}
