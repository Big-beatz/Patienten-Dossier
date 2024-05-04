package com.kalma.Patienten.Dossier.repository;

import com.kalma.Patienten.Dossier.models.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DossierRepository extends JpaRepository<Dossier, Long> {

}
