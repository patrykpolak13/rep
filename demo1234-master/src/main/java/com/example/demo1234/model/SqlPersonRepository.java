package com.example.demo1234.model;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SqlPersonRepository extends PersonRepository, JpaRepository<Person,Integer> {

}
