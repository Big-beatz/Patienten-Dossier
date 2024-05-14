package com.kalma.Patienten.Dossier.repository;


import com.kalma.Patienten.Dossier.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
