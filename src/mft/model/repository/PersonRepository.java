package mft.model.repository;

import mft.model.entity.Person;
import mft.model.entity.SimCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository implements AutoCloseable{
    private Connection connection;
    private PreparedStatement preparedStatement;

    public void save(Person person) throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();

        preparedStatement = connection.prepareStatement("SELECT person_seq.nextval AS next_id FROM dual");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        person.setId(resultSet.getInt("next_id"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO persons" +
                        " (personId, first_name, last_name, national_code)" +
                        " VALUES (?, ?, ?, ?)"
        );
        preparedStatement.setInt(1, person.getId());
        preparedStatement.setString(2, person.getFirstName());
        preparedStatement.setString(3, person.getLastName());
        preparedStatement.setString(4, person.getNationalCode());
        preparedStatement.execute();
    }

    public void edit(Person person) throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE persons" +
                        " SET first_name = ?, last_name = ?, national_code = ?" +
                        " WHERE personId = ?"
        );
        preparedStatement.setString(1, person.getFirstName());
        preparedStatement.setString(2, person.getLastName());
        preparedStatement.setString(3, person.getNationalCode());
        preparedStatement.setInt(4, person.getId());
        preparedStatement.execute();
    }

    public void remove(int id) throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        preparedStatement = connection.prepareStatement(
                "DELETE FROM persons WHERE personId = ?"
        );
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    public List<Person> findAll() throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM persons"
        );
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Person> personList = new ArrayList<>();
        while (resultSet.next()) {
            Person person =
                    Person
                            .builder()
                            .id(resultSet.getInt("personId"))
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .nationalCode(resultSet.getString("national_code"))
                            .build();

            personList.add(person);
        }

        return personList;
    }

    public Person findByNationalCode(String nationalCode) throws Exception {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PERSON_SIMCARD_VIEW WHERE national_code = ?"
        );
        preparedStatement.setString(1, nationalCode);
        ResultSet resultSet = preparedStatement.executeQuery();

        Person person = null;
        while (resultSet.next()) {
            if (person == null) {
                person =
                        Person
                                .builder()
                                .id(resultSet.getInt("person_id"))
                                .firstName(resultSet.getString("first_name"))
                                .lastName(resultSet.getString("last_name"))
                                .nationalCode(resultSet.getString("national_code"))
                                .build();
            }

            SimCard simCard =
                    SimCard
                            .builder()
                            .id(resultSet.getInt("simCard_id"))
                            .simCardNumber(resultSet.getString("phone_number"))
                            .build();

            person.addSimCard(simCard);
        }

        return person;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}