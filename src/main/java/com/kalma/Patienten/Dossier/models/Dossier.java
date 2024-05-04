package com.kalma.Patienten.Dossier.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="dossiers")
public class Dossier {

    //properties
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="dossier_is_closed")
    private boolean dossierIsClosed;

    @Column(name="name")
    private String name;

    @OneToOne(mappedBy = "dossier")
    @JoinColumn(name="patient_id", nullable = false)
    Patient patient;

    @OneToMany(mappedBy = "dossier")
    List<Report> reports;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getDossierIsClosed() {
        return dossierIsClosed;
    }

    public void setDossierIsClosed(boolean dossierIsClosed) {
        this.dossierIsClosed = dossierIsClosed;
    }

    public String getName() {
        return name;
    }

    public void setName(String dossierName) {
        this.name = dossierName;
    }


    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

}
