package edu.java.scrapper.domain;

import edu.java.scrapper.controller.exception.DoubleRegistrationException;
import edu.java.scrapper.controller.exception.UserNotFoundException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcUserRepository {

    private static final String USER_ID = "id";
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = new UserMapper();
    }

    @Transactional
    public void addUser(User user) {
        String sql = "INSERT INTO users(id) VALUES (?)";
        try {
            jdbcTemplate.update(sql, user.getId());
        } catch (DataAccessException e) {
            throw new DoubleRegistrationException("User with ID %d already exists".formatted(user.getId()));
        }
    }

    @Transactional
    public void removeUser(Long userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        int result = jdbcTemplate.update(sql, userId);
        if (result == 0) {
            throw new UserNotFoundException(userId);
        }
    }

    public Optional<User> findUser(Long userId) {
        String sql = "SELECT user_id, user_state FROM users WHERE id = ?";
        Optional<User> user;
        try {
            user = Optional.ofNullable(jdbcTemplate.queryForObject(sql, userMapper, userId));
        } catch (IncorrectResultSizeDataAccessException e) {
            user = Optional.empty();
        }

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userMapper);
    }

    @Transactional(readOnly = true)
    public List<Long> findUsersTrackLink(Link link) {
        String sql = "SELECT user_id FROM user_links WHERE link_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong(USER_ID), link.getId());
    }

    private static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long userId = rs.getLong(USER_ID);
            return new User(userId);
        }
    }
}
