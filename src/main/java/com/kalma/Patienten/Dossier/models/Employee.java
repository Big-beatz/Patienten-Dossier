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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="employees_generator")
    @SequenceGenerator(name ="employees_generator" , allocationSize = 1)
    private Long id;

    @Column(name="first_name", length = 128)
    private String firstName;

    @Column(name="last_name", length = 128)
    private String lastName;

    @Column(name="function", length = 128)
    private String function;

    @ManyToMany
    @JoinTable(
            name="employee_patients",
            joinColumns = @JoinColumn(name="employee_id"),
            inverseJoinColumns = @JoinColumn(name="patient_id")
    )
    private Set<Patient> patients = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    List<Report> reports;

    //login & security relevant
    @Column(name="username", length = 256)
    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

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

    public String getUsername() {
        return username = firstName + "." + lastName;
    }

    public void setUsername(String userName) {
        this.username = firstName + " " + lastName;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }


    public Set<Patient> getPatients() {
        return patients;
    }


    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
