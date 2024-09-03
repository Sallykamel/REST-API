package com.example;

import io.micronaut.http.annotation.*;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class PersonServices {
    private final PersonRepository repository;

    public PersonServices(PersonRepository repository) {
        this.repository = repository;
    }
    public List<Person> getUsers() {
        return repository.findAll();
    }

    public Person getUserById(@PathVariable int id) {
        Optional<Person> person = repository.findById(id); //optional because it might be null
        if (person.isEmpty()){
            throw new IllegalArgumentException("not found id");
        }
        return person.get();
    }


    public Person addPerson(@Body Person person){
        return repository.save(new Person(person.getName(),person.getEmail()));
    }


    public boolean deletePerson(@PathVariable int id){
        Optional<Person> person = repository.findById(id); //optional because it might be null
        if (person.isEmpty()){
            return false;
        }
        repository.delete(person.get());
        System.out.println("successfully deleted");
        return true;
    }


    public Person update(@Body Person person, @PathVariable int id){
        Optional<Person> oldPerson = repository.findById(id); //optional because it might be null
        if (oldPerson.isEmpty()){
            throw new IllegalArgumentException("not found id");
        }
        oldPerson.get().setName(person.getName());
        oldPerson.get().setEmail(person.getEmail());
        return repository.update(oldPerson.get());
    }
}
