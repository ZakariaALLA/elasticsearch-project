package com.zakariaalla.elasticsearchdemo.controller;

import com.zakariaalla.elasticsearchdemo.documents.Person;
import com.zakariaalla.elasticsearchdemo.service.PersonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }


    @PostMapping
    public void createPerson(@RequestBody final Person person) {
        this.personService.savePerson(person);
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable final String id) {
        return this.personService.findById(id);
    }
}
