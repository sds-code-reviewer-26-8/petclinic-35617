package com.example.petclinic.repository;

import com.example.petclinic.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    /** lastName이 주어진 문자열로 시작하는 Owner 목록 (대소문자 무시) */
    List<Owner> findByLastNameStartingWithIgnoreCase(String lastName);
}
