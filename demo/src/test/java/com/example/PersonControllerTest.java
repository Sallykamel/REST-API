package com.example;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
public class PersonControllerTest {
    @Mock
    private static PersonServices personServicesMock;
    @InjectMocks
    private static PersonController personController;
    @BeforeEach
    public void setup(){
        personServicesMock = mock(PersonServices.class);
        personController = new PersonController(personServicesMock);
    }

    @Test
    public void FindAllTest(){
        // Given
        List<Person> persons = Arrays.asList(new Person("sally", "sooo@example.com"));
        when(personServicesMock.getUsers()).thenReturn(persons);

        HttpResponse<List<Person>> response = personController.FindAll();

        // Then
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(persons, response.body());
    }
    @Test
    public void findByIdTest(){
        Person person = new Person("sally","soo@example.com");
        person.setId(1);

        when(personServicesMock.getUserById(anyInt())).thenReturn(person);
        HttpResponse<Person> response = personController.findById(1);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(response.body(),person);
    }

    @Test
    public void addPersonTest(){
        Person person = new Person("John Doe", "john.doe@example.com");
        Person addedPerson = new Person("John Doe", "john.doe@example.com");
        addedPerson.setId(1); // Assuming the repository sets an ID

        when(personServicesMock.addPerson(any(Person.class))).thenReturn(addedPerson);
        HttpResponse<Person> response = personController.addPerson(person);

        assertEquals(HttpStatus.CREATED,response.status());
        assertEquals(response.body(),addedPerson);
    }
    @Test
    public void deletePersonTrueTest(){
        Person person = new Person("John Doe", "john.doe@example.com");
        person.setId(1); // Assuming the repository sets an ID
        when(personServicesMock.deletePerson(anyInt())).thenReturn(true);
        HttpResponse<Void> response = personController.deletePerson(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }
    @Test
    public void deletePersonFalseTest(){
        when(personServicesMock.deletePerson(anyInt())).thenReturn(false);
        HttpResponse<Void> response = personController.deletePerson(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    public void updatePersonTest() {
        // Given
        Person person = new Person("John Doe", "john.doe@example.com");
        person.setId(1);
        Person updatedPerson = new Person("soo", "soo@example.com");
        updatedPerson.setId(1);
        when(personServicesMock.update(any(Person.class), anyInt())).thenReturn(updatedPerson);

        HttpResponse<Person> response = personController.update(person, 1);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(updatedPerson, response.body());
    }

}
