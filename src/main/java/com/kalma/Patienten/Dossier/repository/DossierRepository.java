package com.kalma.Patienten.Dossier.repository;

import com.kalma.Patienten.Dossier.models.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DossierRepository extends JpaRepository<Dossier, Long> {
    Dossier findById(long id);

    Optional<Dossier> findByName(String name);


}
