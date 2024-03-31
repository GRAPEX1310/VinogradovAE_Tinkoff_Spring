package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.controller.exception.DoubleRegistrationException;
import edu.java.scrapper.controller.exception.UserNotFoundException;
import edu.java.scrapper.domain.UserRepository;
import edu.java.scrapper.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private static final String USER_ID = "id";
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = new UserMapper();
    }

    @Override
    public void addUser(Long userId) {
        String sql = "INSERT INTO users(id) VALUES (?)";
        try {
            jdbcTemplate.update(sql, userId);
        } catch (DataAccessException e) {
            throw new DoubleRegistrationException("User with ID %d already exists".formatted(userId));
        }
    }

    @Override
    public void removeUser(Long userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        int result = jdbcTemplate.update(sql, userId);
        if (result == 0) {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
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
    @Override
    public List<Long> findUsersTrackLink(Long linkId) {
        String sql = "SELECT user_id FROM user_links WHERE link_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong(USER_ID), linkId);
    }

    private static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long userId = rs.getLong(USER_ID);
            return new User(userId);
        }
    }
}
