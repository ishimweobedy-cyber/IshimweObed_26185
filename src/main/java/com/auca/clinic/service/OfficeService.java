package com.auca.clinic.service;

import com.auca.clinic.entity.Office;
import com.auca.clinic.repository.OfficeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    public List<Office> getAll() {
        return officeRepository.findAll();
    }

    public Optional<Office> getById(Long id) {
        return officeRepository.findById(id);
    }

    public Optional<Office> getByOfficeNumber(int officeNumber) {
        return officeRepository.findByOfficeNumber(officeNumber);
    }

    public String save(Office office) {
        officeRepository.save(office);
        return "Office saved successfully";
    }

    public String delete(Long id) {
        if (!officeRepository.existsById(id)) {
            return "That office does not exist";
        }
        officeRepository.deleteById(id);
        return "Office deleted successfully";
    }

    // A3 — free offices via JPQL
    public List<Office> getFreeOffices() {
        return officeRepository.findFreeOffices();
    }
}
