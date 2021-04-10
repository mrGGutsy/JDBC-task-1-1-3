package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection = Util.getConnection();
    //открывается соединение

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String commandSQL = "CREATE TABLE IF NOT EXISTS users (\n" +
                "  id BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  name VARCHAR(45) NOT NULL,\n" +
                "  lastName VARCHAR(45) NOT NULL,\n" +
                "  age SMALLINT NOT NULL,\n" +
                "  PRIMARY KEY (id))\n";
        //команда для создания таблицы, если не создана
        try (PreparedStatement preparedStatement = connection.prepareStatement(commandSQL)){
                preparedStatement.executeUpdate(commandSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при создании таблицы!");
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS users");
            //удаление таблицы
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при удалении таблицы");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement prepareStatement = connection.prepareStatement("INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)")) {
            prepareStatement.setString(1, name);
            prepareStatement.setString(2, lastName);
            prepareStatement.setByte(3, age);
            prepareStatement.executeUpdate();
            //добавление пользователей с созданием нужных полей
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при добавлении пользователя!");
        }
    }

    public void removeUserById(long id) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM users WHERE id");
            //удаление пользователя по айди
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка! Не вышло удалить пользователя по ID!");
        }
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                usersList.add(user);
                //заполнение полей
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка! Данные не внесены!");
        }
        return usersList;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE users");
            //очистка таблицы
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при очистке таблицы!");
        }
    }
}
