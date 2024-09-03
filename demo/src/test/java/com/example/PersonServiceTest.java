package com.example;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MicronautTest
public class PersonServiceTest {
    @Mock
    private PersonRepository repositoryMock;
    @InjectMocks
    private PersonServices personServices;

    @BeforeEach
    public void setup(){
        repositoryMock= mock(PersonRepository.class);
        personServices = new PersonServices(repositoryMock);
//        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUsersTest() throws SQLException{
        List<Person> users = new ArrayList<>();
        users.add(new Person("sally","s@gmail.com"));
        users.add(new Person("bozo","b@gmail.com"));
        when(repositoryMock.findAll()).thenReturn(users);
        assertEquals(repositoryMock.findAll(),users);
        assertEquals(repositoryMock.findAll().size(),2);
    }
    @Test
    public void getUserByIdTest(){
        List<Person> users = new ArrayList<>();
        users.add(new Person("sally","s@gmail.com"));
        users.add(new Person("bozo","b@gmail.com"));
        when(repositoryMock.findById(1)).thenReturn(Optional.ofNullable(users.get(1)));
        assertEquals(repositoryMock.findById(1),Optional.ofNullable(users.get(1)));
    }

    @Test
    public void addPersonSuccessTest() {
        // Given
        Person person = new Person("John Doe", "john.doe@example.com");
        Person savedPerson = new Person("John Doe", "john.doe@example.com");
        savedPerson.setId(1); // Assuming the repository sets an ID

        // Mock the repository to return the savedPerson
        when(repositoryMock.save(any(Person.class))).thenReturn(savedPerson);

        // When
        Person result = personServices.addPerson(person);

        // Then
        assertEquals(savedPerson, result);
    }

    @Test
    public void deletePersonSuccessTest() {
        // Given
        Person person = new Person("John Doe", "john.doe@example.com");
        person.setId(1);
        when(repositoryMock.findById(anyInt())).thenReturn(Optional.of(person));

        // When
        boolean result = personServices.deletePerson(1);

        // Then
        assertTrue(result);
        verify(repositoryMock, times(1)).delete(person);
    }
    @Test
    public void deletePersonNotFoundTest() {
        // Given
        when(repositoryMock.findById(anyInt())).thenReturn(Optional.empty());

        // When
        boolean result = personServices.deletePerson(1);

        // Then
        assertFalse(result);
        verify(repositoryMock, never()).delete(any(Person.class));
    }

    @Test
    public void updateSuccesTest(){
        Person person = new Person("John Doe", "john.doe@example.com");
        person.setId(1);

        Person newperson = new Person("soo", "soo@example.com");
        person.setId(1);

        when(repositoryMock.findById(anyInt())).thenReturn(Optional.of(person));
        when(repositoryMock.update(any(Person.class))).thenReturn(newperson);
        Person updated = personServices.update(newperson,1);
        assertEquals(updated,newperson);
        verify(repositoryMock).findById(1);
        verify(repositoryMock).update(person);
    }
    @Test
    public void updateNotFoundTest(){
        Person updatedPerson = new Person("Jane Doe", "jane.doe@example.com");

        when(repositoryMock.findById(anyInt())).thenReturn(Optional.empty());

        // When and Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            personServices.update(updatedPerson, 1);
        });

        assertEquals("not found id", thrown.getMessage());
        verify(repositoryMock).findById(1);
        verify(repositoryMock, never()).update(any(Person.class));
    }






}
