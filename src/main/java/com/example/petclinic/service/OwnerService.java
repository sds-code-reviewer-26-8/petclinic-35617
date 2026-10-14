package com.example.petclinic.service;

import com.example.petclinic.entity.Owner;
import com.example.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public List<Owner> findOwners() {
        return ownerRepository.findAll();
    }

    /**
     * lastName이 주어진 문자열로 시작하는 Owner 목록을 조회한다.
     * lastName이 null 이거나 공백이면 전체 목록을 반환한다.
     */
    public List<Owner> findOwnersByLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            return findOwners();
        }
        return ownerRepository.findByLastNameStartingWithIgnoreCase(lastName.trim());
    }

    public Owner findOwner(Long ownerId) {
        return verifyOwnerExists(ownerId);
    }

    private Owner verifyOwnerExists(Long ownerId) {
        return ownerRepository.findById(ownerId).orElseThrow(
                () -> new NoSuchElementException("Owner does not exist")
        );
    }
}
