package com.example.petclinic.repository;

import com.example.petclinic.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    /** birthDate가 주어진 시각 이후(초과)이고, type.name이 주어진 이름과 같은 Pet 목록 (대소문자 무시) */
    List<Pet> findByBirthDateAfterAndTypeNameIgnoreCase(LocalDateTime birthDate, String typeName);
}
