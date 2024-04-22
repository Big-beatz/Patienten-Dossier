package com.kalma.Patienten.Dossier.models;


import java.time.LocalDate;

public class Person {
    private String name;
    private LocalDate dob;
    private char gender;
    private String hobby;

    public String getHobby(){
        return this.hobby;
    }

    public void setHobby(String hobby){
        this.hobby = hobby;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public char getGender() {
        return this.gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }
}