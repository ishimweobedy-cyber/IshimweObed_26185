package com.auca.clinic.controller;

import com.auca.clinic.entity.Doctor;
import com.auca.clinic.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAll() {
        return ResponseEntity.ok(doctorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Doctor> opt = doctorService.getById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The doctor with that id does not exist");
    }

    // D3 — search doctors by name (fixed path before /{id})
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> search(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchByName(name));
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Doctor doctor) {
        String msg = doctorService.save(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Doctor doctor) {
        doctor.setId(id);
        String msg = doctorService.save(doctor);
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        String msg = doctorService.delete(id);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // A1 — add specialization
    @PutMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<String> addSpecialization(@PathVariable Long doctorId,
                                                     @PathVariable Long specializationId) {
        String msg = doctorService.addSpecialization(doctorId, specializationId);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.contains("already has")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // A2 — remove specialization
    @DeleteMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<String> removeSpecialization(@PathVariable Long doctorId,
                                                        @PathVariable Long specializationId) {
        String msg = doctorService.removeSpecialization(doctorId, specializationId);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.contains("does not have")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // A4 — move doctor to another office
    @PatchMapping("/{doctorId}/office")
    public ResponseEntity<String> changeOffice(@PathVariable Long doctorId,
                                                @RequestParam int officeNumber) {
        String msg = doctorService.changeOffice(doctorId, officeNumber);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.contains("taken")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // D1 — doctor's agenda for a day
    @GetMapping("/{doctorId}/agenda")
    public ResponseEntity<?> getDailyAgenda(@PathVariable Long doctorId,
                                             @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        Object result = doctorService.getDailyAgenda(doctorId, localDate);
        if (result instanceof String msg) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(result);
    }

    // Bonus — doctor workload
    @GetMapping("/workload")
    public ResponseEntity<List<Object[]>> getWorkload(@RequestParam String start,
                                                       @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(doctorService.getWorkload(startDate, endDate));
    }
}
