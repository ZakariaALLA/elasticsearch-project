package com.zakariaalla.elasticsearchdemo.service;

import com.zakariaalla.elasticsearchdemo.documents.Person;
import com.zakariaalla.elasticsearchdemo.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findById(String id) {
        return personRepository.findById(id).orElse(null);
    }

    public void savePerson(Person person) {
        personRepository.save(person);
    }

}
