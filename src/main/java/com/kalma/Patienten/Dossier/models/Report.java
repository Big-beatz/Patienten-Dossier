package com.kalma.Patienten.Dossier.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="reports")
public class Report {

    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reports_generator")
    @SequenceGenerator(name = "reports_generator", allocationSize = 1)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "body", length = 1000)
    private String body;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dossier_id")
    Dossier dossier;

    @ManyToOne()
    @JoinColumn(name = "employees_id")
    Employee employee;

    private String reportFile;

    //getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate reportDate) {
        this.date = reportDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String reportBody) {
        this.body = reportBody;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getReportFile() {
        return reportFile;
    }

    public void setReportFile(String reportFile) {
        this.reportFile = reportFile;
    }
}