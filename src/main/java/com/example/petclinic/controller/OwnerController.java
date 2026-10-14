package com.example.petclinic.controller;

import com.example.petclinic.controller.response.OwnerDto;
import com.example.petclinic.entity.Owner;
import com.example.petclinic.service.OwnerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * GET /api/v1/owners                → 모든 동물 주인 리스트
     * GET /api/v1/owners?lastname=kim   → lastName이 kim으로 시작하는 동물 주인 리스트
     */
    @GetMapping
    public List<OwnerDto> list(@RequestParam(name = "lastname", required = false) String lastName) {
        return ownerService.findOwnersByLastName(lastName).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OwnerDto detail(@PathVariable Long id) {
        return toDto(ownerService.findOwner(id));
    }

    private OwnerDto toDto(Owner o) {
        return new OwnerDto(
                o.getId(),
                o.getFirstName(),
                o.getLastName(),
                o.getAddress(),
                o.getCity(),
                o.getTelephone());
    }
}
