package com.auca.clinic.service;

import com.auca.clinic.entity.Specialization;
import com.auca.clinic.repository.SpecializationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public List<Specialization> getAll() {
        return specializationRepository.findAll();
    }

    public Optional<Specialization> getById(Long id) {
        return specializationRepository.findById(id);
    }

    public String save(Specialization specialization) {
        specializationRepository.save(specialization);
        return "Specialization saved successfully";
    }

    public String delete(Long id) {
        if (!specializationRepository.existsById(id)) {
            return "That specialization does not exist";
        }
        specializationRepository.deleteById(id);
        return "Specialization deleted successfully";
    }
}
