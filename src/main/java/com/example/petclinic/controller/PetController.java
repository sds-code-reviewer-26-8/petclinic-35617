package com.example.petclinic.controller;

import com.example.petclinic.controller.response.OwnerDto;
import com.example.petclinic.controller.response.PetDto;
import com.example.petclinic.controller.response.PetTypeDto;
import com.example.petclinic.entity.Pet;
import com.example.petclinic.service.PetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * GET /api/v1/pets                     → 모든 동물 리스트
     * GET /api/v1/pets?year=3&type=cat     → 현재 날짜 기준 생후 3년 미만인 cat 리스트
     * year, type 둘 중 하나만 전달하면 IllegalArgumentException (400)
     */
    @GetMapping
    public List<PetDto> list(@RequestParam(name = "year", required = false) Integer year,
                             @RequestParam(name = "type", required = false) String type) {
        return petService.findPetsByYearAndType(year, type).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PetDto detail(@PathVariable Long id) {
        return toDto(petService.findPet(id));
    }

    private PetDto toDto(Pet p) {
        PetTypeDto petType = new PetTypeDto(
                p.getType().getId(),
                p.getType().getName());
        OwnerDto owner = new OwnerDto(
                p.getOwner().getId(),
                p.getOwner().getFirstName(),
                p.getOwner().getLastName(),
                p.getOwner().getAddress(),
                p.getOwner().getCity(),
                p.getOwner().getTelephone());
        return new PetDto(
                p.getId(),
                p.getName(),
                p.getBirthDate(),
                petType,
                owner);
    }
}
