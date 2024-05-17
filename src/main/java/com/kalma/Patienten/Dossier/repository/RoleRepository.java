package com.kalma.Patienten.Dossier.repository;

import com.kalma.Patienten.Dossier.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, String> {
    public boolean existsByRolename(String rolename);
}
