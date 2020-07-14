package com.example.demo1234.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    //existBy
    List<Person> findAllByDateOfBirthAfterAndDateOfBirthBefore(LocalDate datefrom, LocalDate dateTo);
    boolean existsById(Integer id);
    boolean existsByNameContains(String name);
    boolean existsBySurnameContains(String surname);
    boolean existsByPesel(String pesel);
    boolean existsByGender(Gender gender);

    //ordered lists
    List<Person> findAllBySurname(String surname);
    List<Person> findAllByName(String name);
    List<Person> findAllByPesel(String pesel);
    List<Person> findAllByGender(Gender gender);

    //unordered list
    List<Person> findAll();
    Page<Person> findAll( Pageable page);

    //get optional of Person
    Optional<Person> findById(int id);
    Optional<Person> findByPesel(String pesel);

    //operations on repository
    Person save(Person person);
    void deleteById(int id);

}
