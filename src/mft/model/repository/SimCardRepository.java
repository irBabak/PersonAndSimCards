package mft.model.repository;

import mft.model.entity.Person;
import mft.model.entity.SimCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimCardRepository implements AutoCloseable{
    Connection connection;
    PreparedStatement preparedStatement;

    public void save(SimCard simCard) throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT simcard_seq.nextval AS next_id FROM dual"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        simCard.setId(resultSet.getInt("next_id"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO simcards" +
                        " (simcardId, simcard_number, ownerId)" +
                        " VALUES (?, ?, ?)"
        );
        preparedStatement.setInt(1, simCard.getId());
        preparedStatement.setString(2, simCard.getSimCardNumber());
        preparedStatement.setInt(3, simCard.getOwner().getId());
        preparedStatement.execute();
    }

    public void edit(SimCard simCard) throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE simcards" +
                        " SET simcard_number = ?" +
                        " WHERE ownerId = ?"
        );
        preparedStatement.setString(1, simCard.getSimCardNumber());
        preparedStatement.setInt(2, simCard.getOwner().getId());
        preparedStatement.execute();
    }

    public void remove(int id) throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        preparedStatement = connection.prepareStatement(
                "DELETE FROM simcards" +
                        " WHERE simcardId = (SELECT simcard_id FROM PERSON_SIMCARD_VIEW WHERE person_id = ?)"
        );
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    public List<SimCard> findAll() throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PERSON_SIMCARD_VIEW"
        );
        ResultSet resultSet = preparedStatement.executeQuery();

        List<SimCard> simCardList = new ArrayList<>();
        while (resultSet.next()) {
            Person person =
                    Person
                            .builder()
                            .id(resultSet.getInt("person_id"))
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .nationalCode(resultSet.getString("national_code"))
                            .build();

            SimCard simCard =
                    SimCard
                            .builder()
                            .id(resultSet.getInt("simcard_id"))
                            .simCardNumber(resultSet.getString("phone_number"))
                            .owner(person)
                            .build();

            simCardList.add(simCard);
        }

        return simCardList;
    }

    public List<SimCard> findByPersonNationalCode(String nationalCode) throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PERSON_SIMCARD_VIEW WHERE national_code = ?"
        );
        preparedStatement.setString(1, nationalCode);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<SimCard> simCardList = new ArrayList<>();
        while (resultSet.next()) {
            Person person =
                    Person
                            .builder()
                            .id(resultSet.getInt("person_id"))
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .nationalCode(resultSet.getString("national_code"))
                            .build();

            SimCard simCard =
                    SimCard
                            .builder()
                            .id(resultSet.getInt("simcard_id"))
                            .simCardNumber(resultSet.getString("phone_number"))
                            .owner(person)
                            .build();

            simCardList.add(simCard);
        }

        return simCardList;
    }

    public int countPersonSimCardsByNationalCode(String nationalCode) throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT COUNT(*) AS simcard_count FROM PERSON_SIMCARD_VIEW WHERE national_code = ?"
        );
        preparedStatement.setString(1, nationalCode);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("simcard_count");

    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();

    }
}
