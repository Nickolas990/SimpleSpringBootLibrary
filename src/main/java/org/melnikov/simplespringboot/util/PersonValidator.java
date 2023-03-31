package org.melnikov.simplespringboot.util;


import org.melnikov.simplespringboot.models.Person;
import org.melnikov.simplespringboot.services.PeopleService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.function.Predicate;

/**
 * @author Nikolay Melnikov
 */
@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        Predicate<Class> isPerson = clazz::equals;
        return isPerson.test(Person.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleService.findByName(person.getFullName()).isPresent()) {
            errors.rejectValue("fullName", "error.person.exists", "Person with this name already exists");
        }

    }
}
