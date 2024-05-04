package com.kalma.Patienten.Dossier.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="reports")
public class Report {

    //properties
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="date")
    private LocalDate date;

    @Column(name="body", length = 1000)
    private String body;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dossier_id")
    Dossier dossier;


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

}