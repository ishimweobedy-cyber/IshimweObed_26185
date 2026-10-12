package com.auca.clinic.controller;

import com.auca.clinic.entity.Appointment;
import com.auca.clinic.entity.AppointmentStatus;
import com.auca.clinic.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Appointment> opt = appointmentService.getById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The appointment with that id does not exist");
    }

    // B1 — safe booking
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Appointment appointment) {
        String msg = appointmentService.save(appointment);
        if (msg.contains("in the past")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    // General update — does NOT change status
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                          @RequestBody Appointment appointment) {
        String msg = appointmentService.update(id, appointment);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // C4 — delete with prescription guard
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        String msg = appointmentService.delete(id);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.contains("prescriptions")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // B2 — status flow
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id,
                                                @RequestParam AppointmentStatus status) {
        String msg = appointmentService.updateStatus(id, status);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.contains("Cannot change")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // B3 — reschedule
    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<String> reschedule(@PathVariable Long id,
                                              @RequestParam String newDate) {
        LocalDate date = LocalDate.parse(newDate);
        String msg = appointmentService.reschedule(id, date);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.contains("cannot be rescheduled") || msg.contains("in the past")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        if (msg.contains("already booked")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }
}
