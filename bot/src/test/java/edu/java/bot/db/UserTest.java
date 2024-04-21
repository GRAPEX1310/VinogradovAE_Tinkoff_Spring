package edu.java.bot.db;

import edu.java.bot.database.User.User;
import edu.java.bot.database.User.UserState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest {

    @Test
    @DisplayName("User class work test")
    public void testUserClass() {
        User testUser = new User(13L);
        assertThat(testUser.getId()).isEqualTo(13L);
        assertThat(testUser.getState()).isEqualTo(UserState.BASE);

        testUser.setId(1000L);
        testUser.setState(UserState.WAITING_TRACKING_LINK);
        assertThat(testUser.getId()).isEqualTo(1000L);
        assertThat(testUser.getState()).isEqualTo(UserState.WAITING_TRACKING_LINK);
    }

}
