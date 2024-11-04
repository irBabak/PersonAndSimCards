package mft.model.service;

import lombok.Getter;
import mft.model.entity.Person;
import mft.model.repository.PersonRepository;

import java.util.List;

public class PersonService {
    @Getter
    private static PersonService personService = new PersonService();

    private PersonService() {}

    public void save(Person person) throws Exception {
        try (PersonRepository repository = new PersonRepository()) {
            repository.save(person);
        }
    }

    public void edit(Person person) throws Exception {
        try (PersonRepository repository = new PersonRepository()) {
            repository.edit(person);
        }
    }

    public void remove(int id) throws Exception {
        try (PersonRepository repository = new PersonRepository()) {
            repository.remove(id);
        }
    }

    public List<Person> findAll() throws Exception {
        try (PersonRepository repository = new PersonRepository()) {
            return repository.findAll();
        }
    }

    public Person findByNationalCode(String nationalCode) throws Exception {
        try (PersonRepository repository = new PersonRepository()) {
            return repository.findByNationalCode(nationalCode);
        }
    }
}
