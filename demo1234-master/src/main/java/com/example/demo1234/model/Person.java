package com.example.demo1234.model;

import org.hibernate.validator.constraints.pl.PESEL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "people")
public
class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name must not be null")
    private String name;

    @NotBlank(message = "Surname must not be blank")
    private String surname;

    @PESEL
    @NotBlank(message = "PESEL must not be blank")
    private String pesel;

    private Gender gender;

    private LocalDate dateOfBirth;

    public Person() {
    }

    public Person(@NotBlank(message = "Name must not be null") final String name, @NotBlank(message = "Surname must not be blank") @NotNull(message = "Surname must not be blank") final String surname, @PESEL @NotBlank(message = "PESEL must not be blank") final String pesel, final Gender gender) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(final String pesel) {
        this.pesel = pesel;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void toUpdate(Person source){
        if(!source.name.isEmpty()){
            name = source.name;
        }

        if(!source.surname.isEmpty()){
            surname = source.surname;
        }

        if(source.gender != null){
            gender = source.gender;
        }

        if(source.dateOfBirth != null){
            dateOfBirth = source.dateOfBirth;
        }

    }
}
