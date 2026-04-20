package com.camilo.authapp.data;

import com.camilo.authapp.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MySqlUserRepository implements UserRepository {
    private final MySqlConnectionFactory connectionFactory;

    public MySqlUserRepository(MySqlConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT username, full_name, password_hash FROM users WHERE username = ?";

        try (Connection connection = connectionFactory.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(new User(
                    resultSet.getString("username"),
                    resultSet.getString("full_name"),
                    resultSet.getString("password_hash")
            ));
        } catch (SQLException exception) {
            throw new IllegalStateException("Error buscando usuario", exception);
        }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, full_name, password_hash) VALUES (?, ?, ?)";

        try (Connection connection = connectionFactory.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.username());
            statement.setString(2, user.fullName());
            statement.setString(3, user.passwordHash());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Error guardando usuario", exception);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
}