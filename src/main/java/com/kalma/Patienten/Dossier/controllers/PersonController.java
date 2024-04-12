package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.exceptions.InputNotValidException;
import com.kalma.Patienten.Dossier.exceptions.RecordNotFoundException;
import com.kalma.Patienten.Dossier.models.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@RequestMapping("/persons") //this is the root path
public class PersonController {
    private ArrayList<Person> persons = new ArrayList<>();

    @GetMapping
    public ResponseEntity<ArrayList<Person>> getAllPersons() {
        return new ResponseEntity<>(this.persons, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable int id) {
        if(id >= 0 && id < this.persons.size()){
            return new ResponseEntity<>(this.persons.get(id), HttpStatus.OK);
        } if(id < 0){
            throw new RecordNotFoundException("Deze id bestaat niet");
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> deletePersonById(@PathVariable int id) {
        if (id >= 0 && id < this.persons.size()) {
        return new ResponseEntity<>(this.persons.remove(id), HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(this.persons.remove(id), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ArrayList<Person>> searchPersonByName(@RequestParam String query) {

        if(query.matches(".*\\d.*")){
            throw new InputNotValidException("Dit is een getal");
        }
        if(query != null && !query.isEmpty())
        {
            ArrayList<Person> matchingPersons = new ArrayList<>();

            for (Person person : this.persons) {
                if (person.getName().toLowerCase().contains(query.toLowerCase())) {
                    matchingPersons.add(person);
                }
            }
            return new ResponseEntity<>(matchingPersons, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        this.persons.add(person);

        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person person) {
        if(id >= 0 && id < this.persons.size() ) {

            this.persons.set(id, person);
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
