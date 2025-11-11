package com.example.Project_Tracker.repository;

import com.example.Project_Tracker.entity.Ibu;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IbuRepository extends JpaRepository<Ibu, String> {
    Optional<Ibu> findByName(String name);
}
