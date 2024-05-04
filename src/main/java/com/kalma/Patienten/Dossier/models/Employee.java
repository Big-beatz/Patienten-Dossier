package com.kalma.Patienten.Dossier.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="employees")
public class Employee {

    //properties

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="first_name", length = 128)
    private String firstName;

    @Column(name="last_name", length = 128)
    private String lastName;

    @Column(name="full_name", length = 256)
    private String fullName;

    @Column(name="role", length = 128)
    private String role;

    @ManyToMany(mappedBy = "employees")
    private Set<Patient> patients = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    List<Dossier> dossiers;
    //getters & setters



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName = firstName + " " + lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = firstName + " " + lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Set<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public List<Dossier> getDossiers() {
        return dossiers;
    }

    public void setDossiers(List<Dossier> dossiers) {
        this.dossiers = dossiers;
    }
}
