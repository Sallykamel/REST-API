package com.example;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller("/api")
public class PersonController {
    private final PersonServices service;

    public PersonController(PersonServices service) {
        this.service = service;
    }

    @Get("/getall")
    public HttpResponse<List<Person>> FindAll() {
        return HttpResponse.ok(service.getUsers());
    }

    @Get("/{id}")
    public HttpResponse<Person> findById(@PathVariable int id) {
        return HttpResponse.ok(service.getUserById(id));
    }

    @Post("/")
    public HttpResponse<Person> addPerson(@Body Person person){
        return HttpResponse.created(service.addPerson(person));
    }

    @Delete("/{id}")
    public HttpResponse<Void> deletePerson(@PathVariable int id){
        boolean result = service.deletePerson(id);
        if (result) {
            return HttpResponse.noContent();
        } else {
            return HttpResponse.status(HttpStatus.NOT_FOUND);
        }
    }

    @Put("/{id}")
    public HttpResponse<Person> update(@Body Person person, @PathVariable int id){
        return HttpResponse.ok(service.update(person,id));
    }

}
