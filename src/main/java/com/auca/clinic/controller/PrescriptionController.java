package com.auca.clinic.controller;

import com.auca.clinic.entity.Prescription;
import com.auca.clinic.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    // C1 — write a prescription
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam Long appointmentId,
                                        @RequestBody Prescription prescription) {
        String msg = prescriptionService.save(appointmentId, prescription);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.contains("only for COMPLETED")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    // C2 — prescriptions of an appointment
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getByAppointment(@PathVariable Long appointmentId) {
        Object result = prescriptionService.getByAppointment(appointmentId);
        if (result instanceof String msg) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(result);
    }

    // C3 — prescription history of a patient
    @GetMapping("/patient/{ssNumber}")
    public ResponseEntity<List<Prescription>> getByPatient(@PathVariable String ssNumber) {
        return ResponseEntity.ok(prescriptionService.getByPatient(ssNumber));
    }

    // C4 — update prescription
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id,
                                          @RequestBody Prescription prescription) {
        String msg = prescriptionService.update(id, prescription);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // C4 — delete prescription
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String msg = prescriptionService.delete(id);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // Bonus — top medicines
    @GetMapping("/stats/top-medicines")
    public ResponseEntity<List<Object[]>> getTopMedicines() {
        return ResponseEntity.ok(prescriptionService.getTopMedicines());
    }
}
