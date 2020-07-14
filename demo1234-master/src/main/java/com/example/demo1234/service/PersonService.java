package com.example.demo1234.service;

import com.example.demo1234.model.Gender;
import com.example.demo1234.model.Person;
import com.example.demo1234.model.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public
class PersonService {
    Logger logger = LoggerFactory.getLogger(PersonService.class);
    private PersonRepository repository;

    public PersonService(final PersonRepository repository) {
        this.repository = repository;
    }
    public Page<Person> findAllPageable(Pageable page){
        return repository.findAll(page);
    }

    public List<Person> findAll(){
        return repository.findAll();
    }

    public Person findById(int id){
        return repository.findById(id).orElse( new Person() );
    }


    public List<Person> findALl(String name, String surname, String pesel, String gender, String datefrom, String dateTo){

        if((!pesel.isBlank()) && repository.existsByPesel(pesel)){
            logger.info("exposing person with incomes Pesel");
            return repository.findAllByPesel(pesel);
        }

        name = name.toUpperCase().replaceAll("\\s+","");
        surname = surname.toUpperCase().replaceAll("\\s+","");
        gender = gender.toUpperCase().replaceAll("\\s+","");

        List<Person> result = repository.findAllByDateOfBirthAfterAndDateOfBirthBefore(
                LocalDate.parse(datefrom),
                LocalDate.parse(dateTo)
        );
        if((!name.isBlank()) && repository.existsByNameContains(name)){
            logger.info("exposing all people with name: " + name);
            String tempVariable = name;

            result = result.stream().filter(
                    x -> x.getName().contains(tempVariable)
            ).collect(Collectors.toList());
        }

        if((!surname.isBlank()) && repository.existsBySurnameContains(surname)){
            logger.info("exposing all people with surname: " + surname);
            String tempVariable = surname;

            result = result.stream().filter(
                    x -> x.getSurname().contains(tempVariable)
            ).collect(Collectors.toList());
        }
        if((!gender.isBlank()) && repository.existsByGender(Gender.valueOf(gender))){
            logger.info("exposing all people with gender: " + gender);
            String tempVariable = gender;

            result = result.stream().filter(
                    x -> x.getGender() == Gender.valueOf(tempVariable)
            ).collect(Collectors.toList());
        }
        return result;
    }

    //

    public Person createPerson(Person source){
        if(!repository.existsByPesel(source.getPesel())) {
            Person result;
            source.setSurname(source.getSurname()
                    .toUpperCase().replaceAll("\\s+",""));

            source.setName(source.getName()
                    .toUpperCase().replaceAll("\\s+",""));

            //pesel to date of birth
            source.setDateOfBirth(peselToDateOfBirth(source.getPesel()));

            return repository.save(source);
        }
        else {
            throw new IllegalArgumentException("Person with this Pesel is already exists");
        }
    }

    public Person updatePerson(String pesel, Person toUpdate) {
        repository.findByPesel(pesel).ifPresent(x -> {
            x.setPesel(pesel);
            x.toUpdate(toUpdate);
            repository.save(x);
        });
        return repository.findByPesel(pesel).get();
    }

    public boolean deletePerson(int id){
        try{
            if(repository.existsById(id)) {
                repository.deleteById(id);
                return true;
            }
        }
        catch (IllegalArgumentException e){
            logger.warn("Wrong request // " + e);
            throw new IllegalArgumentException("Person which You're looking for to DELETE is not exists");
        }
        return false;
    }

    LocalDate peselToDateOfBirth(String pesel){
        LocalDate result;

        Integer yearNow = LocalDate.now().getYear() - 2000;
        Integer year = Integer.parseInt(pesel.substring(0,2));

        if(year >= yearNow){
            year += 1900;
            Integer month = Integer.parseInt(pesel.substring(2,4));
            Integer day = Integer.parseInt(pesel.substring(4,6));

            result = LocalDate.of(year,month,day);
        }
        else {
            year += 2000;
            Integer monthAfterMillenium = (Integer.parseInt(pesel.substring(2,4))) - 20;
            Integer dayAfterMillenium = Integer.parseInt(pesel.substring(4,6));

            result = LocalDate.of(year,monthAfterMillenium,dayAfterMillenium);
        }
        return result;
    }
}
