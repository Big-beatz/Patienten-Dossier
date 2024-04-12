package com.kalma.Patienten.Dossier.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
public class HelloController {

    private String userAddress;

    private ArrayList<String> Names = new ArrayList<>();

    @PostMapping("/addName")
    public void addName(@RequestParam String name) {
        this.Names.add(name);
    }

    @GetMapping("/names")
    public String getNames(@RequestParam(required = false) boolean reversed) {
        if (this.Names == null || this.Names.size() == 0) {
            return "Er zijn nog geen namen toegevoegd";
        }
        else {
            StringBuilder sb = new StringBuilder();
            for (String name : this.Names) {
                if (sb.toString().contains(name)) {
                    //do nothing
                } else {
                    sb.append(name).append("\n");
                }
            }
            if(reversed){
                return sb.reverse().toString();
            }
            else {
                return sb.toString();
            }
        }
    }





    @GetMapping("/hello")
    public String sayHello(@RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()) {
            return "Hello stranger";
        } else {
            return "Hello " + name;
        }
    }

    @PostMapping("/saveAddress")
    public void saveAddress(@RequestParam String address){
        this.userAddress = address;
    }

    @GetMapping("/getAddress")
    public String getAddress() {
        if (this.userAddress.isEmpty() || this.userAddress == null) {
            return "Geen geluk chef";
        } else{
            return userAddress;
        }
    }
}
