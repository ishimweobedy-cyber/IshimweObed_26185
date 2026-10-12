package com.auca.clinic.controller;

import com.auca.clinic.entity.Patient;
import com.auca.clinic.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAll() {
        return ResponseEntity.ok(patientService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Patient> opt = patientService.getById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("That patient does not exist");
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Patient patient) {
        String msg = patientService.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Patient patient) {
        patient.setId(id);
        String msg = patientService.save(patient);
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        String msg = patientService.delete(id);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // D2 — upcoming appointments
    @GetMapping("/{ssNumber}/upcoming")
    public ResponseEntity<?> getUpcoming(@PathVariable String ssNumber) {
        Object result = patientService.getUpcomingAppointments(ssNumber);
        if (result instanceof String msg) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(result);
    }
}
