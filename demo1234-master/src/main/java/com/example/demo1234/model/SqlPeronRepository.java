package com.example.demo1234.model;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SqlPeronRepository extends PersonRepository, JpaRepository<Person,Integer> {


}
