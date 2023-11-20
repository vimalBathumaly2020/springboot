package com.example.test.Repository;

import com.example.test.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findById(int id);


    void deleteById(int id);
}
