package com.example.petclinic.service;

import com.example.petclinic.entity.Pet;
import com.example.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> findPets() {
        return petRepository.findAll();
    }

    /**
     * 현재 날짜 기준 생후 year년 미만이면서 type 이름이 일치하는 Pet 목록을 조회한다.
     * <ul>
     *   <li>year, type 둘 다 없으면 전체 목록을 반환한다.</li>
     *   <li>year, type 둘 중 하나만 전달되면 IllegalArgumentException을 던진다.</li>
     *   <li>year가 1 미만이면 IllegalArgumentException을 던진다.</li>
     * </ul>
     */
    public List<Pet> findPetsByYearAndType(Integer year, String type) {
        boolean hasYear = year != null;
        boolean hasType = type != null && !type.isBlank();

        if (!hasYear && !hasType) {
            return findPets();
        }
        if (hasYear != hasType) {
            throw new IllegalArgumentException("year and type must be provided together");
        }
        if (year < 1) {
            throw new IllegalArgumentException("year must be greater than 0");
        }

        // 생후 n년 미만 == 오늘로부터 n년 전 날짜 이후에 태어남
        LocalDateTime cutoff = LocalDate.now().minusYears(year).atStartOfDay();
        return petRepository.findByBirthDateAfterAndTypeNameIgnoreCase(cutoff, type.trim());
    }

    public Pet findPet(Long petId) {
        return verifyPetExists(petId);
    }

    private Pet verifyPetExists(Long petId) {
        return petRepository.findById(petId).orElseThrow(
                () -> new NoSuchElementException("Pet does not exist")
        );
    }
}
