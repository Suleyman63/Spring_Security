package com.example.demo.repository;

import com.example.demo.model.ERoller;
import com.example.demo.model.KisiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<KisiRole, Integer> {

   Optional<KisiRole> findByName(ERoller name);

}
