package com.example.demo1234;


import com.example.demo1234.model.Gender;
import com.example.demo1234.model.Person;
import com.example.demo1234.model.PersonRepository;
import com.example.demo1234.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);

    private PersonService service;
    private PersonRepository repository;

    public Warmup(final PersonService service, final PersonRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("Application warmup after context refreshed");
        final String surname = "ApplicationContextEvent";


        if (!repository.existsBySurnameContains(surname)){
            logger.info("No required group found! Adding it!");
            //random pesel from https://pesel.cstudios.pl/O-generatorze/Generator-On-Line
            service.createPerson(new Person("TestName", surname, "93010638781", Gender.FEMALE));
            service.createPerson(new Person("ADAM", "NOWAK", "50113082859", Gender.MALE));
            service.createPerson(new Person("JAN", "NOWAK", "79090872745", Gender.MALE));
            service.createPerson(new Person("JAN", "KOWALSKI", "66080149583", Gender.MALE));
            service.createPerson(new Person("TERESA", "KOWALSKI", "83020852312", Gender.FEMALE));
            service.createPerson(new Person("TERESA", "NOWAK", "03310759899", Gender.FEMALE));
        }


    }
}
