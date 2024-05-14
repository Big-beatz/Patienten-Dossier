package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.models.Role;
import com.kalma.Patienten.Dossier.dto.RoleDto;
import com.kalma.Patienten.Dossier.repository.RoleRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoleController {

    private final RoleRepository repos;

    public RoleController(RoleRepository repos) {
        this.repos = repos;
    }
    @GetMapping("/roles")
    public List<RoleDto> getRoles() {
        List<RoleDto> roleDtos = new ArrayList<>();
        for (Role r : repos.findAll()) {
            RoleDto rdto = new RoleDto();
            rdto.rolename = r.getRolename();
            roleDtos.add(rdto);
        }
        return roleDtos;
    }
}