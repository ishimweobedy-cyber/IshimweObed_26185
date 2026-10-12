package com.auca.clinic.controller;

import com.auca.clinic.entity.Office;
import com.auca.clinic.service.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping
    public ResponseEntity<List<Office>> getAll() {
        return ResponseEntity.ok(officeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Office> opt = officeService.getById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("That office does not exist");
    }

    // A3 — free offices (fixed path before /{id})
    @GetMapping("/free")
    public ResponseEntity<List<Office>> getFreeOffices() {
        return ResponseEntity.ok(officeService.getFreeOffices());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Office office) {
        String msg = officeService.save(office);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        String msg = officeService.delete(id);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }
}
