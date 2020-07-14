package com.example.demo1234;

import com.example.demo1234.model.Gender;
import com.example.demo1234.model.Person;
import com.example.demo1234.model.PersonRepository;
import com.example.demo1234.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonServiceTestE2e {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    PersonRepository repo;

    @Autowired
    PersonService service;


    private static String pesel = "69112665257";

    @Test
    void httpGet_returnsAllPeople(){
        //given
        int initial = repo.findAll().size();
        Integer sizeBeforeTest = restTemplate.getForObject("http://localhost:" + port + "/all", Person[].class).length;

        service.createPerson(new Person("TestName", "TestSurname", pesel, Gender.MALE));
        service.createPerson(new Person("TestName", "TestSurname", "73012628825", Gender.FEMALE));

        //when
        Person[] result = restTemplate.getForObject("http://localhost:" + port + "/all", Person[].class);

        //them
        assertThat(result).hasSize(sizeBeforeTest + 2);
    }

    @Test
    void httpPost_checkDateGenerator(){
        //given
        Person personToTest = service.createPerson(new Person("TESTNAME", "TESTSURNAME", pesel, Gender.FEMALE));

        //when
        Person result = restTemplate.getForObject(
                "http://localhost:" + port + "/" + repo.findByPesel(pesel).get().getId(), Person.class);

        //them
        assertThat(result.getDateOfBirth()).isEqualTo("1969-11-26");
    }

    @Test
    void httpPut_checkUpdate(){
        //given
        Person personToTest = service.createPerson(new Person("TESTNAME", "TESTSURNAME", pesel, Gender.FEMALE));
        Person personToUpdate = service.updatePerson(pesel,new Person("", "TEST", "", Gender.MALE));

        //when
        Person result = restTemplate.getForObject(
                "http://localhost:" + port + "/" + repo.findByPesel(pesel).get().getId(), Person.class);

        //them
        assertThat(result.getName()).isEqualTo("TESTNAME");
        assertThat(result.getSurname()).isEqualTo("TEST");
        assertThat(result.getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    void httpPost_formatOfSavedPerson(){
        //given

        Person personToTest = service.createPerson(new Person("TES TN A ME ", "T ES  TS U RNA ME", pesel, Gender.FEMALE));

        //when
        Person result = restTemplate.getForObject(
                "http://localhost:" + port + "/" + repo.findByPesel(pesel).get().getId(), Person.class);

        //them
        assertThat(result.getName()).isEqualTo("TESTNAME");
        assertThat(result.getSurname()).isEqualTo("TESTSURNAME");
    }

    @Test
    void httpDelete_isPersonReallyDeleted(){
        //given

        Person personToTest = service.createPerson(new Person("TES TN A ME ", "T ES  TS U RNA ME", pesel, Gender.FEMALE));
        int id = repo.findByPesel(pesel).get().getId();

        //when
        Person result = restTemplate.getForObject(
                "http://localhost:" + port + "/" + id, Person.class);

        service.deletePerson(repo.findByPesel(pesel).get().getId());

        Person resultAfterDelete = restTemplate.getForObject(
                "http://localhost:" + port + "/" + id, Person.class);

        //them
        assertThat(result.getName()).isEqualTo("TESTNAME");
        assertThat(result.getSurname()).isEqualTo("TESTSURNAME");
        assertThat(resultAfterDelete.getName()).isEqualTo(null);
        assertThat(resultAfterDelete.getSurname()).isEqualTo(null);
    }
}
