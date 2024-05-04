package com.kalma.Patienten.Dossier.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="patients")
public class Patient {

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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Employee> employees = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Dossier dossier;

    //getters & setters

    public Long getId() {
        return id;
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

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }
}
