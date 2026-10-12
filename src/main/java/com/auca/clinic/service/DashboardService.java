package com.auca.clinic.service;

import com.auca.clinic.entity.AppointmentStatus;
import com.auca.clinic.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    public DashboardService(PatientRepository patientRepository,
                            DoctorRepository doctorRepository,
                            OfficeRepository officeRepository,
                            AppointmentRepository appointmentRepository,
                            PrescriptionRepository prescriptionRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    // D4 — build summary map
    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("patients", patientRepository.count());
        summary.put("doctors", doctorRepository.count());
        summary.put("freeOffices", (long) officeRepository.findFreeOffices().size());

        long today = appointmentRepository.findAll().stream()
                .filter(a -> a.getAppointmentDate().equals(LocalDate.now()))
                .count();
        summary.put("appointmentsToday", today);

        long scheduled = appointmentRepository.findAll().stream()
                .filter(a -> a.getStatus() == AppointmentStatus.SCHEDULED)
                .count();
        summary.put("scheduled", scheduled);

        summary.put("prescriptions", prescriptionRepository.count());
        return summary;
    }
}
