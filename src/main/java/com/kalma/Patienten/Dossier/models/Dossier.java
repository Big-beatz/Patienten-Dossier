package com.kalma.Patienten.Dossier.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="dossiers")
public class Dossier {

    //properties
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="dossier_is_closed")
    private boolean dossierIsClosed;

    // todo find a way to add reports in the dossier
    // private List<Reports> reports = new ArrayList<>();

    @OneToOne(mappedBy = "dossier")
    @JoinColumn(name="patient_id", nullable = false)
    Patient patient;

    @OneToMany(mappedBy = "dossier")
    List<Report> reports;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="employee_id")
    Employee employee;

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

}
