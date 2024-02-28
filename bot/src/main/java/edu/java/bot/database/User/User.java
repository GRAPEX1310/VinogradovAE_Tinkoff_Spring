package edu.java.bot.database.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private long id;
    private UserState state = UserState.BASE;

    public User(long id) {
        this.id = id;
    }
}
