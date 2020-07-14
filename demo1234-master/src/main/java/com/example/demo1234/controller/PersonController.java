package com.example.demo1234.controller;

import com.example.demo1234.model.Person;
import com.example.demo1234.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
public class PersonController {
    Logger logger = LoggerFactory.getLogger(PersonController.class);
    private PersonService service;

    public PersonController(final PersonService service) {
        this.service = service;
    }

    @GetMapping(path = "/all", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Person>> readAll(Pageable page) {
        logger.info("Exposing all People");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/all")
    ResponseEntity<List<Person>> readAllPageable(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(service.findAllPageable(page).getContent());
    }

    @GetMapping("/{id}")
    ResponseEntity<Person> readById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }


   @GetMapping("/search")
    ResponseEntity<List<Person>> readAndSort(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String surname,
            @RequestParam(required = false, defaultValue = "") String pesel,
            @RequestParam(required = false, defaultValue = "") String gender,
            @RequestParam(required = false, defaultValue = "1900-01-01") String from,
            @RequestParam(required = false, defaultValue = "2222-12-31") String to
    ) {
        return ResponseEntity.ok(service.findALl(name,surname,pesel,gender, from,to));
    }

    @PostMapping("/add")
    ResponseEntity<Person> createPerson(@RequestBody @Valid Person toCreate) {
        Person result = service.createPerson(toCreate);
        if(result != null){
            return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
        }
        else return ResponseEntity.badRequest().build();
    }


    @PutMapping("/edit/{pesel}")
    public ResponseEntity<Person> updatePerson(@PathVariable @NotNull String pesel, @RequestBody Person toCreate) {
        return ResponseEntity.ok(service.updatePerson(pesel, toCreate));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Person> deletePerson( @PathVariable @NotNull int id){
        if(service.deletePerson(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().build();
    }
}
