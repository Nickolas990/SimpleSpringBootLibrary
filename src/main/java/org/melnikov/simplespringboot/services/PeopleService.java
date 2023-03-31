package org.melnikov.simplespringboot.services;

import org.melnikov.simplespringboot.models.Person;
import org.melnikov.simplespringboot.repositories.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {

    private PersonRepository personRepository;

    public PeopleService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Person> findByName(String name) {
       return personRepository.findByFullName(name);
    }

    @Transactional(readOnly = true)
    public Optional<Person> findById(Integer id) {
        return personRepository.findById(id);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void update (Integer id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }

    @Transactional
    public void deleteById(Integer id) {
        personRepository.deleteById(id);
    }
}
