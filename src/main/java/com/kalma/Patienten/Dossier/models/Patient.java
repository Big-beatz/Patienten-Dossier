package com.kalma.Patienten.Dossier.models;

import jakarta.persistence.*;

@Entity
@Table(name="patients")
public class Patient {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="first_name", length = 128)
    private String firstName;

    @Column(name="last_name", length = 128)
    private String lastName;

    @Column(name="full_name", length = 256)
    private String fullName;


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
}
