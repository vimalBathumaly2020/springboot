package com.example.test.Controller;


import com.example.test.Entity.Appointment;
import com.example.test.Repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        try {
            // Save the new appointment to the database
            Appointment createdAppointment = appointmentRepository.save(appointment);
            return ResponseEntity.ok(createdAppointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable int id) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isPresent()) {
            return ResponseEntity.ok(appointmentOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable int id, @RequestBody Appointment updatedAppointment) {
        try {
            // Check if the appointment with the given ID exists
            Optional<Appointment> existingAppointmentOptional = appointmentRepository.findById(id);
            if (existingAppointmentOptional.isPresent()) {
                Appointment existingAppointment = existingAppointmentOptional.get();
                // Update the existing appointment with the new data
                existingAppointment.setName(updatedAppointment.getName());
                existingAppointment.setDate(updatedAppointment.getDate());
                existingAppointment.setTime(updatedAppointment.getTime());
                existingAppointment.setSalesPerson(updatedAppointment.getSalesPerson());
                existingAppointment.setNotes(updatedAppointment.getNotes());
                existingAppointment.setAmount(updatedAppointment.getAmount());
                existingAppointment.setPhoneNumber(updatedAppointment.getPhoneNumber());
                // Save the updated appointment to the database
                Appointment savedAppointment = appointmentRepository.save(existingAppointment);
                return ResponseEntity.ok(savedAppointment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable int id) {
        try {
            // Check if the appointment with the given ID exists
            Optional<Appointment> existingAppointmentOptional = appointmentRepository.findById(id);
            if (existingAppointmentOptional.isPresent()) {
                // Delete the appointment from the database
                appointmentRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}