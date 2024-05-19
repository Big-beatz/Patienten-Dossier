package com.kalma.Patienten.Dossier.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    private String rolename;

    @ManyToMany(mappedBy = "roles")
    private Set<Employee> employees;

    //getters & setters

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}