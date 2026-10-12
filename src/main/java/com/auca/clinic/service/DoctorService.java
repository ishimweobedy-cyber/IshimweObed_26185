package com.auca.clinic.service;

import com.auca.clinic.entity.*;
import com.auca.clinic.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final OfficeRepository officeRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorService(DoctorRepository doctorRepository,
                         SpecializationRepository specializationRepository,
                         OfficeRepository officeRepository,
                         AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
        this.officeRepository = officeRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getById(Long id) {
        return doctorRepository.findById(id);
    }

    public String save(Doctor doctor) {
        doctorRepository.save(doctor);
        return "Doctor saved successfully";
    }

    public String delete(Long id) {
        if (!doctorRepository.existsById(id)) {
            return "The doctor with that id does not exist";
        }
        doctorRepository.deleteById(id);
        return "Doctor deleted successfully";
    }

    // A1 — add specialization to doctor
    public String addSpecialization(Long doctorId, Long specializationId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Specialization> specOpt = specializationRepository.findById(specializationId);
        if (specOpt.isEmpty()) {
            return "That specialization does not exist";
        }
        Doctor doctor = doctorOpt.get();
        Specialization spec = specOpt.get();
        if (doctor.getSpecializations().contains(spec)) {
            return "The doctor already has that specialization";
        }
        doctor.getSpecializations().add(spec);
        doctorRepository.save(doctor);
        return "Specialization added to the doctor";
    }

    // A2 — remove specialization from doctor
    public String removeSpecialization(Long doctorId, Long specializationId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Specialization> specOpt = specializationRepository.findById(specializationId);
        if (specOpt.isEmpty()) {
            return "That specialization does not exist";
        }
        Doctor doctor = doctorOpt.get();
        Specialization spec = specOpt.get();
        if (!doctor.getSpecializations().contains(spec)) {
            return "The doctor does not have that specialization";
        }
        doctor.getSpecializations().remove(spec);
        doctorRepository.save(doctor);
        return "Specialization removed from the doctor";
    }

    // A4 — move doctor to another office
    public String changeOffice(Long doctorId, int officeNumber) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Office> officeOpt = officeRepository.findByOfficeNumber(officeNumber);
        if (officeOpt.isEmpty()) {
            return "The office with that office number does not exist";
        }
        Doctor doctor = doctorOpt.get();
        Office newOffice = officeOpt.get();

        // Check if office is taken by a DIFFERENT doctor
        if (doctorRepository.existsByOffice(newOffice) && !newOffice.equals(doctor.getOffice())) {
            return "That office is taken by other doctor";
        }
        doctor.setOffice(newOffice);
        doctorRepository.save(doctor);
        return "Office changed successfully";
    }

    // D1 — doctor's agenda for a day
    public Object getDailyAgenda(Long doctorId, LocalDate date) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        return appointmentRepository.findByDoctorAndAppointmentDateAndStatusNot(
                doctorOpt.get(), date, AppointmentStatus.CANCELLED);
    }

    // D3 — search doctors by name (JPQL)
    public List<Doctor> searchByName(String name) {
        return doctorRepository.searchByName(name);
    }

    // Bonus — doctor workload
    public List<Object[]> getWorkload(java.time.LocalDate start, java.time.LocalDate end) {
        return appointmentRepository.findDoctorWorkload(
                com.auca.clinic.entity.AppointmentStatus.COMPLETED, start, end);
    }
}
