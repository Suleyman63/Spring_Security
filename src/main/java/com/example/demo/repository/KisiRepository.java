package com.example.demo.repository;

import com.example.demo.model.Kisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface KisiRepository extends JpaRepository<Kisi, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<Kisi> findByUsername(String username);
}
